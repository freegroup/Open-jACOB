//
//
//
// Source File Name:   ListArea.java

package de.shorti.dragdrop.examples;

import de.shorti.dragdrop.*;
import java.awt.*;
import java.util.Vector;

// Referenced classes of package de.shorti.dragdrop.examples:
//            ListAreaRect

public class ListArea extends FREEArea
{

    public ListArea()
    {
        m_vector = new Vector();
        m_leftPorts = new Vector();
        m_rightPorts = new Vector();
        m_rect = null;
        m_bar = null;
        m_vertical = true;
        m_scrollBarOnRight = true;
        m_alignment = 0;
        m_firstItem = 0;
        m_lastItem = -1;
        m_naxItemSize = new Dimension(-1, -1);
        m_insets = new Insets(1, 4, 1, m_barSize + 4);
        m_spacing = 0;
        m_linePen = null;
    }

    public void initialize()
    {
        setSelectable(false);
        setGrabChildSelection(true);
        m_rect = new ListAreaRect();
        m_rect.setBrush(FREEBrush.makeStockBrush(new Color(230, 230, 230)));
        m_rect.setSelectable(true);
        addObjectAtHead(m_rect);
        m_bar = new FREEScrollBar();
        m_bar.setVertical(isVertical());
        m_bar.setSelectable(false);
        addObjectAtTail(m_bar);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        ListArea listarea = (ListArea)super.copyObject(copyEnv);
        if(listarea != null)
        {
            listarea.m_vertical = m_vertical;
            listarea.m_scrollBarOnRight = m_scrollBarOnRight;
            listarea.m_alignment = m_alignment;
            listarea.m_firstItem = m_firstItem;
            listarea.m_lastItem = m_lastItem;
            listarea.m_insets.top = m_insets.top;
            listarea.m_insets.left = m_insets.left;
            listarea.m_insets.bottom = m_insets.bottom;
            listarea.m_insets.right = m_insets.right;
            listarea.m_spacing = m_spacing;
            listarea.m_linePen = m_linePen;
        }
        return listarea;
    }

    public void copyChildren(FREEArea area, FREECopyEnvironment copyEnv)
    {
        ListArea listarea = (ListArea)area;
        if(m_rect != null)
        {
            listarea.m_rect = (FREERectangle)m_rect.copyObject(copyEnv);
            listarea.addObjectAtHead(listarea.m_rect);
        }
        for(int i = 0; i < m_vector.size(); i++)
        {
            FREEObject object = (FREEObject)m_vector.get(i);
            FREEObject object1 = object.copyObject(copyEnv);
            listarea.m_vector.add(object1);
            listarea.addObjectAtTail(object1);
        }

        for(int j = 0; j < m_leftPorts.size(); j++)
        {
            FREEObject object2 = (FREEObject)m_leftPorts.get(j);
            if(object2 != null)
            {
                FREEObject object3 = object2.copyObject(copyEnv);
                if(object3 != null)
                {
                    listarea.m_leftPorts.add(object3);
                    listarea.addObjectAtTail(object3);
                }
            }
        }

        for(int k = 0; k < m_rightPorts.size(); k++)
        {
            FREEObject object4 = (FREEObject)m_rightPorts.get(k);
            if(object4 != null)
            {
                FREEObject object5 = object4.copyObject(copyEnv);
                if(object5 != null)
                {
                    listarea.m_rightPorts.add(object5);
                    listarea.addObjectAtTail(object5);
                }
            }
        }

        if(m_bar != null)
        {
            listarea.m_bar = (FREEScrollBar)m_bar.copyObject(copyEnv);
            listarea.addObjectAtTail(listarea.m_bar);
        }
    }

    public boolean isVertical()
    {
        return m_vertical;
    }

    public void setVertical(boolean flag)
    {
        if(isVertical() != flag)
        {
            m_vertical = flag;
            if(getScrollBar() != null)
                getScrollBar().setVertical(isVertical());
            Insets insets = getInsets();
            if(flag)
            {
                if(isScrollBarOnRight())
                {
                    insets.right += m_barSize;
                    insets.bottom -= m_barSize;
                } else
                {
                    insets.left += m_barSize;
                    insets.top -= m_barSize;
                }
            } else
            if(isScrollBarOnRight())
            {
                insets.right -= m_barSize;
                insets.bottom += m_barSize;
            } else
            {
                insets.left -= m_barSize;
                insets.top += m_barSize;
            }
            layoutChildren();
        }
    }

    public boolean isScrollBarOnRight()
    {
        return m_scrollBarOnRight;
    }

    public void setScrollBarOnRight(boolean flag)
    {
        if(isScrollBarOnRight() != flag)
        {
            m_scrollBarOnRight = flag;
            Insets insets = getInsets();
            if(flag)
            {
                if(isVertical())
                {
                    insets.right += m_barSize;
                    insets.left -= m_barSize;
                } else
                {
                    insets.bottom += m_barSize;
                    insets.top -= m_barSize;
                }
            } else
            if(isVertical())
            {
                insets.right -= m_barSize;
                insets.left += m_barSize;
            } else
            {
                insets.bottom -= m_barSize;
                insets.top += m_barSize;
            }
            layoutChildren();
        }
    }

    public FREEPen getLinePen()
    {
        return m_linePen;
    }

    public void setLinePen(FREEPen pen)
    {
        if(getLinePen() != pen)
        {
            m_linePen = pen;
            layoutChildren();
        }
    }

    public FREERectangle getRect()
    {
        return m_rect;
    }

    public void setRect(FREERectangle rectangle)
    {
        if(rectangle == null)
            return;
        if(m_rect != null)
            removeObject(m_rect);
        m_rect = rectangle;
        addObjectAtHead(rectangle);
        layoutChildren();
    }

    public FREEScrollBar getScrollBar()
    {
        return m_bar;
    }

    public void setScrollBar(FREEScrollBar scrollbar)
    {
        if(m_bar != null)
            removeObject(m_bar);
        m_bar = scrollbar;
        if(scrollbar != null)
        {
            addObjectAtHead(scrollbar);
            layoutChildren();
        }
    }

    public Insets getInsets()
    {
        return m_insets;
    }

    public void setInsets(Insets insets)
    {
        if(!getInsets().equals(insets))
        {
            m_insets.top = insets.top;
            m_insets.left = insets.left;
            m_insets.bottom = insets.bottom;
            m_insets.right = insets.right;
            layoutChildren();
        }
    }

    public int getSpacing()
    {
        return m_spacing;
    }

    public void setSpacing(int i)
    {
        if(i != getSpacing())
        {
            m_spacing = i;
            layoutChildren();
        }
    }

    public void setAlignment(int i)
    {
        if(getAlignment() != i)
        {
            m_alignment = i;
            layoutChildren();
        }
    }

    public int getAlignment()
    {
        return m_alignment;
    }

    public int getFirstVisibleIndex()
    {
        return m_firstItem;
    }

    public void setFirstVisibleIndex(int i)
    {
        if(i >= 0 && i <= getNumItems() && getFirstVisibleIndex() != i)
        {
            m_firstItem = i;
            layoutChildren();
        }
    }

    public int getLastVisibleIndex()
    {
        return m_lastItem;
    }

    public int getNumItems()
    {
        return m_vector.size();
    }

    public FREEObject getItem(int i)
    {
        if(i < 0 || i >= m_vector.size())
            return null;
        else
            return (FREEObject)m_vector.get(i);
    }

    public void setItem(int i, FREEObject object)
    {
        if(i < 0 || i >= getNumItems())
            return;
        if(object == null)
            return;
        FREEObject object1 = getItem(i);
        if(object1 == null)
            return;
        if(object1 != object)
        {
            object.setResizable(false);
            removeObject(object1);
            m_vector.set(i, object);
            adjustMaxItemSize(object, object1.getWidth(), object1.getHeight());
            addObjectAtTail(object);
            FREERectangle rectangle = getRect();
            if(i < getFirstVisibleIndex())
            {
                object.setVisible(false);
                object.setTopLeft(rectangle.getLeft(), rectangle.getTop());
            } else
            if(i <= getLastVisibleIndex())
            {
                layoutChildren();
            } else
            {
                object.setVisible(false);
                if(isVertical())
                    object.setTopLeft(rectangle.getLeft(), (rectangle.getTop() + rectangle.getHeight()) - object.getHeight());
                else
                    object.setTopLeft((rectangle.getLeft() + rectangle.getWidth()) - object.getWidth(), rectangle.getTop());
            }
        }
    }

    public int findItem(FREEObject object)
    {
        for(int i = 0; i < getNumItems(); i++)
        {
            FREEObject object1 = getItem(i);
            if(object1 == object)
                return i;
        }

        return -1;
    }

    public FREEObject getLeftPort(int i)
    {
        if(i < 0 || i >= m_leftPorts.size())
            return null;
        else
            return (FREEObject)m_leftPorts.get(i);
    }

    public void setLeftPort(int i, FREEObject object)
    {
        FREEObject object1 = getLeftPort(i);
        if(object1 != object)
        {
            if(object1 != null)
            {
                if(object != null)
                    object.setBoundingRect(object1.getBoundingRect());
                removeObject(object1);
            }
            m_leftPorts.set(i, object);
            if(object != null)
            {
                object.setSelectable(false);
                object.setDraggable(false);
                object.setResizable(false);
                addObjectAtTail(object);
            }
        }
    }

    public FREEObject getRightPort(int i)
    {
        if(i < 0 || i >= m_rightPorts.size())
            return null;
        else
            return (FREEObject)m_rightPorts.get(i);
    }

    public void setRightPort(int i, FREEObject object)
    {
        FREEObject object1 = getRightPort(i);
        if(object1 != object)
        {
            if(object1 != null)
            {
                if(object != null)
                    object.setBoundingRect(object1.getBoundingRect());
                removeObject(object1);
            }
            m_rightPorts.set(i, object);
            if(object != null)
            {
                object.setSelectable(false);
                object.setDraggable(false);
                object.setResizable(false);
                addObjectAtTail(object);
            }
        }
    }

    public final void addItem(FREEObject object, FREEObject object1, FREEObject object2)
    {
        addItem(getNumItems(), object, object1, object2);
    }

    public void addItem(int i, FREEObject object, FREEObject object1, FREEObject object2)
    {
        object.setResizable(false);
        FREERectangle rectangle = getRect();
        object.setTopLeft(rectangle.getLeft(), rectangle.getTop());
        m_vector.add(i, object);
        adjustMaxItemSize(object, -1, -1);
        addObjectAtTail(object);
        m_leftPorts.add(i, object1);
        if(object1 != null)
        {
            object1.setTopLeft(rectangle.getLeft(), rectangle.getTop());
            object1.setVisible(object.isVisible());
            object1.setSelectable(false);
            object1.setDraggable(false);
            object1.setResizable(false);
            addObjectAtTail(object1);
        }
        m_rightPorts.add(i, object2);
        if(object2 != null)
        {
            object2.setTopLeft(rectangle.getLeft(), rectangle.getTop());
            object2.setVisible(object.isVisible());
            object2.setSelectable(false);
            object2.setDraggable(false);
            object2.setResizable(false);
            addObjectAtTail(object2);
        }
        if(i < getFirstVisibleIndex())
        {
            object.setVisible(false);
            updateScrollBar();
        } else
        if(i <= getLastVisibleIndex())
        {
            layoutChildren();
        } else
        {
            object.setVisible(false);
            if(isVertical())
                object.setTopLeft(rectangle.getLeft(), (rectangle.getTop() + rectangle.getHeight()) - object.getHeight());
            else
                object.setTopLeft((rectangle.getLeft() + rectangle.getWidth()) - object.getWidth(), rectangle.getTop());
            updateScrollBar();
        }
    }

    public void removeItem(int i)
    {
        FREEObject object = getLeftPort(i);
        m_leftPorts.remove(i);
        if(object != null)
            super.removeObject(object);
        object = getRightPort(i);
        m_rightPorts.remove(i);
        if(object != null)
            super.removeObject(object);
        FREEObject object1 = getItem(i);
        if(object1 != null)
        {
            m_vector.remove(i);
            super.removeObject(object1);
            adjustMaxItemSize(null, object1.getWidth(), object1.getHeight());
        }
        if(i <= getLastVisibleIndex())
            layoutChildren();
        else
            updateScrollBar();
    }

    public void removeObject(FREEObject object)
    {
        boolean flag = false;
        for(int i = 0; i < getNumItems(); i++)
        {
            FREEObject object1 = getItem(i);
            if(object1 != object)
                continue;
            removeItem(i);
            flag = true;
            break;
        }

        if(!flag)
            super.removeObject(object);
    }

    protected void layoutChildren()
    {
        Insets insets = getInsets();
        FREERectangle rectangle = getRect();
        if(rectangle == null)
            return;
        sendObjectToBack(rectangle);
        m_lastItem = getFirstVisibleIndex();
        int i = rectangle.getLeft() + insets.left;
        int j = rectangle.getTop() + insets.top;
        int k = rectangle.getWidth() - insets.left - insets.right;
        int l = rectangle.getHeight() - insets.top - insets.bottom;
        int i1 = 0;
        if(getLinePen() != null)
            i1 = getLinePen().getWidth();
        if(isVertical())
        {
            int j1 = 0;
            for(int l1 = 0; l1 < getNumItems(); l1++)
            {
                FREEObject object = getItem(l1);
                if(object != null)
                {
                    int j2;
                    switch(getAlignment())
                    {
                    case 0: // '\0'
                    default:
                        j2 = 0;
                        break;

                    case 1: // '\001'
                        j2 = k / 2 - object.getWidth() / 2;
                        break;

                    case 2: // '\002'
                        j2 = k - object.getWidth();
                        break;
                    }
                    if(l1 < getFirstVisibleIndex())
                    {
                        object.setVisible(false);
                        object.setTopLeft(i + j2, j);
                        layoutPorts(l1, object, j);
                    } else
                    {
                        int l2 = object.getHeight();
                        if(j1 + l2 <= l)
                        {
                            object.setVisible(true);
                            object.setTopLeft(i + j2, j + j1);
                            layoutPorts(l1, object, j + j1 + l2 / 2);
                            j1 += l2;
                            j1 += Math.max(i1, getSpacing());
                            m_lastItem = l1;
                        } else
                        {
                            object.setVisible(false);
                            object.setTopLeft(i + j2, (j + l) - l2);
                            layoutPorts(l1, object, j + l);
                            j1 = l + 1;
                        }
                    }
                }
            }

            FREEScrollBar scrollbar = getScrollBar();
            if(scrollbar != null)
            {
                if(isScrollBarOnRight())
                    scrollbar.setBoundingRect((rectangle.getLeft() + rectangle.getWidth()) - m_barSize, rectangle.getTop(), m_barSize, rectangle.getHeight());
                else
                    scrollbar.setBoundingRect(rectangle.getLeft(), rectangle.getTop(), m_barSize, rectangle.getHeight());
                updateScrollBar();
            }
        } else
        {
            int k1 = 0;
            for(int i2 = 0; i2 < getNumItems(); i2++)
            {
                FREEObject object1 = getItem(i2);
                if(object1 != null)
                {
                    int k2;
                    switch(getAlignment())
                    {
                    case 0: // '\0'
                    default:
                        k2 = 0;
                        break;

                    case 1: // '\001'
                        k2 = l / 2 - object1.getHeight() / 2;
                        break;

                    case 2: // '\002'
                        k2 = l - object1.getHeight();
                        break;
                    }
                    if(i2 < getFirstVisibleIndex())
                    {
                        object1.setVisible(false);
                        object1.setTopLeft(i, j + k2);
                        layoutPorts(i2, object1, i);
                    } else
                    {
                        int i3 = object1.getWidth();
                        if(k1 + i3 <= k)
                        {
                            object1.setVisible(true);
                            object1.setTopLeft(i + k1, j + k2);
                            layoutPorts(i2, object1, i + k1 + i3 / 2);
                            k1 += i3;
                            k1 += Math.max(i1, getSpacing());
                            m_lastItem = i2;
                        } else
                        {
                            object1.setVisible(false);
                            object1.setTopLeft((i + k) - i3, j + k2);
                            layoutPorts(i2, object1, i + k);
                            k1 = k + 1;
                        }
                    }
                }
            }

            FREEScrollBar scrollbar1 = getScrollBar();
            if(scrollbar1 != null)
            {
                if(isScrollBarOnRight())
                    scrollbar1.setBoundingRect(rectangle.getLeft(), (rectangle.getTop() + rectangle.getHeight()) - m_barSize, rectangle.getWidth(), m_barSize);
                else
                    scrollbar1.setBoundingRect(rectangle.getLeft(), rectangle.getTop(), rectangle.getWidth(), m_barSize);
                updateScrollBar();
            }
        }
    }

    protected void layoutPorts(int i, FREEObject object, int j)
    {
        FREEObject object1 = getLeftPort(i);
        if(object1 != null)
        {
            object1.setVisible(object.isVisible());
            if(isVertical())
            {
                int k = getRect().getLeft() - object1.getWidth();
                int k1 = j - object1.getHeight() / 2;
                object1.setTopLeft(k, k1);
            } else
            {
                int l = j - object1.getWidth() / 2;
                int l1 = getRect().getTop() - object1.getHeight();
                object1.setTopLeft(l, l1);
            }
        }
        object1 = getRightPort(i);
        if(object1 != null)
        {
            object1.setVisible(object.isVisible());
            if(isVertical())
            {
                int i1 = getRect().getLeft() + getRect().getWidth();
                int i2 = j - object1.getHeight() / 2;
                object1.setTopLeft(i1, i2);
            } else
            {
                int j1 = j - object1.getWidth() / 2;
                int j2 = getRect().getTop() + getRect().getHeight();
                object1.setTopLeft(j1, j2);
            }
        }
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        super.paint(graphics2d, view);
        int i = 0;
        if(getLinePen() != null)
            i = getLinePen().getWidth();
        if(i == 0)
            return;
        Insets insets = getInsets();
        FREERectangle rectangle = getRect();
        if(rectangle == null)
            return;
        int j = rectangle.getLeft() + insets.left;
        int k = rectangle.getTop() + insets.top;
        int l = rectangle.getWidth() - insets.left - insets.right;
        int i1 = rectangle.getHeight() - insets.top - insets.bottom;
        if(isVertical())
        {
            int j1 = 0;
            for(int l1 = 0; l1 < getNumItems(); l1++)
            {
                FREEObject object = getItem(l1);
                if(object != null && l1 >= getFirstVisibleIndex() && l1 < getLastVisibleIndex())
                {
                    int j2 = object.getHeight();
                    if(j1 + j2 <= i1)
                    {
                        j1 += j2;
                        int l2 = Math.max(i, getSpacing());
                        if(j1 + l2 <= i1)
                            FREEDrawable.drawLine(graphics2d, getLinePen(), j, k + j1 + l2 / 2, j + l, k + j1 + l2 / 2);
                        j1 += l2;
                    }
                }
            }

        } else
        {
            int k1 = 0;
            for(int i2 = 0; i2 < getNumItems(); i2++)
            {
                FREEObject object1 = getItem(i2);
                if(object1 != null && i2 >= getFirstVisibleIndex() && i2 < getLastVisibleIndex())
                {
                    int k2 = object1.getWidth();
                    if(k1 + k2 <= l)
                    {
                        k1 += k2;
                        int i3 = Math.max(i, getSpacing());
                        if(k1 + i3 <= l)
                            FREEDrawable.drawLine(graphics2d, getLinePen(), j + k1 + i3 / 2, k, j + k1 + i3 / 2, k + i1);
                        k1 += i3;
                    }
                }
            }

        }
    }

    public void updateScrollBar()
    {
        FREEScrollBar scrollbar = getScrollBar();
        if(scrollbar != null)
        {
            scrollbar.setVertical(isVertical());
            if(getFirstVisibleIndex() == 0 && getLastVisibleIndex() == getNumItems() - 1)
            {
                scrollbar.setVisible(false);
            } else
            {
                scrollbar.setVisible(true);
                scrollbar.setValues(getFirstVisibleIndex(), (getLastVisibleIndex() - getFirstVisibleIndex()) + 1, 0, getNumItems(), 1, Math.max(getLastVisibleIndex() - getFirstVisibleIndex(), 1));
            }
        }
    }

    public void update(int i)
    {
        if(i == 1003)
        {
            if(getScrollBar() != null)
                setFirstVisibleIndex(getScrollBar().getValue());
        } else
        {
            super.update(i);
        }
    }

    public void setVisible(boolean flag)
    {
        if(getScrollBar() != null)
            getScrollBar().setVisible(flag);
        super.setVisible(flag);
    }

    protected FREEObject redirectSelection()
    {
        return getRect();
    }

    public Dimension getMinimumSize()
    {
        Insets insets = getInsets();
        int i = getMaxItemSize().width + insets.left + insets.right;
        int j = getMaxItemSize().height + insets.top + insets.bottom;
        return new Dimension(i, j);
    }

    public void setBoundingRect(int i, int j, int k, int l)
    {
        Dimension dimension = getMinimumSize();
        super.setBoundingRect(i, j, Math.max(k, dimension.width), Math.max(l, dimension.height));
    }

    protected void geometryChange(Rectangle rectangle)
    {
        if(rectangle.width != getWidth() || rectangle.height != getHeight())
        {
            if(getRect() != null)
                getRect().setBoundingRect(getBoundingRect());
            layoutChildren();
        } else
        {
            super.geometryChange(rectangle);
        }
    }

    protected boolean geometryChangeChild(FREEObject object, Rectangle rectangle)
    {
        if(isSuspendChildUpdates())
            return false;
        if(object != getRect() && object != getScrollBar() && (object.getWidth() != rectangle.width || object.getHeight() != rectangle.height))
            adjustMaxItemSize(object, rectangle.width, rectangle.height);
        return super.geometryChangeChild(object, rectangle);
    }

    public void adjustMaxItemSize(FREEObject object, int i, int j)
    {
        int k = -1;
        int l = -1;
        if(object != null)
        {
            k = object.getWidth();
            l = object.getHeight();
        }
        Insets insets = getInsets();
        if(m_naxItemSize.width > -1)
            if(k > i && k > m_naxItemSize.width)
            {
                m_naxItemSize.width = k;
                int i1 = k + insets.left + insets.right;
                if(getRect().getWidth() < i1)
                    getRect().setWidth(i1);
            } else
            if(k < i && i == m_naxItemSize.width)
                m_naxItemSize.width = -1;
        if(m_naxItemSize.height > -1)
            if(l > j && l > m_naxItemSize.height)
            {
                m_naxItemSize.height = l;
                int j1 = l + insets.top + insets.bottom;
                if(getRect().getHeight() < j1)
                    getRect().setHeight(j1);
            } else
            if(l < j && j == m_naxItemSize.height)
                m_naxItemSize.height = -1;
    }

    public Dimension getMaxItemSize()
    {
        if(m_naxItemSize.width < 0 || m_naxItemSize.height < 0)
        {
            m_naxItemSize.width = -1;
            m_naxItemSize.height = -1;
            for(int i = 0; i < m_vector.size(); i++)
            {
                FREEObject object = (FREEObject)m_vector.get(i);
                if(object != null)
                {
                    if(object.getWidth() > m_naxItemSize.width)
                        m_naxItemSize.width = object.getWidth();
                    if(object.getHeight() > m_naxItemSize.height)
                        m_naxItemSize.height = object.getHeight();
                }
            }

        }
        return m_naxItemSize;
    }

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    private static int m_barSize = 14;
    private Vector m_vector;
    private Vector m_leftPorts;
    private Vector m_rightPorts;
    private FREERectangle m_rect;
    private FREEScrollBar m_bar;
    private boolean m_vertical;
    private boolean m_scrollBarOnRight;
    private int m_alignment;
    private int m_firstItem;
    private int m_lastItem;
    private transient Dimension m_naxItemSize;
    private Insets m_insets;
    private int m_spacing;
    private FREEPen m_linePen;

}
