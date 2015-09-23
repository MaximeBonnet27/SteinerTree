/**
 * 
 */
package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import algorithms.Tracker.LABELS;

public class Prim {

	public static Tree2D compute(ArrayList<Point> points) {
		// L'arbre que l'on retournera
		Tree2D resultTree = null;
		// On prend un point au hasard pour commencer
		// l'algorithme.
		Point p = points.get(0);//new Random().nextInt(points.size()));
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
			if(Tracker.tracke(LABELS.ERROR, minimumEdge == null, "minimumEdge==null")){
				treePoints.add(treePoints.get(0));
				continue;
			}
			Tree2D leaf = resultTree.getTreeWithRoot(minimumEdge.A);
			// On crée une feuille pour le point à relier.
			Tree2D newLeaf = new Tree2D(minimumEdge.B, new ArrayList<Tree2D>());
			// On relie les deux feuilles.
			leaf.getSubTrees().add(newLeaf);
			// On ajoute le nouveau point à la liste.
			treePoints.add(minimumEdge.B);

		}
		return resultTree;
	}


}
