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
   ArrayList<Food> foods = new ArrayList<Food>();
   Food food= new Food(null,null,null,null,null,null,null,null,0,null,null,null,0,null);
   foods.add(food);
   boolean flag=false;
   
   Gson gson = new Gson();



	ArrayList<String> rc_0 = cd.getCodeNameList("Storehouse", "RG-280");
	ArrayList<String> rc_1 = cd.getCodeNameList("Storehouse", "RG-281");
	ArrayList<String> rc_2 = cd.getCodeNameList("Storehouse", "RG-282");
	ArrayList<String> rc_3 = cd.getCodeNameList("Storehouse", "RG-283");

	ArrayList<String> rcp_0 = cd.getCodeIDList("Storehouse", "RG-280");
	ArrayList<String> rcp_1 = cd.getCodeIDList("Storehouse", "RG-281");
	ArrayList<String> rcp_2 = cd.getCodeIDList("Storehouse", "RG-282");
	ArrayList<String> rcp_3 = cd.getCodeIDList("Storehouse", "RG-283");

	ArrayList<String> Unit = cd.getCodeRemarkList("FoodCode");

	
	String rc0; String rc1; String rc2; String rc3;
	String rcp0; String rcp1; String rcp2; String rcp3;

	rc0 = gson.toJson(rc_0);
	rc1 = gson.toJson(rc_1); 
	rc2 = gson.toJson(rc_2);
	rc3 = gson.toJson(rc_3);
	
	rcp0 = gson.toJson(rcp_0);
	rcp1 = gson.toJson(rcp_1); 
	rcp2 = gson.toJson(rcp_2);
	rcp3 = gson.toJson(rcp_3);

	ArrayList<String> PersonnelReg = cd.getCodeNameList("Regiment");
	ArrayList<Food> Food = cd.getFoodCodeList2();
	ArrayList<String> FoodSource = cd.getCodeNameList("FoodSource");


	   
	String total_data;
	total_data=gson.toJson(foods);

%>

<html>
<head>
<meta charset="UTF-8">
<title>부식재고 현황 입력</title>
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
      height:300px;
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
<span class="title">부식재고 현황 입력</span>

</div>
<table class="table" style="white-space: nowrap;">
<caption>조회 목록</caption> 
	<tr>
         <td class="colt" >소속</td>
      <td class="col" >
      		<select id="reg" name ="reg" style="width: 140px;">
						<%for(int i=0; i<PersonnelReg.size(); i++) {%>
						<option value="<%=cd.getCodeID("Regiment",PersonnelReg.get(i))%>"><%=PersonnelReg.get(i)%></option>
						<%} %>
			</select></td>
  </tr>
   <tr>
      <td class="colt" >창고명</td>
      <td class="col" >
  <select id="Storehouse" style="width: 140px;">
						<option>전체</option>
   </select> 
   </tr>

   <tr>
      <td class="colt" >식재료명</td>
      <td class="col" >
        <select id="Food" style="width: 140px;">
						<%for(int i=0; i<Food.size(); i++) {%>
						<option value='<%=Food.get(i).getFoodCode()%>'><%=Food.get(i).getFoodName()%></option>
						<%} %>
   		</select> 
        <select id="Unit" style="width: 50px;" disabled="disabled">
						<%for(int i=0; i<Food.size(); i++) {%>
						<option value='<%=Food.get(i).getFoodCode()%>'><%=Food.get(i).getUnit()%></option>
						<%} %>
   		</select> 
   </tr>
   
    <tr>
      <td class="colt" >수량</td>
      <td class="col" >
	  <input type="number" id ="CurrentQuantity" >
   </tr>
   <tr>
      <td class="colt" >입고일자</td>
      <td class="col" ><input type="date" id="StoreDate" ></td>
   </tr>
   <tr>
      <td class="colt" >유통기한</td>
      <td class="col" ><input type="date" id="ExpirationDate" ></td>
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
      <!-- 
    <tr>
      <td class="colt" >QR코드</td>
      <td class="col" >
      	  <input type="number" id ="qRcodeIdx" >
      </td>
     </tr>
       -->
    <tr>
      <td class="colt" >기타사항</td>
      <td class="col" >
      <input type="text" id ="remark" >
      </td>
</tr>
</table>
<input type="button" id="edit" value="추가" onclick="pmUpdate()">
<input type="button" id="back" value="닫기" onclick='window.close()'>

<script type="text/javascript">

regSelectChange("RG-280")


$(document).ready(function() {
	

	  
	  $('#reg').on('change', function() {
			regSelectChange($("#reg").val());
		 });
	  
	  $('#Food').on('change', function() {
			$('#Unit').val($('#Food').val()).prop("selected", true);	
		 });
	  
		window.resizeTo(450,600);

 // setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출

});

	getTimeStamp2();

	
	
	var data = <%=total_data%>;
	
	function onFileSelected(event) {
		  var selectedFile = event.target.files[0];
		  var reader = new FileReader();

		  var imgtag = document.getElementById("picture");
		  imgtag.title = selectedFile.name;

		  reader.onload = function(event) {
		    imgtag.src = event.target.result;
		  	data[0].Picture= (event.target.result).replace('data:image/png;base64,', '').replace('data:image/jpg;base64,','').replace('data:image/jpeg;base64,','');
			console.log(data[0].Picture);
		  };

		  reader.readAsDataURL(selectedFile); 

		  	showSearch('fileAdd');


		}
	
	function aes(e){
		//var key= CryptoJS.enc.Hex.parse('01010101010101010101010101010101');
		//var iv = CryptoJS.enc.Hex.parse('01010101010101010101010101010101');

		var encrypted = master.dbo.pCrypto_enc('normal',e,'');
		return encrypted;
	}




	
	function showSearch(id){
		console.log(id);
		console.log(document.getElementById(id).style.display);
		if(id == "fileAdd"){
			document.getElementById('fileEdit').style.display="block";
			document.getElementById('fileDelete').style.display="block";
			document.getElementById('fileAdd').style.display="none";
		}else if(id == "fileDelete"){
			document.getElementById('fileEdit').style.display="none";
			document.getElementById('fileDelete').style.display="none";
			document.getElementById('fileAdd').style.display="block";
			document.getElementById("picturefile").value=null;
			document.getElementById("picture").src="";
			document.getElementById("picture").title="";

		}
		
		//window.open("popup.jsp","popup","width=400, height=300, left=100, top=50");
		//var phoneNum = prompt("전화번호: ");
	}
   

   
function regSelectChange(e) {
	
	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;
	var rc4 = ['전체'];

	var rcp0 = <%=rcp0%>; var rcp1 = <%=rcp1%>;  
	var rcp2 = <%=rcp2%>; var rcp3 = <%=rcp3%>;  
	var rcp4 = ['전체'];
	var target = document.getElementById("Storehouse");

	if(e == "RG-280") {
		var d = rc0;
		var d2= rcp0;
	}else if(e == "RG-281") {
		var d = rc1;
		var d2= rcp1;
	}else if(e == "RG-282") {
		var d = rc2;
		var d2= rcp2;
	}else if(e == "RG-283") {
		var d = rc3;
		var d2= rcp3;
	}else if(e == "전체"){
		var d = rc4;
		var d2= rcp4;
	}

	 $("#Storehouse").children('option').remove();


	for (x in d) {
		var opt = document.createElement("option");
		opt.value = d2[x];
		opt.innerHTML = d[x];
		target.appendChild(opt);
	}
	
	
}


function regSelectChange(e) {
	
	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;
	var rc4 = ['전체'];

	var rcp0 = <%=rcp0%>; var rcp1 = <%=rcp1%>;  
	var rcp2 = <%=rcp2%>; var rcp3 = <%=rcp3%>;  
	var rcp4 = ['전체'];
	var target = document.getElementById("Storehouse");

	if(e == "RG-280") {
		var d = rc0;
		var d2= rcp0;
	}else if(e == "RG-281") {
		var d = rc1;
		var d2= rcp1;
	}else if(e == "RG-282") {
		var d = rc2;
		var d2= rcp2;
	}else if(e == "RG-283") {
		var d = rc3;
		var d2= rcp3;
	}else if(e == "전체"){
		var d = rc4;
		var d2= rcp4;
	}

	 $("#Storehouse").children('option').remove();


	for (x in d) {
		var opt = document.createElement("option");
		opt.value = d2[x];
		opt.innerHTML = d[x];
		target.appendChild(opt);
	}
	
	
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
 function go_url(){

 
 }



 function between_date(date1, date2)
 {   
     var y1970 = new Date(1970, 0, 1).getTime();
     var time1 = null;
     var time2 = null;

     if(date1.length > 8)
         time1 = to_date2(date1).getTime() - y1970;
     else
         time1 = to_date(date1).getTime() - y1970;
    
     if(date2.length > 8)
         time2 = to_date2(date2).getTime() - y1970;
     else
         time2 = to_date(date2).getTime() - y1970;

     var per_day = 1000 * 60 * 60 * 24;              // 1일 밀리초
 console.log(date1);
     console.log(time1);
     console.log(time2);

     return Math.floor(time1/per_day) - Math.floor(time2/per_day);
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

	  document.getElementById("StoreDate").value =s;
	  document.getElementById("ExpirationDate").value =s;
	}


function goBack(){
	location.href = "foodList.jsp";
}



function pmUpdate(){
	if(confirm("음식정보를 추가하시겠습니까?")){
		data[0].regiment=$('#reg').val();
		data[0].storehouse=$('#Storehouse').val();
		data[0].foodName=document.getElementById("Food").options[document.getElementById("Food").selectedIndex].text;
		data[0].foodCode=$('#Food').val();
		data[0].unit=document.getElementById("Unit").options[document.getElementById("Unit").selectedIndex].text;
		data[0].storeDate=$('#StoreDate').val();
		data[0].expirationDate=$('#ExpirationDate').val();
		data[0].foodSource=$('#FoodSource').val();
		data[0].qRcodeIdx=<%=cd.TimeTick()%>;
		data[0].remark=$('#remark').val();
		data[0].currentQuantity=$('#CurrentQuantity').val();
/*
		if(data[0].storeDate == ""){
			alert("입고일자 날짜를 설정하십시오.");
			return false;
		}
		if(data[0].expirationDate  == ""){
			alert("유통기한 날짜를 설정하십시오.");
			return false;
		}
		if(data[0].storeDate > data[0].expirationDate){
			alert("유통기한이 입고일자보다 늦습니다.");
			return false;
		}
*/
	$.ajax({
		url: 'http://110.10.130.51:5002/Food/FoodInventory/FoodInventoryNewSave',
		contentType: "application/json; charset=utf-8",
		method: 'POST',
		data: JSON.stringify(data[0]),
		dataType: "json",
		accept: "application/json",
		success: function(response) {
			// success handle
				alert(data[0].foodName+"가(이) 추가되었습니다.");
				console.log(JSON.stringify(response));
				console.log(JSON.stringify(data));
				opener.location.reload();
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