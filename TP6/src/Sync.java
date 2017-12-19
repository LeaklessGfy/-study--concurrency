import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class Sync<V> {
	private final Object lock = new Object();
	private final ReentrantLock lo = new ReentrantLock();
	
	private boolean running;
	
	public Sync() {
		synchronized(lock) {
			this.running = false;
		}
	}
	
	public boolean insafe() {
		return running;
	}
	
	public V safe(Supplier<? extends V> supplier) throws InterruptedException  {
		while (running) {
			lock.wait();
		}
		
		synchronized(lock) {
			running = true;
		}
		
		V v = supplier.get();
			
		synchronized(lock) {
			running = false;
			lock.notifyAll();
			return v;
		}
	}
	
	public boolean insafe2() {
		return lo.isLocked();
	}
	
	public V safe2(Supplier<? extends V> supplier) {
		lo.lock();
		
		try {
			return supplier.get();
		} finally {
			lo.unlock();
		}
	}
}
