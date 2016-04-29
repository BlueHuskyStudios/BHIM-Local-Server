package org.bh.tools.net.im.localserver.coms;

import com.floatbackwards.statuscodes.HttpStatus;
import org.bh.tools.net.im.core.msg.Transmittable;

/**
 * TransmissionSentResponseListener is copyright Blue Husky Programming ©2016 BH-1-PS <hr>
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-19 (1.0.0) - Kyli created TransmissionSentResponseListener
 * @since 2016-03-19
 */
public interface TransmissionSentResponseListener {

    public void transmissionSent(HttpStatus responseCode, Transmittable response);
}
