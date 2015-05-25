<%--
     This file is part of Open-jACOB
     Copyright (C) 2005-2006 Tarragon GmbH
  
     This program is free software; you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation; version 2 of the License.
  
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
  
     You should have received a copy of the GNU General Public License     
     along with this program; if not, write to the Free Software
     Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
     USA
--%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="j" %>
<%@page import="de.tif.jacob.core.definition.actiontypes.ActionTypeExit"%>
<%@ page import="java.util.*"
    import="java.io.*"
    import="de.tif.jacob.util.*"
    import="de.tif.jacob.license.*"
    import="de.tif.jacob.core.*"
    import="de.tif.jacob.screen.event.*"
    import="de.tif.jacob.core.exception.*"
    import="de.tif.jacob.screen.impl.*"
    import="de.tif.jacob.searchbookmark.*"
    import="de.tif.jacob.screen.impl.html.*"
    import="de.tif.jacob.security.*" 
%>
<%
      String browserId = request.getParameter("browser");
      ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
      if (jacobSession == null)
        return;
      Application app = (Application) jacobSession.getApplication(browserId);
      if (app == null)
        return;
      ClientContext context = new ClientContext(jacobSession.getUser(), out, app, browserId);
      if (!app.isNavigationVisible())
        return;
      if (app.isClosed())
        return;
      
      ToolbarButton exitButton = (ToolbarButton)app.getToolbar().getButton(new ActionTypeExit());
      String menu_scroll_position = StringUtil.toSaveString((String)context.getProperty("menu_scroll_position"));
      if(menu_scroll_position.length()==0)
        menu_scroll_position="0";
%>

<%@page import="de.tif.jacob.core.definition.ActionType"%>
<script>

var expandCurrentImage = "themes/Audi_new/images/ns-expand_current.png";
var expandImage        = "themes/Audi_new/images/ns-expand.png";
var collapseImage      = "themes/Audi_new/images/ns-collapse.png";
var fxMap = new Array();


  function toogleNavigation(domainId, containerId, isCurrentDomain)
  {
    var myAjax = new Ajax.Request("dialogs/toggleDomain.jsp",
    {
      parameters: 
      {
        guid: domainId,
        containerId:containerId,
        browser: browserId
      },
      onSuccess: function(transport)
      {
          var id= transport.request.parameters.containerId;
          var toggleFx = fxMap[id];
          if(toggleFx)
          {
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
          else
          {
            if(isCurrentDomain)
                toggleFx=new fx.Height(id, {duration: 400, onComplete:function(){toggleCurrentImage("image_"+domainId);}});
            else
                toggleFx=new fx.Height(id, {duration: 400, onComplete:function(){toggleImage("image_"+domainId);}});
            fxMap[id]=toggleFx;
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
        }
    });
  }

  function toggleImage(id)
  {
    var img = getObj(id);
    if(img.src.indexOf(expandImage)!=-1)
      img.src= collapseImage;
    else
      img.src= expandImage;
  }

  function toggleCurrentImage(id)
  {
    var img = getObj(id);
    if(img.src.indexOf(expandCurrentImage)!=-1)
      img.src= collapseImage;
    else
      img.src= expandCurrentImage;
  }

(function() {
  document.write('<style type="text/css">.ellipsis { margin-right:-10000px; }</style>');

  Event.observe(window, "resize", function() 
  {
    if(fireEventIsInProcess==true)
      return;
    var height= new PageSize().height;
    var offsetTop = $('menu_scroll_area').positionedOffset().top;
    var newHeight = Math.max(100,height-offsetTop);
    $('menu_scroll_area').setStyle({height:newHeight+"px"});
  });
  
  Event.observe(window, "load", function() 
  {
    var height= new PageSize().height;
    var offsetTop = $('menu_scroll_area').positionedOffset().top;
    var newHeight = Math.max(100,height-offsetTop);
    $('menu_scroll_area').setStyle({height:newHeight+"px"});
    $('menu_scroll_area').scrollTop=<%=menu_scroll_position%>;
    $$('.tip_message').each(function(element)
    {
    	element.parentNode.addClassName("tip_box");
    });

    $$('.ellipsis').each(ellipsis);
    
    $("spotlight").value=SPOTLIGHT_SHADOW_TEXT;
    new Autocomplete('spotlight', { 
        serviceUrl:'./rpc/getSpotlightResults.jsp?browser='+browserId,
        minChars:2, 
        maxHeight:400,
        width:300,
        deferRequestBy:100,
        // callback function:
        onSelect: function(value){
          FireEventData("<%=app.getId()%>","spotlight",value.id);
          }
      });

      Event.observe('spotlight', 'focus', function() {
         if (this.value.trim()==SPOTLIGHT_SHADOW_TEXT) {
           this.value = "";
           this.className="spotlight_focus";
         }
       });

      Event.observe('spotlight', 'blur', function() {
      if (this.value.trim().length==0) {
        this.value = SPOTLIGHT_SHADOW_TEXT;
        this.className="spotlight";
      }
    });
  
    $("menu_scroll_area").observe("scroll", function(){
       if( this.lastScrollTop == this.scrollTop)
          return;
       var myAjax = new Ajax.Request("rpc/setWindowProperty.jsp",
       {
         parameters: 
         {
           browser: browserId,
           name:"menu_scroll_position",
           value: this.scrollTop
         }
       });
       this.lastScrollTop = this.scrollTop;
   });


    if($("sideBar")==null)
      return;
      
    sidebarWidth= new fx.Width('sideBarContents', {duration:300}); 
    sidebarWidth.custom(1,1);
    $("formDiv").appendChild($("sideBar"));
    Event.observe('sideBarTab', 'click', toggleSideBar, true);
    Event.observe($("formDiv"), 'click', hideSideBar);
    Event.observe($("formDiv"), 'scroll', posSideBar);

    var myMenuItems = [
      {
        name: '<j:i18n2unicode id="BUTTON_COMMON_DELETE"/>',
        className: 'delete', 
        callback: function(event) 
        {
          var element = Event.findElement(event,"li");
          var id = element.id;
          var myAjax = new Ajax.Request("rpc/deleteSearchBookmark.jsp",
          {
              parameters:
              {
                method:'get',
                guid:'<%=app.getId()%>',
                event:'deletecriteria',
                data:id,
                browser: browserId
              },
              onSuccess: function (transport)
              {
                var element = $(transport.request.parameters.data);
                element.remove();
              }
           });
        }
      }
    ];

    new Proto.Menu({
      selector: '#sideBar li', // context menu will be shown when element with id of "contextArea" is clicked
      className: 'menu desktop', // this is a class which will be attached to menu container (used for css styling)
      menuItems: myMenuItems // array of menu items
    });
  });
})();

var sidebarWidth = null;

   
function posSideBar()
{
  var scrollPos = $("formDiv").scrollTop;
  $("sideBar").setStyle("top:"+(20+scrollPos)+"px");
}

function hideSideBar()
{
  if(sidebarWidth.now>1)
    sidebarWidth.custom(sidebarWidth.now,1);
}

function toggleSideBar(e)
{
// probleme im Safari. Der kommt mit "width:0px" nicht zurecht
//    sidebarWidth.toggle();
  if(sidebarWidth.now>1)
    sidebarWidth.custom(200,0);
  else
    sidebarWidth.custom(0,200);
  Event.stop(e);
}

</script>
<%
Collection searchCriterias= SearchConstraintManager.getSearchConstraints(context);
if(searchCriterias.size()>0)
{
  %>
  <div id="sideBar">
      <div id="sideBarTab"></div>
      <div id="sideBarContents"  style="width:1px;">
          <j:i18n id="BUTTON_TOOLBAR_MANAGE_SEARCH_CRITERIA"/>
          <div id="sideBarContentsInner">
              <ul>
              <%  
                  Iterator iter = searchCriterias.iterator();
                  while(iter.hasNext())
                  {
                    SearchConstraint constraint = (SearchConstraint)iter.next();
                    out.println("<li id=\""+constraint.getGUID()+"\" ><a class=\"search_criteria\" onclick=\"FireEventData('"+app.getId()+"','"+Application.EVENT_SEARCHBACKFILL+"','"+constraint.getGUID()+"')\" href=\"#\">"+constraint.getName()+"</a></li>");
                  }
              %>
              </ul>
          </div>
      </div>
  </div>
  <%
}
%>
<div id=navigationbar name="navigationbar" class="listbar">

<div class="listbar_header" id="menu_header" ><%=context.getUser().getFullName()%> <br>
<%
    String appLogin ="/application/"+app.getName()+"/"+app.getVersion()+"/userlogin.jsp";
    String path = request.getSession().getServletContext().getRealPath(appLogin); 
    File file = new File(path);
    
    if(context.getUser().isAnonymous() && file.exists()){  %>
        [ <a href=".<%=appLogin%>"><j:i18n id="BUTTON_LOGIN"/></a> ]
    <%}else{%>
        [ <a href="#" onClick="FireEvent(<%=exitButton.getId()%>,'click');"><j:i18n id="BUTTON_TOOLBAR_CLOSE"/></a> ]
    <%}%>
    <input class="spotlight" type="text" value="Suchbegriff eingeben" id="spotlight" autocomplete=”off” >
</div>

<div class="listbar_border" id="menu_scroll_area">
<br>

    <%
          Iterator iter = app.getChildren().iterator();
          while (iter.hasNext())
          {
            Domain domain = (Domain) iter.next();
            IDomainEventHandler.INavigationEntry[] entries = domain.getNavigationEntries(context);
            if (!domain.isVisible() || entries==null || entries.length==0)
              continue;

            String label = domain.getI18NLabel(context.getLocale());
            boolean hideDomainLabel = domain.getName().startsWith("<");
            if (domain.getCanCollapse() == false)
            {
              if (!hideDomainLabel)
              {
                out.println("<div class=\"nav_pane_header\" ><table width='100%'><tr><td width='100%' class=\"simpleNavigationLevel1Normal\"  style='cursor:default' >");
                out.println(StringUtil.htmlEncode(label));
                if(domain.hasChildInDataStatus(context, Domain.UPDATE) )
                   out.println(" *");
                out.println("</td><td align='right'>");
                out.println("</td></tr></table></div>");
              }
              out.println("<div id=\"" + domain.getEtrHashCode() + "\" >");
              for (int i = 0; i < entries.length; i++)
              {
                IDomainEventHandler.INavigationEntry entry = entries[i];
                if(entry.visible==false)
                  continue;
                String onClickAction = entry.getOnClickAction();
                if (onClickAction != null)
                {
                  out.print("<div id=\"");
                  out.print(entry.anchorId);
                  out.print("\" ");
                  if (entry.getColor() != null)
                  {
                    out.print("style=\"color:");
                    out.print(entry.getColor());
                    out.print("\" ");
                  }
                  if (domain.isCurrentNavigationEntry(entry) && domain == context.getDomain())
                    out.print(" class='ellipsis simpleNavigationLevel2Selected' ");
                  else
                    out.print(" class='ellipsis simpleNavigationLevel2Normal' ");
                  out.print(" onClick='" + onClickAction + "'>");
                  out.print(StringUtil.htmlEncode(entry.label,"<br>"));
                  out.println("</div>\n");
                }
                else
                {
                  out.print("<div class=\"simpleNavigationLevel2Separator\" >");
                  out.print(StringUtil.htmlEncode(entry.label,"<br>"));
                  out.println("</div>\n");
                }
              }
              out.println("</div>\n");
            }
            else
            {
              boolean isCurrentDomain = domain == context.getDomain();
              if (!hideDomainLabel)
              {
                // Falls der user nix gemacht hat wird die domain zusammengeklappt dargestellt
                //
                out.println("<div class=\"nav_pane_header\" ><table width='100%'><tr onclick=\"toogleNavigation(" + domain.getId() + ",'" + domain.getEtrHashCode() + "'," + isCurrentDomain + ");\"><td width='100%' class=\"simpleNavigationLevel1Normal\" style='cursor:pointer;'>");
                out.println(StringUtil.htmlEncode(label));
                if(domain.hasChildInDataStatus(context, Domain.UPDATE) )
                   out.println("<span class='update_flag'> *</span>");
                else if(domain.hasChildInDataStatus(context, Domain.NEW) )
                   out.println("<span class='update_flag'> +</span>");
                out.println("</td><td  align='right'>");
                if (Boolean.TRUE.equals(domain.isOpen()))
                {
                  out.println("<img style='cursor:pointer;' id='image_" + domain.getId() + "' src='themes/Audi_new/images/ns-collapse.png'>");
                  out.println("</td></tr></table></div>");
                  out.println("<div id=\"" + domain.getEtrHashCode() + "\" >");
                }
                else
                {
                  if (domain == context.getDomain())
                    out.println("<img style='cursor:pointer;' id='image_" + domain.getId() + "' current='true' src='themes/Audi_new/images/ns-expand_current.png'>");
                  else
                    out.println("<img style='cursor:pointer;' id='image_" + domain.getId() + "' src='themes/Audi_new/images/ns-expand.png'>");
                  out.println("</td></tr></table></div>");
                  out.println("<div style=\"visibility:hidden;height:1px;overflow:hidden;\" id=\"" + domain.getEtrHashCode() + "\" >");
                }
              }
              for (int i = 0; i < entries.length; i++)
              {
                IDomainEventHandler.INavigationEntry entry = entries[i];
                if(entry.visible==false)
                  continue;
                String onClickAction = entry.getOnClickAction();
                if (onClickAction != null)
                {
                  if(hideDomainLabel)
                     out.println("<div class=\"nav_pane_header\" >");
                  out.print("<div id=\"");
                  out.print(entry.anchorId);
                  out.print("\" ");
                  if (entry.getColor() != null)
                  {
                    out.print("style=\"color:");
                    out.print(entry.getColor());
                    out.print("\" ");
                  }
                  if (hideDomainLabel)
                  {
                    if (domain.isCurrentNavigationEntry(entry) && domain == context.getDomain())
                      out.print(" class='outlookNavigationLevel1Selected' ");
                    else
                      out.print(" class='outlookNavigationLevel1Normal' ");
                  }
                  else
                  {
                    if (domain.isCurrentNavigationEntry(entry) && domain == context.getDomain())
                      out.print(" class='ellipsis simpleNavigationLevel2Selected' ");
                    else
                      out.print(" class='ellipsis simpleNavigationLevel2Normal' ");
                  }
                  out.print(" onClick='" + onClickAction + "'>");
                  out.print(StringUtil.htmlEncode(entry.label,"<br>"));
                  if(entry.form instanceof Form)
                  {
                    Form form = (Form)entry.form;
                    if(form.hasChildInDataStatus(context, Domain.UPDATE) )
                      out.println("<span class='update_flag'> *</span>");
                    else if(form.hasChildInDataStatus(context, Domain.NEW) )
                      out.println("<span  class='update_flag'> +</span>");
                  }
                  out.println("</div>\n");
                  if(hideDomainLabel)
                     out.println("</div>\n");
                }
                else
                {
                  out.print("<div class=\"simpleNavigationLevel2Separator\" >");
                  out.print(StringUtil.htmlEncode(entry.label,"<br>"));
                  out.println("</div>\n");
                }
              }
              if (!hideDomainLabel)
              {
                out.println("&nbsp;\n");
                out.println("</div>\n");
              }
            }
          }
    %>
<div class="listbar_footer"> </div>
</div>

</div>