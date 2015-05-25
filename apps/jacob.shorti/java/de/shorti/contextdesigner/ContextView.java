
/**
 * Title:        FREEObject
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      FreeGroup
 * @author Andreas Herz
 * @version 1.0
 */

package de.shorti.contextdesigner;


import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.NumberUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import de.shorti.contextdesigner.objects.ContextNode;
import de.shorti.contextdesigner.objects.Transition;
import de.shorti.dragdrop.FREEPort;
import de.shorti.dragdrop.FREEText;
import de.shorti.dragdrop.FREEView;


public class ContextView extends FREEView
{
    HashMap nodes = new HashMap();
    File    file  = null;

    public ContextView(String contextName, File xmlFile)
    {
        file = xmlFile;
        getDocument().addObjectAtTail(new FREEText(new Point(0,0),contextName));
        readXML(xmlFile);
    }

//    protected FREELink createTemporaryLinkForNewLink(FREEPort port, FREEPort port1)
//    {
//        return new Transition(new FREEPen(FREEPen.DOTTED,3,Color.blue), port, port1);
//    }

    public void newLink(FREEPort port, FREEPort port1)
    {
//        FREEDocument document = getDocument();
//        if(document == null)
//        {
//            return;
//        }
//        else
//        {
//            FREELink link = new Transition(new FREEPen(FREEPen.SOLID,3,Color.blue),"xxx","yyy", port, port1);
//            document.getDefaultLayer().addObjectAtTail(link);
//            return;
//        }
    }

    /**
     *
     */
    public void save()
    {
        try
        {
            file.delete();
            FileWriter writer = new FileWriter(file);
            writer.write(toString());
            writer.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }


    /**
     *
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        sb.append("<ContextList>\n");
        sb.append("\t<Context >\n");
        sb.append("\t\t<nodelist>\n");

        Iterator iter = nodes.keySet().iterator();
        while(iter.hasNext())
        {
            ContextNode node = (ContextNode)nodes.get(iter.next());
            sb.append(node.toString());
        }
        sb.append("\t\t</nodelist>\n");
        sb.append("\t</Context>\n");
        sb.append("</ContextList>\n");

        return sb.toString();
    }

    private void readXML(File file)
    {
        System.out.println(file.getName());
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        builder = factory.newDocumentBuilder();
            Document doc = builder.parse (file);

            Element		root;

            // find all Attributes and insert them in a map
            //
            root = doc.getDocumentElement ();
            NodeList rootList= root.getElementsByTagName("Context");
            // create all nodes
            //
            rootList= root.getElementsByTagName("node");
            for(int nodeIndex=0 ; rootList.getLength() >nodeIndex ;nodeIndex++)
            {
                String   fromNode    = rootList.item(nodeIndex).getAttributes().getNamedItem("name").getNodeValue();
                int      x           = 0;//NumberUtils.stringToInt(rootList.item(nodeIndex).getAttributes().getNamedItem("x").getNodeValue(),0);
                int      y           = 0;//NumberUtils.stringToInt(rootList.item(nodeIndex).getAttributes().getNamedItem("y").getNodeValue(),0);
                ContextNode node = new ContextNode(new Point(x, y), fromNode);
                nodes.put(fromNode,node);
                getDocument().addObjectAtTail(node);
            }
            // create all forwards
            //
            rootList= root.getElementsByTagName("node");
            for(int nodeIndex=0 ; rootList.getLength() >nodeIndex ;nodeIndex++)
            {
                NodeList forwardList = ((Element)rootList.item(nodeIndex)).getElementsByTagName("forward");
                String   fromNode    = rootList.item(nodeIndex).getAttributes().getNamedItem("name").getNodeValue();
                for(int forwardIndex=0 ; forwardList.getLength() >forwardIndex ;forwardIndex++)
                {
                    NodeList alternative = ((Element)forwardList.item(forwardIndex)).getElementsByTagName("alternative");
                    NodeList questionList = ((Element)forwardList.item(forwardIndex)).getElementsByTagName("question");
                    String toNode        = forwardList.item(forwardIndex).getAttributes().getNamedItem("toNode").getNodeValue();
                    int      x           = 0;//Integer.parseInt(forwardList.item(forwardIndex).getAttributes().getNamedItem("x").getNodeValue());
                    int      y           = 0;//Integer.parseInt(forwardList.item(forwardIndex).getAttributes().getNamedItem("y").getNodeValue());

                    ContextNode from = (ContextNode) nodes.get(fromNode);
                    ContextNode to   = (ContextNode) nodes.get(toNode);
                    Transition trans = new Transition(fromNode,toNode, from.m_port,to.m_port,x,y);
                    getDocument().addObjectAtHead(trans);
                    for(int questionIndex=0 ; questionList.getLength() >questionIndex ;questionIndex++)
                    {
                        String pattern       = questionList.item(questionIndex).getAttributes().getNamedItem("pattern").getNodeValue();
                        String defaultAnswer = questionList.item(questionIndex).getAttributes().getNamedItem("defaultAnswer").getNodeValue();
                        trans.addPattern(pattern, defaultAnswer);
//                        ArrayList alterVec = null;
//                        for(int alterIndex=0 ; alternative.getLength() >alterIndex ;alterIndex++)
//                        {
//                            String alterNode = alternative.item(alterIndex).getAttributes().getNamedItem("node").getNodeValue();
//                        }
                    }
                }
            }
        }
        catch (SAXParseException err)
        {
            System.out.println ("** Parsing error"
            + ", line " + err.getLineNumber ()
            + ", uri " + err.getSystemId ());
            System.out.println("   " + err.getMessage ());
        }
        catch (SAXException e)
        {
            System.out.println( e.getMessage ());

        }
        catch (Throwable t)
        {
            t.printStackTrace ();
        }
    }
}
