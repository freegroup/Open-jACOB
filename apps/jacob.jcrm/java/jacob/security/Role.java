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
  static public final transient String RCS_ID = "$Id: Role.java,v 1.4 2006/11/14 10:23:39 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  public static final Role ADMIN = new Role("admin"); 
  
  public static final Role OWNER = new Role("owner"); 
  
  public static final Role AGENT = new Role("agent"); 
  
  public static final Role PRODUCTMANAGER = new Role("productmanager"); 
  
  public static final Role SALESMANAGER = new Role("salesmanager"); 
  
  public static final Role SALESAGENT = new Role("salesagent"); 
  
  public static final Role INCIDENTAGENT = new Role("incidentagent"); 
  
  public static final Role CALLAGENT = new Role("callagent");
  
  private Role(String roleName)
  {
    super(roleName);
  }
}
