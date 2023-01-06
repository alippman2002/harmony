package server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.APIUtility;
import server.deserializationObjects.GenreObj;
import server.deserializationObjects.TrackObj;
import server.deserializationObjects.TrackObj.Image;

/**
 * Abstract class which is extended by both AddSongAtLocHandler and AddSongHandler,
 * as they require the same helper methods.
 */
public abstract class AddSong {

  /**
   * Gets a track's metadata by calling the Spotify API's track endpoint.
   * @param id - song ID
   * @param token - access token
   * @return - A map of string to object containing the metadata
   * @throws URISyntaxException - if the URI is incorrectly formatted
   * @throws IOException - if an IO issue is encountered
   * @throws InterruptedException - if an error occurs during parsing
   */
  public Map<String, Object> getTrackMetadata(String id, String token)
      throws URISyntaxException, IOException, InterruptedException {
    Map<String, Object> resp = new HashMap<>();
    String urlTrack = "https://api.spotify.com/v1/tracks/" + id;

    APIUtility trackURL = new APIUtility(urlTrack);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<TrackObj> trackAdapter = moshi.adapter(TrackObj.class);

    String JSONBody = trackURL.getAPIRequest(token);
    TrackObj trackObj = trackAdapter.fromJson(JSONBody);

    String title = trackObj.name;
    String album = trackObj.album.name;
    String artistId = trackObj.artists.get(0).id;
    String artistName = trackObj.artists.get(0).name;
    String preview = trackObj.preview_url;
    String releaseYear = trackObj.album.release_date.substring(0, 4);
    List<Image> imgURLs = trackObj.album.images;
    String imgURL = imgURLs.get(0).url;


    resp.put("songid", id);
    resp.put("title", title);
    resp.put("album", album);
    resp.put("preview_url", preview);
    resp.put("artist", artistName);
    resp.put("img_url", imgURL);
    resp.put("release_date", releaseYear);

    String urlArtist = "https://api.spotify.com/v1/artists/" + artistId;
    APIUtility artistURL = new APIUtility(urlArtist);
    Moshi moshi2 = new Moshi.Builder().build();
    JsonAdapter<GenreObj> genreAdapter = moshi2.adapter(GenreObj.class);
    String JSONBodyGenre = artistURL.getAPIRequest(token);
    GenreObj genreObj = genreAdapter.fromJson(JSONBodyGenre);

    List<String> genres = genreObj.genres;
    resp.put("genres", genres);
    return resp;
  }

  /**
   * Formats the song's location data in GeoJSON format.
   * @param id - song id
   * @param lat - latitude
   * @param lon - longitude
   * @return - Map of string to object containing GeoJSON data
   */
  public Map<String, Object> getSongLocGJSON(String id, double lat, double lon) {
    Map<String, Object> geoMap = new HashMap<>();
    Map<String, Object> innerGeometryMap = new HashMap<>();
    Map<String, Object> propertiesMap = new HashMap<>();

    geoMap.put("type", "Feature");
    innerGeometryMap.put("type", "Point");
    List<Double> coords = new ArrayList<>();
    coords.add(lon);
    coords.add(lat);
    innerGeometryMap.put("coordinates", coords);
    geoMap.put("geometry", innerGeometryMap);
    propertiesMap.put("name", id);
    geoMap.put("properties", propertiesMap);

    return geoMap;
  }
}
