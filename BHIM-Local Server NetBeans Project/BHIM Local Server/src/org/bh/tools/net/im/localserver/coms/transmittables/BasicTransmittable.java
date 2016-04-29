package org.bh.tools.net.im.localserver.coms.transmittables;

import org.bh.tools.net.im.core.msg.Body;
import org.bh.tools.net.im.core.msg.Footer;
import org.bh.tools.net.im.core.msg.Header;
import org.bh.tools.net.im.core.msg.Transmittable;
import org.bh.tools.net.im.localserver.coms.transmittables.BasicTransmittable.BasicBody;
import org.bh.tools.net.im.localserver.coms.transmittables.BasicTransmittable.NullHeaderFooter;

/**
 * BasicTransmittable, made for BHIM, is copyright Blue Husky Programming ©2016 BH-1-PS <hr>
 *
 * Just raw bytes! No header and no footer.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-18 (1.0.0) - Kyli created BasicTransmittable
 * @since 2016-03-18
 */
public class BasicTransmittable implements Transmittable<NullHeaderFooter, BasicBody, NullHeaderFooter> {

    private final BasicBody body;

    public BasicTransmittable(byte[] contents) {
        body = new BasicBody(contents);
    }

    @Override
    public NullHeaderFooter getHeader() {
        return NullHeaderFooter.NULL;
    }

    @Override
    public BasicBody getBody() {
        return body;
    }

    @Override
    public NullHeaderFooter getFooter() {
        return NullHeaderFooter.NULL;
    }

    @Override
    public long size() {
        return body.size();
    }

    @Override
    public byte[] convertToBytes() {
        return body.convertToBytes();
    }

    public static class NullHeaderFooter implements Header, Footer {

        public static final NullHeaderFooter NULL = new NullHeaderFooter();
        private static final byte[] NULL_BYTES = new byte[0];

        private NullHeaderFooter() {
        }

        @Override
        public long size() {
            return 0;
        }

        @Override
        public byte[] convertToBytes() {
            return NULL_BYTES;
        }
    }

    public static class BasicBody extends Body<byte[]> {

        public BasicBody(byte[] content) {
            super(content);
        }

        @Override
        public byte[] convertToBytes() {
            return _content;
        }
    }
}
