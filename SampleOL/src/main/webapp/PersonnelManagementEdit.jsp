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
   String sn="";
   //String sn="";
   sn=request.getParameter("sn");
  
   //if(request.getParameter("serviceNumber") != null)
     // sn=request.getParameter("serviceNumber");
	String reg="전체";
	String regp="전체";
	String rc="전체";
	String rcp="전체";
	String delnm="0";
	
	   
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


	
   
   int num2;
   DBConnection  cd = new DBConnection();
   ArrayList<PersonnelManagement> personnelmanagements = new ArrayList<PersonnelManagement>();

   boolean flag=false;
   
   Gson gson = new Gson();



	ArrayList<String> rc_0 = cd.getCodeNameList("RegimCompany", "RG-280");
	ArrayList<String> rc_1 = cd.getCodeNameList("RegimCompany", "RG-281");
	ArrayList<String> rc_2 = cd.getCodeNameList("RegimCompany", "RG-282");
	ArrayList<String> rc_3 = cd.getCodeNameList("RegimCompany", "RG-283");

	ArrayList<String> rcp_0 = cd.getCodeIDList("RegimCompany", "RG-280");
	ArrayList<String> rcp_1 = cd.getCodeIDList("RegimCompany", "RG-281");
	ArrayList<String> rcp_2 = cd.getCodeIDList("RegimCompany", "RG-282");
	ArrayList<String> rcp_3 = cd.getCodeIDList("RegimCompany", "RG-283");

	
	
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
	ArrayList<String> DutyReg = cd.getDutyReg();
	ArrayList<String> RankReg = cd.getCodeNameList("Rank");


	if(reg.equals("전체") && rc.equals("전체")){
	} else if(rc.equals("전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else if(reg.equals("전체")){
		rc = cd.getCodeID("RegimCompany", rc);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		rc = cd.getCodeID("RegimCompany", rc);
	}
   
	personnelmanagements = cd.getPersonnelMemberInfo(sn);
	   
	String pm;
	pm=gson.toJson(personnelmanagements);

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
      height:300px;
      width: 1200px;
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
      width:180px;
      vertical-align: middle;
      
   }

   .colt{
      font-weight: 400;
      font-size:20px;
      color:white;
     background: #416def;
     width:120px;
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
<span class="left"><input type="text" id="now" readonly></span>
<span class="title">회원정보 수정삭제</span>

</div>
<table class="table" style="white-space: nowrap;">
<caption>조회 목록</caption> 
   <tr>
 	 <td rowspan="3" width="220px"  style="border-bottom:none;" >
     <%
		if(personnelmanagements.get(0).getPicture().isEmpty())
   		out.println("<img id=\"picture\" src=\"\" width=\"170px\"  style=\"margin-bottom: 4px; max-height:170px\" />");
		else
		out.println("<img id=\"picture\" src=\"data:image/jpg;base64, "+personnelmanagements.get(0).getPicture()+"\" width=\"170px\"  style=\"margin-bottom: 4px; max-height:170px\" />");
     %>

	</td>
      <td class="colt" >군번</td>
      <td class="col" ><input type="text" id="ServiceNumber" value="<%=personnelmanagements.get(0).getServiceNumber() %>"></td>
      <td class="colt" >직위</td>
      <td class="col" >
      <select id="rank" style="width: 140px;">
						<%for(int i=0; i<RankReg.size(); i++) {%>
						<option value="<%=cd.getCodeID("Rank",RankReg.get(i))%>"><%=RankReg.get(i)%></option>
						<%} %>
      </select>
      </td>				

   </tr>
   <tr>

      <td class="colt" >이름</td>
      <td class="col" ><input type="text" id="name" value="<%=personnelmanagements.get(0).getName() %>"></td>
      <td class="colt" style="">직책</td>
      <td class="col" >
      <select id="duty" style="width: 140px;">
						<%for(int i=0; i<DutyReg.size(); i++) {%>
						<option value="<%=DutyReg.get(i)%>"><%=DutyReg.get(i)%></option>
						<%} %>
      </select>
      </td>
      <td style="display:none;">
      <input type="password" id="pw" value="<%=personnelmanagements.get(0).getPassword() %>">
   	</td>
   </tr>
   <tr>
      <td class="colt" >부대</td>
      <td class="col" >
      		<select id="reg" name ="reg" style="width: 140px;">
						<%for(int i=0; i<PersonnelReg.size(); i++) {%>
						<option value="<%=cd.getCodeID("Regiment",PersonnelReg.get(i))%>"><%=PersonnelReg.get(i)%></option>
						<%} %>
			</select></td>
      <td class="colt" >세부소속</td>
      <td class="col" >
      <select id="RegimCompany" style="width: 140px;"></select>
   </td>
  </tr>
   <tr>
   	  <td rowspan="3" style="border-top:none; width:181;">

        <label class="btn btn-primary btn-file" id="fileAdd" style="display: none;">
   	 파일추가 <input id="picturefile" type="file" accept="image/jpg,image/jpeg" style="display: none; width:181;" onchange="onFileSelected(event)">
		</label>
		 <label class="btn btn-primary btn-file" id="fileEdit" style="display: none;">
   	 수정 <input type="file" id="picturefile" accept="image/jpg,image/jpeg" style="display: none;" onchange="onFileSelected(event)">
		</label>
		<label class="btn btn-primary btn-file" id="fileDelete" style="display: none;" >
   	 삭제 
		</label>
	  </td>
   
      <td class="colt" >전화번호</td>
      <td class="col" ><input type="text" id="MobileNumber" value="<%=personnelmanagements.get(0).getMobileNumber() %>"></td>  
      <td class="colt" >핸드폰번호</td>
      <td class="col" ><input type="text" id="MyPhoneNumber" value="<%=personnelmanagements.get(0).getMyPhoneNumber() %>"></td>  
      <td class="colt" >부모번호</td>
      <td class="col" ><input type="text" id="ParentsNumber" value="<%=personnelmanagements.get(0).getParentsNumber() %>"></td>  
   </tr>
      <tr>

      <td class="colt" >생년월일</td>
      <td class="col" ><input type="date" id="birth" value="<%=personnelmanagements.get(0).getBirthDate() %>"></td>
      <td class="colt" >입대일</td>
      <td class="col" ><input type="date" id="join" value="<%=personnelmanagements.get(0).getJoinDate() %>"></td>  
      <td class="colt" >퇴직일</td>
      <td class="col" ><input type="date" id="retire" value="<%=personnelmanagements.get(0).getRetireDate() %>"></td>
   </tr>
	

</table>
<input type="button" id="edit" value="수정" onclick="pmUpdate()">
<input type="button" id="pwreset" value="비밀번호 리셋" onclick="pwReset()">
<script type="text/javascript">


$(document).ready(function() {
	
 	$('#reg').val('<%=personnelmanagements.get(0).getRegiment() %>').prop("selected", true);
	regSelectChange('<%=personnelmanagements.get(0).getRegiment() %>');
	$('#RegimCompany').val('<%=personnelmanagements.get(0).getRegimCompany()%>').prop("selected", true);	
 	$('#duty').val('<%=personnelmanagements.get(0).getDuty().trim() %>').prop("selected", true);
 	$('#rank').val('<%=personnelmanagements.get(0).getRank() %>').prop("selected", true);


	  if('<%=personnelmanagements.get(0).getPicture()%>' === '')	
	  	showSearch('fileDelete');
	  else{
	  	showSearch('fileAdd');
	  		  
	  }


	  getTimeStamp2();
	  
	  $('#reg').on('change', function() {
			regSelectChange($("#reg").val());
		 });

	  $('#fileDelete').on('click', function() {
		  showSearch("fileDelete");
		 });

 // setTimeout('go_url()',10000)  // 10초후 go_url() 함수를 호출

});

	setInterval(getTimeStamp2,1000);


	
	
	var data = <%=pm%>;
	
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
   
function storeSelectChange(e) {
    location.replace("PersonnelManagementEdit.jsp?reg=<%=regp%>&regim_company="+e); 
}
   
function regSelectChange(e) {
	
	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;

	var rcp0 = <%=rcp0%>; var rcp1 = <%=rcp1%>;  
	var rcp2 = <%=rcp2%>; var rcp3 = <%=rcp3%>;  
	var target = document.getElementById("RegimCompany");

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
	
	 $("#RegimCompany").children('option').remove();


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

	  document.getElementById("now").value =s;
	}




function pmUpdate(){
	if(confirm("수정하시겠습니까?")){
		data[0].ServiceNumber=$('#ServiceNumber').val();
		data[0].rank=$('#rank').val();
		data[0].duty=$('#duty').val();
		data[0].name=$('#name').val();
		data[0].Regiment=$('#reg').val();
		data[0].RegimCompany=$('#RegimCompany').val();
		data[0].MobileNumber=$('#MobileNumber').val();
		data[0].MyPhoneNumber=$('#MyPhoneNumber').val();
		data[0].ParentsNumber=$('#ParentsNumber').val();
		data[0].birthDate=$('#birth').val();
		data[0].joinDate=$('#join').val();
		data[0].retireDate=$('#retire').val();
		data[0].Password=$('#pw').val();

	$.ajax({
		url: 'http://211.9.3.55:5010/TenSystem/PersonnelManagement/PersonnelManagementNewSave',
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

			},
		error: function(response) {
				alert("실패했습니다.");
				console.log(JSON.stringify(data));
				console.log(JSON.stringify(response));

			}	
	});
	}
}

function pwReset(){
	document.getElementById("pw").value='<%=cd.PwRandom()%>';
	data[0].Password=$('#pw').val();
	console.log(data[0].Password);
$.ajax({
	url: 'http://211.9.3.55:5010/TenSystem/PersonnelManagement/PersonnelManagementNewSave',
	contentType: "application/json; charset=utf-8",
	method: 'POST',
	data: JSON.stringify(data[0]),
	dataType: "json",
	accept: "application/json",
	success: function(response) {
		// success handle
			console.log(JSON.stringify(response));
			alert("비밀번호를 초기화했습니다.");
		    location.replace("PersonnelManagementEdit.jsp?sn="+"<%=sn%>"); 

		},
	error: function(response) {
			console.log(JSON.stringify(data));
			console.log(JSON.stringify(response));

		}	
});

}

function setThumbnail(event) { 
	var reader = new FileReader(); 
	reader.onload = function(event) { 
		var img = document.createElement("img"); 
		img.setAttribute("src", event.target.result); 
		document.querySelector("div#image_container").appendChild(img); 
	}; 
		reader.readAsDataURL(event.target.files[0]); 
}


function setThumbnail2(event) { 
	var reader = new FileReader(); 
	reader.onload = function(event) { 
		var img = document.createElement("img"); 
		img.setAttribute("src", event.target.result); 
		document.querySelector("div#image_container").appendChild(img); 
	}; 
		reader.readAsDataURL(event.target.files[0]); 
}


</script>
</body>
</html>