
flow.AbstractBlock=function(/*flow.Application*/ app, /*:String*/ id)
{
  this.property = null;
  this.x = 0;
  this.y = 0;
  this.html = null;
  this.app = app;
  this.draggable=null;
  this.canvas = null;
  this.zone = null;
  this.callInitialDirectEdit = false;
  /** @private **/
  if(id===undefined)
	  this.id = flow.UUID.create();
  else
    this.id = id;
    
  this.block_before = null;
  this.block_next   = null;
  this.block_left   = null;
  this.block_outer  = null; // can z.B. ein IfElse oder OkCancelDialog sein
  
  this.dropzone_before = null;
  this.dropzone_next = null;
  this.dropzone_innerblocks = [];
  this.dropzone_static_parameters = [];
  this.dropzone_dynamic_parameters = [];
  this.dropzones = [];
  this.deleteable = true;

  // Der Block hat per default 0 Parameter
  this.dynamic_parameters_max = 0;
};

flow.AbstractBlock.PARAMETER_LAYOUT_TOP  = 0;
flow.AbstractBlock.PARAMETER_LAYOUT_SPACE  = 5;
flow.AbstractBlock.PARAMETER_LAYOUT_HEIGHT = 25;
flow.AbstractBlock.PARAMETER_LAYOUT_WIDTH = 70;
flow.AbstractBlock.PARAMETER_LAYOUT_SPACE_INNER = 30;
flow.AbstractBlock.PARAMETER_LAYOUT_MIN_WIDTH = 120;

/** @private **/
//draw2d.AbstractBlock.prototype = new flow.AbstractObjectModel();
/** @private **/
flow.AbstractBlock.prototype.type="flow.AbstractBlock";
flow.AbstractBlock.prototype.tag="block";

flow.AbstractBlock.prototype.getCSSClassName=function()
{
   return this.type.replace("flow.","");
};
  
flow.AbstractBlock.prototype.getCSSBaseClassName=function()
{
   return this.getCSSClassName();
};


flow.AbstractBlock.prototype.getMenuItems=function(  )
{
    if(this.deleteable===true)
    {
	return [new flow.MenuItem("Delete",function()
	{
	   this.destroy();
	}.bind(this))];
	}
	
	return [];
	
};

flow.AbstractBlock.prototype.setProperty=function(/*:String*/ text)
{
   this.property = text;
   this.layout(true,true);
};


/**
 * @return The calculated width of the element.
 * @type int
 **/
flow.AbstractBlock.prototype.getPreferredWidth=function()
{
   return 100;
};


flow.AbstractBlock.prototype.getAllVariables=function(/*:Array*/ result)
{
	// go recursive to all children
   this.dropzone_static_parameters.each(function(zone, index)
   {
   	  if(zone.droppedBlock !==null)
   	     zone.droppedBlock.getAllVariables(result);
   }.bind(this));
   
   this.dropzone_dynamic_parameters.each(function(zone, index)
   {
   	  if(zone.droppedBlock !==null)
   	     tmp = zone.droppedBlock.getAllVariables(result);
   }.bind(this));
   
   this.dropzone_innerblocks.each(function(zone, index)
   {
   	  if(zone.droppedBlock !==null)
   	     tmp = zone.droppedBlock.getAllVariables(result);
   }.bind(this));
   
   if(this.block_next!==null)
      this.block_next.getAllVariables(result);
};


flow.AbstractBlock.prototype.renameVariable=function(/*:String*/ oldVarName, /*:String*/ newVarName)
{
	// go recursive to all children
   this.dropzone_static_parameters.each(function(zone, index)
   {
   	  if(zone.droppedBlock !==null)
   	     zone.droppedBlock.renameVariable(oldVarName, newVarName);
   }.bind(this));
   
   this.dropzone_dynamic_parameters.each(function(zone, index)
   {
   	  if(zone.droppedBlock !==null)
   	     tmp = zone.droppedBlock.renameVariable(oldVarName, newVarName);
   }.bind(this));
   
   this.dropzone_innerblocks.each(function(zone, index)
   {
   	  if(zone.droppedBlock !==null)
   	     tmp = zone.droppedBlock.renameVariable(oldVarName, newVarName);
   }.bind(this));
   
   if(this.block_next!==null)
      this.block_next.renameVariable(oldVarName, newVarName);
};

flow.AbstractBlock.prototype.getCanvas=function()
{
	return this.canvas;
};



/**
 * @return The calculated height of the element.
 * @type int
 **/
flow.AbstractBlock.prototype.getHeight=function()
{
   var defaultHeight = 30;
   var requiredHeight= 0;
   
   this.dropzone_static_parameters.each(function(zone, index)
   {
      var tmp = flow.AbstractBlock.PARAMETER_LAYOUT_HEIGHT;
   	  if(zone.droppedBlock !==null)
   	     tmp = zone.droppedBlock.getHeight();
	  requiredHeight = requiredHeight+flow.AbstractBlock.PARAMETER_LAYOUT_SPACE+tmp;
   }.bind(this));
   
   this.dropzone_dynamic_parameters.each(function(zone, index)
   {
      var tmp = flow.AbstractBlock.PARAMETER_LAYOUT_HEIGHT;
   	  if(zone.droppedBlock !==null)
   	     tmp = zone.droppedBlock.getHeight();
	  requiredHeight = requiredHeight+flow.AbstractBlock.PARAMETER_LAYOUT_SPACE+tmp;
   }.bind(this));
   
   if(this.dropzone_innerblocks.length>0)
      requiredHeight +=flow.AbstractBlock.PARAMETER_LAYOUT_SPACE_INNER;

   this.dropzone_innerblocks.each(function(zone, index)
   {
      var tmp = flow.AbstractBlock.PARAMETER_LAYOUT_HEIGHT;
   	  if(zone.droppedBlock !==null)
   	     tmp = zone.droppedBlock.getRecursiveHeight();
	  requiredHeight = requiredHeight+flow.AbstractBlock.PARAMETER_LAYOUT_SPACE_INNER+tmp;
   }.bind(this));
   
   return Math.max(defaultHeight, requiredHeight);
};

flow.AbstractBlock.prototype.getRecursiveHeight=function()
{
   var height = this.getHeight();
   if(this.block_next!=null)
     height += this.block_next.getRecursiveHeight();
   return height;
};


flow.AbstractBlock.prototype.enableDropZoneBefore=function()
{
  this.dropzone_before = this.createDropZone("dropzone_before","Before");
  this.dropzone_before.setDropCallback(this.droppedOnDropZoneBefore.bind(this));
};

flow.AbstractBlock.prototype.enableDropZoneNext=function()
{
  this.dropzone_next = this.createDropZone("dropzone_next","Next");
  this.dropzone_next.setDropCallback(this.droppedOnDropZoneNext.bind(this));
};


flow.AbstractBlock.prototype.addDropZoneInnerBlock=function()
{
  var zone = this.createDropZone("dropzone_innerblock","Next");
  zone.setDropCallback(this.droppedOnDropZoneInnerBlock.bind(this));
  this.dropzone_innerblocks.push(zone);
};

flow.AbstractBlock.prototype.addDropZoneStaticParameter=function(/*:String*/ label)
{
  var zone = this.createDropZone("dropzone_parameter","Parameter");
  zone.setDropCallback(this.droppedOnDropZoneStaticParameter.bind(this));
  if(label!==undefined)
  	zone.setLabel(label);
  zone.isStaticParameter = true;
  this.dropzone_static_parameters.push(zone);
};

flow.AbstractBlock.prototype.setDropZoneDynamicParameterCount=function( /*:int*/ param_max,/*:String*/ label)
{
  this.dynamic_parameters_max = param_max;
  // per default einen dynamischen PArameter anlegen
  //
  if(this.dynamic_parameters_max>0)
  {
    var zone = this.createDropZone("dropzone_parameter","Parameter");
    zone.setDropCallback(this.droppedOnDropZoneDynamicParameter.bind(this));
    this.dynamic_param_label = label;
    if(this.dynamic_param_label!==undefined)
       zone.setLabel(this.dynamic_param_label);
    this.dropzone_dynamic_parameters.push(zone);
  }
};

flow.AbstractBlock.prototype.droppedOnDropZoneInnerBlock=function(/*:flow.DropZone*/ zone, /*:flow.AbstractBlock*/ dragged)
{
   if(dragged===this)
     return;
     
   if(dragged===null)
     return;
    
   if(dragged.block_next===this)
     return;
     
   // Das "dragged" Element ist nicht mehr ein TopLevel Element. Es ist ein Kind
   // von diesem Element => beim Canvas austragen
   if(this.canvas!==null)
      this.canvas.removeBlock(dragged);

   var last_dragged = dragged;
   while(last_dragged.block_next!==undefined && last_dragged.block_next!==null)
   {
      last_dragged = last_dragged.block_next;
   }
 
   if(zone.droppedBlock!==null)
   {
      // Das element ist bereits eingehängt. Jetzt ersetzen wir dies mit dem "dropped" und
      // hängen uns an das "dropped" dran.
      last_dragged.setBlockNext(zone.droppedBlock);
   }
   zone.droppedBlock = dragged;
   dragged.block_outer = this;
   dragged.zone = zone;
   
   this.html.appendChild(dragged.getHTML());

   this.layout(true,true);
}


flow.AbstractBlock.prototype.startDirectEdit=function(event)
{
   this.callInitialDirectEdit = false;
}

flow.AbstractBlock.prototype.droppedOnDropZoneBefore=function(/*:flow.DropZone*/ zone, /*:flow.AbstractBlock*/ dragged)
{
   if(dragged===this)
     return;
     
   if(dragged===null)
     return;
   
   // Falls "dragged" nicht den Canvas als Vater hat, dann wurde dieser aus der PAlette
   // direkt auf die Zone geworfen. "Sicherheitshalber zu dem Canvas hinzufügen.
   // Mehrfachaufruf hat keine Auswirkung
   if(dragged.block_before===null)
   	this.canvas.addBlock(dragged);

   // Das letzte Element in der Schlange des Dropped-Element finden. An diesem wird dann angehängt
   //
   var tail = dragged;
   while(tail!==null && tail.block_next!==null)
      tail = tail.block_next;
     
   // falls dieser Block schon wo anders eingehängt war, dann muss dieser
   // dort ausgehängt werden
   //
   if(this.block_before!==null)
   {
      var tmp = this.block_before;
      this.block_before.removeBlock(this);
      // Das element ist bereits eingehängt. Jetzt ersetzen wir uns mit dem "dropped" und
      // hängen uns an das "dropped" dran.
      tmp.setBlockNext(dragged);
   }
   
   if(this.block_outer!==null)
   {
      // Das element ist bereits eingehängt. Jetzt ersetzen wir dies mit dem "dropped" und
      // hängen uns an das "dropped" dran.
      var outer = this.block_outer;
      var zone = this.zone;
      outer.removeBlock(this);
      outer.droppedOnDropZoneInnerBlock(zone,dragged);
   }
   // ansonsten muss es ein TopLevel element gewesen sein
   // => bei dem Canvas austragen, da dieser jetzt ein Unterelement ist.
   else
   {
	  this.canvas.removeBlock(this);
   }
   
   
   tail.setBlockNext(this);
};


flow.AbstractBlock.prototype.droppedOnDropZoneNext=function(/*:flow.DropZone*/ zone, /*:flow.AbstractBlock*/ dragged)
{
   if(dragged===this)
     return;
     
   if(dragged===null)
     return;
    
   if(dragged.block_next===this)
     return;
     
   // Das "dragged" Element ist nicht mehr ein TopLevel Element. Es ist ein Kind
   // von diesem Element => beim Canvas austragen
   if(this.canvas!==null)
      this.canvas.removeBlock(dragged);

   var last_dragged = dragged;
   while(last_dragged.block_next!==undefined && last_dragged.block_next!==null)
   {
      last_dragged = last_dragged.block_next;
   }
 
   if(this.block_next!==null)
   {
      // Das element ist bereits eingehängt. Jetzt ersetzen wir uns mit dem "dropped" und
      // hängen uns an das "dropped" dran.
      last_dragged.setBlockNext(this.block_next);
   }
   this.setBlockNext(dragged);
   dragged.layout(true,true);
}


flow.AbstractBlock.prototype.droppedOnDropZoneStaticParameter=function(/*:flow.DropZone*/ zone, /*:flow.AbstractBlock*/ dragged)
{
   if(dragged===this)
     return;
     
   if(dragged===null)
     return;
    
   // Das "dragged" Element ist nicht mehr ein TopLevel Element. Es ist ein Kind
   // von diesem Element => beim Canvas austragen
   if(this.canvas !==null)
      this.canvas.removeBlock(dragged);

   dragged.setCanvas(this.canvas);
   
   zone.enable(false);
   
   dragged.zone = zone;
   zone.droppedBlock = dragged; 

   this.html.appendChild(dragged.getHTML());
   dragged.block_left = this;
   
   this.layout(true,true);
   dragged.layout();
}


flow.AbstractBlock.prototype.droppedOnDropZoneDynamicParameter=function(/*:flow.DropZone*/ zone, /*:flow.AbstractBlock*/ dragged)
{
   if(dragged===this)
     return;
     
   if(dragged===null)
     return;
   
   // Das "dragged" Element ist nicht mehr ein TopLevel Element. Es ist ein Kind
   // von diesem Element => beim Canvas austragen
   if(this.canvas!==null)
      this.canvas.removeBlock(dragged);

   // Es kann sein, dass ein Block auf einen Zone geworfen wurde, welche bereits
   // durch einen Block belegt ist (dynamic parameter). In diesem Fall müssen alle
   // Block eins weiter rutschen und dem neuen Block Platz machen
   //
   if(zone.droppedBlock!==null)
   {
     var currentZone=this.dropzone_dynamic_parameters.last();
     var index =  this.dropzone_dynamic_parameters.indexOf(zone);
    
     for(var i=(this.dropzone_dynamic_parameters.length-1); i>=index;i--)
     {
        var previousZone = this.dropzone_dynamic_parameters[i];
        currentZone.droppedBlock = previousZone.droppedBlock;
        if(currentZone.droppedBlock!=null)
          currentZone.droppedBlock.zone = currentZone;
        currentZone = previousZone;
     };

   }

   dragged.zone = zone;
   zone.droppedBlock = dragged; 

   this.html.appendChild(dragged.getHTML());
   dragged.block_left = this;

   // Falls keine weiteren dynamischen Parameter angelegt werden dürfen, dann
   // müssen alle DropZones disabled werden.
   //
   var enabled = this.dropzone_dynamic_parameters.length < this.dynamic_parameters_max;
   this.dropzone_dynamic_parameters.each(function(zone, index)
   {
      zone.enable(enabled);
   }.bind(this));
   
   // Falls erlaubt, dann noch mind. einen leeren Slot anlegen
   // wenn dieser nicht schon vorhanden ist
   if(this.dropzone_dynamic_parameters.length<this.dynamic_parameters_max)
   {
    var zone = this.createDropZone("dropzone_parameter","Parameter");
    if(this.dynamic_param_label!==undefined)
       zone.setLabel(this.dynamic_param_label);
    zone.setDropCallback(this.droppedOnDropZoneDynamicParameter.bind(this));
    this.dropzone_dynamic_parameters.push(zone);
   }
   
   this.layout(true,true);
   dragged.layout();
}


flow.AbstractBlock.prototype.layout=function(/*:boolean*/ propagateParentToo, /*:boolean*/ propagateChildToo)
{
   this.applyStyle();
   
   if(this.block_next!=null)
   {
     this.block_next.setPosition(-1, this.getHeight());
   }
   
   if(this.dropzone_before!=null)
   {
     this.dropzone_before.setBoundingBox(0,-5,this.getPreferredWidth(),10);
   }
   
   if(this.dropzone_next!=null)
   {
     this.dropzone_next.setBoundingBox(0,this.getHeight()-5,this.getPreferredWidth(),10);
   }

   var next_y = flow.AbstractBlock.PARAMETER_LAYOUT_TOP;
   
   // Slot's der fixen Parameter arrangieren
   //
   this.dropzone_static_parameters.each(function(zone, index)
   {
     var tmp = flow.AbstractBlock.PARAMETER_LAYOUT_HEIGHT;
     if(zone.droppedBlock !==null)
     {
       tmp = zone.droppedBlock.getHeight();
       zone.droppedBlock.setPosition(this.getPreferredWidth(), next_y);
       zone.enable(false);
     }
     else
     {
       zone.enable(true);
     }
       
  	 zone.setBoundingBox(this.getPreferredWidth(), next_y,flow.AbstractBlock.PARAMETER_LAYOUT_WIDTH,tmp);
     next_y = next_y+flow.AbstractBlock.PARAMETER_LAYOUT_SPACE+tmp;
   }.bind(this));

   // Slot's der dynamischen Parameter arrangieren
   //
   this.dropzone_dynamic_parameters.each(function(zone, index)
   {
     var tmp = flow.AbstractBlock.PARAMETER_LAYOUT_HEIGHT;
     if(zone.droppedBlock !==null)
     {
       tmp = zone.droppedBlock.getHeight();
       zone.droppedBlock.setPosition(this.getPreferredWidth(), next_y);
   	   zone.setBoundingBox(this.getPreferredWidth(),zone.droppedBlock.y-flow.AbstractBlock.PARAMETER_LAYOUT_SPACE,zone.droppedBlock.getPreferredWidth(),2*flow.AbstractBlock.PARAMETER_LAYOUT_SPACE);
	   zone.enable(false);
	 }
	 else
	 {
  	   zone.setBoundingBox(this.getPreferredWidth(), next_y,flow.AbstractBlock.PARAMETER_LAYOUT_WIDTH,tmp);
  	   zone.enable(true);
	 }
     next_y = next_y+flow.AbstractBlock.PARAMETER_LAYOUT_SPACE+tmp;
   }.bind(this));
   

   if(this.dropzone_innerblocks.length>0)
      next_y +=flow.AbstractBlock.PARAMETER_LAYOUT_SPACE_INNER;

   // Slot's der inneren Blöcke arrangieren
   // diese kommen nach allen Parametern (static und dynamic)
   this.dropzone_innerblocks.each(function(zone, index)
   {
     var tmp = flow.AbstractBlock.PARAMETER_LAYOUT_HEIGHT;
     if(zone.droppedBlock !==null)
     {
       tmp = zone.droppedBlock.getRecursiveHeight();
       zone.droppedBlock.setPosition(20, next_y);
     }
  	 zone.setBoundingBox(20, next_y,this.getPreferredWidth()-20,tmp);
     next_y = next_y+flow.AbstractBlock.PARAMETER_LAYOUT_SPACE_INNER+tmp;
   }.bind(this));



   // es kann sein, dass sich die Höhe/Breite des Elementes geändert hat. Dies hat
   // einen Einfluss auf das Elternelement (ParameterKonsument an der Linken Seite)
   if(this.block_before!=null && propagateParentToo===true)
     this.block_before.layout(true,false);
     
   if(this.block_next!=null && propagateChildToo===true)
     this.block_next.layout(false,true);

   else if(this.block_left!=null)
     this.block_left.layout(true, true);
     
   else if(this.block_outer!=null)
     this.block_outer.layout(true, true);
     
   if(this.callInitialDirectEdit===true && this.html.parentNode!==null)
   {
     this.startDirectEdit();
   }
};


/**
 *
 * Adds a PropertyChangeListener to the listener list. The listener is registered for all properties of this class,
 * If listener is null, no exception is thrown and no action is performed.
 *
 **/
flow.AbstractBlock.prototype.setCanvas=function( canvas )
{
    if(this.canvas ===canvas)
      return this.getHTML();
      
    this.canvas = canvas;
	if(canvas===null)
	{
	   this.draggable.destroy();
	   this.html.remove();
	   this.html=null;
       this.dropzones.each(function(zone,index)
	   {
	     zone.setParent(null);
	   }.bind(this));
	}
	else if(this.html===null)
	{
	   this.html = this.createHTML();
	   this.html.observe("contextmenu", function(event)
	   {
	    event.preventDefault();
	    event.stop();
        $$(".menu").each(Element.remove);
	    var items = this.getMenuItems();
	    if(items.length===0)
	      return;
	    var div = new Element("div",{"class":"menu desktop","style":"left:"+event.pointerX()+";top:"+event.pointerY()});
	    var ul = new Element("ul");
	    div.appendChild(ul);
	    items.each(function(element)
	    {
	      var li =new Element("li");
	      var a = new Element("a",{"class":"enabled"}).update(element.getLabel());
	      a.observe("click", element.getAction().wrap(function(proceed)
	      {
	        $$(".menu").each(Element.remove);
	        proceed();
	      }).bind(this));
	      li.appendChild(a);
	      this.appendChild(li);
	    }.bind(ul));
	    $("body").appendChild(div);
	   }.bind(this));
	   
	   // Der IE kann nicht korrekt mit Transparenz und dem ScrollArea umgehen
	   //
	   if(Prototype.Browser.IE)
	   {
  	      this.draggable = new Draggable(this.html, 
	      {
	        starteffect: function(){},
	        onStart: this.onDragStart.bind(this),
	        onEnd: this.onDragEnd.bind(this),
	        onDrag: this.updatePosition.bind(this)
	   });
	   }
	   else
	   {
  	       this.draggable = new Draggable(this.html, 
	       {
	          scroll: $("scrollarea"),
	          starteffect: function(e){new Effect.Opacity(e, { from: 1.0, to: 0.3, duration: 0.5 });},
	          onStart: this.onDragStart.bind(this),
	          onEnd: this.onDragEnd.bind(this),
	         
	          onDrag: this.updatePosition.bind(this)
	       });
	   }
    
       this.html.block=this;
	   this.html.id=this.id;
       this.dropzones.each(function(zone,index)
	   {
	     var html = zone.setParent(this);
	     this.html.appendChild(html);
	   }.bind(this));
    }
	return this.html
};


flow.AbstractBlock.prototype.destroy=function(  )
{
   if(this.zone!==null)
   {
     this.zone.droppedBlock = null;
     this.zone=null;
   }
   
   // Element war als Parameter an einer DropZone angehängt => aushängen
   // 
   if(this.block_left!==null)
   {
     this.block_left.removeBlock(this);
     this.html.remove();
   }
   else if(this.block_before!=null)
   {
     this.block_before.removeBlock(this);
     this.html.remove();
   }
   else
   {
     this.canvas.removeBlock(this);
   }
};


flow.AbstractBlock.prototype.setBlockNext=function(/*:AbstractBlock*/ block)
{
   // Der normale Nachfolger wird entfernt
   //
   block.setCanvas(this.canvas);
   this.block_next= block;
   block.block_before = this;
   this.html.appendChild(this.block_next.getHTML());

   this.layout(true,true);
};

flow.AbstractBlock.prototype.getRootBlock=function()
{
	if(this.block_before!==null)
	  return this.block_before.getRootBlock();
	return this;
};

flow.AbstractBlock.prototype.onDragEnd=function()
{
  $$(".dropzone_glow").each(function(element,index){
  element.removeClassName("dropzone_glow");
  });
  $$(".DropZone").each(function(element,index){
  element.addClassName("noSize");
  });
  this.html.removeClassName("topLevel");
}

/**
 *
 **/
flow.AbstractBlock.prototype.onDragStart=function()
{
   // Falls das Element einen Vorgänger hat, wird dieses jetzt aus dieser Kette
   // ausgehängt und bei dem Canvas als TopLevel Element eingetragen
   //
   if(this.block_before!==null)
   {
     this.block_before.removeBlock(this);
   	 this.app.getCanvas().addBlock(this);
   }
   
   else if(this.block_outer!==null)
   {
     this.block_outer.removeBlock(this);
   	 this.app.getCanvas().addBlock(this);
   }

   // Element war als Parameter an einer DropZone angehängt => aushängen
   // 
   else if(this.block_left!==null)
   {
     this.block_left.removeBlock(this);
   	 this.app.getCanvas().addBlock(this);
   }

   if(this.zone!=null)
   {
     this.zone.droppedBlock=null;
     this.zone = null;
   }
   
   if(this.html.hasClassName("Next"))
   {
   	 $$(".is_Next").each(function(element,index){
   	 if(element.descendantOf(this.html))
   	   return;

   	 // keine überlagerten DropZones anzeigen. Es ist ein Element
   	 // oberhalb vorhanden. Diese DropZone wird angezeigt. Das reicht.
   	 if(element.zone.droppedBlock!==null)
   	   return;
   	   
   	 element.addClassName("dropzone_glow");
   	 }.bind(this));
   }
   
   if(this.html.hasClassName("Before"))
   {
   	 $$(".is_Before").each(function(element,index){
   	 if(element.descendantOf(this.html))
   	   return;
   	 // keine überlagerten DropZones anzeigen. Es ist ein Element
   	 // oberhalb vorhanden. Diese DropZone wird angezeigt. Das reicht.
   	 if(element.zone.parent.block_before!==null)
   	   return;
   	   
   	 element.addClassName("dropzone_glow");
   	 }.bind(this));
   }
   
   if(this.html.hasClassName("Parameter"))
   {
   	 $$(".is_Parameter").each(function(element,index){
   	 if(element.descendantOf(this.html))
   	   return;
   	   
   	 element.addClassName("dropzone_glow");
   	 }.bind(this));
   }
   $$(".DropZone").each(function(element,index){
     element.removeClassName("noSize");
   });
  
   this.html.addClassName("topLevel");
};

flow.AbstractBlock.prototype.removeBlock=function(block)
{
   if(block===this)
     return;
     
   var zone = block.zone;
   if(zone!==null)
   {
      if(this.dropzone_innerblocks.indexOf(zone)!==-1)
      {
        block.zone = null;
        block.block_outer=null;
        zone.droppedBlock = null;
      }
      else if(this.dropzone_dynamic_parameters.indexOf(zone)!==-1)
      {
        // Eventuell unnötige Parameterslot entfernen
        //
        var toCheck = this.dropzone_dynamic_parameters.clone();
        toCheck.each(function(zone, index)
        {
          if(zone.droppedBlock===block)
   	      {
            this.dropzone_dynamic_parameters = this.dropzone_dynamic_parameters.without(zone);
            this.dropzones = this.dropzones.without(zone);
            zone.droppedBlock.zone = null;
            zone.droppedBlock.block_left=null;
   		    zone.setParent(null);
   		    zone.droppedBlock = null;
   	      }
        }.bind(this));
   
        // Falls die Letzte DropZone schon belegt ist, dann wird jetzt
        // eine neue leere erzeugt.
        if(this.dropzone_dynamic_parameters.last().droppedBlock!==null)
        {
          var zone = this.createDropZone("dropzone_parameter","Parameter");
          zone.setDropCallback(this.droppedOnDropZoneDynamicParameter.bind(this));
          if(this.dynamic_param_label!==undefined)
            zone.setLabel(this.dynamic_param_label);
          this.dropzone_dynamic_parameters.push(zone);
         }
      }
      else if(this.dropzone_static_parameters.indexOf(zone) !==-1)
      {
         block.zone = null;
         block.block_left=null;
         zone.droppedBlock = null;
         zone.enable(true);
      }
   }
   
   if(this.block_next===block)
   {
      block.block_before=null;
      this.block_next=null;
   }

   this.layout(true,true);
}   




flow.AbstractBlock.prototype.updatePosition=function()
{
	var pos = this.html.positionedOffset();
	this.x = pos.left;
	this.y = pos.top;
};


flow.AbstractBlock.prototype.createHTML=function()
{
  throw "Override abstract method [flow.AbstractBlock.prototype.createHTML()]";
}
 
 
flow.AbstractBlock.prototype.getHTML=function()
{
  if(this.html===null)
    this.html = this.createHTML();
  return this.html;
}


flow.AbstractBlock.prototype.setPosition=function(/*:int*/ x, /*:int*/ y)
{
   this.x = x;
   this.y = y;
   this.applyStyle();
};



flow.AbstractBlock.prototype.applyStyle=function()
{
   if(this.html!==null)
   {

     this.html.setStyle({position:"absolute",top:this.y+"px",left:this.x+"px",width:this.getPreferredWidth()+"px",height:this.getHeight()+"px"});
   }
};



flow.AbstractBlock.prototype.createDropZone=function( /*:String */name, /*:String*/ accept)
{
	var dropZone = new flow.DropZone(name, accept);
	if(this.html!==null)
	{
       var html = dropZone.setParent(this);
	   this.html.appendChild(html);
    }
    this.dropzones.push(dropZone);
	return dropZone;
};



/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 **/
flow.AbstractBlock.prototype.getPersistentAttributes=function()
{
   var memento = {attributes:{}};
   
   // enrich the base attributes with the class/instance specific properties
   memento.attributes.id= this.id;
   memento.attributes.type= this.type;
   memento.attributes.x= this.x;
   memento.attributes.y= this.y;

   if(this.dropzone_static_parameters.length>0)
   {
     memento.static_param = {};
     memento.static_param.parameters = [];
     this.dropzone_static_parameters.each(function(zone, index)
     {
   	    if(zone.droppedBlock !==null)
           memento.static_param.parameters.push(zone.droppedBlock);
        else
           memento.static_param.parameters.push(new flow.ControlBlock_NULL());
     });
     memento.static_param.getPersistentAttributes = function()
     {
       var memento = {attributes:{}};
       memento.parameters =  this.parameters;
       return memento;
     };
   }
   
   if(this.dropzone_dynamic_parameters.length>0)
   {
     memento.dynamic_param = {};
     memento.dynamic_param.parameters = [];
     this.dropzone_dynamic_parameters.each(function(zone, index)
     {
   	    if(zone.droppedBlock !==null)
           memento.dynamic_param.parameters.push(zone.droppedBlock);
     });
     memento.dynamic_param.getPersistentAttributes = function()
     {
       var memento = {attributes:{}};
       memento.parameters =  this.parameters;
       return memento;
     };
   }

   if(this.dropzone_innerblocks.length>0)
   {
     memento.innerblocks = {};
     memento.innerblocks.parameters = [];
     this.dropzone_innerblocks.each(function(zone, index)
     {
   	    if(zone.droppedBlock !==null)
           memento.innerblocks.parameters.push(zone.droppedBlock);
        else
           memento.innerblocks.parameters.push(new flow.ControlBlock_NULL());
     });
     memento.innerblocks.getPersistentAttributes = function()
     {
       var memento = {attributes:{}};
       memento.parameters =  this.parameters;
       return memento;
     };
   }

   
   if(this.block_next!==null)
     memento.block = this.block_next;
   	
   return memento;
};

