flow.ControlBlock_Prompt=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractUI.call(this,app,"de.tif.jacob.util.flow.block.dialog.Prompt", id);
  
  this.name ="Ask Dialog";
  this.group = "Dialog";
  this.addDropZoneStaticParameter("Question"); // hat genau einen parameter der Text der Eingabeaufforderung
};


/** @private **/
flow.ControlBlock_Prompt.prototype = new flow.ControlBlock_AbstractUI();
/** @private **/
flow.ControlBlock_Prompt.prototype.type="flow.ControlBlock_Prompt";



flow.ControlBlock_Prompt.prototype.isDataProvider=function()
{
   return true;
}

