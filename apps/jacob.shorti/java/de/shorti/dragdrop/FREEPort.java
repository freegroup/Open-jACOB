//
//
//
// Source File Name:   com/nwoods//FREEPort

package de.shorti.dragdrop;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

// Referenced classes of package de.shorti.dragdrop:
//            FREEDrawable, FREEObjList, FREELink, FREEObject,
//            FREEDocument, FREEView, FREEBrush, FREECopyEnvironment,
//            FREEListPosition

public class FREEPort extends FREEDrawable
{

    public FREEPort()
    {
        m_style = 2;
        m_fromLinkSpot = 4;
        m_toLinkSpot = 8;
        m_links = new FREEObjList();
        m_object = null;
        C0(2);
    }

    public FREEPort(Rectangle rectangle)
    {
        super(rectangle);
        m_style = 2;
        m_fromLinkSpot = 4;
        m_toLinkSpot = 8;
        m_links = new FREEObjList();
        m_object = null;
        C0(2);
    }

    public FREEPort(Point point, Dimension dimension)
    {
        super(point, dimension);
        m_style = 2;
        m_fromLinkSpot = 4;
        m_toLinkSpot = 8;
        m_links = new FREEObjList();
        m_object = null;
        C0(2);
    }

    public FREEPort(Rectangle rectangle, FREEObject object)
    {
        super(rectangle);
        m_style = 2;
        m_fromLinkSpot = 4;
        m_toLinkSpot = 8;
        m_links = new FREEObjList();
        m_object = null;
        C0(1);
        if(object.getDocument() != null)
            object.getDocument().removeObject(object.getTopLevelObject());
        else
        if(object.getView() != null)
            object.getView().removeObject(object.getTopLevelObject());
        object.setSelectable(false);
        object.setDraggable(false);
        object.setResizable(false);
        m_object = object;
    }

    private final void C0(int i)
    {
        m_style = i;
        setFlags(getFlags() & 0xfffffffb & 0xffffffef | 0x4000 | 0x2000);
        setBrush(FREEBrush.black);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEPort port = (FREEPort)super.copyObject(copyEnv);
        if(port != null)
        {
            port.setFlags(port.getFlags() & 0xffff7fff);
            port.m_style = m_style;
            port.m_fromLinkSpot = m_fromLinkSpot;
            port.m_toLinkSpot = m_toLinkSpot;
            port.m_object = m_object;
        }
        return port;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        Rectangle rectangle = getBoundingRect();
        switch(getStyle())
        {
        case 0: // '\0'
            return;

        case 1: // '\001'
            FREEObject object = getPortObject();
            if(object != null)
            {
                object.setBoundingRect(rectangle);
                object.paint(graphics2d);
            }
            break;

        case 3: // '\003'
            int ai[] = view.E2();
            int ai2[] = view.E3();
            switch(getToSpot())
            {
            case 1: // '\001'
                ai[0] = rectangle.x + rectangle.width / 2;
                ai2[0] = rectangle.y;
                ai[1] = rectangle.x + rectangle.width;
                ai2[1] = rectangle.y + rectangle.height;
                ai[2] = rectangle.x;
                ai2[2] = rectangle.y + rectangle.height / 2;
                break;

            case 2: // '\002'
                ai[0] = rectangle.x + rectangle.width;
                ai2[0] = rectangle.y;
                ai[1] = rectangle.x + rectangle.width / 2;
                ai2[1] = rectangle.y + rectangle.height;
                ai[2] = rectangle.x;
                ai2[2] = rectangle.y;
                break;

            case 3: // '\003'
                ai[0] = rectangle.x + rectangle.width;
                ai2[0] = rectangle.y + rectangle.height / 2;
                ai[1] = rectangle.x;
                ai2[1] = rectangle.y + rectangle.height;
                ai[2] = rectangle.x + rectangle.width / 2;
                ai2[2] = rectangle.y;
                break;

            case 4: // '\004'
                ai[0] = rectangle.x + rectangle.width;
                ai2[0] = rectangle.y + rectangle.height;
                ai[1] = rectangle.x;
                ai2[1] = rectangle.y + rectangle.height / 2;
                ai[2] = rectangle.x + rectangle.width;
                ai2[2] = rectangle.y;
                break;

            case 5: // '\005'
                ai[0] = rectangle.x + rectangle.width / 2;
                ai2[0] = rectangle.y + rectangle.height;
                ai[1] = rectangle.x;
                ai2[1] = rectangle.y;
                ai[2] = rectangle.x + rectangle.width;
                ai2[2] = rectangle.y + rectangle.height / 2;
                break;

            case 6: // '\006'
                ai[0] = rectangle.x;
                ai2[0] = rectangle.y + rectangle.height;
                ai[1] = rectangle.x + rectangle.width / 2;
                ai2[1] = rectangle.y;
                ai[2] = rectangle.x + rectangle.width;
                ai2[2] = rectangle.y + rectangle.height;
                break;

            case 7: // '\007'
                ai[0] = rectangle.x;
                ai2[0] = rectangle.y + rectangle.height / 2;
                ai[1] = rectangle.x + rectangle.width;
                ai2[1] = rectangle.y;
                ai[2] = rectangle.x + rectangle.width / 2;
                ai2[2] = rectangle.y + rectangle.height;
                break;

            case 0: // '\0'
            case 8: // '\b'
            default:
                ai[0] = rectangle.x;
                ai2[0] = rectangle.y;
                ai[1] = rectangle.x + rectangle.width;
                ai2[1] = rectangle.y + rectangle.height / 2;
                ai[2] = rectangle.x;
                ai2[2] = rectangle.y + rectangle.height;
                break;
            }
            drawPolygon(graphics2d, ai, ai2, 3);
            break;

        case 4: // '\004'
            drawRect(graphics2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            break;

        case 5: // '\005'
            int ai1[] = view.E2();
            int ai3[] = view.E3();
            ai1[0] = rectangle.x + rectangle.width / 2;
            ai3[0] = rectangle.y;
            ai1[1] = rectangle.x + rectangle.width;
            ai3[1] = rectangle.y + rectangle.height / 2;
            ai1[2] = ai1[0];
            ai3[2] = rectangle.y + rectangle.height;
            ai1[3] = rectangle.x;
            ai3[3] = ai3[1];
            drawPolygon(graphics2d, ai1, ai3, 4);
            break;

        case 2: // '\002'
        default:
            drawEllipse(graphics2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            break;
        }
    }

    public void expandRectByPenWidth(Rectangle rectangle)
    {
        FREEObject object = getPortObject();
        if(object != null)
            object.expandRectByPenWidth(rectangle);
        else
            super.expandRectByPenWidth(rectangle);
    }

    public int getStyle()
    {
        return m_style;
    }

    public void setStyle(int i)
    {
        if(getStyle() != i)
        {
            m_style = i;
            update(ChangedStyle);
        }
    }

    public FREEObject getPortObject()
    {
        return m_object;
    }

    public void setPortObject(FREEObject object)
    {
        if(object != null)
            if(object.getDocument() != null)
                object.getDocument().removeObject(object.getTopLevelObject());
            else
            if(object.getView() != null)
                object.getView().removeObject(object.getTopLevelObject());
        FREEObject object1 = getPortObject();
        if(object1 != object)
        {
            m_object = object;
            update(302);
        }
    }

    public int getNumLinks()
    {
        return m_links.getNumObjects();
    }

    public boolean hasNoLinks()
    {
        return m_links.isEmpty();
    }

    public FREEListPosition getFirstLinkPos()
    {
        return m_links.getFirstObjectPos();
    }

    public FREELink getLinkAtPos(FREEListPosition listposition)
    {
        return (FREELink)m_links.getObjectAtPos(listposition);
    }

    public FREEListPosition getNextLinkPos(FREEListPosition listposition)
    {
        return m_links.getNextObjectPos(listposition);
    }

    public void removeAllLinks()
    {
        for(FREEListPosition listposition = getFirstLinkPos(); listposition != null; listposition = getFirstLinkPos())
        {
            FREELink link = getLinkAtPos(listposition);
            link.unlink();
        }

    }

    public Point getLinkPoint(int i, Point point)
    {
        Rectangle rectangle = getBoundingRect();
        if(point == null)
            point = new Point();
        int j = rectangle.x;
        int k = rectangle.x + rectangle.width / 2;
        int l = rectangle.x + rectangle.width;
        int i1 = rectangle.y;
        int j1 = rectangle.y + rectangle.height / 2;
        int k1 = rectangle.y + rectangle.height;
        switch(i)
        {
        case Center: // '\0'
        default:
            point.x = k;
            point.y = j1;
            break;
        case TopLeft: // '\001'
            point.x = j;
            point.y = i1;
            break;
        case TopCenter: // '\002'
            point.x = k;
            point.y = i1;
            break;
        case TopRight: // '\003'
            point.x = l;
            point.y = i1;
            break;
        case RightCenter: // '\004'
            point.x = l;
            point.y = j1;
            break;
        case BottomRight: // '\005'
            point.x = l;
            point.y = k1;
            break;
        case BottomCenter: // '\006'
            point.x = k;
            point.y = k1;
            break;
        case BottomLeft: // '\007'
            point.x = j;
            point.y = k1;
            break;
        case LeftCenter: // '\b'
            point.x = j;
            point.y = j1;
            break;
        }
        return point;
    }

    public final Point getLinkPoint(int i)
    {
        return getLinkPoint(i, null);
    }

    public Point getLinkPointFromPoint(int i, int j, Point point)
    {
        return getLinkPoint(0, point);
    }

    public Point getFromLinkPoint(Point point)
    {
        return getLinkPoint(getFromSpot(), point);
    }

    public final Point getFromLinkPoint()
    {
        return getLinkPoint(getFromSpot(), null);
    }

    public Point getToLinkPoint(Point point)
    {
        return getLinkPoint(getToSpot(), point);
    }

    public final Point getToLinkPoint()
    {
        return getLinkPoint(getToSpot(), null);
    }

    public int getFromSpot()
    {
        return m_fromLinkSpot;
    }

    public void setFromSpot(int i)
    {
        if(i != getFromSpot())
        {
            m_fromLinkSpot = i;
            portChange();
        }
    }

    public int getToSpot()
    {
        return m_toLinkSpot;
    }

    public void setToSpot(int i)
    {
        if(i != getToSpot())
        {
            m_toLinkSpot = i;
            portChange();
        }
    }

    public double getLinkDir(int i)
    {
        switch(i)
        {
        case 0: // '\0'
        default:
            return -1D;

        case 1: // '\001'
            return 3.9269908169872414D;

        case 2: // '\002'
            return 4.7123889803846897D;

        case 3: // '\003'
            return 5.497787143782138D;

        case 4: // '\004'
            return 0.0D;

        case 5: // '\005'
            return 0.78539816339744828D;

        case 6: // '\006'
            return 1.5707963267948966D;

        case 7: // '\007'
            return 2.3561944901923448D;

        case 8: // '\b'
            return 3.1415926535897931D;
        }
    }

    public double getFromLinkDir()
    {
        return getLinkDir(getFromSpot());
    }

    public double getToLinkDir()
    {
        return getLinkDir(getToSpot());
    }

    public boolean validLink(FREEPort port)
    {
        return isVisible() && isValidSource() && port.isVisible() && port.isValidDestination();
    }

    public boolean isValidLink()
    {
        return (getFlags() & 0x8000) != 0;
    }

    public boolean isValidSource()
    {
        return (getFlags() & 0x4000) != 0;
    }

    public void setValidSource(boolean flag)
    {
        if(isValidSource() != flag)
        {
            if(flag)
                setFlags(getFlags() | 0x4000);
            else
                setFlags(getFlags() & 0xffffbfff);
            update(303);
        }
    }

    public boolean isValidDestination()
    {
        return (getFlags() & 0x2000) != 0;
    }

    public void setValidDestination(boolean flag)
    {
        if(isValidDestination() != flag)
        {
            if(flag)
                setFlags(getFlags() | 0x2000);
            else
                setFlags(getFlags() & 0xffffdfff);
            update(304);
        }
    }

    void _mth0127(boolean flag)
    {
        if(flag)
            setFlags(getFlags() | 0x8000);
        else
            setFlags(getFlags() & 0xffff7fff);
    }

    void addLink(FREELink link)
    {
        m_links.addObjectAtTail(link);
        linkChange();
    }

    void removeLink(FREELink link)
    {
        m_links.removeObject(link);
        linkChange();
    }

    protected void geometryChange(Rectangle rectangle)
    {
        super.geometryChange(rectangle);
        portChange();
    }

    public void portChange()
    {
        for(FREEListPosition listposition = getFirstLinkPos(); listposition != null;)
        {
            FREELink link = getLinkAtPos(listposition);
            listposition = getNextLinkPos(listposition);
            link.portChange();
        }

    }

    public void linkChange()
    {
    }

    public void ownerChange(Object obj, Object obj1)
    {
        super.ownerChange(obj, obj1);
        if(obj1 == null)
            removeAllLinks();
    }

    public int getEndSegmentLength()
    {
        return 10;
    }

    public static final int StyleHidden            = 0;
    public static final int StyleObject            = 1;
    public static final int StyleEllipse           = 2;
    public static final int StyleTriangle          = 3;
    public static final int StyleRectangle         = 4;
    public static final int StyleDiamond           = 5;
    public static final int ChangedStyle           = 301;
    public static final int ChangedObject          = 302;
    public static final int ChangedValidSource     = 303;
    public static final int ChangedValidDestination = 304;
    private int m_style;
    private int m_fromLinkSpot;
    private int m_toLinkSpot;
    private FREEObjList m_links;
    private FREEObject m_object;
}
