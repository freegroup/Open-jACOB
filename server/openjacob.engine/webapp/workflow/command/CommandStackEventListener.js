
/**
 * A listener interface for receiving notification before and after commands are executed, undone, or redone.
 *
 * @version @VERSION@
 * @author Andreas Herz
 * @constructor
 */
flow.CommandStackEventListener=function()
{
};

flow.CommandStackEventListener.prototype.type="flow.CommandStackEventListener";

/**
 * Sent when an event occurs on the command stack. @NAMESPACE@CommandStackEvent.getDetail() 
 * can be used to identify the type of event which has occurred.
 * 
 **/
flow.CommandStackEventListener.prototype.stackChanged=function(/*:flow.CommandStackEvent*/ event)
{
};
