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
    import="de.tif.jacob.util.*"
    import="de.tif.jacob.license.*"
    import="de.tif.jacob.core.*"
    import="de.tif.jacob.screen.event.*"
    import="de.tif.jacob.core.exception.*"
    import="de.tif.jacob.searchbookmark.*"
    import="de.tif.jacob.screen.impl.*"
    import="de.tif.jacob.screen.impl.html.*"
    import="de.tif.jacob.security.*" 
%>
<%
try
{
    String browserId    = request.getParameter("browser");

    ClientSession jacobSession = (ClientSession) HTTPClientSession.get(request);
    if(jacobSession==null)
        return;
    Application  app=(Application)jacobSession.getApplication(browserId);
    if(app==null)
        return;
    ClientContext context = new ClientContext(jacobSession.getUser(),out, app, browserId);
    if(!app.isNavigationVisible())
        return;
    if(app.isClosed())
        return;

      ToolbarButton exitButton = (ToolbarButton)app.getToolbar().getButton(new ActionTypeExit());
%>
<script>
(function() {
  function ellipsis(e) {
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
  Event.observe(window, "load", function() {
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
      <div id="sideBarContents" style="width:1px;">
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

<div id=navigationbar name=navigationbar style="position: absolute;width:120px;height:100%;top:0px;left:0px;border:1 solid black;">
    <table border=1  cellspacing="0" cellpadding="0" style="width:120px" height="100%" >
    <%
        String domainName="unset";
        Iterator iter = app.getChildren().iterator();
        while(iter.hasNext())
        {
            Domain domain = (Domain)iter.next();
            if (!domain.isVisible())
            	continue;
            String label = domain.getI18NLabel(context.getLocale());
            if(domain == context.getDomain() || domain.getCanCollapse()==false)
            {
                domainName = app.getName()+"_"+domain.getName();
                out.print("<tr><td");
                if(context.isDebug())
                    out.print(" title='"+domain.getName()+"' guidForTestFramework='"+domain.getName()+"'");
                out.println(" colspan=2 class=outlookbarPaneSelected >"+label+"</td></tr>");
                out.println("<tr  id=scrollAreaParent name=scrollAreaParent ><td colspan=2 valign=top >");
                out.println("<div id=outlookbarButtonArea class=outlookbarButtonArea style=\"border:0;height:100%;width:100%;overflow:auto;\">");
                out.println("<table cellspacing=0 cellpadding=0  style=\"table-layout:fixed;\" border=0 height=100% width=100% >");
                IDomainEventHandler.INavigationEntry[] entries = domain.getNavigationEntries(context);
                for(int i=0; i<entries.length; i++)
                {
                  IDomainEventHandler.INavigationEntry entry = entries[i];
                  if(entry.visible==false)
                    continue;
                  String onClickAction =entry.getOnClickAction();
                  if(onClickAction!=null)
                  {
                    out.print("\n<tr>");
                    out.print("\n<td class=outlookbarButtonAreaNormal id=\"");
                    out.print(entry.anchorId);
                    out.print("\" ><div id='outlookbarButtonArea_"+entry.hashCode()+"' ");
                    if(domain.isCurrentNavigationEntry(entry) && domain == context.getDomain())
                        out.print(" class=outlookbarButtonAreaSelected " );
                    else
                        out.print(" class=outlookbarButtonAreaNormal " );
                    out.print(" onClick='"+onClickAction+"' ");
                    out.print(" onmouseOver=\"if(currentButtonArea!=this)this.className='outlookbarButtonAreaOver';\" ");
                    out.print(" onmouseDown=\"this.className='outlookbarButtonAreaDown';\" ");
                    out.print(" onmouseUp=\"if(currentButtonArea!=null)currentButtonArea.className='outlookbarButtonAreaNormal';currentButtonArea=this;this.className='outlookbarButtonAreaSelected';\" ");
                    out.print(" onmouseOut=\"if(currentButtonArea!=this)this.className='outlookbarButtonAreaNormal';\"> ");
                    out.print("<center>");
                    out.print("\n<a ");
                    out.println(" class=outlookbarButtonText href='javascript:"+onClickAction+"'>");
                    out.print("<img id='outlookbarButton_"+entry.hashCode()+"' ");
                    out.print(" onmouseOver=\"if(currentButton!=this)this.className='outlookbarButtonOver';\" ");
                    out.print(" onmouseDown=\"this.className='outlookbarButtonDown';\" ");
                    out.print(" onmouseUp=\"if(currentButton!=null)currentButton.className='outlookbarButtonNormal';currentButton=this;this.className='outlookbarButtonSelected';\" ");
                    out.print(" onmouseOut=\"if(currentButton!=this)this.className='outlookbarButtonNormal';\" ");
                     if(domain.isCurrentNavigationEntry(entry) && domain == context.getDomain())
                        out.print(" class=outlookbarButtonSelected " );
                    else
                        out.print(" class=outlookbarButtonNormal " );
                    out.print(" border=0 src='image?browser="+browserId+"&image="+entry.getImageSrc()+"' >");
                    out.print("</a><br>\n");
                    out.print("<font class=outlookbarButtonLabel >"+StringUtil.htmlEncode(entry.label,"<br>")+"</font>\n</center>");
                    if(domain.isCurrentNavigationEntry(entry) && domain == context.getDomain())
                    {
                        out.print("<script>\n currentButton=document.getElementById('outlookbarButton_"+entry.hashCode()+"');\n");
                        out.print("currentButtonArea=document.getElementById('outlookbarButtonArea_"+entry.hashCode()+"');</script>");
                    }
                    out.println("</div></td></tr>");
                    if(i<(entries.length+1))
                    {
                        out.println("<tr height=1><td><table border=0 width=100% class=outlookbarButtonAreaSpacer cellspacing=0 cellpadding=0><tr class=outlookbarButtonAreaSpacer><td><div class=outlookbarButtonAreaSpacerLeft >x</div></td><td><div class=outlookbarButtonAreaSpacerRight >x</div></td></tr></table></td></tr>");
                    }
                  }
                }
                out.println("<tr><td></td></tr>");
                out.println("</table></div></td></tr>");
            }
            else
            {
                out.println("<tr>");
                out.println("<td ");
                if(context.isDebug())
                    out.println(" title="+domain.getName()+" guidForTestFramework='"+domain.getName()+"' ");
                if(context.isInReportMode())
                    out.println("class=outlookbarPane >");
                else
                    out.println("class=outlookbarPane onClick=\"javascript:FireEvent('"+domain.getId()+"','show');\">");
                out.println(label);
                out.println("</td>");
                out.println("</tr>");
            }
        }
    %>
</table>    
</div>

<%
}
catch(Exception exc)
{
    System.out.println(exc);
    exc.printStackTrace();
}
%>
