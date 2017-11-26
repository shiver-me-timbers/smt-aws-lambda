package shiver.me.timbers.aws.lambda.soap.stub;

import shiver.me.timbers.aws.apigateway.proxy.ProxyResponse;

import static java.util.Collections.singletonMap;

public class StubbingProxyResponse extends ProxyResponse<String> {

    public StubbingProxyResponse(String message) {
        this(200, message);
    }

    public StubbingProxyResponse(int status, String message) {
        super(status);
        setHeaders(singletonMap("Content-Type", "application/json"));
        setBody(message);
    }
}
