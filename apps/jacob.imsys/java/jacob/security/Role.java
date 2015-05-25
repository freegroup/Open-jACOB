/*
 * Created on Mar 22, 2004
 *
 */
package jacob.security;

import de.tif.jacob.security.impl.AbstractRole;

/**
 * @author Andreas Herz
 *
 */
public class Role extends AbstractRole
{
  static public final transient String RCS_ID = "$Id: Role.java,v 1.5 2005/06/27 12:23:19 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";
  
  public Role(String roleName)
  {
    super(roleName);
  }
}
