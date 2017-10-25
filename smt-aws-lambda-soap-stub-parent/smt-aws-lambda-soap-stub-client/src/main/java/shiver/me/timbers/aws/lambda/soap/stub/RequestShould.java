package shiver.me.timbers.aws.lambda.soap.stub;

import javax.ws.rs.client.WebTarget;
import javax.xml.soap.SOAPMessage;

public class RequestShould {

    private final Soaps soaps;
    private final SOAPMessage requestMessage;
    private final WebTarget target;

    RequestShould(Soaps soaps, SOAPMessage requestMessage, WebTarget target) {
        this.soaps = soaps;
        this.requestMessage = requestMessage;
        this.target = target;
    }

    public RequestVerifying should() {
        return new RequestVerifying(soaps, requestMessage, target);
    }
}
