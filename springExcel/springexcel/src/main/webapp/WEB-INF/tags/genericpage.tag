<%@tag description="Plantilla de pagina" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<link rel="icon" href="/bitmaps/favicon.ico">
		<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
		<link rel="stylesheet" href="/css/style.css">
		<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
		<script
			  src="https://code.jquery.com/jquery-3.4.1.min.js"
			  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
			  crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.bundle.min.js" integrity="sha384-6khuMg9gaYr5AxOqhkVIODVIvm9ynTT5J4V1cfthmT+emCG6yVmEZsRHdxlotUnm" crossorigin="anonymous"></script>
		<script type="text/javascript" src="/js/app.js" defer></script>
		<title>Excel Práctica</title>
		<script type="text/javascript" defer>
			<jsp:invoke fragment="javascript"/>
		</script>
	</head>
	<body>
		<div id="pageheader">
			
			<jsp:invoke fragment="pageHeader" />
		</div>
		<div id="body">
			<jsp:doBody/>
		</div>
		<div id="pageFooter">
			<jsp:invoke fragment="footer"/>
		</div>
	</body>
</html>
