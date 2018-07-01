
/* Inicialización Mapa */
function DefaultMap(){
    _nivelActual = "0";
    centro = [39.512859, -0.4244782];
    _currentPosition = centro;
    _zoom = 18;
}

queryString = GetQueryStringParams();

map = L.map('map', {
	center : centro,
	zoom : _zoom,
	zoomControl : false
});

var osmUrl = 'http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png',
	osmAttrib = '&copy; <a href="http://openstreetmap.org/copyright">OpenStreetMap</a> contributors',
	osm = L.tileLayer(osmUrl, {
		maxZoom : 21,
		attribution : osmAttrib
	}).addTo(map);

//Sidebar
sidebarLayers = L.control.sidebar('sidebarLayers', {
	closeButton : true,
	position : 'left',
	autoPan : false
});
map.addControl(sidebarLayers);

sidebarFacultades = L.control.sidebar('sidebarFacultades', {
	closeButton : true,
	position : 'left',
	autoPan : false
});
map.addControl(sidebarFacultades);
sidebarInfo = L.control.sidebar('sidebarInfo', {
	closeButton : true,
	position : 'left',
	autoPan : false
});
map.addControl(sidebarInfo);

/*
 * Grupos de capas de Marcadores JSON
 */
layerGroupPlanos = L.layerGroup().addTo(map);
layerGroupFac = L.layerGroup().addTo(map);
layerGroupCam = L.layerGroup().addTo(map);
layerGroupGPS = L.layerGroup();
layerGroupSearch = L.layerGroup().addTo(map);



/* Marcadores GeoJSON
 *  Facultades:   Marcadores correspondientes a las facultades de la UV
 *  Campus:       Marcadores correspondientes a los campus de la UV
 * 
 */
L.geoJson(Facultades, {
	pointToLayer : function(feature, coordinates) {
//		console.log(feature);
//		console.log(coordinates);
		var geoMarker = (L.marker(coordinates, {
			icon : L.AwesomeMarkers.icon({
				icon : 'graduation-cap',
				markerColor : 'red',
				prefix : 'fa',
				extraClasses : 'fas',
				iconColor : 'black'
			})
		}));
		layerGroupFac.addLayer(geoMarker);
		return geoMarker;
	},
	onEachFeature : caracteristicasFacultades
}).addTo(map);

L.geoJson(Campus, {
	pointToLayer : function(feature, coordinates) {
		var geoMarker = (L.marker(coordinates, {
			icon : L.AwesomeMarkers.icon({
				icon : 'university',
				markerColor : 'green',
				prefix : 'fa',
				extraClasses : 'fas',
				iconColor : 'black'
			})
		}));
		layerGroupCam.addLayer(geoMarker);
		return geoMarker;
	},
	onEachFeature : caracteristicasCampus
}).addTo(map);
//.bindPopup('<img class=popupStyle src=//www.uv.es/uwm/imatges/GMaps/logo_uv.png width=88% alt=LogoUV><div class=popupTitol>'+\""+titol+"\"+'</div><div class=popupStyle>"+webPopUp+telPopUp.replace("'","&apos;")+emailPopUp+" </div><div>"+descripcio.trim()+"</div>'+'<div id=pano class=googleView> </div>'+'<button class=botoGoogle onclick=ActivarGoogleViewd"+i+"("+contMarkers+");>"+verStreeView+"</button>')
/* Boton Leaflet
 *  Permite abrir y cerrar el Sidebar de información de Profesores y Espacios dejando un marcador para no perder el resultado.
 * 
 */
L.easyButton('<span title="Marcap&aacute;ginas" class="fas fa-bookmark bookmark"></span>', function() {
	sidebarInfo.toggle();
}).addTo(map);

var animatedToggle = L.easyButton({
	id : 'animated-marker-toggle',
	position : 'topright',
	type : 'animate',
	states : [ {
		stateName : 'add-markers',
		icon : 'fa-map-marker marcaGPS',
		title : 'Activar GPS',
		onClick : function(control) {
			getGPS();
			control.state('remove-markers');
		}
	}, {
		stateName : 'remove-markers',
		title : 'Desactivar GPS',
		icon : 'fa-undo marcaGPS',
		onClick : function(control) {
			map.removeLayer(layerGroupGPS);
			control.state('add-markers');
		}
	} ]
});
animatedToggle.addTo(map);


var pnt =0;
var arrey='';
map.on('click', function(e) {
	closeAllSidebars();
	BuscarEspacio(e.latlng.lat,e.latlng.lng);
 });



/*
 * Funcion caracteristicasFacultades
 * @param {type} feature: características del JSON
 * @param {type} layer:   capas
 * 
 */

function caracteristicasFacultades(feature, layer) {
//	console.log("feautre" + feature);
	var popupContent = '<img class="popupStyle" src="//www.uv.es/uwm/imatges/GMaps/logo_uv.png" width="88%" alt="LogoUV">';
	popupContent += '<div class=popupTitol>' + feature.properties.name + '</div>';
	popupContent += '<div class=popupStyle>';
	if (feature.properties && feature.properties.web) {
		popupContent += '<p><i class="fas fa-globe" aria-hidden="true"></i><a href="http://www.uv.es/' + feature.properties.web + '">http://www.uv.es/' + feature.properties.web + '</a></p>';
	}
	if (feature.properties && feature.properties.telefono) {
		popupContent += '<p><i class="fas fa-phone" aria-hidden="true"></i>' + feature.properties.telefono + '</p>';
	}
	if (feature.properties && feature.properties.correo) {
		popupContent += '<p><i class="fas fa-envelope" aria-hidden="true"></i><a href="mailto:' + feature.properties.correo + '">' + feature.properties.correo + '</a></p>';
	}
	popupContent += '</div>';

	layer.bindPopup(popupContent);
}
function caracteristicasCampus(feature, layer) {
	var popupContent = '<img class="popupStyle" src="//www.uv.es/uwm/imatges/GMaps/logo_uv.png" width="88%" alt="LogoUV">';
	popupContent += '<div class=popupTitol>' + feature.properties.name + '</div>';
	popupContent += '<div class=popupStyle>';

	if (feature.properties && feature.properties.facultades) {
		var facultad = feature.properties.facultades.split(',');

		var contador = 0;
		while (contador < facultad.length) {

			popupContent += '<p><i class="fas fa-university" aria-hidden="true"></i>' + facultad[contador] + '</p>';
			contador++;
		}
	}

	popupContent += '</div>';

	layer.bindPopup(popupContent);
}

/*
 * Función tras cambiar zoom en el mapa
 * Se ocultan y muestran las capas ...... y ....
 */
map.on('zoomend', function () {

    // (map.getZoom > 10)
    
    if (map.getZoom() < 17) {
        $("#nivel").removeClass('level');
        $("#nivel").addClass('levelOFF');
        map.removeLayer(layerGroupFac);
        map.addLayer(layerGroupCam);
    } 
    if(map.getZoom() >= 17){
        $("#nivel").addClass('level');
        $("#nivel").removeClass('levelOFF');
        map.addLayer(layerGroupFac);
        map.removeLayer(layerGroupCam);
    }
    
});
/*************/

/* Consulta de Query */
if (queryString !== 'error') {
    
    if (queryString !== 'error'){
    var string = queryString.split(';');
    var recurso = string[0];
    var tipoRecurso = string[1];
    if (tipoRecurso === 'espacio') {
    	LocalizarEspacio(string[0]);
//        var req = $.ajax({
//            type: 'GET',
//            url: _serverDB + 'rest/v0/espacios/' + recurso,
//            dataType: 'json',
//            success: function (response, textStatus, errorThrown) {
//                /* Respuesta correcta */
//           
//                if (textStatus === 'success') {
//                    _nivelActual = response.piso;
//                    centro = [response.idcoordenada.latitud, response.idcoordenada.longitud];
//                    _currentPosition = centro;
//                    _zoom = 22;
//                    _data = response;
//                    
//                    LocalizarEspacio(response.idespacio);
//                    if (!$('.easy-button-container').hasClass("visible")) {
//                    $('.easy-button-container').addClass("visible");
//                }
//                }
//                if (textStatus === 'nocontent') {
//                    DefaultMap();
//                    openModalError(queryString);
//                }
//            },
//            error: function (response, textStatu, error) {
//                
//                /* Respuesta errónea */
//                DefaultMap();
//                openModalError(queryString);
//            },
//            async: false
//        });
    }
    else if (string[1] === 'profesor'){
    	LocalizarProfesor(string[0]);
//        var req = $.ajax({
//            type: 'GET',
//            url: _serverDB + 'rest/v0/profesores/' + string[0],
//            dataType: 'json',
//            success: function (response, textStatus, errorThrown) {
//                /* Respuesta correcta */
//                
//                if (textStatus === 'success') {
//                    _nivelActual = response.idespacio.piso;                 
//                    centro = [response.idespacio.idcoordenada.latitud, response.idespacio.idcoordenada.longitud];
//                    _currentPosition = centro;
//                    _zoom = 22;
//                    _data = response;
//                    
//                    LocalizarProfesor(response.idprofesor);
//                    if (!$('.easy-button-container').hasClass("visible")) {
//                        $('.easy-button-container').addClass("visible");
//                    }
//                }
//                if (textStatus === 'nocontent') {
//                    DefaultMap();
//                    openModalError(queryString);
//                }
//
//            },
//            error: function (response, textStatu, error) {
//                /* Respuesta errónea */
//                DefaultMap();
//                openModalError(queryString);
//            },
//            async: false
//        });
    }
    /* Respuesta por defecto sin queryString */
}
else{
    DefaultMap();
}
}
else{
    openModalError(queryString);
}

SetOptionLayers();   




/* Funciones */

function setPosition(lat, long, zoom, cierre){
    map.setView(new L.LatLng(lat, long), zoom,{animation: true});

    if (String(cierre) === 'true'){
        
        sidebarInfo.hide();
        sidebarFacultades.hide();
    }
}

/* Funcion openModalError
 * Abre un modal informando del error en la queryString
 */
function openModalError(string){
    
    
    var cadena = string.split(";");
    var tipoRecurso = cadena[1];
    var idRecurso = cadena[0];
    var html= '';
    
    if (tipoRecurso === 'espacio'){
        html += '<div class="claseerror">El espacio "'+ idRecurso+'" no es v&aacute;lido. Por favor, revisa tu enlace</div>';
    }
    if (tipoRecurso === 'profesor'){  
        html += '<div class="claseerror">El profesor "'+ idRecurso+'" no es v&aacute;lido. Por favor, revisa tu enlace</div>';
    }
    if ((tipoRecurso !== 'espacio')&&(tipoRecurso !== 'profesor')){
        html += '<div class="claseerror">El recurso que est&aacute; buscando es err\u00F3neo. Verifique su b&uacute;squeda e int&eacute;ntelo de nuevo.</div>'; 
    }
    map.fire('modal', {content: html});
}

/* Funcion openModalPano
 * Abre un modal con el visor de Panoramas
 */
function openModalPano(nombreEspacio){
    
    var html = '<div id="container" ><div class="panorama-name">'+ nombreEspacio+'</div>';
    html += '<div id="visor-panorama" ></div>';
        
    if (_listaPanos.length !== 0) {
        var j = 0;
        if (_listaPanos.length !== 1) {
            html += '<div class="panoramas"><ul class="pagination panorama tohide">';
            
            while (j <= _listaPanos.length - 1) {
//                console.log(_listaPanos[j].panorama);
                html += '<li id="panoTab'+(j)+'" class="page-item ';
                if (j === 0){
                    html += 'active';
                }      
                html += '"><a href="#" onclick="Change(\''+_listaPanos[j].panorama+'\','+j+');">'+(j+1)+'</a></li>';
                j++;
            }
            
            html += '</ul></div>';
        }          
    } 
        
    map.fire('modal', {content: html, MODAL_CONTENT_CLS: 'modal-content pano'});
    
     initPanorama(_listaPanos[0].enlace);
     animate();
}

function ModalClose(){
    map.closeModal();
}

 /* Funcion openSidebarLayers
  * Abre un módulo lateral con opciónes 
  */
function openSidebarLayers() {
    sidebarFacultades.hide();
    sidebarLayers.toggle();
}

 /* Funcion openSidebarFacultades
  * Abre un módulo lateral con listado de Facultades 
  */
function openSidebarFacultades() {
    sidebarLayers.hide();
    sidebarFacultades.toggle();
}
/* Funcion openSidebarInfo
  * Abre un módulo lateral con Información de profesores 
  */
function openSidebarInfo(data,tipo) {

    $('#profesor-info').empty();
//    console.log(tipo);    
    if (tipo === "profesores"){
    	
//    	console.log(data);
//        var asig = getAsignaturas(data.idprofesor);
//        var panos = getPanoramas(data.idespacio.idespacio);
        var html = '<div class="list-group grupo-ficha">';
        
        html += '<div href="#" class="list-group-item active"><ul class="list-inline"><li><h4>'+data.nombre+'</h4></li>'; 
//        if (data.espacios[0]){
//        	html += '<li class="icon-location" onclick="setPosition('+data.espacios[0].latitud+','+data.espacios[0].longitud+',21,\'true\')" ><i title="Mostrar posición en el mapa" alt="Mostrar posición en el mapa" class="fas fa-location-arrow"></i></li>';   
//        }
        if (data.foto != ""){
        	html += '<li><img class="foto-profesor" src="'+data.foto+'" /></li>';
        }
        
        html += '</ul>';
        html += '</div>';
        if (data.espacios[0]){
        	var panos = data.espacios[0].panoramas;
//        	console.log(panos);
        	html += '<div href="#" class=" redes-sociales list-group-item">';

        	html += '<a class="redes" onclick="setPosition('+data.espacios[0].latitud+','+data.espacios[0].longitud+',21,\'false\')" title="Mostrar posición en el mapa" href="#"><i class="fas fa-location-arrow fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-3x" ></i></a>';
	         html += '<a class="redes" onclick="MostrarURL(\''+data.idprofesor+';profesor\');" title="Link" href="#"><i class="fas fa-link fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-3x" ></i></a>';
	         html += '<a class="redes" onclick="ShareTwitter(\''+data.idprofesor+';profesor\');" title="Twitter" href="#"><i class="fab fa-twitter fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-3x" ></i></a>';
	         html += '<a class="redes" onclick="ShareFacebook(\''+data.idprofesor+';profesor\');" title="Facebook" href="#"><i class="fab fa-facebook-f fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-3x" ></i></a>';
	         if (panos.length != 0) {
	         	_listaPanos = panos;
	         	html += '<a class="redes" title="Ver Panoramas 360&deg;" href="#" onclick="openModalPano(\''+data.espacios[0].nombrees+'\');"><i class="fas fa-eye fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-2x" ></i></a>';
	         }
        }
        
        html += '</div>';
        html += '</div>';
        
        if (data.afiliacion != ""){
        	html += '<div href="#" class="list-group-item"><h4>Afiliación</h4><h6 class="ficha">'+data.afiliacion+'</h6></div>';
        }
        if (data.departamento != ""){
        	html += '<div href="#" class="list-group-item"><h4>Departamento</h4><h6 class="ficha">'+data.departamento+'</h6></div>';
        }
        html += '<div href="#" class="list-group-item"><h4>Correo</h4><h5 class="ficha"><a title="Enviar correo a '+data.nombre+'" alt="Enviar correo a '+data.nombre+'" href="mailto:'+data.idprofesor+'@uv.es'+'">'+data.idprofesor+'@uv.es'+'</a></h5></div>';
   
        if (data.espacios[0]){
        	html += '<div href="#" class="list-group-item"><h4>Despacho</h4><h5 class="ficha">'+data.espacios[0].idespacio.split('-')[1]+'</h5></div>';
            html += '<div href="#" class="list-group-item"><h4>Bloque</h4><h5 class="ficha">'+data.espacios[0].bloque+'</h5></div>';
            html += '<div href="#" class="list-group-item"><h4>Piso</h4><h5 class="ficha">'+data.espacios[0].piso+'</h5></div>';
            html += '<div href="#" class="list-group-item"><h4>Facultad</h4><div class="fichaFac"><img title="'+ data.espacios[0].edificioNombreES +'" class="img-fichaFac" src="' + _server+'xanos/'+ data.espacios[0].xano+'.svg"'+' alt="'+ data.espacios[0].edificioNombreES +'"><h5 class="text-fichaFac">'+data.espacios[0].edificioNombreES+'</h5></div></div>';

            
            
            setPosition(data.espacios[0].latitud, data.espacios[0].longitud, 21, "false");
            addMarker(data.espacios[0].latitud, data.espacios[0].longitud);
            _nivelActual = _nivelBusqueda = data.espacios[0].piso;
            SetOptionLayers();
            
            
            MostrarArea(data.espacios[0].boundingbox);
            ChangeMapLayer();
        }
        //        html += '<div href="#" class="list-group-item"><h4>Tutorias</h4>';
        
//        if ((data.tutorias !== null)&&(String(data.tutorias) !== 'undefined')){
//            
//            var periodos = data.tutorias.split(';');
//            if (periodos.length === 1){
//                 if (periodos === 'yes'){
//                     html += '<i class="material-icons md-36">contact_mail</i>';
//                 } 
//            }
//            else if (periodos.length === 2 ){
//                html += '<div class="row">';
//                
//                var tutos = periodos[0].split(',');
//                var i = 0;
//                while (i < tutos.length) {
//                    if (i === 0){
//                        html += '<div class="col-md-6">';
//                    }
//                    html += '<h5 class="ficha">' + tutos[i] + '</h5>';
//                    if (i === tutos.length - 1){
//                        html += '</div>';
//                    }
//                    i++;
//                }
//                
//                if (periodos[1] === 'yes'){
//                     html += '<div class="col-md-6 email" title="Participa en el programa de tutorias electrónicas" alt="Participa en el programa de tutorias electrónicas" ><i class="material-icons md-36">contact_mail</i><i class="material-icons md-36 done">done</i></div>';
//                }
//                else{
//                    html += '<div class="col-md-6 email" title="No participa en el programa de tutorias electrónicas" alt="No participa en el programa de tutorias electrónicas" ><i class="material-icons md-36">contact_mail</i><i class="material-icons md-36 cross">clear</i></div>';
//                }
//                
//                html += '</div>';
//               
//            }
//            else if (periodos.length === 3){
//                html += '<div class="row">';
//                var tutos = periodos[0].split(',');
//                var tutos2= periodos[1].split(',');
//                var i = 0;
//                
//                html += '<div class="col-md-6">';
//                html += '<h5>Primer Cuatrimestre</h5>';
//                while (i < tutos.length) {
//                    
//                    html += '<h5 class="ficha">' + tutos[i] + '</h5>';
//                    
//                    i++;
//                }
//                i=0;
//                
//                html += '<h5>Segundo Cuatrimestre</h5>';
//                while (i < tutos2.length) {
//                    
//                    html += '<h5 class="ficha">' + tutos2[i] + '</h5>';
//                    i++;
//                }
//                html += '</div>';
//                
//                if (periodos[2] === 'yes'){
//                     html += '<div class="col-md-6 email" title="Participa en el programa de tutorias electrónicas" alt="Participa en el programa de tutorias electrónicas" ><i class="material-icons md-36">contact_mail</i><i class="material-icons md-36 done">done</i></div>';
//                }
//                else{
//                    html += '<div class="col-md-6 email" title="No participa en el programa de tutorias electrónicas" alt="No participa en el programa de tutorias electrónicas" ><i class="material-icons md-36">contact_mail</i><i class="material-icons md-36 cross">clear</i></div>';
//                }
//                
//                html += '</div>';
//                
//            }
//            else{
//                html += '<h5 class="ficha">' + 'Informaci&oacute;n disponible en breve' + '</h5>';
//            }
// 
//        }
//        else{
//            html += '<h5 class="ficha">' + 'Informaci&oacute;n disponible en breve' + '</h5>';
//        }
//        
//        html += '</div>';
//        
//        html += '<div href="#" class="list-group-item"><h4>Asignaturas</h4>';
//        if (asig.length !== 0) {
//            var j=0;
//            while (j <= asig.length - 1) {
//                html += '<h5 class="ficha">' + asig[j].nombre + '</h5>';
//                j++;
//            }
//        }
//        else{
//            html += '<h5 class="ficha">' + 'Información disponible en breve' + '</h5>';
//        }
//        html += '</div></div>';
//        html += '<script>$(document).ready(function(){ $( "img.fb" ).hover( function() {$( this ).attr("src","images/social/fb-activo.svg" ); }, function() { $( this ).attr("src","images/social/fb-inactivo.svg");});});  </script>';
//        html += '<script>$(document).ready(function(){ $( "img.tw" ).hover( function() {$( this ).attr("src","images/social/tw-activo.svg" ); }, function() { $( this ).attr("src","images/social/tw-inactivo.svg");});});  </script>';
//        html += '<script>$(document).ready(function(){ $( "img.link" ).hover( function() {$( this ).attr("src","images/social/link-activo.svg" ); }, function() { $( this ).attr("src","images/social/link-inactivo.svg");});});  </script>';
//        html += '<script>$(document).ready(function(){ $( "img.icon-360" ).hover( function() {$( this ).attr("src","images/social/360-activo.png" ); }, function() { $( this ).attr("src","images/social/360-inactivo.png");});});  </script>';
//        setPosition(data.espacios[0].latitud, data.espacios[0].longitud, 21, "false");
//        addMarker(data.espacios[0].latitud, data.espacios[0].longitud);
//        _nivelActual = _nivelBusqueda = data.espacios[0].piso;
//        SetOptionLayers();
//        
//        
//        MostrarArea(data.espacios[0].boundingbox);
//        ChangeMapLayer();
        
        $('#sidebarInfo .sidebar-header .sidebar-header-icon span').attr('class', '').attr('class','fa fa-graduation-cap');
    }
    else if (tipo === "espacios"){
    	var panos;
//    	console.log(data);
    	if (data.panoramas){
    		panos = data.panoramas;
    	}
//        var panos = getPanoramas(data.idespacio);
    	
//        console.log(data);
        var html = '<div class="list-group grupo-ficha">';
        
        html += '<div href="#" class="list-group-item active"><ul class="list-inline"><li><h4>'+data.idespacio.split("-")[1]+'</h4></li>';
//        html += '<li class="icon-location" onclick="setPosition('+data.coordenada.latitud+','+data.coordenada.longitud+',22,\'false\')" ><i title="Mostrar posición en el mapa" alt="Mostrar posición en el mapa" class="fas fa-location-arrow"></i></li>';   
        
//        html += '<li class="icon-location" onclick="setPosition('+data.coordenada.latitud+','+data.coordenada.longitud+',22,\'true\')" ><img class="img-icon-location" src="images/social/location-inactivo.svg" title="Mostrar posici&oacute;n en el mapa" alt="Mostrar posici&oacute;n en el mapa"></li>';    
        html += '</ul>';
        html += '</div>';
        html += '<div class="redes-sociales list-group-item">';
        html += '<a class="redes" onclick="setPosition('+data.coordenada.latitud+','+data.coordenada.longitud+',21,\'false\')" title="Mostrar posición en el mapa" href="#"><i class="fas fa-location-arrow fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-3x" ></i></a>';
        
        html += '<a id="clipboardLink" class="redes" onclick="MostrarURL(\''+data.idespacio+';espacio\');" title="Link" href="#"><i class="fas fa-link fa-2x"  data-fa-mask="fas fa-circle fa-2x" ></i></a>';
        html += '<a class="redes" onclick="ShareTwitter(\''+data.idespacio+';espacio\');" title="Twitter" href="#"><i class="fab fa-twitter fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-2x" ></i></a>';
        html += '<a class="redes" onclick="ShareFacebook(\''+data.idespacio+';espacio\');" title="Facebook" href="#"><i class="fab fa-facebook-f fa-2x"  data-fa-mask="fas fa-circle fa-2x" ></i></a>';
        if(panos != null){
	        if (panos.length != 0) {
	        	_listaPanos = panos;
	        	html += '<a class="redes" title="Ver Panoramas 360&deg;" href="#" onclick="openModalPano(\''+data.nombrees+'\');"><i class="fas fa-eye fa-2x" data-fa-transform="shrink-3.5 down-1.6 right-1.25" data-fa-mask="fas fa-circle fa-2x" ></i></a>';
	        }
        }
        
        html += '</div>';
//        html += '<div href="#" class="list-group-item"><h4>Descripcion</h4><h5 class="ficha">'+data.descripcion+'</h5></div>';
        html += '<div href="#" class="list-group-item"><h4>Bloque</h4><h5 class="ficha">'+data.bloque+'</h5></div>';
        html += '<div href="#" class="list-group-item"><h4>Piso</h4><h5 class="ficha">'+data.piso+'</h5></div>';
        html += '<div href="#" class="list-group-item"><h4>Facultad</h4><div class="fichaFac"><img class="img-fichaFac" src="' + _server+"xanos/"+ data.edificio.xano+".svg" + '" alt="'+ data.edificio.nombrees +'"><div class="text-fichaFac">'+data.edificio.nombrees+'</div></div></div>';
        html += '<div href="#" class="list-group-item"><h4>Tipo</h4><h5 class="ficha">'+data.tipo+'</h5></div>';
        html += '</div>';
       
//        html += '<script>$(document).ready(function(){ $( "img.fb" ).hover( function() {$( this ).attr("src","images/social/fb-activo.svg" ); }, function() { $( this ).attr("src","images/social/fb-inactivo.svg");});});  </script>';
//        html += '<script>$(document).ready(function(){ $( "img.tw" ).hover( function() {$( this ).attr("src","images/social/tw-activo.svg" );  }, function() { $( this ).attr("src","images/social/tw-inactivo.svg");});});  </script>';
//        html += '<script>$(document).ready(function(){ $( "img.link" ).hover( function() {$( this ).attr("src","images/social/link-activo.svg" ); }, function() { $( this ).attr("src","images/social/link-inactivo.svg");});});  </script>';
//        html += '<script>$(document).ready(function(){ $( "img.icon-360" ).hover( function() {$( this ).attr("src","images/social/360-activo.png" ); }, function() { $( this ).attr("src","images/social/360-inactivo.png");});});  </script>';
//        console.log(data);
        addMarker(data.coordenada.latitud, data.coordenada.longitud);
        setPosition(data.coordenada.latitud, data.coordenada.longitud, 21, "false");
        addMarker(data.coordenada.latitud, data.coordenada.longitud);
        _nivelActual = _nivelBusqueda = data.piso;
        SetOptionLayers();
        
        
        MostrarArea(data.boundingbox);
        ChangeMapLayer();
    }

    $(html).appendTo('#profesor-info');
    sidebarLayers.hide();
    sidebarInfo.toggle();
    if (!$('.easy-button-container').hasClass("visible")){
        $('.easy-button-container').addClass("visible");
    }
}


 /* Funcion closeAllSidebars
  * Cierra todos los sidebar existentes 
  */
function closeAllSidebars() {
    sidebarFacultades.hide();
    sidebarLayers.hide();
    sidebarInfo.hide();
    clearMarkerSearch();
    BorrarArea();
    if ($('.easy-button-container').hasClass("visible")){
        $('.easy-button-container').removeClass("visible");  
    }
}

/* Funcion MostrarArea
 * Muestra el area que ocupa el espacio
 * @param {Blob} data
 * 
 */
function MostrarArea(data){

	var coords = data.split(',');
    var points = [];

    for (i=0; i < coords.length-1; i++){
    	points.push(new L.LatLng(coords[i].split(' ')[0],coords[i].split(' ')[1]));
    }
    _area = L.polygon(points, {color: 'red'});
    map.addLayer(_area);
//    console.log("SOK");
//    var polygon = L.polygon(points, {color: 'red'}).addTo(map);
}

/* Función BorrarArea
 * Elimina el area generada como resultado de una busqueda de espacio o profesor
 */
function BorrarArea(){
	if(map.hasLayer(_area)){
		map.removeLayer(_area);
	}
}

/*
 * Función que modifica el mapa activo de acuerdo a los valores globales de:
 * tema, denominacion y nivel
 * 
 */
function ChangeMapLayer(){
	var codigo;
	if (_toponimo === "c"){
		codigo = "co";
	}else{
		codigo = _locale;
	}
	
	layerGroupPlanos.eachLayer(function (layer) {
//		console.log(layer._url.substring(0, layer._url.lastIndexOf(".")-3)+_nivelActual+codigo+".svg");
	    layer.setUrl(layer._url.substring(0, layer._url.lastIndexOf(".")-3)+_nivelActual+codigo+".svg");
	
	});
	
//	console.log ("nivel: "+_nivelActual);
//	console.log ("Bus: "+_nivelBusqueda);

    if (_nivelActual == _nivelBusqueda){
            map.addLayer(_area);
            
    }
    else{
        map.removeLayer(_area);
    }
}


/*
 * Función de retorno GPS
 *  Recoge valores GPS y los envia a ShowPosition si es correcto
 *  Dispara showError si el GPS falla
 * 
 */
function getGPS() {
    if (navigator && navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError, {enableHighAccuracy: true});
    } else {
        alert("NO GPS");
    }
}
/*
 * Función que muestra Posicion del GPS y coloca una marca geográfica
 * en el mapa
 * @param LatLong position
 * 
 */
function showPosition(position){
   
    if ($('.dropdown-menu li:first-child span.labelGPS').hasClass('label-success')) {
        //console.log("OFF");
        $('.dropdown-menu li:first-child span.labelGPS').removeClass('label-success');
        $('.dropdown-menu li:first-child span.labelGPS').addClass('label-danger');
        $('.dropdown-menu li:first-child span.labelGPS').html("OFF");
        map.removeLayer(layerGroupGPS);

    } else {
        //console.log("ON");
        $('.dropdown-menu li:first-child span.labelGPS').removeClass('label-danger');
        $('.dropdown-menu li:first-child span.labelGPS').addClass('label-success');
        $('.dropdown-menu li:first-child span.labelGPS').html("ON");
        var marker = L.marker(new L.LatLng(position.coords.latitude, position.coords.longitude),
                {icon: L.AwesomeMarkers.icon({
                        icon: 'fa-map-marker',
                        markerColor: 'lightblue',
                        prefix: 'fa',
                        iconColor: 'black'})
                }
        );
        var marker2 = L.marker(new L.LatLng(map.getCenter().lat, map.getCenter().lng));
        layerGroupGPS.addLayer(marker);
        
        var group = new L.featureGroup([marker, marker2]);

        map.fitBounds(group.getBounds());
        map.addLayer(layerGroupGPS);
       
    }    
}
/*
 * Añade Marcador al mapa
 * @returns {undefined}
 */
function addMarker(Lat,Lng){
    var marker = L.marker(new L.LatLng(Lat, Lng),
                {icon: L.AwesomeMarkers.icon({
                        icon: 'circle',
                        markerColor: 'blue',
                        prefix: 'fa',
                        iconColor: 'black'})
                }
        );
        layerGroupSearch.addLayer(marker);
    
}
function clearMarkerSearch (){
    layerGroupSearch.clearLayers();
      
}

/*
 * Función que muestra en caso de error GPS
 * 
 */
function showError(error){
    console.log(error.code);
}

 /* Funciones JQuery
  * Disparadores para los cambios de estado de los
  * interruptores de las capas
  */
$(document).ready(function () {

    $("input[name=icons]").change(function () {
        var iconos = $('input:radio[name=icons]:checked').attr("value");
        if (iconos === "on") {
            map.addLayer(layerGroupFac);
            _iconos = true;
        } else {

            map.removeLayer(layerGroupFac);
            _iconos = false;
        }
        ChangeMapLayer();
    });
    $("input[name=tema]").change(function () {
        var tema = $('input:radio[name=tema]:checked').attr("value");
        if (tema === "c") {
            _tema = "c";
        } else {
            _tema = "b";
        }
        ChangeMapLayer();
    });
    $("input[name=denominacion]").change(function () {
        var denominacion = $('input:radio[name=denominacion]:checked').attr("value");
//        console.log(denominacion);
        if (denominacion === "d") {   
            _toponimo = "d";
        } else {
            _toponimo = "c";
        }
        ChangeMapLayer();
    });
    $("input[name=fondo]").change(function () {
        var fondo = $('input:radio[name=fondo]:checked').attr("value");
        if (fondo === "plano") {
            map.removeLayer(googleLayer);
            //map.removeLayer(osm);
            map.addLayer(mapboxTiles);
            _fondo = "plano";
        } else {
            map.removeLayer(mapboxTiles);
            //map.addLayer(osm);
            map.addLayer(googleLayer);
            _fondo = "sat";
        }
        ChangeMapLayer();
    });
    
    
    $("input[name=nivel]").change(function () {
        var nivel = $('input:radio[name=nivel]:checked').attr("value");
        
        if (nivel === "0") {
            map.addLayer(layerGroupFac);
            _nivelActual = "0";
        }
        if (nivel === "1") {
            map.addLayer(layerGroupFac);
            _nivelActual = "1";
        }
        if (nivel === "2") {
            map.addLayer(layerGroupFac);
            _nivelActual = "2";
        }
        if (nivel === "3") {
            map.addLayer(layerGroupFac);
            _nivelActual = "3";
        }
        ChangeMapLayer();
    });
     
//    $(".nav a[data-colapse='true']").on('click', function () {
//        if (isMobile.any()) {
//            $('.btn-navbar').click(); //bootstrap 2.x
//            $('.navbar-toggle').click(); //bootstrap 3.x by Richard
//        }
//});
    
    
    SetOptionLayers();
});

function SetOptionLayers(){
    if (_fondo === "plano"){
        $('#fondoON').addClass('active');
        $('#fondoON input').prop('checked',true);
    }
    else{
        $('#fondoOFF').addClass('active');
        $('#fondoOFF input').prop('checked',true);
    }
    
    if (_tema === "c"){
        $('#temaON').addClass('active');
        $('#temaON input').prop('checked',true);
    }
    else{
        $('#temaOFF').addClass('active');
        $('#temaOFF input').prop('checked',true);
    }
    console.log(_toponimo);
    if (_toponimo == 'd'){
        $('#topoON').addClass('active');
        $('#topoON input').prop('checked',true);
    }
    else{
        $('#topoOFF').addClass('active');
        $('#topoOFF input').prop('checked',true);
    }
    
    for (i=0; i<=3;i++){
    	
        $('#P'+i).removeClass('lvlsearch');
        $('#P'+i).removeClass('active');
        $('#P'+i).children('input').prop('checked', false);
    }
    
    
    $('#P'+ _nivelActual).addClass('active');
    $('#P'+_nivelActual).children('input').prop('checked',true);
    $('#P'+ _nivelBusqueda).addClass('lvlsearch');
}

function showLegend(){
//    var html = '<div class="tabbable" id="leyenda-tab"><ul class="nav nav-tabs"><li class="active"><a href="#panel-tema" data-toggle="tab">Tem&aacute;tico</a></li><li class=""><a href="#panel-iconos" data-toggle="tab">Iconos</a></li></ul></div>';
//    html += '<div id="leyenda"><div class="tab-content">';
//        html += '<div id="panel-tema" class="tab-pane active">';
        
    
   var html = '<div id="panel-iconos" class="tab-pane">';
        html += '<h3>Elementos gráficos</h3>';
        html += '<p>Podemos encontrar varios tipo de iconos en el mapa, algunos corresponden a edificios o campus y se muestran u ocultan en funcion del nivel de zoom o mediante configurción de las capas, permitiendo incluso hacer click para ver información adicional. El resto forman parte del GPS que nos indicará nuestra localización en el mapa respecto de nuestra visualización actual.</p>';
        html += '<div class="row clearfix">';
        html += '<div class="col-md-6 column">';
        html += '<h4>Iconos</h4>';
        html += '<ul class="list-unstyled list-graficos">';
            html += '<li><i class="fas fa-graduation-cap fa-2x iconlegend" aria-hidden="true"></i><div class="legend-marker" data-icon="facultad"></div>Facultad o Escuela</li>';
            html += '<li><i class="fas fa-university fa-2x iconlegend" aria-hidden="true"></i><div class="legend-marker" data-icon="campus"></div>Campus</li>';
            html += '<li><i class="fas fa-location-arrow fa-2x iconlegend" aria-hidden="true"></i><div class="legend-marker" data-icon="gps"></div>Localizaci&oacute;n de GPS</li>';
            html += '<li><i class="fas fa-circle fa-2x iconlegend" aria-hidden="true"></i><div class="legend-marker" data-icon="search"></div>Localizaci&oacute;n de b&uacute;squeda</li>';
        html += '</ul></div>';
    html += '<div class="col-md-6 column">';
        html += '<h4>Marcadores</h4>';
        html += '<ul class="list-unstyled list-icon">';
            html += '<li><div class="legend-icon"><i class="fas fa-eye fa-2x iconlegend" aria-hidden="true"></i></div>Panor&aacute;mica 360 grados</li>';
            html += '<li><div class="legend-icon"><i class="fas fa-link fa-2x iconlegend" aria-hidden="true"></i></div>Compartir Enlace</li>';
            html += '<li><div class="legend-icon"><i class="fab fa-twitter fa-2x iconlegend" aria-hidden="true"></i></div>Compartir en Twitter</li>';
            html += '<li><div class="legend-icon"><i class="fab fa-facebook fa-2x iconlegend" aria-hidden="true"></i></div>Compartir en Facebook</li>';
            html += '<li><div class="legend-icon"><i class="fas fa-bookmark fa-2x iconlegend" aria-hidden="true"></i></div>Marcador de b&uacute;squeda</li>';
        html += '</ul></div>';
    html += '</div></div>';
    html += '</div></div></div>';
    
    map.fire('modal',{content:html});
}
function showHelp(){ //cambiar contenido

     var html = '<h2>Ayuda</h2>';
    html += '<div class="row">';
    html += '<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 bhoechie-tab-container">';
    html += '<div class="list-group">';
    html += '<ul class="nav nav-tabs">';
	html += '<li class="nav-item">';
	html += '<a class="nav-link active" data-toggle="tab" href="#recursoHelp"><h4 class="fas fa-search fa-lg"></h4>Localizar Recurso</a>';
	html += '</li>';
	html += '<li class="nav-item">';
	html += '<a class="nav-link" data-toggle="tab" href="#interfazHelp"><h4 class="fas fa-bars fa-lg"></h4>Interfaz</a>';
	html += '</li>';
	html += '<li class="nav-item">';
	html += '<a class="nav-link" data-toggle="tab" href="#panoramicaHelp"><h4 class="far fa-image fa-lg"></h4>Panorámicas</a>';
	html += '</li>';
    html += '</ul>';
//    html += ' <a href="#" class="list-group-item active text-center">  <h4 class="fa fa-search fa-lg"></h4><br/>Localizar Recurso</a>';
//    html += ' <a href="#" class="list-group-item text-center"><h4 class="fa fa-bars fa-lg"></h4><br/>Interfaz</a>';
//    html += ' <a href="#" class="list-group-item text-center"><h4 class="fa fa-picture-o fa-lg"></h4><br/>Panor&aacute;micas</a>';

    html += '  </div>';
    
    html += '<div class="tab-content">';
    html += '<div class="tab-pane active container" id="recursoHelp">';
	    html += '<ul class="nav nav-tabs">';
		html += '<li class="nav-item">';
		html += '<a class="nav-link active" data-toggle="tab" href="#profesorHelp"><h4 class="fas fa-user fa-lg"></h4>Por Profesor</a>';
		html += '</li>';
		html += '<li class="nav-item">';
		html += '<a class="nav-link" data-toggle="tab" href="#espacioHelp"><h4 class="fas fa-location-arrow fa-lg"></h4>Por Espacio</a>';
		html += '</li>';
	    html += '</ul>';
	    html += '<div class="tab-content">';
	    html += '<div class="tab-pane active container" id="profesorHelp">';
	    	html += '<h3>Búsqueda por Profesor</h3>';
		    html += '<ul class="list-ayuda">';
		    html += '<li>Pulsar Buscador <span class="fa fa-search fa-lg"></span> </li>';
		    html += '<li>Seleccionar Ficha Profesor <span class="fa fa-user fa-lg"></span> </li>';
		    html += '<li>Introducir el nombre del profesor </li>';
		    html += '<li>Seleccionar profesor deseado  </li>';
		    html += '<li>Pulsar en el botón Localizar  </li>';
		    html += '</ul>';
	    html += '</div>';
	    html += '<div class="tab-pane container" id="espacioHelp">';
		    html += ' <h3>Búsqueda por Espacios</h3>';
		    html += '<ul class="list-ayuda">';
		    html += '<li>Pulsar Buscador <span class="fa fa-search fa-lg"></span> </li>';
		    html += '<li>Seleccionar Ficha Espacio <span class="fa fa-sitemap fa-lg"></span> </li>';
		    html += '<li>Introducir el nombre del espacio </li>';
		    html += '<li>Seleccionar espacio deseado  </li>';
		    html += '<li>Pulsar en el botón Localizar  </li>';
		    html += '</ul>';
	    html += '</div>';
    html += '</div>';
    html += '</div>';
    
    html += '<div class="tab-pane container" id="interfazHelp">';
    html += '<ul class="nav nav-tabs">';
	html += '<li class="nav-item">';
	html += '<a class="nav-link active" data-toggle="tab" href="#toponimoHelp"><h4 class="fas fa-clipboard-check fa-lg"></h4>Toponimos</a>';
	html += '</li>';
	html += '<li class="nav-item">';
	html += '<a class="nav-link" data-toggle="tab" href="#iconoHelp"><h4 class="fas fa-question-circle fa-lg"></h4>Iconos</a>';
	html += '</li>';
    html += '</ul>';
    html += '<div class="tab-content">';
    html += '<div class="tab-pane active container" id="toponimoHelp">';
	    html += ' <h3>Top&oacute;nimo</h3>';
	    html += '<h5>Mediante este control podremos variar entre las vistas de c&oacute;digo de espacio o la descripci&oacute; del mismo:</h5>';
	    html += '<ul class="list-ayuda">';
	    html += '<li>Pulsar Capas <span class="fa fa-list-ul fa-lg"></span> </li>';
	    html += '<li>Ir al apartado Fondo</li>';
	    html += '<li>Seleccionar el top&oacute;nimo deseado</li>';
	    html += '</ul>';
    html += '</div>';
    html += '<div class="tab-pane container" id="iconoHelp">';
	    html += ' <h3>Iconos</h3>';
	    html += '<h5>Mediante este control podremos variar entre las vistas de c&oacute;digo de espacio o la descripci&oacute; del mismo:</h5>';
	    html += '<ul class="list-ayuda">';
	    html += '<li>Pulsar Capas <span class="fa fa-list-ul fa-lg"></span> </li>';
	    html += '<li>Ir al apartado Iconos</li>';
	    html += '<li>Seleccionar activar/desactivar los iconos</li>';
	    html += '</ul>';
    html += '</div>';
    html += '</div>';
    html += '</div>';
    
    html += '<div class="tab-pane container" id="panoramicaHelp">';
    html += '<h3>Panoramas</h3>';
    html += '<h5>Mediante este control podremos variar entre las vistas de c&oacute;digo de espacio o la descripci&oacute; del mismo:</h5>';
    html += '<ul class="list-ayuda">';
    html += '<li><b>Arrastrar:</b> Deplazar la panor&aacute;mica </li>';
    html += '<li><b>Play:</b> Activar rotaci&oacute;n autom&aacute;tica </li>';
    html += '<li><b>Stop:</b> Desactivar rotaci&oacute;n autom&aacute;tica </li>';
    html += '<li><b>Pantalla Completa:</b> Activar el modo pantalla completa </li>';
    html += '</ul>';
    html += '</div>';
    
    html += '</div>';
    html += '</div>';
    
//    html += '    <!-- Interfaz section -->';
//    html += '    <div class="bhoechie-tab-content">';
//    html += '<ul class="nav nav-tabs">';
//    html += '<li class="active"><a data-toggle="tab" href="#ayuda-proveedor">Fondo</a></li>';
//    html += '<li><a data-toggle="tab" href="#ayuda-tema">Tema</a></li>';
//    html += '<li><a data-toggle="tab" href="#ayuda-topo">Top&oacute;nimo</a></li>';
//    html += '<li><a data-toggle="tab" href="#ayuda-icono">Iconos</a></li>';
//    html += '</ul>';
//
//    html += '<div class="tab-content">';
//    html += '<div id="ayuda-proveedor" class="tab-pane fade in active">';
//    html += '<h3>Fondo</h3>';
//    html += '<h5>Mediante este control podremos variar entre las vistas de plano y sat&eacute;lite:</h5>';
//    html += '<ul class="list-ayuda">';
//    html += '<li>Pulsar Capas <span class="fa fa-list-ul fa-lg"></span> </li>';
//    html += '<li>Ir al apartado Fondo</li>';
//    html += '<li>Seleccionar el fondo deseado</li>';
//    html += '</ul></div>';
//    html += '<div id="ayuda-tema" class="tab-pane fade">';
//    html += '<h3>Tema</h3>';
//    html += '<h5>Mediante este control podremos variar entre los planos b&aacute;sicos o categorizados:</h5>';
//    html += '<ul class="list-ayuda">';
//    html += '<li>Pulsar Capas <span class="fa fa-list-ul fa-lg"></span> </li>';
//    html += '<li>Ir al apartado Tema.</li>';
//    html += '<li>Seleccionar el tipo de plano deseado.</li>';
//    html += '</ul></div>';
//    html += '<div id="ayuda-topo" class="tab-pane fade">';
//    html += ' <h3>Top&oacute;nimo</h3>';
//    html += '<h5>Mediante este control podremos variar entre las vistas de c&oacute;digo de espacio o la descripci&oacute; del mismo:</h5>';
//    html += '<ul class="list-ayuda">';
//    html += '<li>Pulsar Capas <span class="fa fa-list-ul fa-lg"></span> </li>';
//    html += '<li>Ir al apartado Fondo</li>';
//    html += '<li>Seleccionar el top&oacute;nimo deseado</li>';
//    html += '</ul></div>';
//    html += '<div id="ayuda-icono" class="tab-pane fade">';
//    html += ' <h3>Iconos</h3>';
//    html += '<h5>Mediante este control podremos variar entre las vistas de c&oacute;digo de espacio o la descripci&oacute; del mismo:</h5>';
//    html += '<ul class="list-ayuda">';
//    html += '<li>Pulsar Capas <span class="fa fa-list-ul fa-lg"></span> </li>';
//    html += '<li>Ir al apartado Iconos</li>';
//    html += '<li>Seleccionar activar/desactivar los iconos</li>';
//    html += '</ul></div>';
//    html += '</div>';
//
//    html += '   </div>';
//
//
//
//    html += '   <!-- Panoramas search -->';
//    html += '   <div class="bhoechie-tab-content">';
//    html += '<h3>Panoramas</h3>';
//    html += '<h5>Mediante este control podremos variar entre las vistas de c&oacute;digo de espacio o la descripci&oacute; del mismo:</h5>';
//    html += '<ul class="list-ayuda">';
//    html += '<li><b>Arrastrar:</b> Deplazar la panor&aacute;mica </li>';
//    html += '<li><b>Play:</b> Activar rotaci&oacute;n autom&aacute;tica </li>';
//    html += '<li><b>Stop:</b> Desactivar rotaci&oacute;n autom&aacute;tica </li>';
//    html += '<li><b>Pantalla Completa:</b> Activar el modo pantalla completa </li>';
//    html += '</ul></div>';
//    html += '   </div>';
//
//
//    html += '  </div>';
//    html += ' </div>';
//    html += '</div>';



    map.fire('modal', {content: html});
    $("div.bhoechie-tab-menu>div.list-group>a").click(function (e) {
        e.preventDefault();
        $(this).siblings('a.active').removeClass("active");
        $(this).addClass("active");
        var index = $(this).index();
        $("div.bhoechie-tab>div.bhoechie-tab-content").removeClass("active");
        $("div.bhoechie-tab>div.bhoechie-tab-content").eq(index).addClass("active");
    });
}
$(document).ready(function() {
    $("div.bhoechie-tab-menu>div.list-group>a").click(function(e) {
        e.preventDefault();
        $(this).siblings('a.active').removeClass("active");
        $(this).addClass("active");
        var index = $(this).index();
        $("div.bhoechie-tab>div.bhoechie-tab-content").removeClass("active");
        $("div.bhoechie-tab>div.bhoechie-tab-content").eq(index).addClass("active");
    });
});

