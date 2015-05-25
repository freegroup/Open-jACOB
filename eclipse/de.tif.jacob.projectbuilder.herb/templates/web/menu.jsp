<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="author" content="Andreas Herz">
 	<link type="text/css" rel="stylesheet" href="css/tab.css" />
	<script>
		var oldTab=null;
		function switchTab(obj)
		{
			if(oldTab!=null)
				oldTab.className="";
			obj.className='active';
			oldTab=obj;
		}
		</script>
</head>

<body>
	<div  id="container">
		<ul id="tabnav">
			<li><a onClick="switchTab(this)" target=main href="herb_index.jsp">Herb</a></li>
			<li><a onClick="switchTab(this)" target=main href="information.jsp">Information</a></li>
		</ul>
	</div>
	<div style="z-index:5;color: #9EAD6C;text-align:right;font-size:20px;width:100%;position:absolute;top:0px;left:0px;">
	 The Herb Database&nbsp;
	</div>	
</body>
</html>
