/**
 * Exercice 1
 * @author Vincent Rasquier
 *
 * @Q1
 * @Q3
 */
public class HelloThread {
	private static final int NB_THREADS = 4;
	private static final int NB_ITERATIONS = 5000;

	public static void main(String[] args) {
		for (int i = 0; i < NB_THREADS; i++) {
			int threadNb = i;

			new Thread( () -> {	
				for (int j = 0; j < NB_ITERATIONS; j++) {
					System.out.println("Hello " + threadNb + " " + j);
				}
			}).start();
		}
	}
}
