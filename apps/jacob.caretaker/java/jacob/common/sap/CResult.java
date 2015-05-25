/* 
 * CResult.java
 * Created: 30.05.2005
 *  
 * $Log: CResult.java,v $
 * Revision 1.1  2007/08/01 13:40:03  achim
 * SAP
 * 
 */
package jacob.common.sap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Klasse die das Ergebnis eines SAP-RFC-Aufrufs modelliert. die Klasse hat
 * einen statischen Logger.
 * 
 * @author Bernd.Clemenz
 * @version $Id: CResult.java,v 1.1 2007/08/01 13:40:03 achim Exp $
 * @since 
 */
public final class CResult
{
  private static final String LOG_PREFIX = "SAP-CON-RES: ";
  private static final Log LOGGER = LogFactory.getLog(ConnManager.class.getName());

  private static final String SAP_SUCCESS_CODE = "S";
  
  private final boolean m_bIsSccess;
  private final String  m_sLogMessage;
  private final String  m_sRFCName;
  private final Map     m_oSimpleResult;
  private final CTable  m_oTableResult;
  private final Map     m_oAllTables;
  //----------------------------------------------------------------------------
  /**
   * Konstruktor.
   * @param sRFCName der Name des RFC-Bausteins.
   * @param sResultCode der Ergebniscode aus dem SAP-Aufruf.
   * @param sLogMessage die Statusmeldung des SAP-Systems
   * @param oTable eine Tabelle
   */
  CResult
  (
    final String sRFCName,
    final String sResultCode,
    final String sLogMessage,
    final Map    oSimpleResult,
    final CTable oTable
  )
  {
    super();
    if(null == sResultCode)
      throw new IllegalArgumentException("Need a result code");
    
    if(LOGGER.isTraceEnabled())
      LOGGER.trace(LOG_PREFIX + "CResult created.");
    
    m_bIsSccess     = SAP_SUCCESS_CODE.equals(sResultCode);
    m_sLogMessage   = sLogMessage;
    m_sRFCName      = sRFCName;
    m_oSimpleResult = oSimpleResult;
    m_oTableResult  = oTable;
    
    m_oAllTables = new HashMap();
    
    addTable(oTable);
  }
  //----------------------------------------------------------------------------
  /**
   * @return true wenn der produzierende RFC-aufruf erfolgreich war.
   */
  boolean isSuccess() { return m_bIsSccess; }
  //----------------------------------------------------------------------------
  /**
   * @return die Logmeldung aus dem SAP-Aufruf
   */
  String getLogMessage() { return m_sLogMessage; }
  //----------------------------------------------------------------------------
  /**
   * @return der Name des gerufenen RFC-Bausteins.
   */
  String getRFCName() { return m_sRFCName; }
  //----------------------------------------------------------------------------
  /**
   * @return true wenn das Ergebnis in einer einfachen Map liegt.
   */
  boolean isSimpleResult() { return null != m_oSimpleResult; }
  //----------------------------------------------------------------------------
  /**
   * @return Map mit einem einfachen Ergebnis
   */
  Map getSimpleResult() { return m_oSimpleResult; }
  //----------------------------------------------------------------------------
  /**
   * @return eine Tabelle, kann null sein, wenn keine aus SAP geliefert oder
   * abgefragt wurde.
   * @see CResult#CResult(String, String, String, Map, CTable)
   */
  CTable getPrimaryTableResult() { return m_oTableResult; }
  //----------------------------------------------------------------------------
  /**
   * @return Map mit allen gelieferten Tabellen aus einen SAP-RFC Aufruf.
   */
  Map getAllTables()
  {
    return Collections.unmodifiableMap(m_oAllTables);
  }
  //----------------------------------------------------------------------------
  /**
   * @param oTable die Tabelle, die dem Ergebnis hinzugef&uuml;gt werden soll.
   */
  void addTable(final CTable oTable)
  {
    if(null == oTable)
    {
      if(LOGGER.isDebugEnabled())
        LOGGER.debug(LOG_PREFIX + "try to add null table");
      
      return;
    }
    
    m_oAllTables.put(oTable.getName(),oTable);
    
    if(LOGGER.isDebugEnabled())
      LOGGER.debug(LOG_PREFIX + "added table: " + oTable.getName());
  }
  //----------------------------------------------------------------------------
  /**
   * @param oTblList List mit weiteren Tabellen aus SAP-RFC
   */
  void allTablesFromList(final List oTblList)
  {
    if(null == oTblList) return;
    
    Iterator oIter = oTblList.iterator();
    
    while(oIter.hasNext())
    {
      addTable((CTable) oIter.next());
    } // while
  }
  //----------------------------------------------------------------------------
  /**
   * @return RFC=name;Success=wert;Log=msg;Tables=N
   */
  public String toString()
  {
    StringBuffer sbRet = new StringBuffer();
    
    sbRet.append("RFC=");      sbRet.append(m_sRFCName);
    sbRet.append(";Success="); sbRet.append(m_bIsSccess);
    sbRet.append(";Log=");     sbRet.append(m_sLogMessage);
    sbRet.append(";Tables=");  sbRet.append(m_oAllTables.size());
    sbRet.append(";");
    
    return sbRet.toString();
  }
}
