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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Firebase;
import server.handlers.AddSongAtLocHandler;
import server.handlers.AddSongHandler;
import spark.Spark;

public class TestAddSongAtLocHandler {
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
    Spark.unmap("/addSongAtLoc");
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
    Firebase f = new Firebase();
    Spark.get("/addSongAtLoc", new AddSongAtLocHandler(f));
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("addSongAtLoc");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.get("result"));
  }

  @Test
  public void testEmptyToken() throws IOException {
    Firebase f = new Firebase();
    Spark.get("/addSongAtLoc", new AddSongAtLocHandler(f));
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("addSongAtLoc?token=");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.get("result"));
  }

  @Test
  public void testBadToken() throws IOException {
    Firebase f = new Firebase();
    Spark.get("/addSongAtLoc", new AddSongAtLocHandler(f));
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("addSongAtLoc?token=113131");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.get("result"));
  }

  @Test
  public void testBadParams () throws IOException {
    Firebase f = new Firebase();
    Spark.get("/addSongAtLoc", new AddSongAtLocHandler(f));
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("addSongAtLoc?token=113131&lat=1&lon=1&id=");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Map response =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_invalid_params", response.get("result"));
  }
}
