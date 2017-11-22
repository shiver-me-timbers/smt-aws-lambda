package shiver.me.timbers.aws.pingable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import shiver.me.timbers.aws.common.Env;
import shiver.me.timbers.aws.common.IOStreams;
import shiver.me.timbers.aws.json.JacksonJsonRequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PingableJsonRequestHandler implements RequestStreamHandler {

    private PingableRequestStreamHandler handler;

    public <T> PingableJsonRequestHandler(Class<T> type, RequestHandler<T, ?> handler) {
        this(new Env(), new IOStreams(), type, new ObjectMapper(), handler);
    }

    <T> PingableJsonRequestHandler(
        Env env,
        IOStreams ioStreams,
        Class<T> type,
        ObjectMapper objectMapper,
        RequestHandler<T, ?> requestHandler
    ) {
        handler = new PingableRequestStreamHandler(
            env,
            ioStreams,
            new JacksonJsonRequestStreamHandler(type, objectMapper, requestHandler)
        );
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.handleRequest(input, output, context);
    }
}
