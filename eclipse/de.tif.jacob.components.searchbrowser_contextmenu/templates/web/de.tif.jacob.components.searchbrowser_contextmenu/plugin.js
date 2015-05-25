function searchBrowserContextMenu(e, id)
{
  var target = e.element();
  while(target.tagName.toUpperCase() != "TD" && target!==null)
      target= target.parentNode;
  if(target.tagName.toUpperCase()==="TD")
  {
    var parentTr= target.parentNode;
    var rowIndex= parentTr.rowIndex;
    
    var table = target.parentNode;
    while(table.tagName.toUpperCase() != "TABLE")
          table= table.parentNode;
 
    // JSP rufen um die Menueintraege zu erfragen
    var shortVersionPart = "@VERSION@".split(".");
    var shortVersion = shortVersionPart[0]+"."+shortVersionPart[1];
    var myAjax = new Ajax.Request("application/@APPLICATION@/"+shortVersion+"/de.tif.jacob.components.searchbrowser_contextmenu/plugin.jsp",
    {
      parameters:
      {
        componentId: table.id,
        hookId: id,
        row: rowIndex,
        column: target.cellIndex,
        browser: browserId
      },
      onSuccess: function (transport)
      {
          try
          {
          var entries = transport.responseText.evalJSON();
          var x= $('searchBrowserContextMenu');
          if(x == null || x == undefined)
             x=new Element('div', { id:'searchBrowserContextMenu', 'class': 'contextMenu' });
          else
             x.innerHTML="";

          var t = new Element('table', {cellspacing:0, cellpadding:0, 'class': 'contextMenu' });
          var tbody = new Element('tbody');
          x.appendChild(t);
          t.appendChild(tbody);
          for(var i=0; i<entries.length ; i++)
          {
            var entry = entries[i];
            var row = new Element('tr');
            tbody.appendChild(row);
            var cell1= new Element('td', { 'class': 'contextMenuItem' });
            row.appendChild(cell1);
            var a = new Element('a', { 'class': 'contextMenuItem' }).update(entry.label);
            cell1.appendChild(a);
            var id = transport.request.parameters.hookId;
            Event.observe(a,"click",function(row,column,command)
            {
                FireEventData(id,"click",row+":"+column+":"+command);
            }.bind(this,entry.row,entry.column,entry.command));
          }
          $("body").appendChild(x);
          showContextMenu("searchBrowserContextMenu");
          }
          catch(e)
          {
            alert(e);
          }
      }
    });

  }
  Event.stop(e);
}


