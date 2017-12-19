package fr.umlv.conc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Exchanger<T> {
	enum State {
		awaiting,
		inProgress,
		finished
	}

	private final Object lock = new Object();
	private final ReentrantLock locking = new ReentrantLock();
	private final Condition condition = locking.newCondition();
	private State state = State.awaiting;
	private int nb;
	private T first;
	private T second;
	
	public T exchange(T val) throws InterruptedException {
		locking.lock();
		
		try {
			if (state == State.inProgress) {
				second = val;
				state = State.finished;
				condition.signal();
				
				return first;
			}
			
			first = val;
			state = State.inProgress;
			
			while (state != State.finished) {
				condition.await();
			}
			
			return second;
		} finally {
			locking.unlock();
		}
	}
	
	public T exchangeWithState(T val) throws InterruptedException {
		synchronized(lock) {
			if (state == State.inProgress) {
				second = val;
				state = State.finished;
				lock.notify();
				
				return first;
			}
			
			first = val;
			state = State.inProgress;
			
			while (state != State.finished) {
				lock.wait();
			}
			
			return second;
		}
	}
	
	public T exchangeWithVal(T val) throws InterruptedException {
		synchronized(lock) {
			if (nb > 0) {
				second = val;
				lock.notify();
				nb++;

				return first;
			}
			
			first = val;
			nb++;
			
			while (nb == 1) {
				lock.wait();
			}
			
			return second;
		}
	}
}
