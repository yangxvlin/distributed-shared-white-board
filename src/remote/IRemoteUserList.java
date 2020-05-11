package remote;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 1:22
 * description:
 **/

public interface IRemoteUserList extends Remote {

    void addUser(String userName) throws RemoteException;

    List<String> getUserNames() throws RemoteException;

    String getManagerName() throws RemoteException;

    void setManagerName(String managerName) throws RemoteException;
}
