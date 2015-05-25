package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author
 * @version 1.0
 */
import de.shorti.context.basis.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.*;
import de.shorti.util.basic.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import de.shorti.message.Message;

public class LottoZahlen extends GenericContext
{
	static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public LottoZahlen()
    {
        super("./context/LottoZahlen.xml");
    }

	public int onStart2Start(HashMap userDataMap, String[] matchedGroups, Message m)
	{
		return REPLY;
	}
}