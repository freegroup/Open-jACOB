//
//
//
// Source File Name:   com/nwoods//FREEStroke

package de.shorti.dragdrop;



import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.io.Serializable;
import java.util.Vector;

// Referenced classes of package de.shorti.dragdrop:
//            FREEDrawable, FREEBrush, FREEPen, FREEObject,
//            FREESelection, FREEArea, FREECopyEnvironment, FREEView

public class FREEStroke extends FREEDrawable
{
    private class ArrowInfo   implements Serializable
    {

        int[] getXCoords()
        {
            if(x == null)
                x = new int[4];
            return x;
        }

        int[] getYCoords()
        {
            if(y == null)
                y = new int[4];
            return y;
        }

        double m_length;
        double m_shaftLength;
        double m_angle;
        transient int x[];
        transient int y[];

        private ArrowInfo()
        {
            m_length = 10D;
            m_shaftLength = 8D;
            m_angle = 0.78539816339744828D;
            x = null;
            y = null;
        }

//        ArrowInfo(_cls1 _pcls1)
//        {
//            this();
//        }
    }


    public FREEStroke()
    {
        m_points = new Vector();
        m_highlightPen = null;
        m_arrowInfo = null;
        setBrush(FREEBrush.black);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEStroke stroke = (FREEStroke)super.copyObject(copyEnv);
        if(stroke != null)
        {
            for(int i = 0; i < m_points.size(); i++)
            {
                Point point = (Point)m_points.get(i);
                stroke.m_points.add(i, new Point(point));
            }

            if(m_arrowInfo != null)
            {
                stroke.m_arrowInfo = new ArrowInfo();
                stroke.m_arrowInfo.m_length = m_arrowInfo.m_length;
                stroke.m_arrowInfo.m_shaftLength = m_arrowInfo.m_shaftLength;
                stroke.m_arrowInfo.m_angle = m_arrowInfo.m_angle;
            }
        }
        return stroke;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        FREEPen pen = getPen();
        if(pen == null || pen.getStyle() == 0)
            return;
        int i = getNumPoints();
        FREEPen pen1 = getHighlight();
        if(pen1 != null && pen1.getStyle() != 0)
            if(useBezierLine() && i >= 4)
            {
                java.awt.Stroke stroke = pen1.getStroke();
                if(stroke != null)
                {
                    graphics2d.setStroke(stroke);
                    graphics2d.setColor(pen1.getColor());
                    for(int l = 3; l < i; l += 4)
                    {
                        Point point4 = getPoint(l - 3);
                        Point point6 = getPoint(l - 2);
                        if(l + 4 >= i)
                            l = i - 1;
                        Point point8 = getPoint(l - 1);
                        Point point10 = getPoint(l);
                        java.awt.geom.CubicCurve2D.Double double1 = new java.awt.geom.CubicCurve2D.Double(point4.x, point4.y, point6.x, point6.y, point8.x, point8.y, point10.x, point10.y);
                        graphics2d.draw(double1);
                    }

                }
            } else
            {
                for(int j = 1; j < i; j++)
                {
                    Point point = getPoint(j - 1);
                    Point point2 = getPoint(j);
                    FREEDrawable.drawLine(graphics2d, pen1, point.x, point.y, point2.x, point2.y);
                }

            }
        if(useBezierLine() && i >= 4)
        {
            java.awt.Stroke stroke1 = pen.getStroke();
            if(stroke1 != null)
            {
                graphics2d.setStroke(stroke1);
                graphics2d.setColor(pen.getColor());
                for(int i1 = 3; i1 < i; i1 += 4)
                {
                    Point point5 = getPoint(i1 - 3);
                    Point point7 = getPoint(i1 - 2);
                    if(i1 + 4 >= i)
                        i1 = i - 1;
                    Point point9 = getPoint(i1 - 1);
                    Point point11 = getPoint(i1);
                    java.awt.geom.CubicCurve2D.Double double2 = new java.awt.geom.CubicCurve2D.Double(point5.x, point5.y, point7.x, point7.y, point9.x, point9.y, point11.x, point11.y);
                    graphics2d.draw(double2);
                }
            }
        } else
        {
            for(int k = 1; k < i; k++)
            {
                Point point1 = getPoint(k - 1);
                Point point3 = getPoint(k);
                drawLine(graphics2d, point1.x, point1.y, point3.x, point3.y);
            }

        }
        drawArrows(graphics2d);
    }

    boolean useBezierLine()
    {
        return (getFlags() & 0x1000) != 0;
    }

    public final int addPoint(Point point)
    {
        return addPoint(point.x, point.y);
    }

    public int addPoint(int x, int y)
    {
        m_points.add(new Point(x, y));
        C8(true);
        update(ChangedAddPoint);
        return m_points.size() - 1;
    }

    public final int insertPoint(int i, Point point)
    {
        return insertPoint(i, point.x, point.y);
    }

    public int insertPoint(int i, int j, int k)
    {
        Point point = new Point(j, k);
        int l = i;
        try
        {
            m_points.add(i, point);
        }
        catch(Exception exception)
        {
            m_points.add(point);
            l = m_points.size() - 1;
        }
        C8(true);
        update(ChangedAddPoint);
        return l;
    }

    public void removePoint(int i)
    {
        m_points.removeElementAt(i);
        C8(true);
        update(ChangedRemovePoint);
    }

    public Point getPoint(int i)
    {
        if(i >= 0 && i < getNumPoints())
            return (Point)m_points.elementAt(i);
        else
            return null;
    }

    public int getPointX(int i)
    {
        Point point = getPoint(i);
        if(point != null)
            return point.x;
        else
            return -1;
    }

    public int getPointY(int i)
    {
        Point point = getPoint(i);
        if(point != null)
            return point.y;
        else
            return -1;
    }

    public final void setPoint(int i, Point point)
    {
        setPoint(i, point.x, point.y);
    }

    public void setPoint(int i, int j, int k)
    {
        Point point = getPoint(i);
        if(point != null && (point.x != j || point.y != k))
        {
            point.x = j;
            point.y = k;
            C8(true);
            update(103);
        }
    }

    public void removeAllPoints()
    {
        m_points.removeAllElements();
        C8(true);
        update(ChangedRemovePoint);
    }

    public int getNumPoints()
    {
        return m_points.size();
    }

    public Point getStartPoint()
    {
        return getPoint(0);
    }

    public Point getEndPoint()
    {
        return getPoint(getNumPoints() - 1);
    }

    protected int getFirstPickPoint()
    {
        return 0;
    }

    protected int getLastPickPoint()
    {
        return getNumPoints() - 1;
    }

    public Rectangle handleResize(Graphics2D graphics2d, FREEView view, Rectangle rectangle, Point point, int i, int j, int k, int l)
    {
        if(i >= 100)
            setPoint(i - 100, point);
        else
            return super.handleResize(graphics2d, view, rectangle, point, i, j, k, l);
        return null;
    }

    protected void geometryChange(Rectangle rectangle)
    {
        if(rectangle.width == getWidth() && rectangle.height == getHeight())
        {
            Rectangle rectangle1 = getBoundingRect();
            int i = rectangle1.x - rectangle.x;
            int j = rectangle1.y - rectangle.y;
            if(i == 0 && j == 0)
                return;
            setSuspendUpdates(true);
            for(int k = 0; k < getNumPoints(); k++)
            {
                Point point = getPoint(k);
                if(point != null)
                {
                    int l = point.x + i;
                    int j1 = point.y + j;
                    setPoint(k, l, j1);
                }
            }

            C8(false);
            setSuspendUpdates(false);
        } else
        {
            Rectangle rectangle2 = getBoundingRect();
            double d = 1.0D;
            if(rectangle.width != 0)
                d = (double)rectangle2.width / (double)rectangle.width;
            double d1 = 1.0D;
            if(rectangle.height != 0)
                d1 = (double)rectangle2.height / (double)rectangle.height;
            setSuspendUpdates(true);
            for(int i1 = 0; i1 < getNumPoints(); i1++)
            {
                Point point1 = getPoint(i1);
                if(point1 != null)
                {
                    int k1 = rectangle2.x + (int)Math.rint((double)(point1.x - rectangle.x) * d);
                    int l1 = rectangle2.y + (int)Math.rint((double)(point1.y - rectangle.y) * d1);
                    setPoint(i1, k1, l1);
                }
            }

            C8(false);
            setSuspendUpdates(false);
        }
    }

    protected void gainedSelection(FREESelection selection)
    {
        if(!isResizable())
        {
            selection.createBoundingHandle(this);
            return;
        }
        int i = getLastPickPoint();
        for(int j = getFirstPickPoint(); j <= i; j++)
        {
            Point point = getPoint(j);
            if(point != null)
                selection.createResizeHandle(this, point.x, point.y, 100 + j, true);
        }
    }


    public Rectangle getBoundingRect()
    {
        if(C9())
            _mth012D();
        return super.getBoundingRect();
    }


    public void setBoundingRect(int i, int j, int k, int l)
    {
        C8(false);
        super.setBoundingRect(i, j, k, l);
    }


    private void _mth012D()
    {
        C8(false);
        FREEArea area = getParent();
        Rectangle rectangle = area == null ? null : new Rectangle(C1());
        if(getNumPoints() == 0)
        {
            setBoundingRectForce(getLeft(), getTop(), 0, 0);
            if(area != null)
                area.geometryChangeChild(this, rectangle);
            return;
        }
        Point point = getPoint(0);
        int i = point.x;
        int j = point.y;
        int k = point.x;
        int l = point.y;
        for(int i1 = 1; i1 < getNumPoints(); i1++)
        {
            Point point1 = getPoint(i1);
            i = Math.min(i, point1.x);
            j = Math.min(j, point1.y);
            k = Math.max(k, point1.x);
            l = Math.max(l, point1.y);
        }

        boolean flag = hasArrowAtEnd();
        boolean flag1 = hasArrowAtStart();
        if(flag || flag1)
        {
            if(m_arrowInfo == null)
                m_arrowInfo = new ArrowInfo();
            int x[] = m_arrowInfo.getXCoords();
            int y[] = m_arrowInfo.getYCoords();
            if(flag)
            {
                Point point2 = getArrowToAnchorPoint();
                Point point4 = getArrowToEndPoint();
                if(point2 != null && point4 != null)
                {
                    calculateFilledArrowhead(point2.x, point2.y, point4.x, point4.y, 1, x, y);
                    for(int j1 = 0; j1 < 4; j1++)
                    {
                        i = Math.min(i, x[j1]);
                        j = Math.min(j, y[j1]);
                        k = Math.max(k, x[j1]);
                        l = Math.max(l, y[j1]);
                    }

                }
            }
            if(flag1)
            {
                Point point3 = getArrowFromAnchorPoint();
                Point point5 = getArrowFromEndPoint();
                if(point3 != null && point5 != null)
                {
                    calculateFilledArrowhead(point3.x, point3.y, point5.x, point5.y, 0, x, y);
                    for(int k1 = 0; k1 < 4; k1++)
                    {
                        i = Math.min(i, x[k1]);
                        j = Math.min(j, y[k1]);
                        k = Math.max(k, x[k1]);
                        l = Math.max(l, y[k1]);
                    }

                }
            }
        }
        if(setBoundingRectForce(i, j, k - i, l - j) && area != null)
            area.geometryChangeChild(this, rectangle);
    }

    public void expandRectByPenWidth(Rectangle rectangle)
    {
        FREEPen pen = getPen();
        if(pen != null)
        {
            int i = 2 * pen.getWidth();
            if(getHighlight() != null)
                i = Math.max(i, 2 * getHighlight().getWidth());
            rectangle.grow(i, i);
        }
    }

    public boolean isPointInObj(Point point)
    {
        int i = getSegmentNearPoint(point);
        return i >= 0;
    }

    public int getSegmentNearPoint(Point point)
    {
        Rectangle rectangle = getBoundingRect();
        int i = getPen() == null ? 1 : getPen().getWidth();
        if(point.x < rectangle.x - i || point.x > rectangle.x + rectangle.width + i || point.y < rectangle.y - i || point.y > rectangle.y + rectangle.height + i)
            return -1;
        if(getNumPoints() <= 1)
            return -1;
        i += 4;
        for(int j = 0; j < getNumPoints() - 1; j++)
        {
            Point point1 = getPoint(j);
            Point point2 = getPoint(j + 1);
            int k = Math.max(point1.x, point2.x) + i;
            int l = Math.min(point1.x, point2.x) - i;
            if(l <= point.x && point.x <= k)
            {
                int i1;
                if(point1.x != point2.x)
                {
                    double d = -(double)(point1.y - point2.y) / (double)(point1.x - point2.x);
                    i1 = (int)(d * (double)(-point.x + point1.x) + (double)point1.y);
                } else
                {
                    i1 = point.y;
                }
                int j1 = i1 - i;
                int k1 = i1 + i;
                if(j1 < point.y && point.y < k1)
                    return j;
            }
        }

        return -1;
    }

    public void setArrowHeads(boolean flag, boolean flag1)
    {
        if(hasArrowAtEnd() != flag1 || hasArrowAtStart() != flag)
        {
            int i = 0;
            int j = 0;
            if(flag)
                i |= 0x8000;
            else
                j |= 0x8000;
            if(flag1)
                i |= 0x4000;
            else
                j |= 0x4000;
            setFlags(getFlags() & ~j | i);
            C8(true);
            update(104);
        }
    }

    public boolean hasArrowAtEnd()
    {
        return (getFlags() & 0x4000) != 0;
    }

    public boolean hasArrowAtStart()
    {
        return (getFlags() & 0x8000) != 0;
    }

    public Point getArrowToEndPoint()
    {
        return getEndPoint();
    }

    public Point getArrowToAnchorPoint()
    {
        return getPoint(getNumPoints() - 2);
    }

    public Point getArrowFromEndPoint()
    {
        return getStartPoint();
    }

    public Point getArrowFromAnchorPoint()
    {
        return getPoint(1);
    }

    public void setArrowLength(double d)
    {
        if(getArrowLength() != d)
        {
            if(m_arrowInfo == null)
                m_arrowInfo = new ArrowInfo();
            m_arrowInfo.m_length = d;
            C8(true);
            update(105);
        }
    }

    public double getArrowLength()
    {
        if(m_arrowInfo != null)
            return m_arrowInfo.m_length;
        else
            return 10D;
    }

    public void setArrowShaftLength(double d)
    {
        if(getArrowShaftLength() != d)
        {
            if(m_arrowInfo == null)
                m_arrowInfo = new ArrowInfo();
            m_arrowInfo.m_shaftLength = d;
            C8(true);
            update(106);
        }
    }

    public double getArrowShaftLength()
    {
        if(m_arrowInfo != null)
            return m_arrowInfo.m_shaftLength;
        else
            return 8D;
    }

    public void setArrowAngle(double d)
    {
        if(getArrowAngle() != d && Math.abs(d) < 3D)
        {
            if(m_arrowInfo == null)
                m_arrowInfo = new ArrowInfo();
            m_arrowInfo.m_angle = d;
            C8(true);
            update(107);
        }
    }

    public double getArrowAngle()
    {
        if(m_arrowInfo != null)
            return m_arrowInfo.m_angle;
        else
            return 0.78539816339744828D;
    }

    private void drawArrows(Graphics2D graphics2d)
    {
        boolean flag = hasArrowAtEnd();
        boolean flag1 = hasArrowAtStart();
        if(flag || flag1)
        {
            if(m_arrowInfo == null)
                m_arrowInfo = new ArrowInfo();
            int x[] = m_arrowInfo.getXCoords();
            int y[] = m_arrowInfo.getYCoords();
            if(flag)
            {
                Point point =  getArrowToAnchorPoint();
                Point point2 = getArrowToEndPoint();
                if(point != null && point2 != null)
                {
                    calculateFilledArrowhead(point.x, point.y, point2.x, point2.y, 1, x, y);
                    drawArrowhead(graphics2d, x, y);
                }
            }
            if(flag1)
            {
                Point point1 = getArrowFromAnchorPoint();
                Point point3 = getArrowFromEndPoint();
                if(point1 != null && point3 != null)
                {
                    calculateFilledArrowhead(point1.x, point1.y, point3.x, point3.y, 0, x, y);
                    drawArrowhead(graphics2d, x, y);
                }
            }
        }
    }

    protected void calculateFilledArrowhead(int i, int j, int k, int l, int i1, int x[], int y[])
    {
        int j1 = k - i;
        int k1 = l - j;
        double d = Math.sqrt(j1 * j1 + k1 * k1);
        if(d <= 1.0D)  d = 1.0D;
        double d1 = (double)j1 / d;
        double d2 = (double)k1 / d;
        double d3 = getArrowLength();
        double d4 = getArrowShaftLength();
        double d5 = getArrowAngle();
        int l1 = (int)Math.round(d3 * Math.tan(d5 / 2D));
        double d6 = -d4;
        double d7 = 0.0D;
        double d8 = -d3;
        double d9 = l1;
        double d10 = -d3;
        double d11 = -l1;
        x[0] = k + (int)Math.round(d1 * d6 - d2 * d7);
        y[0] = l + (int)Math.round(d2 * d6 + d1 * d7);
        x[1] = k + (int)Math.round(d1 * d8 - d2 * d9);
        y[1] = l + (int)Math.round(d2 * d8 + d1 * d9);
        x[2] = k;
        y[2] = l;
        x[3] = k + (int)Math.round(d1 * d10 - d2 * d11);
        y[3] = l + (int)Math.round(d2 * d10 + d1 * d11);
    }

    protected void drawArrowhead(Graphics2D graphics2d, int x[], int y[])
    {
        FREEPen pen = getPen();
        if(pen.getStyle() != 65535)
            pen = FREEPen.makeStockPen(pen.getColor());
        FREEDrawable.drawPolygon(graphics2d, pen, getBrush(), x, y, 4);
    }

    public void setHighlight(FREEPen pen)
    {
        if(getHighlight() != pen)
        {
            m_highlightPen = pen;
            update(108);
        }
    }

    public FREEPen getHighlight()
    {
        return m_highlightPen;
    }

    public static final int ChangedAddPoint = 101;
    public static final int ChangedRemovePoint = 102;
    public static final int ChangedModifiedPoint = 103;
    public static final int ChangedArrowHeads = 104;
    public static final int ChangedArrowLength = 105;
    public static final int ChangedArrowShaftLength = 106;
    public static final int ChangedArrowAngle = 107;
    public static final int ChangedHighlight = 108;
    protected Vector m_points;
    private FREEPen m_highlightPen;
    private ArrowInfo m_arrowInfo;

// Unreferenced inner classes:

///* anonymous class */
//    class _cls1
//    {
//    }

}
