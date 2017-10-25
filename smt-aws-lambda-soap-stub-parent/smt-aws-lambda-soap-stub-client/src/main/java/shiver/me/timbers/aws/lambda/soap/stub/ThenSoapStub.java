package shiver.me.timbers.aws.lambda.soap.stub;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ThenSoapStub {

    private final String verifyingUrl;
    private final Soaps soaps;
    private final Client client;

    public ThenSoapStub(String verifyingUrl) {
        this(
            verifyingUrl,
            new Soaps(
                new Marshals(new DocumentFactory(DocumentBuilderFactory.newInstance()), new JaxbContextFactory()),
                new SoapMessages(new SoapMessageFactory())),
            ClientBuilder.newClient()
        );
    }

    ThenSoapStub(String verifyingUrl, Soaps soaps, Client client) {
        this.verifyingUrl = verifyingUrl;
        this.soaps = soaps;
        this.client = client;
    }

    public RequestShould request(Object request) {
        return new RequestShould(soaps, soaps.marshalToSOAPMessage(request), client.target(verifyingUrl));
    }
}
