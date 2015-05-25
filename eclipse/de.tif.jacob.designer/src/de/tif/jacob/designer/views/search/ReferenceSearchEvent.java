/*
 * Created on 29.06.2007
 *
 */
package de.tif.jacob.designer.views.search;

import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.SearchResultEvent;

public class ReferenceSearchEvent extends SearchResultEvent
{

  public ReferenceSearchEvent(ISearchResult searchResult)
  {
    super(searchResult);
  }
}
