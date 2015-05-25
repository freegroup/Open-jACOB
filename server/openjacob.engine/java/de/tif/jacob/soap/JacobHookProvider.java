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
import org.apache.axis.Handler;
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
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.providers.java.RPCProvider;
import org.xml.sax.SAXException;
import de.tif.jacob.deployment.DeployEntry;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.deployment.DeployNotifyee;


public class JacobHookProvider extends RPCProvider
{
  
  public void generateWSDL(MessageContext msgContext) throws AxisFault
  {
    super.generateWSDL(msgContext);
  }

  protected Object makeNewServiceObject(MessageContext msgContext, String clsName) throws Exception
  {
    return msgContext.getProperty("jacobHook");
  }

  protected String getServiceClassName(Handler service)
  {
    MessageContext msgContext=  MessageContext.getCurrentContext();
    Object obj = msgContext.getProperty("jacobHook");
    
    return obj.getClass().getName();
  }

  protected Class getServiceClass(String clsName, SOAPService service, MessageContext msgContext) throws AxisFault
  {
    Object obj = msgContext.getProperty("jacobHook");
    return obj.getClass();
  }

  public void processMessage(MessageContext msgContext, SOAPEnvelope reqEnv, SOAPEnvelope resEnv, Object obj) throws Exception
  {
    super.processMessage(msgContext, reqEnv, resEnv, obj);
  }
  
  /*
  private synchronized void init(MessageContext msgContext)
  {
    try
    {
      JavaServiceDesc serviceDesc = new JavaServiceDesc();
      serviceDesc.setStyle(Style.RPC);
      serviceDesc.setUse(Use.ENCODED);
      serviceDesc.setImplClass(msgContext.getProperty("jacobHook").getClass());
      TypeMappingRegistryImpl typeMappingRegistry = new TypeMappingRegistryImpl();
      typeMappingRegistry.doRegisterFromVersion("1.3");
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
  */
}
