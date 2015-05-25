/*
 * Created on 29.06.2007
 *
 */
package de.tif.jacob.designer.views.search;


import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;


public interface ReferenceSearchQuery extends ISearchQuery 
{


  /*
   * @see org.eclipse.search.ui.ISearchQuery#getSearchResult()
   */
  public ObjectModel getModel();

}