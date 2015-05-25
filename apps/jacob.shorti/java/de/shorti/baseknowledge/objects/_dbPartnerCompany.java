package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:22 GMT+02:00 2002
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

class _dbPartnerCompany extends dbObject 
{

	String    company_id;	// Foreign Key pointing to Table [Company], Field [id]


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
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM PartnerCompany WHERE id='"+_key+"'");
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
	 * Method:      getId()
	 * Description: 
	 * Returns:     String
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Method:      getCompany()
	 * Description: 
	 * Returns:     Company
	 */
	public Company getCompany()
	{
		return Company.findById(company_id);
	}

	/**
	 * Method:      setCompany()
	 * Description: 
	 * Returns:     void
	 */
	public void setCompany(Company _foreigner, boolean _autocommit)
	{
		company_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      getBannerContracts()
	 *  Description:                              
	 *  Returns:     ArrayList<BannerContract>
	 */
	public ArrayList getBannerContracts()
	{
		return _dbBannerContract.findByPartnercompany(this);
	}


	/**
	 *  Method:      getBillingInformations()
	 *  Description:                              
	 *  Returns:     ArrayList<BillingInformation>
	 */
	public ArrayList getBillingInformations()
	{
		return _dbBillingInformation.findByPartnerCompany(this);
	}


	/**
	 *  Method:      getContextAmountItems()
	 *  Description:                              
	 *  Returns:     ArrayList<ContextAmountItem>
	 */
	public ArrayList getContextAmountItems()
	{
		return _dbContextAmountItem.findByPartnerCompany(this);
	}


	/**
	 *  Method:      findByCompany(_dbCompany _company)
	 *  Description: 
	 *  Returns:     PartnerCompany
	 */
	public static PartnerCompany findByCompany(_dbCompany _company)
	{
		PartnerCompany result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.findByCompany");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, company_id FROM PartnerCompany WHERE company_id='"+toSQL(((_company==null)?"":_company.getId()))+"'"+" order by generatedId desc");
				if(s.next())
				{
					String _tmpID = s.getString(1);
					result = (PartnerCompany)getFromCache(_tmpID);
					if(result ==null)
					{
						result = new PartnerCompany();
						result.company_id= s.getString(2);
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
	 *  Method:      findById(String _id)
	 *  Description: 
	 *  Returns:     PartnerCompany
	 */
	public static PartnerCompany findById(String _id)
	{
		PartnerCompany result = (PartnerCompany)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, company_id FROM PartnerCompany WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new PartnerCompany();
					result.company_id= s.getString(2);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.commit");
			try
			{
				stmt.executeUpdate( "UPDATE PartnerCompany set company_id= '"+toSQL(company_id)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<PartnerCompany>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, company_id FROM PartnerCompany order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					PartnerCompany newObject = (PartnerCompany)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new PartnerCompany();
						newObject.company_id=s.getString(2);
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
	static public PartnerCompany createInstance( _dbCompany _company )
	{
		PartnerCompany result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO PartnerCompany ( company_id, id) VALUES ( '"+((_company==null)?"":_company.getId())+"', '"+nextGUID+"')");
				result = new PartnerCompany();
				result.company_id= (_company==null)?"":_company.getId();
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
	static public void newInstance( _dbCompany _company )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO PartnerCompany ( company_id, id) VALUES ( '"+((_company==null)?"":_company.getId())+"', '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM PartnerCompany WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbPartnerCompany.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from PartnerCompany" );
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
		result.append(company_id);
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
			result=dMeta.getColumns(null,null,"PartnerCompany","company_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'company_id' in table 'PartnerCompany' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"PartnerCompany","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'PartnerCompany' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}
