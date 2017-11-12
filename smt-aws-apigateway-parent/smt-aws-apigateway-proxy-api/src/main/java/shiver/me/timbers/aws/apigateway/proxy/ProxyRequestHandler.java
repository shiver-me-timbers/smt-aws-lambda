package shiver.me.timbers.aws.apigateway.proxy;

import com.amazonaws.services.lambda.runtime.RequestHandler;

public interface ProxyRequestHandler extends RequestHandler<ProxyRequest<String>, ProxyResponse<String>> {
}
