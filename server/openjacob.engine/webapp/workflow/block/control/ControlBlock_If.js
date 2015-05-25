flow.ControlBlock_If=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.AbstractBlock.call(this,app, id);
  
  this.enableDropZoneBefore();
  this.enableDropZoneNext();
  this.addDropZoneInnerBlock();
  this.addDropZoneInnerBlock();
  this.addDropZoneStaticParameter(); // hat genau einen parameter
};


/** @private **/
flow.ControlBlock_If.prototype = new flow.AbstractBlock();
/** @private **/
flow.ControlBlock_If.prototype.type="flow.ControlBlock_If";


flow.ControlBlock_If.prototype.createHTML=function()
{
	this.html= new Element("div",{"class":"Before Next AbstractBlock "+this.getCSSClassName()});
	this.label_if= new Element("div",{"class":this.getCSSClassName()+"_label_if"}).update("If");
	this.html.appendChild(this.label_if);

	this.label_test= new Element("div",{"class":this.getCSSClassName()+"_label_test"}).update("test");
	this.html.appendChild(this.label_test);

	this.label_then= new Element("div",{"class":this.getCSSClassName()+"_label_then"}).update("Then Do");
	this.html.appendChild(this.label_then);

	this.label_else= new Element("div",{"class":this.getCSSClassName()+"_label_else"}).update("Else Do");
	this.html.appendChild(this.label_else);

    return this.html;
}

/**
 * @return The calculated width of the element.
 * @type int
 **/
flow.ControlBlock_If.prototype.getPreferredWidth=function()
{
   return Math.max(flow.AbstractBlock.PARAMETER_LAYOUT_MIN_WIDTH,100);
};

flow.ControlBlock_If.prototype.layout=function()
{
   flow.AbstractBlock.prototype.layout.call(this);
   
   var pos_then = this.dropzone_innerblocks[0]; 
   var pos_else = this.dropzone_innerblocks[1];
   
   this.label_then.style.top=(pos_then.y-15)+"px";
   this.label_else.style.top=(pos_else.y-15)+"px";
}

flow.ControlBlock_If.prototype.getPersistentAttributes=function()
{
   var memento = flow.AbstractBlock.prototype.getPersistentAttributes.call(this);

   memento.attributes.implementation = "de.tif.jacob.util.flow.block.control.If";
   
 
   return memento;
};