package jacob.common;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ILinkRenderStrategy;
import de.tif.jacob.screen.RegExprLinkParser;

public class ArticleLinkParser extends RegExprLinkParser
{
  public ArticleLinkParser()
  {
    // Text/Linkdarstellung             =>   RegExpr Pattern
    // ===================================================================================================
    // [[artikel:1232]]                 =>   \\[\\[artikel:([ 0-9]*)(?:\\|(.*?))?\\]\\]
    // http://www.google.de             =>   ((?:(?: ftp| https?){1}://)([-a-zA-Z0-9@:%_\\+.,~#?&//=]+))
    // [[http://www.google.de |Google]] =>   \\[\\[(ftp|https?://[-a-zA-Z0-9@:%_\\+.,~#?&//=]+)(?:\\|(.*?))?\\]\\]
    super("\\[\\[artikel:([ 0-9]*)(?:\\|(.*?))?\\]\\]|((?:(?: ftp| https?){1}://)([-a-zA-Z0-9@:%_\\+.,~#?&//=]+))|\\[\\[(ftp|https?://[-a-zA-Z0-9@:%_\\+.,~#?&//=]+)(?:\\|(.*?))?\\]\\]");
  }

  @Override
  public ILinkRenderStrategy getLinkRenderStrategy(IClientContext context) throws Exception
  {
    return new ArticleLinkRenderStrategy();
  }
  

}
