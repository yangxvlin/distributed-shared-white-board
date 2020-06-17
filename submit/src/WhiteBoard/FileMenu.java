package WhiteBoard;

import Communication.ClientConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static Communication.CommunicationConstant.*;
import static WhiteBoard.Util.*;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-08 15:14
 * description: manager's file menu
 **/

public class FileMenu extends JMenu {
    /**
     * connection to server
     */
    private ClientConnection clientConnection;

    /**
     * @param frame parent frame
     * @param paintManager paint manager
     */
    public FileMenu(Frame frame, PaintManager paintManager) {
        super("File (Alt+F)");
        this.clientConnection = null;
        setMnemonic(KeyEvent.VK_F);

        // New a canvas
        JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New pressed");
                // clear canvas
                paintManager.clearCanvas();
                paintManager.clearPoints();
                // send notification
                if (clientConnection != null) {
                    clientConnection.notifyUserWithManagerOperation(MANAGER_NEW);
                }
            }
        });
        add(newMenuItem);

        // open a canvas
        JMenuItem open = new JMenuItem("Open", KeyEvent.VK_O);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open pressed");

                // choose a canvas
                final JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("IMAGE FILES", IMAGE_TYPE);
                fc.setFileFilter(filter);
                // Open the dialog using null as parent component if you are outside a
                // Java Swing application otherwise provide the parent comment instead
                int returnVal = fc.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    // Retrieve the selected file
                    File file = fc.getSelectedFile();
                    try (FileInputStream fis = new FileInputStream(file)) {
                    } catch (FileNotFoundException e1) {
                        popupDialog("File not found.");
                        return;
                    } catch (IOException e1) {
                        popupDialog("Read file fail.");
                        return;
                    }
                    // read canvas and update
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(file);
                        paintManager.setImage(img);
                        popupDialog("Successful open image: " + file.getName());
                        if (clientConnection != null) {
                            clientConnection.notifyUserWithManagerOperation(MANAGER_OPEN);
                        }
                    } catch (IOException e1) {
                        popupDialog("Read image fail.");
                    }
                }
            }
        });
        add(open);

        // save a canvas to a auto generated file
        JMenuItem save = new JMenuItem("Save", KeyEvent.VK_S);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save pressed");
                String fileName = generateAutoFileName();
                paintManager.saveWhiteBoard(fileName);
            }
        });
        add(save);

        // save a canvas to a specified file
        JMenuItem saveAs = new JMenuItem("Save As", KeyEvent.VK_A);
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save As pressed");
                // select the location to be saved
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("IMAGE FILES", IMAGE_TYPE);
                fc.setFileFilter(filter);
                int returnVal = fc.showSaveDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    // Retrieve the selected file
                    File file = fc.getSelectedFile();
                    String fileName = file.getPath();
                    if (!fileName.endsWith("." + IMAGE_TYPE)) {
                        fileName += "." + IMAGE_TYPE;
                    }
                    // setup save to file
                    file = new File(fileName);
                    boolean result = false;
                    try {
                        result = file.createNewFile();
                    } catch (IOException e1) {
                        popupDialog("File creation fail: " + file);
                        return;
                    }
                    if (result) {
                        System.out.println("    | Successful create image file: " + file);
                    }
                    // save to file
                    paintManager.saveWhiteBoard(fileName);
                }
            }
        });
        add(saveAs);

        // close the application
        JMenuItem close = new JMenuItem("Close", KeyEvent.VK_C);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Close pressed");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        add(close);
    }

    /**
     * @param clientConnection client's connection to server
     */
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }
}
