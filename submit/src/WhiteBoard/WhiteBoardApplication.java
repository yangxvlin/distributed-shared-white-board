package WhiteBoard;

import Communication.ClientConnection;
import remote.IRemoteCanvas;
import remote.IRemoteUserList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import static WhiteBoard.PaintConstant.*;
import static WhiteBoard.Util.popupDialog;
import static WhiteBoard.Util.popupErrorDialog;
import static WhiteBoard.Util.popupNoServerConnectionErrorDialog;
import static WhiteBoard.WhiteBoardConstant.*;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-08 14:31
 * description:
 **/

public class WhiteBoardApplication {
    /**
     * true if user is manager
     */
    private boolean isManager;

    /**
     * frame object
     */
    private JFrame frame;

    /**
     * user unique id
     */
    private String uid;

    /**
     * thread to update user list on GUI from remote object
     */
    private Thread updateUserListThread;

    /**
     * client's connection to server
     */
    private ClientConnection clientConnection;

    /**
     * remote user list object
     */
    private IRemoteUserList remoteUserList;

    /**
     * is the user kicked out by the manager
     */
    private boolean isKickedOut = false;

    /**
     * paint on canvas manager
     */
    private PaintManager paintManager;

    /**
     * file menu object
     */
    private FileMenu fileMenu;

    /**
     * @param isManager true if user is manager
     */
    public WhiteBoardApplication(boolean isManager) {
        this.isManager = isManager;

        // initialize paintManager
        this.paintManager = new PaintManager();

        // setup frame
        frame = new JFrame();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // When close the window, it should remove its information in the system.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                if (isManager) {
                    paintManager.clearCanvas();
                }
                closeFrame(e);
            }
        });
        frame.setSize(1000, 800);
        frame.setResizable(false);

        String appTitle;
        if (this.isManager) {
            // setup manager's app name
            appTitle = APP_TITLE + " (Manager)";
            frame.setTitle(appTitle);

            // setup the file menu for the manager
            JMenuBar menuBar = new JMenuBar();
            frame.setJMenuBar(menuBar);
            fileMenu = new FileMenu(frame, paintManager);
            menuBar.add(fileMenu);

            // setup the label for kickout user
            JLabel kickOutLabel = new JLabel("Kick out an user:");
            kickOutLabel.setSize(kickOutLabel.getPreferredSize());
            kickOutLabel.setLocation(700, 450);
            frame.add(kickOutLabel);

            // setup the inputting file for user to be kickedout
            JTextField kickOutTextField = new JTextField();
            kickOutTextField.setBounds(700, 470, 250, 50);
            frame.add(kickOutTextField);

            // setup button to confirm the kickout of the user
            JButton kickOutButton = new JButton("Kick out");
            kickOutButton.setBounds(700, 530, 250, 30);
            kickOutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // kickout a normal user
                    String kickOutUID = kickOutTextField.getText();
                    System.out.println("kick out: " + kickOutTextField.getText());
                    try {
                        if (kickOutUID.equals(remoteUserList.getManagerName())) {
                            popupDialog("Can't kick out yourself");
                        } else {
                            clientConnection.kickOut(kickOutUID);
                        }
                    } catch (RemoteException e1) {
                        popupNoServerConnectionErrorDialog();
                    }
                }
            });
            frame.add(kickOutButton);
        } else {
            // setup normal user's app name
            appTitle = APP_TITLE + " (User)";
            frame.setTitle(appTitle);
        }

        // set up  users display label
        JLabel jLabel = new JLabel("Users: username (uid)");
        jLabel.setSize(jLabel.getPreferredSize());
        jLabel.setLocation(700, 5);
        frame.add(jLabel);

        // paint tool selection setup
        JRadioButton circleButton = new JRadioButton(CIRCLE);
        circleButton.setBounds(10, 5, 60, 40);
        circleButton.setActionCommand(CIRCLE);
        circleButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);
        circleButton.doClick();

        JRadioButton rectangleButton = new JRadioButton(RECTANGLE);
        rectangleButton.setBounds(80, 5, 100, 40);
        rectangleButton.setActionCommand(RECTANGLE);
        rectangleButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        JRadioButton lineButton = new JRadioButton(LINE);
        lineButton.setBounds(190, 5, 60, 40);
        lineButton.setActionCommand(LINE);
        lineButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        JRadioButton textButton = new JRadioButton(TEXT);
        textButton.setBounds(260, 5, 60, 40);
        textButton.setActionCommand(TEXT);
        textButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        JRadioButton penButton = new JRadioButton(PEN);
        penButton.setBounds(330, 5, 60, 40);
        penButton.setActionCommand(PEN);
        penButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        ButtonGroup group = new ButtonGroup();
        group.add(circleButton);
        group.add(rectangleButton);
        group.add(lineButton);
        group.add(textButton);
        group.add(penButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 450, 40);
        buttonPanel.add(circleButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(lineButton);
        buttonPanel.add(textButton);
        buttonPanel.add(penButton);

        frame.add(buttonPanel);

        // canvas setup
        WhiteboardCanvasPanel whiteboardCanvasPanel = new WhiteboardCanvasPanel(paintManager);
        whiteboardCanvasPanel.setBounds(10, 50, CANVAS_WIDTH, CANVAS_HEIGHT);
        frame.add(whiteboardCanvasPanel);
    }

    /**
     * @param clientConnection client's connection to server
     */
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        if (this.isManager) {
            this.fileMenu.setClientConnection(clientConnection);
        }
    }

    /**
     * @param remoteCanvas remote canvas object
     */
    public void setRemoteCanvas(IRemoteCanvas remoteCanvas) {
        paintManager.setRemoteCanvas(remoteCanvas);
    }

    /**
     * @param remoteUserList remote user list object
     */
    public void setRemoteUserList(IRemoteUserList remoteUserList) {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setBounds(700, 30, 250, 400);
        frame.add(jTextArea);

        this.remoteUserList = remoteUserList;

        // thread to update users in whiteboard
        updateUserListThread = new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {

                    StringBuilder newText = new StringBuilder();

                    try {
                        // has manager
                        if (remoteUserList.getManagerName() != null) {
                            newText.append("(*) ").append(remoteUserList.getManagerName()).append("\n");
                            for (String s : remoteUserList.getUserNames()) {
                                newText.append("(-) ").append(s).append("\n");
                            }
                        }

                        if (!newText.toString().equals(jTextArea.getText())) {
                            jTextArea.setText(newText.toString());
                        }

                    } catch (RemoteException e) {
                        popupNoServerConnectionErrorDialog();
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

        updateUserListThread.start();
    }

    /**
     * @param uid unique user id
     */
    public void start(String uid) {
        this.uid = uid;

        frame.setVisible(true);
    }

    /**
     * @param msg error message
     */
    public void error(String msg) {
        popupErrorDialog(msg);
        stop();
    }

    /**
     * stop the background application
     */
    public void stop() {
        updateUserListThread.interrupt();
    }

    /**
     * @return true if the client is manager
     */
    public boolean isManager() {
        return isManager;
    }

    /**
     * @param candidateUID candidate's unique id
     * @return a counting down dialog to accept/reject client's request to join the whiteboard
     */
    public boolean askAcceptCandidate(String candidateUID) {
        TimeDialog dialog = new TimeDialog();
        return dialog.showDialog(frame, candidateUID + SHARE_PROMPT, ASK_MANAGER_JOIN_TIMEOUT);
    }

    /**
     * user in the application is kicked out
     */
    public void kickedOut() {
        frame.setTitle(frame.getTitle() + " - [Kicked out]");
        stop();
        popupDialog("Kicked out by the manager");
        isKickedOut = true;
        closeFrame();
    }

    /**
     * close the frame
     */
    private void closeFrame() {
        if (!isKickedOut) {
            clientConnection.disconnect(isManager, uid);
        }
        frame.dispose();
        System.exit(0);
    }

    /**
     * @param e WindowEvent
     */
    private void closeFrame(WindowEvent e) {
        if (!isKickedOut) {
            clientConnection.disconnect(isManager, uid);
        }
        stop();
        e.getWindow().dispose();
        System.exit(0);
    }

    /**
     * whiteboard closed by the manager
     */
    public void closeByManager() {
        frame.setTitle(frame.getTitle() + " - [Closed]");
        stop();
        popupDialog("Closed by the manager");
        isKickedOut = true;
        closeFrame();
    }

    /**
     * @param msg popup message for user
     */
    public void notifyUser(String msg) {
        popupDialog(msg);
    }
}
