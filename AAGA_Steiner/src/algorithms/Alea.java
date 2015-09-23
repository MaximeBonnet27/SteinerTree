/**
 * 
 */
package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Alea {

	public static Tree2D compute(int n, ArrayList<Point> points) {
		double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE;
		double yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;
		for (Point p : points) {
			if(p.x > xMax)
				xMax = p.x;
			if(p.x < xMin)
				xMin = p.x;
			if(p.y > yMax)
				yMax = p.y;
			if(p.y < yMin)
				yMin = p.y;
		}
		ArrayList<Point> generes = generate(n, xMin, yMin, xMax, yMax);
		ArrayList<Point> pointsArbre = new ArrayList<Point>();
		pointsArbre.addAll(points);
		pointsArbre.addAll(generes);
		boolean supprimeFeuille = true;
		while(supprimeFeuille){
			
			//System.out.println("applique Prim ");
			Tree2D tree = Prim.compute(pointsArbre);
			
			//System.out.println("score:"+tree.score());
			int tailleOrigine = pointsArbre.size();
			
			//System.out.println("elagage");
			pointsArbre = elagage(tree, generes);
			supprimeFeuille = (tailleOrigine != pointsArbre.size());
			
			//System.out.println("nombre de point enlevé :"+(tailleOrigine-pointsArbre.size()));
		}
		return Prim.compute(pointsArbre);
	}

	private static ArrayList<Point> generate(int n, double xMin, double yMin, double xMax, double yMax){
		ArrayList<Point> liste = new ArrayList<Point>();
		int x, y;
		Random random = new Random();
		for(int i = 0; i < n; ++i){
			x = (int) (xMin + random.nextInt((int) (xMax - xMin)));
			y = (int) (yMin + random.nextInt((int) (yMax - yMin)));
			liste.add(new Point(x, y));
		}
		return liste;
	}
	
	private static ArrayList<Point> elagage(Tree2D tree, ArrayList<Point> pointsGeneres){
		for(Point p : pointsGeneres){
			tree.deleteIfLeaf(p);
		}
		return tree.getListePoints();
	}

}
