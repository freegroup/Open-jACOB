/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Apr 27 19:00:25 CEST 2009
 */
package jacob.event.ui.converter;

import jacob.common.AppLogger;
import jacob.model.Converter;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IHtmlGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IHtmlGroupEventHandler;
import de.tif.jacob.util.StringUtil;


/**
 *
 * @author andherz
 */
 public class AnyGroup extends IHtmlGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: AnyGroup.java,v 1.1 2009/05/22 11:30:46 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	@Override
	public String[] getRequiredIncludeFiles() 
	{
		return new String[]{"{modulname}/dbTube.js"};  
	}

	public void onClick(IClientContext context, IHtmlGroup group, String link)throws Exception 
	{
	   if(link.equals("search"))
		{
	      group.clear(context,true);
			group.getBrowser().getData().search(IRelationSet.LOCAL_NAME);
		}
		else if(link.equals("new"))
		{
		   InputStream is = this.getClass().getResourceAsStream("template.xml");
		   try
		   {
            String content = IOUtils.toString(is);
            IDataTableRecord newRecord = Converter.newRecord(context);
            newRecord.setStringValue(newRecord.getCurrentTransaction(), Converter.xml, content);
            newRecord.setStringValue(newRecord.getCurrentTransaction(), Converter.name, "Import Name");
            context.setPropertyForWindow("draw2d.doc", newRecord);
		   }
		   finally
		   {
		      is.close();
		   }
		}
		else if(link.equals("edit"))
		{
			context.getDataTable().updateSelectedRecord(IRelationSet.LOCAL_NAME);
		}
      else if(link.equals("delete"))
      {
         IDataTableRecord currentRecord = context.getSelectedRecord();
         IDataTransaction trans = context.getDataAccessor().newTransaction();
         try
         {
            IDataBrowserRecord browserRecord = group.getBrowser().getSelectedBrowserRecord(context);
            currentRecord.delete(trans);
            trans.commit();
            group.getBrowser().getData().removeRecord(browserRecord);
            context.getDataTable().clear();
         }
         finally
         {
            trans.close();
         }
      }
		else if(link.equals("save"))
		{
         IDataTableRecord currentRecord = context.getSelectedRecord();
         boolean addInBrowser = currentRecord.isNew();
  		   context.setPropertyForWindow("draw2d.doc", currentRecord);
			IDataTransaction trans = currentRecord.getCurrentTransaction();
			if(trans!=null)
			{
				trans.commit();
				trans.close();
			}
			if(addInBrowser)
			{
			   group.getBrowser().add(context, currentRecord);
			}
		}
		else if(link.equals("cancel"))
		{
         IDataTableRecord currentRecord = context.getSelectedRecord();
         IDataTransaction trans = currentRecord.getCurrentTransaction();
         if(!currentRecord.isNew())
         {
            context.setPropertyForWindow("draw2d.doc", currentRecord);
            if(trans!=null)
              trans.close();
         }
         else
         {
            trans.close();
            currentRecord.getTable().clear();
         }
		}
	}

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
   * @param status  The new status of the group.
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IHtmlGroup group) throws Exception
	{
	  group.setHtml("");
	  if(status==IGroup.SEARCH)
	     searchXML(context, group);
	  else if(status==IGroup.SELECTED)
	     showXML(context, group);
     else if(status==IGroup.NEW)
        editXML(context, group);
	  else if(status==IGroup.UPDATE)
	     editXML(context, group);
	}

  private void searchXML(IClientContext context, IHtmlGroup group) throws Exception
  {
     group.appendHtml("<div class=\"draw2d_toolbar\">");
     group.appendWiki("[[search|Search]] [[new|New]]");
     group.appendHtml("</div>");
     InputStream is = this.getClass().getResourceAsStream("content_search.html");
     try
     {
        String content = IOUtils.toString(is);
        content = StringUtil.replace(content, "@APPLICATION@", context.getApplicationDefinition().getName());
        content = StringUtil.replace(content, "@VERSION@", context.getApplicationDefinition().getVersion().toShortString());
        group.appendHtml(content);
     }
     finally
     {
        is.close();
     }
  }
  
  private void showXML(IClientContext context, IHtmlGroup group) throws Exception
  {
	  group.appendHtml("<div class=\"draw2d_toolbar\">");
	  group.appendWiki("[[edit|Edit]]  [[new|New]]  [[delete|Delete]]");
	  group.appendHtml("</div>");
	  InputStream is = this.getClass().getResourceAsStream("content_show.html");
	  try
	  {
		  IDataTableRecord currentRecord = context.getSelectedRecord();
		  context.setPropertyForWindow("draw2d.doc", currentRecord);
		  String content = IOUtils.toString(is);
		  content = StringUtil.replace(content, "@APPLICATION@", context.getApplicationDefinition().getName());
		  content = StringUtil.replace(content, "@VERSION@", context.getApplicationDefinition().getVersion().toShortString());
		  group.appendHtml(content);
	  }
	  finally
	  {
		  is.close();
	  }
  }
  
  private void editXML(IClientContext context, IHtmlGroup group) throws Exception
  {
	  group.appendHtml("<div class=\"draw2d_toolbar\">");
	  group.appendWiki("[[save|Speichern]] [[cancel|Abbruch]]");
	  group.appendHtml("</div>");
	  InputStream is = this.getClass().getResourceAsStream("content_edit.html");
	  try
	  {
		  IDataTableRecord currentRecord = context.getSelectedRecord();
		  context.setPropertyForWindow("draw2d.doc", currentRecord);
		  String content = IOUtils.toString(is);
		  content = StringUtil.replace(content, "@APPLICATION@", context.getApplicationDefinition().getName());
		  content = StringUtil.replace(content, "@VERSION@", context.getApplicationDefinition().getVersion().toShortString());
		  group.appendHtml(content);
	  }
	  finally
	  {
		  is.close();
	  }
  }
 }
