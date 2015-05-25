/*
 * Created on Mar 22, 2004
 *
 */
package jacob.security;

import de.tif.jacob.security.impl.AbstractRole;

/**
 * @author andreas
 */
public class Role extends AbstractRole
{
  static public final transient String RCS_ID = "$Id: Role.java,v 1.1 2007/11/25 22:10:32 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public static final Role ADMIN = new Role("admin"); 
  public static final Role USER  = new Role("user"); 
  public static final Role GUEST = new Role("guest"); 
  
  
  private Role(String roleName)
  {
    super(roleName);
  }
}
