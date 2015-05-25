package de.shorti.baseknowledge.objects;

/**
 * Class generated by automatic ClassGenerator
 * Date: Sat May 11 21:15:22 GMT+02:00 2002
 */
import de.shorti.db.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

class _dbBannerContract extends dbObject 
{

	String    contactor_id;	// Foreign Key pointing to Table [Contactor], Field [id]
	String    companyContactPerson_id;	// Foreign Key pointing to Table [ContactPerson], Field [id]
	Date      createDate;
	String    partnercompany_id;	// Foreign Key pointing to Table [PartnerCompany], Field [id]
	String    comment;


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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM BannerContract WHERE id='"+_key+"'");
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
	 * Method:      getComment()
	 * Description: 
	 * Returns:     String
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * Method:      setComment(String _comment, boolean _autoCommit)
	 * Description: 
	 * Returns:     void
	 */
	public void setComment(String _comment, boolean _autoCommit)
	{
		comment=_comment;
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
	 * Method:      getContactor()
	 * Description: 
	 * Returns:     Contactor
	 */
	public Contactor getContactor()
	{
		return Contactor.findById(contactor_id);
	}

	/**
	 * Method:      setContactor()
	 * Description: 
	 * Returns:     void
	 */
	public void setContactor(Contactor _foreigner, boolean _autocommit)
	{
		contactor_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getCompanyContactPerson()
	 * Description: 
	 * Returns:     ContactPerson
	 */
	public ContactPerson getCompanyContactPerson()
	{
		return ContactPerson.findById(companyContactPerson_id);
	}

	/**
	 * Method:      setCompanyContactPerson()
	 * Description: 
	 * Returns:     void
	 */
	public void setCompanyContactPerson(ContactPerson _foreigner, boolean _autocommit)
	{
		companyContactPerson_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 * Method:      getPartnercompany()
	 * Description: 
	 * Returns:     PartnerCompany
	 */
	public PartnerCompany getPartnercompany()
	{
		return PartnerCompany.findById(partnercompany_id);
	}

	/**
	 * Method:      setPartnercompany()
	 * Description: 
	 * Returns:     void
	 */
	public void setPartnercompany(PartnerCompany _foreigner, boolean _autocommit)
	{
		partnercompany_id= _foreigner.getId();
		if (_autocommit == true)
			commit();
	}

	/**
	 *  Method:      getBannerAdvertisements()
	 *  Description:                              
	 *  Returns:     ArrayList<BannerAdvertisement>
	 */
	public ArrayList getBannerAdvertisements()
	{
		return _dbBannerAdvertisement.findByBannerContract(this);
	}


	/**
	 *  Method:      findByContactor(_dbContactor _contactor)
	 *  Description: 
	 *  Returns:     ArrayList<BannerContract>
	 */
	public static ArrayList findByContactor(_dbContactor _contactor)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.findByContactor");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment FROM BannerContract WHERE contactor_id='"+toSQL(((_contactor==null)?"":_contactor.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerContract newObject = (BannerContract)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerContract();
						newObject.contactor_id=s.getString(2);
						newObject.companyContactPerson_id=s.getString(3);
						newObject.createDate=s.getDate(4);
						newObject.partnercompany_id=s.getString(5);
						newObject.comment=s.getString(6);
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
	 *  Method:      findByCompanyContactPerson(_dbContactPerson _companyContactPerson)
	 *  Description: 
	 *  Returns:     ArrayList<BannerContract>
	 */
	public static ArrayList findByCompanyContactPerson(_dbContactPerson _companyContactPerson)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.findByCompanyContactPerson");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment FROM BannerContract WHERE companyContactPerson_id='"+toSQL(((_companyContactPerson==null)?"":_companyContactPerson.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerContract newObject = (BannerContract)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerContract();
						newObject.contactor_id=s.getString(2);
						newObject.companyContactPerson_id=s.getString(3);
						newObject.createDate=s.getDate(4);
						newObject.partnercompany_id=s.getString(5);
						newObject.comment=s.getString(6);
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
	 *  Returns:     ArrayList<BannerContract>
	 */
	public static ArrayList findByCreateDate(Date _createDate)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.findByCreateDate");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment FROM BannerContract WHERE createDate='"+toSQL(_createDate)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerContract newObject = (BannerContract)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerContract();
						newObject.contactor_id=s.getString(2);
						newObject.companyContactPerson_id=s.getString(3);
						newObject.createDate=s.getDate(4);
						newObject.partnercompany_id=s.getString(5);
						newObject.comment=s.getString(6);
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
	 *  Method:      findByPartnercompany(_dbPartnerCompany _partnercompany)
	 *  Description: 
	 *  Returns:     ArrayList<BannerContract>
	 */
	public static ArrayList findByPartnercompany(_dbPartnerCompany _partnercompany)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.findByPartnercompany");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment FROM BannerContract WHERE partnercompany_id='"+toSQL(((_partnercompany==null)?"":_partnercompany.getId()))+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerContract newObject = (BannerContract)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerContract();
						newObject.contactor_id=s.getString(2);
						newObject.companyContactPerson_id=s.getString(3);
						newObject.createDate=s.getDate(4);
						newObject.partnercompany_id=s.getString(5);
						newObject.comment=s.getString(6);
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
	 *  Method:      findByComment(String _comment)
	 *  Description: 
	 *  Returns:     ArrayList<BannerContract>
	 */
	public static ArrayList findByComment(String _comment)
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.findByComment");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment FROM BannerContract WHERE comment='"+toSQL(_comment)+"'"+" order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerContract newObject = (BannerContract)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerContract();
						newObject.contactor_id=s.getString(2);
						newObject.companyContactPerson_id=s.getString(3);
						newObject.createDate=s.getDate(4);
						newObject.partnercompany_id=s.getString(5);
						newObject.comment=s.getString(6);
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
	 *  Returns:     BannerContract
	 */
	public static BannerContract findById(String _id)
	{
		BannerContract result = (BannerContract)getFromCache(_id);
		if(result!=null) return result;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.findById");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment FROM BannerContract WHERE id='"+toSQL(_id)+"'"+" order by generatedId desc");
				if(s.next())
				{
					result = new BannerContract();
					result.contactor_id= s.getString(2);
					result.companyContactPerson_id= s.getString(3);
					result.createDate= s.getDate(4);
					result.partnercompany_id= s.getString(5);
					result.comment= s.getString(6);
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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.commit");
			try
			{
				stmt.executeUpdate( "UPDATE BannerContract set contactor_id= '"+toSQL(contactor_id)+"', companyContactPerson_id= '"+toSQL(companyContactPerson_id)+"', createDate= '"+toSQL(createDate)+"', partnercompany_id= '"+toSQL(partnercompany_id)+"', comment= '"+toSQL(comment)+"' WHERE id='"+id+"'");
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
	 * returns ArrayList<BannerContract>
	 */
	public static ArrayList getAll()
	{
		ArrayList result = new ArrayList();
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.getAll");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT id, contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment FROM BannerContract order by generatedId desc");
				while(s.next())
				{
					String _tmpID = s.getString(1);
					BannerContract newObject = (BannerContract)getFromCache(_tmpID);
					if(newObject ==null)
					{
						newObject = new BannerContract();
						newObject.contactor_id=s.getString(2);
						newObject.companyContactPerson_id=s.getString(3);
						newObject.createDate=s.getDate(4);
						newObject.partnercompany_id=s.getString(5);
						newObject.comment=s.getString(6);
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
	static public BannerContract createInstance( _dbContactor _contactor, _dbContactPerson _companyContactPerson, Date  _createDate, _dbPartnerCompany _partnercompany, String  _comment )
	{
		BannerContract result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.createInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO BannerContract ( contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment, id) VALUES ( '"+((_contactor==null)?"":_contactor.getId())+"',  '"+((_companyContactPerson==null)?"":_companyContactPerson.getId())+"',  '"+toSQL(_createDate)+"',  '"+((_partnercompany==null)?"":_partnercompany.getId())+"',  '"+toSQL(_comment)+"', '"+nextGUID+"')");
				result = new BannerContract();
				result.contactor_id= (_contactor==null)?"":_contactor.getId();
				result.companyContactPerson_id= (_companyContactPerson==null)?"":_companyContactPerson.getId();
				result.createDate= _createDate;
				result.partnercompany_id= (_partnercompany==null)?"":_partnercompany.getId();
				result.comment= _comment;
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
	static public void newInstance( _dbContactor _contactor, _dbContactPerson _companyContactPerson, Date  _createDate, _dbPartnerCompany _partnercompany, String  _comment )
	{
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.newInstance");
			try
			{
				String nextGUID = new java.rmi.server.UID().toString();
				stmt.executeUpdate( "INSERT INTO BannerContract ( contactor_id, companyContactPerson_id, createDate, partnercompany_id, comment, id) VALUES ( '"+((_contactor==null)?"":_contactor.getId())+"',  '"+((_companyContactPerson==null)?"":_companyContactPerson.getId())+"',  '"+toSQL(_createDate)+"',  '"+((_partnercompany==null)?"":_partnercompany.getId())+"',  '"+toSQL(_comment)+"', '"+nextGUID+"')");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.destroyInstance");
			try
			{
					stmt.executeUpdate( "DELETE FROM BannerContract WHERE id='"+_key+"'");
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
			stmt  = ConnectionManager.getValid().createStatement("_dbBannerContract.destroyAll");
			try
			{
					stmt.executeUpdate("DELETE from BannerContract" );
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
		result.append(contactor_id);
		result.append("|");
		result.append(companyContactPerson_id);
		result.append("|");
		result.append(createDate);
		result.append("|");
		result.append(partnercompany_id);
		result.append("|");
		result.append(comment);
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
			result=dMeta.getColumns(null,null,"BannerContract","contactor_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'contactor_id' in table 'BannerContract' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerContract","companyContactPerson_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'companyContactPerson_id' in table 'BannerContract' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerContract","createDate");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'createDate' in table 'BannerContract' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerContract","partnercompany_id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'partnercompany_id' in table 'BannerContract' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerContract","comment");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'comment' in table 'BannerContract' missing");
			}
			result.close();
			result=dMeta.getColumns(null,null,"BannerContract","id");
			if(!result.next())
			{
				System.out.println("ERROR: installed DB-schema not compatible to java classes");
				System.out.println("       Attribute 'id' in table 'BannerContract' missing");
			}
			result.close();
		}
		catch (Exception ex)
		{
		}
	}


}