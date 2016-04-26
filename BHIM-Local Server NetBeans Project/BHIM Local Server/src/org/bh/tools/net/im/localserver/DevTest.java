package org.bh.tools.net.im.localserver;

import com.floatbackwards.statuscodes.HttpStatus;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.bh.tools.net.im.core.msg.Recipient;
import org.bh.tools.net.im.core.msg.Sender;
import org.bh.tools.net.im.core.msg.Transmittable;
import org.bh.tools.net.im.core.util.BHIMConstants;
import org.bh.tools.net.im.core.util.LoggingUtils;
import org.bh.tools.net.im.localserver.coms.ChatReceiver;
import org.bh.tools.net.im.localserver.coms.ChatSender;
import org.bh.tools.net.im.localserver.coms.transmittables.StringTransmittable;

/**
 * BHIM Core is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * This is a tiny test app so devs can ensure BHIM Local Server is functioning as intended.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-18 (1.0.0) - Kyli created Main
 * @since 2016-03-18
 */
class DevTest {

    private static final String WINDOW_TITLE = "BHIM Local Server DevTest";

    public static void main(String[] args) {
        String ipAddressStr = JOptionPane.showInputDialog(null, "IP Address to chat with", WINDOW_TITLE, JOptionPane.QUESTION_MESSAGE);
        if (null == ipAddressStr) {
            return;
        }
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(ipAddressStr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(DevTest.class.getName()).log(Level.SEVERE, "Invalid IP Address supplied: " + ipAddressStr, ex);
            JOptionPane.showMessageDialog(null, "Invalid IP:\r\n" + ipAddressStr, WINDOW_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        Recipient recipient = new Recipient(ipAddress, 0);

        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        JLabel chatOutput = new JLabel();
        gbc.gridwidth = 2;
        frame.add(chatOutput, gbc);

        JTextField chatInput = new JTextField();
        gbc.gridwidth = 1;
        gbc.gridy++;
        frame.add(chatInput, gbc);

        JButton sendButton = new JButton("Send");
        gbc.gridx++;
        frame.add(sendButton, gbc);

        sendButton.addActionListener((ActionEvent e) -> {
            String textToSend = chatInput.getText();
            chatInput.setText(null);
            ChatSender.sendText(textToSend, recipient,
                    (HttpStatus responseCode, Transmittable response) -> {
                        LoggingUtils.BACKGROUND.log(Level.INFO, "Transmission sent with response: \n\t" + responseCode
                                + "\n\t" + response);
                    }
            );
        });

        Sender sender = new Sender(ipAddress, BHIMConstants.DEFAULT_CHAT_PORT);
        ChatReceiver.listenForText(sender, (HttpStatus status, StringTransmittable incoming, Throwable problem) -> {
            if (null != problem) {
                LoggingUtils.BACKGROUND.log(Level.SEVERE, "Proplem occurred when trying to send!", problem);
            } else {
                LoggingUtils.BACKGROUND.log(Level.INFO, "Transmission received with response: \n\t" + status
                        + "\n\t" + incoming);
                chatOutput.setText(String.valueOf(incoming.getBody().getContent()));
            }
        });


        frame.setVisible(true);
    }
}

//TODO: Test this with a VM or other computer
