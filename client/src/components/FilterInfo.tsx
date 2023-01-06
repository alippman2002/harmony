import '../styles/FilterInfo.css'
import Dropdown from 'react-dropdown'
import {useState} from 'react'

/**
 * Interface for input properties of FilterInfo 
 */
interface filterInfoProps {
  categories: Array<Set<string>>
  setFilter: Function
}

/**
 * Component for Filter Info that handles the filtering of map song displays by year released and/or music genre.
 * This is accessible and readable by a screenreader.
 * @param props 
 * @returns 
 */
export default function FilterInfo(props: filterInfoProps) {
  //accessible aria label and description
  const ariaLabel: string = "Filter Information"
  const ariaDescription: string = "Filter map song displays here with the toggle menus that contain categories of year released and music genre. Click on reset filters to reset filtering."
  
  const [selectedCategory1, setSelectedCategory1] = useState('Year');
  const [selectedCategory2, setSelectedCategory2] = useState('Genre');
 
  const options1 = props.categories[0] ? Array.from(props.categories[0]).sort(function(a: any,b: any){return a-b}) : [];
  const options2 = props.categories[1] ? Array.from(props.categories[1]).sort() : [];

  /**
   * Function for reseting filter options
   */
  function reset() {
    setSelectedCategory1('Year');
    setSelectedCategory2('Genre');
    props.setFilter([]);
  }

  return (
    <div className="filter-info" aria-label={ariaLabel} aria-description={ariaDescription}>
      <button className="reset-button" onClick={() => reset()}>Reset Filters</button>
      <Dropdown options={options1} value={selectedCategory1} onChange={(e) => {
        setSelectedCategory1(e.value);
        props.setFilter({"release_date" : e.value});
      }}/>
      <Dropdown options={options2} value={selectedCategory2} onChange={(e) => {
        setSelectedCategory2(e.value);
        props.setFilter({"genres" : e.value});
      }}/>
    </div>
  )
}