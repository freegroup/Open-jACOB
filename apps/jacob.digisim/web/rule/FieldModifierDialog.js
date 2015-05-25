/* This notice must be untouched at all times.

Open-jACOB Draw2D
The latest version is available at
http://www.openjacob.org

Copyright (c) 2006 Andreas Herz. All rights reserved.
Created 5. 11. 2006 by Andreas Herz (Web: http://www.freegroup.de )

LICENSE: LGPL

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License (LGPL) as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA,
or see http://www.gnu.org/copyleft/lesser.html
*/

function FieldModifierDialog(figure /*:Figure*/)
{
  this.figure = figure;
  this.inputDiv = null
  this.inputTo = null;
  this.inputSubject = null;
  this.inputMessage = null;
  this.paramInput1 = null;
  this.paramInput2 = null;
  this.paramInput3 = null;
  this.paramInput4 = null;

  this.paramLabel1 = null;
  this.paramLabel2 = null;
  this.paramLabel3 = null;
  this.paramLabel4 = null;

  this.currentHighlight = null;
  this.method2HTML = new Object();

  this.selectedClass = null;
  this.selectedMethod= null;
  this.selectedParamCount=0;

  Dialog.call(this);
  this.setDimension(600,320);
  this.decision2Description= new Object();

  return this;
}
FieldModifierDialog.prototype = new Dialog;
FieldModifierDialog.prototype.type="FieldModifierDialog";


FieldModifierDialog.prototype.createHTMLElement=function()
{
  var item = Dialog.prototype.createHTMLElement.call(this);

  var label = document.createElement("div");
  label.innerHTML ="Modifier";
  label.style.left = "10px";
  label.style.top  = "30px";
  label.style.width= "100px";
  label.style.height="30px";
  label.style.position ="absolute";
  item.appendChild(label);

  this.inputDiv = document.createElement("div");
  this.inputDiv.style.position="absolute";
  this.inputDiv.style.left = "10px";
  this.inputDiv.style.top = "50px";
  this.inputDiv.style.width="200px";
  this.inputDiv.style.height="200px";
  this.inputDiv.style.overflow="auto";
  this.inputDiv.style.border="1px solid black";
  this.inputDiv.style.font="normal 10px verdana";
  item.appendChild(this.inputDiv);

  this.createList(this.inputDiv);

  // create the right side with the parameter fields
  //
  this.paramInput1 = this.createInputElement(320,50);
  this.paramLabel1 = this.createLabelElement("DB Field",225,52);
  item.appendChild(this.paramInput1);
  item.appendChild(this.paramLabel1);

  this.paramInput2 = this.createInputElement(320,80);
  this.paramLabel2 = this.createLabelElement("Value",225,82);
  item.appendChild(this.paramInput2);
  item.appendChild(this.paramLabel2);

  this.paramInput3 = this.createInputElement(320,110);
  this.paramLabel3 = this.createLabelElement("Parameter 3",225,112);
  item.appendChild(this.paramInput3);
  item.appendChild(this.paramLabel3);

  this.paramInput4 = this.createInputElement(320,140);
  this.paramLabel4 = this.createLabelElement("Parameter 4",225,142);
  item.appendChild(this.paramInput4);
  item.appendChild(this.paramLabel4);

  if(this.figure.getProperty("parameter_0")!=null)
    this.paramInput1.value = this.figure.getProperty("parameter_0");
  if(this.figure.getProperty("parameter_1")!=null)
    this.paramInput2.value = this.figure.getProperty("parameter_1");
  if(this.figure.getProperty("parameter_2")!=null)
    this.paramInput3.value = this.figure.getProperty("parameter_2");
  if(this.figure.getProperty("parameter_3")!=null)
    this.paramInput4.value = this.figure.getProperty("parameter_3");


  for(this.selectedParamCount=0;this.selectedParamCount<4;this.selectedParamCount++)
  {
    if(this.figure.getProperty("parameter_"+this.selectedParamCount)==null)
      break;
  }

  this.autosuggest1 = new AutoSuggestControl(item,this.paramInput1);
  this.autosuggest2 = new AutoSuggestControl(item,this.paramInput2);
  this.autosuggest3 = new AutoSuggestControl(item,this.paramInput3);
  this.autosuggest4 = new AutoSuggestControl(item,this.paramInput4);


  this.selectedClass    = this.figure.getProperty("businessClass");
  this.selectedMethod   = this.figure.getProperty("methodName");
  this.currentHighlight = this.method2HTML[this.selectedClass+"."+this.selectedMethod];
  if(this.currentHighlight)
    this.currentHighlight.onclick();

  return item;
}

FieldModifierDialog.prototype.setDimension=function( w /*:int*/, h /*:int*/)
{
  Dialog.prototype.setDimension.call(this,w,h);
  if(this.inputDiv!=null)
  {
    this.inputDiv.style.height=(h-100)+"px";
  }
}

FieldModifierDialog.prototype.onOk=function()
{
  Dialog.prototype.onOk.call(this);
  this.figure.setProperty("parameter_0", null);
  this.figure.setProperty("parameter_1", null);
  this.figure.setProperty("parameter_2", null);
  this.figure.setProperty("parameter_3", null);
  switch(this.selectedParamCount)
  {
    case 4:
      this.figure.setProperty("parameter_3", this.paramInput4.value);
    case 3:
      this.figure.setProperty("parameter_2", this.paramInput3.value);
    case 2:
      this.figure.setProperty("parameter_1", this.paramInput2.value);
    case 1:
      this.figure.setProperty("parameter_0", this.paramInput1.value);
  }
  this.figure.setBusinessClass(this.selectedClass, this.selectedMethod);
}


FieldModifierDialog.prototype.createList=function(parentDiv /*:HTMLElement*/)
{
  var oThis = this;

  // String Decision
  //
  var header = document.createElement("h1");
  header.innerHTML="Record Modifier";
  header.style.fontSize="10pt";
  header.style.marginLeft="5px";
  parentDiv.appendChild(header);

  var list = document.createElement("ul");
  var node = document.createElement("li");
  node.dialog = this;
  node.onclick=function(){this.dialog.select(this,"de.tif.jacob.ruleengine.bo.FieldModifier","setFieldValue",new Array("Table Alias Field","Value"),new Array("getAliasField","getDB_Field"))};
  node.appendChild(document.createTextNode("Set Field Value"));
  list.appendChild(node);
  this.method2HTML["de.tif.jacob.ruleengine.bo.FieldModifier.setFieldValue"]=node;

  var node = document.createElement("li");
  node.dialog = this;
  node.onclick=function(){this.dialog.select(this,"de.tif.jacob.ruleengine.bo.FieldModifier","setLinkedRecord",new Array("Local Table","Foreign Table","Foreign Pkey"),new Array("getAlias","getAlias","getField"))};
  node.appendChild(document.createTextNode("Set Linked Record"));
  list.appendChild(node);
  this.method2HTML["de.tif.jacob.ruleengine.bo.FieldModifier.setLinkedRecord"]=node;


  parentDiv.appendChild(list);
}

FieldModifierDialog.prototype.select=function(/*:HTMLElement*/element , /*:String*/ bo, /*:String*/ method , /*:Array*/ paramNames,  /*:Array*/providerNames)
{
  var paramCount = paramNames.length;

  this.selectedClass  = bo;
  this.selectedMethod = method;
  this.selectedParamCount = paramCount;

  if(this.currentHighlight!=null)
    this.currentHighlight.style.fontWeight="normal";
  this.currentHighlight = element;
  if(this.currentHighlight!=null)
    this.currentHighlight.style.fontWeight="bold";
  this.paramInput1.style.display="none";
  this.paramInput2.style.display="none";
  this.paramInput3.style.display="none";
  this.paramInput4.style.display="none";
  this.paramInput2.style.display="none";
  this.paramInput3.style.display="none";
  this.paramInput4.style.display="none";
  this.paramLabel1.style.display="none";
  this.paramLabel2.style.display="none";
  this.paramLabel3.style.display="none";
  this.paramLabel4.style.display="none";
  switch(paramCount)
  {
    case 4:
      this.paramInput4.style.display="block";
      this.paramLabel4.style.display="block";
      this.paramLabel4.innerHTML=paramNames[3];
      this.autosuggest4.setProvider(providerNames[3]);
    case 3:
      this.paramInput3.style.display="block";
      this.paramLabel3.style.display="block";
      this.paramLabel3.innerHTML=paramNames[2];
      this.autosuggest3.setProvider(providerNames[2]);
    case 2:
      this.paramInput2.style.display="block";
      this.paramLabel2.style.display="block";
      this.paramLabel2.innerHTML=paramNames[1];
      this.autosuggest2.setProvider(providerNames[1]);
    case 1:
      this.paramInput1.style.display="block";
      this.paramLabel1.style.display="block";
      this.paramLabel1.innerHTML=paramNames[0];
      this.autosuggest1.setProvider(providerNames[0]);
  }
  switch(paramCount)
  {
    case 4:
    case 3:
      this.paramInput4.value="";
      break;
    case 2:
      this.paramInput4.value="";
      this.paramInput3.value="";
      break;
    case 1:
      this.paramInput4.value="";
      this.paramInput3.value="";
      this.paramInput2.value="";
      break;
  }
}

FieldModifierDialog.prototype.createInputElement=function(x /**:int*/, y /*:int*/)
{
  var element = document.createElement("input");
  element.type="text";
  element.style.border="1px solid black";
  element.style.width="260px";
  element.style.left = x+"px";
  element.style.top  = y+"px";
  element.style.font="normal 11px verdana";
  element.style.paddingLeft="5px";
  element.style.position ="absolute";

  return element;
}

FieldModifierDialog.prototype.createLabelElement=function(text /*:String*/, x /**:int*/, y /*:int*/)
{
  var element = document.createElement("div");
  element.style.left = x+"px";
  element.style.top  = y+"px";
  element.style.font="normal 11px verdana";
  element.style.position ="absolute";
  element.innerHTML=text;
  return element;
}