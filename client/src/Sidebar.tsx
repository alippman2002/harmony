import './styles/Sidebar.css'
import {Tab, Tabs, TabList, TabPanel} from "react-tabs";
import {ToggleSwitch} from './components/ToggleSwitch';
import FilterInfo from './components/FilterInfo'
import HistoryInfo from './components/HistoryInfo'
import { useState, useEffect} from 'react';

/**
 * Interface containing the input properties for the Sidebar
 */
interface sidebarProps {
    theme: boolean;
    setTheme: Function;
    setEntryIsShown: Function;
    token: string;
    setToken: Function;
    filterCategories: Array<Set<string>>
    setFilter: Function;
  }

/**
 * The sidebar class is in charge of rendering the sidebar that interacts with the map.
 * This is accessible and readable by a screenreader.
 * @param props are from the sidebarProps interface and contain states that interact with the map
 * @returns the interactive rendering of the sidebar
 */
export default function GenerateSidebar(props: sidebarProps) {
    //accessible aria label and description
    const ariaLabel: string = "Sidebar"
    const ariaDescription: string = "This is the sidebar that contains two tabs: View and Profile. View allows you to add a new entry, filter information, and toggle map mode. Profile shows your profile and is where you can log out."

    const [username, setUsername] = useState("<Spotify Username>")
    const [pfp, setPfp] = useState("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")

    /**
     * function to open new entry
     */
    const openNewEntry = () => {
        props.setEntryIsShown(true);
    }

    /**
     * retrieving Spotify user information 
     */
    useEffect(() => {
    fetch(`http://localhost:3232/getUser?token=${props.token}`)
    .then(r => r.json())
    .then(json => {
        console.log("Fetching getUser")
        if (json.result == "success") {
            setUsername(json.name);
            setPfp(json.img_url)
        }
    });}, [])

    return (
        <div className="sidebar" aria-label={ariaLabel} aria-description={ariaDescription}>
            <Tabs className = "tabs">
                <TabList>
                    <Tab>VIEW</Tab>
                    <Tab>PROFILE</Tab>
                </TabList>
                <TabPanel>
            <div className="view-tab">
                <button className="entry-button" onClick={openNewEntry}>+ ADD NEW ENTRY</button>
                <div className = "filter-by">
                    Filter by:
                </div>
                <FilterInfo categories={props.filterCategories} setFilter={props.setFilter}/>
                <ToggleSwitch 
                selected ={props.theme}
                toggleSelected={() => {
                    props.setTheme(!props.theme);
                }}
                />
            </div>
            </TabPanel>
            <TabPanel>
                <div className = "profile-tab">
                    <div className="profile-tab-header">
                        <div><img src={pfp} className="prof-pic"></img></div>
                        <div className="username">{username}</div>
                    </div>
                    <div className = "history">
                        History:
                    </div>
                    <HistoryInfo />
                    <button className="logout-button" onClick={() => props.setToken("no_access")}>LOG OUT</button>
                </div>
                </TabPanel>
            </Tabs>
        </div>
    )
}