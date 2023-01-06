package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import server.Firebase;
import server.deserializationObjects.RecommendationObj;
import server.deserializationObjects.RecommendationObj.ID;
import server.handlers.GetRecommendationHandler;

public class UnitTestRecommendation {
  Firebase f = new Firebase();
  @Test
  public void testUnitRecommendationDeserializeOne() throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/recommendationResponse.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetRecommendationHandler handle = new GetRecommendationHandler(f);
    RecommendationObj recObj = handle.getRecObj(jsonBody);

    List<ID> tracks = recObj.tracks;
    String id1 = tracks.get(0).id;

    assertEquals("5CG9Ps5ynNjpKJHmwc95pa", id1);
  }

  @Test
  public void testUnitRecommendationDeserializeMultiple() throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/recommendationResponse2.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetRecommendationHandler handle = new GetRecommendationHandler(f);
    RecommendationObj recObj = handle.getRecObj(jsonBody);

    List<ID> tracks = recObj.tracks;
    String id1 = tracks.get(0).id;
    String id2 = tracks.get(1).id;
    String id3 = tracks.get(2).id;


    assertEquals("2I0Q1wxFRdnDYtG6Q8W47S", id1);
    assertEquals("7s4dkWBVbUyMYKlDw9DiTC", id2);
    assertEquals("2sPK3NAhY4WP5Jdvrr90SR", id3);
  }
}
