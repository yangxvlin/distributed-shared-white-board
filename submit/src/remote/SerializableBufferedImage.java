package remote;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import static WhiteBoard.PaintConstant.CANVAS_HEIGHT;
import static WhiteBoard.PaintConstant.CANVAS_WIDTH;

/**
 * Xulin Yang, 904904
 *
 * @create 2020-05-12 22:41
 * description: buffered image can be serialized
 **/

public class SerializableBufferedImage implements Serializable {
    /**
     * image
     */
    private BufferedImage image = null;

    public SerializableBufferedImage() {
        super();
    }

    public SerializableBufferedImage(BufferedImage im) {
        this();
        setImage(im);
    }

    /**
     * @return image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param img image
     */
    public void setImage(BufferedImage img) {
        this.image = img;
    }

    /**
     * @param out output
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ImageIO.write(getImage(), "jpg", new MemoryCacheImageOutputStream(out));
    }

    /**
     * @param in input
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        setImage(ImageIO.read(new MemoryCacheImageInputStream(in)));
    }

    /**
     * clear the image
     */
    public void clear() {
        System.out.println("canvas cleared");
        image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }
}
