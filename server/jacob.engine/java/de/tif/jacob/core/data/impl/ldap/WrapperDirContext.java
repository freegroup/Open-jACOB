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

package de.tif.jacob.core.data.impl.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import de.tif.jacob.core.data.impl.sql.SQLMonitor;

/**
 * Wrapper class for directory contextes.
 * 
 * @author Andreas Sonntag
 */
public final class WrapperDirContext implements DirContext
{
  private DirContext embedded;
  private final LdapDataSource dataSource;
  
  protected WrapperDirContext(LdapDataSource dataSource, DirContext embedded)
  {
    this.embedded = embedded;
    this.dataSource = dataSource;
  }
  
	private void checkClosed() throws NamingException
	{
		if (this.embedded == null)
			throw new NamingException("Context is closed");
	}

    /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#bind(javax.naming.Name, java.lang.Object, javax.naming.directory.Attributes)
   */
  public void bind(Name name, Object obj, Attributes attrs) throws NamingException
  {
    checkClosed();
    this.embedded.bind(name, obj, attrs);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#bind(java.lang.String, java.lang.Object, javax.naming.directory.Attributes)
   */
  public void bind(String name, Object obj, Attributes attrs) throws NamingException
  {
    checkClosed();
    this.embedded.bind(name, obj, attrs);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#createSubcontext(javax.naming.Name, javax.naming.directory.Attributes)
   */
  public DirContext createSubcontext(Name name, Attributes attrs) throws NamingException
  {
    checkClosed();
    return new WrapperDirContext(this.dataSource, this.embedded.createSubcontext(name, attrs));
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#createSubcontext(java.lang.String, javax.naming.directory.Attributes)
   */
  public DirContext createSubcontext(String name, Attributes attrs) throws NamingException
  {
    checkClosed();
    return new WrapperDirContext(this.dataSource, this.embedded.createSubcontext(name, attrs));
  }

  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name, java.lang.String[])
   */
  public Attributes getAttributes(Name name, String[] attrIds) throws NamingException
  {
    checkClosed();
    return this.embedded.getAttributes(name, attrIds);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name)
   */
  public Attributes getAttributes(Name name) throws NamingException
  {
    checkClosed();
    return this.embedded.getAttributes(name);
  }
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getAttributes(java.lang.String, java.lang.String[])
   */
  public Attributes getAttributes(String name, String[] attrIds) throws NamingException
  {
    checkClosed();
    return this.embedded.getAttributes(name, attrIds);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getAttributes(java.lang.String)
   */
  public Attributes getAttributes(String name) throws NamingException
  {
    checkClosed();
    return this.embedded.getAttributes(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getSchema(javax.naming.Name)
   */
  public DirContext getSchema(Name name) throws NamingException
  {
    checkClosed();
    return new WrapperDirContext(this.dataSource, this.embedded.getSchema(name));
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getSchema(java.lang.String)
   */
  public DirContext getSchema(String name) throws NamingException
  {
    checkClosed();
    return new WrapperDirContext(this.dataSource, this.embedded.getSchema(name));
  }

  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getSchemaClassDefinition(javax.naming.Name)
   */
  public DirContext getSchemaClassDefinition(Name name) throws NamingException
  {
    checkClosed();
    return new WrapperDirContext(this.dataSource, this.embedded.getSchemaClassDefinition(name));
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#getSchemaClassDefinition(java.lang.String)
   */
  public DirContext getSchemaClassDefinition(String name) throws NamingException
  {
    checkClosed();
    return new WrapperDirContext(this.dataSource, this.embedded.getSchemaClassDefinition(name));
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name, int, javax.naming.directory.Attributes)
   */
  public void modifyAttributes(Name name, int mod_op, Attributes attrs) throws NamingException
  {
    checkClosed();
    this.embedded.modifyAttributes(name, mod_op, attrs);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name, javax.naming.directory.ModificationItem[])
   */
  public void modifyAttributes(Name name, ModificationItem[] mods) throws NamingException
  {
    checkClosed();
    this.embedded.modifyAttributes(name, mods);
  }

  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String, int, javax.naming.directory.Attributes)
   */
  public void modifyAttributes(String name, int mod_op, Attributes attrs) throws NamingException
  {
    checkClosed();
    this.embedded.modifyAttributes(name, mod_op, attrs);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String, javax.naming.directory.ModificationItem[])
   */
  public void modifyAttributes(String name, ModificationItem[] mods) throws NamingException
  {
    checkClosed();
    this.embedded.modifyAttributes(name, mods);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#rebind(javax.naming.Name, java.lang.Object, javax.naming.directory.Attributes)
   */
  public void rebind(Name name, Object obj, Attributes attrs) throws NamingException
  {
    checkClosed();
    this.embedded.rebind(name, obj, attrs);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#rebind(java.lang.String, java.lang.Object, javax.naming.directory.Attributes)
   */
  public void rebind(String name, Object obj, Attributes attrs) throws NamingException
  {
    checkClosed();
    this.embedded.rebind(name, obj, attrs);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(javax.naming.Name, javax.naming.directory.Attributes, java.lang.String[])
   */
  public NamingEnumeration search(Name name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException
  {
    checkClosed();
    return this.embedded.search(name, matchingAttributes, attributesToReturn);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(javax.naming.Name, javax.naming.directory.Attributes)
   */
  public NamingEnumeration search(Name name, Attributes matchingAttributes) throws NamingException
  {
    checkClosed();
    return this.embedded.search(name, matchingAttributes);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(javax.naming.Name, java.lang.String, java.lang.Object[], javax.naming.directory.SearchControls)
   */
  public NamingEnumeration search(Name name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException
  {
    checkClosed();
    return this.embedded.search(name, filterExpr, filterArgs, cons);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(javax.naming.Name, java.lang.String, javax.naming.directory.SearchControls)
   */
  public NamingEnumeration search(Name name, String filter, SearchControls cons) throws NamingException
  {
    checkClosed();
    long start = System.currentTimeMillis();
    NamingEnumeration result = this.embedded.search(name, filter, cons);
    SQLMonitor.log(SQLMonitor.QUERY_LOG_TYPE, start, this.dataSource, "LDAP: name=" + name + " filter=" + filter);
    return result;
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(java.lang.String, javax.naming.directory.Attributes, java.lang.String[])
   */
  public NamingEnumeration search(String name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException
  {
    checkClosed();
    return this.embedded.search(name, matchingAttributes, attributesToReturn);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(java.lang.String, javax.naming.directory.Attributes)
   */
  public NamingEnumeration search(String name, Attributes matchingAttributes) throws NamingException
  {
    checkClosed();
    return this.embedded.search(name, matchingAttributes);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(java.lang.String, java.lang.String, java.lang.Object[], javax.naming.directory.SearchControls)
   */
  public NamingEnumeration search(String name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException
  {
    checkClosed();
    return this.embedded.search(name, filterExpr, filterArgs, cons);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.directory.DirContext#search(java.lang.String, java.lang.String, javax.naming.directory.SearchControls)
   */
  public NamingEnumeration search(String name, String filter, SearchControls cons) throws NamingException
  {
    checkClosed();

    long start = System.currentTimeMillis();
    NamingEnumeration result = this.embedded.search(name, filter, cons);
    SQLMonitor.log(SQLMonitor.QUERY_LOG_TYPE, start, this.dataSource, "LDAP: name=" + name + " filter=" + filter);
    return result;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.naming.Context#addToEnvironment(java.lang.String,
   *      java.lang.Object)
   */
  public Object addToEnvironment(String propName, Object propVal) throws NamingException
  {
    checkClosed();
    return this.embedded.addToEnvironment(propName, propVal);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
   */
  public void bind(Name name, Object obj) throws NamingException
  {
    checkClosed();
    this.embedded.bind(name, obj);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
   */
  public void bind(String name, Object obj) throws NamingException
  {
    checkClosed();
    this.embedded.bind(name, obj);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#close()
   */
  public void close() throws NamingException
  {
		// already closed?
		if (this.embedded == null)
			return;

		this.embedded.close();
		this.embedded = null;
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#composeName(javax.naming.Name, javax.naming.Name)
   */
  public Name composeName(Name name, Name prefix) throws NamingException
  {
    checkClosed();
    return this.embedded.composeName(name, prefix);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
   */
  public String composeName(String name, String prefix) throws NamingException
  {
    checkClosed();
    return this.embedded.composeName(name, prefix);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#createSubcontext(javax.naming.Name)
   */
  public Context createSubcontext(Name name) throws NamingException
  {
    checkClosed();
    return this.embedded.createSubcontext(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#createSubcontext(java.lang.String)
   */
  public Context createSubcontext(String name) throws NamingException
  {
    checkClosed();
    return this.embedded.createSubcontext(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
   */
  public void destroySubcontext(Name name) throws NamingException
  {
    checkClosed();
    this.embedded.destroySubcontext(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#destroySubcontext(java.lang.String)
   */
  public void destroySubcontext(String name) throws NamingException
  {
    checkClosed();
    this.embedded.destroySubcontext(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#getEnvironment()
   */
  public Hashtable getEnvironment() throws NamingException
  {
    checkClosed();
    return this.embedded.getEnvironment();
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#getNameInNamespace()
   */
  public String getNameInNamespace() throws NamingException
  {
    checkClosed();
    return this.embedded.getNameInNamespace();
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#getNameParser(javax.naming.Name)
   */
  public NameParser getNameParser(Name name) throws NamingException
  {
    checkClosed();
    return this.embedded.getNameParser(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#getNameParser(java.lang.String)
   */
  public NameParser getNameParser(String name) throws NamingException
  {
    checkClosed();
    return this.embedded.getNameParser(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#list(javax.naming.Name)
   */
  public NamingEnumeration list(Name name) throws NamingException
  {
    checkClosed();
    return this.embedded.list(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#list(java.lang.String)
   */
  public NamingEnumeration list(String name) throws NamingException
  {
    checkClosed();
    return this.embedded.list(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#listBindings(javax.naming.Name)
   */
  public NamingEnumeration listBindings(Name name) throws NamingException
  {
    checkClosed();
    return this.embedded.listBindings(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#listBindings(java.lang.String)
   */
  public NamingEnumeration listBindings(String name) throws NamingException
  {
    checkClosed();
    return this.embedded.listBindings(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#lookup(javax.naming.Name)
   */
  public Object lookup(Name name) throws NamingException
  {
    checkClosed();
    return this.embedded.lookup(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#lookup(java.lang.String)
   */
  public Object lookup(String name) throws NamingException
  {
    checkClosed();
    return this.embedded.lookup(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#lookupLink(javax.naming.Name)
   */
  public Object lookupLink(Name name) throws NamingException
  {
    checkClosed();
    return this.embedded.lookupLink(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#lookupLink(java.lang.String)
   */
  public Object lookupLink(String name) throws NamingException
  {
    checkClosed();
    return this.embedded.lookupLink(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
   */
  public void rebind(Name name, Object obj) throws NamingException
  {
    checkClosed();
    this.embedded.rebind(name, obj);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
   */
  public void rebind(String name, Object obj) throws NamingException
  {
    checkClosed();
    this.embedded.rebind(name, obj);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
   */
  public Object removeFromEnvironment(String propName) throws NamingException
  {
    checkClosed();
    return this.embedded.removeFromEnvironment(propName);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
   */
  public void rename(Name oldName, Name newName) throws NamingException
  {
    checkClosed();
    this.embedded.rename(oldName, newName);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
   */
  public void rename(String oldName, String newName) throws NamingException
  {
    checkClosed();
    this.embedded.rename(oldName, newName);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#unbind(javax.naming.Name)
   */
  public void unbind(Name name) throws NamingException
  {
    checkClosed();
    this.embedded.unbind(name);
  }
  
  /* (non-Javadoc)
   * @see javax.naming.Context#unbind(java.lang.String)
   */
  public void unbind(String name) throws NamingException
  {
    checkClosed();
    this.embedded.unbind(name);
  }
}
