package jacob.event.ui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 16 13:19:35 CEST 2005
 *
 */
import jacob.common.AppLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.RecordLabelProvider;
import de.tif.jacob.screen.dialogs.IRecordTreeDialog;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * The Event handler for the CallInfo-Button. <br>
 * The onAction will be calle if the user clicks on this button <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author mike
 * 
 */
public class CallInfo extends IButtonEventHandler
{
    static class MyCallback implements IRecordTreeDialogCallback
    {
        private static Map redirects = new HashMap();
        
        static class FormRedirect
        {
          final String alias;  
          final String form;
          final String browser;
          final String relation;
        
        protected FormRedirect(String alias, String form,  String browser,  String relation)
        {
          this.alias = alias;  
          this.form = form;
          this.browser = browser;
          this.relation = relation;
        }
        }
        static
        {
            redirects.put("call",     new FormRedirect("call","UTcallMngrDocument","UcallBrowser","r_call"));
            redirects.put("callduplicate", new FormRedirect("call","UTcallMngrDocument","UcallBrowser","r_call"));
            redirects.put("task",new FormRedirect("task","UTtask","taskUTBrowser","r_task"));
            redirects.put("callmaster", new FormRedirect("call","UTcallMngrDocument","UcallBrowser","r_call"));
            redirects.put("tasktable",new FormRedirect("task","UTtask","taskUTBrowser","r_task"));
            redirects.put("taskview",    new FormRedirect("task","UTtask","taskUTBrowser","r_task"));

        }

        public void onSelect(IClientContext context, IDataTableRecord selectedRecord) throws Exception
        {
            String pkey = selectedRecord.getStringValue("pkey");
            
            String target = selectedRecord.getTableAlias().getName();
            FormRedirect redirect = (FormRedirect)redirects.get(target);
            
            context.setCurrentForm("f_ut_callmanage",redirect.form);
            IDataBrowser callbrowser = context.getDataBrowser(redirect.browser);

            context.clearDomain();
            IDataTable table = context.getDataTable(redirect.alias);
            table.qbeSetValue("pkey", pkey);
            
            callbrowser.search(redirect.relation, Filldirection.BOTH);
            callbrowser.setSelectedRecordIndex(0);
            callbrowser.propagateSelections();
        }
    }

    static class MyLabelProvider extends RecordLabelProvider 
    {
        public static final String STORNIERT = "Storniert";
        public static final String FERTIG_GEMELDET = "Fertig gemeldet";
        public static final String DOKUMENTIERT = "Dokumentiert";
        public static final String ABGERECNHET = "Abgerechnet";
        public static final String ABGESCHLOSSEN = "Abgeschlossen";
        public static final String VERWORFEN = "Verworfen";
        public static final String GESCHLOSSEN = "Geschlossen";        
        private static final Set allClosedStatus = new HashSet();
        private static final Map statusFieldMap = new HashMap();
        private static final Map infoFieldMap = new HashMap();
        private static final Map aliasNameMap = new HashMap();
        private static final Map keyNameMap = new HashMap();
        static
        {
            allClosedStatus.add(STORNIERT);
            allClosedStatus.add(FERTIG_GEMELDET);
            allClosedStatus.add(DOKUMENTIERT);
            allClosedStatus.add(ABGERECNHET);
            allClosedStatus.add(ABGESCHLOSSEN);
            allClosedStatus.add(VERWORFEN);
            allClosedStatus.add(GESCHLOSSEN);
            
            aliasNameMap.put("callmaster","Hauptmeldung");
            aliasNameMap.put("tasktable","Hauptauftrag");
            aliasNameMap.put("call","Meldung");
            aliasNameMap.put("task","Auftrag");
            aliasNameMap.put("taskview","Unterauftrag");
            aliasNameMap.put("callduplicate","Untermeldung");
            
            statusFieldMap.put("callmaster","callstatus");
            statusFieldMap.put("task","taskstatus");
            statusFieldMap.put("call","callstatus");
            statusFieldMap.put("tasktable","taskstatus");
            statusFieldMap.put("taskview","taskstatus");
            statusFieldMap.put("callduplicate","callstatus");
            
            infoFieldMap.put("callmaster","problem");
            infoFieldMap.put("task","summary");
            infoFieldMap.put("call","problem");
            infoFieldMap.put("tasktable","summary");
            infoFieldMap.put("taskview","summary");
            infoFieldMap.put("callduplicate","problem");
            
            keyNameMap.put("call","pkey");
            keyNameMap.put("task","taskno");
            keyNameMap.put("callmaster","pkey");
            keyNameMap.put("tasktable","taskno");
            keyNameMap.put("taskview","taskno");
            keyNameMap.put("callduplicate","pkey");

            
        }
        
        public String getImage(IClientContext context, Object record) 
        {    
            String myReturn = null;
            try
        {
                

                IDataTableRecord thisRec = (IDataTableRecord) record;
                String alias = thisRec.getTableAlias().getName();
                
                // taskgood ,taskbad, callbad und callgood zurückgeben
                String prefix = null;
                if (alias.startsWith("call"))
                {
                    prefix ="call";
                }
                else
                {
                    prefix ="task";
                }
                if( allClosedStatus.contains(thisRec.getSaveStringValue((String)statusFieldMap.get(alias))))
                {
                    myReturn =  prefix+"good";
                }
                else
                {
                    myReturn =  prefix+"bad";
                }
            }
            catch (Exception e)
            {
               ExceptionHandler.handle(context,e);
            }
            return myReturn;

        }

        public String getText(IClientContext context, Object record)
        {
            String myReturn = "";
            try
            {
                IDataTableRecord thisRec = (IDataTableRecord) record;
                String alias = thisRec.getTableAlias().getName();
                myReturn =(String)aliasNameMap.get(alias) + " "+ thisRec.getSaveStringValue((String)keyNameMap.get(alias)) + " : " + thisRec.getSaveStringValue((String)infoFieldMap.get(alias));
                if (myReturn.length()>80)
                {
                    myReturn = myReturn.substring(0,79);
                }
            }
            catch (Exception e)
            {
               ExceptionHandler.handle(context,e);
            }
            return myReturn;

        }
    }

    static public final transient String RCS_ID = "$Id: CallInfo.java,v 1.6 2005/12/02 15:11:11 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.6 $";

    // use this logger to write messages and NOT the System.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The user has been click on the corresponding button. <br>
     * Be in mind: The currentRecord can be null if the button has not the
     * [selected] flag. <br>
     * The selected flag warranted that the event can only be fired if the <br>
     * selectedRecord!=null. <br>
     * 
     * 
     * @param context
     *            The current client context
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
        IDataTableRecord currentRecord = context.getSelectedRecord();
        if (currentRecord.hasLinkedRecord("callmaster"))
        {
            IDataAccessor newAccessor = context.getDataAccessor().newAccessor();
            IDataTable call = newAccessor.getTable("call");
            call.qbeSetKeyValue("pkey",currentRecord.getStringValue("mastercall_key"));
            call.search("r_call");
            if (call.recordCount()==1)
                currentRecord= call.getRecord(0); 
        }
            
        
        IRelationSet relationSet = context.getApplicationDefinition().getRelationSet("r_call_tree");
        final Filldirection filldirection = Filldirection.BOTH;
        IRecordTreeDialog dialog = context.createRecordTreeDialog(button, currentRecord, relationSet, filldirection, new MyCallback());
        dialog.setLabelProvider(new MyLabelProvider());
        dialog.setAutoclose(false);
        dialog.show();
    }

    /**
     * The status of the parent group (TableAlias) has been changed. <br>
     * <br>
     * This is a good place to enable/disable the button on relation to the
     * group state or the selected record. <br>
     * <br>
     * Possible values for the state is defined in IGuiElement <br>
     * <ul>
     * <li>IGuiElement.UPDATE</li>
     * <li>IGuiElement.NEW</li>
     * <li>IGuiElement.SEARCH</li>
     * <li>IGuiElement.SELECTED</li>
     * </ul>
     * 
     * Be in mind: The currentRecord can be null if the button has not the
     * [selected] flag. <br>
     * The selected flag warranted that the event can only be fired if the <br>
     * selectedRecord!=null. <br>
     * 
     * @param context
     *            The current client context
     * @param status
     *            The new group state. The group is the parent of the
     *            corresponding event button.
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        // You can enable/disable the button in relation to your conditions.
        //
        // button.setEnable(true/false);
    }
 }
