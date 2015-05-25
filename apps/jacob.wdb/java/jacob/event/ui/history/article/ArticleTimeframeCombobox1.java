/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 22:30:06 CEST 2010
 */
package jacob.event.ui.history.article;

import java.util.HashMap;
import java.util.Map;

import jacob.common.AppLogger;
import jacob.event.ui.history.AbstractTimeframeCombobox;
import jacob.model.History;
import jacob.relationset.HistoryRelationset;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableComboBoxEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 *
 * @author andherz
 */
public class ArticleTimeframeCombobox1 extends AbstractTimeframeCombobox
{
  static public final transient String RCS_ID = "$Id: ArticleTimeframeCombobox1.java,v 1.1 2010-08-17 20:46:54 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
}
