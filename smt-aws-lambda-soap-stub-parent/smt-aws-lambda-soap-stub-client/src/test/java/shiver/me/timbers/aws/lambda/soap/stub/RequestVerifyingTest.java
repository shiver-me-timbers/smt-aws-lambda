package shiver.me.timbers.aws.lambda.soap.stub;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.soap.SOAPMessage;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Invocation.Builder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someIntegerBetween;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class RequestVerifyingTest {

    private Soaps soaps;
    private SOAPMessage requestMessage;
    private WebTarget target;
    private RequestVerifying verifying;

    @Before
    public void setUp() {
        soaps = mock(Soaps.class);
        requestMessage = mock(SOAPMessage.class);
        target = mock(WebTarget.class);
        verifying = new RequestVerifying(soaps, requestMessage, target);
    }

    @Test
    public void Can_verify_that_the_request_has_been_called() {

        final String request = someString();
        final Builder builder = mock(Builder.class);
        final Response response = mock(Response.class);

        // Given
        given(soaps.toXmlString(requestMessage)).willReturn(request);
        given(target.request(APPLICATION_JSON_TYPE)).willReturn(builder);
        given(builder.put(entity(new Verifying(request), APPLICATION_JSON_TYPE))).willReturn(response);
        given(response.getStatus()).willReturn(OK.getStatusCode());

        // When
        verifying.beCalled();

        // Then
        then(response).should().getStatus();
    }

    @Test
    public void Can_fail_to_verify_that_the_request_has_been_called() {

        final String request = someString();
        final Builder builder = mock(Builder.class);
        final Response response = mock(Response.class);
        final VerifyFailure failure = mock(VerifyFailure.class);
        final String message = someString();

        // Given
        given(soaps.toXmlString(requestMessage)).willReturn(request);
        given(target.request(APPLICATION_JSON_TYPE)).willReturn(builder);
        given(builder.put(entity(new Verifying(request), APPLICATION_JSON_TYPE))).willReturn(response);
        given(response.getStatus()).willReturn(someIntegerBetween(400, 600));
        given(response.readEntity(VerifyFailure.class)).willReturn(failure);
        given(failure.getMessage()).willReturn(message);

        // When
        final Throwable actual = catchThrowable(() -> verifying.beCalled());

        // Then
        assertThat(actual, instanceOf(VerifyRequestError.class));
        assertThat(actual.getMessage(), is(message));
    }
}