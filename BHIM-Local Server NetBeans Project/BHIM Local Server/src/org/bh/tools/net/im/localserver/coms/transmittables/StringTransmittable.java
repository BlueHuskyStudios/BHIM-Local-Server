package org.bh.tools.net.im.localserver.coms.transmittables;

import org.bh.tools.net.im.core.msg.Body;
import org.bh.tools.net.im.core.msg.Transmittable;
import org.bh.tools.net.im.localserver.coms.transmittables.BasicTransmittable.NullHeaderFooter;
import org.bh.tools.net.im.localserver.coms.transmittables.StringTransmittable.StringBody;


/**
 * StringTransmittable, made for BHIM, is copyright Blue Husky Programming ©2016 BH-1-PS <hr/>
 *
 * A very basic transmittable, which sends a string.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-18 (1.0.0) - Kyli created StringTransmittable
 * @since 2016-03-18
 */
public class StringTransmittable implements Transmittable<NullHeaderFooter, StringBody, NullHeaderFooter> {

    private final StringBody body;

    public StringTransmittable(CharSequence contents) {
        body = new StringBody(contents);
    }

    @Override
    public NullHeaderFooter getHeader() {
        return NullHeaderFooter.NULL;
    }

    @Override
    public StringBody getBody() {
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

    public static class StringBody extends Body<CharSequence> {

        public StringBody(CharSequence content) {
            super(content);
        }

        @Override
        public byte[] convertToBytes() {
            return _content == null ? new byte[0] : _content.toString().getBytes();
        }

        @Override
        public String toString() {
            return _content == null ? null : _content.toString();
        }

        @Override
        public int length() {
            return _content.length();
        }

        @Override
        public char charAt(int index) {
            return _content == null ? 0 : _content.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return _content == null ? null : _content.subSequence(start, end);
        }
    }
}
