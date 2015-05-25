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
import java.util.HashMap;
import de.shorti.message.Message;
import de.shorti.util.basic.*;


public class TelefonNummer extends GenericContext
{
	static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public TelefonNummer()
    {
        super("./context/TelefonNummer.xml");
    }

	public int onStart2Location(HashMap userDataMap, String[] matchedGroups, Message m)
	{

		return REPLY;
	}
}