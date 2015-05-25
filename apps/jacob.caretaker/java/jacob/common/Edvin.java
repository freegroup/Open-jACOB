/*
 * Created on Aug 19, 2004
 *
 */
package jacob.common;

import jacob.config.Config;
import jacob.exception.BusinessException;

import java.sql.*;
import java.util.*;

/**
 *
 */
public class Edvin
{
	static public final transient String RCS_ID = "$Id: Edvin.java,v 1.7 2005/02/15 16:40:24 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.7 $";
	
/*
  */
  public static class FieldConstraint
  {
    public final static int NICHTFELD=0;
    public final static int PFLICHTFELD=1;
    public final static int KANNFELD=2;
    
    final int anl_ausfall;
    final int baueinheit;
    final int baugruppe;
    final int bauteil;
    final int mass_code;
    final int schad_code;
    final int stoerbeginn;
    final int stoerdauer;
    
    public FieldConstraint(int anl_ausfall, int baueinheit,int baugruppe,int bauteil,int mass_code, int schad_code,int stoerbeginn,int stoerdauer)
    {
      this.anl_ausfall = anl_ausfall;
      this.baueinheit = baueinheit;
      this.baugruppe = baugruppe;
      this.bauteil = bauteil;
      this.mass_code = mass_code;
      this.schad_code = schad_code;
      this.stoerbeginn = stoerbeginn;
      this.stoerdauer = stoerdauer;
    }
  }
  
  
  /**
   * modifiziert das Constrint zu einer gültigen Query
   * ^BLA bedeutet like 'bla%'
   * BLA bedeutet öile '%bla%'
 * @param constraint
 * @return
 */
private static String sqlQueryString(String constraint)
  {
    if (constraint.startsWith("^")&& constraint.length()>1)
    {
        return constraint.substring(1).toLowerCase() + "%";
    }
    return "%"+ constraint.toLowerCase() + "%";
  }
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param substringCode
   * @param substringDesc
   * @return String[code][description]
   * @throws Exception
   */
  public static String[][] getBaueinheiten(String edvin, String hwgName, String substringCode, String substringDesc) throws Exception
  {
    List data = new ArrayList();
    String statement="SELECT HIGR_BEH_CODE, HIGR_BEH_BEZ  FROM SMC_EXP_HIGR_BEH "+
								    " WHERE HIGR_IH_GRUPPE='"+hwgName+"' "+
								    " AND LOWER(HIGR_BEH_CODE) like '"+sqlQueryString(substringCode)+ "' " +
								    " AND LOWER(HIGR_BEH_BEZ)  like '"+sqlQueryString(substringDesc)+ "' order by HIGR_BEH_CODE";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      while (rset.next())
      {
        data.add(new String[]{rset.getString(1),rset.getString(2)});
      }
    }
    finally
    {
      if(conn!=null) conn.close();
    }
    return(String[][])data.toArray(new String[data.size()][]);
  }

  
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param substringCode
   * @param substringDesc
   * @return String[code][description]
   * @throws Exception
   */
  public static String[][] getBaugruppe(String edvin, String hwgName, String substringCode, String substringDesc) throws Exception
  {
    List data = new ArrayList();
    String statement="SELECT HIGR_BGR_CODE, HIGR_BGR_BEZ FROM SMC_EXP_HIGR_BGR "+
    " WHERE HIGR_IH_GRUPPE='"+hwgName+"' "+
    " AND LOWER(HIGR_BGR_CODE) like '"+sqlQueryString(substringCode)+ "' " +
    " AND LOWER(HIGR_BGR_BEZ)  like '"+sqlQueryString(substringDesc)+ "' order by HIGR_BGR_CODE";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      while (rset.next())
      {
        data.add(new String[]{rset.getString(1),rset.getString(2)});
      }
    }
    finally
    {
      if(conn!=null) conn.close();
    }
    return(String[][])data.toArray(new String[data.size()][]);
  }

  /**
   * 
   * @param edvin
   * @param hwgName
   * @param substringCode
   * @param substringDesc
   * @return String[code][description]
   * @throws Exception
   */
  public static String[][] getBauteil(String edvin, String hwgName, String substringCode, String substringDesc) throws Exception
  {
    List data = new ArrayList();
    String statement="SELECT HIGR_BTL_CODE, HIGR_BTL_BEZ FROM SMC_EXP_HIGR_BTL "+
									    " WHERE HIGR_IH_GRUPPE='"+hwgName+"' "+
									    " AND LOWER(HIGR_BTL_CODE) like '"+sqlQueryString(substringCode)+ "' " +
									    " AND LOWER(HIGR_BTL_BEZ)  like '"+sqlQueryString(substringDesc)+ "' order by HIGR_BTL_CODE";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      while (rset.next())
      {
        data.add(new String[]{rset.getString(1),rset.getString(2)});
      }
    }
    finally
    {
      if(conn!=null) conn.close();
    }
    return(String[][])data.toArray(new String[data.size()][]);
  }

  
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param substringCode
   * @param substringDesc
   * @return String[code][description]
   * @throws Exception
   */
  public static String[][] getMassnahmencode(String edvin, String hwgName, String substringCode, String substringDesc) throws Exception
  {
    List data = new ArrayList();
    String statement="SELECT HIGR_MAH_CODE, HIGR_MAH_BEZ FROM SMC_EXP_HIGR_MAH "+
									    " WHERE HIGR_IH_GRUPPE='"+hwgName+"' "+
									    " AND LOWER(HIGR_MAH_CODE) like '"+sqlQueryString(substringCode)+ "' " +
									    " AND LOWER(HIGR_MAH_BEZ)  like '"+sqlQueryString(substringDesc)+ "' order by HIGR_MAH_CODE";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      while (rset.next())
      {
        data.add(new String[]{rset.getString(1),rset.getString(2)});
      }
    }
    finally
    {
      if(conn!=null) conn.close();
    }
    return(String[][])data.toArray(new String[data.size()][]);
  }

  
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param substringCode
   * @param substringDesc
   * @return String[code][description]
   * @throws Exception
   */
  public static String[][] getSchadencode(String edvin, String hwgName, String substringCode, String substringDesc) throws Exception
  {
    List data = new ArrayList();
    String statement="SELECT HIGR_SHA_CODE, HIGR_SHA_BEZ FROM SMC_EXP_HIGR_SHA "+
									    " WHERE HIGR_IH_GRUPPE='"+hwgName+"' "+
									    " AND LOWER(HIGR_SHA_CODE) like '"+sqlQueryString(substringCode)+ "' " +
									    " AND LOWER(HIGR_SHA_BEZ)  like '"+sqlQueryString(substringDesc)+ "' order by HIGR_SHA_CODE";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      while (rset.next())
      {
        data.add(new String[]{rset.getString(1),rset.getString(2)});
      }
    }
    finally
    {
      if(conn!=null) conn.close();
    }
    return(String[][])data.toArray(new String[data.size()][]);
  }

  

  /**
   * 
   * @param edvin
   * @param hwgName
   * @param art
   * @return
   * @throws Exception
   */
  public static FieldConstraint getFieldConstraints(String edvin, String hwgName, String auftragsArt) throws Exception
  {
    String statement="SELECT higrha_anl_ausfall, higrha_baueinheit, higrha_baugruppe, higrha_bauteil, higrha_mass_code, higrha_schad_code,higrha_stoerbeginn,higrha_stoerdauer  FROM SMC_EXP_HIGRHA WHERE HIGRHA_IH_GRUPPE='"+hwgName+"' AND HIGRHA_IH_ART='"+auftragsArt+"'";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet r = stmt.executeQuery(statement);
      if(r.next())
        return new FieldConstraint(r.getInt(1),r.getInt(2),r.getInt(3),r.getInt(4),r.getInt(5),r.getInt(6),r.getInt(7),r.getInt(8));
      return new FieldConstraint(FieldConstraint.KANNFELD,FieldConstraint.KANNFELD,FieldConstraint.KANNFELD,FieldConstraint.KANNFELD,FieldConstraint.KANNFELD,FieldConstraint.KANNFELD,FieldConstraint.KANNFELD,FieldConstraint.KANNFELD);
    }
    finally
    {
      if(conn!=null) conn.close();
    }
  }
  
  
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param code
   * @return
   * @throws Exception
   */
  public static boolean isValidBaueinheitForHwg(String edvin, String hwgName, String code) throws Exception
  {
    String statement=" SELECT count(*) FROM SMC_EXP_HIGR_BEH WHERE HIGR_IH_GRUPPE='"+hwgName+"' AND HIGR_BEH_CODE='"+code+"'";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      rset.next();
      return rset.getInt(1)==1;
    }
    finally
    {
      if(conn!=null) conn.close();
    }
  }

  
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param code
   * @return
   * @throws Exception
   */
  public static boolean isValidBaugruppeForHwg(String edvin, String hwgName, String code) throws Exception
  {
    String statement=" SELECT count(*) FROM SMC_EXP_HIGR_BGR WHERE HIGR_IH_GRUPPE='"+hwgName+"' AND HIGR_BGR_CODE='"+code+"'";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      rset.next();
      return rset.getInt(1)==1;
    }
    finally
    {
      if(conn!=null) conn.close();
    }
  }
  
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param code
   * @return
   * @throws Exception
   */
  public static boolean isValidBauteilForHwg(String edvin, String hwgName, String code) throws Exception
  {
    String statement=" SELECT count(*) FROM SMC_EXP_HIGR_BTL WHERE HIGR_IH_GRUPPE='"+hwgName+"' AND HIGR_BTL_CODE='"+code+"'";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      rset.next();
      return rset.getInt(1)==1;
    }
    finally
    {
      if(conn!=null) conn.close();
    }
  }

  
  /**
   * 
   * @param edvin
   * @param hwgName
   * @param code
   * @return
   * @throws Exception
   */
  public static boolean isValidMassnahmencodeForHwg(String edvin, String hwgName, String code) throws Exception
  {
    String statement=" SELECT count(*) FROM SMC_EXP_HIGR_MAH WHERE HIGR_IH_GRUPPE='"+hwgName+"' AND HIGR_MAH_CODE='"+code+"'";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      rset.next();
      return rset.getInt(1)==1;
    }
    finally
    {
      if(conn!=null) conn.close();
    }
  }

  /**
   * 
   * @param edvin
   * @param hwgName
   * @param code
   * @return
   * @throws Exception
   */
  public static boolean isValidSchadenscodeForHwg(String edvin, String hwgName, String code) throws Exception
  {
    String statement=" SELECT count(*) FROM SMC_EXP_HIGR_SHA WHERE HIGR_IH_GRUPPE='"+hwgName+"' AND HIGR_SHA_CODE='"+code+"'";
    Connection conn = getConnection(edvin);
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(statement);
      rset.next();
      return rset.getInt(1)==1;
    }
    finally
    {
      if(conn!=null) conn.close();
    }
  }

  /**
   * 
   * @param edvin
   * @return
   */
  private static Connection getConnection(String edvin)throws Exception
  {
  	try
		{
      return DriverManager.getConnection(Config.getString(edvin));
		}
  	catch (Exception e)
		{
  		throw new BusinessException(edvin+" steht in diesem Kontext nicht zur Verfügung");
  	}
  }
}
