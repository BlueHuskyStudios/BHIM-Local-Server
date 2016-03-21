package org.bh.tools.net.im.localserver;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * BHIM Core is copyright Blue Husky Programming ©2015 BH-1-PS <hr/>
 *
 * This is a tiny shell app so someone running the JAR knows why they shouldn't be.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-18 (1.0.0) - Kyli created Main
 * @since 2016-03-18
 */
public class Main {

    public static final String BHIM_URL_STRING = "https://github.com/BlueHuskyStudios/BHIM/releases";
    public static final URL BHIM_URL;
    private static final String DEV_TEST_ARG = "--devtest";

    static {
        URL temp = null;
        try {
            temp = new URL(BHIM_URL_STRING);
        } catch (MalformedURLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "Could not generate URL from string: " + BHIM_URL_STRING, ex);
        }
        BHIM_URL = temp;
    }

    @SuppressWarnings({"UseSpecificCatch", "UseOfSystemOutOrSystemErr"}) // catch everything paranoidly, tell console devs
    public static void main(String[] args) {
        if (Arrays.stream(args).anyMatch((String t) -> t.equals(DEV_TEST_ARG))) {
            DevTest.main(args);
            return;
        }
        String title = "Oops! This isn't exactly an app.";
        String messageLine1 = "BHIM Local Server is a library for the local functionality of BHIM.";
        String message = messageLine1 + " \nFor the full instant messenger, see " + BHIM_URL_STRING;

        try {
            System.out.println(title);
            System.out.println(message);

            if (Desktop.isDesktopSupported()) {
                message = messageLine1;
                String initialSelectionValue = "OK";
                String navigateToUrlValue = "Go get BHIM!";
                String[] selectionValues = {initialSelectionValue, navigateToUrlValue};

                int selection = JOptionPane.showOptionDialog(
                        null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        selectionValues, initialSelectionValue);

                if (selection == 1) {
                    Desktop.getDesktop().browse(BHIM_URL.toURI());
                }
            }
        } catch (Throwable t) {
            title = "Oops! Something went wrong.";
            message = "Couldn't open a browser! \nTo download it, yourself, go to: " + BHIM_URL_STRING;
            Logger.getGlobal().log(Level.SEVERE, message, t);
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
