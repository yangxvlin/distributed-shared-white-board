package remote;

import Communication.CommunicationSocket;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 1:23
 * description:
 **/

public class UserManager {

    private int userCounter = 0;

//    private HashMap<Integer, String> users;

    private HashMap<String, CommunicationSocket> usersSocket;

    private String managerUID = null;

    private IRemoteUserList remoteUserList;

    private List<String> candidateUsers;

    public UserManager() {
        usersSocket = new HashMap<>();

        candidateUsers = new ArrayList<>();
    }

    public synchronized String addUser(String userName) {
        String uid = String.format("%s (%d)", userName, userCounter);

        try {
            if (managerUID == null) {
                managerUID = uid;
                remoteUserList.setManagerName(managerUID);
            } else {
                remoteUserList.addUser(uid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        userCounter++;

        return uid;
    }

    public synchronized String addCandidateUser(String userName) {
        String uid = String.format("%s (%d)", userName, userCounter);
        candidateUsers.add(uid);

        userCounter++;

        return uid;
    }

    public synchronized void acceptCandidateUser(String uid) {
        if (candidateUsers.contains(uid)) {
            candidateUsers.remove(uid);
            try {
                remoteUserList.addUser(uid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void rejectCandidateUser(String uid) {
        if (candidateUsers.contains(uid)) {
            candidateUsers.remove(uid);
            usersSocket.remove(uid);
        }
    }

    public void setRemoteUserList(IRemoteUserList remoteUserList) {
        this.remoteUserList = remoteUserList;
    }

    public synchronized void removeUser(String uid) {
        try {
            if (remoteUserList.getUserNames().contains(uid)) {
                remoteUserList.getUserNames().remove(uid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (usersSocket.containsKey(uid)) {
            try {
                usersSocket.get(uid).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            usersSocket.remove(uid);
        }
    }

//    public synchronized int getUserCounter() {
//        return userCounter;
//    }

    public synchronized void addUserSocket(String uid, CommunicationSocket socket) {
        if (!usersSocket.containsKey(uid)) {
            usersSocket.put(uid, socket);
        } else {
            System.out.println("Error uid already has sockets: " + uid);
        }
    }

    public synchronized CommunicationSocket getManagerCommunicationSocket() {
        return usersSocket.get(managerUID);
    }

    public synchronized CommunicationSocket getCommunicationSocket(String uid) {
        return usersSocket.getOrDefault(uid, null);
    }

    public synchronized String getManagerUID() {
        return managerUID;
    }

    public synchronized void setManagerUID(String managerUID) {
        this.managerUID = managerUID;
    }

    public synchronized boolean hasManager() {
        return managerUID != null;
    }

    public synchronized void clear() {
        userCounter = 0;
        usersSocket.clear();
        managerUID = null;
        try {
            remoteUserList.getUserNames().clear();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        candidateUsers.clear();
    }

//    public synchronized void registerUser(Socket client) {
//
//    }
}
