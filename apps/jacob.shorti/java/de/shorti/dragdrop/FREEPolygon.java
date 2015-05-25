package de.shorti.dragdrop;


import java.awt.*;
import java.awt.geom.GeneralPath;

// Referenced classes of package de.shorti.dragdrop:
//            FREEStroke, FREEDrawable, FREEBrush, FREEPen,
//            FREECopyEnvironment, FREEObject, FREEView

public class FREEPolygon extends FREEStroke
{

    public FREEPolygon()
    {
        _fld014D = null;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEPolygon polygon = (FREEPolygon)super.copyObject(copyEnv);
        if(polygon == null);
        return polygon;
    }

    public int addPoint(int i, int j)
    {
        _mth014E(null);
        return super.addPoint(i, j);
    }

    public void removePoint(int i)
    {
        _mth014E(null);
        super.removePoint(i);
    }

    public void setPoint(int i, int j, int k)
    {
        _mth014E(null);
        super.setPoint(i, j, k);
    }

    public void removeAllPoints()
    {
        _mth014E(null);
        super.removeAllPoints();
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        int i = getNumPoints();
        if(i <= 1)
            return;
        GeneralPath generalpath = _mth014D();
        FREEBrush brush = getBrush();
        if(brush != null)
        {
            java.awt.Paint paint1 = brush.getPaint();
            if(paint1 != null)
            {
                graphics2d.setPaint(paint1);
                graphics2d.fill(generalpath);
            }
        }
        FREEPen pen = getPen();
        if(pen != null)
        {
            java.awt.Stroke stroke = pen.getStroke();
            if(stroke != null)
            {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(pen.getColor());
                graphics2d.draw(generalpath);
            }
        }
    }

    public boolean isPointInObj(Point point)
    {
        Rectangle rectangle = getBoundingRect();
        int i = getPen() == null ? 1 : getPen().getWidth();
        if(point.x < rectangle.x - i || point.x > rectangle.x + rectangle.width + i || point.y < rectangle.y - i || point.y > rectangle.y + rectangle.height + i)
            return false;
        int j = getNumPoints();
        if(j <= 1)
        {
            return false;
        } else
        {
            GeneralPath generalpath = _mth014D();
            return generalpath.contains(point.x, point.y);
        }
    }

    private GeneralPath _mth014D()
    {
        if(_fld014D == null)
        {
            int i = getNumPoints();
            _fld014D = new GeneralPath(1, 2 * i);
            Point point = getPoint(0);
            _fld014D.moveTo(point.x, point.y);
            for(int j = 1; j < i; j++)
            {
                Point point1 = getPoint(j);
                _fld014D.lineTo(point1.x, point1.y);
            }

            _fld014D.closePath();
        }
        return _fld014D;
    }

    private void _mth014E(GeneralPath generalpath)
    {
        _fld014D = generalpath;
    }

    private transient GeneralPath _fld014D;
}
