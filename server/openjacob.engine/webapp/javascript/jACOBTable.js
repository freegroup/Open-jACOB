var DEFAULT_TABLE_BROWSER_HEIGHT=20;
function updateSelectionLabel(emitter)
{
 var container =emitter.up("#searchBrowserTable");
 if(container)
 {
   var checkboxes = container.select(".sbc_checkbox").size();
   var selected = container.select("img[checked=true]").size();
        
   var label = " ( "+selected +" / "+checkboxes+" )";
        
   var div = container.select(".selection_action_counter");
   if(div.size()>0)
   {
     div = div[0];
     if(selected==0)
        div.update("");
     else
        div.update(label);
    }
  }
}

function check(event, elementId)
{
  event = window.event||event ;
  Event.stop(event);
  var element = Event.element(event);

  var myAjax = new Ajax.Request("dialogs/rowPicker.jsp",
  {
    parameters:
    {
      guid: elementId,
      check: element.getAttribute('checked')!="true",
      row: element.parentNode.parentNode.rowIndex,
      browser: browserId
    },
    onSuccess: function (transport)
    {
      var json = transport.responseJSON;
      json.each(function(element)
      {
        if(element.visible)
          $(element.id).show();
        else
          $(element.id).hide();
    
      });      
      if(element.getAttribute('checked')=="true")
      {
        element.setAttribute('checked','false');
        element.src = "./themes/"+userTheme+"/images/unchecked.png";
      }
      else
      {
        element.setAttribute('checked','true');
        element.src = "./themes/"+userTheme+"/images/checked.png";
      }
      updateSelectionLabel(element);
    }
  });
  return true;
}

function checkAll(event, elementId)
{
  event = window.event||event ;
  Event.stop(event);
  var element = Event.element(event);
  var input=$(elementId).getElementsBySelector('img[selectionbox="true"]');
  var myAjax = new Ajax.Request("dialogs/rowPicker.jsp",
  {
    parameters:
    {
      guid: elementId,
      check: element.getAttribute('checked')!="true",
      browser: browserId
    },
    onSuccess: function (transport)
    {
      var json = transport.responseJSON;
      json.each(function(element)
      {
        if(element.visible)
          $(element.id).show();
        else
          $(element.id).hide();
        });     
        if(element.getAttribute('checked')=="true")
        {
           element.setAttribute('checked','false');
           element.src = "./themes/"+userTheme+"/images/unchecked.png";
           input.each(function(s)
                      {
                        s.setAttribute('checked','false');
                        s.src = "./themes/"+userTheme+"/images/unchecked.png";
                      });
        }
        else if(element.getAttribute('checked')=="false")
        {
           element.setAttribute('checked','true');
           element.src = "./themes/"+userTheme+"/images/checked.png";
           input.each(function(s)
                      {
                        s.setAttribute('checked','true');
                        s.src = "./themes/"+userTheme+"/images/checked.png";
                      });
        }
        updateSelectionLabel(element);
    }
  });
}

function jACOBTable_removeColumn(p1,p2,p3,p4)
{
jACOBTable.currentPicker.removeColumn(p4,function(){FireEventData(p1,p2,p3)});
}

function updateDropTableRow(flag, icon)
{
   dragDiv.canDrop = flag;
   dragDiv.style.background="white url("+icon+") center left no-repeat";
}

var dragDiv = null;
function dragTableRow(e)
{
    e = window.event||e;
    var tr = (e.target) ? e.target : e.srcElement;
    while(tr.tagName.toUpperCase()!="TR")
        tr = tr.parentNode;
    var dragIndex = tr.rowIndex;

    var table = tr.parentNode;
    while(table.tagName.toUpperCase()!="TABLE")
       table = table.parentNode;
    var tableId = table.id;

    document.onmouseup=function(event)
    { 
      document.onmousemove='';
      document.onmouseup='';
    }
    document.onmousemove=function(event)
    {
        event = window.event||event;
        var tr = (event.target) ? event.target : event.srcElement;
        while(tr!=null && tr.tagName && tr.tagName.toUpperCase()!="TR")
           tr = tr.parentNode;
        if(tr==null)
          return;
        var dropIndex = tr.rowIndex;

        if(dragDiv==null)
        {
           // Kein DragDrop DIV anzeigen solange sich die Maus ¸ber der
           // gleichen Row befindet.
           if(dropIndex == dragIndex)
              return;
 
           dragDiv = document.createElement("div");
           dragDiv.innerHTML = (e.target) ? e.target.innerHTML : e.srcElement.innerHTML;
           dragDiv.innerHTML = dragDiv.innerHTML.stripTags();
           dragDiv.style.position="absolute";
           dragDiv.className="drag_drop";
           dragDiv.index = dragIndex;
           dragDiv.lastDropIndex=dragIndex;
           dragDiv.style.background="white url(icons/accept.png) center left no-repeat";
           var pos = getMousePos();
           dragDiv.style.top=(pos.y+3)+"px";
           dragDiv.style.left=(pos.x+3)+"px";
           document.body.appendChild(dragDiv);
           document.onmouseup=function(event)
           { 
              event = window.event||event;
              document.onmousemove='';
              document.onmouseup='';
              if(dragDiv!=null)
              {
                 document.body.removeChild(dragDiv);
                 var dragIndex = dragDiv.index;
                 var tr = (event.target) ? event.target : event.srcElement;
                 while(tr.tagName.toUpperCase()!="TR")
                    tr = tr.parentNode;
                 var dropIndex = tr.rowIndex;
                 if(dragIndex!=undefined && dropIndex!=undefined && dragIndex!=dropIndex && dragDiv.canDrop==true)
                 {
                    FireEventData(tableId,"dragdrop",dragIndex+":"+dropIndex);
                 }
              }
              dragDiv=null;
           }
        }
        if(dragDiv.lastDropIndex!=dropIndex)
        {
        	var url = "dialogs/dragdrop.jsp";
            var myAjax = new Ajax.Request(url,
            {
              parameters: 
              {
                 guid: tableId,
                 dragIndex: dragIndex,
                 dropIndex: dropIndex,
                 callback: "validateDrop",
                 browser: browserId
	     }
	     });
        }
        var pos = getMousePos();
        dragDiv.style.top=(pos.y+3)+"px";
        dragDiv.lastDropIndex=dropIndex;
        dragDiv.style.left=(pos.x+3)+"px";
    }
}

function jACOBTable_scrollRowToTop(/*:String */ tableId, /*:int*/ rowIndex)
{
    var t =$(tableId);
    if(t==null)
       return;
    var element = t.rows[rowIndex];
    var scrollArea = element;
    // Den ersten Bereich finden welcher gescrollt wird....wenn vorhanden
    while(scrollArea!=null && scrollArea.clientHeight==scrollArea.scrollHeight)
    {
      scrollArea=scrollArea.parentNode;
    }
    if(scrollArea==null)
        return;
    var po = Position.positionedOffset(element);
    scrollArea.scrollTop = po[1];
    glow(element);
}

/**
 * Stellt sicher, dass der angegebene Index in dem sichtbaren Bereich des Browsers ist.
 * Wenn die Zeile bereits sichtbar ist, dann wird nichts gemacht.
 *
 **/
function jACOBTable_scrollToRow(/*:String */ tableId, /*:int*/ rowIndex)
{
    var t =$(tableId);
    if(t==null)
       return;
    var element = t.rows[rowIndex];
    var scrollArea = element;
    // Den ersten Bereich finden welcher gescrollt wird....wenn vorhanden
    while(scrollArea!=null && scrollArea.clientHeight==scrollArea.scrollHeight)
    {
      scrollArea=scrollArea.parentNode;
    }
    if(scrollArea==null)
        return;
    var po = Position.positionedOffset(element);
     if(scrollArea.scrollTop<po[1] && (scrollArea.scrollTop+scrollArea.offsetHeight)>po[1])
       return;
    scrollArea.scrollTop = po[1]-50;
}

function jACOBTable(/*:String*/ sTableId, /*:boolean*/ withColumnpicker /*default=true*/)
{
  var oThis = this;

  this.tableContainer=null;
  this.captionContainer = null;
  this.originalTable=null;
  this.columnHeaders = new Array();;
  this.headerHeight=0;

  this.fr=null;
  this.fri=null;

  this.originalTable = getObj(sTableId); // original table
  this.jacobId = sTableId;
  this.multiselect = this.originalTable.getAttribute("multiselect")=="true"?true:false;
  this.dragdropsupport = this.originalTable.getAttribute("dragdropsupport")=="true"?true:false;

  this.headerHeight = DEFAULT_TABLE_BROWSER_HEIGHT;// new LayerObject(this.originalTable.tHead.rows[0]).height;

  this.root = document.createElement('div'); // overall container
  this.rootLayer = new LayerObject(this.root);
  this.hasPosition = this.originalTable.parentNode.style.left?true:false;

  // Falls die Tabelle in einem positionierten Container drin ist, wird die Groesse
  // und Position vom Container uebernommen
  //
  if(this.hasPosition)
  {
    this.root.style.position="absolute";
    this.root.style.top="0px";
    this.root.style.left="0px";
    this.root.style.width=this.originalTable.parentNode.style.width;
    this.root.style.height=this.originalTable.parentNode.style.height;
  }
  else
  {
    this.root.style.position="relative";
    this.root.style.overflow="hidden";
  }

  this.fr = document.createElement('div'); // frozen-row container
  this.fr.style.position="absolute";
  this.fr.className="sbh_container";
  this.fr.style.margin="0px";
  this.fr.style.padding="0px";

  this.fri = document.createElement('div'); // frozen-row inner container, for column headings
  this.fri.style.position="absolute";
  this.fri.style.overflow="hidden";
  this.fr.appendChild(this.fri);
  this.root.appendChild(this.fr);

  // add column Picker element to the sbh_container
  //
  if(withColumnpicker ===undefined || withColumnpicker===true)
  {
	  this.columnPicker = document.createElement('img'); // column picker icon
	  this.columnPicker.style.position="absolute";
	  this.columnPicker.style.right="0px";
	  this.columnPicker.style.cursor="pointer";
	  this.columnPicker.src="./themes/"+userTheme+"/images/columns.png";
	  this.fr.appendChild(this.columnPicker);
      this.tmp = function(){oThis.onPicker();}
      if(this.columnPicker.addEventListener)
         this.columnPicker.addEventListener("click",this.tmp,false);
      else if(this.columnPicker.attachEvent)
         this.columnPicker.attachEvent('onclick',this.tmp);
  }

  this.tableContainer = document.createElement('div'); // table container, contains original table
  this.tableContainerLayer = new LayerObject(this.tableContainer);
  this.tableContainer.style.position="absolute";
  this.tableContainer.style.overflow="auto";
  this.tableContainer.id="c_"+this.jacobId;
  this.tableContainer.className="sb_container";
  this.root.appendChild(this.tableContainer);

  if (this.originalTable.caption) 
  {
    this.captionContainer = document.createElement('div'); // caption container
    this.captionContainer.style.position="absolute";
    this.captionContainer.style.overflow="hidden";
    this.captionContainer.className = "sbh_counter";
    this.captionContainer.innerHTML=this.originalTable.caption.innerHTML;
    this.root.appendChild(this.captionContainer);
    this.originalTable.deleteCaption();
  }

  // Create frozen row cells (column headings)
  var a = this.originalTable.rows[0].cells;
  var b = this.originalTable.rows[1].cells;
  var alen = a.length;
  var e;
  for (var i = 0; i < alen; ++i) 
  {
    e = document.createElement('div');
    e.className  =a[i].className;//"sbh";
    e.style.position="absolute";
    e.style.overflow="hidden";
    if(i==0 && this.multiselect )
    {
       // do nothing. Width is defined in the CSS file
    }
    else
    {
      var runtimeWidth =new LayerObject(a[0]).getRuntimeStyle('width');
      if(runtimeWidth!="auto")
        e.style.width=runtimeWidth+"px";
      else
        e.style.width=new LayerObject(a[i]).width+"px";
    }
    e.innerHTML = a[i].innerHTML;
    this.fri.appendChild(e);
    this.columnHeaders[i]=e;
  }
  
  alen = a.length;
  for (var i = 0; i < alen; ++i) 
  {
    if(i==0 && this.multiselect==true )
    {
       // do nothing. Width is defined in the CSS file
    }
    else
    {
       b[i].style.width=new LayerObject(a[i]).width+"px";
       var node = b[i].childNodes[0];
       // Falls eine Input Element in der Zelle ist wird dieses
       // in der Breite an die Zelle angepasst.
       // NICHT jedoch Checkboxen, diese sehen gestreckt nicht gut aus.
       //
       if(node && node.tagName && node.tagName.toUpperCase()!="IMG")
       {
          node.style.width=b[i].style.width;
       }
    }
  }


  if (this.originalTable.tHead)
    this.originalTable.deleteTHead();

  this.originalTable = this.originalTable.parentNode.replaceChild(this.root, this.originalTable);


  this.tableContainer.appendChild(this.originalTable);
  this.originalTableLayer = new LayerObject(this.originalTable);

  // adding drag&drop to the rows if the table supports them
  //
  if(this.dragdropsupport==true)
  {
     this.originalTable.onselectstart=function(){return false;};
     Event.observe(this.originalTable, 'mousedown', function(event) 
     {
        Event.stop(event);
     	if(Event.isLeftClick(event))
    		dragTableRow(event);
     });
  }


  // resize the table to the dimension if it an embeded (like an InformBrowser) table
  //
  if(this.hasPosition)
    this.resize();

  // add scroll event listener to the table container
  // Achtung: Dies ist die mir einzige bekannte Methode um in einem Eventhandler
  //          an das aeussere Objekt zu kommen. oThis MUSS mit "var" declariert werden sonst ist
  //          diese global sichtbar und nur EINMAL vorhanden ( NICHT einmal pro Instance).
  //
  this.onScroll = function()
  {
    new LayerObject(oThis.fri).css.left= -oThis.tableContainer.scrollLeft;
    getObj("j_"+oThis.jacobId).value=oThis.tableContainer.scrollTop+":"+oThis.tableContainer.scrollLeft;
  }
  if(this.tableContainer.addEventListener)
    this.tableContainer.addEventListener("scroll",this.onScroll,false);
  else if(this.tableContainer.attachEvent)
    this.tableContainer.attachEvent('onscroll',this.onScroll);

  // ErrorBar umh‰ngen
  if(sTableId == searchBrowserId)
  {
    this.errorBar = $("error_bar");
    if(this.errorBar)
    {
       var s = this.errorBar.style;
       s.position="absolute";
       s.right="0px";
       s.top="33px";
       s.width="10px";
       this.tableContainer.parentNode.appendChild(this.errorBar);
    }
  }
}


jACOBTable.currentPicker=null;
jACOBTable.prototype.onPicker = function()
{
  var oThis = this;
  var pos = getMousePos();
  var myAjax = new Ajax.Request("dialogs/columnPicker.jsp",
  {
    parameters:
    {
      guid: this.jacobId,
      browser: browserId
    },
    onSuccess: function (transport)
    {
      // das div an der position von dem ColumnPicker anzeigen
      //
      var menu = document.createElement('div'); // caption container
      menu.style.position = "absolute";
      menu.style.left     = "-400px";
      menu.style.top      = pos.y+"px";
      menu.style.opacity  ="0.001";
      menu.style.filter   = "alpha(opacity=1)";
      menu.id             = "columnPicker";
      menu.innerHTML=transport.responseText;
      document.body.appendChild(menu);

      var menuLayer = new LayerObject(menu);
      if(menuLayer.getHeight()>300)
      {
         menu.style.left   = (pos.x-menuLayer.getWidth()-10)+"px";
         menuLayer.css.height="300px";
         menuLayer.css.overflow="auto";
      }
      else
      {
         menu.style.left   = (pos.x-menuLayer.getWidth())+"px";
      }
       
       new fx.Opacity( menu,{duration:500} ).custom(0.001,1.0);
      jACOBTable.currentPicker=oThis;
    }
  });
}

/**
 *
 * @private
 **/
jACOBTable.prototype.removeColumn = function(/*:int*/ index, /*:function*/ onCompleteFunction)
{
    if(this.multiselect)
        index++;

    var rows = this.originalTable.rows;
    // inhalte der Zelle loeschen damit diese zusammen gezogen werden
    // koennen. und den Font auf einer sehr kleinen Groesse setzten
    //
    for (i = 0; i < rows.length; ++i) 
    {
       var element = rows[i].cells[index];
       element.innerHTML="&nbsp;";
       element.style.fontSize="1px";
       element.style.margin="0px";
       element.style.padding="0px";
    }

    var headerToRemove =this.columnHeaders[index];

    // Die rechten Header nach links r¸ber rutschen -falls vorhanden!
    //
    if(index<(this.columnHeaders.length-1))
    {
      var diff = parseInt(this.columnHeaders[index+1].style.left) - parseInt(headerToRemove.style.left);
      for (i = index+1; i < this.columnHeaders.length; ++i)
      { 
         var element = this.columnHeaders[i];
         var left= parseInt(element.style.left);
         new fx.Left(element,{duration:500, transition:fx.linear}).custom(left,left-diff);
      }
    }
    headerToRemove.parentNode.removeChild(headerToRemove);

    // Tabellen column die Weite langsam reduzieren.
    //
    var columnWidth=parseInt(rows[0].cells[index].style.width);
    new fx.Width(rows[0].cells[index],{duration:500,transition:fx.linear, onComplete: function()
                            {
                                  // da die zelle nicht ganz zusammengezogen werden kann
                                  // werden die Zellen jetzt ganz gelˆscht.
                                  for (i = 0; i < rows.length; ++i) 
                                  {
                                      rows[i].cells[index].parentNode.removeChild(rows[i].cells[index]);
                                  }
                                  if(onCompleteFunction)
                                    onCompleteFunction();
                             }
                        } ).custom(columnWidth,1);
}

/**
 *
 * @private
 **/
jACOBTable.prototype.resize = function()
{
    var widthReduce = this.errorBar?parseInt(this.errorBar.style.width):0;
    var cch = 0;
    var w;
    var h;

    // caption container
    if (this.captionContainer)
    {
      var ccLayer = new LayerObject(this.captionContainer);
      cch = ccLayer.height;
      ccLayer.moveTo( 0, 0);
      ccLayer.setWidth(this.rootLayer.getWidth());
    }

    // frozen row
    var frLayer = new LayerObject(this.fr);
    frLayer.moveTo(0, cch);
    frLayer.setWidth(this.rootLayer.getWidth());
    frLayer.setHeight(this.headerHeight);

    var friLayer = new LayerObject(this.fri);
    friLayer.moveTo( 0, 0);
    friLayer.setWidth(this.originalTableLayer.getWidth());
    friLayer.setHeight(this.headerHeight);

    // table container
    this.tableContainerLayer.moveTo(0, cch + this.headerHeight);
    // different box models between IE and FireFox
    if(BROWSER_IE==true)
    {
       this.tableContainerLayer.setHeight(this.rootLayer.getHeight() - cch - this.headerHeight-2);
       this.tableContainerLayer.setWidth(this.rootLayer.getWidth() - 2-widthReduce);
    }
    else
    {
       this.tableContainerLayer.setHeight(this.rootLayer.getHeight() - cch - this.headerHeight);
       this.tableContainerLayer.setWidth(this.rootLayer.getWidth()-widthReduce);
    }

    // size and position frozen row cells
    //
    var a = this.originalTable.rows[0].cells;
    var e = jACOBTable.firstChild(this.fri, 'div');
    this.sum = 0;
    for (i = 0; i < a.length; ++i) 
    {
      aLayer = new LayerObject(a[i]);
      eLayer = new LayerObject(e);
      eLayer.moveTo(this.sum, 0);

      this.sum += Math.max(eLayer.width,aLayer.width);
      e = jACOBTable.nextSibling(e, 'div');
    }
    friLayer.css.left=-this.tableContainer.scrollLeft;
    var values =  getObj("j_"+this.jacobId).value.split(":");
    this.tableContainer.scrollTop  = values[0];
    this.tableContainer.scrollLeft = values[1];

    if(this.errorBar)
     this.errorBar.style.height=this.tableContainer.style.height;
}

/**
 * Return the first HTMLElement with the handsover "type".
 *
 * @private
 **/
jACOBTable.firstChild = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var child = e ? e.firstChild : null;
  while (child) {
    if (child.nodeType == 1 && (!type || child.nodeName.toLowerCase() == type.toLowerCase())){break;}
    child = child.nextSibling;
  }
  return child;
}

jACOBTable.lastChild = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var child = e ? e.lastChild : null;
  while (child) 
  {
    if (child.nodeType == 1 && (!type || child.nodeName.toLowerCase() == type.toLowerCase()))
    {
       break;
    }
    child = child.previousSibling;
  }
  return child;
}

/**
 * Return the next HTMLElement with the handsover "type".
 * @private
 **/
jACOBTable.nextSibling = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var sibling = e ? e.nextSibling : null;
  while (sibling) {
    if (sibling.nodeType == 1 && (!type || sibling.nodeName.toLowerCase() == type.toLowerCase())){break;}
    sibling = sibling.nextSibling;
  }
  return sibling;
}
