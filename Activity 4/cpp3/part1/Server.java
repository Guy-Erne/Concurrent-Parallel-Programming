import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

// class implements Server behaviour
public class Server {
    // Maximum value of simultaneously connected Clients and Server port
    private final static int MAX_USERS = 50, PORT = 1234;

    // the main method. application entry point.
    public static void main(String[] args) {
        // creates database
        Data data = new Data();
        // creates executor service object
        ExecutorService service = Executors.newFixedThreadPool(MAX_USERS);
        // creates semaphore object
        Semaphore semaphore = new Semaphore(MAX_USERS);
        ServerSocket serverSocket = null;
        // tries to create ServerSocket object with the given port
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server running...");
        } catch (IOException e) {
            // terminates execution in case exception was thrown
            System.out.println("I/O Error. Server terminated");
            System.exit(1);
        }

        // infinity loop
        while (true) {
            // tries to acquire semaphore permission
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                // accepts the connection
                Socket socket = serverSocket.accept();
                // execute the Runnable instance (Process object) using executor service
                service.submit(new Process(socket, data, semaphore));
            } catch (IOException e) {
                System.out.println("I/O Error: " + e.getMessage());
            }
        }
    }
}

// class represents the Runnable implementation
// to provide network interaction with the Client
class Process implements Runnable {
    // stores the time of first instance creation to provide useful logs
    private static long timeBegin = System.currentTimeMillis();
    // stores database instance
    private Data data;
    // Socket object
    private Socket socket;
    // Semaphore object
    private Semaphore semaphore;
    private String clientName;
    // counter of connections
    private static int counter;
    // constructs the instance
    public Process(Socket socket, Data data, Semaphore semaphore) {
        this.data = data;
        this.socket = socket;
        this.semaphore = semaphore;
    }

    // main thread method
    @Override
    public void run() {
        try {
            // creates needed streams
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            do {
                Request request;
                try {
                    // retrieves request from the Client
                    request = (Request) objectInputStream.readObject();
                } catch (SocketException | EOFException e) {
                    // creates 'DISCONNECT' request in case some
                    // exceptions occurred during request retrieving
                    request = new Request(Request.RequestType.DISCONNECT);
                }

                Response response;
                // invokes the appropriate methods depending on request type
                switch (request.getRequestType()) {
                    case CONNECT:
                        response = processConnect(request);
                        break;
                    case CARS_FOR_SALE:
                        response = processCarsForSale();
                        break;
                    case ADD_CAR:
                        response = processAddCar(request);
                        break;
                    case CARS_BY_MAKE:
                        response = carsOfMake(request);
                        break;
                    case TOTAL_VALUE:
                        response = totalValue();
                        break;
                    case SELL_CAR:
                        response = sellCar(request);
                        break;
                    case DISCONNECT:
                        response = processDisconnect();
                        break;
                    default:
                        response = new Response(Response.ResponseType.ERROR, "Invalid request");
                }
                if (clientName != null) {
                    // sends Server's response
                    objectOutputStream.writeObject(response);
                }
            } while (clientName != null);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        } finally {
            // closes the socket in case some error occurred or 'DISCONNECT' request was received
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("I/O Error: " + e.getMessage());
            }
            // release one semaphore's permit
            semaphore.release();

        }
    }

    // processes 'CONNECT' request
    private Response processConnect(Request request) {
        Response response;
        if (clientName != null) {
            // creates error response in case the Client already connected
            response = new Response(Response.ResponseType.ERROR, "User '" + clientName + "' already connected");
        } else {
            try {
                // assigns Client's id and returns 'OK' response
                clientName = (String) request.getRequestBody();
                response = new Response(Response.ResponseType.OK);
                // increases connections counter and displays log
                synchronized (Process.class) {
                    counter++;
                    System.out.println("[" + (System.currentTimeMillis() - timeBegin) + "] User '" + clientName + "' connected. " +
                            "Used connections - " + counter);
                }
            } catch (ClassCastException e) {
                response = new Response(Response.ResponseType.ERROR, "Internal server error");
            }
        }
        return response;
    }

    // processes 'DISCONNECT' request
    private Response processDisconnect() {
        // decreases connections counter and displays log
        synchronized (Process.class) {
            counter--;
            System.out.println("[" + (System.currentTimeMillis() - timeBegin) + "] User '" + clientName + "' disconnected. " +
                    "Used connections - " + counter);
        }
        clientName = null;
        // returns 'OK' response
        return new Response(Response.ResponseType.OK);
    }

    // processes 'ADD_CAR' request
    private Response processAddCar(Request request) {
        Response response;
        if (clientName == null) {
            // creates error response in case the Client not connected
            response = new Response(Response.ResponseType.ERROR, "User not connected");
        } else {
            try {
                // retrieves the Car object from the request
                Car car = (Car) request.getRequestBody();
                // adds the Car to the database
                data.add(car);
                // creates 'OK' response
                response = new Response(Response.ResponseType.OK);
            } catch (ClassCastException e) {
                response = new Response(Response.ResponseType.ERROR, "Internal server error");
            }
        }
        return response;
    }

    // processes 'CARS_FOR_SALE' request
    private Response processCarsForSale() {
        Response response;
        if (clientName == null) {
            // creates error response in case the Client not connected
            response = new Response(Response.ResponseType.ERROR, "User not connected");
        } else {
            try {
                // retrieves Cars for sale from the database
                List<Car> carsForSale = data.carsForSale();
                // creates 'OK' response with the list of cars for sale as response body
                response = new Response(Response.ResponseType.OK, carsForSale);
            } catch (ClassCastException e) {
                response = new Response(Response.ResponseType.ERROR, "Internal server error");
            }
        }
        return response;
    }

    // processes 'TOTAL_VALUE' request
    private Response totalValue() {
        Response response;
        if (clientName == null) {
            // creates error response in case the Client not connected
            response = new Response(Response.ResponseType.ERROR, "User not connected");
        } else {
            try {
                // retrieves total sales value from the database
                Integer totalValue = data.totalValue();
                // creates 'OK' response with the total sales value as response body
                response = new Response(Response.ResponseType.OK, totalValue);
            } catch (ClassCastException e) {
                response = new Response(Response.ResponseType.ERROR, "Internal server error");
            }
        }
        return response;
    }

    // processes 'CARS_OF_MAKE' request
    private Response carsOfMake(Request request) {
        Response response;
        if (clientName == null) {
            // creates error response in case the Client not connected
            response = new Response(Response.ResponseType.ERROR, "User not connected");
        } else {
            try {
                // retrieves Cars for sale by make from the database
                List<Car> carsByMake = data.carsByMake((String) request.getRequestBody());
                // creates 'OK' response with the list of cars for sale by make as response body
                response = new Response(Response.ResponseType.OK, carsByMake);
            } catch (ClassCastException e) {
                response = new Response(Response.ResponseType.ERROR, "User not connected");
            }
        }
        return response;
    }

    // processes 'SELL_CAR' request
    private Response sellCar(Request request) {
        Response response;
        if (clientName == null) {
            // creates error response in case the Client not connected
            response = new Response(Response.ResponseType.ERROR, "User not connected");
        } else {
            try {
                // retrieves the result of 'sell a car' action from the database (true or false)
                boolean result = data.sellCar((String) request.getRequestBody());
                // creates response depending on result ('OK' or 'ERROR')
                response = new Response(result ? Response.ResponseType.OK : Response.ResponseType.ERROR,
                        result ? null : "The car wasn't found or already sold");
            } catch (ClassCastException e) {
                response = new Response(Response.ResponseType.ERROR, "Internal server error");
            }
        }
        return response;
    }
}
