package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 1:22
 * description: remote users using whiteboard
 **/

public interface IRemoteUserList extends Remote {

    /**
     * @param userName the name for the user
     */
    void addUser(String userName) throws RemoteException;

    /**
     * @return list of users' names
     */
    List<String> getUserNames() throws RemoteException;

    /**
     * @return whiteboard manager's name
     */
    String getManagerName() throws RemoteException;

    /**
     * @param managerName whiteboard's manager's name
     */
    void setManagerName(String managerName) throws RemoteException;
}
