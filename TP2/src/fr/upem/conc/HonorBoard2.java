package fr.upem.conc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Exercice 2
 * @author Vincent Rasquier
 *
 * @Q1 -> Un lock ré-entrant, est un lock qui permet à une class de rappeler une de ses methodes qui lock également. 
 * En effet, le lock ré-entrant va compter le nombre de fois que l'objet lock est locké.
 */
public class HonorBoard2 {
	  private String firstName;
	  private String lastName;
	  private final ReentrantLock lock = new ReentrantLock();
	  
	  public void set(String firstName, String lastName) {
		  lock.lock();
		  
		  try {
		  	this.firstName = firstName;
		  	this.lastName = lastName;
		  } finally {
			  lock.unlock();
		  }
	  }
	 
	  @Override
	  public String toString() {
		  lock.lock();
		  
		  try {
			  return firstName + ' ' + lastName;
		  } finally {
			  lock.unlock();
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
