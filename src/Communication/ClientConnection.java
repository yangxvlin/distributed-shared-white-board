package Communication;

import WhiteBoard.WhiteBoardApplication;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static Communication.CommunicationConstant.*;
import static WhiteBoard.Util.popupNoServerConnectionErrorDialog;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 15:24
 * description:
 **/

public class ClientConnection {
    private CommunicationSocket socket;

    public ClientConnection(CommunicationSocket socket) {
        this.socket = socket;
    }

    public void connect(WhiteBoardApplication app, String username) {
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();

        if (app.isManager()) {
            // request to create a white board
            obj.put(USER_REQUEST, CREATE_WHITEBOARD);
            obj.put(USERNAME, username);
        } else {
            // request to join a white board
            obj.put(USER_REQUEST, REQUEST_JOIN_WHITEBOARD);
            obj.put(USERNAME, username);
        }

        String response = "";
        try {
            socket.send(obj.toJSONString());
            // grant access to whiteboard or rejected
            response = socket.receive();
            obj = (JSONObject) parser.parse(response);

            String responseType = (String) obj.get(RESPONSE);

            switch (responseType) {
                case ALREADY_HAS_MANAGER:
                    app.error(responseType);
                    break;
                case SUCCESSFUL_CREATE_WHITEBOARD:
                    app.start((String) obj.get(UID));
                    break;
                case REJECTED_BY_MANAGER:
                    app.error(responseType);
                    break;
                case SUCCESSFUL_JOIN_WHITEBOARD:
                    app.start((String) obj.get(UID));
                    break;
                case NO_MANAGER_YET:
                    app.error(responseType);
                    break;
                default:
                    System.out.println("Unknown response: " + response);
            }
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        } catch (ParseException e) {
            System.out.println("Invalid response: " + response);
        }
    }

    public void disconnect(boolean isManager, String uid) {

        JSONObject obj = new JSONObject();

        if (isManager) {
            // manager close
            obj.put(USER_REQUEST, CLOSE_WHITE_BOARD);
            obj.put(UID, uid);
        } else {
            // user leave
            obj.put(USER_REQUEST, LEAVE_WHITE_BOARD);
            obj.put(UID, uid);
        }

        try {
            socket.send(obj.toJSONString());
            socket.close();
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    public void kickOut(String uid) {
        JSONObject obj = new JSONObject();

        obj.put(USER_REQUEST, KICK_OUT_USER);
        obj.put(UID, uid);

        try {
            socket.send(obj.toJSONString());
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    public void notifyUserWithManagerOperation(String type) {
        JSONObject obj = new JSONObject();

        obj.put(USER_REQUEST, type);

        try {
            socket.send(obj.toJSONString());
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }
}
