package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

public class Appointment extends _dbAppointment
{
	/*
	 * Meeting Status (copied from outlook help
	 */
	public final static int MEETINGSTATUS_NONE = 0;
	public final static int MEETINGSTATUS_ORGANIZER = 1;
	public final static int MEETINGSTATUS_TENTATIVE_ACCEPTED = 2;
	public final static int MEETINGSTATUS_ACCEPTED = 3;
	public final static int MEETINGSTATUS_DECLINED = 4;
	public final static int MEETINGSTATUS_NOT_ACCEPTED_YET = 5;

    public static void markAllForDelete(Calendar calendar)
    {
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAppointment.commit");
			try
			{
				stmt.executeUpdate( "UPDATE Appointment set markedForDelete= 1 WHERE calendar_id='"+toSQL(calendar.getId())+"'");
			}
			catch(Exception exc)
			{
				System.err.println(exc);
			}
			stmt.close();

		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
    }

    public static void deleteAllMarked(Calendar calendar)
    {
		SaveStatement  stmt;
		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbAppointment.commit");
			try
			{
				stmt.executeUpdate( "DELETE From Appointment WHERE  markedForDelete= 1 AND calendar_id='"+toSQL(calendar.getId())+"'");
			}
			catch(Exception exc)
			{
				System.err.println(exc);
			}
			stmt.close();

		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
    }

}