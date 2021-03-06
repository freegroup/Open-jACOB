/*
 * Created on 30.03.2004
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jacob.exception;

import de.tif.jacob.core.exception.UserException;

/**
 * @author Andreas Sonntag
 * 
 * UserException is the base class of all Exception which in general result due
 * to inproper user operation.
 * 
 * Exceptions of type UserException should be handled by gui clients by means
 * of generating a nice (i.e. a localized, user understandable) warning.
 */
public class BusinessException extends UserException
{
  static public final transient String RCS_ID = "$Id: BusinessException.java,v 1.2 2004/07/12 06:56:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
	/**
	 * @param message
	 */
	public BusinessException(String message)
	{
		super(message);
	}

}
