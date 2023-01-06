import '../styles/LogIn.css'

/**
 * Interface containing the input properties for the LogIn component
 */
interface LogInProps {
    href: string
}

/**
 * Component for the LogIn screen where first-time users LogIn to Harmony through Logging In with Spotify
 * @param param0 
 * @returns 
 */
function LogIn({href}: LogInProps){
    //accessible aria label and description
    const ariaLabel: string = "Log In Page"
    const ariaDescription: string = "Welcome to Harmony! Press on Log In with Spotify to get started"

    /**
     * Method for opening Spotify Log-in/Authorization Page
     */
    const handleSubmit = () => {
        window.open(href, "_self")
    }

    return (
        <div className="welcome-popup" aria-label={ariaLabel} aria-description={ariaDescription}>
            <div className="welcome-text">Welcome to</div>
            <div className="harmony-text">Harmony</div>
            <button className="spotify-login-button" onClick={handleSubmit}>
                LOG IN WITH SPOTIFY
            </button>
        </div>
    );
}

export default LogIn;