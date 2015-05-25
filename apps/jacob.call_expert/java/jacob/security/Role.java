/**************************************************************************
 * Project  : jacob.CallExpert
 * Date     : Tue Nov 17 16:37:09 CET 2009
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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.2 2009/11/23 11:33:43 R.Spoor Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
   public static final Role ADMIN = new Role("admin");
   public static final Role USER = new Role("user");
   public static final Role CONFIGURATION = new Role("configuration");
   public static final Role DEBUGGING = new Role("debugging");
	 
  private Role(String roleName)
  {
    super(roleName);
  }
}
