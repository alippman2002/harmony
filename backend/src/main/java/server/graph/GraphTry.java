package server.graph;

import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.jgrapht.util.*;

import java.util.*;
import java.util.function.*;

/**
 * Demonstrates how to create a complete graph and perform a depth first search on it.
 *
 */
public final class GraphTry
{
  // number of vertices
  private static final int SIZE = 10;

  /**
   * Main demo entry point.
   *
   * @param args command line arguments
   */
  public static void main(String[] args)
  {
    // Create the VertexFactory so the generator can create vertices
    Supplier<String> vSupplier = new Supplier<>()
    {
      private int id = 0;

      @Override
      public String get()
      {
        return "v" + id++;
      }
    };

    // @example:generate:begin
    // Create the graph object
    Graph<String, DefaultEdge> completeGraph =
        new SimpleGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(), false);

    // Create the CompleteGraphGenerator object
    CompleteGraphGenerator<String, DefaultEdge> completeGenerator =
        new CompleteGraphGenerator<>(SIZE);

    // Use the CompleteGraphGenerator object to make completeGraph a
    // complete graph with [size] number of vertices
    completeGenerator.generateGraph(completeGraph);
    // @example:generate:end

    // Print out the graph to be sure it's really complete
    Iterator<String> iter = new DepthFirstIterator<>(completeGraph);
    while (iter.hasNext()) {
      String vertex = iter.next();
      System.out
          .println(
              "Vertex " + vertex + " is connected to: "
                  + completeGraph.edgesOf(vertex).toString());
    }
  }
}
