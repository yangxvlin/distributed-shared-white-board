package Communication;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 19:02
 * description: constants for communication protocol
 **/

public class CommunicationConstant {
    /**
     * user's request
     */
    public static final String USER_REQUEST = "request";

    /**
     * create whiteboard
     */
    public static final String CREATE_WHITEBOARD = "Create Whiteboard";

    /**
     * join whiteboard request
     */
    public static final String REQUEST_JOIN_WHITEBOARD = "Join Whiteboard";

    /**
     * ask join whiteboard
     */
    public static final String ASK_JOIN_WHITEBOARD = "Ask Join Whiteboard";

    /**
     * ask join whiteboard result
     */
    public static final String ASK_JOIN_WHITEBOARD_RESULT = "Ask Join Whiteboard result";

    /**
     * manager close the whiteboard
     */
    public static final String CLOSE_WHITE_BOARD = "Manager close Whiteboard";

    /**
     * user leave the whiteboard
     */
    public static final String LEAVE_WHITE_BOARD = "User leave Whiteboard";

    /**
     * manager kickout an user
     */
    public static final String KICK_OUT_USER = "Kick out user";

    /**
     * manager close the whiteboard
     */
    public static final String MANAGER_CLOSE = "Manager closed the whiteboard";

    /**
     * manager initialize a whiteboard
     */
    public static final String MANAGER_NEW = "Manager new a whiteboard";

    /**
     * manager open a saved whiteboard
     */
    public static final String MANAGER_OPEN = "Manager open a whiteboard";

    /**
     * result message
     */
    public static final String RESULT = "result";

    /**
     * key for username
     */
    public static final String USERNAME = "username";

    /**
     * key for response
     */
    public static final String RESPONSE = "response";

    /**
     * already has manager exception
     */
    public static final String ALREADY_HAS_MANAGER = "already has manager";

    /**
     * no manager create whiteboard exception
     */
    public static final String NO_MANAGER_YET = "No whiteboard created by manager";

    /**
     * create whiteboard success
     */
    public static final String SUCCESSFUL_CREATE_WHITEBOARD = "Successful create Whiteboard";

    /**
     * manager rejection exception
     */
    public static final String REJECTED_BY_MANAGER = "rejected by the manager to join";

    /**
     * join whiteboard success
     */
    public static final String SUCCESSFUL_JOIN_WHITEBOARD = "Successful join Whiteboard";

    /**
     * key for uid
     */
    public static final String UID = "uid";
}
