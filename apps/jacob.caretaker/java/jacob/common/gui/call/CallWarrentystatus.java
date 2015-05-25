/*
 * Created on 02.12.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 * 
 * @author mike
 *
 */
public class CallWarrentystatus extends IComboBoxEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IComboBoxEventHandler#onSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IComboBox)
	 */
	public void onSelect(IClientContext context, IComboBox comboBox) throws Exception
	{

	}
	 public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	  {
	    IComboBox comboBox =(IComboBox)emitter;

	    IDataTableRecord record =context.getSelectedRecord();
	    if(record!=null)
	    {
	    	comboBox.enableOptions(true);
	      String comboStatus =record.getStringValue("warrentystatus");
	      if ("wird verfolgt".equals(comboStatus))	      	
	      	comboBox.enableOptions(false);
	      else
	      	comboBox.enableOption("wird verfolgt",false);
	     }

	    else
	    {
	    	comboBox.enableOptions(true);
	    	
	    }
	      
	  }

}
