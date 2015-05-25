/*
 * Created on Mar 22, 2004
 *
 */
package jacob.security;

import de.tif.jacob.security.impl.AbstractRole;

/**
  * @author andherz
 *
 */
public class Role extends AbstractRole
{
  static public final transient String RCS_ID = "$Id: Role.java,v 1.1 2005/02/28 15:49:39 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public Role(String roleName)
  {
    super(roleName);
  }
}
