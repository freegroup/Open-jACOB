
flow.PalettePart=function(/*flow.Application*/ app,/*HTMLElement*/ element)
{
  /** @private **/
  this.id = flow.UUID.create();
  this.app = app;
  this.html = element;
  this.blockClassName = element.getAttribute("block");
  this.draggable = new Draggable(element,
  {
     ghosting:true,
     revert:true,
     onDrag: function()
             {
             	var p1 = this.html.cumulativeOffset();
                var p2 = this.app.getCanvas().html.cumulativeOffset();
	
	            var x = p1.left - p2.left;
	            var y = p1.top - p2.top;
	            this.html.block.setPosition(x, y);
             }.bind(this),
     onEnd: this.onDragEnd.bind(this),
     onStart: this.onDragStart.bind(this),
     reverteffect: function(element, top_offset, left_offset) 
                {
                  element.hide();
                  new Effect.Move(element, {x: -left_offset, y: -top_offset, duration: 0});
                  element.show();
                },
     scroll:$("scrollarea")
  });
  element.model = this;
};


/**
 **/
flow.PalettePart.prototype.createBlock=function()
{
    var func = eval(this.blockClassName);
    return new func(this.app);
};

/**
 *
 **/
flow.PalettePart.prototype.onDragEnd=function()
{
  $$(".dropzone_glow").each(function(element,index){
  element.removeClassName("dropzone_glow");
  });
  $$(".DropZone").each(function(element,index){
  element.addClassName("noSize");
  });
}


/**
 *
 **/
flow.PalettePart.prototype.onDragStart=function()
{
   this.html.block = this.createBlock();
   this.html.block.callInitialDirectEdit = true;
   this.html.block.setCanvas(this.app.getCanvas());
 
   if(this.html.hasClassName("Next"))
   {
   	 $$(".is_Next").each(function(element,index){
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
   	 element.addClassName("dropzone_glow");
   	 }.bind(this));
   }

   $$(".DropZone").each(function(element,index){
     element.removeClassName("noSize");
   });

   this.html.addClassName("topLevel");
};