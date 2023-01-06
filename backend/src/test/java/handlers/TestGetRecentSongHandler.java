package handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.eclipse.jetty.util.IO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.handlers.GetRecentSongHandler;
import spark.Spark;

public class TestGetRecentSongHandler {
  /** Handles setup before any tests are called. */
  @BeforeAll
  public static void setup_before_everything() {

    // Set the Spark port number. This can only be done once, and has to
    // happen before any route maps are added. Hence using @BeforeClass.
    // Setting port 0 will cause Spark to use an arbitrary available port.
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /**
   * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
   * need to replace the reference itself. We clear this state out after every test runs.
   */
  private Map<String, Object> responseMap = new HashMap<>();

  /** Handles setup before each individual test is called. */
  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run
    this.responseMap.clear();
  }

  /** Handles teardown after each individual test is run. */
  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/getRecentSong");
    Spark.stop();
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    // clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testNoTokenInParams() throws IOException {
    Spark.get("/getRecentSong", new GetRecentSongHandler());
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("getRecentSong");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.get("result"));
  }

  @Test
  public void testEmptyToken() throws IOException {
    Spark.get("/getRecentSong", new GetRecentSongHandler());
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("getRecentSong?token=");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_no_token", response.get("result"));
  }

  @Test
  public void testBadToken() throws IOException {
    Spark.get("/getRecentSong", new GetRecentSongHandler());
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("getRecentSong?token=113131");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_token", response.get("result"));
  }
}
