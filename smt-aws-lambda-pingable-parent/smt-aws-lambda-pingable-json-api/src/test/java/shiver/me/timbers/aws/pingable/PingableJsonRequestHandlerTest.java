package shiver.me.timbers.aws.pingable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class PingableJsonRequestHandlerTest {

    private Env env;
    private IOStreams ioStreams;
    private ObjectMapper objectMapper;
    private RequestHandler<TestInput, TestOutput> requestHandler;
    private Runnable runnable;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        env = mock(Env.class);
        ioStreams = mock(IOStreams.class);
        requestHandler = mock(RequestHandler.class);
        objectMapper = mock(ObjectMapper.class);
        runnable = mock(Runnable.class);
    }

    private PingableJsonRequestHandler handler() {
        return new PingableJsonRequestHandler(env, ioStreams, TestInput.class, objectMapper, requestHandler) {
            @Override
            public void ping() {
                runnable.run();
            }
        };
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Instantiation_for_coverage() {
        new PingableJsonRequestHandler(Object.class, mock(RequestHandler.class));
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
        final InOrder order = inOrder(ioStreams, bufferedInput, runnable, requestHandler);
        order.verify(ioStreams).buffer(any(InputStream.class));
        order.verify(bufferedInput).mark(0);
        order.verify(ioStreams).readBytesToString(any(InputStream.class), anyInt());
        order.verify(runnable).run();
        verifyZeroInteractions(requestHandler);
    }

    @Test
    public void Can_forward_a_non_ping_request() throws IOException {

        final ByteArrayInputStream input = mock(ByteArrayInputStream.class);
        final OutputStream output = mock(OutputStream.class);
        final Context context = mock(Context.class);

        final String pingString = someString(1024);
        final BufferedInputStream bufferedInput = mock(BufferedInputStream.class);
        final TestInput testInput = mock(TestInput.class);
        final TestOutput testOutput = mock(TestOutput.class);

        // Given
        given(env.get("PING_STRING")).willReturn(pingString);
        given(ioStreams.buffer(input)).willReturn(bufferedInput);
        given(ioStreams.readBytesToString(bufferedInput, 512)).willReturn(someString());
        given(objectMapper.readValue(bufferedInput, TestInput.class)).willReturn(testInput);
        given(requestHandler.handleRequest(testInput, context)).willReturn(testOutput);

        // When
        handler().handleRequest(input, output, context);

        // Then
        final InOrder order = inOrder(ioStreams, bufferedInput, objectMapper);
        order.verify(ioStreams).buffer(any(InputStream.class));
        order.verify(bufferedInput).mark(0);
        order.verify(ioStreams).readBytesToString(any(InputStream.class), anyInt());
        order.verify(bufferedInput).reset();
        order.verify(objectMapper).writeValue(output, testOutput);
        verifyZeroInteractions(runnable);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Direct_call_for_coverage() {
        final Pingable pingable = new PingableJsonRequestHandler(Object.class, mock(RequestHandler.class));
        pingable.ping();
    }

    private interface TestInput {
    }

    private interface TestOutput {
    }
}