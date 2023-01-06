package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import server.Firebase;
import server.deserializationObjects.RecommendationObj;
import server.deserializationObjects.RecommendationObj.ID;
import server.handlers.GetRecommendationHandler;
import server.sort.Quicksort;

public class UnitTestQuickSort {

  private void printPopularity(List<ID> ids, String listName) {
    String listIds = "";
    for (ID id: ids) {
      listIds += id.popularity + ", ";
    }
    System.out.println("Popularities in " + listName+ " list: " + listIds);
  }

  @Test
  public void testSortEmpty() {
    List<ID> arr = new ArrayList<>();
    Quicksort quickSort = new Quicksort(arr);
    quickSort.quickSort(0, 0);
    assertEquals(new ArrayList<>(), arr);
  }

  @Test
  public void testSortOutOfOrder() {
    List<ID> arr = new ArrayList<>();
    ID x = new ID();
    x.popularity = 2;
    ID y = new ID();
    y.popularity = 35;
    ID z = new ID();
    z.popularity = 10;
    ID b = new ID();
    b.popularity = 3;
    ID a = new ID();
    a.popularity = 1;
    arr.add(x);
    arr.add(y);
    arr.add(z);
    arr.add(a);
    arr.add(b);

    this.printPopularity(arr, "original");

    List<ID> expected = new ArrayList<>();
    expected.add(a);
    expected.add(x);
    expected.add(b);
    expected.add(z);
    expected.add(y);

    Quicksort quickSort = new Quicksort(arr);
    quickSort.quickSort(0, arr.size() - 1);

    this.printPopularity(arr, "sorted");
    assertEquals(expected, arr);
  }

  @Test
  public void testSortInOrder() {
    List<ID> arr = new ArrayList<>();
    ID x = new ID();
    x.popularity = 0;
    ID y = new ID();
    y.popularity = 1;
    ID z = new ID();
    z.popularity = 2;
    ID b = new ID();
    b.popularity = 3;
    arr.add(x);
    arr.add(y);
    arr.add(z);
    arr.add(b);

    this.printPopularity(arr, "original");

    List<ID> expected = new ArrayList<>();
    expected.add(x);
    expected.add(y);
    expected.add(z);
    expected.add(b);

    Quicksort quickSort = new Quicksort(arr);
    quickSort.quickSort(0, arr.size() - 1);

    this.printPopularity(arr, "sorted");
    assertEquals(expected, arr);
  }

  @Test
  public void testSortFromRecResponse() throws IOException, ParseException {
    Firebase f = new Firebase();

    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/recommendationResponse.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetRecommendationHandler handle = new GetRecommendationHandler(f);
    RecommendationObj recObj = handle.getRecObj(jsonBody);

    Quicksort sort = new Quicksort(recObj.tracks);
    List<ID> sortedIDs = sort.quickSort(0, recObj.tracks.size() - 1).subList(0,1);

    this.printPopularity(sortedIDs, "original");

    assertEquals("Somewhere On A Beach", sortedIDs.get(0).name);
  }

  @Test
  public void testSortFromRecResponseMultiple() throws IOException, ParseException {
    Firebase f = new Firebase();

    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/recommendationResponse2.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetRecommendationHandler handle = new GetRecommendationHandler(f);
    RecommendationObj recObj = handle.getRecObj(jsonBody);

    Quicksort sort = new Quicksort(recObj.tracks);
    List<ID> sortedIDs = sort.quickSort(0, recObj.tracks.size() - 1).subList(0,1);

    this.printPopularity(sortedIDs, "original");

    assertEquals("Iu2019m All In", sortedIDs.get(0).name);
  }
}
