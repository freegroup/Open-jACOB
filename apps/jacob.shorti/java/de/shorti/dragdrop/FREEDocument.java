package de.shorti.dragdrop;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

// Referenced classes of package de.shorti.dragdrop:
//            FREELayer, FREEArea, FREEDocumentEvent, FREEDocumentListener,
//            FREECopyMap, FREELink, FREEPort, FREEObjectCollection,
//            FREEObject, FREEListPosition, FREEObjectSimpleCollection, FREECopyEnvironment

public class FREEDocument
    implements FREEObjectCollection, Transferable, Serializable
{

    public FREEDocument()
    {
        m_defaultLayer = null;
        m_firstLayer = null;
        m_lastLayer = null;
        m_documentSize = new Dimension();
        m_paperColor = Color.white;
        m_nodifiable = true;
        _fld011C = false;
        _fld011F = null;
        _fld0120 = null;
        m_defaultLayer = new FREELayer();
        m_defaultLayer.C0(this);
        m_firstLayer = m_defaultLayer;
        m_lastLayer = m_defaultLayer;
    }

    public Dimension getDocumentSize()
    {
        return m_documentSize;
    }

    public void setDocumentSize(int i, int j)
    {
        Dimension dimension = getDocumentSize();
        if(dimension.width != i || dimension.height != j)
        {
            Dimension dimension1 = new Dimension(dimension);
            m_documentSize.width = i;
            m_documentSize.height = j;
            fireUpdate(5, 0, dimension1);
        }
    }

    public final void setDocumentSize(Dimension dimension)
    {
        setDocumentSize(dimension.width, dimension.height);
    }

    public void updateDocumentSize(FREEObject object)
    {
        Dimension dimension = getDocumentSize();
        Rectangle rectangle = object.getBoundingRect();
        int i = Math.max(dimension.width, rectangle.x + rectangle.width);
        int j = Math.max(dimension.height, rectangle.y + rectangle.height);
        if(i != dimension.width || j != dimension.height)
            setDocumentSize(i, j);
    }

    public Color getPaperColor()
    {
        return m_paperColor;
    }

    public void setPaperColor(Color color)
    {
        Color color1 = getPaperColor();
        if(color != color1 && color != null && !color.equals(color1))
        {
            m_paperColor = color;
            fireUpdate(6, 0, color1);
        }
    }

    public boolean isModifiable()
    {
        return m_nodifiable;
    }

    public void setModifiable(boolean flag)
    {
        if(isModifiable() != flag)
        {
            m_nodifiable = flag;
            fireUpdate(8, 0, null);
        }
    }

    public int getNumObjects()
    {
        int i = 0;
        for(FREELayer layer = getFirstLayer(); layer != null; layer = layer.getNextLayer())
            i += layer.getNumObjects();

        return i;
    }

    public boolean isEmpty()
    {
        return getNumObjects() == 0;
    }

    public FREEListPosition addObjectAtHead(FREEObject object)
    {
        return getFirstLayer().addObjectAtHead(object);
    }

    public FREEListPosition addObjectAtTail(FREEObject object)
    {
        return getLastLayer().addObjectAtTail(object);
    }

    public FREEListPosition insertObjectBefore(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
        {
            return null;
        } else
        {
            FREEObject object1 = getObjectAtPos(listposition);
            return object1.getLayer().insertObjectBefore(listposition, object);
        }
    }

    public FREEListPosition insertObjectAfter(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
        {
            return null;
        } else
        {
            FREEObject object1 = getObjectAtPos(listposition);
            return object1.getLayer().insertObjectAfter(listposition, object);
        }
    }

    public void bringObjectToFront(FREEObject object)
    {
        getLastLayer().bringObjectToFront(object);
    }

    public void sendObjectToBack(FREEObject object)
    {
        getFirstLayer().sendObjectToBack(object);
    }

    public void removeObject(FREEObject object)
    {
        if(object == null)
        {
            return;
        } else
        {
            object.getLayer().removeObject(object);
            return;
        }
    }

    public FREEObject removeObjectAtPos(FREEListPosition listposition)
    {
        FREEObject object = getObjectAtPos(listposition);
        return object.getLayer().removeObjectAtPos(listposition);
    }

    public void deleteContents()
    {
        for(; getLastLayer() != getFirstLayer(); removeLayer(getLastLayer()));
        getFirstLayer().removeAll();
        setDocumentSize(0, 0);
        fireUpdate(7, 0, null);
    }

    public FREEObject pickObject(Point point, boolean flag)
    {
        for(FREELayer layer = getLastLayer(); layer != null; layer = layer.getPrevLayer())
            if(layer.isVisible())
            {
                FREEObject object = layer.pickObject(point, flag);
                if(object != null)
                    return object;
            }

        return null;
    }

    public FREEListPosition getFirstObjectPos()
    {
        for(FREELayer layer = getFirstLayer(); layer != null; layer = layer.getNextLayer())
        {
            FREEListPosition listposition = layer.getFirstObjectPos();
            if(listposition != null)
                return listposition;
        }

        return null;
    }

    public FREEListPosition getLastObjectPos()
    {
        for(FREELayer layer = getLastLayer(); layer != null; layer = layer.getPrevLayer())
        {
            FREEListPosition listposition = layer.getLastObjectPos();
            if(listposition != null)
                return listposition;
        }

        return null;
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
            {
                FREELayer layer = ((FREEObject) (obj)).getLayer();
                for(layer = layer.getNextLayer(); layer != null; layer = layer.getNextLayer())
                {
                    FREEListPosition listposition2 = layer.getFirstObjectPos();
                    if(listposition2 != null)
                        return listposition2;
                }

                return null;
            }
            listposition1 = area1.C5();
            obj = area1;
        }

        return listposition;
    }

    public FREEListPosition getNextObjectPosAtTop(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        Object obj;
        for(obj = listposition.obj; ((FREEObject) (obj)).getParent() != null; obj = ((FREEObject) (obj)).getParent())
            listposition = ((FREEObject) (obj)).getParent().C5();

        listposition = listposition.next;
        if(listposition == null)
        {
            FREELayer layer = ((FREEObject) (obj)).getLayer();
            for(layer = layer.getNextLayer(); layer != null; layer = layer.getNextLayer())
            {
                FREEListPosition listposition1 = layer.getFirstObjectPos();
                if(listposition1 != null)
                    return listposition1;
            }

        }
        return listposition;
    }

    public FREEListPosition getPrevObjectPos(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        else
            return listposition.prev;
    }

    public FREEObject getObjectAtPos(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        else
            return listposition.obj;
    }

    public FREEListPosition findObject(FREEObject object)
    {
        for(FREELayer layer = getFirstLayer(); layer != null; layer = layer.getNextLayer())
        {
            FREEListPosition listposition = layer.findObject(object);
            if(listposition != null)
                return listposition;
        }

        return null;
    }

    public void addDocumentListener(FREEDocumentListener documentlistener)
    {
        _mth011C().addElement(documentlistener);
    }

    public void removeDocumentListener(FREEDocumentListener documentlistener)
    {
        _mth011C().removeElement(documentlistener);
    }

    Vector _mth011C()
    {
        if(_fld011F == null)
            _fld011F = new Vector();
        return _fld011F;
    }

    public void fireUpdate(int i, int j, Object obj)
    {
        if(isSuspendUpdates())
            return;
        if(_fld0120 == null)
        {
            _fld0120 = new FREEDocumentEvent(this, i, j, obj);
        } else
        {
            _fld0120.setHint(i);
            _fld0120.setFlags(j);
            _fld0120._mth0125(obj);
        }
        FREEDocumentEvent documentevent = _fld0120;
        _fld0120 = null;
        int k = _mth011C().size();
        for(int l = 0; l < k; l++)
        {
            FREEDocumentListener documentlistener = (FREEDocumentListener)_mth011C().elementAt(l);
            documentlistener.documentChanged(documentevent);
        }

        _fld0120 = documentevent;
        _fld0120._mth0125(null);
    }

    public boolean isSuspendUpdates()
    {
        return _fld011C;
    }

    public void setSuspendUpdates(boolean flag)
    {
        boolean flag1 = isSuspendUpdates();
        if(flag1 != flag)
        {
            _fld011C = flag;
            fireUpdate(1, 0, null);
        }
    }

    public static DataFlavor getStandardDataFlavor()
    {
        if(_fld011D == null)
            try
            {
                _fld011D = new DataFlavor(Class.forName("de.shorti.dragdrop.FREESelection"), "FREE Selection");
                DataFlavor adataflavor[] = {
                    _fld011D
                };
                _fld011E = adataflavor;
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        return _fld011D;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        getStandardDataFlavor();
        return _fld011E;
    }

    public boolean isDataFlavorSupported(DataFlavor dataflavor)
    {
        return dataflavor.equals(getStandardDataFlavor());
    }

    public synchronized Object getTransferData(DataFlavor dataflavor)
        throws UnsupportedFlavorException, IOException
    {
        if(dataflavor.equals(getStandardDataFlavor()))
            return this;
        else
            throw new UnsupportedFlavorException(dataflavor);
    }

    public FREECopyEnvironment createDefaultCopyEnvironment()
    {
        return new FREECopyMap();
    }

    public FREECopyEnvironment copyFromCollection(FREEObjectSimpleCollection objectsimplecollection)
    {
        return copyFromCollection(objectsimplecollection, new Point(), null);
    }

    public FREECopyEnvironment copyFromCollection(FREEObjectSimpleCollection objectsimplecollection, Point point, FREECopyEnvironment copyEnv)
    {
        if(copyEnv == null)
            copyEnv = createDefaultCopyEnvironment();
        Point point1 = new Point();
        for(FREEListPosition listposition = objectsimplecollection.getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = objectsimplecollection.getObjectAtPos(listposition);
            listposition = objectsimplecollection.getNextObjectPosAtTop(listposition);
            object = object.getTopLevelObject();
            FREEObject object1 = object.copyObject(copyEnv);
            if(object1 != null)
            {
                point1 = object1.getLocation(point1);
                object1.setLocationOffset(point1, point);
                FREELayer layer = object.getLayer();
                FREELayer layer1 = null;
                if(layer != null)
                    if(layer.getDocument() == this)
                        layer1 = layer;
                    else
                        layer1 = findLayer(layer.getIdentifier());
                if(layer1 == null)
                    layer1 = getDefaultLayer();
                layer1.addObjectAtTail(object1);
            }
        }

        Vector vector = copyEnv.getDelayeds();
        for(int i = 0; i < vector.size(); i++)
        {
            Object obj = vector.get(i);
            if(obj instanceof FREELink)
            {
                FREELink link = (FREELink)obj;
                FREELink link1 = (FREELink)copyEnv.get(link);
                FREEPort port = link.getFromPort();
                FREEPort port1 = link.getToPort();
                FREEPort port2 = (FREEPort)copyEnv.get(port);
                FREEPort port3 = (FREEPort)copyEnv.get(port1);
                if(port2 != null && port3 != null)
                {
                    link1.setFromPort(port2);
                    link1.setToPort(port3);
                } else
                {
                    removeObject(link1);
                }
            }
        }

        return copyEnv;
    }

    public int getNumLayers()
    {
        int i = 0;
        for(FREELayer layer = getFirstLayer(); layer != null; layer = layer.getNextLayer())
            i++;

        return i;
    }

    public FREELayer getFirstLayer()
    {
        return m_firstLayer;
    }

    public FREELayer getLastLayer()
    {
        return m_lastLayer;
    }

    public FREELayer getNextLayer(FREELayer layer)
    {
        if(layer == null)
            return null;
        else
            return layer.getNextLayer();
    }

    public FREELayer getPrevLayer(FREELayer layer)
    {
        if(layer == null)
            return null;
        else
            return layer.getPrevLayer();
    }

    public FREELayer getDefaultLayer()
    {
        return m_defaultLayer;
    }

    public void setDefaultLayer(FREELayer layer)
    {
        if(layer.getDocument() != this)
        {
            return;
        } else
        {
            m_defaultLayer = layer;
            return;
        }
    }

    public FREELayer addLayerAfter(FREELayer layer)
    {
        if(layer == null)
            layer = getLastLayer();
        FREELayer layer1 = new FREELayer();
        layer1.C0(this);
        layer1._mth0121(layer.getNextLayer(), layer);
        if(layer1.getNextLayer() == null)
            m_lastLayer = layer1;
        fireUpdate(10, 0, layer1);
        return layer1;
    }

    public FREELayer addLayerBefore(FREELayer layer)
    {
        if(layer == null)
            layer = getFirstLayer();
        FREELayer layer1 = new FREELayer();
        layer1.C0(this);
        layer1._mth0121(layer, layer.getPrevLayer());
        if(layer1.getPrevLayer() == null)
            m_firstLayer = layer1;
        fireUpdate(10, 0, layer1);
        return layer1;
    }

    public void removeLayer(FREELayer layer)
    {
        if(layer.getDocument() != this)
            return;
        if(layer == getFirstLayer() && layer == getLastLayer())
            return;
        layer.removeAll();
        if(layer.getPrevLayer() == null)
            m_firstLayer = layer;
        if(layer.getNextLayer() == null)
            m_lastLayer = layer;
        layer._mth0122();
        if(layer == getDefaultLayer())
            if(layer.getNextLayer() != null)
                setDefaultLayer(layer.getNextLayer());
            else
            if(layer.getPrevLayer() != null)
                setDefaultLayer(layer.getPrevLayer());
            else
                setDefaultLayer(getLastLayer());
        fireUpdate(11, 0, null);
    }

    public void insertLayerAfter(FREELayer layer, FREELayer layer1)
    {
        if(layer.getDocument() != this || layer1.getDocument() != this || layer == layer1 || layer1.getPrevLayer() == layer)
            return;
        if(m_firstLayer == layer1)
            m_firstLayer = layer1.getNextLayer();
        layer1._mth0122();
        layer1._mth0121(layer.getNextLayer(), layer);
        if(m_lastLayer == layer)
            m_lastLayer = layer1;
        fireUpdate(12, 0, layer1);
    }

    public void insertLayerBefore(FREELayer layer, FREELayer layer1)
    {
        if(layer.getDocument() != this || layer1.getDocument() != this || layer == layer1 || layer1.getNextLayer() == layer)
            return;
        if(m_lastLayer == layer1)
            m_lastLayer = layer1.getPrevLayer();
        layer1._mth0122();
        layer1._mth0121(layer, layer.getPrevLayer());
        if(m_firstLayer == layer)
            m_firstLayer = layer1;
        fireUpdate(12, 0, layer1);
    }

    public void bringLayerToFront(FREELayer layer)
    {
        insertLayerAfter(getLastLayer(), layer);
    }

    public void sendLayerToBack(FREELayer layer)
    {
        insertLayerBefore(getFirstLayer(), layer);
    }

    public FREELayer findLayer(Object obj)
    {
        if(obj == null)
            return null;
        for(FREELayer layer = getFirstLayer(); layer != null; layer = getNextLayer(layer))
        {
            Object obj1 = layer.getIdentifier();
            if(obj1 != null && obj1.equals(obj))
                return layer;
        }

        return null;
    }

    void _mth011D(FREEDocument document)
    {
        for(FREELayer layer = document.getFirstLayer(); layer != null; layer = document.getNextLayer(layer))
        {
            Object obj = layer.getIdentifier();
            if(obj != null && findLayer(obj) == null)
            {
                FREELayer layer1 = addLayerAfter(getLastLayer());
                layer1.setIdentifier(obj);
            }
        }

        Object obj1 = document.getDefaultLayer().getIdentifier();
        FREELayer layer2 = findLayer(obj1);
        if(layer2 != null)
            setDefaultLayer(layer2);
    }

    private FREELayer m_defaultLayer;
    private FREELayer m_firstLayer;
    private FREELayer m_lastLayer;
    private Dimension m_documentSize;
    private Color m_paperColor;
    private boolean m_nodifiable;
    private transient boolean _fld011C;
    private static transient DataFlavor _fld011D = null;
    private static transient DataFlavor _fld011E[] = null;
    private transient Vector _fld011F;
    private transient FREEDocumentEvent _fld0120;

}
