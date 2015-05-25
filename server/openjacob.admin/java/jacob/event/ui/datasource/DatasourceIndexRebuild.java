/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 12 16:08:32 CEST 2010
 */
package jacob.event.ui.datasource;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the DatasourceIndexRebuild record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class DatasourceIndexRebuild extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: DatasourceIndexRebuild.java,v 1.3 2010/07/30 15:53:49 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();

    DataSource dataSource = DataSource.get(currentRecord.getStringValue(Datasource.name));
    if (dataSource instanceof IndexDataSource)
    {
      IndexDataSource indexDataSource = (IndexDataSource) dataSource;
      
      IApplicationDefinition indexUpdateApplication = indexDataSource.getDefaultIndexUpdateApplication();
      if (indexUpdateApplication == null)
        throw new UserException("No application version for rebuilding this index existing");

      // The rebuild must run in the context of the index updating
      // application
      //
      // IBIS: HACK with task context - misuse?
      Context indexContext = new TaskContextSystem(indexUpdateApplication, context.getUser());
      Context.setCurrent(indexContext);
      try
      {
        indexDataSource.rebuild(indexContext);
      }
      finally
      {
        // switch back to the original Context
        //
        Context.setCurrent(context);
      }
      
      alert("Datasource successfully rebuild");
    }
	}
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    button.setEnable(status == IGuiElement.SELECTED && //
      Datasource.rdbType_ENUM._Lucene.equals(context.getSelectedRecord().getStringValue(Datasource.rdbtype)));
	}
}

