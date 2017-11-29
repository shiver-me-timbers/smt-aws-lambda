package shiver.me.timbers.aws.pingable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import shiver.me.timbers.aws.common.Env;
import shiver.me.timbers.aws.common.IOStreams;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class PingableRequestStreamHandlerTest {

    private Env env;
    private IOStreams ioStreams;
    private RequestStreamHandler streamHandler;
    private Runnable runnable;

    @Before
    public void setUp() {
        env = mock(Env.class);
        ioStreams = mock(IOStreams.class);
        streamHandler = mock(RequestStreamHandler.class);
        runnable = mock(Runnable.class);
    }

    private PingableRequestStreamHandler handler() {
        return new PingableRequestStreamHandler(env, ioStreams, streamHandler) {
            @Override
            public void ping() {
                runnable.run();
            }
        };
    }

    @Test
    public void Instantiation_for_coverage() {
        new PingableRequestStreamHandler(mock(RequestStreamHandler.class));
    }

    @Test
    public void Can_respond_successfully_to_a_ping_request() throws IOException {

        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);
        final Context context = mock(Context.class);

        final String pingString = someString();
        final BufferedInputStream bufferedInput = mock(BufferedInputStream.class);

        // Given
        given(env.get("PING_STRING")).willReturn(pingString);
        given(ioStreams.buffer(input)).willReturn(bufferedInput);
        given(ioStreams.readBytesToString(bufferedInput, 512)).willReturn(pingString + someString());

        // When
        handler().handleRequest(input, output, context);

        // Then
        final InOrder order = inOrder(ioStreams, bufferedInput, runnable, streamHandler);
        order.verify(ioStreams).buffer(any(InputStream.class));
        order.verify(bufferedInput).mark(0);
        order.verify(ioStreams).readBytesToString(any(InputStream.class), anyInt());
        order.verify(runnable).run();
        verifyZeroInteractions(streamHandler);
    }

    @Test
    public void Can_forward_a_non_ping_request() throws IOException {

        final ByteArrayInputStream input = mock(ByteArrayInputStream.class);
        final OutputStream output = mock(OutputStream.class);
        final Context context = mock(Context.class);

        final String pingString = someString(1024);
        final BufferedInputStream bufferedInput = mock(BufferedInputStream.class);

        // Given
        given(env.get("PING_STRING")).willReturn(pingString);
        given(ioStreams.buffer(input)).willReturn(bufferedInput);
        given(ioStreams.readBytesToString(bufferedInput, 512)).willReturn(someString());

        // When
        handler().handleRequest(input, output, context);

        // Then
        final InOrder order = inOrder(ioStreams, bufferedInput, runnable, streamHandler);
        order.verify(ioStreams).buffer(any(InputStream.class));
        order.verify(bufferedInput).mark(0);
        order.verify(ioStreams).readBytesToString(any(InputStream.class), anyInt());
        order.verify(bufferedInput).reset();
        order.verify(streamHandler).handleRequest(bufferedInput, output, context);
        verifyZeroInteractions(runnable);
    }

    @Test
    public void Direct_call_for_coverage() {
        final Pingable pingable = new PingableRequestStreamHandler(mock(RequestStreamHandler.class));
        pingable.ping();
    }
}