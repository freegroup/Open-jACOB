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


public class ReferenceSearchQueryFirstLevel implements ReferenceSearchQuery 
{
  private ReferenceSearchResult fResult;
  private ObjectModel model;
  public ReferenceSearchQueryFirstLevel(ObjectModel model) 
  {
    fResult = new ReferenceSearchResult(this);
    this.model = model;
  }
  
  /*
   * @see org.eclipse.search.ui.ISearchQuery#run(org.eclipse.core.runtime.IProgressMonitor)
   */
  public IStatus run(IProgressMonitor monitor) 
  {
    if (fResult != null) 
    {
      new StatusInfo(IStatus.ERROR, "Query has already been running"); //$NON-NLS-1$
    }
    try 
    {
      fResult.fireChange(new ReferenceSearchEvent(fResult));
      JacobModel jacobModel = model.getJacobModel();
      
      jacobModel.addReferrerObject(fResult,model);
    } 
    finally 
    {
      monitor.done();
    }
    return Status.OK_STATUS;
  }
  
  /*
   * @see org.eclipse.search.ui.ISearchQuery#getLabel()
   */
  public String getLabel() {
    return "Reference JobQuery";
  }
  
  public String getResultLabel(int nMatches) 
  {
    if (nMatches == 1) {
      return "Found 1";
    } else {
      return "Found more";
    }
  }
    
  /*
   * @see org.eclipse.search.ui.ISearchQuery#canRerun()
   */
  public boolean canRerun() 
  {
    return false; // must release finder to not keep AST reference
  }

  /*
   * @see org.eclipse.search.ui.ISearchQuery#canRunInBackground()
   */
  public boolean canRunInBackground() 
  {
    return true;
  }

  /*
   * @see org.eclipse.search.ui.ISearchQuery#getSearchResult()
   */
  public ISearchResult getSearchResult() 
  {
    return fResult;
  }

  public ObjectModel getModel()
  {
    return model;
  }
}