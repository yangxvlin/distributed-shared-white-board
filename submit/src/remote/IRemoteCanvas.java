package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-11 1:22
 * description: remote object for canvas
 **/

public interface IRemoteCanvas extends Remote {

    /**
     * @return SerializableBufferedImage for canvas
     */
    SerializableBufferedImage getCanvas() throws RemoteException;

    /**
     * @param t text string
     * @param x x coordinate
     * @param y y coordinate
     */
    void drawText(String t, int x, int y) throws RemoteException;

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of rectangle
     * @param height height of rectangle
     */
    void drawRectangle(int x, int y, int width, int height) throws RemoteException;

    /**
     * @param x1 start point's x coordinate
     * @param y1 start point's y coordinate
     * @param x2 end point's x coordinate
     * @param y2 end point's y coordinate
     */
    void drawLine(int x1, int y1, int x2, int y2) throws RemoteException;

    /**
     * @param x circle's center's x coordinate
     * @param y circle's center's y coordinate
     * @param width circle's width
     * @param height circle's height
     */
    void drawCircle(int x, int y, int width, int height) throws RemoteException;

    /**
     * @param image image for canvas
     */
    void setImage(SerializableBufferedImage image) throws RemoteException;

    /**
     * clear the canvas
     */
    void clearAll() throws RemoteException;
}
