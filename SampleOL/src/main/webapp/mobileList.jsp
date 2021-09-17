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
	int pageNum=1;
	int endPage;
	int startPage;
	int pageNum_list=15;
	
	
	if(request.getParameter("page") != null)
		pageNum=Integer.parseInt(request.getParameter("page"));
	   
   //if(request.getParameter("serviceNumber") != null)
     // sn=request.getParameter("serviceNumber");
	String reg="전체";
	String regp="전체";
	String rc="전체";
	String rcp="전체";
	
	DecimalFormat df = new DecimalFormat("###,###");

	
   if(request.getParameter("reg") != null){
		reg = request.getParameter("reg");
		regp = request.getParameter("reg");
   }
   if(request.getParameter("regim_company") != null){
	   rc = request.getParameter("regim_company");
	   rcp = request.getParameter("regim_company");
	   
   }

	
   
   int num2;
   DBConnection cd = new DBConnection();
   ArrayList<MobileEquip> mobileEquips = new ArrayList<MobileEquip>();

   Gson gson = new Gson();
   
	ArrayList<String> mobileStatusReg = cd.getMobileStatusReg();

	ArrayList<String> regimCompanyList = cd.getCodeNameList("RegimCompany");

	ArrayList<String> rc_0 = cd.getMobileStatusRc("28여단");
	ArrayList<String> rc_1 = cd.getMobileStatusRc("28-1대대");
	ArrayList<String> rc_2 = cd.getMobileStatusRc("28-2대대");
	ArrayList<String> rc_3 = cd.getMobileStatusRc("28-3대대");

	String rc0; String rc1; String rc2; String rc3;

	rc0 = gson.toJson(rc_0);
	rc1 = gson.toJson(rc_1); 
	rc2 = gson.toJson(rc_2);
	rc3 = gson.toJson(rc_3);

	
	if(reg.equals("전체") && rc.equals("전체")){
		
	} else if(rc.equals("전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else if(reg.equals("전체")){
		rc = cd.getCodeID("RegimCompany", rc);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		rc = cd.getCodeID("RegimCompany", rc);
	}
	
   
	mobileEquips = cd.getMobileList(reg, rc);
   
   int cnt = mobileEquips.size();
   
   
   if(pageNum == 1 && cnt>pageNum_list){
	   startPage=1;
	   endPage=(cnt/pageNum_list)+1;
  		num=(pageNum-1)*pageNum_list;
       num2=pageNum_list;

   	}else if(pageNum*pageNum_list<=cnt){
   		startPage=(pageNum/5)*5+1;
   		endPage=(cnt/pageNum_list)+1;
   		num=(pageNum-1)*pageNum_list;
        num2=(pageNum-1)*pageNum_list+pageNum_list;

	}else{
   		startPage=(pageNum/5)*5+1;
		endPage=(cnt/pageNum_list)+1;
   		num= (pageNum==1) ? 0: (pageNum-1)*pageNum_list;
	    num2=cnt;

    }
	
   int totalPage= cnt/pageNum_list+1;


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
<script src="http://code.jquery.com/jquery-latest.js"></script>
<body>
<div>
<span class="left"><input type="text" id="now" readonly> <font>&nbsp;&nbsp;총 개수: <%=cnt %></font> </span>


<span class="title">모바일 기기 목록 현황</span>
<span class="right">
  <select id="reg" name ="reg">
						<option>전체</option>
						<%for(int i=0; i<mobileStatusReg.size(); i++) {%>
						<option value="<%=mobileStatusReg.get(i)%>"><%=mobileStatusReg.get(i)%></option>
						<%} %>
	</select>

	
  <select id="RegimCompany" style="width: 140px;">
   </select>   
</span>
</div>
<table class="table" style="text-overflow:ellipsis; overflow:hidden; white-space:nowrap;">
<caption>조회 목록</caption>
   <tr style="background:green;">
      <td class="colt" style="text-align:center;width:4vw;">NO</td>
      <td class="colt" style="text-align:center;width:13vw;">전화번호</td>
      <td class="colt" style="text-align:center;width:10vw;">소속</td>
      <td class="colt" style="text-align:center;width:10vw;">계급</td>
      <td class="colt" style="text-align:center;width:10vw;">성명</td>
      <td class="colt" style="text-align:center;width:10vw;">군번</td>
      <td class="colt" style="text-align:center;width:10vw;">장비구분</td>
      <td class="colt" style="text-align:center;width:10vw;">배정일</td>
      
   </tr>
   
   <% 
      for(int i=num; i<num2; i++){ 
   %>

   
   <tr id="tr<%=i %>" >
      <td class="col" ><%=i+1 %></td>
      <td class="col" style=" text-align:center;"><%=mobileEquips.get(i).getMobileNumber() %></td>
      <td class="col" style=" text-align:center; "><%=mobileEquips.get(i).getRegiment() %></td>
      <td class="col" style=" text-align:center; "><%=mobileEquips.get(i).getRegimCompany() %></td>
      <td class="col" style=" text-align:center; "><%=mobileEquips.get(i).getName() %></td>
      <td class="col" style=" text-align:center; "><%=mobileEquips.get(i).getServiceNumber() %></td>
      <td class="col" style=" text-align:center; "><%=mobileEquips.get(i).getMobileType() %></td>
      <td class="col" style=" text-align:center; "><%=mobileEquips.get(i).getJoinDate() %></td>
   </tr>
  <%} %>

<!-- 페이징 그리기 -->
    <tr>
        <td height="30" align="center" valign="top" colspan="8" style="font-size:20px;" >
<%

    int block = pageNum/5;

    if(pageNum <= 1){%>
        <font></font>
        <% }else{%>
            <font size=2><a href="mobileList.jsp?reg=<%=regp%>&regim_company=<%=rcp%>">처음</a></font>
        <%}
 
    if(block <=1){%>
        <font> </font>
    <% }else{%>
        <font size=2><a href="mobileList.jsp?reg=<%=regp%>&regim_company=<%=rcp%>&page=<%=startPage-1 %>">이전</a></font>
    <% }
 
    for(int j = startPage; j <=endPage; j++)
    {
 
        if(pageNum == j)
        {%>
            <font size=2 color=red><%=j%></font>

       <%}else if(j > 0 && j <endPage+1){%>
            <font size=2><a href="mobileList.jsp?reg=<%=regp%>&regim_company=<%=rcp%>&page=<%=j%>"><%=j%></a></font>
            <%
          } 
    }

    if(block <= cnt/pageNum_list){%>
    <font> </font>
    <%}else{%>    
        <font size=2><a href="mobileList.jsp?reg=<%=regp%>&regim_company=<%=rcp%>&page=<%=startPage+5%>">다음</a></font>
    <%}
 
 
 
    if(pageNum >= cnt/pageNum_list){%>
            <font></font>
       
        <%}else{%>
            <font size=2><a href="mobileList.jsp?reg=<%=regp%>&regim_company=<%=rcp%>&page=<%=totalPage%>">마지막</a></font>
        <%}
    %>
    </td>
 
    </tr>

</table>


<script type="text/javascript">


	$(document).ready(function() {
	   
	 	$('#reg').val('<%=regp%>').prop("selected", true);
			regSelectChange('<%=regp%>');
 	
   		 $('#reg').on('change', function() {
   		     location.replace("mobileList.jsp?reg="+$('#reg').val()+"&regim_company=전체"); 
   		 });
   			 $('#RegimCompany').on('change', function() {
     	  	 location.replace("mobileList.jsp?reg="+$('#reg').val()+"&regim_company="+$('#RegimCompany').val()
     	  			 ); 
   		 });
	 
   	  getTimeStamp2();
    

      
	  //if(<%=cnt%> >15)
     // setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출
   
	});
  

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
    location.replace("mobileList.jsp?reg=<%=regp%>&regim_company="+e); 
}
   
function regSelectChange(e) {
	
	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;
	var rc4 = ['전체'];

	var target = document.getElementById("RegimCompany");

	if(e == "28여단") var d = rc0;
	else if(e == "28-1대대") var d = rc1;
	else if(e == "28-2대대") var d = rc2;
	else if(e == "28-3대대") var d = rc3;
	else if(e == "전체") var d = rc4;



	for (x in d) {
		var opt = document.createElement("option");
		opt.value = d[x];
		opt.innerHTML = d[x];
		target.appendChild(opt);
	}
	
	

	$('#RegimCompany').val('<%=rcp%>').prop("selected", true);	
	
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

       location.replace("mobileList.jsp?reg=<%=regp%>&regim_company=<%=rcp%>&num=<%=num2%>"); 
 
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

</script>
</body>
</html>