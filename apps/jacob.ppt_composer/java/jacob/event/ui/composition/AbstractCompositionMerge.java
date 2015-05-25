package jacob.event.ui.composition;

import jacob.model.Composition;
import jacob.model.Ppt;
import jacob.model.Ppt_to_composition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.TreeMap;

import com.aspose.slides.Presentation;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

public abstract class AbstractCompositionMerge extends IButtonEventHandler
{
  protected void merge(IClientContext context, IGuiElement button, String documentField) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    String pkey = currentRecord.getSaveStringValue(Composition.pkey);
    IDataAccessor accessor = context.getDataAccessor().newAccessor();

    IDataBrowser linkBrowser = accessor.getBrowser("ppt_to_compositionDocumentBrowser");
    IDataTable table = accessor.getTable(Ppt_to_composition.NAME);

    table.qbeSetKeyValue(Ppt_to_composition.composition_key, pkey);
    linkBrowser.search(IRelationSet.DEFAULT_NAME);
    try
    {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      if (linkBrowser.recordCount() == 0)
      {
        Presentation part1 = new Presentation();
        part1.write(out);
      }
      else if (linkBrowser.recordCount() == 1)
      {
        IDataBrowserRecord linkRecord = linkBrowser.getRecord(0);

        if (linkRecord.getDocumentValue(documentField) != null)
        {
          Presentation part1 = new Presentation(new ByteArrayInputStream(linkRecord.getDocumentValue(documentField).getContent()));
          part1.write(out);
        }
        else
        {
          Presentation part1 = new Presentation();
          part1.write(out);
        }
      }
      else
      {
        IDataBrowserRecord linkRecord = linkBrowser.getRecord(0);
        Presentation part1 = new Presentation(new ByteArrayInputStream(linkRecord.getDocumentValue(documentField).getContent()));

        for (int r = 1; r < linkBrowser.recordCount(); r++)
        {
          linkRecord = linkBrowser.getRecord(r);
          if (linkRecord.getDocumentValue(documentField) != null)
          {
            Presentation part2 = new Presentation(new ByteArrayInputStream(linkRecord.getDocumentValue(documentField).getContent()));

            for (int i = 0; i < part2.getSlides().size(); i++)
            {
              part2.cloneSlide(part2.getSlides().get(i), part1.getSlides().getLastSlidePosition() + 1, part1, new TreeMap());
            }
          }
        }

        part1.write(out);
      }
      context.createDocumentDialog(null, currentRecord.getSaveStringValue(Composition.name) + ".ppt", out.toByteArray()).show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }
}
