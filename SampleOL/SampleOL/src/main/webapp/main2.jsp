<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.SampleOL.*" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%

	DBConnection cd = new DBConnection();
	ArrayList<String> equipIdList = cd.getEquipIdList();
	System.out.println(equipIdList.toString());

%>

<form action="equipLocations.jsp" method="get">
Equip Id: 
<select id="equipId" name="equipId">
<%for(int i=0; i<equipIdList.size(); i++){ %>
    <option value=<%=equipIdList.get(i) %>><%=equipIdList.get(i) %></option>
<%}%>
</select>
<input type="submit" value="전송">


</body>
</html>