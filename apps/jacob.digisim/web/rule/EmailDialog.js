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

function EmailDialog(figure /*:Figure*/)
{
  this.figure = figure;
  this.inputDiv = null
  this.inputTo = null;
  this.inputSubject = null;
  this.inputMessage = null;
  this.currentElement=null;
  Dialog.call(this);
  this.setDimension(610,520);
  return this;
}
EmailDialog.prototype = new Dialog;
EmailDialog.prototype.type="EmailDialog";


EmailDialog.prototype.createHTMLElement=function()
{
  var item = Dialog.prototype.createHTMLElement.call(this);
  this.leftOffset = 160;

  this.inputDiv = document.createElement("form");
  this.inputDiv.style.position="absolute";
  this.inputDiv.style.left = "0px";
  this.inputDiv.style.top = "0px";
  this.inputDiv.style.width="375px";
  this.inputDiv.style.font="normal 10px verdana";
  item.appendChild(this.inputDiv);

  // Add the TO part to the dialog
  //
  var label = document.createElement("div");
  label.style.top  = "25px";
  label.style.left = this.leftOffset+"px";
  label.style.position = "absolute";
  label.innerHTML="To:";
  this.inputDiv.appendChild(label);

  this.inputTo = document.createElement("input");
  this.inputTo.dialog = this;
  this.inputTo.style.border="1px solid gray";
  this.inputTo.style.position = "absolute";
  this.inputTo.style.font="normal 10px verdana";
  this.inputTo.style.top="40px";
  this.inputTo.style.left=this.leftOffset+"px";
  this.inputTo.type="text";
  this.inputTo.style.marginBottom="10px";
  this.inputTo.onfocus=function(){this.dialog.currentElement=this;}
  var value = this.figure.getProperty("parameter_0");
  if(value)
    this.inputTo.value = value;
  else
    this.inputTo.value = "";
  this.inputDiv.appendChild(this.inputTo);

  // Add the SUBJECT to the screen
  //
  label = document.createElement("div");
  label.style.top  = "65px";
  label.style.left = this.leftOffset+"px";
  label.style.position = "absolute";
  label.innerHTML="Subject:";
  this.inputDiv.appendChild(label);

  this.inputSubject = document.createElement("input");
  this.inputSubject.dialog = this;
  this.inputSubject.style.border="1px solid gray";
  this.inputSubject.onfocus=function(){this.dialog.currentElement=this;}
  this.inputSubject.style.font="normal 10px verdana";
  this.inputSubject.style.position = "absolute";
  this.inputSubject.style.top="80px";
  this.inputSubject.style.left=this.leftOffset+"px";
  this.inputSubject.type="text";
  this.inputSubject.style.marginBottom="10px";
  value = this.figure.getProperty("parameter_1");
  if(value)
    this.inputSubject.value = value;
  else
    this.inputSubject.value = "";
  this.inputDiv.appendChild(this.inputSubject);

  // Add the MESSAGE to the screen
  //
  label = document.createElement("div");
  label.style.top  = "105px";
  label.style.left = this.leftOffset+"px";
  label.style.position = "absolute";
  label.innerHTML="Message:";
  this.inputDiv.appendChild(label);

  this.inputMessage = document.createElement("textarea");
  this.inputMessage.dialog = this;
  this.inputMessage.onfocus=function(){this.dialog.currentElement=this;}
  this.inputMessage.style.border="1px solid gray";
  this.inputMessage.style.font="normal 10px verdana";
  this.inputMessage.style.height="350px";
  this.inputMessage.style.position = "absolute";
  this.inputMessage.style.top="120px";
  this.inputMessage.style.left=this.leftOffset+"px";
  value = this.figure.getProperty("parameter_2");
  if(value)
    this.inputMessage.value = value;
  else
    this.inputMessage.value = "";
  this.inputDiv.appendChild(this.inputMessage);

  this.listbox = document.createElement("select");
  this.listbox.style.position="absolute";
  this.listbox.style.overflow="auto";
  this.listbox.size=2;
  this.inputDiv.appendChild(this.listbox);

  this.createList();

  return item;
}

EmailDialog.prototype.setDimension=function( w /*:int*/, h /*:int*/)
{
  Dialog.prototype.setDimension.call(this,w,h);
  if(this.inputDiv!=null)
  {
    this.inputDiv.style.width=""+(this.getWidth()-20)+"px";
    this.inputMessage.style.width=""+(this.getWidth()-this.leftOffset-20)+"px";
    this.inputSubject.style.width=""+(this.getWidth()-this.leftOffset-20)+"px";
    this.inputTo.style.width=""+(this.getWidth()-this.leftOffset-20)+"px";
    this.listbox.style.top = this.titlebar.style.height;
    this.listbox.style.height= (this.getHeight()-parseInt(this.titlebar.style.height)-parseInt(this.buttonbar.style.height)-2)+"px";
    this.listbox.style.width  = (this.leftOffset-15)+"px";
  }
}

EmailDialog.prototype.onOk=function()
{
  Dialog.prototype.onOk.call(this);
  this.figure.setProperty("parameter_0", this.inputTo.value);
  this.figure.setProperty("parameter_1", this.inputSubject.value);
  this.figure.setProperty("parameter_2", this.inputMessage.value);
}

EmailDialog.prototype.createList=function()
{
    var oThis = this;
    if (window.XMLHttpRequest) req = new XMLHttpRequest();
    else if (window.ActiveXObject) req = new ActiveXObject("Microsoft.XMLHTTP");
    else return; // fall on our sword
    req.open("POST", "getAliasField.jsp?browser="+browserId);
    req.setRequestHeader('content-type', 'text/plain');
    req.onreadystatechange = function ()
    {
        if (req.readyState == 4) 
        {
          var values = eval(req.responseText);
          for(key in values)
          {
            var value = values[key];
            var node = document.createElement("option");
            node.value = value;
            node.style.font="normal 9px verdana";
            node.ondblclick=function(){oThis.insert("db_field("+this.value+")");};
            node.appendChild(document.createTextNode(value));
            oThis.listbox.appendChild(node);
          }
        }
    };
    req.send(null);
}


EmailDialog.prototype.insert=function(/*:String */ value)
{
  if(this.currentElement==null)
    return;
  this.currentElement.focus();
  if(typeof document.selection != 'undefined') {
    var range = document.selection.createRange();
    range.text = value;
    range = document.selection.createRange();
    range.moveStart('character',value.length);
    range.select();
  }
  else if(typeof this.currentElement.selectionStart != 'undefined')
  {
    var start = this.currentElement.selectionStart;
    var end = this.currentElement.selectionEnd;
    this.currentElement.value = this.currentElement.value.substr(0, start) + value + this.currentElement.value.substr(end);
    var pos = start +value.length ;
    this.currentElement.selectionStart = pos;
    this.currentElement.selectionEnd = pos;
  }
}
