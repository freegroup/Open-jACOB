package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:21 GMT+02:00 2002
 */
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

class _dbSystemStatistik extends dbObject 
{

	String    name;
	int       value;


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
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM SystemStatistik WHERE id='"+_key+"'");
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
	 * Method:      getValue()
	 * Description: 
	 * Returns:     int
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * Method:      setValue(int _value, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setValue(int _value, boolean _autoCommit)
	{
		value=_value;
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
	 *  Method:      findByName(String _name)
	 *  Description: 
	 *  Returns:     SystemStatistik
	 */
	public static SystemStatistik findByName(String _name)
	{
		SystemStatistik result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.findByName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, name, value FROM SystemStatistik WHERE name='"+toSQL(_name)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (SystemStatistik)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new SystemStatistik();
						result.name= s.getString(2);
						result.value= s.getInt(3);
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
	 *  Method:      findByValue(int _value)
	 *  Description: 
	 *  Returns:     ArrayList<SystemStatistik>
	 */
	public static ArrayList findByValue(int _value)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.findByValue");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, name, value FROM SystemStatistik WHERE value="+_value+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					SystemStatistik newObject = (SystemStatistik)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new SystemStatistik();
						newObject.name=s.getString(2);
						newObject.value=s.getInt(3);
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
	 *  Returns:     SystemStatistik
	 */
	public static SystemStatistik findById(String _id)
	{
		SystemStatistik result = (SystemStatistik)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, name, value FROM SystemStatistik WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new SystemStatistik();
					result.name= s.getString(2);
					result.value= s.getInt(3);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.commit");
			try
			{
				stmt.executeUpdate( "UPDATE SystemStatistik set name= '"+toSQL(name)+"', value= "+value+" WHERE id='"+id+"'");
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
	 * returns ArrayList<SystemStatistik>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, name, value FROM SystemStatistik order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					SystemStatistik newObject = (SystemStatistik)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new SystemStatistik();
						newObject.name=s.getString(2);
						newObject.value=s.getInt(3);
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
	static public SystemStatistik createInstance( String  _name, int  _value )
	{
		SystemStatistik result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO SystemStatistik ( name, value, id) VALUES ( '"+toSQL(_name)+"', "+_value+", '"+nextGUID+"')");
				result = new SystemStatistik();
				result.name= _name;
				result.value= _value;
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
	static public void newInstance( String  _name, int  _value )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO SystemStatistik ( name, value, id) VALUES ( '"+toSQL(_name)+"', "+_value+", '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM SystemStatistik WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbSystemStatistik.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from SystemStatistik" );
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
		result.append(value);
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
			result=dMeta.getColumns(null,null,"SystemStatistik","name");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'name' in table 'SystemStatistik' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"SystemStatistik","value");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'value' in table 'SystemStatistik' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"SystemStatistik","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'SystemStatistik' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
