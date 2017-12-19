import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class PermitSync<V> {
	private final int permits;
	private final Object lock = new Object();
	private final ReentrantLock lo = new ReentrantLock();
	private final Condition condition = lo.newCondition();
	private int nb;
	
  public PermitSync(int permits) {
	  this.permits = permits;
  }
  
  public V safe(Supplier<? extends V> supplier) throws InterruptedException {
	  synchronized(lock) {
		  while (nb >= permits) {
			  lock.wait();
		  }
		  
		  nb++;
	  }
	  
	  try {
		  return supplier.get();
	  } finally {
		  synchronized(lock) {
			  nb--;
			  lock.notifyAll();
			  
		  }
	  }
  }
  
  public V safe2(Supplier<? extends V> supplier) throws InterruptedException {
	  lo.lock();
	  
	  try {
	  	while (nb >= permits) {
	  		condition.await();
		}

		nb++;
	  	V v = supplier.get();
	  	nb--;
	  	condition.signalAll();

	  	return v;
	  } finally {
		  lo.unlock();
	  }
  }
}
