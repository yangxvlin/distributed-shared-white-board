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
 * description:
 **/

public class PaintManager {
    private String selectedToolName;

    private IRemoteCanvas remoteCanvas;

    // Points for drawing lines.
    private Point lastPoint;

    private Point firstPoint;

    private long lastUpdateTime;

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

    public String getSelectedToolName() {
        return selectedToolName;
    }

    public void setRemoteCanvas(IRemoteCanvas remoteCanvas) {
        this.remoteCanvas = remoteCanvas;
    }

    public IRemoteCanvas getRemoteCanvas() {
        return remoteCanvas;
    }

    public Point getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(Point lastPoint) {
        this.lastPoint = lastPoint;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public void clearPoints() {
        System.out.println("    | clear all drawing points");
        firstPoint = null;
        lastPoint = null;
    }

    public void clearCanvas() {
        try {
            remoteCanvas.clearAll();
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    public void drawText(Point p, String text) {
        try {
            remoteCanvas.drawText(text, p.x, p.y);
        } catch (RemoteException e) {
            System.out.println("Error to draw to remote.");
            popupNoServerConnectionErrorDialog();
        }
    }

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

    public void drawPen(Point newPoint) {
        long curTime = System.currentTimeMillis();
        // Limit how regularly new line segments are created to avoid
        // saturating the network with line segments.
        if ((curTime - lastUpdateTime) < 50) {
            return;
        }
        lastUpdateTime = curTime;
        if (!newPoint.equals(lastPoint)) {
            drawLine(newPoint);
        }
    }

    public void setImage(BufferedImage image) {
        try {
            remoteCanvas.setImage(new SerializableBufferedImage(image));
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        }
        clearPoints();
    }

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
