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
import java.util.Iterator;

import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

public class CommunicationChannel extends _dbCommunicationChannel
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();
    ChannelProperty mptp = null;

	static public CommunicationChannel createInstance(String  _connectionString, String  _className, _dbShortiUser _shortiUser )
    {
        return _dbCommunicationChannel.createInstance(0, 0, _connectionString, _className, _shortiUser);
    }

	/**
	 *  Method:      findByShortiUserAndType(_dbShortiUser _shortiUser, int id)
	 *  Description:
	 *  Returns:     CommunicationChannel
	 */
	public static CommunicationChannel findByShortiUserAndType(_dbShortiUser _shortiUser, String _className)
	{
		CommunicationChannel result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("CommunicationChannel.findByShortiUserAndId");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT connectionString, shortiUser_id, id FROM CommunicationChannel WHERE shortiUser_id='"+_shortiUser.getId()+"' AND className='"+_className+"' order by generatedId desc");
				while(s.next())
				{
					result = new CommunicationChannel();
					result.connectionString=s.getString(1);
					result.shortiUser_id=s.getString(2);
					result.id=s.getString(3);
					result.className=_className;
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

    // Mobile Phone Telematics Protocol
    // protocol to request status information about the position of the
    // cell phone......
    //
    public boolean getCanMPTP()
    {
        if(mptp==null)
        {
            ChannelProperty tmp =null;
            Iterator iter= getChannelProperties().iterator();
            while(iter.hasNext())
            {
                tmp = (ChannelProperty)iter.next();
                System.out.println(tmp);
                if(tmp.getName().equals("MPTP"))
                {
                    mptp= tmp;
                    return true;
                }
            }
            return false;
        }
        return true;
    }

   // Mobile Phone Telematics Protocol
    // protocol to request status information about the position of the
    // cell phone......
    //
    public void setCanMPTP(int value)
    {
        // reset
        if(value==0)
        {
            if(getCanMPTP())
            {
                mptp.destroy();
                mptp=null;
            }
        }
        // set
        else
        {
            if(!getCanMPTP())
            {
                mptp=ChannelProperty.createInstance("MPTP",this,"1");
            }
        }
    }
}