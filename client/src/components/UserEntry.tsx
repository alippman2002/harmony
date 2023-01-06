import { useState, useEffect } from 'react';
import '../styles/UserEntry.css'
import '../styles/MapBox.css'
import Map from '../MapBoxPopup'

/**
 * Interface containing input properties for the UserEntry component
 */
interface UserEntryProps {
    theme: boolean;
    setTheme: Function;
    setEntryIsShown: Function;
    token: string
}

/**
 * Component for the UserEntry interface where users can add a New Song Entry by plotting their most-recently played song to their location
 * by double clicking on the map and clicking done. 
 * This is accessible and can be read by a screenreader. 
 * @param param0 
 * @returns 
 */
function UserEntry({theme, setTheme, setEntryIsShown, token}: UserEntryProps){
    //accessible aria label and description
    const ariaLabel: string = "New User Entry"
    const ariaDescription: string = "Log your most-recently played song to your location by double clicking on the map and clicking Done when complete."

    const [entryLat, setEntryLat] = useState(0);
    const [entryLon, setEntryLon] = useState(0)
    const [recentTitle, setRecentTitle] = useState("<Song Title>")
    const [recentImage, setRecentImage] = useState("https://img.icons8.com/ios-glyphs/512/question-mark.png")
    const [recentArtist, setRecentArtist] = useState("<Artist>")
    const [recentId, setRecentId] = useState("")

    /**
     * Function for closing New Entry interface
     */
    const closeNewEntry = () => {
        setEntryIsShown(false);
    }

    /**
     * Function for retrieving data about user's most recently played song
     */
    useEffect(() => {
        let URL = `http://localhost:3232/getRecentSong?token=${token}`
        fetch(URL)
        .then(r => r.json())
        .then(json => {
            if (json.result == "success") {
                setRecentTitle(json.name)
                setRecentImage(json.img_url)
                setRecentArtist(json.artist)
                setRecentId(json.id)
            }
            else console.log("JSON NOT SUCCESS" + token)
        })
    }, [])
    
    /**
     * Function for logging a new entry and adding it to the overall Harmony user map
     */
    const logEntry = () => {
        let URL = `http://localhost:3232/addSongAtLoc?id=${recentId}&lat=${entryLat}&lon=${entryLon}&token=${token}`
        fetch(URL)
        window.location.reload()
    }

    return (
        <div className="entry-popup" id="entryPopup" aria-label={ariaLabel} aria-description={ariaDescription}>
            <div className="entry-header">
                <div className='title'>New Entry</div>
                <button className='close-button' onClick={closeNewEntry}>X</button>
            </div>
            <div className="entry-header-line"></div>
            <div className="entry-text">Double click on the map to plot your location for your most recently played song</div>
            <div className="entry-map">
                <Map theme={theme} setTheme={setTheme} style={{width:650, height:280, left:30, borderRadius:25}} setEntryLon= {setEntryLon} setEntryLat= {setEntryLat} />
            </div>
            <div className="entry-bottom">
                <div className="most-recent-song">
                    <img className="song-icon" src={recentImage} style={{float:'left',width:40, height:40, borderRadius:30}}></img>
                    <div className="song-info">
                        <p style={{fontSize:16, margin:0, fontWeight:500, textAlign:'left', textOverflow:'ellipsis'}}>{recentTitle}</p>
                        <p style={{fontSize:12, margin:0, fontWeight:400, textAlign:'left'}}>{recentArtist}</p>
                    </div>
                </div>
                <button className="done-button" onClick={() => {logEntry(); closeNewEntry()}}>Done</button>
            </div>
        </div>
    );
}

export default UserEntry;