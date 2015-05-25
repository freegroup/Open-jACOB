<div id="palette">
  <div class="heading pallette_heading">Report Result Columns</div>
  Grouped By<br>
  asdf<br>
  User sorted<br>
  <ul id="report_fields">
    <%
    for(int i=0;i<columns.length;i++)
    {
      IReport.Column c = columns[i];
      out.println("<li class=\"report_field caption_normal_search\"><img id=\""+c.table+"."+c.field+"\" class=\"sortorder sortorder_"+c.order.toString()+"\" src=\"\">"+c.table+"."+c.field+"</li>");
    }
    %>
  </ul>
</div>

