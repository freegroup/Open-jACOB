package de.shorti.dragdrop;


import java.io.Serializable;

// Referenced classes of package de.shorti.dragdrop:
//            FREEListPosition, FREEObject

class FREEObjList  implements Serializable
{
    public FREEObjList()
    {
        m_head = null;
        m_tail = null;
        m_numObjects = 0;
        m_owner = false;
    }

    public FREEObjList(boolean flag)
    {
        m_head = null;
        m_tail = null;
        m_numObjects = 0;
        m_owner = false;
        m_owner = flag;
    }

    public void finalize()
    {
        FREEListPosition listposition;
        for(; m_head != null; m_head = listposition)
        {
            listposition = m_head.next;
            m_head.next = null;
            m_head.prev = null;
            if(m_owner && m_head.obj != null)
                m_head.obj.C4(null);
        }

        m_tail = null;
        Object obj = null;
    }

    public int getNumObjects()
    {
        return m_numObjects;
    }

    public boolean isEmpty()
    {
        return m_head == null;
    }

    public FREEListPosition addObjectAtHead(FREEObject object)
    {
        if(object == null)
            return null;
        FREEListPosition listposition = new FREEListPosition(object, null, m_head);
        if(m_head != null)
            m_head.prev = listposition;
        m_head = listposition;
        if(m_tail == null)
            m_tail = m_head;
        m_numObjects++;
        if(m_owner)
            object.C4(m_head);
        return m_head;
    }

    public FREEListPosition addObjectAtTail(FREEObject object)
    {
        if(object == null)
            return null;
        FREEListPosition listposition = new FREEListPosition(object, m_tail, null);
        if(m_tail != null)
            m_tail.next = listposition;
        m_tail = listposition;
        if(m_head == null)
            m_head = m_tail;
        m_numObjects++;
        if(m_owner)
            object.C4(m_tail);
        return m_tail;
    }

    public FREEListPosition insertObjectBefore(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        FREEListPosition listposition1 = new FREEListPosition(object, listposition.prev, listposition);
        if(listposition.prev != null)
            listposition.prev.next = listposition1;
        else
            m_head = listposition1;
        listposition.prev = listposition1;
        m_numObjects++;
        if(m_owner)
            object.C4(listposition1);
        return listposition1;
    }

    public FREEListPosition insertObjectAfter(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        FREEListPosition listposition1 = new FREEListPosition(object, listposition, listposition.next);
        if(listposition.next != null)
            listposition.next.prev = listposition1;
        else
            m_tail = listposition1;
        listposition.next = listposition1;
        m_numObjects++;
        if(m_owner)
            object.C4(listposition1);
        return listposition1;
    }

    public void removeObject(FREEObject object)
    {
        FREEListPosition listposition = null;
        if(m_owner)
            listposition = object.C5();
        if(listposition == null)
            listposition = findObject(object);
        if(listposition != null)
            removeObjectAtPos(listposition);
    }

    public FREEObject removeObjectAtPos(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        FREEObject object = listposition.obj;
        if(listposition == m_head)
            m_head = listposition.next;
        if(listposition == m_tail)
            m_tail = listposition.prev;
        if(listposition.prev != null)
            listposition.prev.next = listposition.next;
        if(listposition.next != null)
            listposition.next.prev = listposition.prev;
        listposition.prev = null;
        listposition.next = null;
        if(m_owner)
            object.C4(null);
        m_numObjects--;
        return object;
    }

    public FREEListPosition getFirstObjectPos()
    {
        return m_head;
    }

    public FREEListPosition getLastObjectPos()
    {
        return m_tail;
    }

    public FREEListPosition getNextObjectPos(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        else
            return listposition.next;
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
        for(FREEListPosition listposition = m_head; listposition != null; listposition = listposition.next)
            if(listposition.obj == object)
                return listposition;

        return null;
    }

    protected FREEListPosition m_head;
    protected FREEListPosition m_tail;
    protected int m_numObjects;
    protected boolean m_owner;
}
