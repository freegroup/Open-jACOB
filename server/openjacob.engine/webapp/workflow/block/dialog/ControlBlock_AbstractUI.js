flow.ControlBlock_AbstractUI=function(/*flow.Application*/ app,/*:String*/ implementation, /*:String*/ id)
{
  flow.AbstractBlock.call(this,app, id);
  
  this.implementation = implementation;
  this.group = "UI";
  this.name = "-unset-";
};


/** @private **/
flow.ControlBlock_AbstractUI.prototype = new flow.AbstractBlock();
/** @private **/
flow.ControlBlock_AbstractUI.prototype.type="flow.ControlBlock_AbstractUI";

flow.ControlBlock_AbstractUI.prototype.getCSSBaseClassName=function()
{
   return "AbstractUI";
};

flow.ControlBlock_AbstractUI.prototype.createHTML=function()
{
	this.html= new Element("div",{"class":"AbstractBlock "+this.getCSSBaseClassName()+" "+this.getCSSClassName()});
	this.decoration= new Element("div",{"class":this.getCSSBaseClassName()+"_decoration"}).update(this.group);
	this.label= new Element("div",{"class":this.getCSSBaseClassName()+"_label "+this.getCSSClassName()+"_label"}).update(this.name);

	this.html.appendChild(this.label);
	this.html.appendChild(this.decoration);

    if(this.dropzone_before!==null)
       this.html.addClassName("Before");
        
    if(this.dropzone_next!==null)
       this.html.addClassName("Next");

    if(this.isDataProvider()===true)
    {
       this.html.addClassName("Parameter");
	   this.image= new Element("div",{"class":"Parameter_plug "+this.getCSSBaseClassName()+"_plug handle"});
	   this.html.appendChild(this.image);
    }
    
    return this.html;
}



flow.ControlBlock_AbstractUI.prototype.isDataProvider=function()
{
   return false;
}

/**
 * @return The calculated width of the element.
 * @type int
 **/
flow.ControlBlock_AbstractUI.prototype.getPreferredWidth=function()
{
   if(this.label===null)
     return 150;
     
   return Math.max(this.label.getWidth()+60+parseInt(this.label.positionedOffset().left),150);
};


flow.ControlBlock_AbstractUI.prototype.getPersistentAttributes=function()
{
   var memento = flow.AbstractBlock.prototype.getPersistentAttributes.call(this);

   memento.attributes.implementation = this.implementation;
   
 
   return memento;
};