/**************************************************************************
 * Project  : jacob.docMan
 * Date     : Thu Sep 16 09:24:26 CEST 2010
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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
   public static final Role ADMIN = new Role("admin");
   public static final Role USER = new Role("user");
   public static final Role ANONYMOUS = new Role("anonymous");
   public static final Role DEBUG = new Role("debug");
   public static final Role PASSWORD = new Role("password");
   public static final Role EDITOR = new Role("editor");
	 
  private Role(String roleName)
  {
    super(roleName);
  }
}
