package server;

import static spark.Spark.after;

import java.util.concurrent.ExecutionException;
import server.handlers.AddSongHandler;
import server.handlers.AddToLikedSongsHandler;
import server.handlers.GetCollectionHandler;
import server.handlers.GetRecentSongHandler;
import server.handlers.GetRecommendationHandler;
import server.handlers.GetTrackHandler;
import server.handlers.GetUserProfileHandler;
import server.handlers.UserLocationHandler;
import server.handlers.AddSongAtLocHandler;
import spark.Spark;

/**
 * Top-level class. Contains the main() method which starts Spark and runs the various handlers.
 */
public class Server {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    Spark.port(3232);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Initialize the Firestore Database
    Firebase f = new Firebase();

    Spark.get("getRecentSong", new GetRecentSongHandler());
    Spark.get("getTrack", new GetTrackHandler(f));
    Spark.get("userLoc", new UserLocationHandler(f));
    Spark.get("addLike", new AddToLikedSongsHandler());
    Spark.get("addSongAtLoc", new AddSongAtLocHandler(f));
    Spark.get("getRecs", new GetRecommendationHandler(f));
    Spark.get("getUser", new GetUserProfileHandler());
    Spark.get("add", new AddSongHandler(f));
    Spark.get("getCollection", new GetCollectionHandler(f));

    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
