package shiver.me.timbers.aws.lambda.soap.stub;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class VerifyingTest {

    @Test
    public void Can_get_the_verifying_request() {

        // Given
        final String expected = someString();

        // When
        final String actual = new Verifying(expected).getRequest();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_set_the_verifying_request() {

        // Given
        final String expected = someString();
        final Verifying verifying = new Verifying();
        verifying.setRequest(expected);

        // When
        final String actual = verifying.getRequest();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Verifying_has_equality() {
        EqualsVerifier.forClass(Verifying.class).usingGetClass().suppress(NONFINAL_FIELDS).verify();
    }
}