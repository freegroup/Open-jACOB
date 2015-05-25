package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import java.sql.ResultSet;

import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

public class SystemMessage extends _dbSystemMessage
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public static String get(String _label, Language _lang)
    {
		SystemMessage result = new SystemMessage();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemMessage.findByLabelAndLanguage");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT message FROM SystemMessage WHERE label='"+_label+"' AND language_id='"+_lang.getId()+"' ");
				if(s.next())
				{
					return s.getString(1);
				}
			}
			catch(Exception exc)
			{
				trace.error(exc);
			}
			stmt.close();

		}
		catch (Exception ex)
		{
			trace.error(ex);
		}

        // report the error. It must be a must to find the message text.
        // This part of code should never be reached!!!!
        //
        if(_lang==null)
            trace.fatal("unable to load message text for ["+_label+"; null]");
        else
            trace.fatal("unable to load message text for ["+_label+"; "+_lang.getName()+"]");

		return "unknown message text";
    }

	/**
	 */
	public String toString()
	{
        return message;
    }
}