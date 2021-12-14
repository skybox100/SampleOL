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


   
   int num2;
   DBConnection  cd = new DBConnection();
   ArrayList<Code> foods = new ArrayList<Code>();
   Code food= new Code("FoodCode","","","식재료명","","");
   foods.add(food);
   
   Gson gson = new Gson();




	   
	String total_data;
	total_data=gson.toJson(foods);

%>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <link href="css/bootstrap.min.css" rel="stylesheet">
<style>

    .table {
      font-size: 1.1rem;
      border-collapse: collapse;
      border-top: 2px solid #19317f;
      height:200px;
      width: 400px;
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
<span class="title">부식재고 목록 추가</span>

</div>
<table class="table" style="white-space: nowrap;">
<caption>조회 목록</caption> 
    <tr>
      <td class="colt" >식재료명</td>
      <td class="col" >
	  <input type="text" id ="Food" >
   </tr>
   <tr>
      <td class="colt" >재고번호</td>
      <td class="col" >
	  <input type="text" id ="FoodCode" >
   </tr>
    <tr>
      <td class="colt" >단위</td>
      <td class="col" >
	  <input type="text" id ="Unit" >
   </tr>
 
</table>
<input type="button" id="edit" value="추가" onclick="pmUpdate()">
<input type="button" id="back" value="닫기" onclick='window.close()'>

<script type="text/javascript">

var data = <%=total_data%>;

function goBack(){
	location.href = "foodList.jsp";
}



function pmUpdate(){
	if(confirm("식재료정보를 목록에 추가하시겠습니까?")){
		//data[0].CodeType="FoodCode";
		//data[0].CodeTypeName="식재료명";
		data[0].CodeID=$('#FoodCode').val();
		data[0].CodeName=$('#Food').val();
		data[0].Remark=$('#Unit').val();
	
		if(data[0].CodeID == ""){
			alert("식재료코드를 입력하십시오");
			return false;
		}
		if(data[0].CodeName == ""){
			alert("식재료명을 입력하십시오");
			return false;
		}
		if(data[0].Remark == ""){
			alert("단위를 입력하십시오");
			return false;
		}


	$.ajax({
		url: 'http://110.10.130.51:5002/common/Code/CodeNewSave',
		contentType: "application/json; charset=utf-8",
		method: 'POST',
		data: JSON.stringify(data[0]),
		dataType: "json",
		accept: "application/json",
		success: function(response) {
			// success handle
				alert(data[0].CodeName+"가(이) 추가되었습니다.");
				console.log(JSON.stringify(response));
				console.log(JSON.stringify(data));
				window.close();
			},
		error: function(response) {
				alert("실패했습니다.");
				console.log(JSON.stringify(data));
				console.log(JSON.stringify(response));

			}	
	});
	}
}





</script>
</body>
</html>