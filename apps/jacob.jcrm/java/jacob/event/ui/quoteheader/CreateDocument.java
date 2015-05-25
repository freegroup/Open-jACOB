/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 13 18:03:09 CEST 2005
 */
package jacob.event.ui.quoteheader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the CreateDocument record selected button. <br>
 * The {@link onAction(IClientContext, IGuiElement)}will be called, if the user
 * clicks on this button. <br>
 * Insert your custom code within this method. <br>
 * 
 * @author andreas
 */
public class CreateDocument extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CreateDocument.java,v 1.2 2005/10/19 11:28:25 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private class GridCallback implements IGridTableDialogCallback
  {
    private final IDataTableRecord quoteRecord;
    private final IDataTable doctemplateTable;

    private GridCallback(IDataTableRecord quoteRecord, IDataTable doctemplateTable)
    {
      this.quoteRecord = quoteRecord;
      this.doctemplateTable = doctemplateTable;
    }

    /*
     * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext,
     *      int, java.util.Properties)
     */
    public void onSelect(IClientContext context, int rowIndex, Properties columns) throws Exception
    {
      IDataTableRecord templateRecord = this.doctemplateTable.getRecord(rowIndex);
      DataDocumentValue docTemplate = templateRecord.getDocumentValue("template");
      DataDocumentValue document = LetterFactory.transform(context, quoteRecord, docTemplate);

      Date today = new Date();
      IDataTransaction trans = context.getDataAccessor().newTransaction();
      try
      {
        // create a new record in the table 'letter'
        //
        IDataTableRecord letter = context.getDataTable("quoteDocument").newRecord(trans);

        // set the data in the field 'letter' (ok - bad name.
        // table=letter and field=letter )
        //
        letter.setDocumentValue(trans, "docfile", document);

        // and any description to the letter. It is also possible the
        // prompt an input dialog
        // to the current agent and request a more meaningful
        // description.( see context.createAskdialog(...) )
        //
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
        String lettername = templateRecord.getStringValue("name") + " " + format.format(today).toString();
        letter.setStringValueWithTruncation(trans, "docname", lettername);
        
        letter.setStringValueWithTruncation(trans, "doctype", templateRecord.getStringValue("doctype"));

        // append the new record to the contract. Be in mind, that the
        // 'letter' table must have a relation to the 'customer' table!
        //
        letter.setLinkedRecord(trans, quoteRecord);

        // commit the transaction
        //
        trans.commit();

        // show created document to the user
        context.createDocumentDialog(document).show();
      }
      finally
      {
        trans.close();
      }
      
      // refresh main window 
      this.quoteRecord.getAccessor().propagateRecord(this.quoteRecord, "r_order", Filldirection.BOTH);
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // search all document templates in the database. It is possible to render
    // the call with different types of xsl-stylesheets.
    //

    // Note: Create new accessor because this ensures, that the resulting records are not cleared by
    //       the main window, if the user keeps the dialog open for a while.
    IDataTable doctemplateTable = context.getDataAccessor().newAccessor().getTable("doctemplate");
    doctemplateTable.qbeSetKeyValue("usedin", "Quote");
    doctemplateTable.search();

    if (doctemplateTable.recordCount() > 0)
    {
      IGridTableDialog dialog = context.createGridTableDialog(button, new GridCallback(context.getSelectedRecord(), doctemplateTable));

      // create the header for the selection grid dialog
      //
      String[] header = new String[]
      { "ID", "Name", "Type" };
      dialog.setHeader(header);

      String[][] daten = new String[doctemplateTable.recordCount()][3];
      for (int j = 0; j < doctemplateTable.recordCount(); j++)
      {
        IDataTableRecord doctemplate = doctemplateTable.getRecord(j);
        daten[j] = new String[]
        { doctemplate.getSaveStringValue("pkey"), doctemplate.getSaveStringValue("name"), doctemplate.getSaveStringValue("doctype") };
      }
      dialog.setData(daten);
      dialog.show(300, 100);
    }
    else
    {
      // no document template in the system found. make a user notification.
      //
      IMessageDialog dialog = context.createMessageDialog(new ApplicationMessage("NO_DOCUMENT_TEMPLATES"));
      dialog.show();
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
