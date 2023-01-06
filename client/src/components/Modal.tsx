import ts, { isPropertySignature, Map } from "typescript";
import React, { useEffect, useState } from "react";
import '../styles/Modal.css'

/**
 * Inteface for the input properties of the Modal Component
 */
interface modalProps {
    isActivated: boolean;
    setActivation: Function;
    songData: ts.ESMap<string, Object>
    location: Array<number>
    token: string
}

/**
 * The modal class is in charge of rendering the modal popup based on song markers. 
 * This is accessible and readable by a screenreader.
 * @param param
 * @returns 
 */
export default function Modal({isActivated, setActivation, songData, location, token}: modalProps) {
    //accessible aria label and description
    const ariaLabel: string = "Modal Pop-Up"
    const ariaDescription: string = "This is where the information of the selected song on the map is displayed. Preview the song here or add to your Spotify liked songs."
    
    if (!isActivated) {
        return null
    }

    const variations = new Map(Object.entries(songData))

    let genres = Array.from(variations.get("genres"))
    genres = genres.slice(0,2)

    let genreString = ""

    genres.forEach((x) => {
        genreString = genreString + x + ", "
    })
    genreString = genreString.slice(0,-1);
    console.log(genreString)
    genreString = genreString.concat(" ...");

    return(
        <div className="modal" aria-label={ariaLabel} aria-description={ariaDescription} style={{left:(location[0]) + 'px', top:(location[1]-140)+'px'}}>
            <button className='modal-close-button' onClick={() => setActivation(false)}>X</button>

            <div className="modal-content">
                <div className="modal-body">
                    <p><strong>Song:</strong> {variations.get("title")}</p>
                    <p><strong>Artist:</strong> {variations.get("artist")}</p>
                    <p><strong>Album:</strong> {variations.get("album")}</p>
                    <p><strong>Release Year:</strong> {variations.get("release_date")}</p>
                    <p><strong>Genre:</strong> {genreString}</p>
                </div>

                <div className="modal-footer">
                    <button className="preview-button" onClick={() => window.open(variations.get("preview_url"))}>PREVIEW</button>
                    <button className={"add-button"}>ADD TO LIKED</button>
                </div>
                <div className="modal-pointer"></div>
            </div>
        </div>
    )
}