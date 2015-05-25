package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import de.shorti.util.basic.*;


public class StockQuote extends _dbStockQuote
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

	static public StockQuote createInstance(String  _wkn)
	{
		return _dbStockQuote.createInstance(0,0,0,"",0,0,_wkn,"",0,"",0,0,"");
	}

    public void incWarningCount()
    {
        setWarningCount(warningCount,true);
    }
}