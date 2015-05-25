/*
 * Created on 27.09.2010
 *
 */
package de.tif.jacob.screen;

/**
 * An implementation of ILinkRenderStrategy transforms a link target (e.g. email address) into a proper link 
 * in the context of the current client implementation (HTML, swing,....
 * 
 * @since 2.10
 */
public interface ILinkRenderStrategy
{
   /**
    *  Uses the link and label to build a proper  link.
    *  
    * @param linkTarget
    * @param linkLabel
    * @return
   * @throws Exception 
    * @since 2.10
    */
    public String buildLink(IClientContext context, IGuiElement target, String link, String label) throws Exception;
    
    public void decorateLink(IClientContext context, IGuiElement target, String link,  ILinkLabel label) throws Exception;
}
