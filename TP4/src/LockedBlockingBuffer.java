import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBlockingBuffer<T> {
	private final int size;
	private final ArrayDeque<T> dq;
	private final ReentrantLock lock;
	private final Condition isNotFull;
	private final Condition isNotEmpty;
	
	public LockedBlockingBuffer(int size) {
		this.size = size;
		this.dq = new ArrayDeque<>(size);
		this.lock = new ReentrantLock();
		this.isNotFull = lock.newCondition();
		this.isNotEmpty = lock.newCondition();
	}
	
	public void put(T element) throws InterruptedException {
		lock.lock();
		
		try {
			while (dq.size() >= size) {
				isNotFull.await();
			}
			
			dq.push(element);
			isNotEmpty.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	public T take() throws InterruptedException {
		lock.lock();
		
		try {
			while (dq.isEmpty()) {
				isNotEmpty.await();
			}
			
			isNotFull.signalAll();
			return dq.removeLast();
		} finally {
			lock.unlock();
		}
	}
}
