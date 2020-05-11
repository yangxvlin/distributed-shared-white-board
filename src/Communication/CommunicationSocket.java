package Communication;

import org.json.simple.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static Communication.CommunicationConstant.*;
import static WhiteBoard.Util.popupNoServerConnectionErrorDialog;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 2:15
 * description:
 **/

public class CommunicationSocket {
    private Socket socket;

    /**
     * client's input data stream
     */
    private DataInputStream is;

    /**
     * client's output data stream
     */
    private DataOutputStream os;

    public CommunicationSocket(String serverAddress, int serverPort) {
        try {
            this.socket = new Socket(serverAddress, serverPort);

            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    public CommunicationSocket(Socket socket) {
        try {
            this.socket = socket;

            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send request message through socket
     * @param request request message
     * @throws IOException io exception
     */
    public void send(String request) throws IOException {
        System.out.println("send: " + request);
        os.writeUTF(request);
        os.flush();
    }

    /**
     * @return received message from the socket
     * @throws IOException io exception
     */
    public String receive() throws IOException {
        String receive = is.readUTF();
        System.out.println("receive: " + receive);
        return receive;
    }

    /**
     * close the socket
     * @throws IOException io exception
     */
    public void close() throws IOException {
        System.out.println("Close the socket");
        socket.close();
        is.close();
        os.close();
    }

    public void responseAlreadyHasManager() throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(RESPONSE, ALREADY_HAS_MANAGER);

        send(jsonObject.toJSONString());
    }

    public void responseNoManagerYet() throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(RESPONSE, NO_MANAGER_YET);

        send(jsonObject.toJSONString());
    }

    public void responseSuccessfulCreateWhiteboard(String uid) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(RESPONSE, SUCCESSFUL_CREATE_WHITEBOARD);
        jsonObject.put(UID, uid);

        send(jsonObject.toJSONString());
    }

    public void askManagerAccept(String candidateUID) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(USER_REQUEST, ASK_JOIN_WHITEBOARD);
        jsonObject.put(UID, candidateUID);

        send(jsonObject.toJSONString());
    }

    public void sendJoinRequestResult(String typeKey, String type, boolean result, String candidateUID) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(typeKey, type);
        jsonObject.put(UID, candidateUID);
        jsonObject.put(RESULT, Boolean.toString(result));

        send(jsonObject.toJSONString());
    }

    public void sendKickOutRequest() throws IOException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(USER_REQUEST, KICK_OUT_USER);

        send(jsonObject.toJSONString());
    }
}
