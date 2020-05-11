package WhiteBoard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-08 15:14
 * description:
 **/

public class FileMenu extends JMenu {
    public FileMenu() {
        super("File (Alt+F)");
        setMnemonic(KeyEvent.VK_F);

        JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New pressed");
            }
        });
        add(newMenuItem);

        JMenuItem open = new JMenuItem("Open", KeyEvent.VK_O);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open pressed");
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
            }
        });
        add(close);
    }
}
