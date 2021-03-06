package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:22 GMT+02:00 2002
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

class _dbRequestLog extends dbObject 
{

	long      insertDate;
	String    message;
	String    request_id;	// Foreign Key pointing to Table [Request], Field [id]


	/**
	 * destroy a object in the database
	 */
	public boolean destroy()
	{
		boolean result = false;
		String    _key   = id;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM RequestLog WHERE id='"+_key+"'");
					result = true;
					removeFromCache(this);
					id     = null;
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}



	/**
	 * Method:      getInsertDate()
	 * Description: 
	 * Returns:     long
	 */
	public long getInsertDate()
	{
		return insertDate;
	}

	/**
	 * Method:      setInsertDate(long _insertDate, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setInsertDate(long _insertDate, boolean _autoCommit)
	{
		insertDate=_insertDate;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getMessage()
	 * Description: 
	 * Returns:     String
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Method:      setMessage(String _message, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setMessage(String _message, boolean _autoCommit)
	{
		message=_message;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getId()
	 * Description: 
	 * Returns:     String
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Method:      getRequest()
	 * Description: 
	 * Returns:     Request
	 */
	public Request getRequest()
	{
		return Request.findById(request_id);
	}

	/**
	 * Method:      setRequest()
	 * Description: 
	 * Returns:     void
	 */
	public void setRequest(Request _foreigner, boolean _autocommit)
	{
		request_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      findByInsertDate(long _insertDate)
	 *  Description: 
	 *  Returns:     ArrayList<RequestLog>
	 */
	public static ArrayList findByInsertDate(long _insertDate)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.findByInsertDate");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, insertDate, message, request_id FROM RequestLog WHERE insertDate="+_insertDate+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					RequestLog newObject = (RequestLog)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new RequestLog();
						newObject.insertDate=s.getLong(2);
						newObject.message=s.getString(3);
						newObject.request_id=s.getString(4);
						newObject.id=_tmpID;
						putToCache(newObject);
					}
					result.add(newObject);
				}
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 *  Method:      findByMessage(String _message)
	 *  Description: 
	 *  Returns:     ArrayList<RequestLog>
	 */
	public static ArrayList findByMessage(String _message)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.findByMessage");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, insertDate, message, request_id FROM RequestLog WHERE message='"+toSQL(_message)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					RequestLog newObject = (RequestLog)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new RequestLog();
						newObject.insertDate=s.getLong(2);
						newObject.message=s.getString(3);
						newObject.request_id=s.getString(4);
						newObject.id=_tmpID;
						putToCache(newObject);
					}
					result.add(newObject);
				}
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 *  Method:      findByRequest(_dbRequest _request)
	 *  Description: 
	 *  Returns:     ArrayList<RequestLog>
	 */
	public static ArrayList findByRequest(_dbRequest _request)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.findByRequest");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, insertDate, message, request_id FROM RequestLog WHERE request_id='"+toSQL(((_request==null)?"":_request.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					RequestLog newObject = (RequestLog)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new RequestLog();
						newObject.insertDate=s.getLong(2);
						newObject.message=s.getString(3);
						newObject.request_id=s.getString(4);
						newObject.id=_tmpID;
						putToCache(newObject);
					}
					result.add(newObject);
				}
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 *  Method:      findById(String _id)
	 *  Description: 
	 *  Returns:     RequestLog
	 */
	public static RequestLog findById(String _id)
	{
		RequestLog result = (RequestLog)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, insertDate, message, request_id FROM RequestLog WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new RequestLog();
					result.insertDate= s.getLong(2);
					result.message= s.getString(3);
					result.request_id= s.getString(4);
					result.id= _id;
					putToCache(result);
				}
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * returns boolean
	 */
	public boolean commit()
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.commit");
			try
			{
				stmt.executeUpdate( "UPDATE RequestLog set insertDate= "+insertDate+", message= '"+toSQL(message)+"', request_id= '"+toSQL(request_id)+"' WHERE id='"+id+"'");
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return true;
	}


	/**
	 * returns ArrayList<RequestLog>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, insertDate, message, request_id FROM RequestLog order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					RequestLog newObject = (RequestLog)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new RequestLog();
						newObject.insertDate=s.getLong(2);
						newObject.message=s.getString(3);
						newObject.request_id=s.getString(4);
						newObject.id=_tmpID;
						putToCache(newObject);
					}
					result.add(newObject);
				}
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}


	/**
	 * create a new object in the database
	 */
	static public RequestLog createInstance( long  _insertDate, String  _message, _dbRequest _request )
	{
		RequestLog result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO RequestLog ( insertDate, message, request_id, id) VALUES ("+_insertDate+",  '"+toSQL(_message)+"',  '"+((_request==null)?"":_request.getId())+"', '"+nextGUID+"')");
				result = new RequestLog();
				result.insertDate= _insertDate;
				result.message= _message;
				result.request_id= (_request==null)?"":_request.getId();
				result.id= nextGUID;
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}


	/**
	 * create a new object in the database
	 */
	static public void newInstance( long  _insertDate, String  _message, _dbRequest _request )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO RequestLog ( insertDate, message, request_id, id) VALUES ("+_insertDate+",  '"+toSQL(_message)+"',  '"+((_request==null)?"":_request.getId())+"', '"+nextGUID+"')");
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
	}


	/**
	 * destroy a object in the database
	 */
	static public boolean destroyInstance( String  _key)
	{
		boolean result = false;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM RequestLog WHERE id='"+_key+"'");
					result = true;
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}


	/**
	 * destroy ALL objects in the database
	 */
	static public boolean destroyAll()
	{
		boolean result = false;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbRequestLog.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from RequestLog" );
					result = true;
			}
			catch(Exception exc)
			{
				System.err.println(exc);
				exc.printStackTrace();
			}
			stmt.close();
		
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		return result;
	}


	/**
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer(1024);
		result.append(insertDate);
		result.append("|");
		result.append(message);
		result.append("|");
		result.append(request_id);
		result.append("|");
		result.append(id);
		return result.toString();
	}


	/**
	 * init the class
	 */
	static
	{
		try
		{
			DatabaseMetaData dMeta = ConnectionManager.getValid().getMetaData ();
			ResultSet result;
			result=dMeta.getColumns(null,null,"RequestLog","insertDate");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'insertDate' in table 'RequestLog' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"RequestLog","message");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'message' in table 'RequestLog' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"RequestLog","request_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'request_id' in table 'RequestLog' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"RequestLog","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'RequestLog' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
