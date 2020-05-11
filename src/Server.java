import Communication.ClientRequestsThread;
import Communication.CommunicationSocket;
import remote.IRemoteUserList;
import remote.RemoteUserList;
import remote.UserManager;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-07 18:13
 * description:
 **/

public class Server {

    private static int serverPort;

//    private static ServerSocket serverSocket;

    public static void main(String args[]) {
        parseArguments(args);

        UserManager userManager = new UserManager();
        try {
//            RemoteUserOperation remoteUserOperation = new RemoteUserOperation();
//            WhiteBoardApplication whiteBoardApplication = new WhiteBoardApplication();
//
//            RemoteWhiteBoard stubWhiteBoard = (RemoteWhiteBoard) UnicastRemoteObject.exportObject(whiteBoardApplication, 0);

            IRemoteUserList userList = new RemoteUserList();

            Registry registry = LocateRegistry.getRegistry();
//            registry.bind(RegistryConstant.WHITEBOARD_APPLICATION, stubWhiteBoard);

//            registry.bind(RegistryConstant.CLIENT_OPERATION, remoteUserOperation);

            registry.bind(RegistryConstant.REMOTE_USER_LIST, userList);
            System.out.println("RMI ready");
            userManager.setRemoteUserList(userList);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try(ServerSocket server = factory.createServerSocket(serverPort)) {
            System.out.println("Socket ready");
            while (true) {
                Socket client = server.accept();

                ClientRequestsThread clientRequestsThread = new ClientRequestsThread(new CommunicationSocket(client), userManager);
                clientRequestsThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Create server socket fail");
            System.exit(1);
        }


    }

    private static void parseArguments(String args[]) {
        if (args.length < 1) {
            System.out.println("Not enough arguments! should be <server port>");
            System.exit(1);
        }

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
    }
}
