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

import static WhiteBoard.Contant.*;
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

public class WhiteBoardApplication extends JFrame {
    private boolean isManager;

    private JFrame frame;

    private String uid;

    private Thread updateUserListThread;

//    private Thread updateCanvasThread;

    private ClientConnection clientConnection;

    private IRemoteUserList remoteUserList;

    private boolean isKickedOut = false;

    private PaintManager paintManager;

    private WhiteboardCanvasPanel whiteboardCanvasPanel;

    public WhiteBoardApplication(boolean isManager) {
        this.isManager = isManager;

        this.paintManager = new PaintManager();

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

        if (this.isManager) {
            frame.setTitle(APP_TITLE + " (Manager)");
            JMenuBar menuBar = new JMenuBar();
            frame.setJMenuBar(menuBar);
            FileMenu fileMenu = new FileMenu(frame, paintManager);
            menuBar.add(fileMenu);

            JLabel kickOutLabel = new JLabel("Kick out an user:");
            kickOutLabel.setSize(kickOutLabel.getPreferredSize());
            kickOutLabel.setLocation(700, 450);
            frame.add(kickOutLabel);

            JTextField kickOutTextField = new JTextField();
            kickOutTextField.setBounds(700, 470, 250, 50);
            frame.add(kickOutTextField);

            JButton kickOutButton = new JButton("Kick out");
            kickOutButton.setBounds(700, 530, 250, 30);
            kickOutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
            frame.setTitle(APP_TITLE + " (User)");
        }

        JLabel jLabel = new JLabel("Users: username (uid)");
        jLabel.setSize(jLabel.getPreferredSize());
        jLabel.setLocation(700, 5);
        frame.add(jLabel);


        JRadioButton circleButton = new JRadioButton(CIRCLE);
        circleButton.setBounds(10, 5, 100, 40);
        circleButton.setActionCommand(CIRCLE);
        circleButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);
        circleButton.doClick();

        JRadioButton rectangleButton = new JRadioButton(RECTANGLE);
        rectangleButton.setBounds(120, 5, 100, 40);
        rectangleButton.setActionCommand(RECTANGLE);
        rectangleButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        JRadioButton lineButton = new JRadioButton(LINE);
        lineButton.setBounds(230, 5, 100, 40);
        lineButton.setActionCommand(LINE);
        lineButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        JRadioButton textButton = new JRadioButton(TEXT);
        textButton.setBounds(340, 5, 100, 40);
        textButton.setActionCommand(TEXT);
        textButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        ButtonGroup group = new ButtonGroup();
        group.add(circleButton);
        group.add(rectangleButton);
        group.add(lineButton);
        group.add(textButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 430, 40);
        buttonPanel.add(circleButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(lineButton);
        buttonPanel.add(textButton);

        frame.add(buttonPanel);

        this.whiteboardCanvasPanel = new WhiteboardCanvasPanel(paintManager);
        whiteboardCanvasPanel.setBounds(10, 50, CANVAS_WIDTH, CANVAS_HEIGHT);
        frame.add(whiteboardCanvasPanel);

//        frame.setVisible(true);
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void setRemoteCanvas(IRemoteCanvas remoteCanvas) {
        paintManager.setRemoteCanvas(remoteCanvas);

//        updateCanvasThread = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                while (true) {
//                    try {
//                        whiteboardCanvasPanel.getGraphics()
//                                .drawImage(paintManager.getRemoteCanvas().getCanvas().getImage(),
//                                        0,
//                                        0,
//                                        whiteboardCanvasPanel.getWidth(),
//                                        whiteboardCanvasPanel.getHeight(),
//                                        null);
//                        whiteboardCanvasPanel.repaint();
//                    } catch (NullPointerException e) {
//                        System.out.println("Null pointer in update canvas");
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                        popupNoServerConnectionErrorDialog();
//                        break;
//                    }
//
//                    try {
//                        Thread.sleep(REMOTE_OBJECT_UI_UPDATE_RATE);
//                    } catch (InterruptedException e) {
//                        System.out.println("Thread sleep error");
//                        break;
//                    }
//                }
//            }
//        };
//
//        updateCanvasThread.start();
    }

    public void setRemoteUserList(IRemoteUserList remoteUserList) {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setBounds(700, 30, 250, 400);
        frame.add(jTextArea);

        this.remoteUserList = remoteUserList;

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

    public void start(String uid) {
        this.uid = uid;

        frame.setVisible(true);
    }

    public void error(String msg) {
        popupErrorDialog(msg);
        stop();
    }

    public void stop() {
        updateUserListThread.interrupt();
//        updateCanvasThread.interrupt();
    }

    public boolean isManager() {
        return isManager;
    }

    public boolean askAcceptCandidate(String candidateUID) {
        TimeDialog dialog = new TimeDialog();
        return dialog.showDialog(frame, candidateUID + SHARE_PROMPT, ASK_MANAGER_JOIN_TIMEOUT);
    }

    public void kickedOut() {
        frame.setTitle(frame.getTitle() + " - [Kicked out]");
        stop();
        popupDialog("Kicked out by the manager");
        isKickedOut = true;
        closeFrame();
    }

    private void closeFrame() {
        if (!isKickedOut) {
            clientConnection.disconnect(isManager, uid);
        }
        frame.dispose();
        System.exit(0);
    }

    private void closeFrame(WindowEvent e) {
        if (!isKickedOut) {
            clientConnection.disconnect(isManager, uid);
        }
        stop();
        e.getWindow().dispose();
        System.exit(0);
    }

    public void closeByManager() {
        frame.setTitle(frame.getTitle() + " - [Closed]");
        stop();
        popupDialog("Closed by the manager");
        isKickedOut = true;
        closeFrame();
    }
}
