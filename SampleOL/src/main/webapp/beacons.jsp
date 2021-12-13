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
	request.setCharacterEncoding("UTF-8");

   int num=0;
   //String sn="";
   if(request.getParameter("num") != null)
      num=Integer.parseInt(request.getParameter("num"));
   
   //if(request.getParameter("serviceNumber") != null)
     // sn=request.getParameter("serviceNumber");
	String reg="소속:전체";
	String regp="소속:전체";
	String rc="세부소속:전체";
	String rcp="세부소속:전체";
	String eq="전체";
	String eqp="전체";

	String delnm="0";
	
	int pageNum=1;
	int endPage;
	int startPage;
	int pageNum_list=10;
	int pagetot=15;
		if(request.getParameter("page") != null)
			pageNum=Integer.parseInt(request.getParameter("page"));
		   
	   //if(request.getParameter("serviceNumber") != null)
	     // sn=request.getParameter("serviceNumber");


		
	DecimalFormat df = new DecimalFormat("###-####-####");

	
	   if(request.getParameter("reg") != null){
			reg = request.getParameter("reg");
			regp = request.getParameter("reg");
	   }
	   if(request.getParameter("regim_company") != null){
		   rc = request.getParameter("regim_company");
		   rcp = request.getParameter("regim_company");
		   
	   }
	   if(request.getParameter("search") != null){
		   eq = request.getParameter("search");
		   eqp = request.getParameter("search");

	   }
	System.out.println(request.getParameter("search"));

	
   
   int num2;
   DBConnection cd = new DBConnection();
   ArrayList<Beacons> beacons = new ArrayList<Beacons>();

   boolean flag=false;
   
   Gson gson = new Gson();

	ArrayList<String> BeaconReg = cd.getBeaconReg();



	ArrayList<String> rc_0 = cd.getBeaconsRc("28여단");
	ArrayList<String> rc_1 = cd.getBeaconsRc("28-1대대");
	ArrayList<String> rc_2 = cd.getBeaconsRc("28-2대대");
	ArrayList<String> rc_3 = cd.getBeaconsRc("28-3대대");


	String rc0; String rc1; String rc2; String rc3;

	rc0 = gson.toJson(rc_0);
	rc1 = gson.toJson(rc_1); 
	rc2 = gson.toJson(rc_2);
	rc3 = gson.toJson(rc_3);



	if(reg.equals("소속:전체") && rc.equals("세부소속:전체")){
	} else if(rc.equals("세부소속:전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else if(reg.equals("소속:전체")){
		rc = cd.getCodeID("RegimCompany", rc);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		rc = cd.getCodeID("RegimCompany", rc);
	}
   
	beacons = cd.getBeaconsList(reg,rc,eq);
	   
	   int cnt = beacons.size();
	   
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
	   total_data=gson.toJson(beacons);


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
      font-weight: 550px;
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
       float:center;
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
   font-size:1.2rem; 
   padding: 5px;
   text-align: center;  
   width: 200px;
   }   
   span select{
   
   height : 30px;
   font-size:18px; 
   padding: 5px;
   }
   form{
   height : 0;
   font-size:18px; 
   padding-left: 240px;
   }
   form input{
   height : 30px;
   }
  </style>
</head>
</head>
<script src="js/jquery-3.6.0.min.js"></script>

<body>
<div style="white-space: nowrap;min-width: 1650px;"> 
<span class="left"><input type="text" id="now" readonly> 
</span>
<form name="search_form" method="get">
	<input type="text" id="search" name="search" placeholder="방이름을 입력하십시오">
	<input type="submit" id="submit" value="검색">
</form>
<span class="title">비콘 현황판</span>
<span class="right">
<font size=4.5>총 개수: <%=cnt %>&nbsp;&nbsp;</font>
  <select id="reg" name ="reg">
						<option>소속:전체</option>
						<%for(int i=0; i<BeaconReg.size(); i++) {%>
						<option value="<%=BeaconReg.get(i)%>"><%=BeaconReg.get(i)%></option>
						<%} %>
	</select>

	
  <select id="RegimCompany" style="width: 160px;">
   </select>   

</span>
</div>

<table class="table" style="white-space: nowrap;min-width: 1650px;">
<caption>조회 목록</caption>
   <tr style="background:green;">
      <td class="colt" style="text-align:center;width:2vw;">NO</td>
      <td class="colt" style="text-align:center;width:6vw;">소속</td>
      <td class="colt" style="text-align:center;width:8vw;">세부소속</td>
      <td class="colt" style="text-align:center;width:6vw;">장비ID</td>
      <td class="colt" style="text-align:center;width:8vw;">UUid</td>
      <td class="colt" style="text-align:center;width:9vw;">기기장소</td>
      <td class="colt" style="text-align:center;width:12vw;">방이름</td>
      <td class="colt" style="text-align:center;width:6vw;">방ID</td>      
      <td class="colt" style="text-align:center;width:6vw;">위도</td>
      <td class="colt" style="text-align:center;width:6vw;">경도</td>
      <td class="colt" style="text-align:center;width:10vw;">군사좌표</td>
       <td class="colt" style="text-align:center;width:6vw;">수정/삭제</td>
      
   </tr>
   
   <% 
      for(int i=num; i<num2; i++){ 
   %>

   
   <tr id="tr<%=i %>" >
      <td class="col" ><%=i+1 %></td>
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getRegimentName() %></td>
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getRegimCompanyName() %></td>
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getEquipId() %></td>
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getUuid() %></td>
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getEquipLocation() %></td>  
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getRoomName() %></td>  
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getRoomNumber() %></td>  
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getLatitude() %></td>
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getLongitude() %></td>
      <td class="col" style=" text-align:center;"><%=beacons.get(i).getMgrs() %></td>
      <td class="col" style=" text-align:center;"><input type="button" value="수정" onclick="location.href='beaconsEdit.jsp?uuid=<%=beacons.get(i).getUuid()%>'"/>&nbsp;/&nbsp;<input type="button" value="삭제" onclick="deleteBC(<%=i %>)"/></td>
   </tr>
<%}%>
   
   <!-- 페이징 그리기 -->
    <tr>
        <td height="30" align="center" valign="top" colspan="12" style="font-size:20px;" >
<%

    int block = (pageNum-1)/pageNum_list;

    if(pageNum <= 1){%>
        <font></font>
        <% }else{%>
            <font size=2><a href="beacons.jsp?&reg=<%=regp%>&regim_company=<%=rcp%>">처음</a></font>
        <%}
 
    if(block <1){%>
        <font> </font>
    <% }else{%>
        <font size=2><a href="beacons.jsp?page=<%=startPage-1 %>&reg=<%=regp%>&regim_company=<%=rcp%>">이전</a></font>
    <% }
 
    for(int j = startPage; j <=endPage; j++)
    {
 
        if(pageNum == j)
        {%>
            <font size=2 color=red><%=j%></font>

       <%}else if(j > 0 && j <endPage+1){%>
            <font size=2><a href="beacons.jsp?page=<%=j%>&reg=<%=regp%>&regim_company=<%=rcp%>"><%=j%></a></font>
            <%
          } 
    }

    if(flag== false){%>
    <font> </font>
    <%}else{%>    
        <font size=2><a href="beacons.jsp?page=<%=startPage+pageNum_list%>&reg=<%=regp%>&regim_company=<%=rcp%>">다음</a></font>
    <%}
 
 
 
    if(pageNum == totalPage){%>       
            <font></font>
        <%}else{%>
            <font size=2><a href="beacons.jsp?page=<%=totalPage%>&reg=<%=regp%>&regim_company=<%=rcp%>">마지막</a></font>
        <%}
    %>
    </td>
 
    </tr>
</table>


<script type="text/javascript">

$('#reg').val('<%=regp %>').prop("selected", true);
regSelectChange('<%=regp %>');
$('#RegimCompany').val('<%=rcp%>').prop("selected", true);	

		
$(document).ready(function() {

		 $('#reg').on('change', function() {
		     location.replace("beacons.jsp?reg="+$('#reg').val()+"&regim_company=세부소속:전체"); 
		 });
			 $('#RegimCompany').on('change', function() {
	 	  	 location.replace("beacons.jsp?reg="+$('#reg').val()+"&regim_company="+$('#RegimCompany').val()); 
		 });


	  getTimeStamp2();


  
  //if(<%=cnt%> >15)
 // setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출

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

   
   function MGRSString (Lat, Long)
	{ 
	console.log(Lat.trim()+","+Long.trim());
	if (Lat < -80) return 'Too far South' ; if (Lat.trim() > 84) return 'Too far North' ;
	var c = 1 + Math.floor ((Long.trim()+180)/6);
	var e = c*6 - 183 ;
	var k = Lat.trim()*Math.PI/180;
	var l = Long.trim()*Math.PI/180;
	var m = e*Math.PI/180;
	var n = Math.cos (k);
	var o = 0.006739496819936062*Math.pow (n,2);
	var p = 40680631590769/(6356752.314*Math.sqrt(1 + o));
	var q = Math.tan (k);
	var r = q*q;
	var s = (r*r*r) - Math.pow (q,6);
	var t = l - m;
	var u = 1.0 - r + o;
	var v = 5.0 - r + 9*o + 4.0*(o*o);
	var w = 5.0 - 18.0*r + (r*r) + 14.0*o - 58.0*r*o;
	var x = 61.0 - 58.0*r + (r*r) + 270.0*o - 330.0*r*o;
	var y = 61.0 - 479.0*r + 179.0*(r*r) - (r*r*r);
	var z = 1385.0 - 3111.0*r + 543.0*(r*r) - (r*r*r);
	var aa = p*n*t + (p/6.0*Math.pow (n,3)*u*Math.pow (t,3)) + (p/120.0*Math.pow (n,5)*w*Math.pow (t,5)) + (p/5040.0*Math.pow (n,7)*y*Math.pow (t,7));
	var ab = 6367449.14570093*(k - (0.00251882794504*Math.sin (2*k)) + (0.00000264354112*Math.sin (4*k)) - (0.00000000345262*Math.sin (6*k)) + (0.000000000004892*Math.sin (8*k))) + (q/2.0*p*Math.pow (n,2)*Math.pow (t,2)) + (q/24.0*p*Math.pow (n,4)*v*Math.pow (t,4)) + (q/720.0*p*Math.pow (n,6)*x*Math.pow (t,6)) + (q/40320.0*p*Math.pow (n,8)*z*Math.pow (t,8));
	aa = aa*0.9996 + 500000.0;
	ab = ab*0.9996; if (ab < 0.0) ab += 10000000.0;
	var ad = 'CDEFGHJKLMNPQRSTUVWXX'.charAt (Math.floor (Lat/8 + 10));
	var ae = Math.floor (aa/100000);
	var af = ['ABCDEFGH','JKLMNPQR','STUVWXYZ'][(c-1)%3].charAt(ae-1);
	var ag = Math.floor (ab/100000)%20;
	var ah = ['ABCDEFGHJKLMNPQRSTUV','FGHJKLMNPQRSTUVABCDE'][(c-1)%2].charAt(ag);
	aa = Math.floor (aa%100000); 
	ab = Math.floor (ab%100000);
		return c + ad + ' ' + af + ah + ' ' + aa + ' ' + ab;
	}   
   
function storeSelectChange(e) {
    location.replace("bd_Beacons?reg=<%=regp%>&regim_company="+e); 
}
   
function regSelectChange(e) {
	
	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;
	var rc4 = ['세부소속:전체'];

	var target = document.getElementById("RegimCompany");

	if(e == "28여단") var d = rc0;
	else if(e == "28-1대대") var d = rc1;
	else if(e == "28-2대대") var d = rc2;
	else if(e == "28-3대대") var d = rc3;
	else if(e == "소속:전체") var d = rc4;



	for (x in d) {
		var opt = document.createElement("option");
		opt.value = d[x];
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

       location.replace("beacons.jsp?num=<%=num2%>"); 
 
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
	    leadingZeros(d.getDate(), 2);

	  document.getElementById("now").value =s;
	}

function deleteBC(num){

	if(confirm(data[num].EquipId+"을 정말 삭제하시겠습니까?")){
		
	$.ajax({
		url: 'http://110.10.130.51:5002/api/Wareable/BeaconsDelete',
		contentType: "application/json; charset=utf-8",
		method: 'POST',
		data: JSON.stringify(data[num]),
		dataType: "json",
		accept: "application/json",
		success: function(response) {
			// success handle
				console.log(JSON.stringify(response));
				alert("삭제가 성공했습니다.");
				location.href="beacons.jsp";
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