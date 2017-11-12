package shiver.me.timbers.aws.apigateway.proxy;

public class JsonDeserialisationException extends RuntimeException {
    public JsonDeserialisationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
