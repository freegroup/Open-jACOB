package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:22 GMT+02:00 2002
 */
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

class _dbBannerAdvertisement extends dbObject 
{

	String    bannerContract_id;	// Foreign Key pointing to Table [BannerContract], Field [id]
	int       impressionCount;
	String    text;
	int       maxImpressionCount;


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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM BannerAdvertisement WHERE id='"+_key+"'");
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
	 * Method:      getImpressionCount()
	 * Description: 
	 * Returns:     int
	 */
	public int getImpressionCount()
	{
		return impressionCount;
	}

	/**
	 * Method:      setImpressionCount(int _impressionCount, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setImpressionCount(int _impressionCount, boolean _autoCommit)
	{
		impressionCount=_impressionCount;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getText()
	 * Description: 
	 * Returns:     String
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Method:      setText(String _text, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setText(String _text, boolean _autoCommit)
	{
		text=_text;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getMaxImpressionCount()
	 * Description: 
	 * Returns:     int
	 */
	public int getMaxImpressionCount()
	{
		return maxImpressionCount;
	}

	/**
	 * Method:      setMaxImpressionCount(int _maxImpressionCount, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setMaxImpressionCount(int _maxImpressionCount, boolean _autoCommit)
	{
		maxImpressionCount=_maxImpressionCount;
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
	 * Method:      getBannerContract()
	 * Description: 
	 * Returns:     BannerContract
	 */
	public BannerContract getBannerContract()
	{
		return BannerContract.findById(bannerContract_id);
	}

	/**
	 * Method:      setBannerContract()
	 * Description: 
	 * Returns:     void
	 */
	public void setBannerContract(BannerContract _foreigner, boolean _autocommit)
	{
		bannerContract_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      getAdvertismentAmounts()
	 *  Description:                              
	 *  Returns:     ArrayList<AdvertismentAmount>
	 */
	public ArrayList getAdvertismentAmounts()
	{
		return _dbAdvertismentAmount.findByBannerAdvertisement(this);
	}


	/**
	 *  Method:      getRequests()
	 *  Description:                              
	 *  Returns:     ArrayList<Request>
	 */
	public ArrayList getRequests()
	{
		return _dbRequest.findByBannerAdvertisement(this);
	}


	/**
	 *  Method:      findByBannerContract(_dbBannerContract _bannerContract)
	 *  Description: 
	 *  Returns:     ArrayList<BannerAdvertisement>
	 */
	public static ArrayList findByBannerContract(_dbBannerContract _bannerContract)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.findByBannerContract");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, bannerContract_id, impressionCount, text, maxImpressionCount FROM BannerAdvertisement WHERE bannerContract_id='"+toSQL(((_bannerContract==null)?"":_bannerContract.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerAdvertisement newObject = (BannerAdvertisement)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerAdvertisement();
						newObject.bannerContract_id=s.getString(2);
						newObject.impressionCount=s.getInt(3);
						newObject.text=s.getString(4);
						newObject.maxImpressionCount=s.getInt(5);
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
	 *  Method:      findByImpressionCount(int _impressionCount)
	 *  Description: 
	 *  Returns:     ArrayList<BannerAdvertisement>
	 */
	public static ArrayList findByImpressionCount(int _impressionCount)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.findByImpressionCount");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, bannerContract_id, impressionCount, text, maxImpressionCount FROM BannerAdvertisement WHERE impressionCount="+_impressionCount+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerAdvertisement newObject = (BannerAdvertisement)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerAdvertisement();
						newObject.bannerContract_id=s.getString(2);
						newObject.impressionCount=s.getInt(3);
						newObject.text=s.getString(4);
						newObject.maxImpressionCount=s.getInt(5);
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
	 *  Method:      findByText(String _text)
	 *  Description: 
	 *  Returns:     ArrayList<BannerAdvertisement>
	 */
	public static ArrayList findByText(String _text)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.findByText");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, bannerContract_id, impressionCount, text, maxImpressionCount FROM BannerAdvertisement WHERE text='"+toSQL(_text)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerAdvertisement newObject = (BannerAdvertisement)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerAdvertisement();
						newObject.bannerContract_id=s.getString(2);
						newObject.impressionCount=s.getInt(3);
						newObject.text=s.getString(4);
						newObject.maxImpressionCount=s.getInt(5);
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
	 *  Method:      findByMaxImpressionCount(int _maxImpressionCount)
	 *  Description: 
	 *  Returns:     ArrayList<BannerAdvertisement>
	 */
	public static ArrayList findByMaxImpressionCount(int _maxImpressionCount)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.findByMaxImpressionCount");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, bannerContract_id, impressionCount, text, maxImpressionCount FROM BannerAdvertisement WHERE maxImpressionCount="+_maxImpressionCount+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerAdvertisement newObject = (BannerAdvertisement)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerAdvertisement();
						newObject.bannerContract_id=s.getString(2);
						newObject.impressionCount=s.getInt(3);
						newObject.text=s.getString(4);
						newObject.maxImpressionCount=s.getInt(5);
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
	 *  Returns:     BannerAdvertisement
	 */
	public static BannerAdvertisement findById(String _id)
	{
		BannerAdvertisement result = (BannerAdvertisement)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, bannerContract_id, impressionCount, text, maxImpressionCount FROM BannerAdvertisement WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new BannerAdvertisement();
					result.bannerContract_id= s.getString(2);
					result.impressionCount= s.getInt(3);
					result.text= s.getString(4);
					result.maxImpressionCount= s.getInt(5);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.commit");
			try
			{
				stmt.executeUpdate( "UPDATE BannerAdvertisement set bannerContract_id= '"+toSQL(bannerContract_id)+"', impressionCount= "+impressionCount+", text= '"+toSQL(text)+"', maxImpressionCount= "+maxImpressionCount+" WHERE id='"+id+"'");
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
	 * returns ArrayList<BannerAdvertisement>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, bannerContract_id, impressionCount, text, maxImpressionCount FROM BannerAdvertisement order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerAdvertisement newObject = (BannerAdvertisement)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerAdvertisement();
						newObject.bannerContract_id=s.getString(2);
						newObject.impressionCount=s.getInt(3);
						newObject.text=s.getString(4);
						newObject.maxImpressionCount=s.getInt(5);
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
	static public BannerAdvertisement createInstance( _dbBannerContract _bannerContract, int  _impressionCount, String  _text, int  _maxImpressionCount )
	{
		BannerAdvertisement result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO BannerAdvertisement ( bannerContract_id, impressionCount, text, maxImpressionCount, id) VALUES ( '"+((_bannerContract==null)?"":_bannerContract.getId())+"', "+_impressionCount+",  '"+toSQL(_text)+"', "+_maxImpressionCount+", '"+nextGUID+"')");
				result = new BannerAdvertisement();
				result.bannerContract_id= (_bannerContract==null)?"":_bannerContract.getId();
				result.impressionCount= _impressionCount;
				result.text= _text;
				result.maxImpressionCount= _maxImpressionCount;
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
	static public void newInstance( _dbBannerContract _bannerContract, int  _impressionCount, String  _text, int  _maxImpressionCount )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO BannerAdvertisement ( bannerContract_id, impressionCount, text, maxImpressionCount, id) VALUES ( '"+((_bannerContract==null)?"":_bannerContract.getId())+"', "+_impressionCount+",  '"+toSQL(_text)+"', "+_maxImpressionCount+", '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM BannerAdvertisement WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerAdvertisement.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from BannerAdvertisement" );
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
		result.append(bannerContract_id);
		result.append("|");
		result.append(impressionCount);
		result.append("|");
		result.append(text);
		result.append("|");
		result.append(maxImpressionCount);
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
			result=dMeta.getColumns(null,null,"BannerAdvertisement","bannerContract_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'bannerContract_id' in table 'BannerAdvertisement' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerAdvertisement","impressionCount");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'impressionCount' in table 'BannerAdvertisement' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerAdvertisement","text");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'text' in table 'BannerAdvertisement' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerAdvertisement","maxImpressionCount");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'maxImpressionCount' in table 'BannerAdvertisement' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerAdvertisement","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'BannerAdvertisement' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
