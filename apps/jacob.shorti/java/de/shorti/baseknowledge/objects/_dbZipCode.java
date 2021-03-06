package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:23 GMT+02:00 2002
 */
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

class _dbZipCode extends dbObject 
{

	String    code;
	double    longitude;
	String    city_id;	// Foreign Key pointing to Table [City], Field [id]
	double    latitude;


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
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ZipCode WHERE id='"+_key+"'");
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
	 * Method:      getCode()
	 * Description: 
	 * Returns:     String
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Method:      setCode(String _code, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setCode(String _code, boolean _autoCommit)
	{
		code=_code;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getLongitude()
	 * Description: 
	 * Returns:     double
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * Method:      setLongitude(double _longitude, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setLongitude(double _longitude, boolean _autoCommit)
	{
		longitude=_longitude;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getLatitude()
	 * Description: 
	 * Returns:     double
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * Method:      setLatitude(double _latitude, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setLatitude(double _latitude, boolean _autoCommit)
	{
		latitude=_latitude;
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
	 *  Method:      getAddreses()
	 *  Description:                              
	 *  Returns:     ArrayList<Address>
	 */
	public ArrayList getAddreses()
	{
		return _dbAddress.findByZipCode(this);
	}


	/**
	 *  Method:      getPreferredRegions()
	 *  Description:                              
	 *  Returns:     ArrayList<PreferredRegion>
	 */
	public ArrayList getPreferredRegions()
	{
		return _dbPreferredRegion.findByZipCode(this);
	}


	/**
	 *  Method:      getRadioRelayStations()
	 *  Description:                              
	 *  Returns:     ArrayList<RadioRelayStation>
	 */
	public ArrayList getRadioRelayStations()
	{
		return _dbRadioRelayStation.findByZipCode(this);
	}


	/**
	 *  Method:      getRequests()
	 *  Description:                              
	 *  Returns:     ArrayList<Request>
	 */
	public ArrayList getRequests()
	{
		return _dbRequest.findBySourceZipCode(this);
	}


	/**
	 *  Method:      findByCode(String _code)
	 *  Description: 
	 *  Returns:     ArrayList<ZipCode>
	 */
	public static ArrayList findByCode(String _code)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.findByCode");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, code, longitude, city_id, latitude FROM ZipCode WHERE code='"+toSQL(_code)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ZipCode newObject = (ZipCode)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ZipCode();
						newObject.code=s.getString(2);
						newObject.longitude=s.getDouble(3);
						newObject.city_id=s.getString(4);
						newObject.latitude=s.getDouble(5);
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
	 *  Method:      findByLongitude(double _longitude)
	 *  Description: 
	 *  Returns:     ArrayList<ZipCode>
	 */
	public static ArrayList findByLongitude(double _longitude)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.findByLongitude");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, code, longitude, city_id, latitude FROM ZipCode WHERE longitude="+_longitude+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ZipCode newObject = (ZipCode)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ZipCode();
						newObject.code=s.getString(2);
						newObject.longitude=s.getDouble(3);
						newObject.city_id=s.getString(4);
						newObject.latitude=s.getDouble(5);
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
	 *  Method:      findByCity(_dbCity _city)
	 *  Description: 
	 *  Returns:     ArrayList<ZipCode>
	 */
	public static ArrayList findByCity(_dbCity _city)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.findByCity");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, code, longitude, city_id, latitude FROM ZipCode WHERE city_id='"+toSQL(((_city==null)?"":_city.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ZipCode newObject = (ZipCode)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ZipCode();
						newObject.code=s.getString(2);
						newObject.longitude=s.getDouble(3);
						newObject.city_id=s.getString(4);
						newObject.latitude=s.getDouble(5);
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
	 *  Method:      findByLatitude(double _latitude)
	 *  Description: 
	 *  Returns:     ArrayList<ZipCode>
	 */
	public static ArrayList findByLatitude(double _latitude)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.findByLatitude");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, code, longitude, city_id, latitude FROM ZipCode WHERE latitude="+_latitude+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ZipCode newObject = (ZipCode)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ZipCode();
						newObject.code=s.getString(2);
						newObject.longitude=s.getDouble(3);
						newObject.city_id=s.getString(4);
						newObject.latitude=s.getDouble(5);
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
	 *  Returns:     ZipCode
	 */
	public static ZipCode findById(String _id)
	{
		ZipCode result = (ZipCode)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, code, longitude, city_id, latitude FROM ZipCode WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new ZipCode();
					result.code= s.getString(2);
					result.longitude= s.getDouble(3);
					result.city_id= s.getString(4);
					result.latitude= s.getDouble(5);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.commit");
			try
			{
				stmt.executeUpdate( "UPDATE ZipCode set code= '"+toSQL(code)+"', longitude= "+longitude+", city_id= '"+toSQL(city_id)+"', latitude= "+latitude+" WHERE id='"+id+"'");
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
	 * returns ArrayList<ZipCode>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, code, longitude, city_id, latitude FROM ZipCode order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ZipCode newObject = (ZipCode)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ZipCode();
						newObject.code=s.getString(2);
						newObject.longitude=s.getDouble(3);
						newObject.city_id=s.getString(4);
						newObject.latitude=s.getDouble(5);
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
	static public ZipCode createInstance( String  _code, double  _longitude, _dbCity _city, double  _latitude )
	{
		ZipCode result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ZipCode ( code, longitude, city_id, latitude, id) VALUES ( '"+toSQL(_code)+"', "+_longitude+",  '"+((_city==null)?"":_city.getId())+"', "+_latitude+", '"+nextGUID+"')");
				result = new ZipCode();
				result.code= _code;
				result.longitude= _longitude;
				result.city_id= (_city==null)?"":_city.getId();
				result.latitude= _latitude;
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
	static public void newInstance( String  _code, double  _longitude, _dbCity _city, double  _latitude )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ZipCode ( code, longitude, city_id, latitude, id) VALUES ( '"+toSQL(_code)+"', "+_longitude+",  '"+((_city==null)?"":_city.getId())+"', "+_latitude+", '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ZipCode WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbZipCode.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from ZipCode" );
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
		result.append(code);
		result.append("|");
		result.append(longitude);
		result.append("|");
		result.append(city_id);
		result.append("|");
		result.append(latitude);
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
			result=dMeta.getColumns(null,null,"ZipCode","code");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'code' in table 'ZipCode' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ZipCode","longitude");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'longitude' in table 'ZipCode' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ZipCode","city_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'city_id' in table 'ZipCode' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ZipCode","latitude");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'latitude' in table 'ZipCode' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ZipCode","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'ZipCode' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
