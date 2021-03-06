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
	String param = "satellite_map";
	String longitude="126.77192";
	String latitude="37.754461";
	int zoom =11;
	if(request.getParameter("gis_setting")!= null){
		param = request.getParameter("gis_setting") ;
	}
	
	String sn = request.getParameter("sn");
	String ps = request.getParameter("ps");

	String reg = request.getParameter("reg");
	String rc = request.getParameter("regim_company");
	
	String regp = request.getParameter("reg");
	String rcp = request.getParameter("regim_company");
	
	String rp = request.getParameter("regimPlatoon");
	
	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	ArrayList<Circle> circle= new ArrayList<Circle>();
	
	Gson gson = new Gson();
	String multi_marker ="";
	
	if(reg.equals("전체") && rc.equals("전체")){
			
	} else if(rc.equals("전체")){
		regp = cd.getCodeName("Regiment", reg);	
		circle=cd.getCircle(reg);
		longitude=circle.get(0).getLongitude();
		latitude=circle.get(0).getLatitude();
		zoom=17;
	} else{
		regp = cd.getCodeName("Regiment", reg);
		rcp = cd.getCodeName("RegimCompany", rc);
		circle=cd.getCircle(reg);
		longitude=circle.get(0).getLongitude();
		latitude=circle.get(0).getLatitude();
		zoom=17;
	}
	

	


	if(rcp.equals("전체")){
		rcp=regp;
	}else if(rp.equals("전체")){
		
	}else{
		rcp=rcp+" "+cd.getCodeName("RegimPlatoon", rp);;
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
	
	locations = cd.getMobileStatus3(reg, rc,rp);
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
        	position:fixed;
        	width: 100%;
            height: 100%;
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
			margin-right: 66px;

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
					<input type="radio" id="geofence" name="gis_setting" class="gis_setting" value="geofence" >
		  			<label for="geofence">군사</label>
		 			 <input type="radio" id="satellite_map" name="gis_setting" class="gis_setting" value="satellite_map" >
		 			 <label for="satellite_map">위성</label>		 			
					</font>		  			
				</td>
				<td style="display: none">
					<input type="hidden" name="reg" value="<%=reg %>">
					<input type="hidden" name="regim_company" value="<%=rc %>">
				</td>				
			</tr>
		</table>

		</form>
	</div>	
	<div id="top_div" style="white-space:nowrap; ">
		
		<form action="detail.jsp" id='frm' method="get" onsubmit="return goDetail()">
			<span id="rcp_frm"><%=rcp %></span>
			<a id="rcp_frm2" href="detail.jsp?reg=<%=reg %>&rc=<%=rc %>&rp=<%=rp %>" onclick="document.getElementById('frm').submit();">인원수: <%=cnt %></a>
			<input type="hidden" name="reg" value="<%=reg%>">
			<input type="hidden" name="rc" value="<%=rc%>">
			<input type="hidden" name="rp" value="<%=rp%>">
			<input type="button" value="초기화" id="zoom-restore">
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
    		    $("input:radio[name=gis_setting]" ).change(function() 
    		    { 
    		    	location.replace("personalLocations3.jsp?reg=<%=reg%>&regim_company=<%=rc%>&gis_setting="+$('input[name=gis_setting]:checked').val()+"&sn=<%=sn%>");

    		    	
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
  								[<%=longitude%>, <%=latitude%>]
  							
  						), 
  						zoom: <%=zoom%>
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
  							[<%=longitude%>, <%=latitude%>]
  					), 
  					zoom: <%=zoom%>
  				})
  		});
  	 	  }
        
		var view = map.getView();
        var zoom = view.getZoom();
        var center = view.getCenter();
        var rotation = view.getRotation();
        
        document.getElementById('zoom-restore').onclick = function(){
        	location.reload();

        	//view.setCenter(center);
        	//view.setRotation(rotation);
        	//view.setZoom(zoom);
        }
        document.getElementById('goback').onclick = function(){
        	location.href="locations.jsp?sn=<%=sn%>&ps=<%=ps%>"

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
					if(distance <5 & cnt <9 & distance >0){
						cnt++;
						multi +='<table style="white-space:nowrap;text-align:left;width:100%">'
					    	+ '<tr ><td>' + item.timestamp+'</td><td style="text-align:right;">'+item.isDevice +'</td></tr>'
						   // + '<tr><td>전화번호&nbsp&nbsp</td><td style="text-align:right;">'+item.MobileNumber+'</td></tr>'
						    + '<tr><td Colspan="2">'+item.regimCompanyName+'&nbsp'+item.rankName+'&nbsp'+item.name+'</td></tr>'
						    //+ '<tr><td>계급성명</td><td style="text-align:right;">'+item.rankName+'&nbsp'+item.name+'</td></tr>'
						    //+ '<tr><td>군번</td><td style="text-align:right;">'+item.serviceNumber+'</td></tr>'
						    + '<tr><td Colspan="2">' + item.mgrs + '</td></tr>'
						    + '<tr><td>'+item.roomName+'</td><td style="text-align:right;">'+item.roomNumber+'</td></tr>'
						    + '<tr><td Colspan="2">-----------------------</td></tr>'
					    	+ '</table>';

					}
					
					
		    	});	
		    	
		        if(selected != feature)
		        {
		            // Event coordinates
		            // popup.setPosition(evt.coordinate);
		            // Lon Lat coordinates
		           
		            
		            var position = ol.proj.transform([feature.get('lon'),feature.get('lat')], 'EPSG:4326', 'EPSG:3857');
		            if(feature.get('desc') != undefined){
		            	content.innerHTML= multi+feature.get('desc');

		            
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
				
				
				//var longitude = item.Lon, latitude = item.Lat, icon = item.Icon, desc = item.Desc;
								var longitude = item.longitude, latitude = item.latitude, idx = item.idx
							, userKey = item.userKey, timestamp = item.timestamp
							, regiment = item.regiment, regimCompany = item.regimCompany,regimentName = item.regimentName, regimCompanyName = item.regimCompanyName
							, serviceNumber = item.serviceNumber,isDevice=item.isDevice
							,mgrs = item.mgrs , duty = item.duty, name = item.name, rank = item.rank, rankName = item.rankName
							,mobileNumber=item.MobileNumber,roomNumber=item.roomNumber,roomName=item.roomName,equipLocation=item.equipLocation;
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
				    desc: '<table style="white-space:nowrap;text-align:left;width:100%">'
				    	+ '<tr ><td>' + timestamp+'</td><td style="text-align:right;">'+isDevice +'</td></tr>'
					  //  + '<tr><td>전화번호&nbsp&nbsp</td><td style="text-align:right;">'+mobileNumber+'</td></tr>'
						+ '<tr><td Colspan="2">'+regimCompanyName+'&nbsp'+rankName+'&nbsp'+name+'</td></tr>'
					  //  + '<tr><td>계급성명</td><td style="text-align:right;">'+rankName+'&nbsp'+name+'</td></tr>'
					  //  + '<tr><td>군번</td><td style="text-align:right;">'+serviceNumber+'</td></tr>'
					  	+ '<tr><td Colspan="2">' + mgrs + '</td></tr>'
					    + '<tr><td>'+roomName+'</td><td style="text-align:right;">'+roomNumber+'</td></tr>'
				    	+ '</table>'
				});
				

				
				if(isDevice == 'W-G' || isDevice == 'W-B'){
					var iconStyle = new ol.style.Style({
					    image: MarkerIcon,
					    text: new ol.style.Text({
					        //scale: 1.5,
					        font: '7px bold',
						    text: 'W',
					        fill: new ol.style.Fill({
					          color: "0",
					          
					        }),
					        stroke: new ol.style.Stroke({
					          color: "#fff",
					          width: 6
					        }),
					    	offsetY: -13
					      })
					});
				}else{
					
				
				var iconStyle = new ol.style.Style({
				    image: MarkerIcon,
				    text: new ol.style.Text({
				        //scale: 1.5,
				        font: '7px bold',
					    text: 'P',
				        fill: new ol.style.Fill({
				          color: "0",
				          
				        }),
				        stroke: new ol.style.Stroke({
				          color: "#fff",
				          width: 6
				        }),
				    	offsetY: -13
				      })
				});
					
				}
				
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