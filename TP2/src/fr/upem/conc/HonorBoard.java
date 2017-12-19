package fr.upem.conc;

/**
 * Exercice 1
 * @author Vincent Rasquier
 *
 * @Q1 -> Une class ThreadSafe est une class qui ne génère pas de résultat incohérent pour l'état de l'objet en concurrence.
 * C'est donc une class qui peux être utiliser avec plusieurs Threads sans générer de comportement inattendu.
 *
 * @Q2 -> HonorBoard n'est pas ThreadSafe car dans un premier thread nous faisons un set, donc un appel de méthod qui va modifier 
 * des champs interne à la class, sans de block synchronized ou de mecanisme équivalent. 
 * Donc le programme peux très bien écrire des résultats incohérents car après l'execution de la première opération 'this.firstname = ...' 
 * l'ordenanceur peux reprendre la main et la donner à une autre opération set.
 *
 * @Q4 -> On ne peux pas utiliser les getters dans le System.out car entre le temps d'execution du premier getter et le temps d'execution
 * du second, le HonorBoard peux avoir changé.
 */
public class HonorBoard {
  private String firstName;
  private String lastName;
  private final Object lock = new Object();
  
  public void set(String firstName, String lastName) {
	  synchronized(lock) {
	  	this.firstName = firstName;
	  	this.lastName = lastName;
	  }
  }
 
  @Override
  public String toString() {
	  synchronized(lock) {
		  return firstName + ' ' + lastName;
	  }
  }
  
  public static void main(String[] args) {
    HonorBoard board = new HonorBoard();
    
    new Thread(() -> {
      for(;;) {
        board.set("John", "Doe");
      }
    }).start();
    
    new Thread(() -> {
      for(;;) {
        board.set("Jane", "Odd");
      }
    }).start();
    
    new Thread(() -> {
      for(;;) {
        System.out.println(board);
      }
    }).start();
  }
}
