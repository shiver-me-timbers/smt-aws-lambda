package shiver.me.timbers.aws.lambda.soap.stub;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubbingProxyResponseTest {

    @Test
    public void Can_create_a_stubbing_proxy_response() {

        // Given
        final String expected = someString();

        // When
        final StubbingProxyResponse actual = new StubbingProxyResponse(expected);

        // Then
        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getHeaders(), (Matcher) hasEntry("Content-Type", "application/json"));
        assertThat(actual.getBody(), is(expected));
    }
}