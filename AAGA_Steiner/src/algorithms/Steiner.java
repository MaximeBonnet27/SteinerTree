/**
 * 
 */
package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class Steiner {

	public static ArrayList<Point> compute(ArrayList<Point> points){
		int n = points.size();
		System.out.println("steneir("+n+")");
		ArrayList<Point> res =new ArrayList<Point>();
		if(n <= 1){
			return res;
		}
		else if (n == 2){
			return points;
		}
		else if(n == 3){
			res.add(new Fermat(points.get(0), points.get(1), points.get(2)));
		}
		else {
			Tree2D reseauCandidat = Prim.compute(points);
			double longueurCandidate = reseauCandidat.score();
			ArrayList<Point> s;
			for(int i = 0; i < points.size() - 1; ++i){
				Point a = points.get(i);
				for(int j = i + 1; j < points.size(); ++j){
					Point b = points.get(j);
					Point c = imageRotation60(b, a);
					
					ArrayList<Point> stein=new ArrayList<Point>(points);
					stein.remove(a);
					stein.remove(b);
					stein.add(c);
					s=compute(stein);
					s.add(new Fermat(a, b, c));
					
					ArrayList<Point> toto=new ArrayList<Point>();
					toto.addAll(points);
					toto.addAll(s);
					Tree2D tree=Prim.compute(toto);
					int longueur=tree.score();
					if(longueur<longueurCandidate){
						longueurCandidate=longueur;
						res=s;
					}
					
				}
			}
		}
		
		return res;
	}
	
	private static Point imageRotation60(Point point, Point centre){
		double x = (point.x - centre.x) * Math.cos(Math.PI/3)
				- (point.y - centre.y) * Math.sin(Math.PI/3);
		double y = (point.x - centre.x) * Math.sin(Math.PI/3) 
				+ (point.y - centre.y) * Math.cos(Math.PI/3);
				return new Point((int) x, (int) y);
	}
	
	
	
}
