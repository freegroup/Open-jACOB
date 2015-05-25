package de.shorti.dragdrop;

import java.awt.*;
import java.util.Vector;
import javax.swing.JScrollBar;

public class FREEGridView extends FREEView
{
    public FREEGridView()
    {
        gridWidth = 50;
        gridHeight = 50;
        gridStyle = 0;
        _fld0147 = 1;
        gridOrigin = new Point();
        gridPen = FREEPen.lightGray;
        snapMove = 0;
        _fld014B = 0;
    }

    public FREEGridView(FREEDocument document)
    {
        super(document);
        gridWidth = 50;
        gridHeight = 50;
        gridStyle = 0;
        _fld0147 = 1;
        gridOrigin = new Point();
        gridPen = FREEPen.lightGray;
        snapMove = 0;
        _fld014B = 0;
    }

    public void setGridWidth(int i)
    {
        if(i != gridWidth)
        {
            gridWidth = i;
            if(getHorizontalScrollBar() != null)
                getHorizontalScrollBar().setUnitIncrement(getGridWidth());
            onGridChange(0);
        }
    }

    public int getGridWidth()
    {
        return gridWidth;
    }

    public void setGridHeight(int i)
    {
        if(i != gridHeight)
        {
            gridHeight = i;
            if(getVerticalScrollBar() != null)
                getVerticalScrollBar().setUnitIncrement(getGridHeight());
            onGridChange(0);
        }
    }

    public int getGridHeight()
    {
        return gridHeight;
    }

    public void setGridStyle(int i)
    {
        if(i != gridStyle)
        {
            gridStyle = i;
            onGridChange(1);
        }
    }

    public int getGridStyle()
    {
        return gridStyle;
    }

    public void setGridOrigin(Point point)
    {
        if(!point.equals(gridOrigin))
        {
            gridOrigin.x = point.x;
            gridOrigin.y = point.y;
            onGridChange(6);
        }
    }

    public Point getGridOrigin()
    {
        return gridOrigin;
    }

    public void setGridSpot(int i)
    {
        if(i != _fld0147)
        {
            _fld0147 = i;
            onGridChange(2);
        }
    }

    public int getGridSpot()
    {
        return _fld0147;
    }

    public void setGridPen(FREEPen pen)
    {
        if(gridPen != pen)
        {
            gridPen = pen;
            onGridChange(5);
        }
    }

    public FREEPen getGridPen()
    {
        return gridPen;
    }

    public int getSnapMove()
    {
        return snapMove;
    }

    public void setSnapMove(int i)
    {
        if(snapMove != i)
        {
            snapMove = i;
            onGridChange(3);
        }
    }

    public int getSnapResize()
    {
        return _fld014B;
    }

    public void setSnapResize(int i)
    {
        if(_fld014B != i)
        {
            _fld014B = i;
            onGridChange(4);
        }
    }

    public void onGridChange(int i)
    {
        fireUpdate(100, i, null);
    }

    protected void paintBackgroundDecoration(Graphics2D graphics2d, Rectangle rectangle)
    {
        switch(getGridStyle())
        {
        case 3: // '\003'
            drawGridLines(graphics2d, rectangle);
            break;

        case 1: // '\001'
            drawGridCrosses(graphics2d, 1, 1, rectangle);
            break;

        case 2: // '\002'
            drawGridCrosses(graphics2d, 6, 6, rectangle);
            break;
        }
    }

    protected void drawGridLines(Graphics2D graphics2d, Rectangle rectangle)
    {
        int i = getGridWidth();
        int j = getGridHeight();
        FREEPen pen = getGridPen();
        int k = rectangle.x - i;
        int l = rectangle.y - j;
        int i1 = rectangle.x + rectangle.width + i;
        int j1 = rectangle.y + rectangle.height + j;
        Point point = findNearestGridPoint(k, l, null);
        Point point1 = findNearestGridPoint(i1, j1, null);
        for(int k1 = point.x; k1 < point1.x; k1 += i)
            FREEDrawable.drawLine(graphics2d, pen, k1, rectangle.y, k1, rectangle.y + rectangle.height);

        for(int l1 = point.y; l1 < point1.y; l1 += j)
            FREEDrawable.drawLine(graphics2d, pen, rectangle.x, l1, rectangle.x + rectangle.width, l1);

    }

    protected void drawGridCrosses(Graphics2D graphics2d, int i, int j, Rectangle rectangle)
    {
        int k = getGridWidth();
        int l = getGridHeight();
        FREEPen pen = getGridPen();
        int i1 = rectangle.x - k;
        int j1 = rectangle.y - l;
        int k1 = rectangle.x + rectangle.width + k;
        int l1 = rectangle.y + rectangle.height + l;
        Point point = findNearestGridPoint(i1, j1, null);
        Point point1 = findNearestGridPoint(k1, l1, null);
        for(int i2 = point.x; i2 < point1.x; i2 += k)
        {
            for(int j2 = point.y; j2 < point1.y; j2 += l)
            {
                FREEDrawable.drawLine(graphics2d, pen, i2, j2 - i / 2, i2, j2 + i / 2);
                FREEDrawable.drawLine(graphics2d, pen, i2 - j / 2, j2, i2 + j / 2, j2);
            }

        }

    }

    public void doLayout()
    {
        if(getHorizontalScrollBar() != null)
            getHorizontalScrollBar().setUnitIncrement(getGridWidth());
        if(getVerticalScrollBar() != null)
            getVerticalScrollBar().setUnitIncrement(getGridHeight());
        super.doLayout();
    }

    public void moveSelection(FREESelection selection, int i, int j, int k, int l)
    {
        int i1 = getSnapMove();
        if(i1 == 1 || i1 == 2 && l == 3)
        {
            Vector vector = null;
            FREEListPosition listposition = selection.getFirstObjectPos();
            Point point = new Point();
            int j1 = getGridSpot();
            while(listposition != null)
            {
                FREEObject object = selection.getObjectAtPos(listposition);
                listposition = selection.getNextObjectPos(listposition);
                if(!object.isDraggable())
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
                Point point1 = object1.getSpotLocation(j1, point);
                int k1 = point1.x + j;
                int l1 = point1.y + k;
                findNearestGridPoint(k1, l1, point);
                object1.setSpotLocation(j1, point);
            }
        } else
        {
            super.moveSelection(selection, i, j, k, l);
        }
    }

    public void handleResizing(Graphics2D graphics2d, Point point, Point point1, int i)
    {
        int j = getSnapResize();
        Point point2;
        Point point3;
        if(j == 1 || j == 2 && i == 3)
        {
            point2 = findNearestGridPoint(point1.x, point1.y, null);
            point3 = docToViewCoords(point2);
        } else
        {
            point2 = point1;
            point3 = point;
        }
        super.handleResizing(graphics2d, point3, point2, i);
    }

    public void snapObject(FREEObject object)
    {
        snapObject(object, getGridSpot());
    }

    public void snapObject(FREEObject object, int i)
    {
        Point point = object.getSpotLocation(i);
        Point point1 = findNearestGridPoint(point.x, point.y, point);
        object.setSpotLocation(i, point1);
    }

    public void snapAllObjects()
    {
        snapAllObjects(getGridSpot());
    }

    public void snapAllObjects(int i)
    {
        FREEDocument document = getDocument();
        for(FREEListPosition listposition = document.getFirstObjectPos(); listposition != null;)
        {
            FREEObject object = document.getObjectAtPos(listposition);
            listposition = document.getNextObjectPosAtTop(listposition);
            snapObject(object, i);
        }

    }

    public Point findNearestGridPoint(int i, int j, Point point)
    {
        Point point1 = getGridOrigin();
        int k = point1.x;
        int l = point1.y;
        int i1 = getGridWidth();
        int j1 = getGridHeight();
        int k1 = i - k;
        if(k1 < 0)
            k1 -= i1;
        k1 = (k1 / i1) * i1 + k;
        int l1 = j - l;
        if(l1 < 0)
            l1 -= j1;
        l1 = (l1 / j1) * j1 + l;
        int i2 = (i - k1) * (i - k1) + (j - l1) * (j - l1);
        int j2 = k1;
        int k2 = l1;
        int l2 = k1 + i1;
        int i3 = l1;
        int j3 = (i - l2) * (i - l2) + (j - i3) * (j - i3);
        if(j3 < i2)
        {
            i2 = j3;
            j2 = l2;
            k2 = i3;
        }
        int k3 = k1;
        int l3 = l1 + j1;
        int i4 = (i - k3) * (i - k3) + (j - l3) * (j - l3);
        if(i4 < i2)
        {
            i2 = i4;
            j2 = k3;
            k2 = l3;
        }
        int j4 = l2;
        int k4 = l3;
        int l4 = (i - j4) * (i - j4) + (j - k4) * (j - k4);
        if(l4 < i2)
        {
            j2 = j4;
            k2 = k4;
        }
        if(point == null)
        {
            return new Point(j2, k2);
        } else
        {
            point.x = j2;
            point.y = k2;
            return point;
        }
    }

    public static final int GridInvisible = 0;
    public static final int GridDot = 1;
    public static final int GridCross = 2;
    public static final int GridLine = 3;
    public static final int NoSnap = 0;
    public static final int SnapJump = 1;
    public static final int SnapAfter = 2;
    public static final int ChangedDimensions = 0;
    public static final int ChangedStyle = 1;
    public static final int ChangedSpot = 2;
    public static final int ChangedSnapMove = 3;
    public static final int ChangedSnapResize = 4;
    public static final int ChangedPen = 5;
    public static final int ChangedOrigin = 6;
    private int gridWidth;
    private int gridHeight;
    private int gridStyle;
    private int _fld0147;
    private Point gridOrigin;
    private FREEPen gridPen;
    private int snapMove;
    private int _fld014B;
}
