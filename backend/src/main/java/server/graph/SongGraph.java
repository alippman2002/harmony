package server.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SongGraph {
  private HashMap<String, SongNode> hashMap;

  /**
   * The TravelGraph constructor, which initializes this.hashMap
   * to be an empty hashmap.
   */
  public SongGraph() {
    this.hashMap = new HashMap<>();
  }

  /**
   * Adds a vertex to the graph.
   * @param vertex the vertex to be added
   */
  public void addVertex(SongNode vertex) {
    if(!this.hashMap.containsValue(vertex)) {
      this.hashMap.put(vertex.toString(), vertex);
    }
    else
    {
      throw new IllegalArgumentException("This vertex already exists!");
    }
  }

  /**
   * Adds an edge to the graph.
   * @param origin the origin of the edge.
   * @param edge the edge to add
   */
  public void addEdge(SongNode origin, Edge edge) {
    origin.addEdge(edge);
  }

  /**
   * Gets the vertices of the graph.
   * @return - a Set containing the vertices of the graph as Cities.
   */
  public Set<SongNode> getVertices() {
    return new HashSet<>(this.hashMap.values());
  }

  /**
   *
   * @param edge the edge
   * @return
   */
  public SongNode getEdgeSource(Edge edge) {
    return edge.getSource();
  }

  /**
   *
   * @param edge the edge
   * @return
   */
  public SongNode getEdgeTarget(Edge edge) {
    return edge.getDest();
  }

  /**
   * Returns the outgoing edges of a given city in the graph.
   * @param fromVertex the vertex
   * @return - Set of Transports emerging from the city
   */
  public Set<Edge> getOutgoingEdges(SongNode fromVertex) {
    return fromVertex.getEdges();
  }

  /**
   * Returns the City represented by the String passed as an argument.
   * @param cityName - city as a string
   * @return - the City if it is contained in the hashmap, or null if not
   */
  public SongNode stringToCity(String cityName) {
    return this.hashMap.getOrDefault(cityName, null);
  }

  /**
   * Returns whether a certain City is contained in the graph.
   * @param vertex - City to check for
   * @return a boolean representing whether the City was found.
   */
  public boolean containsVertex(SongNode vertex)
  {
    return this.hashMap.containsValue(vertex);
  }
}
