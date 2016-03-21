package org.bh.tools.net.im.localserver.coms;

import org.bh.tools.net.im.core.msg.Recipient;

/**
 * ChatSender is copyright Blue Husky Programming ©2016 BH-1-PS <hr/>
 *
 * Sends chat messages.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-20 (1.0.0) - Kyli created ChatSender
 * @since 2016-03-20
 */
public class ChatSender {

    /**
     * Sends the given text to the given recipient, then calls the given listener. All parameters are necessary.
     *
     * @param textToSend       The text to send.
     * @param recipient        The person to whom to send the data.
     * @param responseListener The block to be called when a response is available.
     */
    public static void sendText(String textToSend, Recipient recipient,
            TransmissionSentResponseListener responseListener) {
        TransmissionSender.sendTransmission(new StringTransmittable(textToSend), recipient, responseListener);
    }
}
