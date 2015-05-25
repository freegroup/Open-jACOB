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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.2 2006-03-24 16:01:03 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  public Role(String roleName)
  {
    super(roleName);
  }
}
