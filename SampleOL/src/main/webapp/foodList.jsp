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
   int num=0;
   //String sn="";
   if(request.getParameter("num") != null)
      num=Integer.parseInt(request.getParameter("num"));
   
   //if(request.getParameter("serviceNumber") != null)
     // sn=request.getParameter("serviceNumber");
	String reg="전체";
	String regp="전체";
	String sh="전체";
	String shp="전체";
	
   if(request.getParameter("reg") != null){
		reg = request.getParameter("reg");
		regp = request.getParameter("reg");
   }
   if(request.getParameter("Storehouse") != null){
		sh = request.getParameter("Storehouse");
		shp = request.getParameter("Storehouse");
	   
   }

	
   
   int num2;
   DBConnection cd = new DBConnection();
   ArrayList<Food> foods = new ArrayList<Food>();

   Gson gson = new Gson();
   
	ArrayList<String> mobileStatusReg = cd.getMobileStatusReg();

	ArrayList<String> foodList = cd.getCodeNameList("Storehouse");	

	ArrayList<String> fc_0 = cd.getFoodStore("28여단");
	ArrayList<String> fc_1 = cd.getFoodStore("28-1대대");
	ArrayList<String> fc_2 = cd.getFoodStore("28-2대대");
	ArrayList<String> fc_3 = cd.getFoodStore("28-3대대");
   
	String fc0; String fc1; String fc2; String fc3;

	fc0 = gson.toJson(fc_0);
	fc1 = gson.toJson(fc_1); 
	fc2 = gson.toJson(fc_2);
	fc3 = gson.toJson(fc_3);
	
	if(reg.equals("전체") && sh.equals("전체")){
		
	} else if(sh.equals("전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		sh = cd.getCodeID("Storehouse", sh);
	}
   
   foods = cd.getFoodList(reg, sh);
   
   int cnt = foods.size();
   
   if(num == 0 && cnt>15){
      num2=15;
   }else if(num+15<=cnt){
      num2=num+15;
   }else{
      num2=cnt;
   }



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
   }
   span.left{position:absolute;top:0;left:0;}
   span.right{position:absolute;top:0;right:0;}
  
  span input{
    height : 32px;
   font-size:18px; 
   padding: 5px;
   }   
   span select{
   height : 32px;
   font-size:18px; 
   padding: 5px;
   }
  </style>
</head>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<body>
<div>
<span class="left"><input type="text" id="now"></span>
<span class="title">부식창고 현황판</span>
<span class="right">
  <select id="reg" name ="reg">
						<option>전체</option>
						<%for(int i=0; i<mobileStatusReg.size(); i++) {%>
						<option value="<%=mobileStatusReg.get(i)%>"><%=mobileStatusReg.get(i)%></option>
						<%} %>
	</select>
	
  <select id="Storehouse" style="width: 120px;">
   </select>   
</span>
</div>
<table class="table" style="white-space: nowrap;">
<caption>조회 목록</caption>
   <tr style="background:green;">
      <td class="colt" style="text-align:center;width:4vw;">NO</td>
      <td class="colt" style="text-align:center;width:8vw;">소속</td>
      <td class="colt" style="text-align:center;width:5vw;">창고명</td>
      <td class="colt" style="text-align:center;width:10vw;">재고번호</td>
      <td class="colt" style="text-align:center;width:20vw;">식재료명</td>
      <td class="colt" style="text-align:center;width:11vw;">현재고량/단위</td>
      <td class="colt" style="text-align:center;width:10vw;">입고일자</td>
      <td class="colt" style="text-align:center;width:10vw;">유통기한</td>
      
   </tr>
   
   <% 
      for(int i=num; i<num2; i++){ 
   %>

   
   <tr id="tr<%=i %>" >
      <td class="col" style="width:4vw;"><%=i+1 %></td>
      <td class="col" style=" text-align:center;width:8vw;"><%=foods.get(i).getRegiment() %></td>
      <td class="col" style=" text-align:center;width:5vw; "><%=foods.get(i).getStorehouse() %></td>
      <td class="col" style=" text-align:center;width:10vw; "><%=foods.get(i).getFoodCode() %></td>
      <td class="col" style="width:20vw;">&nbsp;<%=foods.get(i).getFoodName() %></td>
      <td class="col" style=" text-align:right;width:11vw; "><%=foods.get(i).getCurrentQuantity() %>&nbsp;<%=foods.get(i).getUnit() %>&nbsp;&nbsp;</td>
      <td class="col" style=" text-align:center;width:10vw; "><%=foods.get(i).getStoreDate() %></td>
      <td class="col" id="col<%=i %>" style="width:10vw; text-align:center; "><%=foods.get(i).getExpirationDate() %></td>
   </tr>
   <%}   
      if(num2==cnt){
         num2=0;
      }
   %>   
</table>


<script type="text/javascript">



	$(document).ready(function() {
	   
	 	$('#reg').val('<%=regp%>').prop("selected", true);
			regSelectChange('<%=regp%>');
 	
   		 $('#reg').on('change', function() {
   		     location.href="foodList.jsp?reg="+$('#reg').val()+"&Storehouse=전체"; 
   		 });
   			 $('#Storehouse').on('change', function() {
     	   location.href="foodList.jsp?reg="+$('#reg').val()+"&Storehouse="+$('#Storehouse').val(); 
   		 });

	    document.getElementById("now").value = getTimeStamp2();
     
	  for(var i=<%=num%>; i<<%=num2%>; i++)
         passdatechange(i);
      
	  if(<%=cnt%> >15)
      setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출
   
	});
  
   function getTimeStamp() {
	     var d = new Date();
	     var s =
	       leadingZeros(d.getFullYear(), 4) + '-' +
	       leadingZeros(d.getMonth() + 1, 2) + '-' +
	       leadingZeros(d.getDate(), 2);

	     return s;
	   }

   
   
   
function storeSelectChange(e) {
    location.href="foodList.jsp?reg=<%=regp%>&Storehouse="+e; 
}
   
function regSelectChange(e) {
	
	var fc0 = <%=fc0%>; var fc1 = <%=fc1%>;  
	var fc2 = <%=fc2%>; var fc3 = <%=fc3%>;
	var fc4 = ['전체'];

	var target = document.getElementById("Storehouse");

	if(e == "28여단") var d = fc0;
	else if(e == "28-1대대") var d = fc1;
	else if(e == "28-2대대") var d = fc2;
	else if(e == "28-3대대") var d = fc3;
	else if(e == "전체") var d = fc4;



	for (x in d) {
		var opt = document.createElement("option");
		opt.value = d[x];
		opt.innerHTML = d[x];
		target.appendChild(opt);
	}
	
	

	$('#Storehouse').val('<%=shp%>').prop("selected", true);	
	
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

       location.href="foodList.jsp?reg=<%=regp%>&Storehouse=<%=shp%>&num=<%=num2%>"; 
 
 }

 function passdatechange(num)
 {  


      var day2 = document.getElementById('col'+num).innerText;
      var between=between_date(day2,getTimeStamp());
      
      if(between <0){
         document.getElementById('tr'+num).style.background='red';
         document.getElementById('tr'+num).style.color='white';
      }else if(between <3){
         document.getElementById('tr'+num).style.color='red';
      }else if(between < 10){
         document.getElementById('tr'+num).style.color='orange';
      }
      
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

	  return s;
	}

</script>
</body>
</html>