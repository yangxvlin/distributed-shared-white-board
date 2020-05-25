import Communication.ClientConnection;
import Communication.CommunicationSocket;
import Communication.ManagerCommunicationThread;
import Communication.UserCommunicationThread;
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
 * @create 2020-05-07 18:13
 * description:
 **/

public class JoinWhiteBoard {
    private static String serverAddress;

    private static int serverPort;

    private static String userName;

    public static void main(String args[]) {
        parseArguments(args);

        WhiteBoardApplication app = new WhiteBoardApplication(false);

        try {
            //Connect to the rmi registry that is running on localhost
            Registry registry = LocateRegistry.getRegistry("localhost");

            IRemoteUserList remoteUserList = (IRemoteUserList) registry.lookup(RegistryConstant.REMOTE_USER_LIST);
            IRemoteCanvas remoteCanvas = (IRemoteCanvas) registry.lookup(RegistryConstant.REMOTE_CANVAS);
            app.setRemoteUserList(remoteUserList);
            app.setRemoteCanvas(remoteCanvas);

            // create socket
            CommunicationSocket socket = new CommunicationSocket(serverAddress, serverPort);
            // create whiteboard
            ClientConnection connection = new ClientConnection(socket);
            app.setClientConnection(connection);
            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    popupDialog("Waiting for manager to accept join request");
                }
            };
            t.start();

            connection.connect(app, userName);

            System.out.println("Whiteboard joined");
            // response to kick out request
            while (true) {
                String request = socket.receive();
                UserCommunicationThread kickOutThread = new UserCommunicationThread(app, request);
                kickOutThread.start();
            }
        } catch (RemoteException e) {
            System.out.println("Error get remote object");
        } catch (NotBoundException e) {
            popupNoServerConnectionErrorDialog();
        } catch (IOException e) {
            System.out.println("Error receive kick out requests from server");
        }
    }


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
