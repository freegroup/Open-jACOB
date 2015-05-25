//
//
//
// Source File Name:   BasicNode.java

package de.shorti.dragdrop.examples;



import de.shorti.dragdrop.*;
import java.awt.Point;
import java.awt.Rectangle;

// Referenced classes of package de.shorti.dragdrop.examples:
//            BasicNodePort

public class BasicNode extends FREEArea
{

    public BasicNode()
    {
        m_ellipse = null;
        m_label = null;
        m_port = null;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        BasicNode basicnode = (BasicNode)super.copyObject(copyEnv);
        if(basicnode == null);
        return basicnode;
    }

    public void copyChildren(FREEArea area, FREECopyEnvironment copyEnv)
    {
        BasicNode basicnode = (BasicNode)area;
        if(m_ellipse != null)
        {
            basicnode.m_ellipse = (FREEEllipse)m_ellipse.copyObject(copyEnv);
            basicnode.addObjectAtHead(basicnode.m_ellipse);
        }
        if(m_label != null)
        {
            basicnode.m_label = (FREEText)m_label.copyObject(copyEnv);
            basicnode.addObjectAtTail(basicnode.m_label);
        }
        if(m_port != null)
        {
            basicnode.m_port = (BasicNodePort)m_port.copyObject(copyEnv);
            basicnode.addObjectAtTail(basicnode.m_port);
            basicnode.m_port.m_ellipse = basicnode.m_ellipse;
        }
    }

    public void initialize(Point point, String s)
    {
        setSize(20, 20);
        setSelectable(false);
        setGrabChildSelection(true);
        setDraggable(true);
        setResizable(false);
        m_ellipse = new FREEEllipse(getTopLeft(), getSize());
        m_ellipse.setSelectable(false);
        m_ellipse.setDraggable(false);
        setLocation(point);
        if(s != null)
        {
            m_label = new FREEText(s);
            m_label.setSelectable(false);
            m_label.setDraggable(false);
            m_label.setAlignment(1);
            m_label.setTransparent(true);
        }
        m_port = new BasicNodePort();
        m_port.m_ellipse = m_ellipse;
        m_port.setSize(7, 7);
        addObjectAtHead(m_ellipse);
        if(m_label != null)
            addObjectAtTail(m_label);
        addObjectAtTail(m_port);
        layoutChildren();
    }

    public Point getLocation(Point point)
    {
        if(m_ellipse != null)
            return m_ellipse.getSpotLocation(0, point);
        else
            return getSpotLocation(0, point);
    }

    public void setLocation(int i, int j)
    {
        if(m_ellipse != null)
            m_ellipse.setSpotLocation( FREEObject.Center, i, j);
        else
            setSpotLocation( FREEObject.Center, i, j);
        layoutChildren();
    }

    public void layoutChildren()
    {
        if(m_ellipse == null)
            return;
        if(m_label != null)
            m_label.setSpotLocation( FREEObject.BottomCenter, m_ellipse, 2);
        if(m_port != null)
            m_port.setSpotLocation( FREEObject.Center, m_ellipse, 0);
    }

    public void geometryChange(Rectangle rectangle)
    {
        if(rectangle.width == getWidth() && rectangle.height == getHeight())
        {
            super.geometryChange(rectangle);
        } else
        {
            double d = 1.0D;
            if(rectangle.width != 0)
                d = (double)getWidth() / (double)rectangle.width;
            double d1 = 1.0D;
            if(rectangle.height != 0)
                d1 = (double)getHeight() / (double)rectangle.height;
            if(m_ellipse != null)
            {
                int i = getLeft() + (int)Math.rint((double)(m_ellipse.getLeft() - rectangle.x) * d);
                int k = getTop() + (int)Math.rint((double)(m_ellipse.getTop() - rectangle.y) * d1);
                int i1 = (int)Math.rint((double)m_ellipse.getWidth() * d);
                int k1 = (int)Math.rint((double)m_ellipse.getHeight() * d1);
                m_ellipse.setBoundingRect(i, k, i1, k1);
            }
            if(m_port != null)
            {
                int j = getLeft() + (int)Math.rint((double)(m_port.getLeft() - rectangle.x) * d);
                int l = getTop() + (int)Math.rint((double)(m_port.getTop() - rectangle.y) * d1);
                int j1 = (int)Math.rint((double)m_port.getWidth() * d);
                int l1 = (int)Math.rint((double)m_port.getHeight() * d1);
                m_port.setBoundingRect(j, l, j1, l1);
            }
            layoutChildren();
        }
    }

    public FREEPen getPen()
    {
        return m_ellipse.getPen();
    }

    public void setPen(FREEPen pen)
    {
        m_ellipse.setPen(pen);
    }

    public FREEBrush getBrush()
    {
        return m_ellipse.getBrush();
    }

    public void setBrush(FREEBrush brush)
    {
        m_ellipse.setBrush(brush);
    }

    public FREEObject getEllipse()
    {
        return m_ellipse;
    }

    public FREEText getLabel()
    {
        return m_label;
    }

    public BasicNodePort getPort()
    {
        return m_port;
    }

    protected FREEEllipse m_ellipse;
    protected FREEText m_label;
    protected BasicNodePort m_port;
}
