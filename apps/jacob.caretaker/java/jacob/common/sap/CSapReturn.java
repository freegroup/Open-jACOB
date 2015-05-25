package jacob.common.sap;

/**
 * Klasse die ein Teilergebnis beschreibt. Mit dieser Klasse werden dje von
 * einem RFC-Aufruf erwarteten Antworttypen beschrieben.
 */
final class CSapReturn
{
  /** Ergebnistpy: Feld */
  public static final int TYPE_FIELD = 0;
  /** Ergebnistyp: Struktur */
  public static final int TYPE_STRUCTRE = 1;
  /** Ergebnistyp: Tabelle */
  public static final int TYPE_TABLE = 2;
  
  private final int    m_iType;
  private final String m_sName;
  //----------------------------------------------------------------------------
  /**
   * Konstrukor. 
   * @param iType Indikator f&uuml;r den Typ
   * @param sName der Name des Parameters in der SAP-R&uuml;ckgabe.
   * @exception IllegalArgumentException wenn kein Name anegegeben.
   */
  CSapReturn(final int iType,final String sName)
  {
    super();
    if(null == sName || "".equals(sName))
      throw new IllegalArgumentException("Need a name for return value");
    
    m_iType = iType;
    m_sName = sName;
  }
  //----------------------------------------------------------------------------
  /** @return der Name in der SAP-R&uuml;ckgabe */
  String getName() { return m_sName; }
  //----------------------------------------------------------------------------
  /** @return der Typenindikator */
  int getType() { return m_iType; }
  //----------------------------------------------------------------------------
  /**
   * @return Ergebnistyp als NAME=WERT; ...
   */
  public String toString()
  {
    final StringBuffer sbRet = new StringBuffer();
    
    sbRet.append("Type="); sbRet.append(m_iType);
    sbRet.append(";Name="); sbRet.append(m_sName);
    sbRet.append(';');
    
    return sbRet.toString();
  }
}