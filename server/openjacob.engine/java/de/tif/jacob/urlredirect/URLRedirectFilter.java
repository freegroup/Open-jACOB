package de.tif.jacob.urlredirect;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Einfacher URL Redirect oder Forward.
 * Entweder die Anfrage wird an ein anderes Servelet weitergeleitet (Forward) oder
 * es wird an redirekt an den Client gesendet (redirect)
 * 
 * Einmal sieht dies der Client nicht und beim redirect ändert sich auch die URL-Leiste im Browser.
 * 
 * @since 2.10
 */
public class URLRedirectFilter implements Filter
{
  protected String filterName = "jACOB-URL-Rewrite";
  
  /*
   * FilterConfig object we shall need for logging.
   */
  protected FilterConfig filterConfig;

  /*
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig config) throws ServletException
  {
    filterConfig = config;
  }

  /*
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // Should I do something here?
  }

  /*
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
   * javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    AbstractRule[] rules=RedirectManager.getRules();
    
    String matchMe = getRequestURI(httpRequest);
    if(httpRequest.getQueryString()!=null)
      matchMe = matchMe+"?"+httpRequest.getQueryString();

    for (int i = 0; i < rules.length; i++)
    {
      AbstractRule rule = rules[i];
 
      if (rule.matches(matchMe))
      {
        rule.process( httpRequest, httpResponse, this.filterConfig.getServletContext());
        return;
      }
    }
    chain.doFilter(request, response);
  }


  /**
   * Returns the URI the client used to access the specified resource. If the
   * filter operates context-aware, the URI will have the context path stripped
   * off, so the rules can be written in a manner which works no matter where
   * the filter is deployed.
   * 
   * @param request
   *          : HttpServletRequest object for this request.
   * 
   * @return: The request URI, possibly adapted to support context independence.
   */
  protected String getRequestURI(HttpServletRequest request)
  {
    String uri = request.getRequestURI();
    String ctxPath = request.getContextPath();
    
    if (uri.startsWith(ctxPath))
      uri = uri.substring(ctxPath.length());
    
    return uri;
  }  
  
}
