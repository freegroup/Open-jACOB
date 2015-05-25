// xTable r1, Copyright 2007 Michael Foster (Cross-Browser.com)
// Part of X, a Cross-Browser Javascript Library, Distributed under the terms of the GNU LGPL

function jACOBTimeline(sTableId, jacobId)
{
  this.ot = this.firstChild(document.getElementById(sTableId),"table"); // outer div
  if (!this.ot || !document.createElement || !document.appendChild || !this.ot.deleteCaption || !this.ot.deleteTHead) 
  {
    return null;
  }

  this.jacobId = jacobId;
  this.expanded = false;
  this.resizeAttached=false;
  var values =  getObj(this.jacobId).value.split(":");
  var scrollTop= parseInt(values[0]);
  var scrollLeft= parseInt(values[1]);



//this.ot.border="1";
//getObj(sTableId).style.overflow="visible"; // overall container
//return;
  // determine the width and height of the rows and columns
  this.rowHeights = new Array();
  this.rowPos = new Array();
  var sum =0;
  var count =this.ot.rows.length;
  for(var i =0; i< count;i++)
  {
    this.rowHeights[i]=new LayerObject(this.ot.rows[i]).height;
    this.rowPos[i]=sum;
    sum = sum+this.rowHeights[i];
  }

  this.columnWidths = new Array();
  this.columnPos = new Array();
  count =this.ot.rows[1].cells.length;
  sum=0;
  for(var i=0; i< count;i++)
  {
    var cell = new LayerObject(this.ot.rows[1].cells[i]);
    this.columnWidths[i]=parseInt(cell.width);
    this.columnPos[i]=sum;
    sum = sum+this.columnWidths[i];
  }

  this.root = getObj(sTableId); // overall container
  this.rootLayer = new LayerObject(this.root);

  var w = this.rootLayer.width;
  var h = this.rootLayer.height;
  var x = this.rootLayer.x;
  var y = this.rootLayer.y;

  this.tc = document.createElement('div'); // table container, contains ot
  this.tc.style.position="absolute";
  this.tc.style.overflow="auto";
  this.tc.style.top=(this.rowPos[2])+"px";
  this.tc.style.left=this.columnWidths[0]+"px";
  this.tc.style.width=(w-this.columnWidths[0]-2)+"px";
  this.tc.style.height=(h-this.rowPos[2]-2)+"px";
  this.root.appendChild(this.tc);


  // Create Frozen Row
  //
  this.fr = document.createElement('div'); // frozen-row container
  this.fr.style.position="absolute";
  this.fr.style.overflow="hidden";
  this.fr.style.top="0px";
  this.fr.style.left="0px";
  this.fr.style.width=w+"px";
  this.fr.className = "timeline_frozen_row";
  //                   Monat   +Tageszeile
  this.fr.style.height=(this.rowHeights[0]+this.rowHeights[1])+"px";
  this.root.appendChild(this.fr);


  this.fri = document.createElement('div'); // frozen-row inner container, for column headings
  this.fri.style.position="absolute";
  this.fri.style.top="0px";
  this.fri.style.left= (-scrollLeft)+"px";
  this.fr.appendChild(this.fri);
  this.root.appendChild(this.fr);

  // Create frozen row cells (column headings)
  var months = this.ot.rows[0].cells; // monate Überschrift
  var alen = months.length;
  // Monatsüberschrift
  var colIndex=0;
  for (var i = 1; i < alen; ++i) 
  {
    var colspan = parseInt(months[i].getAttribute("colspan"));
    e = document.createElement('div');
    e.className = "timeline_month_container";
    e.style.position="absolute";
    e.style.left = this.columnPos[colIndex+1]+"px";
    e.style.top = "0px";
    e.style.height = this.rowHeights[0]+"px";
    e.style.overflow = "hidden";
    e.style.textAlign="center";
    // 20 mal das Datum innerhalb der Headerï¿½berschirft schreiben
    var title = "<span class='timeline_month_title'>"+months[i].innerHTML+"</span>";
    for(var r=0;r<2;r++)
       title=title+title;
    e.innerHTML = title;
    this.fri.appendChild(e);
    var s=  this.columnPos[colIndex+1];
    colIndex= colIndex+colspan;
    e.style.width = (this.columnWidths[colIndex]+this.columnPos[colIndex]-s)+"px";
  }
  // Tagesüberschrift
  var a = this.ot.rows[1].cells; // tages Überschriften
  var b = this.ot.rows[2].cells; // daten
  alen = a.length;
  for (var i = 0; i < alen; ++i) 
  {
    e = document.createElement('div');
    e.className = "timeline_day_title";
    e.style.position="absolute";
    e.style.left = this.columnPos[i]+"px";
    e.style.top = this.rowHeights[0]+"px";
    e.style.width = this.columnWidths[i]+"px";
    e.style.height = this.rowHeights[1]+"px";
    e.style.fontSize="8px";
    e.style.textAlign="center";
    e.className = a[i].className;
    e.innerHTML = a[i].innerHTML;
    this.fri.appendChild(e);

    e = document.createElement('div');
    e.style.width = this.columnWidths[i]+"px";
    e.innerHTML = b[i].innerHTML;
    b[i].innerHTML="";
    b[i].appendChild(e);
  }
  this.ot.deleteTHead();

  // Create Frozen Column
  //
  this.fc = document.createElement('div'); // frozen-column container
  this.fc.style.position="absolute";
  this.fc.style.overflow="hidden";
  this.fc.style.top=(this.rowPos[2])+"px";
  this.fc.style.left="0px";
  this.fc.className = "timeline_frozen_column";
  this.fc.style.width=this.columnWidths[0]+"px";
  this.fc.style.height=h+"px";
  this.root.appendChild(this.fc);

  this.fci = document.createElement('div'); // frozen-column inner container, for column headings
  this.fci.style.position="absolute";
  this.fci.style.left="0px";
  this.fci.style.top = (-scrollTop)+"px";
  this.fc.appendChild(this.fci);

  var oThis = this;

  var alen = this.ot.rows.length;
  for (var i = 0; i < alen; ++i) 
  {
    var row = this.ot.rows[i];
    var cell = row.cells[0];
    row.style.height=(this.rowHeights[i+2])+"px";
    e = document.createElement('div');
    e.style.position="absolute";
    e.style.left = "0px";
    e.index=i;
    e.style.top = (this.rowPos[i+2]-this.rowPos[2])+"px"; // die hï¿½he der 2 Tabellen Header Zeilen ï¿½berspringen und abziehen
    e.style.width = this.columnWidths[0]+"px";
    var tmpClick = function()
    {
      if(window.event)
         index = parseInt(window.event.srcElement.index);
      else
         index = parseInt(this.index);

       // den start von dem gantt slot finden
       //
       var row = oThis.ot.rows[index];
       var c=0;
       while(row.cells[c].className!="timeline_cell_spanning")
         c++;
       var scroll = new fx.Scroll(oThis.tc,{});
       scroll.scrollTo(oThis.columnPos[c]-oThis.columnWidths[0],oThis.tc.scrollTop);
    };
    this.addEventListener(e,"click",tmpClick,false);
    e.className="timeline_row_title";
    e.style.backgroundColor = cell.style.backgroundColor;
    e.innerHTML = cell.innerHTML;
    e.title = cell.title;
    this.fci.appendChild(e);
    this.ot.rows[i].removeChild(cell);
  }

  // Linke obere Ecke abdecken
  this.topLeftCorner = document.createElement('div'); // frozen-column container
  this.topLeftCorner.style.position="absolute";
  this.topLeftCorner.style.top="0px";
  this.topLeftCorner.style.left="0px";
  this.topLeftCorner.style.width=this.columnWidths[0]+"px";
  this.topLeftCorner.style.height=(this.rowPos[2])+"px";
  this.topLeftCorner.className="timeline_top_left";
  this.root.appendChild(this.topLeftCorner);

  this.toggleIcon = document.createElement('img');
  this.toggleIcon.src = "./themes/expand_ui.png";
  this.toggleIcon.style.cursor= "pointer";
  this.topLeftCorner.appendChild(this.toggleIcon);
  var tmpToggle=function(){oThis.onToggle();};
  this.addEventListener(this.toggleIcon, 'click', tmpToggle, false);

  this.tc.appendChild(this.ot);

  this.tc.scrollLeft = scrollLeft;
  this.tc.scrollTop = scrollTop;

  var tmpScroll=function(){oThis.onScroll();};
  this.addEventListener(this.tc, 'scroll', tmpScroll, false);
}

jACOBTimeline.prototype.onToggle=function()
{
  if(this.expanded==false)
  {
      // die Tabelle wird ausgehängt und direct an das document gehängt
      // Der Ursprungsvater wird sich gemerkt
      this.originalWidth  = parseInt(this.root.style.width);
      this.originalHeight = parseInt(this.root.style.height);
      this.originalLeft   = parseInt(this.root.style.left);
      this.originalTop    = parseInt(this.root.style.top);
      this.originalParent = this.root.parentNode;
      this.originalParent.removeChild(this.root);
      this.rootLayer.moveTo(0,0);
      this.root.style.width="100%";
      this.root.style.height="100%";
      document.body.appendChild(this.root);
      if(this.resizeAttached==false)
      {
        var oThis = this;
        var resizeFunc = function(){oThis.onResize();};
        this.addEventListener(window,"resize",resizeFunc,false);
      }
      this.expanded=true;
      this.toggleIcon.src = "./themes/collapse_ui.png";
      this.onResize();
  }
  else
  {
      // die Tabelle wird ausgehängt und direct an das document gehängt
      // Der Ursprungsvater wird sich gemerkt
      document.body.removeChild(this.root);
      this.root.style.width=this.originalWidth+"px";
      this.root.style.height=this.originalHeight+"px";
      this.root.style.left=this.originalLeft+"px";
      this.root.style.top=this.originalTop+"px";
      this.fc.style.height=this.originalHeight+"px";
      this.fr.style.width=this.originalWidth+"px";
      this.tc.style.width=(this.originalWidth-this.columnWidths[0]-2)+"px";
      this.tc.style.height=(this.originalHeight-this.rowPos[2]-2)+"px";
      this.originalParent.appendChild(this.root);
      this.toggleIcon.src = "./themes/expand_ui.png";
      this.expanded=false;
  }
}

jACOBTimeline.prototype.onResize=function()
{
   if(this.expanded==false)
      return;
   var w =document.body.clientWidth
   var h =document.body.clientHeight;
   this.fc.style.height=h+"px";
   this.fr.style.width=w+"px";
   this.tc.style.width=(w-this.columnWidths[0]-2)+"px";
   this.tc.style.height=(h-this.rowPos[2]-2)+"px";
}

jACOBTimeline.prototype.onScroll=function()
{
  this.fri.style.left= (-this.tc.scrollLeft)+"px";
  this.fci.style.top = (-this.tc.scrollTop)+"px";
  getObj(this.jacobId).value=this.tc.scrollTop+":"+this.tc.scrollLeft;
}


/**
 * Return the first HTMLElement with the handsover "type".
 *
 * @private
 **/
jACOBTimeline.prototype.firstChild = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var child = e ? e.firstChild : null;
  while (child) 
  {
    if(child.nodeType == 1 && (!type || child.nodeName.toLowerCase() == type.toLowerCase()))
    {
       break;
    }
    child = child.nextSibling;
  }
  return child;
}

jACOBTimeline.prototype.lastChild = function(/*HTMLElement*/ e, /*:String*/ type)
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
jACOBTimeline.prototype.nextSibling = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var sibling = e ? e.nextSibling : null;
  while (sibling) {
    if (sibling.nodeType == 1 && (!type || sibling.nodeName.toLowerCase() == type.toLowerCase())){break;}
    sibling = sibling.nextSibling;
  }
  return sibling;
}



jACOBTimeline.prototype.addEventListener=function(e,eT,eL,cap)
{
  eT=eT.toLowerCase();
  if(e.addEventListener)e.addEventListener(eT,eL,cap||false);
  else if(e.attachEvent)e.attachEvent('on'+eT,eL);
  else {
    var o=e['on'+eT];
    e['on'+eT]=typeof o=='function' ? function(v){o(v);eL(v);} : eL;
  }
}
