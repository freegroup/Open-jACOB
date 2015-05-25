
package de.shorti.dragdrop;


import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;

public class FREEBrush
    implements Serializable
{

    public FREEBrush()
    {
        _fld0116 = null;
        m_style = 0;
        _fld0116 = null;
        m_color = null;
    }

    public FREEBrush(int i, Color color)
    {
        _fld0116 = null;
        m_style = i;
        _fld0116 = color;
        m_color = color;
    }

    public FREEBrush(Paint paint)
    {
        _fld0116 = null;
        _fld0116 = paint;
        if(paint instanceof Color)
        {
            m_style = 65535;
            m_color = (Color)paint;
        } else
        {
            m_style = 65534;
            m_color = null;
        }
    }

    public static FREEBrush makeStockBrush(Color color)
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
            return new FREEBrush(65535, color);
    }

    public static FREEBrush make(int i, Color color)
    {
        if(i == 65535)
            return makeStockBrush(color);
        else
            return Null;
    }

    public int getStyle()
    {
        return m_style;
    }

    public Paint getPaint()
    {
        if(_fld0116 == null)
            switch(getStyle())
            {
            case 0: // '\0'
            default:
                _fld0116 = null;
                break;

            case 65535:
                _fld0116 = m_color;
                break;
            }
        return _fld0116;
    }

    public Color getColor()
    {
        return m_color;
    }

    public static final int NONE = 0;
    public static final int SOLID = 65535;
    public static final int CUSTOM = 65534;
    public static final FREEBrush black;
    public static final FREEBrush darkGray;
    public static final FREEBrush gray;
    public static final FREEBrush lightGray;
    public static final FREEBrush white;
    public static final FREEBrush Null = new FREEBrush(0, null);
    private int m_style;
    private Color m_color;
    private transient Paint _fld0116;

    static
    {
        black = new FREEBrush(65535, Color.black);
        darkGray = new FREEBrush(65535, Color.darkGray);
        gray = new FREEBrush(65535, Color.gray);
        lightGray = new FREEBrush(65535, Color.lightGray);
        white = new FREEBrush(65535, Color.white);
    }
}
