package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import java.util.HashMap;

import de.shorti.context.basis.GenericContext;
import de.shorti.message.Message;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

public class PatrolStationLocator  extends GenericContext
{
   static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public PatrolStationLocator()
    {
        super("./context/PatrolStationLocator.xml");
    }

	public int onStart2City(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        m.setResponse("Konnte leider keine Tankstelle finden");
        m.setStatusWarning();

        return REPLY;
   }
}
