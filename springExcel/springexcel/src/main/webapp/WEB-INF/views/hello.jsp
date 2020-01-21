<%@page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageHeader">
		<h1>Header</h1>
	</jsp:attribute>
	<jsp:attribute name="pageFooter">
		<h1>Footer</h1>
	</jsp:attribute>
	<jsp:attribute name="javascriptF">
		<script>
			console.log("Testing this shit");
		</script>
	</jsp:attribute>
	<jsp:body>
		<p>footer</title>
	</jsp:body>
</t:genericpage>