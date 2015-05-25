/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 24 17:06:50 CEST 2008
 */
package jacob.event.ui.{modulename};

import jacob.model.{Modulename};
import jacob.model.{Modulename}_image;
import jacob.model.{Modulename}_link;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IHtmlGroup;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IHtmlGroupEventHandler;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.util.StringUtil;



/**
 *
 * @author andherz
 */
 public class {Modulename}Group extends IHtmlGroupEventHandler 
 {
  static public final transient String RCS_ID = "$Id: {Modulename}Group.java,v 1.3 2008/12/18 11:32:18 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private static String ROOT_PAGE_TAG ="home";

  private static String COMMAND_SHOW     = "Command:show";
  private static String COMMAND_VERSION  = "Command:version";
  private static String COMMAND_ORPHANED = "Command:orphaned";
  private static String COMMAND_UPDATE   = "Command:update";
  private static String COMMAND_SAVE     = "Command:save";
  private static String COMMAND_CANCEL   = "Command:cancel";

  ISelectionAction action = new DiffSelectionAction();
  
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * 
   * @param context The current client context
   * @param status  The new status of the group.
   * @param emitter The corresponding GUI element of this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IHtmlGroup group) throws Exception
  {
    String diff= (String)context.getProperty("diff");

    group.getBrowser().addSelectionAction(action);
    
    if(diff!=null)
    {
      String text ="[["+ROOT_PAGE_TAG+"|Home]] | [["+COMMAND_SHOW+"|Artikel]] | [["+COMMAND_UPDATE+"|Bearbeiten]] | [["+COMMAND_VERSION+"|Versionen]] | [["+COMMAND_ORPHANED+"|Verwaiste Seiten]]<br><hr>\n";
      group.setWiki(text);
      group.appendHtml(diff);
    }
    else if(status == IGuiElement.SELECTED)
    {
      String text ="[["+ROOT_PAGE_TAG+"|Home]] | [["+COMMAND_SHOW+"|Artikel]] | [["+COMMAND_UPDATE+"|Bearbeiten]] | [["+COMMAND_VERSION+"|Versionen]] | [["+COMMAND_ORPHANED+"|Verwaiste Seiten]]<br><hr>\n";
      text = text +context.getSelectedRecord().getSaveStringValue({Modulename}.wikitext);
      group.setWiki(text);
    }
    else if(context.getSelectedRecord() !=null)
    {
      String wikitext = context.getSelectedRecord().getSaveStringValue({Modulename}.wikitext);

      TextArea area = new TextArea();
      area.setName(GuiElement.getEtrHashCode(1));
      area.setStyle("width:100%;height:100%");
      area.setTagText(wikitext);
      area.setClass("longtext_normal editable_inputfield");
      Table table = new Table();
      table.setStyle("width:100%;height:100%");
      table.addElement(new TR()
                       .addElement(new TD()
                            .setTagText(group.wiki2html("[["+ROOT_PAGE_TAG+"|Home]] | [["+COMMAND_SAVE+"|Speichern]] | [["+COMMAND_CANCEL+"|Abbruch]] | [["+COMMAND_UPDATE+"|Bearbeiten]] | [["+COMMAND_VERSION+"|Versionen]] | [["+COMMAND_ORPHANED+"|Verwaiste Seiten]]\n")))
                      .setStyle("height:1px"))
           .addElement(new TR()
                         .addElement(new TD()
                             .addElement(area)
                                     )
                       );
      group.setHtml(table.toString());
    }
  }

  /**
   * 
   */
  public void onClick(IClientContext context,final IHtmlGroup group, String link) throws Exception
  {
    if(link.startsWith("Image:"))
    {
      final String[] parts = link.split(":");
      context.createUploadDialog(new IUploadDialogCallback()
      {
        public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
        {
          IDataTransaction trans = context.getDataAccessor().newTransaction();
          try
          {
            IDataTableRecord imageRecord=null;
            IDataTable imageTable = context.getDataTable({Modulename}_image.NAME);
            imageTable.qbeSetKeyValue({Modulename}_image.name, parts[1]);
            if(imageTable.search()==0)
            {
              imageRecord = imageTable.newRecord(trans);
              imageRecord.setValue(trans, {Modulename}_image.name, parts[1]);
            }
            else
            {
              imageRecord = imageTable.getSelectedRecord();
            }
            imageRecord.setValue(trans, {Modulename}_image.image, DataDocumentValue.create(fileName,fileData));
            trans.commit();
          }
          finally
          {
            trans.close();
          }
        }
      
        public void onCancel(IClientContext context) throws Exception
        {
        }
      }).show();
    }
    else if(COMMAND_UPDATE.equals(link))
    {
      context.getApplication().setSearchBrowserVisible(false);
      IDataTable table = context.getDataTable();
      String link_name = table.getSelectedRecord().getSaveStringValue({Modulename}.link_name);
      String wikitext  = table.getSelectedRecord().getSaveStringValue({Modulename}.wikitext);
      IDataTransaction trans = table.startNewTransaction();
      IDataTableRecord newRecord = table.newRecord(trans);
      newRecord.setValue(trans, {Modulename}.link_name, link_name);
      newRecord.setValue(trans, {Modulename}.wikitext, wikitext);
    }
    else if(COMMAND_SHOW.equals(link))
    {
      context.getApplication().setSearchBrowserVisible(false);
      IDataTable table = context.getDataTable();
      String link_name = table.getSelectedRecord().getSaveStringValue({Modulename}.link_name);
      loadOrCreateWikiPageByTag(context, link_name);
    }
    else if(COMMAND_CANCEL.equals(link))
    {
      context.getApplication().setSearchBrowserVisible(false);
      IDataTable table = context.getDataTable();
      String link_name = table.getSelectedRecord().getSaveStringValue({Modulename}.link_name);
      table.getSelectedRecord().getCurrentTransaction().close();
      loadOrCreateWikiPageByTag(context, link_name);
    }
    else if(COMMAND_SAVE.equals(link))
    {
      String wikitext =  group.getValue(1);

      TextArea area = new TextArea();
      area.setName(GuiElement.getEtrHashCode(1));
      area.setStyle("width:100%;height:100%");
      area.setTagText(wikitext);
      area.setClass("longtext_normal editable_inputfield");
      Table table = new Table();
      table.setStyle("width:100%;height:100%");
      table.addElement(new TR()
                       .addElement(new TD()
                            .setTagText(group.wiki2html("[["+COMMAND_SAVE+"|Speichern]] | [["+COMMAND_CANCEL+"|Abbruch]] | [["+COMMAND_UPDATE+"|Bearbeiten]] | [["+COMMAND_VERSION+"|Versionen]] | [["+COMMAND_ORPHANED+"|Verwaiste Seiten]]\n")))
                      .setStyle("height:1px"))
           .addElement(new TR()
                         .addElement(new TD()
                             .addElement(area)
                                     )
                       );
      group.setHtml(table.toString());

      context.createAskDialog("Kommentar", new IAskDialogCallback()
      {
        public void onOk(IClientContext context, String response) throws Exception
        {
          IDataTransaction trans = context.getSelectedRecord().getCurrentTransaction();
          context.getApplication().setSearchBrowserVisible(false);
          String value = group.getValue(1);
          if(StringUtil.saveEquals(value,context.getSelectedRecord().getSaveStringValue({Modulename}.wikitext) ))
          {
            // do nothing
          }
          else
          {
            context.getSelectedRecord().setValue(trans, {Modulename}.comment, response);
            context.getSelectedRecord().setValue(trans, {Modulename}.wikitext, value);
            trans.commit();
          }
          trans.close();
        }
      
        public void onCancel(IClientContext context) throws Exception
        {
          context.createMessageDialog("Ohne Kommentar kann die Änderung nicht gespeichert werden").show();
        }
      }).show();
    }
    else if(COMMAND_VERSION.equals(link))
    {
      context.getApplication().setSearchBrowserVisible(true);
      IDataTable table = context.getDataTable();
      String link_name = table.getSelectedRecord().getSaveStringValue({Modulename}.link_name);
      if(context.getSelectedRecord().getCurrentTransaction()!=null)
      {
        table.getSelectedRecord().getCurrentTransaction().close();
        loadOrCreateWikiPageByTag(context, link_name);
      }
      table.qbeClear();
      table.qbeSetKeyValue({Modulename}.link_name, link_name);
      IDataBrowser wikiBrowser = context.getDataBrowser("{modulename}Browser");
      wikiBrowser.setMaxRecords(50);
      wikiBrowser.search(IRelationSet.LOCAL_NAME);
    }
    else if (COMMAND_ORPHANED.equals(link))
    {
      // Alle Targets holen und dem Datenhook zum filtern zur Verfügung stellen
      //
      Set targets = new HashSet();
      IDataTable wiki_link = context.getDataAccessor().newAccessor().getTable({Modulename}_link.NAME);
      wiki_link.search();
      for(int i=0;i<wiki_link.recordCount();i++)
      {
        targets.add(wiki_link.getRecord(i).getSaveStringValue({Modulename}_link.target));
      }
      context.setPropertyForRequest("filterOrphanedPages", Boolean.TRUE);
      context.setPropertyForRequest("linkTargets", targets);
      context.setPropertyForRequest("alreadyAddedRecords", new HashSet());
      
      context.getApplication().setSearchBrowserVisible(true);
      context.getDataAccessor().qbeClearAll();
      IDataBrowser wikiBrowser = context.getDataBrowser("{modulename}Browser");
      wikiBrowser.setMaxRecords(100);
      wikiBrowser.search(IRelationSet.LOCAL_NAME);
    }
    else
    {
      context.getApplication().setSearchBrowserVisible(false);
      loadOrCreateWikiPageByTag(context, link);
    }
  }

  public byte[] getImageData(IClientContext context, IHtmlGroup group, String imageId)
  {
    try
    {
      IDataTable imageTable = context.getDataTable({Modulename}_image.NAME);
      imageTable.qbeClear();
      imageTable.qbeSetKeyValue({Modulename}_image.name, imageId);
      if(imageTable.search(IRelationSet.LOCAL_NAME)==1)
      {
        return imageTable.getSelectedRecord().getDocumentValue({Modulename}_image.image).getContent();
      }

      InputStream stream= this.getClass().getResourceAsStream("blank.png");
      byte[] img = IOUtils.toByteArray(stream);
      stream.close();
      return img;
    }
    catch(Exception exc)
    {
      //ignore
    }
    return null;
  }

  /**
   * Will be called if the will be change the state from visible=>hidden.
   * 
   * This happends if the user switch the Domain or Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onHide(IClientContext context, IHtmlGroup group) throws Exception 
  {
    context.getApplication().setSearchBrowserVisible(true);
    context.getApplication().setToolbarVisible(true);
  }
  
  /**
   * Stellt sicher, dass immer ein selectedRecord vorhanden ist.
   * 
   *  1. Record selektiert => ok/return
   *  2. keiner selektiert => "home" suchen und selektieren
   *  3. "home" nicht vorhanden => "home" anlegen und selektieren
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onShow(IClientContext context, IHtmlGroup group) throws Exception 
  {
    context.getApplication().setSearchBrowserVisible(false);
    context.getApplication().setToolbarVisible(false);

    if(context.getSelectedRecord()!=null)
      return;
    loadOrCreateWikiPageByTag(context, ROOT_PAGE_TAG);
  }
  
  /**
   * Zeigt die neuste Wikiseite mit dem übergebenen TAG an. Ist die Seite 
   * nicht vorhanden, so wird diese angelegt und sofort in den Updatemodus gebracht.
   * 
   * @param context
   * @param tag
   * @throws Exception
   */
  private void loadOrCreateWikiPageByTag(IClientContext context, String tag) throws Exception
  {
    IDataAccessor acc =context.getDataAccessor();
    IDataTable wikiTable = acc.getTable({Modulename}.NAME);
    wikiTable.qbeClear();
    wikiTable.qbeSetKeyValue({Modulename}.link_name, tag);
    IDataBrowser wikiBrowser = acc.getBrowser("{modulename}Browser");
    wikiBrowser.setMaxRecords(1);
    wikiBrowser.search(IRelationSet.LOCAL_NAME);
    if(wikiBrowser.recordCount()==0)
    {
      IDataTransaction trans = acc.newTransaction();
      try
      {
        IDataTableRecord wikiRecord =  wikiTable.newRecord(trans);
        wikiRecord.setStringValue(trans, {Modulename}.link_name,tag);
        trans.commit();
        // fill the browser
        wikiBrowser.search(IRelationSet.LOCAL_NAME);
        
        // den neusten Artikel mit dem TAG "home" anzeigen
        //
        wikiBrowser.setSelectedRecordIndex(0);

        // und sofort in den updatemodus bringen
        DataTable table = (DataTable) context.getDataTable();
        table.updateSelectedRecord(table.startNewTransaction());
      }
      finally
      {
        trans.close();
      }
    }
    else
    {
      wikiBrowser.setSelectedRecordIndex(0);
      acc.propagateRecord(wikiBrowser.getRecord(0).getTableRecord(),Filldirection.BOTH);
    }
  }
}
