package server;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class that represents a reference to our Google Firestore database.
 */
public class Firebase {

  private Firestore db;

  /**
   * The constructor calls the initialization method.
   */
  public Firebase() {
    this.initializeFirebase();
  }

  /**
   * Initializes the Firestore reference through the information stored in the service account key
   * file, which is not in GitHub because it needs to remain private.
   * We followed instructions from this video: https://www.youtube.com/watch?v=Mcsp59_2E7E&t=175s
   */
  public void initializeFirebase() {
    try {
      //get private key
      FileInputStream serviceAccount = new FileInputStream(
          "src/main/java/server/ServiceAccountKey.json");

      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      FirebaseApp.initializeApp(options);
      this.db = FirestoreClient.getFirestore();

      //figure out a better way to handle this exception later!
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds a location to a user document in the users collection.
   * @param username - user's id
   * @param userInfo - info to add about the user (location in this case)
   */
  public void addLocation(String username, Map<String, Object> userInfo) {
    this.db.collection("users").document(username).set(userInfo);
  }

  /**
   * Adds a song to the songs collection. Creates a new document with the given
   * authorization token, and adds fields containing info from the map.
   * @param id - authorization code
   * @param songInfo - info about the song
   */
  public void addSong(String id, Map<String, Object> songInfo) {
    this.db.collection("songs").add(songInfo);
  }

  /**
   * Adds a song's metadata to the songInfo collection by creating a new document
   * with the song's id as the document id.
   * @param id - song's id
   * @param metadata - info about the song
   */
  public void addSongInfo(String id, Map<String, Object> metadata) {
    this.db.collection("songInfo").document(id).set(metadata);
  }

  /**
   * Checks whether a document exists in a given collection.
   * @param collection - collection to check
   * @param docName - document being searched for
   * @return - boolean representing whether the doc exists in the collection
   */
  public boolean docExists(String collection, String docName)
      throws ExecutionException, InterruptedException {
    DocumentReference songsRef = this.db.collection(collection)
        .document(docName);

    if (songsRef.get().get().exists()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets data from an entire collection in the Firestore database.
   * @param collection - collection name
   * @return - Map of String to Object where string is id of document
   * @throws ExecutionException - if an error occurs during execution
   * @throws InterruptedException - if an error interrupts the searching process
   */
  public Map<String, Object> getCollection(String collection) {
  Map<String, Object> map = new HashMap<>();
    try {
      ApiFuture<QuerySnapshot> future = this.db.collection(collection).get();
      List<QueryDocumentSnapshot> documents = future.get().getDocuments();
      for (QueryDocumentSnapshot document : documents) {
        map.put(document.getId(), document.getData());
      }
    } catch (Exception e) {
      map.put("error", e.getMessage());
    }
      return map;
  }

  /**
   * Gets data from a given document from a given collection.
   * @param collection - name of the collection to search
   * @return - Map containing the information being returned
   * @throws ExecutionException - if an error occurs during execution
   * @throws InterruptedException - if an error interrupts the searching process
   */
  public Map<String, Object> getData(String collection, String docName)
      throws ExecutionException, InterruptedException {

    DocumentReference docRef = this.db.collection(collection).document(docName);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot document = future.get();
    if (document.exists()) {
      return document.getData();
    } else {
      Map<String, Object> map = new HashMap<>();
      map.put("result", "no data found");
      return map;
    }
  }

  public void removeData(String collection, String doc) {
    ApiFuture<WriteResult> writeResult = db.collection(collection).document(doc).delete();
  }

}