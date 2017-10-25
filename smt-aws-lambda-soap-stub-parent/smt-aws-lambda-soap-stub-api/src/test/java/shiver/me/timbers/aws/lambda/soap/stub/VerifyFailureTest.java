package shiver.me.timbers.aws.lambda.soap.stub;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class VerifyFailureTest {

    @Test
    public void Can_set_the_verify_failure_message() {

        // Given
        final String expected = someString();
        final VerifyFailure failure = new VerifyFailure();
        failure.setMessage(expected);

        // When
        final String actual = failure.getMessage();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_set_the_verify_failure_error_message() {

        // Given
        final String expected = someString();
        final VerifyFailure failure = new VerifyFailure();
        failure.setErrorMessage(expected);

        // When
        final String actual = failure.getErrorMessage();

        // Then
        assertThat(actual, is(expected));
    }
}