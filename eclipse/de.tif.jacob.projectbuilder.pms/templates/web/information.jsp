<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
 	<link type="text/css" rel="stylesheet" href="css/main.css" />
</head>

<style>
body
{
 margin:30px;
}
</style>
<body >
<h1>A <i>Open-jACOB</i> BluePrint Application</h1>
<h2>Problem</h2>
<span style="font-size:14px">
Now, more than ever, enterprise applications need to support multiple types of users with multiple types of interfaces. For example, 
an online store may require an HTML front for Web customers, a WML front for wireless customers, a JavaTM (JFC) / Swing interface 
for administrators, and an XML-based Web service for suppliers.
</span>

<h2>Forces</h2>
<ul  style="font-size:14px">
	<li>The same enterprise data needs to be accessed when presented in different views: e.g. HTML, WML, JFC/Swing, XML</li>
  <li>The same enterprise data needs to be updated through different interactions: e.g. link selections on an HTML page or WML card, 
      button clicks on a JFC/Swing GUI, SOAP messages written in XML</li>
  <li>Supporting multiple types of views and interactions should not impact the components that provide the core functionality of 
      the enterprise application</li>
</ul>

<h2>jACOB Solution</h2>
<span style="font-size:14px">
By applying the Model-View-Controller (MVC) architecture to a JavaTM 2 Platform, Enterprise Edition (J2EETM) application, 
you separate core business model functionality from the presentation and control logic that uses this functionality. 
Such separation allows multiple views to share the same enterprise data model and business logic, which makes supporting multiple 
clients easier to implement, test, and maintain. 
</span>

<h2>Example Application Structure</h2>
<img src="./images/overview.png">
</body>
</html>
