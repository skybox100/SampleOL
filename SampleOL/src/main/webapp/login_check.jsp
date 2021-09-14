<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
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
    </head>
<body>
<%
	String user_id = "";
	String user_pw = "";
	String userLevel = "";
	if(request.getParameter("user_id") == ""){  %>
  	   <script>
	 alert("아이디를 입력하세요");
	 document.location.href='./login.jsp'
	 </script>
    <% }
else if(request.getParameter("user_pw") == ""){ %>
     <script>
	 alert("비밀번호를 입력하세요");
	 document.location.href='./login.jsp'
	 </script>
    <% }
else{
	user_id = request.getParameter("user_id"); //ID값 가져옴
	user_pw = request.getParameter("user_pw"); //PW값 가져옴
    //여기서 부터 DB 연결 코드
    DBConnection cd = new DBConnection();

	String check=cd.loginCheck(user_id, user_pw);
	if(check != "false"){ //ID,PW가 DB에 존재하는 경우 게시판으로 이동하는 코드 
    session.setAttribute("user_id", check); //DB값을 세션에 넣음
    %> 
 			 <script>
  			 document.location.href='./board_list.jsp'
 			 </script> <%

		}   else  { //ID,PW가 일치하지 않는 경우
        %>
     <script>
	 alert("ID 또는 PW를 잘못 입력했습니다.");
	 document.location.href='./login.jsp'
	 </script>
    <%    }
   } %>
</html>