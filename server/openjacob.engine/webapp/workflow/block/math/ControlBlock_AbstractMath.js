flow.ControlBlock_AbstractMath=function(/*flow.Application*/ app,/*String*/ implementation, /*:String*/ id)
{
  flow.AbstractBlock.call(this,app, id);
  
  this.implementation = implementation;
  this.name = "<unknown>";
};


/** @private **/
flow.ControlBlock_AbstractMath.prototype = new flow.AbstractBlock();
/** @private **/
flow.ControlBlock_AbstractMath.prototype.type="flow.ControlBlock_AbstractMath";

flow.ControlBlock_AbstractMath.prototype.getCSSBaseClassName=function()
{
   return "AbstractMath";
};


flow.ControlBlock_AbstractMath.prototype.createHTML=function()
{
	this.html= new Element("div",{"class":"Parameter AbstractBlock AbstractMath "+this.getCSSClassName()});
	this.decoration= new Element("div",{"class":"AbstractMath_decoration"}).update("Math");
	this.label= new Element("div",{"class":"AbstractMath_label"}).update(this.name);
	this.image= new Element("div",{"class":"Parameter_plug AbstractMath_plug handle"});
	this.html.appendChild(this.label);
	this.html.appendChild(this.image);
	this.html.appendChild(this.decoration);

    return this.html;
}


/**
 * @return The calculated width of the element.
 * @type int
 **/
flow.ControlBlock_AbstractMath.prototype.getPreferredWidth=function()
{
   if(this.label===null)
     return 100;
     
   return Math.max(this.label.getWidth()+50+parseInt(this.label.positionedOffset().left),100);
};



flow.ControlBlock_AbstractMath.prototype.getPersistentAttributes=function()
{
   var memento = flow.AbstractBlock.prototype.getPersistentAttributes.call(this);

   memento.attributes.implementation = this.implementation;
   
 
   return memento;
};