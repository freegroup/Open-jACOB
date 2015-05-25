package de.tif.jacob.urlredirect;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.definition.IApplicationDefinition;

/**
 * @since 2.10
 */
class URLForwardRule extends AbstractRule
{
  static protected final transient Log logger = LogFactory.getLog(URLForwardRule.class);
   
  public URLForwardRule(IApplicationDefinition appDef, String urlPattern, String target)
  {
    super(appDef, urlPattern, target);
  }
  
  public void process(HttpServletRequest request, HttpServletResponse response,ServletContext servletContext) throws ServletException, IOException
  {
    String finalURL = getFinalURL(request, response,  target);
    request.getRequestDispatcher(target).forward(request, response);
    
    if(logger.isDebugEnabled())
      logger.debug("Forwarded '" +target + "' to '" + finalURL + "'");

    System.out.println("Forwarded '" +target + "' to '" + finalURL + "'");
}
}