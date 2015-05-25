/*
 * Created on 29.06.2007
 *
 */
package de.tif.jacob.designer.views.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.ObjectModel;

public class ReferenceSearchResult implements ISearchResult 
{
  private List fListeners = new ArrayList();
  
  private ISearchQuery query;
  private List<ObjectModel> references = new ArrayList<ObjectModel>();
  
  public ReferenceSearchResult(ISearchQuery query) 
  {
    this.query = query;
  }


  public ImageDescriptor getImageDescriptor()
  {
    return JacobDesigner.getImageDescriptor("aa_action.gif");
  }

  public String getLabel()
  {
    return "SearchResultLabel";
  }

  public ISearchQuery getQuery()
  {
    return query;
  }

  public String getTooltip()
  {
    return "Tooltip";
  }

  /**
   * {@inheritDoc}
   */
  public void addListener(ISearchResultListener l) 
  {
    synchronized (fListeners) {
      fListeners.add(l);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  public void removeListener(ISearchResultListener l) 
  {
    synchronized (fListeners) {
      fListeners.remove(l);
    }
  }
 

  /**
   * Send the given <code>SearchResultEvent</code> to all registered search
   * result listeners.
   * 
   * @param e the event to be sent
   * 
   * @see ISearchResultListener
   */
  protected void fireChange(SearchResultEvent e) 
  {
    HashSet copiedListeners= new HashSet();
    synchronized (fListeners) {
      copiedListeners.addAll(fListeners);
    }
    Iterator listeners= copiedListeners.iterator();
    while (listeners.hasNext()) {
      ((ISearchResultListener) listeners.next()).searchResultChanged(e);
    }
  }


  public void addReferences(ObjectModel ref)
  {
    references.add(ref);
    fireChange(new ReferenceSearchEvent(this));
  }


  public List<ObjectModel> getReferences()
  {
    return references;
  }
 }