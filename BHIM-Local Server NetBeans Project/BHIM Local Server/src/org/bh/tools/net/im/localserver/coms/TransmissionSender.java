package org.bh.tools.net.im.localserver.coms;

import org.bh.tools.net.im.localserver.coms.transmittables.BasicTransmittable;
import com.floatbackwards.statuscodes.HttpStatus;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Long.max;
import java.net.Socket;
import java.util.logging.Level;
import org.bh.tools.net.im.core.msg.Destination;
import org.bh.tools.net.im.core.msg.Transmittable;
import org.bh.tools.net.im.core.util.LoggingUtils;

/**
 * TransmissionSender is copyright Blue Husky Programming ©2016 BH-1-PS <hr>
 *
 * Sends a transmission.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-19 (1.0.0) - Kyli created TransmissionSender
 * @since 2016-03-19
 */
public class TransmissionSender {

    /**
     * Sends the given data to the given destination, and calls the given response listener with the response.
     *
     * <p>
     * If the given transmittable is null or has a negative size, or if some IOException occurs while attempting to
     * start the transmission, the given response listener is called immediately (with {@link HttpStatus#BadRequest} and
     * {@code null}) and {@code false} is returned.</p>
     *
     * @param outgoing         The data to send.
     * @param destination      Where to send the data.
     * @param responseListener The block to be called when a response is available.
     * @return {@code true} iff the data was successfully sent, whether or not the server received it. If there is a
     *         transmission problem, it will be reflected in an HTTP Status Code passed to {@code responseListener}.
     */
    public static boolean sendTransmission(Transmittable outgoing, Destination destination, TransmissionSentResponseListener responseListener) {
        if (null == responseListener) {
            LoggingUtils.BACKGROUND.severe("sendTransmission requires a response listener!");
            return false;
        }
        Socket socket;
        { // sending data
            long outLength = outgoing.size();
            int outLengthInt = (int) max(Integer.MAX_VALUE, outLength);

            if (outLength < 0) {
                responseListener.transmissionSent(HttpStatus.BadRequest, null);
                return false;
            }

            DataOutputStream outStream = null;

            try {
                // TODO: Fill in local info?
                socket = new Socket(destination.getAddress(), destination.getPort());

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                dos.writeInt(outLengthInt);
                if (outLength > 0) {
                    dos.write(outgoing.convertToBytes(), 0, outLengthInt);
                }
            } catch (Exception ex) {
                LoggingUtils.BACKGROUND.log(Level.SEVERE, "Failed to send data!", ex);
                return false;
            } finally {
                if (null != outStream) {
                    try {
                        outStream.close();
                    } catch (IOException ex) {
                        LoggingUtils.BACKGROUND.log(Level.SEVERE, "Could not close output stream!", ex);
                        responseListener.transmissionSent(HttpStatus.BadRequest, null);
                        return false;
                    }
                }
            }
        }
        { // listening for response
            {
                DataInputStream inStream = null;
                try {
                    inStream = new DataInputStream(socket.getInputStream());
                    int inLength = inStream.readInt();
                    byte[] data = new byte[inLength];
                    if (inLength > 0) {
                        inStream.readFully(data);
                    }
                    BasicTransmittable incoming = new BasicTransmittable(data);
                    responseListener.transmissionSent(HttpStatus.OK, incoming);
                } catch (Exception ex) {
                    LoggingUtils.BACKGROUND.log(Level.SEVERE, "Failed to read response!", ex);
                    return false;
                } finally {
                    if (null != inStream) {
                        try {
                            inStream.close();
                        } catch (IOException ex) {
                            LoggingUtils.BACKGROUND.log(Level.SEVERE, "Could not close input stream!", ex);
                            responseListener.transmissionSent(HttpStatus.BadRequest, null);
                            return false;
                        }
                    }
                }
            }
            LoggingUtils.BACKGROUND.fine("Successfully sent data.");
            return true;
        }
    }
}
