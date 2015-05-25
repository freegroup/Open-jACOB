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
import de.shorti.util.basic.*;
import de.shorti.message.Message;
import java.util.*;

/**
 * This context is responsible for radio station frequenzes.
 * All questions relatet to radio station frequenzes will be answerd
 * by this context.
 *
 */
public class RadioFrequenz  extends GenericContext
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public RadioFrequenz()
    {
        super("./context/RadioFrequenz.xml");
    }

	public int onStart2Sender(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        m.setResponse( "Konnte die Frequenz des Radiosenders in dieser Region nicht bestimmen");
        m.setStatusWarning();

        return REPLY;
   }
}