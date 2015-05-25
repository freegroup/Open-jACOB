package jacob.entrypoint.gui;

import java.util.Properties;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * 
 * You can access this entry point within an WebBrowser with the URL:
 * http://localhost:8080/jacob/enter?entry=ShowTask&app=imsys&user=USERNAME&pwd=PASSWORD&param1=abc
 *  
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and password of the application.
 *       2. Replace localhost:8080 with the real servername and port.
 *       3. You can add any additional parameters to the url. The jACOB application servers will provide them
 *          for you via the properties.getProperty("...") method.
 *
 * @author {user}
 *
 */
public class ShowTask extends IGuiEntryPoint
{
  
	/* 
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 */
    public void enter(IClientContext context, Properties props)throws Exception
    {
      IDataTable table = context.getDataTable("task");
      table.clear();
      table.qbeSetValue("pkey", props.getProperty("pkey"));
      table.search("r_task");
      if (table.recordCount() == 1)
      {
        IDataTableRecord userRecord = table.getRecord(0);
      IRelationSet relSet = context.getDataAccessor().getApplication().getRelationSet("r_task");
        context.getDataAccessor().propagateRecord(userRecord, relSet, Filldirection.BOTH);
      }
    }


    /* 
     * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getDomain()
     * @author Andreas Herz
     */
    public String getDomain()
    {

            return "f_ut_callmanage";

            
    }

    /* 
     * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getForm()
     * @author Andreas Herz
     */
    public String getForm()
    {
        return "UTtask";
            
        
    }

    /* 
     * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasNavigation()
     * @author Andreas Herz
     */
    public boolean hasNavigation()
    {
        return true;
    }

    /* 
     * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasSearchBrowser()
     * @author Andreas Herz
     */
    public boolean hasSearchBrowser()
    {
        return true;
    }

    /* 
     * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasToolbar()
     * @author Andreas Herz
     */
    public boolean hasToolbar()
    {
        return true;
    }
}
