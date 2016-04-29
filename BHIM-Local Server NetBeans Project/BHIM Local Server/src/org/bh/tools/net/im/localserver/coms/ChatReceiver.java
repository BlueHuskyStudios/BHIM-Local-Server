package org.bh.tools.net.im.localserver.coms;

import org.bh.tools.net.im.localserver.coms.transmittables.StringTransmittable;
import org.bh.tools.net.im.core.msg.Sender;

/**
 * ChatReceiver is copyright Blue Husky Programming ©2016 BH-1-PS <hr>
 *
 * Receives chat messages.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-20 (1.0.0) - Kyli created ChatReceiver
 * @since 2016-03-20
 */
public class ChatReceiver {

    /**
     * Listens for text from the given sender, then calls the given listener. All parameters are necessary.
     *
     * @param sender           The person from whom to listen for text.
     * @param responseListener The block to be called when a message is heard.
     */
    public static void listenForText(Sender sender, TransmissionListener<StringTransmittable> responseListener) {
        TransmissionReceiver.listenForTransmissions(sender, responseListener);
    }
}
