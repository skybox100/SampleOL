<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<!DOCTYPE html>

<%@ page import="com.SampleOL.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="net.sf.json.*"%>
<%@ page import="com.google.gson.*"%>
<%

	request.setCharacterEncoding("euc-kr");
	String param = "geofence";

	if(request.getParameter("gis_setting")!= null ){
		param = request.getParameter("gis_setting") ;
	}	
	String uuid = request.getParameter("uuid");
	
	DBConnection cd = new DBConnection();
	ArrayList<Beacons> beaconLocations = new ArrayList<Beacons>();
	Gson gson = new Gson();
	String multi_marker = null;	
	String multi_marker2 = null;	

	String regimentName = "";
	String equipTypeName ="";
	
	try {
		
		beaconLocations = cd.getBeaconById(uuid);
		multi_marker = gson.toJson(beaconLocations);
		

		
	} catch(Exception e) {
		
		beaconLocations = cd.getBeaconsList("전체","전체");
		multi_marker = gson.toJson(beaconLocations);
		
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
        	top:0;
    		height: 30px;
    		font-size: 13px;
			position: absolute;
			right: 0;
			margin-right: 4px;

    	}
    	#goback{
    		top:0;
    		height: 30px;
    		font-size: 13px;
			position: absolute;
			right: 0;
			margin-right: 55px;

    	}
    	
 		#frm{
			position: fixed; /* 이 부분을 고정 */
	  		top:26px; 
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
  			height: 28px;
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
        	padding-top: 2px;
        	top:0;
        	margin-right: 4px;
			
        } 	
        
 		.ol-control{
 		    display: none;
 		}   
    </style>
<!-- OpenLayers map -->
    <link rel="stylesheet" href="css/ol.css" type="text/css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.bundle.min.js"></script>
    <script src="js/ol.js"></script>


</head>

<body>

	<div id="map"></div>
	<div class="top">
		<form action="equipLocations.jsp" id="locations" method="get">
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
					<input type="hidden" name="uuid" value="<%=uuid %>">		
				</td>
			</tr>
		</table>

		</form>
	</div>		

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
	
    $("input:radio[name='gis_setting']:radio[value='<%=param%>']").attr("checked",true);
 
    $(document).ready(function() 
    		{ 
    		    $("input:radio[name=gis_setting]").click(function() 
    		    { 
    		    	location.replace("beaconLocations.jsp?uuid=<%=uuid%>&gis_setting="+$('input[name=gis_setting]:checked').val());
    		    })

    		});
    

    	function submit(){
    		document.getElementById('locations').submit();
 
    	}	
	
    	console.log("Start Log");
    	console.log(<%=multi_marker%>);
    	console.log("End Log");  	

    	var uuid = "<%=uuid%>";
    	
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
								//[126.77192, 37.654461]
 								[data[0].Longitude,data[0].Latitude]

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
							[data[0].Longitude,data[0].Latitude]

					), 
					zoom: 11
				})
		});
	 	  }
      
       var view = map.getView();
       var zoom = view.getZoom();
       var center = view.getCenter();
       var rotation = view.getRotation();
 	   
		$('.ol-zoom-in, .ol-zoom-out').tooltip({
			placement: 'right',
			container: '#map',
		});
		$('.ol-rotate-reset, .ol-attribution button[title]').tooltip({
			placement: 'left',
			container: '#map',
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

		// Click popup


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

		// Popup content
		function ToolTip(desc)
		{
		    var html = '';
		    desc.forEach((i) => {
		        html += '<div class="ol-tooltip">'+
		            '<img src="'+i.img+'">' +
		            '<div class="info">'+
		                '<div class="ol-tooltip-job"> <a href="'+i.link+'"> '+i.job+' </a> </div>'+
		                '<div class="ol-tooltip-salary">'+i.salary+'</div>'+
		                '<div class="ol-tooltip-company">'+i.name+'</div>'+
		            '</div>'+
		        '</div>';
		    });
		    return html;
		}
        
		
		function addPointGeom(data) {	
		       
			data.forEach(function(item) { //iterate through array...

				var Longitude = item.Longitude, Latitude = item.Latitude, Uuid = item.Uuid, Regiment = item.Regiment, RegimCompany = item.RegimCompany
								, EquipType = item.EquipType, EquipLocation=item.EquipLocation;
		
				console.log(Longitude + ":" + Latitude + ":" + Uuid + ":" + Regiment + ":" +
						RegimCompany + ":" + EquipType + ":" + EquipLocation);
				console.log(Uuid == uuid);

				
				if (Uuid == uuid) {
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
				}
				
				
				var iconFeature = new ol.Feature({
				    geometry: new ol.geom.Point(ol.proj.transform([Longitude,Latitude], 'EPSG:4326', 'EPSG:3857')),
				    type: 'Point',
				    lon: Longitude,
				    lat: Latitude,
				    desc: '<table style="white-space:nowrap;width:100%;">'
				    	+ '<tr><td class="block" style="width:auto">위도</td><td style="text-align:right;">' + Latitude + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">경도</td><td style="text-align:right;">' + Longitude + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">장비번호&nbsp&nbsp</td><td style="text-align:right;">' + Uuid + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">소속</td><td style="text-align:right;">' + Regiment + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">세부소속</td><td style="text-align:right;">' + RegimCompany + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">설치위치</td><td style="text-align:right;">' + EquipLocation + '</td></tr>'
				    	+ '</table>'
				}),
				iconStyle = new ol.style.Style({
				    image: MarkerIcon,
				    text: new ol.style.Text({
				    	font: '7px bold',
				        text: 'B',
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

			});
				
		}
		
		addPointGeom(data);		
		
 
    </script>

</body>
</html>