/**************************************************************************
 * Project  : jacob.digisim
 * Date     : Thu Aug 23 20:48:59 CEST 2007
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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.4 2007/08/26 14:08:24 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
   public static final Role ADMIN = new Role("admin");
   public static final Role USER = new Role("user");
   public static final Role GUEST = new Role("guest");
   public static final Role ROOMADMIN = new Role("roomadmin");
	 
  private Role(String roleName)
  {
    super(roleName);
  }
}
