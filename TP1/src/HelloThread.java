/**
 * Exercice 1
 * @author Vincent Rasquier
 *
 * @Q1 Un Runnable est une interface fonctionnelle qui ne prend pas d'argument et qui retourne void. Cette interface sert, dans notre cas, à executer du code dans un thread.
 * @Q3 Nous remarquons que l'affichage se fait par période. En effet, l'affichage produit est une suite croissante de nombre pendant une période T non constante. Cette suite s'interrompt avec une autre suite après la période T.
 * Ce comportement est normal car System.out.println est Thread safe. En effet, en observant le code de la method nous observons qu'un synchornized est présent.
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
