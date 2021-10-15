<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<!DOCTYPE html>

<%@ page import="com.SampleOL.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="net.sf.json.*" %>
<%@ page import="com.google.gson.*" %>
<%@ page import="java.io.*, java.util.*" %>
<%
System.out.println("personalLocations6");
	String param = request.getParameter("gis_setting");
	String param2 = request.getParameter("gis_setting2");
	String reg = request.getParameter("reg");
	String rc = request.getParameter("regim_company");
	
	String regp = request.getParameter("reg");
	String rcp = request.getParameter("regim_company");
	String pn=null;

	String phoneNum = request.getParameter("phoneNum");

	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	Location lastLocation = new Location();
		
	Gson gson = new Gson();
	String multi_marker = "";
	String last_marker ="";

		//int chk = 1;

	if(param.equals("satellite_map")){
		pn = "위성지도";
	} else if(param.equals("geofence")){
		pn = "군사지도";
	} 
	
	if(reg.equals("전체") && rc.equals("전체")){
		
	} else if(rc.equals("전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		rc = cd.getCodeID("RegimCompany", rc);
	}
	 
	
			
		locations = cd.getMobileStatus(reg,rc);
		String lastTimestamp = lastLocation.getTimestamp();
		multi_marker = gson.toJson(locations);
		
		System.out.println(locations.toString());

		ArrayList<String> regimentList = cd.getCodeNameList("Regiment");
		ArrayList<String> regimCompanyList = cd.getCodeNameList("RegimCompany");
		ArrayList<String> isDeviceList = cd.getCodeNameList("IsDevice");
		ArrayList<String> missionTypeList = cd.getCodeNameList("MissionType");
		ArrayList<String> equipTypeList = cd.getCodeNameList("EquipType");	
		ArrayList<String> mobileStatusReg = cd.getMobileStatusReg();
		
		ArrayList<String> rc_0 = cd.getMobileStatusRc("28여단");
		ArrayList<String> rc_1 = cd.getMobileStatusRc("28-1대대");
		ArrayList<String> rc_2 = cd.getMobileStatusRc("28-2대대");
		ArrayList<String> rc_3 = cd.getMobileStatusRc("28-3대대");	
		
		String rc0; String rc1; String rc2; String rc3;
		rc0 = gson.toJson(rc_0);
		rc1 = gson.toJson(rc_1); 
		rc2 = gson.toJson(rc_2);
		rc3 = gson.toJson(rc_3);
		
		
		ArrayList<String> totalEquipReg = cd.getTotalEquipReg();
		
		ArrayList<String> tet_0 = cd.getTotalEquipType("28여단");
		ArrayList<String> tet_1 = cd.getTotalEquipType("28-1대대");
		ArrayList<String> tet_2 = cd.getTotalEquipType("28-2대대");
		ArrayList<String> tet_3 = cd.getTotalEquipType("28-3대대");	
		
		String tet0; String tet1; String tet2; String tet3;
		tet0 = gson.toJson(tet_0);
		tet1 = gson.toJson(tet_1); 
		tet2 = gson.toJson(tet_2);
		tet3 = gson.toJson(tet_3);
		System.out.println("1");
%>


<html>
<head>
<meta charset="UTF-8">
    <meta charset=utf-8>
    <meta name=viewport content="width=device-width, initial-scale=1">
    <meta name=description content="OpenLayer - Map multiple markers.">
    <title>Locations</title>

 <style>
    	body, html{
    		width: 100%;
    		position: fixed; 
			overflow-y: scroll;
			margin-left: 0;
			margin-top: 0;
			
    	}
    	#zoom-restore{
			position: absolute;
    		height: 30px;
    		font-size: 15px;
			right: 0;
			margin-right: 4px;

    	}
    	#btn{
    		height: 30px;
       		margin-left: 4px;
       		margin-right: -4px;
       		font-size: 15px;
       		
    	}

        #map{
        	width: auto;
            height: 1080px;
            
        }
        
        
        #equip_regiment{
           width: 60px;  
        }
        
        #equip_type{
           width: 120px;  
        }
        
        #reg{
         	width: 60px;   
        }

        #regim_company{
        	width: 120px;
        }
        
        #submit{
        	position: absolute;
        	right:0;
			top : 33px;
			height: 30px;
			margin-right: 4px;
        }
     

        
        #gis_setting2{	
			position: absolute;
			top : 33px;
			height: 30px;
        	right:0;
        	margin-top: 4px;
        	margin-right: 62px;
        }
        
         #submit2{
        	position: absolute;
        	right:0;
			top : 33px;
			height: 30px;
			margin-right: 4px;
        }
        
        #status{
        	height: 36px;
        }
        #Scale{
        	height: 36px;
        }
        #equip{
        	height: 36px;
        }
        #table{
    	   	position: fixed;
			top : 33px;
			height: 30px;
			left: 0;
			margin-left: 4px;
        }
        
		#buttonLayer{
			width:auto;
		}
        
        #gis_setting{
              height: 36px;
        }
        #personal{
              height: 36px;
        }
        #rcp_frm{
    	   	position: absolute;
    		top: 0;
    		margin-top: 5px;
    		margin-left: 4px;
    	}
    	
        .ol-tooltip *{
            font-family: Arial, Helvetica, sans-serif;
            font-weight: 300
        }
        .ol-tooltip {
            display: flex; overflow: hidden;
            padding: 3px; margin: 3px 0px;
            border-radius: 6px;
        }
        .ol-tooltip:hover{
            background: rgba(102, 51, 153, 0.062)
        }
        .ol-tooltip img {
            float: left; padding: 5px;
            width: 40px; height: 40px;
        }
        .ol-tooltip-job a{
            font-size: 15px; padding: 2px;
            text-decoration: none;
            color: #0050b8;
            font-weight: bold;
            white-space: nowrap;
        }
        .ol-tooltip-job a:hover{
            color: #c90083;
        }
        .ol-tooltip-salary{
            font-size: 14px; padding: 2px;
            white-space: nowrap;
        }
        .ol-tooltip-company{
            font-size: 13px; padding: 2px;
        }
        .ol-popup {
            position: absolute;
            background-color: white;
            filter: drop-shadow(0 1px 4px rgba(0,0,0,0.2));
            font-size: 11px;
            padding: 10px;
            border-radius: 10px;
            border: 1px solid #cccccc2a;
            bottom: 12px;
            transform: translate(-50%, 0%);
            margin-bottom: 18px;
            opacity: 0.9;
        }
        .ol-popup:after, .ol-popup:before {
            top: 100%;
            border: solid transparent;
            content: " ";
            height: 0;
            width: 0;
            position: absolute;
            pointer-events: none;
        }

        .ol-popup:after {
            border-top-color: white;
            border-width: 10px;
            left: 70px;
            margin-left: -16px;
        }

        .ol-popup:before {
            border-top-color: #cccccc2a;
            border-width: 11px;
            left: 70px;
            margin-left: -16px;
        }
        .marker{
            background: #222 !important;
        }
        .animated {
            position: relative;
            -webkit-animation-duration: 1s;
            animation-duration: 1s;
            -webkit-animation-fill-mode: both;
            animation-fill-mode: both;
        }
        .animated:hover {
            -webkit-animation-iteration-count: infinite;
            animation-iteration-count: infinite;
        }
        @keyframes hop {
            0% {margin-bottom: 0px;}
            50% {margin-bottom: 30px;}
            100% {margin-bottom: 0px;}
        }
        .hop {
            -webkit-animation-name: hop;
            animation-name: hop;
            animation-iteration-count: infinite;
            animation-duration: 2s;
        }
        
        #buttonLayer{
                    white-space: nowrap;
        }
 		.ol-control{
 		    display: none;
 		}      
    </style>
    <!-- OpenLayers map -->
	<script src="js/jquery-3.6.0.min.js"></script>
   <link rel="stylesheet" href="css/ol.css" type="text/css">
    <script src="js/ol.js"></script>
</head>

<body>
	<div id ="buttonLayer">
	<input type="button" value="병력" id="btn" onclick="showSearch('status')"/>
	<input type="button" value="장비" id="btn" onclick="showSearch('equip')"/>
	<input type="button" value="차량" id="btn" onclick="showSearch('vihicle')"/>
	<input type="button" value="개인" id="btn" onclick="showSearch('personal')"/> 
	<input type="button" value="지도" id="btn" onclick="showSearch('gis_setting')"/>
	<input type="button" value="중심" id="btn" onclick="showSearch('Scale')"/>
	<button id="zoom-restore" >reset</button><br>
	
	<div id="status" style="display:none">
	<form action="personalLocations3.jsp" method="get">
		<table id="table">
			<tr>
				<td>소속</td>
				<td>
					<select id="reg" name ="reg" onchange="regimentSelectChange(this)">
						<option>전체</option>
						<%for(int i=0; i<mobileStatusReg.size(); i++) {%>
						<option value="<%=mobileStatusReg.get(i)%>"><%=mobileStatusReg.get(i)%></option>
						<%} %>
					</select>
				</td>
				<td>세부</td>
				<td>
					<select id="regim_company" name="regim_company">
					<option value="전체">전체</option>
					</select>
				</td>
			</tr>
		</table>
		<input type="submit" id="submit" value=" 조회 ">
	</form>
	</div>

	<div id="equip" style="display:none">
	<form action="equipLocations2.jsp" method="get">
		<table id="table">
			<tr>
				<td class="block">소속</td>
				<td>
					<select id="equip_regiment" name="equip_regiment" onchange="eRegimentSelectChange(this)">
						<option>전체</option>
						<%for(int i=0; i<totalEquipReg.size(); i++){ %>
	    				<option value=<%=totalEquipReg.get(i) %>><%=totalEquipReg.get(i) %></option>
						<%}%>
					</select>
				</td>
				<td class="block">장비구분</td>
				<td>
					<select id="equip_type" name="equip_type">
	    				<option value="전체">전체</option>
					</select>
				</td>
			</tr>
		</table>
		<input type="submit" id="submit" value=" 조회 " >
	</form>
	</div>
	
	<div id="vihicle" style="display:none">
	TBD
	</div>
	
	<div id="gis_setting" style="display:none">
		<form action="circle.jsp" method="get">
		<table id="table">
			<tr>
				<td>
					<font size="1px">
					<input type="radio" id="geofence" name="gis_setting" class="gis_setting" value="geofence" checked>
		  			<label for="geofence">군사</label>
		 			 <input type="radio" id="satellite_map" name="gis_setting" class="gis_setting" value="satellite_map" >
		 			 <label for="satellite_map">위성</label>		 			
					
					</font>		  			
				</td>
			</tr>
		</table>
				<font size="1px" style="font-weight: bold" id="gis_setting2">
		 			<input type="radio" id="geofon" name="gis_setting2" class="gis_setting2" value="geofon">
		  			<label for="geofon">GeoF-ON</label>
		 			<input type="radio" id="geofoff" name="gis_setting2" class="gis_setting2" value="geofoff" checked>
		 			<label for="geofoff">GeoF-OFF</label>
		 			<input type="radio" name="reg" class="reg2" value="9사단" checked style="display:none">
		 			<input type="radio" name="reg" class="reg2" value="28여단" style="display:none">
		  			<input type="radio" name="reg" class="reg2" value="28-1대대" style="display:none">
		  			<input type="radio" name="reg" class="reg2" value="28-2대대" style="display:none">
		  			<input type="radio" name="reg" class="reg2" value="28-3대대" style="display:none">
		  			<input type="hidden" name="regim_company" value="전체" style="display:none">
		 			
		 			</font>
		 			
				<font size="1px">
					<input type="submit" id="submit2" value=" 설정 ">
				</font>		  			
		
		</form>
	</div>
	
	<div id="personal" style="display:none">
		<form name="search_form" action="personalLocations4.jsp" method="get" onsubmit="return check()">
			
			<table id="table">
			<tr>
				<td>	
				<select id="search_check" name="search_check" >
   				<option value="phone_num">전화번호</option>
   				<option value="service_num">군번</option>
   				<option value="name">성명</option>
				</select>		
			<input type="text" id="search_this" name="search_this" size="10">
				</td>
			</tr>
		</table>
			<input type="submit" id="submit" value=" 조회 ">
		</form>
	</div>
	
	<div id="Scale" style="display:none">
		<form action="personalLocations6.jsp" method="get">
		<table id="table">
			<tr>
				<td>
					<font size="1px">
					<input type="radio" name="reg" class="reg" value="9사단" checked>
		  			<label for="Scale9">9사단</label>
		 			<input type="radio" name="reg" class="reg" value="28여단">
		  			<label for="Scale28">28여단</label>
		  			<input type="radio" name="reg" class="reg" value="28-1대대">
		  			<label for="Scale1">1대대</label>
		  			<input type="radio" name="reg" class="reg" value="28-2대대">
		  			<label for="Scale2">2대대</label>
		  			<input type="radio" name="reg" class="reg" value="28-3대대">
		  			<label for="Scale3">3대대</label>
		  			<input type="hidden" name="regim_company" value="전체">
					</font>		  			
				</td>
			</tr>
		</table>
				<font size="2px">
					<input type="radio" name="gis_setting" value="geofence" class="gis_setting3" checked style="display:none">
		 			 <input type="radio" name="gis_setting" value="satellite_map" class="gis_setting3"  style="display:none">
		 			<input type="radio"  name="gis_setting2" class="gis_setting4" value="geofon" style="display:none">
		 			<input type="radio"  name="gis_setting2" class="gis_setting4" value="geofoff" checked style="display:none">	
					<input type="submit" id="submit2" value=" 설정 ">
				</font>
		</form>
	</div>
	<div id="name_select" style="display:none">
	</div>
	</div>
	<!-- 
	<div id="test" style="display:none">
	<form action="javascript:searchName()" method="post">
		전화번호 : <input type="text" id="phoneNum" size="10" value="01028957223"> <input type="submit" value="전송">
	</form>
	</div>
	 -->

	<div id="map"></div>
	
	<script src="js/map.js"></script>
	

	<!-- Popup hover -->
    <div id="popup" class="ol-popup">
        <a id="popup-closer" class="ol-popup-closer"></a>
        <div id="popup-content"></div>
    </div>
    <!-- Popup click -->
    <div id="popupClick" class="ol-popup">
        <a id="popup-closer" class="ol-popup-closer"></a>
        <div id="popup-content-click"></div>
    </div>
 	
 	
    <script>
    
    $(document).ready(function() 
    		{ 
			    $("input:radio[name='gis_setting']:radio[value='<%=param%>']").attr("checked",true);
    			$("input:radio[name='gis_setting2']:radio[value='<%=param2%>']").attr("checked",true);
        		$("input:radio[name='reg']:radio[value='<%=regp%>']").attr("checked",true);
    		});


        $("input:radio[class=gis_setting]").click(function(){
            var radioVal = $('input[class="gis_setting"]:checked').val();
            $("input:radio[class='gis_setting3']:radio[value='"+radioVal+"']").prop("checked",true);
        });
        $("input:radio[class=gis_setting2]").click(function(){
            var radioVal = $('input[class="gis_setting2"]:checked').val();
            $("input:radio[class='gis_setting4']:radio[value='"+radioVal+"']").prop("checked",true);

        });
        
        $("input:radio[class='reg']").click(function(){
            var radioVal = $('input[class="reg"]:checked').val();
            $("input:radio[class='reg2']:radio[value='"+radioVal+"']").prop("checked",true);
        });
    	     
    	
    	if(document.referrer.split('/')[4].indexOf('locations.jsp')>=0 || document.referrer.split('/')[4].indexOf('personalLocations6.jsp')>=0 || document.referrer.split('/')[4].indexOf('circle.jsp')>=0){
    		document.getElementById('Scale').style.display="block";
    	}
    
   		// var x = 126.7719083;	var y = 37.6544622;
   		 var x = 126.791344;	var y = 37.767728;

   		var r = 4000;
   		
   		var pnt = ol.proj.fromLonLat([x, y]);
   		
   		 var data = <%=multi_marker%>;
        // var data = <%=last_marker%>;

        
  	    var straitSource = new ol.source.Vector({ wrapX: true });
 	    var straitsLayer = new ol.layer.Vector({
 	        source: straitSource
 	    });
		
 	   if('<%=param%>'=='geofence' ){

        // Instanciate a Map, set the object target to the map DOM id
		var map = new ol.Map({
			target: 'map',  // 위 index.html에 div id가 map인 엘리먼트에 맵을 표출
				layers: [
					viewLayer
				],
				view: new ol.View({
					center: ol.proj.fromLonLat(
							[126.77192, 37.654461]
					), 
					zoom: 11
				})
		});
 	  }else if('<%=param%>'=='satellite_map'){
 	// Instanciate a Map, set the object target to the map DOM id
 		var map = new ol.Map({
 		 target: 'map',  // 위 index.html에 div id가 map인 엘리먼트에 맵을 표출
			layers: [
				viewLayer,viewLayer3
			],
			view: new ol.View({
				center: ol.proj.fromLonLat(
						[126.77192, 37.654461]
				), 
				zoom: 11
			})
	});
 	  }
		var view = map.getView();
        var zoom = view.getZoom();
        var center = view.getCenter();
        var rotation = view.getRotation();
        
        document.getElementById('zoom-restore').onclick = function(){
        	view.setCenter(center);
        	view.setRotation(rotation);
        	view.setZoom(zoom);
        }
      
        
        var vectorSource = new ol.source.Vector({
			projection : 'EPSG:3857'
		}); //새로운 벡터 생성
		var circle = new ol.geom.Circle(pnt, r); //좌표, 반경 넓이
		var CircleFeature = new ol.Feature(circle); //구조체로 형성
		vectorSource.addFeatures([ CircleFeature ]); // 벡터소스에 추가	
		
		var vectorLayer = new ol.layer.Vector({ //추가할 벡터레이어
			source : vectorSource,
			style : [ new ol.style.Style({
				stroke : new ol.style.Stroke({ //두께
					color : 'rgba( 240, 79, 79 ,0.9)',
					width : 2
				}),
				fill : new ol.style.Fill({ //채우기
					color : 'rgba( 255, 133, 133 ,0.5)'
				}),
				text : new ol.style.Text({ //텍스트
					text : 'Yoo!',
					textAlign : 'center',
					font : '15px roboto,sans-serif'
				})
			}) ]
		});
		if('<%=param2%>' === 'geofon'){			
		map.addLayer(vectorLayer); //만들어진 벡터를 추가	
		}
		map.addLayer(straitsLayer);
		
		
		
		// Popup showing the position the hovered marker
		var container = document.getElementById('popup');
		var popup = new ol.Overlay({
		    element: container,
		    positioning: 'bottom-center',
		    //autoPan: true,
		    //autoPanAnimation: {
		    //    duration: 250
		    //}
		});
		map.addOverlay(popup);

		// Popup showing the position the user clicked
		var containerClick = document.getElementById('popupClick');
		var popupClick = new ol.Overlay({
		    element: containerClick,
		    autoPan: true,
		    autoPanAnimation: {
		        duration: 250
		    }
		});
		map.addOverlay(popupClick);

		// Popup part
		var content = document.getElementById('popup-content');
		var contentClick = document.getElementById('popup-content-click');
		var selected = null;

		// Hover popup
		map.on('pointermove', function (evt)
		{
		    var feature = map.forEachFeatureAtPixel(evt.pixel, function (feat, layer) {
		        return feat;
		    });
		    if (map.hasFeatureAtPixel(evt.pixel) === true)
		    {
		        if(selected != feature)
		        {
		            // Event coordinates
		            // popup.setPosition(evt.coordinate);
		            // Lon Lat coordinates
		           
		            
		            var position = ol.proj.transform([feature.get('lon'),feature.get('lat')], 'EPSG:4326', 'EPSG:3857');
		            if(feature.get('desc') != undefined){
		            content.innerHTML = feature.get('desc');
		            
			        console.log("feature.get('lon'):"+feature.get('lon'))

		            // Show marker on top
		         	   MarkerOnTop(feature, true);
		            // Show popup
		         	   popup.setPosition(position);
		            }
		        }
		    }
		    else
		    {
		        straitSource.getFeatures().forEach((f) => {
		            // Hide markers zindex 999
		            MarkerOnTop(f, false);
		        });
		        // Hide popup
		        popup.setPosition(undefined);
		    }

		});

		// Click popup
		map.on('click', function (evt)
		{
		    var feature = map.forEachFeatureAtPixel(evt.pixel, function (feat, layer) {
		        selected = feat;
		        return feat;
		    });
		    if (map.hasFeatureAtPixel(evt.pixel) === true)
		    {
		        // Event coordinates
		        // popup.setPosition(evt.coordinate);
		        // Lon Lat coordinates
		        var position = ol.proj.transform([feature.get('lon'),feature.get('lat')], 'EPSG:4326', 'EPSG:3857');
		        contentClick.innerHTML = feature.get('desc');
				
		        console.log("feature.get('lon')2:"+feature.get('lon'))
	            // Show marker on top
	            if(feature.get('lon') != undefined){
	         	   MarkerOnTop(feature, true);
	            // Show popup
	         	   popup.setPosition(position);
	            }

		    }
		    else
		    {
		        selected = null;
		         // Hide markers zindex 999
		        straitSource.getFeatures().forEach((f) => {
		            MarkerOnTop(f, false);
		        });
		        popupClick.setPosition(undefined);
		    }
		});

		function check(){
			var sc = document.search_form.search_check.value;
			var st = document.search_form.search_this.value;
			
			var regex = /[^0-9]/g;
			
			if(st==""){
				alert("검색어를 입력해주세요.");
				document.search_form.search_this.focus();
				return false;
			}
			
			if(sc == "service_num"){
				var result = st.replace(regex, "");
				
				var num = result.toString();
				numDigit = num.length;
				
				if(numDigit==7 || numDigit==8){
					return true;
				} else{
					alert("군번은 숫자 7자리 또는 8자리를 입력해주세요.");
					return false;
				}

			} else if(sc == "phone_num"){
				var result = st.replace(regex, "");
				
				var num = result.toString();
				numDigit = num.length;
				
				if(numDigit==11){
					return true;
				} else{
					alert("전화번호는 숫자 11자리를 입력해주세요.");
					return false;
				}
			}
			
			else return true;
		}
    
	    function regimentSelectChange(e) {
	    	
	    	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	    	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;
	    	var rc4 = ['전체'];

	    	var target = document.getElementById("regim_company");
	
	    	if(e.value == "28여단") var d = rc0;
	    	else if(e.value == "28-1대대") var d = rc1;
	    	else if(e.value == "28-2대대") var d = rc2;
	    	else if(e.value == "28-3대대") var d = rc3;
	    	else if(e.value == "전체") var d = rc4;

	
	    	target.options.length = 0;
	
	    	for (x in d) {
	    		var opt = document.createElement("option");
	    		opt.value = d[x];
	    		opt.innerHTML = d[x];
	    		target.appendChild(opt);
	    	}
	    }
	    
		function eRegimentSelectChange(e) {
	    	
	    	var tet0 = <%=tet0%>; var tet1 = <%=tet1%>;  
	    	var tet2 = <%=tet2%>;
	    	var tet3 = <%=tet3%>;
	    	var tet4 = ['전체'];
	    	
	    	var target = document.getElementById("equip_type");
	
	    	if(e.value == "28여단") var d = tet0;
	    	else if(e.value == "28-1대대") var d = tet1;
	    	else if(e.value == "28-2대대") var d = tet2;
	    	else if(e.value == "28-3대대") var d = tet3;
	    	else if(e.value == "전체") var d = tet4;

	    	target.options.length = 0;
	
	    	for (x in d) {
	    		var opt = document.createElement("option");
	    		opt.value = d[x];
	    		opt.innerHTML = d[x];
	    		target.appendChild(opt);
	    	}
	    }
    
		function goBack(){
			window.history.back();
		}		
		
		var search = ['personal', 'status', 'equip', 'vihicle', 'gis_setting','Scale'];
		
		function showSearch(id){
			
			if(document.getElementById(id).style.display == "none"){
				document.getElementById(id).style.display="block";
				for(i=0; i<search.length; i++){
					if(search[i] != id){
						document.getElementById(search[i]).style.display="none";
					}	
				}
			} else {
				document.getElementById(id).style.display="none";
			}
			
			//window.open("popup.jsp","popup","width=400, height=300, left=100, top=50");
			//var phoneNum = prompt("전화번호: ");
		}
		
		function searchName(){
			var phoneNum = document.getElementById("phoneNum").value;
		}

		
		// Show marker on top
		function MarkerOnTop(feature, show = false)
		{
		    var style = feature.getStyle();
		    if(show){
		        style.zIndex = 9999;
		        style.zIndex_ = 9999;
		    }else{
		        style.zIndex = 999;
		        style.zIndex_ = 999;
		    }
		    feature.setStyle(style);
		}

		
		function postData(url , data) {
			  // Default options are marked with *
			    return fetch(url, {
			        method: 'POST', // *GET, POST, PUT, DELETE, etc.
			        crossOrigin: true,
			        headers: {
			            'Content-Type': 'json',
			            // 'Content-Type': 'application/x-www-form-urlencoded',
			        },

			        body: data, // body data type must match "Content-Type" header
			    })
			    .then(response => response.json()); // parses JSON response into native JavaScript objects
			}
		
		

		
		function addPointGeom(data) {
			
			var seq = 0;
			
			data.forEach(function(item) { //iterate through array...

				seq++;
				
				var longitude = item.longitude, latitude = item.latitude, idx = item.idx
								, userKey = item.userKey, timestamp = item.timestamp
								, regiment = item.regiment, duty = item.duty, name = item.name
								, regimCompany = item.regimCompany, rank = item.rank
								, serviceNumber = item.serviceNumber;
				
				//var longitude = data.longitude, latitude = data.latitude, idx = data.idx
						//	, userKey = data.userKey, timestamp = data.timestamp;
				console.log(longitude + ":" + latitude + ":" + userKey + ":" + timestamp);
				var time = "<%=lastTimestamp%>";
				
				var pnt_data = ol.proj.fromLonLat([longitude, latitude]);

				var line = new ol.geom.LineString([pnt, pnt_data]);
				var distance = Math.round(line.getLength());
				
				if(distance < r){
					
					var MarkerIcon = new ol.style.Icon({
			            anchor: [0.5, 20],
			            anchorXUnits: 'fraction',
			            anchorYUnits: 'pixels',
			            src: 'image/marker_bl.png',
			            scale: 1.2
			        });
					
					
				} else {

					var MarkerIcon = new ol.style.Icon({
			            anchor: [0.5, 20],
			            anchorXUnits: 'fraction',
			            anchorYUnits: 'pixels',
			            src: 'image/marker_rd.png',
			            scale: 1.2
			        });
					
					//alert("경계를 넘었습니다.");
					console.log(data);
					if('<%=param2%>'=== 'geofon'){			

					
					$.ajax({
						url: 'http://110.10.130.51:5002/Emergency/EventStatus/EventStatusSave',
						contentType: "application/json; charset=utf-8",
						method: 'POST',
						data: JSON.stringify(data),
						dataType: "json",
						accept: "application/json",
						success: function(response) {
							// success handle
								console.log(JSON.stringify(data));
								console.log(JSON.stringify(response));
							},
						error: function(response) {
								console.log(JSON.stringify(response));
							}	
					});
					
					}

				}
				

				
				var iconFeature = new ol.Feature({
				    geometry: new ol.geom.Point(ol.proj.transform([longitude,latitude], 'EPSG:4326', 'EPSG:3857')),
				    type: 'Point',
				    lon: longitude,
				    lat: latitude,
				    desc: '<table style="white-space:nowrap;width:100%;text-align:left">'
					    + '<tr><td>' + userKey + '</td></tr>'
					    + '<tr><td>' + regimCompany + '</td></tr>'
					    + '<tr><td>' + rank + '&nbsp' + name + '&nbsp' + duty + '</td></tr>'
					    + '<tr><td>' + serviceNumber + '</td></tr>'
				    	+ '<tr><td>' + timestamp + '</td></tr>'
				    	+ '</table>'
				});
				
				iconStyle = new ol.style.Style({
				    image: MarkerIcon,
				    text: new ol.style.Text({
				        //scale: 1.5,
				        fill: new ol.style.Fill({
				          color: "0"
				        }),
				        stroke: new ol.style.Stroke({
				          color: "#fff",
				          width: 6
				        }),
				    	offsetY: -13
				      })
				});
					
					var iconStyle = new ol.style.Style({
					    image: MarkerIcon,
					});
					
					// Add icon style
					iconFeature.setStyle(iconStyle);
					straitSource.addFeature(iconFeature);
					MarkerOnTop(iconFeature, true);
			
		});		
		
	}


		addPointGeom(data);
	

		
    </script>
    
</body>
</html>