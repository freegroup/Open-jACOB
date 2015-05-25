package de.shorti.dragdrop;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

// Referenced classes of package de.shorti.dragdrop:
//            FREEDrawable, FREEObject, FREECopyEnvironment, FREEView

public class FREERoundRect extends FREEDrawable
{

    public FREERoundRect()
    {
        m_arcDimension = new Dimension();
        C0(5, 5);
    }

    public FREERoundRect(Dimension dimension)
    {
        m_arcDimension = new Dimension();
        C0(dimension.width, dimension.height);
    }

    public FREERoundRect(Rectangle rectangle, Dimension dimension)
    {
        super(rectangle);
        m_arcDimension = new Dimension();
        C0(dimension.width, dimension.height);
    }

    public FREERoundRect(Point point, Dimension dimension, Dimension dimension1)
    {
        super(point, dimension);
        m_arcDimension = new Dimension();
        C0(dimension1.width, dimension1.height);
    }

    private final void C0(int i, int j)
    {
        m_arcDimension.width = i;
        m_arcDimension.height = j;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREERoundRect roundrect = (FREERoundRect)super.copyObject(copyEnv);
        if(roundrect != null)
            roundrect.m_arcDimension.setSize(m_arcDimension);
        return roundrect;
    }

    public Dimension getArcDimension()
    {
        return m_arcDimension;
    }

    public void setArcDimension(Dimension dimension)
    {
        Dimension dimension1 = getArcDimension();
        if(dimension1.width != dimension.width || dimension1.height != dimension.height)
        {
            m_arcDimension = dimension;
            update(404);
        }
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        Rectangle rectangle = getBoundingRect();
        drawRoundRect(graphics2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height, m_arcDimension.width, m_arcDimension.height);
    }

    public static final int ChangedArcDimension = 404;
    private Dimension m_arcDimension;
}
