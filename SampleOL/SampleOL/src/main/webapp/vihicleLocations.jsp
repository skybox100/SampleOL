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

	//String serviceNum = request.getParameter("serviceNum");
	
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
            filter: drop-shadow(0 1px 4px rgba(0,0,0,0.1));
            font-size: 11px;
            padding: 10px;
            border-radius: 10px;
            border: 1px solid #cccccc2a;
            bottom: 12px;
            transform: translate(-50%, 0%);
            margin-bottom: 10px;
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
            left: 120px;
            margin-left: -16px;
        }

        .ol-popup:before {
            border-top-color: #cccccc2a;
            border-width: 11px;
            left: 120px;
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
 		.ol-control{
 		    display: none;
 		}
    </style>
    <!-- OpenLayers map -->
    <script src="css/ol.css"></script>
    <script src="js/ol.js"></script>
</head>

<body>

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
         
    var data = [{"userKey":"+821028957223","idx":"637553079145651859","latitude":"37.65474622","longitude":"126.7719083"}];
        
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
      
       
        
		// Popup showing the position the hovered marker
		var container = document.getElementById('popup');
		var popup = new ol.Overlay({
		    element: container,
		    autoPan: true,
		    autoPanAnimation: {
		        duration: 450
		    }
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

				//var longitude = item.Lon, latitude = item.Lat, icon = item.Icon, desc = item.Desc;
				var longitude = item.longitude, latitude = item.latitude, idx = item.idx
							, userKey = item.userKey, timestamp = item.timestamp;
				console.log(longitude);
				console.log(latitude);
				console.log(idx);
				console.log(userKey);
				console.log(timestamp);
				
				var MarkerIcon = new ol.style.Icon({
		            anchor: [0.5, 50],
		            anchorXUnits: 'fraction',
		            anchorYUnits: 'pixels',
		            src: 'image/marker.png'
		            ,scale: 0.5
		        });
				
				var iconFeature = new ol.Feature({
				    geometry: new ol.geom.Point(ol.proj.transform([longitude,latitude], 'EPSG:4326', 'EPSG:3857')),
				    type: 'Point',
				    lon: longitude,
				    lat: latitude,
				    desc: '<table style="white-space:nowrap;width:100%;">'
				    	+ '<tr><td>위도</td><td style="text-align:right;">' + latitude + '</td></tr>'
				    	+ '<tr><td>경도</td><td style="text-align:right;">' + longitude + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">전화번호&nbsp</td><td style="text-align:right;">' + userKey + '</td></tr>'
				    	+ '<tr><td>시간</td><td style="text-align:right;">' + timestamp + '</td></tr>'
				    	+ '</table>'
				});
				    		
				
				iconStyle = new ol.style.Style({
				    image: MarkerIcon,
				    text: new ol.style.Text({
				    	font: '7px bold',
				        text: 'C',
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
	        
			});		
			
		}

		addPointGeom(data);
	
		
    </script>
    
		

</body>
</html>