//
//
//
//

package de.shorti.dragdrop;


import java.awt.Point;
import java.util.EventObject;

public class FREEViewEvent extends EventObject
{
    public static final int UPDATE_ALL = 1;
    public static final int INSERTED   = 2;
    public static final int CHANGED    = 3;
    public static final int REMOVED    = 4;
    public static final int SELECTION_GAINED = 5;
    public static final int SELECTION_LOST = 6;
    public static final int POSITION_CHANGED = 7;
    public static final int SCALE_CHANGED = 8;
    public static final int SELECTION_COLOR_CHANGED = 9;
    public static final int CLICKED = 10;
    public static final int DOUBLE_CLICKED = 11;
    public static final int GRID_CHANGED = 100;
    public static final int LAST = 65535;
    private int m_hint;
    private int m_flags;
    private Object m_obj;
    private Point _fld012F;
    private Point _fld0130;
    private int _fld0131;

    public FREEViewEvent(FREEView view, int i, int j, Object obj, Point point, Point point1, int k)
    {
        super(view);
        m_obj = obj;
        m_hint = i;
        m_flags = j;
        _fld012F = point;
        _fld0130 = point1;
        _fld0131 = k;
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

    public Point getPointViewCoords()
    {
        return _fld012F;
    }

    void _mth012F(Point point)
    {
        _fld012F = point;
    }

    public Point getPointDocCoords()
    {
        return _fld0130;
    }

    void _mth0130(Point point)
    {
        _fld0130 = point;
    }

    public int getModifiers()
    {
        return _fld0131;
    }

    void _mth0131(int i)
    {
        _fld0131 = i;
    }
}
