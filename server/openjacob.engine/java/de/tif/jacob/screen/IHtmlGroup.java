/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen;


public interface IHtmlGroup extends IGroup
{
  static public final String RCS_ID = "$Id: IHtmlGroup.java,v 1.4 2008/12/01 21:06:27 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.4 $";
  
  /**
   * Set the HTML to show.
   * 
   * @param html
   * @throws Exception
   */
  public void setHtml(String html) throws Exception;

  /**
   * Set the HTML to show in Wikipedia syntax. Got to www.wikipedia.org for a clean
   * description of the wikpedia syntax.
   * 
   * @param wiki
   * @throws Exception
   */
  public void setWiki(String wiki) throws Exception;
  
  /**
   * Append the HTML to show.
   * 
   * @param html
   * @throws Exception
   */
  public void appendHtml(String html) throws Exception;

  /**
   * Append the HTML to show in Wikipedia syntax. Got to www.wikipedia.org for a clean
   * description of the wikpedia syntax.
   * 
   * @param wiki
   * @throws Exception
   */
  public void appendWiki(String wiki) throws Exception;
  
  
  public String wiki2html(String wiki) throws Exception;

  /**
   * Returns the HTML Form variable value with the given id.
   * 
   * @param id
   * @return
   */
  public String getValue(int id);
}
