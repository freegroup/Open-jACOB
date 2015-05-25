package de.shorti.dragdrop;


import java.awt.*;
import java.util.EventObject;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObject, FREEDocument

public class FREEDocumentEvent extends EventObject
{

    public FREEDocumentEvent(FREEDocument document, int i, int j, Object obj)
    {
        super(document);
        _fld0112 = new Point();
        _fld0113 = new Dimension();
        _fld0114 = new Rectangle();
        m_hint = i;
        m_flags = j;
        m_obj = obj;
    }

    public int getHint()
    {
        return m_hint;
    }

    void setHint(int i)
    {
        m_hint = i;
    }

    public int getFlags()
    {
        return m_flags;
    }

    void setFlags(int i)
    {
        m_flags = i;
    }

    public FREEObject getFREEObject()
    {
        if(m_obj instanceof FREEObject)
            return (FREEObject)m_obj;
        else
            return null;
    }

    public Object getObject()
    {
        return m_obj;
    }

    void _mth0125(Object obj)
    {
        m_obj = obj;
    }

    Dimension E4()
    {
        return _fld0113;
    }

    Rectangle E5()
    {
        return _fld0114;
    }

    public static final int UPDATE_ALL = 1;
    public static final int INSERTED = 2;
    public static final int CHANGED = 3;
    public static final int REMOVED = 4;
    public static final int SIZE_CHANGED = 5;
    public static final int PAPER_COLOR_CHANGED = 6;
    public static final int ALL_REMOVED = 7;
    public static final int MODIFIABLE_CHANGED = 8;
    public static final int LAYER_INSERTED = 10;
    public static final int LAYER_REMOVED = 11;
    public static final int LAYER_MOVED = 12;
    public static final int LAYER_CHANGED = 13;
    public static final int LAST = 65535;
    private int m_hint;
    private int m_flags;
    private Object m_obj;
    private transient Point _fld0112;
    private transient Dimension _fld0113;
    private transient Rectangle _fld0114;
}
