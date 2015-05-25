flow.ControlBlock_Join=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractText.call(this,app,"de.tif.jacob.util.flow.block.text.Join", id);
  
  this.name ="Join";
  
  this.setDropZoneDynamicParameterCount(20,"text"); // hat höchstens 10 dynamische Parameter
};


/** @private **/
flow.ControlBlock_Join.prototype = new flow.ControlBlock_AbstractText();
/** @private **/
flow.ControlBlock_Join.prototype.type="flow.ControlBlock_Join";
