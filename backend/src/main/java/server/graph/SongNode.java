package server.graph;

import java.util.HashSet;
import java.util.Set;

public class SongNode {

  private String id;
  private Set<Edge> edges;

  public SongNode(String id) {
    this.id = id;
    this.edges = new HashSet<>();
  }

  public Set<Edge> getEdges() {
    return this.edges;
  }

  public void addEdge(Edge edge) {
    this.edges.add(edge);
  }

  public String toString() {
    return this.id;
  }
}
