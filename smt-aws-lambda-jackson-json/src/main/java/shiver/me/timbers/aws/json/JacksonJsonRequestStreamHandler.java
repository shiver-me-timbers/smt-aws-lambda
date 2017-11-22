package shiver.me.timbers.aws.json;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JacksonJsonRequestStreamHandler implements RequestStreamHandler {

    private final Class type;
    private final ObjectMapper objectMapper;
    private final RequestHandler handler;

    public <T> JacksonJsonRequestStreamHandler(Class<T> type, ObjectMapper objectMapper, RequestHandler<T, ?> handler) {
        this.type = type;
        this.objectMapper = objectMapper;
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        objectMapper.writeValue(
            output,
            handler.handleRequest(objectMapper.readValue(input, type), context)
        );
    }
}
