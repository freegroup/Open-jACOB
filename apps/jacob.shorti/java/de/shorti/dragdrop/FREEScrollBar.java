//
//
//
// Source File Name:   com/nwoods//FREEScrollBar

package de.shorti.dragdrop;



import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JScrollBar;

// Referenced classes of package de.shorti.dragdrop:
//            FREEControl, FREEView, FREEObject, FREECopyEnvironment

public class FREEScrollBar extends FREEControl
{

    public FREEScrollBar()
    {
        m_vertical = true;
        m_value = 0;
        m_extent = 10;
        m_ninimum = 0;
        m_naximum = 100;
        m_unitIncrement = 1;
        m_blockIncrement = 9;
        _fld0153 = false;
    }

    public FREEScrollBar(Rectangle rectangle, boolean flag)
    {
        super(rectangle);
        m_vertical = true;
        m_value = 0;
        m_extent = 10;
        m_ninimum = 0;
        m_naximum = 100;
        m_unitIncrement = 1;
        m_blockIncrement = 9;
        _fld0153 = false;
        m_vertical = flag;
    }

    public FREEScrollBar(Point point, Dimension dimension, boolean flag)
    {
        super(point, dimension);
        m_vertical = true;
        m_value = 0;
        m_extent = 10;
        m_ninimum = 0;
        m_naximum = 100;
        m_unitIncrement = 1;
        m_blockIncrement = 9;
        _fld0153 = false;
        m_vertical = flag;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREEScrollBar scrollbar = (FREEScrollBar)super.copyObject(copyEnv);
        if(scrollbar != null)
        {
            scrollbar.m_vertical = m_vertical;
            scrollbar.m_value = m_value;
            scrollbar.m_extent = m_extent;
            scrollbar.m_ninimum = m_ninimum;
            scrollbar.m_naximum = m_naximum;
            scrollbar.m_unitIncrement = m_unitIncrement;
            scrollbar.m_blockIncrement = m_blockIncrement;
        }
        return scrollbar;
    }

    public JComponent createComponent(FREEView view)
    {
        JScrollBar jscrollbar = new JScrollBar(isVertical() ? 1 : 0);
        jscrollbar.setValues(getValue(), getExtent(), getMinimum(), getMaximum());
        final FREEView final_view = view;
        final FREEScrollBar final_scrollbar1 = this;
        jscrollbar.addAdjustmentListener(new AdjustmentListener()
        {
            public void adjustmentValueChanged(AdjustmentEvent adjustmentevent)
            {
                if(!FREEScrollBar.D1(_fld0140) && adjustmentevent.getAdjustmentType() == 5)
                {
                    int i = adjustmentevent.getValue();
                    _fld0140.valueChanged(i, _fld014F);
                }
            }

            private final FREEScrollBar _fld0140 = final_scrollbar1;
            private final FREEView      _fld014F = final_view;
        });
        new DropTarget(jscrollbar, new DropTargetListener() {

            public void dragEnter(DropTargetDragEvent droptargetdragevent)
            {
                _fld014F.onDragEnter(_mth0150(droptargetdragevent));
            }

            public void dragOver(DropTargetDragEvent droptargetdragevent)
            {
                _fld014F.onDragOver(_mth0150(droptargetdragevent));
            }

            public void dropActionChanged(DropTargetDragEvent droptargetdragevent)
            {
                _fld014F.onDropActionChanged(_mth0150(droptargetdragevent));
            }

            public void dragExit(DropTargetEvent droptargetevent)
            {
                _fld014F.onDragExit(droptargetevent);
            }

            public void drop(DropTargetDropEvent droptargetdropevent)
            {
                _fld014F.onDrop(_mth0151(droptargetdropevent));
            }

            DropTargetDragEvent _mth0150(DropTargetDragEvent droptargetdragevent)
            {
                return new DropTargetDragEvent(droptargetdragevent.getDropTargetContext(), _mth0152(droptargetdragevent.getLocation()), droptargetdragevent.getDropAction(), droptargetdragevent.getSourceActions());
            }

            DropTargetDropEvent _mth0151(DropTargetDropEvent droptargetdropevent)
            {
                return new DropTargetDropEvent(droptargetdropevent.getDropTargetContext(), _mth0152(droptargetdropevent.getLocation()), droptargetdropevent.getDropAction(), droptargetdropevent.getSourceActions());
            }

            Point _mth0152(Point point)
            {
                Point point1 = _fld014F.docToViewCoords(_fld0140.getTopLeft());
                point1.translate(point.x, point.y);
                return point1;
            }

            private final FREEView _fld014F = final_view;
            private final FREEScrollBar _fld0140 = FREEScrollBar.this;
        });
        return jscrollbar;
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
            if(jcomponent instanceof JScrollBar)
            {
                JScrollBar jscrollbar = (JScrollBar)jcomponent;
                jscrollbar.setValueIsAdjusting(true);
                jscrollbar.setValueIsAdjusting(false);
            } else
            {
                jcomponent.repaint();
            }
        }
    }

    public boolean isVertical()
    {
        return m_vertical;
    }

    public void setVertical(boolean flag)
    {
        if(isVertical() != flag)
        {
            m_vertical = flag;
            _fld0153 = true;
            JScrollBar jscrollbar;
            for(Iterator iterator = getIterator(); iterator.hasNext(); jscrollbar.setValueIsAdjusting(false))
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                FREEView view = (FREEView)entry.getKey();
                jscrollbar = (JScrollBar)entry.getValue();
                jscrollbar.setOrientation(isVertical() ? 1 : 0);
                jscrollbar.setValueIsAdjusting(true);
            }

            _fld0153 = false;
            update(1004);
        }
    }

    public void setValues(int i, int j, int k, int l, int i1, int j1)
    {
        if(getValue() != i || getExtent() != j || getMinimum() != k || getMaximum() != l || getUnitIncrement() != i1 || getBlockIncrement() != j1)
        {
            m_value = i;
            m_extent = j;
            m_ninimum = k;
            m_naximum = l;
            _fld0153 = true;
            JScrollBar jscrollbar;
            for(Iterator iterator = getIterator(); iterator.hasNext(); jscrollbar.setValueIsAdjusting(false))
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                FREEView view = (FREEView)entry.getKey();
                jscrollbar = (JScrollBar)entry.getValue();
                jscrollbar.setValues(i, j, k, l);
                jscrollbar.setUnitIncrement(i1);
                jscrollbar.setBlockIncrement(j1);
                jscrollbar.setValueIsAdjusting(true);
            }

            _fld0153 = false;
            update(1002);
        }
    }

    public int getValue()
    {
        return m_value;
    }

    public int getExtent()
    {
        return m_extent;
    }

    public int getMinimum()
    {
        return m_ninimum;
    }

    public int getMaximum()
    {
        return m_naximum;
    }

    public int getUnitIncrement()
    {
        return m_unitIncrement;
    }

    public int getBlockIncrement()
    {
        return m_blockIncrement;
    }

    public void valueChanged(int i, FREEView view)
    {
        m_value = i;
        FREEArea area = getParent();
        if(area != null)
            area.update(1003);
    }

    static boolean D1(FREEScrollBar scrollbar)
    {
        return scrollbar._fld0153;
    }

    public static final int ChangedScrollBar = 1002;
    public static final int ChangedScrollBarValue = 1003;
    public static final int ChangedScrollBarVertical = 1004;
    private boolean m_vertical;
    private int m_value;
    private int m_extent;
    private int m_ninimum;
    private int m_naximum;
    private int m_unitIncrement;
    private int m_blockIncrement;
    private transient boolean _fld0153;
}
