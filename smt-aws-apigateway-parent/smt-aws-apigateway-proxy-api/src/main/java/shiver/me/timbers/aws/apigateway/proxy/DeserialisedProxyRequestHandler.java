package shiver.me.timbers.aws.apigateway.proxy;

import com.amazonaws.services.lambda.runtime.RequestHandler;

public interface DeserialisedProxyRequestHandler<I, O> extends RequestHandler<ProxyRequest<I>, ProxyResponse<O>> {
}
