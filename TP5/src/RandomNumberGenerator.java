package fr.umlv.conc;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Exercice 2
 * @author vrasquie
 *
 * @Q1 -> Un générateur de pseudo -aléatoire renvoi un nombre aléatoire selon une seed.
 * L'algorithme ci-dessous n'est pas Thread safe car il n'y aucun lock dans la method next et le nombre x évolue en lecture et ecriture.
 *
 * @Q2 -> 
 */
public class RandomNumberGenerator {
    private final AtomicLong x;
    
    public RandomNumberGenerator(long seed) {
        if (seed == 0) {
            throw new IllegalArgumentException("seed == 0");
        }
        x = new AtomicLong(seed);
    }
    
    private long update(long x) {
    	long newValue = x;
    	newValue ^= newValue >>> 12;
        newValue ^= newValue << 25;
        newValue ^= newValue >>> 27;
        
        return newValue;
    }
    
    public long next() {  // Marsaglia's XorShift
    	for (;;) {
	    	long x = this.x.get();
	    	long newValue = update(x);
	        
	        if (this.x.compareAndSet(x, newValue)) {
	        	return newValue * 2685821657736338717L;
	        }
    	}
    }
    
    public long next2() {
    	return x.updateAndGet(this::update) * 2685821657736338717L;
    }
    
    public static void main(String[] args) throws InterruptedException {
        HashSet<Long> set1 = new HashSet<>();
        HashSet<Long> set2 = new HashSet<>();
        RandomNumberGenerator rng = new RandomNumberGenerator(1);
        Thread t = new Thread(() -> {
            for (int i = 0; i < 5_000; i++) {
                set1.add(rng.next());
            }
        });
        t.start();
        for (int i = 0; i < 5_000; i++) {
            set2.add(rng.next());
        }
        t.join();
        
        System.out.println("set1: " + set1.size() + ", set2: " + set2.size());
        set1.addAll(set2);
        System.out.println("union: " + set1.size());
        // if the RandomNumberGenerator is thread-safe the two sets should be disjoint
        // and the size of the union should be 10_000 the sum of the two sizes
    }
    
}
