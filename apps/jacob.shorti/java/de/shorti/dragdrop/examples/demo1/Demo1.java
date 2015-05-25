//
//
//
// Source File Name:   Demo1.java

package de.shorti.dragdrop.examples.demo1;





import de.shorti.dragdrop.*;
import de.shorti.dragdrop.examples.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.border.TitledBorder;

// Referenced classes of package de.shorti.dragdrop.examples.demo1:
//            Demo1View, Demo1Palette, Demo1List, AppAction,
//            AboutDialog, Demo1RoundRect, GridOptionsDialog, StrokeDialog,
//            TextPropsDialog, DrawablePropsDialog, ImagePropsDialog, ObjectPropsDialog

public class Demo1 extends JFrame
{

    public Demo1()
    {
        basicNodeCounter = 0;
        mainMenuBar = new JMenuBar();
        filemenu = new JMenu();
        editmenu = new JMenu();
        insertmenu = new JMenu();
        layoutmenu = new JMenu();
        helpmenu = new JMenu();
        m_defaultLocation = new Point(10, 10);
        Container container = getContentPane();
        container.setBackground(new Color(255, 204, 204));
        setSize(500, 500);
        setTitle("FREE Demonstration Application");
        m_view = new Demo1View();
        m_doc = m_view.getDocument();
        m_doc.setPaperColor(new Color(255, 255, 221));
        m_nainLayer = m_doc.getFirstLayer();
        m_nainLayer.setTransparency(0.5F);
        m_foregroundLayer = m_doc.addLayerAfter(m_nainLayer);
        m_foregroundLayer.setIdentifier("in foreground layer");
        m_backgroundLayer = m_doc.addLayerBefore(m_nainLayer);
        m_backgroundLayer.setIdentifier("in read-only semitransparent background layer");
        m_backgroundLayer.setTransparency(0.5F);
        m_backgroundLayer.setModifiable(false);
        m_view.setGridWidth(20);
        m_view.setGridHeight(20);
        m_view.setBorder(new TitledBorder("Demo1 View"));
        m_doc.addDocumentListener(new FREEDocumentListener() {

            public void documentChanged(FREEDocumentEvent documentevent)
            {
                processDocChange(documentevent);
            }

        });
        m_view.addViewListener(new FREEViewListener() {

            public void viewChanged(FREEViewEvent viewevent)
            {
                processViewChange(viewevent);
            }

        });
        m_view.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                int i = keyevent.getKeyCode();
                if(i == 127)
                    m_view.deleteSelection();
                else
                if(keyevent.isControlDown() && i == 81)
                    System.exit(0);
            }

        });
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                Object obj = windowevent.getSource();
                if(obj == Demo1.this)
                    System.exit(0);
            }

        });
        AppAction appaction = new AppAction("About", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                aboutAction();
            }

        };
        AppAction appaction1 = new AppAction("Exit", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                exitAction();
            }

        };
        AppAction appaction2 = new AppAction("Lots of stuff", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                stuffAction();
            }

        };
        AppAction appaction3 = new AppAction("Rectangle", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                rectangleAction();
            }

        };
        AppAction appaction4 = new AppAction("Rounded Rectangle", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                roundedRectangleAction();
            }

        };
        AppAction appaction5 = new AppAction("Ellipse", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                ellipseAction();
            }

        };
        AppAction appaction6 = new AppAction("Simple Node", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                simpleNodeAction();
            }

        };
        AppAction appaction7 = new AppAction("Basic Node", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                basicNodeAction();
            }

        };
        AppAction appaction8 = new AppAction("MultiPort Node", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                multiPortNodeAction();
            }

        };
        AppAction appaction9 = new AppAction("Text Node", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                textNodeAction();
            }

        };
        AppAction appaction10 = new AppAction("List Area", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                listAreaAction();
            }

        };
        AppAction appaction11 = new AppAction("Record Nodes", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                recordNodeAction();
            }

        };
        AppAction appaction12 = new AppAction("Stroke", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                strokeAction();
            }

        };
        AppAction appaction13 = new AppAction("Polygon", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                polygonAction();
            }

        };
        AppAction appaction14 = new AppAction("Text", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                textAction();
            }

        };
        AppAction appaction15 = new AppAction("3D Rectangle", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                threeDRectAction();
            }

        };
        AppAction appaction16 = new AppAction("Comment", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                commentAction();
            }

        };
        AppAction appaction17 = new AppAction("10000 Objects", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                insert10000Action();
            }

        };
        AppAction appaction18 = new AppAction("Cut", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                getView().cut();
            }

            public boolean canAct()
            {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        AppAction appaction19 = new AppAction("Copy", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                getView().copy();
            }

            public boolean canAct()
            {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        AppAction appaction20 = new AppAction("Paste", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                getView().paste();
            }

        };
        AppAction appaction21 = new AppAction("Select All", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                getView().selectAll();
            }

        };
        AppAction appaction22 = new AppAction("Move to Front", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveToFrontAction();
            }

            public boolean canAct()
            {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        AppAction appaction23 = new AppAction("Move to Back", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveToBackAction();
            }

            public boolean canAct()
            {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        AppAction appaction24 = new AppAction("Change Layers", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                changeLayersAction();
            }

        };
        AppAction appaction25 = new AppAction("Group", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                groupAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction26 = new AppAction("Ungroup", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                ungroupAction();
            }

            public boolean canAct()
            {
                return super.canAct() && !getView().getSelection().isEmpty() && (getView().getSelection().getPrimarySelection() instanceof FREEArea);
            }

        };
        AppAction appaction27 = new AppAction("Properties", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                propertiesAction();
            }

            public boolean canAct()
            {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        AppAction appaction28 = new AppAction("Zoom In", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                zoomInAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getScale() < 8D;
            }

        };
        AppAction appaction29 = new AppAction("Zoom Out", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                zoomOutAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getScale() > 0.12999999523162842D;
            }

        };
        AppAction appaction30 = new AppAction("Zoom Normal Size", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                zoomNormalAction();
            }

        };
        AppAction appaction31 = new AppAction("Zoom To Fit", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                zoomToFitAction();
            }

        };
        AppAction appaction32 = new AppAction("Print", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                getView().print();
            }

        };
        AppAction appaction33 = new AppAction("Grid", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                gridAction();
            }

        };
        AppAction appaction34 = new AppAction("Overview", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                overviewAction();
            }

        };
        AppAction appaction35 = new AppAction("Align Left Sides", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                leftAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction36 = new AppAction("Align Horizontal Centers", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                horizontalAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction37 = new AppAction("Align Right Sides", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                rightAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction38 = new AppAction("Align Tops", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                topAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction39 = new AppAction("Align Bottoms", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                bottomAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction40 = new AppAction("Align Vertical Centers", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                verticalAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction41 = new AppAction("Make Same Size Widths", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                sameWidthAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction42 = new AppAction("Make Same Size Heights", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                sameHeightAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        AppAction appaction43 = new AppAction("Make Same Size Both", this) {

            public void actionPerformed(ActionEvent actionevent)
            {
                sameBothAction();
            }

            public boolean canAct()
            {
                return super.canAct() && getView().getSelection().getNumObjects() >= 2;
            }

        };
        filemenu.setText("File");
        filemenu.add(appaction32).setAccelerator(KeyStroke.getKeyStroke(80, 2));
        filemenu.addSeparator();
        filemenu.add(appaction1).setAccelerator(KeyStroke.getKeyStroke(115, 2));
        mainMenuBar.add(filemenu);
        editmenu.setText("Edit");
        editmenu.add(appaction18).setAccelerator(KeyStroke.getKeyStroke(88, 2));
        editmenu.add(appaction19).setAccelerator(KeyStroke.getKeyStroke(67, 2));
        editmenu.add(appaction20).setAccelerator(KeyStroke.getKeyStroke(86, 2));
        editmenu.add(appaction21).setAccelerator(KeyStroke.getKeyStroke(65, 2));
        editmenu.addSeparator();
        editmenu.add(appaction22).setAccelerator(KeyStroke.getKeyStroke(70, 2));
        editmenu.add(appaction23).setAccelerator(KeyStroke.getKeyStroke(66, 2));
        editmenu.add(appaction24).setAccelerator(KeyStroke.getKeyStroke(82, 2));
        editmenu.addSeparator();
        editmenu.add(appaction29).setAccelerator(KeyStroke.getKeyStroke(90, 2));
        editmenu.add(appaction28).setAccelerator(KeyStroke.getKeyStroke(90, 3));
        editmenu.add(appaction30).setAccelerator(KeyStroke.getKeyStroke(90, 1));
        editmenu.add(appaction31);
        editmenu.addSeparator();
        editmenu.add(appaction25);
        editmenu.add(appaction26);
        editmenu.addSeparator();
        editmenu.add(appaction27).setAccelerator(KeyStroke.getKeyStroke(10, 2));
        editmenu.add(appaction33).setAccelerator(KeyStroke.getKeyStroke(71, 2));
        editmenu.add(appaction34).setAccelerator(KeyStroke.getKeyStroke(79, 2));
        mainMenuBar.add(editmenu);
        insertmenu.setText("Insert");
        insertmenu.add(appaction2);
        insertmenu.add(appaction12);
        insertmenu.add(appaction13);
        insertmenu.add(appaction3);
        insertmenu.add(appaction4);
        insertmenu.add(appaction15);
        insertmenu.add(appaction16);
        insertmenu.add(appaction5);
        insertmenu.add(appaction6);
        insertmenu.add(appaction7);
        insertmenu.add(appaction8);
        insertmenu.add(appaction9);
        insertmenu.add(appaction14);
        insertmenu.add(appaction10);
        insertmenu.add(appaction11);
        insertmenu.add(appaction17);
        mainMenuBar.add(insertmenu);
        layoutmenu.setText("Layout");
        layoutmenu.add(appaction35);
        layoutmenu.add(appaction36);
        layoutmenu.add(appaction37);
        layoutmenu.add(appaction38);
        layoutmenu.add(appaction39);
        layoutmenu.add(appaction40);
        layoutmenu.addSeparator();
        layoutmenu.add(appaction41);
        layoutmenu.add(appaction42);
        layoutmenu.add(appaction43);
        mainMenuBar.add(layoutmenu);
        setJMenuBar(mainMenuBar);
        helpmenu.setText("Help");
        helpmenu.add(appaction);
        mainMenuBar.add(helpmenu);
        setJMenuBar(mainMenuBar);
        m_palette = new Demo1Palette();
        m_list = new Demo1List();
        AppAction aappaction[] = {
            appaction2, appaction12, appaction13, appaction3, appaction4, appaction15, appaction16, appaction5, appaction6, appaction7,
            appaction8, appaction9, appaction14, appaction10, appaction11
        };
        m_list.setListData(aappaction);
        m_paletteAndList = new JSplitPane(0);
        m_paletteAndList.setContinuousLayout(true);
        m_paletteAndList.setTopComponent(m_palette);
        m_listScrollPane = new JScrollPane(m_list);
        m_paletteAndList.setBottomComponent(m_listScrollPane);
        container.setLayout(new BorderLayout());
        container.add(m_view, "Center");
        container.add(m_paletteAndList, "West");
        container.validate();
    }

    protected void initPalette()
    {
        FREEDocument document = m_doc;
        FREELayer layer = m_backgroundLayer;
        FREELayer layer1 = m_nainLayer;
        FREELayer layer2 = m_foregroundLayer;
        FREEDocument document1 = m_palette.getDocument();
        document1.setSuspendUpdates(true);
        m_doc = document1;
        m_backgroundLayer = document1.getDefaultLayer();
        m_nainLayer = document1.getDefaultLayer();
        m_foregroundLayer = document1.getDefaultLayer();
        stuffAction();
        m_doc = document;
        m_backgroundLayer = layer;
        m_nainLayer = layer1;
        m_foregroundLayer = layer2;
        m_palette.init(this);
        document1.setSuspendUpdates(false);
    }

    public void setVisible(boolean flag)
    {
        if(flag)
            setLocation(50, 50);
        super.setVisible(flag);
    }

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Demo1 demo1 = new Demo1();
            demo1.setVisible(true);
            demo1.initPalette();
            updateActions();
        }
        catch(Throwable throwable)
        {
            System.err.println(throwable);
            throwable.printStackTrace();
            System.exit(1);
        }
    }

    Point getDefaultLocation()
    {
        if(m_defaultLocation != null)
        {
            m_defaultLocation.x += 4;
            m_defaultLocation.y += 4;
        }
        return m_defaultLocation;
    }

    void setDefaultLocation(Point point)
    {
        m_defaultLocation = point;
    }

    public void processDocChange(FREEDocumentEvent documentevent)
    {
        switch(documentevent.getHint())
        {
        default:
            break;

        case 2: // '\002'
            if(documentevent.getFREEObject() instanceof FREELink)
            {
                FREELink link = (FREELink)documentevent.getFREEObject();
                FREEPort port = link.getToPort();
                if(port != null && (port instanceof BasicNodePort))
                {
                    link.setArrowHeads(false, true);
                    if(link instanceof FREELabeledLink)
                    {
                        FREELabeledLink labeledlink = (FREELabeledLink)link;
                        labeledlink.setToLabel(null);
                    }
                }
                if(port != null && port.getParent() != null && (port.getParent().getFlags() & 0x10000) != 0)
                    link.setOrthogonal(true);
                if(link.getFromPort() != null && port.getClass() != link.getFromPort().getClass())
                    link.setHighlight(FREEPen.make(65535, link.getPen().getWidth() + 4, Color.red));
            }
            break;
        }
    }

    public void processViewChange(FREEViewEvent viewevent)
    {
        switch(viewevent.getHint())
        {
        case 1: // '\001'
        case 5: // '\005'
        case 6: // '\006'
            updateActions();
            break;

        case 8: // '\b'
            m_view.updateBorder();
            break;

        case 11: // '\013'
            FREEObject object = viewevent.getFREEObject();
            callDialog(object);
            break;
        }
    }

    void aboutAction()
    {
        try
        {
            (new AboutDialog(this, "Demo1 Application - About", true)).setVisible(true);
        }
        catch(Exception exception) { }
    }

    void exitAction()
    {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    void rectangleAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(200, 250);
        FREERectangle rectangle = new FREERectangle(point, new Dimension(75, 75));
        rectangle.setBrush(FREEBrush.makeStockBrush(Color.red));
        m_nainLayer.addObjectAtTail(rectangle);
    }

    void roundedRectangleAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(200, 75);
        Demo1RoundRect demo1roundrect = new Demo1RoundRect(point, new Dimension(80, 80), new Dimension(15, 30));
        demo1roundrect.setPen(FREEPen.make(4, 3, Color.darkGray));
        m_nainLayer.addObjectAtTail(demo1roundrect);
    }

    void ellipseAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(100, 250);
        FREEEllipse ellipse = new FREEEllipse(point, new Dimension(50, 75));
        ellipse.setBrush(FREEBrush.makeStockBrush(Color.green));
        m_nainLayer.addObjectAtTail(ellipse);
    }

    void simpleNodeAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(100, 70);
        SimpleNode simplenode = new SimpleNode();
        FREEImage image = new FREEImage(new Rectangle(0, 0, 40, 40));
        image.loadImage("doc.gif", false);
        simplenode.initialize(point, new Dimension(50, 50), image, "a simple node", true, true);
        m_nainLayer.addObjectAtTail(simplenode);
    }

    void basicNodeAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(40, 140);
        BasicNode basicnode = new BasicNode();
        String s = Integer.toString(basicNodeCounter++);
        basicnode.initialize(point, s);
        basicnode.setBrush(FREEBrush.makeStockBrush(Color.orange));
        m_nainLayer.addObjectAtTail(basicnode);
    }

    void textNodeAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(210, 190);
        TextNode textnode = new TextNode("hello!");
        textnode.setTopLeft(point);
        textnode.getRect().setBrush(FREEBrush.makeStockBrush(Color.pink));
        m_nainLayer.addObjectAtTail(textnode);
    }

    void recordNodeAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(100, 100);
        RecordNode recordnode = makeRecordNode(true);
        recordnode.setTopLeft(point.x + 200, point.y);
        recordnode.setHeight(150);
        recordnode.setLinePen(FREEPen.gray);
        recordnode.setSpacing(3);
        RecordNode recordnode1 = makeRecordNode(false);
        recordnode1.setTopLeft(point);
        recordnode1.setHeight(150);
        FREEPort port = recordnode1.getRightPort(5);
        FREEPort port1 = recordnode1.getRightPort(6);
        FREEPort port2 = recordnode1.getRightPort(7);
        FREEPort port3 = recordnode1.getRightPort(8);
        FREEPort port4 = recordnode.getLeftPort(5);
        FREEPort port5 = recordnode.getLeftPort(10);
        FREEPort port6 = recordnode.getLeftPort(0);
        FREEPort port7 = recordnode.getLeftPort(19);
        m_foregroundLayer.addObjectAtTail(new FREELink(port, port4));
        FREEImage image = new FREEImage();
        image.loadImage("star.gif", true);
        port4.setSize(7, 7);
        port4.setStyle(1);
        port4.setPortObject(image);
        m_foregroundLayer.addObjectAtTail(new FREELink(port1, port5));
        port5.setSize(7, 7);
        port5.setPortObject(image);
        port5.setStyle(1);
        m_foregroundLayer.addObjectAtTail(new FREELink(port2, port6));
        port6.setSize(7, 7);
        port6.setPortObject(image);
        port6.setStyle(1);
        m_foregroundLayer.addObjectAtTail(new FREELink(port3, port7));
        port7.setSize(7, 7);
        port7.setPortObject(image);
        port7.setStyle(1);
        m_foregroundLayer.addObjectAtTail(new FREELink(port, port6));
        FREEPort port8 = recordnode1.getRightPort(13);
        port8.setSize(9, 9);
        port8.setStyle(5);
        port8.setBrush(FREEBrush.makeStockBrush(Color.magenta));
    }

    RecordNode makeRecordNode(boolean flag)
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(20, 250);
        RecordNode recordnode = new RecordNode();
        recordnode.initialize();
        recordnode.setScrollBarOnRight(flag);
        FREEText text = new FREEText("a Record");
        text.setAlignment(1);
        text.setBkColor(Color.blue);
        text.setTextColor(Color.white);
        text.setBold(true);
        text.setClipping(true);
        recordnode.setHeader(text);
        for(int i = 0; i < 20; i++)
        {
            FREEPort port = null;
            FREEPort port1 = null;
            if(recordnode.isScrollBarOnRight())
            {
                port = new FREEPort();
                port.setSize(5, 5);
                port.setFromSpot(8);
                port.setToSpot(8);
            } else
            {
                port1 = new FREEPort();
                port1.setSize(5, 5);
                port1.setFromSpot(4);
                port1.setToSpot(4);
            }
            recordnode.addItem(makeListItem(i), port, port1);
        }

        m_foregroundLayer.addObjectAtTail(recordnode);
        return recordnode;
    }

    ListArea listAreaAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(20, 250);
        ListArea listarea = new ListArea();
        listarea.initialize();
        switch(m_lAorg)
        {
        case 0: // '\0'
        default:
            listarea.setVertical(true);
            listarea.setScrollBarOnRight(true);
            m_lAorg++;
            break;

        case 1: // '\001'
            listarea.setVertical(true);
            listarea.setScrollBarOnRight(false);
            m_lAorg++;
            break;

        case 2: // '\002'
            listarea.setVertical(false);
            listarea.setScrollBarOnRight(true);
            m_lAorg++;
            break;

        case 3: // '\003'
            listarea.setVertical(false);
            listarea.setScrollBarOnRight(false);
            m_lAorg = 0;
            break;
        }
        for(int i = 0; i < 10; i++)
            listarea.addItem(makeListItem(i), null, null);

        m_nainLayer.addObjectAtTail(listarea);
        listarea.setTopLeft(point.x, point.y);
        listarea.setFirstVisibleIndex(3);
        listarea.setAlignment(0);
        return listarea;
    }

    FREEObject makeListItem(int i)
    {
        if(i == 7)
        {
            FREEPolygon polygon = new FREEPolygon();
            polygon.setBrush(FREEBrush.makeStockBrush(Color.red));
            polygon.setPen(FREEPen.make(65535, 3, Color.white));
            polygon.addPoint(new Point(10, 0));
            polygon.addPoint(new Point(20, 0));
            polygon.addPoint(new Point(30, 10));
            polygon.addPoint(new Point(30, 20));
            polygon.addPoint(new Point(20, 30));
            polygon.addPoint(new Point(10, 30));
            polygon.addPoint(new Point(0, 20));
            polygon.addPoint(new Point(0, 10));
            return polygon;
        }
        if(i == 13)
        {
            FREEText text = new FREEText("start a link");
            text.setDraggable(false);
            text.setTransparent(true);
            text.setSelectBackground(true);
            text.setTextColor(Color.blue);
            text.setBkColor(m_view.getSecondarySelectionColor());
            text.setItalic(true);
            text.setFontSize(16);
            return text;
        }
        if(i == 14)
        {
            FREEText text1 = new FREEText("not selectable");
            text1.setSelectable(false);
            text1.setTransparent(true);
            text1.setSelectBackground(true);
            text1.setTextColor(Color.red);
            text1.setBkColor(m_view.getSecondarySelectionColor());
            text1.setItalic(true);
            return text1;
        }
        String s = "Item " + i;
        FREEText text2 = new FREEText(s);
        text2.setTransparent(true);
        text2.setSelectBackground(true);
        text2.setBkColor(m_view.getSecondarySelectionColor());
        text2.setFaceName("Helvetica");
        if(i == 2 || i == 3 || i == 5 || i == 7 || i == 11 || i == 13 || i == 17 || i == 19)
            text2.setBold(true);
        return text2;
    }

    void multiPortNodeAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(30, 180);
        FREEArea area = new FREEArea();
        area.setFlags(area.getFlags() | 0x10000);
        FREERectangle rectangle = new FREERectangle(point, new Dimension(50, 50));
        rectangle.setSelectable(false);
        rectangle.setBrush(FREEBrush.makeStockBrush(Color.gray));
        area.addObjectAtHead(rectangle);
        addMultiPort(area, rectangle, 0);
        addMultiPort(area, rectangle, 1);
        addMultiPort(area, rectangle, 2);
        addMultiPort(area, rectangle, 3);
        addMultiPort(area, rectangle, 4);
        addMultiPort(area, rectangle, 5);
        addMultiPort(area, rectangle, 6);
        addMultiPort(area, rectangle, 7);
        addMultiPort(area, rectangle, 8);
        m_nainLayer.addObjectAtTail(area);
    }

    void addMultiPort(FREEArea area, FREEObject object, int i)
    {
        FREEPort port = new FREEPort();
        port.setSize(6, 6);
        port.setStyle(5);
        port.setBrush(FREEBrush.makeStockBrush(Color.magenta));
        port.setSpotLocation(i, object, i);
        port.setFromSpot(i);
        port.setToSpot(i);
        area.addObjectAtTail(port);
    }

    void strokeAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(10, 10);
        FREEStroke stroke = new FREEStroke();
        stroke.addPoint(new Point(point.x + 0, point.y + 0));
        stroke.addPoint(new Point(point.x + 68, point.y + 46));
        stroke.addPoint(new Point(point.x + 68, point.y + 90));
        stroke.addPoint(new Point(point.x + 53, point.y + 57));
        stroke.addPoint(new Point(point.x + 85, point.y + 34));
        m_nainLayer.addObjectAtTail(stroke);
    }

    void polygonAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(10, 80);
        FREEPolygon polygon = new FREEPolygon();
        polygon.setBrush(FREEBrush.makeStockBrush(Color.red));
        polygon.setPen(FREEPen.make(65535, 3, Color.lightGray));
        polygon.addPoint(new Point(point.x + 10, point.y + 0));
        polygon.addPoint(new Point(point.x + 20, point.y + 0));
        polygon.addPoint(new Point(point.x + 30, point.y + 10));
        polygon.addPoint(new Point(point.x + 30, point.y + 20));
        polygon.addPoint(new Point(point.x + 20, point.y + 30));
        polygon.addPoint(new Point(point.x + 10, point.y + 30));
        polygon.addPoint(new Point(point.x + 0, point.y + 20));
        polygon.addPoint(new Point(point.x + 0, point.y + 10));
        m_nainLayer.addObjectAtTail(polygon);
    }

    void textAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(150, 50);
        String s = System.getProperty("user.dir");
        FREEText text = new FREEText(point, 12, s, "Serif", true, true, true, 1, false, true);
        text.setResizable(true);
        text.setEditOnSingleClick(true);
        m_nainLayer.addObjectAtTail(text);
    }

    void threeDRectAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(100, 150);
        FREE3DRect rect = new FREE3DRect(point, new Dimension(55, 25));
        rect.setBrush(FREEBrush.makeStockBrush(Color.cyan));
        m_nainLayer.addObjectAtTail(rect);
    }

    void commentAction()
    {
        Point point = getDefaultLocation();
        if(point == null)
            point = new Point(100, 200);
        Comment comment = new Comment("This is a\nmultiline comment.");
        comment.setTopLeft(point.x, point.y);
        m_nainLayer.addObjectAtTail(comment);
    }

    void insert10000Action()
    {
        FREEPen pen  = FREEPen.makeStockPen(Color.red);
        FREEPen pen1 = FREEPen.makeStockPen(Color.green);
        FREEPen pen2 = FREEPen.makeStockPen(Color.blue);
        for(int i = 0; i < 100; i++)
        {
            for(int j = 0; j < 100; j++)
            {
                Object obj;
                switch((i * 100 + j) % 3)
                {
                case 0: // '\0'
                default:
                    obj = new FREERectangle();
                    break;

                case 1: // '\001'
                    obj = new FREEEllipse();
                    break;

                case 2: // '\002'
                    obj = new FREERoundRect();
                    break;
                }
                switch((i * 100 + j) % 7)
                {
                case 0: // '\0'
                default:
                    ((FREEDrawable) (obj)).setPen(FREEPen.black);
                    break;

                case 1: // '\001'
                    ((FREEDrawable) (obj)).setPen(pen);
                    break;

                case 2: // '\002'
                    ((FREEDrawable) (obj)).setPen(FREEPen.gray);
                    break;

                case 3: // '\003'
                    ((FREEDrawable) (obj)).setPen(FREEPen.black);
                    break;

                case 4: // '\004'
                    ((FREEDrawable) (obj)).setPen(pen1);
                    break;

                case 5: // '\005'
                    ((FREEDrawable) (obj)).setPen(FREEPen.white);
                    break;

                case 6: // '\006'
                    ((FREEDrawable) (obj)).setPen(pen2);
                    break;
                }
                ((FREEObject) (obj)).setResizable(false);
                ((FREEObject) (obj)).setTopLeft(i * 10, j * 10);
                ((FREEObject) (obj)).setSize(8, 8);
                m_nainLayer.addObjectAtTail(((FREEObject) (obj)));
            }

        }

    }

    void stuffAction()
    {
        Point point = getDefaultLocation();
        setDefaultLocation(null);
        rectangleAction();
        roundedRectangleAction();
        ellipseAction();
        simpleNodeAction();
        basicNodeAction();
        multiPortNodeAction();
        textNodeAction();
        strokeAction();
        polygonAction();
        textAction();
        threeDRectAction();
        commentAction();
        setDefaultLocation(point);
    }

    void propertiesAction()
    {
        try
        {
            doEditProperties();
        }
        catch(Exception exception) { }
    }

    public void doEditProperties()
    {
        FREESelection selection = m_view.getSelection();
        if(!selection.isEmpty())
        {
            selection.clearSelectionHandles(null);
            for(de.shorti.dragdrop.FREEListPosition listposition = selection.getFirstObjectPos(); listposition != null;)
            {
                FREEObject object = selection.getObjectAtPos(listposition);
                listposition = selection.getNextObjectPos(listposition);
                callDialog(object);
            }

            selection.restoreSelectionHandles(null);
        }
    }

    void gridAction()
    {
        try
        {
            GridOptionsDialog gridoptionsdialog = new GridOptionsDialog(this, "Grid View Options", true, m_view);
            gridoptionsdialog.setVisible(true);
        }
        catch(Exception exception) { }
    }

    void overviewAction()
    {
        if(m_overview == null)
        {
            m_overview = new FREEView(m_doc);
            m_overview.setHorizontalScrollBar(null);
            m_overview.setVerticalScrollBar(null);
            m_overview.setCorner(null);
            m_overview.setScale(0.14999999999999999D);
            m_overview.setKeyEnabled(false);
            m_overview.setMouseEnabled(false);
            m_overview.setDragDropEnabled(false);
            m_overviewDialog = new JDialog((Frame)null, "Overview", false);
            m_overviewDialog.getContentPane().setLayout(new BorderLayout());
            m_overviewDialog.getContentPane().add(m_overview, "Center");
        }
        m_overviewDialog.pack();
        m_overviewDialog.show();
    }

    void moveToFrontAction()
    {
        FREESelection selection = m_view.getSelection();
        for(de.shorti.dragdrop.FREEListPosition listposition = selection.getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(object.getParent() != null)
                object.getParent().bringObjectToFront(object);
            else
                m_doc.bringObjectToFront(object);
        }

    }

    void moveToBackAction()
    {
        FREESelection selection = m_view.getSelection();
        for(de.shorti.dragdrop.FREEListPosition listposition = selection.getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(object.getParent() != null)
                object.getParent().sendObjectToBack(object);
            else
                m_doc.sendObjectToBack(object);
        }

    }

    void zoomInAction()
    {
        double d = Math.rint((m_view.getScale() / 0.89999997615814209D) * 100D) / 100D;
        m_view.setScale(d);
        updateActions();
    }

    void zoomOutAction()
    {
        double d = Math.rint(m_view.getScale() * 0.89999997615814209D * 100D) / 100D;
        m_view.setScale(d);
        updateActions();
    }

    void zoomNormalAction()
    {
        double d = 1.0D;
        m_view.setScale(d);
        updateActions();
    }

    void zoomToFitAction()
    {
        double d = 1.0D;
        if(!m_doc.isEmpty())
        {
            double d1 = m_view.getExtentSize().width;
            double d2 = m_view.getPrintDocumentSize().width;
            double d3 = m_view.getExtentSize().height;
            double d4 = m_view.getPrintDocumentSize().height;
            d = Math.min(d1 / d2, d3 / d4);
        }
        if(d > 2D)
            d = 1.0D;
        d *= m_view.getScale();
        m_view.setScale(d);
        m_view.setViewPosition(0, 0);
        updateActions();
    }

    void groupAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEDocument document = m_view.getDocument();
        FREEArea area = new FREEArea();
        area.setSelectable(false);
        area.setGrabChildSelection(true);
        for(de.shorti.dragdrop.FREEListPosition listposition = selection.getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            document.removeObject(object);
            object.setSelectable(false);
            if(object instanceof FREEArea)
                ((FREEArea)object).setGrabChildSelection(false);
            area.addObjectAtTail(object);
        }

        document.addObjectAtTail(area);
        selection.extendSelection(area);
    }

    void ungroupAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        if(object != null && (object instanceof FREEArea))
        {
            FREEArea area = (FREEArea)object;
            FREEDocument document = area.getDocument();
            for(de.shorti.dragdrop.FREEListPosition listposition = area.getFirstObjectPos(); listposition != null;)
            {
                FREEObject object1 = area.getObjectAtPos(listposition);
                listposition = area.getNextObjectPos(listposition);
                document.removeObject(object1);
                document.addObjectAtTail(object1);
                object1.setSelectable(true);
                selection.extendSelection(object1);
            }

            document.removeObject(area);
        }
    }

    void leftAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        Point point = object.getSpotLocation(1);
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(!(object1 instanceof FREELink))
                object1.setSpotLocation( FREEObject.TopLeft, new Point(point.x, object1.getTop()));
        }
    }

    void horizontalAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        Point point = object.getSpotLocation(0);
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(!(object1 instanceof FREELink))
                object1.setSpotLocation( FREEObject.TopCenter, new Point(point.x, object1.getTop()));
        }
    }

    void rightAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        Point point = object.getSpotLocation(3);
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(!(object1 instanceof FREELink))
                object1.setSpotLocation( FREEObject.TopRight, new Point(point.x, object1.getTop()));
        }
    }

    void topAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        Point point = object.getSpotLocation(2);
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(!(object1 instanceof FREELink))
                object1.setSpotLocation( FREEObject.TopLeft, new Point(object1.getLeft(), point.y));
        }
    }

    void bottomAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        Point point = object.getSpotLocation(6);
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(!(object1 instanceof FREELink))
                object1.setSpotLocation( FREEObject.BottomLeft, new Point(object1.getLeft(), point.y));
        }
    }

    void verticalAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        Point point = object.getSpotLocation(0);
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(!(object1 instanceof FREELink))
                object1.setSpotLocation( FREEObject.LeftCenter, new Point(object1.getLeft(), point.y));
        }
    }

    void sameWidthAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        int i = object.getWidth();
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(object1.isTopLevel() && !(object1 instanceof FREELink))
                object1.setWidth(i);
        }
    }

    void sameHeightAction()
    {
        FREESelection selection = m_view.getSelection();
        FREEObject object = selection.getPrimarySelection();
        de.shorti.dragdrop.FREEListPosition listposition;
        for(listposition = selection.getFirstObjectPos(); object != null && (object instanceof FREELink) && listposition != null; listposition = selection.getNextObjectPos(listposition))
            object = selection.getObjectAtPos(listposition);

        if(object == null)
            return;
        int i = object.getHeight();
        while(listposition != null)
        {
            FREEObject object1 = selection.getObjectAtPos(listposition);
            listposition = selection.getNextObjectPos(listposition);
            if(object1.isTopLevel() && !(object1 instanceof FREELink))
                object1.setHeight(i);
        }
    }

    void sameBothAction()
    {
        sameHeightAction();
        sameWidthAction();
    }

    void changeLayersAction()
    {
        FREESelection selection = m_view.getSelection();
        if(selection.isEmpty())
        {
            m_nainLayer.setTransparency((float)Math.random());
            m_backgroundLayer.setVisible(Math.random() >= 0.5D);
        } else
        {
            FREEObject object = selection.getObjectAtPos(selection.getFirstObjectPos());
            FREELayer layer = object.getLayer();
            layer = layer.getPrevLayer();
            if(layer == null)
                layer = m_foregroundLayer;
            for(de.shorti.dragdrop.FREEListPosition listposition = selection.getFirstObjectPos(); listposition != null;)
            {
                FREEObject object1 = selection.getObjectAtPos(listposition);
                listposition = selection.getNextObjectPosAtTop(listposition);
                layer.addObjectAtTail(object1);
            }

            m_backgroundLayer.setVisible(true);
        }
    }

    void callDialog(FREEObject object)
    {
        if((object instanceof FREEStroke) && !(object instanceof FREEPolygon))
        {
            StrokeDialog strokedialog = new StrokeDialog(m_view.getFrame(), "Stroke Properties", true, (FREEStroke)object);
            strokedialog.setVisible(true);
        } else
        if(object instanceof FREEText)
        {
            TextPropsDialog textpropsdialog = new TextPropsDialog(m_view.getFrame(), "Text Properties", true, (FREEText)object);
            textpropsdialog.setVisible(true);
        } else
        if(object instanceof FREEDrawable)
        {
            DrawablePropsDialog drawablepropsdialog = new DrawablePropsDialog(m_view.getFrame(), "Drawable Properties", true, (FREEDrawable)object);
            drawablepropsdialog.setVisible(true);
        } else
        if(object instanceof FREEImage)
        {
            ImagePropsDialog imagepropsdialog = new ImagePropsDialog(m_view.getFrame(), "Image Properties", true, (FREEImage)object);
            imagepropsdialog.setVisible(true);
        } else
        if(object instanceof FREEObject)
        {
            ObjectPropsDialog objectpropsdialog = new ObjectPropsDialog(m_view.getFrame(), "Object Properties", true, object);
            objectpropsdialog.setVisible(true);
        }
    }

    public static void updateActions()
    {
        AppAction.updateAllActions();
    }

    public Demo1View getCurrentView()
    {
        return m_view;
    }

    int basicNodeCounter;
    static int m_lAorg = 0;
    public static final int flagMultiPortNode = 0x10000;
    protected Demo1View m_view;
    protected FREEDocument m_doc;
    protected FREELayer m_nainLayer;
    protected FREELayer m_foregroundLayer;
    protected FREELayer m_backgroundLayer;
    protected JSplitPane m_paletteAndList;
    protected JScrollPane m_listScrollPane;
    protected Demo1Palette m_palette;
    protected Demo1List m_list;
    protected JDialog m_overviewDialog;
    protected FREEView m_overview;
    protected JMenuBar mainMenuBar;
    protected JMenu filemenu;
    protected JMenu editmenu;
    protected JMenu insertmenu;
    protected JMenu layoutmenu;
    protected JMenu helpmenu;
    protected Point m_defaultLocation;

}
