package fr.umlv.conc;

import java.util.Objects;

public class RendezVous<V> {
  private V value;
  private final Object lock = new Object();
  
  public void set(V value) {
	  synchronized(lock) {
		  Objects.requireNonNull(value);
		  this.value = value;
		  lock.notify();
	  }
  }
  
  public V badGet() {
	  for (;;) {
		  synchronized(lock) {
			  if (value != null) return value; 
		  }
	  }	  
  }
  
  public V get() throws InterruptedException {
	  synchronized(lock) {
		  while (value == null) {
			  lock.wait();
		  }
		  
		  return value;
	  }
  }
  
  public static void main(String[] args) throws InterruptedException {
	RendezVous<String> rendezVous = new RendezVous<>();
    new Thread(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
      rendezVous.set("hello");
    }).start();
    
    System.out.println(rendezVous.badGet());
  }
}
