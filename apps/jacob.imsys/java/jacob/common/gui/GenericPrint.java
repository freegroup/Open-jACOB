/*
 * Created on 30.07.2004
 * by mike
 *public 
 */
package jacob.common.gui;
import jacob.common.Yan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Dieser ActionHandler wird gerufen wenn der Benutzer in dem ContextMenu "Meldung/Auftrag drucken" auswählt.<br>
 * Dem Anwender wird dann ein Dialog mit den im caretaker parametresierten Dokumentenvorlagen
 * angeboten.<br>
 * Der Anwender wählt eine aus. Diese Dokumentenvorlage wird dann einem EntryPoint übergeben
 * welcher daraus ein RTF Dokument generiert und anzeigt.
 * 
 */
public abstract class GenericPrint extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: GenericPrint.java,v 1.2 2005/11/17 17:35:16 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public abstract String getConstraint(IClientContext context) throws Exception;

  /**
   * Callback Klasse für den Nachfragedialog.
   * Eingegebenen Werte in das Dokumenteintragen und einen Eintrag in den yan_task machen.
   *
   */
  static class AskCallback implements IFormDialogCallback
  {
    String document;
    AskCallback(String document)
    {
      this.document=document;
    }
    public void onSubmit(IClientContext context,String buttonId, Map formValues)throws Exception
    {
      Iterator iter = formValues.keySet().iterator();
      while(iter.hasNext())
      {
        String key  =(String)iter.next();
        String value=(String)formValues.get(key);
        if(key.startsWith("db_field(*ASK:"))
          document = StringUtils.replace(document,key,value);
      }
      context.createDocumentDialog("application/msword","test.rtf",document.getBytes("ISO-8859-1")).show();
    }
  }

  // Diese Klasse ist dafür zuständig anhand der Benutzerselektion
  // den Datensatz mittels eines EntryPoints als RTF darzustellen oder die 
  // Aktion abzubrechen.
  //
  class PrintCallback implements IFormDialogCallback
  {
    final Map name2templates;
    final IDataTableRecord call;
    PrintCallback(IDataTableRecord call, Map names2template)
    {
      this.name2templates=names2template;
      this.call=call;
    }

    /* 
     * Selektierte Vorlage an einen EntryPoint übergeben welcher dann das RTF Dokument generiert.
     * 
     */
    public void onSubmit(IClientContext context, String buttonId, Map values)  throws Exception
    {
      String userSelection =(String)values.get("template");
      
      IDataTableRecord docrecord= (IDataTableRecord)name2templates.get(userSelection);
      if(docrecord==null)
      {
        context.createMessageDialog("Keine Dokumentenvorlage gefunden. Ausdrucken ist somit nicht möglich").show();
        return;
      }
      // propagiere vom Record das Default Releationset für das RTF-Template
      context.getDataAccessor().propagateRecord(call,Filldirection.BACKWARD);

      String  rtfDocument   = Yan.fillDBFields(context,call,docrecord,docrecord.getStringValue("rtftext"),false, "\\par ");
      Yan.fillAskFields(context,rtfDocument,new AskCallback(rtfDocument));
    }
  }
  
  /* 
   * Der Benutzer wählt im ContextMenu der Meldung den dem ContextMenu "Meldung/Auftrag drucken"
   * aus. 
   * <ul>
   * <li>Es wird der selektierte Datensatz geholt.
   * <li>Holen aller Dokumentenvorlagen aus dem caretaker
   * <li>Dialog dem Anwender darstellen in dem dieser die gewünschte Vorlage auswählt
   * </ul>
   * 
   */
  public void onAction(IClientContext context, IGuiElement button)   throws Exception
  {
    IDataTableRecord call= context.getSelectedRecord();

    // search all document templates in the database. It is possible to render the 
    // call with different type of xsl-stylesheets.
    //
    IDataTable table=context.getDataTable("doc_template");
    table.clear();
    table.qbeClear();
    table.qbeSetValue("use_in",getConstraint( context));
    // nur RTF Dokumente
    
    table.qbeSetValue("rtftext","!NULL");
    table.search();
    
    if(table.recordCount()>0)
    {  
      // retrieve all names of the templates and store them in a string array for
      // the select dialog.
      //
      Map      names2template=new HashMap();
		  String[] names= new String[table.recordCount()];
		  for(int i=0;i<table.recordCount();i++)
		  {
		    IDataTableRecord docRecord= table.getRecord(i);
		    names[i]=docRecord.getStringValue("name");
		    // store the correponding template in a map 
		    names2template.put(docRecord.getStringValue("name"),docRecord);
		  }
		  
		  // Create a FormDialog with a list of all available doc templates.
		  //
		  FormLayout layout=new FormLayout("10dlu, 300dlu,10dlu",   // columns
		                                   "10dlu,p,10dlu,250dlu,10dlu"); // rows
		  CellConstraints cc=new CellConstraints();
		  IFormDialog dialog=context.createFormDialog("Dokumentenvorlage",layout,new PrintCallback(call,names2template));
		  dialog.addLabel("Dokumentenvorlage auswählen",cc.xy(1,1));
		  dialog.addListBox("template",names,0,cc.xy(1,3));
		  dialog.addSubmitButton("show","Anzeigen");
		  // Show the dialog with a prefered size. The dialog trys to resize to the optimum size!
		  dialog.show(400,500);
    }
    else
    {
      // no document template in the system found. make a user notification.
      //
      IMessageDialog dialog=context.createMessageDialog("Es ist keine Dokumentenvorlage hinterlegt. Drucken ist nicht möglich!");
      dialog.show();
    }
  }
}
