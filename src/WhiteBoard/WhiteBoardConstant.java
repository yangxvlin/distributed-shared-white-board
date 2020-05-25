package WhiteBoard;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-08 15:24
 * description: constant for whiteboard application
 **/

public class WhiteBoardConstant {
    /**
     * default title
     */
    public static final String APP_TITLE = "Distributed Shared Whiteboard";

    /**
     * manager accept client's join
     */
    public static final boolean ACCEPTED = true;

    /**
     * manager reject client's join
     */
    public static final boolean REJECTED = false;

    /**
     * auto reject join request timeout for manager, unit: second
     */
    public static final int ASK_MANAGER_JOIN_TIMEOUT = 10;

    /**
     * client's request to be displayed to the manager
     */
    public static final String SHARE_PROMPT = " wants to share your whiteboard";
}
