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
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

public class Alias extends _dbAlias
{
	/**
	 *  Method:      findByAlias(String _alias)
	 *  Description:
	 *  Returns:     Alias
	 */
	public static Alias findByShortiUserANDAlias(_dbShortiUser _shortiUser, String _alias)
	{
		Alias result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAlias.findByAlias");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id ,question FROM Alias WHERE alias='"+toSQL(_alias)+"' AND  shortiUser_id='"+_shortiUser.getId()+"'");
				if(s.next())
				{
					String _tmpID = s.getString(1);
                    result = (Alias)getFromCache(_tmpID);
                    if(result==null)
                    {
                        result = new Alias();
                        result.id = _tmpID;
                        result.question= s.getString(2);
                        result.alias= _alias;
                        result.shortiUser_id= _shortiUser.getId();
                        putToCache(result);
                    }
				}
			}
			catch(Exception exc)
			{
				System.err.println(exc);
			}
            finally
            {
    			stmt.close();
            }

		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
		return result;
	}
}