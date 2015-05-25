package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.db.*;
import de.shorti.util.basic.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

public class SubscribedAction extends _dbSubscribedAction
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    /**
     *
     */
    public void incSendCount()
    {
        setSendCount(sendCount+1,true);
    }

	public static ArrayList findByChannelAndAction(CommunicationChannel _channel, Action _action)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSubscribedAction.findByChannel");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT minuteInterval, lastDate, id, startDate, lockStatus, formatType, sendCount, endDate FROM SubscribedAction WHERE channel_id='"+_channel.getId()+"' AND action_id='"+_action.getId()+"' order by generatedId desc");
				while(s.next())
				{
					SubscribedAction newObject = new SubscribedAction();
					newObject.minuteInterval=s.getLong(1);
					newObject.lastDate=s.getLong(2);
					newObject.id=s.getString(3);
					newObject.startDate=s.getLong(4);
					newObject.lockStatus=s.getInt(5);
					newObject.formatType=s.getString(6);
					newObject.sendCount=s.getInt(7);
					newObject.endDate=s.getLong(8);
					newObject.channel_id=_channel.getId();
					newObject.action_id=_action.getId();
					result.add(newObject);
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
		return result;
	}
}