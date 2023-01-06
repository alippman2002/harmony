package server.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class Dijkstra {
  private HashMap<SongNode, Edge> paths ;
  private HashMap<SongNode, Double> weights;
  private Function<Edge, Double> edgeWeight;

  /**
   * Dijkstra constructor, which takes in no parameters; here, instance variables
   * are initialized to have either an empty hashmap or just to null.
   */
  public Dijkstra() {
    this.paths = new HashMap<>();
    this.weights = new HashMap<>();
    this.edgeWeight = null;
  }

  /**
   * Returns the best path from source to destination, considering the edge weights
   * of each edge (with help of the edgeWeight function passed as an argument). The
   * method uses a PriorityQueue and a comparator to order the edge weights from low
   * to high.
   * @param graph       the graph including the vertices
   * @param source      the source vertex
   * @param destination the destination vertex
   * @param edgeWeight  the weight (cost or time) associated with the edge
   * @return
   */
  public List<Edge> getShortestPath(SongGraph graph, SongNode source, SongNode destination,
      Function<Edge, Double> edgeWeight) {
    this.edgeWeight = edgeWeight;

    // order numbers decreasing
    Comparator<SongNode> numsInIncreasingOrder = Comparator.comparingDouble(
        city -> this.weights.get(city));

    //populate hashmap with cities and values to infinity, except source (0)
    this.populateHashMap(graph, source);

    PriorityQueue<SongNode> toCheckQueue = new PriorityQueue<>(numsInIncreasingOrder);

    toCheckQueue.addAll(graph.getVertices());

    //while there are more cities to check in the PQ
    while (!toCheckQueue.isEmpty()) {
      SongNode checking = toCheckQueue.poll();
      double checkingDist = this.weights.get(checking);

      //for each edge emerging from the checking city
      for (Edge neighborEdge : graph.getOutgoingEdges(checking)) {

        SongNode neighborCity = graph.getEdgeTarget(neighborEdge);
        double neighborDist = this.weights.get(neighborCity);
        double cost = this.getEdgeWeight(neighborEdge);

        System.out.println("cost: " + cost);

        //if the checking distance and cost is smaller than the
        //neighbor city weight
        //TODO: NEVER GETS IN HERE!
        if ((checkingDist + cost) < neighborDist) {
          this.weights.replace(neighborCity, checkingDist + cost);
          //put dest city, then edge leading to city
          this.paths.put(neighborCity, neighborEdge);
          toCheckQueue.remove(neighborCity);
          toCheckQueue.add(neighborCity);
        }
      }
    }
    return this.returnCorrectPath(graph, source, destination);
  }

  /**
   * Helper function that initially populates the hashmap of vertices
   * and all their weights to be infinity, except for the source weight,
   * which will be 0.0.
   * @param graph - the IGraph<V,E> passed into getShortestPath
   * @param source - the source city passed into getShortestPath
   */
  private void populateHashMap(SongGraph graph, SongNode source) {
    for (SongNode currCity : graph.getVertices()) {
      if (currCity.equals(source)){
        this.weights.put(currCity, 0.0);
      } else {
        this.weights.put(currCity, Double.POSITIVE_INFINITY);
      }
    }
  }

  /**
   * A helper method that returns the edgeWeight associated with a certain edge.
   * @param edge - the edge to be checked
   * @return - the edge weight as a double
   */
  private double getEdgeWeight(Edge edge) {
    return this.edgeWeight.apply(edge);
  }

  /**
   * Helper method that goes through the hashmap of V -> E that will return
   * the correct path by starting at the destination vertex and work backwards
   * through the edges until it reaches the source. If there is no plausible
   * path, then an empty List is returned.
   * @param graph - the IGraph<V,E> passed into getShortestPath
   * @param start - the source city passed into getShortestPath
   * @param end - the destination city passed into getShortestPath
   * @return - a List<E> containing the edges from source to destination
   */
  private List<Edge> returnCorrectPath(SongGraph graph, SongNode start, SongNode end) {
    LinkedList<Edge> transports = new LinkedList<>();
    Edge leading = this.paths.get(end);
    if (leading != null) {
      SongNode currEdgeSource = graph.getEdgeSource(leading); // B
      // Start = A
      transports.add(leading); //B -> C in list
      while (currEdgeSource != start) { // B != A
        Edge nextEdge = this.paths.get(currEdgeSource); // A -> B
        currEdgeSource = graph.getEdgeSource(nextEdge); // A
        transports.add(nextEdge); // A -> B in list
      }
    }
    return transports;
  }
}
