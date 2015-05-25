/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
fader = new Array();

var allreadyResized =[];
function resizeContainer(element)
{
   if(allreadyResized.indexOf(element)!==-1)
      return;
   allreadyResized.push(element);
   // erst alle kinder anpassen damit das Element seine Grˆﬂe korrekt berchnen kann
   //
  element.select(".resize").each(resizeContainer);
  var resizeMode = element.getAttribute("resizeMode");

  if(resizeMode==="booth" || resizeMode==="height")
	  element.style.height=(Math.max(element.scrollHeight,element.offsetHeight)+10)+"px";

  if(resizeMode==="booth" || resizeMode==="width")
	  element.style.width=(Math.max(element.scrollWidth,element.offsetWidth)+10)+"px";

  element.style.overflow="hidden";
}

/** Klappt leider nicht wenn der Bereich der sich vergrˆﬂern soll Bilder enth‰lt.
    Grund: Das Event wird VOR dem Laden der Bilder ausgelˆst. Somit ist die Seite
    eigentlich nicht nicht richtig layouted. "onload" ist zwar nicht so schˆn, da
    man erst auf alle Bilder und Resource (auch fremde) laden muss. Der Browser kann
    an dieser Stelle somit nicht parallel arbeiten. Nachteil bei Chrome, FF und Safarie.
    IE ist an dieser Stelle eh sau doof und es wird hier somit noch einen tick langsamer.

document.observe('dom:loaded', function()
{
  $$(".resize").each(resizeContainer);
});
**/
Event.observe(window,'load', function()
{
  $$(".resize").each(resizeContainer);

  /*
  $$(".listbox_selected").each(function (element)
  {
       var toggleIcon = new Element('img',{src:"./themes/"+userTheme+"/images/expand_ui.png",
                                           position:"absolute",
                                           style:"position:absolute;top:0px;left:0px;cursor:pointer"
                                           });
       element.appendChild(toggleIcon);
       toggleIcon.expanded=false;
       toggleIcon.originalParent=element;
       toggleIcon.table = element.select("table")[0];
       Event.observe(toggleIcon,"click",function(event)
       {
          if(this.expanded)
          {
              this.originalParent.appendChild(this.table);
              this.originalParent.appendChild(this);
              this.overlay.remove();
              $("navigationbar").show();
              $("eventForm").show();
          }
          else
          {
              this.overlay = new Element("div",{style:"overflow:scroll;width:100%;height:100%"});
              $("body").appendChild(this.overlay);
              this.overlay.appendChild(this.table);
              this.overlay.appendChild(this);
            
              $("navigationbar").hide();
              $("eventForm").hide();
          }
          this.expanded=!this.expanded;
       }.bind(toggleIcon));
  });
  */
});

 
function marchingAnts(/*: String*/ jacobId)
{
  var element = $(jacobId);
  if(element!=null)
  {
     // for pretty browser;
     var container = $("container_"+jacobId);
     if(container!=null)
       element = container;
     var top = new Element("div");
     top.style.top=(parseInt(element.style.top)-5)+"px";
     top.style.left=(parseInt(element.style.left)-5)+"px";
     top.style.width=(parseInt(element.style.width)+10)+"px";
     top.style.height="2px";
     top.style.position="absolute";
     top.style.overflow="hidden";
     top.style.background="url('./themes/"+userTheme+"/images/ants-horizontal.gif') repeat-x scroll 0 -6px transparent";
     element.parentNode.appendChild(top);
     
     var bottom = new Element("div");
     bottom.style.top=(parseInt(element.style.height)+parseInt(element.style.top)+5)+"px";
     bottom.style.left=(parseInt(element.style.left)-5)+"px";
     bottom.style.width=(parseInt(element.style.width)+10)+"px";
     bottom.style.height="2px";
     bottom.style.position="absolute";
     bottom.style.overflow="hidden";
     bottom.style.background="url('./themes/"+userTheme+"/images/ants-horizontal.gif') repeat-x scroll 0 0 transparent";
     element.parentNode.appendChild(bottom);

     var right = new Element("div");
     right.style.top=(parseInt(element.style.top)-5)+"px";
     right.style.left=(parseInt(element.style.width)+parseInt(element.style.left)+5)+"px";
     right.style.width="2px";
     right.style.height=(parseInt(element.style.height)+10)+"px";
     right.style.position="absolute";
     right.style.overflow="hidden";
     right.style.background="url('./themes/"+userTheme+"/images/ants-vertical.gif') repeat-y scroll 0 0 transparent";
     element.parentNode.appendChild(right);

     var left = new Element("div");
     left.style.top=(parseInt(element.style.top)-5)+"px";
     left.style.left=(parseInt(element.style.left)-5)+"px";
     left.style.width="2px";
     left.style.height=(parseInt(element.style.height)+10)+"px";
     left.style.position="absolute";
     left.style.overflow="hidden";
     left.style.background="url('./themes/"+userTheme+"/images/ants-vertical.gif') repeat-y scroll -6px 0 transparent";
     element.parentNode.appendChild(left);

     new fx.Scroll('formDiv', {duration: 2000}).toElement(element);
   }
}

function initBehaviourListbox(boxId, scrollValueId)
{
  var list = getObj("container_j_"+boxId);
  if(list==null)
   return;

  var values =  getObj(scrollValueId).value.split(":");
  list.scrollTop  = values[0];
  list.scrollLeft = values[1];

  list.valueId = scrollValueId;
  list['onscroll']= function()
  {
    getObj(this.valueId).value=this.scrollTop+":"+this.scrollLeft;
  };

  var icon = getObj("fade_"+boxId);
  if(icon==null)
   return;
  icon.listboxId = list.id;
  icon.style.cursor="pointer";
  fader["container_j_"+boxId]=new fx.Opacity(icon);
  fader["fade_"+boxId] = fader["container_j_"+boxId];
  fader["container_j_"+boxId].setOpacity(0.001);

  // Falls die Liste keine Eintr‰ge hat braucht man auch nicht das Menu einblenden
  // d.h. Die fadeIn/fadeOut Methoden brauchen nicht initialisiert werden.
  //
  var child = list ? list.firstChild : null;
  while (child) 
  {
    if(child.nodeType == 1 && (child.nodeName.toLowerCase() == "div"))
       break;
    child = child.nextSibling;
  }
  if(child==null)
    return;

   // Die Liste enth‰lt elemente => fadeIn/fadeOut initialisieren
   //
   list['onmouseover']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].setOpacity(0.9);
   };
   list['onmouseout']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].custom(0.9,0.001);
   };

   icon['onmouseover']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].setOpacity(0.9);
   };
   icon['onmouseout']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].custom(0.9,0.001);
   };

   // falls der Scrollbar sichtbar ist werden die Icons nach links verschoben.
   //
   if(list.scrollHeight>list.clientHeight)
   {
     var scrollbarWidth = parseInt(list.style.width)-list.clientWidth;
     icon.style.left=(parseInt(icon.style.left)-scrollbarWidth)+"px";
   }
}

function initBehaviourImage(imgId)
{
  var obj = getObj("fade_"+imgId);
  if(obj==null)
   return;
	fader["fade_"+imgId]=new fx.Opacity(obj);
	fader["fade_"+imgId].setOpacity(0.001);
	
	obj['onmouseover']= function()
	        {
						fader[this.id].clearTimer();
				    fader[this.id].setOpacity(0.8);
				  };
	obj['onmouseout']= function()
	        {
						fader[this.id].clearTimer();
				    fader[this.id].custom(0.8,0.001);
					};
}

function initBehaviourLongText(textId, caretId, startId, endId)
{
   var text = getObj("j_"+textId);

   if(text==null)
      return;
   var _startId=startId;
   var _endId = endId;
   var _caretId = caretId;
   
   // Cursor Position bei einem ReadOnly macht keinen Sinn und kostet Performance
   //
   if(!text.readOnly && text.tagName.toLowerCase()==="textarea")
   {
	   text['onselect']= function()
	   {
	      updateTextareaSelection(this,_caretId, _startId, _endId);
	   };
	   text['onclick']= function()
	   {
	      updateTextareaSelection(this,_caretId, _startId, _endId);
	   };
	   text['onkeyup']= function()
	   {
	      updateTextareaSelection(this,_caretId, _startId, _endId);
	   };
   }
   
   var contentType=  text.getAttribute("contentType");
   if(contentType==="text/html")
   {            
      var top  = parseInt(text.style.top);
      var left = parseInt(text.style.left);
      var height = parseInt(text.style.height)-26; // 26 == Editor Toolbar Height. Anstatt teuer dyn. zu berechnen nehme ich hier den einmalig bestimmten Wert
      var div = $(document.createElement('div'));
      div.style.top=top+"px";
      div.style.left=left+"px";
      div.style.position="absolute";
      text.parentNode.appendChild(div);
      div.appendChild(text);
	  new nicEditor({iconsPath : './themes/'+userTheme+'/images/nicEditorIcons.gif',maxHeight : height}).panelInstance("j_"+textId);
   }
      
      
   var icon = getObj("fade_"+textId);
   if(icon===null)
      return;

   icon.textareaId = text.id;
   icon.style.cursor="pointer";
   fader["j_"+textId]=new fx.Opacity(icon);
   fader["fade_"+textId] = fader["j_"+textId];
   fader["j_"+textId].setOpacity(0.001);
	

   text['onmouseover']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].setOpacity(0.8);
   };
   text['onmouseout']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].custom(0.8,0.001);
   };
   icon['onmouseover']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].setOpacity(0.8);
   };
   icon['onmouseout']= function()
   {
      fader[this.id].clearTimer();
      fader[this.id].custom(0.8,0.001);
   };
   icon['onclick']= function()
   {
      pop = window.open();
      pop.document.open();
      if(getObj(this.textareaId).value === undefined)
     	   pop.document.write("<html><body>"+getObj(this.textareaId).innerHTML+"</body><script>window.print();</script></html>");
      else
         pop.document.write("<html><body><pre>"+getObj(this.textareaId).value+"</pre></body><script>window.print();</script></html>");
      pop.document.close();
   };
}

function initBehaviourDiverse(textId)
{
   var text = $(textId);
   if(text==null)
      return;

   text.addClassName("diverse");

   if(!text.readOnly)
   {
    // radio button group
    if(text.type=="checkbox")
    {
            text.hide();
            var top  = parseInt(text.style.top)+3;
            var left = parseInt(text.style.left)+3;
            var div = $(document.createElement('div'));
            div.style.top=top+"px";
            div.style.left=left+"px";
            div.setAttribute("name", "diverse_"+textId);
            div.style.position="absolute";
            div.className="diverse_radiobutton";
            text.parentNode.appendChild(div);
            div['onclick']= function()
            {
               this.remove();
               var name = this.getAttribute("name");
               var mainId = name.substring(8);
               $(mainId).show();
               $(mainId).click();
            }
    }
    else if(text.type=="radio")
    {
      var mainId = textId.substring(8);
      for(var i=0;;i++)
      {
         var newId = "radio_"+i+"_"+mainId;
         var button = $(newId);
         if(button)
         {
            button.hide();
            var top = parseInt(button.style.top)+3;
            var div = $(document.createElement('div'));
            div.style.top=top+"px";
            div.style.left="3px";
            div.setAttribute("name", "diverse_"+mainId);
            div.setAttribute("index",i);
            div.style.position="absolute";
            div.className="diverse_radiobutton diverse_"+mainId;
            $(mainId).parentNode.appendChild(div);
            div['onclick']= function()
            {
               $$("."+this.getAttribute("name")).each(function(s){s.remove();});
               for(var i=0;;i++)
               {
                  var newId = "radio_"+i+"_"+mainId;
                  var button = $(newId);
                  if(button)
                    button.show();
                  else
                    break;
               }
               $("radio_"+this.getAttribute("index")+"_"+mainId).click();
               updateRadioButtonGroup(mainId);
            };
         }
         else
         {
            break;
         }
      }
    }
    else
    {
      text['onkeyup']= function(e)
      {
          text.value="";
          text['onkeyup']= function(){};
          text['onclick']= function(){};
          text.removeClassName("diverse");
          return true;
      };
      text['onclick']= function()
      {
        this.focus();
        this.select();
      };
    }
  }
}

function initBehaviourDocument(docId)
{	
  var obj = getObj(docId);
  var menu = getObj('document_menu_'+docId.replace("container_j_",""));
  // Falls kein Menu vorhanden ist, dann brauchen wir auch das
  // ganze mouseover-Gerummsel nicht
  //
  if(menu==null)
     return;
	
	obj['onmouseover']= function()
	         {
						try{
						  var obj = getObj('docimg_'+this.id);
						  if(obj==null)
		  					return;
		  				obj.oldSrc=obj.src;	
		  				obj.src="./themes/menubutton.png";
					    }catch(e){}
						};
						
	obj['onmouseout']= function()
	        {
					  try{
						  var obj = getObj('docimg_'+this.id);
						  if(obj==null)
		  					return;
		  				obj.src=obj.oldSrc;	
				    }catch(e){}
					};
					
	obj['onclick']= function(event){
					  try{
						hideContextMenu();
						event = window.event||event ;
						Event.stop(event);
					    	var pos = getMousePos();
						currentMenu = new LayerObject('document_menu_'+docId.replace("container_j_",""));
						currentMenu.show();
						currentMenu.moveTo(pos.x,pos.y);
						new fx.Opacity(currentMenu.obj).setOpacity(0.9);
				    	}catch(e){alert(e)}
					};
}


function FloatingElement(/*:String*/ id)
{
   this.div = $(id);
   this.formDiv = $("formDiv");  
   this.originalTop  = parseInt(this.div.style.top);
   var elementOffset=this.formDiv.cumulativeOffset();
   var containerOffset=this.div.cumulativeOffset();
   this.diff = containerOffset.top - elementOffset.top;
   Event.observe(this.formDiv, "scroll", this.messure.bind(this));
   var oThis = this;
   this.div.ancestors().each(function(element)
   {
     var style = element.getStyle("background-color");
      if(style !== "" && style!=="transparent")
      {
        oThis.div.setStyle({backgroundColor:style});
        throw $break;  
      }
   });
};


FloatingElement.prototype.messure=function()
{
    var top = this.formDiv.scrollTop;
    if(top >this.diff)
      this.div.setStyle({top:top});
    else
      this.div.setStyle({top:this.originalTop});
};  
