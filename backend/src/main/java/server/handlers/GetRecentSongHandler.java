package server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.APIUtility;
import server.Firebase;
import server.ServerResponse;
import server.deserializationObjects.SongID;
import server.deserializationObjects.SongID.Item;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Retrieves the user's most recently listened-to song.
 */
public class GetRecentSongHandler implements Route {

  /**
   * Invoked when the getRecentSong endpoint is called. The request must include a token.
   * @param request - the request object for the getRecentSong endpoint with HTTP request information.
   * @param response - the response object that allows response modification.
   * @return the serialized Map of String to Object containing the result.
   * @throws Exception - if an error is encountered in the retrieval process
   * Example query: http://localhost:3232/getRecentSong?token=[]
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> resp = new HashMap<>();
    try {
      QueryParamsMap params = request.queryMap();
      if (!params.hasKey("token")) {
        resp.put("result", "error_bad_request");
        return new ServerResponse().serialize(resp);
      } else if (params.get("token").value().equals("")) {
        resp.put("result", "error_no_token");
        return new ServerResponse().serialize(resp);
      }
      String token = params.get("token").value();
      String url = "https://api.spotify.com/v1/me/player/recently-played?limit=1";

      APIUtility idURL = new APIUtility(url);

      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SongID> trackAdapter = moshi.adapter(SongID.class);

      String JSONBody = idURL.getAPIRequest(token);
      SongID idObj = trackAdapter.fromJson(JSONBody);

      List<Item> items = idObj.items;
      String id = items.get(0).track.id;
      String name = items.get(0).track.name;
      String img_url = items.get(0).track.album.images.get(0).url;
      String artist = items.get(0).track.artists.get(0).name;

      resp.put("result", "success");
      resp.put("id", id);
      resp.put("name", name);
      resp.put("img_url", img_url);
      resp.put("artist", artist);
      return new ServerResponse().serialize(resp);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      resp.put("result", "error_bad_token");
      return new ServerResponse().serialize(resp);
    }
  }

  /**
   * Method that allows for unit testing of deserialization using the SongID class.
   * @param JSONBody - Mock JSON data
   * @return - Deserialized songID
   * @throws IOException - if the file containing the JSON is not found
   */
  public SongID getIDObj(String JSONBody) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<SongID> trackAdapter = moshi.adapter(SongID.class);

    return trackAdapter.fromJson(JSONBody);
  }
}
