package de.shorti.dragdrop.examples;

import de.shorti.dragdrop.*;
import java.awt.*;

public class SimpleNode extends FREEArea
{

    public SimpleNode()
    {
        m_label = null;
        m_icon = null;
        m_inputPort = null;
        m_outputPort = null;
    }

    public void initialize(Point point, Dimension dimension, FREEObject object, String s, boolean flag, boolean flag1)
    {
        setSelectable(false);
        setGrabChildSelection(true);
        setDraggable(true);
        setResizable(false);
        set4ResizeHandles(true);
        m_icon = object;
        if(m_icon != null)
        {
            m_icon.setBoundingRect(point, dimension);
            m_icon.setSelectable(false);
            addObjectAtHead(m_icon);
        }
        if(s != null)
            m_label = new SimpleNodeLabel(s, this);
        if(flag)
            m_inputPort = new SimpleNodePort(true, this);
        if(flag1)
            m_outputPort = new SimpleNodePort(false, this);
        layoutChildren();
        setTopLeft(point);
    }

    public void copyChildren(FREEArea area, FREECopyEnvironment copyEnv)
    {
        SimpleNode simplenode = (SimpleNode)area;
        if(m_icon != null)
        {
            simplenode.m_icon = m_icon.copyObject(copyEnv);
            simplenode.addObjectAtHead(simplenode.m_icon);
        }
        if(m_label != null)
        {
            simplenode.m_label = (FREEText)m_label.copyObject(copyEnv);
            simplenode.addObjectAtTail(simplenode.m_label);
        }
        if(m_inputPort != null)
        {
            simplenode.m_inputPort = (FREEPort)m_inputPort.copyObject(copyEnv);
            simplenode.addObjectAtTail(simplenode.m_inputPort);
        }
        if(m_outputPort != null)
        {
            simplenode.m_outputPort = (FREEPort)m_outputPort.copyObject(copyEnv);
            simplenode.addObjectAtTail(simplenode.m_outputPort);
        }
    }

    public void layoutChildren()
    {
        if(m_icon != null)
        {
            m_icon.setSpotLocation( FREEObject.TopCenter, this, 2);
            int i = getHeight();
            if(m_label != null)
                i -= m_label.getHeight();
            if(i > m_icon.getHeight())
                m_icon.setTop(m_icon.getTop() + (i - m_icon.getHeight()) / 2);
        }
        if(m_label != null)
            if(m_icon != null)
                m_label.setSpotLocation( FREEObject.TopCenter, m_icon, 6);
            else
                m_label.setSpotLocation( FREEObject.BottomCenter, this, 6);
        if(m_inputPort != null)
            if(m_icon != null)
                m_inputPort.setSpotLocation( FREEObject.RightCenter, m_icon, 8);
            else
                m_inputPort.setSpotLocation( FREEObject.LeftCenter, this, 8);
        if(m_outputPort != null)
            if(m_icon != null)
                m_outputPort.setSpotLocation( FREEObject.LeftCenter, m_icon, 4);
            else
                m_outputPort.setSpotLocation( FREEObject.RightCenter, this, 4);
    }

    public void geometryChange(Rectangle rectangle)
    {
        if(getWidth() != rectangle.width || getHeight() != rectangle.height)
        {
            if(m_icon != null)
            {
                int i = m_icon.getWidth();
                int j = m_icon.getHeight();
                if(i <= 0)
                    i = 1;
                double d = (double)j / (double)i;
                int k = getWidth();
                int l = getHeight();
                if(m_inputPort != null)
                    k -= m_inputPort.getWidth();
                if(m_outputPort != null)
                    k -= m_outputPort.getWidth();
                if(m_label != null)
                    l -= m_label.getHeight();
                double d1 = (double)l / (double)k;
                if(d < d1)
                    l = (int)Math.rint(d * (double)k);
                else
                    k = (int)Math.rint((double)l / d);
                m_icon.setSize(k, l);
            }
            layoutChildren();
        } else
        {
            super.geometryChange(rectangle);
        }
    }

    public Dimension getMinimumIconSize()
    {
        return new Dimension(20, 20);
    }

    public Dimension getMinimumSize()
    {
        int i = 0;
        int j = 0;
        if(m_inputPort != null)
            i += m_inputPort.getWidth();
        if(m_outputPort != null)
            i += m_outputPort.getWidth();
        if(m_label != null)
            j += m_label.getHeight();
        Dimension dimension = getMinimumIconSize();
        i += dimension.width;
        j += dimension.height;
        return new Dimension(i, j);
    }

    public void setBoundingRect(int i, int j, int k, int l)
    {
        Dimension dimension = getMinimumSize();
        super.setBoundingRect(i, j, Math.max(k, dimension.width), Math.max(l, dimension.height));
    }

    protected Rectangle handleResize(Graphics2D graphics2d, FREEView view, Rectangle rectangle, Point point, int i, int j, int k,
            int l)
    {
        Dimension dimension = getMinimumSize();
        Rectangle rectangle1 = super.handleResize(graphics2d, view, rectangle, point, i, j, dimension.width, dimension.height);
        if(j == 2)
            setBoundingRect(rectangle1);
        return null;
    }

    public boolean doMouseClick(int i, Point point, Point point1, FREEView view)
    {
        FREEText text = getLabel();
        if(text != null && text.isEditable() && text.isEditOnSingleClick())
        {
            FREEObject object = view.pickDocObject(point, false);
            if(object == text && object.getLayer() != null && object.getLayer().isModifiable())
            {
                text.doStartEdit(view, point1);
                return true;
            }
        }
        return false;
    }

    public FREEText getLabel()
    {
        return m_label;
    }

    public FREEObject getIcon()
    {
        return m_icon;
    }

    public FREEPort getInputPort()
    {
        return m_inputPort;
    }

    public FREEPort getOutputPort()
    {
        return m_outputPort;
    }

    protected FREEText m_label;
    protected FREEObject m_icon;
    protected FREEPort m_inputPort;
    protected FREEPort m_outputPort;
}
