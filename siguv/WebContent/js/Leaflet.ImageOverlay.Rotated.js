L.ImageOverlay.Rotated=L.ImageOverlay.extend({initialize:function(t,i,a,e,s){"string"==typeof t?this._url=t:this._rawImage=t,this._topLeft=L.latLng(i),this._topRight=L.latLng(a),this._bottomLeft=L.latLng(e),L.setOptions(this,s)},onAdd:function(t){this._image||(this._initImage(),this.options.opacity<1&&this._updateOpacity()),this.options.interactive&&(L.DomUtil.addClass(this._rawImage,"leaflet-interactive"),this.addInteractiveTarget(this._rawImage)),t.on("zoomend resetview",this._reset,this),this.getPane().appendChild(this._image),this._reset()},onRemove:function(t){t.off("zoomend resetview",this._reset,this),L.ImageOverlay.prototype.onRemove.call(this,t)},_initImage:function(){var t=this._rawImage;this._url&&((t=L.DomUtil.create("img")).style.display="none",this.options.crossOrigin&&(t.crossOrigin=""),t.src=this._url,this._rawImage=t),L.DomUtil.addClass(t,"leaflet-image-layer");var i=this._image=L.DomUtil.create("div","leaflet-image-layer "+(this._zoomAnimated?"leaflet-zoom-animated":""));i.appendChild(t),i.onselectstart=L.Util.falseFn,i.onmousemove=L.Util.falseFn,t.onload=function(){this._reset(),t.style.display="block",this.fire("load")}.bind(this),t.alt=this.options.alt},_reset:function(){var t=this._image,i=this._map.latLngToLayerPoint(this._topLeft),a=this._map.latLngToLayerPoint(this._topRight),e=this._map.latLngToLayerPoint(this._bottomLeft),s=a.subtract(i).add(e),o=L.bounds([i,a,e,s]),n=o.getSize(),r=i.subtract(o.min),h=a.subtract(i),l=e.subtract(i),m=Math.atan2(h.y,h.x),g=Math.atan2(l.x,l.y);this._bounds=L.latLngBounds(this._map.layerPointToLatLng(o.min),this._map.layerPointToLatLng(o.max)),L.DomUtil.setPosition(t,o.min),t.style.width=n.x+"px",t.style.height=n.y+"px";var _=this._rawImage.width,d=this._rawImage.height;if(_&&d){var p=i.distanceTo(a)/_*Math.cos(m),c=i.distanceTo(e)/d*Math.cos(g);this._rawImage.style.transformOrigin="0 0",this._rawImage.style.transform="translate("+r.x+"px, "+r.y+"px)skew("+g+"rad, "+m+"rad) scale("+p+", "+c+") "}},reposition:function(t,i,a){this._topLeft=L.latLng(t),this._topRight=L.latLng(i),this._bottomLeft=L.latLng(a),this._reset()},setUrl:function(t){return this._url=t,this._rawImage&&(this._rawImage.src=t),this}}),L.imageOverlay.rotated=function(t,i,a,e,s){return new L.ImageOverlay.Rotated(t,i,a,e,s)};