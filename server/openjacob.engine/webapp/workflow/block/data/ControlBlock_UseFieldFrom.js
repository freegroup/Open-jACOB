flow.ControlBlock_UseFieldFrom=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractData.call(this,app,"de.tif.jacob.util.flow.block.record.UseFieldFrom", id);
  this.property = "field name";

  this.addDropZoneStaticParameter("from"); // hat genau einen parameter der Text der Eingabeaufforderung
};

/** @private **/
flow.ControlBlock_UseFieldFrom.prototype = new flow.ControlBlock_AbstractData();
/** @private **/
flow.ControlBlock_UseFieldFrom.prototype.type="flow.ControlBlock_UseFieldFrom";


flow.ControlBlock_UseFieldFrom.prototype.isDataProvider=function()
{
   return true;
}

