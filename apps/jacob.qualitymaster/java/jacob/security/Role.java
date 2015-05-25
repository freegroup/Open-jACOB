/**************************************************************************
 * Project  : jacob.qualitymaster
 * Date     : Wed Apr 30 19:18:35 CEST 2008
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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.5 2008/04/30 17:39:54 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";
  
   public static final Role DEVELOPER = new Role("developer");
   public static final Role CUSTOMER = new Role("customer");
   public static final Role ANONYMOUS = new Role("anonymous");
	 
  private Role(String roleName)
  {
    super(roleName);
  }
}
