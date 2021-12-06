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
<%

	String reg = request.getParameter("regp");
	String rc = request.getParameter("rcp");
	
	String regp = request.getParameter("regp");
	String rcp = request.getParameter("rcp");
	
	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	
	Gson gson = new Gson();
	String multi_marker ="";
	
	if(reg.equals("전체") && rc.equals("전체")){
			
	} else if(rc.equals("전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		rc = cd.getCodeID("RegimCompany", rc);
	}
	
	locations = cd.getMobileStatus(reg, rc);
	multi_marker=gson.toJson(locations);
	
	int cnt = locations.size();
		
%>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    .table {
      width: 100%;
      font-size: 11px;
      border-collapse: collapse;
      border-top: 3px solid #168;
    }  
    .table th {
      color: #168;
      background: #f0f6f9;
      text-align: center;
    }
    .table th, .table td {
      padding: 5px;
      border: 1px solid #ddd;
    }
    .table th:first-child, .table td:first-child {
      border-left: 0;
    }
    .table th:last-child, .table td:last-child {
      border-right: 0;
    }
    .table tr td:first-child{
      text-align: center;
    }
    .table caption{caption-side: bottom; display: none;}
	#goback{
		position: absolute;	
			right: 0;
			margin-top : 4px;
			margin-right: 4px;
			height: 30px;
			
	}
  </style>
</head>
<body>

<table class="table">
<caption>조회 목록</caption>
	<tr>
		<td>NO</td>
		<td>소속</td>
		<td>계급</td>
		<td>성명</td>
		<td>전화번호</td>
		<td>비고</td>
	</tr>
	
	<%for(int i=0; i<locations.size(); i++){ %>
	
	<tr>
		<td><%=i+1 %></td>
		<td><%=locations.get(i).getRegimCompanyName() %></td>
		<td><%=locations.get(i).getRankName() %></td>
		<td><%=locations.get(i).getName() %></td>
		<td><a href="personalLocations5.jsp?search_check=phone_num&search_this=<%=locations.get(i).getUserKey() %>">
		<%=locations.get(i).getUserKey() %>
		</a></td>
		<td><%=locations.get(i).getEtc() %></td>
	</tr>
	
	<%} %>
	
</table>

<input type="button" value=" 이전 " id="goback">
<script type="text/javascript">
document.getElementById('goback').onclick = function(){
	window.history.back();
}
</script>
</body>
</html>