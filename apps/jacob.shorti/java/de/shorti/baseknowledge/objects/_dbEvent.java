package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:23 GMT+02:00 2002
 */
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

class _dbEvent extends dbObject 
{

	String    allDayEventPattern;
	String    foreignId;
	int       allDayEvent;
	int       reminderSet;
	int       isPrivate;
	long      creationTime;
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
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Event WHERE id='"+_key+"'");
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
	 * Method:      getAllDayEventPattern()
	 * Description: 
	 * Returns:     String
	 */
	public String getAllDayEventPattern()
	{
		return allDayEventPattern;
	}

	/**
	 * Method:      setAllDayEventPattern(String _allDayEventPattern, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setAllDayEventPattern(String _allDayEventPattern, boolean _autoCommit)
	{
		allDayEventPattern=_allDayEventPattern;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getForeignId()
	 * Description: 
	 * Returns:     String
	 */
	public String getForeignId()
	{
		return foreignId;
	}

	/**
	 * Method:      setForeignId(String _foreignId, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setForeignId(String _foreignId, boolean _autoCommit)
	{
		foreignId=_foreignId;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getAllDayEvent()
	 * Description: 
	 * Returns:     int
	 */
	public int getAllDayEvent()
	{
		return allDayEvent;
	}

	/**
	 * Method:      setAllDayEvent(int _allDayEvent, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setAllDayEvent(int _allDayEvent, boolean _autoCommit)
	{
		allDayEvent=_allDayEvent;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getReminderSet()
	 * Description: 
	 * Returns:     int
	 */
	public int getReminderSet()
	{
		return reminderSet;
	}

	/**
	 * Method:      setReminderSet(int _reminderSet, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setReminderSet(int _reminderSet, boolean _autoCommit)
	{
		reminderSet=_reminderSet;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getIsPrivate()
	 * Description: 
	 * Returns:     int
	 */
	public int getIsPrivate()
	{
		return isPrivate;
	}

	/**
	 * Method:      setIsPrivate(int _isPrivate, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setIsPrivate(int _isPrivate, boolean _autoCommit)
	{
		isPrivate=_isPrivate;
		if(_autoCommit)
		{
			commit();
		}
	}

	/**
	 * Method:      getCreationTime()
	 * Description: 
	 * Returns:     long
	 */
	public long getCreationTime()
	{
		return creationTime;
	}

	/**
	 * Method:      setCreationTime(long _creationTime, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setCreationTime(long _creationTime, boolean _autoCommit)
	{
		creationTime=_creationTime;
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
	 *  Method:      findByAllDayEventPattern(String _allDayEventPattern)
	 *  Description: 
	 *  Returns:     ArrayList<Event>
	 */
	public static ArrayList findByAllDayEventPattern(String _allDayEventPattern)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findByAllDayEventPattern");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE allDayEventPattern='"+toSQL(_allDayEventPattern)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Event newObject = (Event)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Event();
						newObject.allDayEventPattern=s.getString(2);
						newObject.foreignId=s.getString(3);
						newObject.allDayEvent=s.getInt(4);
						newObject.reminderSet=s.getInt(5);
						newObject.isPrivate=s.getInt(6);
						newObject.creationTime=s.getLong(7);
						newObject.calendar_id=s.getString(8);
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
	 *  Method:      findByForeignId(String _foreignId)
	 *  Description: 
	 *  Returns:     Event
	 */
	public static Event findByForeignId(String _foreignId)
	{
		Event result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findByForeignId");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE foreignId='"+toSQL(_foreignId)+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (Event)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new Event();
						result.allDayEventPattern= s.getString(2);
						result.foreignId= s.getString(3);
						result.allDayEvent= s.getInt(4);
						result.reminderSet= s.getInt(5);
						result.isPrivate= s.getInt(6);
						result.creationTime= s.getLong(7);
						result.calendar_id= s.getString(8);
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
	 *  Method:      findByAllDayEvent(int _allDayEvent)
	 *  Description: 
	 *  Returns:     ArrayList<Event>
	 */
	public static ArrayList findByAllDayEvent(int _allDayEvent)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findByAllDayEvent");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE allDayEvent="+_allDayEvent+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Event newObject = (Event)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Event();
						newObject.allDayEventPattern=s.getString(2);
						newObject.foreignId=s.getString(3);
						newObject.allDayEvent=s.getInt(4);
						newObject.reminderSet=s.getInt(5);
						newObject.isPrivate=s.getInt(6);
						newObject.creationTime=s.getLong(7);
						newObject.calendar_id=s.getString(8);
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
	 *  Method:      findByReminderSet(int _reminderSet)
	 *  Description: 
	 *  Returns:     ArrayList<Event>
	 */
	public static ArrayList findByReminderSet(int _reminderSet)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findByReminderSet");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE reminderSet="+_reminderSet+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Event newObject = (Event)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Event();
						newObject.allDayEventPattern=s.getString(2);
						newObject.foreignId=s.getString(3);
						newObject.allDayEvent=s.getInt(4);
						newObject.reminderSet=s.getInt(5);
						newObject.isPrivate=s.getInt(6);
						newObject.creationTime=s.getLong(7);
						newObject.calendar_id=s.getString(8);
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
	 *  Method:      findByIsPrivate(int _isPrivate)
	 *  Description: 
	 *  Returns:     ArrayList<Event>
	 */
	public static ArrayList findByIsPrivate(int _isPrivate)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findByIsPrivate");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE isPrivate="+_isPrivate+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Event newObject = (Event)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Event();
						newObject.allDayEventPattern=s.getString(2);
						newObject.foreignId=s.getString(3);
						newObject.allDayEvent=s.getInt(4);
						newObject.reminderSet=s.getInt(5);
						newObject.isPrivate=s.getInt(6);
						newObject.creationTime=s.getLong(7);
						newObject.calendar_id=s.getString(8);
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
	 *  Method:      findByCreationTime(long _creationTime)
	 *  Description: 
	 *  Returns:     ArrayList<Event>
	 */
	public static ArrayList findByCreationTime(long _creationTime)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findByCreationTime");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE creationTime="+_creationTime+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Event newObject = (Event)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Event();
						newObject.allDayEventPattern=s.getString(2);
						newObject.foreignId=s.getString(3);
						newObject.allDayEvent=s.getInt(4);
						newObject.reminderSet=s.getInt(5);
						newObject.isPrivate=s.getInt(6);
						newObject.creationTime=s.getLong(7);
						newObject.calendar_id=s.getString(8);
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
	 *  Returns:     ArrayList<Event>
	 */
	public static ArrayList findByCalendar(_dbCalendar _calendar)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findByCalendar");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE calendar_id='"+toSQL(((_calendar==null)?"":_calendar.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Event newObject = (Event)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Event();
						newObject.allDayEventPattern=s.getString(2);
						newObject.foreignId=s.getString(3);
						newObject.allDayEvent=s.getInt(4);
						newObject.reminderSet=s.getInt(5);
						newObject.isPrivate=s.getInt(6);
						newObject.creationTime=s.getLong(7);
						newObject.calendar_id=s.getString(8);
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
	 *  Returns:     Event
	 */
	public static Event findById(String _id)
	{
		Event result = (Event)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new Event();
					result.allDayEventPattern= s.getString(2);
					result.foreignId= s.getString(3);
					result.allDayEvent= s.getInt(4);
					result.reminderSet= s.getInt(5);
					result.isPrivate= s.getInt(6);
					result.creationTime= s.getLong(7);
					result.calendar_id= s.getString(8);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.commit");
			try
			{
				stmt.executeUpdate( "UPDATE Event set allDayEventPattern= '"+toSQL(allDayEventPattern)+"', foreignId= '"+toSQL(foreignId)+"', allDayEvent= "+allDayEvent+", reminderSet= "+reminderSet+", isPrivate= "+isPrivate+", creationTime= "+creationTime+", calendar_id= '"+toSQL(calendar_id)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<Event>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id FROM Event order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					Event newObject = (Event)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new Event();
						newObject.allDayEventPattern=s.getString(2);
						newObject.foreignId=s.getString(3);
						newObject.allDayEvent=s.getInt(4);
						newObject.reminderSet=s.getInt(5);
						newObject.isPrivate=s.getInt(6);
						newObject.creationTime=s.getLong(7);
						newObject.calendar_id=s.getString(8);
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
	static public Event createInstance( String  _allDayEventPattern, String  _foreignId, int  _allDayEvent, int  _reminderSet, int  _isPrivate, long  _creationTime, _dbCalendar _calendar )
	{
		Event result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Event ( allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id, id) VALUES ( '"+toSQL(_allDayEventPattern)+"',  '"+toSQL(_foreignId)+"', "+_allDayEvent+", "+_reminderSet+", "+_isPrivate+", "+_creationTime+",  '"+((_calendar==null)?"":_calendar.getId())+"', '"+nextGUID+"')");
				result = new Event();
				result.allDayEventPattern= _allDayEventPattern;
				result.foreignId= _foreignId;
				result.allDayEvent= _allDayEvent;
				result.reminderSet= _reminderSet;
				result.isPrivate= _isPrivate;
				result.creationTime= _creationTime;
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
	static public void newInstance( String  _allDayEventPattern, String  _foreignId, int  _allDayEvent, int  _reminderSet, int  _isPrivate, long  _creationTime, _dbCalendar _calendar )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO Event ( allDayEventPattern, foreignId, allDayEvent, reminderSet, isPrivate, creationTime, calendar_id, id) VALUES ( '"+toSQL(_allDayEventPattern)+"',  '"+toSQL(_foreignId)+"', "+_allDayEvent+", "+_reminderSet+", "+_isPrivate+", "+_creationTime+",  '"+((_calendar==null)?"":_calendar.getId())+"', '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM Event WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbEvent.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from Event" );
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
		result.append(allDayEventPattern);
		result.append("|");
		result.append(foreignId);
		result.append("|");
		result.append(allDayEvent);
		result.append("|");
		result.append(reminderSet);
		result.append("|");
		result.append(isPrivate);
		result.append("|");
		result.append(creationTime);
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
			result=dMeta.getColumns(null,null,"Event","allDayEventPattern");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'allDayEventPattern' in table 'Event' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Event","foreignId");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'foreignId' in table 'Event' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Event","allDayEvent");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'allDayEvent' in table 'Event' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Event","reminderSet");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'reminderSet' in table 'Event' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Event","isPrivate");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'isPrivate' in table 'Event' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Event","creationTime");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'creationTime' in table 'Event' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Event","calendar_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'calendar_id' in table 'Event' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"Event","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'Event' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}