(function() 
{
  var shortVersionPart = "@VERSION@".split(".");
  var shortVersion = shortVersionPart[0]+"."+shortVersionPart[1];
  function setImage(imgElement, check)
  {
      if(check)
      {
        imgElement.setAttribute('checked','true');
        imgElement.src = "./themes/"+userTheme+"/images/checked.png";
      }
      else
      {
        imgElement.setAttribute('checked','false');
        imgElement.src = "./themes/"+userTheme+"/images/unchecked.png";
      }
  }
  
  function scroll(event)
  {
    Event.stop(event);
    
    var div = Event.findElement(event,"div.grouped_listbox");
    var myAjax = new Ajax.Request("application/@APPLICATION@/"+shortVersion+"/de.tif.jacob.components.groupedlist/plugin.jsp",
    {
       parameters:
       {
          guid: div.id,
          event:"scroll",
          value:div.scrollLeft+":"+div.scrollTop,
          browser: browserId
       },
       onSuccess: function (transport)
       {
       }
    });
  }

  function selectAll(event, flag)
  {
    var id = this.id;
    var myAjax = new Ajax.Request("application/@APPLICATION@/"+shortVersion+"/de.tif.jacob.components.groupedlist/plugin.jsp",
    {
        parameters:
        {
          guid: id,
          event: "selectAll",
          value: flag,
          browser: browserId
        },
        onSuccess: function (transport)
        {
           var container = Event.findElement(event,".plugin_container");
           container.select(".grouped_listbox_checkbox").each(function(e){setImage(e,flag);});
        }
    });
  }

  function check(event)
  {
   Event.stop(event);
   
   var li = Event.findElement(event,"li");
    var container = Event.findElement(event,".grouped_listbox");
    var root_ul = Event.findElement(event,".grouped_listbox_root");
    var sub_ul = Event.findElement(event,".grouped_listbox_group");
    var element = Event.findElement(event,"img");

    var id = container.id;
    var root_index =-1;
    var sub_index=-1;

    if(sub_ul)
    {
       root_index = root_ul.childElements().indexOf(sub_ul.parentNode);
       sub_index = sub_ul.childElements().indexOf(li);
       sub_li = li.parentNode.parentNode;
    }
    else
    {
       root_index = root_ul.childElements().indexOf(li);
    }

    var myAjax = new Ajax.Request("application/@APPLICATION@/"+shortVersion+"/de.tif.jacob.components.groupedlist/plugin.jsp",
   {
      parameters:
      {
         guid: id,
         event: element.getAttribute('checked')!="true"?"check":"uncheck",
         value: root_index+":"+sub_index,
          root_index:root_index,
          sub_index:sub_index,
         browser: browserId
      },
      onSuccess: function (transport)
      {
          var check=element.getAttribute('checked')!=="true";
          setImage(element, check);
          // Falls kein sub_index angegeben wurde, so ist eine ganze Gruppe
          // selektiert worden
          if(transport.request.parameters.sub_index==-1)
          {
            li.select(".grouped_listbox_checkbox").each(function(e){setImage(e,check);});
          }
          // Falls ein unterelement deselktiert wurde, dann muï¿½ auch die ï¿½bergeordnete Gruppe
          // deselektiert werden
          if(check==false && transport.request.parameters.sub_index!=-1)
          {
            setImage(sub_li.select(".heading img.grouped_listbox_checkbox")[0],false);
          }
      }
   });
  }
  
  function onClick(event)
  {
    Event.stop(event);
   
    var li = Event.findElement(event,"li");
    var container = Event.findElement(event,".grouped_listbox");
    var root_ul = Event.findElement(event,".grouped_listbox_root");
    var sub_ul = Event.findElement(event,".grouped_listbox_group");
    var element = Event.findElement(event,"img");

    var id = container.id;
    var root_index =-1;
    var sub_index=-1;

    root_index = root_ul.childElements().indexOf(sub_ul.parentNode);
    sub_index = sub_ul.childElements().indexOf(li);
    sub_li = li.parentNode.parentNode;
    FireEventData(id, "click",root_index+":"+sub_index);
  }
  
  function collapse(event)
  {
   Event.stop(event);
   
   var li = Event.findElement(event,"li");
    var inner = Event.findElement(event,".grouped_listbox");
    var root_ul = Event.findElement(event,".grouped_listbox_root");
    var element = Event.findElement(event,"img");
    var outer = inner.parentNode;
    var toggleIcon = outer.select(".toggleIcon")[0];

    var id = inner.id;
    var root_index =root_index = root_ul.childElements().indexOf(li);

   // Kinder in dem Baum einhängen und das Icon auf [-] setzen
   //
   var image=element.src.replace("plus.png","load.gif");
   element.src = image;

    var myAjax = new Ajax.Request("application/@APPLICATION@/"+shortVersion+"/de.tif.jacob.components.groupedlist/collapse.jsp",
   {
      parameters:
      {
         guid: id,
         event: element.getAttribute('expanded')==="true"?"collapse":"expand",
         index:root_index,
         browser: browserId
      },
      onSuccess: function (transport)
      {
          var expanded=element.getAttribute('expanded')!=="true";
          element.setAttribute("expanded",expanded?"true":"false");
          var entries = li.select("ul.grouped_listbox_group");
          if(entries.length>0)
          {
            // Alle Kinder lï¿½schen und das Icon auf [+] setzen
            //
            entries[0].parentNode.removeChild(entries[0]);
            var image=element.src.replace(/minus/g,"plus");
            element.src = image;
          }
          else
          {
            var x = $(document.createElement("div"));
            li.appendChild(x);
            x.replace(transport.responseText);
            x=$(x);
            var image=element.src.replace("load.gif","minus.png");
            element.src = image;
            var collection =  li.select('.grouped_listbox_onclick');
            
            li.select('.grouped_listbox_checkbox').each(function(element){Event.observe(element,"click",check);});
            li.select('.grouped_listbox_onclick').each(function(element){Event.observe(element,"click",onClick);});
          }
      }
   });
  }

  function toggle(event)
  {
    var image = Event.findElement(event,"img");
    var outer = image.parentNode.parentNode;
    var inner = outer.firstChild;

    if(originalParent==null)
    {
      originalParent = outer.parentNode;
      innerWidth =inner.style.width;
      innerHeight =inner.style.height;
      outerWidth =outer.style.width;
      outerHeight =outer.style.height;
      outerLeft =outer.style.left;
      outerTop =outer.style.top;

      $("body").appendChild(outer);
      outer.style.left="0px";
      outer.style.top="0px";
      outer.style.width="100%";
      outer.style.height="100%";
      inner.style.width="100%";
      inner.style.height="100%";
    }
    else
    {
      outer.style.left=outerLeft;
      outer.style.top=outerTop;
      outer.style.width=outerWidth;
      outer.style.height=outerHeight;
      inner.style.width=innerWidth;
      inner.style.height=innerHeight;
      originalParent.appendChild(outer);
      originalParent = null;
    }
  }

  function initEventHandler(rootContainer)
  {
    rootContainer.select('.grouped_listbox_collapse').each(function(element){Event.observe(element,"click",collapse);});
    rootContainer.select('.grouped_listbox_checkbox').each(function(element){Event.observe(element,"click",check);});
    rootContainer.select('.grouped_listbox_onclick').each(function(element){Event.observe(element,"click",onClick);});
    rootContainer.select('div.grouped_listbox').each(function(element)
    {
       var bar = document.createElement("div");
       bar.style.position="absolute";
       bar.style.top="0px";
       bar.style.right="16px";
       bar.style.width="auto";
       bar.style.opacity="0.01";
       bar.style.filter="alpha(opacity=1)";
       bar.className="grouped_listbox_icons";

       var selectAllIcon = document.createElement('img');
       selectAllIcon.src = "./themes/"+userTheme+"/images/listbox_selectall.png";
       selectAllIcon.style.cursor= "pointer";
       selectAllIcon.style.float= "left";
       bar.appendChild(selectAllIcon);
       Event.observe(selectAllIcon,"click",selectAll.bindAsEventListener(element, true));

       var deselectAllIcon = document.createElement('img');
       deselectAllIcon.src = "./themes/"+userTheme+"/images/listbox_deselectall.png";
       deselectAllIcon.style.cursor= "pointer";
       deselectAllIcon.style.float= "left";
       bar.appendChild(deselectAllIcon);
       Event.observe(deselectAllIcon,"click",selectAll.bindAsEventListener(element,false));

       var toggleIcon = document.createElement('img');
       toggleIcon.src = "./themes/"+userTheme+"/images/expand_ui.png";
       toggleIcon.style.cursor= "pointer";
       toggleIcon.style.float= "left";
       bar.appendChild(toggleIcon);
       Event.observe(toggleIcon,"click",toggle);

       Event.observe(element,"scroll",scroll);
       Event.observe(element.parentNode,"mouseenter", function(event)
       {
           this.select(".grouped_listbox_icons").each(function(e)
           {
             new fx.Opacity(e,{duration: 400}).custom(0.01,1);
           });
       });
       Event.observe(element.parentNode,"mouseleave", function(event)
       {
           this.select(".grouped_listbox_icons").each(function(e)
           {
             new fx.Opacity(e,{duration: 400}).custom(1,0.01);
           });
       });
       element.parentNode.appendChild(bar);
    });
  }

  Event.observe(window, "load", function() 
  {
      initEventHandler($("formDiv"));
  });

  // globale Variablen fï¿½r das fullscrenn/normal des Feldes
  //
  var innerWidth =-1;
  var innerHeight =-1;
  var outerWidth =-1;
  var outerHeight =-1;
  var outerLeft =-1;
  var outerTop =-1;
  var originalParent=null;

})();

