flow.ControlBlock_Multiply=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractMath.call(this,app,"de.tif.jacob.util.flow.block.math.Multiply", id);
  this.name = "Multiply";
   
  this.setDropZoneDynamicParameterCount(10, "number"); // hat höchstens 10 dynamische Parameter
};


/** @private **/
flow.ControlBlock_Multiply.prototype = new flow.ControlBlock_AbstractMath();
/** @private **/
flow.ControlBlock_Multiply.prototype.type="flow.ControlBlock_Multiply";

