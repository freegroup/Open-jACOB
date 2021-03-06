package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:22 GMT+02:00 2002
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

class _dbContextRegistry extends dbObject 
{

	String    name;


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
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ContextRegistry WHERE id='"+_key+"'");
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
	 * Method:      getName()
	 * Description: 
	 * Returns:     String
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Method:      setName(String _name, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setName(String _name, boolean _autoCommit)
	{
		name=_name;
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
	 *  Method:      getAdvertismentAmounts()
	 *  Description:                              
	 *  Returns:     ArrayList<AdvertismentAmount>
	 */
	public ArrayList getAdvertismentAmounts()
	{
		return _dbAdvertismentAmount.findByContextRegistry(this);
	}


	/**
	 *  Method:      getContextAmountItems()
	 *  Description:                              
	 *  Returns:     ArrayList<ContextAmountItem>
	 */
	public ArrayList getContextAmountItems()
	{
		return _dbContextAmountItem.findByContextRegistry(this);
	}


	/**
	 *  Method:      getContextPerformances()
	 *  Description:                              
	 *  Returns:     ArrayList<ContextPerformance>
	 */
	public ArrayList getContextPerformances()
	{
		return _dbContextPerformance.findByContextRegistry(this);
	}


	/**
	 *  Method:      getHistoryValues()
	 *  Description:                              
	 *  Returns:     ArrayList<HistoryValue>
	 */
	public ArrayList getHistoryValues()
	{
		return _dbHistoryValue.findByContextRegistry(this);
	}


	/**
	 *  Method:      getRequests()
	 *  Description:                              
	 *  Returns:     ArrayList<Request>
	 */
	public ArrayList getRequests()
	{
		return _dbRequest.findByContextRegistry(this);
	}


	/**
	 *  Method:      findByName(String _name)
	 *  Description: 
	 *  Returns:     ContextRegistry
	 */
	public static ContextRegistry findByName(String _name)
	{
		ContextRegistry result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.findByName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, name FROM ContextRegistry WHERE name='"+toSQL(_name)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (ContextRegistry)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new ContextRegistry();
						result.name= s.getString(2);
						result.id= _tmpID;
						putToCache(result);
					}
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
	 *  Returns:     ContextRegistry
	 */
	public static ContextRegistry findById(String _id)
	{
		ContextRegistry result = (ContextRegistry)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, name FROM ContextRegistry WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new ContextRegistry();
					result.name= s.getString(2);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.commit");
			try
			{
				stmt.executeUpdate( "UPDATE ContextRegistry set name= '"+toSQL(name)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<ContextRegistry>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, name FROM ContextRegistry order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ContextRegistry newObject = (ContextRegistry)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ContextRegistry();
						newObject.name=s.getString(2);
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
	static public ContextRegistry createInstance( String  _name )
	{
		ContextRegistry result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ContextRegistry ( name, id) VALUES ( '"+toSQL(_name)+"', '"+nextGUID+"')");
				result = new ContextRegistry();
				result.name= _name;
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
	static public void newInstance( String  _name )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ContextRegistry ( name, id) VALUES ( '"+toSQL(_name)+"', '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ContextRegistry WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbContextRegistry.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from ContextRegistry" );
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
		result.append(name);
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
			result=dMeta.getColumns(null,null,"ContextRegistry","name");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'name' in table 'ContextRegistry' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ContextRegistry","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'ContextRegistry' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
