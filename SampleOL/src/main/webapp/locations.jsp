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
	String param = "geofence";
	String param2 = "geofoff";
	String param3 = "전체";

	
	if(request.getParameter("gis_setting")!= null && request.getParameter("gis_setting2")!=null){
		param = request.getParameter("gis_setting") ;
		param2 = request.getParameter("gis_setting2");	
	}
	if(request.getParameter("geofence")!= null ){
		param3 = request.getParameter("geofence") ;
	}
	
	String st = request.getParameter("pn");
	String pn=null;
	
	DBConnection cd = new DBConnection();
	ArrayList<Location> locations = new ArrayList<Location>();
	Location lastLocation = new Location();
	ArrayList<Circle> Circles = new ArrayList<Circle>();

	Gson gson = new Gson();
	String multi_marker = "";
	String last_marker ="";
	String circle_marker ="";

		//int chk = 1;

	if(param.equals("satellite_map")){
		pn = "위성지도";
	} else if(param.equals("geofence")){
		pn = "군사지도";
	} 
	
	if(param3.equals("전체")){
		Circles=cd.getCircle(param3);
	} else{
		Circles=cd.getCircle(cd.getCodeID("Regiment", param3));
	}
	
		circle_marker=gson.toJson(Circles);
	
		if(request.getParameter("pn")!= null){
			String rest = st.replaceAll("[^0-9]","");

			locations = cd.getMobileStatus(rest);
		}else{		
			locations = cd.getMobileStatus();
		}
		multi_marker = gson.toJson(locations);
		String lastTimestamp = lastLocation.getTimestamp();
		
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

		//String geo_0 = cd.getCodeID("Regiment",  "28여단");
		//String geo_1 = cd.getCodeID("Regiment", "28-1대대");
		//String geo_2 = cd.getCodeID("Regiment", "28-2대대");
		//String geo_3 = cd.getCodeID("Regiment", "28-3대대");
		
		String geo_0 = "28여단";
		String geo_1 = "28-1대대";
		String geo_2 = "28-2대대";
		String geo_3 = "28-3대대";
		
		String geo0; String geo1; String geo2; String geo3;
		geo0 = gson.toJson(geo_0);
		geo1 = gson.toJson(geo_1); 
		geo2 = gson.toJson(geo_2);
		geo3 = gson.toJson(geo_3);
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
       		margin-left: 3px;
       		font-size: 13px;
       		text-align: center;
       		
    	}
   

        #map{
        	
        	width: auto;
			height:1080px;
            
        }
        
        
        #equip_regiment{
         	width: 75px;   
        }
        
        #equip_type{
           width: 105px;  
        }
        
        #reg{
         	width: 75px;   
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
        
         #submit2{
        	position: absolute;
        	right:0;
			top : 33px;
			height: 30px;
			margin-right: 4px;
        }
        
        #status{
        	height: 62px;
        }
        #Scale{
        	height: 62px;
        }
        #equip{
        	height: 62px;
        }
        #geofenceLayer{
        	height: 62px;
        }
        #table{
    	   	position: fixed;
			top : 63px;
			height: 30px;
			left: 0;
			margin-left: 4px;
        }
        
		#buttonLayer{
			position: fixed; /* 이 부분을 고정 */
			display: flex;
			justify-content:flex-start;
	  		top:30px; 
  			width: 100%;
			white-space: nowrap;
			width: 100%;
  			background: white;
			padding-bottom: 4px;
		}
        
        #gis_setting{
        	height: 62px;
        }
        #personal{
        	height: 62px;
        }
        #rcp_frm{
    	   	position: absolute;
    		top: 0;
    		margin-top: 5px;
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
 		.ol-control{
 		    display: none;
 		}       

        .form{
        	
        }
    </style>

    <!-- OpenLayers map -->
    <script  src="http://code.jquery.com/jquery-latest.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/css/ol.css" type="text/css">
    <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/build/ol.js" data-main="app"></script>

</head>

<body>
	<div id="map"></div>
	<div class="top">
		<form action="locations.jsp" id="locations" method="get">
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
				<font size="1px" style="font-weight: bold" id="gis_setting2">
		 			<input type="radio" id="geofon" name="gis_setting2" class="gis_setting2" value="geofon">
		  			<label for="geofon">GeoF-ON</label>
		 			<input type="radio" id="geofoff" name="gis_setting2" class="gis_setting2" value="geofoff" checked>
		 			<label for="geofoff">GeoF-OFF</label>
		 		</font>
				</td>
			</tr>
		</table>

		</form>
	</div>	


	<div id ="buttonLayer">
	<input type="button" value="병력 위치" id="btn" onclick="showSearch('status')"/>
	<input type="button" value="장비 위치" id="btn" onclick="showSearch('equip')"/>
	<input type="button" value="이동 조회" id="btn" onclick="showSearch('personal')"/> 
	<input type="button" value="Geofence" id="btn" onclick="showSearch('geofenceLayer')"/> 
	
	<button id="zoom-restore" >reset</button><br>
	
	<div id="status" style="display:none; background: white;">
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

	<div id="equip" style="display:none; background: white;">
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
	
	
	
	<div id="personal" style="display:none; background: white;">
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
	
	<div id="Scale" style="display:none; background: white;">
		<form action="locations.jsp" method="get">
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
	<div id="geofenceLayer" style="display:none; background: white;">
		<table id="table">
			<tr>
				<td>
					<select id="geo" name ="geo" onchange="geofenceSelectChange(this)">
						<option>전체</option>
						<%for(int i=0; i<mobileStatusReg.size(); i++) {%>
						<option value="<%=mobileStatusReg.get(i)%>"><%=mobileStatusReg.get(i)%></option>
						<%} %>
					</select>
				</td>
			</tr>
		</table>
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
 	 <script src="js/ol.js"></script>
	

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
    	$("input:radio[name='gis_setting2']:radio[value='<%=param2%>']").attr("checked",true);
    	$("#geo").val("<%=param3%>").prop("selected", true);

       
    	     
        
        $(document).ready(function() 
        		{ 
        		    $("input:radio[name=gis_setting]" || "input:radio[name=gis_setting2]").click(function() 
        		    { 
        		    	location.replace("locations.jsp?gis_setting="+$('input[class="gis_setting"]:checked').val()+"&gis_setting2="+$('input[class="gis_setting2"]:checked').val());
        		    }),
        		    $("input:radio[name=gis_setting2]").click(function() 
                	{ 
        		    	if($("select[name=geo]").val() != '전체' && $('input[class="gis_setting2"]:checked').val() == 'geofon'){
        	 		   		location.replace("locations.jsp?gis_setting="+$('input[class="gis_setting"]:checked').val()+
        	 		    			"&gis_setting2="+$('input[class="gis_setting2"]:checked').val()+
        	 		    			"&geofence="+$("select[name=geo]").val());
        		    	}else{
        		    		location.replace("locations.jsp?gis_setting="+$('input[class="gis_setting"]:checked').val()+"&gis_setting2="+$('input[class="gis_setting2"]:checked').val());        		    		
        		    	}
                	})
      
        
        		});

        	function submit(){
        		document.getElementById('locations').submit();
     
        	}
    	     
       var x =new Array();
       var y =new Array();
       var r =new Array();
       var rc =new Array();


   		// var x = 126.7719083;	var y = 37.6544622;
     	var data = <%=multi_marker%>;
        // var data = <%=last_marker%>;

   		var data2 = <%=circle_marker%>;

   		for(var i=0;i<data2.length;i++){
       		    x.push(data2[i][Object.keys(data2[i].latitude)[0]]);
       		    y.push(data2[i][Object.keys(data2[i].longitude)[0]]);
       		    r.push(data2[i][Object.keys(data2[i].r)[0]]);
       		    rc.push(data2[i][Object.keys(data2[i].regiment)[0]]);

        }
   		 

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
      
        

   		var pnt=new Array();

		
		var seq3=0;
		data2.forEach(function(item) { //iterate through array...
			pnt[seq3]= ol.proj.fromLonLat([item.latitude, item.longitude]);
	
	   		var vectorSource = new ol.source.Vector({
				projection : 'EPSG:3857'
			}); //새로운 벡터 생성
			
			var circle = new ol.geom.Circle(pnt[seq3], Number(item.r)); //좌표, 반경 넓이
			CircleFeature = new ol.Feature(circle); //구조체로 형성
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
						text : item.regiment,
						textAlign : 'center',
						font : '15px roboto,sans-serif'
				})
				}) ]
			});
			if('<%=param2%>' == 'geofon'){	
				map.addLayer(vectorLayer); 
			}
			seq3++;
		});
		


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
    
	    function geofenceSelectChange(e) {
	    	var geo0 = <%=geo0%>; var geo1 = <%=geo1%>;  
	    	var geo2 = <%=geo2%>;
	    	var geo3 = <%=geo3%>;
	    	var geo4 = ['전체'];
	    	
	
	    	if(e.value == "28여단") var d = geo0;
	    	else if(e.value == "28-1대대") var d = geo1;
	    	else if(e.value == "28-2대대") var d = geo2;
	    	else if(e.value == "28-3대대") var d = geo3;
	    	else if(e.value == "전체") var d = geo4;

	    	if('<%=param2%>'== 'geofon'){	    		
	 		   	if(d == "전체"){
	 		   	location.replace("locations.jsp?gis_setting="+$('input[class="gis_setting"]:checked').val()+
		    			"&gis_setting2="+$('input[class="gis_setting2"]:checked').val());
	 		   	}else{	 		   		
	 		   		location.replace("locations.jsp?gis_setting="+$('input[class="gis_setting"]:checked').val()+
	    			"&gis_setting2="+$('input[class="gis_setting2"]:checked').val()+
	    			"&geofence="+d);
	 		   	}
	    	}

	    	
	    }

		function goBack(){
			window.history.back();
		}		
		
		var search = ['personal', 'status', 'equip', 'Scale','geofenceLayer'];
		
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

		
		function getTimeStamp() {
			  var d = new Date();
			  var s =
			    leadingZeros(d.getFullYear(), 4) + '-' +
			    leadingZeros(d.getMonth() + 1, 2) + '-' +
			    leadingZeros(d.getDate(), 2) + ' ' +

			    leadingZeros(d.getHours(), 2) + ':' +
			    leadingZeros(d.getMinutes(), 2) + ':' +
			    leadingZeros(d.getSeconds(), 2);

			  return s;
			}

			function leadingZeros(n, digits) {
			  var zero = '';
			  n = n.toString();

			  if (n.length < digits) {
			    for (i = 0; i < digits - n.length; i++)
			      zero += '0';
			  }
			  return zero + n;
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
				var line;
				var distance;
				var r2;


		   		var seq2 = 0;
		   		data2.forEach(function(item) { //iterate through array...
		  	 		if(regiment == item.regiment){
   						line = new ol.geom.LineString([pnt[seq2], pnt_data]);
   						distance = Math.round(line.getLength());
   						r2=Number(item.r);
   					}	
		   			seq2++;
		   		}); 		
		   		
				var day1= new Date(timestamp);
				var day2= new Date(getTimeStamp());
				var difference= Math.abs(day2-day1);
				days = difference/(1000 * 3600 * 24);

				console.log(getTimeStamp());
				console.log(difference);					
				console.log(timestamp);
				
				if(days<1 && '<%=param2%>' == 'geofoff'){
					
					var MarkerIcon = new ol.style.Icon({
			            anchor: [0.5, 20],
			            anchorXUnits: 'fraction',
			            anchorYUnits: 'pixels',
			            src: 'image/marker_bl_01.png',
				        text: 'P',
			            scale: 1.2
			        });
					console.log(days);
				}else if('<%=param2%>' == 'geofoff'){
						
					
					var MarkerIcon = new ol.style.Icon({
			            anchor: [0.5, 20],
			            anchorXUnits: 'fraction',
			            anchorYUnits: 'pixels',
			            src: 'image/marker_yl_01.png',
			            scale: 1.2
				        });
					console.log(days);

				}else if( distance < r2){
					
					var MarkerIcon = new ol.style.Icon({
			            anchor: [0.5, 20],
			            anchorXUnits: 'fraction',
			            anchorYUnits: 'pixels',
			            src: 'image/marker_bl.png',
				        text: 'P',
			            scale: 1.2
			        });
					
					
				}else if(regiment != '<%=param3%>' && '전체'!= '<%=param3%>'){
					
					var MarkerIcon = new ol.style.Icon({
			            anchor: [0.5, 20],
			            anchorXUnits: 'fraction',
			            anchorYUnits: 'pixels',
			            src: 'image/marker_yl_01.png',
				        text: 'P',
			            scale: 1.2
			        });
					
					
				}else
				{

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
						data: JSON.stringify(item),
						dataType: "json",
						accept: "application/json",
						success: function(response) {
							// success handle
								//console.log(JSON.stringify(data));
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