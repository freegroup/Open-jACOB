package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:21 GMT+02:00 2002
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

class _dbShortiUser extends dbObject 
{

	String    nickname;
	String    preferedLanguage_id;	// Foreign Key pointing to Table [Language], Field [id]
	String    passwd;
	String    homeCountry_id;	// Foreign Key pointing to Table [Country], Field [id]
	String    account_id;	// Foreign Key pointing to Table [Account], Field [id]
	String    calendar_id;	// Foreign Key pointing to Table [Calendar], Field [id]


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
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ShortiUser WHERE id='"+_key+"'");
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
	 * Method:      getNickname()
	 * Description: 
	 * Returns:     String
	 */
	public String getNickname()
	{
		return nickname;
	}

	/**
	 * Method:      setNickname(String _nickname, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setNickname(String _nickname, boolean _autoCommit)
	{
		nickname=_nickname;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getPasswd()
	 * Description: 
	 * Returns:     String
	 */
	public String getPasswd()
	{
		return passwd;
	}

	/**
	 * Method:      setPasswd(String _passwd, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setPasswd(String _passwd, boolean _autoCommit)
	{
		passwd=_passwd;
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
	 * Method:      getPreferedLanguage()
	 * Description: 
	 * Returns:     Language
	 */
	public Language getPreferedLanguage()
	{
		return Language.findById(preferedLanguage_id);
	}

	/**
	 * Method:      setPreferedLanguage()
	 * Description: 
	 * Returns:     void
	 */
	public void setPreferedLanguage(Language _foreigner, boolean _autocommit)
	{
		preferedLanguage_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getHomeCountry()
	 * Description: 
	 * Returns:     Country
	 */
	public Country getHomeCountry()
	{
		return Country.findById(homeCountry_id);
	}

	/**
	 * Method:      setHomeCountry()
	 * Description: 
	 * Returns:     void
	 */
	public void setHomeCountry(Country _foreigner, boolean _autocommit)
	{
		homeCountry_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getAccount()
	 * Description: 
	 * Returns:     Account
	 */
	public Account getAccount()
	{
		return Account.findById(account_id);
	}

	/**
	 * Method:      setAccount()
	 * Description: 
	 * Returns:     void
	 */
	public void setAccount(Account _foreigner, boolean _autocommit)
	{
		account_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getCalendar()
	 * Description: 
	 * Returns:     Calendar
	 */
	public Calendar getCalendar()
	{
		return Calendar.findById(calendar_id);
	}

	/**
	 * Method:      setCalendar()
	 * Description: 
	 * Returns:     void
	 */
	public void setCalendar(Calendar _foreigner, boolean _autocommit)
	{
		calendar_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      getAliaes()
	 *  Description:                              
	 *  Returns:     ArrayList<Alias>
	 */
	public ArrayList getAliaes()
	{
		return _dbAlias.findByShortiUser(this);
	}


	/**
	 *  Method:      getCommunicationChannels()
	 *  Description:                              
	 *  Returns:     ArrayList<CommunicationChannel>
	 */
	public ArrayList getCommunicationChannels()
	{
		return _dbCommunicationChannel.findByShortiUser(this);
	}


	/**
	 *  Method:      getHistoryValues()
	 *  Description:                              
	 *  Returns:     ArrayList<HistoryValue>
	 */
	public ArrayList getHistoryValues()
	{
		return _dbHistoryValue.findByShortiUser(this);
	}


	/**
	 *  Method:      getPreferredRegions()
	 *  Description:                              
	 *  Returns:     ArrayList<PreferredRegion>
	 */
	public ArrayList getPreferredRegions()
	{
		return _dbPreferredRegion.findByShortiUser(this);
	}


	/**
	 *  Method:      findByNickname(String _nickname)
	 *  Description: 
	 *  Returns:     ShortiUser
	 */
	public static ShortiUser findByNickname(String _nickname)
	{
		ShortiUser result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.findByNickname");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser WHERE nickname='"+toSQL(_nickname)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (ShortiUser)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new ShortiUser();
						result.nickname= s.getString(2);
						result.preferedLanguage_id= s.getString(3);
						result.passwd= s.getString(4);
						result.homeCountry_id= s.getString(5);
						result.account_id= s.getString(6);
						result.calendar_id= s.getString(7);
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
	 *  Method:      findByPreferedLanguage(_dbLanguage _preferedLanguage)
	 *  Description: 
	 *  Returns:     ArrayList<ShortiUser>
	 */
	public static ArrayList findByPreferedLanguage(_dbLanguage _preferedLanguage)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.findByPreferedLanguage");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser WHERE preferedLanguage_id='"+toSQL(((_preferedLanguage==null)?"":_preferedLanguage.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ShortiUser newObject = (ShortiUser)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ShortiUser();
						newObject.nickname=s.getString(2);
						newObject.preferedLanguage_id=s.getString(3);
						newObject.passwd=s.getString(4);
						newObject.homeCountry_id=s.getString(5);
						newObject.account_id=s.getString(6);
						newObject.calendar_id=s.getString(7);
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
	 *  Method:      findByPasswd(String _passwd)
	 *  Description: 
	 *  Returns:     ArrayList<ShortiUser>
	 */
	public static ArrayList findByPasswd(String _passwd)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.findByPasswd");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser WHERE passwd='"+toSQL(_passwd)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ShortiUser newObject = (ShortiUser)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ShortiUser();
						newObject.nickname=s.getString(2);
						newObject.preferedLanguage_id=s.getString(3);
						newObject.passwd=s.getString(4);
						newObject.homeCountry_id=s.getString(5);
						newObject.account_id=s.getString(6);
						newObject.calendar_id=s.getString(7);
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
	 *  Method:      findByHomeCountry(_dbCountry _homeCountry)
	 *  Description: 
	 *  Returns:     ArrayList<ShortiUser>
	 */
	public static ArrayList findByHomeCountry(_dbCountry _homeCountry)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.findByHomeCountry");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser WHERE homeCountry_id='"+toSQL(((_homeCountry==null)?"":_homeCountry.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ShortiUser newObject = (ShortiUser)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ShortiUser();
						newObject.nickname=s.getString(2);
						newObject.preferedLanguage_id=s.getString(3);
						newObject.passwd=s.getString(4);
						newObject.homeCountry_id=s.getString(5);
						newObject.account_id=s.getString(6);
						newObject.calendar_id=s.getString(7);
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
	 *  Method:      findByAccount(_dbAccount _account)
	 *  Description: 
	 *  Returns:     ArrayList<ShortiUser>
	 */
	public static ArrayList findByAccount(_dbAccount _account)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.findByAccount");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser WHERE account_id='"+toSQL(((_account==null)?"":_account.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ShortiUser newObject = (ShortiUser)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ShortiUser();
						newObject.nickname=s.getString(2);
						newObject.preferedLanguage_id=s.getString(3);
						newObject.passwd=s.getString(4);
						newObject.homeCountry_id=s.getString(5);
						newObject.account_id=s.getString(6);
						newObject.calendar_id=s.getString(7);
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
	 *  Method:      findByCalendar(_dbCalendar _calendar)
	 *  Description: 
	 *  Returns:     ArrayList<ShortiUser>
	 */
	public static ArrayList findByCalendar(_dbCalendar _calendar)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.findByCalendar");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser WHERE calendar_id='"+toSQL(((_calendar==null)?"":_calendar.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ShortiUser newObject = (ShortiUser)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ShortiUser();
						newObject.nickname=s.getString(2);
						newObject.preferedLanguage_id=s.getString(3);
						newObject.passwd=s.getString(4);
						newObject.homeCountry_id=s.getString(5);
						newObject.account_id=s.getString(6);
						newObject.calendar_id=s.getString(7);
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
	 *  Returns:     ShortiUser
	 */
	public static ShortiUser findById(String _id)
	{
		ShortiUser result = (ShortiUser)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new ShortiUser();
					result.nickname= s.getString(2);
					result.preferedLanguage_id= s.getString(3);
					result.passwd= s.getString(4);
					result.homeCountry_id= s.getString(5);
					result.account_id= s.getString(6);
					result.calendar_id= s.getString(7);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.commit");
			try
			{
				stmt.executeUpdate( "UPDATE ShortiUser set nickname= '"+toSQL(nickname)+"', preferedLanguage_id= '"+toSQL(preferedLanguage_id)+"', passwd= '"+toSQL(passwd)+"', homeCountry_id= '"+toSQL(homeCountry_id)+"', account_id= '"+toSQL(account_id)+"', calendar_id= '"+toSQL(calendar_id)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<ShortiUser>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id FROM ShortiUser order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					ShortiUser newObject = (ShortiUser)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new ShortiUser();
						newObject.nickname=s.getString(2);
						newObject.preferedLanguage_id=s.getString(3);
						newObject.passwd=s.getString(4);
						newObject.homeCountry_id=s.getString(5);
						newObject.account_id=s.getString(6);
						newObject.calendar_id=s.getString(7);
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
	static public ShortiUser createInstance( String  _nickname, _dbLanguage _preferedLanguage, String  _passwd, _dbCountry _homeCountry, _dbAccount _account, _dbCalendar _calendar )
	{
		ShortiUser result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ShortiUser ( nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id, id) VALUES ( '"+toSQL(_nickname)+"',  '"+((_preferedLanguage==null)?"":_preferedLanguage.getId())+"',  '"+toSQL(_passwd)+"',  '"+((_homeCountry==null)?"":_homeCountry.getId())+"',  '"+((_account==null)?"":_account.getId())+"',  '"+((_calendar==null)?"":_calendar.getId())+"', '"+nextGUID+"')");
				result = new ShortiUser();
				result.nickname= _nickname;
				result.preferedLanguage_id= (_preferedLanguage==null)?"":_preferedLanguage.getId();
				result.passwd= _passwd;
				result.homeCountry_id= (_homeCountry==null)?"":_homeCountry.getId();
				result.account_id= (_account==null)?"":_account.getId();
				result.calendar_id= (_calendar==null)?"":_calendar.getId();
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
	static public void newInstance( String  _nickname, _dbLanguage _preferedLanguage, String  _passwd, _dbCountry _homeCountry, _dbAccount _account, _dbCalendar _calendar )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO ShortiUser ( nickname, preferedLanguage_id, passwd, homeCountry_id, account_id, calendar_id, id) VALUES ( '"+toSQL(_nickname)+"',  '"+((_preferedLanguage==null)?"":_preferedLanguage.getId())+"',  '"+toSQL(_passwd)+"',  '"+((_homeCountry==null)?"":_homeCountry.getId())+"',  '"+((_account==null)?"":_account.getId())+"',  '"+((_calendar==null)?"":_calendar.getId())+"', '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM ShortiUser WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbShortiUser.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from ShortiUser" );
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
		result.append(nickname);
		result.append("|");
		result.append(preferedLanguage_id);
		result.append("|");
		result.append(passwd);
		result.append("|");
		result.append(homeCountry_id);
		result.append("|");
		result.append(account_id);
		result.append("|");
		result.append(calendar_id);
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
			result=dMeta.getColumns(null,null,"ShortiUser","nickname");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'nickname' in table 'ShortiUser' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ShortiUser","preferedLanguage_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'preferedLanguage_id' in table 'ShortiUser' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ShortiUser","passwd");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'passwd' in table 'ShortiUser' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ShortiUser","homeCountry_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'homeCountry_id' in table 'ShortiUser' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ShortiUser","account_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'account_id' in table 'ShortiUser' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ShortiUser","calendar_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'calendar_id' in table 'ShortiUser' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"ShortiUser","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'ShortiUser' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
