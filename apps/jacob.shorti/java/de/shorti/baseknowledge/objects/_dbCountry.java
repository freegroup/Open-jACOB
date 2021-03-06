package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:23 GMT+02:00 2002
 */
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

class _dbCountry extends dbObject 
{

	String    continent_id;	// Foreign Key pointing to Table [Continent], Field [id]
	String    capital_id;	// Foreign Key pointing to Table [City], Field [id]
	String    shortName;
	String    name;
	String    dialPrefix;
	String    language_id;	// Foreign Key pointing to Table [Language], Field [id]


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
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Country WHERE id='"+_key+"'");
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
	 * Method:      getDialPrefix()
	 * Description: 
	 * Returns:     String
	 */
	public String getDialPrefix()
	{
		return dialPrefix;
	}

	/**
	 * Method:      setDialPrefix(String _dialPrefix, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setDialPrefix(String _dialPrefix, boolean _autoCommit)
	{
		dialPrefix=_dialPrefix;
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
	 * Method:      getContinent()
	 * Description: 
	 * Returns:     Continent
	 */
	public Continent getContinent()
	{
		return Continent.findById(continent_id);
	}

	/**
	 * Method:      setContinent()
	 * Description: 
	 * Returns:     void
	 */
	public void setContinent(Continent _foreigner, boolean _autocommit)
	{
		continent_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getCapital()
	 * Description: 
	 * Returns:     City
	 */
	public City getCapital()
	{
		return City.findById(capital_id);
	}

	/**
	 * Method:      setCapital()
	 * Description: 
	 * Returns:     void
	 */
	public void setCapital(City _foreigner, boolean _autocommit)
	{
		capital_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getLanguage()
	 * Description: 
	 * Returns:     Language
	 */
	public Language getLanguage()
	{
		return Language.findById(language_id);
	}

	/**
	 * Method:      setLanguage()
	 * Description: 
	 * Returns:     void
	 */
	public void setLanguage(Language _foreigner, boolean _autocommit)
	{
		language_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      getAirports()
	 *  Description:                              
	 *  Returns:     ArrayList<Airport>
	 */
	public ArrayList getAirports()
	{
		return _dbAirport.findByCountry(this);
	}


	/**
	 *  Method:      getShortiUsers()
	 *  Description:                              
	 *  Returns:     ArrayList<ShortiUser>
	 */
	public ArrayList getShortiUsers()
	{
		return _dbShortiUser.findByHomeCountry(this);
	}


	/**
	 *  Method:      getStates()
	 *  Description:                              
	 *  Returns:     ArrayList<State>
	 */
	public ArrayList getStates()
	{
		return _dbState.findByCountry(this);
	}


	/**
	 *  Method:      findByContinent(_dbContinent _continent)
	 *  Description: 
	 *  Returns:     ArrayList<Country>
	 */
	public static ArrayList findByContinent(_dbContinent _continent)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.findByContinent");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country WHERE continent_id='"+toSQL(((_continent==null)?"":_continent.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Country newObject = (Country)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Country();
						newObject.continent_id=s.getString(2);
						newObject.capital_id=s.getString(3);
						newObject.shortName=s.getString(4);
						newObject.name=s.getString(5);
						newObject.dialPrefix=s.getString(6);
						newObject.language_id=s.getString(7);
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
	 *  Method:      findByCapital(_dbCity _capital)
	 *  Description: 
	 *  Returns:     ArrayList<Country>
	 */
	public static ArrayList findByCapital(_dbCity _capital)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.findByCapital");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country WHERE capital_id='"+toSQL(((_capital==null)?"":_capital.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Country newObject = (Country)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Country();
						newObject.continent_id=s.getString(2);
						newObject.capital_id=s.getString(3);
						newObject.shortName=s.getString(4);
						newObject.name=s.getString(5);
						newObject.dialPrefix=s.getString(6);
						newObject.language_id=s.getString(7);
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
	 *  Method:      findByShortName(String _shortName)
	 *  Description: 
	 *  Returns:     Country
	 */
	public static Country findByShortName(String _shortName)
	{
		Country result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.findByShortName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country WHERE shortName='"+toSQL(_shortName)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (Country)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new Country();
						result.continent_id= s.getString(2);
						result.capital_id= s.getString(3);
						result.shortName= s.getString(4);
						result.name= s.getString(5);
						result.dialPrefix= s.getString(6);
						result.language_id= s.getString(7);
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
	 *  Method:      findByName(String _name)
	 *  Description: 
	 *  Returns:     Country
	 */
	public static Country findByName(String _name)
	{
		Country result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.findByName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country WHERE name='"+toSQL(_name)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (Country)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new Country();
						result.continent_id= s.getString(2);
						result.capital_id= s.getString(3);
						result.shortName= s.getString(4);
						result.name= s.getString(5);
						result.dialPrefix= s.getString(6);
						result.language_id= s.getString(7);
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
	 *  Method:      findByDialPrefix(String _dialPrefix)
	 *  Description: 
	 *  Returns:     Country
	 */
	public static Country findByDialPrefix(String _dialPrefix)
	{
		Country result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.findByDialPrefix");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country WHERE dialPrefix='"+toSQL(_dialPrefix)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (Country)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new Country();
						result.continent_id= s.getString(2);
						result.capital_id= s.getString(3);
						result.shortName= s.getString(4);
						result.name= s.getString(5);
						result.dialPrefix= s.getString(6);
						result.language_id= s.getString(7);
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
	 *  Method:      findByLanguage(_dbLanguage _language)
	 *  Description: 
	 *  Returns:     ArrayList<Country>
	 */
	public static ArrayList findByLanguage(_dbLanguage _language)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.findByLanguage");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country WHERE language_id='"+toSQL(((_language==null)?"":_language.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Country newObject = (Country)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Country();
						newObject.continent_id=s.getString(2);
						newObject.capital_id=s.getString(3);
						newObject.shortName=s.getString(4);
						newObject.name=s.getString(5);
						newObject.dialPrefix=s.getString(6);
						newObject.language_id=s.getString(7);
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
	 *  Returns:     Country
	 */
	public static Country findById(String _id)
	{
		Country result = (Country)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new Country();
					result.continent_id= s.getString(2);
					result.capital_id= s.getString(3);
					result.shortName= s.getString(4);
					result.name= s.getString(5);
					result.dialPrefix= s.getString(6);
					result.language_id= s.getString(7);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.commit");
			try
			{
				stmt.executeUpdate( "UPDATE Country set continent_id= '"+toSQL(continent_id)+"', capital_id= '"+toSQL(capital_id)+"', shortName= '"+toSQL(shortName)+"', name= '"+toSQL(name)+"', dialPrefix= '"+toSQL(dialPrefix)+"', language_id= '"+toSQL(language_id)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<Country>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, continent_id, capital_id, shortName, name, dialPrefix, language_id FROM Country order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Country newObject = (Country)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Country();
						newObject.continent_id=s.getString(2);
						newObject.capital_id=s.getString(3);
						newObject.shortName=s.getString(4);
						newObject.name=s.getString(5);
						newObject.dialPrefix=s.getString(6);
						newObject.language_id=s.getString(7);
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
	static public Country createInstance( _dbContinent _continent, _dbCity _capital, String  _shortName, String  _name, String  _dialPrefix, _dbLanguage _language )
	{
		Country result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Country ( continent_id, capital_id, shortName, name, dialPrefix, language_id, id) VALUES ( '"+((_continent==null)?"":_continent.getId())+"',  '"+((_capital==null)?"":_capital.getId())+"',  '"+toSQL(_shortName)+"',  '"+toSQL(_name)+"',  '"+toSQL(_dialPrefix)+"',  '"+((_language==null)?"":_language.getId())+"', '"+nextGUID+"')");
				result = new Country();
				result.continent_id= (_continent==null)?"":_continent.getId();
				result.capital_id= (_capital==null)?"":_capital.getId();
				result.shortName= _shortName;
				result.name= _name;
				result.dialPrefix= _dialPrefix;
				result.language_id= (_language==null)?"":_language.getId();
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
	static public void newInstance( _dbContinent _continent, _dbCity _capital, String  _shortName, String  _name, String  _dialPrefix, _dbLanguage _language )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Country ( continent_id, capital_id, shortName, name, dialPrefix, language_id, id) VALUES ( '"+((_continent==null)?"":_continent.getId())+"',  '"+((_capital==null)?"":_capital.getId())+"',  '"+toSQL(_shortName)+"',  '"+toSQL(_name)+"',  '"+toSQL(_dialPrefix)+"',  '"+((_language==null)?"":_language.getId())+"', '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Country WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbCountry.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from Country" );
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
		result.append(continent_id);
		result.append("|");
		result.append(capital_id);
		result.append("|");
		result.append(shortName);
		result.append("|");
		result.append(name);
		result.append("|");
		result.append(dialPrefix);
		result.append("|");
		result.append(language_id);
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
			result=dMeta.getColumns(null,null,"Country","continent_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'continent_id' in table 'Country' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Country","capital_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'capital_id' in table 'Country' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Country","shortName");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'shortName' in table 'Country' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Country","name");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'name' in table 'Country' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Country","dialPrefix");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'dialPrefix' in table 'Country' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Country","language_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'language_id' in table 'Country' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Country","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'Country' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
