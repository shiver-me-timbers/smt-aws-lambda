package shiver.me.timbers.aws.common;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOStreams {

    public BufferedInputStream buffer(InputStream inputStream) {
        if (inputStream instanceof BufferedInputStream) {
            return (BufferedInputStream) inputStream;
        }
        return new BufferedInputStream(inputStream);
    }

    public String readString(InputStream stream, int byteNum) throws IOException {
        final byte[] buffer = new byte[byteNum];
        stream.read(buffer);
        return new String(buffer);
    }
}
