package WhiteBoard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-12 16:03
 * description:
 **/

public class PaintManager {
    private String selectedToolName;

    public final ActionListener PAINT_TOOL_ACTION_LISTENER = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[" + PaintManager.class.getSimpleName() + "] " + selectedToolName + " change to " + e.getActionCommand());
            selectedToolName = e.getActionCommand();
        }
    };

    public PaintManager() {

    }

    public String getSelectedToolName() {
        return selectedToolName;
    }

    public void setSelectedToolName(String selectedToolName) {
        this.selectedToolName = selectedToolName;
    }
}
