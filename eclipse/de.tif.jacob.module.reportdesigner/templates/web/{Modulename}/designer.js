var Dialog = {};
Dialog.Box = Class.create();
Object.extend(Dialog.Box.prototype, {
    initialize: function(id) {
  
      this.dialog_box = $(id);
      this.dialog_box.show = this.show.bind(this);
      this.dialog_box.persistent_show = this.persistent_show.bind(this);
      this.dialog_box.hide = this.hide.bind(this);

      this.parent_element = this.dialog_box.parentNode;

      this.dialog_box.style.position = "absolute";
  
      var e_dims = Element.getDimensions(this.dialog_box);
      var b_dims = Element.getDimensions($("formDiv"));
  
      this.dialog_box.style.left = ((b_dims.width/2) - (e_dims.width/2)) + 'px';
      this.dialog_box.style.top = this.getScrollTop() + ((this.winHeight() - (e_dims.width/2))/2) + 'px';
    },

    show: function() {
      this.selectBoxes('hide');
      this.dialog_box.style.display = '';
  
      this.dialog_box.style.left = '0px';
   
      var e_dims = Element.getDimensions(this.dialog_box);
        
        this.dialog_box.style.left = ((this.winWidth()/2) - (e_dims.width)/2) + 'px';
        
      this.dialog_box.style.top = this.getScrollTop() + ((this.winHeight() - (e_dims.width/2))/2) + 'px';
        
    },
    
    getScrollTop: function() {
        return (window.pageYOffset)?window.pageYOffset:(document.documentElement && document.documentElement.scrollTop)?document.documentElement.scrollTop:document.body.scrollTop;
    },
    
    persistent_show: function() {
      this.overlay.style.height = this.bodyHeight()+'px';
   
      this.selectBoxes('hide');
      new Effect.Appear(this.overlay, {duration: 0.1, from: 0.0, to: 0.3});
      
      this.dialog_box.style.display = '';
        this.dialog_box.style.left = '0px';
      var e_dims = Element.getDimensions(this.dialog_box);
        this.dialog_box.style.left = ((this.winWidth()/2) - (e_dims.width)/2) + 'px';
        
    },
  
    hide: function() {
      this.selectBoxes('show');
      this.dialog_box.style.display = 'none';
      $A(this.dialog_box.getElementsByTagName('input')).each(function(e){if(e.type!='submit')e.value=''});
    },
  
    selectBoxes: function(what) {
      $A(document.getElementsByTagName('select')).each(function(select) {
        Element[what](select);
      });
  
      if(what == 'hide')
        $A(this.dialog_box.getElementsByTagName('select')).each(function(select){Element.show(select)})
    },
    
    bodyWidth: function() { return document.body.offsetWidth || window.innerWidth || document.documentElement.clientWidth || 0; },
    bodyHeight: function() { return document.body.offsetHeight || window.innerHeight || document.documentElement.clientHeight || 0; },
  
    winWidth: function() {
         var viewportwidth;
         
         // the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight
         
         if (typeof window.innerWidth != 'undefined')
         {
              viewportwidth = window.innerWidth;
         }
         
        // IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
        
         else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0)
         {
               viewportwidth = document.documentElement.clientWidth;
         }
         
         // older versions of IE
         
         else
         {
               viewportwidth = document.getElementsByTagName('body')[0].clientWidth
         }
         return viewportwidth;
    
     },
    winHeight: function() { 
         var viewportheight;
         
         // the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight
         
         if (typeof window.innerHeight != 'undefined')
         {
              viewportheight = window.innerHeight
         }
         
        // IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
        
         else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientHeight != 'undefined' && document.documentElement.clientHeight != 0)
         {
               viewportheight = document.documentElement.clientHeight;
         }
         
         // older versions of IE
         
         else
         {
               viewportheight = document.getElementsByTagName('body')[0].clientHeight;
         }
         return viewportheight;
    
    }
  
  }); 

/*
Element.implement({
 
    highlight: function(search, insensitive, klass)
    {
        var regex = new RegExp('(<[^>]*>)|(\\b'+ search.escapeRegExp() +')', insensitive ? 'ig' : 'g');
        return this.set('html', this.get('html').replace(regex, function(a, b, c){
            return (a.charAt(0) == '<') ? a : '<strong class="'+ klass +'">' + c + '</strong>'; 
        }));
    }
});
*/

/***************************************************
 * Stringfunktionen des Browsers erweitern
 *
 * PRIVATE
 ***************************************************/
String.prototype.trim = function( )
{
  return( this.replace(new RegExp("^([\\s]+)|([\\s]+)$", "gm"), "") );
};

function winWidth()  { return document.body.clientWidth || window.innerWidth || document.documentElement.clientWidth || 0; }
function winHeight() { return document.body.clientHeight || window.innerHeight || document.documentElement.clientHeight || 0; }


var fxMap = [];
function toggleArea(event)
{
  var element = event.element();
  var id = element.id.replace("_trigger","_area");

  var toggleFx = fxMap[id];
  if(!toggleFx)
  {
     toggleFx=new fx.Height(id, {duration: 400, onComplete:function(e)
     {
        // HÃ¯Â¿Â½henangabe im Style muÃ¯Â¿Â½ entfernt werden sonst funktioniert der
        // automatische Resize des Textarea nicht mehr.
        element.src = img["PLUS"]
        if(parseInt(this.el.style.height)>1)
        {
          this.el.style.height="";
          element.src = img["MINUS"];
        }
     }});
     fxMap[id]=toggleFx;
  }
  if(toggleFx.el.offsetHeight>1)
  {
    toggleFx.custom(toggleFx.el.offsetHeight,1);
    new fx.Opacity(id,{duration: 400}).custom(1,0);
  }
  else
  {
    toggleFx.custom(1,toggleFx.el.scrollHeight);
    new fx.Opacity(id,{duration: 400}).custom(0,1);
  }
}


var reporting_menu=null;
var textMetric=20; // default character width. will be callculated in the document.load method
var dialog=null;

(function() 
{
  var divider=null;

  function initGroupDivider(element)
  {
      Event.observe(element, "mousedown", function(event)
      {
          divider= event.element();
          event.stop();
      });
      Event.observe(element, "mouseup", function(event)
      {
          if(divider==null)
             return;
          divider=null;
          event.stop();
      });
  }

  function initDeleteGroupButton(button)
  {
      Event.observe(button, "click", function(event)
      {
          event.stop();
          event.element().ancestors().each(function(e)
          {
              if(e.hasClassName("group"))
              {
                  new Ajax.Request("rpc/removeGroup.jsp",
                  {
                    parameters:
                    {
                        method:"post",
                        browser: browserId, 
                        group:e.id.replace("group_","")
                    },
                    onSuccess:function (transport)
                    { 
                      var key = e.select(".group_by").first().innerHTML;
                      var childGroup = e.select(".group").first();
                      if(childGroup)
                        e.replace(childGroup);
                      else
                        e.replace($("report_columns"));

                      var group_field = $("grouped_by_field_"+key);
                      group_field.id = "report_field_"+key;
                      $("report_fields").appendChild(group_field);
                      group_field.removeClassName("grouped_by_field");
                      group_field.addClassName("report_field");
                      initSortableField();
                      layout();

                      var children = $("group_parent").select("div.group");
                      var groupCount = children.length;
                      for(var i=1;i<=groupCount;i++)
                      {
                        var index=i;
                        var index_1 = index-1;
                        $("group_"+index).id="group_"+index_1;
                        $("section_"+index).id="section_"+index_1;
                        $("section_header_"+index+"_collapse_trigger").id="section_header_"+index_1+"_collapse_trigger";
                        $("section_footer_"+index+"_collapse_trigger").id="section_footer_"+index_1+"_collapse_trigger";
                        $("section_header_"+index+"_tip").id="section_header_"+index_1+"_tip";
                        $("section_footer_"+index+"_tip").id="section_footer_"+index_1+"_tip";
                        $("group_by_"+index).id="group_by_"+index_1;
                        $("section_header_"+index+"_collapse_area").id="section_header_"+index_1+"_collapse_area";
                        $("section_footer_"+index+"_collapse_area").id="section_footer_"+index_1+"_collapse_area";
                        $("section_header_"+index+"_textarea").id="section_header_"+index_1+"_textarea";
                        $("section_footer_"+index+"_textarea").id="section_footer_"+index_1+"_textarea";
                        $("section_header_"+index+"_textarea_preformated").id="section_header_"+index_1+"_textarea_preformated";
                        $("section_footer_"+index+"_textarea_preformated").id="section_footer_"+index_1+"_textarea_preformated";
                      }                    
                    }
                  });
                  throw $break;
              }
          });
      });
  }

  function initGroupedByButton(button)
  {
      Event.observe(button, "click", function(event)
      {
          event.stop();
          var button = event.element();
          event.element().ancestors().each(function(e)
          {
              if(e.hasClassName("group"))
              {
                  var group = e;
                  var group_id= group.id.replace("group_","");

                  if(reporting_menu!=null) 
                     reporting_menu.remove();
                  // gruppe gefunden
                  reporting_menu = $(document.createElement("div"));
                  reporting_menu.className = "suggestions";
                  reporting_menu.style.position="absolute";
                  reporting_menu.zIndex = "1000";

                  var ul = document.createElement("ul");
                  ul.className= "suggestions";
                  reporting_menu.appendChild(ul);
                  for(var i=0;i<fields.length;i++)
                  {
                    var key = fields[i];
                    if($("grouped_by_field_"+key)!=null)
                      continue;
                    var li = document.createElement("li");
                    ul.appendChild(li);

                    var a = document.createElement("a");
                    li.appendChild(a);

                    a.className="suggestion";
                    a.innerHTML=key;
                    Event.observe(a,"mousedown",function(field_event)
                    {
                        new Ajax.Request("rpc/setGroupedBy.jsp",
                        {
                          parameters:
                          {
                              method:"post",
                              browser: browserId, 
                              field: field_event.element().innerHTML,
                              group: group_id
                          },
                          onSuccess:function (transport)
                          { 
                              var old_field = button.innerHTML;
                              var new_field = transport.responseText.trim();
                              button.innerHTML= new_field;
                              var group_field = $("grouped_by_field_"+old_field);
                              var report_field = $("report_field_"+new_field);
                              group_field.id = "report_field_"+old_field;
                              report_field.id="grouped_by_field_"+new_field;
                              $("report_fields").appendChild(group_field);
                              $("grouped_by_fields").appendChild(report_field);

                              group_field.removeClassName("grouped_by_field");
                              group_field.addClassName("report_field");

                              report_field.removeClassName("report_field");
                              report_field.addClassName("grouped_by_field");

                              initSortableField();
                          }
                        });
                    });
                  }
                  reporting_menu.style.top = event.clientY;
                  reporting_menu.style.left = event.clientX;
                  document.body.appendChild(reporting_menu);
                  throw $break;
              }
          });
      });
  }

  function initSortableColumns()
  {
    Sortable.create('report_columns_container',
    {
      constraint:false,
      ghosting:false,
      tag:"div",
      onUpdate:function(sortable, event)
      {
          var columns= $$('.report_column').collect(function(e){return e.id.replace("report_column_","");});
          new Ajax.Request("rpc/setReportColumnSequence.jsp",
          {
            parameters:
            {
                method:"post",
                browser: browserId, 
                columns: columns
            }
          });
      }
    });

    $$(".column_delete_button").each(function(e){e.stopObserving()});
    $$(".column_delete_button").each( function(element)
    { 
       Event.observe(element,"click",function(event)
       {
           event.stop();
           var index = $$(".column_delete_button").indexOf($(event.element()));
           new Ajax.Request("rpc/removeColumn.jsp",
           {
             parameters:
             {
                 method:"post",
                 browser: browserId, 
                 index: index
             },
             onSuccess: function (transport)
             {
                 $(event.element().parentNode).remove();
             }
           });
       });
    });
    
    $$(".column_edit_button").each(function(e){e.stopObserving()});
    $$(".column_edit_button").each( function(element)
    { 
       Event.observe(element,"click",function(event)
       {
           event.stop();
           var index = $$(".column_edit_button").indexOf($(event.element()));
           new Ajax.Request("rpc/getColumn.jsp",
           {
             parameters:
             {
                 evalJSON:true,
                 method:"post",
                 browser: browserId, 
                 index: index
             },
             onSuccess: function (transport)
             {
                 var data = transport.responseJSON;
                 if(dialog ==null)
                     dialog = new Dialog.Box("dialog_edit_column");
                 dialog.show();
                 dialog.columnIndex = index;
                 $("column_label").value =data.label;
                 $("column_ident").value =data.ident;
                 $("column_width").value =data.width;
                 $("column_label").focus();
             }
           });
       });
    });
    
  }

  function initSortableField()
  {
      Sortable.create('report_fields',
      {
        ghosting:false,
        onUpdate:function(sortable, event)
        {
            var groupFields= $$('.grouped_by_field').collect(function(e){return e.id.replace("grouped_by_field_","");});
            var normalFields= $$('.report_field').collect(function(e){return e.id.replace("report_field_","");});
            normalFields.each(function(e) {
                groupFields.push(e);
            });
            new Ajax.Request("rpc/setReportFieldSequence.jsp",
            {
              parameters:
              {
                  method:"post",
                  browser: browserId, 
                  fields: groupFields
              }
           });
        }
      });
  }

  function initTextarea(textarea)
  {

    var r=  new ResizingTextArea(textarea);

    $(textarea.id.replace("_textarea","_tip")).hide();
    Event.observe(textarea,"focus",function(e)
    {
        $(e.element().id.replace("_textarea","_tip")).show();
    });
    Event.observe(textarea,"blur",function(e)
    {
        $(e.element().id.replace("_textarea","_tip")).hide();
    });

  }

  Event.observe(window, "load", function() 
  {
      // create/init the layout of the designer
      layout(200);

      initSortableField();
      initSortableColumns();

      var m =$("textmetrics");
      textMetric = parseInt(m.getWidth()/m.innerHTML.length);
      m.remove;
      // die bereits eingefÃ¯Â¿Â½gten Gruppen mit dem berechneten Wert aktualisieren
      //
      $("group_parent").select(".group").each(function(group)
      {
        var s = group.style.left;
        var l=0;
        if(s && s.length>0)
           l = parseInt(s);
        group.style.left=(l*textMetric)+"px";
      });


      Event.observe($("close_dialog_button"), "click", function(event)
      {
          if(dialog!=null)
             dialog.hide();
      });

      Event.observe($("save_column_button"), "click", function(event)
      {
          var label = $F("column_label");
          var ident = $F("column_ident");
          var width = $F("column_width");
          
          var myAjax = new Ajax.Request("rpc/editColumn.jsp",
          {
            parameters:
            {
              method:"post",
              browser: browserId,
              index: dialog.columnIndex,
              label: label,
              ident: ident,
              width:width
            }
        });
          if(dialog!=null)
             dialog.hide();
      });


      Event.observe($("cancel_report"), "click", function(event)
      {
         var myAjax = new Ajax.Request("rpc/cancelReport.jsp",
         {
           parameters:{ method:"post",browser: browserId},
           onSuccess: function (transport){ parent.FireEvent(0,0); }
         });
      });


      Event.observe($("report_columns_add"), "click", function(event)
      {
          reporting_menu = $(document.createElement("div"));
          reporting_menu.className = "suggestions";
          reporting_menu.style.position="absolute";
          reporting_menu.zIndex = "1000";

          var ul = document.createElement("ul");
          ul.className= "suggestions";
          reporting_menu.appendChild(ul);
          for(var i=0;i<fields.length;i++)
          {
            var key = fields[i];

            var li = document.createElement("li");
            ul.appendChild(li);

            var a = document.createElement("a");
            li.appendChild(a);

            a.className="suggestion";
            a.innerHTML=key;
            Event.observe(a,"mousedown",function(field_event)
            {
                var field = field_event.element().innerHTML;
                var myAjax = new Ajax.Request("rpc/addColumn.jsp",
                {
                  parameters:
                  {
                    method:"post",
                    browser: browserId,
                    field: field
                  },
                  onSuccess: function (transport)
                  {
                    if(reporting_menu!=null)
                    {
                       reporting_menu.remove();
                       reporting_menu=null;
                    }
                    var div = document.createElement("div");
                    div.innerHTML = field;
                    div.className="caption_normal_search report_column";

                    var ed = document.createElement("img");
                    ed.src = img["EDIT"];
                    ed.className="column_edit_button";
                    div.appendChild(ed);

                    var del = document.createElement("img");
                    del.src = img["CLOSE"];
                    del.className="column_delete_button";
                    div.appendChild(del);
                    $("report_columns_container").insertBefore(div,$("float_breaker"));
                    initSortableColumns();
                  }
                });
             });
          }
          reporting_menu.style.top = event.clientY;
          reporting_menu.style.left = event.clientX;
          document.body.appendChild(reporting_menu);
          event.stop();
      });

      Event.observe($("save_report"), "click", function(event)
      {
         var myAjax = new Ajax.Request("rpc/saveReport.jsp",
         {
           parameters:
           {
            method:"post",
            browser: browserId,
            first_page_header:$F("first_page_header_textarea"),
            page_header: $F("page_header_textarea"),
            page_footer: $F("page_footer_textarea"),
            section_header: $$('.section_header_textarea').collect($F),
            section_footer: $$('.section_footer_textarea').collect($F).reverse(),
            last_page_footer: $F("last_page_footer_textarea")
           },
           onSuccess: function (transport){ parent.FireEvent(0,0); }
         });
      });

      Event.observe($("add_group"), "click", function()
      {
          if($("report_fields").select("li").first()==null)
          {
            alert("no more fields available for grouping");
            return;
          }
          new Ajax.Request("rpc/addGroup.jsp",
          {
            parameters:
            {
               method:"post",
               browser: browserId,
               tableAlias:"alias",
               field:"field"
            },
            onSuccess: function (transport)
            { 
                var group_by_field = transport.responseText.trim();
                var group_field = $("report_field_"+group_by_field);
                group_field.id = "grouped_by_field_"+group_by_field;
                $("grouped_by_fields").insertBefore(group_field,$("grouped_by_fields").firstChild);
                group_field.removeClassName("report_field");
                group_field.addClassName("grouped_by_field");

                initSortableField();
              
                // alle bestehenden gruppen um 1 hochzÃ¯Â¿Â½hlen
                //
                var children = $("group_parent").select("div.group");
                var groupCount = children.length;
                for(var i=1;i<=groupCount;i++)
                {
                  var index=groupCount-i;
                  var index_1 = index+1;
                  $("group_"+index).id="group_"+index_1;
                  $("section_"+index).id="section_"+index_1;
                  $("section_header_"+index+"_collapse_trigger").id="section_header_"+index_1+"_collapse_trigger";
                  $("section_footer_"+index+"_collapse_trigger").id="section_footer_"+index_1+"_collapse_trigger";
                  $("section_header_"+index+"_tip").id="section_header_"+index_1+"_tip";
                  $("section_footer_"+index+"_tip").id="section_footer_"+index_1+"_tip";
                  $("group_by_"+index).id="group_by_"+index_1;
                  $("section_header_"+index+"_collapse_area").id="section_header_"+index_1+"_collapse_area";
                  $("section_footer_"+index+"_collapse_area").id="section_footer_"+index_1+"_collapse_area";
                  $("section_header_"+index+"_textarea").id="section_header_"+index_1+"_textarea";
                  $("section_footer_"+index+"_textarea").id="section_footer_"+index_1+"_textarea";
                  $("section_header_"+index+"_textarea_preformated").id="section_header_"+index_1+"_textarea_preformated";
                  $("section_footer_"+index+"_textarea_preformated").id="section_footer_"+index_1+"_textarea_preformated";
                }

                var firstChild = $("group_parent").select("div")[0];
                var group = document.createElement("div");
                group.className = "group";
                group.id = "group_0";

                // HEADER 
                //
                var section = document.createElement("div");
                section.className="section";
                section.id="section_0";
                group.appendChild(section);

                var section_header = document.createElement("div");
                section_header.className="section_header";
                section.appendChild(section_header);

                var group_divider = $(document.createElement("span"));
                section_header.appendChild(group_divider);
                group_divider.className="group_divider";
                initGroupDivider(group_divider);

                var trigger=document.createElement("img");
                section_header.appendChild(trigger);
                trigger.id ="section_header_0_collapse_trigger";
                trigger.className="collapse_trigger";
                trigger.src=img["MINUS"];

                var caption = document.createElement("span");
                section_header.appendChild(caption);
                caption.className="header_title";
                caption.innerHTML="Grouped by";

                var group_by = document.createElement("span");
                section_header.appendChild(group_by);
                group_by.id ="group_by_0";
                group_by.className="group_by report_button";
                group_by.innerHTML=group_by_field;
                initGroupedByButton(group_by);


                var del = $(document.createElement("img"));
                section_header.appendChild(del);
                del.className ="section_delete_button";
                del.id = "section_0_delete";
                del.src = img["CLOSE"];
                initDeleteGroupButton(del);

                var tip = $(document.createElement("span"));
                section_header.appendChild(tip);
                tip.id="section_header_0_tip";
                tip.className = "autosuggesttip";
                tip.innerHTML="Press ctrl+space for autosuggest";
                
                var collapse_area = document.createElement("div");
                section.appendChild(collapse_area);
                collapse_area.className="section_collapse_area";
                collapse_area.id="section_header_0_collapse_area";

                var text = document.createElement("textarea");
                collapse_area.appendChild(text);
                text.id="section_header_0_textarea";
                text.className ="autogrow autosuggest section_header_textarea";
                text.rows="1";
                text.wrap="OFF";

                $("group_parent").appendChild(group);
                section.appendChild(firstChild);
                initTextarea(text);

                Event.observe(trigger,"click",toggleArea);
                
                // FOOTER 
                //
                var section_header = document.createElement("div");
                section_header.className="section_header";
                section.appendChild(section_header);

                var trigger=document.createElement("img");
                section_header.appendChild(trigger);
                trigger.id ="section_footer_0_collapse_trigger";
                trigger.className="collapse_trigger";
                trigger.src=img["MINUS"];

                var caption = document.createElement("span");
                section_header.appendChild(caption);
                caption.className="footer_title";
                caption.innerHTML="Group Footer";

                var tip = $(document.createElement("span"));
                section_header.appendChild(tip);
                tip.id="section_footer_0_tip";
                tip.className = "autosuggesttip";
                tip.innerHTML="Press ctrl+space for autosuggest";
                tip.hide();

                var collapse_area = document.createElement("div");
                section.appendChild(collapse_area);
                collapse_area.className="section_collapse_area";
                collapse_area.id="section_footer_0_collapse_area";

                var text = document.createElement("textarea");
                collapse_area.appendChild(text);
                text.id="section_footer_0_textarea";
                text.className ="autogrow autosuggest section_footer_textarea";
                text.rows="1";
                text.wrap="OFF";
                initTextarea(text);

                Event.observe(trigger,"click",toggleArea);

                layout();
            }
          });
      });

      Event.observe(window, "resize", function(){layout();});

      $$(".collapse_trigger").each(function(e){ Event.observe(e,"click",toggleArea);});
      $$(".group_divider").each(initGroupDivider);
      $$(".section_delete_button").each(initDeleteGroupButton);
      $$("img.sortorder_NONE").each(function(element){element.src=img["NONE"];});
      $$("img.sortorder_ASCENDING").each(function(element){element.src=img["ASCENDING"];});
      $$("img.sortorder_DESCENDING").each(function(element){element.src=img["DESCENDING"];});
      $$("textarea.autogrow").each(initTextarea);
      $$(".group_by").each(initGroupedByButton);

      Event.observe($("resize_handle"), "mousedown", function(event)
      {
          divider= event.element();
          event.stop();
      });


      $$(".sortorder").each(function(element)
      {
           Event.observe(element,"click",function(event)
           {
               new Ajax.Request("rpc/toggleSortOrder.jsp",
               {
                 parameters:{method:"post",browser: browserId, column:event.element().id },
                 onSuccess: function (transport)
                 { 
                     var sortOrder = transport.responseText.trim();
                     $(transport.request.parameters.column).src=img[sortOrder] 
                 }
               });
           });
      });

      Event.observe(document, "mouseup", function(event)
      {
         if(divider!=null && divider.id!="resize_handle")
         {
            divider.ancestors().each(function(e)
            {
                if(e.hasClassName("group"))
                {
                    new Ajax.Request("rpc/setGroupIdent.jsp",
                    {
                      parameters:
                      {
                           method:"post",
                           browser: browserId, 
                           group:e.id.replace("group_",""),
                           ident:parseInt(parseInt(e.style.left)/textMetric)
                      }
                    });
                    throw $break;
                }
            });
          }
          divider=null;
      });

      Event.observe(document, "click", function(event)
      {
         if(divider!=null && divider.id!="resize_handle")
         {
            divider.ancestors().each(function(e)
            {
                if(e.hasClassName("group"))
                {
                    new Ajax.Request("rpc/setGroupIdent.jsp",
                    {
                      parameters:
                      {
                           method:"post",
                           browser: browserId, 
                           group:e.id.replace("group_",""),
                           ident:parseInt(parseInt(e.style.left)/textMetric)
                      }
                    });
                    throw $break;
                }
            });
          }
          divider=null;
          if(reporting_menu!=null)
          {
            reporting_menu.remove();
            reporting_menu = null;
          }
      });

      Event.observe(document, "mousemove", function(event)
      {
         if(divider==null)
           return;

         var element = event.element();
         if(divider.id=="resize_handle")
         {
            var body_width= winWidth();
            var left = Math.min(body_width-150,event.clientX);
            layout(body_width-left);
         }
         else
         {
            // alle margin-left der Ã¯Â¿Â½bergeordneten gruppen addieren und das was 
            // fehlt an der betroffenen gruppe setzten.
            var body_width= winWidth();
            var marginLeft=0;
            var firstGroup=null;
            divider.ancestors().each(function(e)
            {
                if(e.hasClassName("group"))
                {
                   if(firstGroup==null)
                   {
                     firstGroup = e;
                   }
                   else
                   {
                    var left = parseInt(e.style.left);
                    if(left)
                      marginLeft = marginLeft+left;
                   }
                }
            });
            var div = Math.max(0,event.clientX-marginLeft-5);
            firstGroup.style.left=div+"px";
            
            // fuer alle untergeordneten gruppen die weite berechnen
            //
            layout();
         }
         event.stop();
      });
   });
})();

  function checkSaveColumnButton()
  {
//    if(isValidFieldName(fieldName) && type!=null && type.length>0)
      Form.Element.enable("save_column_button");
//    else
//      Form.Element.disable("save_column_button");
  }

  function isValidInteger(value)
  {
     if(value.length==0)
     {
        return "Enter a valid number";
     }

     var ex = new RegExp("^[0-9]*$");
     if(!name.match(ex))
     {
        return "Enter a valid number";
     }
    
    return null;
  }

   function layout(palette_width)
   {
     if(!palette_width)
     {
        palette_width = Math.max(150,$("palette").getWidth());
     }

     var body_width= winWidth();
     var body_height= winHeight();
     var resize_left=  Math.max(1,body_width-palette_width);
     var top = parseInt($("content_area").getStyle("top"));

     $("resize_handle").setStyle({left:resize_left+"px"});
     resize_left = resize_left-parseInt($("content_area").getStyle("left"));;
     $("content_area").setStyle({width:(resize_left)+"px",height: Math.max(1,body_height-top)+"px"});
     $("palette").setStyle({width: Math.max(1,palette_width)+"px"});

     $$(".section").each(function(section)
     {
        var s = section.parentNode.getStyle("left");
        var left=0;
        if(s && s.length>0)
           left = parseInt(s);
         var width =  Math.max(1,resize_left-left-10);
        section.style.width=width+"px";
     });

     var decWidth= parseInt($("report_detail").getWidth());
     $("group_parent").select(".group").each(function(group)
     {
        var s = group.style.left;
        var l=0;
        if(s && s.length>0)
           l = parseInt(s);
        decWidth =  Math.max(10,decWidth-l);
        group.style.width=(decWidth)+"px";
     });


   }
