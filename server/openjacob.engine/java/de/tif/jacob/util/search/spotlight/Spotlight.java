/*
 * Created on 14.10.2010
 *
 */
package de.tif.jacob.util.search.spotlight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.util.search.ISearch;
import de.tif.jacob.util.search.Result;
import de.tif.jacob.util.search.fuzzy.FuzzySearch;

public  class Spotlight
{
  public static int MATCH_PERCENT = 50;
  private static String PROPERTY = "spotlight_search";
  
  public static boolean checkSpotlightSupport(IGuiElement root)
  {
     return checkRecursive(root);
  }
  
  private static boolean checkRecursive(IGuiElement parent)
  {
    if (parent instanceof GuiElement)
    {
      GuiElement gui = (GuiElement) parent;
      String prop = gui.getProperty(PROPERTY);
      if (StringUtils.isNotBlank(prop))
        return true;
    }
    List children = parent.getChildren();
    for (int i = 0; i < children.size(); i++)
    {
       if(checkRecursive((IGuiElement) children.get(i))==true)
         return true;
    }
    return false;
  }
  
  public static List  search(IGuiElement root, String searchTerm)
  {
    List result = new ArrayList(); // of type SpotlightResult
    enrichRecursive( result, root,  new FuzzySearch(), searchTerm);
    Collections.sort(result);
    return result;
  }
  
  private static void enrichRecursive( List hit, IGuiElement parent,ISearch searchEngine, String searchTerm)
  {
    if(parent instanceof GuiElement)
    {
      GuiElement gui = (GuiElement)parent;
      hit.addAll(getMatch(searchEngine, gui, searchTerm));
    }
    
    // Es ist ein Designfehler, dass eine Form als Kind an der Domain UND an einer Formgroup hängen kann
    // Somit kann es vorkommen, dass ein Treffer mehrfach zurückgeliefert wird. Diesem Umstand wird jetzt hier Rechnung getragen
    //
    if(parent instanceof IFormGroup)
      return;
    
    List children = parent.getChildren();
    for(int i=0;i<children.size();i++)
    {
      IGuiElement child = (IGuiElement)children.get(i);
      enrichRecursive( hit, child,searchEngine, searchTerm);
    }
  }
  
  private static List getMatch(ISearch searchEngine, GuiElement element, String searchTerm)
  {
    String prop = element.getProperty(PROPERTY);
    Set  searchTerms = Collections.EMPTY_SET;
    if(!StringUtils.isEmpty(prop))
      searchTerms = new HashSet(Arrays.asList(prop.split(",|;")));
    else 
      return Collections.EMPTY_LIST;
    
    List result = new ArrayList();
    List hits=searchEngine.find(searchTerms, searchTerm, MATCH_PERCENT);
    for(int i=0;i<hits.size();i++)
    {
      Result hit = (Result)hits.get(i);
      result.add(new  SpotlightResult(element, hit.keyword, hit.percentage));
    }
    return result;
  }
}
