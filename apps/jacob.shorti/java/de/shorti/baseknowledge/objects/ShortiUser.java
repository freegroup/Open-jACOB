package de.shorti.baseknowledge.objects;

/**
 * short-i Class generated by automatic ClassGenerator
 * Date: Wed Mar 14 14:59:41 GMT+01:00 2001
 */

import de.shorti.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import de.shorti.baseknowledge.*;
import de.shorti.baseknowledge.objects.*;

public class ShortiUser extends _dbShortiUser
{
    /**
     *
     */
	static public ShortiUser createInstance( String  _nickname, _dbLanguage _preferedLanguage,  _dbCountry _homeCountry, _dbAccount _account )
	{
        return ShortiUser.createInstance( _nickname, _preferedLanguage ,null, _homeCountry ,_account ,null);
    }

    /**
     *
     */
    public void setHistoryValue(ContextRegistry context, String valueName, String value)
    {
        HistoryValue historyValue = HistoryValue.findByKey(this,context,valueName);
        if(historyValue==null)
        {
            historyValue = HistoryValue.createInstance(valueName,this,value,context);
        }
        historyValue.setValue(value,true);
    }

    /**
     *
     */
    public String getHistoryValue(ContextRegistry context, String valueName)
    {
        String value =null;
        HistoryValue historyValue = HistoryValue.findByKey(this,context,valueName);
        if(historyValue !=null)
        {
            value = historyValue.getValue();
        }
        return value;
    }
}
