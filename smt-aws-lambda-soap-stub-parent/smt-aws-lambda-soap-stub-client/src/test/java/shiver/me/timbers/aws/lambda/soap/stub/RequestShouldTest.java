package shiver.me.timbers.aws.lambda.soap.stub;

import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.xml.soap.SOAPMessage;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.matchers.Matchers.hasField;

public class RequestShouldTest {

    @Test
    public void Can_create_a_request_verifier() {

        // Given
        final Soaps soaps = mock(Soaps.class);
        final SOAPMessage requestMessage = mock(SOAPMessage.class);
        final WebTarget target = mock(WebTarget.class);

        // When
        final RequestVerifying actual = new RequestShould(soaps, requestMessage, target).should();

        // Then
        assertThat(actual, hasField("soaps", soaps));
        assertThat(actual, hasField("requestMessage", requestMessage));
        assertThat(actual, hasField("target", target));
    }
}