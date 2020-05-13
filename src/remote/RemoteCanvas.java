package remote;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static WhiteBoard.Contant.*;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-12 16:27
 * description:
 **/

public class RemoteCanvas extends UnicastRemoteObject implements IRemoteCanvas {
    private SerializableBufferedImage canvas;

    public RemoteCanvas() throws RemoteException {
        canvas = new SerializableBufferedImage(new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB));
    }

    public SerializableBufferedImage getCanvas() throws RemoteException {
        return canvas;
    }

    @Override
    public void drawText(String t, int x, int y) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString(t, x, y);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawCircle(int x, int y, int width, int height) throws RemoteException {
        Graphics g = canvas.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
    }

    @Override
    public void setImage(SerializableBufferedImage image) throws RemoteException {
        canvas = image;
    }

    @Override
    public void clearAll() throws RemoteException {
        canvas.clear();
    }
}
