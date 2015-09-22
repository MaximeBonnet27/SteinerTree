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
      //      geometricMedian(res);
      //      barycentresSubAndSubSub(res);
      //      barycentresSub(res);
      barycentreMultiSubs(10, res);
      newScore = 1000000000; //score(res);
    }while(newScore < oldScore);
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
  // Retourne l'arbre dont la racine est le point p.
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
  // Fonction d'évaluation
  private int score(Tree2D tree) {
    System.out.println("SCORE");
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
  // Ajoute des barycentres à l'arbre passé en paramètre
  // si ceux-ci permettent d'améliorer le score.
  // Configuration étudiée ici :
  // Racine - Fils - Petit-Fils
  private void barycentresSubAndSubSub(Tree2D tree){
    // Liste des arbres a etudier
    ArrayList<Tree2D> trees = new ArrayList<Tree2D>();
    trees.add(tree);
    while(!trees.isEmpty()){
      Tree2D current = trees.remove(0);
      // Les trois points considérés.
      // P est le point correspondant à la racine de l'arbre.
      Point p = current.getRoot();
      Point q = null;
      Point r = null;
      // On parcourt les sous-arbres
      for(int i = 0; i < current.getSubTrees().size(); ++i){
        Tree2D subTree = current.getSubTrees().get(i); 
        trees.add(subTree);
        // Q est la racine du sous-arbre
        q = subTree.getRoot();
        // On parcourt les sous-sous-arbres
        for(int j = 0; j < subTree.getSubTrees().size(); ++j){
          Tree2D subSubTree = subTree.getSubTrees().get(j);
          // R est la racinde du sous sous arbre
          r = subSubTree.getRoot();
          // Si on a bien trois points et qu'on gagne à rajouter le barycentre.
          if(p != null && q != null && r != null &&
              betterWithBarycentre(p,q,r)){
            // Création du barycentre et on relie correctement les arbres entre eux.
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
  // Idem que la méthode précédente.
  // Configuration étudiée ici :
  // Fils - Racine - Fils
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

  // Détermine si oui ou non on gagne à rajouter le barycentre pour 
  // les points p, q et r.
  private boolean betterWithBarycentre(Point p, Point q, Point r){
    double regularDistance = p.distance(q) + q.distance(r);
    Point barycentre = new Point((int) ((p.x + q.x + r.x) / 3), (int) ((p.y + q.y + r.y) / 3));
    double barycentreDistance = p.distance(barycentre) + barycentre.distance(q) + barycentre.distance(r);
    return barycentreDistance < regularDistance;
  }

  private void geometricMedian(Tree2D tree){

    // Tableaux pour parcourir les voisins
    // dans une boucle for
    int dx[] = {-1, 0, 1, 0};
    int dy[] = {0, 1, 0, -1};

    double delta = 20;
    double epsilon = 0.001;
    // Efficace que si l'arbre a au moins 2 fils
    ArrayList<Tree2D> trees = new ArrayList<>();
    ArrayList<Tree2D> visited = new ArrayList<>();
    trees.add(tree);
    while(!trees.isEmpty()){
      Tree2D current = trees.remove(0);
      visited.add(current);
      Tree2D medianTree = null;
      if(current.getSubTrees().size() > 1){
        // Tout d'abord on cherche le centre de gravité des points.
        ArrayList<Point> points = new ArrayList<>();
        for(Tree2D subTree : current.getSubTrees()){
          points.add(subTree.getRoot());
        }
        // Il faut prendre en considération la racine aussi.
        points.add(current.getRoot());

        Point center = centerOfGravity(points);
        double bestSum = sumDistance(center, points);

        Point candidate = null;
        boolean found = false;
        while(delta > epsilon){
          found = false;
          // Boucle pour checker des voisins autour du centre actuel
          for(int i = 0; i < 4; ++i){
            double x = center.getX() + delta * dx[i];
            double y = center.getY() + delta * dy[i];

            candidate = new Point((int) x, (int) y);
            double newSum = sumDistance(candidate, points);

            if(newSum < bestSum){
              bestSum = newSum;
              center = candidate;
              found = true;
              break;
            }
          }
          // Si on n'a pas trouvé un meilleur candidat
          // on cherche plus proche du point actuel
          if(!found){
            delta /= 2;
          }
        }
        // Après la boucle, center contient une approximation
        // du "geometric median" on peut donc le rajouter à l'arbre
        medianTree = new Tree2D(center, new ArrayList<Tree2D>());
        medianTree.getSubTrees().addAll(current.getSubTrees());
        current.getSubTrees().clear();
        current.getSubTrees().add(medianTree);
      }
      if(medianTree == null){
        medianTree = current;
      }
      for(Tree2D subtree : medianTree.getSubTrees()){
        if(!visited.contains(subtree)){
          trees.add(subtree);
        }
      }
    }
    System.out.println(visited.size());
  }

  private Point centerOfGravity(ArrayList<Point> points){
    double x = 0, y = 0;
    for(Point p : points){
      x += p.x;
      y += p.y;
    }
    x /= points.size();
    y /= points.size();
    return new Point((int) x, (int) y);
  }

  private double sumDistance(Point p, ArrayList<Point> points){
    double sum = 0;
    for(Point q : points){
      sum += p.distance(q);
    }
    return sum;
  }

  private void barycentreMultiSubs(int nbSubs, Tree2D tree){

    Random random = new Random();
    ArrayList<Tree2D> trees = new ArrayList<>();
    ArrayList<Tree2D> visited = new ArrayList<>();
    trees.add(tree);
    while(!trees.isEmpty()){
      Tree2D current = trees.remove(0);
      visited.add(current);
      ArrayList<Tree2D> path = new ArrayList<>();
      path.add(current);
      for(int i = 0; i < nbSubs && current.getSubTrees().size() > 0; ++i){
        current = current.getSubTrees().get(random.nextInt(current.getSubTrees().size()));
        path.add(current);
      }
      double ratio, bestRatio = 1;
      int bestNbTrees = 0;
      for(int nbTrees = 3; nbTrees < path.size(); ++nbTrees){
        ArrayList<Tree2D> beingChecked = new ArrayList<>();
        for(int i = 0; i < nbTrees; ++i){
          beingChecked.add(path.get(i));
        }
        Point barycentre = barycentre(beingChecked);
        ratio = ratio(barycentre, beingChecked);
        System.out.println(ratio);
        if(ratio < bestRatio){
          bestRatio = ratio;
          bestNbTrees = nbTrees;
        }
      }
      ArrayList<Tree2D> toReplace = new ArrayList<>();
      if(bestNbTrees >= 3){
        for(int i = 0; i < bestNbTrees; ++i){
          toReplace.add(path.get(i));
        }
        Point barycentre = barycentre(toReplace);
        Tree2D baryTree = new Tree2D(barycentre, new ArrayList<Tree2D>());
        for(int i = 0; i < toReplace.size() - 1; ++i){
          System.out.println("Before : " + toReplace.get(i).getSubTrees().size());
          toReplace.get(i).getSubTrees().remove(toReplace.get(i + 1));
          System.out.println("After : " + toReplace.get(i).getSubTrees().size());
        }
        toReplace.get(0).getSubTrees().add(baryTree);
        for(int i = 1; i < toReplace.size(); ++i){
          baryTree.getSubTrees().add(toReplace.get(i));
        }

      }
      for(Tree2D sub : current.getSubTrees()){
        if(!visited.contains(sub)){
          trees.add(sub);
        }
      }
    }
    System.out.println("FIN");
  }
  // Comme "centerOfGravity", mais pour Tree2D (peut pas surcharger a
  // cause de l'AList)
  private Point barycentre(ArrayList<Tree2D> trees){
    double x = 0, y = 0;
    for(Tree2D t : trees){
      x += t.getRoot().x;
      y += t.getRoot().y;
    }
    x /= trees.size();
    y /= trees.size();
    return new Point((int) x, (int) y);
  }

  private double ratio(Point barycentre, ArrayList<Tree2D> trees){
    Point current = trees.get(0).getRoot();
    double regularDistance = 0;
    for(int i = 1; i < trees.size(); ++i){
      regularDistance += current.distance(trees.get(i).getRoot());
      current = trees.get(i).getRoot();
    }
    double barycentreDistance = 0;
    for(Tree2D t : trees){
      barycentreDistance += barycentre.distance(t.getRoot());
    }
    return barycentreDistance / regularDistance;
  }

}
