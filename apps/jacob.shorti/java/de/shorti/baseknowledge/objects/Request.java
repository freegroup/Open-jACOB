package de.shorti.baseknowledge.objects;

/**
 * short-i Class generated by automatic ClassGenerator
 * Date: Wed Mar 14 14:59:41 GMT+01:00 2001
 */
import de.shorti.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

public class Request extends _dbRequest
{
    public static final int CREATED    = 100;
    public static final int PROCESSING = 200;
    public static final int SUCCESS    = 300;
    public static final int WARNING    = 400;
    public static final int ERROR      = 500;
    public static final int FATAL      = 600;

    public Request()
    {
        finishDate=0;
        startDate=0;
    }

	static public Request createInstance(  CommunicationChannel _channel ,String  _question)
    {
    	return createInstance( null, now(),_question, -1, CREATED, null, _channel, null,null);
    }

	public void setStatus(int _status, boolean _autoCommit)
    {
        // it is only possible to set the status 1 greater or equal. That means,
        // that is not possible to set the status from FATAL -> SUCCESS or
        // SUCCESS -> PROCESSING and so on
        //
        if(_status >= status)
        {
            super.setStatus(_status,_autoCommit );
        }
        else
        {
            String from = statusString(status);
            String to   = statusString(_status);

            RequestLog.createInstance("status switch from "+from+"->"+to+" not possible",this);
        }
    }


    public long getTimeForProcess()
    {
        return getFinishDate()-getStartDate();
    }


    public long getCurrentTimeForProcess()
    {
        return System.currentTimeMillis()-getStartDate();
    }

    static long now()
    {
        return new java.util.Date().getTime();
    }


    /**
     *
     */
    protected String statusString(int s)
    {
        switch(s)
        {
            case CREATED:
                return "CREATED";
            case PROCESSING:
                return "PROCESSING";
            case SUCCESS:
                return "SUCCESS";
            case WARNING:
                return "WARNING";
            case ERROR:
                return "ERROR";
            case FATAL:
                return "FATAL";
        }
        return "unknown";
    }
}
