package server.deserializationObjects;

import java.util.List;

/**
 * Class used for deserialization of the JSON response from the get track endpoint of
 * the Spotify API.
 */
public class TrackObj {
  public String name;
  public String preview_url;
  public Album album;
  public List<Artists> artists;

  public static class Album {
    public List<Image> images;
    public String name;
    public String release_date;
  }

  public static class Artists {
    public String id;
    public String name;
  }
  public static class Image{
    public int height;
    public String url;
    public int width;
  }
}
