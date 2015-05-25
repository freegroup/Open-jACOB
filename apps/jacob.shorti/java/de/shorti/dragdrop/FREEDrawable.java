package de.shorti.dragdrop;



import java.awt.*;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObject, FREEPen, FREEBrush, FREECopyEnvironment

public abstract class FREEDrawable extends FREEObject
{

    public FREEDrawable()
    {
        m_currentPen = FREEPen.black;
        m_currentBrush = null;
    }

    public FREEDrawable(Rectangle rectangle)
    {
        super(rectangle);
        m_currentPen = FREEPen.black;
        m_currentBrush = null;
    }

    public FREEDrawable(Point point, Dimension dimension)
    {
        super(point, dimension);
        m_currentPen = FREEPen.black;
        m_currentBrush = null;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEDrawable drawable = (FREEDrawable)super.copyObject(copyEnv);
        if(drawable != null)
        {
            drawable.m_currentPen = m_currentPen;
            drawable.m_currentBrush = m_currentBrush;
        }
        return drawable;
    }

    public void setPen(FREEPen pen)
    {
        if(getPen() != pen)
        {
            m_currentPen = pen;
            update(11);
        }
    }

    public FREEPen getPen()
    {
        return m_currentPen;
    }

    public void setBrush(FREEBrush brush)
    {
        if(getBrush() != brush)
        {
            m_currentBrush = brush;
            update(12);
        }
    }

    public FREEBrush getBrush()
    {
        return m_currentBrush;
    }

    public void expandRectByPenWidth(Rectangle rectangle)
    {
        if(getPen() != null)
        {
            int i = getPen().getWidth();
            rectangle.grow(i, i);
        }
    }

    public static void drawLine(Graphics2D graphics2d, FREEPen pen, int i, int j, int k, int l)
    {
        if(pen != null && pen.getStyle() != 0)
        {
            java.awt.Stroke stroke = pen.getStroke();
            if(stroke != null)
            {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(pen.getColor());
                graphics2d.drawLine(i, j, k, l);
            }
        }
    }

    public void drawLine(Graphics2D graphics2d, int i, int j, int k, int l)
    {
        drawLine(graphics2d, getPen(), i, j, k, l);
    }

    public static void drawEllipse(Graphics2D graphics2d, FREEPen pen, FREEBrush brush, int i, int j, int k, int l)
    {
        if(brush != null)
        {
            java.awt.Paint paint = brush.getPaint();
            if(paint != null)
            {
                graphics2d.setPaint(paint);
                graphics2d.fillOval(i, j, k, l);
            }
        }
        if(pen != null)
        {
            java.awt.Stroke stroke = pen.getStroke();
            if(stroke != null)
            {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(pen.getColor());
                graphics2d.drawOval(i, j, k, l);
            }
        }
    }

    public void drawEllipse(Graphics2D graphics2d, int i, int j, int k, int l)
    {
        drawEllipse(graphics2d, getPen(), getBrush(), i, j, k, l);
    }

    public void drawEllipse(Graphics2D graphics2d, Rectangle rectangle)
    {
        drawEllipse(graphics2d, getPen(), getBrush(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public static void drawRect(Graphics2D graphics2d, FREEPen pen, FREEBrush brush, int i, int j, int k, int l)
    {
        if(brush != null)
        {
            java.awt.Paint paint = brush.getPaint();
            if(paint != null)
            {
                graphics2d.setPaint(paint);
                graphics2d.fillRect(i, j, k, l);
            }
        }
        if(pen != null)
        {
            java.awt.Stroke stroke = pen.getStroke();
            if(stroke != null)
            {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(pen.getColor());
                graphics2d.drawRect(i, j, k, l);
            }
        }
    }

    public void drawRect(Graphics2D graphics2d, int i, int j, int k, int l)
    {
        drawRect(graphics2d, getPen(), getBrush(), i, j, k, l);
    }

    public void drawRect(Graphics2D graphics2d, Rectangle rectangle)
    {
        drawRect(graphics2d, getPen(), getBrush(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public static void drawRoundRect(Graphics2D graphics2d, FREEPen pen, FREEBrush brush, int i, int j, int k, int l, int i1,
            int j1)
    {
        if(brush != null)
        {
            java.awt.Paint paint = brush.getPaint();
            if(paint != null)
            {
                graphics2d.setPaint(paint);
                graphics2d.fillRoundRect(i, j, k, l, i1, j1);
            }
        }
        if(pen != null && pen.getStyle() != 0)
        {
            java.awt.Stroke stroke = pen.getStroke();
            if(stroke != null)
            {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(pen.getColor());
                graphics2d.drawRoundRect(i, j, k, l, i1, j1);
            }
        }
    }

    public void drawRoundRect(Graphics2D graphics2d, int i, int j, int k, int l, int i1, int j1)
    {
        drawRoundRect(graphics2d, getPen(), getBrush(), i, j, k, l, i1, j1);
    }

    public static void draw3DRect(Graphics2D graphics2d, FREEPen pen, FREEBrush brush, int i, int j, int k, int l, boolean flag)
    {
        if(brush != null)
        {
            java.awt.Paint paint = brush.getPaint();
            if(paint != null)
            {
                graphics2d.setPaint(paint);
                graphics2d.fill3DRect(i, j, k, l, flag);
            }
        }
        if(pen != null && pen.getStyle() == 65535)
        {
            java.awt.Stroke stroke = pen.getStroke();
            if(stroke != null)
            {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(pen.getColor());
                graphics2d.draw3DRect(i, j, k, l, flag);
            }
        }
    }

    public void draw3DRect(Graphics2D graphics2d, int i, int j, int k, int l, boolean flag)
    {
        draw3DRect(graphics2d, getPen(), getBrush(), i, j, k, l, flag);
    }

    public static void drawPolygon(Graphics2D graphics2d, FREEPen pen, FREEBrush brush, int x[], int y[], int num)
    {
        if(brush != null)
        {
            java.awt.Paint paint = brush.getPaint();
            if(paint != null)
            {
                graphics2d.setPaint(paint);
                graphics2d.fillPolygon(x, y, num);
            }
        }
        if(pen != null)
        {
            java.awt.Stroke stroke = pen.getStroke();
            if(stroke != null)
            {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(pen.getColor());
                graphics2d.drawPolygon(x, y, num);
            }
        }
    }

    public void drawPolygon(Graphics2D graphics2d, int x[], int y[], int num)
    {
        drawPolygon(graphics2d, getPen(), getBrush(), x, y, num);
    }

    private FREEPen m_currentPen;
    private FREEBrush m_currentBrush;
}
