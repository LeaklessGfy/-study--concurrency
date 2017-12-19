package fr.umlv.conc;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.HashSet;
import java.lang.invoke.VarHandle;

public class RandomNumberGenerator2 {
	private volatile long x;
    private final static VarHandle X_REF;

    static {
    	Lookup look = MethodHandles.lookup();
    	try {
    		X_REF = look.findVarHandle(RandomNumberGenerator2.class, "x", long.class);
    	} catch (NoSuchFieldException | IllegalAccessException e) {
    		throw new AssertionError(e);
    	}
    }
    
    public RandomNumberGenerator2(long seed) {
        if (seed == 0) {
            throw new IllegalArgumentException("seed == 0");
        }
        x = seed;
    }
    
    private long update(long x) {
    	long newValue = x;
    	newValue ^= newValue >>> 12;
        newValue ^= newValue << 25;
        newValue ^= newValue >>> 27;
        
        return newValue;
    }
    
    public long next() {
    	for (;;) {
    		long x = this.x;
    		long newValue = update(x);
    		
    		if (X_REF.compareAndSet(this, x, newValue)) {
    			return newValue * 2685821657736338717L;
    		}
    	}
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
