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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.3 2004/07/12 06:56:42 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  public Role(String roleName)
  {
    super(roleName);
  }
}
