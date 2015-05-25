//
//
//
// Source File Name:   com/nwoods//FREESelection

package de.shorti.dragdrop;


import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObjList, FREEHandle, FREEObjectSimpleCollection, FREEPen,
//            FREEObject, FREEView, FREEDrawable, FREEBrush,
//            FREEDocument, FREEListPosition

public class FREESelection
    implements FREEObjectSimpleCollection, Transferable, Serializable
{

    public FREESelection()
    {
        m_selectedObjects = new FREEObjList();
        m_handles = new HashMap();
        _fld0117 = null;
        _fld0118 = FREEPen.black;
        _fld0119 = null;
        m_resizeHandleSizeInViewCoords = false;
        m_view = null;
    }

    public FREESelection(FREEView view)
    {
        m_selectedObjects = new FREEObjList();
        m_handles = new HashMap();
        _fld0117 = null;
        _fld0118 = FREEPen.black;
        _fld0119 = null;
        m_resizeHandleSizeInViewCoords = false;
        m_view = view;
    }

    public final FREEView getView()
    {
        return m_view;
    }

    public int getNumObjects()
    {
        return m_selectedObjects.getNumObjects();
    }

    public boolean isEmpty()
    {
        return m_selectedObjects.isEmpty();
    }

    public FREEListPosition getFirstObjectPos()
    {
        return m_selectedObjects.getFirstObjectPos();
    }

    public FREEObject getObjectAtPos(FREEListPosition listposition)
    {
        return m_selectedObjects.getObjectAtPos(listposition);
    }

    public FREEListPosition getNextObjectPos(FREEListPosition listposition)
    {
        return m_selectedObjects.getNextObjectPos(listposition);
    }

    public FREEListPosition getNextObjectPosAtTop(FREEListPosition listposition)
    {
        return m_selectedObjects.getNextObjectPos(listposition);
    }

    public FREEObject getPrimarySelection()
    {
        FREEListPosition listposition = m_selectedObjects.getFirstObjectPos();
        if(listposition == null)
            return null;
        else
            return m_selectedObjects.getObjectAtPos(listposition);
    }

    public FREEObject selectObject(FREEObject object)
    {
        if(object == null)
            return null;
        if(getPrimarySelection() == object && getNumObjects() == 1)
        {
            return object;
        } else
        {
            clearSelection();
            return extendSelection(object);
        }
    }

    public FREEObject extendSelection(FREEObject object)
    {
        if(object == null)
            return null;
        object = object.redirectSelection();
        if(object == null)
            return null;
        if(!isSelected(object))
            _mth0117(object);
        return object;
    }

    public FREEObject toggleSelection(FREEObject object)
    {
        if(object == null)
            return null;
        object = object.redirectSelection();
        if(object == null)
            return null;
        if(isSelected(object))
            _mth0118(object);
        else
            _mth0117(object);
        return object;
    }

    public void clearSelection(FREEObject object)
    {
        if(object == null)
        {
            clearSelection();
            return;
        }
        object = object.redirectSelection();
        if(object == null)
            return;
        if(isSelected(object))
            _mth0118(object);
    }

    public void clearSelection()
    {
        FREEView view = getView();
        for(FREEListPosition listposition = m_selectedObjects.getFirstObjectPos(); listposition != null; listposition = m_selectedObjects.getFirstObjectPos())
        {
            FREEObject object = m_selectedObjects.getObjectAtPos(listposition);
            m_selectedObjects.removeObjectAtPos(listposition);
            object.lostSelection(this);
            if(view != null)
                view.fireUpdate(6, 0, object);
            m_handles.remove(object);
        }

    }

    public boolean isInSelection(FREEObject object)
    {
        if(object == null)
            return false;
        object = object.redirectSelection();
        if(object == null)
            return false;
        else
            return isSelected(object);
    }

    public boolean isSelected(FREEObject object)
    {
        return m_handles.containsKey(object);
    }

    private void _mth0117(FREEObject object)
    {
        m_selectedObjects.addObjectAtTail(object);
        m_handles.put(object, null);
        FREEView view = getView();
        object.gainedSelection(this);
        if(view != null)
            view.fireUpdate(5, 0, object);
    }

    private void _mth0118(FREEObject object)
    {
        FREEObject object1 = getPrimarySelection();
        m_selectedObjects.removeObject(object);
        FREEView view = getView();
        object.lostSelection(this);
        if(view != null)
            view.fireUpdate(6, 0, object);
        m_handles.remove(object);
        if(object1 == object)
        {
            FREEObject object2 = getPrimarySelection();
            if(object2 != null)
            {
                object2.lostSelection(this);
                if(view != null)
                    view.fireUpdate(6, 0, object2);
                object2.gainedSelection(this);
                if(view != null)
                    view.fireUpdate(5, 0, object2);
            }
        }
    }

    public void clearSelectionHandles(FREEObject object)
    {
        if(object != null)
        {
            object.hideSelectionHandles(this);
        } else
        {
            for(FREEListPosition listposition = m_selectedObjects.getFirstObjectPos(); listposition != null;)
            {
                object = m_selectedObjects.getObjectAtPos(listposition);
                listposition = m_selectedObjects.getNextObjectPos(listposition);
                object.hideSelectionHandles(this);
            }

        }
    }

    public void restoreSelectionHandles(FREEObject object)
    {
        if(object != null)
        {
            if(isSelected(object) && object.isVisible())
                object.showSelectionHandles(this);
        }
        else
        {
            for(FREEListPosition listposition = m_selectedObjects.getFirstObjectPos(); listposition != null;)
            {
                object = m_selectedObjects.getObjectAtPos(listposition);
                listposition = m_selectedObjects.getNextObjectPos(listposition);
                if(object.isVisible())
                    object.showSelectionHandles(this);
            }

        }
    }

    public void showHandles(FREEObject object)
    {
        Object obj = m_handles.get(object);
        if(obj == null)
            return;
        FREEView view = getView();
        if(view != null)
            if(obj instanceof Vector)
            {
                Vector vector = (Vector)obj;
                for(int i = 0; i < vector.size(); i++)
                {
                    FREEHandle handle1 = (FREEHandle)vector.elementAt(i);
                    handle1.setVisible(true);
                }

            } else
            {
                FREEHandle handle = (FREEHandle)obj;
                handle.setVisible(true);
            }
    }

    public void hideHandles(FREEObject object)
    {
        Object obj = m_handles.get(object);
        if(obj == null)
            return;
        FREEView view = getView();
        if(view != null)
            if(obj instanceof Vector)
            {
                Vector vector = (Vector)obj;
                for(int i = 0; i < vector.size(); i++)
                {
                    FREEHandle handle1 = (FREEHandle)vector.elementAt(i);
                    handle1.setVisible(false);
                }

            } else
            {
                FREEHandle handle = (FREEHandle)obj;
                handle.setVisible(false);
            }
    }

    public void createBoundingHandle(FREEObject object)
    {
        FREEView view = getView();
        Rectangle rectangle = view == null ? new Rectangle() : view.E5();
        rectangle.setBounds(object.getBoundingRect());
        rectangle.grow(1, 1);
        FREEHandle handle = new FREEHandle(rectangle, 0);
        handle.setHandleType(FREEObject.NoHandle);
        handle.setSelectable(false);
        Color color;
        if(view != null)
        {
            if(getPrimarySelection() == object)
                color = view.getPrimarySelectionColor();
            else
                color = view.getSecondarySelectionColor();
        } else
        {
            color = Color.black;
        }
        if(_fld0117 == null || !_fld0117.getColor().equals(color))
            _fld0117 = FREEPen.make(65535, 2, color);
        handle.setPen(_fld0117);
        handle.setBrush(null);
        addHandle(object, handle);
    }

    public boolean isResizeHandleSizeInViewCoords()
    {
        return m_resizeHandleSizeInViewCoords;
    }

    public void setResizeHandleSizeInViewCoords(boolean flag)
    {
        m_resizeHandleSizeInViewCoords = flag;
    }

    public FREEHandle allocateResizeHandle(FREEObject object, int i, int j, int k)
    {
        FREEView view = getView();
        Rectangle rectangle = view == null ? new Rectangle() : view.E5();
        rectangle.width = FREEHandle.getHandleWidth();
        rectangle.height = FREEHandle.getHandleHeight();
        if(isResizeHandleSizeInViewCoords() && view != null && view.getScale() != 1.0D)
        {
            Dimension dimension = view.E4();
            dimension.setSize(rectangle.width, rectangle.height);
            view.convertViewToDoc(dimension);
            rectangle.width = dimension.width;
            if(rectangle.width < 2)
                rectangle.width = 2;
            rectangle.height = dimension.height;
            if(rectangle.height < 2)
                rectangle.height = 2;
        }
        rectangle.x = i - rectangle.width / 2;
        rectangle.y = j - rectangle.height / 2;
        FREEHandle handle = new FREEHandle(rectangle, 0);
        return handle;
    }

    public void createResizeHandle(FREEObject object, int i, int j, int k, boolean flag)
    {
        FREEView view = getView();
        FREEHandle handle = allocateResizeHandle(object, i, j, k);
        handle.setHandleType(k);
        if(k == -1)
            handle.setSelectable(false);
        else
            handle.setSelectable(true);
        handle.setPen(_fld0118);
        if(flag)
        {
            Color color;
            if(view != null)
            {
                if(getPrimarySelection() == object)
                    color = view.getPrimarySelectionColor();
                else
                    color = view.getSecondarySelectionColor();
            } else
            {
                color = Color.black;
            }
            if(_fld0119 == null || !_fld0119.getColor().equals(color))
                _fld0119 = FREEBrush.make(65535, color);
            handle.setBrush(_fld0119);
        } else
        {
            handle.setBrush(null);
        }
        addHandle(object, handle);
    }

    public void addHandle(FREEObject object, FREEHandle handle)
    {
        handle.setHandleFor(object);
        Object obj = m_handles.get(object);
        if(obj == null)
            m_handles.put(object, handle);
        else
        if(obj instanceof Vector)
        {
            Vector vector = (Vector)obj;
            vector.addElement(handle);
        } else
        {
            Vector vector1 = new Vector();
            vector1.addElement(obj);
            vector1.addElement(handle);
            m_handles.put(object, vector1);
        }
        if(getView() != null)
            getView().addObjectAtHead(handle);
    }

    public void deleteHandles(FREEObject object)
    {
        Object obj = m_handles.get(object);
        if(obj == null)
            return;
        FREEView view = getView();
        if(view != null)
            if(obj instanceof Vector)
            {
                Vector vector = (Vector)obj;
                for(int i = 0; i < vector.size(); i++)
                {
                    FREEHandle handle1 = (FREEHandle)vector.elementAt(i);
                    view.removeObject(handle1);
                }

            } else
            {
                FREEHandle handle = (FREEHandle)obj;
                view.removeObject(handle);
            }
        m_handles.put(object, null);
    }

    public int getNumHandles(FREEObject object)
    {
        Object obj = m_handles.get(object);
        if(obj == null)
            return 0;
        if(obj instanceof Vector)
        {
            Vector vector = (Vector)obj;
            return vector.size();
        } else
        {
            return 1;
        }
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        FREEDocument document = getDocument();
        if(document != null)
            return document.getTransferDataFlavors();
        else
            return new DataFlavor[0];
    }

    public boolean isDataFlavorSupported(DataFlavor dataflavor)
    {
        FREEDocument document = getDocument();
        if(document != null)
            return document.isDataFlavorSupported(dataflavor);
        else
            return false;
    }

    public synchronized Object getTransferData(DataFlavor dataflavor)
        throws UnsupportedFlavorException, IOException
    {
        if(isDataFlavorSupported(dataflavor))
            return this;
        else
            throw new UnsupportedFlavorException(dataflavor);
    }

    private FREEDocument getDocument()
    {
        FREEDocument document = null;
        if(getView() != null)
        {
            document = getView().getDocument();
        } else
        {
            for(FREEListPosition listposition = m_selectedObjects.getFirstObjectPos(); listposition != null;)
            {
                FREEObject object = m_selectedObjects.getObjectAtPos(listposition);
                listposition = m_selectedObjects.getNextObjectPos(listposition);
                document = object.getDocument();
                if(document != null)
                    break;
                FREEView view = object.getView();
                if(view != null)
                    document = view.getDocument();
                if(document != null)
                    break;
            }

        }
        return document;
    }

    private FREEObjList m_selectedObjects;
    private HashMap m_handles;
    private transient FREEView m_view;
    private transient FREEPen _fld0117;
    private transient FREEPen _fld0118;
    private transient FREEBrush _fld0119;
    private boolean m_resizeHandleSizeInViewCoords;
}
