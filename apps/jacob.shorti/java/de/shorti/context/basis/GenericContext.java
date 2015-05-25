package de.shorti.context.basis;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.baseknowledge.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.util.basic.*;
import de.shorti.message.Message;
import de.shorti.util.basic.*;
import org.apache.oro.text.regex.*;
import java.util.*;
import java.util.Locale;
import de.shorti.Shorti;
import org.apache.oro.text.regex.*;
import de.shorti.*;
import de.shorti.util.basic.*;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import de.shorti.message.Message;
import de.shorti.baseknowledge.objects.CommunicationChannel;
import de.shorti.baseknowledge.objects.SystemStatistik;
import de.shorti.baseknowledge.objects.ContextAmountItem;
import java.util.Comparator;
import java.util.Collections;
// Imported TraX classes
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
// Imported java classes
import java.io.*;
import java.lang.reflect.*;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.*;

import org.apache.oro.text.regex.*;
/**
 *
 */
class StatusNode
{
	static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public StatusNode(String _nodeName)
    {
        nodeName = _nodeName;
    }

    public Transition getTransition(String question)
    {
        Iterator iter = transitions.iterator();
        while(iter.hasNext())
        {
            Transition t=(Transition)iter.next();
            if(t.matcher.matches(question, t.perl5Pattern))
                return t;
        }
        return null;
    }

    public void addTransition(StatusNode fromNode, StatusNode toNode, String pattern, String _defaultMessage)
    {
        Transition t = new Transition(fromNode, toNode, pattern, _defaultMessage);
        transitions.add(t);
    }

    public String toString()
    {
        StringBuffer sb= new StringBuffer();
        sb.append( "[");
        sb.append(nodeName);
        sb.append("]\n");

        Iterator iter = transitions.iterator();
        while(iter.hasNext())
        {
            Transition t=(Transition)iter.next();
            sb.append("\t"+t+"\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public ArrayList  transitions = new ArrayList();  // Pattern -> Transition
    public String     nodeName    = "";
}

/**
 *
 */
class Transition
{
	static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

	protected static Perl5Compiler   compiler   = new Perl5Compiler();
	protected static Perl5Matcher    matcher    = new Perl5Matcher();

    public Transition(StatusNode _fromNode,StatusNode _toNode,String _pattern,  String _defaultMessage)
    {
        fromNode       = _fromNode;
        toNode         = _toNode;
        defaultMessage = _defaultMessage;
        stringPattern  = _pattern;
        eventName      = "on"+firstUpper(fromNode.nodeName)+"2"+firstUpper(toNode.nodeName);
		try
		{
            perl5Pattern = compiler.compile(_pattern);
		}
		catch(MalformedPatternException e)
		{
			trace.error("Bad pattern: "+_pattern);
			trace.error(e);
		}
    }

    public String getTransitionFunction()
    {
        return eventName;
    }

    public String toString()
    {
        return fromNode.nodeName+"->"+toNode.nodeName+"       "+stringPattern;
    }

    static String firstUpper(String prop)
    {
       return new String(new String(""+prop.charAt(0)).toUpperCase()  + prop.substring(1));
    }

    public StatusNode fromNode;
    public StatusNode toNode;
    public String     defaultMessage;
    public String     eventName;
    public String     stringPattern;
    public Pattern    perl5Pattern;
}

public abstract class GenericContext
{
    // possible return values for the context
    //
    public final static int REPLY             = 1;
    public final static int NO_HIT            = 2;
    public final static int RENDER_OUTPUT     = 3;

    private static final Class[]  CALLBACK_SIGNATURE =  { HashMap.class, String[].class , Message.class};

	protected HashMap    currentNode     = new HashMap();
	protected HashMap    dataMap         = new HashMap();
	protected HashMap    responseItems   = new HashMap();
    protected HashMap    methodCallbacks = new HashMap();
	protected SystemStatistik requestCount  = null;

    protected String     className = "";
    protected HashMap    nodes = new HashMap();
    protected StatusNode startNode =null;

    protected long       xmlFileTimestamp;
    protected String     xmlFileName;

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    protected ContextRegistry context= null;


    public GenericContext(String xmlFile)
    {
        className = getClass().getName();

        // register the context in the Database
        //
        context = ContextRegistry.findByName(className);
        if(context == null)
            context = ContextRegistry.createInstance(getClass().getName());

        // crete the item for the SystemStatistik
        //
        requestCount = SystemStatistik.findByName(className);
        if(requestCount==null)
            requestCount = SystemStatistik.createInstance(className,0);

        loadXmlDescFile(xmlFile);
    }

    protected void loadXmlDescFile(String xmlFile)
    {
        currentNode   = new HashMap();
        dataMap       = new HashMap();
        responseItems = new HashMap();

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        builder = factory.newDocumentBuilder();
            File file        = new File (xmlFile);
            xmlFileName      = xmlFile;
            xmlFileTimestamp = file.lastModified();
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
                nodes.put(fromNode, new StatusNode(fromNode));
                if(fromNode.equalsIgnoreCase("start"))
                    startNode=(StatusNode)nodes.get(fromNode);
            }

            // create all forwards
            //
            rootList= root.getElementsByTagName("node");
            for(int nodeIndex=0 ; rootList.getLength() >nodeIndex ;nodeIndex++)
            {
                NodeList forwardList = ((Element)rootList.item(nodeIndex)).getElementsByTagName("forward");
                String   fromNode    = rootList.item(nodeIndex).getAttributes().getNamedItem("name").getNodeValue();
                StatusNode sNodeFrom = (StatusNode)nodes.get(fromNode);
                for(int forwardIndex=0 ; forwardList.getLength() >forwardIndex ;forwardIndex++)
                {
                    NodeList alternative = ((Element)forwardList.item(forwardIndex)).getElementsByTagName("alternative");
                    NodeList questionList = ((Element)forwardList.item(forwardIndex)).getElementsByTagName("question");
                    String toNode        = forwardList.item(forwardIndex).getAttributes().getNamedItem("toNode").getNodeValue();

                    StatusNode sNodeTo =(StatusNode) nodes.get(toNode);

                    for(int questionIndex=0 ; questionList.getLength() >questionIndex ;questionIndex++)
                    {
                        String pattern       = questionList.item(questionIndex).getAttributes().getNamedItem("pattern").getNodeValue();
                        String defaultAnswer = questionList.item(questionIndex).getAttributes().getNamedItem("defaultAnswer").getNodeValue();
                        sNodeFrom.addTransition(sNodeFrom, sNodeTo,pattern,defaultAnswer);
                        ArrayList alterVec = null;
                        for(int alterIndex=0 ; alternative.getLength() >alterIndex ;alterIndex++)
                        {
                            String alterNode = alternative.item(alterIndex).getAttributes().getNamedItem("node").getNodeValue();
                        }
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
            e.printStackTrace();
        }
        catch (Throwable t)
        {
            t.printStackTrace ();
        }
    }

	protected void setAlternative(Message m, int node) throws java.lang.IllegalStateException
	{
		throw new java.lang.IllegalStateException();
	}

	protected void renderOutput(Message m)
	{
		try
		{
            String XSL = "./xsl/"+className+"/FORMAT_"+m.getFormatType()+".xsl";
            trace.debug1("using XSLT-template ["+XSL+"] to render output");

		    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		    TransformerFactory tFactory = TransformerFactory.newInstance();
		    Transformer transformer = tFactory.newTransformer(new StreamSource(new File(XSL)));
		    StringReader sr= new StringReader(toXML(m));
		    transformer.transform(new StreamSource(sr), new StreamResult(outStream));
		    m.setResponse(outStream.toString());
		}
		catch (Exception ex)
		{
            ex.printStackTrace();
		    trace.error(ex);
		}
	}

	protected String toXML(Message m)
	{
		Iterator iter = ((ArrayList)responseItems.get(m.getShortiUser().getId())).iterator();
		StringBuffer sb =new StringBuffer();
		sb.append("<?xml version=\"1.0\"  encoding=\"ISO-8859-1\"?>\n");
		sb.append("<ResponseItems>\n");
		while(iter.hasNext())
		{
			Object item= iter.next();
            Field fields[]= item.getClass().getDeclaredFields();
            sb.append("\t<Item ");
            for(int i=0; i<fields.length; i++)
            {
                try
                {
        			sb.append(fields[i].getName()+"=\""+fields[i].get(item)+"\" ");
                }
                catch (Exception ex)
                {
                }
            }
			sb.append(" />\n");
		}

        // add user information
        //

        // add request information
        //
		sb.append("\t<RequestInformation processTime=\""+toSeconds(m.getCurrentTimeForProcess())+"\" />\n");
		sb.append("</ResponseItems>\n");
		return sb.toString();
	}


    /**
     *
     */
	final public boolean process(Message m)
	{
        long startTime = System.currentTimeMillis();
        try
        {
            trace.debug1(m, "checking '"+className+"'");
            // check if it neccessary to reload the XML file with regular expressions
            //
            File f= new File(xmlFileName);
            if(f.lastModified() != xmlFileTimestamp)
                loadXmlDescFile(xmlFileName);

            // try to find a answer of the question
            //
            StatusNode node = (StatusNode)currentNode.get(m.getShortiUser().getId());
            int result= tryResponse(m);

            // 'restart' the context if no transition found and the current node of the context is NOT
            // the start node.
            //
            if((result==NO_HIT) && (node!=null) && (node!= startNode))
            {
                trace.debug1("reset context and reentry into '"+className+"'");
                currentNode.put(m.getShortiUser().getId(),startNode);
                dataMap.put(m.getShortiUser().getId(),new HashMap());
                result =  tryResponse(m);
            }

            if(result!=NO_HIT)
            {
                // increment some statistical values
                requestCount.incValue();
                return true;
            }
        }
        finally
        {
            // insert performance data for the context processing
            //
            ContextPerformance.newInstance(System.currentTimeMillis()-startTime,m.getRequest(),context);
        }

		return false;
	}


    /**
     *
     */
	private int tryResponse(Message m)
	{
		try
		{
			StatusNode node        = (StatusNode) currentNode.get(m.getShortiUser().getId());
			HashMap    userDataMap = (HashMap)    dataMap.get(m.getShortiUser().getId());
			if((node==null) || (userDataMap==null))
			{
				node = startNode;
				userDataMap = new HashMap();
				currentNode.put(m.getShortiUser().getId(),node);
				dataMap.put(m.getShortiUser().getId(),userDataMap);
			}

            Transition t= node.getTransition(m.getQuestion());
            if(t!=null)
            {
                trace.debug1(m, "transition found "+t+". Retrieving matching elements");
                MatchResult match = t.matcher.getMatch();
                int r      = NO_HIT;
                int groupCount = match.groups();
                String[] groups = new String[groupCount-1];
                for(int loop=0 ; loop<(groupCount-1);loop++)
                    groups[loop] = match.group(loop+1);
                trace.debug1(m, "retrieving matching elements done.");
                m.setResponse(t.defaultMessage);
                responseItems.put(m.getShortiUser().getId(),new ArrayList());
                try
                {
                    Object[] data  =  {userDataMap, groups, m};
                    Method method = (Method)methodCallbacks.get(t.getTransitionFunction());
                    if(method==null)
                    {
                        method = getClass().getMethod(t.getTransitionFunction(), CALLBACK_SIGNATURE);
                        methodCallbacks.put(t.getTransitionFunction(),method);
                    }
                    trace.debug2(m, "calling '"+t.getTransitionFunction()+"'");
                    r=Integer.parseInt(method.invoke(this, data).toString());
                }
                catch (Exception ex)
                {
                    r=REPLY;
                    // ignore the exception
                }

                if((StatusNode)currentNode.get(m.getShortiUser().getId())==node)
                {
                    trace.debug2("switch current node from user ["+m.getShortiUser().getId()+ "] to ["+t.toNode.nodeName+"]");
                    currentNode.put(m.getShortiUser().getId(),t.toNode);
                }

                switch(r)
                {
                    case RENDER_OUTPUT:
                        // on success render output
                        //
                        trace.debug2(m,"start off rendering the output with XSLT");
                        renderOutput(m);
                        trace.debug2(m,"rendering done.");
                        r = REPLY;
                        userDataMap.put("LAST_VALID_RESPONSE",m.getResponse());
                        break;
                    case REPLY:
                        userDataMap.put("LAST_VALID_RESPONSE",m.getResponse());
                        break;
//                    case SILENT:
//                        break;
                }
                return r;
            }
		}
		catch(Exception e)
		{
			trace.error(e.getMessage());
			trace.error(e.getClass().getName());
		}
		currentNode.put(m.getShortiUser().getId(),startNode);
		return NO_HIT;
	}

	final protected void sortResultArrayList(Message m, Comparator comp)
	{
		ArrayList items = (ArrayList)responseItems.get(m.getShortiUser().getId());
		Collections.sort(items, comp);
	}

	final protected String getLastValidResponse(HashMap userDataMap)
	{
		return (String)userDataMap.get("LAST_VALID_RESPONSE");
	}

    public ContextRegistry getContextRegistry()
    {
        return context;
    }

    public void addResponseItem(Message m, Object obj)
    {
		((ArrayList)responseItems.get(m.getShortiUser().getId())).add(obj);
    }

   /**
     *
     */
    public boolean isAdmin(Message m)
    {
        if(m.getChannel().getIsAdmin()==0)
            return false;
        return true;
    }

    /**
     * only to fit a number from '1234567689'  to '123456.7'
     * ...userful for some outputs.
     */
    private static double toSeconds(long time)
    {
        double value = time;

        int distInt = (int) (value / 100.0);
        value = (double) distInt / 10.0;

        if((((int)(value * 10.0)) % 10) == 0)
        {
            return (double)(int)value;
        }

        return value;
    }


    public static void main(String[] args)
    {
    }
}