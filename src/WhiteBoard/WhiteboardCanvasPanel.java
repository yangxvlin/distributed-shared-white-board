package WhiteBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.RemoteException;

import static WhiteBoard.Contant.*;
import static WhiteBoard.Util.popupNoServerConnectionErrorDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-12 21:30
 * description:
 **/

public class WhiteboardCanvasPanel extends JPanel implements MouseListener, MouseMotionListener {

    private PaintManager paintManager;

    public WhiteboardCanvasPanel(PaintManager paintManager) {
        this.paintManager = paintManager;
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            g.drawImage(paintManager.getRemoteCanvas().getCanvas().getImage(),
                            0,
                            0,
                            getWidth(),
                            getHeight(),
                            null);
            this.repaint();
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        } catch (NullPointerException e) {
            System.out.println("    | canvas panel <paintComponent> null pointer");
        }
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
//        canvas.requestFocusInWindow();
//        e = SwingUtilities.convertMouseEvent(e.getComponent(), e, this);
        Point newPoint = e.getPoint();
//        newPoint = new Point(newPoint.x + 10, newPoint.y + 50);

        String toolSelected = paintManager.getSelectedToolName();

        switch (toolSelected) {
            case LINE:
                if (isLeftMouseButton(e)) {
                    // Set origin/ end point.
                    paintManager.drawLine(newPoint);
                } else if (isRightMouseButton(e)) {
                    // Cancel drawing.
                    paintManager.clearPoints();
                } else {
                    System.out.println("    | Unknown button command to draw rectangle");
                }
                break;
            case CIRCLE:
                if (isLeftMouseButton(e)) {
                    // Set origin/ end point.
                    paintManager.drawCircle(newPoint);
                } else if (isRightMouseButton(e)) {
                    // Cancel drawing.
                    paintManager.clearPoints();
                } else {
                    System.out.println("    | Unknown button command to draw rectangle");
                }
                break;
            case RECTANGLE:
                if (isLeftMouseButton(e)) {
                    // Set origin/end point.
                    paintManager.drawRectangle(newPoint);
                } else if (isRightMouseButton(e)) {
                    // Cancel drawing.
                    paintManager.clearPoints();
                } else {
                    System.out.println("    | Unknown button command to draw rectangle");
                }
                break;
            case TEXT:
                String data = showInputDialog("Please input the text:");
                if (data != null && !data.isEmpty()) {
                    System.out.println(data);
                    paintManager.drawText(newPoint, data);
                } else {
                    System.out.println("    | user cancel to input text or input nothing");
                }
                break;
            case PEN:
//                paintManager.clearPoints();
                System.out.println("    | Pen clicked");
                break;
            default:
                System.out.println("Drawing tool not implemented: " + toolSelected);
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        String toolSelected = paintManager.getSelectedToolName();
        switch (toolSelected) {
            case PEN:
                System.out.println("    | Pen released");
                paintManager.clearPoints();
                break;
            default:
                System.out.println("Unknown tool dragged");
        }
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point newPoint = e.getPoint();
        String toolSelected = paintManager.getSelectedToolName();
        switch (toolSelected) {
            case PEN:
                System.out.println("    | Pen dragged");
                paintManager.drawPen(newPoint);
                break;
            default:
                System.out.println("Unknown tool dragged");
        }
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
