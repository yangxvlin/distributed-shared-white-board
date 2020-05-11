import Communication.ClientConnection;
import Communication.CommunicationSocket;
import Communication.ManagerCommunicationThread;
import WhiteBoard.WhiteBoardApplication;
import remote.IRemoteUserList;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static WhiteBoard.Util.popupNoServerConnectionErrorDialog;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-07 18:12
 * description:
 **/

public class CreateWhiteBoard {
    private static String serverAddress;

    private static int serverPort;

    private static String userName;

    public static void main(String args[]) {
        parseArguments(args);

//        WhiteBoardApplication whiteBoardView = new WhiteBoardApplication(true);
        WhiteBoardApplication app = new WhiteBoardApplication(true);

        try {
//            //Connect to the rmi registry that is running on localhost
            Registry registry = LocateRegistry.getRegistry("localhost");

            IRemoteUserList remoteUserList = (IRemoteUserList) registry.lookup(RegistryConstant.REMOTE_USER_LIST);
            app.setRemoteUserList(remoteUserList);

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
        } catch (RemoteException e) {
            System.out.println("Error get remote object");
        } catch (NotBoundException e) {
            System.out.println("Remote object not bound");
            popupNoServerConnectionErrorDialog();
        } catch (IOException e) {
            System.out.println("Error receive join requests from server");
        }
    }

    private static void parseArguments(String args[]) {
        if (args.length < 3) {
            System.out.println("Not enough arguments! should be <serverIPAddress> <serverPort> username");
            System.exit(1);
        }

        serverAddress = args[0];

        try {
            serverPort = Integer.parseInt(args[1]);

            if (serverPort < 1204 || serverPort > 65535) {
                System.out.println("server port number should be 1024-65535");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.out.println("server port number should be an integer");
            System.exit(1);
        }

        userName = args[2];
    }
}
