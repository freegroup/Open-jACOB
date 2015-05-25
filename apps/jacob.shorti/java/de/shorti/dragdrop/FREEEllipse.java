package de.shorti.dragdrop;


import java.awt.*;

public class FREEEllipse extends FREEDrawable
{

    public FREEEllipse()
    {
    }

    public FREEEllipse(Rectangle rectangle)
    {
        super(rectangle);
    }

    public FREEEllipse(Point point, Dimension dimension)
    {
        super(point, dimension);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEEllipse ellipse = (FREEEllipse)super.copyObject(copyEnv);
        if(ellipse == null);
        return ellipse;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        drawEllipse(graphics2d, getBoundingRect());
    }

    public boolean isPointInObj(Point point)
    {
        if(!super.isPointInObj(point))
        {
            return false;
        } else
        {
            double d = (double)getWidth() / 2D;
            double d1 = (double)point.x - ((double)getLeft() + d);
            double d2 = (double)getHeight() / 2D;
            double d3 = (double)point.y - ((double)getTop() + d2);
            return (d1 * d1) / (d * d) + (d3 * d3) / (d2 * d2) <= 1.0D;
        }
    }
}
