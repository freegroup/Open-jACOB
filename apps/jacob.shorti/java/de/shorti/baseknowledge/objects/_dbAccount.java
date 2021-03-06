package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:21 GMT+02:00 2002
 */
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

class _dbAccount extends dbObject 
{

	int       lockStatus;
	int       requestCount;
	Date      createDate;
	int       serviceFactor;
	int       creditWarningCount;
	int       credit;


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
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Account WHERE id='"+_key+"'");
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
	 * Method:      getLockStatus()
	 * Description: 
	 * Returns:     int
	 */
	public int getLockStatus()
	{
		return lockStatus;
	}

	/**
	 * Method:      setLockStatus(int _lockStatus, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setLockStatus(int _lockStatus, boolean _autoCommit)
	{
		lockStatus=_lockStatus;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getRequestCount()
	 * Description: 
	 * Returns:     int
	 */
	public int getRequestCount()
	{
		return requestCount;
	}

	/**
	 * Method:      setRequestCount(int _requestCount, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setRequestCount(int _requestCount, boolean _autoCommit)
	{
		requestCount=_requestCount;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getCreateDate()
	 * Description: 
	 * Returns:     Date
	 */
	public Date getCreateDate()
	{
		return createDate;
	}

	/**
	 * Method:      setCreateDate(Date _createDate, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setCreateDate(Date _createDate, boolean _autoCommit)
	{
		createDate=_createDate;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getServiceFactor()
	 * Description: 
	 * Returns:     int
	 */
	public int getServiceFactor()
	{
		return serviceFactor;
	}

	/**
	 * Method:      setServiceFactor(int _serviceFactor, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setServiceFactor(int _serviceFactor, boolean _autoCommit)
	{
		serviceFactor=_serviceFactor;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getCreditWarningCount()
	 * Description: 
	 * Returns:     int
	 */
	public int getCreditWarningCount()
	{
		return creditWarningCount;
	}

	/**
	 * Method:      setCreditWarningCount(int _creditWarningCount, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setCreditWarningCount(int _creditWarningCount, boolean _autoCommit)
	{
		creditWarningCount=_creditWarningCount;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getCredit()
	 * Description: 
	 * Returns:     int
	 */
	public int getCredit()
	{
		return credit;
	}

	/**
	 * Method:      setCredit(int _credit, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setCredit(int _credit, boolean _autoCommit)
	{
		credit=_credit;
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
	 *  Method:      getBills()
	 *  Description:                              
	 *  Returns:     ArrayList<Bill>
	 */
	public ArrayList getBills()
	{
		return _dbBill.findByAccount(this);
	}


	/**
	 *  Method:      getShortiUsers()
	 *  Description:                              
	 *  Returns:     ArrayList<ShortiUser>
	 */
	public ArrayList getShortiUsers()
	{
		return _dbShortiUser.findByAccount(this);
	}


	/**
	 *  Method:      findByLockStatus(int _lockStatus)
	 *  Description: 
	 *  Returns:     ArrayList<Account>
	 */
	public static ArrayList findByLockStatus(int _lockStatus)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.findByLockStatus");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account WHERE lockStatus="+_lockStatus+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Account newObject = (Account)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Account();
						newObject.lockStatus=s.getInt(2);
						newObject.requestCount=s.getInt(3);
						newObject.createDate=s.getDate(4);
						newObject.serviceFactor=s.getInt(5);
						newObject.creditWarningCount=s.getInt(6);
						newObject.credit=s.getInt(7);
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
	 *  Method:      findByRequestCount(int _requestCount)
	 *  Description: 
	 *  Returns:     ArrayList<Account>
	 */
	public static ArrayList findByRequestCount(int _requestCount)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.findByRequestCount");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account WHERE requestCount="+_requestCount+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Account newObject = (Account)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Account();
						newObject.lockStatus=s.getInt(2);
						newObject.requestCount=s.getInt(3);
						newObject.createDate=s.getDate(4);
						newObject.serviceFactor=s.getInt(5);
						newObject.creditWarningCount=s.getInt(6);
						newObject.credit=s.getInt(7);
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
	 *  Method:      findByCreateDate(Date _createDate)
	 *  Description: 
	 *  Returns:     ArrayList<Account>
	 */
	public static ArrayList findByCreateDate(Date _createDate)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.findByCreateDate");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account WHERE createDate='"+toSQL(_createDate)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Account newObject = (Account)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Account();
						newObject.lockStatus=s.getInt(2);
						newObject.requestCount=s.getInt(3);
						newObject.createDate=s.getDate(4);
						newObject.serviceFactor=s.getInt(5);
						newObject.creditWarningCount=s.getInt(6);
						newObject.credit=s.getInt(7);
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
	 *  Method:      findByServiceFactor(int _serviceFactor)
	 *  Description: 
	 *  Returns:     ArrayList<Account>
	 */
	public static ArrayList findByServiceFactor(int _serviceFactor)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.findByServiceFactor");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account WHERE serviceFactor="+_serviceFactor+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Account newObject = (Account)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Account();
						newObject.lockStatus=s.getInt(2);
						newObject.requestCount=s.getInt(3);
						newObject.createDate=s.getDate(4);
						newObject.serviceFactor=s.getInt(5);
						newObject.creditWarningCount=s.getInt(6);
						newObject.credit=s.getInt(7);
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
	 *  Method:      findByCreditWarningCount(int _creditWarningCount)
	 *  Description: 
	 *  Returns:     ArrayList<Account>
	 */
	public static ArrayList findByCreditWarningCount(int _creditWarningCount)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.findByCreditWarningCount");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account WHERE creditWarningCount="+_creditWarningCount+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Account newObject = (Account)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Account();
						newObject.lockStatus=s.getInt(2);
						newObject.requestCount=s.getInt(3);
						newObject.createDate=s.getDate(4);
						newObject.serviceFactor=s.getInt(5);
						newObject.creditWarningCount=s.getInt(6);
						newObject.credit=s.getInt(7);
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
	 *  Method:      findByCredit(int _credit)
	 *  Description: 
	 *  Returns:     ArrayList<Account>
	 */
	public static ArrayList findByCredit(int _credit)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.findByCredit");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account WHERE credit="+_credit+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Account newObject = (Account)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Account();
						newObject.lockStatus=s.getInt(2);
						newObject.requestCount=s.getInt(3);
						newObject.createDate=s.getDate(4);
						newObject.serviceFactor=s.getInt(5);
						newObject.creditWarningCount=s.getInt(6);
						newObject.credit=s.getInt(7);
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
	 *  Returns:     Account
	 */
	public static Account findById(String _id)
	{
		Account result = (Account)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new Account();
					result.lockStatus= s.getInt(2);
					result.requestCount= s.getInt(3);
					result.createDate= s.getDate(4);
					result.serviceFactor= s.getInt(5);
					result.creditWarningCount= s.getInt(6);
					result.credit= s.getInt(7);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.commit");
			try
			{
				stmt.executeUpdate( "UPDATE Account set lockStatus= "+lockStatus+", requestCount= "+requestCount+", createDate= '"+toSQL(createDate)+"', serviceFactor= "+serviceFactor+", creditWarningCount= "+creditWarningCount+", credit= "+credit+" WHERE id='"+id+"'");
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
	 * returns ArrayList<Account>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit FROM Account order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Account newObject = (Account)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Account();
						newObject.lockStatus=s.getInt(2);
						newObject.requestCount=s.getInt(3);
						newObject.createDate=s.getDate(4);
						newObject.serviceFactor=s.getInt(5);
						newObject.creditWarningCount=s.getInt(6);
						newObject.credit=s.getInt(7);
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
	static public Account createInstance( int  _lockStatus, int  _requestCount, Date  _createDate, int  _serviceFactor, int  _creditWarningCount, int  _credit )
	{
		Account result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Account ( lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit, id) VALUES ("+_lockStatus+", "+_requestCount+",  '"+toSQL(_createDate)+"', "+_serviceFactor+", "+_creditWarningCount+", "+_credit+", '"+nextGUID+"')");
				result = new Account();
				result.lockStatus= _lockStatus;
				result.requestCount= _requestCount;
				result.createDate= _createDate;
				result.serviceFactor= _serviceFactor;
				result.creditWarningCount= _creditWarningCount;
				result.credit= _credit;
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
	static public void newInstance( int  _lockStatus, int  _requestCount, Date  _createDate, int  _serviceFactor, int  _creditWarningCount, int  _credit )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Account ( lockStatus, requestCount, createDate, serviceFactor, creditWarningCount, credit, id) VALUES ("+_lockStatus+", "+_requestCount+",  '"+toSQL(_createDate)+"', "+_serviceFactor+", "+_creditWarningCount+", "+_credit+", '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Account WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbAccount.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from Account" );
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
		result.append(lockStatus);
		result.append("|");
		result.append(requestCount);
		result.append("|");
		result.append(createDate);
		result.append("|");
		result.append(serviceFactor);
		result.append("|");
		result.append(creditWarningCount);
		result.append("|");
		result.append(credit);
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
			result=dMeta.getColumns(null,null,"Account","lockStatus");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'lockStatus' in table 'Account' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Account","requestCount");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'requestCount' in table 'Account' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Account","createDate");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'createDate' in table 'Account' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Account","serviceFactor");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'serviceFactor' in table 'Account' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Account","creditWarningCount");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'creditWarningCount' in table 'Account' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Account","credit");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'credit' in table 'Account' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Account","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'Account' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
