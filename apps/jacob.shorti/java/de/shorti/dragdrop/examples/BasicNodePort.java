//
//
//
// Source File Name:   BasicNodePort.java

package de.shorti.dragdrop.examples;


import de.shorti.dragdrop.*;
import java.awt.Point;
import java.awt.Rectangle;

public class BasicNodePort extends FREEPort
{
    public BasicNodePort()
    {
        m_ellipse = null;
        setSelectable(false);
        setDraggable(false);
        setStyle(2);
        setFromSpot(NoSpot);
        setToSpot(NoSpot);
    }

    public Point getLinkPointFromPoint(int i, int j, Point point)
    {
        Rectangle rectangle = m_ellipse.getBoundingRect();
        int k = rectangle.width / 2;
        int l = rectangle.height / 2;
        int i1 = getLeft() + getWidth() / 2;
        int j1 = getTop() + getHeight() / 2;
        int k1 = i - i1;
        int l1 = j - j1;
        if(k1 == 0)
            k1 = 1;
        if(l1 == 0)
            l1 = 1;
        double d = Math.atan((double)l1 / (double)k1);
        if(point == null)
            point = new Point();
        if(k1 < 0)
        {
            point.x = i1 - (int)Math.rint((double)k * Math.cos(d));
            point.y = j1 - (int)Math.rint((double)l * Math.sin(d));
        } else
        {
            point.x = i1 + (int)Math.rint((double)k * Math.cos(d));
            point.y = j1 + (int)Math.rint((double)l * Math.sin(d));
        }
        return point;
    }

    public FREEEllipse m_ellipse;
}
