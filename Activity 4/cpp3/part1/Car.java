import java.io.Serializable;

// class represents a car
public class Car implements Serializable {
    // car's attributes
    private String registration, make;
    private int price, mileage;
    private boolean forSale;

    // constructs the Car using the given attributes
    public Car(String registration, String make, int price, int mileage) {
        this.registration = registration;
        this.make = make;
        this.price = price;
        this.mileage = mileage;
        forSale = true;
    }

    // setter for 'forSale' attribute
    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    // getters for attributes
    public String getRegistration() {
        return registration;
    }

    public String getMake() {
        return make;
    }

    public int getPrice() {
        return price;
    }


    public boolean isForSale() {
        return forSale;
    }


    // returns the string representation of a Car
    @Override
    public String toString() {
        return "[registration=" + registration +
                ", make=" + make +
                ", price=" + price +
                ", milleage=" + mileage +
                ", forSale=" + forSale + ']';
    }

    // returns true if this Car equal to other (the registration attributes are equal)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return registration != null ? registration.equals(car.registration) : car.registration == null;
    }
}
