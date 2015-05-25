/**************************************************************************
 * Project  : jacob.ppt_composer
 * Date     : Thu Oct 04 14:04:50 CEST 2007
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
  static public final transient String RCS_ID = "$Id: Role.vm,v 1.1 2007/06/19 14:37:48 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
   public static final Role USER = new Role("USER");
   public static final Role ADMIN = new Role("ADMIN");
	 
  private Role(String roleName)
  {
    super(roleName);
  }
}
