package de.shorti.dragdrop;


import java.awt.*;
import java.net.URL;
import java.awt.geom.*;
import java.awt.image.*;

public class FREEImage extends FREEObject    implements ImageObserver
{

    public FREEImage()
    {
        image = null;
        m_uRL = null;
        m_filename = null;
        m_transparentColor = null;
    }

    public FREEImage(Point point, Dimension dimension)
    {
        super(point, dimension);
        image = null;
        m_uRL = null;
        m_filename = null;
        m_transparentColor = null;
    }

    public FREEImage(Rectangle rectangle)
    {
        super(rectangle);
        image = null;
        m_uRL = null;
        m_filename = null;
        m_transparentColor = null;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEImage img = (FREEImage)super.copyObject(copyEnv);
        if(img != null)
        {
            img.image = image;
            img.m_uRL = m_uRL;
            img.m_filename = m_filename;
            img.m_transparentColor = m_transparentColor;
        }
        return img;
    }

    public boolean loadImage(Image image, boolean flag)
    {
        this.image = image;
        if(flag)
        {
            MediaTracker mediatracker = new MediaTracker(FREEGlobal.getComponent());
            mediatracker.addImage(image, 0);
            try
            {
                mediatracker.waitForID(0);
            }
            catch(InterruptedException interruptedexception)
            {
                return false;
            }
        }
        return true;
    }

    public boolean loadImage(URL url, boolean flag)
    {
        m_uRL = url;
        m_filename = null;
        return loadImage(FREEGlobal.getToolkit().getImage(url), flag);
    }

    public boolean loadImage(String s, boolean flag)
    {
        m_uRL = null;
        m_filename = s;
        return loadImage(FREEGlobal.getToolkit().getImage(s), flag);
    }

    public Image getImage()
    {
        return image;
    }

    public void flipX()
    {
        ImageFilter   replicate = new ReplicateScaleFilter(getWidth(), -getHeight());
        ImageProducer prod      = new FilteredImageSource(image.getSource(),replicate);
        loadImage(Toolkit.getDefaultToolkit().createImage(prod),true);
    }

    public URL getURL()
    {
        return m_uRL;
    }

    public String getFilename()
    {
        return m_filename;
    }

    public Color getTransparentColor()
    {
        return m_transparentColor;
    }

    public void setTransparentColor(Color color)
    {
        Color color1 = getTransparentColor();
        if(color1 == null)
        {
            if(color != null)
            {
                m_transparentColor = color;
                update(903);
            }
        } else
        if(!color1.equals(color))
        {
            m_transparentColor = color;
            update(903);
        }
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        if(getImage() == null)
            if(getURL() != null)
                loadImage(getURL(), false);
            else
            if(getFilename() != null)
                loadImage(getFilename(), false);
        if(getImage() != null)
        {
            Rectangle rectangle = getBoundingRect();
            if(getTransparentColor() == null)
                graphics2d.drawImage(getImage(), rectangle.x, rectangle.y, rectangle.width, rectangle.height, this);
            else
                graphics2d.drawImage(getImage(), rectangle.x, rectangle.y, rectangle.width, rectangle.height, getTransparentColor(), this);
        }
    }

    public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1)
    {
        if((i & 0x20) != 0)
        {
            update(902);
            return false;
        } else
        {
            return true;
        }
    }

    public static final int ChangedImage = 901;
    public static final int ChangedImageUpdate = 902;
    public static final int ChangedTransparentColor = 903;
    private transient Image image;
    private URL m_uRL;
    private String m_filename;
    private Color m_transparentColor;
}
