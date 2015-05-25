flow.ControlBlock_Min=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractMath.call(this,app,"de.tif.jacob.util.flow.block.math.Min", id);
  this.name = "Min";
  
  this.setDropZoneDynamicParameterCount(10, "number"); // hat höchstens 10 dynamische Parameter
};


/** @private **/
flow.ControlBlock_Min.prototype = new flow.ControlBlock_AbstractMath();
/** @private **/
flow.ControlBlock_Min.prototype.type="flow.ControlBlock_Min";
