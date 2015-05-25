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
import de.shorti.baseknowledge.objects.*;
import de.shorti.*;
import de.shorti.util.basic.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import de.shorti.message.Message;

public class AcronymResolver  extends GenericContext
{
    public class ResponseItem
    {
        public String acronym;
        public String description;
    }

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public AcronymResolver()
    {
        super("./context/AcronymResolver.xml");
    }

    /**
     *
     */
    public int onStart2End(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        String acronym = matchedGroups[0];

		trace.debug1("try to resolve acronym ["+acronym+"]");
        Iterator iter = Acronym.findByAcronym(acronym).iterator();

        // Der Benutzer hat nicht nach einem gültigen Acronym gefragt.
        // Es besteht auch die Möglichkeit, dass er eine Frage gestellt
        // hat wie z.B "was bedeutet dass" - welches keine Frage nach der Bedeutung
        // des Wortes 'dass' war.
        //
        if(!iter.hasNext())
            return NO_HIT;

        while(iter.hasNext())
        {
            ResponseItem item = new ResponseItem();
            Acronym acr = (Acronym)iter.next();
            item.acronym     = acr.getAcronym();
            item.description = acr.getDescription();
            addResponseItem(m,item);
        }
		return RENDER_OUTPUT;
	}
}
