package question1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarManagementDemo {
	
	public static void main(String[] args) throws InterruptedException {
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
			executor.submit(new Runnable() {
				
				@Override
				public void run() {
					CarManagement carManagement = new CarManagement();
					carManagement.carOperation();
				}
			});
			
		executor.shutdown();
		
	}

}
