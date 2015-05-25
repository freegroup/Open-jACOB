package de.shorti.dragdrop;



import java.awt.*;


public class FREE3DNoteRect extends FREERectangle
{

    public FREE3DNoteRect()
    {
        m_shadowSize = 4;
        m_flapSize = 8;
    }

    public FREE3DNoteRect(Rectangle rectangle)
    {
        super(rectangle);
        m_shadowSize = 4;
        m_flapSize = 8;
    }

    public FREE3DNoteRect(Point point, Dimension dimension)
    {
        super(point, dimension);
        m_shadowSize = 4;
        m_flapSize = 8;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREE3DNoteRect note = (FREE3DNoteRect)super.copyObject(copyEnv);
        if(note != null)
        {
            note.m_shadowSize = m_shadowSize;
            note.m_flapSize = m_flapSize;
        }
        return note;
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        Rectangle rectangle = getBoundingRect();
        int i = getShadowSize();
        int j = rectangle.x;
        int k = rectangle.y;
        int l = rectangle.width - i;
        int i1 = rectangle.height - i;
        drawRect(graphics2d, j, k, l, i1);
        int ai[] = view.E2();
        int ai1[] = view.E3();
        ai[0] = j + l;
        ai1[0] = k;
        ai[1] = rectangle.x + rectangle.width;
        ai1[1] = rectangle.y + i;
        ai[2] = rectangle.x + rectangle.width;
        ai1[2] = rectangle.y + rectangle.height;
        ai[3] = rectangle.x + i;
        ai1[3] = rectangle.y + rectangle.height;
        ai[4] = j;
        ai1[4] = k + i1;
        ai[5] = j + l;
        ai1[5] = k + i1;
        graphics2d.setColor(Color.lightGray);
        graphics2d.fillPolygon(ai, ai1, 6);
        int j1 = getFlapSize();
        ai[0] = (j + l) - j1;
        ai1[0] = k + i1;
        ai[1] = (j + l) - (j1 * 7) / 8;
        ai1[1] = (k + i1) - (j1 * 7) / 8;
        ai[2] = j + l;
        ai1[2] = (k + i1) - j1;
        int k1 = Color.gray.getRed();
        int l1 = Color.gray.getGreen();
        int i2 = Color.gray.getBlue();
        graphics2d.setColor(new Color(k1, l1, i2, 192));
        graphics2d.fillPolygon(ai, ai1, 3);
    }

    public int getShadowSize()
    {
        return m_shadowSize;
    }

    public void setShadowSize(int i)
    {
        if(getShadowSize() != i)
        {
            m_shadowSize = Math.max(i, 0);
            update(401);
        }
    }

    public int getFlapSize()
    {
        return m_flapSize;
    }

    public void setFlapSize(int i)
    {
        if(getFlapSize() != i)
        {
            m_flapSize = Math.max(i, 0);
            update(402);
        }
    }

    public static final int ChangedShadowSize = 401;
    public static final int ChangedFlapSize = 402;
    private int m_shadowSize;
    private int m_flapSize;
}
