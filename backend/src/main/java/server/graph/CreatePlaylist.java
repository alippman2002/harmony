package server.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import server.deserializationObjects.RecommendationObj.ID;

public class CreatePlaylist {
  private List<SongNode> songs = new ArrayList<>();
  private List<Double> durations = new ArrayList<>();
  private List<Integer> releaseYears = new ArrayList<>();
  private SongGraph graph;

  public CreatePlaylist(List<ID> tracks) {
    this.graph = new SongGraph();
    for (ID track : tracks) {
      this.songs.add(new SongNode(track.id));
      this.durations.add(Double.valueOf(track.duration_ms));
      this.releaseYears.add(Integer.valueOf(track.album.release_date.substring(0, 4)));
    }

    Dijkstra dijkstra = new Dijkstra();
    //Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
    Function<Edge, Double> edgeDoubleFunction = e -> e.yearDiff;

    this.addVertices();
    this.addEdges();
    System.out.println("playlist created");

    Set<SongNode> vertices = this.graph.getVertices();
    for (SongNode vertex : vertices) {
      System.out.println(vertex.toString() + " " + this.songs.indexOf(vertex));
      Set<Edge> edges = this.graph.getOutgoingEdges(vertex);
      for (Edge edge: edges) {
        System.out.println(edge.toString());
      }
    }

    List<Edge> list = dijkstra.getShortestPath(this.graph, this.songs.get(0), this.songs.get(3), edgeDoubleFunction);
    System.out.println(list.size());
    for (Edge edge: list) {
      System.out.println(edge.toString());
    }
  }

  private void addVertices() {
    for (SongNode song : this.songs) {
      this.graph.addVertex(song);
    }
  }

  private void addEdges() {
    for (int i=0; i<this.songs.size(); i++) {
      SongNode source = this.songs.get(i);
      int z = 1;
      if (i == 0) {
        z = 2;
      }
      for (int j=this.songs.size() - z; j>i; j--) {
//        if (i==j) {
//          continue;
//        }
        SongNode dest = this.songs.get(j);
        Integer yearDiff = Math.abs(this.releaseYears.get(i) - this.releaseYears.get(j));
        Double durDiff = Math.abs(this.durations.get(i) - this.durations.get(j));
        Edge newEdge = new Edge(source, dest, yearDiff, durDiff);
        source.addEdge(newEdge);
        this.graph.addEdge(source, newEdge);
      }
    }
  }

  @Override
  public String toString() {
    return "CreatePlaylist{" +
        "songs=" + this.songs +
        ", durations=" + this.durations +
        ", releaseYears=" + this.releaseYears +
        '}';
  }

  //  public addEdges() {
//
//  }
}
