import java.util.ArrayList;

/**
 * Exercice 2
 * @author Vincent Rasquier
 */
public class HelloThreadJoin {
	private static final int NB_THREADS = 4;
	private static final int NB_ITERATIONS = 5000;

	public static void main(String[] args) throws InterruptedException {
		ArrayList<Thread> threads = new ArrayList<>(NB_THREADS);
		
		for (int i = 0; i < NB_THREADS; i++) {
			int threadNb = i;

			threads.add(new Thread( () -> {	
				for (int j = 0; j < NB_ITERATIONS; j++) {
					System.out.println("Hello " + threadNb + " " + j);
				}
			}));

			threads.get(i).start();
		}

		for (Thread t : threads) {
			t.join();
		}

		System.out.println("Le programme est fini");
	}
}
