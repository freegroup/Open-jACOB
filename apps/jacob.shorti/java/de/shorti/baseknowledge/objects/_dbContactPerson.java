package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:24 GMT+02:00 2002
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

class _dbContactPerson extends dbObject 
{

	String    lastName;
	String    telekom_id;	// Foreign Key pointing to Table [Telekom], Field [id]
	String    firstName;
	String    roleDescription;


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
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ContactPerson WHERE id='"+_key+"'");
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
	 * Method:      getLastName()
	 * Description: 
	 * Returns:     String
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * Method:      setLastName(String _lastName, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setLastName(String _lastName, boolean _autoCommit)
	{
		lastName=_lastName;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getFirstName()
	 * Description: 
	 * Returns:     String
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * Method:      setFirstName(String _firstName, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setFirstName(String _firstName, boolean _autoCommit)
	{
		firstName=_firstName;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getRoleDescription()
	 * Description: 
	 * Returns:     String
	 */
	public String getRoleDescription()
	{
		return roleDescription;
	}

	/**
	 * Method:      setRoleDescription(String _roleDescription, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setRoleDescription(String _roleDescription, boolean _autoCommit)
	{
		roleDescription=_roleDescription;
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
	 * Method:      getTelekom()
	 * Description: 
	 * Returns:     Telekom
	 */
	public Telekom getTelekom()
	{
		return Telekom.findById(telekom_id);
	}

	/**
	 * Method:      setTelekom()
	 * Description: 
	 * Returns:     void
	 */
	public void setTelekom(Telekom _foreigner, boolean _autocommit)
	{
		telekom_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      getBannerContracts()
	 *  Description:                              
	 *  Returns:     ArrayList<BannerContract>
	 */
	public ArrayList getBannerContracts()
	{
		return _dbBannerContract.findByCompanyContactPerson(this);
	}


	/**
	 *  Method:      getCompanies()
	 *  Description:                              
	 *  Returns:     ArrayList<Company>
	 */
	public ArrayList getCompanies()
	{
		return _dbCompany.findByContactPerson(this);
	}


	/**
	 *  Method:      getGeschaeftsstelles()
	 *  Description:                              
	 *  Returns:     ArrayList<Geschaeftsstelle>
	 */
	public ArrayList getGeschaeftsstelles()
	{
		return _dbGeschaeftsstelle.findByContactPerson(this);
	}


	/**
	 *  Method:      findByLastName(String _lastName)
	 *  Description: 
	 *  Returns:     ArrayList<ContactPerson>
	 */
	public static ArrayList findByLastName(String _lastName)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.findByLastName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lastName, telekom_id, firstName, roleDescription FROM ContactPerson WHERE lastName='"+toSQL(_lastName)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ContactPerson newObject = (ContactPerson)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ContactPerson();
						newObject.lastName=s.getString(2);
						newObject.telekom_id=s.getString(3);
						newObject.firstName=s.getString(4);
						newObject.roleDescription=s.getString(5);
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
	 *  Method:      findByTelekom(_dbTelekom _telekom)
	 *  Description: 
	 *  Returns:     ArrayList<ContactPerson>
	 */
	public static ArrayList findByTelekom(_dbTelekom _telekom)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.findByTelekom");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lastName, telekom_id, firstName, roleDescription FROM ContactPerson WHERE telekom_id='"+toSQL(((_telekom==null)?"":_telekom.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ContactPerson newObject = (ContactPerson)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ContactPerson();
						newObject.lastName=s.getString(2);
						newObject.telekom_id=s.getString(3);
						newObject.firstName=s.getString(4);
						newObject.roleDescription=s.getString(5);
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
	 *  Method:      findByFirstName(String _firstName)
	 *  Description: 
	 *  Returns:     ArrayList<ContactPerson>
	 */
	public static ArrayList findByFirstName(String _firstName)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.findByFirstName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lastName, telekom_id, firstName, roleDescription FROM ContactPerson WHERE firstName='"+toSQL(_firstName)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ContactPerson newObject = (ContactPerson)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ContactPerson();
						newObject.lastName=s.getString(2);
						newObject.telekom_id=s.getString(3);
						newObject.firstName=s.getString(4);
						newObject.roleDescription=s.getString(5);
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
	 *  Method:      findByRoleDescription(String _roleDescription)
	 *  Description: 
	 *  Returns:     ArrayList<ContactPerson>
	 */
	public static ArrayList findByRoleDescription(String _roleDescription)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.findByRoleDescription");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lastName, telekom_id, firstName, roleDescription FROM ContactPerson WHERE roleDescription='"+toSQL(_roleDescription)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ContactPerson newObject = (ContactPerson)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ContactPerson();
						newObject.lastName=s.getString(2);
						newObject.telekom_id=s.getString(3);
						newObject.firstName=s.getString(4);
						newObject.roleDescription=s.getString(5);
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
	 *  Returns:     ContactPerson
	 */
	public static ContactPerson findById(String _id)
	{
		ContactPerson result = (ContactPerson)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lastName, telekom_id, firstName, roleDescription FROM ContactPerson WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new ContactPerson();
					result.lastName= s.getString(2);
					result.telekom_id= s.getString(3);
					result.firstName= s.getString(4);
					result.roleDescription= s.getString(5);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.commit");
			try
			{
				stmt.executeUpdate( "UPDATE ContactPerson set lastName= '"+toSQL(lastName)+"', telekom_id= '"+toSQL(telekom_id)+"', firstName= '"+toSQL(firstName)+"', roleDescription= '"+toSQL(roleDescription)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<ContactPerson>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lastName, telekom_id, firstName, roleDescription FROM ContactPerson order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ContactPerson newObject = (ContactPerson)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ContactPerson();
						newObject.lastName=s.getString(2);
						newObject.telekom_id=s.getString(3);
						newObject.firstName=s.getString(4);
						newObject.roleDescription=s.getString(5);
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
	static public ContactPerson createInstance( String  _lastName, _dbTelekom _telekom, String  _firstName, String  _roleDescription )
	{
		ContactPerson result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ContactPerson ( lastName, telekom_id, firstName, roleDescription, id) VALUES ( '"+toSQL(_lastName)+"',  '"+((_telekom==null)?"":_telekom.getId())+"',  '"+toSQL(_firstName)+"',  '"+toSQL(_roleDescription)+"', '"+nextGUID+"')");
				result = new ContactPerson();
				result.lastName= _lastName;
				result.telekom_id= (_telekom==null)?"":_telekom.getId();
				result.firstName= _firstName;
				result.roleDescription= _roleDescription;
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
	static public void newInstance( String  _lastName, _dbTelekom _telekom, String  _firstName, String  _roleDescription )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ContactPerson ( lastName, telekom_id, firstName, roleDescription, id) VALUES ( '"+toSQL(_lastName)+"',  '"+((_telekom==null)?"":_telekom.getId())+"',  '"+toSQL(_firstName)+"',  '"+toSQL(_roleDescription)+"', '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ContactPerson WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbContactPerson.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from ContactPerson" );
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
		result.append(lastName);
		result.append("|");
		result.append(telekom_id);
		result.append("|");
		result.append(firstName);
		result.append("|");
		result.append(roleDescription);
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
			result=dMeta.getColumns(null,null,"ContactPerson","lastName");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'lastName' in table 'ContactPerson' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ContactPerson","telekom_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'telekom_id' in table 'ContactPerson' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ContactPerson","firstName");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'firstName' in table 'ContactPerson' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ContactPerson","roleDescription");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'roleDescription' in table 'ContactPerson' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ContactPerson","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'ContactPerson' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
