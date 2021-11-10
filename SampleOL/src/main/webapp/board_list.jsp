<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%@ page import="com.SampleOL.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="net.sf.json.*" %> 
<%@ page import="com.google.gson.*" %>
<%@ page import="java.io.*, java.util.*" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
   <%
   String user_id = (String)session.getAttribute("user_id");
   if(user_id == null){
	   response.sendRedirect("./login.jsp");
   }  	%>   
    <script  type="text/javascript">
     function logout(){
            window.location.href = './login.jsp';
            }
     </script>
</head>
<body>

<table >
    <tr>
        <td align="center">번호</td>
        <td align="center">제목</td>
    </tr>
	<tr>
		<td>1</td>
		<td><a href="foodList.jsp?serviceNumber=<%=user_id%>">재고관리</a></td>
	</tr>
</table>
<a href="./login.jsp" style="margin-left:3%">로그아웃</a>

</body>
</html>