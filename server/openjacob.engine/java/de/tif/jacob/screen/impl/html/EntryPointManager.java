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

package de.tif.jacob.screen.impl.html;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SystemContext;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.InvalidApplicationException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.HTTPApplication;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.tag.BrowserType;
import de.tif.jacob.security.HttpAuthentificator;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;
import de.tif.jacob.security.UserManagement;
import de.tif.jacob.util.Base64;
import de.tif.jacob.util.StringUtil;

/**
* @author Andreas Herz
*
*/
public class EntryPointManager extends HttpServlet
{
    static public final transient String RCS_ID = "$Id: EntryPointManager.java,v 1.9 2010/10/15 11:18:00 ibissw Exp $";
    static public final transient String RCS_REV = "$Revision: 1.9 $";

    public static void main(String[] args) throws Exception
    {
        // L2phY29iL2VudGVyP2VudHJ5PU5ld0NhbGwmYXBwPWN1c3RpbmZv
        System.out.println(new String(Base64.decode("L2phY29iL2VudGVyP2VudHJ5PU5ld0NhbGwmYXBwPWN1c3RpbmZv")));
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setHeader("Cache-Control","no-cache");
        response.setContentType("text/html");
        Writer w = new OutputStreamWriter(response.getOutputStream());
        try
        {
            String userId         = request.getParameter("user");
            String passwd         = request.getParameter("pwd");
            String entryPoint     = request.getParameter("entry");
            String applicationId  = request.getParameter("app");
            String version        = request.getParameter("version");
            String browserId      = request.getParameter("browser");

            // no version hands over. Get the default version for the
            // application.
            //
            IApplicationDefinition applicationDef;
            if (version == null)
            {
                applicationDef = DeployMain.getActiveApplication(applicationId);
            }
            else
                applicationDef = DeployMain.getApplication(applicationId, version);

            // Get user factory
            IUserFactory userFactory = UserManagement.getUserFactory(applicationDef);
            
            // Set context for user factory calls
            Context.setCurrent(new SystemContext(applicationDef));
            
            // If no user id or password provided and the application's user factory is an HttpAuthenticator,
            // then login using that
            //
            IUser user = null;
            if (userId == null || passwd == null)
            {
                if (userFactory instanceof HttpAuthentificator)
                {
                    user = ((HttpAuthentificator)userFactory).get(request, response);
                    if (user != null)
                    {
                        userId = user.getLoginId();
                    }
                }
            }
            
            // we might have already a jacob session 
            //
            HTTPClientSession jacobSession = HTTPClientSession.get(request.getSession(), browserId);
            if (jacobSession == null)
            {
              if (user != null)
                  jacobSession = HTTPClientSession.get(request.getSession(), applicationDef, user);
              else
                  jacobSession = HTTPClientSession.get(request.getSession(), applicationDef, userId);
            }
            else
            {
              IUser userToCheck = null;
              if (user != null)
                userToCheck = user;
              else if (userId != null)
              {
                try
                {
                  // find user because the userId might be treated case insensitive by the
                  // user factory of the application
                  userToCheck = userFactory.findUser(userId);
                }
                catch (UserNotExistingException ex)
                {
                  // ignore
                }
              }
              
              // The user has a session and applications instance. It is NOT the requested application.
              // OR the user of the session is not the hands over user.
              //
              // ->Redirect to the login screen. The loginscreen automatically logout the user and enforce an new login
              // Anmerkung: Falls eine userId übergeben wird, dann muss diese identisch mit der Session.userId sein.
              //
              if (!jacobSession.getApplicationDefinition().equals(applicationDef) || //
                  (userToCheck != null && !jacobSession.getUser().getLoginId().equals(userToCheck.getLoginId())))
              {
                  w.write("<html><head><META id=failpoint03 HTTP-EQUIV=Refresh CONTENT=\"0; URL=login.jsp?fromUrl=");
                  w.write(getFromUrl(request));
                  w.write("&app=");
                  w.write(applicationId);
                  if (version != null)
                  {
                    w.write("&version=");
                    w.write(version);
                  }
                  w.write("&user=");
                  w.write(StringUtil.toSaveString(userId));
                  w.write("\"></head></html>");
                  w.close();
                  return;
              }
            }

            // retrieve the user from session is the a valid jacobSession
            //
            if (jacobSession != null)
            {
                // Note: This might overwrite the possible HttpAuthenticator logged in user with the jacob session user
                user = jacobSession.getUser();
            }
            
            // No user or no password and no jacob user. Redirect to the login screen.
            // We need the user AND the password to create a valid jacobSession.
            //
            if((userId==null || passwd==null) && user==null)
            {
              w.write("<html><head><META id=failpoint02 HTTP-EQUIV=Refresh CONTENT=\"0; URL=login.jsp?fromUrl=");
              w.write(getFromUrl(request));
              w.write("&app=");
              w.write(applicationId);
              if (version != null)
              {
                w.write("&version=");
                w.write(version);
              }
              w.write("&user=");
              w.write(StringUtil.toSaveString(userId));
              w.write("\"></head></html>");
              w.close();
              return;
            }

            Properties props = new Properties();
            Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements())
            {
                String element = (String)enumeration.nextElement();
                props.put(element,request.getParameter(element));
            }

            if (jacobSession == null)
            {
                if (user == null)
                {
                    user = UserManagement.getValid(applicationDef.getName(), applicationDef.getVersion().toString(), userId, passwd);
                }
                jacobSession = new ClientSession(request, user, applicationDef);
                jacobSession.register(request.getSession());
            }

            // If the user has been redirected to the login page and has successfully logged in,
            // we already have an application object.
            HTTPApplication app = (HTTPApplication)jacobSession.getApplication(browserId);

            if (app == null)
                app = jacobSession.createApplication();
            if (app == null)
                throw new InvalidApplicationException("No valid application found.");

            IGuiEntryPoint entry = (IGuiEntryPoint)ClassProvider.getInstance(app.getApplicationDefinition(), "jacob.entrypoint.gui." + entryPoint);
            if (entry == null)
            {
                w.write("<html><head><META id=failpoint04 HTTP-EQUIV=Refresh CONTENT=\"0; URL=invalidEnryPoint.jsp\"></head></html>");
            }
            else
            {
                ClientContext context = new ClientContext(user, w, app, app.getHTTPApplicationId());
                Context.setCurrent(context);

                IGuiElement element = app.findByName(entry.getDomain());
                if (!(element instanceof IDomain))
                    throw new UserException(new CoreMessage("UNKNOWN_DOMAIN", entry.getDomain()));
                IDomain domain = (IDomain) element;

                element = app.findByName(entry.getForm());
                if (!(element instanceof Form))
                    throw new UserException(new CoreMessage("UNKNOWN_FORM", entry.getForm()));
                Form form = (Form) element;

                context.setDomain(domain);
                context.setForm(form);

                domain.setCurrentForm(context,form);

                // call the entry point hook
                //
                entry.enter(context, props);

                app.setToolbarVisible(entry.hasToolbar());
                app.setSearchBrowserVisible(entry.hasSearchBrowser());
                app.setNavigationVisible(entry.hasNavigation());

                app.setActiveDomain(context, domain);

                // move message boxes and other HTML stuff to the next page request
                // reason: It is possible that he application programmes has called a IMessageBox.
                //         This HTMLCode will be lost if we don't transfer them to the next page request cycle.
                //
                jacobSession.addAsynchronHtml(context.getAdditionalHtml());
                if (jacobSession instanceof ClientSession)
                   ((ClientSession) jacobSession).addAsynchronOnLoadJavaScript(context.getOnLoadJavascript());
                String clientType = BrowserType.getType(request);
                w.write("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=" + clientType + "_index.jsp?browser=" + app.getHTTPApplicationId() + "\"></head></html>");
            }
        }
        catch (Throwable e)
        {
            ExceptionHandler.handle(e);
            w.write("<html><head></head><body>" + e.toString() + "</body></html>");
        }
        w.close();
    }
    
    private static String getFromUrl(HttpServletRequest request)
    {
      String base64Url = request.getRequestURI()+"?"+request.getQueryString();
      return Base64.encode(base64Url.getBytes());
    }
}
