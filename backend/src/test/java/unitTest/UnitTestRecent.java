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
import server.deserializationObjects.SongID;
import server.deserializationObjects.SongID.Item;
import server.handlers.GetRecentSongHandler;

public class UnitTestRecent {

  @Test
  public void testUnitRecentDeserialize() throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/recentResponse.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetRecentSongHandler handle = new GetRecentSongHandler();
    SongID idObj = handle.getIDObj(jsonBody);

    List<Item> items = idObj.items;
    String id = items.get(0).track.id;
    String name = items.get(0).track.name;
    String img_url = items.get(0).track.album.images.get(0).url;
    String artist = items.get(0).track.artists.get(0).name;

    assertEquals("0JfsIu62NVXNQl2s7ATN37", id);
    assertEquals("Me Gustas", name);
    assertEquals("https://i.scdn.co/image/ab67616d0000b2730e036776e70ccbb71216c835", img_url);
    assertEquals("Young Tender", artist);
  }
}
