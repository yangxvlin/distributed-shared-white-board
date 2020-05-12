package WhiteBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
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

import static WhiteBoard.Util.popupDialog;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-08 15:14
 * description:
 **/

public class FileMenu extends JMenu {
    public FileMenu(Frame frame, PaintManager paintManager) {
        super("File (Alt+F)");
        setMnemonic(KeyEvent.VK_F);

        JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New pressed");
                paintManager.clearCanvas();
                paintManager.clearPoints();
            }
        });
        add(newMenuItem);

        JMenuItem open = new JMenuItem("Open", KeyEvent.VK_O);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open pressed");

                final JFileChooser fc = new JFileChooser();
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

                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(file);
                        paintManager.setImage(img);
                    } catch (IOException e1) {
                        popupDialog("Read image fail.");
                    }
                }
            }
        });
        add(open);

        JMenuItem save = new JMenuItem("Save", KeyEvent.VK_S);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save pressed");
            }
        });
        add(save);

        JMenuItem saveAs = new JMenuItem("Save As", KeyEvent.VK_A);
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save As pressed");
            }
        });
        add(saveAs);

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
}
