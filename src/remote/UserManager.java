package remote;

import Communication.CommunicationSocket;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 1:23
 * description: manager for managing users
 **/

public class UserManager {

    /**
     * number of users
     */
    private int userCounter = 0;

    /**
     * a mapping for user: client's socket
     */
    private HashMap<String, CommunicationSocket> usersSocket;

    /**
     * whiteboard manager's id
     */
    private String managerUID = null;

    /**
     * remote user list object
     */
    private IRemoteUserList remoteUserList;

    /**
     * list of candidate users
     */
    private List<String> candidateUsers;

    public UserManager() {
        usersSocket = new HashMap<>();

        candidateUsers = new ArrayList<>();
    }

    /**
     * @param userName user's name
     * @return user's unique id
     */
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

    /**
     * @param userName candidate user's name
     * @return an unique id for candidate user
     */
    public synchronized String addCandidateUser(String userName) {
        String uid = String.format("%s (%d)", userName, userCounter);
        candidateUsers.add(uid);

        userCounter++;

        return uid;
    }

    /**
     * @param uid candidate user's name
     */
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

    /**
     * @param uid candidate user's name
     */
    public synchronized void rejectCandidateUser(String uid) {
        if (candidateUsers.contains(uid)) {
            candidateUsers.remove(uid);
            usersSocket.remove(uid);
        }
    }

    /**
     * @param remoteUserList remote user list object
     */
    public void setRemoteUserList(IRemoteUserList remoteUserList) {
        this.remoteUserList = remoteUserList;
    }

    /**
     * @param uid user's id to be removed
     */
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

    /**
     * @param uid unique user id
     * @param socket connection to the client
     */
    public synchronized void addUserSocket(String uid, CommunicationSocket socket) {
        if (!usersSocket.containsKey(uid)) {
            usersSocket.put(uid, socket);
        } else {
            System.out.println("Error uid already has sockets: " + uid);
        }
    }

    /**
     * @return manager's socket
     */
    public synchronized CommunicationSocket getManagerCommunicationSocket() {
        return usersSocket.get(managerUID);
    }

    /**
     * @param uid user's unique id
     * @return user's socket
     */
    public synchronized CommunicationSocket getCommunicationSocket(String uid) {
        return usersSocket.getOrDefault(uid, null);
    }

    /**
     * @return manager's unique id
     */
    public synchronized String getManagerUID() {
        return managerUID;
    }

    /**
     * @param managerUID manager's unique id
     */
    public synchronized void setManagerUID(String managerUID) {
        this.managerUID = managerUID;
    }

    /**
     * @return true if the whiteboard has manager
     */
    public synchronized boolean hasManager() {
        return managerUID != null;
    }

    /**
     * clear the user manager
     */
    public synchronized void clear() {
        System.out.println("    | clear UserManager");
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

    /**
     * @param operation manager's operation
     */
    public synchronized void broadcastManagerOperation(String operation) {
        for (Map.Entry<String, CommunicationSocket> entry: usersSocket.entrySet()) {
            String uid = entry.getKey();
            CommunicationSocket communicationSocket = entry.getValue();

            if (communicationSocket.isClosed()) {
                System.out.println("    | " + uid + " socket has already closed");
            } else if (!uid.equals(managerUID)) {
                System.out.println("    | " + uid + " sent with manager operation: " + operation);
                try {
                    communicationSocket.sendManagerOperation(operation);
                } catch (IOException e) {
                    System.out.println("        |Socket error");
                }
            }
        }
    }
}
