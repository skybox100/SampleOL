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


	String reg = request.getParameter("reg");
	String et = request.getParameter("et");
	
	String regp = request.getParameter("reg");
	String etp = request.getParameter("et");
	
	DBConnection cd = new DBConnection();
	ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
	
	Gson gson = new Gson();
	String multi_marker = null;	

	if(reg.equals("전체") && et.equals("전체")){
		
	} else if(et.equals("전체")){
		regp = cd.getCodeName("Regiment", reg);
	} else{
		regp = cd.getCodeName("Regiment", reg);
		etp = cd.getCodeName("EquipType", et);
	}

	equipLocations = cd.getEquipLocations(reg, et);
	multi_marker = gson.toJson(equipLocations);
	
	int cnt = equipLocations.size();
	
	
%>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    .table {
      width: 100%;
      font-size: 10px;
      border-collapse: collapse;
      border-top: 3px solid #168;
      text-align: center;
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
		<td>부대</td>
		<td>장비종류</td>
		<td>위치</td>
		<td>비고</td>
	</tr>
	
	<%for(int i=0; i<equipLocations.size(); i++){ %>
	
	<tr>
		<td><%=i+1 %></td>
		<td><%=equipLocations.get(i).getRegiment() %></td>
		<td><%=equipLocations.get(i).getEquipType() %></td>
		<td><%=equipLocations.get(i).getEquipLocation() %></td>
		<td></td>
	</tr>
	
	<%} %>
	
</table>

<input type="button" value=" 이전 " id="goback">
<script type="text/javascript">
console.log(<%=multi_marker%>);

document.getElementById('goback').onclick = function(){
	window.history.back();
}
</script>
</body>
</html>