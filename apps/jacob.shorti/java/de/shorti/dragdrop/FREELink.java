package de.shorti.dragdrop;



import java.awt.*;

public class FREELink extends FREEStroke
{
    public FREELink()
    {
        m_fromPort = null;
        m_toPort = null;
        setFlags(getFlags() & 0xfffffff7);
    }

    public FREELink(FREEPort port, FREEPort port1)
    {
        m_fromPort = null;
        m_toPort = null;
        setFlags(getFlags() & 0xfffffff7);
        m_fromPort = port;
        m_toPort = port1;
        if(m_fromPort != null)
            m_fromPort.addLink(this);
        if(m_toPort != null)
            m_toPort.addLink(this);
        calculateStroke();
    }


    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREELink link = (FREELink)super.copyObject(copyEnv);
        if(link != null)
        {
            copyEnv.delay(this);
            link.m_fromPort = null;
            link.m_toPort = null;
        }
        return link;
    }

    public void unlink()
    {
        if(getFromPort() != null)
            getFromPort().removeLink(this);
        if(getToPort() != null)
            getToPort().removeLink(this);
        FREEDocument document = getDocument();
        if(document != null)
            document.removeObject(this);
        FREEView view = getView();
        if(view != null)
            view.removeObject(this);
    }

    protected void gainedSelection(FREESelection selection)
    {
        if(!isResizable())
        {
            super.gainedSelection(selection);
        } else
        {
            int i = getLastPickPoint();
            for(int j = getFirstPickPoint(); j <= i; j++)
            {
                Point point = getPoint(j);
                if(point != null)
                {
                    int k = 100 + j;
                    if((j == 2 || j == 3) && getNumPoints() >= 5 && isOrthogonal() && getPoint(2) != null && getPoint(3) != null)
                        if(getPoint(2).y == getPoint(3).y)
                            k = 2;
                        else
                            k = 4;
                    selection.createResizeHandle(this, point.x, point.y, k, true);
                }
            }

        }
    }

    public Rectangle handleResize(Graphics2D graphics2d, FREEView view, Rectangle rectangle, Point point, int i, int j, int k,
            int l)
    {
        FREEPort port = null;
        if(i == 100 + getFirstPickPoint())
            port = getToPort();
        else
        if(i == 100 + getLastPickPoint())
            port = getFromPort();
        if(port != null)
        {
            unlink();
            view.startNewLink(port, point);
        } else
        if((i == 2 || i == 4) && isOrthogonal() && getPoint(1) != null && getPoint(2) != null && getPoint(3) != null && getPoint(4) != null)
        {
            if(getPoint(2).y == getPoint(3).y)
            {
                setPoint(2, getPoint(1).x, point.y);
                setPoint(3, getPoint(4).x, point.y);
            } else
            {
                setPoint(2, point.x, getPoint(1).y);
                setPoint(3, point.x, getPoint(4).y);
            }
        } else
        {
            setPoint(i - 100, point);
        }
        return null;
    }

    protected void ownerChange(Object obj, Object obj1)
    {
        super.ownerChange(obj, obj1);
        if(obj != null && obj1 == null)
            unlink();
    }

    public void portChange()
    {
        calculateStroke();
    }

    protected int getFirstPickPoint()
    {
        return getNumPoints() < 4 ? 0 : 1;
    }

    protected int getLastPickPoint()
    {
        if(getNumPoints() >= 4)
            return getNumPoints() - 2;
        else
            return getNumPoints() - 1;
    }

    public FREEPort getFromPort()
    {
        return m_fromPort;
    }

    public FREEPort getToPort()
    {
        return m_toPort;
    }

    public void setFromPort(FREEPort port)
    {
        FREEPort port1 = getFromPort();
        if(port1 != port)
        {
            if(port1 != null)
                port1.removeLink(this);
            m_fromPort = port;
            if(m_fromPort != null)
                m_fromPort.addLink(this);
            calculateStroke();
            update(201);
        }
    }

    public void setToPort(FREEPort port)
    {
        FREEPort port1 = getToPort();
        if(port1 != port)
        {
            if(port1 != null)
                port1.removeLink(this);
            m_toPort = port;
            if(m_toPort != null)
                m_toPort.addLink(this);
            calculateStroke();
            update(202);
        }
    }

    public FREEPort getOtherPort(FREEPort port)
    {
        if(getFromPort() == port)
            return getToPort();
        if(getToPort() == port)
            return getFromPort();
        else
            return null;
    }

    protected void calculateStroke()
    {
        FREEPort from = getFromPort();
        FREEPort to = getToPort();
        if(from == null)
            return;
        if(to == null)
            return;
        try
        {
            setSuspendUpdates(true);
            removeAllPoints();
            if(from.getFromSpot() == -1 && to.getToSpot() == -1)
            {
                Point point = new Point();
                point = from.getLinkPointFromPoint(to.getLeft() + to.getWidth() / 2, to.getTop() + to.getHeight() / 2, point);
                addPoint(point);
                point = to.getLinkPointFromPoint(from.getLeft() + from.getWidth() / 2, to.getTop() + from.getHeight() / 2, point);
                addPoint(point);
            } else
            {
                int i = 0;
                int j = 0;
                double d = -1D;
                if(from.getFromSpot() != NoSpot)
                {
                    int k = from.getEndSegmentLength();
                    d = from.getFromLinkDir();
                    i = (int)((double)k * Math.cos(d));
                    j = (int)((double)k * Math.sin(d));
                }
                int l = 0;
                int i1 = 0;
                double d1 = -1D;
                if(to.getToSpot() != NoSpot)
                {
                    int j1 = to.getEndSegmentLength();
                    d1 = to.getToLinkDir();
                    l = (int)((double)j1 * Math.cos(d1));
                    i1 = (int)((double)j1 * Math.sin(d1));
                }
                Point point1 = new Point();
                Point point2 = new Point();
                if(from.getFromSpot() != NoSpot)
                    point1 = from.getFromLinkPoint(point1);
                if(to.getToSpot() != NoSpot)
                    point2 = to.getToLinkPoint(point2);
                if(from.getFromSpot() == NoSpot)
                    point1 = from.getLinkPointFromPoint(point2.x + l, point2.y + i1, point1);
                if(to.getToSpot() == NoSpot)
                    point2 = to.getLinkPointFromPoint(point1.x + i, point1.y + j, point2);
                addPoint(point1);
                addPoint(point1.x + i, point1.y + j);
                if(isOrthogonal())
                    addOrthoPoints(point1.x + i, point1.y + j, d, point2.x + l, point2.y + i1, d1);
                addPoint(point2.x + l, point2.y + i1);
                addPoint(point2);
            }
        }
        finally
        {
            setSuspendUpdates(false);
        }
    }

    protected void addOrthoPoints(int i, int j, double d, int k, int l, double d1)
    {
        if(d != 0.0D && d != 1.5707963267948966D && d != 3.1415926535897931D && d != 4.7123889803846897D)
            return;
        if(d1 != 0.0D && d1 != 1.5707963267948966D && d1 != 3.1415926535897931D && d1 != 4.7123889803846897D)
            return;
        int i1 = i;
        int j1 = j;
        int k1 = k;
        int l1 = l;
        if(d == 0.0D)
        {
            if(k >= i)
            {
                if(d1 == 0.0D || d1 == 1.5707963267948966D && l <= j || d1 == 4.7123889803846897D && l >= j)
                {
                    if(Math.abs(k - i) > Math.abs(l - j))
                    {
                        i1 = getMidOrthoPosition(i, k);
                        j1 = j;
                        k1 = k;
                        l1 = j;
                    } else
                    {
                        i1 = k;
                        j1 = j;
                        k1 = k;
                        l1 = getMidOrthoPosition(j, l);
                    }
                } else
                {
                    i1 = getMidOrthoPosition(i, k);
                    j1 = j;
                    k1 = i1;
                    l1 = l;
                }
            } else
            if(d1 == 0.0D || d1 == 1.5707963267948966D && l >= j || d1 == 4.7123889803846897D && l <= j)
            {
                if(Math.abs(k - i) > Math.abs(l - j))
                {
                    i1 = i;
                    j1 = l;
                    k1 = getMidOrthoPosition(i, k);
                    l1 = l;
                } else
                {
                    i1 = i;
                    j1 = getMidOrthoPosition(j, l);
                    k1 = i;
                    l1 = l;
                }
            } else
            {
                i1 = i;
                j1 = getMidOrthoPosition(j, l);
                k1 = k;
                l1 = j1;
            }
        } else
        if(d == 3.1415926535897931D)
        {
            if(k < i)
            {
                if(d1 == 3.1415926535897931D || d1 == 1.5707963267948966D && l <= j || d1 == 4.7123889803846897D && l >= j)
                {
                    if(Math.abs(k - i) > Math.abs(l - j))
                    {
                        i1 = getMidOrthoPosition(i, k);
                        j1 = j;
                        k1 = k;
                        l1 = j;
                    } else
                    {
                        i1 = k;
                        j1 = j;
                        k1 = k;
                        l1 = getMidOrthoPosition(j, l);
                    }
                } else
                {
                    i1 = getMidOrthoPosition(i, k);
                    j1 = j;
                    k1 = i1;
                    l1 = l;
                }
            } else
            if(d1 == 3.1415926535897931D || d1 == 1.5707963267948966D && l >= j || d1 == 4.7123889803846897D && l <= j)
            {
                if(Math.abs(k - i) > Math.abs(l - j))
                {
                    i1 = i;
                    j1 = l;
                    k1 = getMidOrthoPosition(i, k);
                    l1 = l;
                } else
                {
                    i1 = i;
                    j1 = getMidOrthoPosition(j, l);
                    k1 = i;
                    l1 = l;
                }
            } else
            {
                i1 = i;
                j1 = getMidOrthoPosition(j, l);
                k1 = k;
                l1 = j1;
            }
        } else
        if(d == 1.5707963267948966D)
        {
            if(l >= j)
            {
                if(d1 == 1.5707963267948966D || d1 == 3.1415926535897931D && k >= i || d1 == 0.0D && k <= i)
                {
                    if(Math.abs(l - j) > Math.abs(k - i))
                    {
                        j1 = getMidOrthoPosition(j, l);
                        i1 = i;
                        l1 = l;
                        k1 = i;
                    } else
                    {
                        j1 = l;
                        i1 = i;
                        l1 = l;
                        k1 = getMidOrthoPosition(i, k);
                    }
                } else
                {
                    j1 = getMidOrthoPosition(j, l);
                    i1 = i;
                    l1 = j1;
                    k1 = k;
                }
            } else
            if(d1 == 1.5707963267948966D || d1 == 3.1415926535897931D && k <= i || d1 == 0.0D && k >= i)
            {
                if(Math.abs(l - j) > Math.abs(k - i))
                {
                    j1 = j;
                    i1 = k;
                    l1 = getMidOrthoPosition(j, l);
                    k1 = k;
                } else
                {
                    j1 = j;
                    i1 = getMidOrthoPosition(i, k);
                    l1 = j;
                    k1 = k;
                }
            } else
            {
                j1 = j;
                i1 = getMidOrthoPosition(i, k);
                l1 = l;
                k1 = i1;
            }
        } else
        if(d == 4.7123889803846897D)
            if(l < j)
            {
                if(d1 == 4.7123889803846897D || d1 == 3.1415926535897931D && k >= i || d1 == 0.0D && k <= i)
                {
                    if(Math.abs(l - j) > Math.abs(k - i))
                    {
                        j1 = getMidOrthoPosition(j, l);
                        i1 = i;
                        l1 = l;
                        k1 = i;
                    } else
                    {
                        j1 = l;
                        i1 = i;
                        l1 = l;
                        k1 = getMidOrthoPosition(i, k);
                    }
                } else
                {
                    j1 = getMidOrthoPosition(j, l);
                    i1 = i;
                    l1 = j1;
                    k1 = k;
                }
            } else
            if(d1 == 4.7123889803846897D || d1 == 3.1415926535897931D && k <= i || d1 == 0.0D && k >= i)
            {
                if(Math.abs(l - j) > Math.abs(k - i))
                {
                    j1 = j;
                    i1 = k;
                    l1 = getMidOrthoPosition(j, l);
                    k1 = k;
                } else
                {
                    j1 = j;
                    i1 = getMidOrthoPosition(i, k);
                    l1 = j;
                    k1 = k;
                }
            } else
            {
                j1 = j;
                i1 = getMidOrthoPosition(i, k);
                l1 = l;
                k1 = i1;
            }
        addPoint(i1, j1);
        addPoint(k1, l1);
    }

    protected int getMidOrthoPosition(int i, int j)
    {
        return (i + j) / 2;
    }

    public boolean isOrthogonal()
    {
        return (getFlags() & 0x2000) != 0;
    }

    public void setOrthogonal(boolean flag)
    {
        if(isOrthogonal() != flag)
        {
            int i = getFlags();
            if(flag)
                i |= 0x2000;
            else
                i &= 0xffffdfff;
            setFlags(i);
            calculateStroke();
            update(ChangedOrthogonal);
        }
    }

    public static final int ChangedFromPort = 201;
    public static final int ChangedToPort = 202;
    public static final int ChangedOrthogonal = 203;
    private FREEPort m_fromPort;
    private FREEPort m_toPort;
}
