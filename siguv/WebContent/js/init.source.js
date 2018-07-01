/* Variables Globales   */
_data = "";
_nivelActual = "0";
_nivelBusqueda = '';
_locale = "es";

_fondo = 'plano';
_tema = 'b';
_toponimo = 'd';
centro = [ 39.512859, -0.4244782 ];
_currentPosition = centro;
_zoom = 18;
_iconos = true;
_area = "";



/* Variables Servidor   */
_server = "//localhost/";
//            _server = "//adretse.uv.es/";
_serverPano = "//147.156.82.219/siguv/";
_listaPanos = '';

/* POI */

Facultades = [ {
	"type" : "Feature",
	"properties" : {
		"name" : "Escola T&egrave;cnica Superior d'Enginyeria",
		"name_ES" : "Escuela T&eacute;cnica Superior de Ingenier&iacute;a",
		"name_EN" : "School of Engineering",
		"web" : "etse",
		"telefono" : "963543211",
		"correo" : "etse@uv.es",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.423967, 39.512877 ]
	}
}, {
	"type" : "Feature",
	"properties" : {
		"name" : "Facultat de Farm&agrave;cia",
		"name_ES" : "Facultad de Farmacia",
		"name_EN" : "Faculty of Pharmacy",
		"web" : "farmacia",
		"telefono" : "963543211",
		"correo" : "farmacia@uv.es",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.420370, 39.509662 ]
	}
}, {
	"type" : "Feature",
	"properties" : {
		"name" : "Facultat de Matem&agrave;tiques",
		"name_ES" : "Facultad de Matemáticas",
		"name_EN" : "Faculty of Mathematics",
		"web" : "matematiques",
		"telefono" : "963543211",
		"correo" : "matematiques@uv.es",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.420719, 39.508131 ]
	}
}, {
	"type" : "Feature",
	"properties" : {
		"name" : "Facultat de Ciencies Biol&ograve;giques",
		"name_ES" : "Facultad de Ciencias Biológicas",
		"name_EN" : "Faculty of Biological Sciencies",
		"web" : "biologia",
		"telefono" : "666",
		"correo" : "biologia@uv.es",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.421032, 39.507887 ]
	}
}, {
	"type" : "Feature",
	"properties" : {
		"name" : "Facultat de F&iacute;sica",
		"name_ES" : "Facultad de Física",
		"name_EN" : "Faculty of Physics",
		"web" : "fisica",
		"telefono" : "666",
		"correo" : "fisica@uv.es",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.419948, 39.507325 ]
	}
}, {
	"type" : "Feature",
	"properties" : {
		"name" : "Facultat de Qu&iacute;mica",
		"name_ES" : "Facultad de Química",
		"name_EN" : "Faculty of Chemistry",
		"web" : "quimica",
		"telefono" : "666",
		"correo" : "quimica@uv.es",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.419312, 39.507786 ]
	}
} ];

Campus = [ {
	"type" : "Feature",
	"properties" : {
		"name" : "Campus Blasco Iba&ntilde;ez",
		"name_ES" : "Campus Blasco Ibañez",
		"name_EN" : "Campus Blasco Ibañez",
		"facultades" : "Geografia i Història, Filosofía, Traducció, fcafe",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.360882, 39.477835 ]
	}
}, {
	"type" : "Feature",
	"properties" : {
		"name" : "Campus Burjassot-Paterna",
		"name_ES" : "Campus Burjassot-Paterna",
		"name_EN" : "Campus Burjassot-Paterna",
		"facultades" : "Escola Tècnica Superior d'Enginyeria,Farmàcia,Matemàtiques,Física,Biologia,Química",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.421388, 39.509518 ]
	}
}, {
	"type" : "Feature",
	"properties" : {
		"name" : "Campus Tarongers",
		"name_ES" : "Campus Tarongers",
		"name_EN" : "Campus Tarongers",
		"facultades" : "Dret,Magisteri,Economia",
		"popupContent" : "This is where the Rockies play!"
	},
	"geometry" : {
		"type" : "Point",
		"coordinates" : [ -0.341757, 39.4782067 ]
	}
} ];

//_serverDB = "//147.156.82.219/servidor/";
_serverDB = "//localhost:8080/servidor/";
var camera,
	scene,
	renderer;

isUserInteracting = false,
onMouseDownMouseX = 0, onMouseDownMouseY = 0,
lon = 0, onMouseDownLon = 0,
lat = 0, onMouseDownLat = 0,
phi = 0, theta = 0;
isMobile = {
	Android : function() {
		return navigator.userAgent.match(/Android/i);
	},
	BlackBerry : function() {
		return navigator.userAgent.match(/BlackBerry/i);
	},
	iOS : function() {
		return navigator.userAgent.match(/iPhone|iPad|iPod/i);
	},
	Opera : function() {
		return navigator.userAgent.match(/Opera Mini/i);
	},
	Windows : function() {
		return navigator.userAgent.match(/IEMobile/i);
	},
	any : function() {
		return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
	}
};