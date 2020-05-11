package Communication;

import WhiteBoard.WhiteBoardApplication;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static Communication.CommunicationConstant.*;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 20:26
 * description:
 **/

public class ManagerCommunicationThread extends Thread {

    private final WhiteBoardApplication app;
    private final String request;
    private final CommunicationSocket socket;

    public ManagerCommunicationThread(WhiteBoardApplication app, String request, CommunicationSocket socket) {
        this.app = app;
        this.request = request;
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();

        JSONObject jsonObject = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        try {
            jsonObject = (JSONObject) jsonParser.parse(request);

            String requestType = (String) jsonObject.get(USER_REQUEST);
            switch (requestType) {
                case ASK_JOIN_WHITEBOARD:
                    String candidateUID = (String) jsonObject.get(UID);
                    boolean result = app.askAcceptCandidate(candidateUID);
                    socket.sendJoinRequestResult(USER_REQUEST, ASK_JOIN_WHITEBOARD_RESULT, result, candidateUID);
                    break;
                default:
                    System.out.println("Unknown request: " + request);
            }
        } catch (ParseException e) {
            System.out.println("Invalid request: " + request);
        } catch (IOException e) {
            System.out.println("Error send message to server");
        }
    }
}
