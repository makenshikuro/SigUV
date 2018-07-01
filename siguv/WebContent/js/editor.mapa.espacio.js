_data = "";

_nivelActual = "0";
centro = [ 39.512859, -0.4244782 ];
_currentPosition = centro;
_zoom = 18;

/* Variables Globales */
_fondo = "plano";
_tema = "b";
_iconos = true;
_toponimo = "d";
_area = "";

/* Variables */
var surOeste = new L.LatLng(39.51178034700101, -0.4247921705245971);
var norEste = new L.LatLng(39.51368822239936, -0.42276978492736816);
_mapBounds = new L.LatLngBounds(surOeste, norEste);
_mapMinZoom = 5;
_mapMaxZoom = 25;



var inputLocalizacion = $("#map-editor\\:getLocalizacion");
var inputPolygon = $("#map-editor\\:getPolygon");
var modo = $("#map-editor\\:testMap").data("modo");


var inputIdEspacio = $("#espacio-basico\\:getIdEspacio");
var inputIdfake = $("#espacio-basico\\:getIdfake");
var inputPiso = $("#espacio-basico\\:getPiso");
var inputBloque = $("#espacio-basico\\:getBloque");
var inputEspacio = $("#espacio-basico\\:getEspacio");


$("#espacio-basico\\:getEspacio").keyup(function() {

	var regex = /[1-9]/;

	if (regex.test(inputEspacio.val())) {
		var espacio = "E" + inputEspacio.val();
		var piso = "P" + inputPiso.val();
		var bloque = "B" + inputBloque.val();
		inputIdEspacio.val(bloque + piso + espacio);
		inputIdfake.val(bloque + piso + espacio);
		console.log(inputIdfake.val());
	}

	//console.log( "Handler for .keyup() called.1"+espacio );

});
$("#espacio-basico\\:getBloque").keyup(function() {
	var regex = /[1-9]/;

	if (regex.test(inputBloque.val())) {
		var espacio = "E" + inputEspacio.val();
		var piso = "P" + inputPiso.val();
		var bloque = "B" + inputBloque.val();
		inputIdEspacio.val(bloque + piso + espacio);
		inputIdfake.val(bloque + piso + espacio);
		console.log(inputIdfake.val());
	}

});
$("#espacio-basico\\:getPiso").keyup(function() {
	var regex = /[1-9]/;

	if (regex.test(inputPiso.val())) {
		var espacio = "E" + inputEspacio.val();
		var piso = "P" + inputPiso.val();
		var bloque = "B" + inputBloque.val();
		inputIdEspacio.val(bloque + piso + espacio);
		inputIdfake.val(bloque + piso + espacio);
		console.log(inputIdfake.val());
	}

});


/* Inicialización Mapa */
map = L.map('map', {
	center : centro,
	zoom : _zoom,
	zoomControl : false
});


var osmUrl = 'http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png';
osm = L.tileLayer(osmUrl, {
	maxZoom : 25,
}).addTo(map);



var editableLayers = new L.FeatureGroup();
map.addLayer(editableLayers);

var drawControl = new L.Control.Draw({
	position : 'topleft',
	draw : {
		marker : true,
		polyline : false,
		polygon : {
			allowIntersection : false,
			showArea : true,
			drawError : {
				color : '#b00b00',
				timeout : 1000
			},
			shapeOptions : {
				color : '#bada55'
			}
		},

		rectangle : false,
		circle : false,
		circlemarker : false
	},
	edit : {
		featureGroup : editableLayers,
		remove : true
	}
});

var drawControlOnlyMarker = new L.Control.Draw({
	position : 'topleft',
	draw : {
		marker : true,
		polyline : false,
		polygon : false,

		rectangle : false,
		circle : false,
		circlemarker : false
	},
	edit : {
		featureGroup : editableLayers,
		remove : true
	}
});
var drawControlOnlyPolygon = new L.Control.Draw({
	position : 'topleft',
	draw : {
		marker : false,
		polyline : false,
		polygon : {
			icon : new L.DivIcon({
				iconSize : new L.Point(16, 16),
				className : 'leaflet-div-icon leaflet-editing-icon my-own-class'
			}),
			allowIntersection : false,
			showArea : true,
			drawError : {
				color : '#b00b00',
				timeout : 1000
			},
			shapeOptions : {
				color : '#bada55'
			}
		},

		rectangle : false,
		circle : false,
		circlemarker : false
	},
	edit : {
		featureGroup : editableLayers,
		remove : true
	}
});
var drawControlEmpty = new L.Control.Draw({
	position : 'topleft',
	draw : {
		marker : false,
		polyline : false,
		polygon : false,

		rectangle : false,
		circle : false,
		circlemarker : false
	},
	edit : {
		featureGroup : editableLayers,
		remove : true
	}
});

map.addControl(drawControl);

var activeControl = drawControl;

map.on('draw:created', function(e) {
	var type = e.layerType,
		layer = e.layer;
//	console.log(type);


	if (type === 'marker') {
		layer.bindPopup('A popup!');
		editableLayers.addLayer(layer);
		//console.log(e);
		inputLocalizacion.val(e.layer._latlng.lat + "," + e.layer._latlng.lng);
		//console.log(activeControl === drawControl);
		//console.log(activeControl === drawControlOnlyMarker);
		if (activeControl === drawControl) {
			map.removeControl(drawControl);
			map.addControl(drawControlOnlyPolygon);
			activeControl = drawControlOnlyPolygon;
		} else if (activeControl === drawControlOnlyMarker) {
			map.removeControl(drawControlOnlyMarker);
			map.addControl(drawControlEmpty);
			activeControl = drawControlEmpty;
		}
	} else if (type === 'polygon') {
		console.log('polygon');
		editableLayers.addLayer(layer);
		
//		console.log(e.layer.getLatLngs()[0]);
		var loc = "";
		for (var j= 0; j < e.layer.getLatLngs()[0].length; j++){
			console.log(e.layer.getLatLngs()[0][j]);
			loc += e.layer.getLatLngs()[0][j].lat+" "+e.layer.getLatLngs()[0][j].lng+",";
		}
		loc += e.layer.getLatLngs()[0][0].lat+" "+e.layer.getLatLngs()[0][0].lng;
		
//		var inputd = e.layer.getLatLngs()[0].toString().replace(/LatLng\(|\)|\s/gi, '').split(",");

//		var loc = "";
//		for (var i = 0; i < inputd.length; i+=2){
//			loc += inputd[i]+" "+inputd[i+1]+",";
//		}
//		loc += inputd[0]+" "+inputd[1];

		console.log(loc);
//		console.log(inputd.length);
		
		inputPolygon.val(loc);
		
		
//		console.log(inputd.toString().replace(/LatLng\(|\)|\s/gi, ''));
//		console.log("inputso: "+inputd.toString().replace(/LatLng\(|\)|\s/gi, ''));
		
		if (activeControl === drawControl) {
			map.removeControl(drawControl);
			map.addControl(drawControlOnlyMarker);
			activeControl = drawControlOnlyMarker;
		} else if (activeControl === drawControlOnlyPolygon) {
			map.removeControl(drawControlOnlyPolygon);
			map.addControl(drawControlEmpty);
			activeControl = drawControlEmpty;
		}
	}
//console.log(editableLayers._layers.length());
});

map.on('draw:editvertex', function(e) {

	var inputd = e.poly.getLatLngs()[0].toString().replace(/LatLng\(|\)|\s/gi, '').split(",");

	var loc = "";
	for (var i = 0; i < inputd.length; i+=2){
		loc += inputd[i]+" "+inputd[i+1]+",";
	}
	loc += inputd[0]+" "+inputd[1];
	
	inputPolygon.val(loc);

});

map.on("draw:deleted", function(e) {

	if (editableLayers.getLayers().length === 0) {
		map.removeControl(drawControlEmpty);
		map.removeControl(drawControlOnlyPolygon);
		map.removeControl(drawControlOnlyMarker);
		map.addControl(drawControl);
		activeControl = drawControl;
	} else if (editableLayers.getLayers().length === 1) {
		editableLayers.eachLayer(function(layer) {

			if (layer.hasOwnProperty('_latlng')) {
				//console.log('Marker');
				map.removeControl(drawControlEmpty);
				map.removeControl(drawControlOnlyMarker);
				map.removeControl(drawControl);
				map.addControl(drawControlOnlyPolygon);
				activeControl = drawControlOnlyPolygon;
			} else {
				//console.log('Poly');
				map.removeControl(drawControlEmpty);
				map.removeControl(drawControlOnlyMarker);
				map.removeControl(drawControl);
				map.addControl(drawControlOnlyMarker);
				activeControl = drawControlOnlyMarker;
			}
		});
	}
});


var option = $('#map-editor\\:selectEspacio option:eq(0)').val();
var inputTopLeft = $("#map-editor\\:testMap").data("ptl");
var inputTopRight = $("#map-editor\\:testMap").data("ptr");
var inputBottomLeft = $("#map-editor\\:testMap").data("pbl");

var pointTopLeft = L.latLng(inputTopLeft.split(",")[0], inputTopLeft.split(",")[1]);
var pointTopRight = L.latLng(inputTopRight.split(",")[0], inputTopRight.split(",")[1]);
var pointBottomLeft = L.latLng(inputBottomLeft.split(",")[0], inputBottomLeft.split(",")[1]);

bounds = new L.LatLngBounds(pointTopLeft, pointTopRight).extend(pointBottomLeft);
map.fitBounds(bounds);


var overlay = L.imageOverlay.rotated("//" + location.hostname + "/planos/" + option, pointTopLeft, pointTopRight, pointBottomLeft, {
	opacity : 0.8,
	interactive : false
});
map.addLayer(overlay);

bounds = new L.LatLngBounds(pointTopLeft, pointTopRight).extend(pointBottomLeft);
map.fitBounds(bounds);

function changeMap(sel) {
	map.removeLayer(overlay);

	overlay = L.imageOverlay.rotated("//" + location.hostname + "/planos/" + sel.value, pointTopLeft, pointTopRight, pointBottomLeft, {
		opacity : 0.8,
		interactive : true
	});
	map.addLayer(overlay);
	bounds = new L.LatLngBounds(pointTopLeft, pointTopRight).extend(pointBottomLeft);
	map.fitBounds(bounds);
	console.log(sel.value);
}