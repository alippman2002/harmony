package server.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.Firebase;
import server.ServerResponse;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The GetCollectionHandler class. This class is used when the frontend loads
 * all previously-submitted songs, and displays them graphically.
 */
public class GetCollectionHandler implements Route {

  private Firebase f;

  /**
   * Constructor for GetCollectionHandler. Initializes the Firebase
   * instance variable.
   * @param f - Firebase instance
   */
  public GetCollectionHandler(Firebase f) {
    this.f = f;
  }

  /**
   * Retrieves all the data from the songs collection in the Firestore database
   * into a Map of String to Object where string is the id of the document, and serializes it
   * so the frontend can display song data geographically.
   * @param request - the request object for the getCollection endpoint with HTTP request information.
   * @param response - the response object that allows response modification.
   * @return - The serialized map of string to object
   * @throws Exception - if an error is encountered during retrieval
   * Example query: localhost:3232/getCollection?name=[]
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> resp = new HashMap<>();
    try {
      QueryParamsMap params = request.queryMap();
      if (!params.hasKey("name")) {
        resp.put("result", "error_bad_request");
        return new ServerResponse().serialize(resp);
      } else if (params.get("name").value().equals("")) {
        resp.put("result", "error_no_token");
        return new ServerResponse().serialize(resp);
      }
      String name = params.get("name").value();

      resp.put("result", "success");
      resp.put("data", this.f.getCollection(name));
      return new ServerResponse().serialize(resp);
    } catch (Exception e) {
      e.printStackTrace();
      resp.put("result", e.toString());
      return new ServerResponse().serialize(resp);
    }
  }
}
