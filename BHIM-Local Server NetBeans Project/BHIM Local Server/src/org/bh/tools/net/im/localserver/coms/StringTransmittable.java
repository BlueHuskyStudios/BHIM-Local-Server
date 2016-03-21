package org.bh.tools.net.im.localserver.coms;

import org.bh.tools.net.im.core.util.TextUtils;


/**
 * StringTransmittable, made for BHIM, is copyright Blue Husky Programming ©2016 BH-1-PS <hr/>
 *
 * A very basic transmittable, which sends a string.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-18 (1.0.0) - Kyli created StringTransmittable
 * @since 2016-03-18
 */
public class StringTransmittable extends BasicTransmittable {

    public StringTransmittable(String contents) {
        super(contents.getBytes(TextUtils.DEFAULT_CHARSET));
    }
}
