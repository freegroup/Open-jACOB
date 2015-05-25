package de.shorti.dragdrop;


import java.io.Serializable;
import java.util.*;

// Referenced classes of package de.shorti.dragdrop:
//            FREECopyEnvironment

public class FREECopyMap
    implements FREECopyEnvironment, Serializable
{

    public FREECopyMap()
    {
        m_nap = new HashMap();
        m_delayeds = new Vector();
    }

    public void clear()
    {
        m_nap.clear();
    }

    public boolean containsKey(Object obj)
    {
        return m_nap.containsKey(obj);
    }

    public boolean containsValue(Object obj)
    {
        return m_nap.containsValue(obj);
    }

    public Set entrySet()
    {
        return m_nap.entrySet();
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof FREECopyMap)
        {
            FREECopyMap copymap = (FREECopyMap)obj;
            return m_nap.equals(copymap.m_nap) && m_delayeds.equals(copymap.m_delayeds);
        } else
        {
            return false;
        }
    }

    public Object get(Object obj)
    {
        return m_nap.get(obj);
    }

    public int hashCode()
    {
        return m_nap.hashCode();
    }

    public boolean isEmpty()
    {
        return m_nap.isEmpty();
    }

    public Set keySet()
    {
        return m_nap.keySet();
    }

    public Object put(Object obj, Object obj1)
    {
        return m_nap.put(obj, obj1);
    }

    public void putAll(Map map)
    {
        m_nap.putAll(map);
    }

    public Object remove(Object obj)
    {
        return m_nap.remove(obj);
    }

    public int size()
    {
        return m_nap.size();
    }

    public Collection values()
    {
        return m_nap.values();
    }

    public void clearDelayeds()
    {
        m_delayeds.clear();
    }

    public boolean isEmptyDelayeds()
    {
        return m_delayeds.isEmpty();
    }

    public int sizeDelayeds()
    {
        return m_delayeds.size();
    }

    public boolean isDelayed(Object obj)
    {
        return m_delayeds.contains(obj);
    }

    public void delay(Object obj)
    {
        m_delayeds.add(obj);
    }

    public void removeDelayed(Object obj)
    {
        m_delayeds.remove(obj);
    }

    public Vector getDelayeds()
    {
        return m_delayeds;
    }

    protected HashMap m_nap;
    protected Vector m_delayeds;
}
