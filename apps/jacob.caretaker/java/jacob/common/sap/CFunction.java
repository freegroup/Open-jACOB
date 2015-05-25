/*
 * CFunction.java
 * 
 * Created: 30.05.2007
 * 
 * $Log: CFunction.java,v $
 * Revision 1.2  2007/08/08 11:38:31  sonntag
 * Überarbeitung Fehlerbegandlung, Exceptionhandling, etc.
 *
 * Revision 1.1  2007/08/01 13:40:03  achim
 * SAP
 * 
 */
package jacob.common.sap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.ParameterList;

import de.tif.jacob.core.exception.UserException;

/**
 * Hilfsklasse zur Ausf&uml;hrung von RFC-Call.
 * 
 * @version $Id: CFunction.java,v 1.2 2007/08/08 11:38:31 sonntag Exp $
 * @author Bernd.Clemenz
 * @since 
 */
final class CFunction
{
  private static final String LOG_PREFIX = "SAP-CON-FCT: ";
  private static final Log LOGGER = LogFactory.getLog(ConnManager.class.getName());
  
  private final String m_sPoolName; // NOPMD by Bernd.Clemenz on 31.05.07 10:55
  //----------------------------------------------------------------------------
	/**
	 * Konstruktor.
   * @param sPoolName der Name des zu verwendenden SAP-ConnectionPools
   * @exception IllegalArgumentException wenn kein Name eines Pools angegeben
   * ist.
	 */
	CFunction(final String sPoolName)
	{
		super();
    if(null == sPoolName || "".equals(sPoolName))
      throw new IllegalArgumentException("Need a pool name");
    
    m_sPoolName = sPoolName;
    if(LOGGER.isTraceEnabled())
      LOGGER.trace(LOG_PREFIX + "created for: " + m_sPoolName);
	}
  //----------------------------------------------------------------------------
  private void readStructure2Map
               (
                 final Map           oOutParam,
                 final String        sStructureName,
                 final ParameterList oOutParamLst 
               )
  {
    if(LOGGER.isTraceEnabled())
      LOGGER.trace(LOG_PREFIX + "reading structure: " + sStructureName);
    
    final JCO.Structure oStruct = oOutParamLst.getStructure(sStructureName); // NOPMD by Bernd.Clemenz on 31.05.07 10:56
    if(null == oStruct)
      throw new IllegalStateException
                (
                  "Required structure not in SAP response: " + sStructureName
                );
    
    for(int i = oStruct.getNumFields(); --i >= 0;)
    {
      oOutParam.put(oStruct.getName(i), oStruct.getValue(i));
    } // for
  }
	//----------------------------------------------------------------------------
  /**
   * @param oInParam die Eingabeparameter. Sie werden als NAME=WERT Paare
   * erwartet.
   * @param aoOutParam Beschreiberfeld f&uuml;r die zu erwartenden Ausgabe-
   * parameter.
   * @throws UserException bildet SAP-Fehler auf diese Exception ab.
   * @exception IllegalArgumentException wenn kein RFCName angegeben ist.
   */
   CResult exec
           (
             final String       sRFCName,
             final Map          oInParam,
             final CSapReturn[] aoOutParam
           )
  throws UserException
  { 
    if(null == sRFCName || "".equals(sRFCName))
      throw new IllegalArgumentException("Need a RFC Name");
    
    final JCO.Pool oPool = JCO.getClientPoolManager().getPool(m_sPoolName);
    if (null == oPool)
      throw new UserException("Pool '" + m_sPoolName + "' is lost.");
    
    if(LOGGER.isTraceEnabled())
      LOGGER.trace(LOG_PREFIX + "exec for: " + sRFCName);
      
    final IRepository oRepos = JCO.createRepository
                               (
                                 "_repos",
                                 m_sPoolName
                               );
    final IFunctionTemplate oFctTempl   = oRepos.getFunctionTemplate(sRFCName);
    final JCO.Function      oFct        = oFctTempl.getFunction();
    final JCO.ParameterList oInParamLst = oFct.getImportParameterList();
    
    /*
     * Tatsächliche Übergabeparameter und SAP-Importliste synchronisieren.
     */
    final boolean bReal = null != oInParam && !oInParam.isEmpty();
    if(bReal && null == oInParamLst)
      throw new IllegalStateException("real parameters but no SAP import params");
    
    if(bReal)
    {
      final Iterator oIter = oInParam.entrySet().iterator();
      
      while(oIter.hasNext())
      {
        final Map.Entry oData = (Map.Entry) oIter.next();
        
        oInParamLst.setValue((String) oData.getValue(),(String) oData.getKey());
      } // while
    }
   
    final JCO.Client oClient = JCO.getClient(m_sPoolName);
    try
    {
      oClient.execute(oFct);
      
      /*
       * Auslesen der Ergebnisse des SAP-RFC Aufrufs
       */
      final ParameterList oOutParamLst = oFct.getExportParameterList();
      final JCO.Structure oQuality = oOutParamLst.getStructure("ES_RETURN");
      
      Map    oSimpleOut   = null;
      CTable oTableResult = null;
      final List oAllTables = new LinkedList();
      
      if(null != aoOutParam)
      {
        oSimpleOut = new HashMap();
      
        for(int i = aoOutParam.length; --i >= 0; )
        {
          final CSapReturn oRetType = aoOutParam[i];
          final int        iType = oRetType.getType();
          final String     sName = oRetType.getName();
          
          switch(iType)
          {
            /*
             * Ein einfaches Feld soll ausgelesen werden.
             */
            case CSapReturn.TYPE_FIELD:
              oSimpleOut.put(sName,oOutParamLst.getField(sName).getValue());
              if(LOGGER.isDebugEnabled())
                LOGGER.debug(LOG_PREFIX + "Got field: " + sName);
              break;
            
            /*
             * Eine SAP-Struktur als einzeilige Tabelle wird ausgelsen
             */
            case CSapReturn.TYPE_STRUCTRE:
              readStructure2Map(oSimpleOut,sName,oOutParamLst);
              break;
              
            /*
             * Eine Tabelle wird erzeugt und aus SAP ausgelesen.
             */  
            case CSapReturn.TYPE_TABLE:
              if(null == oTableResult)
                oTableResult = new CTable(sName,oFct.getTableParameterList());
              else
                oAllTables.add(new CTable(sName,oFct.getTableParameterList()));
              break;
              
            default:
              throw new IllegalStateException("Invalid type requested: " + iType);
          }
        } // for
      }
      
      final CResult oReturn = new CResult
                                  (
                                    sRFCName,
                                    oQuality.getString("TYPE"),
                                    oQuality.getString("MESSAGE"),
                                    oSimpleOut,
                                    oTableResult
                                  );
      
      oReturn.allTablesFromList(oAllTables);
      
      return oReturn;
    }
    finally
    {
      if(null != oClient) JCO.releaseClient(oClient);
    }
  }
  //----------------------------------------------------------------------------
   /**
    * @return Pool=name;
    */
  public String toString()
  {
    final StringBuffer sbRet = new StringBuffer();
    
    sbRet.append("Pool="); sbRet.append(m_sPoolName);
    
    sbRet.append(';');
    
    return sbRet.toString();
  }
}
