package shiver.me.timbers.aws.pingable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import shiver.me.timbers.aws.common.Env;
import shiver.me.timbers.aws.common.IOStreams;
import shiver.me.timbers.aws.json.JacksonJsonRequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.String.format;

public class PingableJsonRequestHandler implements RequestStreamHandler, Pingable {

    private final Logger log = Logger.getLogger(getClass());

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
        log.info(format("Starting the %s", getClass().getSimpleName()));
        handler = new PingableRequestStreamHandler(
            env,
            ioStreams,
            new JacksonJsonRequestStreamHandler(type, objectMapper, requestHandler)
        ) {
            @Override
            public void ping() {
                PingableJsonRequestHandler.this.ping();
            }
        };
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.handleRequest(input, output, context);
    }

    @Override
    public void ping() {
    }
}
