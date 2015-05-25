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
import java.net.*;
import java.awt.*;
import java.util.*;

public class ContextNode extends FREEArea
{
    protected FREEText      m_label  = null;
    public    ContextPort   m_port   = null;

    public ContextNode(Point point, String s)
    {
        initialize(point,s);
    }

    public void initialize(Point point, String s)
    {
        setLocation( point);
        setSelectable(false);
        setGrabChildSelection(true);
        setDraggable(true);
        setResizable(false);


        m_port = new ContextPort();
        m_port.setLocation(point);
        m_port.setSize(15, 15);
        m_port.setStyle(m_port.StyleEllipse);
        if(s.equalsIgnoreCase("start"))
        {
            m_port.setPen(FREEPen.makeStockPen(Color.cyan));
            m_port.setBrush(FREEBrush.makeStockBrush(Color.red));
        }
        addObjectAtTail(m_port);

        if(s != null)
        {
            m_label = new FREEText(s);
            m_label.setSpotLocation(FREEObject.TopCenter,m_port,FREEObject.BottomCenter );
            m_label.setSelectable(false);
            m_label.setDraggable(false);
            m_label.setAlignment(FREEText.ALIGN_CENTER);
            m_label.setTransparent(true);
            addObjectAtTail(m_label);
        }
    }

    public String toString()
    {
        Set out = new HashSet();

        StringBuffer sb = new StringBuffer(1024);
        sb.append("\t\t\t<node name=\""+m_label.getText()+"\" x=\""+(int)m_port.getLocation().getX()+"\" y=\""+(int)m_port.getLocation().getY()+"\">\n");
        FREEListPosition pos= m_port.getFirstLinkPos();
        if(pos!=null)
        {
            FREELink link = m_port.getLinkAtPos(pos);
            while (link!=null)
            {
                if(!out.contains(link) && link.getFromPort()==m_port)
                {
                    sb.append(link);
                    out.add(link);
                }
                pos = m_port.getNextLinkPos(pos);
                link = m_port.getLinkAtPos(pos);
            }
        }
        sb.append("\t\t\t</node>\n");
        return sb.toString();
    }
}
