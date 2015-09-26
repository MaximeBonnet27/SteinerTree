/**
 * 
 */
package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.CipherInputStream;

import algorithms.Tracker.LABELS;

public class Prim {

	public static Tree2D compute(ArrayList<Point> points,Point depart) {

		int beforeSize=points.size();
		points=checkDoublons(points);

		Tracker.tracke(LABELS.INFO, beforeSize!=points.size(), "doublons repéré");

		// L'arbre que l'on retournera
		Tree2D resultTree = null;
		
		// Une liste qui contiendra tous les points que l'on a consulté
		ArrayList<Point> treePoints = new ArrayList<>();
		
		// On initialise l'arbre avec le premier point
		resultTree = new Tree2D(depart, new ArrayList<Tree2D>()); 
		treePoints.add(depart);
		
		// Condition d'arret : tous les points sont dans l'arbre 
		// (l'arbre doit couvrir tous les points)
		while (treePoints.size()<points.size()) {
			
			//enleve fermat doublons
			beforeSize=points.size();
			points=checkDoublons(points);
			Tracker.tracke(LABELS.INFO, beforeSize!=points.size(), "doublons repéré");
			
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

			Tracker.tracke(LABELS.INFO, resultTree.ApplyFermatSubAndSubSub(), "ApplyFermatSubAndSubSub OK:"+resultTree.getPoints().size());
			Tracker.tracke(LABELS.INFO, resultTree.applyFermatSubAndSub(), "ApplyFermatSubAndSub OK:"+resultTree.getPoints().size());

			/*boolean fermatAdded;
			do{
				fermatAdded=false;
				if(Tracker.tracke(LABELS.INFO, Tree2D.applyFermat(resultTree), "applyFermat OK:"+resultTree.getPoints().size())){
					fermatAdded=true;
				}
			}while(fermatAdded);*/
			

		}
		return resultTree;
	}

	public static ArrayList<Point> checkDoublons(ArrayList<Point> list){
		ArrayList<Point> resultat=new ArrayList<Point>();

		for (Point point : list) {
			if(!resultat.contains(point))
				resultat.add(point);
		}
		return resultat;
	}

}
