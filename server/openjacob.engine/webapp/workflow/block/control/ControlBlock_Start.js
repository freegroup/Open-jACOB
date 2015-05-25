flow.ControlBlock_Start=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.AbstractBlock.call(this,app, id);
  
  this.enableDropZoneNext();
  this.property = "onClicks";
  this.deleteable=false;
};


/** @private **/
flow.ControlBlock_Start.prototype = new flow.AbstractBlock();
/** @private **/
flow.ControlBlock_Start.prototype.type="flow.ControlBlock_Start";


flow.ControlBlock_Start.prototype.createHTML=function()
{
	this.html= new Element("div",{"class":"Before AbstractBlock "+this.getCSSClassName()});
	this.label= new Element("div",{"class":"ControlBlock_Start_label"}).update(this.property);
	this.html.appendChild(this.label);
  
    return this.html;
}

flow.ControlBlock_Start.prototype.layout=function()
{
   this.label.update(this.property);
   flow.AbstractBlock.prototype.layout.call(this);
}

/**
 * @return The calculated width of the element.
 * @type int
 **/
flow.ControlBlock_Start.prototype.getPreferredWidth=function()
{
   return Math.max(flow.AbstractBlock.PARAMETER_LAYOUT_MIN_WIDTH,100);
};


flow.ControlBlock_Start.prototype.getPersistentAttributes=function()
{
   var memento = flow.AbstractBlock.prototype.getPersistentAttributes.call(this);

   memento.property = this.property;
   memento.attributes.implementation = "de.tif.jacob.util.flow.block.control.Start";
    
   return memento;
};