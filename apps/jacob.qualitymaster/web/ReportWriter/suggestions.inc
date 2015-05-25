<script>
var fields = [];
<%
IReport.Column[] cs = report.getColumns();
for(int i=0;i<cs.length;i++)
{
 IReport.Column c = cs[i];
 out.println("fields.push('"+c.table+"."+c.field+"');");
}
%>

var suggestions = [];
<%
for(int i=0;i<cs.length;i++)
{
 IReport.Column c = cs[i];
 out.println("suggestions.push('db_field("+c.table+"."+c.field+")');");
}
%>
suggestions.push("~time");
suggestions.push("~date");
suggestions.push("~page");
suggestions.push("~avg()");
suggestions.push("~sum()");
suggestions.push("~min()");
suggestions.push("~max()");
suggestions.push("~count()");
<%
for(int i=0;i<cs.length;i++)
{
 IReport.Column c = cs[i];
 out.println("suggestions.push('"+c.table+"."+c.field+"');");
}
%>
</script>
