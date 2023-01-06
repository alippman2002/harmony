package server.deserializationObjects;

import java.util.List;

/**
 * Class used for deserialization of the JSON response from the get profile spotify API endpoint.
 */
public class UserObj {

  public String display_name;
  public List<Image> images;
  public String id;

  public static class Image {
    public String url;
  }
}
