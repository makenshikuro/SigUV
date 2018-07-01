<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Inicio Usuario</h1>

	<%
 String usuario = request.getUserPrincipal().getName();
 out.println("<h2>Bienvenido(a) : " + usuario + "</h2>");
  
 if(request.isUserInRole("ADMIN")){
 out.println("<a href='../admin/'>Administrador</a>");
 }
if(request.isUserInRole("EDITOR")){
 out.println("<a href='../editor/'>Editor</a>");
 }
 
%>

</body>
</html>