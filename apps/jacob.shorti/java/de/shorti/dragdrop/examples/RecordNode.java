//
//
//
// Source File Name:   RecordNode.java

package de.shorti.dragdrop.examples;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import de.shorti.dragdrop.FREEArea;
import de.shorti.dragdrop.FREECopyEnvironment;
import de.shorti.dragdrop.FREEObject;
import de.shorti.dragdrop.FREEPen;
import de.shorti.dragdrop.FREEPort;
import de.shorti.dragdrop.FREEView;

// Referenced classes of package de.shorti.dragdrop.examples:
//            ListArea

public class RecordNode extends FREEArea
{

    public RecordNode()
    {
        m_listArea = null;
        m_header = null;
        m_footer = null;
    }

    public void initialize()
    {
        setSelectable(false);
        setGrabChildSelection(true);
        m_listArea = new ListArea();
        m_listArea.initialize();
        m_listArea.setVertical(true);
        m_listArea.setSelectable(false);
        m_listArea.setGrabChildSelection(false);
        m_listArea.getRect().setSelectable(false);
        addObjectAtHead(m_listArea);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        RecordNode recordnode = (RecordNode)super.copyObject(copyEnv);
        if(recordnode == null);
        return recordnode;
    }

    public void copyChildren(FREEArea area, FREECopyEnvironment copyEnv)
    {
        RecordNode recordnode = (RecordNode)area;
        if(m_listArea != null)
        {
            recordnode.m_listArea = (ListArea)m_listArea.copyObject(copyEnv);
            recordnode.addObjectAtHead(recordnode.m_listArea);
        }
        if(m_header != null)
        {
            recordnode.m_header = m_header.copyObject(copyEnv);
            recordnode.addObjectAtHead(recordnode.m_header);
        }
        if(m_footer != null)
        {
            recordnode.m_footer = m_footer.copyObject(copyEnv);
            recordnode.addObjectAtHead(recordnode.m_footer);
        }
    }

    public ListArea getListArea()
    {
        return m_listArea;
    }

    public FREEObject getHeader()
    {
        return m_header;
    }

    public void setHeader(FREEObject object)
    {
        FREEObject object1 = getHeader();
        if(object1 != object)
        {
            if(object1 != null)
                removeObject(object1);
            m_header = object;
            if(object != null)
            {
                object.setSelectable(false);
                addObjectAtHead(object);
            }
            layoutChildren();
        }
    }

    public FREEObject getFooter()
    {
        return m_footer;
    }

    public void setFooter(FREEObject object)
    {
        FREEObject object1 = getFooter();
        if(object1 != object)
        {
            if(object1 != null)
                removeObject(object1);
            m_footer = object;
            if(object != null)
            {
                object.setSelectable(false);
                addObjectAtHead(object);
            }
            layoutChildren();
        }
    }

    protected Rectangle handleResize(Graphics2D graphics2d, FREEView view, Rectangle rectangle, Point point, int i, int j, int k,
            int l)
    {
        ListArea listarea = getListArea();
        int i1 = listarea.getWidth() - listarea.getRect().getWidth();
        Dimension dimension = listarea.getMinimumSize();
        if(getHeader() != null)
            dimension.height += getHeader().getHeight();
        if(getFooter() != null)
            dimension.height += getFooter().getHeight();
        return super.handleResize(graphics2d, view, rectangle, point, i, j, dimension.width + i1, dimension.height);
    }

    public void setBoundingRect(int i, int j, int k, int l)
    {
        ListArea listarea = getListArea();
        int i1 = listarea.getWidth() - listarea.getRect().getWidth();
        Dimension dimension = listarea.getMinimumSize();
        if(getHeader() != null)
            dimension.height += getHeader().getHeight();
        if(getFooter() != null)
            dimension.height += getFooter().getHeight();
        super.setBoundingRect(i, j, Math.max(k, dimension.width + i1), Math.max(l, dimension.height));
    }

    public void setVisible(boolean flag)
    {
        if(getListArea() != null)
            getListArea().setVisible(flag);
        super.setVisible(flag);
    }

    protected void geometryChange(Rectangle rectangle)
    {
        if(getWidth() != rectangle.width || getHeight() != rectangle.height)
            layoutChildren();
        else
            super.geometryChange(rectangle);
    }

    protected void layoutChildren()
    {
        int i = getLeft();
        int j = getTop();
        int k = getWidth();
        int l = getHeight();
        ListArea listarea = getListArea();
        int i1 = listarea.getRect().getLeft() - listarea.getLeft();
        int j1 = listarea.getWidth() - listarea.getRect().getWidth();
        int k1 = i + i1;
        int l1 = k - j1;
        int i2 = 0;
        if(getHeader() != null)
        {
            i2 = getHeader().getHeight();
            getHeader().setBoundingRect(k1, j, l1, i2);
        }
        int j2 = 0;
        if(getFooter() != null)
        {
            j2 = getFooter().getHeight();
            getFooter().setBoundingRect(k1, (j + l) - j2, l1, j2);
        }
        listarea.getRect().setBoundingRect(k1, j + i2, l1, l - i2 - j2);
    }

    protected boolean geometryChangeChild(FREEObject object, Rectangle rect)
    {
        if(isSuspendChildUpdates())
            return false;
        if(object.getWidth() != rect.width)
        {
            de.shorti.dragdrop.FREERectangle rectangle = getListArea().getRect();
            int i = rectangle.getLeft();
            int j = rectangle.getWidth();
            if(getHeader() != null)
            {
                getHeader().setLeft(i);
                getHeader().setWidth(j);
            }
            if(getFooter() != null)
            {
                getFooter().setLeft(i);
                getFooter().setWidth(j);
            }
        }
        return super.geometryChangeChild(object, rect);
    }

    public void addItem(FREEObject object, FREEPort port, FREEPort port1)
    {
        if(port != null)
        {
            port.setValidSource(false);
            port.setValidDestination(true);
            port.setFromSpot(8);
            port.setToSpot(8);
        }
        if(port1 != null)
        {
            port1.setValidSource(true);
            port1.setValidDestination(false);
            port1.setFromSpot(4);
            port1.setToSpot(4);
        }
        m_listArea.addItem(getNumItems(), object, port, port1);
    }

    public FREEPort getLeftPort(int i)
    {
        return (FREEPort)m_listArea.getLeftPort(i);
    }

    public void setLeftPort(int i, FREEPort port)
    {
        if(port != null)
        {
            port.setFromSpot(8);
            port.setToSpot(8);
        }
        m_listArea.setLeftPort(i, port);
    }

    public FREEPort getRightPort(int i)
    {
        return (FREEPort)m_listArea.getRightPort(i);
    }

    public void setRightPort(int i, FREEPort port)
    {
        if(port != null)
        {
            port.setFromSpot(4);
            port.setToSpot(4);
        }
        m_listArea.setRightPort(i, port);
    }

    public int getNumItems()
    {
        return m_listArea.getNumItems();
    }

    public FREEObject getItem(int i)
    {
        return m_listArea.getItem(i);
    }

    public void setItem(int i, FREEObject object)
    {
        m_listArea.setItem(i, object);
    }

    public int findItem(FREEObject object)
    {
        return m_listArea.findItem(object);
    }

    public boolean isScrollBarOnRight()
    {
        return m_listArea.isScrollBarOnRight();
    }

    public void setScrollBarOnRight(boolean flag)
    {
        m_listArea.setScrollBarOnRight(flag);
    }

    public FREEPen getLinePen()
    {
        return m_listArea.getLinePen();
    }

    public void setLinePen(FREEPen pen)
    {
        m_listArea.setLinePen(pen);
    }

    public Insets getInsets()
    {
        return m_listArea.getInsets();
    }

    public void setInsets(Insets insets)
    {
        m_listArea.setInsets(insets);
    }

    public int getSpacing()
    {
        return m_listArea.getSpacing();
    }

    public void setSpacing(int i)
    {
        m_listArea.setSpacing(i);
    }

    public int getAlignment()
    {
        return m_listArea.getAlignment();
    }

    public void setAlignment(int i)
    {
        m_listArea.setAlignment(i);
    }

    public int getFirstVisibleIndex()
    {
        return m_listArea.getFirstVisibleIndex();
    }

    public void setFirstVisibleIndex(int i)
    {
        m_listArea.setFirstVisibleIndex(i);
    }

    public int getLastVisibleIndex()
    {
        return m_listArea.getLastVisibleIndex();
    }

    private ListArea m_listArea;
    private FREEObject m_header;
    private FREEObject m_footer;
}
