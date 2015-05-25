/*
 * Created on 14.10.2010
 *
 */
package de.tif.jacob.util.search.spotlight;

import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;

public  class SpotlightResult implements Comparable
{
  private IGuiElement element;
  private String match;
  private int percent;
  private IDomain domain=null;
  private IForm form=null;
  
  public SpotlightResult(IGuiElement element, String match, int percent)
  {
    this.element = element;
    this.match = match;
    this.percent=percent;
    IGuiElement current = element;
    domain =element instanceof IDomain?(IDomain)element:null;
    form = null;
    while(domain==null)
    {
      if(current instanceof IDomain)
        domain = (IDomain) current;
      else if(current instanceof IForm)
        form = (IForm) current;
      current = current.getParent();
    }
  }

  public String toString()
  {
    if(this.getDomain()!=null)
        return "SpotlightResult [domain="+this.getDomain().getName()+" element="+element.getName()+" match=" + match + ", percent=" + percent + "]";
    return "SpotlightResult [domain=NULL element="+element.getName()+" match=" + match + ", percent=" + percent + "]";
   }

  public int compareTo(Object arg0)
  {
    SpotlightResult other = (SpotlightResult)arg0;
    
    // Für ein GUI Element gibt es nur einen Treffer. Daher return 0 für ==
    if(this.element == other.element)
      return 0;
    
    // Falls keine Domain vorhanden, dann ist percentage Pflicht
    //
    if(other.getDomain()==null && this.getDomain()==null)
      return Float.compare(other.percent, this.percent);
    
    if(other.getDomain()!=null && this.getDomain()==null)
      return -1;
 
    if(other.getDomain()==null && this.getDomain()!=null)
      return 1;
    
    // Unterschiedliche Domains => sortierung nur nach Domainname
    //
    if(other.getDomain() != this.getDomain())
      return other.getDomain().getName().compareTo(this.getDomain().getName());
    
    // Identische Domain => nach Name und Percentage sortierung
    //
    if(other.percent==this.percent)
      return other.match.compareTo(this.match);
    
    return Float.compare(other.percent, this.percent);
 }
  
  public IDomain getDomain()
  {
    return domain;
  }

  public IForm getForm()
  {
    return form;
  }
  
  public IGuiElement getElement()
  {
    return element;
  }

  public String getMatch()
  {
    return match;
  }

  public int getPercent()
  {
    return percent;
  }
 }
