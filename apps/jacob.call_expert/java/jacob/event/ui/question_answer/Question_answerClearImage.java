/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Apr 02 12:47:46 CEST 2009
 */
package jacob.event.ui.question_answer;


import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class Question_answerClearImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    element.getGroup().setInputFieldValue("question_answerQuestion", "");
    element.getGroup().setInputFieldValue("question_answerAnswer", "");
    element.getGroup().setInputFieldValue("question_answerNotes", "");
  }

  /**
   * The event handler if the group status has been changed.<br>
   * 
   * @param context The current work context of the jACOB application. 
   * @param status  The new state of the group.
   * @param emitter The emitter of the event.
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
  {
    // Your code here.....
  }
}

