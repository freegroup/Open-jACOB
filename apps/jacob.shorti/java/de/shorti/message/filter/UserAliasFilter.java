package de.shorti.message.filter;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.baseknowledge.objects.Alias;
import de.shorti.message.Message;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

public class UserAliasFilter implements IMessageFilter
{
    static TraceDispatcher  trace   = TraceFactory.getTraceDispatcher();

    public boolean onBeginProcess(Message m)
    {
        // check/replace first the user alias
        //
        Alias alias = Alias.findByShortiUserANDAlias( m.getShortiUser(),m.getQuestion());
        if(alias!=null)
        {
            trace.debug1(m,"user alias found. replace '"+m.getQuestion()+"' with '"+alias.getQuestion()+"'");
            m.setQuestion(alias.getQuestion());
        }
        return true;
    }

    public boolean onEndProcess(Message m)
    {
        return true;
    }
}