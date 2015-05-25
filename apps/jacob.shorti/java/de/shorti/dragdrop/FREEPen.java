//
//
//
// Source File Name:   com/nwoods//FREEPen

package de.shorti.dragdrop;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.Serializable;

public class FREEPen
    implements Serializable
{

    public FREEPen()
    {
        _fld0115 = null;
        m_style  = SOLID;
        m_width  = 1;
        m_color  = Color.black;
        _fld0115 = null;
    }

    public FREEPen(int style, int width, Color color)
    {
        _fld0115 = null;
        m_style = style;
        m_width = (style != NONE) ? Math.max(width, 1) : 0;
        m_color = color;
        _fld0115 = null;
    }

    public FREEPen(Stroke stroke, Color color)
    {
        _fld0115 = null;
        m_style  = CUSTOM;
        if(stroke != null && (stroke instanceof BasicStroke))
            m_width = (int)((BasicStroke)stroke).getLineWidth();
        else
            m_width = 1;
        m_color = color;
        _fld0115 = stroke;
    }

    public static FREEPen makeStockPen(Color color)
    {
        if(color.equals(Color.black))
            return black;
        if(color.equals(Color.darkGray))
            return darkGray;
        if(color.equals(Color.gray))
            return gray;
        if(color.equals(Color.lightGray))
            return lightGray;
        if(color.equals(Color.white))
            return white;
        else
            return new FREEPen(SOLID, 1, color);
    }

    public static FREEPen make(int i, int j, Color color)
    {
        if(i == 0)
            return Null;
        if(i == 65535 && j == 1)
            return makeStockPen(color);
        else
            return new FREEPen(i, j, color);
    }

    public int getStyle()
    {
        return m_style;
    }

    public int getWidth()
    {
        return m_width;
    }

    public Color getColor()
    {
        return m_color;
    }

    public Stroke getStroke()
    {
        if(_fld0115 == null)
            switch(getStyle())
            {
            case 0: // '\0'
            default:
                _fld0115 = null;
                break;

            case 65535:
                int i = getWidth();
                _fld0115 = new BasicStroke(i, 0, 0, 2 * i);
                break;

            case 1: // '\001'
                float f = getWidth();
                float af[] = {
                    3F * f, f
                };
                _fld0115 = new BasicStroke(f, 0, 0, 2.0F * f, af, 0.0F);
                break;

            case 2: // '\002'
                float f1 = getWidth();
                float af1[] = {
                    f1, f1
                };
                _fld0115 = new BasicStroke(f1, 0, 0, 2.0F * f1, af1, 0.0F);
                break;

            case 3: // '\003'
                float f2 = getWidth();
                float af2[] = {
                    3F * f2, f2, f2, f2
                };
                _fld0115 = new BasicStroke(f2, 0, 0, 2.0F * f2, af2, 0.0F);
                break;

            case 4: // '\004'
                float f3 = getWidth();
                float af3[] = {
                    3F * f3, f3, f3, f3, f3, f3
                };
                _fld0115 = new BasicStroke(f3, 0, 0, 2.0F * f3, af3, 0.0F);
                break;
            }
        return _fld0115;
    }

    public static final int NONE = 0;
    public static final int SOLID = 65535;
    public static final int DASHED = 1;
    public static final int DOTTED = 2;
    public static final int DASHDOT = 3;
    public static final int DASHDOTDOT = 4;
    public static final int CUSTOM = 65534;
    public static final FREEPen black;
    public static final FREEPen darkGray;
    public static final FREEPen gray;
    public static final FREEPen lightGray;
    public static final FREEPen white;
    public static final FREEPen Null;
    private int m_style;
    private int m_width;
    private Color m_color;
    private transient Stroke _fld0115;

    static
    {
        black = new FREEPen(65535, 1, Color.black);
        darkGray = new FREEPen(65535, 1, Color.darkGray);
        gray = new FREEPen(65535, 1, Color.gray);
        lightGray = new FREEPen(65535, 1, Color.lightGray);
        white = new FREEPen(65535, 1, Color.white);
        Null = new FREEPen(0, 1, Color.black);
    }
}
