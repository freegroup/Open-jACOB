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
 * 
 * @since 2.10
 */
class URLRedirectRule extends AbstractRule
{
  static protected final transient Log logger = LogFactory.getLog(URLForwardRule.class);
  public boolean encodeUrl = false;
  public boolean entireUrl = false;

  public URLRedirectRule(IApplicationDefinition appDef, String urlPattern, String target)
  {
    super(appDef, urlPattern, target);
  }

  public void process(HttpServletRequest request, HttpServletResponse response,ServletContext servletContext) throws ServletException, IOException
  {
    /*
     *         String target = rule.getTarget();
        if (matcher.groupCount() > 0)
        {
          for (int j = 1; j <= matcher.groupCount(); j++)
            target = target.replaceAll("\\$" + String.valueOf(j), matcher.group(j));
        }
        
*/
    String finalURL = getFinalURL(request, response, target);
    response.sendRedirect(finalURL);
    
    if (logger.isDebugEnabled())
      logger.debug("Redirect '" + target + "' to '" + finalURL + "'");
  }
}