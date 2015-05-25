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
var RCS_ID_COMMON = "$Id: ie_common.js,v 1.68 2010/08/04 13:14:16 freegroup Exp $";

var currentButton=null;                // PUBLIC
var currentButtonArea=null;            // PUBLIC
var fireEventEmitter=false;            // PUBLIC
var isVisible=false;                   // PUBLIC
var windowsToShow = new Array();       // PUBLIC
var reloadPage=true;                   // PUBLIC
var lastFocusElement=null;             // PUBLIC
var isLoggedOutBySystem=false;         // PUBLIC
var currentMenu  = null;               // PUBLIC
var globalMouseX = 300;                // PUBLIC
var globalMouseY = 300;                // PUBLIC

var fader = new Array();               // private
var helpModeIsOn=false;                // private 
var lastSelectedText=null;             // private
var inlineForeignFieldIsVisible=false; // private
var fireEventIsInProcess=false;        // private
var searchBrowser=null;
var searchBrowserId=null;
var BROWSER_IE = true;
var BROWSER_NS = false;
var statusMessageUpdater =null;

// PUBLIC
// DEPRECATED
function getMousePos()
{
  var pos = new Object();
  pos.x=globalMouseX;
  pos.y=globalMouseY;
  return pos;
}

/***************************************************
 * Stringfunktionen des Browsers erweitern
 *
 * PRIVATE
 ***************************************************/
String.prototype.trim = function( )
{
  return( this.replace(new RegExp("^([\\s]+)|([\\s]+)$", "gm"), "") );
};


/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** Wechselt zwischen der Unsichtbaren/Sichtbaren Seite.                                **/
/** Toolfunktionen welche fuer das Flickerfree page reload benï¿½tigt werden.              **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function flipPage()
{
    var teaserHeight = document.getElementById('teaser').currentStyle['height'];
    if(!teaserHeight)
        teaserHeight="0";

    var outlookbar=getObj('outlookbarButtonArea');
    if(outlookbar)
        outlookbar.scrollTop=getObj('domainScrollPos').value;

    if(browserVisible==true && searchBrowserId!=null)
    {
       searchBrowser = new jACOBTable(searchBrowserId);
       resizeSearchBrowserCallback();
    }

    getObj('formDiv').scrollTop=getObj('formScrollPos').value;

    // speichern in welchem frame die nï¿½chste Seite angezeigt werden muss
    // (hidden/visible page Verfahren wie bei Spiele)
    //
    if(parent.frames['jacob_content1']==window)
    {
        parent.jacob_twinframe.rows=teaserHeight+",100%,0";
        getObj('eventForm').target='jacob_content2';
    }
    else
    {
        parent.jacob_twinframe.rows=teaserHeight+",0,100%";
        getObj('eventForm').target='jacob_content1';
    }
    parent.contentFrame=window;

    afterPageLoad();

    if(window['foreingField'])
        foreingField();

    // Wird benoetigt, da bei dem modifizieren des DOM Baumes die Eventhandler 'irgendwie' und 'plï¿½tzlich' verloren gehen.
    // Als 'Fallback' muss der Body dann von dem aktive ELement rekusive die Parent-Nodes suchen und nachsehen ob ein Eventhandler
    // uebersprungen wurde. Falls dies der Fall ist, wird der Eventhandler dann von Hand aufgerufen.
    addKeyHandler(document.body);
    document.body.addKeyPress(27,hideForeignField);
    document.body.attachEvent("onclick",hideContextMenu);

    // Die FireEvent methode wird jetzt frei geschaltet. So wird verhindert, dass der Benutzer schon auf ein 
    // Icon/Button klickt bevor die Seite verfï¿½gbar ist. Es *kï¿½nnte* eventuell ein synch. Problem dadurch
    // entstehen. Bentuzer klickt und HTML wird gleichzeitig in dieser Methode modifieziert -> deadlock!?
    // (be save).
    fireEventIsInProcess=false;
    isVisible=true;
    fireEventEmitter=true;

    // array kopieren damit der serverPush die Fenster nicht nochmal anzeigt.
    // Dies kann bei einem "prompt(...)" passieren. Der IE oder FireFox stoppt bei der Verarbeitung
    // eines alert/prompt. Wenn in dieser Zeit ein ServerPush kommt wird auf "windowsToShow" zugegriffen und dies
    // ist dann nicht zurueck gesetzt => die Fenster werden doppelt angezeigt.
    //
    var tmp = windowsToShow;
    windowsToShow = new Array();
    for(a=0;a<tmp.length; a++)
    {
      tmp[a].show();
    }

    setTimeout("new LayerObject('notepadIcon').show(); ",200);
    if(browserVisible==true)
        window.onresize=function(){resizeSearchBrowserCallback();};

    window.status="Server processing:"+serverCalculationTime+" ms";
    Event.observe(document, 'mousemove', function(e)
    {
      globalMouseX = Event.pointerX(e);
      globalMouseY = Event.pointerY(e);
    });
}

/**
 * Hebt ein Element hervor und dimmt dann die Farbe wieder auf
 * Normal herrunter.
 *
 **/
function glow(/*:HTMLElement*/element)
{
    var elem = new LayerObject(element)
    var x = elem.getRuntimeStyle("backgroundColor");
    while(x==undefined || x=="" || x=="transparent")
    {
      x =  elem.getRuntimeStyle("backgroundColor");
      elem =  new LayerObject(elem.obj.parentNode);
    }
    if(x.startsWith('rgb'))
      x = x.rgbToHex();
    new fx.Color(element,'background-color' ).set("#FFFF00");
    new fx.Color(element,'background-color',{duration:4000} ).custom("#FFFF00", x);
}

function ellipsis(/*:HTMLElement*/ e)
{
 e= $(e);
 e.style.textOverflow="ellipsis";
 e.style.overflow="hidden";
 e.style.whiteSpace="nowrap";
 var t = e.innerHTML;
 var title = t.replace(/<br>/g, "");
 e.setAttribute("title",title);
}

function refreshTeaser()
{
   parent.frames['teaser'].location.href="teaser.jsp?browser="+browserId;
}

function visiblePage()
{
    if(parent.frames['jacob_content1']==parent.contentFrame)
        return parent.frames['jacob_content1'];
    else
        return parent.frames['jacob_content2'];
}

function zoomInNotepad()
{
 var obj = new LayerObject('notepadDiv');
 if(!obj.x)
 {   
  var page =new PageSize();
  
  obj.moveTo(page.width-100,page.height-50);
  var icon  = new LayerObject('notepadIcon');
  var div   = new LayerObject('notepadDiv');
  var area  = new LayerObject('notepadTextarea');
  area.show();
  div.show();
  icon.hide();
  setTimeout("zoomInNotepad();",0);
  return;
 }
 if(obj.x<100 || obj.y<100)
 {
  var shadow = new LayerObject('notepadShadow');
  var close = new LayerObject('notepadClose');
  var header = new LayerObject('notepadHeader');
  var area  = new LayerObject('notepadTextarea');
  area.obj.focus();
  close.show();
  header.show();
  shadow.show();
  shadow.moveTo(obj.x+5,obj.y+5);
  shadow.css.height=obj.css.height;
  shadow.css.width=obj.css.width;
  return;
 }
 
// obj.moveBy(-90,-70);
 obj.moveBy(-45,-35);
 var width=parseInt(obj.getRuntimeStyle('width'));
 var height=parseInt(obj.getRuntimeStyle('height'));
 obj.css.height=height+20;
 obj.css.width=width+30;
 
 
 setTimeout("zoomInNotepad();",0);
}

function zoomOutNotepad()
{
 var obj = new LayerObject('notepadDiv');
 obj.css.bottom=0;
 obj.css.right=20;
 obj.css.top="auto";
 obj.css.left="auto";
 obj.css.height=0;
 obj.css.width=0;
 var area   = new LayerObject('notepadTextarea');
 var close  = new LayerObject('notepadClose');
 var header = new LayerObject('notepadHeader');
 var div = new LayerObject('notepadDiv');
 var icon = new LayerObject('notepadIcon');
 var shadow = new LayerObject('notepadShadow');
 
 area.hide();
 div.hide();
 close.hide();
 header.hide();
 shadow.hide();
 icon.show();
 
}


/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** THE EVENT HANDLER TO THE SERVER                                                     **/
/**                                                                                     **/
/** Each GUI element can send an event to the server. This is the one and only function **/
/** to do this.                                                                         **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
/* Fire an event to the server without any event data */

function FireEvent(control, eventId)
{
    if(window.event)
        window.event.cancelBubble=true;

    if(fireEventEmitter != undefined &&  fireEventEmitter==true)
    {
       // lock the second ( or more) server request before the last one isn't back
       //
       if(fireEventIsInProcess==true)
            return;

       if(isLoggedOutBySystem==true)
       {
          new jACOBTransparentMessage(APPLICATION_TERMINATED+"<br><br><a style='font-size:8pt' href='login.jsp'>"+BUTTON_LOGIN+"</a>",false).show();
          getObj('eventForm').onsubmit=function (){return false;};
          return;
       }

       fireEventIsInProcess=true;
       isVisible=false;

       // Disable all form buttons to avoid multiple submit of an request
       //
       // Die Buttons nur disabled falls ein page reload gemacht wird
       // Falls ein Dokument herrunter geladen wird, dann passiert keine page reload
       //
       if(reloadPage==true)
       {
        var toDisableElements=getObj('eventForm').getElementsByTagName('button');
        for (var i = 0; i < toDisableElements.length; i++)
        {
           toDisableElements[i].disabled=true;
        }
      }

       getObj("browser").value = browserId;
       getObj("control").value = control;
       getObj("event").value   = eventId;
       markScrollPosForm(getObj('formDiv'));
       // es ist nicht gesagt, dass es eine outlookbar gibt
       //
       if(getObj('outlookbarButtonArea')!=null)
           markScrollPosDomain(getObj('outlookbarButtonArea'));

       // DON't use "form.submit()"; => The event method [onSubmit] will not be fired! Some GUI ELements
       // has been register an event handler on this event. e.g. The HTML WYSIWYG Editor.
       getObj('submitButton').click();
       // Die Buttons nur disabled falls ein page reload gemacht wird
       // Falls ein Dokument herrunter geladen wird, dann passiert keine page reload
       //
       if(reloadPage==true)
       {
          window.setTimeout("new LayerObject('waitDiv').show();",500);
	        var tmp = function()
	        {
	           new Ajax.Request('statusMessage.jsp',
	           {
	             method:'get',
	             parameters:  "browser="+browserId,
	             onSuccess: function(transport)
	                {
	                  var result = transport.responseText.trim();
	                  if(result.length==0)
	                    statusMessageUpdater.stop();
	                  else
	                    $("statusDivText").innerHTML= result;
	                }
	            });
	          }
	          statusMessageUpdater = new PeriodicalExecuter(tmp, 2);
       }
       else
       {
          reloadPage=true;
          fireEventIsInProcess=false;
          isVisible=true;
          fireEventEmitter=true;
       }
    }
    else if(opener!=null)
    {
        var emitter = opener.parent.frames['jacob_content1'].isVisible==true?opener.parent.frames['jacob_content1']:opener.parent.frames['jacob_content2'];
        var doClose=(window['autoCloseWindow'] && autoCloseWindow==true);
        if(control==window && eventId=='close')
        {
            control = '0'; // control MUST be a number;
            eventId = '0';
            doClose=true;
        }
       emitter.FireEvent(control,eventId);
        // Falls das Fenster eine Variable autoclose=true hat, wird es nach dem FireEventData sofort geschlossen.
        //
        if(doClose)
            window.close();
    }
    else
        alert('error1');
}

/* Fire an event to the server with attached data for the event */
function FireEventData(control, eventId, value)
{
    if(window.event)
        window.event.cancelBubble=true;

    // ich bin ide content.jsp und darf somit auch zum Server posten.
    //
    if(fireEventEmitter != undefined &&  fireEventEmitter==true)
    {
        if(fireEventIsInProcess==true)
            return;
        getObj("eventValue").value = value;
        FireEvent(control,eventId);
    }
    // vieleicht bin ich ein popup welches vom contentFrame ge?ffnet worden ist.
    //
    else if(opener!=null)
    {
        var emitter = opener.parent.frames['jacob_content1'].isVisible==true?opener.parent.frames['jacob_content1']:opener.parent.frames['jacob_content2'];
        emitter.FireEventData(control, eventId,value);
    }
    else
        alert('error2');

    // Falls das Fenster eine Variable autoclose=true hat, wird es nach dem FireEventData sofort geschlossen.
    //
    if(window['autoCloseWindow'] && autoCloseWindow==true)
        window.close();
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** Limit the hands over element with the attribute length                              **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function checkMaxLength(element)
{
    var maxCharSize=element.getAttribute('maxCharSize');

    if(isNaN(parseInt(maxCharSize)))
       return;

    if(element.value.length>maxCharSize)
    {
        window.event.cancelBubble = true;
        window.event.returnValue = false;
        element.value=element.value.substring(0,Math.min(maxCharSize,element.value.length-1));
        alert(unescape(MSG_COMMON_MAX_CHARACTER_LIMIT));
    }
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** DATE / TIME Popups                                                                  **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function showDateTimeSelector( elementId, myIfFormat, myTimeFormat)
{
    if(fireEventIsInProcess==true)
        return;
    cal= Calendar.setup({
            inputField  : elementId,
            ifFormat    : myIfFormat,
            timeFormat  : myTimeFormat,
            showsTime   : true,
            singleClick : true,
            eventName   : 'Click'
            });
    cal['onClick']();
}

function showDateSelector( elementId, myIfFormat)
{
    if(fireEventIsInProcess==true)
        return;

    cal= Calendar.setup({
            inputField  : elementId,
            ifFormat    : myIfFormat,
            singleClick : true,
            eventName : 'Click'
            });
    cal['onClick']();
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** Allgemeine Funktionen f?r das Handling mit Cut&Paste in das ClipBoard.              **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function getTextSelection()
{
   if (window.getSelection) 
    return window.getSelection();
   else if (document.getSelection) 
    return document.getSelection();
   else if (document.selection) 
    return document.selection.createRange().text;
    
  return "";
}

function copyToClipboard()
{
    lastSelectedText=getTextSelection();
    if(lastSelectedText=="" && lastFocusElement!=null)
        lastSelectedText=lastFocusElement.value;
    if(lastSelectedText==null)
        lastSelectedText="";
    window.clipboardData.setData("Text",lastSelectedText);
}

function pasteFromClipboard()
{
    var value = window.clipboardData.getData("Text");
    value = (value==null)?"":value;
    if(lastFocusElement!=null)
    {
        setCaretToPos(lastFocusElement,getCaretPosition(lastFocusElement));
        if(lastFocusElement.getAttribute('readonly')!=true)
            replaceSelection(lastFocusElement,value);
    }
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** Verwalten und Hinzuf?gen von Tastaturk?rzel and HTML Elemente                       **/
/**                                                                                     **/
/*****************************************************************************************/
function formKeyPress(/*:Event*/ event)
{
   // Der Benutzer hat in einem Eingabe feld ENTER gedr?ckt.
   // 1. Hochlaufen bis man die "Group" gefunden hat
   if(event.keyCode==13)
   {
      var emitter = document.activeElement;
      // Falls das Element ein TEXTARE ist wird NICHT der Defaultbutton ausgelï¿½st
      // Man hätte sonst nicht die Möglichkeit mit ENTER einen Zeilenumbruch in dem
      // Feld einzufügen
      if(emitter.tagName.toUpperCase()==="TEXTAREA" || emitter.getAttribute("contentEditable")=="true")
         return;
         
      while(emitter!=null)
      {
         var value = emitter.getAttribute('isGroup');
         if(value!=null && value=="true")
         {
           // Gruppe gefunden. Jetzt in der Gruppe den default Button finden
           //
           var buttons = emitter.getElementsByTagName("button");
           for(var i=0;i<buttons.length;i++)
           {
               if(buttons[i].getAttribute("isDefault")!=null)
               {
                  buttons[i].click();
                  return false;
               }
           }
         }
         // Am document.body angelangt. NICHT true zurï¿½ckliefern, da sonst sich die Form den ersten
         // Button nimmt den diese findet. Dies ist dann der erste Button in der Toolbar. :-(
         // Dies kann dann z.b. der "Logout" button sein.
         //
         if(emitter.tagName.toUpperCase()=="BODY")
           return false;

         emitter=emitter.parentNode;
      }
      return false;
   }
   return true;
}

/*****************************************************************************************/
// man kann den Event-Handler nicht direkt an das Element selbst h?ngen.
// Bei dem modifizieren des DOM-Baums durch die ComboBox werden diese 'pl?tzlich' abgeh?ngt.(?!)
// => assoziatives Array mit den gemappten Funktionen erstellen und die Handle Funktion aus diesem holen.
//
var keyHandlerObjects        = new Array();
var stoppedKeyHandlerObjects = new Array();

function stopKeyEventing(elementId,flag)
{
    stoppedKeyHandlerObjects[elementId]=flag;
}

function addKeyHandler(element) 
{
    // doppel regestrierung ist nicht notwendig (sogar schaedlich)
    if(keyHandlerObjects[element.id])
       return;

    stoppedKeyHandlerObjects[element.id]=false;
    var _keyObject = new Array();
    _keyObject["keydown"]  = new Array();
    _keyObject["keyup"]    = new Array();
    _keyObject["keypress"] = new Array();
    keyHandlerObjects[element.id]=_keyObject;

    element.addKeyDown = function (keyCode, action)
    {
        keyHandlerObjects[element.id]["keydown"][keyCode] = action;
    }

    element.removeKeyDown = function (keyCode)
    {
        keyHandlerObjects[element.id]["keydown"][keyCode] = null;
    }

    element.addKeyUp = function (keyCode, action)
    {
        keyHandlerObjects[element.id]["keyup"][keyCode] = action;
    }

    element.removeKeyUp = function (keyCode)
    {
        keyHandlerObjects[element.id]["keyup"][keyCode] = null;
    }

    element.addKeyPress = function (keyCode, action)
    {
        keyHandlerObjects[element.id]["keypress"][keyCode] = action;
    }

    element.removeKeyPress = function (keyCode)
    {
        keyHandlerObjects[element.id]["keypress"][keyCode] = null;
    }

    function handleEvent()
    {
        if(stoppedKeyHandlerObjects[element.id] == true)
        {
            window.event.cancelBubble = true;
            window.event.returnValue = true;
            return;
        }
        var emitter = document.activeElement;
        while(emitter!=null)
        {
            var id = emitter.getAttribute('id');
            if(id!=null && id!="" && keyHandlerObjects[id]!=null)
            {
                break;
            }
            // unable to determine a handler object in the DOM tree
            if(emitter.tagName=="BODY")
            {
                break;
            }

            emitter=emitter.parentNode;
        }
        var type = window.event.type;
        var code = window.event.keyCode;

        if (keyHandlerObjects[emitter.id][type][code] != null)
        {
            keyHandlerObjects[emitter.id][type][code]();
            window.event.cancelBubble = true;
            window.event.returnValue = false;
        }
    }

    element.attachEvent('onkeypress', handleEvent);
    element.attachEvent('onkeydown' , handleEvent);
    element.attachEvent('onkeyup'   , handleEvent);
}


/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** PATH THE CTRL+N of the IE.                                                          **/
/** This is only  hack and works only in some IE versions!!!                            **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
document.onkeydown = function()
{
    if ((event.keyCode == 78) && (event.ctrlKey))
    {
        openNewApplicationWindow();
        event.cancelBubble = true;
        event.returnValue = false;
        event.keyCode = false; 
        return false;
    }
}

function openNewApplicationWindow(/*:String*/ browserId) /*public function*/
{
    var newUrl="";
    if(document.location.href.split('/ie_content')[0])
        newUrl=document.location.href.split('/ie_content')[0]+"/ie_index.jsp?browser="+browserId;
    window.open(newUrl, "_blank");
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** The handle functions for the RadioButonGroup                                       **/
/**                                                                                     **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function updateRadioButtonGroup(/*:String*/ groupId)
{
    // get all values from this radiobutton group and transfer them in the hidden field
    //
    var value="";
    for(var i=0;;i++)
    {
        var obj= getObj("radio_"+i+"_"+groupId);

        if(obj==null)
          break;
        if(obj.checked==true)
        {
            if(value!="")
                value   = value+"|"+obj.getAttribute("value");
            else
                value = obj.getAttribute("value");
        }
    }
    getObj(groupId).value=value;
}



/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** The handle functions for Multiselction combobox                                     **/
/**                                                                                     **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
var Multiselection_elActiveMenu = null;
var Multiselection_tmrMenu      = null;

/*  Display the list items of the combo box. The corresponding DIV is be named in the attribute 'submenu'
 *
 */
function Multiselection_doMenuClick(element )
{
    if(fireEventIsInProcess==true)
        return;
    element = $(element);
    element.focus();

    // altes contextmenu verbergen
    //
    hideContextMenu();

    element.addClassName("Multiselection_TitleOver");
    var vsSubmenu       = element.getAttribute( "submenu" );

    Multiselection_collapseMenu();
    if (vsSubmenu!=null && vsSubmenu != "" )
    {
        var containerLayer = new LayerObject(element.getAttribute( "container" ));
        var formLayer      = new LayerObject("formDiv");
        var pos2 = containerLayer.realPosition();
        var pos3 = formLayer.realPosition();

        // wenn das Element ein Kind des Form DIV ist, dann muss
        // die Position korrigiert werden. Die ComboBox des SearchBrowsers ist z.B.
        // kein Kind des Form DIV.
        //
        if(isChild(containerLayer.obj, formLayer.obj))
        {
           pos2.x = pos2.x -pos3.x;
           pos2.y = pos2.y -pos3.y;
        }

        var mnuTarget= new LayerObject( vsSubmenu );
        mnuTarget.moveTo(pos2.x,pos2.y+pos2.height+new LayerObject("formDiv").obj.scrollTop);
        mnuTarget.show();
        if(mnuTarget.getHeight()>200)
        {
           mnuTarget.setHeight(200);
           mnuTarget.css.overflowY="auto";
           mnuTarget.css.overflowX="hidden";
        }

        // Falls das Div nicht vollständig sichtbar ist, wird dies dann nach oben aufgeklappt.
        //
        if(!mnuTarget.isInVisibleArea())
        {
           var st = formLayer.obj.scrollTop;
           mnuTarget.moveTo(pos2.x,pos2.y+pos2.height-element.getHeight()-mnuTarget.getHeight()-3+st);
        }

        Multiselection_elActiveMenu = $(vsSubmenu);
    }
    onFocusElement($(element.getAttribute( "container" )));
}

function Multiselection_doMenuMouseOut(element )
{
    element = $(element);
    element.removeClassName("Multiselection_TitleOver");
}

function Multiselection_doClearMenuTimer(element )
{
    if ( Multiselection_tmrMenu )
    {
        clearTimeout( Multiselection_tmrMenu );
        Multiselection_tmrMenu = null;
    }
}

function Multiselection_doStartMenuTimer(element )
{
    Multiselection_tmrMenu = setTimeout( "Multiselection_collapseMenu()", 50 );
}

function Multiselection_collapseMenu( element )
{
   if(Multiselection_elActiveMenu!=null)
   {
     Multiselection_elActiveMenu.hide();
     var displayItem = Multiselection_elActiveMenu.getAttribute("parent");
     $(displayItem).focus();
     Multiselection_elActiveMenu=null;
   }
}


function Multiselection_doItemCheck(element )
{
    element = $(element);
    var displayItem = element.getAttribute("displayItem");
    var displayObj=$(displayItem);
    displayObj.removeClassName("diverse");

    if(displayObj.getAttribute('multiselect')=='false')
        Multiselection_doDeselectAll(displayObj);

    if( element.getAttribute('checked')=="true" )
    {
       element.setAttribute("checked", "false");
       element.removeClassName("checked");
       if(displayObj.getAttribute('multiselect')!='false')
         element.addClassName("unchecked");
    }
    else
    {
       element.setAttribute("checked", "true");
       element.removeClassName("unchecked");
       if(displayObj.getAttribute('multiselect')!='false')
          element.addClassName("checked");
    }

    // get all values from this combobox and display them in the input field
    //
    var value="";
    for(var i=0;;i++)
    {
        var obj= $("item_"+i+"_"+displayItem);

        if(obj==null)
          break;
        if(obj.getAttribute('checked')=="true")
        {
            if(value!="")
                value = value+"|"+obj.getAttribute("value");
            else
                value = obj.getAttribute("value");
        }
    }
    displayObj.value=value;
    displayObj.focus();

    if(displayObj.getAttribute('multiselect')=='false')
        Multiselection_collapseMenu();
}

function Multiselection_doDeselectAll(element)
{
    var multiselect = (element.getAttribute('multiselect')!='false');
    for(var i=0;;i++)
    {
        var obj= $("item_"+i+"_"+element.id);
        if(obj==null)
            break;
        obj.setAttribute("checked", "false");
        obj.removeClassName("checked");
        if(multiselect)
           obj.addClassName("unchecked");
    }
    element.value='';
}


function Multiselection_doUserDefinedValue(element)
{
   Multiselection_collapseMenu();
    element = $(element);
    var displayItem = element.getAttribute("displayItem");
    var displayObj=$(displayItem);
    displayObj.removeClassName("diverse");
    Multiselection_doDeselectAll(displayObj);
   
    var dialog = new jACOBPrompt("","","<unused>",false);
    dialog.onOk = function()
    {
      var content = $("body");

      content.removeChild(this.antiClick);
      content.removeChild(this.windowDiv);
      fireEventIsInProcess=false;
      displayObj.value = this.input.value;     
    }
    dialog.show();
    windowsToShow = new Array();
}



/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** TO HANDLE THE LISTBOX ELEMENT                                                       **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function Listbox_doItemCheck(valueItem /*DOMElement*/, container /*DOMObject*/, element /*DOMObject*/  )
{
    if(container.getAttribute('multiselect')=='false')
        Listbox_doDeselectAll(valueItem, container);

    if ( element.getAttribute("checked")=="true" )
    {
        element.className="listboxitem";
        element.setAttribute("checked", "false");
    }
    else
    {
        element.className="listboxitem_selected";
        element.setAttribute("checked", "true");
    }

    // get all values from this combobox and display them in the input field
    //
    var value = "";
    for(var i=0;;i++)
    {
        var obj= getObj(container.id+"_"+i);
        if(obj==null)
            break;
        if(obj.getAttribute('checked')=="true")
        {
            if(value!="")
                value = value+"|"+obj.getAttribute("value");
            else
                value = obj.getAttribute("value");
        }
    }

    valueItem.setAttribute('value',value);
}


function Listbox_doDeselectAll(valueItem , container)
{
    var id = container.id+"_";
    for(var i=0;;i++)
    {
        var obj= getObj(id+i);
        if(obj==null)
            break;
        if ( obj.getAttribute("checked")=="true" )
        {
           obj.setAttribute("checked", "false");
           obj.className="listboxitem";
        }
    }
    valueItem.setAttribute('value','');
}

function Listbox_doSelectAll(valueItem , container)
{
    var value ="";
    var id = container.id+"_";
    for(var i=0;;i++)
    {
        var obj= getObj(id+i);
        if(obj==null)
            break;
        // Es gibt auch elemente welche nicht selektierbar sind. Diese müssen hier ausgenommen
        // werden
        if( obj.getAttribute("checked")=="true" || obj.getAttribute("checked")=="false")
        {
           obj.className="listboxitem_selected";
           obj.setAttribute("checked", "true");
           if(value!="")
             value = value+"|"+obj.getAttribute("value");
           else
             value = obj.getAttribute("value");
        }
    }
    valueItem.setAttribute('value',value);
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** TO HANDLE THE INPUT FOCUS                                                           **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
var lockSetFocus=false;
function setFocus(controlId, caretPosition)
{
    if(lockSetFocus==true)
        return;
    var obj=getObj(controlId);
    if(obj!=null && obj['focus'])
    {
      try
      {
        var page=visiblePage();
        if(page!=null)
        {
          // Falls es sich um ein ForeignField handelt welches von 'backfilled'->empty wechselt.
          // z.B. wenn der Anwender den Text in einem gebackfilled ForeingField mit der Maus selektiert und l?scht.
          // (FireEvent wird gefeuert und es geht zum Server um das ForeingField/TableAlias zu deselektieren)
          // Der Anwender tippt aber weiter. Die eingetippten Werte werden dann in das neue Feld ?bernommen.
          //
          var pageObj= page.getObj(controlId);
          if(pageObj!=null && pageObj.getAttribute('state') && pageObj.getAttribute('state')=='backfilled')
          {
             obj.value=pageObj.value;
             caretPosition = page.getCaretPosition(pageObj);
          }
        }
        
	    obj.focus();
        setSelectionRange(obj,caretPosition,caretPosition);
	  }
	  catch(exc)
	  {
	     // ignore. Kann passieren wenn das Feld disabled oder auf unsichtbar geschaltet worden ist.
	  }
    }
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** Cursor (caret) movement and text replacements util functions                        **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
/* Select text in the handsover element with the given from-to index 
 */
function setSelectionRange(input, selectionStart, selectionEnd) 
{
  if (input.setSelectionRange) 
  {
    input.focus();
    input.setSelectionRange(selectionStart, selectionEnd);
  }
  else if (input.createTextRange) 
  {
    var range = input.createTextRange();
    range.collapse(true);
    range.moveEnd('character', selectionEnd);
    range.moveStart('character', selectionStart);
    range.select();
  }
}

/* move the cursor to the end of the text element
 */
function setCaretToEnd (input) 
{
  setSelectionRange(input, input.value.length, input.value.length);
}

/* move the cursor to the start of the text element
 */
function setCaretToBegin (input) 
{
  setSelectionRange(input, 0, 0);
}

/* move the cursor to the end of the text element
 */
function setCaretToPos (input, pos) 
{
  setSelectionRange(input, pos, pos);
}

/* 
 * PUBLIC FUNCTION
 *
 * Liefert die Cursor position des ?bergebenen elementes
 */
function getCaretPosition(obj)
{
   var caretPos = window.document.selection.createRange().duplicate();
   caretPos.collapse(true);
   caretPos.moveStart("textedit",-1);
   return caretPos.text.length;
}

/* select the hands over text in the element
 */
function selectString (input, string) 
{
  var match = new RegExp(string, "i").exec(input.value);
  if (match)
  {
    setSelectionRange (input, match.index, match.index + match[0].length);
  }
}

/* move the cursor to the end of the text element
 */
function replaceSelection (input, replaceString) 
{
  if (input.setSelectionRange) 
  {
    var selectionStart = input.selectionStart;
    var selectionEnd = input.selectionEnd;
    input.value = input.value.substring(0, selectionStart)
                  + replaceString
                  + input.value.substring(selectionEnd);
    if (selectionStart != selectionEnd) // has there been a selection
      setSelectionRange(input, selectionStart, selectionStart + replaceString.length);
    else 
      setCaretToPos(input, selectionStart + replaceString.length);
  }
  else if (document.selection) 
  {
    var range = document.selection.createRange();
    if (range.parentElement() == input) 
    {
      var isCollapsed = range.text == '';
      range.text = replaceString;
      if (!isCollapsed)  
      { // there has been a selection
        //it appears range.select() should select the newly 
        //inserted text but that fails with IE
        range.moveStart('character', -replaceString.length);
        range.select();
      }
    }
  }
}


/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD
 *
 * Return true if the CTRL-Key is pressed during the event has ben fired (like MousePress...)
 *
 ******************************************************************************************************* 
 *******************************************************************************************************/
function isCtrlKeyPressed(event)
{
  Event.stop(window.event);
  return window.event.ctrlKey;
}

/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD
 *
 * Register the "Divider" eventhandler if the mouse moves over the div.
 *
 ******************************************************************************************************* 
 *******************************************************************************************************/
var Divider_dragObject = null;
function Divider_register()
{
   document.onmousedown = Divider_onMouseDown;
   document.onmouseup   = Divider_onMouseUp;
   document.onmousemove = Divider_onMouseMove;
}

/**
  * PRIVATE METHOD.
  * 
 **/
function Divider_onMouseDown() 
{
    if(fireEventIsInProcess==true)
        return;

    el = Divider_getReal(window.event.srcElement);
    if (el!=null && el.id == "divider")
    {
        Divider_dragObject = el;
        hideContextMenu();
//        hideForeignField();
        Multiselection_collapseMenu();
        return false;
    }
    else
    {
        Divider_dragObject = null;
    }
}

/**
  * PRIVATE METHOD.
  * 
 **/
function Divider_onMouseUp()
{
    Divider_dragObject=null;
}

/**
  * PRIVATE METHOD.
  * 
 **/
function Divider_onMouseMove()
{
    if(fireEventIsInProcess==true)
        return;

    if (Divider_dragObject)
    {
        var newHeight = window.event.clientY-10;
        var table=getObj('searchBrowserTable');
        getObj('dividerPos').value=newHeight;
        table.style.height=newHeight;
        resizeSearchBrowserCallback();
        return false;
    }
}


/**
  * PRIVATE METHOD.
  * 
 **/
function Divider_getReal(el)
{
    temp = el;
    while ((temp != null) && (temp.tagName != "BODY"))
    {
        if ((temp.id == "divider"))
        {
            el = temp;
            return el;
        }
        // verhindern, dass sich der IE tot laufen kann
        // Ist eigentlich nicht notwendig, aber bei DaimlerChrysler kommt manchmal der Dr.Watson
        // wenn dies nicht gepr?ft wird.
        //
        if(temp == temp.parentElement)
            break;
        temp = temp.parentElement;
    }
    return el;
}


function isChild(possibleChild, node)
{
   while(possibleChild)
   {
      if (possibleChild==node)
        return true;
      possibleChild=possibleChild.parentNode;
   }
   return false;
}

/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD
 *
 * Crossbrowser replacement for getElementById.
 *
 ******************************************************************************************************* 
 *******************************************************************************************************/
function getObj(objId)
{
  return document.getElementById(objId);
}


/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD
 *
 * Create a object with the current client browser page width/height.
 *
 * usage: var size = new PageSize();
 *        alert("Current page size:["+size.width+","+size.height+"]");
 *
 ******************************************************************************************************* 
 *******************************************************************************************************/
function PageSize()
{
  this.width  = document.body.offsetWidth;
  this.height = document.body.offsetHeight;
  return this;
}

/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD
 *
 * Create a crossbrowser layer object with some handle functions.
 *
 * Usage:
 *  var obj = new LayerObject('myObjectId');
 *  obj.show();
 *  obj.hide();
 *  obj.moveTo(x,y);
 *  obj.moveBy(xoffset,yoffset);
 *  obj.writeText("This is the new inner HTML");
 *  obj.appendText("This will append to the current inner HTML");
 *
 ******************************************************************************************************* 
 *******************************************************************************************************/
function LayerObject(objId)
{
  if(typeof objId == "string")
    this.obj=getObj(objId);
  else
    this.obj=objId;

  if(this.obj==null)
    return;

  // assigne member variables
  //
  this.name=objId;
  this.css=this.obj.style;
  this.x=parseInt(this.css.left);
  this.y=parseInt(this.css.top);
  this.width=parseInt(this.obj.offsetWidth);
  this.height=parseInt(this.obj.offsetHeight);
}

/**
 * funktioniert nur wenn die elemente inner des formdiv sind.
 **/
LayerObject.prototype.isInVisibleArea=function()
{
    // Get the top and bottom position of the *visible* part of the formDiv.
    var parent = $("formDiv");
    var child  = this.obj;
    while (child != null) 
    {
       if (child == parent)
       {
        return   (parent.scrollTop<this.y && (parent.scrollTop+parent.offsetHeight)>this.y)
              &&(parent.scrollTop+parent.offsetHeight)>this.y+this.getHeight();
       }
       child = child.parentNode;
    }
    return true;
}

LayerObject.prototype.realPosition=function()
{
 var d = new Object();
 if(this.obj.getBoundingClientRect)
 {
    d.x = this.obj.getBoundingClientRect().left;
    d.y = this.obj.getBoundingClientRect().top;
    d.width = this.obj.getBoundingClientRect().right - this.obj.getBoundingClientRect().left;
    d.height = this.obj.getBoundingClientRect().bottom - this.obj.getBoundingClientRect().top;
 }
 return d;
}

LayerObject.prototype.setWidth= function(/*:int*/ value)
{
  this.css.width=value;
}

LayerObject.prototype.setHeight= function(/*:int*/ value)
{
  this.css.height=value;
}

LayerObject.prototype.setX= function(/*:int*/ value)
{
  this.css.left=value;
}

LayerObject.prototype.setY= function(/*:int*/ value)
{
  this.css.top= value;
}

LayerObject.prototype.getWidth= function()
{
  return parseInt(this.obj.offsetWidth);
}

LayerObject.prototype.getHeight= function()
{
  return parseInt(this.obj.offsetHeight);
}

LayerObject.prototype.getX= function()
{
  return parseInt(this.css.left);
}

LayerObject.prototype.getY= function()
{
  return parseInt(this.css.top);
}

// binding for LayerObject function: show
LayerObject.prototype.show= function()
{
  this.css.display='block';
}

// binding for LayerObject function: hide
LayerObject.prototype.hide= function()
{
  this.css.display='none';
}

// binding for LayerObject function:  move absolute
LayerObject.prototype.moveTo= function(/*:int*/x, /*:int*/y)
{
  this.x = x;
  this.y = y;
  this.css.left=x;
  this.css.top=y;
}

// binding for LayerObject function:  move relative
LayerObject.prototype.moveBy= function(/*:int*/x,/*:int*/y)
{
  this.moveTo(this.x+x, this.y+y);
}

// binding for LayerObject function: writeText
LayerObject.prototype.writeText= function(/*:String*/ text)
{
    this.obj.innerHTML=text;
}

// binding for LayerObject function: appendText
LayerObject.prototype.appendText= function(/*:String*/ text)
{
    this.obj.innerHTML=this.obj.innerHTML+text;
}

// binding for LayerObject function: getRuntimeStyle
LayerObject.prototype.getRuntimeStyle= function(/*:String*/ styleName)
{
    return this.obj.currentStyle[styleName];
}

/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** CONTEXTMENU RELATED STUFF                                                           **/
/**                                                                                     **/
/** This section handles the usage of context menues.                                   **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function showContextMenu(contextMenuId)
{
    if(fireEventIsInProcess==true)
        return;

    // altes contextmenu verbergen
    //
    hideContextMenu();

    // Combobox einklappen
    //
    Multiselection_collapseMenu();

    currentMenu = new LayerObject(contextMenuId);
    currentMenu.moveTo(globalMouseX,globalMouseY);
    currentMenu.show();
}

function hideContextMenu()
{
    if(currentMenu!=null)
        currentMenu.hide();
    currentMenu=null;

    // das Contextmenu kann auch ein ColumnPicker sein
    // dies wird dann komplett aus dem DOM Baum ausgehaengt
    //
    var columnPickerMenu = getObj("columnPicker");
    if(columnPickerMenu!=null)
    {
       columnPickerMenu.parentNode.removeChild(columnPickerMenu);
    }

    // hide the foreingfield if visible
    hideForeignField();
}


/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** FOREIGN FIELD FUNMCTIONS                                                            **/
/**                                                                                     **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function showForeignFieldDialog(anchorId, clientBrowserId, foreignBrowserId)
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefï¿½hrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund drï¿½ngt.
    lockSetFocus=true;

    var pos =getAnchorWindowPosition(anchorId);
    foreignUrl="ie_foreignField.jsp?browser="+clientBrowserId+"&foreignBrowser="+foreignBrowserId;
    var testpopup1 = new PopupWindow();
    var dividerFiller= (browserVisible==true)?getObj('dividerFiller').height:0;
    testpopup1.offsetY = (-getObj('formDiv').scrollTop)+30-dividerFiller;
    // temp change because of Daimler request
    testpopup1.setSize(450,180);
    testpopup1.autoHide();
    testpopup1.setUrl(foreignUrl);
    testpopup1.showPopup(anchorId);
    testpopup1.popupWindow.moveTo(pos.x,pos.y+testpopup1.offsetY);
}

function showForeignFieldDialogInline(anchorId, browserContentDivId)
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefï¿½hrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund drï¿½ngt.
    lockSetFocus=true;

    var pos =getAnchorPosition(anchorId);
    var dividerFiller= getObj('dividerFiller').height;
    var offsetY=pos.y-parseInt(getObj('dividerPos').value)+ 25-dividerFiller;
    new LayerObject(browserContentDivId).moveTo(pos.x-parseInt(new LayerObject('eventForm').css.marginLeft),offsetY);
    inlineForeignFieldIsVisible=true;
}


/*******************************************************************************************************
 * PRIVATE METHOD!
 *
 * Hide the inline ForeingField DIV
 *
 *******************************************************************************************************/
function hideForeignField()
{
    if(inlineForeignFieldIsVisible==true)
    {
      var field = new LayerObject('foreign_field_inline');
      if(field.obj!=null)
         field.hide();
    }
}


/*******************************************************************************************************
 * PUBLIC METHOD!
 *
 * Open e new browser window with toolbar navigation. 
 *
 * NOTE:
 * The window will be open asynchron after the "pageFlip". This is importand for the IE on WindowsXP.
 * In some conditions you have a dead look if you call them before the pageFlip. DON'T KNOW WHY!!!
 *
 * URL     the URL which should be displa in the new window
 * width   The width of the window
 * height  The heigth of the window
 *
 *******************************************************************************************************/
function popupModal(url, width, height)
{
    windowsToShow.push(new jWindow(url,width,height,true));
}


/*******************************************************************************************************
 * PUBLIC METHOD!
 *
 * Open e new popup window. This is usefull for dialogs, informations panels,....
 *
 * NOTE:
 * The window will be open asynchron after the "pageFlip". This is importand for the IE on WindowsXP.
 * In some conditions you have a dead look if you call them before the pageFlip. DON'T KNOW WHY!!!
 *
 * URL     the URL which should be displa in the new window
 * width   The width of the window
 * height  The heigth of the window
 *
 *******************************************************************************************************/
function jACOBPopup(url, width, height, withScrollbars)
{
    windowsToShow.push(this);

    // assign the member variables
    //
    this.url      = url;
    this.width    = width;
    this.height   = height;
    this.left     = (screen.width  - this.width)  / 2;
    this.top      = (screen.height - this.height) / 2;
    this.scroll   = withScrollbars?"1":"0";

    this.property = 'left='+this.left+', top='+this.top+', toolbar=0,scrollbars='+this.scroll+',location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width='+this.width+',height='+this.height;
}

/**
 * 
 **/
jACOBPopup.prototype.show=function()
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefï¿½hrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund drï¿½ngt.
    lockSetFocus=true;

    window.open(this.url, "_blank", this.property);
}


/**
 * 
 *
 * Create e new browser window object.
 *
 * URL     the URL which should be displa in the new window
 * width   The width of the window
 * height  The heigth of the window
 *
 * Note:
 * On WinXP you can have dead look in some 'cases'. Don't know why! Use openPopup or openWindow
 * to open a window asynchron
 **/
function jACOBWindow(url, width, height, withScrollbars)
{
    windowsToShow.push(this);

    // assign the member variables
    //
    this.url      = url;
    this.width    = width;
    this.height   = height;
    this.left     = (screen.width  - this.width)  / 2;
    this.top      = (screen.height - this.height) / 2;
    this.scroll   = withScrollbars?"1":"0";

    this.property = 'left='+this.left+', top='+this.top+', toolbar=1,scrollbars='+this.scroll+',location=1,statusbar=1,menubar=1,resizable=1,alwaysRaised,width='+this.width+',height='+this.height;
}

/**
 *
 **/
jACOBWindow.prototype.show=function()
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefï¿½hrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund drï¿½ngt.
    lockSetFocus=true;

    window.open(this.url, "_blank", this.property);
}


/**
 *
 * Create e new browser window which close itself if the lost the focus.
 *
 * NOTE:
 * The window will be open asynchron after the "pageFlip". This is importand for the IE on WindowsXP.
 * In some conditions you have a dead look if you call them before the pageFlip. DON'T KNOW WHY!!!
 *
 * anchorId the anchor for the popup.
 * url      the URL which should be displa in the new window
 * width    The width of the window
 * height   The heigth of the window
 *
 * 
 **/
function jACOBAutocloseWindow(anchorId, url, width, height)
{
    windowsToShow.push(this);

    // assign the member variables
    //
    this.url      = url;
    this.width    = width;
    this.height   = height;
    this.anchorId = anchorId;
}

/**
 *
 **/
jACOBAutocloseWindow.prototype.show=function()
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefï¿½hrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund drï¿½ngt.
    lockSetFocus=true;

    var testpopup1 = new PopupWindow();
    testpopup1.offsetY = 25;
    testpopup1.setSize(this.width,this.height);
    testpopup1.autoHide();
    testpopup1.setUrl(this.url);
    if(document.all[this.anchorId])
    {
       testpopup1.showPopup(this.anchorId);
    }
    else
    {
      // Falls das Fenster von einem ContextMenu geï¿½ffnet wird, dann wird das
      // Fenster zentriert
      //
      var left = (screen.width  - this.width)  / 2 - window.screenLeft;
      var top  = (screen.height - this.height) / 2 - window.screenTop;
      var obj  =new LayerObject( "dialogAnchor" );
      obj.show();
      obj.moveTo(left,top);
      testpopup1.showPopup("dialogAnchor");
      obj.hide();
    }
}

/*******************************************************************************************************
 *******************************************************************************************************
 * PUBLIC METHOD!
 *
 * Reload javascript into the current page without any page request cycle.   
 *                                                                           
 *******************************************************************************************************
 ******************************************************************************************************/
function loadRemotePage(page)
{
    /*
       Dr.Watson im IE falls man den DOM-Baum modifieziert!!!!!
       Hat bei DaimlerChrysler den ServiceDesk damals lahm gelegt.
       
    var head = document.getElementsByTagName('head').item(0);
    var scriptTag = getObj('serverPush');
    if(scriptTag!=null)
        head.removeChild(scriptTag);
    script = document.createElement('script');
    script.src = page;
    script.type = 'text/javascript';
    script.id = 'serverPush';
    head.appendChild(script);

    ......so geht es auch    */
    try{
	    document.all.serverPush.src= page;
    }catch(e)
    {
     // Falls gerade der "SaveAs" Dialog sichtbar ist - z.B. wenn man
     // an einem Dokument Feld auf "Anzeigen" klickt, dann tritt hier ein Fehler auf (z.b beim keepAlive)
     // Anscheinen kann bei dem anzeigen des Dialoges das Hauptfenster sich nicht ver?ndern.
     // => ignore
    }
}



/**
 *
 *
**/
function adjustOutlookbar()
{
   var layer =  new LayerObject('outlookbarButtonArea');
   if(layer.obj!=null)
       layer.css.height="100%";
}


/*******************************************************************************************************
 *******************************************************************************************************
 * PUBLIC METHOD!
 *
 * Will be called if the search browser has to be fit to the new screen size. 
 *                                                                           
 *******************************************************************************************************
 ******************************************************************************************************/
var headerHeight=-1;           // private
var tabfooterHeight=-1         // private
var actionfooterHeight=-1      // private
var toolbarHeight=-1;          // private
var toolbarLeftWidth=-1;       // private
var halfDividerHeight=-1;      // private
var toReduceContentHeight=-1   // private
function resizeSearchBrowserCallback()
{
    if(fireEventIsInProcess===true)
      return;
   
    if(toolbarHeight==-1)
      toolbarHeight = $("toolbar").getHeight();

    if(tabfooterHeight==-1)
      tabfooterHeight =$("searchTabFooter").getHeight();
 
    if(actionfooterHeight==-1)
      actionfooterHeight = $("searchActionFooter").getHeight();

    if(halfDividerHeight==-1)
      halfDividerHeight=$("dividerFiller").getHeight();

    if(toReduceContentHeight==-1)
      toReduceContentHeight = actionfooterHeight+tabfooterHeight+toolbarHeight+halfDividerHeight;

    if(toolbarLeftWidth==-1)
    {
      var element = new LayerObject("toolbar_left");
      toolbarLeftWidth= element.obj?parseInt(element.getRuntimeStyle("width")):0;
    }

    var element = new LayerObject("toolbar_left");
    var toolBarLeft= element.obj?parseInt(element.getRuntimeStyle("width")):0;
    var newWidth=new PageSize().width- parseInt(new LayerObject("eventForm").css.marginLeft)-toolBarLeft;
    if(newWidth<=0)
     newWidth=10;

   if(searchBrowser!=null)
   {
    searchBrowser.root.style.width = newWidth+"px";
    searchBrowser.root.style.height= Math.max(80,parseInt(getObj("dividerPos").value)+halfDividerHeight-toReduceContentHeight)+"px";
    searchBrowser.resize();
   }

   if(window['onContentAreaResize'])
   {
     onContentAreaResize(newWidth,new LayerObject ("formDiv").getHeight());
   }
}

/*******************************************************************************************************
 *******************************************************************************************************
 * PUBLIC METHOD!
 *
 * Will be called if the user clicks on n input element. The element must be stored. After a page refresh
 * the input focus will be set on the last focus element in the form.
 *                                                                           
 *******************************************************************************************************
 ******************************************************************************************************/
function onFocusElement(/*:HTMLElement*/ obj, /*:String*/objName)
{
   // Falls das neue Eingabeelement "autosuggest" unterstuetzt, wird dies durch
   // ein Gluehbirnchen dargestellt.
   //
   var bulb = new LayerObject("autosuggestBulb");
   bulb.hide();
   if(obj.getAttribute("autosuggest")=="true")
   {
      var inputElement = new LayerObject(obj.id);
      bulb.moveTo(inputElement.x-3,inputElement.y+3);
      bulb.show();
   }

   // Highlight the current element which has the focus
   if(lastFocusElement!=null)
     lastFocusElement.removeClassName("focus");
   lastFocusElement =  $(obj);
   if(lastFocusElement.getAttribute("readonly")==false || lastFocusElement.getAttribute("readonly")==null)
   {
     lastFocusElement.addClassName("focus");
     // Fuer den nachsten Serverrequest merken welches Element gerade den Fokus hatte
     getObj('focusElement').value=objName;

     // falls es sich um ein zurueckgefuelltes ForeignField handlet, wird der Inhalt komplett
     // ausgewaehlt
     if(obj.getAttribute("state")=="backfilled")
        obj.setSelectionRange(0,obj.value.length);
   }
}


/*******************************************************************************************************
 *******************************************************************************************************
 * PUBLIC METHOD!
 *
 * Wird f?r die Tastaturnavigation des Searchbrowsers ben?tigt
 *                                                                           
 *******************************************************************************************************
 ******************************************************************************************************/
var currentHighlightedRow=null;
var currentHoverRow=null;
function calculateCurrentHighlightedRow(browserId)
{
  if(currentHighlightedRow!=null)
    return;

  var table = getObj(browserId);
  var tableRows = table.getElementsByTagName("tr");
  // Die Zeile finden welche selected="true" hat.
  // 
  for(var i=0;i< tableRows.length;i++)
  {
    if(tableRows[i].getAttribute("selected"))
    {
      currentHighlightedRow=tableRows[i];
    }
  }
	
  if(currentHighlightedRow==null)
  {
    currentHighlightedRow=tableRows[0];
    if(currentHighlightedRow.onmouseover)
      currentHighlightedRow.onmouseover();
    currentHoverRow = currentHighlightedRow;
  }
}

/**
 * Die n?chste Zeile hervorheben  
 **/
function onCursorDownKey(browserId)
{
  calculateCurrentHighlightedRow(browserId);
  var last=currentHoverRow;
  if(currentHoverRow==null)
          currentHoverRow = currentHighlightedRow.nextSibling;
  else
    currentHoverRow = currentHoverRow.nextSibling;

  if(currentHoverRow.getAttribute("filler"))
  {
    currentHoverRow = last;
    return;
  }
  if(last && last.onmouseout)
    last.onmouseout();
  if(currentHoverRow.onmouseover)
    currentHoverRow.onmouseover();

  var table = getObj(browserId);
  table.scrollTop=table.scrollTop+currentHoverRow.offsetHeight;
}

/**
 *  die Vorg?nger Zeile hervorheben
 **/
function onCursorUpKey(browserId)
{
  calculateCurrentHighlightedRow(browserId);
  var last=currentHoverRow;
  if(currentHoverRow==null)
    currentHoverRow = currentHighlightedRow.previousSibling;
  else
    currentHoverRow = currentHoverRow.previousSibling;

  if(!currentHoverRow)
  {
    currentHoverRow = last;
    return;
  }
  if(last && last.onmouseout)
    last.onmouseout();
  if(currentHoverRow.onmouseover)
    currentHoverRow.onmouseover();
  var table = getObj(browserId);
  table.scrollTop=table.scrollTop-currentHoverRow.offsetHeight;
}

function onEnter()
{
  if(currentHoverRow!=null && currentHoverRow.onclick)
    currentHoverRow.onclick();
}

function adjustTipDialog(/*:String*/ tipId, /*:String*/ anchorId)
{
   var anchorLayer =  new LayerObject(anchorId);
   var boundigBox = anchorLayer.realPosition();

   var tip =new LayerObject(tipId);
   tip.moveTo(boundigBox.x+(boundigBox.width/3), boundigBox.y+boundigBox.height);
   tip.obj.onclick= function(){this.style.display="none";};
}


/**
 * The purpose of this function is to attempt to open
 * a popup window to determine if a popup blocker is
 * enabled.
 **/
function hasPopupblocker() 
{
  var objChild;
  var reWork = new RegExp('object','gi');

  try
  {
     objChild = window.open('','child','width=50,height=50,status=no,resizable=yes'); 
     objChild.close();
  }
  catch(e) { }

  if(!reWork.test(String(objChild)))
     return true;
  return false;
}


/**
 * This function update the input-fields (parameter) of the textarea with the
 * current caret and selection index.
 * PUBLIC
 **/
function updateTextareaSelection(textarea, caretId, startId, endId)
{
    if(textarea.getAttribute('readonly')==true)
       return;

    // make sure it's the textarea's selection
    var range = document.selection.createRange();
    // create a selection of the whole textarea
    var range_all = document.body.createTextRange();
    range_all.moveToElementText(textarea);

    // calculate selection start point by moving beginning of range_all to beginning of range
    for (var sel_start = 0; range_all.compareEndPoints('StartToStart', range) < 0; sel_start ++)
      range_all.moveStart('character', 1);

    $(startId).value = sel_start;
    $(caretId).value = sel_start;

    // create a selection of the whole textarea
    range_all = document.body.createTextRange();
    range_all.moveToElementText(textarea);
    // calculate selection end point by moving beginning of range_all to end of range
    for (var sel_end = 0; range_all.compareEndPoints('StartToEnd', range) < 0; sel_end ++)
      range_all.moveStart('character', 1);
    $(endId).value = sel_end;
}
