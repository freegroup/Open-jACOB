/*
 * Created on Jul 26, 2004
 *
 */
package jacob.common.gui;

import jacob.common.Yan;
import jacob.common.data.DataUtils;
import jacob.common.xml.Converter;
import jacob.exception.BusinessException;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormActionEmitter;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 *
 * @author Andreas Herz
 */
public abstract class GenericSendAs extends IButtonEventHandler 
{
  public abstract IDataTable getDocTemplates(IClientContext context) throws Exception;
  public abstract IDataTableRecord getRecordToSend(IClientContext context) throws Exception;
  
  /**
   * Callback Klasse für den Nachfragedialog.
   * Eingegebenen Werte in das Dokumenteintragen und einen Eintrag in den yan_task machen.
   *
   */
  static class AskCallback implements IFormDialogCallback
  {
    String document;
    String address;
    String xslStylesheet;
    IDataTableRecord callrecord;
    AskCallback(String document, String address, String xslStylesheet,IDataTableRecord callrecord)
    {
      this.document=document;
      this.address=address;
      this.xslStylesheet=xslStylesheet;
      this.callrecord=callrecord;
    }
    public void onSubmit(IClientContext context,String buttonId, Map formValues)throws Exception
    {
      Iterator iter = formValues.keySet().iterator();
      while(iter.hasNext())
      {
        String key  =(String)iter.next();
        String value=Converter.encode((String)formValues.get(key));
        if(key.startsWith("db_field(*ASK:"))
          document = StringUtils.replace(document,key,value);
      }
      Yan.createInstance(context,document,address, xslStylesheet);
      IMessageDialog dialog =context.createMessageDialog("Nachricht wurde erfolgreich versendet an ["+address+"]");
      dialog.show();
      // Historie aktualisieren
      IDataTransaction myTrans = context.getDataAccessor().newTransaction();
      try
			{
      	callrecord.appendToHistory(myTrans,"Nachricht wurde manuell versandt an: "+ address);
      	myTrans.commit();
      }
      finally
			{
      	myTrans.close();
      }
    }
  }
  
  /**
   * 
   *
   */
  public class ComboBoxCallback implements IFormActionEmitter
  {
    
    /** 
     * Falls in dem Dialog ein neuer Combobox Eintrag ausgewählt wird, wird dieser Callback
     * aufgerufen. Der Callback füllt in Abhängigkeit der ComboBox 'msgTyp' und 'recipienType'
     * den Defaultwert für die Empfängeraddresse vor.<br>
     *
     */
    public void onAction( IClientContext context, IFormDialog parent,  Map formValues) throws Exception
    {
      int type  = (Integer.parseInt((String)formValues.get("msgTypeIndex"))+1)*10;
      int group = Integer.parseInt((String)formValues.get("recipienTypeIndex"))+1;
      String input="<unset>";
      // Vorblenden der Empfängeraddresse in abhängigkeit von Type der Meldung und 
      // Typ des Empfängers
      //
      switch(type+group)
      {
      case 11: // eMail -> Gruppe
        input=context.getDataTable("callworkgroup").getSelectedRecord().getStringValue("email");
        break;
      case 21: // Fax   -> Gruppe
        input=context.getDataTable("callworkgroup").getSelectedRecord().getStringValue("fax");
        break;
      case 31: // SMS   -> Gruppe
        input=context.getDataTable("callworkgroup").getSelectedRecord().getStringValue("phone");
        break;
      case 12: // eMail -> Kunde
        input=context.getDataTable("customerint").getSelectedRecord().getStringValue("emailcorr");
        break;
      case 32: // SMS   -> Kunde
        input=context.getDataTable("customerint").getSelectedRecord().getStringValue("phonecorr");
        break;
      case 22: // Fax   -> Kunde
        input=context.getDataTable("customerint").getSelectedRecord().getStringValue("faxcorr");
        break;
      case 13: // eMail -> Centerleiter
        input=getCenterleiter(context).getStringValue("emailcorr");
        break;
      case 23: // Fax   -> Centerleiter
        input=getCenterleiter(context).getStringValue("faxcorr");
        break;
      case 33: // SMS   -> Centerleiter
        input=getCenterleiter(context).getStringValue("phonecorr");
        break;
      }
      formValues.put("recipienAddress",StringUtil.toSaveString(input));
     
    }
  }
  
  /**
   * Call per eMail, Fax oder per SMS versenden.<br>
   * Dem Benutzter wird ein Dialog angezeit in dem folgende Auswahl getroffen werden kann:<br>
   * <ul>
   *   <li>Ausgabekanal - SMS, eMail oder Fax.</li>
   *   <li>Typ des Empfängers - Gruppe, Kunde oder Centerleiter</li>
   *   <li>Typ der Dokumentenvorlage</li>
   * </lu> 
   * <br>
   * Nachdem der Benutzer 'Senden' drückt wird die ermittelte Nachrichtenkonfiguration in die 
   * Tabelle 'yan_task' eingetragen. Dort wird diese dann durch ein externes Programm 'YAN'
   * abgearbeitet.
   * <br>
   */
  public void onAction(IClientContext context, IGuiElement button)   throws Exception 
  {
    final IDataTableRecord call = getRecordToSend(context);
    final IDataTable docTemplateTable = getDocTemplates(context);
    
    String[] templates= new String[docTemplateTable.recordCount()];
    for (int i = 0; i < docTemplateTable.recordCount(); i++)
    {
      templates[i]=docTemplateTable.getRecord(i).getStringValue("name");
    }
   
    
    // Constraints für den Dialog festlegen
    //
    CellConstraints cc= new CellConstraints();
    FormLayout  layout = new FormLayout("10dlu,10dlu,p,3dlu,300dlu,10dlu",  // columns
                                        "10dlu,p,3dlu,p,10dlu,p,3dlu,p,3dlu,p,10dlu,p,3dlu,100dlu,50dlu");      // rows

    // Dialog samt Callback-Klasse anlegen
    //
    IFormDialog dialog=context.createFormDialog("Title",layout,new IFormDialogCallback()
		    {
		      public void onSubmit(IClientContext context, String buttonId, Map formValues)throws Exception
		      {
		        IDataTableRecord docTemplate     = docTemplateTable.getRecord(Integer.parseInt((String)formValues.get("templateIndex")));
		        String           recipienAddress = (String)formValues.get("recipienAddress");
		        if(StringUtil.toSaveString(recipienAddress).length()==0)
		        {
		          alert("Keine Addressangabe - Meldung wurde nicht versendet.");
		          return;
		        }
		        String           stylesheet      = "<unset>";
		        switch(Integer.parseInt((String)formValues.get("msgTypeIndex")))
		        {
		        	case 0:
		        	  recipienAddress = "email://"+recipienAddress;
		        	  stylesheet      = docTemplate.getStringValue("email_xsl");
		        	  break;
		        	case 1:
		        	  recipienAddress = "rightfax://"+recipienAddress;
		        	  stylesheet      = docTemplate.getStringValue("fax_xsl");
		        	  break;
		        	case 2:
		        	  recipienAddress = "sms://"+recipienAddress;
		        	  stylesheet      = docTemplate.getStringValue("sms_xsl");
		        	  break;
		        }
		        String template=docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
		        
		        // Datenbankfelder in dem XML ersetzen
		        //
		        String auftrag=Yan.fillDBFields(context, call, docTemplate,template,true, null);
		        
		        // Felder welche mit ask() gekennzeichnet sind bei dem Anwender nachfragen
		        //
		        Yan.fillAskFields(context,auftrag,new AskCallback(auftrag,recipienAddress, stylesheet,call));
		        //Yan.createInstance(context,call,docTemplate,recipienAddress,stylesheet);
		      }
		    });
    
    // Elemente in den Dialog einfügen
    //
    dialog.addHeader("Nachricht",cc.xywh(1,1,4,1));
    dialog.addLabel("Typ:",cc.xy(2,3));   dialog.addComboBox(new ComboBoxCallback(),"msgType",new String[]{"eMail","Fax","SMS"},0,cc.xy(4,3));
    
    dialog.addHeader("Empfänger",cc.xywh(1,5,4,1));
    dialog.addLabel("Typ:",cc.xy(2,7));     dialog.addComboBox(new ComboBoxCallback(),"recipienType",new String[]{"Gruppe","Kunde","Centerleiter"},0,cc.xy(4,7));
    
    // eMail an Kunde wird vorgeblendet
  //  String defaultAddress=context.getDataTable("customerint").getSelectedRecord().getStringValue("emailcorr");
    // eMail an AK wird vorgeblendet
    String defaultAddress=context.getDataTable("callworkgroup").getSelectedRecord().getStringValue("email");
    dialog.addLabel("Adresse:",cc.xy(2,9)); dialog.addTextField("recipienAddress",defaultAddress,cc.xy(4,9));

    dialog.addHeader("Vorlage",cc.xywh(1,11,4,1));
    dialog.addListBox("template",templates,0,cc.xywh(2,13,3,1));
    
    dialog.setCancelButton("Abbruch");
    dialog.addSubmitButton("ok","Senden");
    
    
    // Dialog anzeigen
    //
//    dialog.setDebug(true);
    dialog.show(400,350);
  }
  
  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status,  IGuiElement emitter) throws Exception 
  {
  }
  
  /**
   * Bestimmen des Centerleiters
   * 
   * @param context
   * @return
   * @throws Exception
   */
  private IDataTableRecord getCenterleiter(IClientContext context) throws Exception
  {
    IDataAccessor accessor =context.getDataAccessor().newAccessor();
    String centerleiterPkey = DataUtils.getAppprofileValue(context,"employee_key");
    IDataTable employee=accessor.getTable("employee");
    employee.qbeSetValue("pkey",centerleiterPkey);
    if(employee.search()!=1)
      throw new BusinessException("Centerleiter konnte nicht bestimmt werden");
    return employee.getRecord(0);
  }
}
