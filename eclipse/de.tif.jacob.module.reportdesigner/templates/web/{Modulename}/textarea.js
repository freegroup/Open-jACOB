
var ResizingTextArea = Class.create();


ResizingTextArea.prototype = 
{
    defaultRows: 1,

    initialize: function(field)
    {
        this.field = field;
        this.defaultRows = Math.max(field.rows, 1);
        this.resizeNeeded = this.resizeNeeded.bindAsEventListener(this);
        this.keyDown = this.keyDown.bindAsEventListener(this);
        this.insertSuggestion = this.insertSuggestion.bindAsEventListener(this);
        Event.observe(field, "click", this.resizeNeeded);
        Event.observe(field, "keyup", this.resizeNeeded);
        Event.observe(field, "keydown", this.keyDown);
        var lines = field.value.split('\n');
        var newRows = lines.length;
        field.rows = Math.max(this.defaultRows, newRows);

        this.pre = document.createElement("pre");
        this.pre.className="preformated";
        this.pre.id = this.field.id +"_preformated";
        this.field.parentNode.insertBefore(this.pre,this.field);
    },

    resizeNeeded: function(event)
    {
        var lines = this.field.value.split('\n');
        var newRows = lines.length;
        this.field.rows = Math.max(this.defaultRows, newRows);
layout();
    },

    keyDown: function (event)
    {
        if(reporting_menu!==null)
           reporting_menu.remove();
        reporting_menu = null;
        if(event.ctrlKey && event.keyCode===32)
        {
          var top=0;
          var left=0;
          this.lastCaretPos = this.getCaretPosition();
          if(Prototype.Browser.IE)
          {
              this.selectionRange = document.selection.createRange();
              top = this.selectionRange.offsetTop;
              left = this.selectionRange.offsetLeft;
          }
          else
          {
              var preformated = $(this.field.id+"_preformated");
              var taSelectionStart = this.field.selectionStart;       
              preformated.innerHTML = this.field.value.substr(0,taSelectionStart)+'<span id="'+this.field.id+'_cursorPos">X</span>';
              var cp = $(this.field.id+'_cursorPos');
              var leftTop = this.findPos(cp);

              top = leftTop[1];
              left = leftTop[0];
          }
          reporting_menu = $(document.createElement("div"));
          reporting_menu.className = "suggestions";
          reporting_menu.style.position="absolute";
          reporting_menu.zIndex = "1000";

          var ul = document.createElement("ul");
          ul.className= "suggestions";
          reporting_menu.appendChild(ul);
          for(var i=0;i<suggestions.length;i++)
          {
             var li = document.createElement("li");
             ul.appendChild(li);

             var a = document.createElement("a");
             li.appendChild(a);

             a.className="suggestion";
             a.innerHTML=suggestions[i];
             Event.observe(a,"click",this.insertSuggestion);
          }
          reporting_menu.style.top = top;
          reporting_menu.style.left = left;
          document.body.appendChild(reporting_menu);
        }
      },

      findPos: function(obj) 
      {
        var curleft = curtop = 0;
        if (obj.offsetParent) {
            do {
                    curleft += obj.offsetLeft;
                    curtop += obj.offsetTop;
            } while (obj = obj.offsetParent);
        }
        return [curleft,curtop];
      },


    /* move the cursor to the end of the text element
    */
    setCaretToPos: function ( pos) 
    {
      if(Prototype.Browser.IE)
      {
        var range = this.field.createTextRange();
        range.collapse(true);
        range.moveEnd('character', pos);
        range.moveStart('character', pos);
        range.select();
      }
      else
      {
        this.field.setSelectionRange(pos, pos);
      }
    },

    /* 
    * PUBLIC FUNCTION
    *
    * Liefert die Cursor position des ?bergebenen elementes
    */
    getCaretPosition: function()
    {
      if(Prototype.Browser.IE)
      {
         // The current selection
         var range = document.selection.createRange();
         var rangeCopy = range.duplicate();
         // Select all text
         rangeCopy.moveToElementText(this.field);
         // Now move 'dummy' end point to end point of original range
         rangeCopy.setEndPoint( 'EndToEnd', range );
         // Now we can calculate start and end points
         start = rangeCopy.text.length - range.text.length;
         end = start + range.text.length;
        return start;
      }
      else
      {
        return this.field.selectionEnd;
      }
    },

    insertSuggestion: function(event)
    {
        var element = event.element();
        var t= element.innerHTML.trim();
        if(Prototype.Browser.IE)
        {
          this.selectionRange.text=t;
        }
        else
        {
          var taSelectionStart = this.field.selectionStart;       
          var before = this.field.value.substr(0,taSelectionStart);
          var after = this.field.value.substr(taSelectionStart);
          this.field.value=before+t+after;
        }
        reporting_menu.remove();
        reporting_menu=null;
        this.field.focus();
        this.setCaretToPos( this.lastCaretPos+t.length);
        event.stop();
    }
}

