//
//
//
// Source File Name:   SimpleNodeLabel.java

package de.shorti.dragdrop.examples;

import de.shorti.dragdrop.*;

public class SimpleNodeLabel extends FREEText
{

    public SimpleNodeLabel()
    {
    }

    public SimpleNodeLabel(String s, FREEArea area)
    {
        super(s);
        initialize(s, area);
    }

    public void initialize(String s, FREEArea area)
    {
        setSelectable(false);
        setDraggable(false);
        setResizable(false);
        setVisible(true);
        setEditable(true);
        setEditOnSingleClick(true);
        setTransparent(true);
        setAlignment(1);
        setTopLeft(area.getLeft(), area.getTop());
        area.addObjectAtTail(this);
    }
}
