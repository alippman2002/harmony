/**
 * The MapBoxPopup is similar to the MapBox class, however it is altered to not be as interactive as the main map. This class is in charge of
 * selecting coordinates for a user to add a new entry. If it is double clicked, a marker will appear.
 */
import { useRef, useEffect, useState } from 'react';
import mapboxgl, {Marker} from 'mapbox-gl'; 
import * as MapboxGeocoder from '@mapbox/mapbox-gl-geocoder'
import {myKey} from './private/key'
import './styles/MapBoxPopup.css'
import 'mapbox-gl/dist/mapbox-gl.css'

mapboxgl.accessToken = myKey;

/**
 * The default method for the MapBoxPopup class creates the map and handles reactive aspects such as movement and marker selection.
 * It takes in theme, style, and entry-related parameters so it can communicate with the user entry interface.
 * @param {theme, setTheme, style, setEntryLat, setEntryLon} props 
 * @returns a popup map for adding entries
 */
export default function GenerateMap(props) {
  const mapContainer = useRef(null);
  const myMap = useRef(null);

  //default to Brown University
  const [lng, setLng] = useState(-71.4025);
  const [lat, setLat] = useState(41.8268);
  const [zoom, setZoom] = useState(15); 

  // Initialize the map and the geocoder
  useEffect(() => {
    if (myMap.current) return;
   // initialize map only once
    myMap.current = new mapboxgl.Map({
      container: mapContainer.current,
      center: [lng, lat],
      zoom: zoom,
      projection: "mercator",
      controls: [],
      doubleClickZoom: false
    });
    var geocoder = new MapboxGeocoder({
      accessToken: myKey,
      mapboxgl: mapboxgl,
    });
    myMap.current.addControl(geocoder);
    geocoder.addTo('#geocoder-container-popup')
  });


  // change the style of the map if the theme value chages
  useEffect(() => {
    myMap.current.setStyle((props.theme ? 'mapbox://styles/mapbox/light-v11' : 'mapbox://styles/mapbox/dark-v11'))
  },[props.theme])

  // handle interaction with the map
  useEffect(() => {
    if (!myMap.current) return; // wait for map to initialize
    myMap.current.on('move', () => {
      setLng(myMap.current.getCenter().lng.toFixed(4));
      setLat(myMap.current.getCenter().lat.toFixed(4));
      setZoom(myMap.current.getZoom().toFixed(2));
    });

    //add a new marker when the map is double clicked
    let marker = new Marker()
    myMap.current.on('dblclick', (e) => {
      marker.setLngLat(e.lngLat).addTo(myMap.current);
      console.log(e.lngLat)
      props.setEntryLat(e.lngLat.lat)
      props.setEntryLon(e.lngLat.lng)
    })
  })

  const ariaLabel = "New Entry MapBox"
  const ariaDescription = "Log your new entry in this map by double-clicking on a location"

  return (
    <div className="geomap-popup-container" aria-label={ariaLabel} aria-description={ariaDescription}>
      <div ref={mapContainer} className="map-popup-container"><div id="geocoder-container-popup"></div></div>
    </div>
  );
}
