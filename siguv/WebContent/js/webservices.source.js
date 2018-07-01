$("#map").ready(function(){

		$.getJSON(_serverDB + 'rest/v0/edificios', function(v) {
			var html = '<table class="table table-hover"><tbody>';
		
			$.each(v, function(i, data) {
		
				var point1 = L.latLng(data.topleft.split(",")[0], data.topleft.split(",")[1]);
				var point2 = L.latLng(data.topright.split(",")[0], data.topright.split(",")[1]);
				var point3 = L.latLng(data.bottomleft.split(",")[0], data.bottomleft.split(",")[1]);
				
				var	bounds = new L.LatLngBounds(point1, point2).extend(point3);
		
				map.fitBounds(bounds);
		
		
				var overlay = L.imageOverlay.rotated(_server + 'planos/' + data.idedificio + _nivelActual + _locale + '.svg', point1, point2, point3, {
					opacity : 0.8,
					interactive : false
				});
		//		overlay.reposition(point1.getLatLng(), point2.getLatLng(), point3.getLatLng());
				layerGroupPlanos.addLayer(overlay);
				html += '<tr onclick="setPosition(' + data.coordenada.latitud + ',' + data.coordenada.longitud +', 19,true);"><td><img title="'+data.nombrees+'" alt="'+data.nombrees+'" width= "50px" src="'+ _server+'xanos/'+ data.xano + '">' + data.nombrees + '</td></tr>';
			});	
			
			 html += "</tbody></table>";
		     $(html).appendTo('#facultades');
		});
		
});


function LocalizarProfesor(id) {
	console.log("id: "+id);
    $.getJSON(_serverDB + 'rest/v0/profesores/' + id, function (data) {
    	
    	//console.log("data: "+data.visibilidad);

        if (data.visibilidad == true){
        	
        	$.ajax({
       		 url: _serverDB + 'rest/v0/profesores/buscar?user='+id,
       		 dataType: 'json',
       		 success: function( data ) {
       		   console.log( 'SUCCESS: ', data );
	       		
	            $('#busqueda-tab-todo .typeahead').typeahead('val', '');
	            $('#busqueda-tab-profesor .typeahead').typeahead('val', '');
	            $('#busqueda-tab-asignatura .typeahead').typeahead('val', '');
	            $('#listaDocentes').empty();
	            $('#listaTodo').empty();
	  
	            openSidebarInfo(data, "profesores");
	       		 }
       		 
       		});
        	
        	
//            setPosition(data.idespacio.idcoordenada.latitud, data.idespacio.idcoordenada.longitud, 21, "false");
//            addMarker(data.idespacio.idcoordenada.latitud, data.idespacio.idcoordenada.longitud);
//            $('#busqueda-tab-todo .typeahead').typeahead('val', '');
//            $('#busqueda-tab-profesor .typeahead').typeahead('val', '');
//            $('#busqueda-tab-asignatura .typeahead').typeahead('val', '');
//            $('#listaDocentes').empty();
//            $('#listaTodo').empty();
//  
//            openSidebarInfo(data, "profesores");
//            _nivelActual = _nivelBusqueda = data.idespacio.piso;
//            SetOptionLayers();
//            
//            MostrarArea(data.idespacio.boundingbox);
//            ChangeMapLayer();
            //console.log(_nivelActual+' '+_nivelBusqueda);
        }
        else{
            var html = '<div class="claseerror">';
                html += 'Lo sentimos, pero el recurso "'+data.nombre+'" no es accesible por lo que no se mostrar\u00e1 informaci\u00f3n al respecto. Disculpe las molestias';
                html += '</div>';
            map.fire('modal', {content: html});
        }
    });
    ModalClose();
}
function BuscarEspacio(lat,long){
//	BorrarArea();
	$.ajax({
		 url: _serverDB + 'rest/v0/espacios/buscar?lat='+lat+'&long='+long+'&piso='+_nivelActual,
		 dataType: 'json',
		 success: function( data ) {
			 LocalizarEspacio(data.idespacio);
//		   console.log( 'SUCCESS: ', data );
		 }
		 
	});
	
//	$.getJSON(_serverDB + 'rest/v0/espacios/buscar?lat='+lat+'&long='+long+'&piso='+_nivelActual, function (data) {
//		console.log(data);
//	},function(XMLHttpRequest, textStatus, errorThrown){
//        console.log("Error");
//    });
}

function LocalizarEspacio(id) {
    $.getJSON(_serverDB + 'rest/v0/espacios/' + id, function (data) {
    	//console.log(data);

        if (data.visibilidad == true){
            setPosition(data.coordenada.latitud, data.coordenada.longitud, 21, "false");
            addMarker(data.coordenada.latitud, data.coordenada.longitud);
            $('#busqueda-tab-todo .typeahead').typeahead('val', '');
            $('#busqueda-tab-profesor .typeahead').typeahead('val', '');
            $('#busqueda-tab-asignatura .typeahead').typeahead('val', '');
            $('#listaDocentes').empty();
            $('#listaTodo').empty();

            openSidebarInfo(data, "espacios");
            
            //console.log(_nivelActual+' '+_nivelBusqueda);
        }
        else{
            var html = '<div class="claseerror">';
                html += 'Lo sentimos, pero el recurso "'+data.nombre+'" no es accesible por lo que no se mostrar\u00e1 informaci\u00f3n al respecto. Disculpe las molestias';
                html += '</div>';
            map.fire('modal', {content: html});      
        }
    });
    ModalClose();
}


function ListarDocentesAsignatura(idAsignatura) {
    var profesores;
    $.ajax({
        type: 'GET',
        url: _serverDB + 'webresources/asignaturas/'+idAsignatura+'/profesores',
        dataType: 'json',
        success: function(response, textStatus, errorThrown) {
                /* Respuesta correcta */
                if(textStatus === 'success'){
                    profesores = response;
                }
        },
        async: false
    });
    return profesores;
}

function getAsignaturas(idProfesor){
    var asignaturas;
    $.ajax({
        type: 'GET',
        url: _serverDB + 'webresources/profesores/'+idProfesor+'/asignaturas',
        dataType: 'json',
        success: function(response, textStatus, errorThrown) {
                /* Respuesta correcta */
                if(textStatus === 'success'){
                    asignaturas = response;
                }
        },
        async: false
    });
    return asignaturas;
}
function getPanoramas(idEspacio){
    var panoramas;
    $.ajax({
        type: 'GET',
        url: _serverDB + 'webresources/espacios/'+idEspacio+'/panoramas',
        dataType: 'json',
        success: function(response, textStatus, errorThrown) {
                /* Respuesta correcta */
                if(textStatus === 'success'){
                    //console.log("done");
                    panoramas = response;
                    
                }
                /* Respuesta errónea */
                else{
                    //console.log("fail");
                    
                }
        },
        async: false
    });
    //console.log("salgo");
    return panoramas;
    
}

function GetQueryStringParams(){
    var query = 'error';
    var flag = false;
    
    var sPageURL = window.location.search.substring(1);

    if (!sPageURL) {
        query = 'default';
    }
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var hayquery = sURLVariables[i].split('=');
        if (hayquery !== -1) {
            var sParameterName = sURLVariables[i].split('=');
            if ((sParameterName[0] === "espacio") && (flag !== true))
            {
                if (typeof (sParameterName[1]) !== 'undefined') {
                    query = sParameterName[1] + ';' + "espacio";
                    flag = true;
                    //console.log("espacioID");
                }

            } else if ((sParameterName[0] === "profesor") && (flag !== true))
            {
                if (typeof (sParameterName[1]) !== 'undefined') {
                    query = sParameterName[1] + ';' + "profesor";
                    flag = true;
                    //console.log("profesorID");
                }
            }
        }
    }
    return query;
    
}

function MostrarURL(recurso){
    //console.log(window.location.origin+window.location.pathname);
//    
    var aux = recurso.split(';');
    var idrecurso = aux[0];
    var tipoRecurso = aux[1];
    var url = '';
    
    if (tipoRecurso === 'espacio') {
        url = window.location.origin + "/siguv/index.xhtml?espacio=" + idrecurso;
    } else if (tipoRecurso === 'profesor') {
        url = window.location.origin + "/siguv/index.xhtml?profesor=" + idrecurso;
    }
    
//    var html = '<div class="urlbox"><div class=""></div><div id="url">' + url + '</div><div class="btn" data-clipboard-target="#url"><img class="copy" src="images/social/copy-inactivo.svg" alt="Copiar al Portapapeles" title="Copiar al Portapapeles"></div></div>';
    var html = '<div class="urlbox row">';
//    	html += '<div class=""></div>';
    	html += '<div class="col-10">';
    		html += '<input id="url" class="form-control" value="'+url+'">';
    	html += '</div>';
    	html += '<div class="col-2">';
    		html += '<div class="btn withoutPadding" data-clipboard-target="#url">';
    		html += '<i class="fas fa-clipboard fa-2x" ></i>';
    	html += '</div>';
    	html += '</div>';
    	html += '</div>';
    	
   
    map.fire('modal', {content: html});
    var clipboard = new ClipboardJS('.btn');
    //$('#url').append(url);
//     $( ".btn" ).hover(
//        function() {$('.copy').attr('src','images/social/copy-activo.svg');},
//        function() {$('.copy').attr('src','images/social/copy-inactivo.svg');});
    
    //console.log(window.location.pathname);
}
function ShareTwitter(idrecurso){
	
	console.log(idrecurso.split(";")[1]);
    var opciones = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
    var url;
    if(idrecurso.split(";")[1] == 'profesor' ){
    	url = window.location.origin+"/siguv/index.xhtml?profesor="+idrecurso.split(";")[0];
    }
    else{
    	url = window.location.origin+"/siguv/index.xhtml?espacio="+idrecurso.split(";")[0];
    }
    var html = 'http://twitter.com/share?url='+url;
    window.open(html, '_blank',opciones);
    
   
}
function ShareFacebook(idrecurso){
	console.log(idrecurso.split(";")[1]);
    var opciones = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
    var url;
    if(idrecurso.split(";")[1] == 'profesor' ){
    	url = window.location.origin+"/siguv/index.xhtml?profesor="+idrecurso.split(";")[0];
//    	url = "http://147.156.148.9:8080"+"/siguv/index.xhtml?profesor="+idrecurso.split(";")[0];
    }
    else{
    	url = window.location.origin+"/siguv/index.xhtml?espacio="+idrecurso.split(";")[0];
//    	url = "http://147.156.148.9:8080"+"/siguv/index.xhtml?espacio="+idrecurso.split(";")[0];
    }
    var html = 'https://www.facebook.com/sharer/sharer.php?u='+url;
    window.open(html, '_blank',opciones);

}
function recovery(){
	

	var ajax = $.ajax({
	    type: 'POST',
	    // make sure you respect the same origin policy with this url:
	    // http://en.wikipedia.org/wiki/Same_origin_policy
//	    url: _serverDB+'rest/v0/recovery?usuario='+$('#inputUser').val()+'&correo='+$('#inputEmail').val(),
	    url: _serverDB+'rest/v0/recovery',
	    data: { 
	        'correo': $('#inputEmail').val() 
	    },
	    success: function(msg){
	        console.log('wow' + msg);
	    }
	});
	console.log (ajax);
}

function CallSucceed(json){
    console.log("dentro");
    query = json;
}