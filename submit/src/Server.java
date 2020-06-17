import Communication.ClientRequestsThread;
import Communication.CommunicationSocket;
import ServerGUI.ServerGUI;
import remote.*;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static WhiteBoard.Util.popupDialog;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-07 18:13
 * description: distributed shared whiteboard server
 **/

public class Server {

    /**
     * server port number
     */
    private static int serverPort;

    public static void main(String args[]) {
        parseArguments(args);

        UserManager userManager = new UserManager();
        try {
            // initialize remote object
            IRemoteUserList userList = new RemoteUserList();
            IRemoteCanvas canvas = new RemoteCanvas();

            // get remote object
            Registry registry = LocateRegistry.getRegistry();

            // bind remote object
            registry.bind(RegistryConstant.REMOTE_USER_LIST, userList);
            registry.bind(RegistryConstant.REMOTE_CANVAS, canvas);
            System.out.println("RMI ready");
            userManager.setRemoteUserList(userList);

            // start GUI
            ServerGUI serverGUI = new ServerGUI(userList);
            System.out.println("GUI ready");
        } catch (AlreadyBoundException e) {
            popupDialog("RMI object already bound");
            System.exit(1);
        } catch (AccessException e) {
            popupDialog("RMI access fail");
            System.exit(1);
        } catch (RemoteException e) {
            popupDialog("RMI connection fail");
            System.exit(1);
        }


        // start server
        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try(ServerSocket server = factory.createServerSocket(serverPort)) {
            System.out.println("Socket ready");
            while (true) {
                // start new thread for request
                Socket client = server.accept();

                ClientRequestsThread clientRequestsThread = new ClientRequestsThread(new CommunicationSocket(client), userManager);
                clientRequestsThread.start();
            }
        } catch (IOException e) {
            System.out.println("Create server socket fail");
        }
    }

    /**
     * @param args inputted arguments
     */
    private static void parseArguments(String args[]) {
        if (args.length < 2) {
            popupDialog("Not enough arguments! should be <server address> <server port>");
            System.exit(1);
        }


        serverPort = util.parsePort(args[1]);
    }
}
