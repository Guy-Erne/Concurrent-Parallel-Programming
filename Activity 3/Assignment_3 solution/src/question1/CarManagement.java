package question1;

import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

public class CarManagement {

	private Semaphore semaphore = new Semaphore(50);

	public void carOperation() {
		try {
			semaphore.acquire();

			Scanner sc = new Scanner(System.in);
			String scan = null;
			int option = 0;
			CopyOnWriteArrayList<Car> cars = new CopyOnWriteArrayList<>();
			int carPrice = 0;

			do {
				System.out.println();
				System.out.println("# # Choose one of the options below # #");
				System.out.println("Option 1 - A sales person can add a new car to the system.");
				System.out.println("Option 2 - Sell a car");
				System.out.println("Option 3 - Request information from the system");
				System.out.println("Option 0 - Exit program");
				System.out.println("_______________________");

				System.out.print("Enter your choice here:"); 
				String nextLine2 = sc.nextLine();
				String nextLine = nextLine2;
				option = Integer.parseInt(nextLine);

				if (option == 1) {
					System.out.println("************ Add a Car ************");
					cars.add(new Car("16L1234","Ferrari", 120000, 1000, true));
					cars.add(new Car("01LH1234","Ford Fiesta", 1000, 1000, true));
					cars.add(new Car("02D1234","Ford Focus", 11000, 2000, true));
					cars.add(new Car("03WW1234","Ford Mondeo", 120000, 3000, true));
					cars.add(new Car("05KK1234","Ford Mustang", 14000, 5000, true));
					cars.add(new Car("06CW1234","Ford B-Max", 150000, 6000, true));
					cars.add(new Car("08KE1234","Ford S-Max", 17000, 8000, true));
					cars.add(new Car("10WM1234","Toyota Starlet", 19000, 10000, true));
					cars.add(new Car("11M1234","Toyota Avensis", 200000, 11000, true));
					cars.add(new Car("03WW1234","Ford Mondeo", 12000, 3000, true));
					cars.add(new Car("10WM1234","Toyota Starlet", 19000, 10000, true));
					cars.add(new Car("11M1234","Toyota Avensis", 20000, 11000, true));
					cars.add(new Car("16L1274","Ferrari", 120000, 1000, true));
					cars.add(new Car("01LH1234","Ford Fiesta", 200000, 1000, true));
					cars.add(new Car("02D1234","Ford Focus", 12000, 3000, true));

					for (Car car : cars) {
						System.out.println(car.getMake() +" added successfully");
					}

				} else if (option == 2) { 
					System.out.println("************ Cars for Sale ************");

					for (Car car : cars) {
						System.out.println(car);
					}

				} else if (option == 3) { 
					do {
						System.out.println("************ Request from system *************");
						System.out.println("Option a - cars for sale");
						System.out.println("Option b - cars of a given make");
						System.out.println("Option c - total value of all sales");

						//semaphore.acquire();

						System.out.print("Enter your choice here:");
						scan = sc.nextLine();

						if ("a".equalsIgnoreCase(scan)) {
							System.out.print("Enter registration number to sell the car : ");
							String sellCarReg = sc.nextLine();

							for (Car car : cars) {
								if(car.getRegistration().equalsIgnoreCase(sellCarReg)) {
									System.out.println(car.getRegistration() + " sold successfully");
									car.setForSale(false);
								}
							}

						} else if ("b".equalsIgnoreCase(scan)) { 
							System.out.print("Enter make to search : ");
							String makeToSearch = sc.nextLine();
							for (Car car : cars) {
								if(car.getMake().equalsIgnoreCase(makeToSearch)) {
									System.out.println(car);
								}
							}

						} else if ("c".equalsIgnoreCase(scan)) { 
							for (Car car : cars) {
								carPrice += car.getPrice();
							}
							System.out.print("Total Value of all sales: " + carPrice);

						}
						//semaphore.release();

					} while (!(scan.equals("a") || scan.equals("b") || scan.equals("c")));

				}
			} while (option != 0);
			sc.close();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}
}
