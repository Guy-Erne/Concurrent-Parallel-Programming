import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// class represents the Server's database
public class Data {
    // internal storage of cars
    private List<Car> cars;

    // constructs Data instance
    public Data() {
        cars = new ArrayList<>();
        // initializes the internal list with the 15 Cars
        cars.add(new Car("16L1234", "Ferrari", 120000, 1000));
        cars.add(new Car("01LH1234", "Ford Fiesta", 1000, 1000));
        cars.add(new Car("02D1234", "Ford Focus", 11000, 2000));
        cars.add(new Car("03WW1234", "Ford Mondeo", 12000, 3000));
        cars.add(new Car("05KK1234", "Ford Mustang", 14000, 5000));
        cars.add(new Car("06CW1234", "Ford B-Max", 15000, 6000));
        cars.add(new Car("07LS1234", "Ford C-Max", 16000, 7000));
        cars.add(new Car("08KE1234", "Ford S-Max", 17000, 8000));
        cars.add(new Car("09ER1234", "Toyota Corolla", 18000, 9000));
        cars.add(new Car("10WM1234", "Toyota Starlet", 19000, 10000));
        cars.add(new Car("11M1234", "Toyota Avensis", 20000, 11000));
        cars.add(new Car("12RF1234", "Audi A3", 21000, 12000));
        cars.add(new Car("13FT1234", "Audi A4", 22000, 13000));
        cars.add(new Car("14JD1234", "BMW 320", 23000, 14000));
        cars.add(new Car("15SH1234", "BMW 530", 24000, 15000));

    }

    // adds the given Car to the internal list.
    // the method executed in a multithreaded environment only in one thread.
    public synchronized void add(Car car) {
        // creates temporary ArrayList to store all data from the internal list
        List<Car> newCars = new ArrayList<>(cars);
        // adds the given Car to the temporary ArrayList
        newCars.add(car);
        // assigns the temporary ArrayList as the internal list
        cars = newCars;
    }

    // returns the total sales value
    public int totalValue() {
        return cars.stream()
                .filter(car -> !car.isForSale())
                .mapToInt(car -> car.getPrice())
                .reduce(0, (o, n) -> o + n);
    }

    // returns the list of Cars for sale by make
    public List<Car> carsByMake(String make) {
        return cars.stream()
                .filter(car -> car.isForSale() && car.getMake().contains(make))
                .collect(Collectors.toList());
    }

    // returns the list of Cars for sale
    public List<Car> carsForSale() {
        return cars.stream()
                .filter(car -> car.isForSale())
                .collect(Collectors.toList());
    }

    // sets the Car's 'forSale' attribute to false.
    // returns true if the action was correct.
    // returns false if the Car already sold or
    // doesn't present into the internal list.
    public boolean sellCar(String registration) {
        boolean result = false;
        for (Car car : cars) {
            if (car.getRegistration().equals(registration)) {
                if (car.isForSale()) {
                    car.setForSale(false);
                    result = true;
                } else {
                    return false;
                }
                break;
            }
        }
        return result;
    }
}
