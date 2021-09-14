<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.SampleOL.*" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body onload=="window.resizeTo(400,200)">
	
		<form name="searchName">
		전화번호 : <input type="number" name="phoneNum" size="10" placeholder="전화번호 입력" >
		<input type="submit" value="검색" onClick="return searchName();">
		</form> 
		이  름 : 
		<form action="personalLocations2.jsp" method="get">
		<input type="submit" value="전송">
	</form>

<script>
function searchName(){
	var p = document.searchName;
	console.log(p);
	<%
	DBConnection cd = new DBConnection();
	
	
	%>
	
	
}
</script>

</body>
</html>