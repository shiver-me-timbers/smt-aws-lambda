package shiver.me.timbers.aws.lambda.soap.stub;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.soap.SOAPMessage;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class RequestVerifying {

    private final Soaps soaps;
    private final SOAPMessage requestMessage;
    private final WebTarget target;

    RequestVerifying(Soaps soaps, SOAPMessage requestMessage, WebTarget target) {
        this.soaps = soaps;
        this.requestMessage = requestMessage;
        this.target = target;
    }

    public void beCalled() {
        final Response response = target.request(APPLICATION_JSON_TYPE).put(
            entity(new Verifying(soaps.toXmlString(requestMessage)), APPLICATION_JSON_TYPE)
        );
        if (response.getStatus() >= 400) {
            throw new VerifyRequestError(response.readEntity(VerifyFailure.class).getMessage());
        }
    }
}
