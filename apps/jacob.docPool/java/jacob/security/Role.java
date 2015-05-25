/**************************************************************************
 * Project  : jacob.docPool
 * Date     : Mon Jul 05 12:57:39 CEST 2010
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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.2 2010-07-16 14:26:15 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
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
