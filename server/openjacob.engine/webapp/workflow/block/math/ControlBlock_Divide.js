flow.ControlBlock_Divide=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractMath.call(this,app,"de.tif.jacob.util.flow.block.math.Divide", id);
  this.name = "Divide";
  
  this.addDropZoneStaticParameter("dividend"); 
  this.addDropZoneStaticParameter("divisor"); 
};


/** @private **/
flow.ControlBlock_Divide.prototype = new flow.ControlBlock_AbstractMath();
/** @private **/
flow.ControlBlock_Divide.prototype.type="flow.ControlBlock_Divide";

