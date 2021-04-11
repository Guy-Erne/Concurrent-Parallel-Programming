package question1;

public class Car {

	private String registration;
	private String make;
	private int price;
	private int mileage;
	private boolean forSale;

	public Car() {

	}

	public Car(String registration, String make, int price, int mileage, boolean forSale) {
		this.registration = registration;
		this.make = make;
		this.price = price;
		this.mileage = mileage;
		this.forSale = forSale;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public boolean isForSale() {
		return forSale;
	}

	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}

	@Override
	public String toString() {
		return "[registration=" + registration + ", make=" + make + ", price=" + price + ", mileage=" + mileage
				+ ", forSale=" + forSale + "]";
	}

	

}
