//
//
//
// Source File Name:   com/nwoods//FREELinkLabel

package de.shorti.dragdrop;



import java.awt.Point;
import java.awt.Rectangle;

// Referenced classes of package de.shorti.dragdrop:
//            FREEText, FREELabeledLink, FREEObject

public class FREELinkLabel extends FREEText
{

    public FREELinkLabel()
    {
        m_labeledLink = null;
        C0();
    }

    public FREELinkLabel(String s)
    {
        super(s);
        m_labeledLink = null;
        C0();
    }

    public FREELinkLabel(Point point, int i, String s, String s1, boolean flag, boolean flag1, boolean flag2,
            int j, boolean flag3, boolean flag4)
    {
        super(point, i, s, s1, flag, flag1, flag2, j, flag3, flag4);
        m_labeledLink = null;
        C0();
    }

    private final void C0()
    {
        setTransparent(true);
        setDraggable(false);
        setResizable(false);
    }

    protected FREEObject redirectSelection()
    {
        FREELabeledLink labeledlink = getLabeledLink();
        if(labeledlink != null && labeledlink.isGrabChildSelection())
            return labeledlink;
        else
            return this;
    }

    protected void geometryChange(Rectangle rectangle)
    {
        super.geometryChange(rectangle);
        FREELabeledLink labeledlink = getLabeledLink();
        if(labeledlink != null)
            labeledlink.geometryChangeChild(this, rectangle);
    }

    public FREELabeledLink getLabeledLink()
    {
        FREEObject object = getPartner();
        if(object instanceof FREELabeledLink)
            return (FREELabeledLink)object;
        else
            return null;
    }

    public FREEObject getPartner()
    {
        return m_labeledLink;
    }

    public void setPartner(FREEObject object)
    {
        m_labeledLink = object;
    }

    private FREEObject m_labeledLink;
}
