import {useState, useEffect} from 'react';
import './styles/App.css'; 
import Sidebar from './Sidebar'
import LogIn from './components/LogIn'
import UserEntry from './components/UserEntry';
import GeoPlaylist from './components/GeoPlaylist';
import GeoPlaylistButton from './components/GeoPlaylistButton';
import MapBox from './MapBox.jsx'
import './styles/MapBox.css'
import {LngLatBounds} from 'mapbox-gl'

/**
 * App function that contains all the React Components and adding needed component inputs/properties,
 * represents the web interface top-level organization. Handles needed Spotify Developer Wep API information.
 * @returns 
 */
function App() {
  //Spotify Wep API information
  const CLIENT_ID = 'ce58270f079346658ebe132ae27ae27b'
  const CLIENT_SECRET = '8ce08f38b60f474896c4ce17af94d709'
  const REDIRECT_URI = 'http://localhost:3000'
  const AUTH_ENDPOINT = "https://accounts.spotify.com/authorize"
  const RESPONSE_TYPE = "token"
  const SCOPES = "user-read-recently-played user-library-modify user-read-email user-read-private"

  const [access_token, setAccessToken] = useState("no_access");
  const [theme, setTheme] = useState(false);
  const [entryIsShown, setEntryIsShown] = useState(false);
  const [generatePlaylist, setGeneratePlaylist] = useState(false);
  const [bounds, setBounds] = useState(new LngLatBounds)

  const [filterCategories, setFilterCategories] = useState(new Array)
  const [filter, setFilter] = useState(new Map)

  /**
   * Function handling POST requeset for retrieving and setting Spotify Developer access token and client credentials
   */
  useEffect(() => {
    var authParameters = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'grant_type=client_credentials&client_id=' + CLIENT_ID + '&client_secret=' + CLIENT_SECRET
    }
    if (window.location.hash !== "") {
      setAccessToken(window.location.hash.substring(14,238))
    }
  }, [])

  const href: string = `${AUTH_ENDPOINT}?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=${RESPONSE_TYPE}&scope=${SCOPES}`
  
  //log-in vs. logged-out interface
  if (access_token !== "no_access") {
    return (
      <div className="App">
        <div className="web-container">
          <MapBox 
            token={access_token}
            theme={theme} 
            setTheme={setTheme}   
            setBounds={setBounds} 
            setFilterCategories={setFilterCategories} 
            filter={filter}/>
          <GeoPlaylistButton setGeneratePlaylist={setGeneratePlaylist}/>
          <Sidebar 
            theme={theme} 
            setTheme={setTheme} 
            setEntryIsShown={setEntryIsShown} 
            token={access_token} 
            setToken={setAccessToken}
            filterCategories={filterCategories}
            setFilter={setFilter}/> 
          {generatePlaylist && <GeoPlaylist setGeneratePlaylist={setGeneratePlaylist} token={access_token} bounds={bounds}></GeoPlaylist>}
          {entryIsShown && <UserEntry theme={theme} setTheme={setTheme} setEntryIsShown={setEntryIsShown} token={access_token}></UserEntry>}
        </div>
      </div>
    );
  }
  else {
    return (
      <div className="App">
        <div className="web-container">
         <LogIn href={href}></LogIn>
        </div>
      </div>
    )
    }
  }

export default App;