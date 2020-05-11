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
 * description:
 **/

public class RemoteUserList extends UnicastRemoteObject implements IRemoteUserList {

    private String managerName = null;

    private List<String> usernames;

    public RemoteUserList() throws RemoteException {
        usernames = new ArrayList<>();
    }

    @Override
    public void addUser(String userName) throws RemoteException{
        usernames.add(userName);
        System.out.println("added: " + userName);
        System.out.println(Arrays.toString(usernames.toArray()));
    }

    @Override
    public List<String> getUserNames() throws RemoteException {
        return usernames;
    }

    @Override
    public String getManagerName() throws RemoteException{
        return managerName;
    }

    @Override
    public void setManagerName(String managerName) throws RemoteException{
        this.managerName = managerName;
    }
}
