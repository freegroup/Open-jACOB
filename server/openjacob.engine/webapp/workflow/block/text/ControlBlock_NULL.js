flow.ControlBlock_NULL=function()
{
  flow.AbstractBlock.call(this);
};


/** @private **/
flow.ControlBlock_NULL.prototype = new flow.AbstractBlock();
/** @private **/
flow.ControlBlock_NULL.prototype.type="flow.ControlBlock_NULL";


flow.ControlBlock_NULL.prototype.getPersistentAttributes=function()
{
   var memento = {attributes:{}};
   memento.attributes.type= this.type;
   
   return memento;
};

flow.ControlBlock_NULL.prototype.getPersistentAttributes=function()
{
   var memento = flow.AbstractBlock.prototype.getPersistentAttributes.call(this);

   memento.attributes.implementation = "de.tif.jacob.util.flow.block.text.NULL";
   
 
   return memento;
};