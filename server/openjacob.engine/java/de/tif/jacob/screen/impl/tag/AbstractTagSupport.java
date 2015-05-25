/*
 * Created on 20.05.2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.screen.impl.tag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTagSupport extends TagSupport
{
  static protected final Log logger = LogFactory.getLog(AbstractTagSupport.class);

  protected void handleStartTagException(Exception exc) throws JspTagException
  {
    logger.warn("doStartTag() failed", exc);

    throw new JspTagException(exc.getMessage());
  }
}
