package WhiteBoard;

import javax.swing.*;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 19:34
 * description:
 **/

public class Util {
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
}
