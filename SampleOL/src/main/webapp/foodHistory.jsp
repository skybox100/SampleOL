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
<%@ page import="java.text.*"%>
<%

  

   DBConnection  cd = new DBConnection();
   ArrayList<Food> Food = new ArrayList<Food>();

   Food = cd.getFoodHistoryList();
   DecimalFormat df = new DecimalFormat("###,###");



%>

<html>
<head>
<meta charset="UTF-8">
<title>부식재고 기록</title>
 <link href="css/bootstrap.min.css" rel="stylesheet">
<style>
	body{
	      margin: 10px;
	      width: 710px;
	      
	}
    .table {
      width: 100%;
      font-size: 16px;
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
      white-space:nowrap;
      padding: 5px;
      border: 1px solid #ddd;
      text-align: center;
      vertical-align: middle;
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

   .col{
      font-weight: 550;
      font-size:18px; 
      width:130px;
      vertical-align: middle;
      
   }

   .colt{
      font-weight: 100;
      font-size:20px;
      color:white;
     background: #416def;
      width:130px;
     vertical-align: middle;
     text-align:center;
     
   }

   td input{
	width:155px;
   height : 32px;
   font-weight: 500;
   font-size:18px;
   }

   div{
       position:relative;
   }
   div span.title{
       display:block;
         text-align: center;  
         font-size:32px; 
       font-weight:700;
       white-space: nowrap;
   }
   span.left{position:absolute;top:0;left:0;}
   span.right{position:absolute;top:0;right:0;}
  

 span input{
    height : 22px;
   font-size:18px; 
   padding: 5px;
   text-align: center;  
   width: 200px;
   }   
   span select{
   
   height : 32px;
   font-size:18px; 
   padding: 5px;
   }
   
   td select{
    font-weight: 500;
    font-size:16px; 
   }
   


}


  </style>
  
</head>
<script src="js/jquery-3.6.0.min.js"></script>
<script src="js/aes.js"></script>

<body>
<div>
<span class="title">부식재고 최근기록</span>

</div>
<table class="table" style="white-space: nowrap;">
<caption>조회 목록</caption> 
		<tr>
		<th>NO</th>
		<th>갱신일</th>
		<th>소속</th>
        <th>창고명</th>
		<th>음식명</th>
		<th>재고량(단위)</th>
		<th>유통기한</th>
		<th>유형</th>
	</tr>
	<%for(int i=0; i<Food.size(); i++){ %>
	<tr>
		<td><%=i+1 %></td>
		<td><%=Food.get(i).getInOutDate() %></td>
		<td><%=Food.get(i).getRegimentName() %></td>
		<td><%=Food.get(i).getStorehouseName() %></td>
		<td><%=Food.get(i).getFoodName() %></td>
		<td><%=df.format(Food.get(i).getCurrentQuantity()) %>&nbsp;<%=Food.get(i).getUnit() %></td>
		<td><%=Food.get(i).getExpirationDate() %></td>
		<td><%=Food.get(i).getInOutName() %></td>
	</tr>
	
	<%} %>
</table>
<script type="text/javascript">


$(document).ready(function() {

	window.resizeTo(760,800);
	//setTimeout(location.reload(),1000);

});

</script>
</body>
</html>