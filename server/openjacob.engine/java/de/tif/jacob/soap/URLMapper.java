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

package de.tif.jacob.soap;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.Constants;
import org.apache.axis.MessageContext;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.JavaServiceDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.SerializerFactory;
import org.apache.axis.encoding.TypeMappingRegistryImpl;
import org.apache.axis.encoding.ser.BaseDeserializerFactory;
import org.apache.axis.encoding.ser.BaseSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.UserManagement;

/**
 * An <code>URLMapper</code> attempts to use the extra path info of this
 * request as the service name.
 * 
 */
public class URLMapper extends BasicHandler
{
  static public transient final String RCS_ID = "$Id: URLMapper.java,v 1.2 2009/11/19 13:42:04 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  public final static String SOAP_PACKAGE  = "jacob.entrypoint.soap.";

  public URLMapper()
  {
    
  }
  
  public void invoke(MessageContext msgContext) throws AxisFault
  {
    setService(msgContext);
    IApplicationDefinition appDef = (IApplicationDefinition)msgContext.getProperty("appDef");
		// No user identification. We need at minimum the userId.
		//
		if(msgContext.getUsername()==null)
     throw new AxisFault("Invalid username/password combination for application ["+appDef+"]");

		IUser user=null;
		try
    {
      // try to get the user from the user management. It that case userId and passwd must be hands over.
      //
      user =UserManagement.getValid(appDef.getName(), appDef.getVersion().toString(), msgContext.getUsername(),msgContext.getPassword());
    }
    catch (Exception e)
    {
      throw new AxisFault(e.getMessage());
    }

    if(user==null)
      throw new AxisFault("Invalid username/password combination for application ["+appDef+"]");
    SOAPSession jacobSession  = new SOAPSession(appDef, user);
    SOAPContext context=new SOAPContext(jacobSession,appDef,user);
    Context.setCurrent(context);
  }

  public void generateWSDL(MessageContext msgContext) throws AxisFault
  {
    setService(msgContext);
  }
  
  private void setService(MessageContext msgContext) throws AxisFault
  {
    String path = (String) msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETPATHINFO);
    if ((path != null) && (path.length() >= 1))
    { 
      // path may or may not start with a "/".
      if (path.startsWith("/"))
        path = path.substring(1); //chop the extra "/"
      // determine the jacob application name and SOAP handler
      //
      String[] names= path.split("/");
      
      // wrong parameter....maybe it is another SOAP service...
      //
      if(names.length!=2)
        return;
      
      String appName     = names[0];
      String soapHandler = names[1];

      IApplicationDefinition appDef =DeployMain.getActiveApplication(appName);
      
       
      // ..if no system internal implementation available try to load the APPLICATION 
      // implementation of the entry point.
      //
      SOAPEntryPoint entry = (SOAPEntryPoint)ClassProvider.getInstance(appDef, SOAP_PACKAGE+soapHandler);

      if(entry==null)
        throw new AxisFault("Unable to locate SOAP handler for ["+soapHandler+"]");

      msgContext.setProperty("jacobHook", entry);
      msgContext.setProperty("appDef",appDef);
      msgContext.setTargetService("jacob");

      try
      {
        JavaServiceDesc serviceDesc = new JavaServiceDesc();
        serviceDesc.setStyle(Style.RPC);
        serviceDesc.setUse(Use.ENCODED);
        serviceDesc.setImplClass(msgContext.getProperty("jacobHook").getClass());
        TypeMappingRegistryImpl typeMappingRegistry = new TypeMappingRegistryImpl();
        // Default version is "1.3"
        String typeMappingVersion = Property.SOAP_TYPE_MAPPING_VERSION.getValue();
        typeMappingRegistry.doRegisterFromVersion(typeMappingVersion);
        org.apache.axis.encoding.TypeMapping typeMapping =  typeMappingRegistry.getOrMakeTypeMapping(Use.ENCODED_STR);

        serviceDesc.setTypeMappingRegistry(typeMappingRegistry);
        serviceDesc.setTypeMapping(typeMapping);

        SerializerFactory ser =  BaseSerializerFactory.createFactory(SimpleSerializerFactory.class, String.class, new QName(Constants.URI_2001_SCHEMA_XSD, "string"));
        DeserializerFactory deser =   BaseDeserializerFactory.createFactory(SimpleDeserializerFactory.class,  String.class, new QName(Constants.URI_2001_SCHEMA_XSD, "string"));
        typeMapping.register(String.class, new QName(Constants.URI_2001_SCHEMA_XSD, "string"), ser, deser);
   
      //  msgContext.setOperation(null);
        if(msgContext.getOperation()!=null)
          msgContext.getOperation().setParent(serviceDesc);
        if(msgContext.getService()!=null)
          msgContext.getService().setServiceDescription(serviceDesc);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
