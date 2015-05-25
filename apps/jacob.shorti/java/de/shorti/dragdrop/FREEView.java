package de.shorti.dragdrop;


import java.applet.Applet;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.Border;


public class FREEView extends JComponent
    implements FREEObjectCollection, Printable, ClipboardOwner, Autoscroll, DragGestureListener, DragSourceListener, DropTargetListener, FREEDocumentListener, KeyListener
{
    private static int D5 = 10 + (int)Math.rint(Math.random() * 30D);
    private static Method D6 = null;
    private static String D8;
    private static String D9;
    public static final int EventMouseDown = 1;
    public static final int EventMouseMove = 2;
    public static final int EventMouseUp = 3;
    public static final int DebugEvents = 1;
    private FREEViewCanvas m_canvas;
    private JScrollBar DB;
    private JScrollBar DC;
    private JComponent DD;
    private Frame        m_frame;
    private FREEDocument m_document;
    private FREEObjList   m_objects;
    private Point DF;
    private double scale;
    private double E1;
    private Insets E2;
    private boolean E3;
    private boolean mouseEnabled;
    private transient int E5;
    private transient Point E6;
    private boolean E7;
    private transient Point E8;
    private transient MouseEvent E9;
    private transient Point EA;
    private transient int EB;
    private transient FREEObject EC;
    private int m_mouseAction;
    private transient FREESelection EE;
    private transient FREEObject EF;
    private boolean F0;
    private transient boolean F1;
    private transient boolean F2;
    private transient DragSource F3;
    private transient DragGestureRecognizer F4;
    private transient DropTarget F5;
    private transient Point F6;
    private boolean keyEnabled;
    private FREESelection F9;
    private Color primarySelectionColor;
    private Color secondarySelectionColor;
    private transient boolean FC;
    private Cursor m_defaultCursor;
    private transient FREEPort FE;
    private transient FREEPort FF;
    private transient FREELink _fld0100;
    private transient Rectangle _fld0101;
    private transient boolean _fld0102;
    private transient Rectangle _fld0103;
    private transient Point _fld0104;
    private transient FREETextEdit m_textEdit;
    private transient boolean _fld0105;
    private transient Dimension _fld0106;
    private transient java.awt.geom.Rectangle2D.Double _fld0107;
    private transient int _fld0108;
    private transient int _fld0109;
    private transient double _fld010A;
    private transient double _fld010B;
    private transient Vector _fld010C;
    private transient FREEViewEvent _fld010D;
    private transient int _fld010E;
    private transient FontRenderContext _fld010F;
    private transient int _fld0110[];
    private transient int _fld0111[];
    private transient Point _fld0112;
    private transient Dimension _fld0113;
    private transient Rectangle _fld0114;

    class XORRectangle extends FREERectangle
    {

        public void paint(Graphics2D graphics2d, FREEView view)
        {
            graphics2d.setXORMode(Color.white);
            super.paint(graphics2d, view);
            graphics2d.setPaintMode();
        }

        protected void gainedSelection(FREESelection selection)
        {
        }

        protected void lostSelection(FREESelection selection)
        {
        }

        public XORRectangle()
        {
        }

        public XORRectangle(Rectangle rectangle)
        {
            super(rectangle);
        }
    }

    class XORStroke extends FREEStroke
    {

        public void paint(Graphics2D graphics2d, FREEView view)
        {
            graphics2d.setXORMode(Color.white);
            super.paint(graphics2d, view);
            graphics2d.setPaintMode();
        }

        protected void gainedSelection(FREESelection selection)
        {
        }

        protected void lostSelection(FREESelection selection)
        {
        }

        public XORStroke()
        {
        }
    }

    public class FREEViewCanvas extends JComponent
        implements Autoscroll, MouseListener, MouseMotionListener, DragGestureListener, DragSourceListener, DropTargetListener
    {

        public void setBorder(Border border)
        {
        }

        protected void paintComponent(Graphics g)
        {
            m_view.draw(g);
        }

        protected void paintBorder(Graphics g)
        {
        }

        public void mouseClicked(MouseEvent mouseevent)
        {
        }

        public void mousePressed(MouseEvent mouseevent)
        {
            m_view.onMousePressed(mouseevent);
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            m_view.onMouseReleased(mouseevent);
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
        }

        public void mouseExited(MouseEvent mouseevent)
        {
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
            m_view.onMouseMoved(mouseevent);
        }

        public void mouseDragged(MouseEvent mouseevent)
        {
            m_view.onMouseDragged(mouseevent);
        }

        public void dragGestureRecognized(DragGestureEvent draggestureevent)
        {
            m_view.onDragGestureRecognized(draggestureevent);
        }

        public void dragEnter(DragSourceDragEvent dragsourcedragevent)
        {
            m_view.onDragEnter(dragsourcedragevent);
        }

        public void dragOver(DragSourceDragEvent dragsourcedragevent)
        {
            m_view.onDragOver(dragsourcedragevent);
        }

        public void dropActionChanged(DragSourceDragEvent dragsourcedragevent)
        {
            m_view.onDropActionChanged(dragsourcedragevent);
        }

        public void dragExit(DragSourceEvent dragsourceevent)
        {
            m_view.onDragExit(dragsourceevent);
        }

        public void dragDropEnd(DragSourceDropEvent dragsourcedropevent)
        {
            m_view.onDragDropEnd(dragsourcedropevent);
        }

        public void dragEnter(DropTargetDragEvent droptargetdragevent)
        {
            m_view.onDragEnter(droptargetdragevent);
        }

        public void dragOver(DropTargetDragEvent droptargetdragevent)
        {
            m_view.onDragOver(droptargetdragevent);
        }

        public void dropActionChanged(DropTargetDragEvent droptargetdragevent)
        {
            m_view.onDropActionChanged(droptargetdragevent);
        }

        public void dragExit(DropTargetEvent droptargetevent)
        {
            m_view.onDragExit(droptargetevent);
        }

        public void drop(DropTargetDropEvent droptargetdropevent)
        {
            m_view.onDrop(droptargetdropevent);
        }

        public Cursor getCursor()
        {
            return m_view.getCursor();
        }

        public Insets getAutoscrollInsets()
        {
            return m_view.getAutoscrollInsets();
        }

        public void autoscroll(Point point)
        {
            m_view.autoscroll(point);
        }

        public String getToolTipText(MouseEvent mouseevent)
        {
            return m_view.getToolTipText(mouseevent);
        }

        public FREEView m_view;

        public FREEViewCanvas(FREEView view1)
        {
            setOpaque(true);
            m_view = view1;
        }
    }


    public FREEView()
    {
        m_objects = new FREEObjList(true);
        DF = new Point();
        scale = 1.0D;
        E1 = 1.0D;
        E2 = new Insets(12, 12, 12, 12);
        E3 = false;
        mouseEnabled = false;
        E5 = 0;
        E6 = null;
        E7 = false;
        E8 = new Point();
        E9 = null;
        EA = new Point();
        m_mouseAction = DnDConstants.ACTION_COPY_OR_MOVE;
        EE = null;
        EF = null;
        F0 = false;
        F1 = false;
        F2 = false;
        F3 = null;
        F4 = null;
        F5 = null;
        F6 = new Point();
        keyEnabled = false;
        primarySelectionColor = null;
        secondarySelectionColor = null;
        FC = true;
        FE = null;
        FF = null;
        _fld0100 = null;
        _fld0101 = new Rectangle();
        _fld0102 = false;
        _fld0103 = new Rectangle();
        _fld0104 = new Point();
        m_textEdit = null;
        _fld0105 = false;
        _fld0108 = 1;
        _fld0109 = 1;
        _fld010A = 1.0D;
        _fld010B = 1.0D;
        _fld010C = new Vector();
        _fld010D = null;
        _fld010E = 0;
        _fld010F = null;
        _fld0110 = new int[100];
        _fld0111 = new int[100];
        _fld0112 = new Point();
        _fld0113 = new Dimension();
        _fld0114 = new Rectangle();
        C0(createDefaultModel());
    }

    public FREEView(FREEDocument document)
    {
        m_objects = new FREEObjList(true);
        DF = new Point();
        scale = 1.0D;
        E1 = 1.0D;
        E2 = new Insets(12, 12, 12, 12);
        E3 = false;
        mouseEnabled = false;
        E5 = 0;
        E6 = null;
        E7 = false;
        E8 = new Point();
        E9 = null;
        EA = new Point();
        m_mouseAction = DnDConstants.ACTION_COPY_OR_MOVE;
        EE = null;
        EF = null;
        F0 = false;
        F1 = false;
        F2 = false;
        F3 = null;
        F4 = null;
        F5 = null;
        F6 = new Point();
        keyEnabled = false;
        primarySelectionColor = null;
        secondarySelectionColor = null;
        FC = true;
        FE = null;
        FF = null;
        _fld0100 = null;
        _fld0101 = new Rectangle();
        _fld0102 = false;
        _fld0103 = new Rectangle();
        _fld0104 = new Point();
        m_textEdit = null;
        _fld0105 = false;
        _fld0108 = 1;
        _fld0109 = 1;
        _fld010A = 1.0D;
        _fld010B = 1.0D;
        _fld010C = new Vector();
        _fld010D = null;
        _fld010E = 0;
        _fld010F = null;
        _fld0110 = new int[100];
        _fld0111 = new int[100];
        _fld0112 = new Point();
        _fld0113 = new Dimension();
        _fld0114 = new Rectangle();
        C0(document);
    }

    private final void C0(FREEDocument document)
    {
        FREEGlobal.setComponent(this);
        F9 = createDefaultSelection();
        setLayout(null);
        m_canvas = new FREEViewCanvas(this);
        add(m_canvas);
        JScrollBar jscrollbar = new JScrollBar(1);
        jscrollbar.setSize(jscrollbar.getPreferredSize());
        jscrollbar.setUnitIncrement(50);
        setVerticalScrollBar(jscrollbar);
        JScrollBar jscrollbar1 = new JScrollBar(0);
        jscrollbar1.setSize(jscrollbar1.getPreferredSize());
        jscrollbar1.setUnitIncrement(50);
        setHorizontalScrollBar(jscrollbar1);
        JPanel jpanel = new JPanel();
        jpanel.setSize(jscrollbar.getWidth(), jscrollbar1.getHeight());
        setCorner(jpanel);
        setPreferredSize(new Dimension(400, 400));
        validate();
        setDocument(document);
        setKeyEnabled(true);
        initializeMouseHandling();
    }

    public Frame getFrame()
    {
        if(m_frame == null)
        {
            Object obj;
            for(obj = this; obj != null && !(obj instanceof Applet) && !(obj instanceof Frame); obj = ((Component) (obj)).getParent());
            if(obj != null && (obj instanceof Frame))
            {
                m_frame = (Frame)obj;
                FREEGlobal.setComponent(this);
            }
        }
        return m_frame;
    }

    public void addNotify()
    {
        super.addNotify();
        m_frame = null;
        if(getFrame() != null)
            initializeDragDropHandling();
    }

    public FREEViewCanvas getCanvas()
    {
        return m_canvas;
    }

    public JScrollBar getVerticalScrollBar()
    {
        return DB;
    }

    public void setVerticalScrollBar(JScrollBar jscrollbar)
    {
        if(DB != jscrollbar)
        {
            if(DB != null)
                remove(DB);
            DB = jscrollbar;
            if(DB != null)
            {
                add(DB);
                DB.addMouseListener(new MouseAdapter() {

                    public void mousePressed(MouseEvent mouseevent)
                    {
                        if(!isMouseEnabled())
                            return;
                        if(!hasFocus())
                            requestFocus();
                    }

                });
                DB.addAdjustmentListener(new AdjustmentListener() {

                    public void adjustmentValueChanged(AdjustmentEvent adjustmentevent)
                    {
                        onScrollEvent(adjustmentevent);
                    }
                });
            }
            validate();
        }
    }

    public JScrollBar getHorizontalScrollBar()
    {
        return DC;
    }

    public void setHorizontalScrollBar(JScrollBar jscrollbar)
    {
        if(DC != jscrollbar)
        {
            if(DC != null)
                remove(DC);
            DC = jscrollbar;
            if(DC != null)
            {
                add(DC);
                DC.addMouseListener(new MouseAdapter() {

                    public void mousePressed(MouseEvent mouseevent)
                    {
                        if(!isMouseEnabled())
                            return;
                        if(!hasFocus())
                            requestFocus();
                    }

                });
                DC.addAdjustmentListener(new AdjustmentListener() {

                    public void adjustmentValueChanged(AdjustmentEvent adjustmentevent)
                    {
                        onScrollEvent(adjustmentevent);
                    }
                });
            }
            validate();
        }
    }

    public JComponent getCorner()
    {
        return DD;
    }

    public void setCorner(JComponent jcomponent)
    {
        if(DD != jcomponent)
        {
            if(DD != null)
                remove(DD);
            DD = jcomponent;
            if(DD != null)
                add(DD);
            validate();
        }
    }

    public FREEDocument createDefaultModel()
    {
        return new FREEDocument();
    }

    public FREEDocument getDocument()
    {
        return m_document;
    }

    public void setDocument(FREEDocument document)
    {
        if(document == null)
            return;
        if(document != getDocument())
        {
            if(getDocument() != null)
            {
                doCancelMouse();
                doEndEdit();
                getSelection().clearSelection();
                for(FREEListPosition listposition = getFirstObjectPos(); listposition != null; listposition = getFirstObjectPos())
                {
                    FREEObject object = getObjectAtPos(listposition);
                    removeObjectAtPos(listposition);
                }

                setViewPosition(0, 0);
                getDocument().removeDocumentListener(this);
            }
            m_document = document;
            getDocument().addDocumentListener(this);
            fireUpdate(1, 0, null);
        }
    }

    public Dimension getDocumentSize()
    {
        FREEDocument document = getDocument();
        if(document != null)
        {
            Dimension dimension = document.getDocumentSize();
            return new Dimension(dimension.width + 2 * FREEHandle.getHandleWidth(), dimension.height + 2 * FREEHandle.getHandleHeight());
        } else
        {
            return new Dimension();
        }
    }

    public void convertViewToDoc(Point point)
    {
        Point point1 = getViewPosition();
        point.x = (int)((double)point.x / getScale()) + point1.x;
        point.y = (int)((double)point.y / D6()) + point1.y;
    }

    public void convertViewToDoc(Dimension dimension)
    {
        dimension.width = (int)Math.ceil((double)dimension.width / getScale());
        dimension.height = (int)Math.ceil((double)dimension.height / D6());
    }

    public void convertViewToDoc(Rectangle rectangle)
    {
        Point point = getViewPosition();
        rectangle.x = (int)((double)rectangle.x / getScale()) + point.x;
        rectangle.y = (int)((double)rectangle.y / D6()) + point.y;
        rectangle.width = (int)Math.ceil((double)rectangle.width / getScale());
        rectangle.height = (int)Math.ceil((double)rectangle.height / D6());
    }

    public final Point viewToDocCoords(Point point)
    {
        Point point1 = new Point(point);
        convertViewToDoc(point1);
        return point1;
    }

    public final Dimension viewToDocCoords(Dimension dimension)
    {
        Dimension dimension1 = new Dimension(dimension);
        convertViewToDoc(dimension1);
        return dimension1;
    }

    public final Rectangle viewToDocCoords(Rectangle rectangle)
    {
        Rectangle rectangle1 = new Rectangle(rectangle);
        convertViewToDoc(rectangle1);
        return rectangle1;
    }

    public final Rectangle viewToDocCoords(Point point, Dimension dimension)
    {
        Rectangle rectangle = new Rectangle(point, dimension);
        convertViewToDoc(rectangle);
        return rectangle;
    }

    public final Rectangle viewToDocCoords(int i, int j, int k, int l)
    {
        Rectangle rectangle = new Rectangle(i, j, k, l);
        convertViewToDoc(rectangle);
        return rectangle;
    }

    public void convertDocToView(Point point)
    {
        Point point1 = getViewPosition();
        point.x = (int)((double)(point.x - point1.x) * getScale());
        point.y = (int)((double)(point.y - point1.y) * D6());
    }

    public void convertDocToView(Dimension dimension)
    {
        dimension.width = (int)Math.ceil((double)dimension.width * getScale());
        dimension.height = (int)Math.ceil((double)dimension.height * D6());
    }

    public void convertDocToView(Rectangle rectangle)
    {
        Point point = getViewPosition();
        rectangle.x = (int)((double)(rectangle.x - point.x) * getScale());
        rectangle.y = (int)((double)(rectangle.y - point.y) * D6());
        rectangle.width = (int)Math.ceil((double)rectangle.width * getScale());
        rectangle.height = (int)Math.ceil((double)rectangle.height * D6());
    }

    public final Point docToViewCoords(Point point)
    {
        Point point1 = new Point(point);
        convertDocToView(point1);
        return point1;
    }

    public final Dimension docToViewCoords(Dimension dimension)
    {
        Dimension dimension1 = new Dimension(dimension);
        convertDocToView(dimension1);
        return dimension1;
    }

    public final Rectangle docToViewCoords(Rectangle rectangle)
    {
        Rectangle rectangle1 = new Rectangle(rectangle);
        convertDocToView(rectangle1);
        return rectangle1;
    }

    public final Rectangle docToViewCoords(Point point, Dimension dimension)
    {
        Rectangle rectangle = new Rectangle(point, dimension);
        convertDocToView(rectangle);
        return rectangle;
    }

    public final Rectangle docToViewCoords(int i, int j, int k, int l)
    {
        Rectangle rectangle = new Rectangle(i, j, k, l);
        convertDocToView(rectangle);
        return rectangle;
    }

    public final void setViewPosition(Point point)
    {
        setViewPosition(point.x, point.y);
    }

    public void setViewPosition(int i, int j)
    {
        Point point = getViewPosition();
        if(point.x != i || point.y != j)
        {
            Point point1 = new Point(point);
            DF.x = i;
            DF.y = j;
            fireUpdate(7, 0, point1);
        }
    }

    public Point getViewPosition()
    {
        return DF;
    }

    public Dimension getExtentSize()
    {
        int i = getCanvas().getWidth();
        int j = getCanvas().getHeight();
        return new Dimension((int)((double)i / getScale()), (int)((double)j / D6()));
    }

    public Rectangle getViewRect()
    {
        return new Rectangle(getViewPosition(), getExtentSize());
    }

    public void scrollRectToVisible(Rectangle rectangle)
    {
        Rectangle rectangle1 = getViewRect();
        if(rectangle1.contains(rectangle))
            return;
        if(!rectangle1.contains(rectangle.getLocation()))
        {
            Dimension dimension = getExtentSize();
            int i;
            if(rectangle.width < dimension.width)
                i = (rectangle.x + rectangle.width) - dimension.width;
            else
                i = rectangle.x;
            if(i < 0)
                i = 0;
            int j;
            if(rectangle.height < dimension.height)
                j = (rectangle.y + rectangle.height) - dimension.height;
            else
                j = rectangle.y;
            if(j < 0)
                j = 0;
            setViewPosition(i, j);
        }
    }

    public void setScale(double d)
    {
        if(d < 0.05)
            d = 0.05;
        if(d > 10D)
            d = 10D;
        if(getScale() != d || D6() != d)
        {
            double d1 = scale;
            scale = d;
            E1 = d;
            fireUpdate(8, 0, new Double(d1));
        }
    }

    public double getScale()
    {
        return scale;
    }

    private final double D6()
    {
        return E1;
    }

    public FREEObject pickDocObject(Point point, boolean flag)
    {
        for(FREELayer layer = getLastLayer(); layer != null; layer = getPrevLayer(layer))
            if(layer.isVisible())
            {
                FREEObject object = layer.pickObject(point, flag);
                if(object != null)
                    return object;
            }

        return null;
    }

    public FREEObject pickObject(Point point, boolean flag)
    {
        for(FREEListPosition listposition = getLastObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getPrevObjectPos(listposition);
            if(object.isVisible() && object.isPointInObj(point))
            {
                if(object instanceof FREEArea)
                {
                    FREEObject object1 = ((FREEArea)object).pickObject(point, flag);
                    if(object1 != null)
                        return object1;
                }
                if(!flag)
                    return object;
                if(object.isSelectable())
                    return object;
            }
        }

        return null;
    }

    public int getNumObjects()
    {
        return m_objects.getNumObjects();
    }

    public boolean isEmpty()
    {
        return m_objects.isEmpty();
    }

    public FREEListPosition addObjectAtHead(FREEObject object)
    {
        if(object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getLayer() != null)
            return null;
        if(object.getView() != null)
        {
            if(object.getView() != this)
            {
                return null;
            } else
            {
                m_objects.removeObject(object);
                FREEListPosition listposition = m_objects.addObjectAtHead(object);
                object.update(10);
                return listposition;
            }
        } else
        {
            FREEListPosition listposition1 = m_objects.addObjectAtHead(object);
            object.C3(this);
            return listposition1;
        }
    }

    public FREEListPosition addObjectAtTail(FREEObject object)
    {
        if(object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getLayer() != null)
            return null;
        if(object.getView() != null)
        {
            if(object.getView() != this)
            {
                return null;
            } else
            {
                m_objects.removeObject(object);
                FREEListPosition listposition = m_objects.addObjectAtTail(object);
                object.update(10);
                return listposition;
            }
        } else
        {
            FREEListPosition listposition1 = m_objects.addObjectAtTail(object);
            object.C3(this);
            return listposition1;
        }
    }

    public FREEListPosition insertObjectBefore(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getLayer() != null)
            return null;
        if(object.getView() != null)
        {
            if(object.getView() != this)
                return null;
            FREEListPosition listposition1 = m_objects.findObject(object);
            if(listposition1 != null)
            {
                m_objects.removeObjectAtPos(listposition1);
                FREEListPosition listposition3 = m_objects.insertObjectBefore(listposition, object);
                object.update(10);
                return listposition3;
            }
        }
        FREEListPosition listposition2 = m_objects.insertObjectBefore(listposition, object);
        object.C3(this);
        return listposition2;
    }

    public FREEListPosition insertObjectAfter(FREEListPosition listposition, FREEObject object)
    {
        if(listposition == null || object == null)
            return null;
        if(object.getParent() != null)
            return null;
        if(object.getLayer() != null)
            return null;
        if(object.getView() != null)
        {
            if(object.getView() != this)
                return null;
            FREEListPosition listposition1 = m_objects.findObject(object);
            if(listposition1 != null)
            {
                m_objects.removeObjectAtPos(listposition1);
                FREEListPosition listposition3 = m_objects.insertObjectAfter(listposition, object);
                object.update(10);
                return listposition3;
            }
        }
        FREEListPosition listposition2 = m_objects.insertObjectAfter(listposition, object);
        object.C3(this);
        return listposition2;
    }

    public void bringObjectToFront(FREEObject object)
    {
        addObjectAtTail(object);
    }

    public void sendObjectToBack(FREEObject object)
    {
        addObjectAtHead(object);
    }

    public void removeObject(FREEObject object)
    {
        if(object == null)
            return;
        if(object.getView() != this)
            return;
        FREEArea area = object.getParent();
        if(area != null)
        {
            area.removeObject(object);
        } else
        {
            FREEListPosition listposition = findObject(object);
            if(listposition != null)
                removeObjectAtPos(listposition);
        }
    }

    public FREEObject removeObjectAtPos(FREEListPosition listposition)
    {
        FREEObject object = m_objects.removeObjectAtPos(listposition);
        if(object != null)
            object.C3(null);
        return object;
    }

    public FREEListPosition getFirstObjectPos()
    {
        return m_objects.getFirstObjectPos();
    }

    public FREEListPosition getLastObjectPos()
    {
        return m_objects.getLastObjectPos();
    }

    public FREEListPosition getNextObjectPos(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        Object obj = listposition.obj;
        if(obj instanceof FREEArea)
        {
            FREEArea area = (FREEArea)obj;
            if(!area.isEmpty())
                return area.getFirstObjectPos();
        }
        FREEListPosition listposition1;
        for(listposition = listposition.next; listposition == null; listposition = listposition1.next)
        {
            FREEArea area1 = ((FREEObject) (obj)).getParent();
            if(area1 == null)
                return null;
            listposition1 = area1.C5();
            obj = area1;
        }

        return listposition;
    }

    public FREEListPosition getNextObjectPosAtTop(FREEListPosition listposition)
    {
        if(listposition == null)
            return null;
        for(Object obj = listposition.obj; ((FREEObject) (obj)).getParent() != null; obj = ((FREEObject) (obj)).getParent())
            listposition = ((FREEObject) (obj)).getParent().C5();

        return listposition.next;
    }

    public FREEListPosition getPrevObjectPos(FREEListPosition listposition)
    {
        return m_objects.getPrevObjectPos(listposition);
    }

    public FREEObject getObjectAtPos(FREEListPosition listposition)
    {
        return m_objects.getObjectAtPos(listposition);
    }

    public FREEListPosition findObject(FREEObject object)
    {
        return m_objects.findObject(object);
    }

    public FREESelection createDefaultSelection()
    {
        return new FREESelection(this);
    }

    public FREESelection getSelection()
    {
        return F9;
    }

    public FREEObject selectObject(FREEObject object)
    {
        return getSelection().selectObject(object);
    }

    public void selectAll()
    {
        FREESelection selection = getSelection();
        for(FREELayer layer = getFirstLayer(); layer != null; layer = getNextLayer(layer))
            if(layer.isVisible())
            {
                for(FREEListPosition listposition = layer.getFirstObjectPos(); listposition != null;)
                {
                    FREEObject object = layer.getObjectAtPos(listposition);
                    listposition = layer.getNextObjectPosAtTop(listposition);
                    if(object.isVisible() && (object.isSelectable() || (object instanceof FREEArea) && ((FREEArea)object).isGrabChildSelection() || object.redirectSelection() != object))
                        getSelection().extendSelection(object);
                }

            }

    }

    private boolean D8(Rectangle rectangle, Rectangle rectangle1)
    {
        int i = rectangle.width;
        int j = rectangle.height;
        int k = rectangle1.width;
        int l = rectangle1.height;
        if(i <= 0 || j <= 0 || k < 0 || l < 0)
        {
            return false;
        } else
        {
            int i1 = rectangle.x;
            int j1 = rectangle.y;
            int k1 = rectangle1.x;
            int l1 = rectangle1.y;
            return k1 >= i1 && l1 >= j1 && k1 + k <= i1 + i && l1 + l <= j1 + j;
        }
    }

    public void selectInBox(Rectangle rectangle)
    {
        for(FREELayer layer = getFirstLayer(); layer != null; layer = getNextLayer(layer))
            if(layer.isVisible())
            {
                for(FREEListPosition listposition = layer.getFirstObjectPos(); listposition != null;)
                {
                    FREEObject object = layer.getObjectAtPos(listposition);
                    listposition = layer.getNextObjectPosAtTop(listposition);
                    if(object.isVisible() && D8(rectangle, object.getBoundingRect()) && (object.isSelectable() || (object instanceof FREEArea) && ((FREEArea)object).isGrabChildSelection() || object.redirectSelection() != object))
                        getSelection().extendSelection(object);
                }

            }

    }

    public Color getPrimarySelectionColor()
    {
        if(primarySelectionColor == null)
            return FREEGlobal.getPrimarySelectionColor();
        else
            return primarySelectionColor;
    }

    public void setPrimarySelectionColor(Color color)
    {
        Color color1 = getPrimarySelectionColor();
        if(color == null || !color1.equals(color))
        {
            primarySelectionColor = color;
            fireUpdate(9, 0, color1);
        }
    }

    public Color getSecondarySelectionColor()
    {
        if(secondarySelectionColor == null)
            return FREEGlobal.getSecondarySelectionColor();
        else
            return secondarySelectionColor;
    }

    public void setSecondarySelectionColor(Color color)
    {
        Color color1 = getSecondarySelectionColor();
        if(color == null || !color1.equals(color))
        {
            secondarySelectionColor = color;
            fireUpdate(9, 0, color1);
        }
    }

    final void draw(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        java.awt.geom.AffineTransform affinetransform = graphics2d.getTransform();
        Rectangle rectangle = new Rectangle();
        graphics2d.getClipBounds(rectangle);
        convertViewToDoc(rectangle);
        graphics2d.scale(getScale(), D6());
        Point point = getViewPosition();
        graphics2d.translate(-point.x, -point.y);
        draw(graphics2d, rectangle);
        graphics2d.setTransform(affinetransform);
    }

    final void draw(Graphics2D graphics2d, Rectangle rectangle)
    {
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        paintPaperColor(graphics2d, rectangle);

        paintBackgroundDecoration(graphics2d, rectangle);
        paintDocumentObjects(graphics2d, rectangle);
        paintViewObjects(graphics2d, rectangle);
    }

    protected void paintPaperColor(Graphics2D graphics2d, Rectangle rectangle)
    {
        FREEDocument document = getDocument();
        if(document != null)
        {
            graphics2d.setColor(document.getPaperColor());
            graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width + 5, rectangle.height + 5);
        }
    }

    protected void paintBackgroundDecoration(Graphics2D graphics2d, Rectangle rectangle)
    {
    }

    protected void paintDocumentObjects(Graphics2D graphics2d, Rectangle rectangle)
    {
        Rectangle rectangle1 = new Rectangle();
        int i = 3;
        float f = 1.0F;
        for(FREELayer layer = getFirstLayer(); layer != null; layer = getNextLayer(layer))
            if(layer.isVisible())
            {
                if(layer._mth0123() != i || layer.getTransparency() != f)
                {
                    i = layer._mth0123();
                    f = layer.getTransparency();
                    AlphaComposite alphacomposite = AlphaComposite.getInstance(i, f);
                    graphics2d.setComposite(alphacomposite);
                }
                _fld010F = graphics2d.getFontRenderContext();
                for(FREEListPosition listposition = layer.getFirstObjectPos(); listposition != null;)
                {
                    FREEObject object = layer.getObjectAtPos(listposition);
                    listposition = layer.getNextObjectPosAtTop(listposition);
                    if(object.isVisible())
                    {
                        rectangle1.setBounds(object.getBoundingRect());
                        object.expandRectByPenWidth(rectangle1);
                        if(rectangle1.intersects(rectangle))
                            object.paint(graphics2d, this);
                    }
                }

            }

        if(i != 3 || f != 1)
            graphics2d.setComposite(AlphaComposite.SrcOver);
    }

    protected void paintViewObjects(Graphics2D graphics2d, Rectangle rectangle)
    {
        _fld010F = graphics2d.getFontRenderContext();
        Rectangle rectangle1 = new Rectangle();
        for(FREEListPosition listposition = getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = getObjectAtPos(listposition);
            listposition = getNextObjectPosAtTop(listposition);
            if(object.isVisible())
            {
                rectangle1.setBounds(object.getBoundingRect());
                object.expandRectByPenWidth(rectangle1);
                if(rectangle1.intersects(rectangle))
                    object.paint(graphics2d, this);
            }
        }

    }

    FontRenderContext DB()
    {
        return _fld010F;
    }

    public boolean isOptimizedDrawingEnabled()
    {
        return false;
    }

    public void updateScrollbars()
    {
        JScrollBar jscrollbar = getHorizontalScrollBar();
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if(jscrollbar == null && jscrollbar1 == null)
            return;
        Dimension dimension = getDocumentSize();
        Point point = getViewPosition();
        Insets insets = getInsets();
        E3 = true;
        if(jscrollbar1 != null)
        {
            int i = getHeight() - insets.top - insets.bottom;
            if(jscrollbar != null)
                i -= jscrollbar.getHeight();
            int k = (int)((double)i / D6());
            if(k >= dimension.height && point.y == 0)
            {
                jscrollbar1.setValues(0, dimension.height, 0, dimension.height);
                jscrollbar1.setEnabled(false);
            } else
            {
                jscrollbar1.setEnabled(true);
                jscrollbar1.setValues(point.y, k, 0, dimension.height);
            }
        }
        if(jscrollbar != null)
        {
            int j = getWidth() - insets.left - insets.right;
            if(jscrollbar1 != null)
                j -= jscrollbar1.getWidth();
            int l = (int)((double)j / getScale());
            if(l >= dimension.width && point.x == 0)
            {
                jscrollbar.setValues(0, dimension.width, 0, dimension.width);
                jscrollbar.setEnabled(false);
            } else
            {
                jscrollbar.setEnabled(true);
                jscrollbar.setValues(point.x, l, 0, dimension.width);
            }
        }
        E3 = false;
    }

    public void updateView(Rectangle rectangle)
    {
        FREEViewCanvas viewcanvas = getCanvas();
        if(rectangle.x < viewcanvas.getWidth() && rectangle.y < viewcanvas.getHeight() && rectangle.x + rectangle.width >= 0 && rectangle.y + rectangle.height >= 0)
            viewcanvas.repaint(rectangle);
    }

    public void updateView()
    {
        Rectangle rectangle = new Rectangle(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
        updateView(rectangle);
        updateScrollbars();
    }

    public void addViewListener(FREEViewListener viewlistener)
    {
        _fld010C.addElement(viewlistener);
    }

    public void removeViewListener(FREEViewListener viewlistener)
    {
        _fld010C.removeElement(viewlistener);
    }

    public final void fireUpdate(int i, int j, Object obj)
    {
        fireUpdate(i, j, obj, null, null, 0);
    }

    public void fireUpdate(int i, int j, Object obj, Point point, Point point1, int k)
    {
        switch(i)
        {
        case 1: // '\001'
            updateView();
            break;

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            FREEObject object = (FREEObject)obj;
            Rectangle rectangle = new Rectangle();
            rectangle.setBounds(object.getBoundingRect());
            object.expandRectByPenWidth(rectangle);
            convertDocToView(rectangle);
            rectangle.grow(1, 1);
            updateView(rectangle);
            if(object.C7() && i != 4)
            {
                rectangle.setBounds(object.C1());
                object.expandRectByPenWidth(rectangle);
                convertDocToView(rectangle);
                rectangle.grow(1, 1);
                updateView(rectangle);
            }
            break;

        case 7: // '\007'
            updateView();
            break;

        case 8: // '\b'
            updateView();
            break;

        case 9: // '\t'
            updateView();
            break;

        case 100: // 'd'
            updateView();
            break;
        }
        if(_fld010C.size() > 0)
        {
            if(_fld010D == null)
            {
                _fld010D = new FREEViewEvent(this, i, j, obj, point, point1, k);
            } else
            {
                _fld010D.setHint(i);
                _fld010D.setFlags(j);
                _fld010D._mth0125(obj);
                _fld010D._mth012F(point);
                _fld010D._mth0130(point1);
                _fld010D._mth0131(k);
            }
            FREEViewEvent viewevent = _fld010D;
            _fld010D = null;
            for(int l = 0; l < _fld010C.size(); l++)
            {
                FREEViewListener viewlistener = (FREEViewListener)_fld010C.elementAt(l);
                viewlistener.viewChanged(viewevent);
            }

            _fld010D = viewevent;
            _fld010D._mth0125(null);
        }
    }

    public void documentChanged(FREEDocumentEvent documentevent)
    {
        FREEObject object = documentevent.getFREEObject();
        switch(documentevent.getHint())
        {
        case 8: // '\b'
        case 9: // '\t'
        default:
            break;

        case 1: // '\001'
            getSelection().clearSelectionHandles(null);
            getSelection().restoreSelectionHandles(null);
            updateView();
            break;

        case 3: // '\003'
            Rectangle rectangle = documentevent.E5();
            rectangle.setBounds(object.getBoundingRect());
            object.expandRectByPenWidth(rectangle);
            convertDocToView(rectangle);
            rectangle.grow(1, 1);
            updateView(rectangle);
            if(object.C7())
            {
                rectangle.setBounds(object.C1());
                object.expandRectByPenWidth(rectangle);
                convertDocToView(rectangle);
                rectangle.grow(1, 1);
                updateView(rectangle);
            }
            if(documentevent.getFlags() == 1)
            {
                FREESelection selection = getSelection();
                if(selection.getNumHandles(object) <= 0)
                    break;
                if(object.isVisible())
                    object.showSelectionHandles(selection);
                else
                    object.hideSelectionHandles(selection);
                break;
            }
            if(documentevent.getFlags() != 2)
                break;
            FREESelection selection1 = getSelection();
            if(!selection1.isSelected(object))
                break;
            if(object.isVisible())
                object.showSelectionHandles(selection1);
            else
                object.hideSelectionHandles(selection1);
            break;

        case 2: // '\002'
            Rectangle rectangle1 = documentevent.E5();
            rectangle1.setBounds(object.getBoundingRect());
            object.expandRectByPenWidth(rectangle1);
            convertDocToView(rectangle1);
            rectangle1.grow(1, 1);
            updateView(rectangle1);
            break;

        case 4: // '\004'
            getSelection().clearSelection(object);
            Rectangle rectangle2 = documentevent.E5();
            rectangle2.setBounds(object.getBoundingRect());
            object.expandRectByPenWidth(rectangle2);
            convertDocToView(rectangle2);
            rectangle2.grow(1, 1);
            updateView(rectangle2);
            break;

        case 5: // '\005'
            updateScrollbars();
            break;

        case 6: // '\006'
            updateView();
            break;

        case 7: // '\007'
            updateView();
            break;

        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 13: // '\r'
            updateView();
            break;
        }
    }

    public void print()
    {
        PrinterJob printerjob = PrinterJob.getPrinterJob();
        PageFormat pageformat = printerjob.defaultPage();
        Paper paper = new Paper();
        double d = 36D;
        paper.setImageableArea(d, d, paper.getWidth() - d * 2D, paper.getHeight() - d * 2D);
        pageformat.setPaper(paper);
        printerjob.setPrintable(this, pageformat);
        if(printerjob.printDialog())
        {
            boolean flag = isDoubleBuffered();
            try
            {
                setDoubleBuffered(false);
                printerjob.print();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            setDoubleBuffered(flag);
        }
    }

    protected void printBegin(Graphics2D graphics2d, PageFormat pageformat)
    {
        if(!_fld0105)
        {
            _fld0105 = true;
            _fld0106 = getPrintDocumentSize();
            int i = _fld0106.width;
            int j = _fld0106.height;
            _fld0107 = getPrintPageRect(graphics2d, pageformat);
            double d = _fld0107.width;
            double d1 = _fld0107.height;
            _fld010A = getPrintScale(graphics2d, pageformat);
            _fld010B = _fld010A;
            _fld0108 = (int)Math.ceil(((double)i * _fld010A) / d);
            _fld0109 = (int)Math.ceil(((double)j * _fld010B) / d1);
        }
    }

    public Dimension getPrintDocumentSize()
    {
        FREEDocument document = getDocument();
        Dimension dimension = new Dimension(1, 1);
        if(document == null)
            return dimension;
        Rectangle rectangle = new Rectangle();
        for(FREEListPosition listposition = document.getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = document.getObjectAtPos(listposition);
            listposition = document.getNextObjectPosAtTop(listposition);
            rectangle.setBounds(object.getBoundingRect());
            object.expandRectByPenWidth(rectangle);
            if(rectangle.x + rectangle.width > dimension.width)
                dimension.width = rectangle.x + rectangle.width;
            if(rectangle.y + rectangle.height > dimension.height)
                dimension.height = rectangle.y + rectangle.height;
        }

        return dimension;
    }

    protected java.awt.geom.Rectangle2D.Double getPrintPageRect(Graphics2D graphics2d, PageFormat pageformat)
    {
        return new java.awt.geom.Rectangle2D.Double(pageformat.getImageableX(), pageformat.getImageableY(), pageformat.getImageableWidth(), pageformat.getImageableHeight());
    }

    protected double getPrintScale(Graphics2D graphics2d, PageFormat pageformat)
    {
        return 1.0D;
    }

    protected void printEnd(Graphics2D graphics2d, PageFormat pageformat)
    {
        _fld0105 = false;
    }

    public int print(Graphics g, PageFormat pageformat, int i)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        printBegin(graphics2d, pageformat);
        double d = _fld0107.x;
        double d1 = _fld0107.y;
        double d2 = _fld0107.width;
        double d3 = _fld0107.height;
        if(i >= _fld0108 * _fld0109)
        {
            printEnd(graphics2d, pageformat);
            return 1;
        }
        int j = i % _fld0108;
        int k = i / _fld0108;
        printDecoration(graphics2d, pageformat, j, k);
        Point point = getViewPosition();
        double d4 = getScale();
        double d5 = D6();
        try
        {
            DF = new Point((int)(((double)j * d2) / _fld010A), (int)(((double)k * d3) / _fld010B));
            scale = _fld010A;
            E1 = _fld010B;
            graphics2d.clip(_fld0107);
            Rectangle rectangle = new Rectangle();
            graphics2d.getClipBounds(rectangle);
            rectangle.x -= (int)d;
            rectangle.y -= (int)d1;
            convertViewToDoc(rectangle);
            graphics2d.translate(d, d1);
            graphics2d.scale(getScale(), D6());
            Point point1 = getViewPosition();
            graphics2d.translate(-point1.x, -point1.y);
            printView(graphics2d, rectangle);
        }
        finally
        {
            DF = point;
            scale = d4;
            E1 = d5;
        }
        return 0;
    }

    protected void printDecoration(Graphics2D graphics2d, PageFormat pageformat, int i, int j)
    {
        double d = _fld0107.x;
        double d1 = _fld0107.y;
        double d2 = _fld0107.width;
        double d3 = _fld0107.height;
        double d4 = d + d2;
        double d5 = d1 + d3;
        java.awt.Paint paint = graphics2d.getPaint();
        graphics2d.setPaint(Color.black);
        java.awt.Stroke stroke = graphics2d.getStroke();
        graphics2d.setStroke(new BasicStroke(0.7F));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d1, d + 10D, d1));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d1, d, d1 + 10D));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d1, d4 - 10D, d1));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d1, d4, d1 + 10D));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d5, d + 10D, d5));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d5, d, d5 - 10D));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d5, d4 - 10D, d5));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d5, d4, d5 - 10D));
        graphics2d.setPaint(paint);
        graphics2d.setStroke(stroke);
    }

    protected void printView(Graphics2D graphics2d, Rectangle rectangle)
    {
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        paintBackgroundDecoration(graphics2d, rectangle);
        paintDocumentObjects(graphics2d, rectangle);
    }

    public void doLayout()
    {
        super.doLayout();
        JComponent jcomponent = getCorner();
        JScrollBar jscrollbar = getHorizontalScrollBar();
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        int i = getWidth();
        int j = getHeight();
        Insets insets = getInsets();
        int k = insets.left;
        int l = insets.top;
        int i1 = 0;
        int j1 = 0;
        if(jscrollbar != null)
            j1 = jscrollbar.getHeight();
        if(jscrollbar1 != null)
            i1 = jscrollbar1.getWidth();
        int k1 = i - i1 - insets.right;
        int l1 = j - j1 - insets.bottom;
        int i2 = k1 - k;
        int j2 = l1 - l;
        getCanvas().setBounds(k, l, i2, j2);
        if(jscrollbar != null)
        {
            jscrollbar.setBounds(k, l1, i2, j1);
            int k2 = jscrollbar.getUnitIncrement();
            int i3 = (int)((double)i2 / getScale());
            int k3 = Math.max(k2, i3 - k2);
            jscrollbar.setBlockIncrement(k3);
        }
        if(jscrollbar1 != null)
        {
            jscrollbar1.setBounds(k1, l, i1, j2);
            int l2 = jscrollbar1.getUnitIncrement();
            int j3 = (int)((double)j2 / D6());
            int l3 = Math.max(l2, j3 - l2);
            jscrollbar1.setBlockIncrement(l3);
        }
        if(jcomponent != null)
            if(i1 != 0 && j1 != 0)
            {
                jcomponent.setBounds(k1, l1, i1, j1);
                jcomponent.setVisible(true);
            } else
            {
                jcomponent.setVisible(false);
            }
        updateScrollbars();
    }

    public void setSize(int i, int j)
    {
        super.setSize(i, j);
        validate();
    }

    public final void setSize(Dimension dimension)
    {
        setSize(dimension.width, dimension.height);
    }

    public void setBounds(int i, int j, int k, int l)
    {
        super.setBounds(i, j, k, l);
        validate();
    }

    public final void setBounds(Rectangle rectangle)
    {
        setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Dimension getPreferredSize()
    {
        Dimension dimension = getDocumentSize();
        Dimension dimension1 = getMinimumSize();
        Dimension dimension2 = new Dimension(Math.max(dimension.width, dimension1.width), Math.max(dimension.height, dimension1.height));
        convertDocToView(dimension2);
        return dimension2;
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(50, 50);
    }

    protected void onScrollEvent(AdjustmentEvent adjustmentevent)
    {
        if(E3)
            return;
        Point point = getViewPosition();
        int i = point.x;
        int j = point.y;
        JScrollBar jscrollbar = getHorizontalScrollBar();
        if(jscrollbar != null)
            i = jscrollbar.getValue();
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if(jscrollbar1 != null)
            j = jscrollbar1.getValue();
        setViewPosition(i, j);
    }

    public void setKeyEnabled(boolean flag)
    {
        if(flag && !keyEnabled)
        {
            keyEnabled = true;
            addKeyListener(this);
        } else
        if(!flag && keyEnabled)
        {
            keyEnabled = false;
            removeKeyListener(this);
        }
    }

    public boolean getKeyEnabled()
    {
        return keyEnabled;
    }

    public void keyPressed(KeyEvent keyevent)
    {
        if(getKeyEnabled())
        {
            onKeyEvent(keyevent);
        }
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    protected void onKeyEvent(KeyEvent keyevent)
    {
        int i = keyevent.getKeyCode();
        if(i == 33 || i == 34)
        {
            JScrollBar jscrollbar;
            if(keyevent.isControlDown())
                jscrollbar = getHorizontalScrollBar();
            else
                jscrollbar = getVerticalScrollBar();
            if(jscrollbar == null)
                return;
            byte byte0;
            int j;
            if(i == 33)
            {
                byte0 = -1;
                j = jscrollbar.getMaximum();
            } else
            {
                byte0 = 1;
                j = jscrollbar.getMinimum();
            }
            int k = jscrollbar.getValue();
            int l = jscrollbar.getBlockIncrement(byte0);
            int i1 = k + byte0 * l;
            if(byte0 == 1)
                i1 = Math.max(i1, j);
            else
                i1 = Math.min(i1, j);
            jscrollbar.setValue(i1);
        }
    }

    public boolean isFocusTraversable()
    {
        return true;
    }

    public boolean isIgnoreNextMouseDown()
    {
        return E7;
    }

    public void setIgnoreNextMouseDown(boolean flag)
    {
        E7 = flag;
    }

    protected boolean keyMultipleSelect(int i)
    {
        boolean flag = (i & 0x1) != 0;
        boolean flag1 = (i & 0x2) != 0;
        return flag || flag1;
    }

    protected boolean keyClearSelection(int i)
    {
        return (i & 0x1) == 0;
    }

    protected void initializeMouseHandling()
    {
        getCanvas().setToolTipText("");
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent mouseevent)
            {
                if(!isMouseEnabled())
                    return;
                if(!hasFocus())
                    requestFocus();
            }
        });
        setMouseEnabled(true);
    }

    public void setMouseEnabled(boolean flag)
    {
        if(flag && !mouseEnabled)
        {
            mouseEnabled = true;
            getCanvas().addMouseListener(getCanvas());
            getCanvas().addMouseMotionListener(getCanvas());
        } else
        if(!flag && mouseEnabled)
        {
            mouseEnabled = false;
            getCanvas().removeMouseListener(getCanvas());
            getCanvas().removeMouseMotionListener(getCanvas());
        }
    }

    public boolean isMouseEnabled()
    {
        return mouseEnabled;
    }

    void DC(JInternalFrame jinternalframe)
    {
        Class aclass[] = new Class[0];
        if(D6 == null)
            try
            {
                Class class1 = Class.forName("javax.swing.JInternalFrame");
                D6 = class1.getDeclaredMethod("restoreSubcomponentFocus", aclass);
            }
            catch(Exception exception)
            {
                FREEGlobal.TRACE("*** FREEView: unable to get restoreSubcomponentFocus method");
                exception.printStackTrace();
                D6 = null;
            }
        if(D6 != null)
            try
            {
                D6.invoke(jinternalframe, aclass);
            }
            catch(Exception exception1)
            {
                FREEGlobal.TRACE("*** FREEView: unable to call restoreSubcomponentFocus method");
            }
    }

    protected final MouseEvent getCurrentMouseEvent()
    {
        return E9;
    }

    protected void onMousePressed(MouseEvent mouseevent)
    {
        if(!isMouseEnabled())
            return;
        if(!hasFocus())
        {
            JInternalFrame jinternalframe = null;
            for(Container container = getParent(); container != null; container = container.getParent())
            {
                if(!(container instanceof JInternalFrame))
                    continue;
                jinternalframe = (JInternalFrame)container;
                break;
            }

            if(jinternalframe != null && FREEGlobal.isAtLeastJavaVersion(1.3D))
                DC(jinternalframe);
            requestFocus();
            if(jinternalframe != null && !FREEGlobal.isAtLeastJavaVersion(1.22D))
            {
                setIgnoreNextMouseDown(false);
                return;
            }
        }
        if(isIgnoreNextMouseDown())
        {
            setIgnoreNextMouseDown(false);
            return;
        }
        E9 = mouseevent;
        Point point = mouseevent.getPoint();
        EA.setLocation(point);
        convertViewToDoc(EA);
        doEndEdit();
        if(mouseevent.getClickCount() == 1)
            doMouseDown(mouseevent.getModifiers(), EA, point);
        E9 = null;
    }

    protected void onMouseReleased(MouseEvent mouseevent)
    {
        if(!isMouseEnabled())
            return;
        F1 = false;
        if(!hasFocus())
        {
            E5 = 0;
            return;
        }
        E9 = mouseevent;
        Point point = mouseevent.getPoint();
        EA.setLocation(point);
        convertViewToDoc(EA);
        if(mouseevent.getClickCount() <= 1)
        {
            if(doMouseUp(mouseevent.getModifiers(), EA, point))
                E5 = 0;
        } else
        {
            doMouseDblClick(mouseevent.getModifiers(), EA, point);
        }
        E9 = null;
    }

    protected void onMouseMoved(MouseEvent mouseevent)
    {
        if(!isMouseEnabled())
            return;
        F1 = false;
        if(E5 != 0)
            doCancelMouse();
        if(!hasFocus())
        {
            return;
        } else
        {
            E9 = mouseevent;
            Point point = mouseevent.getPoint();
            EA.setLocation(point);
            convertViewToDoc(EA);
            doMouseMove(mouseevent.getModifiers(), EA, point);
            E9 = null;
            return;
        }
    }

    protected void onMouseDragged(MouseEvent mouseevent)
    {
        if(!isMouseEnabled())
            return;
        if(!hasFocus())
            return;
        if(!getDragDropEnabled())
        {
            E9 = mouseevent;
            Point point = mouseevent.getPoint();
            EA.setLocation(point);
            convertViewToDoc(EA);
            doMouseMove(mouseevent.getModifiers(), EA, point);
            E9 = null;
        } else
        {
            F1 = true;
        }
    }

    public boolean doMouseDown(int i, Point point, Point point1)
    {
        E6 = point;
        if((i & 0x10) != 0 && getDocument() != null && getDocument().isModifiable())
        {
            FREEHandle handle = pickHandle(point);
            if(handle != null && startResizing(handle, point, point1))
                return true;
            FREEPort port = pickPort(point);
            if(port != null && startNewLink(port, point))
                return true;
        }
        EC = pickDocObject(point, true);
        if(EC != null)
        {
            E5 = 1;
        } else
        {
            if(keyClearSelection(i))
                getSelection().clearSelection();
            E5 = 6;
            _fld0104.x = point1.x;
            _fld0104.y = point1.y;
            Graphics2D graphics2d = getFREEGraphics();
            drawXORBox(graphics2d, _fld0104.x, _fld0104.y, point1.x, point1.y, 1);
            graphics2d.dispose();
        }
        return true;
    }

    public boolean doMouseMove(int i, Point point, Point point1)
    {
        switch(E5)
        {
        default:
            break;

        case 0: // '\0'
            doUncapturedMouseMove(i, point, point1);
            break;

        case 1: // '\001'
            if(EC == null)
                break;
            if(!getSelection().isInSelection(EC))
                EC = getSelection().selectObject(EC);
            if(EC == null || !EC.isDraggable() || EC.getLayer() != null && !EC.getLayer().isModifiable() || (getInternalMouseActions() & 0x3) == 0)
                break;
            if(FC)
                getSelection().clearSelectionHandles(null);
            E8.x = E6.x - EC.getLeft();
            E8.y = E6.y - EC.getTop();
            doMoveSelection(i, 0, 0, 1);
            E5 = 2;
            break;

        case 2: // '\002'
            int j = point.x - EC.getLeft() - E8.x;
            int k = point.y - EC.getTop() - E8.y;
            doMoveSelection(i, j, k, 2);
            break;

        case 6: // '\006'
            Graphics2D graphics2d = getFREEGraphics();
            drawXORBox(graphics2d, _fld0104.x, _fld0104.y, point1.x, point1.y, 2);
            graphics2d.dispose();
            break;

        case 5: // '\005'
            Graphics2D graphics2d1 = getFREEGraphics();
            handleResizing(graphics2d1, point1, point, 2);
            graphics2d1.dispose();
            break;

        case 3: // '\003'
            FREEPort port = pickNearestPort(point);
            if(port != null && port != FE)
                FF.setLocation(port.getToLinkPoint(_fld0104));
            else
                FF.setLocation(point);
            break;

        case 4: // '\004'
            FREEPort port1 = pickNearestPort(point);
            if(port1 != null && port1 != FE)
                FF.setLocation(port1.getFromLinkPoint(_fld0104));
            else
                FF.setLocation(point);
            break;
        }
        return true;
    }

    public boolean doMouseUp(int i, Point point, Point point1)
    {
        switch(E5)
        {
        case 0: // '\0'
        default:
            break;

        case 1: // '\001'
            if(EC == null)
                break;
            if(keyMultipleSelect(i))
                EC = getSelection().toggleSelection(EC);
            else
                doMouseClick(i, point, point1);
            break;

        case 2: // '\002'
            int j = point.x - EC.getLeft() - E8.x;
            int k = point.y - EC.getTop() - E8.y;
            doMoveSelection(i, j, k, 3);
            if(FC)
                getSelection().restoreSelectionHandles(null);
            break;

        case 6: // '\006'
            Graphics2D graphics2d = getFREEGraphics();
            drawXORBox(graphics2d, _fld0104.x, _fld0104.y, point1.x, point1.y, 3);
            if(Math.abs(point1.x - _fld0104.x) < 3 && Math.abs(point1.y - _fld0104.y) < 3)
            {
                doBackgroundClick(i, point, point1);
            } else
            {
                Rectangle rectangle = viewToDocCoords(_fld0101);
                selectInBox(rectangle);
            }
            graphics2d.dispose();
            break;

        case 5: // '\005'
            Graphics2D graphics2d1 = getFREEGraphics();
            handleResizing(graphics2d1, point1, point, 3);
            if(FC)
                getSelection().restoreSelectionHandles(EC);
            graphics2d1.dispose();
            break;

        case 3: // '\003'
            FREEPort port = pickNearestPort(point);
            removeObject(_fld0100);
            if(port != null && port != FE)
            {
                newLink(FE, port);
            } else
            {
                FREEPort port2 = pickPort(point);
                noNewLink(FE, port2);
            }
            break;

        case 4: // '\004'
            FREEPort port1 = pickNearestPort(point);
            removeObject(_fld0100);
            if(port1 != null && port1 != FE)
            {
                newLink(port1, FE);
            } else
            {
                FREEPort port3 = pickPort(point);
                noNewLink(port3, FE);
            }
            break;
        }
        E5 = 0;
        return true;
    }

    public boolean doMouseClick(int i, Point point, Point point1)
    {
        Object obj = pickDocObject(point, true);
        obj = getSelection().selectObject(((FREEObject) (obj)));
        if(obj != null)
            fireUpdate(10, 0, obj, point1, point, i);
        for(; obj != null; obj = ((FREEObject) (obj)).getParent())
            if(((FREEObject) (obj)).doMouseClick(i, point, point1, this))
                return true;

        return false;
    }

    public boolean doMouseDblClick(int i, Point point, Point point1)
    {
        Object obj = pickDocObject(point, false);
        if(obj != null)
            fireUpdate(11, 0, obj, point1, point, i);
        for(; obj != null; obj = ((FREEObject) (obj)).getParent())
            if(((FREEObject) (obj)).doMouseDblClick(i, point, point1, this))
                return true;

        return false;
    }

    public void doCancelMouse()
    {
        switch(E5)
        {
        case 0: // '\0'
        case 1: // '\001'
        default:
            break;

        case 2: // '\002'
            doCancelMoveSelection(E8);
            if(FC)
                getSelection().restoreSelectionHandles(null);
            break;

        case 6: // '\006'
            Graphics2D graphics2d = getFREEGraphics();
            drawXORBox(graphics2d, 0, 0, 0, 0, 3);
            graphics2d.dispose();
            break;

        case 5: // '\005'
            doCancelResize(_fld0103);
            if(EC != null && FC)
                getSelection().restoreSelectionHandles(EC);
            break;

        case 3: // '\003'
            removeObject(_fld0100);
            _fld0100 = null;
            noNewLink(FE, null);
            FE = null;
            FF = null;
            break;

        case 4: // '\004'
            removeObject(_fld0100);
            _fld0100 = null;
            noNewLink(null, FE);
            FE = null;
            FF = null;
            break;
        }
        E5 = 0;
    }

    public void doUncapturedMouseMove(int i, Point point, Point point1)
    {
        FREEObject object = pickObject(point, false);
        if(object != null && object.doUncapturedMouseMove(i, point, point1, this))
            return;
        for(Object obj = pickDocObject(point, false); obj != null; obj = ((FREEObject) (obj)).getParent())
            if(((FREEObject) (obj)).doUncapturedMouseMove(i, point, point1, this))
                return;

        Cursor cursor = getDefaultCursor();
        if(getCursor() != cursor)
            setCursor(cursor);
    }

    public void setCursor(Cursor cursor)
    {
    /*
        final FREEView final_view1 = this;
        Cursor cursor1 = cursor;
        Thread thread = new Thread()
        {
            private final FREEView D1 = final_view1;
            private final Cursor   D2 = Cursor.this;
            public void run()
            {
                _cls7 _lcls7 = new _cls7();
                SwingUtilities.invokeLater(_lcls7);
            }

            static Cursor D1(_cls6 _pcls6)
            {
                return _pcls6.D2;
            }

            static FREEView D2(_cls6 _pcls6)
            {
                return _pcls6.D1;
            }
        };
        thread.start();
        */
    }

    public void setCursorImmediately(Cursor cursor)
    {
        super.setCursor(cursor);
    }

    public Cursor getDefaultCursor()
    {
        if(m_defaultCursor == null)
            m_defaultCursor = Cursor.getPredefinedCursor(0);
        return m_defaultCursor;
    }

    public void setDefaultCursor(Cursor cursor)
    {
        m_defaultCursor = cursor;
    }

    public void doBackgroundClick(int i, Point point, Point point1)
    {
    }

    public final void doBackgroundClick(Point point)
    {
        doBackgroundClick(E9.getModifiers(), viewToDocCoords(point), point);
    }

    public void doMoveSelection(int i, int j, int k, int l)
    {
        if(getDocument() != null && getDocument().isModifiable())
        {
            boolean flag = (i & 0x2) != 0;
            int i1 = getInternalMouseActions();
            boolean flag1 = (i1 & 0x1) != 0;
            boolean flag2 = i1 == 1;
            boolean flag3 = flag2 | flag1 & flag;
            if(flag3)
                DF();
            else
                E0();
            if(flag3 && l == 3)
            {
                FREESelection selection = getSelection();
                Point point = new Point(EC.getLeft() - EF.getLeft(), EC.getTop() - EF.getTop());
                E0();
                FREECopyEnvironment copyEnv = getDocument().createDefaultCopyEnvironment();
                getDocument().copyFromCollection(selection, point, copyEnv);
                selection.clearSelection();
                for(Iterator iterator = copyEnv.values().iterator(); iterator.hasNext();)
                {
                    Object obj = iterator.next();
                    if(obj instanceof FREEObject)
                    {
                        FREEObject object = (FREEObject)obj;
                        if(object.isTopLevel())
                            selection.extendSelection(object);
                    }
                }

            } else
            if(flag3)
                moveSelection(DD(), i, j, k, l);
            else
                moveSelection(getSelection(), i, j, k, l);
        }
    }

    public int getInternalMouseActions()
    {
        return m_mouseAction;
    }

    public void setInternalMouseActions(int i)
    {
        m_mouseAction = i;
    }

    FREESelection DD()
    {
        return EE;
    }

    FREESelection m_frame()
    {
        FREESelection selection = new FREESelection(this);
        FREESelection selection1 = getSelection();
        for(FREEListPosition listposition = selection1.getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = selection1.getObjectAtPos(listposition);
            listposition = selection1.getNextObjectPosAtTop(listposition);
            Object obj;
            if(object instanceof FREELink)
            {
                XORStroke xorstroke = new XORStroke();
                FREEStroke stroke = (FREEStroke)object;
                for(int i = 0; i < stroke.getNumPoints(); i++)
                {
                    Point point = stroke.getPoint(i);
                    xorstroke.addPoint(point);
                }

                obj = xorstroke;
            } else
            {
                obj = new XORRectangle(object.getBoundingRect());
            }
            addObjectAtTail(((FREEObject) (obj)));
            selection.extendSelection(((FREEObject) (obj)));
            if(EC == object)
                EC = ((FREEObject) (obj));
        }

        return selection;
    }

    void DF()
    {
        if(EE == null)
        {
            EF = EC;
            EE = m_frame();
            if(EE.isEmpty())
            {
                EF = null;
                EE = null;
                return;
            }
            moveSelection(getSelection(), 0, E6.x - E8.x - EF.getLeft(), E6.y - E8.y - EF.getTop(), 2);
            if(EC.getView() != this)
                EC = EE.getObjectAtPos(EE.getFirstObjectPos());
        }
    }

    void E0()
    {
        if(EE != null)
        {
            for(FREEListPosition listposition = EE.getFirstObjectPos(); listposition != null; listposition = EE.getNextObjectPosAtTop(listposition))
            {
                FREEObject object = EE.getObjectAtPos(listposition);
                removeObject(object);
            }

            EE = null;
            EC = EF;
            EF = null;
        }
    }

    public void moveSelection(FREESelection selection, int i, int j, int k, int l)
    {
        if(j == 0 && k == 0)
            return;
        Vector vector = null;
        FREEListPosition listposition = selection.getFirstObjectPos();
        while(listposition != null)
        {
            FREEObject object = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(!object.isDraggable() || object.getLayer() != null && !object.getLayer().isModifiable())
                continue;
            FREEObject object1 = object.getTopLevelObject();
            if(object1 != object)
            {
                if(selection.isSelected(object1) || vector != null && vector.contains(object1))
                    continue;
                if(vector == null)
                    vector = new Vector();
                vector.add(object1);
            }
            int i1 = object1.getLeft();
            int j1 = object1.getTop();
            object1.setBoundingRect(i1 + j, j1 + k, object1.getWidth(), object1.getHeight());
        }
    }

    public void doCancelMoveSelection(Point point)
    {
        E0();
        if(EC == null)
            return;
        if(getDocument() == null || !getDocument().isModifiable())
        {
            return;
        } else
        {
            moveSelection(getSelection(), 0, E6.x - point.x - EC.getLeft(), E6.y - point.y - EC.getTop(), 3);
            return;
        }
    }

    protected boolean startResizing(FREEHandle handle, Point point, Point point1)
    {
        FREEObject object = handle.getHandleFor();
        if(object.getLayer() == null || object.getLayer().isModifiable())
        {
            EC = object;
            EB = handle.getHandleType();
            if(FC)
                getSelection().clearSelectionHandles(EC);
            E5 = 5;
            Graphics2D graphics2d = getFREEGraphics();
            handleResizing(graphics2d, point1, point, 1);
            graphics2d.dispose();
            return true;
        } else
        {
            return false;
        }
    }

    protected void handleResizing(Graphics2D graphics2d, Point point, Point point1, int i)
    {
        if(EC == null || !EC.isResizable())
            return;
        Rectangle rectangle = null;
        if(i == 1)
        {
            _fld0103.setBounds(EC.getBoundingRect());
            rectangle = new Rectangle(_fld0103);
        }
        Rectangle rectangle1 = EC.handleResize(graphics2d, this, _fld0103, point1, EB, i, 0, 0);
        if(rectangle1 != null)
            if(rectangle != null)
            {
                convertDocToView(rectangle);
                drawXORBox(graphics2d, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, 1);
            } else
            {
                convertDocToView(rectangle1);
                drawXORBox(graphics2d, rectangle1.x, rectangle1.y, rectangle1.x + rectangle1.width, rectangle1.y + rectangle1.height, i);
            }
    }

    public void doCancelResize(Rectangle rectangle)
    {
        Graphics2D graphics2d = getFREEGraphics();
        drawXORBox(graphics2d, 0, 0, 0, 0, 3);
        graphics2d.dispose();
        if(EC == null || !EC.isResizable())
        {
            return;
        } else
        {
            EC.setBoundingRect(rectangle);
            return;
        }
    }

    public String getToolTipText(MouseEvent mouseevent)
    {
        if(!isMouseEnabled())
            return null;
        Point point = mouseevent.getPoint();
        EA.setLocation(point);
        convertViewToDoc(EA);
        for(Object obj = pickDocObject(EA, false); obj != null; obj = ((FREEObject) (obj)).getParent())
        {
            String s = ((FREEObject) (obj)).getToolTipText();
            if(s != null)
                return s;
        }

        return null;
    }

    public void setEditControl(FREETextEdit textedit)
    {
        if(m_textEdit != textedit)
        {
            if(textedit == null)
                removeObject(m_textEdit);
            m_textEdit = textedit;
            if(m_textEdit != null)
                addObjectAtTail(m_textEdit);
        }
    }

    public FREETextEdit getEditControl()
    {
        return m_textEdit;
    }

    public boolean isEditingTextControl()
    {
        return getEditControl() != null;
    }

    public void doEndEdit()
    {
        if(isEditingTextControl())
            getEditControl().doEndEdit();
    }

    protected void drawXORRect(Graphics2D graphics2d, int i, int j, int k, int l)
    {
        graphics2d.setXORMode(Color.white);
        FREEDrawable.drawRect(graphics2d, FREEPen.darkGray, null, i, j, k, l);
        graphics2d.setPaintMode();
    }

    protected void drawXORBox(Graphics2D graphics2d, int i, int j, int k, int l, int i1)
    {
        if(_fld0102)
        {
            if(_fld0101.width != 0 || _fld0101.height != 0)
                drawXORRect(graphics2d, _fld0101.x, _fld0101.y, _fld0101.width, _fld0101.height);
            _fld0102 = false;
        }
        if(i1 != 3)
        {
            _fld0101.x = Math.min(i, k);
            _fld0101.y = Math.min(j, l);
            _fld0101.width = Math.abs(i - k);
            _fld0101.height = Math.abs(j - l);
            if(_fld0101.width != 0 || _fld0101.height != 0)
                drawXORRect(graphics2d, _fld0101.x, _fld0101.y, _fld0101.width, _fld0101.height);
            _fld0102 = true;
        }
    }

    public Graphics2D getFREEGraphics()
    {
        return (Graphics2D)getCanvas().getGraphics();
    }

    protected FREEHandle pickHandle(Point point)
    {
        FREEObject object = pickObject(point, true);
        if(object instanceof FREEHandle)
            return (FREEHandle)object;
        else
            return null;
    }

    public void newLink(FREEPort port, FREEPort port1)
    {
        FREEDocument document = getDocument();
        if(document == null)
        {
            return;
        } else
        {
            FREELink link = new FREELink(port, port1);
            document.getDefaultLayer().addObjectAtTail(link);
            return;
        }
    }

    protected void noNewLink(FREEPort port, FREEPort port1)
    {
    }

    private void E1(boolean flag)
    {
        for(FREELayer layer = getFirstLayer(); layer != null; layer = getNextLayer(layer))
            if(layer.isVisible())
            {
                for(FREEListPosition listposition = layer.getFirstObjectPos(); listposition != null;)
                {
                    FREEObject object = layer.getObjectAtPos(listposition);
                    listposition = layer.getNextObjectPos(listposition);
                    if(object instanceof FREEPort)
                    {
                        FREEPort port = (FREEPort)object;
                        if(flag)
                            port._mth0127(validLink(FE, port));
                        else
                            port._mth0127(validLink(port, FE));
                    }
                }

            }

    }

    public boolean validLink(FREEPort port, FREEPort port1)
    {
        return port.getLayer() != null && port.getLayer().isModifiable() && port1.getLayer() != null && port1.getLayer().isModifiable() && port.validLink(port1);
    }

    public boolean validSourcePort(FREEPort port)
    {
        return port.getLayer() != null && port.getLayer().isModifiable() && port.isValidSource();
    }

    public boolean validDestinationPort(FREEPort port)
    {
        return port.getLayer() != null && port.getLayer().isModifiable() && port.isValidDestination();
    }

    public int getPortGravity()
    {
        return FREEGlobal.getPortGravity();
    }

    public FREEPort pickNearestPort(Point point)
    {
        FREEPort port = null;
        double d = getPortGravity();
        d *= d;
        for(FREELayer layer = getFirstLayer(); layer != null; layer = getNextLayer(layer))
            if(layer.isVisible())
            {
                FREEListPosition listposition = layer.getFirstObjectPos();
                Point point1 = new Point();
                while(listposition != null)
                {
                    FREEObject object = layer.getObjectAtPos(listposition);
                    listposition = layer.getNextObjectPos(listposition);
                    if(object instanceof FREEPort)
                    {
                        FREEPort port1 = (FREEPort)object;
                        if(port1.isValidLink())
                        {
                            point1 = port1.getLinkPoint(0, point1);
                            double d1 = point.x - point1.x;
                            double d2 = point.y - point1.y;
                            double d3 = d1 * d1 + d2 * d2;
                            if(d3 <= d)
                            {
                                port = port1;
                                d = d3;
                            }
                        }
                    }
                }
            }

        return port;
    }

    protected FREEPort pickPort(Point point)
    {
        FREEObject object = pickDocObject(point, false);
        if(object == null)
            return null;
        if(object instanceof FREEPort)
            return (FREEPort)object;
        else
            return null;
    }

    protected boolean startNewLink(FREEPort port, Point point)
    {
        boolean flag = validSourcePort(port);
        boolean flag1 = validDestinationPort(port);
        if(flag || flag1)
        {
            FE = port;
            FF = createTemporaryPortForNewLink(port, point);
            if(flag)
            {
                E5 = 3;
                _fld0100 = createTemporaryLinkForNewLink(FE, FF);
                E1(true);
            } else
            {
                E5 = 4;
                _fld0100 = createTemporaryLinkForNewLink(FF, FE);
                E1(false);
            }
            addObjectAtTail(_fld0100);
            return true;
        } else
        {
            return false;
        }
    }

    protected FREEPort createTemporaryPortForNewLink(FREEPort port, Point point)
    {
        FREEPort port1 = new FREEPort();
        port1.setLocation(point);
        port1.setToSpot(-1);
        port1.setFromSpot(-1);
        return port1;
    }

    protected FREELink createTemporaryLinkForNewLink(FREEPort port, FREEPort port1)
    {
        return new FREELink(port, port1);
    }

    public void lostOwnership(Clipboard clipboard, Transferable transferable)
    {
    }

    public void copy()
    {
        Toolkit toolkit = getToolkit();
        if(toolkit == null)
            toolkit = FREEGlobal.getToolkit();
        copyToClipboard(toolkit.getSystemClipboard());
    }

    public void copyToClipboard(Clipboard clipboard)
    {
        FREEDocument document = getDocument();
        if(document != null)
            try
            {
                Class class1 = document.getClass();
                FREEDocument document1 = (FREEDocument)class1.newInstance();
                document1._mth011D(document);
                document1.copyFromCollection(getSelection());
                clipboard.setContents(document1, this);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
    }

    public void cut()
    {
        copy();
        deleteSelection();
    }

    public void deleteSelection()
    {
        FREESelection selection = getSelection();
        for(FREEListPosition listposition = selection.getFirstObjectPos(); listposition != null; listposition = selection.getFirstObjectPos())
        {
            FREEObject object = selection.getObjectAtPos(listposition);
            selection.clearSelection(object);
            if(object.getParent() != null)
                object = object.getTopLevelObject();
            if(object.getLayer() != null)
                object.getLayer().removeObject(object);
            else
            if(object.getView() != null)
                object.getView().removeObject(object);
        }

    }

    public void paste()
    {
        Toolkit toolkit = getToolkit();
        if(toolkit == null)
            toolkit = FREEGlobal.getToolkit();
        pasteFromClipboard(toolkit.getSystemClipboard());
    }

    public FREECopyEnvironment pasteFromClipboard(Clipboard clipboard)
    {
        Transferable transferable = clipboard.getContents(this);
        java.awt.datatransfer.DataFlavor dataflavor = FREEDocument.getStandardDataFlavor();
        if(transferable != null && transferable.isDataFlavorSupported(dataflavor))
            try
            {
                FREEDocument document = getDocument();
                if(document != null)
                {
                    FREEObjectSimpleCollection objectsimplecollection = (FREEObjectSimpleCollection)transferable.getTransferData(dataflavor);
                    return document.copyFromCollection(objectsimplecollection);
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        return null;
    }

    public void initializeDragDropHandling()
    {
        if(F4 == null)
        {
            F3 = DragSource.getDefaultDragSource();
            F4 = F3.createDefaultDragGestureRecognizer(getCanvas(), 3, getCanvas());
            F5 = new DropTarget(getCanvas(), 3, getCanvas());
            F2 = false;
            F0 = true;
            F1 = false;
        }
    }

    public void setDragDropEnabled(boolean flag)
    {
        if(F4 == null)
            return;
        if(flag && !F0)
        {
            F0 = true;
            try
            {
                F4.addDragGestureListener(getCanvas());
                F5.addDropTargetListener(getCanvas());
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else if(!flag && F0)
        {
            F0 = false;
            F4.removeDragGestureListener(getCanvas());
            F5.removeDropTargetListener(getCanvas());
        }
    }

    public boolean getDragDropEnabled()
    {
        return F0;
    }

    protected void onDragGestureRecognized(DragGestureEvent draggestureevent)
    {
        if(!getDragDropEnabled())
            return;
        boolean flag = !F1;
        F1 = false;
        if(flag)
        {
            return;
        } else
        {
            F2 = true;
            dragGestureRecognized(draggestureevent);
            return;
        }
    }

    public void dragGestureRecognized(DragGestureEvent draggestureevent)
    {
        try
        {
            if(E5 == 5)
                draggestureevent.startDrag(getCursor(), getSelection(), getCanvas());
            else
                draggestureevent.startDrag(DragSource.DefaultMoveDrop, getSelection(), getCanvas());
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void onDragEnter(DragSourceDragEvent dragsourcedragevent)
    {
        if(!getDragDropEnabled())
        {
            return;
        } else
        {
            dragEnter(dragsourcedragevent);
            return;
        }
    }

    protected void onDragOver(DragSourceDragEvent dragsourcedragevent)
    {
        if(!getDragDropEnabled())
        {
            return;
        } else
        {
            dragOver(dragsourcedragevent);
            return;
        }
    }

    protected void onDropActionChanged(DragSourceDragEvent dragsourcedragevent)
    {
        if(!getDragDropEnabled())
        {
            return;
        } else
        {
            dropActionChanged(dragsourcedragevent);
            return;
        }
    }

    protected void onDragExit(DragSourceEvent dragsourceevent)
    {
        if(!getDragDropEnabled())
        {
            return;
        } else
        {
            dragExit(dragsourceevent);
            return;
        }
    }

    protected void onDragDropEnd(DragSourceDropEvent dragsourcedropevent)
    {
        if(!getDragDropEnabled())
            return;
        if(F2)
        {
            F2 = false;
            doCancelMouse();
        } else
        {
            dragDropEnd(dragsourcedropevent);
        }
    }

    public void dragEnter(DragSourceDragEvent dragsourcedragevent)
    {
    }

    public void dragOver(DragSourceDragEvent dragsourcedragevent)
    {
    }

    public void dropActionChanged(DragSourceDragEvent dragsourcedragevent)
    {
    }

    public void dragExit(DragSourceEvent dragsourceevent)
    {
    }

    public void dragDropEnd(DragSourceDropEvent dragsourcedropevent)
    {
    }

    public int convertActionToModifiers(int i)
    {
        int j = 16;
        switch(i)
        {
        case 2: // '\002'
            j |= 0x1;
            break;

        case 1: // '\001'
            j |= 0x2;
            break;

        case 1073741824:
            j |= 0x3;
            break;
        }
        return j;
    }

    protected void onDragEnter(DropTargetDragEvent droptargetdragevent)
    {
        if(!getDragDropEnabled())
            return;
        if(F2)
        {
            Point point = droptargetdragevent.getLocation();
            F6.setLocation(point);
            convertViewToDoc(F6);
            doMouseMove(convertActionToModifiers(droptargetdragevent.getDropAction()), F6, point);
        } else
        {
            dragEnter(droptargetdragevent);
        }
    }

    protected void onDragOver(DropTargetDragEvent droptargetdragevent)
    {
        if(!getDragDropEnabled())
            return;
        if(F2)
        {
            Point point = droptargetdragevent.getLocation();
            F6.setLocation(point);
            convertViewToDoc(F6);
            doMouseMove(convertActionToModifiers(droptargetdragevent.getDropAction()), F6, point);
        } else
        {
            dragOver(droptargetdragevent);
        }
    }

    protected void onDropActionChanged(DropTargetDragEvent droptargetdragevent)
    {
        if(!getDragDropEnabled())
            return;
        if(!F2)
            dropActionChanged(droptargetdragevent);
    }

    protected void onDragExit(DropTargetEvent droptargetevent)
    {
        if(!getDragDropEnabled())
            return;
        if(!F2)
            dragExit(droptargetevent);
    }

    protected void onDrop(DropTargetDropEvent droptargetdropevent)
    {
        if(!getDragDropEnabled())
        {
            droptargetdropevent.rejectDrop();
            return;
        }
        if(F2)
        {
            F2 = false;
            Point point = droptargetdropevent.getLocation();
            F6.setLocation(point);
            convertViewToDoc(F6);
            if(doMouseUp(convertActionToModifiers(droptargetdropevent.getDropAction()), F6, point))
            {
                droptargetdropevent.acceptDrop(droptargetdropevent.getDropAction());
                E5 = 0;
                completeDrop(droptargetdropevent, true);
            } else
            {
                droptargetdropevent.rejectDrop();
            }
        } else
        {
            drop(droptargetdropevent);
        }
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent)
    {
        if(!isDropFlavorAcceptable(droptargetdragevent))
        {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if(i == 0)
            droptargetdragevent.rejectDrag();
        else
            droptargetdragevent.acceptDrag(i);
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent)
    {
        if(!isDropFlavorAcceptable(droptargetdragevent))
        {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if(i == 0)
            droptargetdragevent.rejectDrag();
        else
            droptargetdragevent.acceptDrag(i);
    }

    public void dropActionChanged(DropTargetDragEvent droptargetdragevent)
    {
        if(!isDropFlavorAcceptable(droptargetdragevent))
        {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if(i == 0)
            droptargetdragevent.rejectDrag();
        else
            droptargetdragevent.acceptDrag(i);
    }

    public void dragExit(DropTargetEvent droptargetevent)
    {
    }

    public void drop(DropTargetDropEvent droptargetdropevent)
    {
        if(getDocument() == null || !getDocument().getDefaultLayer().isModifiable() || !doDrop(droptargetdropevent, null))
            droptargetdropevent.rejectDrop();
    }

    public boolean isDropFlavorAcceptable(DropTargetDragEvent droptargetdragevent)
    {
        java.awt.datatransfer.DataFlavor dataflavor = FREEDocument.getStandardDataFlavor();
        return droptargetdragevent.isDataFlavorSupported(dataflavor);
    }

    public int computeAcceptableDrop(DropTargetDragEvent droptargetdragevent)
    {
        if(getDocument() == null || !getDocument().getDefaultLayer().isModifiable())
            return 0;
        if((droptargetdragevent.getDropAction() & 0x3) != 0)
            return droptargetdragevent.getDropAction();
        else
            return 0;
    }

    public boolean doDrop(DropTargetDropEvent droptargetdropevent, FREECopyEnvironment copyEnv)
    {
        try
        {
            java.awt.datatransfer.DataFlavor dataflavor = FREEDocument.getStandardDataFlavor();
            if(droptargetdropevent.isDataFlavorSupported(dataflavor) && (droptargetdropevent.getDropAction() & 0x3) != 0)
            {
                droptargetdropevent.acceptDrop(droptargetdropevent.getDropAction());
                Transferable transferable = droptargetdropevent.getTransferable();
                Object obj = transferable.getTransferData(dataflavor);
                FREEObjectSimpleCollection objectsimplecollection = (FREEObjectSimpleCollection)obj;
                Point point = null;
                FREEListPosition listposition = objectsimplecollection.getFirstObjectPos();
                if(listposition != null)
                {
                    FREEObject object = objectsimplecollection.getObjectAtPos(listposition);
                    point = object.getLocation();
                } else
                {
                    completeDrop(droptargetdropevent, true);
                    return true;
                }
                Point point1 = droptargetdropevent.getLocation();
                Point point2 = viewToDocCoords(point1);
                Point point3 = new Point(point2.x - point.x, point2.y - point.y);
                FREEDocument document = getDocument();
                if(document != null)
                    document.copyFromCollection(objectsimplecollection, point3, copyEnv);
                completeDrop(droptargetdropevent, true);
                return true;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return false;
    }

    public static void completeDrop(DropTargetDropEvent droptargetdropevent, boolean flag)
    {
    /*
        final DropTargetDropEvent final_droptargetdropevent = droptargetdropevent;
        final boolean final_flag = flag;
        Thread thread = new Thread()
            {

            private final DropTargetDropEvent D3 = final_droptargetdropevent;
            private final boolean D4             = final_flag;
            public void run()
            {
                _cls9 _lcls9 = new _cls9();
                SwingUtilities.invokeLater(_lcls9);
            }

            static boolean D3(_cls8 _pcls8)
            {
                return _pcls8.D4;
            }

            static DropTargetDropEvent D4(_cls8 _pcls8)
            {
                return _pcls8.D3;
            }
        };
        thread.start();
        */
    }

    public Insets getAutoscrollInsets()
    {
        return E2;
    }

    public void setAutoscrollInsets(Insets insets)
    {
        E2 = insets;
    }

    public void autoscroll(Point point)
    {
        Object obj = null;
        JScrollBar jscrollbar = getHorizontalScrollBar();
        Dimension dimension = getCanvas().getSize();
        Insets insets = getAutoscrollInsets();
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        if(jscrollbar != null)
        {
            int i = jscrollbar.getValue();
            int j = i;
            if(point.x <= insets.left)
            {
                j = i - jscrollbar.getUnitIncrement();
                j = Math.max(j, jscrollbar.getMinimum());
            } else
            if(point.x >= dimension.width - insets.right)
            {
                j = i + jscrollbar.getUnitIncrement();
                j = Math.min(j, jscrollbar.getMaximum());
            }
            switch(E5)
            {
            case 6: // '\006'
                Graphics2D graphics2d = getFREEGraphics();
                drawXORBox(graphics2d, 0, 0, 0, 0, 3);
                graphics2d.dispose();
                jscrollbar.setValue(j);
                if(i != jscrollbar.getValue())
                    _fld0104.x = _fld0104.x + (i - jscrollbar.getValue());
                break;

            case 5: // '\005'
                Graphics2D graphics2d1 = getFREEGraphics();
                drawXORBox(graphics2d1, 0, 0, 0, 0, 3);
                graphics2d1.dispose();
                jscrollbar.setValue(j);
                if(i != jscrollbar.getValue() && _fld0101 != null)
                {
                    int i1 = i - jscrollbar.getValue();
                    _fld0101.x += i1;
                    _fld0101.width += i1;
                }
                break;

            default:
                jscrollbar.setValue(j);
                break;
            }
        }
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if(jscrollbar1 != null)
        {
            int k = jscrollbar1.getValue();
            int l = k;
            if(point.y <= insets.top)
            {
                l = k - jscrollbar1.getUnitIncrement();
                l = Math.max(l, jscrollbar1.getMinimum());
            } else
            if(point.y >= dimension.height - insets.bottom)
            {
                l = k + jscrollbar1.getUnitIncrement();
                l = Math.min(l, jscrollbar1.getMaximum());
            }
            switch(E5)
            {
            case 6: // '\006'
                Graphics2D graphics2d2 = getFREEGraphics();
                drawXORBox(graphics2d2, 0, 0, 0, 0, 3);
                graphics2d2.dispose();
                jscrollbar1.setValue(l);
                if(k != jscrollbar1.getValue())
                    _fld0104.y = _fld0104.y + (k - jscrollbar1.getValue());
                break;

            case 5: // '\005'
                Graphics2D graphics2d3 = getFREEGraphics();
                drawXORBox(graphics2d3, 0, 0, 0, 0, 3);
                graphics2d3.dispose();
                jscrollbar1.setValue(l);
                if(k != jscrollbar1.getValue() && _fld0101 != null)
                {
                    int j1 = k - jscrollbar1.getValue();
                    _fld0101.y += j1;
                    _fld0101.height += j1;
                }
                break;

            default:
                jscrollbar1.setValue(l);
                break;
            }
        }
    }

    public FREELayer getFirstLayer()
    {
        FREEDocument document = getDocument();
        if(document == null)
            return null;
        else
            return document.getFirstLayer();
    }

    public FREELayer getLastLayer()
    {
        FREEDocument document = getDocument();
        if(document == null)
            return null;
        else
            return document.getLastLayer();
    }

    public FREELayer getNextLayer(FREELayer layer)
    {
        if(layer == null)
            return null;
        else
            return layer.getNextLayer();
    }

    public FREELayer getPrevLayer(FREELayer layer)
    {
        if(layer == null)
            return null;
        else
            return layer.getPrevLayer();
    }

   int[] E2()
    {
        return _fld0110;
    }

    int[] E3()
    {
        return _fld0111;
    }

    Dimension E4()
    {
        return _fld0113;
    }

    Rectangle E5()
    {
        return _fld0114;
    }
}
