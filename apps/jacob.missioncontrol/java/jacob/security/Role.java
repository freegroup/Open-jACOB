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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.1 2005/07/13 13:29:26 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public Role(String roleName)
  {
    super(roleName);
  }
}
