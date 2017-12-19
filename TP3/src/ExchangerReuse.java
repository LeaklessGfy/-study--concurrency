package fr.umlv.conc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ExchangerReuse<T> {
	private enum State {
		EMPTY,
		HALF,
		FULL
	}

	private final ReentrantLock lock = new ReentrantLock();
	private final Condition isFull = lock.newCondition();
	private final Condition isEmpty = lock.newCondition();
	
	private State state;
	private T first;
	private T second;

	public ExchangerReuse() {
		lock.lock();
		try{
			state = State.EMPTY;
		} finally {
			lock.unlock();
		}
	}
	
	public T exchange(T val) throws InterruptedException {
		lock.lock();

		try {
			while (state == State.FULL) {
				isEmpty.await();
			}

			switch (state) {
				case EMPTY:
					first = val;
					state = State.HALF;
					while (state != State.FULL) {
						isFull.await();
					}
					state = State.EMPTY;
					isEmpty.signalAll();

					return second;
				case HALF:
					second = val;
					state = State.FULL;
					isFull.signal();

					return first;
				default:
					throw new AssertionError();
			}
		} finally {
			lock.unlock();
		}
	}
}
