package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class DefaultTeam {
  public Tree2D calculSteiner(ArrayList<Point> points) {
    Tree2D res = prim(points);
    return res;
    
  }
  
  private Tree2D prim(ArrayList<Point> points){
    
    Tree2D resultTree = null;
    Point p = points.get(0);
    ArrayList<Point> treePoints = new ArrayList<>();
    resultTree = new Tree2D(p, new ArrayList<Tree2D>());
    treePoints.add(p);
    while(treePoints.size() != points.size()){
      Edge minimumEdge = null;
      double minimumEdgeLength = Double.MAX_VALUE;
      for(Point treePoint : treePoints){
        for(Point point : points){
          if(!treePoints.contains(point)){
            if(treePoint.distance(point) < minimumEdgeLength){
               minimumEdge = new Edge(treePoint, point);
               minimumEdgeLength = minimumEdge.length();
            }
          }
        }
      }
      Tree2D leaf = findInTree(resultTree, minimumEdge.A);
      Tree2D newLeaf = new Tree2D(minimumEdge.B, new ArrayList<Tree2D>());
      leaf.getSubTrees().add(newLeaf);
      treePoints.add(minimumEdge.B);
      
    }
    System.out.println(treePoints.size());
    System.out.println(score(resultTree));
    return resultTree;
  }
    
  private Tree2D findInTree(Tree2D tree, Point p){
    if(tree == null) return null;
    if(tree.getRoot().equals(p)){
      return tree;
    }
    else {
      Tree2D res = null;
      for(Tree2D sub : tree.getSubTrees()){
        if((res = findInTree(sub, p)) != null){
          return res;
        }
      }
      return null;
    }
  }
  
  private int score(Tree2D tree){
    int res = 0;
    ArrayList<Tree2D> trees = new ArrayList<>();
    trees.add(tree);
    while(!trees.isEmpty()){
      Tree2D current = trees.remove(0);
      res += current.distanceRootToSubTrees();
      trees.addAll(current.getSubTrees());
    }
    return res;
  }
}
