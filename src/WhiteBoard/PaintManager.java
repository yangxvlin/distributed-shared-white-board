package WhiteBoard;

import remote.IRemoteCanvas;
import remote.SerializableBufferedImage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;

import static WhiteBoard.Util.popupDialog;
import static WhiteBoard.Util.popupNoServerConnectionErrorDialog;
import static WhiteBoard.Util.saveImage;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-12 16:03
 * description: manager user's request to paint on the canvas
 **/

public class PaintManager {
    /**
     * user's using tool
     */
    private String selectedToolName;

    /**
     * remote canvas object
     */
    private IRemoteCanvas remoteCanvas;

    /**
     * end point for drawing lines
     */
    private Point lastPoint;

    /**
     * start point for drawing lines.
     */
    private Point firstPoint;

    /**
     * user's last update time
     */
    private long lastUpdateTime;

    /**
     * listener for using changing the tools
     */
    public final ActionListener PAINT_TOOL_ACTION_LISTENER = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[" + PaintManager.class.getSimpleName() + "] " + selectedToolName + " change to " + e.getActionCommand());
            selectedToolName = e.getActionCommand();
            clearPoints();
        }
    };

    public PaintManager() {
    }

    /**
     * @return selected Tool Name
     */
    public String getSelectedToolName() {
        return selectedToolName;
    }

    /**
     * @param remoteCanvas remote canvas object
     */
    public void setRemoteCanvas(IRemoteCanvas remoteCanvas) {
        this.remoteCanvas = remoteCanvas;
    }

    public IRemoteCanvas getRemoteCanvas() {
        return remoteCanvas;
    }

    /**
     * clear drawing points
     */
    public void clearPoints() {
        System.out.println("    | clear all drawing points");
        firstPoint = null;
        lastPoint = null;
    }

    /**
     * clear remote canvas
     */
    public void clearCanvas() {
        try {
            remoteCanvas.clearAll();
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    /**
     * @param p location of text
     * @param text text string
     */
    public void drawText(Point p, String text) {
        try {
            remoteCanvas.drawText(text, p.x, p.y);
        } catch (RemoteException e) {
            System.out.println("Error to draw to remote.");
            popupNoServerConnectionErrorDialog();
        }
    }

    /**
     * @param newPoint one point of rectangle
     */
    public void drawRectangle(Point newPoint) {
        if (firstPoint == null) {
            firstPoint = newPoint;
            System.out.println("    | Input firstPoint: " + firstPoint.toString());
        } else {
            lastPoint = newPoint;

            Dimension rectSize = null;
            // Calculate the rectangle size.
            if (firstPoint.x <= lastPoint.x && firstPoint.y <= lastPoint.y) {
                rectSize = new Dimension(
                        lastPoint.x - firstPoint.x,
                        lastPoint.y - firstPoint.y
                );
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y >= lastPoint.y) {
                rectSize = new Dimension(
                        firstPoint.x - lastPoint.x,
                        firstPoint.y - lastPoint.y
                );
                firstPoint = new Point(lastPoint.x, lastPoint.y);
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y <= lastPoint.y) {
                rectSize = new Dimension(
                        firstPoint.x - lastPoint.x,
                        lastPoint.y - firstPoint.y
                );
                firstPoint = new Point(lastPoint.x, firstPoint.y);
            } else if (firstPoint.x <= lastPoint.x && firstPoint.y >= lastPoint.y) {
                rectSize = new Dimension(
                        lastPoint.x - firstPoint.x,
                        firstPoint.y - lastPoint.y
                );
                firstPoint = new Point(firstPoint.x, lastPoint.y);
            } else {
                System.out.println("    | unknown rectangle position");
                this.clearPoints();
                return;
            }

            // Draw the rectangle to remote.
            try {
                remoteCanvas.drawRectangle(firstPoint.x, firstPoint.y, rectSize.width, rectSize.height);
            } catch (RemoteException e) {
                popupNoServerConnectionErrorDialog();
            }

            System.out.println("    | complete drawing");
            this.clearPoints();
        }
    }

    /**
     * @param newPoint point of a line
     */
    public void drawLine(Point newPoint) {
        if (firstPoint == null) {
            firstPoint = newPoint;
            System.out.println("    | Input firstPoint: " + firstPoint.toString());
        } else {
            lastPoint = newPoint;

            try {
                remoteCanvas.drawLine(firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y);
            } catch (RemoteException e) {
                popupNoServerConnectionErrorDialog();
            }

            System.out.println("    | draw lastPoint: " + lastPoint.toString());
            // now lastPoint become the start point of next line
            firstPoint = lastPoint;
        }
    }

    /**
     * @param newPoint point of a circle
     */
    public void drawCircle(Point newPoint) {
        if (firstPoint == null) {
            firstPoint = newPoint;
            System.out.println("    | Input firstPoint: " + firstPoint.toString());
        } else {
            lastPoint = newPoint;

            int radius = (int) Math.round(firstPoint.distance(lastPoint));
            Point topLeft = null;
            // Calculate the circle center
            if (firstPoint.x <= lastPoint.x && firstPoint.y <= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y >= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y <= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else if (firstPoint.x <= lastPoint.x && firstPoint.y >= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else {
                System.out.println("    | unknown rectangle position");
                this.clearPoints();
                return;
            }


            // Draw the rectangle to remote.
            try {
                remoteCanvas.drawCircle(topLeft.x, topLeft.y, 2 * radius, 2 * radius);
            } catch (RemoteException e) {
                popupNoServerConnectionErrorDialog();
            }

            System.out.println("    | complete drawing");
            this.clearPoints();
        }
    }

    /**
     * @param newPoint one point of freedom line by pen
     */
    public void drawPen(Point newPoint) {
        long curTime = System.currentTimeMillis();
        // Limit how regularly new line segments are created to avoid saturating the network with line segments.
        if ((curTime - lastUpdateTime) < 50) {
            return;
        }
        lastUpdateTime = curTime;
        if (!newPoint.equals(lastPoint)) {
            drawLine(newPoint);
        }
    }

    /**
     * @param image loaded image
     */
    public void setImage(BufferedImage image) {
        try {
            remoteCanvas.setImage(new SerializableBufferedImage(image));
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        }
        clearPoints();
    }

    /**
     * @param fileName file to be saved to
     */
    public void saveWhiteBoard(String fileName) {
        try {
            saveImage(this.getRemoteCanvas().getCanvas().getImage(), fileName);
            popupDialog("Successful save whiteboard to image: " + fileName);
            System.out.println("    | Successful ave whiteboard to image: " + fileName);
        } catch (RemoteException e1) {
            popupNoServerConnectionErrorDialog();
        }
    }
}
