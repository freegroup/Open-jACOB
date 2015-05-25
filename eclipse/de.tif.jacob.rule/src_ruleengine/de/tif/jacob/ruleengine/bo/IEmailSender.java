/*
 * Created on 07.12.2006
 *
 */
package de.tif.jacob.ruleengine.bo;

import de.tif.jacob.core.Context;

public interface IEmailSender
{

  void send(Context context, String to, String subject, String message) throws Exception;
}
