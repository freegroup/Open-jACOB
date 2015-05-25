package de.shorti.dragdrop;


import java.awt.*;

// Referenced classes of package de.shorti.dragdrop:
//            FREERectangle, FREEObject, FREEBrush, FREEDrawable,
//            FREELayer, FREEView, FREECopyEnvironment, FREESelection

public class FREEHandle extends FREERectangle
{

    public FREEHandle()
    {
        m_cursorType = 0;
        m_handleType = 0;
        m_handleFor = null;
        C0(0, 0);
    }

    public FREEHandle(Rectangle rectangle, int i)
    {
        super(rectangle);
        m_cursorType = 0;
        m_handleType = 0;
        m_handleFor = null;
        C0(i, 0);
    }

    public FREEHandle(Rectangle rectangle, int i, int j)
    {
        super(rectangle);
        m_cursorType = 0;
        m_handleType = 0;
        m_handleFor = null;
        C0(i, j);
    }

    private final void C0(int i, int j)
    {
        setFlags(getFlags() & 0xffffffeb);
        m_cursorType = i;
        m_handleType = j;
        setBrush(FREEBrush.black);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        return null;
    }

    public boolean doUncapturedMouseMove(int i, Point point, Point point1, FREEView view)
    {
        FREEObject object = getHandleFor();
        if(object != null && object.getLayer() != null && !object.getLayer().isModifiable())
            return false;
        Cursor cursor = getCursor();
        if(view.getCursor() != cursor)
            view.setCursor(cursor);
        return true;
    }

    public Cursor getCursor()
    {
        return Cursor.getPredefinedCursor(getCursorType());
    }

    protected void gainedSelection(FREESelection selection)
    {
    }

    protected void lostSelection(FREESelection selection)
    {
    }

    protected FREEObject redirectSelection()
    {
        return null;
    }

    public FREEObject getPartner()
    {
        return m_handleFor;
    }

    public void setPartner(FREEObject object)
    {
        m_handleFor = object;
    }

    public final void setHandleFor(FREEObject object)
    {
        setPartner(object);
    }

    public final FREEObject getHandleFor()
    {
        return getPartner();
    }

    public void setHandleType(int i)
    {
        m_handleType = i;
        int j = getCursorType();
        int k = j;
        switch(m_handleType)
        {
        case 1: // '\001'
            k = 6;
            break;

        case 2: // '\002'
            k = 8;
            break;

        case 3: // '\003'
            k = 7;
            break;

        case 8: // '\b'
            k = 10;
            break;

        case 4: // '\004'
            k = 11;
            break;

        case 7: // '\007'
            k = 4;
            break;

        case 6: // '\006'
            k = 9;
            break;

        case 5: // '\005'
            k = 5;
            break;
        }
        if(k != j)
            setCursorType(k);
    }

    public int getHandleType()
    {
        return m_handleType;
    }

    public void setCursorType(int i)
    {
        m_cursorType = i;
    }

    public int getCursorType()
    {
        return m_cursorType;
    }

    public static int getHandleWidth()
    {
        return 5;
    }

    public static int getHandleHeight()
    {
        return 5;
    }

    private int m_cursorType;
    private int m_handleType;
    private FREEObject m_handleFor;
}
