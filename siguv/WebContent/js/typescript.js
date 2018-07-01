/* global Bloodhound, Handlebars, _serverDB */
    profes =''; 
    places ='';
$.ajax({
        type: 'GET',
        url: _serverDB + 'rest/v0/profesores',
        dataType: 'json',
        success: function(response, textStatus, errorThrown) {
                /* Respuesta correcta */
        	
                if(textStatus === 'success'){
                    profes = response;
                }
        },
        async: false
    });
  
    $.ajax({
        type: 'GET',
        url: _serverDB + 'rest/v0/espacios',
        dataType: 'json',
        success: function(response, textStatus, errorThrown) {
                /* Respuesta correcta */
                if(textStatus === 'success'){
                    places = response;
                }
        },
        async: false
    });
function typeahead() {
    
    var profesores = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('usuario'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        local: profes
    });
    
    var espacios = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('idespacio'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        local: places
    });

    $('#busqueda-tab-profesor .typeahead').typeahead({
        hint: true,
        highlight: true,
        minLength: 1,
        classNames: {
            input: 'Typeahead-input',
            hint: 'Typeahead-hint',
            selectable: 'Typeahead-selectable',
            menu: 'Typeahead-menu',
            dataset: 'Typeahead-dataset',
            suggestion: 'Typeahead-suggestion',
            empty: 'Typeahead-empty',
            open: 'Typeahead-open',
            cursor: 'Typeahead-cursor'
        }
    },
    {
        name: 'profesores',
        display: function(item){ return item.usuario},
        displayKey: 'usuario',
        source: profesores,
        templates: {
            suggestion: Handlebars.compile('<div class="typeahead-resultados-profesores">{{usuario}}</div>')
        }
    });
    
    $('#busqueda-tab-espacio .typeahead').typeahead({
        hint: true,
        highlight: true,
        minLength: 1,
        
        
        classNames: {
            input: 'Typeahead-input',
            hint: 'Typeahead-hint',
            selectable: 'Typeahead-selectable',
            menu: 'Typeahead-menu',
            dataset: 'Typeahead-dataset',
            suggestion: 'Typeahead-suggestion',
            empty: 'Typeahead-empty',
            open: 'Typeahead-open',
            cursor: 'Typeahead-cursor'
        }
    },
    {
        name: 'espacios',
        display: 'idespacio',
      displayKey:'idespacio',
      
//                displayKey: function(item){ 
//                	console.log(item);
//                	return item.idespacio.split("-")[1]
//                	},
//                sorter: function (items) {
//                	    return items.idespacio.sort();
//                },
        cancelButton: true, 
        source: espacios,
        offset: true,
        
        templates: {
        	suggestion: Handlebars.compile('<div class="typeahead-resultados-espacios">{{ idespacio }} &#45; <span class="label label-primary">{{nombrees}}</span></div>')
//            suggestion: '<div class="typeahead-resultados-espacios">{{ idespacio }} &#45; <span class="label label-primary">{{nombrees}}</span></div>'
        }
    });

    $('#busqueda-tab-todo .typeahead').typeahead({
        highlight: true,
        hint: true,
        minLength: 1,
        classNames: {
            input: 'Typeahead-input',
            hint: 'Typeahead-hint',
            selectable: 'Typeahead-selectable',
            menu: 'Typeahead-menu',
            dataset: 'Typeahead-dataset',
            suggestion: 'Typeahead-suggestion',
            empty: 'Typeahead-empty',
            open: 'Typeahead-open',
            cursor: 'Typeahead-cursor'
        }
    },
            {
                name: 'profesores',
                display: 'usuario',
                source: profesores,
                templates: {
                    header: '<div class="typeahead-header-resultados"><span class="fas fa-users"></span>Profesores</h3>',
                    suggestion: Handlebars.compile('<div class="typeahead-resultados-profesores">{{usuario}} </div>')
                }
            },
            {
                name: 'espacios',
                display: 'idespacio',
                source: espacios,
                templates: {
                    header: '<div class="typeahead-header-resultados"><span class="fas fa-sitemap"></span>Espacios</h3>',
                    suggestion: Handlebars.compile('<div class="typeahead-resultados-espacios">{{ idespacio}} &#45; <span class="label label-primary">{{edificio.nombrees}}</span> </div>')
                }
            });

    //update al seleccionar profesor
    $('#busqueda-tab-profesor .typeahead').on('typeahead:selected', function (evt, item) {
        $('#localizar-profesor').attr('onclick', '').attr('onclick', 'LocalizarProfesor("' + item.idprofesor + '");');
    });

    //update al seleccionar espacio
    $('#busqueda-tab-espacio .typeahead').on('typeahead:selected', function (evt, item) {
        $('#localizar-espacio').attr('onclick', '').attr('onclick', 'LocalizarEspacio("' + item.idespacio + '");');
    });

    //update al seleccionar todo
    $('#busqueda-tab-todo .typeahead').on('typeahead:selected.espacios', function (evt, item) {
        
        if (item.idprofesor) {

            $('#localizar-all').show();
            $('#listaDocentes').empty();
            $('#listaTodo').empty();
            $('#localizar-all').attr('onclick', '').attr('onclick', 'LocalizarProfesor("' + item.idprofesor + '");');
        }

        if (item.bloque) {

            $('#localizar-all').show();
            $('#listaDocentes').empty();
            $('#listaTodo').empty();
            $('#localizar-all').attr('onclick', '').attr('onclick', 'LocalizarEspacio("' + item.idespacio + '");');
        }

    });

    /*
     * Funcion de Handlebars para separar cadenas de texto
     * @param String cadena
     * @return String
     */
    Handlebars.registerHelper('splitString', function (string) {
    	var cadena;
    	
    	
    	
//    	console.log(string);
//    	console.log(string.split("-")[1]);
    	
    	
    	return string.split("-")[1];
    });
    /*
     * Funcion de Handlebars para crear subcadenas de texto
     * @param String cadena
     * @return String
     */
    Handlebars.registerHelper('trimString', function (string) {
        var cadena;
        
        
        cadena = string + '';
        console.log(cadena);
        if (cadena !== 'undefined') {
            var theString = cadena.split(',');
            console.log(theString);
            cadena = new Handlebars.SafeString(theString[0]);
        } else {
            cadena = '';
        }
        return cadena;
    });
    // Completar base de datos...

}

function Buscador() {
	

    var html = '<div class="modal-header">';

    html += '   <h4 class="modal-title">Buscador</h4>';
    html += '            </div>';
    html += '           <div class="modal-body">';
    html += '              <p> Buscador que nos permite localizar cualquier estancia de la ETSE, desde el punto de vista de las personas, las estancias, las denominaciones de espacios, o las aulas .</p>';
    html += '              <ul class="nav nav-tabs">';
    html += '     <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#busqueda-tab-todo"><span class="fas fa-search"></span><span class="modal-tab-text">Todo</span></a></li>';
    html += '     <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#busqueda-tab-profesor"><span class="fas fa-users"></span><span class="modal-tab-text">Profesor</span></a></li>';
    //html += '     <li><a data-toggle="tab" href="#busqueda-tab-asignatura"><span class="fa fa-graduation-cap"></span><span class="modal-tab-text">Asignatura</span></a></li>';
    html += '     <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#busqueda-tab-espacio"><span class="fas fa-sitemap"></span><span class="modal-tab-text">Espacio</span></a></li>';
    html += ' </ul>';

    html += ' <div class="tab-content">';
    html += '     <div id="busqueda-tab-todo" class="tab-pane active container">';
    html += '         <div class="clearM1"></div>';
    html += '         <p><input type="text" class="typeahead" placeholder="Búsqueda por todo"></p>';
    html += '         <div id="listaTodo">';

    html += '         </div>';
    html += '         <button id="localizar-all" type="button" class="btn btn-default" >Localizar</button>';
    html += '     </div>';

    html += '     <div id="busqueda-tab-profesor" class="tab-pane container">';
    html += '         <div class="clearM1"></div>';
    html += '         <p><input type="text" class="typeahead" placeholder="Búsqueda por profesor"></p>';
    html += '         <input type="hidden" id="todo">';
    html += '         <div id = "resultados-profesor"></div>';
    html += '         <button id="localizar-profesor" type="button" class="btn btn-default"  >Localizar</button>';

    html += '     </div>';
//    html += '     <div id="busqueda-tab-asignatura" class="tab-pane fade">';
//    html += '         <div class="clearM1"></div>';
//    html += '<p><input type="text" class="typeahead" placeholder="Campo de texto"></p>';
//
//    html += '         <div id="listaDocentes">';
//
//    html += '         </div>';
//    html += '     </div>';
    html += '     <div id="busqueda-tab-espacio" class="tab-pane container">';
    html += '         <div class="clearM1"></div>';
    html += '         <p><input type="text" class="typeahead" placeholder="Búsqueda por espacios"></p>';
    html += '         <button id="localizar-espacio" type="button" class="btn btn-default">Localizar</button>';

    html += '     </div>';
    html += '  </div>';
    html += '</div>';
    html += '<div class="modal-footer">';
    html += '      <button type="button" class="btn btn-default" onclick="ModalClose();">Close</button>';
    html += '     </div>';


    map.fire('modal', {content: html, MODAL_CONTENT_CLS: 'modal-content search'});
    typeahead();
    //$('.search').css('margin-top', 100);
}