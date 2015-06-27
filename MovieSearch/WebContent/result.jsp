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
<%@ page import = "java.util.List" %>
<%@ page import = "java.util.ArrayList" %>
	<%  String name1 = request.getParameter("name1");
		String name2 = request.getParameter("name2");
		Searchengine se = new Searchengine(name1, name2);
		se.eval();
		List<String> filmlist = se.getMatch();
		for (String s : filmlist) {
			out.println(s + "<br>");	
		}

	%>
		
	
</body>
</html>