
package de.shorti.dragdrop.examples;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.shorti.dragdrop.FREERectangle;
import de.shorti.dragdrop.FREEView;

// Referenced classes of package de.shorti.dragdrop.examples:
//            ListArea

public class ListAreaRect extends FREERectangle
{

    public ListAreaRect()
    {
    }

    protected Rectangle handleResize(Graphics2D graphics2d, FREEView view, Rectangle rectangle, Point point, int i, int j, int k,
            int l)
    {
        if(getParent() == null)
        {
            return super.handleResize(graphics2d, view, rectangle, point, i, j, k, l);
        } else
        {
            ListArea listarea = (ListArea)getParent();
            Dimension dimension = listarea.getMinimumSize();
            return super.handleResize(graphics2d, view, rectangle, point, i, j, dimension.width, dimension.height);
        }
    }

    public void setBoundingRect(int i, int j, int k, int l)
    {
        if(getParent() == null)
        {
            return;
        } else
        {
            ListArea listarea = (ListArea)getParent();
            Dimension dimension = listarea.getMinimumSize();
            super.setBoundingRect(i, j, Math.max(k, dimension.width), Math.max(l, dimension.height));
            return;
        }
    }

    protected void geometryChange(Rectangle rectangle)
    {
        if(rectangle.width != getWidth() || rectangle.height != getHeight())
        {
            if(getParent() == null)
                return;
            ListArea listarea = (ListArea)getParent();
            listarea.layoutChildren();
        } else
        {
            super.geometryChange(rectangle);
        }
    }
}
