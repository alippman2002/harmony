package server.handlers;

import java.util.HashMap;
import java.util.Map;
import server.APIUtility;
import server.ServerResponse;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Adds or removes a song from the Spotify user's saved songs library.
 */
public class AddToLikedSongsHandler implements Route {

  /**
   * Invoked when the addLike endpoint is called. The "add" parameter indicates whether the song
   * is to be added or removed from the library based on a boolean value of true or false. The request
   * must also include the user's session token and the id of the song to add or remove.
   * @param request - the request object for the addLike endpoint with HTTP request information.
   * @param response - the response object that allows response modification.
   * @return the serialized Map of String to Object containing the result.
   * @throws Exception - if an error is encountered in the retrieval process
   * Example query: localhost:3232/addLike?token=[token]&id=[song id]&add=[true or false]
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> resp = new HashMap<>();
    try {
      QueryParamsMap params = request.queryMap();
      if (!params.hasKey("token") || !params.hasKey("id") || !params.hasKey("add")) {
        resp.put("result", "error_bad_request");
        return new ServerResponse().serialize(resp);
      } else if (params.get("id").value().equals("") || params.get("token").value().equals("") || params.get("add").value().equals("")) {
        resp.put("result", "error_no_token");
        return new ServerResponse().serialize(resp);
      }
      String token = params.get("token").value();
      String id = params.get("id").value();
      String url = "https://api.spotify.com/v1/me/tracks?ids=" + id;

      APIUtility idURL = new APIUtility(url);

      String JSONBody;
      boolean add = Boolean.parseBoolean(params.get("add").value());
      if (add) {
        JSONBody = idURL.putAPIRequest(token, id);
      } else {
        JSONBody = idURL.deleteAPIRequest(token);
      }

      if (JSONBody != null) {
        resp.put("result", "error_bad_token");
      } else {
        resp.put("result", "success");
      }

      return new ServerResponse().serialize(resp);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      resp.put("result", "error_bad_token");
      System.out.println(new ServerResponse().serialize(resp));
      return new ServerResponse().serialize(resp);
    }
  }
}
