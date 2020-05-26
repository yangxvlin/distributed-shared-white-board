package remote;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static WhiteBoard.PaintConstant.*;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-12 16:27
 * description: implementation for remote object for canvas
 **/

public class RemoteCanvas extends UnicastRemoteObject implements IRemoteCanvas {
    /**
     * remote canvas's image
     */
    private SerializableBufferedImage canvas;

    public RemoteCanvas() throws RemoteException {
        canvas = new SerializableBufferedImage(new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB));
    }

    /**
     * @return SerializableBufferedImage for canvas
     */
    public SerializableBufferedImage getCanvas() throws RemoteException {
        return canvas;
    }

    /**
     * @param t text string
     * @param x x coordinate
     * @param y y coordinate
     */
    @Override
    public void drawText(String t, int x, int y) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString(t, x, y);
    }

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of rectangle
     * @param height height of rectangle
     */
    @Override
    public void drawRectangle(int x, int y, int width, int height) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    /**
     * @param x1 start point's x coordinate
     * @param y1 start point's y coordinate
     * @param x2 end point's x coordinate
     * @param y2 end point's y coordinate
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);
    }

    /**
     * @param x circle's center's x coordinate
     * @param y circle's center's y coordinate
     * @param width circle's width
     * @param height circle's height
     */
    @Override
    public void drawCircle(int x, int y, int width, int height) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
    }

    /**
     * @param image image for canvas
     */
    @Override
    public void setImage(SerializableBufferedImage image) throws RemoteException {
        canvas = image;
    }

    /**
     * clear the canvas
     */
    @Override
    public void clearAll() throws RemoteException {
        canvas.clear();
    }
}
