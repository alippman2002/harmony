import '../styles/GeoPlaylistButton.css'
import '../styles/GeoPlaylist.css'

/**
 * Interface containing the input properties for GeoPlaylistButton component
 */
interface GeoPlaylistButtonProps {
    setGeneratePlaylist: Function;
}

/**
 * Component for the GeoPlaylistButton - when clicked it generates song recommendations based on the 
 * user's perceived Geographic area on the Mapand opens the GeoPlaylist interface to display them.
 * This is accessible and readable by a screenreader
 * @param param0 
 * @returns 
 */
function GeoPlaylistButton({setGeneratePlaylist}: GeoPlaylistButtonProps) {
    //accessible aria label and description
    const ariaLabel: string = "Make A New GeoPlaylist Button"
    const ariaDescription: string = "Press here to generate a new GeoPlaylist of songs using data from the songs displayed within the bounds of the MapBox"

    /**
     * Method for opening New GeoPlaylist
     */
    const openNewPlaylist = () => {
        setGeneratePlaylist(true);
    }
    
    return(
        <button className="playlist-button" onClick={openNewPlaylist} aria-label={ariaLabel} aria-description={ariaDescription}>MAKE A GEO-PLAYLIST</button>
    )
}

export default GeoPlaylistButton;