/*
 * Created on 30.03.2004
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jacob.exception;

import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.Message;

/**
 * BusinessException derived from UserException which enforces localization of
 * messages.
 * 
 * @author andreas
 */
public class BusinessException extends UserException
{
  static public final transient String RCS_ID = "$Id: BusinessException.java,v 1.3 2005/10/19 11:28:25 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
	/**
	 * @param message
	 */
	public BusinessException(Message message)
	{
		super(message);
	}
}
