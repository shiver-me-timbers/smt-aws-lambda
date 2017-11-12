package shiver.me.timbers.aws.apigateway.proxy;

import java.util.Map;

/**
 * This is a Java representation of the AWS API Gateway Proxy response.
 *
 * @see <a href="http://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html#api-gateway-simple-proxy-for-lambda-output-format">Output Format of a Lambda Function for Proxy Integration</a>
 */
public class ProxyResponse<T> {

    private int statusCode;
    private Map<String, String> headers;
    private boolean isBase64Encoded;
    private T body;

    public ProxyResponse() {
    }

    public ProxyResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public ProxyResponse(ProxyResponse<?> response, T body) {
        this.statusCode = response.getStatusCode();
        this.headers = response.getHeaders();
        this.isBase64Encoded = response.isBase64Encoded();
        this.body = body;
    }

    /**
     * @return the HTTP status code for the API Gateway resource response (200, 302, 400, 500...).
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode - the HTTP status code for the API Gateway resource response (200, 302, 400, 500...).
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the headers to be sent back with the API Gateway resource HTTP response.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers - the headers to be sent back with the API Gateway resource HTTP response.
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
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
     * @return the body of the HTTP response.
     */
    public T getBody() {
        return body;
    }

    /**
     * @param body - the body of the HTTP response.
     */
    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProxyResponse that = (ProxyResponse) o;

        if (statusCode != that.statusCode) return false;
        if (isBase64Encoded != that.isBase64Encoded) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        return body != null ? body.equals(that.body) : that.body == null;
    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (isBase64Encoded ? 1 : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
