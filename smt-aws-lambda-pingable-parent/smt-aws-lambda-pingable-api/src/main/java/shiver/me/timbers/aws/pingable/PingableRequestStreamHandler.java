package shiver.me.timbers.aws.pingable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import shiver.me.timbers.aws.common.Env;
import shiver.me.timbers.aws.common.IOStreams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PingableRequestStreamHandler implements RequestStreamHandler {

    private final Env env;
    private final IOStreams ioStreams;
    private final RequestStreamHandler streamHandler;

    public PingableRequestStreamHandler(RequestStreamHandler streamHandler) {
        this(new Env(), new IOStreams(), streamHandler);
    }

    PingableRequestStreamHandler(Env env, IOStreams ioStreams, RequestStreamHandler streamHandler) {
        this.env = env;
        this.ioStreams = ioStreams;
        this.streamHandler = streamHandler;
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        final BufferedInputStream bufferedInput = ioStreams.buffer(input);
        bufferedInput.mark(0);
        if (isPingRequest(bufferedInput)) {
            return;
        }
        bufferedInput.reset();
        streamHandler.handleRequest(bufferedInput, output, context);
    }

    private boolean isPingRequest(BufferedInputStream bufferedInput) throws IOException {
        return ioStreams.readBytesToString(bufferedInput, 512).startsWith(env.get("PING_STRING"));
    }
}
