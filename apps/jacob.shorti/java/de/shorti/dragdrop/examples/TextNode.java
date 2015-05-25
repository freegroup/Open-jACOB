//
//
//
// Source File Name:   TextNode.java

package de.shorti.dragdrop.examples;


import java.awt.Point;
import java.awt.Rectangle;

import de.shorti.dragdrop.FREE3DRect;
import de.shorti.dragdrop.FREEArea;
import de.shorti.dragdrop.FREECopyEnvironment;
import de.shorti.dragdrop.FREEObject;
import de.shorti.dragdrop.FREEPort;
import de.shorti.dragdrop.FREERectangle;
import de.shorti.dragdrop.FREEText;
import de.shorti.dragdrop.FREEView;

public class TextNode extends FREEArea
{

    public TextNode()
    {
        m_label = null;
        m_rect = null;
        m_inPort = null;
        m_outPort = null;
        m_object = null;
    }

    public TextNode(String s)
    {
        m_label = null;
        m_rect = null;
        m_inPort = null;
        m_outPort = null;
        m_object = null;
        initialize(s);
    }

    public void initialize(String s)
    {
        setResizable(false);
        setGrabChildSelection(true);
        m_rect = new FREE3DRect();
        m_rect.setSelectable(false);
        m_label = new FREEText(s);
        m_label.setMultiline(true);
        m_label.setSelectable(false);
        m_label.setResizable(false);
        m_label.setDraggable(false);
        m_label.setEditable(false);
        m_label.setEditOnSingleClick(true);
        m_label.setTransparent(true);
        m_inPort = new FREEPort();
        m_inPort.setStyle(0);
        m_inPort.setSize(1, 1);
        m_inPort.setFromSpot(8);
        m_inPort.setToSpot(8);
        m_outPort = new FREEPort();
        m_outPort.setStyle(0);
        m_outPort.setSize(1, 1);
        m_outPort.setFromSpot(4);
        m_outPort.setToSpot(4);
        addObjectAtHead(m_rect);
        addObjectAtTail(m_label);
        addObjectAtTail(m_inPort);
        addObjectAtTail(m_outPort);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        TextNode textnode = (TextNode)super.copyObject(copyEnv);
        if(textnode != null)
            textnode.m_object = m_object;
        return textnode;
    }

    public void copyChildren(FREEArea area, FREECopyEnvironment copyEnv)
    {
        TextNode textnode = (TextNode)area;
        if(m_rect != null)
        {
            textnode.m_rect = (FREERectangle)m_rect.copyObject(copyEnv);
            textnode.addObjectAtHead(textnode.m_rect);
        }
        if(m_label != null)
        {
            textnode.m_label = (FREEText)m_label.copyObject(copyEnv);
            textnode.addObjectAtTail(textnode.m_label);
        }
        if(m_inPort != null)
        {
            textnode.m_inPort = (FREEPort)m_inPort.copyObject(copyEnv);
            textnode.addObjectAtTail(textnode.m_inPort);
        }
        if(m_outPort != null)
        {
            textnode.m_outPort = (FREEPort)m_outPort.copyObject(copyEnv);
            textnode.addObjectAtTail(textnode.m_outPort);
        }
    }

    protected boolean geometryChangeChild(FREEObject object, Rectangle rectangle)
    {
        if(super.geometryChangeChild(object, rectangle))
        {
            if(object == getLabel())
                layoutChildren();
            return true;
        } else
        {
            return false;
        }
    }

    protected void layoutChildren()
    {
        FREEText text = getLabel();
        if(text != null && getRect() != null)
        {
            getRect().setBoundingRect(text.getLeft() - 2, text.getTop(), text.getWidth() + 4, text.getHeight() + 2);
            if(getInPort() != null)
                getInPort().setSpotLocation( FREEObject.RightCenter, getRect(), 8);
            if(getOutPort() != null)
                getOutPort().setSpotLocation( FREEObject.LeftCenter, getRect(), 4);
        }
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

    public void setLabel(FREEText text)
    {
        if(m_label != null)
            removeObject(m_label);
        m_label = text;
        if(text != null)
            addObjectAtTail(text);
    }

    public FREERectangle getRect()
    {
        return m_rect;
    }

    public void setRect(FREERectangle rectangle)
    {
        if(m_rect != null)
            removeObject(m_rect);
        m_rect = rectangle;
        if(rectangle != null)
            addObjectAtHead(rectangle);
    }

    public FREEPort getInPort()
    {
        return m_inPort;
    }

    public void setInPort(FREEPort port)
    {
        if(m_inPort != null)
            removeObject(m_inPort);
        m_inPort = port;
        if(port != null)
            addObjectAtTail(port);
    }

    public FREEPort getOutPort()
    {
        return m_outPort;
    }

    public void setOutPort(FREEPort port)
    {
        if(m_outPort != null)
            removeObject(m_outPort);
        m_outPort = port;
        if(port != null)
            addObjectAtTail(port);
    }

    public String getText()
    {
        return getLabel().getText();
    }

    public void setText(String s)
    {
        getLabel().setText(s);
    }

    public Object getObject()
    {
        return m_object;
    }

    public void setObject(Object obj)
    {
        m_object = obj;
    }

    private FREEText m_label;
    private FREERectangle m_rect;
    private FREEPort m_inPort;
    private FREEPort m_outPort;
    private Object m_object;
}
