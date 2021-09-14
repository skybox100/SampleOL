<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String search_check = request.getParameter("search_check");
	String search_this = request.getParameter("search_this");
	
%>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<script>

var search_check = "<%=search_check%>";
var search_this = "<%=search_this%>";

console.log(search_check);
console.log(search_this);

</script>
</body>
</html>