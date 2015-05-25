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

public class PlayListEntry extends _dbPlayListEntry
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

	/**
	 *  Method:      findByRadioStationAndTime(_dbRadioStation _radioStation)
	 *  Description:
	 *  Returns:     ArrayList<PlayListEntry>
	 */
	public static ArrayList findByRadioStationAndTime(_dbRadioStation _radioStation, long time, long timeOffset)
	{
		SaveStatement  stmt;
		ArrayList result = new ArrayList();
		ArrayList temp = new ArrayList();
        long startTime = time - timeOffset;
        long endTime = time + timeOffset;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbPlayListEntry.findByRadioStaion");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT album, radioStation_id, interpret, startTime, title, id FROM PlayListEntry WHERE radioStation_id='"+_radioStation.getId()+"'"+" order by starttime");
				while(s.next())
				{
					PlayListEntry newObject = new PlayListEntry();
					newObject.album=s.getString(1);
					newObject.radioStation_id=s.getString(2);
					newObject.interpret=s.getString(3);
					newObject.startTime=s.getLong(4);
					newObject.title=s.getString(5);
					newObject.id=s.getString(6);
					temp.add(newObject);
				}
			}
			catch(Exception exc)
			{
				trace.error(exc);
			}
			stmt.close();

            Iterator it = temp.iterator();
            while(it.hasNext())
            {
                PlayListEntry entry = (PlayListEntry) it.next();
                if(entry.getStartTime() >= startTime && entry.getStartTime() <= endTime)
                {
                    result.add(entry);
                }
            }
		}
		catch (Exception ex)
		{
			trace.error(ex);
		}
		return result;
	}
}