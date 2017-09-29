import java.util.ArrayList;

/**
 * Exercice 3
 * @author Vincent Rasquier
 *
 * @Q2 La taille de la list est plus petite que le nombre total d"élément car dans la method add, le champs size est mise à jour. Ce champs représente la dernière case de la list qui est disponnible (et ainsi la taille de celle-ci). Entre le temps de lecture de la valeur size (au moment du size++) et le moment d'écriture de la nouvelle valeur, l'objet list peut avoir évolué à cause d'un autre thread (+1, +2, +*) or au retour sur le thread avant interruption, le thread ne prendra pas en compte ces ajouts.
 * @Q3 Il peut y avoir un out-of-bounds, c'est à dire que la size devient supérieur à la taille du tableau et donc ne passe plus dans le grow car la method add fait un test d'égalité et non >. De plus, avec les optimisations de Java, il se peut que le processeur change l'ordre d'execution de certaine instruction comme 'augmenter la taille du tableau' avant de finir la copie entière du tableau (array.copy).
 */
public class HelloListBug {
	private static final int NB_THREADS = 4;
	private static final int NB_ITERATIONS = 5000;

	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		ArrayList<Thread> threads = new ArrayList<>(NB_THREADS);
		ArrayList<Integer> list = new ArrayList<>();

		for (int i = 0; i < NB_THREADS; i++) {
			threads.add(new Thread( () -> {	
				for (int j = 0; j < NB_ITERATIONS; j++) {
					synchronized(lock) {
						list.add(j);
					}
				}
			}));

			threads.get(i).start();
		}

		for (Thread t : threads) {
			t.join();
		}

		System.out.println(list.size());
	}
}
