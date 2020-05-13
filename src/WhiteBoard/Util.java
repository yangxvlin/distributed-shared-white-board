package WhiteBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 19:34
 * description:
 **/

public class Util {
    public static final String IMAGE_TYPE = "jpg";

    /**
     * @param message the description of the error
     * invoke dialog for error
     */
    public static void popupErrorDialog(String message) {
        JOptionPane.showConfirmDialog(
                null,
                message,
                "Error",
                JOptionPane.OK_CANCEL_OPTION
        );
        System.exit(1);
    }

    /**
     * @param message the description of the dialog
     * invoke dialog for error
     */
    public static void popupDialog(String message) {
        JOptionPane.showConfirmDialog(
                null,
                message,
                "Info",
                JOptionPane.OK_CANCEL_OPTION
        );
    }

    public static void popupNoServerConnectionErrorDialog() {
        popupErrorDialog("No connection to server");
    }

    public static void saveImage(BufferedImage bufferedImage, String filePath) {
        File outfile = new File(filePath);
        try {
            ImageIO.write(bufferedImage, IMAGE_TYPE, outfile);
        } catch (IOException e) {
            popupDialog("Failed to save to: "+ filePath + "." + IMAGE_TYPE);
        }
    }

    public static String generateAutoFileName() {
        return "shared-whiteboard-auto-save-" +
                new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()) +
                "." + IMAGE_TYPE;
    }
}
