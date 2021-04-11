import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// class implements Client behaviour and provides simple interaction
// to exchange the data with the Server using console
public class Client {
    // Server's port
    private static final int PORT = 1234;
    private static Scanner scanner;
    private String clientName;
    // socket object to connect to the Server
    private Socket socket;
    // the needed stream's to process data exchange
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    // constructs the Client object with the given name
    public Client(String clientName) {
        this.clientName = clientName;
    }

    // The Client's console application entry point
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.print("Enter you name:");
        // creates Client instance
        Client client = new Client(scanner.nextLine());
        // and connects to the Server
        client.connect();
        String registration, make;
        int choice;
        // displays menu and provides the appropriate actions
        // depending on user's choice until user select 'Exit'
        do {
            choice = menu();
            switch (choice) {
                // adds a car to the Server's database.
                case 1:
                    // reads Car's data from the console
                    System.out.println("\nAdd a car.");
                    System.out.print("Enter car's registration: ");
                    registration = scanner.nextLine();
                    System.out.print("Enter car's make: ");
                    make = scanner.nextLine();
                    System.out.print("Enter car's price: ");
                    int price = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter car's mileage: ");
                    int mileage = scanner.nextInt();
                    scanner.nextLine();
                    // and sends the appropriate request to the Server to add the Car
                    client.addCar(new Car(registration, make, price, mileage));
                    break;
                // sells a Car
                case 2:
                    // reads Car's registration from the console
                    System.out.println("\nSell a car.");
                    System.out.print("Enter car's registration: ");
                    registration = scanner.nextLine();
                    // end sends the request to the Server to sell the Car
                    if (client.sellCar(registration)) {
                        System.out.print(registration + " sold successfully.");
                    }
                    System.out.println();
                    break;
                // gets the list of Cars to sale
                case 3:
                    System.out.println("\nCar for sale.");
                    // sends the request to the Server to get the
                    // list of Cars for sale
                    List<Car> carForSale = client.getCarForSale();
                    if (carForSale.size() == 0) {
                        System.out.println("No available cars for sale.");
                    }
                    // displays the list
                    for (Car car : carForSale) {
                        System.out.println(car);
                    }
                    System.out.println();
                    break;
                // gets the list of Cars to sale by make
                case 4:
                    System.out.println("\nSearch by make.");
                    System.out.println("Enter car's make: ");
                    // reads Car's make from the console
                    make = scanner.nextLine();
                    // sends the request to the Server to get the
                    // list of Cars for sale by make
                    List<Car> carsByMake = client.getCarsByMake(make);
                    if (carsByMake.size() == 0) {
                        System.out.println("No available cars by " + make);
                    }
                    // displays the list
                    for (Car car : carsByMake) {
                        System.out.println(car);
                    }
                    System.out.println();
                    break;
                // gets the total value of sales
                case 5:
                    System.out.println("\nSearch total value.");
                    // sens the request to the server and displays the total value
                    System.out.println(client.totalValue() + "\n");
                    break;

            }
        } while (choice != 6);
        // disconnects from the Server
        client.disconnect();
    }

    // displays menu and provides user input to select the valid option
    private static int menu() {
        int choice = 0;
        do {
            System.out.println("1. Add a car");
            System.out.println("2. Sell a car");
            System.out.println("3. Cars for sale");
            System.out.println("4. Search by make");
            System.out.println("5. Total value of all sales");
            System.out.println("6. Exit");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > 6) {
                    System.out.println("Invalid input");
                    choice = 0;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
            }
        } while (choice < 0 || choice > 6);
        return choice;
    }

    // disconnects from the Server
    public void disconnect() {
        try {
            // sends the 'DISCONNECT' request
            objectOutputStream.writeObject(new Request(Request.RequestType.DISCONNECT));
            // and closes all streams and socket
            inputStream.close();
            outputStream.close();
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Disconnect error.");
        } finally {
            System.out.println(clientName+" disconnected.");
        }
    }

    // connects to the Server
    public void connect() {
        boolean firstMessage = false;
        // loops until the connection will be established
        do {
            InetAddress host = null;
            // tries get the localhost address
            try {
                host = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }

            try {
                // creates new Socket instance with the given host and port
                socket = new Socket(host, PORT);
                // creates all streams
                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);
                // sends 'CONNECT' request to the Server
                objectOutputStream.writeObject(new Request(Request.RequestType.CONNECT, clientName));
                Response response;
                try {
                    // reads Server's response
                    response = (Response) objectInputStream.readObject();
                    // and displays the response information
                    if (response.getResponseType() == Response.ResponseType.OK) {
                        System.out.println("Connection established.");
                        System.out.println("Welcome " + clientName + "!");
                    } else {
                        System.out.println("Server response: " + response.getResponseBody());
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Invalid server response.");
                    System.exit(1);
                }
            } catch (IOException e) {
                // displays only first error message in case some problems with connection
                // were occurred (e.g. the Client was started but Server wasn't).
                if (!firstMessage) {
                    System.out.println(clientName + ": waiting for server response...");
                    firstMessage = true;
                }
            }
        } while (socket == null);
    }

    // requests the list of Cars for sale from the Server
    @SuppressWarnings("unchecked")
    public List<Car> getCarForSale() {
        List<Car> result = new ArrayList<>();
        try {
            // sends 'CARS_FOR_SALE' request
            objectOutputStream.writeObject(new Request(Request.RequestType.CARS_FOR_SALE));
            // receives Server's response
            Response response = (Response) objectInputStream.readObject();
            if (response.getResponseType() == Response.ResponseType.OK) {
                // extracts response body and converts it to the List
                result = (List<Car>) response.getResponseBody();
            } else {
                // displays error message
                System.out.println("Server response: " + response.getResponseBody());
            }
        } catch (RuntimeException | ClassNotFoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return result;
    }

    // requests the list of Cars for sale by make from the Server
    @SuppressWarnings("unchecked")
    public List<Car> getCarsByMake(String make) {
        List<Car> result = new ArrayList<>();
        try {
            // sends 'CARS_FOR_SALE' request
            objectOutputStream.writeObject(new Request(Request.RequestType.CARS_BY_MAKE, make));
            // receives Server's response
            Response response = (Response) objectInputStream.readObject();
            if (response.getResponseType() == Response.ResponseType.OK) {
                // extracts response body and converts it to the List
                result = (List<Car>) response.getResponseBody();
            } else {
                // displays error message
                System.out.println("Server response: " + response.getResponseBody());
            }
        } catch (RuntimeException | ClassNotFoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return result;
    }

    // requests the selling of a Car
    public boolean sellCar(String registration) {
        boolean result = false;
        try {
            // sends 'SELL_CAR' request
            objectOutputStream.writeObject(new Request(Request.RequestType.SELL_CAR, registration));
            // receives response
            Response response = (Response) objectInputStream.readObject();
            if (response.getResponseType() == Response.ResponseType.OK) {
                // sets the result flag to 'true' if the response status is 'OK'
                result = true;
            } else {
                // displays error message otherwise
                System.out.println("Server response: " + response.getResponseBody());
            }
        } catch (RuntimeException | ClassNotFoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return result;
    }

    // reuests the total sales value
    public int totalValue() {
        int result = 0;
        try {
            // sends 'TOTAL_VALUE' request
            objectOutputStream.writeObject(new Request(Request.RequestType.TOTAL_VALUE));
            // retrieves response
            Response response = (Response) objectInputStream.readObject();
            // converts the response body to the integer
            result = (Integer) response.getResponseBody();
        } catch (RuntimeException | ClassNotFoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return result;
    }

    // requests for adding the Car to the Server's databse
    public void addCar(Car car) {
        try {
            // sends 'ADD_CAR' request
            objectOutputStream.writeObject(new Request(Request.RequestType.ADD_CAR, car));
            // retrieves response
            Response response = (Response) objectInputStream.readObject();
            if (response.getResponseType() == Response.ResponseType.OK) {
                // display message if the Car was added successfully
                System.out.println("Car added successfully");
            } else {
                // displays error message otherwise
                System.out.println("Server response: " + response.getResponseBody());
            }
        } catch (RuntimeException | ClassNotFoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

