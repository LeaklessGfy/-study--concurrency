package fr.upem.conc;

import java.util.Scanner;

/**
 * Exercice 3
 * @author Vincent Rasquier
 *
 * @Q1 -> Car la Thread alloue de l'espace mémoire et peux aussi faire des locks. Si nous pouvions terminer une thread sans avoir son accord, 
 * nous pourrions perdre de la mémoire ou provoquer des deadlocks.
 *
 * @Q2 -> Une opération bloquante est une opération qui a besoin de de s'effectuer de façon synchrone. C'est à dire, une opération qui ne doit pas
 * être intérrompu sous faute d'une exception. Pour interrompre une opération bloquante il faut mettre un try catch autour de cette opération.
 *
 * @Q3 -> Thread.interrupted() nous renvois un booléan pour savoir si le flag interrupt à été mis sur la thread courante. Nous pouvons l'utiliser,
 * dans le cas présent, pour arrêter notre boucle infini dans la condition de notre for.
 *
 * @Q4 -> Faire un test sur le Thread.interrupted() et faire un return si c'est effectivement le cas.
 *
 * @Q5 -> interrupted fais deux choses à la fais. Elle lis le status de la thread et le clean. Ce qui veux dire que si Thread.interrupted() est
 * appelé deux fois succèssivement le résultat sera 'false' au second appel car interrupted remet le flag interupt sur false après sa lecture.
 * Cette méthode est donc mal nommé car elle effectue deux choses en même temps. Elle devrait s'appeler : interruptedAndClean ou quelque chose du
 * genre.
 *
 * @Q6 -> Il faut rajouter un scanner.hasNext() dans notre while. Puis faire une boucle pour demander l'arrêt aux différents Threads.
 */
public class Coitus {
	private static int slow() {
		int result = 1;
	    for (int i = 0; i < 1_000_000; i++) {
	      result += (result * 7) % 513;
	    }
	    return result;
	}
	
	public static void main(String[] args) {
		Thread[] threads = new Thread[4];
		
		for (int i = 0; i < 4; i++) {
			int threadNb = i;

			threads[i] = new Thread(() -> {
				int forNothing = 0;

				for (int j = 0; Thread.interrupted() != true; j++) {
					System.out.println("Thread: " + threadNb + ", Compteur: " + j);
					forNothing += slow();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						continue;
					}
				}
				
				System.out.println("End: " + forNothing);
			});

			threads[i].start();
		}
		
		System.out.println("Enter a thread id:");
        
		try(Scanner scanner = new Scanner(System.in)) {
		  while(scanner.hasNextInt() && scanner.hasNext()) {
			  int threadId = scanner.nextInt();
		    
			  if (threadId < 0 || threadId > 3) {
			  	System.out.println("Wrong ID!");
			  	continue;
			  }
		    
			  threads[threadId].interrupt();
		  }
		  
		  for (int i = 0; i < 4; i++) {
			  threads[i].interrupt();
		  }
        }
	}
}
