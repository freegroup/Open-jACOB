package jacob.event.ui.imported_files;

import jacob.model.Imported_files;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;

public class ButtonController
{
  public static void updateButtons(IClientContext context) throws Exception
  {
    IDataTableRecord record = context.getSelectedRecord();
    
    IButton uploadButton = (IButton)context.getGroup().findByName("uploadButton");
    IButton testButton   = (IButton)context.getGroup().findByName("testButton");
    IButton importButton = (IButton)context.getGroup().findByName("importButton");
    
    uploadButton.setEnable(false);
    testButton.setEnable(false);
    importButton.setEnable(false);
    uploadButton.setEmphasize(false);
    testButton.setEmphasize(false);
    importButton.setEmphasize(false);

    if(record == null)
    {
      uploadButton.setEnable(true);
      uploadButton.setEmphasize(true);
      return;
    }
    
    String state = record.getSaveStringValue(Imported_files.state);
    if(state.length()==0)
    {
      uploadButton.setEnable(true);
      uploadButton.setEmphasize(true);
      return;
    }
    if(state.equals(Imported_files.state_ENUM._uploaded))
    {
      uploadButton.setEnable(true);
      testButton.setEnable(true);
      testButton.setEmphasize(true);
      return;
    }
    if(state.equals(Imported_files.state_ENUM._tested))
    {
      testButton.setEnable(true);
      importButton.setEnable(true);
      importButton.setEmphasize(true);
      return;
    }
    if(state.equals(Imported_files.state_ENUM._imported))
    {
      return;
    }
   
  }
}
