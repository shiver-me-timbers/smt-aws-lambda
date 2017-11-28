package shiver.me.timbers.aws.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class IOStreams {

    public BufferedInputStream buffer(InputStream inputStream) {
        if (inputStream instanceof BufferedInputStream) {
            return (BufferedInputStream) inputStream;
        }
        return new BufferedInputStream(inputStream);
    }

    public String readBytesToString(InputStream stream, int byteNum) throws IOException {
        final byte[] buffer = new byte[byteNum];
        stream.read(buffer);
        return new String(buffer);
    }

    public String toString(InputStream stream) throws IOException {
        try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(stream))) {
            return buffer.lines().collect(Collectors.joining(""));
        }
    }

    public InputStream toStream(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }
}
