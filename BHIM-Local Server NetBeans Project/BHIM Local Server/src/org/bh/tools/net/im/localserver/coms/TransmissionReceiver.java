package org.bh.tools.net.im.localserver.coms;

import com.floatbackwards.statuscodes.HttpStatus;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocketFactory;
import org.bh.tools.net.im.core.msg.Source;
import org.bh.tools.net.im.core.msg.Transmittable;
import org.bh.tools.net.im.core.util.LoggingUtils;
import org.bh.tools.net.im.localserver.coms.transmittables.StringTransmittable;

/**
 * TransmissionReceiver is copyright Blue Husky Programming ©2016 BH-1-PS <hr/>
 *
 * Receives transmissions from a specific source. Adapted from
 * http://students.engr.scu.edu/~nzooleh/COEN146S05/lab3/WebServer.java
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-19 (1.0.0) - Kyli created TransmissionReceiver
 * @since 2016-03-19
 */
public class TransmissionReceiver {


    /**
     * Listens for data from the given source, and calls the given response listener with the response.
     *
     * <p>
     * If the given source is null, or if some Exception occurs while attempting to start listening, the given response
     * listener is called immediately (with {@link HttpStatus#BadRequest} and {@code null}), and {@code false} is
     * returned.</p>
     *
     * @param source           [Required] Where to send the data.
     * @param responseListener [Required] The block to be called when a response is available.
     * @return {@code true} iff the data was successfully sent, whether or not the server received it. If there is a
     *         transmission problem, it will be reflected in an HTTP Status Code passed to {@code responseListener}.
     */
    public static boolean listenForTransmissions(final Source source, final TransmissionListener responseListener) {
        return listenForTransmissions(source, responseListener, null);
    }

    /**
     * Listens for data from the given source, and calls the given response listener with the response.
     *
     * <p>
     * If the given transmittable is null or has a negative size, or if some IOException occurs while attempting to
     * start the transmission, the given response listener is called immediately (with {@link HttpStatus#BadRequest} and
     * {@code null}) and {@code false} is returned.</p>
     *
     * @param source           [Required] Where to send the data.
     * @param responseListener [Required] The block to be called when a response is available.
     * @param stopper          [Optional] The block to tell this when to stop listening.
     * @return {@code true} iff the data was successfully sent, whether or not the server received it. If there is a
     *         transmission problem, it will be reflected in an HTTP Status Code passed to {@code responseListener}.
     */
    public static boolean listenForTransmissions(final Source source, final TransmissionListener responseListener, final Stopper stopper) {
        if (null == responseListener) {
            LoggingUtils.BACKGROUND.severe("listenForTransmission requires a listener. Not listening.");
            return false;
        }
        if (null == source) {
            LoggingUtils.BACKGROUND.severe("listenForTransmission requires a source. Not listening.");
            responseListener.transmissionReceived(HttpStatus.BadRequest, null, null);
            return false;
        }

        final Stopper finalNonnullStopper = (null == stopper) ? () -> false : stopper;

        new Thread(() -> {
            try {
                while (!finalNonnullStopper.shouldStop()) {
                    LoggingUtils.BACKGROUND.fine("Opening new socket...");
                    // TODO: Fill in local info?
                    ServerSocket connectionSocket = SSLServerSocketFactory.getDefault().createServerSocket(source.getPort(), 0, source.getAddress());

                    LoggingUtils.BACKGROUND.fine("Starting request thread...");

                    new Thread(() -> {
                        try (// these are auto-closed:
                                Socket remoteSocket = connectionSocket.accept();
                                PrintWriter out = new PrintWriter(remoteSocket.getOutputStream(), true);
                                BufferedReader inReader = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));) {
                            char[] input = null;
                            StringBuilder inBuilder = new StringBuilder();
                            while (inReader.read(input) >= 0 && !finalNonnullStopper.shouldStop()) {
                                Transmittable incoming = new StringTransmittable(inBuilder);
                                responseListener.transmissionReceived(HttpStatus.OK, incoming, null);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(TransmissionReceiver.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }).start();
                }
            } catch (IOException ex) {
                LoggingUtils.BACKGROUND.log(Level.SEVERE, "Exception on ", ex);
            }
        }).start();
        return true;
    }

    final static class HttpRequest implements Runnable {

        final static String CRLF = "\r\n";
        Socket socket;
        TransmissionListener listener;

        public HttpRequest(Socket initSocket, TransmissionListener initListener) {
            if (null == initListener) {
                String reason = "HttpRequest requires a listener.";
                LoggingUtils.BACKGROUND.severe(reason + " Not listening.");
                throw new IllegalArgumentException(reason);
            }
            if (null == initSocket) {
                String reason = "HttpRequest requires a listener.";
                LoggingUtils.BACKGROUND.severe(reason + " Not listening.");
                initListener.transmissionReceived(HttpStatus.BadRequest, null, null);
                throw new IllegalArgumentException(reason);
            }
            listener = initListener;
            socket = initSocket;
        }
        // Implement the run() method of the Runnable interface.

        @Override
        public void run() {
            try {
                processRequest();
            } catch (Throwable t) {
                LoggingUtils.BACKGROUND.log(Level.SEVERE, "Problem when processing request!", t);
                listener.transmissionReceived(HttpStatus.BadRequest, null, t);
            }
        }

        private void processRequest() throws IOException {

            //Get a reference to the socket's input and output streams.
            InputStream inStream = socket.getInputStream();
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            //Set up input stream filters.
            FilterInputStream fis;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Get the request line of the HTTP request message.
            String requestLine = reader.readLine();
            //Display request line.
            System.out.println();
            System.out.println(requestLine);

            // Get and display the header lines
            String headerline = null;
            while ((headerline = reader.readLine()).length() != 0) {
                System.out.println(headerline);
            }
            //close streams and socket.
            //os.close();
            //br.close();
            //socket.close();
            // Extract the filename from the request line.
            StringTokenizer tokens = new StringTokenizer(requestLine);
            tokens.nextToken();  // skip over the method, which should be "GET"
            String fileName = tokens.nextToken();
            //Prepend a "." so that file request is within the current directory.
            fileName = "." + fileName;
            // Open the requested file.
            FileInputStream fis1 = null;
            boolean fileExists = true;
            try {
                fis1 = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                fileExists = false;
            }
            //Construct the response message.
            String statusLine = null;
            String contentTypeLine = null;
            String entityBody = null;

            if (fileExists) {
                statusLine = "Responding to existing file";
                contentTypeLine = "Content-type:"
                        + contentType(fileName) + CRLF;
            } else {
                statusLine = "File doesn't exist\n";
                contentTypeLine = "no contents\n";
                entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
                // Send the status line.
                outStream.writeBytes(statusLine);
                // Send the content type line.
                outStream.writeBytes(contentTypeLine);
                // Send a blank line to indicate the end of the header lines.
                outStream.writeBytes(CRLF);

                // Send the entity body.

                if (fileExists) {
                    sendBytes(fis1, outStream);
                    fis1.close();
                } else {
                    outStream.writeBytes(entityBody);
                }
            }

            outStream.close();
            reader.close();
            socket.close();
        }

        private static void sendBytes(FileInputStream fis, OutputStream os) throws IOException {
            // Construct a 1K buffer to hold bytes on their way to the socket.
            byte[] buffer = new byte[1024];
            int bytes = 0;
            // Copy requested file into the socket's output stream.
            while ((bytes = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytes);
            }
        }

        private static String contentType(String fileName) {
            if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
                return "text/html";
            }
            if (fileName.endsWith(".gif") || fileName.endsWith(".GIF")) {
                return "image/gif";
            }
            if (fileName.endsWith(".jpeg")) {
                return "image/jpeg";
            }
            if (fileName.endsWith(".java")) {
                return "java file";
            }
            if (fileName.endsWith(".sh")) {
                return "bourne/awk";
            }


            return "application/octet-stream";
        }
    }
}
