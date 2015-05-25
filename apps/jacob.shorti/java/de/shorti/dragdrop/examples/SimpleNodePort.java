//
//
//
// Source File Name:   SimpleNodePort.java

package de.shorti.dragdrop.examples;


import java.awt.Rectangle;

import de.shorti.dragdrop.FREEArea;
import de.shorti.dragdrop.FREEBrush;
import de.shorti.dragdrop.FREELink;
import de.shorti.dragdrop.FREEPen;
import de.shorti.dragdrop.FREEPort;

public class SimpleNodePort extends FREEPort
{

    public SimpleNodePort()
    {
    }

    public SimpleNodePort(boolean flag, FREEArea area)
    {
        super(TriangleRect());
        initialize(flag, area);
    }

    public void initialize(boolean flag, FREEArea area)
    {
        setSelectable(false);
        setDraggable(false);
        setResizable(false);
        setVisible(true);
        setStyle(3);
        setPen(FREEPen.darkGray);
        setBrush(FREEBrush.lightGray);
        if(flag)
        {
            setValidSource(false);
            setValidDestination(true);
            setToSpot(8);
        } else
        {
            setValidSource(true);
            setValidDestination(false);
            setFromSpot(4);
        }
        setTopLeft(area.getLeft(), area.getTop());
        area.addObjectAtTail(this);
    }

    public final boolean isInput()
    {
        return isValidDestination();
    }

    public final boolean isOutput()
    {
        return isValidSource();
    }

    public boolean validLink(FREEPort port)
    {
        return getParent() != port.getParent() && isOutput() && (port instanceof SimpleNodePort) && ((SimpleNodePort)port).isInput() && !alreadyLinked(port);
    }

    public boolean alreadyLinked(FREEPort port)
    {
        for(de.shorti.dragdrop.FREEListPosition listposition = getFirstLinkPos(); listposition != null;)
        {
            FREELink link = getLinkAtPos(listposition);
            listposition = getNextLinkPos(listposition);
            if(link.getToPort() == port)
                return true;
        }

        return false;
    }

    public static Rectangle TriangleRect()
    {
        return m_triangleRect;
    }

    private static Rectangle m_triangleRect = new Rectangle(0, 0, 8, 8);

}
