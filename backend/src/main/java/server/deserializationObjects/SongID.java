package server.deserializationObjects;

import java.util.List;

/**
 * Class used for deserialization of song ids.
 */
public class SongID {
  public List<Item> items;

  public static class Item {
    public Track track;
  }

  public static class Track {
    public String id;
    public String name;
    public Album album;
    public List<Artist> artists;
  }

  public static class Artist {
    public String name;
  }

  public static class Album {
    public List<Image> images;
  }

  public static class Image {
    public String url;
  }
}

