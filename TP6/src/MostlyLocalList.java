import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MostlyLocalList<E> {
    private volatile ArrayList<E> list;
    private final static VarHandle LIST_HANDLE;
    private final int size;
    private final ThreadLocal<ArrayList<E>> threadList;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            LIST_HANDLE = lookup.findVarHandle(MostlyLocalList.class, "list", ArrayList.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }
    // ...

    /**
     * Create a MostlyLocalList asking to have at most
     * {@code threadLocalAllocatorSize} elements in each thread specific list.
     */
    public MostlyLocalList(int threadLocalAllocatorSize) {
        threadList = new ThreadLocal<>(){
            @Override
            protected ArrayList<E> initialValue() {
                return new ArrayList<>();
            }
        };
        size = threadLocalAllocatorSize;
        list = new ArrayList<>();
    }

    /**
     * Add an element to the a thread specific list and
     * if the thread specific list contains {@code threadLocalAllocatorSize}
     * elements, the elements are copied into the global dynamic array and
     * the thread speficic list is cleared.
     * @param element the element to add.
     */
    public void add(E element) {
        ArrayList<E> myList = threadList.get();
        myList.add(element);

        if (myList.size() > size) {
            flush();
        }
    }

    /**
     * Copied all elements from the thread specific list into the global
     * dynamic array and free all bookeeping data structured specific to
     * the current thread.
     */
    public void flush() {
        ArrayList<E> myList = threadList.get();

        for (;;) {
            ArrayList<E> oldList = list;
            ArrayList<E> newList = new ArrayList<>(oldList);
            newList.addAll(myList);

            if (LIST_HANDLE.compareAndSet(this, oldList, newList)) {
                myList.clear();
                threadList.remove();
                return;
            }
        }
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MostlyLocalList<Integer> m = new MostlyLocalList<>(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<Object> v = () -> {
            for (int i = 0; i < 500; i++) {
                m.add(i);
            }

            return null;
        };

        for (int j = 0; j < 5; j++) {
            Future<Object> f = executorService.submit(v);
            f.get();
        }

        executorService.shutdown();
        System.out.println(m.size());
    }
}
