package de.shorti.dragdrop;


import java.awt.Point;


public class FREELabeledLink extends FREELink
{

    public FREELabeledLink()
    {
        m_fromLabel = null;
        m_nidLabel = null;
        m_toLabel = null;
        C0();
    }

    public FREELabeledLink(FREEPort port, FREEPort port1)
    {
        super(port, port1);
        m_fromLabel = null;
        m_nidLabel = null;
        m_toLabel = null;
        C0();
    }

    private final void C0()
    {
        setFlags(getFlags() | 0x200);
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREELabeledLink labeledlink = (FREELabeledLink)super.copyObject(copyEnv);
        if(labeledlink != null)
        {
            if(m_fromLabel != null)
                labeledlink.setFromLabel(m_fromLabel.copyObject(copyEnv));
            if(m_nidLabel != null)
                labeledlink.setMidLabel(m_nidLabel.copyObject(copyEnv));
            if(m_toLabel != null)
                labeledlink.setToLabel(m_toLabel.copyObject(copyEnv));
        }
        return labeledlink;
    }

    protected void ownerChange(Object obj, Object obj1)
    {
        super.ownerChange(obj, obj1);
        if(obj == null && obj1 != null)
        {
            if(obj1 instanceof FREEObjectCollection)
            {
                FREEObjectCollection objectcollection = (FREEObjectCollection)obj1;
                FREEListPosition listposition = objectcollection.findObject(this);
                if(listposition != null)
                {
                    if(getFromLabel() != null)
                        listposition = objectcollection.insertObjectAfter(listposition, getFromLabel());
                    if(getMidLabel() != null)
                        listposition = objectcollection.insertObjectAfter(listposition, getMidLabel());
                    if(getToLabel() != null)
                        listposition = objectcollection.insertObjectAfter(listposition, getToLabel());
                }
            }
        } else
        if(obj != null && obj1 == null && (obj instanceof FREEObjectCollection))
        {
            FREEObjectCollection objectcollection1 = (FREEObjectCollection)obj;
            objectcollection1.removeObject(getFromLabel());
            objectcollection1.removeObject(getMidLabel());
            objectcollection1.removeObject(getToLabel());
        }
    }

    public boolean doMouseClick(int i, Point point, Point point1, FREEView view)
    {
        FREEObject object = view.pickDocObject(point, false);
        if(object == getFromLabel() || object == getMidLabel() || object == getToLabel())
            object.doMouseClick(i, point, point1, view);
        return false;
    }

    public boolean doMouseDblClick(int i, Point point, Point point1, FREEView view)
    {
        FREEObject object = view.pickDocObject(point, false);
        if(object == getFromLabel() || object == getMidLabel() || object == getToLabel())
            object.doMouseDblClick(i, point, point1, view);
        return false;
    }

    protected void calculateStroke()
    {
        super.calculateStroke();
        positionLabels();
    }

    public void positionLabels()
    {
        int i = getNumPoints();
        if(i >= 2)
        {
            FREEObject object = getFromLabel();
            if(object != null)
            {
                int j = getPointX(0);
                int i1 = getPointY(0);
                int l1 = getPointX(1);
                int k2 = getPointY(1);
                if(i == 2)
                    positionEndLabel(object, j, i1, j, i1, l1, k2);
                else
                    positionEndLabel(object, j, i1, l1, k2, getPointX(2), getPointY(2));
            }
            object = getMidLabel();
            if(object != null)
            {
                int k = i / 2;
                int j1 = getPointX(k - 1);
                int i2 = getPointY(k - 1);
                int l2 = getPointX(k);
                int j3 = getPointY(k);
                positionMidLabel(object, j1, i2, l2, j3);
            }
            object = getToLabel();
            if(object != null)
            {
                int l = getPointX(i - 1);
                int k1 = getPointY(i - 1);
                int j2 = getPointX(i - 2);
                int i3 = getPointY(i - 2);
                if(i == 2)
                    positionEndLabel(object, l, k1, l, k1, j2, i3);
                else
                    positionEndLabel(object, l, k1, j2, i3, getPointX(i - 3), getPointY(i - 3));
            }
        }
    }

    protected void positionEndLabel(FREEObject object, int i, int j, int k, int l, int i1, int j1)
    {
        if(i < k)
        {
            if(l <= j1)
                object.setSpotLocation( BottomLeft, i, j);
            else
                object.setSpotLocation( TopLeft, i, j);
        } else
        if(i > k)
        {
            if(l <= j1)
                object.setSpotLocation( BottomRight, i, j);
            else
                object.setSpotLocation( TopRight, i, j);
        } else
        if(j < l)
        {
            if(k <= i1)
                object.setSpotLocation( TopRight, i, j);
            else
                object.setSpotLocation( TopLeft, i, j);
        } else
        if(j > l)
        {
            if(k <= i1)
                object.setSpotLocation( BottomRight, i, j);
            else
                object.setSpotLocation( BottomLeft, i, j);
        } else
        if(k <= i1)
        {
            if(l <= j1)
                object.setSpotLocation( BottomLeft, k, l);
            else
                object.setSpotLocation( TopLeft, k, l);
        } else
        if(l <= j1)
            object.setSpotLocation( BottomRight, k, l);
        else
            object.setSpotLocation( TopRight, k, l);
    }

    protected void positionMidLabel(FREEObject object, int i, int j, int k, int l)
    {
        if((object instanceof FREEText) && Math.abs(j - l) < 2)
            object.setSpotLocation( FREEObject.BottomCenter, (i + k) / 2, (j + l) / 2);
        else
            object.setSpotLocation( FREEObject.Center, (i + k) / 2, (j + l) / 2);
    }

    public FREEObject getFromLabel()
    {
        return m_fromLabel;
    }

    public void setFromLabel(FREEObject object)
    {
        FREEObject object1 = getFromLabel();
        if(object1 != object)
        {
            Object obj = getDocument();
            if(obj == null)
                obj = getView();
            if(object1 != null)
            {
                if(obj != null)
                    ((FREEObjectCollection) (obj)).removeObject(object1);
                object1.setPartner(null);
            }
            m_fromLabel = object;
            if(object != null)
                if(object == getMidLabel())
                {
                    m_nidLabel = null;
                    update(212);
                } else
                if(object == getToLabel())
                {
                    m_toLabel = null;
                    update(213);
                } else
                {
                    object.setPartner(this);
                    if(obj != null)
                        ((FREEObjectCollection) (obj)).insertObjectAfter(((FREEObjectCollection) (obj)).findObject(this), object);
                }
            calculateStroke();
            update(211);
        }
    }

    public FREEObject getMidLabel()
    {
        return m_nidLabel;
    }

    public void setMidLabel(FREEObject object)
    {
        FREEObject object1 = getMidLabel();
        if(object1 != object)
        {
            Object obj = getDocument();
            if(obj == null)
                obj = getView();
            if(object1 != null)
            {
                if(obj != null)
                    ((FREEObjectCollection) (obj)).removeObject(object1);
                object1.setPartner(null);
            }
            m_nidLabel = object;
            if(object != null)
                if(object == getFromLabel())
                {
                    m_fromLabel = null;
                    update(211);
                } else
                if(object == getToLabel())
                {
                    m_toLabel = null;
                    update(213);
                } else
                {
                    object.setPartner(this);
                    if(obj != null)
                        ((FREEObjectCollection) (obj)).insertObjectAfter(((FREEObjectCollection) (obj)).findObject(this), object);
                }
            calculateStroke();
            update(212);
        }
    }

    public FREEObject getToLabel()
    {
        return m_toLabel;
    }

    public void setToLabel(FREEObject object)
    {
        FREEObject object1 = getToLabel();
        if(object1 != object)
        {
            Object obj = getDocument();
            if(obj == null)
                obj = getView();
            if(object1 != null)
            {
                if(obj != null)
                    ((FREEObjectCollection) (obj)).removeObject(object1);
                object1.setPartner(null);
            }
            m_toLabel = object;
            if(object != null)
                if(object == getMidLabel())
                {
                    m_nidLabel = null;
                    update(212);
                } else
                if(object == getFromLabel())
                {
                    m_fromLabel = null;
                    update(211);
                } else
                {
                    object.setPartner(this);
                    if(obj != null)
                        ((FREEObjectCollection) (obj)).insertObjectAfter(((FREEObjectCollection) (obj)).findObject(this), object);
                }
            calculateStroke();
            update(213);
        }
    }

    public void update(int i)
    {
        if(isSuspendUpdates())
            return;
        if(i == 10)
        {
            Object obj = getParent();
            if(obj == null)
                obj = getDocument();
            if(obj == null)
                obj = getView();
            if(obj == null)
                return;
            FREEListPosition listposition = ((FREEObjectCollection) (obj)).findObject(this);
            if(listposition == null)
                return;
            FREEObject object = getFromLabel();
            if(object != null)
                listposition = ((FREEObjectCollection) (obj)).insertObjectAfter(listposition, object);
            object = getMidLabel();
            if(object != null)
                listposition = ((FREEObjectCollection) (obj)).insertObjectAfter(listposition, object);
            object = getToLabel();
            if(object != null)
                listposition = ((FREEObjectCollection) (obj)).insertObjectAfter(listposition, object);
        } else
        if(i == 103 || i == 101 || i == 102 || i == 7)
            positionLabels();
        super.update(i);
    }

    public static final int ChangedFromLabel = 211;
    public static final int ChangedMidLabel = 212;
    public static final int ChangedToLabel = 213;
    private FREEObject m_fromLabel;
    private FREEObject m_nidLabel;
    private FREEObject m_toLabel;
}
