/*
 * Created on 27.09.2010
 *
 */
package de.tif.jacob.screen;

public  abstract class ILinkParser
{
  /**
   * Parses the text and changes it according to the provided {@link ILinkRenderStrategy} implementations
   * 
   * @param text
   * @return
   * @throws Exception 
   * @since 2.10
   */
  public  abstract String parse(IClientContext context,  IGuiElement target, String text) throws Exception;
  
  /**
   * Returns the render strategy of the parser. 
   * 
   * @since 2.10
   */
   public ILinkRenderStrategy getLinkRenderStrategy(IClientContext context) throws Exception
   {
     return context.createDefaultLinkRenderStrategy();
   }
}
