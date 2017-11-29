package shiver.me.timbers.aws.pingable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.log4j.Logger;
import shiver.me.timbers.aws.common.Env;
import shiver.me.timbers.aws.common.IOStreams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.String.format;

public class PingableRequestStreamHandler implements RequestStreamHandler, Pingable {

    private final Logger log = Logger.getLogger(getClass());

    private final String pingString;
    private final IOStreams ioStreams;
    private final RequestStreamHandler streamHandler;

    public PingableRequestStreamHandler(RequestStreamHandler streamHandler) {
        this(new Env(), new IOStreams(), streamHandler);
    }

    PingableRequestStreamHandler(Env env, IOStreams ioStreams, RequestStreamHandler streamHandler) {
        log.info(format("Starting the %s", getClass().getSimpleName()));
        this.pingString = env.get("PING_STRING");
        this.ioStreams = ioStreams;
        this.streamHandler = streamHandler;
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        final BufferedInputStream bufferedInput = ioStreams.buffer(input);
        bufferedInput.mark(0);
        if (isPingRequest(bufferedInput)) {
            ping();
            return;
        }
        bufferedInput.reset();
        streamHandler.handleRequest(bufferedInput, output, context);
    }

    private boolean isPingRequest(BufferedInputStream bufferedInput) throws IOException {
        return ioStreams.readBytesToString(bufferedInput, 512).startsWith(pingString);
    }

    @Override
    public void ping() {
    }
}
