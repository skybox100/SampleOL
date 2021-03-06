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
	if(request.getParameter("num") != null)
    num=Integer.parseInt(request.getParameter("num"));
   //String sn="";
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
	String search="";

	int pageNum=1;
	int endPage;
	int startPage;
	int pageNum_list=10;
	int pagetot=15;
	DecimalFormat df = new DecimalFormat("###,###");
	   boolean flag=false;

	
   if(request.getParameter("page") != null)
		pageNum=Integer.parseInt(request.getParameter("page"));
		   
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
   if(request.getParameter("search") != null){
	 	search = request.getParameter("search");
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
   
   foods = cd.getFoodList(reg, sh,fd,od,search);
   
   int cnt = foods.size();
   
   if(cnt<=pagetot){
	   startPage=1;
	   endPage= 1; 
  		num=0;
  	flag=cnt/pagetot <=pagetot ? false:true;

   	}else if(cnt-((pageNum-1)-(pageNum-1)%pageNum_list)*pagetot <= pageNum_list*pagetot){
   		startPage=(pageNum/pageNum_list)*pageNum_list+1;
   		endPage=(cnt/pagetot)+1;
   		num= (pageNum-1)*pagetot;
  		if(cnt%pagetot == 0) endPage=(cnt/pagetot);

	}else{
   		startPage=((pageNum-1)/pageNum_list)*pageNum_list+1;
		endPage=startPage+pageNum_list-1;
   		num= (pageNum-1)*pagetot;
  		flag=true;

    }
	
   if(num == 0 && cnt>pagetot){
	      num2=pagetot;
	   }else if(num+pagetot<=cnt){
	      num2=num+pagetot;
	   }else{
	      num2=cnt;
	   }

   int totalPage= cnt%pagetot==0 && cnt!=0? cnt/pagetot :cnt/pagetot+1;

   String total_data="";
   total_data=gson.toJson(foods);

%>

<html>
<head>
<meta charset="UTF-8">
<title>부식창고 현황판</title>
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

   form{   
   top:0;
   height : 0;
   font-size:18px; 
   padding-left: 440px;
   }
   form input{
   height : 30px;
   }
  </style> 
</head>
</head>
<script src="js/jquery-3.6.0.min.js"></script>
<body>
<div style="white-space: nowrap;min-width: 1800px;">
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
<form name="search_form" method="get">
	<input type="text" id="search" name="search" value="<%=search%>">
	<input type="hidden" name="order" value="<%=od%>">
	<input type="hidden" name="Storehouse" value="<%=shp%>">
	<input type="hidden" name="reg" value="<%=regp%>">
	<input type="hidden" name="food" value="<%=fdp%>">
	<input type="submit" id="submit" value="검색">
</form>
<span class="title">부식창고 현황판</span>
<span class="right">
<font size=4.5>총 개수: <%=cnt %>&nbsp;&nbsp;</font>
<button id="new" onclick="winPopup2('foodHistory.jsp')" style="height:36px;padding: 5px;">기록</button>
<button id="new" onclick="winPopup2('foodInsert.jsp')" style="height:36px;padding: 5px;">신규</button>
<button id="new" onclick="winPopup('foodCodeList.jsp')" style="height:36px;padding: 5px;">목록</button>
<button id="new" onclick="winPopup2('foodCodeInsert.jsp')" style="height:36px;padding: 5px;">식재료추가</button>

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
<table class="table" style="white-space: nowrap;min-width: 1800px;">
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
       <td class="colt" style="text-align:center;width:6vw;">수정/삭제</td>
      
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
      <td class="col" style=" text-align:center;"><input type="button" value="수정" onclick="winPopup('foodEdit.jsp?Regiment=<%=foods.get(i).getRegiment()%>&Storehouse=<%=foods.get(i).getStorehouse() %>&FoodCode=<%=foods.get(i).getFoodCode() %>&ExpirationDate=<%=foods.get(i).getExpirationDate() %>&QRcodeIdx=<%=foods.get(i).getqRcodeIdx() %>')"/>&nbsp;/&nbsp;<input type="button" value="삭제" onclick="deleteFD(<%=i %>)"/></td>

   </tr>
   <%}%>   
   <!-- 페이징 그리기 -->
    <tr>
        <td height="30" align="center" valign="top" colspan="9" style="font-size:20px;" >
<%

    int block = (pageNum-1)/pageNum_list;

    if(pageNum <= 1){%>
        <font></font>
        <% }else{%>
            <font size=2><a href="foodList.jsp?reg=<%=regp%>&Storehouse=<%=shp%>&food=<%=fdp%>&order=<%=od%>&search=<%=search%>">처음</a></font>
        <%}
 
    if(block <1){%>
        <font> </font>
    <% }else{%>
        <font size=2><a href="foodList.jsp?page=<%=startPage-1 %>&reg=<%=regp%>&Storehouse=<%=shp%>&food=<%=fdp%>&order=<%=od%>&search=<%=search%>">이전</a></font>
    <% }
 
    for(int j = startPage; j <=endPage; j++)
    {
 
        if(pageNum == j)
        {%>
            <font size=2 color=red><%=j%></font>

       <%}else if(j > 0 && j <endPage+1){%>
            <font size=2><a href="foodList.jsp?page=<%=j%>&reg=<%=regp%>&Storehouse=<%=shp%>&food=<%=fdp%>&order=<%=od%>&search=<%=search%>"><%=j%></a></font>
            <%
          } 
    }

    if(flag== false){%>
    <font> </font>
    <%}else{%>    
        <font size=2><a href="foodList.jsp?page=<%=startPage+pageNum_list%>&reg=<%=regp%>&Storehouse=<%=shp%>&food=<%=fdp%>&order=<%=od%>&search=<%=search%>">다음</a></font>
    <%}
 
 
 
    if(pageNum == totalPage){%>       
            <font></font>
        <%}else{%>
            <font size=2><a href="foodList.jsp?page=<%=totalPage%>&reg=<%=regp%>&Storehouse=<%=shp%>&food=<%=fdp%>&order=<%=od%>&search=<%=search%>">마지막</a></font>
        <%}
    %>
    </td>
 
    </tr>
</table>


<script type="text/javascript">

	$('#reg').val('<%=regp%>').prop("selected", true);
	$('#Storehouse').val('<%=shp%>').prop("selected", true);
	$('#foodidx').val('<%=fdp%>').prop("selected", true);	
	$('#order').val('<%=odp%>').prop("selected", true);	

	$(document).ready(function() {
	   

 		 $('#reg').on('change', function() {
 			location.replace("foodList.jsp?reg="+$('#reg').val()+"&order="+$('#order').val()); 
 		 });
   			 $('#Storehouse').on('change', function() {
     	  	 location.replace("foodList.jsp?reg="+$('#reg').val()+"&Storehouse="+$('#Storehouse').val()+"&order="+$('#order').val()); 
   		 });
   			 
   			$('#foodidx').on('change', function() {
        	  	 location.replace("foodList.jsp?reg="+$('#reg').val()+"&Storehouse="+$('#Storehouse').val()+"&food="+$('#foodidx').val()+"&order="+$('#order').val()); 
      		 });
   			$('#order').on('change', function() {
      		     location.replace("foodList.jsp?reg="+$('#reg').val()+"&Storehouse="+$('#Storehouse').val()+"&food="+$('#foodidx').val()+"&order="+$('#order').val()+"&search=<%=search%>"); 
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

          pageNum=1;
       }else{
           pageNum++;

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
    location.replace("foodList.jsp?reg=<%=regp%>&Storehouse="+e); 
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
    	 location.replace("foodList.jsp?reg=<%=regp%>&Storehouse=<%=shp%>&food=<%=fdp%>&order=<%=od%>&sc=시작&search=<%=search%>&page=<%=pageNum%>"); 

 
 }
 
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
				//alert("삭제가 성공했습니다.");
				location.reload();
			},
		error: function(response) {

				console.log(JSON.stringify(data));
				console.log(JSON.stringify(response));

			}	
	});
}

function alram_access(){ 
    var xmlhttp = getXmlHttpRequest(); 
    var url = '/ajax/alram_access.jsp'; 
    if(url){ 
        xmlhttp.open("GET", url, true); 
        xmlhttp.onreadystatechange = function() { 
            if(xmlhttp.readyState == 4) { 
                if(xmlhttp.status == 200) { 
                    var alram_msg = trim(xmlhttp.responseText); 
                    if(alram_msg!=''){ 
                        view_msg(alram_msg); 
                    } 
                } else { 
                    //alert("Error loading "+url+", "+xmlhttp.status+"("+xmlhttp.statusText+")"); 
                } 
            } 
        } 
        xmlhttp.send(null); 
    } 
    setTimeout("alram_access()", 3000);//3초 마다 서버와 통신함 
    return false; 
} 
function view_msg(msg){ 
    var width = 350; 
    var height = 150; 
    var left = (document.body.clientWidth-width)/2; 
    var top = (document.body.clientHeight-height)/2; 
    var alram_win = window.open('/ajax/alram_view.jsp?msg='+msg, '', 'left='+left+',top='+top+',width='+width+',height='+height+',toolbar=no ,directories=no,menubar=no,location=no,scrollbars=no,resizable=yes,status=no'); 
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