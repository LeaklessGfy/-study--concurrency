package fr.umlv.conc;

import java.util.stream.IntStream;

public class ExchangerReuseExample {
    public static void main(String[] args) throws InterruptedException {
        ExchangerReuse<String> exchanger = new ExchangerReuse<>();
        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                try {
                    System.out.println("thread " + i + " received from " + exchanger.exchange("thread " + i));
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }
            }).start();
        });
    }
}
