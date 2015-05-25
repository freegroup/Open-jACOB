package de.shorti.dragdrop;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObject, FREEView, FREETextEdit, FREEGlobal,
//            FREESelection, FREELayer, FREEControl, FREECopyEnvironment

public class FREEText extends FREEObject
{

    public FREEText()
    {
        m_string = "";
        m_faceName = _fld0132;
        m_fontSize = _fld0133;
        m_alignment = 0;
        m_textColor = Color.black;
        m_bkColor = Color.white;
        m_textFlags = 0;
        m_userTextFlags = 0;
        C0(null, _fld0133, "", _fld0132, false, false, false, 0, false, false);
    }

    public FREEText(String s)
    {
        m_string = "";
        m_faceName = _fld0132;
        m_fontSize = _fld0133;
        m_alignment = 0;
        m_textColor = Color.black;
        m_bkColor = Color.white;
        m_textFlags = 0;
        m_userTextFlags = 0;
        C0(null, _fld0133, s, _fld0132, false, false, false, 0, false, false);
    }

    public FREEText(Point point, String s)
    {
        m_string = "";
        m_faceName = _fld0132;
        m_fontSize = _fld0133;
        m_alignment = 0;
        m_textColor = Color.black;
        m_bkColor = Color.white;
        m_textFlags = 0;
        m_userTextFlags = 0;
        C0(point, _fld0133, s, _fld0132, false, false, false, 0, false, false);
    }

    public FREEText(Point point, int i, String s, String s1, boolean flag, boolean flag1, boolean flag2,
            int j, boolean flag3, boolean flag4)
    {
        m_string = "";
        m_faceName = _fld0132;
        m_fontSize = _fld0133;
        m_alignment = 0;
        m_textColor = Color.black;
        m_bkColor = Color.white;
        m_textFlags = 0;
        m_userTextFlags = 0;
        C0(point, i, s, s1, flag, flag1, flag2, j, flag3, flag4);
    }

    private final void C0(Point point, int i, String s, String s1, boolean flag, boolean flag1, boolean flag2,
            int j, boolean flag3, boolean flag4)
    {
        m_string = s;
        m_faceName = s1;
        m_fontSize = i;
        m_alignment = j;
        m_textColor = Color.black;
        m_bkColor = Color.white;
        int k = 16384;
        if(flag)
            k |= 0x2;
        if(flag1)
            k |= 0x4;
        if(flag2)
            k |= 0x8;
        if(flag3)
            k |= 0x10;
        if(flag4)
            k |= 0x20;
        m_textFlags = k;
        _fld0134 = 1;
        _fld0135 = null;
        _fld0136 = null;
        _fld0137 = null;
        _fld0138 = null;
        m_textEdit = null;
        setResizable(false);
        _mth013B(null, null);
        if(point != null)
            setLocation(point);
        m_textFlags |= 0x200;
        update();
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEText text = (FREEText)super.copyObject(copyEnv);
        if(text != null)
        {
            text.m_string = m_string;
            text.m_faceName = m_faceName;
            text.m_fontSize = m_fontSize;
            text.m_alignment = m_alignment;
            text.m_textColor = m_textColor;
            text.m_bkColor = m_bkColor;
            text.m_textFlags = m_textFlags;
            text.m_userTextFlags = m_userTextFlags;
            text._fld0134 = _fld0134;
            text._fld0135 = null;
            text._fld0136 = null;
            text._fld0137 = null;
            text._fld0138 = null;
            text.m_textEdit = null;
        }
        return text;
    }

    public void setText(String s)
    {
        if(!m_string.equals(s))
        {
            m_string = s;
            _mth013A(512);
            update(501);
        }
    }

    public String getText()
    {
        return m_string;
    }

    public void setAlignment(int i)
    {
        if(getAlignment() != i)
        {
            m_alignment = i;
            update(502);
        }
    }

    public int getAlignment()
    {
        return m_alignment;
    }

    public void setFontSize(int i)
    {
        if(i > 0 && getFontSize() != i)
        {
            m_fontSize = i;
            _mth013A(768);
            update(503);
        }
    }

    public int getFontSize()
    {
        return m_fontSize;
    }

    public void setFaceName(String s)
    {
        if(!getFaceName().equals(s))
        {
            m_faceName = s;
            _mth013A(768);
            update(504);
        }
    }

    public String getFaceName()
    {
        return m_faceName;
    }

    public void setTextColor(Color color)
    {
        if(!getTextColor().equals(color))
        {
            m_textColor = color;
            update(505);
        }
    }

    public Color getTextColor()
    {
        return m_textColor;
    }

    public void setBkColor(Color color)
    {
        if(!getBkColor().equals(color))
        {
            m_bkColor = color;
            update(506);
        }
    }

    public Color getBkColor()
    {
        return m_bkColor;
    }

    public final void setTextFlags(int i)
    {
        m_userTextFlags = i;
    }

    public final int getTextFlags()
    {
        return m_userTextFlags;
    }

    public void setTransparent(boolean flag)
    {
        if(isTransparent() != flag)
        {
            if(flag)
                m_textFlags |= 0x1;
            else
                m_textFlags &= 0xfffffffe;
            update(507);
        }
    }

    public boolean isTransparent()
    {
        return (m_textFlags & 0x1) != 0;
    }

    public void setBold(boolean flag)
    {
        if(isBold() != flag)
        {
            if(flag)
                m_textFlags |= 0x2;
            else
                m_textFlags &= 0xfffffffd;
            _mth013A(768);
            update(508);
        }
    }

    public boolean isBold()
    {
        return (m_textFlags & 0x2) != 0;
    }

    public void setUnderline(boolean flag)
    {
        if(isUnderline() != flag)
        {
            if(flag)
                m_textFlags |= 0x4;
            else
                m_textFlags &= 0xfffffffb;
            _mth013A(768);
            update(509);
        }
    }

    public boolean isUnderline()
    {
        return (m_textFlags & 0x4) != 0;
    }

    public void setStrikeThrough(boolean flag)
    {
        if(isStrikeThrough() != flag)
        {
            if(flag)
                m_textFlags |= 0x2000;
            else
                m_textFlags &= 0xffffdfff;
            _mth013A(768);
            update(517);
        }
    }

    public boolean isStrikeThrough()
    {
        return (m_textFlags & 0x2000) != 0;
    }

    public void setItalic(boolean flag)
    {
        if(isItalic() != flag)
        {
            if(flag)
                m_textFlags |= 0x8;
            else
                m_textFlags &= 0xfffffff7;
            _mth013A(768);
            update(510);
        }
    }

    public boolean isItalic()
    {
        return (m_textFlags & 0x8) != 0;
    }

    public void setMultiline(boolean flag)
    {
        if(isMultiline() != flag)
        {
            if(flag)
                m_textFlags |= 0x10;
            else
                m_textFlags &= 0xffffffef;
            if(!flag)
                _fld0134 = 1;
            _mth013A(512);
            update(511);
        }
    }

    public boolean isMultiline()
    {
        return (m_textFlags & 0x10) != 0;
    }

    public void setEditable(boolean flag)
    {
        if(isEditable() != flag)
        {
            if(flag)
                m_textFlags |= 0x20;
            else
                m_textFlags &= 0xffffffdf;
            update(512);
        }
    }

    public boolean isEditable()
    {
        return (m_textFlags & 0x20) != 0;
    }

    public void setEditOnSingleClick(boolean flag)
    {
        if(isEditOnSingleClick() != flag)
        {
            if(flag)
                m_textFlags |= 0x40;
            else
                m_textFlags &= 0xffffffbf;
            update(513);
        }
    }

    public boolean isEditOnSingleClick()
    {
        return (m_textFlags & 0x40) != 0;
    }

    public void setSelectBackground(boolean flag)
    {
        if(isSelectBackground() != flag)
        {
            if(flag)
                m_textFlags |= 0x800;
            else
                m_textFlags &= 0xfffff7ff;
            update(515);
        }
    }

    public boolean isSelectBackground()
    {
        return (m_textFlags & 0x800) != 0;
    }

    public void set2DScale(boolean flag)
    {
        if(is2DScale() != flag)
        {
            if(flag)
                m_textFlags |= 0x80;
            else
                m_textFlags &= 0xffffff7f;
            update(514);
        }
    }

    public boolean is2DScale()
    {
        return (m_textFlags & 0x80) != 0;
    }

    public void setClipping(boolean flag)
    {
        if(isClipping() != flag)
        {
            if(flag)
                m_textFlags |= 0x1000;
            else
                m_textFlags &= 0xffffefff;
            update(516);
        }
    }

    public boolean isClipping()
    {
        return (m_textFlags & 0x1000) != 0;
    }

    public void setAutoResize(boolean flag)
    {
        if(isAutoResize() != flag)
        {
            if(flag)
                m_textFlags |= 0x4000;
            else
                m_textFlags &= 0xffffbfff;
            update(518);
        }
    }

    public boolean isAutoResize()
    {
        return (m_textFlags & 0x4000) != 0;
    }

    public void expandRectByPenWidth(Rectangle rectangle)
    {
        int i = 10;
        if(_fld0136 != null)
            i = _fld0136.charWidth('M');
        rectangle.x -= i;
        rectangle.y -= i;
        rectangle.width += i * 4;
        rectangle.height += i * 2;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        if((m_textFlags & 0x400) == 1024)
            _mth0133(graphics2d, view);
        graphics2d.setFont(getFont());
        if((m_textFlags & 0x200) == 512)
            _mth013B(graphics2d, view.DB());
        Rectangle rectangle = getBoundingRect();
        if(!isTransparent())
        {
            graphics2d.setColor(getBkColor());
            graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        String s = getText();
        if(s.equals(""))
            return;
        graphics2d.setColor(getTextColor());
        Rectangle rectangle1 = null;
        if(isClipping())
        {
            rectangle1 = graphics2d.getClipBounds();
            graphics2d.clipRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        int i = _mth0137(graphics2d).getHeight() - _mth0137(graphics2d).getMaxDescent();
        if(isMultiline())
        {
            int j = 0;
            byte byte0 = -1;
            for(boolean flag = false; !flag;)
            {
                int l = s.indexOf(10, j);
                if(l == -1)
                {
                    l = s.length();
                    flag = true;
                }
                if(j < l)
                {
                    int i1 = _mth013E(j, l, view.DB());
                    if(FREEGlobal.isAtLeastJavaVersion(1.22D))
                    {
                        graphics2d.drawString(_mth0138().getIterator(null, j, l), i1, rectangle.y + i);
                    } else
                    {
                        String s1 = s.substring(j, l);
                        graphics2d.drawString(s1, i1, rectangle.y + i);
                        if(isUnderline() || isStrikeThrough())
                        {
                            BasicStroke basicstroke1 = new BasicStroke(1.0F, 0, 2, 0.0F);
                            graphics2d.setStroke(basicstroke1);
                            if(isUnderline())
                                graphics2d.drawLine(i1, rectangle.y + i, i1 + _mth0137(graphics2d).stringWidth(s1), rectangle.y + i);
                            if(isStrikeThrough())
                                graphics2d.drawLine(i1, (rectangle.y + i) - _mth0137(graphics2d).getAscent() / 3, i1 + _mth0137(graphics2d).stringWidth(s1), (rectangle.y + i) - _mth0137(graphics2d).getAscent() / 3);
                        }
                    }
                }
                j = l + 1;
                i += _mth0137(graphics2d).getHeight();
            }

        } else
        {
            int k = _mth013E(0, s.length(), view.DB());
            if(FREEGlobal.isAtLeastJavaVersion(1.22D))
            {
                graphics2d.drawString(_mth0139(), k, rectangle.y + i);
            } else
            {
                graphics2d.drawString(s, k, rectangle.y + i);
                if(isUnderline() || isStrikeThrough())
                {
                    BasicStroke basicstroke = new BasicStroke(1.0F, 0, 2, 0.0F);
                    graphics2d.setStroke(basicstroke);
                    if(isUnderline())
                        graphics2d.drawLine(k, rectangle.y + i, k + _mth0137(graphics2d).stringWidth(s), rectangle.y + i);
                    if(isStrikeThrough())
                        graphics2d.drawLine(k, (rectangle.y + i) - _mth0137(graphics2d).getAscent() / 3, k + _mth0137(graphics2d).stringWidth(s), (rectangle.y + i) - _mth0137(graphics2d).getAscent() / 3);
                }
            }
        }
        if(rectangle1 != null)
            graphics2d.setClip(rectangle1);
    }

    protected void ownerChange(Object obj, Object obj1)
    {
        if(_fld0136 == null && obj1 != null)
            _mth013A(512);
    }

    protected void gainedSelection(FREESelection selection)
    {
        if(!isResizable())
        {
            if(isSelectBackground())
                setTransparent(false);
            else
                selection.createBoundingHandle(this);
            return;
        }
        if(is2DScale())
        {
            super.gainedSelection(selection);
            return;
        }
        Rectangle rectangle = getBoundingRect();
        int i = rectangle.x;
        int j = rectangle.x + rectangle.width / 2;
        int k = rectangle.x + rectangle.width;
        int l = rectangle.y;
        int i1 = rectangle.y + rectangle.height / 2;
        int j1 = rectangle.y + rectangle.height;
        selection.createResizeHandle(this, i, l, -1, false);
        selection.createResizeHandle(this, k, l, -1, false);
        selection.createResizeHandle(this, i, j1, -1, false);
        selection.createResizeHandle(this, k, j1, -1, false);
        if(!is4ResizeHandles())
        {
            selection.createResizeHandle(this, j, l, 2, true);
            selection.createResizeHandle(this, k, i1, -1, false);
            selection.createResizeHandle(this, j, j1, 6, true);
            selection.createResizeHandle(this, i, i1, -1, false);
        }
    }

    protected void lostSelection(FREESelection selection)
    {
        if(isSelectBackground())
            setTransparent(true);
        super.lostSelection(selection);
    }

    private boolean _mth0132(Graphics2D graphics2d, FREEView view, Font font)
    {
        int i = _mth013C(font, view.DB());
        Rectangle rectangle = getBoundingRect();
        if(rectangle.width < i)
            return false;
        FontMetrics fontmetrics = graphics2d.getFontMetrics(font);
        int j = _fld0134 * fontmetrics.getHeight();
        return rectangle.height >= j;
    }

    private void _mth0133(Graphics2D graphics2d, FREEView view)
    {
        String s = getFont().getFontName();
        int i = getFont().getStyle();
        int j;
        Font font;
        for(j = 13; _mth0132(graphics2d, view, font = new Font(s, i, j)); j += 14);
        Font font1;
        for(j--; !_mth0132(graphics2d, view, font1 = new Font(s, i, j)) && j > 1; j--);
        if(j != getFontSize())
        {
            m_fontSize = j;
            _fld0135 = font1;
            _fld0136 = null;
        }
        m_textFlags &= 0xfffffbff;
    }

    private boolean _mth0134(Graphics2D graphics2d, String s, int i, int j, int k)
    {
        Font font = new Font(s, i, j);
        FontMetrics fontmetrics = graphics2d.getFontMetrics(font);
        return fontmetrics.getHeight() < k;
    }

    private int _mth0135(int i, Graphics2D graphics2d)
    {
        int j = 0;
        if(isBold())
            j |= 0x1;
        if(isItalic())
            j |= 0x2;
        int k;
        for(k = 13; _mth0134(graphics2d, getFaceName(), j, k, i); k += 14);
        for(k--; !_mth0134(graphics2d, getFaceName(), j, k, i) && k > 1; k--);
        return k;
    }

    public Rectangle handleResize(Graphics2D graphics2d, FREEView view, Rectangle rectangle, Point point, int i, int j, int k,
            int l)
    {
        if(!is2DScale())
        {
            Rectangle rectangle1 = new Rectangle(rectangle);
            switch(i)
            {
            case 2: // '\002'
                if(point.y < rectangle.y + rectangle.height)
                    rectangle1.height += rectangle.y - point.y;
                rectangle1.y = point.y;
                break;

            case 6: // '\006'
                if(point.y > rectangle.y)
                    rectangle1.height = point.y - rectangle.y;
                break;
            }
            if(rectangle1.height < 1)
                rectangle1.height = 1;
            if(rectangle1.height == rectangle.height)
                return null;
            if(_fld0134 < 1)
                _fld0134 = 1;
            int i1 = rectangle1.height / _fld0134;
            m_fontSize = _mth0135(i1, graphics2d);
            _mth0136();
            rectangle1.width = _mth013C(getFont(), view.DB());
            rectangle1.height = _fld0134 * _mth0137(graphics2d).getHeight();
            setBoundingRect(rectangle1);
            return null;
        } else
        {
            return super.handleResize(graphics2d, view, rectangle, point, i, j, 1, 1);
        }
    }

    protected void geometryChange(Rectangle rectangle)
    {
        Rectangle rectangle1 = getBoundingRect();
        if(rectangle1.width != rectangle.width || rectangle1.height != rectangle.height)
        {
            if(isResizable())
                m_textFlags |= 0x400;
            update();
        }
    }

    private void _mth0136()
    {
        int i = 0;
        if(isBold())
            i |= 0x1;
        if(isItalic())
            i |= 0x2;
        _fld0135 = new Font(getFaceName(), i, getFontSize());
        _fld0136 = null;
    }

    public Font getFont()
    {
        if(_fld0135 == null)
            _mth0136();
        return _fld0135;
    }

    FontMetrics _mth0137(Graphics2D graphics2d)
    {
        if(_fld0136 == null)
            _fld0136 = graphics2d.getFontMetrics(getFont());
        return _fld0136;
    }

    AttributedString _mth0138()
    {
        if(_fld0138 == null)
        {
            AttributedString attributedstring = new AttributedString(getText());
            attributedstring.addAttribute(TextAttribute.FONT, getFont());
            if(isUnderline())
                attributedstring.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            if(isStrikeThrough())
                attributedstring.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            _fld0138 = attributedstring;
        }
        return _fld0138;
    }

    AttributedCharacterIterator _mth0139()
    {
        if(_fld0137 == null)
            _fld0137 = _mth0138().getIterator();
        else
            _fld0137.first();
        return _fld0137;
    }

    private void _mth013A(int i)
    {
        _fld0137 = null;
        _fld0138 = null;
        if((i & 0x100) != 0)
            _mth0136();
        if((i & 0x200) != 0)
            if(isAutoResize())
                _mth013B(null, null);
            else
                i &= 0xfffffdff;
        m_textFlags |= i;
    }

    private void _mth013B(Graphics2D graphics2d, FontRenderContext fontrendercontext)
    {
        int i;
        if(graphics2d != null)
        {
            if(fontrendercontext == null)
                fontrendercontext = graphics2d.getFontRenderContext();
            i = _mth013C(getFont(), fontrendercontext);
        } else
        {
            Component component = FREEGlobal.getComponent();
            if(component == null)
                return;
            graphics2d = (Graphics2D)component.getGraphics();
            if(graphics2d == null)
                return;
            if(fontrendercontext == null && (component instanceof FREEView))
                fontrendercontext = ((FREEView)component).DB();
            if(fontrendercontext == null)
                fontrendercontext = graphics2d.getFontRenderContext();
            i = _mth013C(getFont(), fontrendercontext);
            graphics2d.dispose();
        }
        if(i < 10)
            i = 10;
        int j = _mth0137(graphics2d).getHeight() * _fld0134;
        if(i != getWidth() || j != getHeight())
        {
            int k = getFlags();
            setFlags(k & 0xffffffef);
            Point point = getLocation();
            setSize(i, j);
            setLocation(point);
            setFlags(k);
        }
        m_textFlags &= 0xfffffdff;
    }

    private int _mth013C(Font font, FontRenderContext fontrendercontext)
    {
        if(getText().equals(""))
        {
            _fld0134 = 1;
            return 0;
        }
        String s = getText();
        if(isMultiline())
        {
            int i = 0;
            int j = 0;
            boolean flag = false;
            for(_fld0134 = 0; !flag; _fld0134++)
            {
                int k = s.indexOf(10, j);
                if(k == -1)
                {
                    k = s.length();
                    flag = true;
                }
                int l = _mth013D(j, k, font, fontrendercontext);
                if(l > i)
                    i = l;
                j = k + 1;
            }

            return i;
        } else
        {
            _fld0134 = 1;
            return _mth013D(0, s.length(), font, fontrendercontext);
        }
    }

    private int _mth013D(int i, int j, Font font, FontRenderContext fontrendercontext)
    {
        if(i >= j)
            return 0;
        double d;
        if(i != 0 && !FREEGlobal.isAtLeastJavaVersion(1.22D))
        {
            String s = getText().substring(i, j);
            d = font.getStringBounds(s, fontrendercontext).getWidth();
        } else
        {
            d = font.getStringBounds(_mth0139(), i, j, fontrendercontext).getWidth();
        }
        int k = (int)Math.ceil(d);
        return k;
    }

    private int _mth013E(int i, int j, FontRenderContext fontrendercontext)
    {
        Rectangle rectangle = getBoundingRect();
        int l;
        switch(getAlignment())
        {
        case 0: // '\0'
        default:
            return rectangle.x;

        case 1: // '\001'
            int k = _mth013D(i, j, getFont(), fontrendercontext);
            return rectangle.x + (rectangle.width - k) / 2;

        case 2: // '\002'
            l = _mth013D(i, j, getFont(), fontrendercontext);
            break;
        }
        return (rectangle.x + rectangle.width) - l;
    }

    public Point getLocation(Point point)
    {
        Rectangle rectangle = getBoundingRect();
        if(point == null)
            point = new Point();
        switch(getAlignment())
        {
        case 0: // '\0'
        default:
            point.x = rectangle.x;
            point.y = rectangle.y;
            break;

        case 1: // '\001'
            point.x = rectangle.x + rectangle.width / 2;
            point.y = rectangle.y;
            break;

        case 2: // '\002'
            point.x = rectangle.x + rectangle.width;
            point.y = rectangle.y;
            break;
        }
        return point;
    }

    public void setLocation(int i, int j)
    {
        Rectangle rectangle = getBoundingRect();
        switch(getAlignment())
        {
        case 0: // '\0'
        default:
            setBoundingRect(i, j, rectangle.width, rectangle.height);
            break;

        case 1: // '\001'
            setBoundingRect(i - rectangle.width / 2, j, rectangle.width, rectangle.height);
            break;

        case 2: // '\002'
            setBoundingRect(i - rectangle.width, j, rectangle.width, rectangle.height);
            break;
        }
    }

    public boolean doMouseClick(int i, Point point, Point point1, FREEView view)
    {
        if(!isEditable())
            return false;
        if(!isEditOnSingleClick())
            return false;
        if(getLayer() != null && !getLayer().isModifiable())
        {
            return false;
        } else
        {
            doStartEdit(view, point1);
            return true;
        }
    }

    public boolean doMouseDblClick(int i, Point point, Point point1, FREEView view)
    {
        if(!isEditable())
            return false;
        if(isEditOnSingleClick())
            return false;
        if(getLayer() != null && !getLayer().isModifiable())
        {
            return false;
        } else
        {
            doStartEdit(view, point1);
            return true;
        }
    }

    public void doStartEdit(FREEView view, Point point)
    {
        if(view == null)
            return;
        view.getSelection().clearSelectionHandles(this);
        Rectangle rectangle = new Rectangle(getBoundingRect());
        if(isMultiline())
            rectangle.height += getHeight() / _fld0134;
        view.setEditControl(new FREETextEdit(rectangle, getText(), isMultiline(), this));
        m_textEdit = view.getEditControl();
        if(m_textEdit != null)
        {
            JComponent jcomponent = m_textEdit.getComponent(view);
            if(jcomponent instanceof JTextComponent)
            {
                JTextComponent jtextcomponent = (JTextComponent)jcomponent;
                jtextcomponent.selectAll();
                jtextcomponent.grabFocus();
            }
        }
    }

    public void doEndEdit()
    {
        if(m_textEdit != null)
        {
            FREEView view = m_textEdit.getView();
            if(view != null)
                view.setEditControl(null);
            m_textEdit = null;
            _mth013A(512);
            update();
            if(view != null)
            {
                view.getSelection().restoreSelectionHandles(this);
                view.grabFocus();
            }
        }
    }

    public static void setDefaultFontFaceName(String s)
    {
        _fld0132 = s;
    }

    public static String getDefaultFontFaceName()
    {
        return _fld0132;
    }

    public static void setDefaultFontSize(int i)
    {
        _fld0133 = i;
    }

    public static int getDefaultFontSize()
    {
        return _fld0133;
    }

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ChangedText = 501;
    public static final int ChangedAlignment = 502;
    public static final int ChangedFontSize = 503;
    public static final int ChangedFaceName = 504;
    public static final int ChangedTextColor = 505;
    public static final int ChangedBkColor = 506;
    public static final int ChangedTransparent = 507;
    public static final int ChangedBold = 508;
    public static final int ChangedUnderline = 509;
    public static final int ChangedItalic = 510;
    public static final int ChangedMultiline = 511;
    public static final int ChangedEditable = 512;
    public static final int ChangedEditOnSingleClick = 513;
    public static final int Changed2DScale = 514;
    public static final int ChangedSelectBackground = 515;
    public static final int ChangedClipping = 516;
    public static final int ChangedStrikeThrough = 517;
    public static final int ChangedAutoResize = 518;
    private static final int CB = 0x40000;
    private static final int CA = 0x80000;
    private static String _fld0132 = "SansSerif";
    private static int _fld0133 = 12;
    private String m_string;
    private String m_faceName;
    private int m_fontSize;
    private int m_alignment;
    private Color m_textColor;
    private Color m_bkColor;
    private int m_textFlags;
    private int m_userTextFlags;
    private transient int _fld0134;
    private transient Font _fld0135;
    private transient FontMetrics _fld0136;
    private transient AttributedCharacterIterator _fld0137;
    private transient AttributedString _fld0138;
    private transient FREETextEdit m_textEdit;

}
