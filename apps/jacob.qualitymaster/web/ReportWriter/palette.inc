<%
  Set grouped_by_fields = new HashSet();
  IReport.Column[] columns = report.getColumns();
%>
<div id="palette">
  <div class="heading pallette_heading">Sort Order</div>
  <div class="heading">Grouped By</div>
  <ul id="grouped_by_fields">
  <%
        CastorLayoutPart part = layout.getPart();
        CastorGroup group = part.getGroup();
        while(group!=null)
        {
           if(group.getPart()==null)
              break;
          String group_key = group.getTableAlias()+"."+group.getField();
          grouped_by_fields.add(group_key);
          
          for(int i=0;i<columns.length;i++)
          {
            IReport.Column c = columns[i];
            String key = c.table+"."+c.field;
            if(key.equals(group_key))
            {
              out.println("<li id=\"grouped_by_field_"+key+"\" class=\"grouped_by_field caption_normal_search\">");
              out.println("<img title=\"click to change sort order\" id=\""+key+"\" class=\"sortorder sortorder_"+c.order.toString()+"\" src=\"\">");
              out.println(key);
              out.println("</li>");
            }
          }
          group = group.getPart().getGroup();
        }        
  %>
  </ul>
  <br/>
  <div class="heading">User Order</div>
  <ul id="report_fields">
    <%

    for(int i=0;i<columns.length;i++)
    {
      IReport.Column c = columns[i];
      String key = c.table+"."+c.field;
      if(!grouped_by_fields.contains(key))
      {
        out.println("<li  id=\"report_field_"+key+"\" class=\"report_field caption_normal_search\">");
        out.println("<img title=\"click to change sort order\" id=\""+key+"\" class=\"sortorder sortorder_"+c.order.toString()+"\" src=\"\">");
        out.println(key);
        out.println("</li>");
      }
    }
    %>
  </ul>
</div>

