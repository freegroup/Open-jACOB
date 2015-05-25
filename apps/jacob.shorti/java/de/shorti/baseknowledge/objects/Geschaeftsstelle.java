package de.shorti.baseknowledge.objects;

/**
 * short-i Class generated by automatic ClassGenerator
 * Date: Wed Mar 14 14:59:41 GMT+01:00 2001
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import de.shorti.util.basic.*;

public class Geschaeftsstelle extends _dbGeschaeftsstelle
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

	/**
	 *  Method:      findByServiceAndZipCode(_dbService _service, _dbZipCode _zipCode)
	 *  Description:
	 *  Returns:     ArrayList<Geschaeftsstelle>
	 */
	public static ArrayList findByServiceAndZipCode(Service _service, ZipCode _zipCode)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbGeschaeftsstelle.findByAddress");
			try
			{
                String statement;
                statement  = "SELECT  Geschaeftsstelle.id ";
                statement += "  FROM  Geschaeftsstelle2Service, Geschaeftsstelle, Address ";
                statement += "  WHERE Geschaeftsstelle2Service.service_id='" + _service.getId() + "' ";
                statement += "    AND Geschaeftsstelle.id=Geschaeftsstelle2Service.geschaeftsstelle_id ";
                statement += "    AND Address.id=Geschaeftsstelle.address_id ";
                statement += "    AND Address.zipCode_id='" + _zipCode.getId() + "' ";

				ResultSet s = stmt.executeQuery(statement);
				while(s.next())
				{
					Geschaeftsstelle newObject = Geschaeftsstelle.findById(s.getString(1));
                    if(newObject != null)
                    {
					    result.add(newObject);
                    }
				}
			}
			catch(Exception exc)
			{
                trace.error(exc);
			}
			stmt.close();

		}
		catch (Exception ex)
		{
            trace.error(ex);
		}
		return result;
	}

	/**
	 *  Method:      findByServiceAndZipCode(_dbService _service, _dbZipCode _zipCode)
	 *  Description:
	 *  Returns:     ArrayList<Geschaeftsstelle>
	 */
	public static ArrayList findByServiceAndZipCodes(Service _service, ArrayList _zipCodes)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbGeschaeftsstelle.findByAddress");
			try
			{
                StringBuffer sb = new StringBuffer();
                sb.append("SELECT  distinct Geschaeftsstelle.id ");
                sb.append("  FROM  Geschaeftsstelle2Service, Geschaeftsstelle, Address ");
                sb.append("  WHERE Address.zipCode_id in (");
                Iterator iter = _zipCodes.iterator();
                while(iter.hasNext())
                {
                    ZipCode zip = (ZipCode)iter.next();
                    sb.append("'"+zip.getId()+"'");
                    if(iter.hasNext())
                        sb.append(",");
                }
                sb.append(") ");
                sb.append("  AND Geschaeftsstelle2Service.service_id='" + _service.getId() + "' ");
                sb.append("  AND Geschaeftsstelle.id=Geschaeftsstelle2Service.geschaeftsstelle_id ");
                sb.append("  AND Address.id=Geschaeftsstelle.address_id ");

				ResultSet s = stmt.executeQuery(sb.toString());
				while(s.next())
				{
					Geschaeftsstelle newObject = Geschaeftsstelle.findById(s.getString(1));
                    if(newObject != null)
                    {
					    result.add(newObject);
                    }
				}
			}
			catch(Exception exc)
			{
                trace.error(exc);
			}
            finally
            {
    			stmt.close();
            }
    	}
		catch (Exception ex)
		{
            trace.error(ex);
		}
		return result;
	}
}
