package server.sort;

import java.util.Collections;
import java.util.List;
import server.deserializationObjects.RecommendationObj.ID;

/**
 * Sorts a List of IDs based on the quicksort algorithm.
 * This algorithm has an average time complexity of Θ(n log(n)).
 * We learned about the quicksort algorithm here: https://www.geeksforgeeks.org/quick-sort/
 */
public class Quicksort {

  private List<ID> ids;

  /**
   * Initialize the ids instance variable when quicksort constructor is called.
   * @param ids - List of IDs
   */
  public Quicksort(List<ID> ids) {
    this.ids = ids;
  }
  /**
   * Finds the index of the partition.
   * @param arr - the List of IDs
   * @param low - the low index
   * @param high - the high index
   * @return - int representing partition index
   */
  private int partition(List<ID> arr, int low, int high) {
    //pivot is the last element in the list
    ID pivot = arr.get(high);

    //this represents the "greater" element we are comparing to
    int i = low - 1;
    for (int j = low; j <= high - 1; j++) {
      //if the current element's popularity is less than the pivot's popularity,
      //it is swapped to left of the pivot.
      if (arr.get(j).popularity < pivot.popularity) {
        i++;
        //swap the element at i with element at j
        Collections.swap(arr, i, j);
      }
    }
    //swap pivot with "greater" element
    Collections.swap(arr, i+1, high);
    return i+1;
  }

  /**
   * Sorts the list that was passed into the constructor (instance variable ids);
   * @param low - lowest index to sort from
   * @param high - highest index to sort from
   * @return - sorted List of IDs
   */
  public List<ID> quickSort(int low, int high) {
    if (low < high) {
      //retrieving the pivot element, where all elements smaller are on its left
      //and all larger elements are on its right
      int partitionIndex = this.partition(this.ids, low, high);
      //sort the elements to the left of the pivot
      this.quickSort(low, partitionIndex - 1);
      //sort the elements to the right of the pivot
      this.quickSort(partitionIndex + 1, high);
    }
    return this.ids;
  }
}
