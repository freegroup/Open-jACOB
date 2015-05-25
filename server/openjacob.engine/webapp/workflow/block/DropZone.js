
flow.DropZone=function(/*:String*/ name, /*:String*/ accept)
{
  this.name = name;
  this.x=0;
  this.y=0;
  this.width=10;
  this.height=10;
  this.width_save=10;
  this.height_saved=10;
  this.html=null;
  this.image=null;
  this.callback=null;
  this.parent=null;
  this.droppedBlock=null;
  this.accept=accept;
  this.label = "";
  this.label_html = null;
  this.enabled=true;
  this.isStaticParameter=false;
  /** @private **/
  this.id = flow.UUID.create();
};


/** @private **/
flow.DropZone.prototype.type="DropZone";


/**
 *
 * Adds a PropertyChangeListener to the listener list. The listener is registered for all properties of this class,
 * If listener is null, no exception is thrown and no action is performed.
 *
 **/
flow.DropZone.prototype.setParent=function(/*:flow.AbstractBlock*/ block )
{
	if(block===null)
	{
	   if(this.html!==null)
	      this.html.remove();
	   this.parent = null;
	   this.html=null;
	}
	else
	{
	   this.html = this.createHTML();
	   this.parent = block;
	   
	   if(this.label_html!=null)
	      this.label_html.addClassName(block.getCSSBaseClassName()+"_parameter_decoration");
	      
       Droppables.add(this.html, 
       {    
         accept: this.accept,
         hoverclass:"droppable_hover",
         onDrop: this.onDroppedAbstractBlock.bind(this)
       });	  
       
	}
	return this.html;
};


flow.DropZone.prototype.getParent=function()
{
	return this.parent;
};




flow.DropZone.prototype.onDroppedAbstractBlock=function(/*:HTMLElement*/ dragged, /*:HTMLElement*/ dropped, /*:Event*/ event )
{
	if(this.callback!=null)
	  this.callback(this, dragged.block);
};


flow.DropZone.prototype.createHTML=function()
{
    // Falls die DropZone Parameter akzeptiert, dann muss er sich auch so darstellen,
    // dass man dies erkennt
    if(this.accept === "Parameter")
    {
       this.html= new Element("div",{"class":this.type}).update(" ");
       this.label_html= new Element("div",{"class":"Parameter_decoration"}).update(this.label);
       this.image= new Element("img",{"class":"Parameter_slot",src:"images/Parameter_slot.gif"});
       this.html.appendChild(this.label_html);
	   this.html.appendChild(this.image);
    }
    else
    {
       this.html= new Element("div",{"class":this.type}).update(" ");
    }
    this.html.addClassName("is_"+this.accept);
    this.html.addClassName("noSize");
    this.html.zone = this;
	this.applyStyle();
	return this.html;
}


flow.DropZone.prototype.enable=function(/*:boolean*/ flag)
{
  if(this.image===null)
    return;

  this.enabled = flag;
  if(flag===true)
  {
    this.image.show();
    this.label_html.removeClassName("Parameter_decoration_plugged");
  }
  else
  {
    this.image.hide();
    this.label_html.addClassName("Parameter_decoration_plugged");
  }
  this.applyStyle();
}

flow.DropZone.prototype.setLabel=function(/*:String*/ label)
{
 	this.label=label;
 	if(this.label_html!==null)
 	   this.label_html.update(this.label);
}


flow.DropZone.prototype.setBoundingBox=function(/*:int*/ x, /*:int*/ y, /*:int*/ width, /*:int*/ height)
{
   this.x = x;
   this.y = y;
   this.width = width;
   this.height = height;
   this.applyStyle();
};

flow.DropZone.prototype.setPosition=function(/*:int*/ x, /*:int*/ y)
{
   this.x = x;
   this.y = y;
   this.applyStyle();
};

flow.DropZone.prototype.getPosition=function()
{
   var result = {x:x,y:y};

   return;
};


flow.DropZone.prototype.setDropCallback=function(/*:function*/ callback)
{
	this.callback = callback;
}

flow.DropZone.prototype.applyStyle=function()
{
   if(this.html!==null)
   {
     if(this.isStaticParameter===false || this.enabled==true)
     {
        this.html.setStyle({"position":"absolute","top":this.y+"px","left":this.x+"px","width":this.width+"px","height":this.height+"px"});
     }
     else
     {
        // Da die z-order des elemente ganz oben ist, fängt das Element die DragDrop Events ab. Somit können
        // Angehängte Elemente (wie Parameter) nicht abgehängt werden da das Linke Element mit dieser DropDrop das
        // KlickEvent abfängt. Lösung: Bei angehängtem Element machen wir die DropZone im disabled Mode ganz klein.
        this.html.setStyle({"position":"absolute","top":this.y+"px","left":this.x+"px","width":"0px","height":"0px"});
     }
   }
}