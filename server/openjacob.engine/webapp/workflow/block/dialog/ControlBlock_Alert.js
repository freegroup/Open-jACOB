flow.ControlBlock_Alert=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractUI.call(this,app,"de.tif.jacob.util.flow.block.dialog.Alert", id);
  
  this.group="Dialog";
  this.name="Message Box";
  this.enableDropZoneBefore();
  this.enableDropZoneNext();
  this.addDropZoneStaticParameter("msg"); // hat genau einen parameter
};


/** @private **/
flow.ControlBlock_Alert.prototype = new flow.ControlBlock_AbstractUI();
/** @private **/
flow.ControlBlock_Alert.prototype.type="flow.ControlBlock_Alert";

