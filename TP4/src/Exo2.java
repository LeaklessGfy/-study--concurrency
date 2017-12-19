
public class Exo2 {
	public static void main(String[] args) {
		LockedBlockingBuffer<String> lbb = new LockedBlockingBuffer<>(1);
		
		for (int i = 0; i < 2; i++) {
			int nb = i;
			new Thread(() -> {
				try {
					for (;;) {
						lbb.put("Hello " + nb);
						Thread.sleep(1);
					}
				} catch (InterruptedException e) {
					System.out.println("Error");
					return;
				}
			}).start();			
		}
		
		for (int i = 0; i < 3; i++) {
			new Thread(() -> {
				try {
					for (;;) {
						System.out.println(lbb.take());
						Thread.sleep(4);
					}
				} catch (InterruptedException e) {
					System.out.println("Error");
					return;
				}
			}).start();
		}
	}

}
