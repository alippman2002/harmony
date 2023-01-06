/**
 * The MarkerHandler class is in charge of rendering all of the song entry markers on the map accordingly.
 * @param {*} map the map to render the entries onto
 * @param {*} setModalActivation the function to activate the modal if an entry is selected
 * @param {*} setSongSelected the function communicating with the modal about entry data
 * @param {*} setModalLoc the function that changes the location of the modal on the window according to marker interaction
 * @param {*} setFilterCategories a function that is passed through to the sidebar to communicate filter categories from parsed marker data
 * @param {*} filter a filter map to filter markers
 */
export default async function MarkerHandler(map, setModalActivation, setSongSelected, setModalLoc, setFilterCategories, filter) {
    
    fetch('http://localhost:3232/getCollection?name=songs')
        .then(r => r.json())
        .then(json => {
            if (json.result == "success") {
            console.log("Fetching getCollection"); 
            console.log(json)
            jsonToMarkers(json.data, map, setModalActivation, setSongSelected, setModalLoc, setFilterCategories, filter)}
})
}

// These global variables contain parsed entry data that will be used to determine filtering categories in the sidebar
let years = new Set
let genres = new Set

/**
 * This method takes in the parsed data from the API call to the backend and contains the logic behind determing when/where to render song markers.
 * @param {*} map the map to render the entries onto
 * @param {*} setModalActivation the function to activate the modal if an entry is selected
 * @param {*} setSongSelected the function communicating with the modal about entry data
 * @param {*} setModalLoc the function that changes the location of the modal on the window according to marker interaction
 * @param {*} setFilterCategories a function that is passed through to the sidebar to communicate filter categories from parsed marker data
 * @param {*} filter a filter map to filter markers
 */
function jsonToMarkers(json, map, setModalActivation, setSongSelected, setModalLoc, setFilterCategories, filter) {
    
    const entries = Object.keys(json);

        for (const entry of entries) {
            const entryData = json[entry].data;
            const track_data = entryData["track-data"]
            const geojson = entryData["userGeoJSON"]
            const img_url = track_data["img_url"]

            // add to the filter data global variables
            years.add(track_data["release_date"]);
            let trackGenres = Array.from(track_data["genres"])
            trackGenres.forEach((x) => {
            genres.add(x)
            })

            //handle filtering based on the passed in filter map
            let filterKeys = Object.keys(filter)
            if (track_data[filterKeys[0]] == filter[filterKeys[0]] || trackGenres.includes(filter[filterKeys[0]])) {

                map.setLayoutProperty(entry, 'visibility', 'visible'); // in case it was filtered out prior
                if(!map.hasImage(entry)) {
                map.loadImage(
                    img_url,
                    (error, image) => {
                        if (error) throw error;
                        map.addImage(entry, image);
                        map.addSource(entry, {
                            'type': 'geojson',
                            'data': geojson
                        })
                        map.addLayer({
                            'id':entry,
                            'type':'symbol',
                            'source':entry,
                            'layout': {
                                'icon-image':entry,
                                'icon-anchor':"top",
                                'icon-size':0.1,
                                'icon-allow-overlap':true,                        
                            },
                        });
                    })

                    map.on('click', entry, (e) => {
                        var x = e.point.x
                        var y = e.point.y

                        setModalActivation(true)
                        setSongSelected(track_data)
                        setModalLoc([x,y])
                    });

                    //changes the cursor to a pointer when it enters a marker layer
                    map.on('mouseenter',entry, () => {
                        map.getCanvas().style.cursor = 'pointer';
                    });

                    //changes the cursor back to its original state after leaving a marker layer
                    map.on('mouseleave', entry, () => {
                        map.getCanvas().style.cursor ='';
                    });
        }
    }
    else map.setLayoutProperty(entry, 'visibility', 'none');
    }
    setFilterCategories([years, genres])
}

