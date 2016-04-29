package org.bh.tools.net.im.localserver.coms.transmittables;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bh.tools.net.im.core.msg.Body;
import org.bh.tools.net.im.core.msg.Footer;
import org.bh.tools.net.im.core.msg.Header;
import org.bh.tools.net.im.core.msg.Transmittable;
import org.bh.tools.net.im.localserver.coms.transmittables.IntArrayTransmittable.IntArrayBody;
import org.bh.tools.net.im.localserver.coms.transmittables.IntArrayTransmittable.IntArrayHeaderFooter;

/**
 * IntArrayTransmittable, made for BHIM, is copyright Blue Husky Programming ©2016 BH-1-PS <hr>
 *
 * Just raw ints! No header and no footer.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-24 (1.0.0) - Kyli created IntArrayTransmittable
 * @since 2016-03-24
 */
public class IntArrayTransmittable implements Transmittable<IntArrayHeaderFooter, IntArrayBody, IntArrayHeaderFooter> {

    protected IntArrayHeaderFooter _header, _footer;
    protected IntArrayBody _body;

    public IntArrayTransmittable(IntArrayHeaderFooter initHeader, IntArrayHeaderFooter initFooter, IntArrayBody initBody) {
        _header = initHeader;
        _footer = initFooter;
        _body = initBody;
    }

    @Override
    public IntArrayHeaderFooter getHeader() {
        return _header;
    }

    @Override
    public IntArrayBody getBody() {
        return _body;
    }

    @Override
    public IntArrayHeaderFooter getFooter() {
        return _footer;
    }

    @Override
    public long size() {
        long size = 0;
        if (null != _header) {
            size += _header.size();
        }
        if (null != _body) {
            size += _body.size();
        }
        if (null != _footer) {
            size += _footer.size();
        }
        return size;
    }

    @Override
    public byte[] convertToBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            if (null != _header) {
                dos.write(_header.convertToBytes());
            }
            if (null != _body) {
                dos.write(_body.convertToBytes());
            }
            if (null != _footer) {
                dos.write(_footer.convertToBytes());
            }
        } catch (IOException ex) {
            Logger.getLogger(IntArrayTransmittable.class.getName()).log(Level.SEVERE, "Could not convert int[] to byte[]", ex);
            return null;
        }

        return baos.toByteArray();
    }

    private static byte[] intsToBytes(int[] ints) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < ints.length; ++i) {
            try {
                dos.writeInt(ints[i]);
            } catch (IOException ex) {
                Logger.getLogger(IntArrayTransmittable.class.getName()).log(Level.SEVERE, "Could not convert int[] to byte[]", ex);
                return null;
            }
        }

        return baos.toByteArray();

    }

    public static class IntArrayHeaderFooter implements Header, Footer {

        protected int[] _content;

        public IntArrayHeaderFooter(int[] initContent) {
            _content = initContent;
        }

        @Override
        public long size() {
            return _content.length * Integer.BYTES;
        }

        @Override
        public byte[] convertToBytes() {
            return intsToBytes(_content);
        }

    }

    public static class IntArrayBody extends Body<int[]> {

        public IntArrayBody(int[] initContents) {
            super(initContents);
        }

        @Override
        public byte[] convertToBytes() {
            return intsToBytes(super._content);
        }
    }
}
