package shiver.me.timbers.aws.apigateway.proxy;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_NOT_ACCEPTABLE;

public class JsonProxyRequestHandler<I, O> implements ProxyRequestHandler {

    private final Class<I> type;
    private final ObjectMapper objectMapper;
    private final DeserialisedProxyRequestHandler<I, O> handler;

    public JsonProxyRequestHandler(
        Class<I> type,
        ObjectMapper objectMapper,
        DeserialisedProxyRequestHandler<I, O> handler
    ) {
        this.type = type;
        this.objectMapper = objectMapper;
        this.handler = handler;
    }

    @Override
    public ProxyResponse<String> handleRequest(ProxyRequest<String> request, Context context) {
        try {
            if (notJsonRequest(request)) {
                return new ProxyResponse<>(HTTP_NOT_ACCEPTABLE);
            }
            final I requestBody = objectMapper.readValue(request.getBody(), type);
            final ProxyResponse<O> response = handler.handleRequest(new ProxyRequest<>(request, requestBody), context);
            final String responseBody = objectMapper.writeValueAsString(response.getBody());
            return new ProxyResponse<>(response, responseBody);
        } catch (IOException e) {
            throw new JsonDeserialisationException("Failed to deserialise the JSON body.", e);
        }
    }

    private static boolean notJsonRequest(ProxyRequest<String> request) {
        return !request.getHeaders().get("Content-Type").equals("application/json");
    }
}
