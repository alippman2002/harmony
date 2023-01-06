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
import server.deserializationObjects.GenreObj;
import server.deserializationObjects.TrackObj;
import server.deserializationObjects.TrackObj.Image;
import server.handlers.GetTrackHandler;


public class UnitTestTrack {

  private Firebase f = new Firebase();
  @Test
  public void testUnitTrackDeserialize() throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/trackResponse.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetTrackHandler handle = new GetTrackHandler(f);
    TrackObj trackObj = handle.getTrackObj(jsonBody);

    String title = trackObj.name;
    String album = trackObj.album.name;
    String preview = trackObj.preview_url;
    List<Image> imgURLs = trackObj.album.images;
    String imgURL = imgURLs.get(0).url;
    String releaseYear = trackObj.album.release_date.substring(0, 4);

    assertEquals("Cut To The Feeling", title);
    assertEquals("Cut To The Feeling", album);
    assertEquals("https://p.scdn.co/mp3-preview/4e69d142cceaca1fa4bc8db7a319ab7a0b8ffd82?cid=774b29d4f13844c495f206cafdad9c86", preview);
    assertEquals("https://i.scdn.co/image/ab67616d0000b2737359994525d219f64872d3b1", imgURL);
    assertEquals("2017", releaseYear);
  }

  @Test
  public void testUnitGenreDeserialize() throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/artistResponse.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetTrackHandler handle = new GetTrackHandler(f);
    GenreObj trackObj = handle.getGenreObj(jsonBody);

    List<String> genres = trackObj.genres;

    List<String> expected = new ArrayList<>();
    expected.add("dance pop");
    expected.add("miami hip hop");
    expected.add("pop");
    expected.add("pop rap");

    assertEquals(expected, genres);
  }
}
