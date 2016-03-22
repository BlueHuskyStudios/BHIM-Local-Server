package org.bh.tools.net.im.localserver.coms;

import com.floatbackwards.statuscodes.HttpStatus;
import org.bh.tools.net.im.core.msg.Transmittable;

/**
 * TransmissionListener is copyright Blue Husky Programming ©2016 BH-1-PS <hr/>
 *
 * @param <IncomingType> The type of data to listen for
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.1.0 - 2016-03-19 (1.1.0) - Kyli created TransmissionListener
 * @since 2016-03-19
 */
public interface TransmissionListener<IncomingType extends Transmittable> {

    /**
     * Called when any transmission was received, or if a problem occurred while listening for one.
     *
     * @param status   The status from the incoming transmission.
     * @param incoming The incoming transmission.
     * @param problem  Any problem that occurred while listening.
     * @since 1.0.0
     */
    public void transmissionReceived(HttpStatus status, IncomingType incoming, Throwable problem);
}
