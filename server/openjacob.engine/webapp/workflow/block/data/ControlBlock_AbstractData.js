flow.ControlBlock_AbstractData=function(/*flow.Application*/ app,/*String*/ implementation, /*:String*/ id)
{
  if(app===undefined)
    return;
    
  flow.AbstractBlock.call(this,app, id);
  
  this.implementation = implementation;
  
  this.label = null;
  this.property = "DblClick to edit";
  this.group ="Data";
};


/** @private **/
flow.ControlBlock_AbstractData.prototype = new flow.AbstractBlock();
/** @private **/
flow.ControlBlock_AbstractData.prototype.type="flow.ControlBlock_AbstractData";


flow.ControlBlock_AbstractData.prototype.getCSSBaseClassName=function()
{
   return "AbstractData";
};

flow.ControlBlock_AbstractData.prototype.createHTML=function()
{
	this.html= new Element("div",{"class":"AbstractBlock "+this.getCSSBaseClassName()+" "+this.getCSSClassName()});
	this.decoration= new Element("div",{"class":this.getCSSBaseClassName()+"_decoration"}).update(this.group);
	this.label= new Element("div",{"class":this.getCSSBaseClassName()+"_label "+this.getCSSClassName()+"_label"}).update(this.property);
	this.label.title="double click to edit";
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

   // direct edit is not supported if the variable are locked. E.g. a "predefiend Variable" is
   // is locked by default.
   //
   if(this.supportsDirectEdit()===true)
   {
      Event.observe(this.label,"dblclick",this.startDirectEdit.bind(this));
   
     this.label.observe('mouseenter', function(e) 
     {
       this.label.addClassName("antBox");
     }.bind(this));

     this.label.observe('mouseleave', function(e) 
     {
        this.label.removeClassName("antBox");
     }.bind(this));
   }
   
   return this.html;
};

/**
 * 
 **/
flow.ControlBlock_AbstractData.prototype.supportsDirectEdit=function()
{
   return true;
};


flow.ControlBlock_AbstractData.prototype.isDataProvider=function()
{
   return false;
};


flow.ControlBlock_AbstractData.prototype.layout=function()
{
   this.label.update(this.property);
   flow.AbstractBlock.prototype.layout.call(this);
};


/**
 * @return The calculated width of the element.
 * @type int
 **/
flow.ControlBlock_AbstractData.prototype.getPreferredWidth=function()
{
   if(this.label===null)
     return 100;
     
   return Math.max(this.label.getWidth()+50+parseInt(this.label.positionedOffset().left),100);
};


flow.ControlBlock_AbstractData.prototype.startDirectEdit=function(event)
{
   this.callInitialDirectEdit = false;
   this.directEditInputField = new Element("input");
   this.directEditInputField.value = this.property;
   this.directEditInputField.setStyle("z-index:30000;position:absolute;top:"+this.html.style.top+";left:"+this.html.style.left+";height:"+Math.min(30,this.getHeight()+2)+"px;width:"+this.getPreferredWidth()+"px");
   this.directEditInputField.style.backgroundColor="#F1FCBF";
   this.html.parentNode.appendChild(this.directEditInputField);
   this.directEditInputField.focus();
   Form.Element.select( this.directEditInputField);
   Event.observe(this.directEditInputField,"keypress",function(event)
   {
      var charCode = event.charCode || event.keyCode || event.which;
      if(charCode == Event.KEY_RETURN)
      {
         var value = $F(this.directEditInputField);
         event.stop();
         this.directEditInputField.remove();
         this.directEditInputField=null;
         this.setProperty(value);
      }
      else if(charCode == Event.KEY_ESC)
      {
         event.stop();
         this.directEditInputField.remove();
         this.directEditInputField=null;
      }
   }.bind(this));

   Event.observe(this.directEditInputField,"blur",function(event)
   {
      var value = $F(this.directEditInputField);
      event.stop();
      this.directEditInputField.remove();
      this.directEditInputField=null;
      this.setProperty(value);
   }.bind(this));
   
};



flow.ControlBlock_AbstractData.prototype.getPersistentAttributes=function()
{
   var memento = flow.AbstractBlock.prototype.getPersistentAttributes.call(this);

   memento.property = this.property;
   memento.attributes.implementation = this.implementation;
   
 
   return memento;
};