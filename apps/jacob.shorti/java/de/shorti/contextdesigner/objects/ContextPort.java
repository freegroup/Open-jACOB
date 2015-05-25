package de.shorti.contextdesigner.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */


import de.shorti.dragdrop.*;
import java.awt.Point;
import java.awt.Rectangle;

public class ContextPort extends FREEPort
{
    public ContextPort()
    {
        setSelectable(false);
        setDraggable(false);
        setStyle(2);
//        setFromSpot(NoSpot);
//        setToSpot(NoSpot);
    }
}
