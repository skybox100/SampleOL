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

	
	String param = "satellite_map";
	
	if(request.getParameter("gis_setting")!= null ){
		param = request.getParameter("gis_setting") ;
	}

	String reg = request.getParameter("equip_regiment");
	String et = request.getParameter("equip_type");
	
	String regp = request.getParameter("equip_regiment");
	String etp = request.getParameter("equip_type");
	
	String sn = request.getParameter("sn");
	String ps = request.getParameter("ps");

	DBConnection cd = new DBConnection();
	ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
	
	Gson gson = new Gson();
	String multi_marker = null;	

	if(reg.equals("전체") && et.equals("전체")){
		
	} else if(et.equals("전체")){
		regp = cd.getCodeName("Regiment", reg);
	} else{
		regp = cd.getCodeName("Regiment", reg);
		etp = cd.getCodeName("EquipType", et);
	}

	if(etp.equals("전체")){
		etp=regp;
	}
	
	equipLocations = cd.getEquipLocations(reg, et);
	multi_marker = gson.toJson(equipLocations);
	
	int cnt = equipLocations.size();

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
		<form action="equipLocations2.jsp" id="locations" method="get">
		<table>
			<tr>
				<td class="gis_setting3">
					<font size="1px">
					<input type="radio" id="geofence" name="gis_setting" class="gis_setting" value="geofence">
		  			<label for="geofence">군사</label>
		 			 <input type="radio" id="satellite_map" name="gis_setting" class="gis_setting" value="satellite_map" >
		 			 <label for="satellite_map">위성</label>		 			
					
					</font>		  			
				</td>
				<td style="display: none">
					<input type="hidden" name="equip_regiment" value="<%=reg %>">
					<input type="hidden" name="equip_type" value="<%=et %>">
				</td>				
			</tr>
		</table>

		</form>
	</div>	
	<div id="top_div" style="white-space:nowrap; ">
		<form action="detail2.jsp" id='frm' method="get" onsubmit="return goDetail()">
			<a id="rcp_frm"><%=etp %></a>
			<a id="rcp_frm2" href="detail2.jsp?reg=<%=reg%>&et=<%=et%>">장비수: <%=cnt %></a>
			<input type="hidden" name="et" value="<%=et%>">
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
    		    $("input:radio[name=gis_setting]").change(function() 
    		    { 
    		    	location.replace("equipLocations2.jsp?equip_regiment=<%=reg%>&equip_type=<%=et%>&gis_setting="+$('input[name=gis_setting]:checked').val()+"&sn=<%=sn%>");
    		    })
 
    		});
    

    	function submit(){
    		document.getElementById('locations').submit();
 
    	}	
	
    	console.log("Start Log");
    	console.log(<%=multi_marker%>);
    	console.log("End Log");  	
    	
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
  								[126.77192, 37.754461]
 								//[data[0].longitude,data[0].latitude]

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
  							[126.77192, 37.754461]
								//[data[0].longitude,data[0].latitude]

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
        
		function MGRSString (Lat, Long)
		{ 
		console.log(Lat.trim()+","+Long.trim());
		if (Lat < -80) return 'Too far South' ; if (Lat.trim() > 84) return 'Too far North' ;
		var c = 1 + Math.floor ((Long.trim()+180)/6);
		var e = c*6 - 183 ;
		var k = Lat.trim()*Math.PI/180;
		var l = Long.trim()*Math.PI/180;
		var m = e*Math.PI/180;
		var n = Math.cos (k);
		var o = 0.006739496819936062*Math.pow (n,2);
		var p = 40680631590769/(6356752.314*Math.sqrt(1 + o));
		var q = Math.tan (k);
		var r = q*q;
		var s = (r*r*r) - Math.pow (q,6);
		var t = l - m;
		var u = 1.0 - r + o;
		var v = 5.0 - r + 9*o + 4.0*(o*o);
		var w = 5.0 - 18.0*r + (r*r) + 14.0*o - 58.0*r*o;
		var x = 61.0 - 58.0*r + (r*r) + 270.0*o - 330.0*r*o;
		var y = 61.0 - 479.0*r + 179.0*(r*r) - (r*r*r);
		var z = 1385.0 - 3111.0*r + 543.0*(r*r) - (r*r*r);
		var aa = p*n*t + (p/6.0*Math.pow (n,3)*u*Math.pow (t,3)) + (p/120.0*Math.pow (n,5)*w*Math.pow (t,5)) + (p/5040.0*Math.pow (n,7)*y*Math.pow (t,7));
		var ab = 6367449.14570093*(k - (0.00251882794504*Math.sin (2*k)) + (0.00000264354112*Math.sin (4*k)) - (0.00000000345262*Math.sin (6*k)) + (0.000000000004892*Math.sin (8*k))) + (q/2.0*p*Math.pow (n,2)*Math.pow (t,2)) + (q/24.0*p*Math.pow (n,4)*v*Math.pow (t,4)) + (q/720.0*p*Math.pow (n,6)*x*Math.pow (t,6)) + (q/40320.0*p*Math.pow (n,8)*z*Math.pow (t,8));
		aa = aa*0.9996 + 500000.0;
		ab = ab*0.9996; if (ab < 0.0) ab += 10000000.0;
		var ad = 'CDEFGHJKLMNPQRSTUVWXX'.charAt (Math.floor (Lat/8 + 10));
		var ae = Math.floor (aa/100000);
		var af = ['ABCDEFGH','JKLMNPQR','STUVWXYZ'][(c-1)%3].charAt(ae-1);
		var ag = Math.floor (ab/100000)%20;
		var ah = ['ABCDEFGHJKLMNPQRSTUV','FGHJKLMNPQRSTUVABCDE'][(c-1)%2].charAt(ag);
		aa = Math.floor (aa%100000); 
		ab = Math.floor (ab%100000);
			return c + ad + ' ' + af + ah + ' ' + aa + ' ' + ab;
		}
		
		function addPointGeom(data) {	
		       
			data.forEach(function(item) { //iterate through array...

				var longitude = item.longitude, latitude = item.latitude, id = item.equipId, regiment = item.regiment
								, equipType = item.equipType, equipLocation = item.equipLocation,mgrs = item.mgrs;
		
				console.log(longitude + ":" + latitude + ":" + id + ":" + regiment + ":" +
						equipType + ":" + equipLocation);
				//console.log(id == equipId);


				
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
				    desc: '<table style="white-space:nowrap;width:100%;">'
				    	+ '<tr><td class="block" style="width:auto">장비번호&nbsp&nbsp</td><td style="text-align:right;">' + id.substr(0,15) + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">소속</td><td style="text-align:right;">' + regiment + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">장비종류</td><td style="text-align:right;">' + equipType + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">설치위치</td><td style="text-align:right;">' + equipLocation + '</td></tr>'
				    	+ '<tr><td class="block" style="width:auto">군사좌표</td><td style="text-align:right;">' + mgrs + '</td></tr>'
				    	+ '</table>'
				}),
				iconStyle = new ol.style.Style({
				    image: MarkerIcon,
				    text: new ol.style.Text({
				    	font: '7px bold',
				        text: 'E',
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