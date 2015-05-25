flow.ControlBlock_AbstractText=function(/*flow.Application*/ app,/*String*/ implementation, /*:String*/ id)
{
  flow.AbstractBlock.call(this,app, id);
  
  this.implementation = implementation;
  this.name = "<unknown>";
};


/** @private **/
flow.ControlBlock_AbstractText.prototype = new flow.AbstractBlock();
/** @private **/
flow.ControlBlock_AbstractText.prototype.type="flow.ControlBlock_AbstractText";


flow.ControlBlock_AbstractText.prototype.getCSSBaseClassName=function()
{
   return "AbstractText";
};

flow.ControlBlock_AbstractText.prototype.createHTML=function()
{
	this.html= new Element("div",{"class":"Parameter AbstractBlock AbstractText "+this.getCSSClassName()});
	this.decoration= new Element("div",{"class":"AbstractText_decoration"}).update("Text");
	this.label= new Element("div",{"class":"AbstractText_label"}).update(this.name);
	this.image= new Element("div",{"class":"Parameter_plug AbstractText_plug handle"});
	this.html.appendChild(this.label);
	this.html.appendChild(this.image);
	this.html.appendChild(this.decoration);

    return this.html;
}



/**
 * @return The calculated width of the element.
 * @type int
 **/
flow.ControlBlock_AbstractText.prototype.getPreferredWidth=function()
{
   if(this.label===null)
     return 80;
     
   return Math.max(this.label.getWidth()+10+parseInt(this.label.positionedOffset().left),80);
};


flow.ControlBlock_AbstractText.prototype.getPersistentAttributes=function()
{
   var memento = flow.AbstractBlock.prototype.getPersistentAttributes.call(this);

   memento.attributes.implementation = this.implementation;
   
 
   return memento;
};