
flow.Canvas=function(/*String*/ id, /*String*/ scrollDivID)
{
  /** @private **/
  this.html  = $(id);
  this.scroll = $(scrollDivID);
  this.html.observe("click",function(){	$$(".menu").each(Element.remove);});
  
  this.blocks = [];
  
  Droppables.add(id, 
  { 
    accept: ["palette_part","AbstractBlock"],
    onDrop: this.onDroppedPalettePart.bind(this)
  });
};



/**
 * Set the new id of the cloude node element.
 *
 * @param {String} if The new id of the model element
 * @private
 **/
flow.Canvas.prototype.onDroppedPalettePart=function(/*:HTMLElement*/ dragged, /*:HTMLElement*/ dropped, /*:Event*/ event )
{
    if(dragged.model===undefined)
      return;

	var p1 = dragged.cumulativeOffset();
	var p2 = this.html.cumulativeOffset();
	
	var block = dragged.model.createBlock();
	var x = p1.left - p2.left;
	var y = p1.top - p2.top;
	block.setPosition(x, y);
	this.addBlock(block);
	block.startDirectEdit();
};

flow.Canvas.prototype.addBlock=function(/*:flow.AbstractBlock*/ block)
{
    if(this.blocks.indexOf(block)!==-1)
       return;

	var html = block.setCanvas(this);
	this.html.appendChild(html);
	this.blocks.push(block);
    block.layout();
};

flow.Canvas.prototype.removeBlock=function(/*:flow.AbstractBlock*/ block)
{
    if(this.blocks.indexOf(block)===-1)
       return;
       
	block.html.remove();
	this.blocks = this.blocks.without(block);
};


flow.Canvas.prototype.getScrollX=function()
{
//	return this.scroll.
};

flow.Canvas.prototype.getScrollY=function()
{

};
