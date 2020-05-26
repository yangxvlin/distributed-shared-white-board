package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 1:41
 * description: implementation for remote users using whiteboard
 **/

public class RemoteUserList extends UnicastRemoteObject implements IRemoteUserList {

    /**
     * manage's name
     */
    private String managerName = null;

    /**
     * list of users' names
     */
    private List<String> usernames;

    public RemoteUserList() throws RemoteException {
        usernames = new ArrayList<>();
    }

    /**
     * @param userName the name for the user
     */
    @Override
    public void addUser(String userName) throws RemoteException{
        usernames.add(userName);
        System.out.println("added: " + userName);
        System.out.println("   |"+ Arrays.toString(usernames.toArray()));
    }

    /**
     * @return list of users' names
     */
    @Override
    public List<String> getUserNames() throws RemoteException {
        return usernames;
    }

    /**
     * @return whiteboard manager's name
     */
    @Override
    public String getManagerName() throws RemoteException{
        return managerName;
    }

    /**
     * @param managerName whiteboard's manager's name
     */
    @Override
    public void setManagerName(String managerName) throws RemoteException{
        this.managerName = managerName;
    }
}
