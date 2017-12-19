package fr.umlv.conc;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LockFreeStringList {
	private final static VarHandle NEXT_REF;
	
	static {
		Lookup lookup = MethodHandles.lookup();
		
		try {
			NEXT_REF = lookup.findVarHandle(Entry.class, "next", Entry.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}
	
	static final class Entry {
		final String element;
		volatile Entry next;

		Entry(String element) {
			this.element = element;
		}
	}
	
	private final Entry head;

	public LockFreeStringList() {
		head = new Entry(null);
	}
	
	private Entry getLast() {
		
	}

	public void addLast(String element) {
		Entry entry = new Entry(element);
	    Entry last = head;

	    for (;;) {
	      Entry next = last.next;
	      
	      if (next == null) {
		    	  if (NEXT_REF.compareAndSet(last, null, entry)) {
		    		  return;
		    	  }
	      }
	      
	      last = next;
	    }
	}

  public int size() {
    int count = 0;
    for (Entry e = head.next; e != null; e = e.next) {
      count++;
    }
    return count;
  }

  private Runnable runnable(int id) {
    return () -> {
      for (int j = 0; j < 1_000; j++) {
        addLast(id + " " + j);
      }
    };
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    int threadCount = 5;
    LockFreeStringList list = new LockFreeStringList();
    List<Callable<Object>> tasks = IntStream.range(0, threadCount)
        .mapToObj(list::runnable)
        .map(Executors::callable)
        .collect(Collectors.toList());
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	List<Future<Object>> futures = executor.invokeAll(tasks);
    executor.shutdown();
    for(Future<Object> f : futures) {
    	f.get();
    }
    System.out.println(list.size());
  }
}