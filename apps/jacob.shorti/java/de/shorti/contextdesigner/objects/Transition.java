/**
 * Title:        FREEObject
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      FreeGroup
 * @author Andreas Herz
 * @version 1.0
 */

package de.shorti.contextdesigner.objects;

import de.shorti.dragdrop.*;
import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import de.shorti.contextdesigner.*;

public class Transition extends FREELink
{
    protected Vector actions = new Vector();
    protected String fromNode;
    protected String toNode;
	QuadCurve2D.Double quad = new QuadCurve2D.Double();

    public class Action
    {
        public String pattern;
        public String defaulAnswer;
    }


    /**
     *
     */
    public Transition(String _fromNode, String _toNode, FREEPort from, FREEPort to, int x, int y)
    {
        super(from, to);
        setArrowHeads(false,true);
        setResizable(true);
        setSelectable(true);
        setArrowShaftLength(15);
        setArrowLength(15);
        setArrowAngle(0.3);
        setPoint(1,x,y);

        fromNode = _fromNode;
        toNode   = _toNode;
        setHighlight(FREEPen.makeStockPen(Color.blue));
        setPen(FREEPen.lightGray);
        setBrush(FREEBrush.lightGray);
    }

    public void resetActions()
    {
        actions = new Vector();
    }

    public Collection getActions()
    {
        return actions;
    }

    /**
     *
     */
    public int getSnapSpot()
    {
        return FREEObject.Center;
    }

    /**
     *
     */
    public void addPattern(String pattern, String defaulMessage)
    {
        Action a = new Action();
        a.defaulAnswer = defaulMessage;
        a.pattern      = pattern;
        actions.add(a);
    }

    /**
     *
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer(1024);
        int x= (int)getPoint(1).getX();
        int y= (int)getPoint(1).getY();
        sb.append("\t\t\t\t<forward toNode=\""+toNode+"\"  x=\""+x+"\" y=\""+y+"\">\n");
        Iterator iter = actions.iterator();
        while (iter.hasNext())
        {
            Action action = (Action)iter.next();
            sb.append("\t\t\t\t\t<question pattern=\""+action.pattern+"\" defaultAnswer=\""+action.defaulAnswer+"\"/>\n");
        }
        sb.append("\t\t\t\t</forward>\n");

        return sb.toString();
    }

    /**
     *
     */
    protected void calculateStroke()
    {
        FREEPort from = getFromPort();
        FREEPort to   = getToPort();
        if(from == null)   return;
        if(to == null)     return;

        try
        {
            setSuspendUpdates(true);
            // initial add of the points
            //
            if(m_points.size()==0)
            {
                Point fp = from.getLocation();
                Point tp = to.getLocation();
                addPoint(fp);
                addPoint(0,0);
                addPoint(tp);
            }

            Point point1 = from.getLinkPoint(Center);
            Point point2 = to.getLinkPoint(Center);
            getPoint(getFirstPickPoint()).setLocation(point1);
            getPoint(getLastPickPoint()).setLocation(point2);
        }
        finally
        {
            setSuspendUpdates(false);
        }
    }

    /**
     *
     */
    protected void gainedSelection(FREESelection selection)
    {
        super.gainedSelection(selection);
        MainFrame.setCurrentTransition(this);
    }
}
