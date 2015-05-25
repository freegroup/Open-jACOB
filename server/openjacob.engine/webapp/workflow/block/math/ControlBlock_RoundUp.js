flow.ControlBlock_RoundUp=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractMath.call(this,app,"de.tif.jacob.util.flow.block.math.RoundUp", id);
  this.name = "Round";
 
  this.addDropZoneStaticParameter("number"); 
  this.addDropZoneStaticParameter("scale"); 
};


/** @private **/
flow.ControlBlock_RoundUp.prototype = new flow.ControlBlock_AbstractMath();
/** @private **/
flow.ControlBlock_RoundUp.prototype.type="flow.ControlBlock_RoundUp";

