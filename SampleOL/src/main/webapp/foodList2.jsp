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
   int num=0;
   //String sn="";
   if(request.getParameter("num") != null)
      num=Integer.parseInt(request.getParameter("num"));
   
   //if(request.getParameter("serviceNumber") != null)
     // sn=request.getParameter("serviceNumber");
	String reg="소속:전체";
	String regp="소속:전체";
	String sh="식당명:전체";
	String shp="식당명:전체";
	String fd="식재료명:전체";
	String fdp="식재료명:전체";
	String od="전체";
	String odp="전체";
	String sc="중지";
	String sc2="시작";

	DecimalFormat df = new DecimalFormat("###,###");

	
   if(request.getParameter("reg") != null){
		reg = request.getParameter("reg");
		regp = request.getParameter("reg");
   }
   if(request.getParameter("Storehouse") != null){
		sh = request.getParameter("Storehouse");
		shp = request.getParameter("Storehouse");
	   
   }
   
   if(request.getParameter("food") != null){
		fd = request.getParameter("food");
		fdp = request.getParameter("food");
   }

   if(request.getParameter("order") != null){
		od = request.getParameter("order");
		odp = request.getParameter("order");
   }
   if(request.getParameter("sc") != null){
	   sc = request.getParameter("sc");
   }
   
   if(sc.equals("시작"))sc2="중지";
   else if(sc.equals("중지"))sc2="시작";
   
   int num2;
   DBConnection cd = new DBConnection();
   ArrayList<Food> foods = new ArrayList<Food>();

   Gson gson = new Gson();
   
	ArrayList<String> mobileStatusReg = cd.getFoodReg();

	ArrayList<String> foodList = cd.getFoodIndex(reg,sh);	

	ArrayList<String> Storehouse = cd.getFoodStore(reg);


	
	String fc0; String fc1; String fc2; String fc3; String fc4;



	if(reg.equals("소속:전체") && sh.equals("식당명:전체")){
	} else if(sh.equals("식당명:전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else if(reg.equals("소속:전체")){
		sh = cd.getCodeID("Storehouse", sh);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		sh = cd.getCodeID("Storehouse", sh);
	}
   
   foods = cd.getFoodList(reg, sh,fd,od,"");
   
   int cnt = foods.size();
   
   if(num == 0 && cnt>15){
      num2=15;
   }else if(num+15<=cnt){
      num2=num+15;
   }else{
      num2=cnt;
   }

   String total_data="";
   total_data=gson.toJson(foods);

%>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>

    .table {
      table-layout:fixed;
      width: 100%;
      font-size: 1.1rem;
      border-collapse: collapse;
      border-top: 2px solid #19317f;
      
    }  
    .table th {
      color: #168;
      background: #f0f6f9;
      text-align: center;
    }
    .table th, .table td {
      text-overflow:ellipsis; overflow:hidden; white-space:nowrap;
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

   .col{
      font-weight: 550;
      font-size:1.2rem; 
      
   }

   .colt{
      font-weight: 400;
      font-size:1.3rem;
      color:white;
     background: #416def;
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
  </style> 
</head>
</head>
<script src="js/jquery-3.6.0.min.js"></script>
<body>
<div style="white-space: nowrap;min-width: 1400px;">
<span class="left"><input type="text" id="now" readonly>
<select id="order">
						<option selected>전체</option>
						<option>재고번호</option>
						<option>식재료명</option>
						<option>입고일자(내)</option>
						<option>입고일자(올)</option>
						<option>유통기한(내)</option>
						<option>유통기한(올)</option>
</select>
<button id="sc" value='<%=sc%>' style="height:36px;width:70px;padding: 5px;"><%=sc2%></button>
</span>
<span class="title">부식창고 현황판</span>
<span class="right">
<font size=4.5>총 개수: <%=cnt %>&nbsp;&nbsp;</font>

  <select id="reg" name ="reg">
						<option>소속:전체</option>
						<%for(int i=0; i<mobileStatusReg.size(); i++) {%>
						<option value="<%=mobileStatusReg.get(i)%>"><%=mobileStatusReg.get(i)%></option>
						<%} %>
	</select>
	
  <select id="Storehouse" style="width: 140px;">
						<%for(int i=0; i<Storehouse.size(); i++) {%>
						<option value="<%=Storehouse.get(i)%>"><%=Storehouse.get(i)%></option>
						<%} %>
   </select>   
    <select id="foodidx" style="width: 160px;">
	<%for(int i=0; i<foodList.size(); i++) {%>
	<option value="<%=foodList.get(i)%>"><%=foodList.get(i)%></option>
	<%} %>
   </select>   
</span>
</div>
<table class="table" style="white-space: nowrap;min-width: 1400px;">
<caption>조회 목록</caption>
   <tr style="background:green;">
      <td class="colt" style="text-align:center;width:4vw;">NO</td>
      <td class="colt" style="text-align:center;width:8vw;">소속</td>
      <td class="colt" style="text-align:center;width:10vw;">창고명</td>
      <td class="colt" style="text-align:center;width:10vw;">재고번호</td>
      <td class="colt" style="text-align:center;width:20vw;">식재료명</td>
      <td class="colt" style="text-align:center;width:11vw;">재고량(단위)</td>
      <td class="colt" style="text-align:center;width:10vw;">입고일자</td>
      <td class="colt" style="text-align:center;width:10vw;">유통기한</td>
      
   </tr>
   
   <% 
      for(int i=num; i<num2; i++){ 
   %>

   
   <tr id="tr<%=i %>" >
      <td class="col" ><%=i+1 %></td>
      <td class="col" style=" text-align:center;"><%=foods.get(i).getRegimentName() %></td>
      <td class="col" style=" text-align:center; "><%=foods.get(i).getStorehouseName() %></td>
      <td class="col" style=" text-align:center; "><%=foods.get(i).getFoodCode() %></td>
      <td class="col" >&nbsp;<%=foods.get(i).getFoodName() %></td>
      <td class="col" style=" text-align:right; "><%=df.format(foods.get(i).getCurrentQuantity()) %>&nbsp;<%=foods.get(i).getUnit() %>&nbsp;&nbsp;</td>
      <td class="col" style=" text-align:center; "><%=foods.get(i).getStoreDate()%></td>
      <td class="col" id="col<%=i %>" style="text-align:center; "><%=foods.get(i).getExpirationDate() %></td>

   </tr>
   <%}   

   %>   
</table>


<script type="text/javascript">

	$('#reg').val('<%=regp%>').prop("selected", true);
	$('#Storehouse').val('<%=shp%>').prop("selected", true);
	$('#foodidx').val('<%=fdp%>').prop("selected", true);	
	$('#order').val('<%=odp%>').prop("selected", true);	
	
	$(document).ready(function() {
	   


   		 $('#reg').on('change', function() {
   		     location.replace("foodList2.jsp?reg="+$('#reg').val()+"&order="+$('#order').val()); 
   		 });
   			 $('#Storehouse').on('change', function() {
     	  	 location.replace("foodList2.jsp?reg="+$('#reg').val()+"&Storehouse="+$('#Storehouse').val()+"&order="+$('#order').val()); 
   		 });
   			 
   			$('#foodidx').on('change', function() {
        	  	 location.replace("foodList2.jsp?reg="+$('#reg').val()+"&Storehouse="+$('#Storehouse').val()+"&food="+$('#foodidx').val()+"&order="+$('#order').val()); 
      		 });
   			$('#order').on('change', function() {
      		     location.replace("foodList2.jsp?reg="+$('#reg').val()+"&Storehouse="+$('#Storehouse').val()+"&food="+$('#foodidx').val()+"&order="+$('#order').val()); 
      		 });
   		 $('#sc').on('click', function() {
			
   			if($('#sc').val() == '시작'){
   				document.getElementById('sc').value='중지';
   				document.getElementById('sc').innerText='시작';

   			}else{
   				document.getElementById('sc').value='시작';
   				document.getElementById('sc').innerText='중지';

   				setTimeout('go_url()',1000);
   			}

   		 });
   			 
   	  getTimeStamp2();
    
	  for(var i=<%=num%>; i<<%=num2%>; i++)
   	  passdatechange(i);
      <%
      if(num2==cnt){
          num2=0;
       }
	  %>
	  if(<%=cnt%> >15  )
	  	setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출
   
	});
	var data = <%=total_data%>;


	setInterval(getTimeStamp2,1000);
	
	
   function getTimeStamp() {
	     var d = new Date();
	     var s =
	       leadingZeros(d.getFullYear(), 4) + '-' +
	       leadingZeros(d.getMonth() + 1, 2) + '-' +
	       leadingZeros(d.getDate(), 2);

	     return s;
	   }

   
   
   
function storeSelectChange(e) {
    location.replace("foodList2.jsp?reg=<%=regp%>&Storehouse="+e); 
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
	 if($('#sc').val() == '시작')
    	 location.replace("foodList2.jsp?reg=<%=regp%>&Storehouse=<%=shp%>&food=<%=fdp%>&sc=시작&num=<%=num2%>"); 

 
 }

 function passdatechange(num)
 {  


      var day2 = document.getElementById('col'+num).innerText;
      var between=between_date(day2,getTimeStamp());
      
      if(between <0){
          document.getElementById('tr'+num).style.background='red';
          document.getElementById('tr'+num).style.color='white';

      }else if(between <=14){
         document.getElementById('tr'+num).style.background='orange';
      }else if(between <= 30){
         document.getElementById('tr'+num).style.background='yellow';
      }
      console.log(between);
      
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

function getTimeStamp2() {
	  var d = new Date();
	  var s =
	    leadingZeros(d.getFullYear(), 4) + '-' +
	    leadingZeros(d.getMonth() + 1, 2) + '-' +
	    leadingZeros(d.getDate(), 2) + ' ' +

	    leadingZeros(d.getHours(), 2) + ':' +
	    leadingZeros(d.getMinutes(), 2) + ':' +
	    leadingZeros(d.getSeconds(), 2);

	  document.getElementById("now").value =s;
	}
	
function foodInsert(){
	location.href = "foodInsert.jsp";
}
	
function deleteFD(num){

	if(confirm(data[num].foodName+"("+data[num].foodCode+")을 정말 삭제하시겠습니까?")){
		
	$.ajax({
		url: 'http://110.10.130.51:5002/Food/FoodInventory/FoodInventoryDelete',
		contentType: "application/json; charset=utf-8",
		method: 'POST',
		data: JSON.stringify(data[num]),
		dataType: "json",
		accept: "application/json",
		success: function(response) {
			// success handle
				console.log(JSON.stringify(response));
				alert("삭제가 성공했습니다.");
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