import java.io.Serializable;

// class represents Server's response
public class Response implements Serializable {
    // the inner enum to store response types
    public enum ResponseType {
        ERROR, OK
    }
    // response type of the Response object
    private ResponseType responseType;
    // the body of response. the additional information depending on the
    // response type (may by null)
    private Object responseBody;

    // constructs Response object with the given parameters
    public Response(ResponseType responseType, Object responseBody) {
        this.responseType = responseType;
        this.responseBody = responseBody;
    }

    // constructs Response object with only response type
    public Response(ResponseType responseType) {
        this.responseType = responseType;
    }

    // returns response type
    public ResponseType getResponseType() {
        return responseType;
    }

    // returns response body
    public Object getResponseBody() {
        return responseBody;
    }
}
