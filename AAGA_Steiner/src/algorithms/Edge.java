package algorithms;

import java.awt.Point;

public class Edge implements Comparable<Edge>{

  public Point A, B;
  
  
  public Edge(Point a, Point b) {
    super();
    A = a;
    B = b;
  }

  public double length(){
    return A.distance(B);
  }
  
  @Override
  public int compareTo(Edge other) {
    return Double.compare(length(), other.length());
  }

  
  
}
