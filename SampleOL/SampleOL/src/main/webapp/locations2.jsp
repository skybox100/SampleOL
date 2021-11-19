<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.SampleOL.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="net.sf.json.*" %>
<%@ page import="com.google.gson.*" %>
<%@ page import="java.io.*, java.util.*" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String reg = request.getParameter("reg");
	String rc = request.getParameter("regim_company");
	String device = request.getParameter("device");
	
	DBConnection cd = new DBConnection();
	ArrayList<Location> l = new ArrayList<Location>();
	
	Gson gson = new Gson();
	String multi_marker ="";
	
	if(reg.equals("전체") && device.equals("전체")){
		
	} else if(device.equals("전체")){
		if(rc.equals("전체")){
			reg = cd.getCodeID("Regiment", reg);	
		} else {
			reg = cd.getCodeID("Regiment", reg);
			rc = cd.getCodeID("RegimCompany", rc);
		}
	} else if(reg.equals("전체")) {
		device = cd.getCodeID("IsDevice", device);
	} else{
		if(rc.equals("전체")){
			reg = cd.getCodeID("Regiment", reg);
			device = cd.getCodeID("IsDevice", device);
		}else{
			reg = cd.getCodeID("Regiment", reg);
			rc = cd.getCodeID("RegimCompany", rc);
			device = cd.getCodeID("IsDevice", device);
		}
	}

	l = cd.getLocations(reg, rc, device);
	multi_marker=gson.toJson(l);
	
%>
<script>

	
	var data = <%=multi_marker%>;
	console.log(data);
</script>
</body>
</html>