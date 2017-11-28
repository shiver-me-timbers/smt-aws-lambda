package shiver.me.timbers.aws.lambda.soap.stub;

import org.junit.Test;

import javax.xml.transform.Templates;
import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class NamespaceTemplatesFactoryTest {

    @Test
    public void Can_create_a_namespace_templates() {

        final TemplatesFactory templatesFactory = mock(TemplatesFactory.class);
        final InputStream stream = mock(InputStream.class);

        final Templates expected = mock(Templates.class);

        // Given
        given(templatesFactory.create(stream)).willReturn(expected);

        // When
        final Templates actual = new NamespaceTemplatesFactory(templatesFactory, stream).create();

        // Then
        assertThat(actual, is(expected));
    }
}