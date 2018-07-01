/* global THREE, THREEx */
var object;
var isPlay = false;
function initPanorama(idPano) {

    var container, mesh;
    //THREE.ImageUtils.crossOrigin = '';
    container = document.getElementById('visor-panorama');

    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 1, 1100);
    camera.target = new THREE.Vector3(0, 0, 0);

    scene = new THREE.Scene();

    var loader = new THREE.TextureLoader();
    loader.crossOrigin = true;
    
//    loader.load('http://147.156.82.219/recursos/panoramicas/' + idPano + '.JPG', function (texture) {
    loader.load(_server+'panoramas/'+ idPano, function (texture) {
        var geometry = new THREE.SphereGeometry(500, 60, 40);
        geometry.scale(-1, 1, 1);
        var mat = new THREE.MeshBasicMaterial({
            map: texture
        });

        mesh = new THREE.Mesh(geometry, mat);
        object = mesh;
        scene.add(mesh);
    });
    
    /*var html = '<div class="panorama-control">';
        html += '</div>';
        
    $( "canvas" ).after(html);*/
        

    renderer = new THREE.WebGLRenderer();
    //renderer = new THREE.CanvasRenderer();
    renderer.setPixelRatio(window.devicePixelRatio);
    //console.log($('#container').width());
    
    $('.pano').css('max-width', (window.innerWidth / 2)+30 );
     $('.pano').css('margin-top', 100 );
    /*$('.pano').css('height', (window.innerHeight / 2) );*/
    renderer.setSize(window.innerWidth / 2, window.innerHeight / 2);
    container.appendChild(renderer.domElement);

    document.addEventListener('mousedown', onDocumentMouseDown, false);
    document.addEventListener('mousemove', onDocumentMouseMove, false);
    document.addEventListener('mouseup', onDocumentMouseUp, false);
    document.addEventListener('mousewheel', onDocumentMouseWheel, false);
    document.addEventListener('MozMousePixelScroll', onDocumentMouseWheel, false);
  

    //

    document.addEventListener('dragover', function (event) {

        event.preventDefault();
        event.dataTransfer.dropEffect = 'copy';

    }, false);

    document.addEventListener('dragenter', function (event) {

        document.body.style.opacity = 0.5;

    }, false);

    document.addEventListener('dragleave', function (event) {

        document.body.style.opacity = 1;

    }, false);

    document.addEventListener('drop', function (event) {

        event.preventDefault();

        var reader = new FileReader();
        reader.addEventListener('load', function (event) {

            material.map.image.src = event.target.result;
            material.map.needsUpdate = true;

        }, false);
        reader.readAsDataURL(event.dataTransfer.files[ 0 ]);

        document.body.style.opacity = 1;

    }, false);

    //
    WindowResize();
    //window.addEventListener('resize', onWindowResize, false);
    var html = '<div class="panorama-control">';
        html +='<div id="iniR" title="Iniciar Rotación" onclick="iniciaRotacion();" class="control-pano button"><i class="large material-icons">play_arrow</i></i></div>';
        html +='<div id="stopR" title="Detener Rotación" onclick="detenerRotacion();" class="control-pano button"><i class="large material-icons">stop</i></div>';
        html +='<div id="fullscreen" title="Activar Pantalla Completa" onclick="fullscreen();" class="control-pano button request"><i class="large material-icons">fullscreen</i></div>';
        html +='<div id="fullscreen_cancel" title="Desactivar Pantalla Completa" onclick="cancelFullScreen();" style="display: none;" class="control-pano button cancel"><i class="large material-icons">fullscreen_exit</i></div>';
        html += '</div>';
        
     $('canvas').after(html);
     $('#stopR').hide();
     //$('panorama-control').css("width",(window.innerWidth / 2));
     isPlay = false;
     
    THREEx.FullScreen.bindKey({
        dblclick: true
    });

    document.querySelector(".button.request").addEventListener('click', function () {
        THREEx.FullScreen.request();

    }, false);

    document.querySelector(".button.cancel").addEventListener('click', function () {
        THREEx.FullScreen.cancel();

    }, false);
    
     $( "canvas" ).mousedown(function(event) {
        onDocumentMouseDown(event);
});
}

function Change(idPano, id) {

    var loader = new THREE.TextureLoader();
    loader.crossOrigin = true;
//    var tex = loader.load(_serverPano + idPano + '.jpg');
    var tex = loader.load(_server+'panoramas/'+ idPano);
    object.material.map = tex;
    object.material.needsUpdate = true;
  
    var nTabs = $('ul.pagination.panorama li').length;
    for (indice=0; indice < nTabs; indice++){  
        if (indice === id){
            $('#panoTab'+indice).addClass('active');
        }
        else{
            $('#panoTab'+indice).removeClass('active');
        }
    }
}

function cancelFullScreen(){
    WindowResize();
}

function WindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    $('.pano').css('max-width', (window.innerWidth / 2)+30 );
    $('panorama-control').css("width",(window.innerWidth / 2));

    renderer.setSize(window.innerWidth / 2, window.innerHeight / 2);
    $('#fullscreen').show();
    $('#fullscreen_cancel').hide();

}
function fullscreen() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    $('.pano').css('max-width', (window.innerWidth));
    $('.pano').css({width: '100%', height: 'auto'});
    $('#fullscreen').hide();
    $('#fullscreen_cancel').show();
    //$('panorama-control').css("width",(window.innerWidth));

    renderer.setSize(window.innerWidth, window.innerHeight);
    THREEx.FullScreen.request();
}

function iniciaRotacion(){
    
    $('#iniR').hide();
    $('#stopR').show();
    isPlay = true;
    
}
function detenerRotacion(){
    $('#stopR').hide();
    $('#iniR').show();
    isPlay = false;
}




function onDocumentMouseDown(event) {

    event.preventDefault();

    isUserInteracting = true;

    onPointerDownPointerX = event.clientX;
    onPointerDownPointerY = event.clientY;

    onPointerDownLon = lon;
    onPointerDownLat = lat;
    
    

}

function onDocumentMouseMove(event) {

    if (isUserInteracting === true) {

        lon = (onPointerDownPointerX - event.clientX) * 0.1 + onPointerDownLon;
        lat = (event.clientY - onPointerDownPointerY) * 0.1 + onPointerDownLat;
        
    }
}

function onDocumentMouseUp(event) {
    isUserInteracting = false;
}

function onDocumentMouseWheel(event) {

    // WebKit

    if (event.wheelDeltaY) {

        camera.fov -= event.wheelDeltaY * 0.05;

        // Opera / Explorer 9

    } else if (event.wheelDelta) {

        camera.fov -= event.wheelDelta * 0.05;

        // Firefox

    } else if (event.detail) {

        camera.fov += event.detail * 1.0;

    }

    camera.updateProjectionMatrix();

}

function animate() {

    requestAnimationFrame(animate);
    update();

}

function update() {
    
    
        
    if (isPlay === true){
        if (isUserInteracting === false) {

                lon += 0.1;

            }
    }

    lat = Math.max(-85, Math.min(85, lat));
    phi = THREE.Math.degToRad(90 - lat);
    theta = THREE.Math.degToRad(lon);
    
    camera.target.x = 500 * Math.sin(phi) * Math.cos(theta);
    camera.target.y = 500 * Math.cos(phi);
    camera.target.z = 500 * Math.sin(phi) * Math.sin(theta);
    
    camera.lookAt(camera.target);

    renderer.render(scene, camera);
}