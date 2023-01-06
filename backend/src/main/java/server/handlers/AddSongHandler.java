package server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.APIUtility;
import server.Firebase;
import server.ServerResponse;
import server.deserializationObjects.GenreObj;
import server.deserializationObjects.SongID;
import server.deserializationObjects.SongID.Item;
import server.deserializationObjects.TrackObj;
import server.deserializationObjects.TrackObj.Image;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Gets the user's most recent song and adds it at their latitude and longitude.
 */
public class AddSongHandler extends AddSong implements Route {

  private Firebase f;

  /**
   * Initializes the Firebase instance variable.
   * @param f - Firebase instance
   */
  public AddSongHandler(Firebase f) {
    this.f = f;
  }

  /**
   * Invoked when the add endpoint is called. Requires a token and values of latitude
   * and longitude.
   * @param request - the request object for the add endpoint with HTTP request information.
   * @param response - the response object that allows response modification.
   * @return - serialized map of string to object
   * Example query: localhost:3232/add?token=[]&lat=[]&lon=[]
   */
  @Override
  public Object handle(Request request, Response response){
    Map<String, Object> resp = new HashMap<>();
    QueryParamsMap params = request.queryMap();
      if (!params.hasKey("token")) {
        resp.put("result", "error_token_param");
      } else if(!params.hasKey("lat") || !params.hasKey("lon")) {
        resp.put("result", "error_lat_lon_params");
      } else if (params.get("token").value().equals("") || params.get("lat").value().equals("")
          || params.get("lon").value().equals("")) {
        resp.put("result", "error_invalid_params");
      } else {
        try {
          String token = params.get("token").value();
          resp.put("result", "success");

          double lat = Double.parseDouble(params.get("lat").value());
          double lon = Double.parseDouble(params.get("lon").value());

          Map<String, Object> dataMap = new HashMap<>();;

          //store the token
          dataMap.put("token", token);

          //store id of track
          String id = this.getMostRecentSong(params.get("token").value());
          dataMap.put("id", id);

          //store track metadata
          Map<String, Object> trackMetadata = super.getTrackMetadata(id, token);
          dataMap.put("track-data", trackMetadata);

          this.f.addSongInfo(id, trackMetadata);

          //store user location
          Map<String, Object> loc = super.getSongLocGJSON(id, lat, lon);
          dataMap.put("userGeoJSON", loc);

          //check if token already exists in the collection
          resp.put("data", dataMap);

          this.f.addSong(token, resp);
        } catch (Exception e) {
          e.printStackTrace();
          resp.put("result", e.getMessage());
        }
      }
      return new ServerResponse().serialize(resp);
  }

  private String getMostRecentSong(String token) {
    try {
      String url = "https://api.spotify.com/v1/me/player/recently-played?limit=1";

      APIUtility idURL = new APIUtility(url);

      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SongID> trackAdapter = moshi.adapter(SongID.class);

      String JSONBody = idURL.getAPIRequest(token);
      SongID idObj = trackAdapter.fromJson(JSONBody);

      List<Item> items = idObj.items;
      String id = items.get(0).track.id;
      return id;

    } catch (Exception e ) {
      return "invalid token";
    }
  }
}

