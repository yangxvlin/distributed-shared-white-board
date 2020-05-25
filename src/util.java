import static WhiteBoard.Util.popupDialog;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-26 1:58
 * description:
 **/

public class util {

    public static int parsePort(String port) {
        int result = 6000;

        try {
            result = Integer.parseInt(port);

            if (result < 1024 || result > 65535) {
                popupDialog("server port number should be 1024-65535");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            popupDialog("server port number should be an integer");
            System.exit(1);
        }

        return result;
    }
}
