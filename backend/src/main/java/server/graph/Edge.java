package server.graph;

public class Edge {

  private SongNode source;
  private SongNode dest;
  public double yearDiff;
  public double durDiff;

  public Edge(SongNode source, SongNode dest, double yearDiff, double durDiff) {
    this.source = source;
    this.dest = dest;
    this.yearDiff = yearDiff;
    this.durDiff = durDiff;
  }

  public SongNode getSource() {
    return this.source;
  }

  public SongNode getDest() {
    return this.source;
  }

  public double getYearDiff() {
    return this.yearDiff;
  }

  public double getPopularityDiff() {
    return this.durDiff;
  }

  @Override
  public String toString() {
    return this.source.toString() + " -> " + this.dest.toString() +
        ", Year difference: " + this.yearDiff +
        ", Duration difference: " + this.durDiff;
  }
}
