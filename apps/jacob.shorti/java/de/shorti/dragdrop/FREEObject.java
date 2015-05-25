package de.shorti.dragdrop;


import java.awt.*;
import java.io.Serializable;
import java.util.Map;

// Referenced classes of package de.shorti.dragdrop:
//            FREEArea, FREEDocument, FREEView, FREEPen,
//            FREEDrawable, FREESelection, FREELayer, FREECopyEnvironment,
//            FREEListPosition

public abstract class FREEObject
    implements Serializable
{

    public FREEObject()
    {
        m_parentArea = null;
        m_layer = null;
        m_view = null;
        m_boundingRect = new Rectangle();
        m_flags = 0;
        m_prevRect = new Rectangle();
        C0();
    }

    public FREEObject(Rectangle rectangle)
    {
        m_parentArea = null;
        m_layer = null;
        m_view = null;
        m_boundingRect = new Rectangle();
        m_flags = 0;
        m_prevRect = new Rectangle();
        C0();
        m_boundingRect.x = rectangle.x;
        m_boundingRect.y = rectangle.y;
        m_boundingRect.width = rectangle.width;
        m_boundingRect.height = rectangle.height;
    }

    public FREEObject(Point point, Dimension dimension)
    {
        m_parentArea = null;
        m_layer = null;
        m_view = null;
        m_boundingRect = new Rectangle();
        m_flags = 0;
        m_prevRect = new Rectangle();
        C0();
        m_boundingRect.x = point.x;
        m_boundingRect.y = point.y;
        m_boundingRect.width = dimension.width;
        m_boundingRect.height = dimension.height;
    }

    private final void C0()
    {
        setFlags(30);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEObject object = (FREEObject)copyEnv.get(this);
        if(object != null)
            return null;
        try
        {
            Class class1 = getClass();
            object = (FREEObject)class1.newInstance();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(object != null)
        {
            copyEnv.put(this, object);
            object.m_parentArea = null;
            object.m_layer = null;
            object.m_view = null;
            object.m_boundingRect.x = m_boundingRect.x;
            object.m_boundingRect.y = m_boundingRect.y;
            object.m_boundingRect.width = m_boundingRect.width;
            object.m_boundingRect.height = m_boundingRect.height;
            object.m_prevRect.x = m_prevRect.x;
            object.m_prevRect.y = m_prevRect.y;
            object.m_prevRect.width = m_prevRect.width;
            object.m_prevRect.height = m_prevRect.height;
            object.m_flags = m_flags;
        }
        return object;
    }

    public Rectangle getBoundingRect()
    {
        return m_boundingRect;
    }

    public void setBoundingRect(int i, int j, int k, int l)
    {
        if(!setBoundingRectForce(i, j, k, l))
            return;
        geometryChange(m_prevRect);
        FREEArea area = getParent();
        if(area != null)
            area.geometryChangeChild(this, m_prevRect);
        update(1);
        C6(false);
    }

    public final void setBoundingRect(Point point, Dimension dimension)
    {
        setBoundingRect(point.x, point.y, dimension.width, dimension.height);
    }

    public final void setBoundingRect(Rectangle rectangle)
    {
        setBoundingRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    protected final boolean setBoundingRectForce(int i, int j, int k, int l)
    {
        if(m_boundingRect.x == i && m_boundingRect.y == j && m_boundingRect.width == k && m_boundingRect.height == l)
            return false;
        m_prevRect.x = m_boundingRect.x;
        m_prevRect.y = m_boundingRect.y;
        m_prevRect.width = m_boundingRect.width;
        m_prevRect.height = m_boundingRect.height;
        C6(true);
        m_boundingRect.x = i;
        m_boundingRect.y = j;
        m_boundingRect.width = Math.max(k, 0);
        m_boundingRect.height = Math.max(l, 0);
        FREEDocument document = getDocument();
        if(document != null)
            document.updateDocumentSize(this);
        return true;
    }

    public final boolean setBoundingRectForce(Rectangle rectangle)
    {
        return setBoundingRectForce(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public final Dimension getSize()
    {
        return getSize(null);
    }

    public final Dimension getSize(Dimension dimension)
    {
        if(dimension == null)
            dimension = new Dimension();
        dimension.width = getWidth();
        dimension.height = getHeight();
        return dimension;
    }

    public final void setSize(Dimension dimension)
    {
        setBoundingRect(getLeft(), getTop(), dimension.width, dimension.height);
    }

    public final void setSize(int i, int j)
    {
        setBoundingRect(getLeft(), getTop(), i, j);
    }

    public final int getWidth()
    {
        return getBoundingRect().width;
    }

    public final void setWidth(int i)
    {
        setBoundingRect(getLeft(), getTop(), i, getHeight());
    }

    public final int getHeight()
    {
        return getBoundingRect().height;
    }

    public final void setHeight(int i)
    {
        setBoundingRect(getLeft(), getTop(), getWidth(), i);
    }

    public final int getLeft()
    {
        return getBoundingRect().x;
    }

    public final void setLeft(int i)
    {
        setBoundingRect(i, getTop(), getWidth(), getHeight());
    }

    public final int getTop()
    {
        return getBoundingRect().y;
    }

    public final void setTop(int i)
    {
        setBoundingRect(getLeft(), i, getWidth(), getHeight());
    }

    public final Point getTopLeft()
    {
        return getTopLeft(null);
    }

    public final Point getTopLeft(Point point)
    {
        if(point == null)
            point = new Point();
        point.x = getLeft();
        point.y = getTop();
        return point;
    }

    public final void setTopLeft(Point point)
    {
        setBoundingRect(point.x, point.y, getWidth(), getHeight());
    }

    public final void setTopLeft(int i, int j)
    {
        setBoundingRect(i, j, getWidth(), getHeight());
    }

    public Point getLocation(Point point)
    {
        return getTopLeft(point);
    }

    public final Point getLocation()
    {
        return getLocation(null);
    }

    public void setLocation(int i, int j)
    {
        setTopLeft(i, j);
    }

    public final void setLocation(Point point)
    {
        setLocation(point.x, point.y);
    }

    public final void setLocationOffset(int i, int j, int k, int l)
    {
        setLocation(i + k, j + l);
    }

    public final void setLocationOffset(Point point, Point point1)
    {
        setLocation(point.x + point1.x, point.y + point1.y);
    }

    public static int spotOpposite(int i)
    {
        switch(i)
        {
        default:
            return i;

        case 0: // '\0'
            return 0;

        case 1: // '\001'
            return 5;

        case 2: // '\002'
            return 6;

        case 3: // '\003'
            return 7;

        case 4: // '\004'
            return 8;

        case 5: // '\005'
            return 1;

        case 6: // '\006'
            return 2;

        case 7: // '\007'
            return 3;

        case 8: // '\b'
            return 4;
        }
    }

    public Point getSpotLocation(int i, Point point)
    {
        Rectangle rectangle = getBoundingRect();
        if(point == null)
        {
            point = new Point(rectangle.x, rectangle.y);
        } else
        {
            point.x = rectangle.x;
            point.y = rectangle.y;
        }
        switch(i)
        {
        case 0: // '\0'
            point.x += rectangle.width / 2;
            point.y += rectangle.height / 2;
            break;

        case 2: // '\002'
            point.x += rectangle.width / 2;
            break;

        case 3: // '\003'
            point.x += rectangle.width;
            break;

        case 4: // '\004'
            point.x += rectangle.width;
            point.y += rectangle.height / 2;
            break;

        case 5: // '\005'
            point.x += rectangle.width;
            point.y += rectangle.height;
            break;

        case 6: // '\006'
            point.x += rectangle.width / 2;
            point.y += rectangle.height;
            break;

        case 7: // '\007'
            point.y += rectangle.height;
            break;

        case 8: // '\b'
            point.y += rectangle.height / 2;
            break;
        }
        return point;
    }

    public final Point getSpotLocation(int i)
    {
        return getSpotLocation(i, null);
    }

    public void setSpotLocation(int i, int j, int k)
    {
        Point point = getSpotLocation(i);
        int l = j - (point.x - getLeft());
        int i1 = k - (point.y - getTop());
        setBoundingRect(l, i1, getWidth(), getHeight());
    }

    public final void setSpotLocation(int i, Point point)
    {
        setSpotLocation(i, point.x, point.y);
    }

    public final void setSpotLocation(int i, FREEObject object, int j)
    {
        Point point = object.getSpotLocation(j);
        setSpotLocation(i, point.x, point.y);
    }

    public final void setSpotLocationOffset(int i, int j, int k, int l, int i1)
    {
        setSpotLocation(i, j + l, k + i1);
    }

    public final void update()
    {
        update(0);
    }

    public void update(int i)
    {
        if(isSuspendUpdates())
            return;
        FREEDocument document = getDocument();
        if(document != null)
        {
            document.fireUpdate(3, i, this);
        } else
        {
            FREEView view = getView();
            if(view != null)
                view.fireUpdate(3, i, this);
        }
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        Rectangle rectangle = getBoundingRect();
        FREEDrawable.drawRect(graphics2d, FREEPen.black, null, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        FREEDrawable.drawLine(graphics2d, FREEPen.black, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
        FREEDrawable.drawLine(graphics2d, FREEPen.black, rectangle.x, rectangle.y + rectangle.height, rectangle.x + rectangle.width, rectangle.y);
    }

    public final void paint(Graphics2D graphics2d)
    {
        paint(graphics2d, null);
    }

    Rectangle C1()
    {
        return m_prevRect;
    }

    public boolean isPointInObj(Point point)
    {
        return getBoundingRect().contains(point.x, point.y);
    }

    public void expandRectByPenWidth(Rectangle rectangle)
    {
    }

    public boolean doMouseClick(int i, Point point, Point point1, FREEView view)
    {
        return false;
    }

    public boolean doMouseDblClick(int i, Point point, Point point1, FREEView view)
    {
        return false;
    }

    public boolean doUncapturedMouseMove(int i, Point point, Point point1, FREEView view)
    {
        return false;
    }

    public String getToolTipText()
    {
        return null;
    }

    protected void ownerChange(Object obj, Object obj1)
    {
    }

    protected void gainedSelection(FREESelection selection)
    {
        if(!isResizable())
        {
            selection.createBoundingHandle(this);
            return;
        }
        Rectangle rectangle = getBoundingRect();
        int i = rectangle.x;
        int j = rectangle.x + rectangle.width / 2;
        int k = rectangle.x + rectangle.width;
        int l = rectangle.y;
        int i1 = rectangle.y + rectangle.height / 2;
        int j1 = rectangle.y + rectangle.height;
        selection.createResizeHandle(this, i, l, 1, true);
        selection.createResizeHandle(this, k, l, 3, true);
        selection.createResizeHandle(this, i, j1, 7, true);
        selection.createResizeHandle(this, k, j1, 5, true);
        if(!is4ResizeHandles())
        {
            selection.createResizeHandle(this, j, l, 2, true);
            selection.createResizeHandle(this, k, i1, 4, true);
            selection.createResizeHandle(this, j, j1, 6, true);
            selection.createResizeHandle(this, i, i1, 8, true);
        }
    }

    protected void lostSelection(FREESelection selection)
    {
        selection.deleteHandles(this);
    }

    protected FREEObject redirectSelection()
    {
        return this;
    }

    protected Rectangle handleResize(Graphics2D graphics2d, FREEView view, Rectangle rectangle, Point point, int i, int j, int k,
            int l)
    {
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        switch(i)
        {
        case 1: // '\001'
            rectangle1.x = Math.min(point.x, (rectangle.x + rectangle.width) - k);
            rectangle1.y = Math.min(point.y, (rectangle.y + rectangle.height) - l);
            rectangle1.width += rectangle.x - rectangle1.x;
            rectangle1.height += rectangle.y - rectangle1.y;
            break;

        case 2: // '\002'
            rectangle1.y = Math.min(point.y, (rectangle.y + rectangle.height) - l);
            rectangle1.height += rectangle.y - rectangle1.y;
            break;

        case 8: // '\b'
            rectangle1.x = Math.min(point.x, (rectangle.x + rectangle.width) - k);
            rectangle1.width += rectangle.x - rectangle1.x;
            break;

        case 3: // '\003'
            rectangle1.y = Math.min(point.y, (rectangle.y + rectangle.height) - l);
            rectangle1.width = Math.max(point.x - rectangle.x, k);
            rectangle1.height += rectangle.y - rectangle1.y;
            break;

        case 7: // '\007'
            rectangle1.x = Math.min(point.x, (rectangle.x + rectangle.width) - k);
            rectangle1.width += rectangle.x - rectangle1.x;
            rectangle1.height = Math.max(point.y - rectangle.y, l);
            break;

        case 4: // '\004'
            rectangle1.width = Math.max(point.x - rectangle.x, k);
            break;

        case 6: // '\006'
            rectangle1.height = Math.max(point.y - rectangle.y, l);
            break;

        case 5: // '\005'
            rectangle1.width = Math.max(point.x - rectangle.x, k);
            rectangle1.height = Math.max(point.y - rectangle.y, l);
            break;
        }
        if(j == 3)
            setBoundingRect(rectangle1);
        return rectangle1;
    }

    protected void geometryChange(Rectangle rectangle)
    {
    }

    protected boolean geometryChangeChild(FREEObject object, Rectangle rectangle)
    {
        return false;
    }

    public final void geometryChangeChild(FREEObject object)
    {
        geometryChangeChild(object, object.C1());
    }

    public FREEArea getParent()
    {
        return m_parentArea;
    }

    protected void setParent(FREEArea area)
    {
        m_parentArea = area;
    }

    public final boolean isTopLevel()
    {
        return getParent() == null;
    }

    public FREEObject getTopLevelObject()
    {
        Object obj;
        for(obj = this; !((FREEObject) (obj)).isTopLevel(); obj = ((FREEObject) (obj)).getParent());
        return ((FREEObject) (obj));
    }

    public FREEDocument getDocument()
    {
        if(m_layer == null)
            return null;
        else
            return m_layer.getDocument();
    }

    public FREELayer getLayer()
    {
        return m_layer;
    }

    void C2(FREELayer layer)
    {
        FREEDocument document = getDocument();
        m_layer = layer;
        FREEDocument document1 = getDocument();
        if(document != document1)
        {
            ownerChange(document, document1);
            if(document1 == null)
                document.fireUpdate(4, 0, this);
            else
                document1.fireUpdate(2, 0, this);
        }
    }

    public FREEView getView()
    {
        return m_view;
    }

    void C3(FREEView view)
    {
        FREEView view1 = getView();
        m_view = view;
        if(view1 != m_view)
        {
            ownerChange(view1, m_view);
            if(view == null)
                view1.fireUpdate(4, 0, this);
            else
                view.fireUpdate(2, 0, this);
        }
    }

    public void showSelectionHandles(FREESelection selection)
    {
        lostSelection(selection);
        gainedSelection(selection);
    }

    public void hideSelectionHandles(FREESelection selection)
    {
        lostSelection(selection);
    }

    public FREEObject getPartner()
    {
        return null;
    }

    public void setPartner(FREEObject object)
    {
    }

    void C4(FREEListPosition listposition)
    {
    }

    FREEListPosition C5()
    {
        return null;
    }

    public final void setFlags(int i)
    {
        m_flags = i;
    }

    public final int getFlags()
    {
        return m_flags;
    }

    public void setVisible(boolean flag)
    {
        if(isVisible() != flag)
        {
            if(flag)
                m_flags |= 0x2;
            else
                m_flags &= 0xfffffffd;
            update(2);
        }
    }

    public boolean isVisible()
    {
        return (m_flags & 0x2) != 0;
    }

    public void setSelectable(boolean flag)
    {
        if(isSelectable() != flag)
        {
            if(flag)
                m_flags |= 0x4;
            else
                m_flags &= 0xfffffffb;
            update(3);
        }
    }

    public boolean isSelectable()
    {
        return (m_flags & 0x4) != 0;
    }

    public void setDraggable(boolean flag)
    {
        if(isDraggable() != flag)
        {
            if(flag)
                m_flags |= 0x8;
            else
                m_flags &= 0xfffffff7;
            update(4);
        }
    }

    public boolean isDraggable()
    {
        return (m_flags & 0x8) != 0;
    }

    public void setResizable(boolean flag)
    {
        if(isResizable() != flag)
        {
            if(flag)
                m_flags |= 0x10;
            else
                m_flags &= 0xffffffef;
            update(5);
        }
    }

    public boolean isResizable()
    {
        return (m_flags & FLAG_IS_RESIZEABLE) != 0;
    }

    public void set4ResizeHandles(boolean flag)
    {
        if(is4ResizeHandles() != flag)
        {
            if(flag)
                m_flags |= 0x20;
            else
                m_flags &= 0xffffffdf;
            update(6);
        }
    }

    public boolean is4ResizeHandles()
    {
        return (m_flags & 0x20) != 0;
    }

    public void setSuspendUpdates(boolean flag)
    {
        if(isSuspendUpdates() != flag)
        {
            if(flag)
                m_flags |= 0x40;
            else
                m_flags &= 0xffffffbf;
            update(7);
        }
    }

    public boolean isSuspendUpdates()
    {
        return (m_flags & 0x40) != 0;
    }

    public void setSuspendChildUpdates(boolean flag)
    {
        if(isSuspendChildUpdates() != flag)
            if(flag)
                m_flags |= 0x80;
            else
                m_flags &= 0xffffff7f;
    }

    public boolean isSuspendChildUpdates()
    {
        return (m_flags & 0x80) != 0;
    }

    public void setGrabChildSelection(boolean flag)
    {
        if(isGrabChildSelection() != flag)
        {
            if(flag)
                m_flags |= 0x200;
            else
                m_flags &= 0xfffffdff;
            update(9);
        }
    }

    public boolean isGrabChildSelection()
    {
        return (m_flags & 0x200) != 0;
    }

    void C6(boolean flag)
    {
        if(flag)
            m_flags |= 0x1;
        else
            m_flags &= 0xfffffffe;
    }

    boolean C7()
    {
        return (m_flags & 0x1) != 0;
    }

    void C8(boolean flag)
    {
        if(flag)
            m_flags |= 0x100;
        else
            m_flags &= 0xfffffeff;
    }

    boolean C9()
    {
        return (m_flags & 0x100) != 0;
    }

    public static final int NoSpot       = -1;
    public static final int NoHandle     = -1;
    public static final int Center       = 0;
    public static final int TopLeft      = 1;
    public static final int TopCenter    = 2;
    public static final int TopRight     = 3;
    public static final int RightCenter  = 4;
    public static final int BottomRight  = 5;
    public static final int BottomCenter = 6;
    public static final int BottomLeft   = 7;
    public static final int LeftCenter   = 8;
    public static final int NumReservedHandles = 100;
    public static final int ChangedAll = 0;
    public static final int ChangedGeometry = 1;
    public static final int ChangedVisible = 2;
    public static final int ChangedSelectable = 3;
    public static final int ChangedDraggable = 4;
    public static final int ChangedResizable = 5;
    public static final int Changed4ResizeHandles = 6;
    public static final int ChangedSuspendUpdates = 7;
    public static final int ChangedGrabChildSelection = 9;
    public static final int ChangedZOrder = 10;
    public static final int ChangedPen = 11;
    public static final int ChangedBrush = 12;
    public static final int LastChangedHint = 65535;
    static final int C0 = 1;
    static final int C1 = 2;
    static final int C2 = 4;
    static final int C3 = 8;
    static final int FLAG_IS_RESIZEABLE = 16;
    static final int C5 = 32;
    static final int C6 = 64;
    static final int C7 = 128;
    static final int C8 = 256;
    static final int C9 = 512;
    static final int CA = 1024;
    static final int CB = 2048;
    static final int FLAG_USE_BEZIERLINE = 4096; // bezier ?!
    static final int CD = 8192;
    static final int CE = 16384;
    static final int CF = 32768;
    private FREEArea m_parentArea;
    private FREELayer m_layer;
    private transient FREEView m_view;
    private Rectangle m_boundingRect;
    private int m_flags;
    private Rectangle m_prevRect;
}
