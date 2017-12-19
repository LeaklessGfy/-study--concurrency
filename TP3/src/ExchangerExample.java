package fr.umlv.conc;

//import java.util.concurrent.Exchanger;

/**
 * Exercice 2
 * @author vrasquie
 *
 * @Q1 -> Il faut utiliser un champs interne à la class
 */
public class ExchangerExample {
  public static void main(String[] args) throws InterruptedException {
    Exchanger<String> exchanger = new Exchanger<>();
    new Thread(() -> {
      try {
        System.out.println("thread 1 " + exchanger.exchange("foo1"));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    }).start();
    System.out.println("main " + exchanger.exchange(null));
  }
}
