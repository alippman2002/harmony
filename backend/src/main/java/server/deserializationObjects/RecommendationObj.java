package server.deserializationObjects;

import java.util.List;

/**
 * Class used for deserialization of the JSON response of the spotify get recommendations
 * endpoint.
 */
public class RecommendationObj {
  public List<ID> tracks;

  public static class ID {
    public String id;
    public Album album;
    public String name;
    public List<Artist> artists;
    public String duration_ms;
    public int popularity;
    public String preview_url;
  }

  public static class Album {
    public String release_date;
    public String name;
  }

  public static class Artist {
    public String name;
  }

}
