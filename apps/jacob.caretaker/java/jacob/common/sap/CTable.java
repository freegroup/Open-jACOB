/*
 * CTable.java
 * Created: 30.05.2007
 * 
 * $Log: CTable.java,v $
 * Revision 1.5  2008/06/27 14:58:21  achim
 * €nderung, weil aus SAP nicht merh ausschlie§lich Strings geliefert werden
 *
 * Revision 1.4  2008/03/03 16:45:56  achim
 * €nderungen wg. Writeback, es wird jetzt direkt mit SQL geschrieben, sowie €nderungen bzgl System.out ...
 *
 * Revision 1.3  2007/08/29 07:23:48  achim
 * Objectimport
 *
 * Revision 1.2  2007/08/20 17:47:11  achim
 * zwischenstand
 *
 * Revision 1.1  2007/08/01 13:40:03  achim
 * SAP
 * 
 */
package jacob.common.sap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.ParameterList;

/**
 * Klasse die eine einfache Tabelle mit nur wenigen Zeilen modelliert. Die
 * Klasse hat einen statischen Logger.
 * 
 * @author Bernd.Clemenz
 * @version $Id: CTable.java,v 1.5 2008/06/27 14:58:21 achim Exp $
 * @since
 */
final class CTable
{
  private static final String LOG_PREFIX = "SAP-CON-RES: ";
  private static final Log LOGGER = LogFactory.getLog(ConnManager.class.getName());
  
  private final String     m_sTableName;
  private final int        m_iNumRows;
  private final int        m_iNumCols;
  private final String[]   m_asFieldNames;
  private final String[][] m_asValues;
  private final Map        m_oFieldNames;
   
  //----------------------------------------------------------------------------
  /**
   * Konstruktor.
   * @param sName der Name der Tabelle, wie er aus SAP geliefert wird.
   * @param oTablParamLst die Liste der Tabellenparameter, wie sie vom JCo
   * Function-Object geliefert wird.
   * @exception IllegalArgumentException wenn kein Tabellenname angegeben ist
   * @exception IllegalArgumentException wenn keine Parameterliste angegeben
   * ist.
   * @exception IllegalStateException wenn keine Tabelle mit dem angegebenen
   * Namen in der Parameterlist gefunden wird.
   */
  CTable   
  (
    final String        sName,
    final ParameterList oTablParamLst
  )
  {
    super();
    if(null == sName || "".equals(sName))
      throw new IllegalArgumentException("Need a name of the table");
    
    if(null == oTablParamLst)
      throw new IllegalArgumentException("Need a list of table parameters");
    
    m_sTableName = sName;
    
    if(LOGGER.isTraceEnabled())
      LOGGER.trace(LOG_PREFIX + "Created table: " + m_sTableName);
    
    final JCO.Table oTbl = oTablParamLst.getTable(m_sTableName);
    if(null == oTbl)
      throw new IllegalStateException("No table with name: " + m_sTableName);

    m_iNumRows = oTbl.getNumRows();
    m_iNumCols = oTbl.getNumColumns();

    CSAPHelperClass.printDebug(m_iNumRows + " Rows in Table to Import");
    /*
     * Indizierung der Namen merken
     */
    m_asFieldNames = new String[m_iNumCols];
    m_oFieldNames  = new HashMap();
    
    for(int i = 0; i < m_iNumCols; ++i)
    {
      final String sTblName = oTbl.getName(i);
//	  System.out.println("***IN CRES EXEC ctable sTblName: " + sTblName);
      m_asFieldNames[i] = sTblName;
      m_oFieldNames.put(sTblName,new Integer(i));
    } // for
    
    /*
     * SAP-Tabelle ablaufen und Daten intern speichern.
     */
    String[][] asTblValues = new String[m_iNumRows][]; // <--- m_iNumRows statt m_iNumCols
    for(int i = 0; i < m_iNumRows; ++i)
    {
      oTbl.setRow(i);
      String[] asValues = new String[m_iNumCols];
      asTblValues[i] = asValues;
      for(int j = 0; j < m_iNumCols; ++j)
      {
        Object obj = oTbl.getValue(j);
        asValues[j] = obj==null?"":obj.toString();
        //asValues[j] = (String) oTbl.getValue(j);
   //          System.out.println("***IN CRES EXEC ctable asValues[j]: " + asValues[j]);
         
      } // for j
    } // for i
    
    m_asValues = asTblValues;
  }
  //----------------------------------------------------------------------------
  /**
   * @return der Name der Tabelle, wie in den Parametern gesetzt ist.
   */
  String getName() { return m_sTableName; }
  //----------------------------------------------------------------------------
  /**
   * @return Anzahl der Zeilen in der Tabelle
   */
  int getNumRows() { return m_iNumRows; }
  //----------------------------------------------------------------------------
  /**
   * @return Anzahl der Spalten
   */
  int getNumCols() { return m_iNumCols; }
  //----------------------------------------------------------------------------
  /**
   * @param iCol Index der Spalte, deren Name bestimmt werden soll.
   * @return Name der Spalte
   */
  String getFieldName(final int iCol) { return m_asFieldNames[iCol]; }
  //----------------------------------------------------------------------------
  /**
   * @param iRow die Zeile in der Tabelle
   * @param iCol die Spalte in der Tabelle
   * @return der Wert aus der Tabelle
   */
  String getValue(final int iRow, final int iCol)
  {
    return m_asValues[iRow][iCol];
  }
  //----------------------------------------------------------------------------
  /**
   * @param sFieldName der Name der Spalte die ausgelesen werden soll
   * @param iRow die Nummer der Zeile
   * @return Wert aus der Tabelle.
   * @exception IllegalArgumentException wenn kein Feldname angegeben
   * @exception IllegalStateException wenn Feldname nicht gefunden wird
   */
  String getValue(final String sFieldName, final int iRow)
  {
    if(null == sFieldName || "".equals(sFieldName))
      throw new IllegalArgumentException("Need a field name");
    
    final Integer oiCol = (Integer) m_oFieldNames.get(sFieldName);
    if(null == oiCol)
      throw new IllegalStateException("Unknown field name: " + sFieldName);
    
    final int iCol = oiCol.intValue();
    
    return getValue(iRow,iCol);
  }
  //----------------------------------------------------------------------------
  /**
   * @param sFieldName der Name des Feldes dessen Index ermittelt werden soll
   * @return der Index des Feldes wenn gefunden, -1 wenn nicht gefunden.
   */
  int getIndexOfField(final String sFieldName)
  {
    if(null == sFieldName || "".equals(sFieldName))
      throw new IllegalArgumentException("Need field name");
    
    final Integer oInt = (Integer) m_oFieldNames.get(sFieldName);
    return (null == oInt) ? -1 : oInt.intValue();
  }
  //----------------------------------------------------------------------------
  /**
   * oTranslateFields Zuordnung.
   * <pre>
   *   +----------+------------+
   *   I SAP-Name I jACOB-Name I
   *   +----------+------------+
   * </pre>
   * @param oTranslateFields Map mit SAP &lt;-&gt; jACOB Zuordnung von Feldern
   * die direkt &uuml;bernommen werden.
   * @param iRow die Zeile in der Tabelle
   * @return Map jACOB-Feld/SAP-Wert Zuordnng
   */
  Map transferFields
      (
        final Map oTranslateFields,
        final int iRow
      )
  {
    if(null == oTranslateFields || oTranslateFields.isEmpty())
      return Collections.EMPTY_MAP;
    
    final Map oValues = new HashMap();
    
    Iterator oIter = oTranslateFields.entrySet().iterator();
    
    while(oIter.hasNext())
    {
      final Map.Entry oData = (Map.Entry) oIter.next();
      String sValueFormSap  = getValue((String) oData.getKey(),iRow);
      oValues.put(oData.getValue(),sValueFormSap);
    } // while
    
    return oValues;
  }
  //----------------------------------------------------------------------------
  /**
   * @return Name=Tabelle; Zeile NR: WERT(Zeile,j), ;
   * Die einzelnen Werte sind kommagetrennt.
   */
  public String toString()
  {
    final StringBuffer sbRet = new StringBuffer(1024);
    
    sbRet.append("Name: "); sbRet.append(m_sTableName);
    sbRet.append("; Daten: ");
    
    for(int i = 0; i < m_iNumRows; ++i)
    {
      sbRet.append("; Zeile: ");
      sbRet.append(i);
      sbRet.append(": ");
      
      for(int j = 0; j < m_iNumCols; ++j)
      {
        sbRet.append(getValue(i,j));
        sbRet.append(',');
      } // for j
    } // for i
    
    sbRet.append(';');
    
    return sbRet.toString();
  }
}
