package de.shorti.dragdrop;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

// Referenced classes of package de.shorti.dragdrop:
//            FREERectangle, FREEBrush, FREEDrawable, FREEPen,
//            FREEObject, FREECopyEnvironment, FREEView

public class FREE3DRect extends FREERectangle
{

    public FREE3DRect()
    {
        m_state = 0;
        C0();
    }

    public FREE3DRect(Rectangle rectangle)
    {
        super(rectangle);
        m_state = 0;
        C0();
    }

    public FREE3DRect(Point point, Dimension dimension)
    {
        super(point, dimension);
        m_state = 0;
        C0();
    }

    private final void C0()
    {
        setBrush(FREEBrush.lightGray);
        setPen(FREEPen.lightGray);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREE3DRect rect = (FREE3DRect)super.copyObject(copyEnv);
        if(rect != null)
            rect.m_state = m_state;
        return rect;
    }

    public void setState(int i)
    {
        if(getState() != i)
        {
            m_state = i;
            update(403);
        }
    }

    public int getState()
    {
        return m_state;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        boolean flag = true;
        Rectangle rectangle = getBoundingRect();
        switch(m_state)
        {
        case 0: // '\0'
            flag = true;
            break;

        case 1: // '\001'
        case 2: // '\002'
            flag = false;
            break;
        }
        draw3DRect(graphics2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height, flag);
    }

    public static final int StateUp = 0;
    public static final int StateDown = 1;
    public static final int StateToggled = 2;
    public static final int ChangedState = 403;
    private int m_state;
}
