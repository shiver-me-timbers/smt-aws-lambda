package shiver.me.timbers.aws.apigateway.proxy;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class JsonProxyRequestHandlerTest {

    private ObjectMapper objectMapper;
    private DeserialisedProxyRequestHandler<SomeRequestType, SomeResponseType> deserialisedHandler;
    private JsonProxyRequestHandler<SomeRequestType, SomeResponseType> handler;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        deserialisedHandler = mock(DeserialisedProxyRequestHandler.class);
        handler = new JsonProxyRequestHandler<>(SomeRequestType.class, objectMapper, deserialisedHandler);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_deserialise_a_json_request() throws IOException {

        final ProxyRequest<String> request = mock(ProxyRequest.class);
        final Context context = mock(Context.class);

        final String resource = someString();
        final String path = someString();
        final String httpMethod = someString();
        final Map<String, String> requestHeaders = mock(Map.class);
        final Map<String, String> queryStringParameters = mock(Map.class);
        final Map<String, String> pathParameters = mock(Map.class);
        final Map<String, String> stageVariables = mock(Map.class);
        final Map<String, Object> requestContext = mock(Map.class);
        final boolean requestIsBase64Encoded = someBoolean();
        final String requestBody = someString();
        final SomeRequestType deserialisedRequestBody = mock(SomeRequestType.class);
        final ArgumentCaptor<ProxyRequest<SomeRequestType>> captor =
            (ArgumentCaptor) ArgumentCaptor.forClass(ProxyRequest.class);
        final ProxyResponse<SomeResponseType> response = mock(ProxyResponse.class);
        final int statusCode = someInteger();
        final Map<String, String> responseHeaders = mock(Map.class);
        final Boolean responseIsBase64Encoded = someBoolean();
        final SomeResponseType deserialisedResponseBody = mock(SomeResponseType.class);
        final String responseBody = someString();


        // Given
        given(request.getResource()).willReturn(resource);
        given(request.getPath()).willReturn(path);
        given(request.getHttpMethod()).willReturn(httpMethod);
        given(request.getHeaders()).willReturn(requestHeaders);
        given(request.getQueryStringParameters()).willReturn(queryStringParameters);
        given(request.getPathParameters()).willReturn(pathParameters);
        given(request.getStageVariables()).willReturn(stageVariables);
        given(request.getRequestContext()).willReturn(requestContext);
        given(request.isBase64Encoded()).willReturn(requestIsBase64Encoded);
        given(request.getBody()).willReturn(requestBody);
        given(objectMapper.readValue(requestBody, SomeRequestType.class)).willReturn(deserialisedRequestBody);
        given(deserialisedHandler.handleRequest(captor.capture(), eq(context))).willReturn(response);
        given(response.getStatusCode()).willReturn(statusCode);
        given(response.getHeaders()).willReturn(responseHeaders);
        given(response.isBase64Encoded()).willReturn(responseIsBase64Encoded);
        given(response.getBody()).willReturn(deserialisedResponseBody);
        given(objectMapper.writeValueAsString(deserialisedResponseBody)).willReturn(responseBody);

        // When
        final ProxyResponse actual = handler.handleRequest(request, context);

        // Then
        final ProxyRequest<SomeRequestType> desriealisedRequest = captor.getValue();
        assertThat(desriealisedRequest.getResource(), is(resource));
        assertThat(desriealisedRequest.getPath(), is(path));
        assertThat(desriealisedRequest.getHttpMethod(), is(httpMethod));
        assertThat(desriealisedRequest.getHeaders(), is(requestHeaders));
        assertThat(desriealisedRequest.getQueryStringParameters(), is(queryStringParameters));
        assertThat(desriealisedRequest.getPathParameters(), is(pathParameters));
        assertThat(desriealisedRequest.getStageVariables(), is(stageVariables));
        assertThat(desriealisedRequest.getRequestContext(), is(requestContext));
        assertThat(desriealisedRequest.isBase64Encoded(), is(requestIsBase64Encoded));
        assertThat(desriealisedRequest.getBody(), is(deserialisedRequestBody));
        assertThat(actual.getStatusCode(), is(statusCode));
        assertThat(actual.getHeaders(), is(responseHeaders));
        assertThat(actual.isBase64Encoded(), is(responseIsBase64Encoded));
        assertThat(actual.getBody(), is(responseBody));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_deserialise_a_json_request() throws IOException {

        final ProxyRequest<String> request = mock(ProxyRequest.class);
        final Context context = mock(Context.class);

        final String requestBody = someString();
        final IOException exception = mock(IOException.class);

        // Given
        given(request.getBody()).willReturn(requestBody);
        given(objectMapper.readValue(requestBody, SomeRequestType.class)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> handler.handleRequest(request, context));

        // Then
        assertThat(actual, instanceOf(JsonDeserialisationException.class));
        assertThat(actual.getMessage(), equalTo("Failed to deserialise the JSON body."));
        assertThat(actual.getCause(), is(exception));
    }

    private static class SomeRequestType {
    }

    private static class SomeResponseType {
    }
}