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
 
	String phoneNum = request.getParameter("phoneNum");

	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	Location lastLocation = new Location();
	
	Gson gson = new Gson();
	String multi_marker = "";
	String last_marker ="";
		
	locations = cd.getMobileStatus();
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
        #map{
        	width: auto;
            height: 1080px;
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
    </style>
    <!-- OpenLayers map -->
    <link rel="stylesheet" href="css/ol.css" type="text/css">
    <script src="js/ol.js"></script>
</head>

<body>

	<input type="button" value="병력" onclick="showSearch('status')"/>
	<input type="button" value="장비" onclick="showSearch('equip')"/>
	<input type="button" value="차량" onclick="showSearch('vihicle')"/>
	<input type="button" value="설정" onclick="showSearch('gis_setting')"/>
	<input type="button" value="개인" onclick="showSearch('personal')"/> 
	<button id="zoom-restore">reset</button><br>
	
	<div id="status" style="display:none">
	<form action="personalLocations3.jsp" method="get">
		<table>
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
				<td>세부소속</td>
				<td>
					<select id="regim_company" name="regim_company">
					<option value="전체">전체</option>
					</select>
				</td>
				<td><input type="submit" value="조회"></td>
			</tr>
		</table>
	</form>
	</div>

	<div id="equip" style="display:none">
	<form action="equipLocations2.jsp" method="get">
		<table>
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
				<td><input type="submit" value="조회"></td>
			</tr>
		</table>
	</form>
	</div>
	
	<div id="vihicle" style="display:none">
	TBD
	</div>
	
	<div id="gis_setting" style="display:none">
		<form action="circle.jsp" method="get">
		  <input type="radio" id="satellite_map" name="gis_setting" value="satellite_map" checked>
		  <label for="satellite_map">위성지도</label>
		  <input type="radio" id="geofence" name="gis_setting" value="geofence">
		  <label for="geofence">GeoFence</label>
		  <input type="submit" value="조회">
		</form>
	</div>
	
	<div id="personal" style="display:none">
		<form name="search_form" action="personalLocations4.jsp" method="get" onsubmit="return check()">
			<select id="search_check" name="search_check" >
   				<option value="phone_num">전화번호</option>
   				<option value="service_num">군번</option>
   				<option value="name">성명</option>
			</select>
			<input type="text" id="search_this" name="search_this" size="10">
			<input type="submit" value="조회">
		</form>
	</div>
	
	<!-- 
	<div id="test" style="display:none">
	<form action="javascript:searchName()" method="post">
		전화번호 : <input type="text" id="phoneNum" size="10" value="01028957223"> <input type="submit" value="전송">
	</form>
	</div>
	 -->

	<div id="map"></div>

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
  	<script src="js/map.js"></script>
 	
 	
    <script>
	     
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
				
				if(numDigit==7 || numDigit==7){
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
			} else if(sc == "name") {
				
				return true;
			}
			
			else return true;
		}
    
	    function regimentSelectChange(e) {
	    	
	    	var rc0 = <%=rc0%>; var rc1 = <%=rc1%>;  
	    	var rc2 = <%=rc2%>; var rc3 = <%=rc3%>;
	    	
	    	var target = document.getElementById("regim_company");
	
	    	if(e.value == "28여단") var d = rc0;
	    	else if(e.value == "28-1대대") var d = rc1;
	    	else if(e.value == "28-2대대") var d = rc2;
	    	else if(e.value == "28-3대대") var d = rc3;
	
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
	    	
	    	var target = document.getElementById("equip_type");
	
	    	if(e.value == "28여단") var d = tet0;
	    	else if(e.value == "28-1대대") var d = tet1;
	    	else if(e.value == "28-2대대") var d = tet2;
	    	else if(e.value == "28-3대대") var d = tet3;
	
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
		
		var search = ['personal', 'status', 'equip', 'vihicle', 'gis_setting'];
		
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
		
		function showNameSearch(){
			document.getElementById('name_select').style.display="block";
			
		}
		
		function searchName(){
			var phoneNum = document.getElementById("phoneNum").value;
		}
		
        var data = <%=multi_marker%>;
        
  	    var straitSource = new ol.source.Vector({ wrapX: true });
 	    var straitsLayer = new ol.layer.Vector({
 	        source: straitSource
 	    });

        // Instanciate a Map, set the object target to the map DOM id
		var map = new ol.Map({
			target: 'map',  // 위 index.html에 div id가 map인 엘리먼트에 맵을 표출
				layers: [
					viewLayer,viewLayer3,
					straitsLayer
				],
				view: new ol.View({
					center: ol.proj.fromLonLat(
							[126.77192, 37.654461]
					), 
					zoom: 11
				})
		});
        
		var view = map.getView();
        var zoom = view.getZoom();
        var center = view.getCenter();
        var rotation = view.getRotation();
        
        document.getElementById('zoom-restore').onclick = function(){
        	view.setCenter(center);
        	view.setRotation(rotation);
        	view.setZoom(zoom);
        }
      
        
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
		            content.innerHTML = feature.get('desc');
		            // Show marker on top
		            MarkerOnTop(feature, true);
		            // Show popup
		            popup.setPosition(position);
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
		        // Show marker on top
		        MarkerOnTop(feature, true);
		        // Show Popup
		        popupClick.setPosition(position);
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

		
		function addPointGeom(data) {
			
			var seq = 0;
			
			data.forEach(function(item) { //iterate through array...

				seq++;
				
				var longitude = item.longitude, latitude = item.latitude, idx = item.idx
								, userKey = item.userKey, timestamp = item.timestamp
								, regiment = item.regiment, duty = item.duty, name = item.name
								, regimCompany = item.regimCompany, rank = item.rank
								, serviceNumber = item.serviceNumber;
				console.log(longitude + ":" + latitude + ":" + userKey + ":" + timestamp);
				var time = "<%=lastTimestamp%>";
				
				if(timestamp == time){
					
				} else {
				
					var MarkerIcon = new ol.style.Icon({
			            anchor: [0.5, 20],
			            anchorXUnits: 'fraction',
			            anchorYUnits: 'pixels',
			            src: 'image/marker_bl.png',
			            scale: 1.2
			        });
					
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
					
					var iconStyle = new ol.style.Style({
					    image: MarkerIcon,
					});
					
					// Add icon style
					iconFeature.setStyle(iconStyle);
					straitSource.addFeature(iconFeature);
					MarkerOnTop(iconFeature, true);
	        
				}
			});		
			
		}

		
		addPointGeom(data);
	

		
    </script>
    
		

</body>
</html>