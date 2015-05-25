flow.ControlBlock_Max=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractMath.call(this,app,"de.tif.jacob.util.flow.block.math.Max", id);
  this.name = "Max";
 
  this.setDropZoneDynamicParameterCount(10, "number"); // hat höchstens 10 dynamische Parameter
};


/** @private **/
flow.ControlBlock_Max.prototype = new flow.ControlBlock_AbstractMath();
/** @private **/
flow.ControlBlock_Max.prototype.type="flow.ControlBlock_Max";

