//
//
//
// Source File Name:   Comment.java

package de.shorti.dragdrop.examples;


import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import de.shorti.dragdrop.FREE3DNoteRect;
import de.shorti.dragdrop.FREEArea;
import de.shorti.dragdrop.FREEBrush;
import de.shorti.dragdrop.FREECopyEnvironment;
import de.shorti.dragdrop.FREEObject;
import de.shorti.dragdrop.FREEPen;
import de.shorti.dragdrop.FREEText;
import de.shorti.dragdrop.FREEView;

public class Comment extends FREEArea
{

    public Comment()
    {
        m_label = null;
        m_rect = null;
    }

    public Comment(String s)
    {
        m_label = null;
        m_rect = null;
        initialize(s);
    }

    public void initialize(String s)
    {
        setResizable(false);
        setGrabChildSelection(true);
        m_rect = new FREE3DNoteRect();
        m_rect.setSelectable(false);
        m_rect.setPen(FREEPen.lightGray);
        m_rect.setBrush(FREEBrush.makeStockBrush(new Color(255, 255, 204)));
        m_label = new FREEText(s);
        m_label.setMultiline(true);
        m_label.setSelectable(false);
        m_label.setResizable(false);
        m_label.setDraggable(false);
        m_label.setEditable(false);
        m_label.setEditOnSingleClick(true);
        m_label.setTransparent(true);
        addObjectAtHead(m_rect);
        addObjectAtTail(m_label);
    }

    public void copyChildren(FREEArea area, FREECopyEnvironment copyEnv)
    {
        Comment comment = (Comment)area;
        if(m_rect != null)
        {
            comment.m_rect = (FREE3DNoteRect)m_rect.copyObject(copyEnv);
            comment.addObjectAtHead(comment.m_rect);
        }
        if(m_label != null)
        {
            comment.m_label = (FREEText)m_label.copyObject(copyEnv);
            comment.addObjectAtTail(comment.m_label);
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
        if(text != null)
        {
            FREE3DNoteRect note = getRect();
            if(note != null)
                note.setBoundingRect(text.getLeft() - 4, text.getTop() - 2, text.getWidth() + 8, text.getHeight() + 4 + note.getFlapSize());
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

    public FREE3DNoteRect getRect()
    {
        return m_rect;
    }

    public void setRect(FREE3DNoteRect note)
    {
        if(m_rect != null)
            removeObject(m_rect);
        m_rect = note;
        if(note != null)
            addObjectAtHead(note);
    }

    public String getText()
    {
        return getLabel().getText();
    }

    public void setText(String s)
    {
        getLabel().setText(s);
    }

    public boolean isEditable()
    {
        return getLabel().isEditable();
    }

    public void setEditable(boolean flag)
    {
        getLabel().setEditable(flag);
    }

    private FREEText m_label;
    private FREE3DNoteRect m_rect;
}
