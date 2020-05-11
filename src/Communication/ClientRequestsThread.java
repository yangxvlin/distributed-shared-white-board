package Communication;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import remote.UserManager;

import java.io.IOException;

import static Communication.CommunicationConstant.*;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 1:21
 * description:
 **/

public class ClientRequestsThread extends Thread {

    private CommunicationSocket client;

    private UserManager userManager;

    public ClientRequestsThread(CommunicationSocket client, UserManager userManager) {
        this.client = client;

        this.userManager = userManager;
    }

    @Override
    public void run() {
        super.run();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        String request;
        while (true) {
            try {
                request = client.receive();
                try {
                    jsonObject = (JSONObject) parser.parse(request);
                } catch (ParseException e) {
                    System.out.println("Invalid request: " + request);
                    interrupt();
                }

                String requestType = (String) jsonObject.get(USER_REQUEST);

                switch (requestType) {
                    case CREATE_WHITEBOARD:
                        // already has manager
                        if (userManager.hasManager()) {
                            client.responseAlreadyHasManager();
                        } else {
                            String uid = userManager.addUser((String) jsonObject.get(USERNAME));
                            userManager.setManagerUID(uid);
                            userManager.addUserSocket(uid, client);
                            client.responseSuccessfulCreateWhiteboard(uid);
                        }
                        break;
                    case REQUEST_JOIN_WHITEBOARD:
                        CommunicationSocket managerSocket = userManager.getManagerCommunicationSocket();
                        // no manager reject
                        if (!userManager.hasManager()) {
                            client.responseNoManagerYet();
                            break;
                        }
                        String uid = userManager.addCandidateUser((String) jsonObject.get(USERNAME));
                        userManager.addUserSocket(uid, client);
                        managerSocket.askManagerAccept(uid);
                        break;
                    case ASK_JOIN_WHITEBOARD_RESULT:
                        String candidateUID = (String) jsonObject.get(UID);
                        boolean result = Boolean.parseBoolean((String) jsonObject.get(RESULT));
                        CommunicationSocket candidateSocket = userManager.getCommunicationSocket(candidateUID);
                        assert candidateSocket != null;
                        if (result) {
                            candidateSocket.sendJoinRequestResult(RESPONSE, SUCCESSFUL_JOIN_WHITEBOARD, result, candidateUID);
                            userManager.acceptCandidateUser(candidateUID);
                        } else {
                            candidateSocket.sendJoinRequestResult(RESPONSE, REJECTED_BY_MANAGER, result, candidateUID);
                            userManager.rejectCandidateUser(candidateUID);
                        }
                        break;
                    case LEAVE_WHITE_BOARD:
                        String uidToBeRemoved = (String) jsonObject.get(UID);
                        userManager.removeUser(uidToBeRemoved);
                        break;
                    case CLOSE_WHITE_BOARD:
                        userManager.broadcastManagerClose();
                        userManager.clear();
                        break;
                    case KICK_OUT_USER:
                        String kickOutUID = (String) jsonObject.get(UID);
                        CommunicationSocket kickOutSocket = userManager.getCommunicationSocket(kickOutUID);
                        if (kickOutSocket != null) {
                            System.out.println("    |send kick out request to " + UID);
                            kickOutSocket.sendKickOutRequest();
                        } else {
                            System.out.println("    |kick out non-existing uer: " + UID);
                        }
                        userManager.removeUser(kickOutUID);
                        break;
                    default:
                        System.out.println("Unknown request type: " + request);
                }
            } catch (IOException e) {
                System.out.println("Error in server socket");
                break;
            }
        }
    }
}
