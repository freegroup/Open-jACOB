<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%
 String pkey = request.getParameter("pkey");
%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
  <link type="text/css" rel="stylesheet" href="css/main.css" />
  <title>Meldungserfassung</title>
</head>

<body>
<form method="POST" action="../../../cmdenter" style="width:100%;height:100%">

<table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
<tr>
  <td colspan="3" valign="top">
  <table style="width:100%;height:100%" cellspacing="0" cellpadding="0" >
  <tr>
    <td colspan="2">
    <input type="hidden" name="entry" value="CreateDefect"/>
    <input type="hidden" name="app"   value="{applicationName} "/>
    <input type="hidden" name="user"  value="anonymous"/>
    <input type="hidden" name="pwd"   value=""/>
    <input type="hidden" name="pkey"  value="<%=pkey%>">
    </td>
  </tr>
  <tr class="hint">
  <td width="1%" style="padding:10px;" valign="top">
  Hinweis:
  </td>
  <td colspan="2"  style="padding:10px;" valign="top">
  Sie haben hier die M&ouml;glichkeit ein Beanstandung des beschriebenen Prozesses zu Melden. 
  Dies ist Bestandteil vom <big><i>kontinuierlichen Verbesserungsprozess</i></big> (KVP) und wird von dem 
  Prozessverantwortlichen bewertet und falls m&ouml;glich umgesetzt. Falls eine Umsetzung mal nicht 
  m&ouml;glich sein sollte, wird dies nachvollziehbar Begr&uuml;ndet. 
  </td>
  </tr>
  <tr>
  <td width="1%"  class="label">
  Betreff:
  </td>
  <td  style="padding:10px;">
    <input type="text" name="subject" value="" size="2" style="width:100%;">
  </td>
  </tr>
  <tr height="100%" style="white-space:nowrap">
    <td valign="top" class="label">Grund der Meldung</td>
    <td  style="padding:10px;"><textarea name="description" style="width:100%;height:100%"></textarea></td>
  </tr>
  </table>
</td>
</tr>
<tr height="1" class="buttonbar">
  <td width="100%">
  </td>
  <td style="padding:10px;">
    <button class="button" onclick="window.close()">Schliessen</button>
  </td>
  <td style="padding:10px;">
    <input class="button" type="submit" value="Speichern">
  </td>
</tr>
</table>
</form>
</body>
</html>