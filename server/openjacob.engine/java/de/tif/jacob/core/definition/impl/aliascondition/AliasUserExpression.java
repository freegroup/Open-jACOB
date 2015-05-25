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
import java.util.Iterator;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.impl.sql.SQL;
import de.tif.jacob.core.definition.IDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.security.IRole;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AliasUserExpression extends AliasExpression
{
  static public transient final String RCS_ID = "$Id: AliasUserExpression.java,v 1.5 2009/12/14 12:00:56 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";

  public static final AliasUserExpression ROLES = new AliasUserRolesExpression();

  public static final AliasUserExpression USER_ID = new AliasUserIdExpression(false);
  
  public static final AliasUserExpression QUOTED_USER_ID = new AliasUserIdExpression(true);
  
  public static final AliasUserExpression LOGIN_ID = new AliasLoginIdExpression();

  /**
   * The application name wildcard.
   * 
   * @since 2.9
   */
  public static final AliasUserExpression APPLICATION_NAME = new AliasApplicationNameExpression();

  public static final AliasUserExpression MANDATOR_ID = new AliasMandatorIdExpression(false);
  
  public static final AliasUserExpression QUOTED_MANDATOR_ID = new AliasMandatorIdExpression(true);
  
	/**
	 *  
	 */
	private AliasUserExpression()
	{
		// nothing more to do
	}

	private static IUser getUser()
	{
		return Context.getCurrent().getUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.impl.alias.AliasExpression#isDynamic()
	 */
	public boolean isDynamic()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasElement#childrenPostProcessing()
	 */
	protected void childrenPostProcessing(IDefinition definition, ITableAlias alias)
	{
    // nothing more to do
	}

  private static class AliasUserRolesExpression extends AliasUserExpression
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.impl.alias.AliasElement#isObsolete()
		 */
		public boolean isObsolete()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#print(de.tif.jacob.core.definition.impl.aliascondition.AliasConditionResult)
		 */
		protected void print(AliasConditionResult result)
		{
      result.writer.print("(");
      try
      {
        Iterator roles = getUser().getRoles();
        if (roles == null || !roles.hasNext())
        {
          // TODO: hack to avoid empty list
          result.writer.print("'DEFAULT'");
        }
        else
        {
          result.writer.print(SQL.convertToSQL(((IRole) roles.next()).getName(), true));
          while (roles.hasNext())
          {
            result.writer.print(",");
            result.writer.print(SQL.convertToSQL(((IRole) roles.next()).getName(), true));
          }
        }
      }
      catch (GeneralSecurityException ex)
      {
      	throw new RuntimeException("Fetching roles failed", ex);
      }
      result.writer.print(")");
    }
	}

	private static class AliasUserIdExpression extends AliasUserExpression
	{
    private final boolean doQuote;
    
    private AliasUserIdExpression(boolean doQuote)
    {
    	this.doQuote = doQuote; 
    }
    
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.impl.alias.AliasElement#isObsolete()
		 */
		public boolean isObsolete()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#print(de.tif.jacob.core.definition.impl.aliascondition.AliasConditionResult)
		 */
		protected void print(AliasConditionResult result)
		{
			result.writer.print(SQL.convertToSQL(getUser().getKey(), this.doQuote));
		}
	}

  private static class AliasLoginIdExpression extends AliasUserExpression
  {
    public boolean isObsolete()
    {
      return getUser().getLoginId() == null;
    }

    protected void print(AliasConditionResult result)
    {
      result.writer.print(SQL.convertToSQL(getUser().getLoginId(), true));
    }
  }

  private static class AliasApplicationNameExpression extends AliasUserExpression
  {
    public boolean isObsolete()
    {
      return false;
    }

    protected void print(AliasConditionResult result)
    {
      result.writer.print(SQL.convertToSQL(Context.getCurrent().getApplicationDefinition().getName(), true));
    }
  }

  private static class AliasMandatorIdExpression extends AliasUserExpression
  {
    private final boolean doQuote;
    
    private AliasMandatorIdExpression(boolean doQuote)
    {
      this.doQuote = doQuote; 
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.definition.impl.alias.AliasElement#isObsolete()
     */
    public boolean isObsolete()
    {
      return getUser().getMandatorId() == null;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#print(de.tif.jacob.core.definition.impl.aliascondition.AliasConditionResult)
     */
    protected void print(AliasConditionResult result)
    {
      result.writer.print(SQL.convertToSQL(getUser().getMandatorId(), this.doQuote));
    }
  }
  
  protected static AliasUserExpression getUserPropertyExpression(String property, boolean doQuote)
  {
    return new AliasUserPropertyExpression(property, doQuote);
  }

  private static class AliasUserPropertyExpression extends AliasUserExpression
  {
    private final String propertyName;
    private final boolean doQuote;
    
    private AliasUserPropertyExpression(String propertyName, boolean doQuote)
    {
      this.propertyName = propertyName;
      this.doQuote = doQuote;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.definition.impl.alias.AliasElement#isObsolete()
     */
    public boolean isObsolete()
    {
      return getUser().getProperty(this.propertyName) == null;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#print(de.tif.jacob.core.definition.impl.aliascondition.AliasConditionResult)
     */
    protected void print(AliasConditionResult result)
    {
      if (this.doQuote)
        result.writer.print(SQL.convertToSQL(getUser().getProperty(this.propertyName).toString(), this.doQuote));
      else
        result.writer.print(getUser().getProperty(this.propertyName));
    }
  }

}
