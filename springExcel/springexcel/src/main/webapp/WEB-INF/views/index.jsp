<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
    
<t:genericpage>
	<jsp:attribute name="header">
		<h1>Title</h1>
	</jsp:attribute>
	<jsp:body>
		<h1>Spring Boot - Web application</h1>		
		<div class="form" >
			<form action="hello" method="post"  onSubmit="return validate()">
				<table>
					<tr>
						<td>Enter your name</td>
						<td><input id="name" name="name" /></td>
						<td><input type="submit" value="Submit"/></td>
					</tr>
				</table>
			</form>
		</div>
	</jsp:body>
</t:genericpage>