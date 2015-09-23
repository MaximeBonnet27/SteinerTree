/**
 * 
 */
package algorithms;

import java.awt.Point;

public class Fermat extends Point{

	private static final double PI3 = Math.PI / 3;

	public Fermat(Point a, Point b, Point c) {
		double x, y;
		double bc = b.distance(c);
		double ab = a.distance(b);
		double ca = c.distance(a);
		
		double sinPi3A = calculSinPi3(b, a, c);
		double sinPi3B = calculSinPi3(c, b, a);
		double sinPi3C = calculSinPi3(a, c, b);
		
		x = ((bc / sinPi3A) * a.x + (ab / sinPi3B) * b.x + (ca / sinPi3C) * c.x) / 
			((bc / sinPi3A) + (ab / sinPi3B) + (ca / sinPi3C));
		y = ((bc / sinPi3A) * a.y + (ab / sinPi3B) * b.y + (ca / sinPi3C) * c.y) / 
				((bc / sinPi3A) + (ab / sinPi3B) + (ca / sinPi3C));

		this.x = (int) x;
		this.y = (int) y;
		
	}
	
	// Calcul de sin (PQR + PI/3)
	private double calculSinPi3(Point p, Point q, Point r){
		double a = q.distance(r);
		double b = p.distance(q);
		double c = p.distance(r);
		
		double angle = Math.acos(((a*a) + (b*b) - (c*c)) / (2 * a*b));
		
		return Math.sin(angle + PI3);
	}
	
	
}
