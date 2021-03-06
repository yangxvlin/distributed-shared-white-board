import Communication.ClientConnection;
import Communication.CommunicationSocket;
import Communication.ManagerCommunicationThread;
import WhiteBoard.WhiteBoardApplication;
import remote.IRemoteCanvas;
import remote.IRemoteUserList;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static WhiteBoard.Util.popupDialog;
import static WhiteBoard.Util.popupNoServerConnectionErrorDialog;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-07 18:12
 * description: manager CreateWhiteBoard
 **/

public class CreateWhiteBoard {
    /**
     * the address of the server
     */
    private static String serverAddress;

    /**
     * the address of the server port
     */
    private static int serverPort;

    /**
     * client's user name
     */
    private static String userName;

    public static void main(String args[]) {
        parseArguments(args);

        // initialize app
        WhiteBoardApplication app = new WhiteBoardApplication(true);

        try {
            // Connect to the rmi registry that is running on localhost
            Registry registry = LocateRegistry.getRegistry("localhost");

            // find remote object
            IRemoteUserList remoteUserList = (IRemoteUserList) registry.lookup(RegistryConstant.REMOTE_USER_LIST);
            IRemoteCanvas remoteCanvas = (IRemoteCanvas) registry.lookup(RegistryConstant.REMOTE_CANVAS);
            app.setRemoteUserList(remoteUserList);
            app.setRemoteCanvas(remoteCanvas);

            // create socket
            CommunicationSocket socket = new CommunicationSocket(serverAddress, serverPort);

            // create whiteboard
            ClientConnection connection = new ClientConnection(socket);
            app.setClientConnection(connection);
            connection.connect(app, userName);
            System.out.println("Whiteboard created");

            // response to join request
            while (true) {
                String request = socket.receive();
                ManagerCommunicationThread joinRequestThread = new ManagerCommunicationThread(app, request, socket);
                joinRequestThread.start();
            }
        } catch (RemoteException | NotBoundException e) {
            popupNoServerConnectionErrorDialog();
        } catch (IOException e) {
            System.out.println("Error receive join requests from server");
        }
    }

    /**
     * @param args inputted arguments
     */
    private static void parseArguments(String args[]) {
        if (args.length < 3) {
            popupDialog("Not enough arguments! should be <serverIPAddress> <serverPort> username");
            System.exit(1);
        }

        serverAddress = args[0];

        serverPort = util.parsePort(args[1]);

        userName = args[2];
    }
}
