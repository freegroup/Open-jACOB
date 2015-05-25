//
//
//
// Source File Name:   com/nwoods//FREERectangle

package de.shorti.dragdrop;


import java.awt.*;

// Referenced classes of package de.shorti.dragdrop:
//            FREEDrawable, FREEObject, FREECopyEnvironment, FREEView

public class FREERectangle extends FREEDrawable
{

    public FREERectangle()
    {
    }

    public FREERectangle(Rectangle rectangle)
    {
        super(rectangle);
    }

    public FREERectangle(Point point, Dimension dimension)
    {
        super(point, dimension);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREERectangle rectangle = (FREERectangle)super.copyObject(copyEnv);
        if(rectangle == null);
        return rectangle;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        Rectangle rectangle = getBoundingRect();
        drawRect(graphics2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
