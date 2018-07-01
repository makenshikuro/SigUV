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

/* Inicialización Mapa */
map = L.map('map', {
	center : centro,
	zoom : _zoom,
	zoomControl : false
});


var osmUrl = 'http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png';
osm = L.tileLayer(osmUrl, {
	maxZoom : 21,
}).addTo(map);

_topRight = false;
_topLeft = false;
_bottomLeft = false;


var inputTopLeft = $("#map-editor\\:getTopLeft");
var inputTopRight = $("#map-editor\\:getTopRight");
var inputBottomLeft = $("#map-editor\\:getBottomLeft");
var inputLocalizacion = $("#map-editor\\:getLocalizacion");
var modo = $("#map-editor\\:testMap").data("modo");

var topLeftMarker = false;
var pointTopLeft,
	pointTopRight,
	pointBottomLeft;
var topRightMarker = false;
var bottomLeftMarker = false;
var overlay;
var bounds;

function addTopLeft() {
	_topLeft = true;

}
function addTopRight() {
	_topRight = true;

}
function addBottonLeft() {
	_bottomLeft = true;

}

function onMapClick(e) {
	if (_topLeft) {
		if (topLeftMarker) {
			map.removeLayer(topLeftMarker);
		}
		pointTopLeft = L.latLng(e.latlng);
		topLeftMarker = L.marker(e.latlng, {
			draggable : true,
			title : "Resource location",
			alt : "Resource Location",
			riseOnHover : true
		}).addTo(map).bindPopup("TopLeft " + e.latlng.toString()).openPopup();
		_topLeft = false;

		inputTopLeft.val(e.latlng.lat + "," + e.latlng.lng);
		generarMapa();

	}
	if (_topRight) {
		if (topRightMarker) {
			map.removeLayer(topRightMarker);
		}
		pointTopRight = L.latLng(e.latlng);
		topRightMarker = L.marker(e.latlng, {
			draggable : true,
			title : "Resource location",
			alt : "Resource Location",
			riseOnHover : true
		}).addTo(map).bindPopup("TopRight" + e.latlng.toString()).openPopup();
		_topRight = false;

		inputTopRight.val(e.latlng.lat + "," + e.latlng.lng);
		generarMapa();

	}
	if (_bottomLeft) {
		if (bottomLeftMarker) {
			map.removeLayer(bottomLeftMarker);
		}
		pointBottomLeft = L.latLng(e.latlng);
		bottomLeftMarker = L.marker(e.latlng, {
			draggable : true,
			title : "Resource location",
			alt : "Resource Location",
			riseOnHover : true
		}).addTo(map).bindPopup("BottomLeft " + e.latlng.toString())
			.openPopup();
		_bottomLeft = false;

		inputBottomLeft.val(e.latlng.lat + "," + e.latlng.lng);
		generarMapa();

	}

}




function generarMapa() {
	if (topLeftMarker && topRightMarker && bottomLeftMarker) {

		if (overlay) {
			map.removeLayer(overlay);
		}

		bounds = new L.LatLngBounds(pointTopLeft, pointTopRight).extend(pointBottomLeft);

		//inputLocalizacion. = bounds.getCenter();
		inputLocalizacion.val(bounds.getCenter().lat + "," + bounds.getCenter().lng);
		console.log(inputLocalizacion);
		map.fitBounds(bounds);


//		overlay = L.imageOverlay.rotated("//"+location.hostname+"/" + $("#map-editor\\:testMap").val(), pointTopLeft, pointTopRight, pointBottomLeft, {
		overlay = L.imageOverlay.rotated("//localhost/" + $("#map-editor\\:testMap").val(), pointTopLeft, pointTopRight, pointBottomLeft, {
			opacity : 0.8,
			interactive : true
		});

		topLeftMarker.on('drag dragend', repositionImage);
		topRightMarker.on('drag dragend', repositionImage);
		bottomLeftMarker.on('drag dragend', repositionImage);

		map.addLayer(overlay);
	}
}
function repositionImage() {
	var tl = L.latLng(topLeftMarker.getLatLng().lat, topLeftMarker.getLatLng().lng);
	var tr = L.latLng(topRightMarker.getLatLng().lat, topRightMarker.getLatLng().lng);
	var bl = L.latLng(bottomLeftMarker.getLatLng().lat, bottomLeftMarker.getLatLng().lng);

	inputTopLeft.val(topLeftMarker.getLatLng().lat + "," + topLeftMarker.getLatLng().lng);
	inputTopRight.val(topRightMarker.getLatLng().lat + "," + topRightMarker.getLatLng().lng);
	inputBottomLeft.val(bottomLeftMarker.getLatLng().lat + "," + bottomLeftMarker.getLatLng().lng);

	
	inputLocalizacion.val(bounds.getCenter().lat + "," + bounds.getCenter().lng);

	overlay.reposition(topLeftMarker.getLatLng(), topRightMarker.getLatLng(), bottomLeftMarker.getLatLng());
}
;

if (modo !== 'editor') {

	L.easyButton('fa-2x fas fa-map-marker-alt planoMarker topleft', function(btn, map) {
		addTopLeft();
	}, 'Punto superior izquierda').addTo(map);

	L.easyButton('fa-2x fas fa-map-marker-alt planoMarker topright', function(btn, map) {
		addTopRight();
	}, 'Punto superior derecha').addTo(map);

	L.easyButton('fa-2x fas fa-map-marker-alt planoMarker bottomleft', function(btn, map) {
		addBottonLeft();
	}, 'Punto inferior izquierda').addTo(map);

	map.on('click', onMapClick);

	//console.log ($("#map-editor\\:testMap").val());
	console.log($("#map-editor\\:testMap").data("modo"));
} else {
	console.log($("#map-editor\\:testMap").data("modo"));
	var inputTopLeft = $("#map-editor\\:testMap").data("ptl");
	var inputTopRight = $("#map-editor\\:testMap").data("ptr");
	var inputBottomLeft = $("#map-editor\\:testMap").data("pbl");


	var pointTopLeft = L.latLng(inputTopLeft.split(",")[0], inputTopLeft.split(",")[1]);
	var pointTopRight = L.latLng(inputTopRight.split(",")[0], inputTopRight.split(",")[1]);
	var pointBottomLeft = L.latLng(inputBottomLeft.split(",")[0], inputBottomLeft.split(",")[1]);
	//	if (overlay) {
	//		map.removeLayer(overlay);
	//	}

	bounds = new L.LatLngBounds(pointTopLeft, pointTopRight).extend(pointBottomLeft);
	inputLocalizacion.val(bounds.getCenter().lat + "," + bounds.getCenter().lng);
	map.fitBounds(bounds);


	overlay = L.imageOverlay.rotated("//"+location.hostname+"/" + $("#map-editor\\:testMap").val(), pointTopLeft, pointTopRight, pointBottomLeft, {
		opacity : 0.8
	});
	map.addLayer(overlay);
}

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