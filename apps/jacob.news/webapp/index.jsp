<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>

<%@ page import="org.apache.commons.net.*" %>
<%@ page import="org.apache.commons.net.nntp.*" %>
<%
NNTPClient client = new NNTPClient();
client.connect("localhost",1190);
NewsgroupInfo info[]= client.listNewsgroups();
for(int i=0;i<info.length;i++)
{
  out.println(info[i].getNewsgroup()+ "<br>");
}
client.logout();
%>
