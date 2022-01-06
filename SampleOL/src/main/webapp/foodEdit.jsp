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

  
   //if(request.getParameter("serviceNumber") != null)
     // sn=request.getParameter("serviceNumber");


	
	String reg=request.getParameter("Regiment");	   
	String sh=request.getParameter("Storehouse");	   
	String fc=request.getParameter("FoodCode");	   
	String ed=request.getParameter("ExpirationDate");	   
	String qr=request.getParameter("QRcodeIdx");	   
	   //if(request.getParameter("serviceNumber") != null)
	     // sn=request.getParameter("serviceNumber");
	

		
	DecimalFormat df = new DecimalFormat("###-####-####");

	



	
   
   int num2;
   DBConnection  cd = new DBConnection();
   ArrayList<Food> foods = new ArrayList<Food>();

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
	ArrayList<String> Storehouse = cd.getFoodStore(reg);
	ArrayList<Food> Food = cd.getFoodCodeList2();
	ArrayList<String> Unit = cd.getCodeRemarkList("FoodCode");

	  
	foods = cd.getFoodinfo(reg,sh,fc,ed,qr);
	   
	String total_data;
	total_data=gson.toJson(foods);

%>

<html>
<head>
<meta charset="UTF-8">
<title>부식재고 정보 수정</title>
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
<span class="title">부식재고 정보 수정</span>

</div>
<table class="table" style="white-space: nowrap;">
<caption>조회 목록</caption> 
	<tr>
         <td class="colt" >소속</td>
      <td class="col" >
      		<select id="reg" name ="reg" style="width: 140px;" disabled>
						<%for(int i=0; i<PersonnelReg.size(); i++) {%>
						<option value="<%=cd.getCodeID("Regiment",PersonnelReg.get(i))%>"><%=PersonnelReg.get(i)%></option>
						<%} %>
			</select></td>
  </tr>
   <tr>
      <td class="colt" >창고명</td>
      <td class="col" >
  <select id="Storehouse" style="width: 140px;" disabled>
						<%for(int i=0; i<Storehouse.size(); i++) {%>
						<option value='<%=cd.getCodeID("Storehouse",Storehouse.get(i))%>'><%=Storehouse.get(i)%></option>
						<%} %>
   </select> 
   </tr>

   <tr>
      <td class="colt" >식재료명</td>
      <td class="col" >
        <select id="Food" style="width: 140px;" disabled>
						<%for(int i=0; i<Food.size(); i++) {%>
						<option value='<%=Food.get(i).getFoodCode()%>'><%=Food.get(i).getFoodName()%></option>
						<%} %>
   		</select> 
        <select id="Unit" style="width: 50px;" disabled>
						<%for(int i=0; i<Food.size(); i++) {%>
						<option value='<%=Food.get(i).getFoodCode()%>'><%=Food.get(i).getUnit()%></option>
						<%} %>
   		</select>  
   </tr>
       <tr>
      <td class="colt" >재고수량</td>
      <td class="col" >
	  <input type="number" id ="CurrentQuantity" >
       <input type="hidden" id="qRcodeIdx" value=<%=foods.get(0).getqRcodeIdx()%> >
   </tr>
   <tr>
      <td class="colt" >입고일자</td>
      <td class="col" ><input type="date" id="StoreDate" value="<%=foods.get(0).getStoreDate()%>"  ></td>
   </tr>
   <tr>
      <td class="colt" >유통기한</td>
      <td class="col" ><input type="date" id="ExpirationDate" value="<%=foods.get(0).getExpirationDate()%>" disabled></td>
   </tr>
 

</table>
<input type="button" id="edit" value="수정" onclick="pmUpdate()">
<input type="button" id="back" value="닫기" onclick='window.close()'>

<script type="text/javascript">


$(document).ready(function() {
	
 	$('#reg').val('<%=foods.get(0).getRegiment() %>').prop("selected", true);
	regSelectChange('<%=foods.get(0).getRegiment() %>');
	$('#Storehouse').val('<%=foods.get(0).getStorehouse()%>').prop("selected", true);	
	$('#Food').val('<%=foods.get(0).getFoodCode()%>').prop("selected", true);	
	$('#CurrentQuantity').val(<%=foods.get(0).getCurrentQuantity()%>);	


	  
	  $('#reg').on('change', function() {
			regSelectChange($("#reg").val());
		 });

 // setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출
		window.resizeTo(450,600);
});


	var data = <%=total_data%>;
	

	






   
function regSelectChange(e) {
	
	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;

	var rcp0 = <%=rcp0%>; var rcp1 = <%=rcp1%>;  
	var rcp2 = <%=rcp2%>; var rcp3 = <%=rcp3%>;  
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
	}

	 $("#Storehouse").children('option').remove();


	for (x in d) {
		var opt = document.createElement("option");
		opt.value = d2[x];
		opt.innerHTML = d[x];
		target.appendChild(opt);
	}
	
	
}






function pmUpdate(){
	if(confirm("음식정보를 수정하시겠습니까?")){
		data[0].currentQuantity=$('#CurrentQuantity').val();
		data[0].qRcodeIdx=$('#qRcodeIdx').val();
		data[0].storeDate=$('#StoreDate').val();
		//data[0].expirationDate=$('#ExpirationDate').val();
	$.ajax({
		url: 'http://110.10.130.51:5002/Food/FoodInventory/FoodInventorySave',
		contentType: "application/json; charset=utf-8",
		method: 'POST',
		data: JSON.stringify(data[0]),
		dataType: "json",
		accept: "application/json",
		success: function(response) {
			// success handle
				alert("수정을 성공했습니다.");
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