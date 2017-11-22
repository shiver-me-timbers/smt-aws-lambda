package shiver.me.timbers.aws.json;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class JacksonJsonRequestStreamHandlerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_json_request() throws IOException {

        final ObjectMapper objectMapper = mock(ObjectMapper.class);
        final RequestHandler<TestInput, TestOutput> handler = mock(RequestHandler.class);
        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);
        final Context context = mock(Context.class);

        final TestInput testInput = mock(TestInput.class);
        final TestOutput testOutput = mock(TestOutput.class);

        // Given
        given(objectMapper.readValue(input, TestInput.class)).willReturn(testInput);
        given(handler.handleRequest(testInput, context)).willReturn(testOutput);

        // When
        new JacksonJsonRequestStreamHandler(TestInput.class, objectMapper, handler).handleRequest(input, output, context);

        // Then
        then(objectMapper).should().writeValue(output, testOutput);
    }

    private interface TestInput {
    }

    private interface TestOutput {
    }
}