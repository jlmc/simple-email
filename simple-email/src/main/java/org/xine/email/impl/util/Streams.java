package org.xine.email.impl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The Class Streams.
 */
public class Streams {

    /** The Constant BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 0x1000;

    /**
     * To byte array.
     * @param is
     *            the is
     * @return the byte[]
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static byte[] toByteArray(final InputStream is) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        copy(is, os);
        return os.toByteArray();
    }

    /**
     * Inspired by Guava's ByteStreams copy.
     * @param is
     *            the is
     * @param os
     *            the os
     * @return the long
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static long copy(final InputStream is, final OutputStream os) throws IOException {
        final byte[] buf = new byte[BUFFER_SIZE];
        long total = 0;
        while (true) {
            final int i = is.read(buf);
            if (i == -1) {
                break;
            }
            os.write(buf, 0, i);
            total += i;
        }
        return total;
    }
}
