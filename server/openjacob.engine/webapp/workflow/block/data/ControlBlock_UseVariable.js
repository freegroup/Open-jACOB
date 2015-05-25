flow.ControlBlock_UseVariable=function(/*flow.Application*/ app, /*:String*/ id)
{
  flow.ControlBlock_AbstractData.call(this,app,"de.tif.jacob.util.flow.block.variable.UseVariable", id);

  this.group = "Variable";
  this.property = "variable name";
};

/** @private **/
flow.ControlBlock_UseVariable.prototype = new flow.ControlBlock_AbstractData();
/** @private **/
flow.ControlBlock_UseVariable.prototype.type="flow.ControlBlock_UseVariable";



flow.ControlBlock_UseVariable.prototype.renameVariable=function(/*:String*/ oldVarName, /*:String*/ newVarName)
{
    // it is not possible to rename predefined Variables. Predefined Variables are set by the app-programmer
    // via script.
    if(this.app.getPredefinedVariables().indexOf(oldVarName)!==-1)
      return; // silently

   flow.ControlBlock_AbstractData.prototype.renameVariable.call(this, oldVarName, newVarName);
   if(this.property===oldVarName)
   {
      this.property=newVarName;
      this.layout();
   }
};


flow.ControlBlock_UseVariable.prototype.isDataProvider=function()
{
   return true;
};

flow.ControlBlock_UseVariable.prototype.createHTML=function()
{
   var html =flow.ControlBlock_AbstractData.prototype.createHTML.call(this);
   this.selector = new Element("div",{"class":this.getCSSBaseClassName()+"_selector"}).update("&or;");
   this.selector.hide();
   this.selector.title="Click to use existing variable";
   this.html.observe('mouseenter', function(e) 
   {
     this.selector.appear({ duration: 0.3 });
   }.bind(this));

   this.html.observe('mouseleave', function(e) 
   {
     this.selector.fade({ duration: 0.3 });
   }.bind(this));

   this.selector.observe('click', function(e) 
   {
      $$(".menu").each(Element.remove);
      e.stop();
      var elements = this.app.getAllVariables();
      
      var list = new Element('ul',{className:"menu "+this.getCSSBaseClassName()+"_selectormenu"});
      elements.each(function(item) 
      {
          var e_li = new Element('li', {className: ''});
          var e_a  = new Element('a', { href: '#',title: item, className: ''}).update(item);
          e_a.observe("click",function(item, event)
          {
            event.stop();
            $$(".menu").each(Element.remove);
            this.setProperty(item);
          }.bind(this, item));
          
          list.insert(e_li.insert(e_a));
      }.bind(this));
      var pos1 = this.selector.cumulativeOffset();
      var pos2 = this.canvas.html.cumulativeOffset();
      pos1.left -= pos2.left;
      pos1.top -= pos2.top;
      list.hide();
      this.canvas.html.appendChild(list);
      list.setStyle(pos1);
      new Effect.Appear(list, {duration:0.5, from:0.1, to:1.0});
   }.bind(this));

   this.html.appendChild(this.selector);
   return html;
};