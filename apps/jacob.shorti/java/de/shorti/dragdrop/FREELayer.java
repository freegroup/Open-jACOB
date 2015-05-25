//
//
//
// Source File Name:   com/nwoods//FREELayer

package de.shorti.dragdrop;


import java.awt.Point;
import java.io.Serializable;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObjList, FREEArea, FREEObjectCollection, FREEObject,
//            FREEDocument, FREEListPosition

public class FREELayer
    implements FREEObjectCollection, Serializable
{

    FREELayer()
    {
        m_nextLayer = null;
        m_prevLayer = null;
        m_visible = true;
        m_transparency = 1.0F;
        m_compositeRule = 3;
        m_nodifiable = true;
        m_identifier = null;
    }

    void C0(FREEDocument document)
    {
        m_objects = new FREEObjList(true);
        m_document = document;
    }

    void _mth0121(FREELayer layer, FREELayer layer1)
    {
        m_nextLayer = layer;
        m_prevLayer = layer1;
        if(layer != null)
            layer.m_prevLayer = this;
        if(layer1 != null)
            layer1.m_nextLayer = this;
    }

    void _mth0122()
    {
        if(m_prevLayer != null)
            m_prevLayer.m_nextLayer = m_nextLayer;
        if(m_nextLayer != null)
            m_nextLayer.m_prevLayer = m_prevLayer;
    }

    public FREEDocument getDocument()
    {
        return m_document;
    }

    public FREELayer getNextLayer()
    {
        return m_nextLayer;
    }

    public FREELayer getPrevLayer()
    {
        return m_prevLayer;
    }

    public int getNumObjects()
    {
        return m_objects.getNumObjects();
    }

    public boolean isEmpty()
    {
        return m_objects.isEmpty();
    }

    public FREEListPosition addObjectAtHead(FREEObject object)
    {
        if(object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
        {
            if(object.getDocument() != getDocument())
                return null;
            FREEListPosition listposition = getFirstObjectPos();
            if(getObjectAtPos(listposition) == object)
            {
                return listposition;
            } else
            {
                object.getLayer().m_objects.removeObject(object);
                FREEListPosition listposition2 = m_objects.addObjectAtHead(object);
                object.C2(this);
                object.update(10);
                return listposition2;
            }
        } else
        {
            FREEListPosition listposition1 = m_objects.addObjectAtHead(object);
            object.C2(this);
            getDocument().updateDocumentSize(object);
            return listposition1;
        }
    }

    public FREEListPosition addObjectAtTail(FREEObject object)
    {
        if(object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
        {
            if(object.getDocument() != getDocument())
                return null;
            FREEListPosition listposition = getLastObjectPos();
            if(getObjectAtPos(listposition) == object)
            {
                return listposition;
            } else
            {
                object.getLayer().m_objects.removeObject(object);
                FREEListPosition listposition2 = m_objects.addObjectAtTail(object);
                object.C2(this);
                object.update(10);
                return listposition2;
            }
        } else
        {
            FREEListPosition listposition1 = m_objects.addObjectAtTail(object);
            object.C2(this);
            getDocument().updateDocumentSize(object);
            return listposition1;
        }
    }

    public FREEListPosition insertObjectBefore(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
        {
            if(object.getDocument() != getDocument())
                return null;
            if(getObjectAtPos(listposition) == object)
            {
                return listposition;
            } else
            {
                object.getLayer().m_objects.removeObject(object);
                FREEListPosition listposition1 = m_objects.insertObjectBefore(listposition, object);
                object.C2(this);
                object.update(10);
                return listposition1;
            }
        } else
        {
            FREEListPosition listposition2 = m_objects.insertObjectBefore(listposition, object);
            object.C2(this);
            getDocument().updateDocumentSize(object);
            return listposition2;
        }
    }

    public FREEListPosition insertObjectAfter(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getView() != null)
            return null;
        if(object.getDocument() != null)
        {
            if(object.getDocument() != getDocument())
                return null;
            if(getObjectAtPos(listposition) == object)
            {
                return listposition;
            } else
            {
                object.getLayer().m_objects.removeObject(object);
                FREEListPosition listposition1 = m_objects.insertObjectAfter(listposition, object);
                object.C2(this);
                object.update(10);
                return listposition1;
            }
        } else
        {
            FREEListPosition listposition2 = m_objects.insertObjectAfter(listposition, object);
            object.C2(this);
            getDocument().updateDocumentSize(object);
            return listposition2;
        }
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
        if(object.getLayer() != this)
            return;
        FREEArea area = object.getParent();
        if(area != null)
        {
            area.removeObject(object);
        } else
        {
            FREEListPosition listposition = findObject(object);
            if(listposition != null)
                removeObjectAtPos(listposition);
        }
    }

    public FREEObject removeObjectAtPos(FREEListPosition listposition)
    {
        FREEObject object = m_objects.removeObjectAtPos(listposition);
        if(object != null)
            object.C2(null);
        return object;
    }

    public FREEObject pickObject(Point point, boolean flag)
    {
        for(FREEListPosition listposition = getLastObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getPrevObjectPos(listposition);
            if(object.isVisible() && object.isPointInObj(point))
            {
                if(object instanceof FREEArea)
                {
                    FREEObject object1 = ((FREEArea)object).pickObject(point, flag);
                    if(object1 != null)
                        return object1;
                }
                if(!flag)
                    return object;
                if(object.isSelectable())
                    return object;
            }
        }

        return null;
    }

    public FREEListPosition getFirstObjectPos()
    {
        return m_objects.getFirstObjectPos();
    }

    public FREEListPosition getLastObjectPos()
    {
        return m_objects.getLastObjectPos();
    }

    public FREEListPosition getNextObjectPos(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        Object obj = listposition.obj;
        if(obj instanceof FREEArea)
        {
            FREEArea area = (FREEArea)obj;
            if(!area.isEmpty())
                return area.getFirstObjectPos();
        }
        FREEListPosition listposition1;
        for(listposition = listposition.next; listposition == null; listposition = listposition1.next)
        {
            FREEArea area1 = ((FREEObject) (obj)).getParent();
            if(area1 == null)
                return null;
            listposition1 = area1.C5();
            obj = area1;
        }

        return listposition;
    }

    public FREEListPosition getNextObjectPosAtTop(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        for(Object obj = listposition.obj; ((FREEObject) (obj)).getParent() != null; obj = ((FREEObject) (obj)).getParent())
            listposition = ((FREEObject) (obj)).getParent().C5();

        return listposition.next;
    }

    public FREEListPosition getPrevObjectPos(FREEListPosition listposition)
    {
        return m_objects.getPrevObjectPos(listposition);
    }

    public FREEObject getObjectAtPos(FREEListPosition listposition)
    {
        return m_objects.getObjectAtPos(listposition);
    }

    public FREEListPosition findObject(FREEObject object)
    {
        return m_objects.findObject(object);
    }

    public void removeAll()
    {
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null; listposition = getFirstObjectPos())
            removeObjectAtPos(listposition);

    }

    public boolean isVisible()
    {
        return m_visible;
    }

    public void setVisible(boolean flag)
    {
        if(isVisible() != flag)
        {
            m_visible = flag;
            getDocument().fireUpdate(13, 0, this);
        }
    }

    public float getTransparency()
    {
        return m_transparency;
    }

    public void setTransparency(float f)
    {
        if(getTransparency() != f)
        {
            m_transparency = f;
            getDocument().fireUpdate(13, 1, this);
        }
    }

    int _mth0123()
    {
        return m_compositeRule;
    }

    public boolean isModifiable()
    {
        return m_nodifiable && getDocument().isModifiable();
    }

    public void setModifiable(boolean flag)
    {
        if(m_nodifiable != flag)
        {
            m_nodifiable = flag;
            getDocument().fireUpdate(8, 0, this);
        }
    }

    public Object getIdentifier()
    {
        return m_identifier;
    }

    public void setIdentifier(Object obj)
    {
        m_identifier = obj;
    }

    private FREEObjList m_objects;
    private FREEDocument m_document;
    private FREELayer m_nextLayer;
    private FREELayer m_prevLayer;
    private boolean m_visible;
    private float m_transparency;
    private int m_compositeRule;
    private boolean m_nodifiable;
    private Object m_identifier;
}
