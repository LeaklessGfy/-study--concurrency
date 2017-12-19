package fr.umlv.conc;

/**
 * Exercice 1
 * @author vrasquie
 *
 * @Q1 -> Il peut y avoir une boucle infini car le JIT optimise le code en transformant le stop en une variable local. 
 * @Q3 -> Nous devons faire un synchronized dans la method .stop() et la method .runCounter() avec un lock private final.
 * @Q4 -> Une implantations qui na pas de bloc synchronized ni de lock est une implantation lock-free.
 */
public class Bogus {
	private final Object lock = new Object();
	private boolean stop;
	
	public void runCounter() {
		int localCounter = 0;
		for(;;) {
			synchronized(lock) {
				if (stop) {
					break;
				}
			}

			localCounter++;
		}
		System.out.println(localCounter);
	}

	public void stop() {
		synchronized(lock) {
			stop = true;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Bogus bogus = new Bogus();
		Thread thread = new Thread(bogus::runCounter);
		thread.start();
		Thread.sleep(100);
		bogus.stop();
		thread.join();
	}
}
