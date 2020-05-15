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
 * @create 2020-05-11 23:44
 * description:
 **/

public class UserCommunicationThread extends Thread {

    private final WhiteBoardApplication app;
    private final String request;

    public UserCommunicationThread(WhiteBoardApplication app, String request) {
        this.app = app;
        this.request = request;
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
                case KICK_OUT_USER:
                    app.kickedOut();
                    break;
                case MANAGER_CLOSE:
                    app.closeByManager();
                    break;
                case MANAGER_NEW:
                    app.notifyUser(MANAGER_NEW);
                    break;
                case MANAGER_OPEN:
                    app.notifyUser(MANAGER_OPEN);
                    break;
                default:
                    System.out.println("Unknown request: " + request);
            }
        } catch (ParseException e) {
            System.out.println("Invalid request: " + request);
        }
    }
}
