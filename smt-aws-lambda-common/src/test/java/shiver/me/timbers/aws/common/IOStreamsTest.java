package shiver.me.timbers.aws.common;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someIntegerBetween;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class IOStreamsTest {

    private IOStreams ioStreams;

    @Before
    public void setUp() {
        ioStreams = new IOStreams();
    }

    @Test
    public void Can_buffer_an_input_stream() throws IOException {

        // Given
        final String expected = someString();

        // When
        final BufferedInputStream actual = ioStreams.buffer(new ByteArrayInputStream(expected.getBytes()));

        // Then
        assertThat(actual, instanceOf(BufferedInputStream.class));
        assertThat(IOUtils.toString(actual), equalTo(expected));
    }

    @Test
    public void Will_not_buffer_a_buffered_input_stream() throws IOException {

        // Given
        final BufferedInputStream expected = mock(BufferedInputStream.class);

        // When
        final BufferedInputStream actual = ioStreams.buffer(expected);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_read_some_bytes_from_a_stream() throws IOException {

        final InputStream stream = mock(InputStream.class);
        final int byteNum = someIntegerBetween(1, 1024);
        final String expected = someString(byteNum);

        // Given
        given(stream.read(new byte[byteNum])).will((Answer<Integer>) invocationOnMock -> {
            final byte[] buffer = invocationOnMock.getArgumentAt(0, byte[].class);
            final byte[] bytes = expected.getBytes();
            System.arraycopy(bytes, 0, buffer, 0, bytes.length);
            return bytes.length;
        });

        // When
        final String actual = ioStreams.readString(stream, byteNum);

        // Then
        assertThat(actual, equalTo(expected));
    }
}