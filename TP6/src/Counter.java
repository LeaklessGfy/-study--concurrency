import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Counter {
	private final Sync<Integer> syn;
	private int count;
	
	public Counter() {
		syn = new Sync<>();
	}

	public int count() throws InterruptedException {		
		return syn.safe(() -> count++);				
	}

	public static void main(String[] args) {
		Counter counter = new Counter();
		Executor executor = Executors.newFixedThreadPool(2);
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		
		for (int i = 0; i < 2; i++) {
			executor.execute(() -> {
				for(;;) {
					try {
						System.out.println(counter.count());
					} catch (InterruptedException e) {
						return;
					}
				}
			});
			
			executorService.submit(() -> {
				for(;;) {
					try {
						System.out.println(counter.count());
					} catch (InterruptedException e) {
						return null;
					}
				}
			});
		}
	
		for (int i = 0; i < 2; i++) {
			new Thread(() -> {
				for(;;) {
					try {
						System.out.println(counter.count());
					} catch (InterruptedException e) {
						return;
					}
				}
			}).start();
		}
	}
}
