import java.util.ArrayDeque;

public class SynchronizedBlockingBuffer<T> {
	private final int size;
	private final ArrayDeque<T> dq;
	
	public SynchronizedBlockingBuffer(int size) {
		this.size = size;
		this.dq = new ArrayDeque<>(size);
	}
	
	public void put(T element) throws InterruptedException {
		synchronized(dq) {
			while (dq.size() >= size) {
				dq.wait();
			}
			
			dq.push(element);
			dq.notifyAll();
		}
	}
	
	public T take() throws InterruptedException {
		synchronized(dq) {
			while (dq.isEmpty()) {
				dq.wait();
			}
			
			dq.notifyAll();
			return dq.removeLast();
		}
	}
}
