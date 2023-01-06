import { useEffect } from 'react';
import '../styles/GeoPlaylist.css'
import {useState} from 'react'
import {LngLatBounds} from 'mapbox-gl';

/**
 * Interface containing the input properties for GeoPlaylist component
 */
interface GeoPlaylistProps {
    setGeneratePlaylist: Function
    token: string
    bounds: LngLatBounds
}

/**
 * Component for the GeoPlaylist interface that generates a new playlist of 10 songs based on the properties of the songs on the MapBox 
 * (within the browser bounding box) at the time. Users can preview and add the recommended songs after generation. 
 * This is accessible and readable by a screenreader
 * @param param0 
 * @returns 
 */
function GeoPlaylist({setGeneratePlaylist, token, bounds}: GeoPlaylistProps){
    //accessible aria label and description
    const ariaLabelPlaylist: string = "Your GeoPlaylist"
    const ariaDescriptionPlaylist: string = "Your newly generated GeoPlaylist based on the songs displayed on your MapBox. Click on Preview to listen to each corresponding song. Click on Add to Liked Songs to add the corresponding song to your Spotify Liked Songs. Click on the top right X button to close this playlist."

    const [songIds, setSongIds] = useState("")
    const [imgRecs, setImgRecs] = useState(["https://img.icons8.com/ios-glyphs/512/question-mark.png"])
    const [previewRecs, setPreviewRecs] = useState([''])
    const [idRecs, setIdRecs] = useState([''])
    const [recs, setRecs] = useState(['Loading...'])

    const recImgs: string[] = [];
    const recPreviews: string[] = [];
    const recItems: string[] = [];
    const recIds: string[] = [];

    /**
     * function to close the playlist
     */
    const closeNewPlaylist = () => {
        setGeneratePlaylist(false);
    }

    // will probably need to use map.QueryRenderedFeatures function:
    // https://docs.mapbox.com/mapbox-gl-js/api/map/#map#queryrenderedfeatures

    /**
     * Retrieiving Song IDs and data from the songs currently displayed within the bounding box of the mapbox
     */
    useEffect(() => {
    fetch('http://localhost:3232/getCollection?name=songs')
        .then(r => r.json())
        .then(json => {
            console.log("Fetching getCollection");
            const ids = Object.keys(json.data);

            const inputIds: string[] = []
            let queryInput = ""

            for (const id of ids) {
                const coordinates = json.data[id].data["userGeoJSON"]["geometry"]["coordinates"]
                console.log(coordinates)
                const lon = coordinates[0]
                const lat = coordinates[1]

                //bug: stops if invalid lat or lon values are in dataset
                if (bounds.contains(coordinates)) {
                    inputIds.push(json.data[id].data["id"])
                    console.log(inputIds)
                }
            }
            inputIds.forEach((id) => {
                queryInput = queryInput + id + ","
            })
            queryInput = queryInput.slice(0,-1)
            //setSongIds(queryInput)
            setSongIds(queryInput)})

        },[])

        /**
         * Getting song recommendations and adding them to the list of songs displayed on the GeoPlaylist
         */
        useEffect(() => {
            if (songIds == "") return;
            let URL = `http://localhost:3232/getRecs?token=${token}&songIds=${songIds}`
            console.log("GPURL " + URL)
            fetch(URL)
                .then(r => r.json())
                .then(json => {
                    console.log("Fetching getRecs")
                    if (json.result == "success") {
                        console.log("fetch get rec success!")
                        const recsList = json.sorted
                        for (let i = 0; i < 10; i++) {
                            const title = recsList[i]["title"].toString()
                            const artist = recsList[i]["artist"].toString()
                            recIds.push(recsList[i]["songid"].toString())
                            console.log("rec " + i + " song id " + recsList[i]["songid"].toString())
                            recImgs.push(recsList[i]["img_url"].toString())
                            recPreviews.push(recsList[i]["preview_url"].toString())
                            recItems.push((i + 1) + ". " + title + " by " + artist)
                        }
                        setImgRecs(recImgs)
                        setPreviewRecs(recPreviews)
                        setRecs(recItems)
                        setIdRecs(recIds)
                    }
                    else {
                        console.log("fetch get rec fail" + token)
                    }
                })
            },[songIds])

            const addSong = (token: string, songId: string) => {
                let URL = `http://localhost:3232/addLike?token=${token}&id=${songId}&add=true`;
                fetch(URL)
            }
            
    return (
        <div className="playlist-popup" id="playlistPopup" aria-label={ariaLabelPlaylist} aria-description={ariaDescriptionPlaylist}>
            <div className="playlist-header">
                    <div className='playlist-icon'>
                        <img className="song-img" src={imgRecs[0]}></img>
                        <img className="song-img" src={imgRecs[1]}></img>
                        <img className="song-img" src={imgRecs[2]}></img>
                        <img className="song-img" src={imgRecs[3]}></img>
                    </div>
                <div className='playist-title-wrapper'>
                    <p className='playlist-title'>Your <br></br> Geoplaylist</p>
                </div>
                <button className='close-button' onClick={closeNewPlaylist}>X</button>
            </div>
            <div className="playlist-content">
                <div className="songs-list">
                    {recs.map(function(item, i){console.log(item); 
                        return (
                        <div>
                            <p style={{margin:0}} key={i}>{item}</p>
                            <button key={i} className="preview-button-playlist" onClick={() => window.open(previewRecs[i])}>PREVIEW</button>
                            <button key={i} className="add-button-playlist" style={{width:115}} onClick={() => {addSong(token, idRecs[i])}}>ADD TO LIKED</button>
                        </div>)}
                    )}
                </div>
            </div>
        </div>
    );
}

export default GeoPlaylist;