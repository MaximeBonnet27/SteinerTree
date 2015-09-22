package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class DefaultTeam {
  public Tree2D calculSteiner(ArrayList<Point> points) {
    Tree2D res;
    res = prim(points);
    int oldScore, newScore;
    do{
      oldScore = score(res);
      barycentresSubAndSubSub(res);
      barycentresSub(res);
      newScore = score(res);
    }while(newScore < oldScore);
    System.out.println(score(res));
    return res;

  }

  private Tree2D prim(ArrayList<Point> points) {
    // L'arbre que l'on retournera
    Tree2D resultTree = null;
    // On prend un point au hasard pour commencer
    // l'algorithme.
    Point p = points.get(0);
    // Une liste qui contiendra tous les points que l'on a consulté
    ArrayList<Point> treePoints = new ArrayList<>();
    // On initialise l'arbre avec le premier point
    resultTree = new Tree2D(p, new ArrayList<Tree2D>()); 
    treePoints.add(p);
    // Condition d'arret : tous les points sont dans l'arbre 
    // (l'arbre doit couvrir tous les points)
    while (treePoints.size() != points.size()) {
      // On cherche l'arête qui relie un point de l'arbre à un point 
      // qui n'est pas dans l'arbre. De plus c'est l'arête de longueur
      // minimale.
      Edge minimumEdge = null;
      double minimumEdgeLength = Double.MAX_VALUE;
      for (Point treePoint : treePoints) {
        for (Point point : points) {
          if (!treePoints.contains(point)) {
            if (treePoint.distance(point) < minimumEdgeLength) {
              minimumEdge = new Edge(treePoint, point);
              minimumEdgeLength = minimumEdge.length();
            }
          }
        }
      }
      // On récupère la feuille correspondant au point appartenant à l'arbre
      // déjà créé. (On a fait en sorte que ce soit le bout A de l'arête)
      Tree2D leaf = findInTree(resultTree, minimumEdge.A);
      // On crée une feuille pour le point à relier.
      Tree2D newLeaf = new Tree2D(minimumEdge.B, new ArrayList<Tree2D>());
      // On relie les deux feuilles.
      leaf.getSubTrees().add(newLeaf);
      // On ajoute le nouveau point à la liste.
      treePoints.add(minimumEdge.B);

    }
    return resultTree;
  }

  private Tree2D findInTree(Tree2D tree, Point p) {
    if (tree == null)
      return null;
    if (tree.getRoot().equals(p)) {
      return tree;
    } else {
      Tree2D res = null;
      for (Tree2D sub : tree.getSubTrees()) {
        if ((res = findInTree(sub, p)) != null) {
          return res;
        }
      }
      return null;
    }
  }

  private int score(Tree2D tree) {
    double res = 0;
    ArrayList<Tree2D> trees = new ArrayList<>();
    trees.add(tree);
    while (!trees.isEmpty()) {
      Tree2D current = trees.remove(0);
      res += current.distanceRootToSubTrees();
      trees.addAll(current.getSubTrees());
    }
    return (int) res;
  }

  private void barycentresSubAndSubSub(Tree2D tree){
    // Liste des arbres a etudier
    ArrayList<Tree2D> trees = new ArrayList<Tree2D>();
    trees.add(tree);
    while(!trees.isEmpty()){
      Tree2D current = trees.remove(0);
      Point p = current.getRoot();
      Point q = null;
      Point r = null;
      for(int i = 0; i < current.getSubTrees().size(); ++i){
        Tree2D subTree = current.getSubTrees().get(i); 
        trees.add(subTree);
        q = subTree.getRoot();
        for(int j = 0; j < subTree.getSubTrees().size(); ++j){
          Tree2D subSubTree = subTree.getSubTrees().get(j);
          r = subSubTree.getRoot();
          if(p != null && q != null && r != null &&
              betterWithBarycentre(p,q,r)){
            Point barycentre = new Point((int) ((p.x + q.x + r.x) / 3), (int) ((p.y + q.y + r.y) / 3));
            Tree2D baryTree = new Tree2D(barycentre, new ArrayList<Tree2D>());
            current.getSubTrees().remove(subTree);
            current.getSubTrees().add(baryTree);
            subTree.getSubTrees().remove(subSubTree);
            baryTree.getSubTrees().add(subTree);
            baryTree.getSubTrees().add(subSubTree);
          }
        }
      }
    }
  }

  private void barycentresSub(Tree2D tree){
    ArrayList<Tree2D> trees = new ArrayList<Tree2D>();
    trees.add(tree);
    while(!trees.isEmpty()){
      Tree2D current = trees.remove(0);
      Point p = current.getRoot();
      Point q = null;
      Point r = null;
      for(int i = 0; i < current.getSubTrees().size(); ++i){
        Tree2D subTree1 = current.getSubTrees().get(i);
        trees.add(subTree1);
        for(int j = i + 1; j < current.getSubTrees().size(); ++j){
          Tree2D subTree2 = current.getSubTrees().get(j);
          q = subTree1.getRoot();
          r = subTree2.getRoot();
          if(p != null && q != null && r != null &&
              betterWithBarycentre(q,p,r)){
            Point barycentre = new Point((int) ((p.x + q.x + r.x) / 3), (int) ((p.y + q.y + r.y) / 3));
            Tree2D baryTree = new Tree2D(barycentre, new ArrayList<Tree2D>());
            current.getSubTrees().remove(subTree1);
            current.getSubTrees().remove(subTree2);
            baryTree.getSubTrees().add(subTree1);
            baryTree.getSubTrees().add(subTree2);
            current.getSubTrees().add(baryTree);
          }
        }
      }
    }
  }
  

  private boolean betterWithBarycentre(Point p, Point q, Point r){
    double regularDistance = p.distance(q) + q.distance(r);
    Point barycentre = new Point((int) ((p.x + q.x + r.x) / 3), (int) ((p.y + q.y + r.y) / 3));
    double barycentreDistance = p.distance(barycentre) + barycentre.distance(q) + barycentre.distance(r);
    return barycentreDistance < regularDistance;
  }
}
