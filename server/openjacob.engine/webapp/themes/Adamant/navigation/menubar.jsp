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
    import="de.tif.jacob.core.definition.*"
    import="de.tif.jacob.searchbookmark.*"
    import="de.tif.jacob.screen.event.*"
    import="de.tif.jacob.core.exception.*"
    import="de.tif.jacob.screen.impl.*"
    import="de.tif.jacob.screen.impl.html.*"
    import="de.tif.jacob.security.*" 
%>
<%
      String browserId = request.getParameter("browser");
      ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
      if (jacobSession == null)
        return;
      IApplicationDefinition appDef = jacobSession.getApplicationDefinition();
      Application app = (Application) jacobSession.getApplication(browserId);
      if (app == null)
        return;
      ClientContext context = new ClientContext(jacobSession.getUser(), out, app, browserId);
      if (!app.isNavigationVisible())
        return;
      if (app.isClosed())
        return;

      ToolbarButton exitButton = (ToolbarButton)app.getToolbar().getButton(new ActionTypeExit());
%>
<script>


(function() {
  function ellipsis(e) 
  {
    var w = e.getWidth()-10;
    var t = e.innerHTML;
    var title = t.replace(/<br>/g, "");
    var hasElipsis = false;
    e.innerHTML = "<span>" + t + "</span>";
    e = e.down();
    while (t.length > 0 && e.getWidth() >= w) {
      t = t.substr(0, t.length - 1);
      e.innerHTML = t + "...";
      hasElipsis=true;
    }
    if(hasElipsis)
      e.setAttribute("title",title);
  }
  document.write('<style type="text/css">' + '.ellipsis { margin-right:-10000px; }</style>');

  Event.observe(window, "load", function() 
  {
    $$('.ellipsis').each(ellipsis);
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
                event:'<%=Application.EVENT_SEARCHDELETE%>',
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
      selector: '#sideBar li',
      className: 'menu desktop',
      menuItems: myMenuItems
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
  {
    sidebarWidth.custom(200,1);
  }
  else
  {
    sidebarWidth.custom(1,200);
  }
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
<div id="navigationbar" name="navigationbar" class="listbar">
<div class="listbar_header"><%=context.getUser().getFullName()%> <br>
<%
    String appLogin ="/application/"+app.getName()+"/"+appDef.getVersion().toShortString()+"/userlogin.jsp";
    String path = request.getSession().getServletContext().getRealPath(appLogin); 
    File file = new File(path);
    
    if(context.getUser().isAnonymous() && file.exists()){  %>
        [ <a href=".<%=appLogin%>"><j:i18n id="BUTTON_LOGIN"/></a> ]
    <%}else{%>
        [ <a href="#" onClick="FireEvent(<%=exitButton.getId()%>,'click');"><j:i18n id="BUTTON_TOOLBAR_CLOSE"/></a> ]
    <%}%>
</div>

<%
          Iterator iter = app.getChildren().iterator();
          while (iter.hasNext())
          {
            Domain domain = (Domain) iter.next();
            IDomainEventHandler.INavigationEntry[] entries = domain.getNavigationEntries(context);
            if (!domain.isVisible() || entries==null || entries.length==0)
              continue;
            String label = domain.getI18NLabel(context.getLocale());
            if (domain == context.getDomain() || domain.getCanCollapse() == false)
            {
              out.println("<div  id=\"" + domain.getEtrHashCode() + "\" class=\"ellipsis simpleNavigationLevel1Selected\" >" +StringUtil.htmlEncode(label)+ "</div>");
              for (int i = 0; i < entries.length; i++)
              {
                IDomainEventHandler.INavigationEntry entry = entries[i];
                if(entry.visible==false)
                  continue;
                String onClickAction = entry.getOnClickAction();
                if (onClickAction != null)
                {
                  out.print("<div  id=\"");
                  out.print(entry.anchorId);
                  out.print("\" ");
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
                  out.print("<div class=\"ellipsis simpleNavigationLevel2Separator\" >");
                  out.print(StringUtil.htmlEncode(entry.label,"<br>"));
                  out.println("</div>\n");
                }
              }
            }
            else
            {
              out.println("<div  class=\"ellipsis simpleNavigationLevel1Normal\" ");
              if (!context.isInReportMode())
                out.println(" onClick=\"javascript:FireEvent('" + domain.getId() + "','show');\"");
              out.println(">");
              out.println(StringUtil.htmlEncode(label));
              out.println("</div>");
            }
          }
    %>
<div class="listbar_footer">    </div>
</div>
