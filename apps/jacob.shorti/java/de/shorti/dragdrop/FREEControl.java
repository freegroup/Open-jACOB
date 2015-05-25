package de.shorti.dragdrop;


import java.awt.*;
import java.util.*;
import javax.swing.JComponent;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObject, FREEDocument, FREEView

public abstract class FREEControl extends FREEObject
{

    public FREEControl()
    {
        m_nap = null;
    }

    public FREEControl(Rectangle rectangle)
    {
        super(rectangle);
        m_nap = null;
    }

    public FREEControl(Point point, Dimension dimension)
    {
        super(point, dimension);
        m_nap = null;
    }

    protected void ownerChange(Object obj, Object obj1)
    {
        if(obj != null && obj1 == null && (obj instanceof FREEDocument))
        {
            FREEDocument document = (FREEDocument)obj;
            for(Iterator iterator = getIterator(); iterator.hasNext();)
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                FREEView view2 = (FREEView)entry.getKey();
                JComponent jcomponent2 = (JComponent)entry.getValue();
                if(view2 != null && jcomponent2 != null)
                    view2.getCanvas().remove(jcomponent2);
            }

            _mth0126().clear();
        } else
        if(obj == null && obj1 != null && (obj1 instanceof FREEDocument) && isVisible())
        {
            FREEDocument document1 = (FREEDocument)obj1;
            Vector vector = document1._mth011C();
            for(int i = 0; i < vector.size(); i++)
            {
                Object obj2 = vector.get(i);
                if(obj2 instanceof FREEView)
                {
                    FREEView view3 = (FREEView)obj2;
                    JComponent jcomponent3;
                    if(view3 != null)
                        jcomponent3 = getComponent(view3);
                }
            }

        } else
        if(obj != null && obj1 == null && (obj instanceof FREEView))
        {
            FREEView view = (FREEView)obj;
            if(_mth0126().get(view) != null)
            {
                JComponent jcomponent = (JComponent)_mth0126().remove(view);
                if(jcomponent != null)
                    view.getCanvas().remove(jcomponent);
            }
        } else
        if(obj == null && obj1 != null && (obj1 instanceof FREEView) && isVisible())
        {
            FREEView view1 = (FREEView)obj1;
            JComponent jcomponent1 = getComponent(view1);
        }
    }

    public JComponent getComponent(FREEView view)
    {
        JComponent jcomponent = (JComponent)_mth0126().get(view);
        if(jcomponent == null)
        {
            jcomponent = createComponent(view);
            if(jcomponent != null)
            {
                _mth0126().put(view, jcomponent);
                view.getCanvas().add(jcomponent);
            }
        }
        if(jcomponent != null)
        {
            jcomponent.setVisible(true);
            jcomponent.repaint();
        }
        return jcomponent;
    }

    public abstract JComponent createComponent(FREEView view);

    public void setVisible(boolean flag)
    {
        if(flag != isVisible())
        {
            for(Iterator iterator = getIterator(); iterator.hasNext();)
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                FREEView view = (FREEView)entry.getKey();
                JComponent jcomponent = (JComponent)entry.getValue();
                if(view != null)
                    if(flag && jcomponent == null)
                        getComponent(view);
                    else
                    if(!flag && jcomponent != null)
                    {
                        view.getCanvas().remove(jcomponent);
                        _mth0126().put(view, null);
                    }
            }

        }
        super.setVisible(flag);
    }

    protected void geometryChange(Rectangle rectangle)
    {
        super.geometryChange(rectangle);
        Rectangle rectangle1 = new Rectangle();
        for(Iterator iterator = getIterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            FREEView view = (FREEView)entry.getKey();
            JComponent jcomponent = (JComponent)entry.getValue();
            if(view != null && jcomponent != null)
            {
                rectangle1.setBounds(getBoundingRect());
                view.convertDocToView(rectangle1);
                jcomponent.setBounds(rectangle1);
                jcomponent.repaint();
            }
        }

    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        JComponent jcomponent = getComponent(view);
        if(jcomponent != null)
        {
            Rectangle rectangle = view.E5();
            rectangle.setBounds(getBoundingRect());
            view.convertDocToView(rectangle);
            jcomponent.setBounds(rectangle);
            jcomponent.repaint();
        }
    }

    public Iterator getIterator()
    {
        return _mth0126().entrySet().iterator();
    }

    HashMap _mth0126()
    {
        if(m_nap == null)
            m_nap = new HashMap();
        return m_nap;
    }

    private transient HashMap m_nap;
}
