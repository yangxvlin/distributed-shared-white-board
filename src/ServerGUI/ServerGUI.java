package ServerGUI;

import remote.IRemoteUserList;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import static WhiteBoard.PaintConstant.REMOTE_OBJECT_UI_UPDATE_RATE;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-15 21:51
 * description: server GUI
 **/

public class ServerGUI {

    private JFrame frame;

    private Thread updateUIThread;

    public ServerGUI(IRemoteUserList userList) {
        frame = new JFrame();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closed");
                if (updateUIThread != null) {
                    updateUIThread.interrupt();
                }
                System.exit(0);
            }
        });

        // setup frame
        frame.setSize(200, 350);
        frame.setResizable(false);
        frame.setTitle("Distributed Whiteboard Server");

        JLabel managerLabel = new JLabel("Manager: ");
        managerLabel.setBounds(0, 0, 70, 20);
        frame.add(managerLabel);

        JTextArea managerText = new JTextArea();
        managerText.setEditable(false);
        managerText.setBounds(70, 5, 120, 20);
        frame.add(managerText);

        JLabel userLabel = new JLabel("Users: ");
        userLabel.setBounds(0, 30, 70, 20);
        frame.add(userLabel);

        JTextArea userText = new JTextArea();
        userText.setEditable(false);
        userText.setBounds(70, 30, 120, 250);
        frame.add(userText);

        // tread to update user list GUI periodically
        updateUIThread = new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    try {
                        String managerName = userList.getManagerName();
                        if (managerName!= null && !managerName.equals(managerText.getText())) {
                            managerText.setText(managerName);
                        }

                        StringBuilder newText = new StringBuilder();
                        for (String s: userList.getUserNames()) {
                            newText.append(s).append("\n");
                        }

                        if (!newText.toString().equals(userText.getText())) {
                            userText.setText(newText.toString());
                        }

                    } catch (RemoteException e) {
                        break;
                    }

                    try {
                        Thread.sleep(REMOTE_OBJECT_UI_UPDATE_RATE);
                    } catch (InterruptedException e) {
                        System.out.println("Thread sleep error");
                        break;
                    }
                }
            }
        };
        updateUIThread.start();

        frame.setVisible(true);
    }
}
