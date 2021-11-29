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
System.out.println("personalLocations3");
	String param = "geofence";

	if(request.getParameter("gis_setting")!= null){
		param = request.getParameter("gis_setting") ;
	}
	
	String reg = request.getParameter("reg");
	String rc = request.getParameter("regim_company");
	
	String regp = request.getParameter("reg");
	String rcp = request.getParameter("regim_company");
	
	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	
	Gson gson = new Gson();
	String multi_marker ="";
	
	if(reg.equals("전체") && rc.equals("전체")){
			
	} else if(rc.equals("전체")){
		reg = cd.getCodeID("Regiment", reg);	
	} else{
		reg = cd.getCodeID("Regiment", reg);
		rc = cd.getCodeID("RegimCompany", rc);
	}
	
	/*
	if(reg.equals("전체") && device.equals("전체")){
		
	} else if(device.equals("전체")){
		if(rc.equals("전체")){
			reg = cd.getCodeID("Regiment", reg);	
		} else {
			reg = cd.getCodeID("Regiment", reg);
			rc = cd.getCodeID("RegimCompany", rc);
		}
	} else if(reg.equals("전체")) {
		device = cd.getCodeID("IsDevice", device);
	} else{
		if(rc.equals("전체")){
			reg = cd.getCodeID("Regiment", reg);
			device = cd.getCodeID("IsDevice", device);
		}else{
			reg = cd.getCodeID("Regiment", reg);
			rc = cd.getCodeID("RegimCompany", rc);
			device = cd.getCodeID("IsDevice", device);
		}
	
	}
	*/
	
	locations = cd.getMobileStatus(reg, rc);
	multi_marker=gson.toJson(locations);
	
	int cnt = locations.size();
		
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
        #box1{
        	width: 150px;
        }
        #box2{
	        width: 175px;
	        float: right;
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
    		height: 34px;
    	}
    	
    	#rcp_frm{
    		position: absolute;
    		top: 0;
    		margin-top: 6px;
    		margin-left: 4px;
    	}
    	
    	#rcp_frm2{
    		height: 30px;
    		font-size: 15px;
			position: absolute;
			right: 0;
			margin-top: 6px;
			margin-right: 125px;
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
        
 		#top_div{
			position: fixed; /* 이 부분을 고정 */
	  		top:30px; 
  			width: 100%;
			white-space: nowrap;
			width: 100%;
  			background: white;
  			height: 30px;
			padding-bottom: 4px;
		}       
    	
         .gis_setting3{	
			position: absolute;
        	left:0;
        	bottom:12px;
        	margin-right: 4px;
			
        }   
        .ol-tooltip *{
            font-family: Arial, Helvetica, sans-serif;
            font-weight: 300
        }
        .ol-tooltip {
            display: flex; 
            overflow: hidden;
            padding: 3px; 
            margin: 3px 0px;
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
        .box1 {float: left;}
		.box2 {float: right;}
        .ol-zoom ol unselectable ol-control{
        	display:none;
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
		<form action="personalLocations3.jsp" id="locations" method="get">
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
				<td style="display: none">
					<input type="hidden" name="reg" value="<%=regp %>">
					<input type="hidden" name="regim_company" value="<%=rcp %>">
				</td>				
			</tr>
		</table>

		</form>
	</div>	
	<div id="top_div" style="white-space:nowrap; ">
		
		<form action="detail.jsp" id='frm' method="get" onsubmit="return goDetail()">
			<span id="rcp_frm"><%=regp%>/<%=rcp %></span>
			<a id="rcp_frm2" href="#" onclick="document.getElementById('frm').submit();">인원수: <%=cnt %></a>
			<input type="hidden" name="regp" value="<%=regp%>">
			<input type="hidden" name="rcp" value="<%=rcp%>">
			<input type="button" value="reset" id="zoom-restore">
			<input type="button" value=" 이전 " id="goback">
		</form>
		
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
 	
 	
    <script>
    $("input:radio[name='gis_setting']:radio[value='<%=param%>']").attr("checked",true);

   
	     
    
    $(document).ready(function() 
    		{ 
    		    $("input:radio[name=gis_setting]" ).click(function() 
    		    { 
    		    	location.replace("personalLocations3.jsp?reg=<%=regp%>&regim_company=<%=rcp%>&gis_setting="+$('input[name=gis_setting]:checked').val());

    		    	
    		    })

    		});
    

    	function submit(){
    		document.getElementById('locations').submit();
 
    	}	
    	
    
        var data = <%=multi_marker%>;
        
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
  					viewLayer,viewLayer3,straitsLayer
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
		    	
		    	var cnt=0;
		    	var multi='';
		    	var distance=0;
		    	data.forEach(function(item) {
		    		
		    		
            		var pnt_data = ol.proj.fromLonLat([feature.get('lon'),feature.get('lat')]);
            		var pnt_data2 = ol.proj.fromLonLat([item.longitude,item.latitude]);

					var line = new ol.geom.LineString([pnt_data, pnt_data2]);
					distance = Math.round(line.getLength());
					console.log("distance:" +distance);
					if(distance <100 & cnt <4 & distance >0){
						cnt++;
						multi +='<table style="white-space:nowrap;text-align:left;">'
					    	+ '<tr ><td Colspan="2">' + item.timestamp + '&nbsp&nbsp&nbsp&nbsp&nbsp'+item.isDevice +'</td></tr>'
						    + '<tr><td>전화번호&nbsp&nbsp</td><td style="text-align:right;">'+item.MobileNumber+'</td></tr>'
						    + '<tr><td>소속</td><td style="text-align:right;">'+item.regimCompany+'</td></tr>'
						    + '<tr><td>계급성명</td><td style="text-align:right;">'+item.rank+'&nbsp'+item.name+'</td></tr>'
						    + '<tr><td>군번</td><td style="text-align:right;">'+item.serviceNumber+'</td></tr>'
						    + '<tr><td>'+item.equipLocation+'</td><td style="text-align:right;">'+item.roomName+'</td></tr>'
					    	+ '</table><br>';

					}
					
					
		    	});	
		    	
		        if(selected != feature)
		        {
		            // Event coordinates
		            // popup.setPosition(evt.coordinate);
		            // Lon Lat coordinates
		           
		            
		            var position = ol.proj.transform([feature.get('lon'),feature.get('lat')], 'EPSG:4326', 'EPSG:3857');
		            if(feature.get('desc') != undefined){
		            	if(cnt >=2){
		            		content.innerHTML= multi+feature.get('desc');
		            	}else{
				            content.innerHTML = feature.get('desc');	            		
		            	}
		            
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
			
			data.forEach(function(item) { //iterate through array...
				
				var longitude = item.longitude, latitude = item.latitude, idx = item.idx
				, userKey = item.userKey, timestamp = item.timestamp
				, regiment = item.regiment, regimCompany = item.regimCompany
				, serviceNumber = item.serviceNumber,isDevice=item.isDevice
				, duty = item.duty, name = item.name, rank = item.rank
				,mobileNumber=item.MobileNumber,roomName=item.roomName,equipLocation=item.equipLocation;
				console.log(longitude + ":" + latitude + ":" + userKey + ":" + timestamp + ":" + regiment  
						+ ":" + regimCompany  + ":" + serviceNumber  + ":" + isDevice  + ":" + duty  + ":" + 
						name + ":" + rank  + ":" + mobileNumber  + ":" + roomName + ":" +equipLocation );
								
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
				        text: 'P',
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
	        
			});		
			
		}
		
		addPointGeom(data);
	

		
    </script>
    
		

</body>
</html>