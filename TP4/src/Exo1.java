import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Exercice 1
 * @author vrasquie
 *
 * @Q1 -> Le pattern ProducteurConsomateur résoud un problème lié à la concurrence. Ce problème vient du fait que nous voulons
 * faire communiquer des threads qui n'ont pas un temps d'execution fixe.
 *
 * @Q2 -> La method pour mettre une donnée s'appel .put(). La method pour prendre une donnée s'appel .take().
 *
 * @Q4 -> ArrayBlockingQueue est sizé par rapport à LinkedBlockingQueue, c'est à dire que nous devons lui donner une taille maximal qui
 * sera la taille d'instantiation du tableau en mémoire. LinkedBlockingQueue n'est pas forcément sizé, ce qui permet d'avoir une liste à n élément,
 * le problème est que cette implémentation peux poser des problèmes dans le cas où il n'y a plus de mémoire disponnible, cela va générer des erreurs.
 */
public class Exo1 {
	private static int BUFFER_SIZE = 1;

	public static void main(String[] args) {
		int[] producersTimes = {1, 4};
		int[] consumersTimes = {2, 3, 5};
		BlockingQueue<String> bq = new ArrayBlockingQueue<>(BUFFER_SIZE);
		
		for (int i = 0; i < 2; i++) {
			int nb = i;
			new Thread(() -> {
				try {
					for (;;) {
						bq.put("Hello " + nb);
						Thread.sleep(producersTimes[nb]);
					}
				} catch (InterruptedException e) {
					System.out.println("Error");
					return;
				}
			}).start();			
		}
		
		for (int i = 0; i < 3; i++) {
			int nb = i;
			new Thread(() -> {
				try {
					for (;;) {
						System.out.println(bq.take());
						Thread.sleep(consumersTimes[nb]);
					}
				} catch (InterruptedException e) {
					System.out.println("Error");
					return;
				}
			}).start();
		}
	}
}
