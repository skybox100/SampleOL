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
	String serviceNum = request.getParameter("serviceNum");
	
	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	Location lastLocation = new Location();
	
	Gson gson = new Gson();
	String multi_marker = null;
	String last_marker = null;
	
	locations = cd.getLocationsByService(serviceNum);
	lastLocation = cd.getLastLocation(serviceNum);
	String lastTimestamp = lastLocation.getTimestamp();
	multi_marker = gson.toJson(locations);
	last_marker = gson.toJson(lastLocation);
	
	System.out.println(locations.toString());
	System.out.println(lastLocation.toString());
	
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
            filter: drop-shadow(0 1px 4px rgba(0,0,0,0.1));
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
        .tooltip {
		  position: relative;
		  padding: 3px;
		  background: rgba(0, 0, 0, 0.5);
		  color: white;
		  opacity: 0.7;
		  white-space: nowrap;
		  font: 10pt sans-serif;
		}
        .tooltip-inner {
        white-space: nowrap;
      }
    </style>
    <!-- OpenLayers map -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/css/ol.css" type="text/css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/build/ol.js"></script>

</head>

<body>
	
	<div style="text-align:center">
		<font size="2">
		<%=cd.getCodeName("RegimCompany", locations.get(0).getRegiment())%>&nbsp;
		<font color="blue">
		<%=cd.getCodeName("rank", locations.get(0).getRank())%>&nbsp;
		<%=locations.get(0).getName()%>&nbsp;
		</font>
		<%=locations.get(0).getDuty()%>&nbsp;
		<%=serviceNum%>&nbsp;
		</font>
		<button id="zoom-restore">reset</button>
	</div>
	
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
    	    	
        var data = <%=multi_marker%>;
        var last_data = <%=last_marker%>;
    
  	    var straitSource = new ol.source.Vector({ wrapX: true });
 	    var straitsLayer = new ol.layer.Vector({
 	        source: straitSource
 	    });

        // Instanciate a Map, set the object target to the map DOM id
		var map = new ol.Map({
			target: 'map',  // ??? index.html??? div id??? map??? ??????????????? ?????? ??????
				layers: [
					new ol.layer.Tile({
						source: new ol.source.OSM()
					}),
					straitsLayer
					
				],
				view: new ol.View({
					center: ol.proj.fromLonLat(
							[<%=lastLocation.getLongitude()%>, <%=lastLocation.getLatitude()%>]
					),  // ?????? ??????????????? ??? ????????? ?????? ??????(??????) ??????-?????????
					//rotation: Math.PI / 5,
					zoom: 18  // ??? ????????? ??? ????????? ?????? ?????? (????????? ???????????? ?????? ???)
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
        
        /*
		var tooltip = document.getElementById('tooltip');
		var overlay = new ol.Overlay({
		  element: tooltip,
		  offset: [10, 0],
		  positioning: 'bottom-left'
		});
		map.addOverlay(overlay);
        
		function displayTooltip(evt) {
			  var pixel = evt.pixel;
			  var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
			    return feature;
			  });
			  tooltip.style.display = feature ? '' : 'none';
			  if (feature) {
			    overlay.setPosition(evt.coordinate);
			    tooltip.innerHTML = feature.get('name');
			  }
			};

			map.on('pointermove', displayTooltip);
			*/
		/*
		$('.ol-zoom-in, .ol-zoom-out').tooltip({
		  placement: 'right',
		  container: '#map',
		});
		$('.ol-rotate-reset, .ol-attribution button[title]').tooltip({
		  placement: 'left',
		  container: '#map',
		});
        */
		
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
				var longitude = item.longitude, latitude = item.latitude, userKey = item.userKey, timestamp = item.timestamp;
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
						    + '<tr><td><b>' + seq + '</b>&nbsp&nbsp' + userKey + '</td></tr>'
					    	+ '<tr><td>' + timestamp + '</td></tr>'
					    	+ '</table>' 
					});
					    		
					
					var iconStyle = new ol.style.Style({
					    image: MarkerIcon,
					    text: new ol.style.Text({
					    	font: '7px serif',
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
					
				}
	        
			});		
			
		}
		
		
		function addLastPoint(data) { 

			//var longitude = item.Lon, latitude = item.Lat, icon = item.Icon, desc = item.Desc;
			var longitude = data.longitude, latitude = data.latitude
						, userKey = data.userKey, timestamp = data.timestamp;
			console.log("last point: " + longitude + ":" + latitude + ":" + userKey + ":" + timestamp);
			
			var MarkerIcon = new ol.style.Icon({
	            anchor: [0.5, 20],
	            anchorXUnits: 'fraction',
	            anchorYUnits: 'pixels',
	            src: 'image/marker_rd.png',
	            scale: 1.2
	        });
			
			var iconFeature = new ol.Feature({
			    geometry: new ol.geom.Point(ol.proj.transform([longitude,latitude], 'EPSG:4326', 'EPSG:3857')),
			    type: 'Point',
			    lon: longitude,
			    lat: latitude,
			    desc: '<table style="white-space:nowrap;width:100%;text-align:center">'
				    + '<tr><td><b>1</b>&nbsp&nbsp' + userKey + '</td></tr>'
			    	+ '<tr><td>' + timestamp + '</td></tr>'
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
      
		};		
		
		addPointGeom(data);
		addLastPoint(last_data);

		
		
		
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