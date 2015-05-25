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
var RCS_ID_COMMON = "$Id: ns_common.js,v 1.69 2010/10/13 14:22:01 freegroup Exp $";
var fader = new Array();
var currentButton=null;                // PUBLIC
var currentButtonArea=null;            // PUBLIC
var fireEventEmitter=false             // PUBLIC
var isVisible=false;                   // PUBLIC
var windowsToShow = new Array();       // PUBLIC
var reloadPage=true;                   // PUBLIC
var lastFocusElement=null;             // PUBLIC
var isLoggedOutBySystem=false;         // PUBLIC
var currentMenu  = null;               // PUBLIC
var searchBrowser = null;              // PUBLIC
var searchBrowserId=null;
var BROWSER_IE = false;
var BROWSER_NS = true;

var globalMouseX = 300;                // private
var globalMouseY = 300;                // private
var innerMouseX = 300;                 // private
var innerMouseY = 300;                 // private
var helpModeIsOn=false;                // private
var fireEventIsInProcess=true;         // private
var inlineForeignFieldIsVisible=false; // private
var statusMessageUpdater = null;

/**************************************************************************
 * PRIVATE FUNCTION
 *************************************************************************/
window.addEventListener("mousemove",captureMouse,false); // private
function captureMouse(e)
{
     globalMouseX = e.screenX;
     globalMouseY = e.screenY;

     innerMouseX = e.clientX;
     innerMouseY = e.clientY;
}


// PUBLIC
function getMousePos()
{
  var pos = new Object();
  pos.x=innerMouseX;
  pos.y=innerMouseY;
  return pos;
}

function ellipsis(/*:HTMLElement*/ e)
{
	e = $(e);
	var w = e.getWidth()-10;
	var t = e.innerHTML;
	var title = t.replace(/<br>/g, "");
	var hasElipsis = false;
	e.innerHTML = "<span>" + t + "</span>";
	e = e.down();
	while (t.length > 0 && e.getWidth() >= w) 
	{
	  t = t.substr(0, t.length - 1);
	  e.innerHTML = t + "...";
	  hasElipsis=true;
	}
	if(hasElipsis)
	  e.setAttribute("title",title);
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
/** Toolfunktionen welche fr das Flickerfree page reload ben???igt werden.              **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
function flipPage()
{

    var teaserHeight = parseInt(new LayerObject('teaser').getRuntimeStyle('height'));
    if(!teaserHeight)
       teaserHeight="0";

    parent.contentFrame=window;

    if(getObj('outlookbar')!=null)
        getObj('outlookbar').scrollTop=$F('domainScrollPos');

    window.parent.contentFrame=window;

    globalMouseX = parseInt($F('mouseX'));
    globalMouseY = parseInt($F('mouseY'));

    if(window['foreingField'])
      foreingField();


    // Wird benoetigt, da bei dem modifizieren des DOM Baumes die Eventhandler 'irgendwie' und 'ploetzlich' verloren gehen.
    // Als 'Fallback' muss der Body dann von dem aktive ELement rekusive die Parent-Nodes suchen und nachsehen ob ein Eventhandler
    // uebersprungen wurde. Falls dies der Fall ist, wird der Eventhandler dann von Hand aufgerufen.
    addKeyHandler(document.body);
    document.body.addKeyPress(27,hideForeignField);
//    document.body.addEventListener("click",hideForeignField,false);
    document.body.addEventListener("click",hideContextMenu,false);
    // Die FireEvent methode wird jetzt frei geschaltet. So wird verhindert, dass der Butzer schon auf ein
    // Icon/Button klickt bevor die Seite verfï¿½gbar ist. Es *kï¿½nnte* eventuell ein synch. Problem dadurch
    // entstehen. Bentuzer klickt und HTML wird gleichzeitig in dieser Methode modifieziert -> deadlock!?
    // (be save).
    fireEventIsInProcess=false;
    fireEventEmitter=true;
    isVisible=true;

    // Speichern in welchem frame die n?chste Seite angezeigt werden muss
    // (hidden/visible page Verfahren wie bei Spiele)
    //
    getObj('eventForm').target='jacob_content1';

    if(browserVisible==true && searchBrowserId!=null)
    {
       searchBrowser = new jACOBTable(searchBrowserId);
       resizeSearchBrowserCallback();
    }
    var formDiv = new LayerObject("formDiv");
    var fff = new LayerObject("searchBrowserTable");
    formDiv.css.top = fff.height+"px";
    formDiv.obj.scrollTop=$F('formScrollPos');
    document.body.scrollTop=0; // vermeidet aus unerfindlichen Grï¿½nden ein Flackern des Browsers und ist somit SEHR wichtig

    // vermindert das flicker wenn ein Fenster/Popup geoeffnet wird
    //
    window.setTimeout("xyz();",100);

    var twin=  window.parent.document.body;
    if(parent.frames['jacob_content1']==window)
    {
        twin.rows=teaserHeight+",100%,0";
        getObj('eventForm').target='jacob_content2';
        parent.frames['jacob_content2'].location.href="about:blank";
    }
    else
    {
        twin.rows=teaserHeight+",0,100%";
        getObj('eventForm').target='jacob_content1';
        parent.frames['jacob_content1'].location.href="about:blank";
    }
    afterPageLoad();

    // Der Searchbrowser braucht nur angepasst werden, wenn dieser auch sichtbar ist
    //
    if(browserVisible==true)
        window.onresize=function(){resizeSearchBrowserCallback();};
}

function xyz()
{
  // array kopieren damit der serverPush die Fenster nicht nochmal anzeigt.
  // Dies kann bei einem "prompt(...)" passieren. Der IE oder FireFox stoppt bei der verarbeitung
  // eines alert/prompt. Wenn in dieser Zeit ein ServerPush kommt wird auf "windowsToShow" zugegriffen und dies
  // ist dann nicht zurï¿½ck gesetzt => die Fenter werden doppelt angezeigt.
  //
  var tmp = windowsToShow;
  windowsToShow = new Array();
  for(a=0;a<tmp.length; a++)
  {
    tmp[a].show();
  }
}

function glow(/*:HTMLElement*/element)
{
    var elem = new LayerObject(element)

    var x = elem.getRuntimeStyle("background-color");
    while(x=="" || x=="transparent")
    {
      x =  elem.getRuntimeStyle("background-color");
      elem =  new LayerObject(elem.obj.parentNode);
    }
    if(x.startsWith('rgb'))
      x = x.rgbToHex();
    new fx.Color(element,'background-color' ).set("#FFFF00");
    new fx.Color(element,'background-color',{duration:3000} ).custom("#FFFF00", x);
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
    if(window['fireEventEmitter'] &&  fireEventEmitter==true)
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
       // Die Buttons nur disabled falls ein page reload gemacht wird
       // Falls ein Dokument herrunter geladen wird, dann passiert keine page reload
       //
       if(reloadPage==true)
       {
        var toDisableElements=getObj('eventForm').getElementsByTagName('button');
        for (i = 0; i < toDisableElements.length; i++)
        {
            toDisableElements[i].onclick=new function(){};
// don't do that. Creates a fidgety screen
//            toDisableElements[i].className = toDisableElements[i].className.replace("_normal","_disabled");
        }
       }

       getObj("browser").value = browserId;
       getObj("control").value = control;
       getObj("event").value   = eventId;
       getObj('mouseX').value  = globalMouseX;
       getObj('mouseY').value  = globalMouseY;
       markScrollPosForm(getObj('formDiv'));
       if(getObj('outlookbarButtonArea')!==null)
           markScrollPosDomain(getObj('outlookbarButtonArea'));

       // DON't use "form.submit()"; => The event method [onSubmit] will not be fired! Some GUI ELements
       // has been register an event handler on this event. e.g. The HTML WYSIWYG Editor.
       getObj('submitButton').click();
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
}

/* Fire an event to the server with attached data for the event */
function FireEventData(control, eventId, value)
{
    // ich bin das content.jsp...ich bin der einzigste welcher FireEvent direkt ausfuehren darf.
    //
    if(window['fireEventEmitter'] &&  fireEventEmitter==true)
    {
        if(fireEventIsInProcess==true)
            return;
        getObj("eventValue").value = value;
        FireEvent(control,eventId);
    }
    // vieleicht bin ich ein popup welches vom contentFrame ge???fnet worden ist.
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
/** Allgemeine Funktionen fr das Handling mit Cut&Paste in das ClipBoard.              **/
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
    if(lastSelectedText=="")
        lastSelectedText=lastFocusElement.value;
    if(lastSelectedText==null)
        lastSelectedText="";
    window.clipboardData.setData("Text",lastSelectedText);
}

function pasteFromClipboard()
{
    var value = window.clipboardData.getData("Text");
    value = (value==null)?"":value;
    setCaretToPos(lastFocusElement,lastCursorPos);
    if(lastFocusElement.getAttribute('readonly')!=true)
        replaceSelection(lastFocusElement,value);
}

function formKeyPress(/*:Event*/ event)
{
   var charCode = event.charCode || event.keyCode || event.which; // Chrome, Firefox and Safarie handling :-(
   if(charCode == Event.KEY_RETURN)
   {
      var emitter = event.element();
      // Falls das Element ein TEXTARE ist wird NICHT der Defaultbutton ausgeloest
      // Man haette sonst nicht die Moeglichkeit mit ENTER einen Zeilenumbruch in dem
      // Feld einzufuegen
      if(emitter.tagName.toUpperCase()==="TEXTAREA" || emitter.getAttribute("contentEditable")=="true")
         return;

      var group = event.findElement("div[isGroup=true]");         
      if(group!==null)
      {
          // Gruppe gefunden. Jetzt in der Gruppe den default Button finden
          //
          var buttons = group.select("button[isDefault=true]");
          if(buttons.length>0)
          {
             buttons[0].click();
             event.stop();
          }
      }
      event.stop();
   }
}


/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** Verwalten und Hinzufgen von Tastaturkrzel and HTML Elemente                       **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
// man kann den Event-Handler nicht direkt an das Element selbst hï¿½ngen.
// Bei dem modifizieren des DOM-Baums durch die ComboBox werden diese 'plï¿½tzlich' abgehï¿½ngt.(?!)
// => assoziatives Array mit den gemappten Funktionen erstellen und die Handle Funktion aus diesem holen.
//
var keyHandlerObjects = new Array();
var stoppedKeyHandlerObjects = new Array();

function stopKeyEventing(elementId,flag)
{
    stoppedKeyHandlerObjects[elementId]=flag;
}

function addKeyHandler(element) 
{
    // doppel registrierung ist nicht notwendig (ist sogar schädlich)
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

    function handleEvent(e)
    {
        if(stoppedKeyHandlerObjects[element.id] == true)
        {
            return;
        }
        var emitter = this;
        while(emitter!=null)
        {
            var id = emitter.getAttribute('id');
            if(id!=null && id!="" && keyHandlerObjects[id]!=null)
            {
                break;
            }
            // unable to determine a handler object in the DOM tree
            if(emitter.tagName.toUpperCase()=="BODY")
            {
                break;
            }
            emitter=emitter.parentNode;
        }
        var type = e.type;
        var code = e.which;
        var handlerMap = keyHandlerObjects[emitter.getAttribute('id')];
        if(handlerMap != undefined && handlerMap[type][code] != null)
        {
            handlerMap[type][code]();
            e.cancelBubble = true;
            e.returnValue = false;
        }
    }

    element.onkeypress = handleEvent;
    element.onkeydown  = handleEvent;
    element.onkeyup    = handleEvent;
}


/*****************************************************************************************/
/*****************************************************************************************/
/**                                                                                     **/
/** PATH THE CTRL+N of the IE.                                                          **/
/** This is only  hack and works only in some IE versions!!!                            **/
/**                                                                                     **/
/*****************************************************************************************/
/*****************************************************************************************/
document.onkeydown = function(e)
{
    if ((e.keyCode == 78) && (e.ctrlKey))
    {
    	openNewApplicationWindow();
        e.cancelBubble = true;
        e.returnValue = false;
        return false;
    }
}
function openNewApplicationWindow(/*:String*/ browserId) /*PUBLIC*/
{
    var newUrl="";
    if(document.location.href.split('/ns_content')[0])
        newUrl=document.location.href.split('/ns_content')[0]+"/ns_index.jsp?browser="+browserId;
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
    // alle eventuell geöffneten Popup/Menu schliessen
    //
    hideContextMenu();

    element.addClassName("Multiselection_TitleOver");
    var vsSubmenu       = element.getAttribute( "submenu" );

    Multiselection_collapseMenu();
    if (vsSubmenu!=null && vsSubmenu != "" )
    {
        var containerLayer = new LayerObject(element.getAttribute( "container" ));
        var formLayer      =  new LayerObject("formDiv");
        var pos2 = containerLayer.realPosition();
        var pos3 = formLayer.realPosition();

        // wenn das Element ein Kind des Form DIV ist, dann muss
        // die Position korrigiert werden. Die ComboBox des SearchBrowsers ist z.B.
        // kein Kind des Form DIV.
        //
        if(isChild(containerLayer.obj, formLayer.obj))
        {
           pos2.x = pos2.x -pos3.x-1;
           pos2.y = pos2.y -pos3.y-1;
        }

        var mnuTarget= new LayerObject( vsSubmenu );

        mnuTarget.moveTo(pos2.x,pos2.y+pos2.height);
        mnuTarget.style.zIndex=200;
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
          mnuTarget.moveTo(pos2.x,pos2.y+pos2.height-element.getHeight()-mnuTarget.getHeight()-3);
        }

        Multiselection_elActiveMenu = $(vsSubmenu);
    }
    onFocusElement($(element.getAttribute( "container" )));
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

function realPosition(el)
{
 var d = new Object();
 if(document.getBoxObjectFor)
 {
   d.x = document.getBoxObjectFor(el).x;
   d.y =  document.getBoxObjectFor(el).y;
   d.w = document.getBoxObjectFor(el).width;
   d.h = document.getBoxObjectFor(el).height;
 }
 else
 {
   d.x = offsetBy.call(el, null, 'Left');
   d.y = offsetBy.call(el, null, 'Top');
   d.w = el.offsetWidth;
   d.h = el.offsetHeight;
 }
 return d;
}

function offsetBy(el, type) {
  if (this===el) return 0;
  var v=999, owner=this, border='client'+type;
  type = 'offset'+type;

  do {
    v += owner[type];
  } while ((owner=owner.offsetParent) && owner!==el && (v+=owner[border]))
  return v-999;
}


function Multiselection_doMenuMouseOut(element )
{
    $(element).removeClassName("Multiselection_TitleOver");
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

function Multiselection_collapseMenu( )
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


function Listbox_doDeselectAll(valueItem, container)
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

/* set the input foucs to the element with the hands over id and move them to the cursor position
 */
var lockSetFocus=false;
function setFocus(controlId, caretPosition)
{
    if(lockSetFocus==true)
        return;
    var obj=getObj(controlId);
    if(obj!=null)
    {

      var page=visiblePage();
      if(page)
      {
          // Falls es sich um ein ForeignField handelt welches von 'backfilled'->empty wechselt.
          // z.B. wenn der Anwender den Text in einem gebackfilled ForeingField mit der Maus selektiert und l???cht.
          // (FireEvent wird gefeuert und es geht zum Server um das ForeingField/TableAlias zu deselektieren)
          // Der Anwender tippt aber weiter. Die eingetippten Werte werden dann in das neue Feld bernommen.
          //
          var pageObj= page.getObj(controlId);
          if(pageObj && pageObj.getAttribute('state') && pageObj.getAttribute('state')=='backfilled')
          {
              obj.value=pageObj.value;
              caretPosition = page.getCaretPosition(pageObj);
          }
      }

      try
      {
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
  // Wenn man bei einem versteckten INPUT Element den cursor setzen mï¿½chte
  // kracht es
  //
  if(input.type=="hidden")
    return;

  input.focus();
  input.setSelectionRange(selectionStart, selectionEnd);
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
function getCaretPosition(obj )
{
   if(obj)
	  return obj.selectionEnd;
	 return 0;
}


/* select the hands over text in the element
 */
function selectString (input, string) 
{
  var match = new RegExp(string, "i").exec(input.value);
  if (match) {
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
    else // set caret
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
  Event.stop(event);
  return event.ctrlKey;
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
 *  PRIVATE METHOD
 **/
function Divider_onMouseDown(event) 
{
    Divider_dragObject= (event.target.id == "divider")?event.target:null;
    if(Divider_dragObject!=null)
    {
       hideContextMenu();
	   Multiselection_collapseMenu();
    }
}


/**
 *  PRIVATE METHOD
 **/
function Divider_onMouseUp(event )
{
    Divider_dragObject=null;
}

/**
 *  PRIVATE METHOD
 **/
var oldMousePos =0;
function Divider_onMouseMove(event )
{
    if(oldMousePos==0)
        oldMousePos=event.pageY;

    if (Divider_dragObject!=null)
    {
        getObj('dividerPos').value=event.pageY;
        resizeSearchBrowserCallback();
        new LayerObject("formDiv").css.top = new LayerObject("searchBrowserTable").height+"px";
    }
    oldMousePos=event.pageY;
}

/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD.
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
 * PUBLIC METHOD.
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
  this.width  = window.innerWidth;
  this.height = window.innerHeight;
}

/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD.
 *
 * Create a crossbrowser layer object with some handle functions.
 *
 * Usage:
 *  var obj = new LayerObject('myObjectId');
 *  obj.show();
 *  obj.hide();
 *  obj.moveTo(x,y);
 *  obj.mveBy(xoffset,yoffset);
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

  this.name=objId;
  this.css=this.obj.style;
  this.style=this.obj.style;
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
 if(document.getBoxObjectFor)
 {
   d.x = document.getBoxObjectFor(this.obj).x;
   d.y = document.getBoxObjectFor(this.obj).y;
   d.width = document.getBoxObjectFor(this.obj).width;
   d.height = document.getBoxObjectFor(this.obj).height;
 }
 else // Safari fallback
 {
   d.x = offsetBy.call(this.obj, null, 'Left');
   d.y = offsetBy.call(this.obj, null, 'Top');
   d.width = this.obj.offsetWidth;
   d.height = this.obj.offsetHeight;
 }
 return d;
}

function offsetBy(el, type) 
{
  if (this===el) return 0;
  var v=999, owner=this, border='client'+type;
  type = 'offset'+type;
  do 
  {
     v += owner[type];
   } while ((owner=owner.offsetParent) && owner!==el && (v+=owner[border]))
   return v-999;
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

//crossbrowser show
LayerObject.prototype.show= function()
{
  this.css.display='block';
}

//crossbrowser hide
LayerObject.prototype.hide= function()
{
  this.css.display='none';
}

//crossbrowser move absolute
LayerObject.prototype.moveTo= function(/*:int*/x, /*:int*/y)
{
  this.x = x;
  this.y = y;
  this.css.left=x;
  this.css.top=y;
}

//crossbrowser move relative
LayerObject.prototype.moveBy= function(/*:int*/x,/*:int*/y)
{
  this.moveTo(this.x+x, this.y+y);
}

//write text into a layer (not supported by Opera 5!)
//this function is not w3c-dom compatible but ns6
//support innerHTML also!
//Opera 5 does not support change of layer content!!
LayerObject.prototype.writeText= function(/*:String*/ text)
{
    this.obj.innerHTML=text;
}

//write text into a layer (not supported by Opera 5!)
//this function is not w3c-dom compatible but ns6
//support innerHTML also!
//Opera 5 does not support change of layer content!!
LayerObject.prototype.appendText= function(/*:String*/ text)
{
    this.obj.innerHTML=this.obj.innerHTML+text;
}

LayerObject.prototype.getRuntimeStyle= function(/*:String*/ styleName)
{
    return (getComputedStyle(this.obj,'').getPropertyValue(styleName));
}


/*******************************************************************************************************
 *******************************************************************************************************
 *
 * PUBLIC METHOD.
 *
 * Display the hands over ContextMenu
 *
 ******************************************************************************************************* 
 *******************************************************************************************************/
function showContextMenu(contextMenuId)
{
    // altes contextmenu verbergen
    //
    hideContextMenu();
//    hideForeignField();

    currentMenu = new LayerObject(contextMenuId);
    currentMenu.moveTo(innerMouseX,innerMouseY);
    currentMenu.show();
}

/******************************************************************************************************
 * PRIVATE FUNCTION
 *
 *****************************************************************************************************/
function hideContextMenu()
{
    if(currentMenu!=null)
        currentMenu.hide();
    currentMenu=null;
    // das Contextmenu kann auch ein ColumnPicker sein
    // dies wird dann komplett aus dem Baum ausgehï¿½ngt
    //
    var columnPickerMenu = getObj("columnPicker");
    if(columnPickerMenu!=null)
    {
       columnPickerMenu.parentNode.removeChild(columnPickerMenu);
    }

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
/* Das Aufrufen eines ForeignFields sollte asynchron zum Seitenaufbau passieren.
 * Wenn man das Fenster gleich ???fnet hat man 'sehr' lange eine wei??? Seite.
 * Die wei??? Seite bleibt so lange, bis das Fenster des ForeignFields dargestellt ist.
 * Mit dem Timer ist das Laden des ForeignFields mit dem Laden der Hauptseite entkoppelt.
 */
function showForeignFieldDialog(anchorId, clientBrowserId, foreignBrowserId)
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefï¿½hrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund drï¿½ngt.
    lockSetFocus=true;

    var pos =getAnchorWindowPosition(anchorId);
    foreignUrl="ns_foreignField.jsp?browser="+clientBrowserId+"&foreignBrowser="+foreignBrowserId;
    var testpopup1 = new PopupWindow();
    var dividerFiller= (browserVisible==true)?getObj('dividerFiller').height:0;
    testpopup1.offsetY = (-getObj('formDiv').scrollTop)+30-dividerFiller;
    // temp change because of Daimler request
    testpopup1.setSize(450,180);
    testpopup1.autoHide();
    testpopup1.setUrl(foreignUrl);
    testpopup1.showPopup(anchorId);
    testpopup1.popupWindow.moveTo(globalMouseX-150,globalMouseY+15);
}


function showForeignFieldDialogInline(anchorId, browserContentDivId)
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefï¿½hrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund drï¿½ngt.
    lockSetFocus=true;

    var pos =getAnchorPosition(anchorId);
    var dividerFiller= getObj('dividerFiller').height;
    var offsetY=pos.y/*-parseInt(getObj('dividerPos').value)*/+ 25-dividerFiller;
    var browser = new LayerObject(browserContentDivId);
    browser.css.height="140px";
    browser.moveTo(pos.x-marginLeft,offsetY);
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
    if(inlineForeignFieldIsVisible==true && getObj('foreign_field_inline')!=null)
        new LayerObject('foreign_field_inline').hide();
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
function popupModal(_url, width, height)
{
    // falls ein fenster geï¿½ffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefuehrt werden, da die das geï¿½ffnete Fenster sonst in den
    // Hintergrund draengt.
    lockSetFocus=true;

    var winleft = (screen.width - width) / 2;
    var winUp   = (screen.height - height) / 2;

    var newWindow = window.open(_url, "_blank", 'left='+winleft+', top='+winUp+', toolbar=0,scrollbars=0,location=0,status=no, menubar=0,resizable=1,alwaysRaised, width='+width+',height='+height);
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
    // falls ein Fenster geoeffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefuehrt werden, da die das geÃ¶ffnete Fenster sonst in den
    // Hintergrund drÃ¤ngt.
    lockSetFocus=true;

    this.width    = width;
    this.height   = height;
    this.url      = url;
    this.winleft  = (screen.width - width) / 2;
    this.winUp    = (screen.height - height) / 2;
    this.scroll   = withScrollbars?"1":"0";
}

/**
 *
 **/
jACOBPopup.prototype.show=function()
{
    window.open(this.url, "_blank", 'left='+this.winleft+', top='+this.winUp+', toolbar=0,scrollbars='+this.scroll+',location=0,status=no, menubar=0,resizable=1,alwaysRaised, width='+this.width+',height='+this.height);
}

/*******************************************************************************************************
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
function jACOBWindow(url, width, height, withScrollbars)
{
    windowsToShow.push(this);

    // falls ein fenster geoeffnet wird, darf die Funktion 'setFocus'
    // nicht ausgefuehrt werden, da die das geÃ¶ffnete Fenster sonst in den
    // Hintergrund drÃ¤ngt.
    lockSetFocus=true;
    this.width    = width;
    this.height   = height;
    this.url      = url;
    this.winleft  = (screen.width - width) / 2;
    this.winUp    = (screen.height - height) / 2;
    this.scroll   = withScrollbars?"1":"0";
}


/**
 *
 **/
jACOBWindow.prototype.show=function()
{
    window.open(this.url, '_blank', 'left='+this.winleft+', top='+this.winUp+', toolbar=1,scrollbars='+this.scroll+',location=1,statusbar=1,menubar=1,resizable=1,alwaysRaised,width='+this.width+',height='+this.height);
}



/*******************************************************************************************************
 *******************************************************************************************************
 * PUBLIC METHOD!
 *
 * Show e new browser window which close itself if the lost the focus.
 *******************************************************************************************************
 *******************************************************************************************************/
function jACOBAutocloseWindow(anchorId, url, width, height)
{
  windowsToShow.push(this);

  this.anchorId = anchorId;
  this.url      = url;
  this.width    = width;
  this.height   = height;
}

jACOBAutocloseWindow.prototype.show=function()
{
    // falls ein fenster ge???net wird, darf die Funktion 'setFocus'
    // nicht ausgefhrt werden, da die das ge???fnete Fenster sonst in den
    // Hintergrund drÃ¤ngt.
    lockSetFocus=true;

    var testpopup1 = new PopupWindow();
    testpopup1.offsetY = 25;
    testpopup1.setSize(this.width,this.height);
    testpopup1.autoHide();
    testpopup1.setUrl(this.url);
    testpopup1.showPopup(this.anchorId);;
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
    var head = document.getElementsByTagName('head').item(0)
    var scriptTag = document.getElementById('serverPush');
    if(scriptTag)
        head.removeChild(scriptTag);
    script = document.createElement('script');
    script.src = page;
    script.type = 'text/javascript';
    script.id = 'serverPush';
    head.appendChild(script);
}


/**
 *
 *
**/
function adjustOutlookbar()
{
   var parentContainer = new LayerObject('scrollAreaParent');
   var childContainer  = new LayerObject('outlookbarButtonArea');
   // es ist nicht gesagt, dass es eine outlookbar gibt
   //
   if(childContainer.obj==null)
    return;
   childContainer.css.height="1px";
   childContainer.css.height=parentContainer.getRuntimeStyle('height');
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
      toolbarHeight =  parseInt(new LayerObject("toolbar").getRuntimeStyle("height"));

    if(tabfooterHeight==-1)
      tabfooterHeight =  parseInt(new LayerObject("searchTabFooter").getRuntimeStyle("height"));

    if(actionfooterHeight==-1)
      actionfooterHeight =  parseInt(new LayerObject("searchActionFooter").getRuntimeStyle("height"));

    if(halfDividerHeight==-1)
      halfDividerHeight=new LayerObject("dividerFiller").height/2;

    if(toReduceContentHeight==-1)
      toReduceContentHeight = actionfooterHeight+tabfooterHeight+toolbarHeight+(new LayerObject("dividerFiller").height);

    if(toolbarLeftWidth==-1)
    {
      var element = new LayerObject('toolbar_left');
      toolbarLeftWidth= element.obj?parseInt(element.getRuntimeStyle('width')):0;
    }

    if(searchBrowser!=null)
    {
      searchBrowser.root.style.width = (parseInt(new LayerObject('searchBrowserTable').getRuntimeStyle("width"))-marginLeft-toolbarLeftWidth)+"px";
      searchBrowser.root.style.height= Math.max(80,parseInt(getObj('dividerPos').value)+halfDividerHeight-toReduceContentHeight)+"px";
      searchBrowser.resize();
   }
   adjustOutlookbar();
   if(window['onContentAreaResize'])
   {
     onContentAreaResize(parseInt(searchBrowser.root.style.width),$("formDiv").getHeight());
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
function onFocusElement(/*:HTMLElement*/ obj, /*:String*/ objName)
{
   // Falls das neue Eingabeelement "autosuggest" unterstï¿½tzt, wird dies durch
   // ein Glühbirnchen dargestellt.
   //
   var bulb = new LayerObject("autosuggestBulb");
   bulb.hide();
   if(obj.getAttribute("autosuggest")=="true")
   {
      var inputElement = getAnchorPosition(obj.id);//new LayerObject(obj.id);
      var formDiv = getAnchorPosition("formDiv");
      bulb.moveTo(inputElement.x-4-formDiv.x,inputElement.y+3-formDiv.y);
      bulb.show();
   }

   // Highlight the current element which has the focus
   if(lastFocusElement!=null)
     lastFocusElement.removeClassName("focus");
   lastFocusElement =  $(obj);

   if(lastFocusElement.getAttribute("readonly")==null)
   {
     lastFocusElement.addClassName("focus");
     // Für den nachsten Serverrequest merken welches Element gerade den Fokus hatte
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
 * Wird fuer die Tastaturnavigation des Searchbrowsers benoetigt
 *                                                                           
 *******************************************************************************************************
 ******************************************************************************************************/
var currentHighlightedRow=-1;
var currentHoverRow=-1;
var tableRows=null;
function calculateCurrentHighlightedRow(browserId)
{
    if(currentHighlightedRow!=-1)
        return;
    var table = getObj(browserId);
    tableRows = table.getElementsByTagName("tr");
    // Die Zeile finden welche selected="true" hat.
    // 
    for(var i=0;i< tableRows.length;i++)
    {
      if(tableRows[i].getAttribute("selected"))
      {
        currentHighlightedRow=i;
      }
    }
    if(currentHighlightedRow==-1)
    {
        currentHighlightedRow=0;
        currentHoverRow=0;
        if(tableRows[currentHoverRow].onmouseover)
          tableRows[currentHoverRow].onmouseover();
    }
}

/**
 * Die naechste Zeile hervorheben  
 **/
function onCursorDownKey(browserId)
{
	calculateCurrentHighlightedRow(browserId);
	
	
	var last=currentHoverRow;
	if(currentHoverRow==-1)
		currentHoverRow = currentHighlightedRow+1;
	else
	  currentHoverRow = currentHoverRow+1;
	if(tableRows[currentHoverRow].getAttribute("filler"))
	{
	  currentHoverRow = last;
	  return;
	}
  if(last!=-1 && tableRows[last].onmouseout)
		tableRows[last].onmouseout();
	if(tableRows[currentHoverRow].onmouseover)
		tableRows[currentHoverRow].onmouseover();
	var table = getObj(browserId);
	table.scrollTop=table.scrollTop+tableRows[currentHoverRow].offsetHeight;
}

/**
 *  die Vorgï¿½nger Zeile hervorheben
 **/
function onCursorUpKey(browserId)
{
	calculateCurrentHighlightedRow(browserId);
	var last=currentHoverRow;
	if(currentHoverRow==-1)
		currentHoverRow = currentHighlightedRow-1;
	else
	  currentHoverRow = currentHoverRow-1;

	if(currentHoverRow<0)
	{
	  currentHoverRow=0;
	  return;
	}
  if(last!=-1 && tableRows[last].onmouseout)
		tableRows[last].onmouseout();
	if(tableRows[currentHoverRow].onmouseover)
		tableRows[currentHoverRow].onmouseover();
	var table = getObj(browserId);
	table.scrollTop=table.scrollTop-tableRows[currentHoverRow].offsetHeight;
}

function onEnter()
{
		if(currentHoverRow!=-1 && tableRows[currentHoverRow].onclick)
			tableRows[currentHoverRow].onclick();
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
  $(caretId).value = textarea.selectionStart;
  if(textarea.selectionStart<textarea.selectionEnd)
  {
    $(startId).value = textarea.selectionStart;
    $(endId).value = textarea.selectionEnd;
  }
  else
  {
    $(startId).value = textarea.selectionEnd;
    $(endId).value = textarea.selectionStart;
  }
}
