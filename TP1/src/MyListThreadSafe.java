import java.util.ArrayList;

/**
 * Exercice 3
 * @author Vincent Rasquier
 */
public class MyListThreadSafe<E> {
	private final Object lock = new Object();
	private final ArrayList<E> list = new ArrayList<>();

	public boolean add(E e) {
		synchronized(lock) {
			return list.add(e);
		}
	}

	public int size() {
		synchronized(lock) {
			return list.size();
		}
	}
}
