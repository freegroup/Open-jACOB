/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen;


public interface IJacobGroup extends IGroup
{
  static public final String RCS_ID = "$Id: IJacobGroup.java,v 1.2 2008/12/01 21:06:27 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.2 $";

  /**
   * Enable or disable the border of this group.
   * 
   * @param flag
   *          The border visibility.
   * 
   * @since 2.7.4
   */
  public void setBorder(boolean flag);
}
