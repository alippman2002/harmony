package server.handlers;

import java.util.HashMap;
import java.util.Map;
import server.Firebase;
import server.ServerResponse;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Adds a song at a specified latitude and longitude.
 */
public class AddSongAtLocHandler extends AddSong implements Route {

  private Firebase f;

  /**
   * Initializes the Firebase instance variable.
   * @param f - Firebase instance
   */
  public AddSongAtLocHandler(Firebase f) {
    this.f = f;
  }

  /**
   * Invoked when the addSongAtLoc endpoint is called. Requires a song id,
   * latitude, longitude, and an access token.
   * @param request - the request object for the addSongAtLoc endpoint with HTTP request information.
   * @param response - the response object that allows response modification.
   * @return - serialized map of string to object
   * Example query: localhost:3232/addSongAtLoc?id=[songID]&lat=[]&lon=[]&token=[]
   */
//  @Override
//  public Object handle(Request request, Response response) {
//    Map<String, Object> resp = new HashMap<>();
//    QueryParamsMap params = request.queryMap();
//    if (!params.hasKey("token")) {
//      resp.put("result", "error_token_param");
//    } else if(!params.hasKey("lat") || !params.hasKey("lon")) {
//      resp.put("result", "error_lat_lon_params");
//    } else if (!params.hasKey("id")){
//      resp.put("result", "error_no_id");
//    } else if (params.get("token").value().equals("") || params.get("lat").value().equals("")
//        || params.get("lon").value().equals("") || params.get("id").value().equals("")) {
//      resp.put("result", "error_invalid_params");
//    } else {
//      try {
//        String token = params.get("token").value();
//        resp.put("result", "success");
//
//        double lat = Double.parseDouble(params.get("lat").value());
//        double lon = Double.parseDouble(params.get("lon").value());
//
//        String id = params.get("id").value();
//
//        Map<String, Object> dataMap = new HashMap<>();;
//
//        //store the token
//        dataMap.put("token", token);
//
//        //store id of track
//        dataMap.put("id", id);
//
//        //store track metadata
//        Map<String, Object> trackMetadata = super.getTrackMetadata(id, token);
//        dataMap.put("track-data", trackMetadata);
//
//        this.f.addSongInfo(id, trackMetadata);
//
//        //store user location
//        Map<String, Object> loc = super.getSongLocGJSON(id, lat, lon);
//        dataMap.put("userGeoJSON", loc);
//
//        //check if token already exists in the collection
//        resp.put("data", dataMap);
//
//        this.f.addSong(token, resp);
//      } catch (Exception e) {
//        e.printStackTrace();
//        resp.put("result", "error_data_source");
//      }
//    }
//    return new ServerResponse().serialize(resp);
//  }
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> resp = new HashMap<>();
    try {
      QueryParamsMap params = request.queryMap();
      if (!params.hasKey("token") || !params.hasKey("id") || !params.hasKey("lat")
          || !params.hasKey("lon")) {
        resp.put("result", "error_bad_request");
        return new ServerResponse().serialize(resp);
      } else if (params.get("token").value().equals("") || params.get("lat").value().equals("")
          || params.get("lon").value().equals("") || params.get("id").value().equals("")) {
        resp.put("result", "error_invalid_params");
        return new ServerResponse().serialize(resp);
      }
      String token = params.get("token").value();

      double lat = Double.parseDouble(params.get("lat").value());
      double lon = Double.parseDouble(params.get("lon").value());

      String id = params.get("id").value();

      Map<String, Object> dataMap = new HashMap<>();

      //store the token
      dataMap.put("token", token);

      //store id of track
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

      resp.put("result", "success");
      return new ServerResponse().serialize(resp);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      resp.put("result", "error_bad_token");
      return new ServerResponse().serialize(resp);
    }
  }
}
