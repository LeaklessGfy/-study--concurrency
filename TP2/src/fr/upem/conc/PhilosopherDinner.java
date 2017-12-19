package fr.upem.conc;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Exercice 4
 * @author Vincent Rasquier
 *
 * @Q1 -> Il y a un deadlock. Des verrous ne se 'unlock' pas.
 *
 * 
 */
public class PhilosopherDinner {
  private final ReentrantLock[] forks;
  
  public PhilosopherDinner(int forkCount) {
    ReentrantLock[] forks = new ReentrantLock[forkCount];
    Arrays.setAll(forks, i -> new ReentrantLock());
    this.forks = forks;
  }
  
  public void eat(int index) {
	  int index2 = (index + 1) % forks.length;
	  
	  System.out.println("Fork1: " + index);
	  System.out.println("Fork2: " + index2);
	  
	  ReentrantLock fork1 = forks[index];
	  ReentrantLock fork2 = forks[index2];
	
	  System.out.println(" -> Lock1: " + index);
	  fork1.lock();
	
	  try {
		  System.out.println(" -> Lock2: " + index2);
		  fork2.lock();
  
		  try {
			  System.out.println("philosopher " + index + " eat");
		  } finally {
			  System.out.println(" <- Unlock2: " + index2);
			  fork2.unlock();
		  }
	  } finally {
		  System.out.println(" <- Unlock1: " + index);
		  fork1.unlock();
	  }
  }
  
  public static void main(String[] args) {
    PhilosopherDinner dinner = new PhilosopherDinner(5);
    IntStream.range(0, 5).forEach(i -> {
      new Thread(() -> {
        for(;;) {
          dinner.eat(i);
        }
      }).start();
    });
  }
}
