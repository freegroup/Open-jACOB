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
var RCS_ID_AUTOSUGGEST = "$Id: autosuggest.js,v 1.4 2010/10/15 11:40:40 freegroup Exp $";

function RemoteDialogSuggestions() 
{
    if (typeof XMLHttpRequest != "undefined") {
        this.http = new XMLHttpRequest();
    } else if (typeof ActiveXObject != "undefined") {
        this.http = new ActiveXObject("MSXML2.XmlHttp");
    } else {
        alert("No XMLHttpRequest object available. This functionality will not work.");
    }
}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
RemoteDialogSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,bTypeAhead /*:boolean*/) 
{
    var oHttp = this.http;
                                                             
    //if there is already a live request, cancel it
    if (oHttp.readyState != 0) 
    {
        oHttp.abort();
    }  
    var caret= getCaretPosition(oAutoSuggestControl.textbox);    
    if(caret==null) 
    	caret =oAutoSuggestControl.textbox.value.length;
    var sURL = "suggestions.jsp?browser="+browserId+"&control="+oAutoSuggestControl.textbox.id+"&guid="+guid+"&caretPosition="+caret+"&userInput=" + encodeURIComponent(oAutoSuggestControl.textbox.value);
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
};
RemoteDialogSuggestions.prototype.select = function (controlId, index) 
{
  // do nothing at the moment. Call the server in the future
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
    
    //build the URL
    var caret= getCaretPosition(oAutoSuggestControl.textbox);    
    if(caret==null) 
    	caret =oAutoSuggestControl.textbox.value.length;
    var sURL = "suggestions.jsp?browser="+browserId+"&control="+oAutoSuggestControl.textbox.id+"&caretPosition="+caret+"&userInput=" + encodeURIComponent(oAutoSuggestControl.textbox.value);
    
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
};
RemoteSuggestions.prototype.select = function (controlId, index) 
{
	FireEventData(controlId,"autosuggest",index);
}

function AutoSuggestControl(oTextbox /*:HTMLInputElement*/ , suggestProvider) 
{
    this.cur /*:int*/ = -1;
    this.layer = null;
    if(suggestProvider)
    	this.provider = suggestProvider;
    else
	    this.provider = new RemoteSuggestions();
    this.textbox /*:HTMLInputElement*/ = oTextbox;

    this.init();
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
    if (aSuggestions.length > 0) 
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
};

/**
 * Creates the dropdown layer to display multiple suggestions.
 * @scope private
 */
AutoSuggestControl.prototype.createDropDown = function () {
    var oThis = this;
    this.layer = getObj("autosuggest_"+this.textbox.id);
    
    this.layer.style.width = this.textbox.offsetWidth;
    
    //when the user clicks on the a suggestion, get the text (innerHTML)
    //and place it into a textbox
    this.layer.onmousedown = 
    this.layer.onmouseup = 
    this.layer.onmouseover = function (oEvent) {
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
    };
};

/**
 * Gets the left coordinate of the textbox.
 * @scope private
 * @return The left coordinate of the textbox in pixels.
 */
AutoSuggestControl.prototype.getLeft = function () /*:int*/ 
{
    return parseInt(new LayerObject(this.textbox.id).css.left);    
};

/**
 * Gets the top coordinate of the textbox.
 * @scope private
 * @return The top coordinate of the textbox in pixels.
 */
AutoSuggestControl.prototype.getTop = function () /*:int*/ 
{
    return parseInt(new LayerObject(this.textbox.id).css.top);    
};

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
    if (iKeyCode == 8 || iKeyCode == 46) {
        this.provider.requestSuggestions(this, false);
        
    //make sure not to interfere with non-character keys
    } else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode < 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
        //ignore
    } else {
        //request suggestions from the suggestion provider with typeahead
        this.provider.requestSuggestions(this, true);
    }
};

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
AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) {
    
    for (var i=0; i < this.layer.childNodes.length; i++) {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode) {
            oNode.className = "current"
        } else if (oNode.className == "current") {
            oNode.className = "";
        }
    }
};

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
    };
    
    //create the suggestions dropdown
    this.createDropDown();
};

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
};

/**
 * Selects a range of text in the textbox.
 * @scope public
 * @param iStart The start index (base 0) of the selection.
 * @param iLength The number of characters to select.
 */
AutoSuggestControl.prototype.selectRange = function (iStart /*:int*/, iLength /*:int*/) {
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
    
}; 

/**
 * Builds the suggestion layer contents, moves it into position,
 * and displays the layer.
 * @scope private
 * @param aSuggestions An array of suggestions for the control.
 */
AutoSuggestControl.prototype.showSuggestions = function (aSuggestions /*:Array*/) {
    
    var oDiv = null;
    this.layer.innerHTML = "";  //clear contents of the layer
    
    for (var i=0; i < aSuggestions.length; i++) {
        oDiv = document.createElement("div");
        oDiv.index=i;
        oDiv.style.width="100%";
        oDiv.innerHTML=aSuggestions[i];
//        oDiv.appendChild(document.createTextNode(aSuggestions[i]));
        this.layer.appendChild(oDiv);
    }
    
    this.layer.style.left = this.getLeft() + "px";
    this.layer.style.top = (this.getTop()+this.textbox.offsetHeight) + "px";
    this.layer.style.visibility = "visible";
    // im fall eines Dialoges kann sich die Größe verandern,
    // aus diesem Grund wird diese jedes mal neu berechnet.
    //
    this.layer.style.minWidth = this.textbox.offsetWidth;
    this.layer.style.width = "auto";

};

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
};



//==============================================================================
var Autocomplete = function(el, options){
  this.el = $(el);
  this.id = this.el.identify();
  this.el.setAttribute('autocomplete','off');
  this.suggestions = [];
  this.badQueries = [];
  this.selectedIndex = -1;
  this.currentValue = this.el.value;
  this.intervalId = 0;
  this.cachedResponse = [];
  this.instanceId = null;
  this.onChangeInterval = null;
  this.ignoreValueChange = false;
  this.serviceUrl = options.serviceUrl;
  this.options = {
    autoSubmit:false,
    minChars:1,
    maxHeight:300,
    deferRequestBy:0,
    width:0,
    container:null
  };
  if(options){ Object.extend(this.options, options); }
  this.initialize();
};

Autocomplete.instances = [];

Autocomplete.getInstance = function(id){
  var instances = Autocomplete.instances;
  var i = instances.length;
  while(i--){ if(instances[i].id === id){ return instances[i]; }}
};

Autocomplete.highlight = function(value, re){
  return value.replace(re, function(match){ return '<strong>' + match + '<\/strong>' });
};

Autocomplete.prototype = {

  killerFn: null,

  initialize: function() {
    var me = this;
    this.killerFn = function(e) {
      if (!$(Event.element(e)).up('.autocomplete')) {
        me.killSuggestions();
        me.disableKillerFn();
      }
    } .bindAsEventListener(this);

    var div = new Element('div', { style: 'z-index:5000;position:absolute;' });
    div.update('<div class="autocomplete" id="Autocomplete_' + this.id + '" style="display:none;"></div>');

    this.options.container = $(this.options.container);
    if (this.options.container) {
      this.options.container.appendChild(div);
      this.fixPosition = function() { };
    } else {
      document.body.appendChild(div);
    }

    this.mainContainerId = div.identify();
    this.container = $('Autocomplete_' + this.id);
    this.fixPosition();
    
    Event.observe(this.el, window.opera ? 'keypress':'keydown', this.onKeyPress.bind(this));
    Event.observe(this.el, 'keyup', this.onKeyUp.bind(this));
    Event.observe(this.el, 'blur', this.enableKillerFn.bind(this));
    Event.observe(this.el, 'focus', this.fixPosition.bind(this));
    this.container.setStyle({ maxHeight: this.options.maxHeight + 'px' });
    this.instanceId = Autocomplete.instances.push(this) - 1;
  },

  fixPosition: function() {
    var offset = this.el.cumulativeOffset();
    $(this.mainContainerId).setStyle({ top: (offset.top + this.el.getHeight()) + 'px', left: offset.left + 'px' });
  },

  enableKillerFn: function() {
    Event.observe(document.body, 'click', this.killerFn);
  },

  disableKillerFn: function() {
    Event.stopObserving(document.body, 'click', this.killerFn);
  },

  killSuggestions: function() {
    this.stopKillSuggestions();
    this.intervalId = window.setInterval(function() { this.hide(); this.stopKillSuggestions(); } .bind(this), 300);
  },

  stopKillSuggestions: function() {
    window.clearInterval(this.intervalId);
  },

  onKeyPress: function(e) {
    if (!this.enabled) { return; }
    // return will exit the function
    // and event will not fire
    switch (e.keyCode) {
      case Event.KEY_ESC:
        this.el.value = this.currentValue;
        this.hide();
        break;
      case Event.KEY_TAB:
      case Event.KEY_RETURN:
        if (this.selectedIndex === -1) {
          this.hide();
          return;
        }
        this.select(this.selectedIndex);
        if (e.keyCode === Event.KEY_TAB) { return; }
        break;
      case Event.KEY_UP:
        this.moveUp();
        break;
      case Event.KEY_DOWN:
        this.moveDown();
        break;
      default:
        return;
    }
    Event.stop(e);
  },

  onKeyUp: function(e) {
    switch (e.keyCode) {
      case Event.KEY_UP:
      case Event.KEY_DOWN:
        return;
    }
    clearInterval(this.onChangeInterval);
    if (this.currentValue !== this.el.value) {
      if (this.options.deferRequestBy > 0) {
        // Defer lookup in case when value changes very quickly:
        this.onChangeInterval = setInterval((function() {
          this.onValueChange();
        }).bind(this), this.options.deferRequestBy);
      } else {
        this.onValueChange();
      }
    }
  },

  onValueChange: function() {
    clearInterval(this.onChangeInterval);
    this.currentValue = this.el.value;
    this.selectedIndex = -1;
    if (this.ignoreValueChange) {
      this.ignoreValueChange = false;
      return;
    }
    if (this.currentValue === '' || this.currentValue.length < this.options.minChars) {
      this.hide();
    } else {
      this.getSuggestions();
    }
  },

  getSuggestions: function() {
    var cr = this.cachedResponse[this.currentValue];
    if (cr && Object.isArray(cr.suggestions)) {
      this.suggestions = cr.suggestions;
      this.suggest();
    } else if (!this.isBadQuery(this.currentValue)) {
      new Ajax.Request(this.serviceUrl, {
        parameters: { query: this.currentValue },
        onComplete: this.processResponse.bind(this),
        method: 'get'
      });
    }
  },

  isBadQuery: function(q) {
    var i = this.badQueries.length;
    while (i--) {
      if (q.indexOf(this.badQueries[i]) === 0) { return true; }
    }
    return false;
  },

  hide: function() {
    this.enabled = false;
    this.selectedIndex = -1;
    this.container.hide();
  },

  suggest: function() {
    if (this.suggestions.length === 0) {
      this.hide();
      return;
    }
    var content = [];
    var re = new RegExp(this.currentValue.match(/\w+/g), 'gi');
    var lastDomain ="";
    content.push('<table cellspacing="0" cellpadding="0" >');
    this.suggestions.each(function(value, i) 
    {
        if(lastDomain===value.domain)
        {
             
             content.push(
                    '<tr>',
                    '<td class="spotlight_domain" >&nbsp;</td>',
                    (this.selectedIndex === i ? '<td class="spotlight_match selected"' : '<td class="spotlight_match"'), 
                    ' title="', 
                    value.match, 
                    '" onclick="Autocomplete.instances[', 
                    this.instanceId, '].select(', i, ');" onmouseover="Autocomplete.instances[', this.instanceId, '].activate(', i, ');">', 
                    Autocomplete.highlight(value.match, re), 
                    '</td></tr>');
      }
      else
      {
             content.push(
                    '<tr>',
                    '<td class="spotlight_domain">',
                    value.domain,
                    '</td>',
                    (this.selectedIndex === i ? '<td class="spotlight_match selected"' : '<td class="spotlight_match"'), 
                    ' title="', 
                    value.match, 
                    '" onclick="Autocomplete.instances[', 
                    this.instanceId, '].select(', i, ');" onmouseover="Autocomplete.instances[', this.instanceId, '].activate(', i, ');">', 
                   
                    Autocomplete.highlight(value.match, re), 
                    '</td></tr>');
      }
      lastDomain = value.domain;
    } .bind(this));
    content.push("</table>");
    this.enabled = true;
    this.container.update(content.join('')).show();
  },

  processResponse: function(xhr) {
    var response;
    try {
      response = xhr.responseText.evalJSON();
      if (!Object.isArray(response.data)) { response.data = []; }
    } catch (err) { return; }
    this.cachedResponse[response.query] = response;
    if (response.suggestions.length === 0) { this.badQueries.push(response.query); }
    if (response.query === this.currentValue) {
      this.suggestions = response.suggestions;
      this.suggest(); 
    }
  },

  activate: function(index) {
    var divs = $(this.container).select(".spotlight_match");
    var activeItem;
    // Clear previous selection:
    if (this.selectedIndex !== -1 && divs.length > this.selectedIndex) {
      divs[this.selectedIndex].removeClassName("selected");
    }
    this.selectedIndex = index;
    if (this.selectedIndex !== -1 && divs.length > this.selectedIndex) {
      activeItem =divs[this.selectedIndex];
      activeItem.addClassName('selected');
    }
    return activeItem;
  },

  deactivate: function(div, index) {
    div.className = '';
    if (this.selectedIndex === index) { this.selectedIndex = -1; }
  },

  select: function(i) {
    var selectedValue = this.suggestions[i];
    if (selectedValue) {
      this.el.value = selectedValue.match;
      if (this.options.autoSubmit && this.el.form) {
        this.el.form.submit();
      }
      this.ignoreValueChange = true;
      this.hide();
      this.onSelect(i);
    }
  },

  moveUp: function() {
    if (this.selectedIndex === -1) { return; }
    if (this.selectedIndex === 0) {
      var divs = $(this.container).select(".spotlight_match");
      divs[0].removeClassName("selected");
      this.selectedIndex = -1;
      this.el.value = this.currentValue;
      return;
    }
    this.adjustScroll(this.selectedIndex - 1);
  },

  moveDown: function() {
    if (this.selectedIndex === (this.suggestions.length - 1)) { return; }
    this.adjustScroll(this.selectedIndex + 1);
  },

  adjustScroll: function(i) {
    var container = this.container;
    var activeItem = this.activate(i);
    var offsetTop = activeItem.offsetTop;
    var upperBound = container.scrollTop;
    var lowerBound = upperBound + this.options.maxHeight - 25;
    if (offsetTop < upperBound) {
      container.scrollTop = offsetTop;
    } else if (offsetTop > lowerBound) {
      container.scrollTop = offsetTop - this.options.maxHeight + 25;
    }
    this.el.value = this.suggestions[i].match;
  },

  onSelect: function(i) {
    (this.options.onSelect || Prototype.emptyFunction)(this.suggestions[i]);
  }
};
