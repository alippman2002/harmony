import '../styles/EntryButton.css'
import '../styles/UserEntry.css'

/**
 * This is the Entry Button React Component class contained within the sidebar and opens a new Entry when clicked.
 * This is accessible and readable by a screenreader
 * @returns 
 */
function EntryButton() {
    //accessible label and description
    const ariaLabel = "Add New Entry Button"
    const ariaDescription = "Press this button to submit a new entry of plotting your most recently listened to song to the Harmony map"

    /**
     * Toggles New Entry interface to be shown when "open new"
     */
    const openNew = () => {
        var popup = document.getElementById("entryPopup");
        if(popup != null){
            popup.classList.toggle("show");
        }
    }
    
    return(
        <button aria-label={ariaLabel} aria-description={ariaDescription} className="entry-button" onClick={openNew}>+ ADD NEW ENTRY</button>
    )
}

export default EntryButton;