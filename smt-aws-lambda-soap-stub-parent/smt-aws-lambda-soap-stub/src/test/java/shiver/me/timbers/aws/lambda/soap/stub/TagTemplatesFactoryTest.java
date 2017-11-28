package shiver.me.timbers.aws.lambda.soap.stub;

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.aws.common.IOStreams;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class TagTemplatesFactoryTest {

    private String tag1;
    private String tag2;
    private String tag3;
    private List<String> tags;
    private InputStream stream;
    private IOStreams ioStreams;
    private TemplatesFactory templatesFactory;
    private TagTemplatesFactory factory;

    @Before
    public void setUp() {
        tag1 = someAlphanumericString(3);
        tag2 = someAlphanumericString(5);
        tag3 = someAlphanumericString(8);
        tags = asList(tag1, tag2, tag3);
        stream = mock(InputStream.class);
        ioStreams = mock(IOStreams.class);
        templatesFactory = mock(TemplatesFactory.class);
        factory = new TagTemplatesFactory(tags, stream, ioStreams, templatesFactory);
    }

    @Test
    public void Can_create_tag_templates() throws IOException {

        final String prefix = someString();
        final String suffix = someString();
        final String xslt = prefix + "{TAG_NAME}" + suffix;
        final InputStream xsltStream1 = mock(InputStream.class);
        final InputStream xsltStream2 = mock(InputStream.class);
        final InputStream xsltStream3 = mock(InputStream.class);
        final Templates templates1 = mock(Templates.class);
        final Templates templates2 = mock(Templates.class);
        final Templates templates3 = mock(Templates.class);

        // Given
        given(ioStreams.toString(stream)).willReturn(xslt);
        given(ioStreams.toStream(prefix + tag1 + suffix)).willReturn(xsltStream1);
        given(ioStreams.toStream(prefix + tag2 + suffix)).willReturn(xsltStream2);
        given(ioStreams.toStream(prefix + tag3 + suffix)).willReturn(xsltStream3);
        given(templatesFactory.create(xsltStream1)).willReturn(templates1);
        given(templatesFactory.create(xsltStream2)).willReturn(templates2);
        given(templatesFactory.create(xsltStream3)).willReturn(templates3);

        // When
        final List<Templates> actual = factory.createAll();

        // Then
        assertThat(actual, contains(templates1, templates2, templates3));
    }

    @Test
    public void Can_fail_to_create_tag_templates() throws IOException {

        final IOException exception = mock(IOException.class);

        // Given
        given(ioStreams.toString(stream)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> factory.createAll());

        // Then
        assertThat(actual, instanceOf(XsltTemplateException.class));
        assertThat(actual.getMessage(), equalTo("Failed to create the XSLT template."));
        assertThat(actual.getCause(), is(exception));
    }
}