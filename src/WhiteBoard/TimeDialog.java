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
 * description:
 **/

public class TimeDialog {
    private int seconds = 0;
    private JLabel label = new JLabel();
    private JDialog dialog = null;
    private boolean result = REJECTED;
    public boolean showDialog(JFrame father, String message, int sec) {

        String message1 = message;
        seconds = sec;

        label.setText(message);
        label.setBounds(80,6, 400, 40);

        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();

        JButton confirm = new JButton("Accept");
        confirm.setBounds(100,40,80,20);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = ACCEPTED;
                father.setEnabled(true);
                TimeDialog.this.dialog.dispose();
            }
        });

        JButton cancel = new JButton("Reject");
        cancel.setBounds(270,40,80,20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = REJECTED;
                father.setEnabled(true);
                TimeDialog.this.dialog.dispose();
            }
        });

        dialog = new JDialog(father, true);
        father.setEnabled(false);
        dialog.setTitle("Note: Auto reject after: "+ seconds +" seconds");
        dialog.setLayout(null);
        dialog.add(label);
        dialog.add(confirm);
        dialog.add(cancel);

        s.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

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
        dialog.setLocationRelativeTo(father);
        dialog.setVisible(true);

        return result;
    }
}
