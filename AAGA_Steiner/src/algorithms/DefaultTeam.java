package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class DefaultTeam {
  public Tree2D calculSteiner(ArrayList<Point> points) {
    Tree2D res = null;
    
    Tree2D tmp = null;
    int bestBestScore = Integer.MAX_VALUE;
    for (int depart = 0; depart < points.size(); ++depart) {
      tmp = Prim.compute(depart, points);
      int score = 0, bestScore = tmp.score();
      for (int iterations = 0; iterations < 100; ++iterations) {
//        scan(tmp); // pas top 
        barycentresSubAndSubSub(tmp);
        barycentresSub(tmp);
        barycentreMultiSubs(tmp);
        score = tmp.score();
        if (score < bestScore) {
          bestScore = score;
        }

      }
      System.out.println(bestScore);
      if (bestScore < bestBestScore) {
        bestBestScore = bestScore;
        res = tmp;
        System.out.println(depart + " : " + score);
      }
    }
    
    return res;
  }

  // Ajoute des barycentres à l'arbre passé en paramètre
  // si ceux-ci permettent d'améliorer le score.
  // Configuration étudiée ici :
  // Racine - Fils - Petit-Fils
  private void barycentresSubAndSubSub(Tree2D tree) {
    // Liste des arbres a etudier
    ArrayList<Tree2D> trees = new ArrayList<Tree2D>();
    trees.add(tree);
    while (!trees.isEmpty()) {
      Tree2D current = trees.remove(0);
      // Les trois points considérés.
      // P est le point correspondant à la racine de l'arbre.
      Point p = current.getRoot();
      Point q = null;
      Point r = null;
      // On parcourt les sous-arbres
      for (int i = 0; i < current.getSubTrees().size(); ++i) {
        Tree2D subTree = current.getSubTrees().get(i);
        trees.add(subTree);
        // Q est la racine du sous-arbre
        q = subTree.getRoot();
        // On parcourt les sous-sous-arbres
        for (int j = 0; j < subTree.getSubTrees().size(); ++j) {
          Tree2D subSubTree = subTree.getSubTrees().get(j);
          // R est la racinde du sous sous arbre
          r = subSubTree.getRoot();
          // Si on a bien trois points et qu'on gagne à rajouter le barycentre.
          if (p != null && q != null && r != null
              && betterWithBarycentre(p, q, r)) {
            // Création du barycentre et on relie correctement les arbres entre
            // eux.
            // Point barycentre = new Point((int) ((p.x + q.x + r.x) / 3), (int)
            // ((p.y + q.y + r.y) / 3));
            Point barycentre = new Fermat(p, q, r);
            Tree2D baryTree = new Tree2D(barycentre, new ArrayList<Tree2D>());
            current.getSubTrees().remove(subTree);
            current.getSubTrees().add(0, baryTree);
            subTree.getSubTrees().remove(subSubTree);
            baryTree.getSubTrees().add(0, subTree);
            baryTree.getSubTrees().add(0, subSubTree);
          }
        }
      }
    }
  }

  // Idem que la méthode précédente.
  // Configuration étudiée ici :
  // Fils - Racine - Fils
  private void barycentresSub(Tree2D tree) {
    ArrayList<Tree2D> trees = new ArrayList<Tree2D>();
    trees.add(tree);
    while (!trees.isEmpty()) {
      Tree2D current = trees.remove(0);
      Point p = current.getRoot();
      Point q = null;
      Point r = null;
      for (int i = 0; i < current.getSubTrees().size(); ++i) {
        Tree2D subTree1 = current.getSubTrees().get(i);
        trees.add(subTree1);
        for (int j = i + 1; j < current.getSubTrees().size(); ++j) {
          Tree2D subTree2 = current.getSubTrees().get(j);
          q = subTree1.getRoot();
          r = subTree2.getRoot();
          if (p != null && q != null && r != null
              && betterWithBarycentre(q, p, r)) {
            // Point barycentre = new Point((int) ((p.x + q.x + r.x) / 3), (int)
            // ((p.y + q.y + r.y) / 3));
            Point barycentre = new Fermat(p, q, r);
            Tree2D baryTree = new Tree2D(barycentre, new ArrayList<Tree2D>());
            current.getSubTrees().remove(subTree1);
            current.getSubTrees().remove(subTree2);
            baryTree.getSubTrees().add(0, subTree1);
            baryTree.getSubTrees().add(0, subTree2);
            current.getSubTrees().add(0, baryTree);
          }
        }
      }
    }
  }

  // Détermine si oui ou non on gagne à rajouter le barycentre pour
  // les points p, q et r.
  private boolean betterWithBarycentre(Point p, Point q, Point r) {
    double regularDistance = p.distance(q) + q.distance(r);
    // Point barycentre = new Point((int) ((p.x + q.x + r.x) / 3), (int) ((p.y +
    // q.y + r.y) / 3));
    Point barycentre = new Fermat(p, q, r);
    double barycentreDistance = p.distance(barycentre) + barycentre.distance(q)
    + barycentre.distance(r);
    return barycentreDistance < regularDistance;
  }

  private void geometricMedian(Tree2D tree) {

    // Tableaux pour parcourir les voisins
    // dans une boucle for
    int dx[] = { -1, 0, 1, 0 };
    int dy[] = { 0, 1, 0, -1 };

    double delta = 20;
    double epsilon = 0.001;
    // Efficace que si l'arbre a au moins 2 fils
    ArrayList<Tree2D> trees = new ArrayList<>();
    ArrayList<Tree2D> visited = new ArrayList<>();
    trees.add(tree);
    while (!trees.isEmpty()) {
      Tree2D current = trees.remove(0);
      visited.add(current);
      Tree2D medianTree = null;
      if (current.getSubTrees().size() > 1) {
        // Tout d'abord on cherche le centre de gravité des points.
        ArrayList<Point> points = new ArrayList<>();
        for (Tree2D subTree : current.getSubTrees()) {
          points.add(subTree.getRoot());
        }
        // Il faut prendre en considération la racine aussi.
        points.add(current.getRoot());

        Point center = centerOfGravity(points);
        double bestSum = sumDistance(center, points);

        Point candidate = null;
        boolean found = false;
        while (delta > epsilon) {
          found = false;
          // Boucle pour checker des voisins autour du centre actuel
          for (int i = 0; i < 4; ++i) {
            double x = center.getX() + delta * dx[i];
            double y = center.getY() + delta * dy[i];

            candidate = new Point((int) x, (int) y);
            double newSum = sumDistance(candidate, points);

            if (newSum < bestSum) {
              bestSum = newSum;
              center = candidate;
              found = true;
              break;
            }
          }
          // Si on n'a pas trouvé un meilleur candidat
          // on cherche plus proche du point actuel
          if (!found) {
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
      if (medianTree == null) {
        medianTree = current;
      }
      for (Tree2D subtree : medianTree.getSubTrees()) {
        if (!visited.contains(subtree)) {
          trees.add(subtree);
        }
      }
    }
    System.out.println(visited.size());
  }

  private Point centerOfGravity(ArrayList<Point> points) {
    double x = 0, y = 0;
    for (Point p : points) {
      x += p.x;
      y += p.y;
    }
    x /= points.size();
    y /= points.size();
    return new Point((int) x, (int) y);
  }

  private double sumDistance(Point p, ArrayList<Point> points) {
    double sum = 0;
    for (Point q : points) {
      sum += p.distance(q);
    }
    return sum;
  }

  private void barycentreMultiSubs(Tree2D tree) {

    Stack<Tree2D> stack = new Stack<>();
    stack.push(tree);
    while (!stack.isEmpty()) {
      double regularScore, centerScore;
      double ratio, bestRatio = 1;
      int bestLength = 0;
      Point bestCenter = null;
      Tree2D current = stack.pop();
      ArrayList<Tree2D> path = randomPathToLeaf(current);
      for (int length = 3; length < path.size(); ++length) {
        ArrayList<Tree2D> checked = new ArrayList<Tree2D>();
        for (int i = 0; i < length; ++i) {
          checked.add(path.get(i));
        }
        Point center;
        if (length == 3) {
          center = new Fermat(checked.get(0).getRoot(),
              checked.get(1).getRoot(), checked.get(2).getRoot());
        } else {
          center = barycentre(checked);
        }
        regularScore = pathScore(checked);
        centerScore = centerScore(center, checked);
        ratio = centerScore / regularScore;
        if (ratio < bestRatio) {
          bestLength = length;
          bestRatio = ratio;
          bestCenter = center;
        }
      }
      if (bestLength >= 3) {
        Tree2D centerTree = new Tree2D(bestCenter, new ArrayList<Tree2D>());
        path.get(0).getSubTrees().add(centerTree);
        for (int i = 0; i < bestLength - 1; ++i) {
          path.get(i).getSubTrees().remove(path.get(i + 1));
        }
        for (int i = 1; i < bestLength; ++i) {
          centerTree.getSubTrees().add(path.get(i));
        }
      }
      for (int i = 0; i < current.getSubTrees().size(); ++i) {
        stack.push(current.getSubTrees().get(i));
      }
    }

  }

  // Comme "centerOfGravity", mais pour Tree2D (peut pas surcharger a
  // cause de l'AList)
  private Point barycentre(ArrayList<Tree2D> trees) {
    double x = 0, y = 0;
    for (Tree2D t : trees) {
      x += t.getRoot().x;
      y += t.getRoot().y;
    }
    x /= trees.size();
    y /= trees.size();
    return new Point((int) x, (int) y);
  }

  private double ratio(Point barycentre, ArrayList<Tree2D> trees) {
    Point current = trees.get(0).getRoot();
    double regularDistance = 0;
    for (int i = 1; i < trees.size(); ++i) {
      regularDistance += current.distance(trees.get(i).getRoot());
      current = trees.get(i).getRoot();
    }
    double barycentreDistance = 0;
    for (Tree2D t : trees) {
      barycentreDistance += barycentre.distance(t.getRoot());
    }
    return barycentreDistance / regularDistance;
  }

  private ArrayList<Tree2D> randomPathToLeaf(Tree2D root) {
    ArrayList<Tree2D> path = new ArrayList<Tree2D>();
    Tree2D current = root;
    while (!current.getSubTrees().isEmpty()) {
      path.add(current);
      current = current.getSubTrees()
          .get(new Random().nextInt(current.getSubTrees().size()));
    }
    path.add(current);
    return path;
  }

  private double pathScore(ArrayList<Tree2D> path) {
    double sum = 0;
    for (int i = 1; i < path.size(); ++i) {
      sum += path.get(i - 1).getRoot().distance(path.get(i).getRoot());
    }
    return sum;
  }

  private double centerScore(Point center, ArrayList<Tree2D> path) {
    double sum = 0;
    for (int i = 0; i < path.size(); ++i) {
      sum += path.get(i).getRoot().distance(center);
    }
    return sum;
  }

  private void scan(Tree2D tree) {
    double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE;
    double yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;
    ArrayList<Point> listePoints = tree.getListePoints();
    for (Point p : listePoints) {
      if (p.x > xMax)
        xMax = p.x;
      if (p.x < xMin)
        xMin = p.x;
      if (p.y > yMax)
        yMax = p.y;
      if (p.y < yMin)
        yMin = p.y;
    }

    int width = 100;
    int height = 100;
    double xPos, yPos;
    for (xPos = xMin; xPos + width < xMax; xPos += width) {
      for (yPos = yMin; yPos + height < yMax; yPos += height) {
        ArrayList<Point> localPoints = pointsInWindow(listePoints, xPos, yPos,
            width, height);
        boolean found = false;
        for (int i = 0; i < localPoints.size() - 2 && !found; ++i) {
          Point p = localPoints.get(i);
          for (int j = i + 1; j < localPoints.size() - 1 && !found; ++j) {
            Point q = localPoints.get(j);
            for (int k = j + 1; k < localPoints.size() && !found; ++k) {
              Point r = localPoints.get(k);
              Point fermat = new Fermat(p, q, r);
              if (betterWithBarycentre(p, q, r)) {
                Tree2D fermatTree = new Tree2D(fermat, new ArrayList<Tree2D>());
                Tree2D pTree = Prim.findInTree(tree, p);
                Tree2D qTree = Prim.findInTree(tree, q);
                Tree2D rTree = Prim.findInTree(tree, r);


                pTree.getSubTrees().remove(qTree);
                pTree.getSubTrees().remove(rTree);

                qTree.getSubTrees().remove(pTree);
                qTree.getSubTrees().remove(rTree);

                rTree.getSubTrees().remove(pTree);
                rTree.getSubTrees().remove(qTree);

                pTree.getSubTrees().add(fermatTree);

                fermatTree.getSubTrees().add(qTree);
                fermatTree.getSubTrees().add(rTree);
                found = true;
                
              }
            }
          }
        }
      }
    }
  }

  private ArrayList<Point> pointsInWindow(ArrayList<Point> points, double x, double y, int width, int height) {
    ArrayList<Point> res = new ArrayList<Point>();
    for(Point p : points){
      if(p.x < x + width && p.x >= x 
          && p.y < y + height && p.y >= y){
        res.add(p);
      }
    }
    return res;
  }

}
