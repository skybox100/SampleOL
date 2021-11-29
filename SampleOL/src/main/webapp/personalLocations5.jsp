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
System.out.println("personalLocations5");
	String param = "geofence";

	if(request.getParameter("gis_setting")!= null){
		param = request.getParameter("gis_setting") ;
	}
	String regp = request.getParameter("regp");
	String rcp = request.getParameter("rcp");
	
	String sc = request.getParameter("search_check");
	String st = request.getParameter("search_this");

	System.out.println(sc);
	System.out.println(st);

	
	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	Location lastLocation = new Location();
	ArrayList<Location> locations_member = new ArrayList<Location>();

	Gson gson = new Gson();
	String multi_marker = "";
	String last_marker ="";
	String lastTimestamp = "";
	List<String> rc2 = new ArrayList<>();
	List<String> rank2 = new ArrayList<>();
	List<String> name2 = new ArrayList<>();
	List<String> duty2 = new ArrayList<>();
	List<String> serviceNumber2 = new ArrayList<>();
	String rc = "";
	String rank = "";
	String name = "";
	String duty = "";
	String serviceNumber = "";
	int chk = 1;
	int subnum=1;
	
	if(sc.equals("phone_num")){
		
		String rest = st.replaceAll("[^0-9]","");
		
		ArrayList<Location> s = cd.getMobileStatusByMobile(st);
		
		if(s.size() == 0){
			chk = 0;
		} else {
			locations = cd.getLocationsByUser(rest);
			lastLocation = cd.getLastLocationByUser(rest);
			
			System.out.println("locations: " + locations.toString());
			System.out.println("lastLocation: " + lastLocation.toString());
			
		}
		
	} else if(sc.equals("service_num")){
		
		String rest = st.replaceAll("[^0-9]","");
		
		String str1 = rest.substring(0, 2);
		String str2 = rest.substring(2);
		
		st = str1 + "-" + str2;
		System.out.println(st);
		
		ArrayList<Location> s = cd.getMobileStatusByService(st);
		
		if(s.size() == 0){
			chk = 0;
		} else {
			locations = cd.getLocationsByUser(s.get(0).getUserKey());
			lastLocation = cd.getLastLocationByUser(s.get(0).getUserKey());
		}
		
	} else if(sc.equals("name")) {
		ArrayList<Location> s = cd.getMobileStatusByName(st);
		//subnum=s.size();
		System.out.println("s.size(): " + s.size());

		if(s.size() == 0){
			
			chk = 0;
			//locations = cd.getLocationsByUser("01029215835");
			//lastLocation = cd.getLastLocationByUser("01029215835");

		}else if(s.size() == 1){
			locations = cd.getLocationsByUser(s.get(0).getUserKey());
			lastLocation = cd.getLastLocationByUser(s.get(0).getUserKey());
		} else{
			for(int i=0;i<s.size();++i){
				//locations = cd.getLocationsByUser(s.get(i).getUserKey());
				lastLocation = cd.getLastLocationByUser(s.get(i).getUserKey());
				locations_member.add(cd.getLastLocationByUser(s.get(i).getUserKey()));
			}
			subnum=locations_member.size();

		}
		
	}

	try{
		lastTimestamp = lastLocation.getTimestamp();
		multi_marker = gson.toJson(locations);
		last_marker = gson.toJson(lastLocation);
		System.out.println("subnum="+subnum);
		if(subnum==1){
				rc = locations.get(0).getRegiment();
				rank = locations.get(0).getRank();
				name = locations.get(0).getName();
				duty = locations.get(0).getDuty();
				serviceNumber = locations.get(0).getServiceNumber();
		}else {
			for (int i = 0; i < subnum; ++i) {
	    		rc2.add(cd.getCodeName("RegimCompany", locations_member.get(i).getRegiment()));
				rank2.add(cd.getCodeName("rank", locations_member.get(i).getRank()));
				serviceNumber2.add(locations_member.get(i).getServiceNumber());
				name2.add(locations_member.get(i).getName());
			}
			
		}
	} catch(Exception e){
		e.printStackTrace();
	}
	
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
        #zoom-restore{
    		height: 30px;
    		font-size: 15px;
			position: absolute;
			right: 0;
			margin-right: 4px;

    	}
    	#goback{
    		height: 30px;
    		font-size: 15px;
			position: absolute;
			right: 0;
			margin-right: 60px;

    	}
    	
 		#frm{
			position: fixed; /* 이 부분을 고정 */
	  		top:30px; 
  			width: 100%;
			white-space: nowrap;
			width: 100%;
  			background: white;
    		height: 34px;
			padding-bottom: 4px;
		}       
    	
    	#frm_label{
    		position: absolute;
    		top: 0;
    		margin-top: 6px;
    		margin-left: 4px;

    	}
    	
    	.top{
    		position: fixed; /* 이 부분을 고정 */
	  		top:0; 
  			width: 100%;
  			background: white;
  			height: 36px;
  			padding-top: 2px;
			white-space: nowrap;		
    	}
    	
        #gis_setting2{	
			position: absolute;
        	right:0;
        	bottom:14px;
        	margin-right: 4px;
        }    	
        

    	
         .gis_setting3{	
			position: absolute;
        	left:0;
        	bottom:12px;
        	margin-right: 4px;
			
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
	<div id="map"></div>
	<div class="top">
		<form action="personalLocations5.jsp" id="locations" method="get">
		<table>
			<tr>
				<td class="gis_setting3">
					<font size="1px">
					<input type="radio" id="geofence" name="gis_setting" class="gis_setting" value="geofence" checked>
		  			<label for="geofence">군사</label>
		 			 <input type="radio" id="satellite_map" name="gis_setting" class="gis_setting" value="satellite_map" >
		 			 <label for="satellite_map">위성</label>		 			
					
					</font>		  			
				</td>
				<td>
				<font size="1px" style="font-weight: bold; display:none;" id="gis_setting2">
		 			<input type="radio" id="geofon" name="gis_setting2" class="gis_setting2" value="geofon">
		  			<label for="geofon">GeoF-ON</label>
		 			<input type="radio" id="geofoff" name="gis_setting2" class="gis_setting2" value="geofoff" checked>
		 			<label for="geofoff">GeoF-OFF</label>
		 		</font>
				</td>
				<td style="display: none">
					<input type="hidden" name="search_check" value="<%=sc %>">
					<input type="hidden" name="search_this" value="<%=st %>">
				</td>
				<td style="display: none">
					<input type="hidden" name="regp" value="<%=regp %>">
					<input type="hidden" name="rcp" value="<%=rcp %>">
				</td>
			</tr>
		</table>

		</form>
	</div>	


<%
	if(subnum > 1){
		for(int i=0; i<locations_member.size();++i){
%>
	<div style="white-space:nowrap; ">
	<%=rc2.get(i)%>&nbsp;
	<%=rank2.get(i)%>&nbsp;
	<a href="personalLocations5.jsp?search_check=service_num&search_this=<%=serviceNumber2.get(i)%>">
	<%=name2.get(i)%>
	</a>&nbsp;
	<%=serviceNumber2.get(i)%>&nbsp;<br>
	
<%
		}
%>
	<br>
	<input type="button" value="reset" id="zoom-restore">
	<input type="button" value=" 이전 " id="goback">
	</div>
<%
	}else{
%>
	<div id="frm" style="white-space:nowrap; ">
	<a id="frm_label">
	<%=rc%>&nbsp;
	<font color="blue">
	<%=rank%>&nbsp;
	<%=name%>&nbsp;
	</font>
	</a>
	<input type="button" value="reset" id="zoom-restore">
	<input type="button" value=" 이전 " id="goback">
	</div>
	
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
 	<%
		}
	%>
 	
    <script>
	    
    $("input:radio[name='gis_setting']:radio[value='<%=param%>']").attr("checked",true);

   
	     
    
    $(document).ready(function() 
    		{ 
    		    $("input:radio[name=gis_setting]").click(function() 
    		    { 
    		    	location.replace("personalLocations5.jsp?search_check=<%=sc%>&search_this=<%=st%>&gis_setting="+$('input[name=gis_setting]:checked').val());
    		    })
    		});
    

    	function submit(){
    		document.getElementById('locations').submit();
 
    	}
    
    	var chk = <%=chk%>;

    	if(chk==0){
    		alert("검색 결과가 없습니다.");
    		location.href = document.referrer;
    	}
    
		function goBack(){
    		location.href = "location.jsp";
		}
		
        var data = <%=multi_marker%>;
        var last_data = <%=last_marker%>;
        
        var rc = "<%=rc%>";
    	var rank = "<%=rank%>";
        
  	    var straitSource = new ol.source.Vector({ wrapX: true });
 	    var straitsLayer = new ol.layer.Vector({
 	        source: straitSource
 	    });

   	   if('<%=param%>'=='geofence' ){

 	        // Instanciate a Map, set the object target to the map DOM id
 			var map = new ol.Map({
 				target: 'map',  // 위 index.html에 div id가 map인 엘리먼트에 맵을 표출
 					layers: [
 						viewLayer,straitsLayer
 					],
 					view: new ol.View({
 						center: ol.proj.fromLonLat(
 								//[126.77192, 37.654461]
 								[last_data.longitude,last_data.latitude]

 						), 
 						zoom: 11
 					})
 			});
	 	  }else if('<%=param%>'=='satellite_map'){
 	 	// Instanciate a Map, set the object target to the map DOM id
 	 		var map = new ol.Map({
 	 		 target: 'map',  // 위 index.html에 div id가 map인 엘리먼트에 맵을 표출
 				layers: [
 					viewLayer,viewLayer3,straitsLayer
 				],
 				view: new ol.View({
 					center: ol.proj.fromLonLat(
 							//[126.77192, 37.654461]
								[last_data.longitude,last_data.latitude]

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
        document.getElementById('goback').onclick = function(){
			window.history.back();
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
		map.on('click', function (evt)
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
							, userKey = item.userKey, timestamp = item.timestamp
							, regiment = item.regiment, regimCompany = item.regimCompany
							, serviceNumber = item.serviceNumber,isDevice=item.isDevice
							, duty = item.duty, name = item.name, rank = item.rank
							,mobileNumber=item.MobileNumber,roomName=item.roomName,equipLocation=item.equipLocation;
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
					    desc: '<table style="white-space:nowrap;text-align:left;">'
					    	+ '<tr ><td>' + seq +'</td></tr>'
					    	+ '<tr ><td Colspan="2">' + timestamp + '&nbsp&nbsp&nbsp&nbsp&nbsp'+isDevice +'</td></tr>'
						    + '<tr><td>전화번호&nbsp&nbsp</td><td style="text-align:right;">'+mobileNumber+'</td></tr>'
						    + '<tr><td>소속</td><td style="text-align:right;">'+regimCompany+'</td></tr>'
						    + '<tr><td>계급성명</td><td style="text-align:right;">'+rank+'&nbsp'+name+'</td></tr>'
						    + '<tr><td>군번</td><td style="text-align:right;">'+serviceNumber+'</td></tr>'
						    + '<tr><td>'+equipLocation+'</td><td style="text-align:right;">'+roomName+'</td></tr>'
					    	+ '</table>'
					});
					
					var iconStyle = new ol.style.Style({
					    image: MarkerIcon,
					    text: new ol.style.Text({
					    	font: '7px bold',
					        text: seq + '',
					        //scale: 1.5,
					        fill: new ol.style.Fill({
					          color: "0"
					        }),
					        stroke: new ol.style.Stroke({
					          color: "#fff",
					          width: 2
					        }),
					    	offsetY: -13
					      })
					});
					
					// Add icon style
					iconFeature.setStyle(iconStyle);
					straitSource.addFeature(iconFeature);
					MarkerOnTop(iconFeature, true);
	        
				}
			});		
			
		}

		function addLastPoint(data) { 

			//var longitude = data.Lon, latitude = data.Lat, icon = data.Icon, desc = data.Desc;
				var longitude = data.longitude, latitude = data.latitude, idx = data.idx
							, userKey = data.userKey, timestamp = data.timestamp
							, regiment = data.regiment, regimCompany = data.regimCompany
							, serviceNumber = data.serviceNumber,isDevice=data.isDevice
							, duty = data.duty, name = data.name, rank = data.rank
							,mobileNumber=data.MobileNumber,roomName=data.roomName,equipLocation=data.equipLocation;
				console.log(longitude + ":" + latitude + ":" + userKey + ":" + timestamp);
								
			var MarkerIcon = new ol.style.Icon({
	            anchor: [0.5, 20],
	            anchorXUnits: 'fraction',
	            anchorYUnits: 'pixels',
	            src: 'image/marker_rd.png'
	            ,scale: 1.2
	        });
			
			var iconFeature = new ol.Feature({
			    geometry: new ol.geom.Point(ol.proj.transform([longitude,latitude], 'EPSG:4326', 'EPSG:3857')),
			    type: 'Point',
			    lon: longitude,
			    lat: latitude,
			    desc: '<table style="white-space:nowrap;text-align:left;">'
			    	+ '<tr ><td>1</td></tr>'
			    	+ '<tr ><td Colspan="2">' + timestamp + '&nbsp&nbsp&nbsp&nbsp&nbsp'+isDevice +'</td></tr>'
				    + '<tr><td>전화번호&nbsp&nbsp</td><td style="text-align:right;">'+mobileNumber+'</td></tr>'
				    + '<tr><td>소속</td><td style="text-align:right;">'+regimCompany+'</td></tr>'
				    + '<tr><td>계급성명</td><td style="text-align:right;">'+rank+'&nbsp'+name+'</td></tr>'
				    + '<tr><td>군번</td><td style="text-align:right;">'+serviceNumber+'</td></tr>'
				    + '<tr><td>'+equipLocation+'</td><td style="text-align:right;">'+roomName+'</td></tr>'
			    	+ '</table>'
			});
			    		
			var iconStyle = new ol.style.Style({
			    image: MarkerIcon,
			    text: new ol.style.Text({
			    	font: '7px serif',
			        text: '1',
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
			
			// Add icon style
			iconFeature.setStyle(iconStyle);
			straitSource.addFeature(iconFeature);
			MarkerOnTop(iconFeature, true);
		};		
		
		addPointGeom(data);
		addLastPoint(last_data);
	

		
    </script>
    
		

</body>
</html>
