import java.io.Serializable;

// class represents Client's request
public class Request implements Serializable {
    // the inner enum to store request types
    public enum RequestType {
        CARS_FOR_SALE, CARS_BY_MAKE, TOTAL_VALUE, SELL_CAR, ADD_CAR, CONNECT, DISCONNECT;
    }
    // request type of the Request object
    private RequestType requestType;
    // the body of request. the additional information depending on the
    // request type (may by null)
    private Object requestBody;

    // constructs Request object with the given parameters
    public Request(RequestType requestType, Object requestBody) {
        this.requestType = requestType;
        this.requestBody = requestBody;
    }

    // constructs Request object with only request type
    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    // returns request type
    public RequestType getRequestType() {
        return requestType;
    }

    // returns request body
    public Object getRequestBody() {
        return requestBody;
    }
}
