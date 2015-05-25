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
package de.tif.jacob.soap.test;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SPClient 
{

		public static void main(String[] args) 
		{
			try 
			{
				Service service = new org.apache.axis.client.Service();
				Call call = (Call) service.createCall();

				call.setTargetEndpointAddress("http://localhost:8070/jacob/services/tutorial/AchimTest");

				call.setOperationName(new QName("echo"));
			  call.addParameter("value", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			  
				call.setUsername("guest");
				call.setPassword("");
				call.setReturnType(Constants.XSD_STRING);
			
				 Object result = call.invoke(new Object[]{"Dies ist mein Wert"});
				 System.out.println( "Antwort vom Server: " + result);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.err.println(e.toString());
			}
		}
}