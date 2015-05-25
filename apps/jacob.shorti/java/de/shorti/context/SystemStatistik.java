package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.context.basis.*;
import de.shorti.baseknowledge.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.message.Message;
import de.shorti.util.basic.*;
import java.util.*;

public class SystemStatistik  extends GenericContext
{
    public class ResponseItem
    {
        public String name;
        public String value;
    }

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public SystemStatistik()
    {
        super("./context/SystemStatistik.xml");
    }

    /**
     *
     */
	public int onStart2MessageCount(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        // send only the system statistik if the channel has the permissions
        //
        if(isAdmin(m)==false)
            return NO_HIT;

        de.shorti.baseknowledge.objects.SystemStatistik stat = null;

        stat = de.shorti.baseknowledge.objects.SystemStatistik.findByName("HTTP received count");
        if(stat!= null)
        {
            ResponseItem item = new ResponseItem();
            item.name = "HTTPreceive";
            item.value= ""+stat.getValue();
            addResponseItem(m,item);
        }

        stat = de.shorti.baseknowledge.objects.SystemStatistik.findByName("HTTP send count");
        if(stat!= null)
        {
            ResponseItem item = new ResponseItem();
            item.name = "HTTPsend";
            item.value= ""+stat.getValue();
            addResponseItem(m,item);
        }

        stat = de.shorti.baseknowledge.objects.SystemStatistik.findByName("SMS received count");
        if(stat!= null)
        {
            ResponseItem item = new ResponseItem();
            item.name = "SMSreceive";
            item.value= ""+stat.getValue();
            addResponseItem(m,item);
        }

        stat = de.shorti.baseknowledge.objects.SystemStatistik.findByName("SMS send count");
        if(stat!= null)
        {
            ResponseItem item = new ResponseItem();
            item.name = "SMSsend";
            item.value= ""+stat.getValue();
            addResponseItem(m,item);
        }

        stat = de.shorti.baseknowledge.objects.SystemStatistik.findByName("MAIL received count");
        if(stat!= null)
        {
            ResponseItem item = new ResponseItem();
            item.name = "MAILreceive";
            item.value= ""+stat.getValue();
            addResponseItem(m,item);
        }

        stat = de.shorti.baseknowledge.objects.SystemStatistik.findByName("MAIL send count");
        if(stat!= null)
        {
            ResponseItem item = new ResponseItem();
            item.name = "MAILsend";
            item.value= ""+stat.getValue();
            addResponseItem(m,item);
        }
		return RENDER_OUTPUT;
	}

}
