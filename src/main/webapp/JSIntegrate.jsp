<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>iPlant Tool Integration</title>

<link type="text/css" rel="stylesheet" href="gxt/css/gxt-all.css">
<link type="text/css" rel="stylesheet" href="gxt/css/gxt-gray.css">
<link type="text/css" rel="stylesheet" href="JSIntegrate.css">

<script type="text/javascript" language="javascript"
	src="jsintegrate/jsintegrate.nocache.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/json2.js"></script>
</head>

<body>

	<!-- OPTIONAL: include this if you want history support -->
	<iframe src="javascript:''" id="__gwt_historyFrame"
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>

</body>
</html>
