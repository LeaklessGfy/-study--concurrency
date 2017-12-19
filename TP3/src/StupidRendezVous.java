package fr.umlv.conc;

import java.util.Objects;

/**
 * Exercice 1
 * @author vrasquie
 *
 * Note: this code does several stupid things !
 *
 * @Q1 & @Q2 -> La class attend aux alentours de 5 secondes et puis affiche hello. 
 * Quand on enlève le Thread.sleep(1), le code semble attendre sans s'arrêter, le code bloque dans une boucle infini.
 * Le get de StupidRendezVous declenche l'optimisation JIT car la machine virtuelle va executer beaucoup plus de fois
 * la boucle. L'optimisation va arrêter de relire la valeur en mémoire et donc value sera toujours égale à null et donc cela va provoquer
 * la boucle infini. 
 *
 * @Q4 -> L'attente active est une method qui va tout le temps regarder une
 * valeur dans une boucle et le CPU va faire des milliers d'opérations pour
 * tout le temps aller chercher la nouvelle valeur. La consommation CPU augmente
 * drastiquement (jusqu'a 100%).
 *
 * @Q5 -> La consommation CPU reste modéré et ne monte plus comme dans la 
 * methode qui l'utilise.
 *
 * @Q6 -> Avec une boucle while qui vérifie le cas de spurious wakeup 
 * est bien géré.
 */
public class StupidRendezVous<V> {
  private V value;
  
  public void set(V value) {
    Objects.requireNonNull(value);
    this.value = value;
  }
  
  public V get() throws InterruptedException {
    while(value == null) {
        Thread.sleep(1);  // then comment this line !
    }
    return value;
  }
  
  public static void main(String[] args) throws InterruptedException {
    StupidRendezVous<String> rendezVous = new StupidRendezVous<>();
    new Thread(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
      rendezVous.set("hello");
    }).start();
    
    System.out.println(rendezVous.get());
  }
}
