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

<%@ page import="java.io.*" %>
<%@ page import="de.tif.jacob.cluster.ClusterManager" %>
<%@ page import="de.tif.jacob.cluster.impl.ClusterUdpGroupProvider" %>
<%@ page import="de.tif.jacob.cluster.impl.Group" %>

<html>
<head>
</head>

<body style="color:white;background-color:#081040">
<b>*** CLUSTER GROUP ***</b>
<pre>
<%
    Group clusterGroup = ((ClusterUdpGroupProvider) ClusterManager.getProvider()).getClusterGroup();
    if (clusterGroup != null)
    	clusterGroup.printInfo(new PrintWriter(out));
%>   
</pre> 
<p>
<b>*** GLOBAL GROUP ***</b>
<pre>
<%
    Group globalGroup = ((ClusterUdpGroupProvider) ClusterManager.getProvider()).getGlobalGroup();
    if (globalGroup != null)
    	globalGroup.printInfo(new PrintWriter(out));
%>   
</pre> 
</body>
</html>
