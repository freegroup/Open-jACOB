/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.core.definition.impl.aliascondition;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.impl.AbstractApplicationDefinition;
import de.tif.jacob.core.definition.impl.AbstractApplicationInfo;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractTableAlias;
import de.tif.jacob.screen.impl.html.Application;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.security.IRole;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.impl.AbstractUser;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Test
{
	static public transient final String RCS_ID = "$Id: Test.java,v 1.4 2007/10/16 00:24:14 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.4 $";

	private static final String[] text_expressions = { //
		"", //
		" ", //
		"agent.pkey not	in (0,1)", //
		"agent.supportrole like 'asd%'", //
		"agent.supportrole is  null", //
		"agent.supportrole is not  null", //
		"agent.supportrole not like 'asd%'", //
		"agent.supportrole between 1 and 2", //
		"agent.supportrole not   between 1 and 2", //
		"callworkgroup.wrkgrptype='hhh''aa' ", //
		"problemmanager.wrkgrptype={MANDATORID}", //
		"problemmanager.wrkgrptype='{MANDATORID}'", //
		"problemmanager.wrkgrptype<>{USERID}", //
		"problemmanager.wrkgrptype={LOGINID}", //
		"problemmanager.wrkgrptype={MANDATORID} and problemmanager.name>=67", //
    "problemmanager.wrkgrptype='{MANDATORID}' and problemmanager.name>=67", //
    "problemmanager.wrkgrptype={USERPROPERTY.myprop} and problemmanager.name>=67", //
    "problemmanager.wrkgrptype='{USERPROPERTY.myprop}' and problemmanager.name>=67", //
    "problemmanager.wrkgrptype={USERPROPERTY.mypropnotexisting} and problemmanager.name>=67", //
    "problemmanager.wrkgrptype='{USERPROPERTY.mypropnotexisting}' and problemmanager.name>=67", //
		"problemmanager.wrkgrptype=abc()", //
		"problemmanager.wrkgrptype<=lower(1, 5)+4", //
		"webqauthint.employeewebqauth not in ({ROLES})", //
		"webqauthint.employeewebqauth in ({ROLES})", //
		"webqauthint.employeewebqauth in (1, 2,3)" };

	private static void testExpression(AliasCondition condition, String expression) throws Exception
	{
		if (condition == null)
		{
			System.out.println("'" + expression + "' -> SQL: NO EXPRESSION!");
		}
		else
		{
			ITableAliasCondition aliasCondition = condition.getResult(null);
			System.out.println("'" + expression + "' -> SQL: " + (aliasCondition == null ? "NO EXPRESSION!" : aliasCondition.toString()));
		}
	}

	private static void testExpression(IUser user) throws Exception
	{
		System.out.println();
		System.out.println("*** User: " + user);
		
		Definition definition = new Definition();
		ApplicationDefinition applDefinition = new ApplicationDefinition(definition);
		
		// initialization
		Context.setCurrent(new ClientContext(user,new TestApplication(user, applDefinition), "dummy"));

		String[] expressions = text_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			testExpression(AliasCondition.parseWithPostProcessing(expressions[i], definition, new Alias("dummy")), expressions[i]);
		}
	}

	public static void main(String[] args)
	{
		try
		{
			long start = System.currentTimeMillis();

			IUser user = new User("myadminid", "admin", null, new String[] { "ADMIN", "ALL" });
			testExpression(user);

			IUser user2 = new User("myuserid", "hans", "tarragon", new String[] {
			});
			testExpression(user2);

			System.out.println("SUCCESS (" + (System.currentTimeMillis() - start) + "ms)!");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static class Role implements IRole
	{
		private final String name;

		public Role(String roleName)
		{
			name = roleName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.security.IRole#getName()
		 */
		public String getName()
		{
			return this.name;
		}
	}

	private static class User extends AbstractUser
	{
		private String mandatorId;
		private String userId;
		private String loginId;
		private Collection roles;

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.security.impl.AbstractUser#fetchRoles()
		 */
		protected Iterator fetchRoles() throws GeneralSecurityException
		{
			return this.roles.iterator();
		}

		private User(String userId, String loginId, String mandatorId, String[] roles)
		{
			this.userId = userId;
			this.loginId = loginId;
			this.mandatorId = mandatorId;
			this.roles = new ArrayList(roles.length);
			for (int i = 0; i < roles.length; i++)
				this.roles.add(new Role(roles[i]));
      
      setProperty("myprop", "myvalue");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.security.IUser#getEMail()
		 */
		public String getEMail()
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.security.IUser#getFullName()
		 */
		public String getFullName()
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.security.IUser#getKey()
		 */
		public String getKey()
		{
			return this.userId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.security.IUser#getLoginId()
		 */
		public String getLoginId()
		{
			return this.loginId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.security.IUser#getMandatorId()
		 */
		public String getMandatorId()
		{
			return this.mandatorId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append("User[");
			buffer.append("mandatorId = ").append(mandatorId);
			buffer.append(", userId = ").append(userId);
			buffer.append(", loginId = ").append(loginId);
			buffer.append(", roles = ").append(roles);
			buffer.append("]");
			return buffer.toString();
		}
	}

	private static class TestApplication extends Application
	{

		/**
		 * @param user
		 * @param definition
		 * @throws Exception
		 */
		TestApplication(IUser user, IApplicationDefinition definition) throws Exception
		{
			super(/*user,*/ definition, "dummyid");
		}

	}

	private static class ApplicationInfo extends AbstractApplicationInfo
	{
		public static final String NAME = "testappl";
			/**
		 * @param name
		 * @param title
		 */
		public ApplicationInfo()
		{
			super(NAME, "testtitle", null);
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.impl.AbstractApplicationInfo#getDomainNames()
		 */
		public Iterator getDomainNames()
		{
			return Collections.EMPTY_LIST.iterator();
		}

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.impl.AbstractApplicationInfo#getEventHandler()
     */
    public String getEventHandler()
    {
      return null;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.impl.AbstractApplicationInfo#lookupEventHandlerByReference()
     */
    public boolean lookupEventHandlerByReference()
    {
      return false;
    }
}
	
	private static class Alias extends AbstractTableAlias
	{
			/**
		 * @param aliasName
		 * @param tableName
		 * @param condition
		 */
		public Alias(String aliasName)
		{
			super(aliasName, aliasName, null);
		}

}

	private static class Definition extends AbstractDefinition
	{
		public Definition()
		{
			this.add(new ApplicationInfo());
			
			this.add(new Alias("agent"));
			this.add(new Alias("callworkgroup"));
			this.add(new Alias("problemmanager"));
			this.add(new Alias("webqauthint"));
		}
	}

	private static class ApplicationDefinition extends AbstractApplicationDefinition
	{
		/**
		 * @param definition
		 * @param name
		 * @param version
		 */
		public ApplicationDefinition(AbstractDefinition definition)
		{
			super(definition, ApplicationInfo.NAME, Version.ENGINE);
		}

	}
}
