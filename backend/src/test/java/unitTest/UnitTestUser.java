package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import server.deserializationObjects.UserObj;
import server.handlers.GetUserProfileHandler;

public class UnitTestUser {

  @Test
  public void testUnitUserDeserialize() throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    Reader reader = new FileReader("data/userResponse.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    String jsonBody = jsonObject.toJSONString();
    jsonBody = jsonBody.replaceAll("\\\\", "");

    GetUserProfileHandler handle = new GetUserProfileHandler();
    UserObj userObj = handle.getUserObj(jsonBody);

    String username = userObj.display_name;
    String img_url = userObj.images.get(0).url;

    assertEquals("grcecant", username);
    assertEquals("https://i.scdn.co/image/ab6775700000ee85246eadbfbe8a8e2fc24d13de", img_url);
  }
}
