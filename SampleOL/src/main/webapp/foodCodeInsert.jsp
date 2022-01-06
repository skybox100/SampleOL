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
   ArrayList<Food> food2s = new ArrayList<Food>();
   Code food= new Code("FoodCode","","","식재료명","","");
   Food food2= new Food("","","","","","");
   foods.add(food);
   food2s.add(food2);
   Gson gson = new Gson();

	ArrayList<String> FoodSource = cd.getCodeNameList("FoodSource");



	   
	String total_data;
	String total_data2;
	total_data=gson.toJson(foods);
	total_data2=gson.toJson(food2s);

%>

<html>
<head>
<meta charset="UTF-8">
<title>부식재고 목록 추가</title>
 <link href="css/bootstrap.min.css" rel="stylesheet">
<style>
	body{
	      margin: 10px;
	      width: 420px;
	}
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
     <tr>
      <td class="colt" >조달근거</td>
      <td class="col" >
	  <select id="FoodSource" style="width: 140px;">
						<%for(int i=0; i<FoodSource.size(); i++) {%>
						<option value="<%=cd.getCodeID("FoodSource",FoodSource.get(i))%>"><%=FoodSource.get(i)%></option>
						<%} %>
   </select> 
	</td>
      </tr>
   <tr>
      <td class="colt" >갱신일자</td>
      <td class="col" ><input type="date" id="UpdateDate" ></td>
   </tr>
    <tr>
      <td class="colt" >비고</td>
      <td class="col" >
      <input type="text" id ="remark" >
      </td>
</tr>
</table>
<input type="button" id="edit" value="추가" onclick="pmUpdate()">
<input type="button" id="back" value="닫기" onclick='window.close()'>

<script type="text/javascript">

$(document).ready(function() {

	window.resizeTo(450,600);
// setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출

});

getTimeStamp2();

var data = <%=total_data%>;
var data2 = <%=total_data2%>;

function goBack(){
	location.href = "foodList.jsp";
}


function to_date(date_str)
{
    var yyyyMMdd = String(date_str);
    var sYear = yyyyMMdd.substring(0,4);
    var sMonth = yyyyMMdd.substring(4,6);
    var sDate = yyyyMMdd.substring(6,8);

    return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
}
function to_date2(date_str)
{
    var yyyyMMdd = String(date_str);
    var sYear = yyyyMMdd.substring(0,4);
    var sMonth = yyyyMMdd.substring(5,7);
    var sDate = yyyyMMdd.substring(8,10);

    //alert("sYear :"+sYear +"   sMonth :"+sMonth + "   sDate :"+sDate);
    return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
}

function getTimeStamp() {
    var d = new Date();
    var s =
      leadingZeros(d.getFullYear(), 4) + '-' +
      leadingZeros(d.getMonth() + 1, 2) + '-' +
      leadingZeros(d.getDate(), 2);

    return s;
  }
  
function getTimeStamp2() {
	  var d = new Date();
	  var s =
	    leadingZeros(d.getFullYear(), 4) + '-' +
	    leadingZeros(d.getMonth() + 1, 2) + '-' +
	    leadingZeros(d.getDate(), 2);

	  document.getElementById("UpdateDate").value =s;
	}


function leadingZeros(n, digits) {
    var zero = '';
    n = n.toString();

    if (n.length < digits) {
      for (i = 0; i < digits - n.length; i++)
        zero += '0';
    }
    return zero + n;
  }


function pmUpdate(){
	if(confirm("식재료정보를 목록에 추가하시겠습니까?")){
		//data[0].CodeType="FoodCode";
		//data[0].CodeTypeName="식재료명";
		data[0].CodeID=$('#FoodCode').val();
		data2[0].foodCode=$('#FoodCode').val();
		data[0].CodeName=$('#Food').val();
		data2[0].foodName=$('#Food').val();
		data[0].Remark=$('#Unit').val();
		data2[0].unit=$('#Unit').val();
		data2[0].updateDate=$('#UpdateDate').val();
		data2[0].remark=$('#remark').val();
		data2[0].foodSource=$('#FoodSource').val();
	
		if($('#FoodCode').val() == ""){
			alert("식재료명을 입력하십시오");
			return false;
		}
		if($('#FoodCode').val() == ""){
			alert("재고번호를 입력하십시오");
			return false;
		}
		if($('#Unit').val() == ""){
			alert("단위를 입력하십시오");
			return false;
		}



	
	$.ajax({
		url: 'http://110.10.130.51:5002/Food/FoodManagement/FoodManagementNewSave',
		contentType: "application/json; charset=utf-8",
		method: 'POST',
		data: JSON.stringify(data2[0]),
		dataType: "json",
		accept: "application/json",
		success: function(response) {
			// success handle
				console.log(JSON.stringify(response));
				console.log(JSON.stringify(data));
				alert(data2[0].foodName+"가(이) 추가되었습니다.");

				window.close();

			},
		error: function(response) {
				alert("실패했습니다.");
				console.log(JSON.stringify(data));
				console.log(JSON.stringify(response));
				//window.close();

			}	
	});



	}
}





</script>
</body>
</html>