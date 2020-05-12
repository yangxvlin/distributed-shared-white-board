package remote;

import java.awt.image.BufferedImage;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteCanvas extends Remote {

    SerializableBufferedImage getCanvas() throws RemoteException;

    void drawText(String t, int x, int y) throws RemoteException;

    void drawRectangle(int x, int y, int width, int height) throws RemoteException;

    void drawLine(int x1, int y1, int x2, int y2) throws RemoteException;

    void drawCircle(int x, int y, int width, int height) throws RemoteException;

    void setImage(BufferedImage image) throws RemoteException;

    void clearAll() throws RemoteException;
}
