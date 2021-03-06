package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:23 GMT+02:00 2002
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

class _dbAirport extends dbObject 
{

	String    shortName;
	String    name;
	String    city_id;	// Foreign Key pointing to Table [City], Field [id]
	String    country_id;	// Foreign Key pointing to Table [Country], Field [id]


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
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Airport WHERE id='"+_key+"'");
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
	 * Method:      getShortName()
	 * Description: 
	 * Returns:     String
	 */
	public String getShortName()
	{
		return shortName;
	}

	/**
	 * Method:      setShortName(String _shortName, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setShortName(String _shortName, boolean _autoCommit)
	{
		shortName=_shortName;
		if(_autoCommit)
		{
			commit();
		}
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
	 * Method:      getCity()
	 * Description: 
	 * Returns:     City
	 */
	public City getCity()
	{
		return City.findById(city_id);
	}

	/**
	 * Method:      setCity()
	 * Description: 
	 * Returns:     void
	 */
	public void setCity(City _foreigner, boolean _autocommit)
	{
		city_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getCountry()
	 * Description: 
	 * Returns:     Country
	 */
	public Country getCountry()
	{
		return Country.findById(country_id);
	}

	/**
	 * Method:      setCountry()
	 * Description: 
	 * Returns:     void
	 */
	public void setCountry(Country _foreigner, boolean _autocommit)
	{
		country_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      findByShortName(String _shortName)
	 *  Description: 
	 *  Returns:     Airport
	 */
	public static Airport findByShortName(String _shortName)
	{
		Airport result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.findByShortName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, shortName, name, city_id, country_id FROM Airport WHERE shortName='"+toSQL(_shortName)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (Airport)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new Airport();
						result.shortName= s.getString(2);
						result.name= s.getString(3);
						result.city_id= s.getString(4);
						result.id= _tmpID;
						result.country_id= s.getString(5);
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
	 *  Method:      findByName(String _name)
	 *  Description: 
	 *  Returns:     ArrayList<Airport>
	 */
	public static ArrayList findByName(String _name)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.findByName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, shortName, name, city_id, country_id FROM Airport WHERE name='"+toSQL(_name)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Airport newObject = (Airport)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Airport();
						newObject.shortName=s.getString(2);
						newObject.name=s.getString(3);
						newObject.city_id=s.getString(4);
						newObject.id=_tmpID;
						newObject.country_id=s.getString(5);
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
	 *  Method:      findByCity(_dbCity _city)
	 *  Description: 
	 *  Returns:     ArrayList<Airport>
	 */
	public static ArrayList findByCity(_dbCity _city)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.findByCity");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, shortName, name, city_id, country_id FROM Airport WHERE city_id='"+toSQL(((_city==null)?"":_city.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Airport newObject = (Airport)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Airport();
						newObject.shortName=s.getString(2);
						newObject.name=s.getString(3);
						newObject.city_id=s.getString(4);
						newObject.id=_tmpID;
						newObject.country_id=s.getString(5);
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
	 *  Returns:     Airport
	 */
	public static Airport findById(String _id)
	{
		Airport result = (Airport)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, shortName, name, city_id, country_id FROM Airport WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new Airport();
					result.shortName= s.getString(2);
					result.name= s.getString(3);
					result.city_id= s.getString(4);
					result.id= _id;
					result.country_id= s.getString(5);
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
	 *  Method:      findByCountry(_dbCountry _country)
	 *  Description: 
	 *  Returns:     ArrayList<Airport>
	 */
	public static ArrayList findByCountry(_dbCountry _country)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.findByCountry");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, shortName, name, city_id, country_id FROM Airport WHERE country_id='"+toSQL(((_country==null)?"":_country.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Airport newObject = (Airport)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Airport();
						newObject.shortName=s.getString(2);
						newObject.name=s.getString(3);
						newObject.city_id=s.getString(4);
						newObject.id=_tmpID;
						newObject.country_id=s.getString(5);
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
	 * returns boolean
	 */
	public boolean commit()
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.commit");
			try
			{
				stmt.executeUpdate( "UPDATE Airport set shortName= '"+toSQL(shortName)+"', name= '"+toSQL(name)+"', city_id= '"+toSQL(city_id)+"', country_id= '"+toSQL(country_id)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<Airport>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, shortName, name, city_id, country_id FROM Airport order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Airport newObject = (Airport)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Airport();
						newObject.shortName=s.getString(2);
						newObject.name=s.getString(3);
						newObject.city_id=s.getString(4);
						newObject.id=_tmpID;
						newObject.country_id=s.getString(5);
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
	static public Airport createInstance( String  _shortName, String  _name, _dbCity _city, _dbCountry _country )
	{
		Airport result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Airport ( shortName, name, city_id, id, country_id) VALUES ( '"+toSQL(_shortName)+"',  '"+toSQL(_name)+"',  '"+((_city==null)?"":_city.getId())+"', '"+nextGUID+"',  '"+((_country==null)?"":_country.getId())+"')");
				result = new Airport();
				result.shortName= _shortName;
				result.name= _name;
				result.city_id= (_city==null)?"":_city.getId();
				result.id= nextGUID;
				result.country_id= (_country==null)?"":_country.getId();
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
	static public void newInstance( String  _shortName, String  _name, _dbCity _city, _dbCountry _country )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Airport ( shortName, name, city_id, id, country_id) VALUES ( '"+toSQL(_shortName)+"',  '"+toSQL(_name)+"',  '"+((_city==null)?"":_city.getId())+"', '"+nextGUID+"',  '"+((_country==null)?"":_country.getId())+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Airport WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbAirport.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from Airport" );
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
		result.append(shortName);
		result.append("|");
		result.append(name);
		result.append("|");
		result.append(city_id);
		result.append("|");
		result.append(id);
		result.append("|");
		result.append(country_id);
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
			result=dMeta.getColumns(null,null,"Airport","shortName");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'shortName' in table 'Airport' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Airport","name");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'name' in table 'Airport' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Airport","city_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'city_id' in table 'Airport' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Airport","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'Airport' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Airport","country_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'country_id' in table 'Airport' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
