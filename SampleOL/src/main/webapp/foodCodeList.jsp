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
	Code FoodCode = new Code("foodCode","","","식재료명","","");
   Gson gson = new Gson();

   Food = cd.getFoodCodeList();
   String total_data="";
   String total_data2="";

   total_data=gson.toJson(Food);
   total_data2=gson.toJson(FoodCode);


%>

<html>
<head>
<meta charset="UTF-8">
<title>부식재고 목록</title>
 <link href="css/bootstrap.min.css" rel="stylesheet">
<style>
	body{
	      margin: 10px;
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
   
   button{
   
   }


}


  </style>
  
</head>
<script src="js/jquery-3.6.0.min.js"></script>
<script src="js/aes.js"></script>

<body>
<div>
<span class="title">부식재고 목록</span>

</div>
<table class="table" style="white-space: nowrap;">
<caption>조회 목록</caption> 
	<tr>
		<th>NO</th>
		<th>부식코드</th>
		<th>식재료명</th>
		<th>단위</th>
		<th>갱신일</th>
		<th>수정/삭제</th>
	</tr>
	<%for(int i=0; i<Food.size(); i++){ %>
	<tr>
		<td><%=i+1 %></td>
		<td><%=Food.get(i).getFoodCode() %></td>
		<td><%=Food.get(i).getFoodName() %></td>
		<td><%=Food.get(i).getUnit() %></td>
		<td><%=Food.get(i).getUpdateDate() %></td>
      <td ><input type="button" value="수정" onclick="winPopup('foodCodeEdit.jsp?foodCode=<%=Food.get(i).getFoodCode()%>')"/>&nbsp;/&nbsp;<input type="button" value="삭제" onclick="deleteFD(<%=i %>)"/></td>
	</tr>
	
	<%} %>
</table>
<script type="text/javascript">


$(document).ready(function() {

	window.resizeTo(700,600);

});

var data = <%=total_data%>;
var data2 = <%=total_data%>;


function winPopup(e){
	var popUrl = e;
	var popOption = getPopOptions(550,600,1);
	var win=window.open(popUrl,'popup',popOption);
	win.focus();
}

function winPopup2(e){
	var popUrl = e;
	var popOption = getPopOptions2(550,600);
	var win=window.open(popUrl,e,popOption);
	win.focus();
}

var getPopOptions = function(width, height,num){
  var screenW = screen.availWidth;  // 스크린 가로사이즈
  var screenH = screen.availHeight; // 스크린 세로사이즈
  var popW = width; // 띄울창의 가로사이즈
  var popH = height; // 띄울창의 세로사이즈
  var posL=( screenW-popW ) / 2 + 500;   // 띄울창의 가로 포지션 
  var posT=( screenH-popH ) / 2;   // 띄울창의 세로 포지션 

 return 'width='+ popW +',height='+ popH +',top='+ 30 +',left='+ posL +',status=no,menubar=no,toolbar=no,resizable=0, location=no,scrollbars=yes';
}


function getPopOptions2(width, height) {
  var screenW = screen.availWidth;  // 스크린 가로사이즈
  var screenH = screen.availHeight; // 스크린 세로사이즈
  var popW = width; // 띄울창의 가로사이즈
  var popH = height; // 띄울창의 세로사이즈
  var posL=( screenW-popW ) / 2;   // 띄울창의 가로 포지션 
  var posT=( screenH-popH ) / 2;   // 띄울창의 세로 포지션 

 return 'width='+ popW +',height='+ popH +',top='+ 30 +',left='+ posL +',status=no,menubar=no,toolbar=no,resizable=0, location=no,scrollbars=yes';
}


function deleteFD(num){

	if(confirm(data[num].foodName+"("+data[num].foodCode+")을 정말 삭제하시겠습니까?")){
		
	$.ajax({
		url: 'http://110.10.130.51:5002/Food/FoodManagement/FoodManagementDelete',
		contentType: "application/json; charset=utf-8",
		method: 'POST',
		data: JSON.stringify(data[num]),
		dataType: "json",
		accept: "application/json",
		success: function(response) {
			// success handle
				alert("삭제가 성공했습니다.");
				console.log(JSON.stringify(response));
				location.reload();
			},
		error: function(response) {
				alert("삭제가 실패해습니다.");

				console.log(JSON.stringify(data));
				console.log(JSON.stringify(response));

			}	
	});
		
	}
	return false;
}

</script>
</body>
</html>