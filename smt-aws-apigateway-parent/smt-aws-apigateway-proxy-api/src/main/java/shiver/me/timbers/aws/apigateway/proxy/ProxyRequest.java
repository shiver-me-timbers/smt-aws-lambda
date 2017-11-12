package shiver.me.timbers.aws.apigateway.proxy;

import java.util.Map;

/**
 * This is a Java representation of the AWS API Gateway Proxy request.
 *
 * @see <a href="http://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html#api-gateway-simple-proxy-for-lambda-input-format">Input Format of a Lambda Function for Proxy Integration</a>
 */
public class ProxyRequest<T> {

    private String resource;
    private String path;
    private String httpMethod;
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;
    private Map<String, Object> requestContext;
    private boolean isBase64Encoded;
    private T body;

    public ProxyRequest() {
    }

    public ProxyRequest(ProxyRequest<?> request, T body) {
        this.resource = request.getResource();
        this.path = request.getPath();
        this.httpMethod = request.getHttpMethod();
        this.headers = request.getHeaders();
        this.queryStringParameters = request.getQueryStringParameters();
        this.pathParameters = request.getPathParameters();
        this.stageVariables = request.getStageVariables();
        this.requestContext = request.getRequestContext();
        this.isBase64Encoded = request.isBase64Encoded();
        this.body = body;
    }

    /**
     * @return the path of the API Gateway resource that was called.
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource - the name of the API Gateway resource that was called.
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * @return the path under the API Gateway resource that was called.
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path - the path under the API Gateway resource that was called.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the HTTP method (POST,GET,PUT,DELETE...) that was used in the API Gateway resource request.
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * @param httpMethod - the HTTP method (POST,GET,PUT,DELETE...) that was used in the API Gateway resource request.
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * @return the headers sent through with the API Gateway resource HTTP request.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers - the headers sent through with the API Gateway resource HTTP request.
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return the query string parameters sent through with the API Gateway resource HTTP request.
     */
    public Map<String, String> getQueryStringParameters() {
        return queryStringParameters;
    }

    /**
     * @param queryStringParameters - the query string parameters sent through with the API Gateway resource HTTP
     *                              request.
     */
    public void setQueryStringParameters(Map<String, String> queryStringParameters) {
        this.queryStringParameters = queryStringParameters;
    }

    /**
     * @return the path variables (/resource/{id}) that were contained in the path under the API Gateway resource.
     */
    public Map<String, String> getPathParameters() {
        return pathParameters;
    }

    /**
     * @param pathParameters - the path variables (/resource/{id}) that were contained in the path under the API
     *                       Gateway resource.
     */
    public void setPathParameters(Map<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * @return the stage variables that were configured for this API Gateway resource.
     */
    public Map<String, String> getStageVariables() {
        return stageVariables;
    }

    /**
     * @param stageVariables - the stage variables that were configured for this API Gateway resource.
     */
    public void setStageVariables(Map<String, String> stageVariables) {
        this.stageVariables = stageVariables;
    }

    /**
     * @return the context for this API Gateway resource request. It is a {@link Map} because the context can differ
     * greatly depending on the type of request.
     */
    public Map<String, Object> getRequestContext() {
        return requestContext;
    }

    /**
     * @param requestContext - the context for this API Gateway resource request. It is a {@link Map} because the
     *                       context can differ greatly depending on the type of request.
     */
    public void setRequestContext(Map<String, Object> requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * @return {code true} if the body has been Base64 encoded, otherwise false.
     */
    public boolean isBase64Encoded() {
        return isBase64Encoded;
    }

    /**
     * @param base64Encoded - {code true} if the body has been Base64 encoded, otherwise false.
     */
    public void setBase64Encoded(boolean base64Encoded) {
        isBase64Encoded = base64Encoded;
    }

    /**
     * @return the body of the HTTP request.
     */
    public T getBody() {
        return body;
    }

    /**
     * @param body - the body of the HTTP request.
     */
    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProxyRequest that = (ProxyRequest) o;

        if (isBase64Encoded != that.isBase64Encoded) return false;
        if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (httpMethod != null ? !httpMethod.equals(that.httpMethod) : that.httpMethod != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (queryStringParameters != null ? !queryStringParameters.equals(that.queryStringParameters) : that.queryStringParameters != null)
            return false;
        if (pathParameters != null ? !pathParameters.equals(that.pathParameters) : that.pathParameters != null)
            return false;
        if (stageVariables != null ? !stageVariables.equals(that.stageVariables) : that.stageVariables != null)
            return false;
        if (requestContext != null ? !requestContext.equals(that.requestContext) : that.requestContext != null)
            return false;
        return body != null ? body.equals(that.body) : that.body == null;
    }

    @Override
    public int hashCode() {
        int result = resource != null ? resource.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (httpMethod != null ? httpMethod.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (queryStringParameters != null ? queryStringParameters.hashCode() : 0);
        result = 31 * result + (pathParameters != null ? pathParameters.hashCode() : 0);
        result = 31 * result + (stageVariables != null ? stageVariables.hashCode() : 0);
        result = 31 * result + (requestContext != null ? requestContext.hashCode() : 0);
        result = 31 * result + (isBase64Encoded ? 1 : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
