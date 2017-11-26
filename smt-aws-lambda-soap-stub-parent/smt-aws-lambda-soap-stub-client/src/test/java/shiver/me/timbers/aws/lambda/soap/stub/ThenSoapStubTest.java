package shiver.me.timbers.aws.lambda.soap.stub;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.xml.soap.SOAPMessage;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.matchers.Matchers.hasField;

public class ThenSoapStubTest {

    @Test
    public void Can_create_a_request_should() {

        final String verifyingUrl = someString();
        final Soaps soaps = mock(Soaps.class);
        final Client client = mock(Client.class);
        final Object request = someThing();

        final WebTarget target = mock(WebTarget.class);
        final SOAPMessage requestMessage = mock(SOAPMessage.class);

        // Given
        given(soaps.marshalToSOAPMessage(request)).willReturn(requestMessage);
        given(client.target(verifyingUrl)).willReturn(target);

        // When
        final RequestShould actual = new ThenSoapStub(verifyingUrl, soaps, client).request(request);

        // Then
        assertThat(actual, hasField("soaps", soaps));
        assertThat(actual, hasField("requestMessage", requestMessage));
        assertThat(actual, hasField("target", target));
    }
}