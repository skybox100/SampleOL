


var viewLayer = new ol.layer.Tile({
	id : 'viewLayer',
    source: new ol.source.XYZ({
    url: 'http://210.179.64.28/basemap/raster/250K/{z}/{x}/{y}.png'
	//url: 'http://xdworld.vworld.kr:8080/2d/Base/202002/{z}/{x}/{y}.png'
    })
});

var viewLayer2 = new ol.layer.Tile({
	id : 'viewLayer2',
    source: new ol.source.XYZ({
    url: 'http://210.179.64.28/basemap/raster/25K/{z}/{x}/{y}.png'
    })
});

var viewLayer3 = new ol.layer.Tile({
	id : 'viewLayer3',
    source: new ol.source.XYZ({
    url: 'http://210.179.64.28/basemap/air/{z}/{x}/{y}.png'
    })
});


//geoserver 설정
var tileImg = new ol.layer.Tile({
    visible: true,
    source: new ol.source.TileWMS({
        url: 'http://210.179.64.28/geoserver/wms',
        params: {
            'FORMAT': 'image/png',
            'VERSION': '1.1.1',
            tiled: true,
            "STYLES": '',
            "LAYERS": '' // workspace:layer
        }
    })
});


//위치는 우리나라 중앙쯤(?)
var view = new ol.View({
    center: ol.proj.fromLonLat([126.784000, 37.792650]),
	zoom:12
});




