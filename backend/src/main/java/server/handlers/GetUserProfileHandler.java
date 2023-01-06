package server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import server.APIUtility;
import server.ServerResponse;
import server.deserializationObjects.UserObj;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Gets information on the user's profile from their access token.
 */
public class GetUserProfileHandler implements Route {

  /**
   * Invoked when the getUser endpoint is called. The params must include just the user's
   * access token from the authorization process.
   * @param request - the request object for the addLike endpoint with HTTP request information.
   * @param response - the response object that allows response modification.
   * @return the serialized Map of String to Object containing the result.
   * @throws Exception - if an error is encountered in the retrieval process
   */
  @Override
  public Object handle(Request request, Response response) {
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

      String url = "https://api.spotify.com/v1/me";
      APIUtility recURL = new APIUtility(url);

      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<UserObj> recAdapter = moshi.adapter(UserObj.class);

      String JSONBody = recURL.getAPIRequest(token);
      UserObj userObj = recAdapter.fromJson(JSONBody);

      System.out.println(userObj.toString());

      resp.put("result", "success");
      resp.put("name", userObj.display_name);
      resp.put("img_url", userObj.images.get(0).url);
      return new ServerResponse().serialize(resp);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      resp.put("result", e.getMessage());
      return new ServerResponse().serialize(resp);
    }
  }

  /**
   * Returns a track object from the JSON body. Used for unit testing.
   * @param JSONBody - JSON body
   * @return - deserialized UserObj
   * @throws IOException
   */
  public UserObj getUserObj(String JSONBody) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<UserObj> userAdapter = moshi.adapter(UserObj.class);

    return userAdapter.fromJson(JSONBody);
  }
}
