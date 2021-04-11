// Class provides some tests. JVM option -ea must be provided.
// Tests allow get the right results only one time.
// Restart the Server to execute tests one more time.
public class CarShopTest {
    public static void main(String[] args) {
        // expected value of total sales
        int expectedTotalValue = 0;
        // array to store Clients threads
        ClientTest[] clients = new ClientTest[100];
        // fills the array of threads and starts them
        for (int i = 0; i < clients.length; i++) {
            // updates expected value of total sales
            expectedTotalValue += 10000 + i * 10;
            clients[i] = new ClientTest(i);
            clients[i].start();
        }

        // waits until all threads stop
        for (ClientTest clientTest : clients) {
            try {
                clientTest.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // compares the expected total value with the sum of total sales of all Client
        assert ClientTest.getTotalValue() == expectedTotalValue;
    }
}

// class represents the Client's thread
class ClientTest extends Thread {
    // the sum of total sales of all Clients
    private static int totalValue;
    // the unique id of Client
    private int i;

    // creates Client instance
    public ClientTest(int i) {
        this.i = i;
    }

    // returns the total sales of all Clients
    public static int getTotalValue() {
        return totalValue;
    }

    // main thread's method
    @Override
    public void run() {
        // creates Client with the unique id
        Client client = new Client("User " + i);
        // connects to the Server
        client.connect();
        // simulates the client’s 'thinking'
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // creates Car instance with the unique registration and make
        Car car = new Car(i + "XX" + (1000 + i),
                "Make " + i,
                10000 + i * 10,
                50000 + i * 1000);

        // adds the Car to the Server database
        client.addCar(car);
        // tests if the Server contains information about the added above Car using
        // 'CARS_BY_MAKE' query
        assert client.getCarsByMake("Make " + i).contains(car);
        // tests if the Server contains information about the added above Car using
        // 'CARS_FOR_SALE' query
        assert client.getCarForSale().contains(car);
        // sells the Car and tests that the selling result is valid
        assert client.sellCar(i + "XX" + (1000 + i));
        // updates the sum of total sales of all Clients
        synchronized (ClientTest.class) {
            totalValue += 10000 + i * 10;
        }
        // sells the same Car again and tests that the selling result is invalid
        assert !client.sellCar(i + "XX" + (1000 + i));
        // tests if the Server doesn't contain information about the added above Car using
        // 'CARS_FOR_SALE' query
        assert !client.getCarForSale().contains(car);
        // tests if the Server doesn't contain information about the added above Car using
        // 'CARS_BY_MAKE' query
        assert !client.getCarsByMake("Make " + i).contains(car);
        // simulates the client’s 'thinking'
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // disconnects ftom the Server
        client.disconnect();
    }
}