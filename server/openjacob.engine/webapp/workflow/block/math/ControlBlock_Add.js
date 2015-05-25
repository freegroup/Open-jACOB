flow.ControlBlock_Add=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractMath.call(this,app,"de.tif.jacob.util.flow.block.math.Add", id);
  this.name = "Add";
  this.setDropZoneDynamicParameterCount(10, "number"); // hat höchstens 10 dynamische Parameter
};


/** @private **/
flow.ControlBlock_Add.prototype = new flow.ControlBlock_AbstractMath();
/** @private **/
flow.ControlBlock_Add.prototype.type="flow.ControlBlock_Add";

