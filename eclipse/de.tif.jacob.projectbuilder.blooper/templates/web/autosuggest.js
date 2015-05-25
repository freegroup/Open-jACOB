function TestSuggestions()
{
}

TestSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,bTypeAhead /*:boolean*/) 
{
  var aSuggestions = new Array("Test1","Test2","Test3");
  //provide suggestions to the control
  oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
}

TestSuggestions.prototype.select = function (controlId, index)
{
}


function RemoteSuggestions()
{
    if (typeof XMLHttpRequest != "undefined") {
        this.http = new XMLHttpRequest();
    } else if (typeof ActiveXObject != "undefined") {
        this.http = new ActiveXObject("MSXML2.XmlHttp");
    } else {
        alert("No XMLHttpRequest object available. This functionality will not work.");
    }
    this.functionJSPName = "getDB_Field";
}

RemoteSuggestions.prototype.setProvider=function(/*:String*/name)
{
    this.functionJSPName = name;
}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
RemoteSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,bTypeAhead /*:boolean*/) 
{
    var oHttp = this.http;

    //if there is already a live request, cancel it
    if (oHttp.readyState != 0) 
    {
        oHttp.abort();
    }

    var sURL = this.functionJSPName+".jsp?browser="+browserId+"&userInput=" + encodeURIComponent(oAutoSuggestControl.textbox.value);


    //open connection to states.txt file
    oHttp.open("get", sURL , true);
    oHttp.onreadystatechange = function ()
    {
        if (oHttp.readyState == 4) 
        {
            //evaluate the returned text JavaScript (an array)
            var aSuggestions = eval(oHttp.responseText);
            //provide suggestions to the control
            oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
        }
    };
    oHttp.send(null);
}

RemoteSuggestions.prototype.select = function (controlId, index)
{
}



function AutoSuggestControl(dialog /*:Dialog*/, oTextbox /*:HTMLInputElement*/ , suggestProvider)
{
  this.cur /*:int*/ = -1;
  this.layer = null;
  if(suggestProvider)
    this.provider = suggestProvider;
  else
    this.provider = new RemoteSuggestions();

  this.textbox = oTextbox;
  this.dialog  = dialog;
  this.init();
}

AutoSuggestControl.prototype.setProvider=function(/*:String*/name)
{
    this.provider.setProvider(name);
}

/**
 * Autosuggests one or more suggestions for what the user has typed.
 * If no suggestions are passed in, then no autosuggest occurs.
 * @scope private
 * @param aSuggestions An array of suggestion strings.
 * @param bTypeAhead If the control should provide a type ahead suggestion.
 */
AutoSuggestControl.prototype.autosuggest = function (aSuggestions /*:Array*/,
                                                     bTypeAhead /*:boolean*/) {

  //make sure there's at least one suggestion
  if (aSuggestions && aSuggestions.length > 0)
  {
    if (bTypeAhead)
    {
        this.typeAhead(aSuggestions[0]);
    }
    this.showSuggestions(aSuggestions);
  }
  else
  {
    this.hideSuggestions();
  }
}

/**
 * Creates the dropdown layer to display multiple suggestions.
 * @scope private
 */
AutoSuggestControl.prototype.createDropDown = function () 
{

    var oThis = this;
    //create the layer and assign styles
    this.layer = document.createElement("div");
    this.layer.style.position="absolute";
    this.layer.style.border="1px solid black";
    this.layer.style.backgroundColor="white";
    this.layer.style.mozBoxSizing="border-box";
    this.layer.style.boxSizing="border-box";
    this.layer.style.padding="2px";
    this.layer.style.visibility="hidden";

    //when the user clicks on the a suggestion, get the text (innerHTML)
    //and place it into a textbox
    this.layer.onmousedown =
    this.layer.onmouseup =
    this.layer.onmouseover = function (oEvent)
    {
        oEvent = oEvent || window.event;
        oTarget = oEvent.target || oEvent.srcElement;

        if (oEvent.type == "mousedown") {
            oThis.textbox.value = oTarget.firstChild.nodeValue;
            oThis.cur = oTarget.index;
            oThis.hideSuggestions();
        } else if (oEvent.type == "mouseover") {
            oThis.highlightSuggestion(oTarget);
        } else {
            oThis.textbox.focus();
        }
    }

    this.dialog.appendChild(this.layer);

    this.bulp  = document.createElement("img");
    this.bulp.style.position="absolute";
    this.bulp.style.visibility ="hidden";
    this.bulp.style.left = parseInt(this.textbox.style.left)-10+"px";
    this.bulp.style.top =this.textbox.style.top;
    this.bulp.src = "bulp.gif";
    this.bulp.title= "Press CTRL+Space for autosuggest";
    this.dialog.appendChild(this.bulp);
}

/**
 * Gets the left coordinate of the textbox.
 * @scope private
 * @return The left coordinate of the textbox in pixels.
 */
AutoSuggestControl.prototype.getLeft = function () /*:int*/ 
{
  return parseInt(this.textbox.style.left);
}

/**
 * Gets the top coordinate of the textbox.
 * @scope private
 * @return The top coordinate of the textbox in pixels.
 */
AutoSuggestControl.prototype.getTop = function () /*:int*/ 
{
  return parseInt(this.textbox.style.top);
}

/**
 * Handles three keydown events.
 * @scope private
 * @param oEvent The event object for the keydown event.
 */
AutoSuggestControl.prototype.handleKeyDown = function (oEvent /*:Event*/) 
{
    switch(oEvent.keyCode) 
    {
        case 38: //up arrow
            this.previousSuggestion();
            break;
        case 40: //down arrow
            this.nextSuggestion();
            break;
        case 13: //enter
            this.hideSuggestions();
            break;
        case 27: //esc
            this.cur=-1;
            oEvent.cancelBubble = true;
            oEvent.returnValue = false;
            this.hideSuggestions();
            break;
    }
};

/**
 * Handles keyup events.
 * @scope private
 * @param oEvent The event object for the keyup event.
 */
AutoSuggestControl.prototype.handleKeyUp = function (oEvent /*:Event*/) {

    var iKeyCode = oEvent.keyCode;
    //for backspace (8) and delete (46), shows suggestions without typeahead
    if (iKeyCode == 8 || iKeyCode == 46) 
    {
      this.provider.requestSuggestions(this, false);
      //make sure not to interfere with non-character keys
    }
    else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode < 46) || (iKeyCode >= 112 && iKeyCode <= 123)) 
    {
      //ignore
    }
    else
    {
      //request suggestions from the suggestion provider with typeahead
      this.provider.requestSuggestions(this, true);
    }
}

/**
 * Hides the suggestion dropdown.
 * @scope private
 */
AutoSuggestControl.prototype.hideSuggestions = function () 
{
  if(this.layer.style.visibility=="hidden")
    return;
  this.layer.style.visibility="hidden";

  if(this.cur==-1)
    return;

  this.provider.select(this.textbox.getAttribute("eventId"),this.cur);
}

/**
 * Highlights the given node in the suggestions dropdown.
 * @scope private
 * @param oSuggestionNode The node representing a suggestion in the dropdown.
 */
AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) 
{
    for (var i=0; i < this.layer.childNodes.length; i++) 
    {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode) 
            oNode.style.backgroundColor="#d0d0d0";
        else
            oNode.style.backgroundColor="white";
    }
}

/**
 * Initializes the textbox with event handlers for
 * auto suggest functionality.
 * @scope private
 */
AutoSuggestControl.prototype.init = function () {

    //save a reference to this object
    var oThis = this;

    //assign the onkeyup event handler
    this.textbox.onkeyup = function (oEvent) {

        //check for the proper location of the event object
        if (!oEvent) {
            oEvent = window.event;
        }

        //call the handleKeyUp() method with the event object
        oThis.handleKeyUp(oEvent);
    };

    //assign onkeydown event handler
    this.textbox.onkeydown = function (oEvent) {

        //check for the proper location of the event object
        if (!oEvent) {
            oEvent = window.event;
        }
        //call the handleKeyDown() method with the event object
        oThis.handleKeyDown(oEvent);
    };

    //assign onblur event handler (hides suggestions)    
    this.textbox.onblur = function () {
        oThis.hideSuggestions();
        oThis.bulp.style.visibility = "hidden";
    };

    //assign onblur event handler (hides suggestions)
    this.textbox.onfocus = function () {
        oThis.bulp.style.visibility = "visible";
    };

    //create the suggestions dropdown
    this.createDropDown();
}

/**
 * Highlights the next suggestion in the dropdown and
 * places the suggestion into the textbox.
 * @scope private
 */
AutoSuggestControl.prototype.nextSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length-1) {
        var oNode = cSuggestionNodes[++this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue;
    }
};

/**
 * Highlights the previous suggestion in the dropdown and
 * places the suggestion into the textbox.
 * @scope private
 */
AutoSuggestControl.prototype.previousSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur > 0) {
        var oNode = cSuggestionNodes[--this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue;   
    }
}

/**
 * Selects a range of text in the textbox.
 * @scope public
 * @param iStart The start index (base 0) of the selection.
 * @param iLength The number of characters to select.
 */
AutoSuggestControl.prototype.selectRange = function (iStart /*:int*/, iLength /*:int*/) 
{
    //use text ranges for Internet Explorer
    if (this.textbox.createTextRange) {
        var oRange = this.textbox.createTextRange(); 
        oRange.moveStart("character", iStart); 
        oRange.moveEnd("character", iLength - this.textbox.value.length);      
        oRange.select();
    //use setSelectionRange() for Mozilla
    } else if (this.textbox.setSelectionRange) {
        this.textbox.setSelectionRange(iStart, iLength);
    }

    //set focus back to the textbox
    this.textbox.focus();
}

/**
 * Builds the suggestion layer contents, moves it into position,
 * and displays the layer.
 * @scope private
 * @param aSuggestions An array of suggestions for the control.
 */
AutoSuggestControl.prototype.showSuggestions = function (aSuggestions /*:Array*/) 
{
    var oDiv = null;
    this.layer.innerHTML = "";  //clear contents of the layer

    for (var i=0; i < aSuggestions.length; i++) 
    {
        oDiv = document.createElement("div");
        oDiv.index=i;
        oDiv.style.width="100%";
        oDiv.innerHTML=aSuggestions[i];
        this.layer.appendChild(oDiv);
    }
    if(aSuggestions.length>15)
    {
//      this.layer.style.overflow="auto";
//      this.layer.style.height="150px";
    }

    this.layer.style.left = this.getLeft() + "px";
    this.layer.style.top  = (this.getTop()+this.textbox.offsetHeight) + "px";
    this.layer.style.visibility = "visible";
}

/**
 * Inserts a suggestion into the textbox, highlighting the 
 * suggested part of the text.
 * @scope private
 * @param sSuggestion The suggestion for the textbox.
 */
AutoSuggestControl.prototype.typeAhead = function (sSuggestion /*:String*/) {
/*
    //check for support of typeahead functionality
    if (this.textbox.createTextRange || this.textbox.setSelectionRange){
        var iLen = this.textbox.value.length; 
      this.textbox.value = sSuggestion;
        this.selectRange(iLen, sSuggestion.length);
    }
*/
}

