package jacob.entrypoint.gui;

import jacob.model.IncidentEntry;
import jacob.relationset.IncidentEntryRelationset;

import java.util.Properties;

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
 * http://localhost:8080/jacob/enter?entry=ShowIncident&app=qualitymaster&user=USERNAME&pwd=PASSWORD&param1=abc
 *  
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and password of the application.
 *       2. Replace localhost:8080 with the real servername and port.
 *       3. You can add any additional parameters to the url. The jACOB application servers will provide them
 *          for you via the properties.getProperty("...") method.
 *
 * @author {user}
 *
 */
public class ShowIncidentCustomer extends IGuiEntryPoint
{
  
	/* 
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 */
	public void enter(IClientContext context, Properties props)throws Exception
	{
    IDataTable table = context.getDataTable(IncidentEntry.NAME);
    table.clear();
    table.qbeSetValue(IncidentEntry.pkey, props.getProperty("pkey"));
    table.search();
    if (table.recordCount() == 1)
    {
      IDataTableRecord userRecord = table.getRecord(0);
      IRelationSet relSet = context.getDataAccessor().getApplication().getRelationSet(IncidentEntryRelationset.NAME);
      context.getDataAccessor().propagateRecord(userRecord, relSet, Filldirection.BOTH);
    }
	}


	/**
	 * Return the domain for the GUI entry point. 
	 * A Domain is the name of a group in your outlookbar/composed application
	 */
	public String getDomain()
	{
			return "<customer>";
	}

	/**
	 * Return the name of a form within the returned domain. 
	 */
	public String getForm()
	{
		return "incidentEntry";
	}

	/**
	 * @return false if the GUI entry point has no left side navigation (outlookbar) 
	 */
	public boolean hasNavigation()
	{
		return true;
	}

	/** 
	 * @return false if the GUI entry point has no search browser
	 */
	public boolean hasSearchBrowser()
	{
		return true;
	}

	/** 
	 * @return false if the GUI entry point has no toolbar a the top
	 */
	public boolean hasToolbar()
	{
		return true;
	}
}
