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
	rc0 = gson.toJson(rc_0); rc1 = gson.toJson(rc_1); 
	rc2 = gson.toJson(rc_2); rc3 = gson.toJson(rc_3);
	
	ArrayList<String> totalEquipReg = cd.getTotalEquipReg();
	
	ArrayList<String> tel_0 = cd.getTotalEquipLocation("28여단");
	ArrayList<String> tel_1 = cd.getTotalEquipLocation("28-1대대");
	ArrayList<String> tel_2 = cd.getTotalEquipLocation("28-2대대");
	ArrayList<String> tel_3 = cd.getTotalEquipLocation("28-3대대");	
	
	String tel0; String tel1; String tel2; String tel3;
	tel0 = gson.toJson(tel_0); tel1 = gson.toJson(tel_1); 
	tel2 = gson.toJson(tel_2); tel3 = gson.toJson(tel_3);
	
	
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
    	}
        #map{
        	width: 100%;
            height: 605px;
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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/css/ol.css" type="text/css">
    <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/build/ol.js"></script>
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
			<tr><td>소속</td>
			<td><select id="reg" name ="reg" onchange="regimentSelectChange(this)">
				<option>전체</option>
				<%for(int i=0; i<mobileStatusReg.size(); i++) {%>
				<option value="<%=mobileStatusReg.get(i)%>"><%=mobileStatusReg.get(i)%></option>
				<%} %>
			</select></td></tr><tr>
			<td>세부소속</td>
			<td><select id="regim_company" name="regim_company">
				<option value="전체">전체</option>
			</select></td></tr><tr>
			<td>기기구분</td>
			<td><select id="device" name="device">
				<option value="전체">전체</option>
				<option value="Phone-Bcon">Phone-Bcon</option>
				<option value="Phone-GPS">Phone-GPS</option>
				<option value="Watch-Bcon">Watch-Bcon</option>
				<option value="Watch-GPS">Watch-GPS</option>
			</select></td>
			<tr></tr>
			<td><input type="submit" value="조회"></td></tr>
		</table>
	</form>
	</div>

	<div id="equip" style="display:none">
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
			</tr>
			<tr>
				<td class="block">장비위치</td>
				<td>
					<select id="equip_location" name="equip_location">
						<option value="전체">전체</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="block">체계명</td>
				<td>
					<select id="mission_type" name="mission_type">
					<%for(int i=0; i<missionTypeList.size(); i++){ %>
	    				<option value=<%=missionTypeList.get(i) %>><%=missionTypeList.get(i) %></option>
					<%}%>
					</select>
				</td>
			</tr>
			<tr>
				<td class="block">장비구분</td>
				<td>
					<select id="equip_type" name="device">
					<%for(int i=0; i<equipTypeList.size(); i++){ %>
	    				<option value=<%=equipTypeList.get(i) %>><%=equipTypeList.get(i) %></option>
					<%}%>
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="조회"></td>
			</tr>
		</table>
	</div>
	
	<div id="vihicle" style="display:none">
	TBD
	</div>
	
	<div id="gis_setting" style="display:none">
		<form>
		  <input type="radio" id="satellite_map" name="gis_setting" value="satellite_map">
		  <label for="satellite_map">위성지도</label><br>
		  <input type="radio" id="military_map" name="gis_setting" value="military_map">
		  <label for="military_map">군사지도</label><br>
		  <input type="radio" id="geofence" name="gis_setting" value="geofence">
		  <label for="geofence">GeoFence</label><br>
		  <input type="radio" id="cctv" name="gis_setting" value="cctv">
		  <label for="cctv">CCTV</label><br>
		  <input type="radio" id="operations" name="gis_setting" value="operations">
		  <label for="operations">작전구역</label><br>
		  <input type="submit" value="조회">
		</form>
	</div>
	
	<div id="personal" style="display:none">
	<form action="personalLocations2.jsp" method="get">
		전화번호 : <input type="text" name="phoneNum" size="10" value="01028957223"> <input type="submit" value="조회">
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
 	
 	
    <script>
	     
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
	    	
	    	var tel0 = <%=tel0%>; var tel1 = <%=tel1%>;  
	    	var tel2 = <%=tel2%>; var tel3 = <%=tel3%>;
	    	
	    	var target = document.getElementById("equip_location");
	
	    	if(e.value == "28여단") var d = tel0;
	    	else if(e.value == "28-1대대") var d = tel1;
	    	else if(e.value == "28-2대대") var d = tel2;
	    	else if(e.value == "28-3대대") var d = tel3;
	
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
					new ol.layer.Tile({
						source: new ol.source.OSM()
					}),
					straitsLayer
				],
				view: new ol.View({
					center: ol.proj.fromLonLat(
							[<%=locations.get(0).getLongitude()%>, <%=locations.get(0).getLatitude()%>]
					), 
					zoom: 18
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
				
				//var longitude = item.Lon, latitude = item.Lat, icon = item.Icon, desc = item.Desc;
				var longitude = item.longitude, latitude = item.latitude, idx = item.idx
							, userKey = item.userKey, timestamp = item.timestamp;
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
					    desc: '<table style="white-space:nowrap;width:100%;text-align:center">'
						    + '<tr><td>' + userKey + '</td></tr>'
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
</body>
</html>