package shiver.me.timbers.aws.pingable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.aws.common.Env;
import shiver.me.timbers.aws.common.IOStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class ITPingableRequestStreamHandler {

    private Env env;
    private EchoRequestStreamHandler streamHandler;
    private PingableRequestStreamHandler handler;

    @Before
    public void setUp() {
        env = mock(Env.class);
        streamHandler = new EchoRequestStreamHandler();
        handler = new PingableRequestStreamHandler(env, new IOStreams(), streamHandler);
    }

    @Test
    public void Can_respond_successfully_to_a_ping_request() throws IOException {

        final String pingString = someString(1, 256);
        final String input = pingString + someString();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Given
        given(env.get("PING_STRING")).willReturn(pingString);

        // When
        handler.handleRequest(new ByteArrayInputStream(input.getBytes()), output, mock(Context.class));

        // Then
        assertThat(output.toString(), isEmptyString());
    }

    @Test
    public void Can_forward_a_non_ping_request() throws IOException {

        final String pingString = someString(1, 256);
        final String input = someString();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Given
        given(env.get("PING_STRING")).willReturn(pingString);

        // When
        handler.handleRequest(new ByteArrayInputStream(input.getBytes()), output, mock(Context.class));

        // Then
        assertThat(output.toString(), equalTo(input));
    }

    private class EchoRequestStreamHandler implements RequestStreamHandler {
        @Override
        public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
            IOUtils.copy(input, output);
        }
    }
}