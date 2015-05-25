flow.ControlBlock_Sqrt=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractMath.call(this,app,"de.tif.jacob.util.flow.block.math.Sqrt", id);
  this.name = "Sqrt";
 
  this.addDropZoneStaticParameter("number"); 
};


/** @private **/
flow.ControlBlock_Sqrt.prototype = new flow.ControlBlock_AbstractMath();
/** @private **/
flow.ControlBlock_Sqrt.prototype.type="flow.ControlBlock_Sqrt";
