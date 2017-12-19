package fr.upem.conc;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class PhilosopherDinner2 {
	private final ReentrantLock[] forks;
	  
	  public PhilosopherDinner2(int forkCount) {
	    ReentrantLock[] forks = new ReentrantLock[forkCount];
	    Arrays.setAll(forks, i -> new ReentrantLock());
	    this.forks = forks;
	  }
	  
	  public void eat(int index) {
	    ReentrantLock fork1 = forks[index];
	    ReentrantLock fork2 = forks[(index + 1) % forks.length];

	    fork1.lock();
	    fork2.lock();

	    try {
	    	System.out.println("philosopher " + index + " eat");
	    } finally {
    		fork1.unlock();
    		fork2.unlock();
	    }
	  }
	  
	  public static void main(String[] args) {
	    PhilosopherDinner2 dinner = new PhilosopherDinner2(5);
	    IntStream.range(0, 5).forEach(i -> {
	      new Thread(() -> {
	        for(;;) {
	          dinner.eat(i);
	        }
	      }).start();
	    });
	  }
}
