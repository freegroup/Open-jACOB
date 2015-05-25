/*
 * Created on 19.07.2004
 * by mike
 *
 */
package jacob.common;

import org.apache.commons.lang.SystemUtils;

import jacob.common.data.DataUtils;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.UserManagement;



/**
 * Die apache.commons kommt mit Umlaute nicht zurecht <br>
 * also rausfiltern!
 * @author mike
 *
 */
public class Util
{
  static public final transient String RCS_ID = "$Id: Util.java,v 1.5 2005/09/26 10:31:13 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

	public static String PrepareSoundex(String str)
	{
		String out="";
		String without="ßüäöÜÄÖ";
		
		for (int i = 0; i < str.length(); i++)
		{
			String sub = str.substring(i,i+1);
			if (without.indexOf(sub) < 0)
			{
				out = out + sub;
			}
		}
		return out;
	}	
	
	/**
	 * Speichert in cclist die Userid um zu sehen welcher User über den Jacob den Datensatz manipuliert
	 * @param record
	 * @param transaction
	 * @throws Exception
	 */
	public static void TraceUser(IDataTableRecord record,IDataTransaction transaction) throws Exception
	{
		String cclist = record.getSaveStringValue("cclist");
		String userID = "#" + transaction.getUser().getLoginId();
		cclist = cclist + userID;
		if (cclist.length()<81)
		{
			record.setStringValue(transaction,"cclist",cclist);
		}
	}
	/**
	 * Workaround um festzustellen ob ich SystemUser bin
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isSystemUser(Context context)
	{
        // MIKE: HACK HACK. Bitte ordentlcih aPI bereitstellen um SystemUser zu erkennen
        //
        try
        {
            context.getUser().getKey();
        }
        catch (RuntimeException e)
        {
            return true;
        }
        return false;
	}
	public static String getUserKey(Context context) throws Exception
	{
		String userKey;
		if (isSystemUser(context))
		{
			userKey=DataUtils.getAppprofileValue(context,"webqagent_key");
		}
		else
		{
			userKey = context.getUser().getKey();
		}
		return userKey;
	}
	//MIKE: Workaround SystemUser korrigieren
	public static String getUserLoginID(Context context) throws Exception
	{
		String loginID;
		if (isSystemUser(context))
		{
			loginID="caretaker";
		}
		else
		{
			loginID = context.getUser().getLoginId();
		}
		return loginID;
	}
	public static void main(String[] args)
	{
		System.out.println(PrepareSoundex("Döring"));
	}
}
