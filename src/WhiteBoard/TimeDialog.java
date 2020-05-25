package WhiteBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static WhiteBoard.WhiteBoardConstant.ACCEPTED;
import static WhiteBoard.WhiteBoardConstant.REJECTED;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 20:00
 * description: dialog with time counting down
 **/

public class TimeDialog {
    /**
     * second to count down
     */
    private int seconds = 0;

    /**
     * label to be displayed
     */
    private JLabel label = new JLabel();

    /**
     * java dialog
     */
    private JDialog dialog = null;

    /**
     * user's selection result for the dialog
     */
    private boolean result = REJECTED;

    /**
     * @param parent parent frame
     * @param message message to be displayed
     * @param sec seconds to be count down
     * @return true if user accept, false otherwise
     */
    public boolean showDialog(JFrame parent, String message, int sec) {
        seconds = sec;

        // setup label
        label.setText(message);
        label.setBounds(80,6, 400, 40);

        // user confirm the dialog
        JButton confirm = new JButton("Accept");
        confirm.setBounds(100,40,80,20);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = ACCEPTED;
                parent.setEnabled(true);
                TimeDialog.this.dialog.dispose();
            }
        });

        // user cancel the dialog
        JButton cancel = new JButton("Reject");
        cancel.setBounds(270,40,80,20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = REJECTED;
                parent.setEnabled(true);
                TimeDialog.this.dialog.dispose();
            }
        });

        // setup the dialog
        dialog = new JDialog(parent, true);
        parent.setEnabled(false);
        dialog.setTitle("Note: Auto reject after: "+ seconds +" seconds");
        dialog.setLayout(null);
        dialog.add(label);
        dialog.add(confirm);
        dialog.add(cancel);

        // scheduler for counting down and update GUI
        // select cancel for auto timeout
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        s.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                TimeDialog.this.seconds--;
                if (TimeDialog.this.seconds == 0) {
                    cancel.doClick();
                }else {
                    dialog.setTitle("Note: Auto reject after: "+ seconds +"seconds");
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        dialog.pack();
        dialog.setSize(new Dimension(500,150));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return result;
    }
}
