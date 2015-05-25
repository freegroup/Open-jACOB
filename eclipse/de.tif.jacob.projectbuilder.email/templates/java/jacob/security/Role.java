/**************************************************************************
 * Project  : jacob.email
 * Date     : Tue Nov 20 10:36:04 CET 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.security;

import de.tif.jacob.security.impl.AbstractRole;

/**
 * @author andreas
 */
public class Role extends AbstractRole
{
  static public final transient String RCS_ID = "$Id: Role.java,v 1.1 2007/11/25 22:12:37 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
   public static final Role USER = new Role("user");
   public static final Role ADMIN = new Role("admin");
	 
  private Role(String roleName)
  {
    super(roleName);
  }
}
