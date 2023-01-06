package server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

/**
 * The APIUtility class handles requests to any external API and returns the result of that
 * request.
 */
public class APIUtility {

  // the url for the given api
  private String apiUrl;

  /**
   * The first APIUtility constructor takes in the URL to the API, a list of endpoints, and a list
   * of parameters.
   *
   * @param apiUrl    the URL for a given API
   * @param endpoints the endpoints for a given API
   * @param params    the parameters for the endpoints of an API
   */
  public APIUtility(String apiUrl, List<String> endpoints, List<String> params) {
    this.apiUrl = apiUrl;
    for (int i = 0; i < endpoints.size(); i++) {
      this.apiUrl += endpoints.get(i) + params.get(i);
    }
  }

  /**
   * The second constructor solely takes in an URL to an external API in the event that a user does
   * not need to specific endpoints and parameters
   *
   * @param apiUrl the URL to an API
   */
  public APIUtility(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  /**
   * This method returns the result of a GET request to any external API, provided correct endpoints
   * and parameters were provided.
   * @param token - the access token provided to the user through the authentication process
   * @return the result of an API response as a Json String
   * @throws URISyntaxException   If the URL was incorrectly formatted
   * @throws IOException          if the response from the URL could not be read
   * @throws InterruptedException if the connection to the API was interrupted
   */
  public String getAPIRequest(String token) throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(this.apiUrl))
        .GET()
        .header("Authorization", "Bearer " + token)
        .build();
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
    System.out.println(response.body());
    return response.body();
  }

  /**
   * This method returns the result of a PUT request to any external API, provided correct endpoints
   * and parameters were provided.
   * @param token - the access token provided to the user through the authentication process
   * @param id - content for the body of the PUT request
   * @return - JSON response body
   * @throws URISyntaxException   If the URL was incorrectly formatted
   * @throws IOException          if the response from the URL could not be read
   * @throws InterruptedException if the connection to the API was interrupted
   */
  public String putAPIRequest(String token, String id) throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(this.apiUrl))
        .header("Authorization", "Bearer " + token)
        .PUT(HttpRequest.BodyPublishers.ofString(id))
        .build();
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
    System.out.println(response.body());
    return response.body();
  }

  /**
   * This method returns the result of a DELETE request to any external API, provided correct endpoints
   * and parameters were provided.
   * @param token - the access token provided to the user through the authentication process
   * @return - JSON response body
   * @throws URISyntaxException   If the URL was incorrectly formatted
   * @throws IOException          if the response from the URL could not be read
   * @throws InterruptedException if the connection to the API was interrupted
   */
  public String deleteAPIRequest(String token) throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(this.apiUrl))
        .header("Authorization", "Bearer " + token)
        .DELETE()
        .build();
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
    System.out.println(response.body());
    return response.body();
  }
}
