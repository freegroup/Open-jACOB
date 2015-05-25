package jacob.common.tc;

import jacob.exception.BusinessException;

/**
 * Exception thrown if no active campaign has been configured so far.
 *
 */
public class NoActiveCampaignException extends BusinessException
{
  static public final transient String RCS_ID = "$Id: NoActiveCampaignException.java,v 1.1 2006/07/28 14:37:31 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
	/**
	 * @param message
	 */
	public NoActiveCampaignException(String message)
	{
		super(message);
	}
}
