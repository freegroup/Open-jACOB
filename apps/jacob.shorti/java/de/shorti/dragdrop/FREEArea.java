package de.shorti.dragdrop;




import java.awt.*;


public class FREEArea extends FREEObject  implements FREEObjectCollection
{
    private           FREEObjList      m_children;
    private transient FREEListPosition _fld011A;

    public FREEArea()
    {
        m_children = new FREEObjList(true);
        _fld011A = null;
        setFlags(getFlags() | 0x200 | 0x100);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEArea area = (FREEArea)super.copyObject(copyEnv);
        if(area != null)
            copyChildren(area, copyEnv);
        return area;
    }

    public void copyChildren(FREEArea area, FREECopyEnvironment copyEnv)
    {
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getNextObjectPos(listposition);
            FREEObject object1 = object.copyObject(copyEnv);
            area.addObjectAtTail(object1);
        }

    }

    void C2(FREELayer layer)
    {
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getNextObjectPos(listposition);
            object.C2(layer);
        }

        super.C2(layer);
    }

    void C3(FREEView view)
    {
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getNextObjectPos(listposition);
            object.C3(view);
        }

        super.C3(view);
    }

    public FREEObject pickObject(Point point, boolean flag)
    {
        if(!super.isPointInObj(point))
            return null;
        for(FREEListPosition listposition = getLastObjectPos(); listposition != null;)
        {
            Object obj = getObjectAtPos(listposition);
            listposition = getPrevObjectPos(listposition);
            if(((FREEObject) (obj)).isVisible() && ((FREEObject) (obj)).isPointInObj(point))
            {
                if(obj instanceof FREEArea)
                {
                    FREEObject object = ((FREEArea)obj).pickObject(point, flag);
                    if(object != null)
                        return object;
                }
                if(!flag)
                    return ((FREEObject) (obj));
                if(((FREEObject) (obj)).isSelectable())
                    return ((FREEObject) (obj));
                while(((FREEObject) (obj)).getParent() != null)
                {
                    obj = ((FREEObject) (obj)).getParent();
                    if(((FREEObject) (obj)).isGrabChildSelection())
                        return ((FREEObject) (obj));
                }
            }
        }

        return null;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getNextObjectPos(listposition);
            if(object.isVisible())
                object.paint(graphics2d, view);
        }

    }

    public void expandRectByPenWidth(Rectangle rectangle)
    {
        int i = rectangle.x;
        int j = rectangle.y;
        int k = i + rectangle.width;
        int l = j + rectangle.height;
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getNextObjectPos(listposition);
            rectangle.setBounds(object.getBoundingRect());
            object.expandRectByPenWidth(rectangle);
            i = Math.min(i, rectangle.x);
            j = Math.min(j, rectangle.y);
            k = Math.max(k, rectangle.x + rectangle.width);
            l = Math.max(l, rectangle.y + rectangle.height);
        }

        rectangle.setBounds(i, j, k - i, l - j);
    }

    protected void geometryChange(Rectangle rectangle)
    {
        Rectangle rectangle1 = getBoundingRect();
        if(rectangle.equals(rectangle1))
            return;
        if(rectangle.width == getWidth() && rectangle.height == getHeight())
        {
            for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
            {
                FREEObject object = getObjectAtPos(listposition);
                listposition = getNextObjectPos(listposition);
                int i = object.getLeft() - rectangle.x;
                int j = object.getTop() - rectangle.y;
                object.setBoundingRect(rectangle1.x + i, rectangle1.y + j, object.getWidth(), object.getHeight());
            }

        } else
        {
            double d = 1.0D;
            if(rectangle.width != 0)
                d = (double)rectangle1.width / (double)rectangle.width;
            double d1 = 1.0D;
            if(rectangle.height != 0)
                d1 = (double)rectangle1.height / (double)rectangle.height;
            for(FREEListPosition listposition1 = getFirstObjectPos(); listposition1 != null;)
            {
                FREEObject object1 = getObjectAtPos(listposition1);
                listposition1 = getNextObjectPos(listposition1);
                Rectangle rectangle2 = object1.getBoundingRect();
                int k = rectangle1.x + (int)Math.rint((double)(rectangle2.x - rectangle.x) * d);
                int l = rectangle1.y + (int)Math.rint((double)(rectangle2.y - rectangle.y) * d1);
                int i1 = (int)Math.rint((double)rectangle2.width * d);
                int j1 = (int)Math.rint((double)rectangle2.height * d1);
                object1.setBoundingRect(k, l, i1, j1);
            }

        }
    }

    protected boolean geometryChangeChild(FREEObject object, Rectangle rectangle)
    {
        if(isSuspendChildUpdates())
            return false;
        Rectangle rectangle1 = object.getBoundingRect();
        if(rectangle1.equals(rectangle))
            return false;
        if(C9())
            return true;
        Rectangle rectangle2 = super.getBoundingRect();
        if(!object.C7() || _mth011A(rectangle, rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height))
        {
            C8(true);
        } else
        {
            int i = rectangle2.x;
            int j = rectangle2.y;
            int k = i + rectangle2.width;
            int l = j + rectangle2.height;
            i = Math.min(i, rectangle1.x);
            j = Math.min(j, rectangle1.y);
            k = Math.max(k, rectangle1.x + rectangle1.width);
            l = Math.max(l, rectangle1.y + rectangle1.height);
            if(setBoundingRectForce(i, j, k - i, l - j) && getParent() != null)
                getParent().geometryChangeChild(((FREEObject) (this)), C1());
        }
        return true;
    }

    private boolean _mth011A(Rectangle rectangle, int i, int j, int k, int l)
    {
        Rectangle rectangle1 = super.getBoundingRect();
        if(rectangle1.x == rectangle.x && i > rectangle1.x)
            return true;
        if(rectangle1.y == rectangle.y && j > rectangle1.y)
            return true;
        int i1 = rectangle1.x + rectangle1.width;
        int j1 = rectangle1.y + rectangle1.height;
        int k1 = i + k;
        int l1 = j + l;
        if(i1 == rectangle.x + rectangle.width && k1 < i1)
            return true;
        return j1 == rectangle.y + rectangle.height && l1 < j1;
    }

    public Rectangle getBoundingRect()
    {
        if(C9())
            _mth011B();
        return super.getBoundingRect();
    }

    public void setBoundingRect(int i, int j, int k, int l)
    {
        C8(false);
        boolean flag = isSuspendChildUpdates();
        setSuspendChildUpdates(true);
        super.setBoundingRect(i, j, k, l);
        setSuspendChildUpdates(flag);
    }

    private void _mth011B()
    {
        Rectangle rectangle = null;
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getNextObjectPos(listposition);
            if(!object.C9())
                if(rectangle == null)
                    rectangle = new Rectangle(object.getBoundingRect());
                else
                    rectangle.add(object.getBoundingRect());
        }

        if(rectangle != null)
        {
            C8(false);
            if(setBoundingRectForce(rectangle) && getParent() != null)
                getParent().geometryChangeChild(this, C1());
        }
    }

    public int getNumObjects()
    {
        return m_children.getNumObjects();
    }

    public boolean isEmpty()
    {
        return m_children.isEmpty();
    }

    public FREEListPosition addObjectAtHead(FREEObject object)
    {
        if(object == null)
            return null;
        if(object.getParent() != null)
        {
            if(object.getParent() != this)
                return null;
            FREEListPosition listposition = getFirstObjectPos();
            if(getObjectAtPos(listposition) == object)
            {
                return listposition;
            } else
            {
                m_children.removeObject(object);
                FREEListPosition listposition2 = m_children.addObjectAtHead(object);
                object.update(10);
                return listposition2;
            }
        }
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
            return null;
        FREEListPosition listposition1 = m_children.addObjectAtHead(object);
        object.setParent(this);
        FREELayer layer = getLayer();
        if(layer != null)
        {
            object.C2(layer);
        } else
        {
            FREEView view = getView();
            if(view != null)
                object.C3(view);
        }
        geometryChangeChild(object, object.C1());
        return listposition1;
    }

    public FREEListPosition addObjectAtTail(FREEObject object)
    {
        if(object == null)
            return null;
        if(object.getParent() != null)
        {
            if(object.getParent() != this)
                return null;
            FREEListPosition listposition = getLastObjectPos();
            if(getObjectAtPos(listposition) == object)
            {
                return listposition;
            } else
            {
                m_children.removeObject(object);
                FREEListPosition listposition2 = m_children.addObjectAtTail(object);
                object.update(10);
                return listposition2;
            }
        }
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
            return null;
        FREEListPosition listposition1 = m_children.addObjectAtTail(object);
        object.setParent(this);
        FREELayer layer = getLayer();
        if(layer != null)
        {
            object.C2(layer);
        } else
        {
            FREEView view = getView();
            if(view != null)
                object.C3(view);
        }
        geometryChangeChild(object, object.C1());
        return listposition1;
    }

    public FREEListPosition insertObjectBefore(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        if(object.getParent() != null)
        {
            if(object.getParent() != this)
                return null;
            FREEListPosition listposition1 = m_children.findObject(object);
            if(listposition1 != null)
            {
                m_children.removeObjectAtPos(listposition1);
                FREEListPosition listposition2 = m_children.insertObjectBefore(listposition, object);
                object.update(10);
                return listposition2;
            }
        }
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
            return null;
        listposition = m_children.insertObjectBefore(listposition, object);
        object.setParent(this);
        FREELayer layer = getLayer();
        if(layer != null)
        {
            object.C2(layer);
        } else
        {
            FREEView view = getView();
            if(view != null)
                object.C3(view);
        }
        geometryChangeChild(object, object.C1());
        return listposition;
    }

    public FREEListPosition insertObjectAfter(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        if(object.getParent() != null)
        {
            if(object.getParent() != this)
                return null;
            FREEListPosition listposition1 = m_children.findObject(object);
            if(listposition1 != null)
            {
                m_children.removeObjectAtPos(listposition1);
                FREEListPosition listposition2 = m_children.insertObjectAfter(listposition, object);
                object.update(10);
                return listposition2;
            }
        }
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
            return null;
        listposition = m_children.insertObjectAfter(listposition, object);
        object.setParent(this);
        FREELayer layer = getLayer();
        if(layer != null)
        {
            object.C2(layer);
        } else
        {
            FREEView view = getView();
            if(view != null)
                object.C3(view);
        }
        geometryChangeChild(object, object.C1());
        return listposition;
    }

    public void bringObjectToFront(FREEObject object)
    {
        addObjectAtTail(object);
    }

    public void sendObjectToBack(FREEObject object)
    {
        addObjectAtHead(object);
    }

    public void removeObject(FREEObject object)
    {
        if(object == null)
            return;
        if(object.getParent() != this)
            return;
        FREEListPosition listposition = m_children.findObject(object);
        if(listposition != null)
            removeObjectAtPos(listposition);
    }

    public FREEObject removeObjectAtPos(FREEListPosition listposition)
    {
        FREEObject object = m_children.removeObjectAtPos(listposition);
        if(object != null)
        {
            if(!isEmpty())
            {
                Rectangle rectangle = getBoundingRect();
                if(_mth011A(rectangle, (rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2, 0, 0))
                    C8(true);
            }
            if(object.getLayer() != null)
                object.C2(null);
            else
            if(object.getView() != null)
                object.C3(null);
            object.setParent(null);
        }
        return object;
    }

    public void removeAll()
    {
        while(getFirstObjectPos()!=null)
        {
            removeObjectAtPos(getFirstObjectPos());
        }
    }

    public FREEListPosition getFirstObjectPos()
    {
        return m_children.getFirstObjectPos();
    }

    public FREEListPosition getLastObjectPos()
    {
        return m_children.getLastObjectPos();
    }

    public FREEListPosition getNextObjectPos(FREEListPosition listposition)
    {
        return m_children.getNextObjectPos(listposition);
    }

    public FREEListPosition getNextObjectPosAtTop(FREEListPosition listposition)
    {
        return m_children.getNextObjectPos(listposition);
    }

    public FREEListPosition getPrevObjectPos(FREEListPosition listposition)
    {
        return m_children.getPrevObjectPos(listposition);
    }

    public FREEObject getObjectAtPos(FREEListPosition listposition)
    {
        return m_children.getObjectAtPos(listposition);
    }

    public FREEListPosition findObject(FREEObject object)
    {
        return m_children.findObject(object);
    }

    void C4(FREEListPosition listposition)
    {
        _fld011A = listposition;
    }

    FREEListPosition C5()
    {
        if(_fld011A == null)
        {
            Object obj = getParent();
            if(obj == null)
                obj = getDocument();
            if(obj == null)
                obj = getView();
            if(obj != null)
            {
                FREEListPosition listposition = ((FREEObjectCollection) (obj)).findObject(this);
                if(listposition != null)
                    _fld011A = listposition;
            }
        }
        return _fld011A;
    }
}
