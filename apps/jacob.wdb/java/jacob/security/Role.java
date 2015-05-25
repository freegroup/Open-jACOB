/**************************************************************************
 * Project  : jacob.wdb
 * Date     : Fri Jul 30 14:27:52 CEST 2010
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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.1 2010-08-03 13:54:19 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
   public static final Role ADMIN = new Role("admin");
   public static final Role USER = new Role("user");
   public static final Role ANONYMOUS = new Role("anonymous");
   public static final Role DEBUG = new Role("debug");
   public static final Role PASSWORD = new Role("password");
	 
  private Role(String roleName)
  {
    super(roleName);
  }
}
