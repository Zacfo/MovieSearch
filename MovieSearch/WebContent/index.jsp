<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MovieSearch</title>
</head>
<body>
<%@ page import = "com.mathedia.*" %>

	<h2>Enter two actors:</h2>
	
	    <form action="result.jsp">
            Actor 1: <input type = "text" name = "name1"><br/>
            Actor 2: <input type = "text" name = "name2"><br/><br/><br/>
            <input type="submit"/>
        </form>
</body>
</html>