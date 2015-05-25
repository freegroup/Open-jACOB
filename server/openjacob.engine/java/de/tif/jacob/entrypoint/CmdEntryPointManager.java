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

package de.tif.jacob.entrypoint;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SystemContext;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.AuthenticationException;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.security.HttpAuthentificator;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;
import de.tif.jacob.security.UserManagement;

/**
* @author Andreas Herz
*
*/
public class CmdEntryPointManager extends HttpServlet
{
    static public final transient String RCS_ID = "$Id: CmdEntryPointManager.java,v 1.8 2010/07/06 12:59:33 freegroup Exp $";
    static public final transient String RCS_REV = "$Revision: 1.8 $";

    final static String SYS_ENTRYPOINT_PACKAGE = CmdEntryPointManager.class.getPackage().getName()+".cmd.";
    final static String APP_ENTRYPOINT_PACKAGE = "jacob.entrypoint.cmd.";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DataInputStream in = new DataInputStream(request.getInputStream());
        int length =request.getContentLength();
        byte[] body = new byte[length];
        in.readFully(body, 0, length);

        process(request, response, new String(body));
        in.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        process(request, response, null);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response, String body) throws ServletException, IOException
    {
        String userId            = request.getParameter("user");
        String passwd          = request.getParameter("pwd");
        String entryPoint      = request.getParameter("entry");
        String applicationId  = request.getParameter("app");
        String version           = request.getParameter("version");
        try
        {
            IApplicationDefinition appDef;
            if (version == null)
                appDef = DeployMain.getActiveApplication(applicationId);
            else
                appDef = DeployMain.getApplication(applicationId, version);
          
            // Get user factory
            IUserFactory userFactory = UserManagement.getUserFactory(appDef);
            
            // Set context for user factory calls
            Context.setCurrent(new SystemContext(appDef));
            
            // If no user id or password provided and the application's user factory is an HttpAuthenticator,
            // then login using that
            IUser user = null;
            if (userId == null || passwd == null)
            {
                if (userFactory instanceof HttpAuthentificator)
                {
                    try
                    {
                        user = ((HttpAuthentificator) userFactory).get(request, response);
                    }
                    catch (AuthenticationException ex)
                    {
                        return /*silently*/;
                    }
                    
                     if (user != null)
                       userId = user.getLoginId();
                 }
            }
            
            // support anonymous login if the application provides this
            //
            if(userId==null)
            {
              IUser anonymous = userFactory.getAnonymous();
              if(anonymous!=null)
              {
                user = anonymous;
                userId = user.getLoginId();
              }
            }
            
            // No user identification. We need at minimum the userId.
            //
            if (userId == null)
            {
                return /*silently*/;
            }

            // Check if there a valid session if the no passwd hands over.
            // A valid session is defined with:
            // The user in the session must be equals the user in the hands over parameter
            // The application in the session must be equals the application hands over the entry point.
            //
            HTTPClientSession jacobSession;
            if (user != null)
                jacobSession = HTTPClientSession.get(request.getSession(), appDef, user);
            else
                jacobSession = HTTPClientSession.get(request.getSession(), appDef, userId);

            // retrieve the user from session is the a valid jacobSession
            //
            if (jacobSession != null)
            {
                // Note: This might overwrite the possible HttpAuthenticator logged in user with the jacob session user
                user = jacobSession.getUser();
            }

            // try to get the user from the user management. It that case userId and passwd must be hands over.
            //
            if (user == null && passwd != null)
            {
              try
              {
                user = UserManagement.getValid(appDef.getName(), appDef.getVersion().toString(), userId, passwd);
              }
              catch (AuthenticationException ex)
              {
                return /*silently*/;
              }
            }

            // ohoh - you can't enter a EntryPoint without a valid user.
            //
            if (user == null)
                return /*silently*/;

            // Falls zuvor keine Session vorhanden war, kann diese jetzt angelegt werden
            // ....alle benötigten Informationen sind jetzt vorhanden
            if (jacobSession == null)
                jacobSession = new ClientSession(request, user, appDef);

            Properties props = new Properties();
            // can be overriden by a user parameter.
            if (body != null)
                props.put("body", body);

            Enumeration e = request.getParameterNames();
            while (e.hasMoreElements())
            {
                String element = (String)e.nextElement();
                props.put(element, request.getParameter(element));
            }

            // try to find a SYSTEM implementation of the entry point
            //
            ICmdEntryPoint entry = (ICmdEntryPoint)ClassProvider.getInstance(appDef, SYS_ENTRYPOINT_PACKAGE + entryPoint);

            // ..if no system internal implementation available try to load the APPLICATION
            // implementation of the entry point.
            //
            if (entry == null)
                entry = (ICmdEntryPoint)ClassProvider.getInstance(appDef, APP_ENTRYPOINT_PACKAGE + entryPoint);

            // No entry point found. Return silently. Do not provide any information to the requester.
            // A silently return complicate a hacker attack. The hacker can't interpret the return code.
            //
            if (entry == null)
                return /*silently*/;

            // call the entry point hook
            //
            CmdEntryPointContext context = new CmdEntryPointContext(jacobSession, response,appDef, user);
            Context.setCurrent(context);
            response.setContentType(entry.getMimeType(context, props));
            entry.enter(context, props);
        }
        catch (Exception e)
        {
            ExceptionHandler.handle(e);
        }
        finally
        {
            Context.setCurrent(null);
        }
    }
}
