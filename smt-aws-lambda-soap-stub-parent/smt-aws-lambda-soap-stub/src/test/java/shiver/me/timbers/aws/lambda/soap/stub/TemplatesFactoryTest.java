package shiver.me.timbers.aws.lambda.soap.stub;

import org.junit.Test;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import java.io.InputStream;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class TemplatesFactoryTest {

    @Test
    public void Can_create_a_templates() {

        // When
        final Templates actual = new TemplatesFactory().create(getSystemResourceAsStream("remove-namespaces.xslt"));

        // Then
        assertThat(actual, not(nullValue()));
    }

    @Test
    public void Can_fail_to_create_a_templates() {

        // When
        final Throwable actual = catchThrowable(() -> new TemplatesFactory().create(mock(InputStream.class)));

        // Then
        assertThat(actual, instanceOf(XsltTemplateException.class));
        assertThat(actual.getMessage(), equalTo("Failed to create the XSLT template."));
        assertThat(actual.getCause(), instanceOf(TransformerConfigurationException.class));
    }
}