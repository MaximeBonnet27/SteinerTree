/**
 * 
 */
package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Fermat extends Point implements Comparable<Fermat>{

	private static final double PI3 = Math.PI / 3;
	
	private double gain;
	
	public Fermat(Point a, Point b, Point c) {
		double x, y;
		double bc = b.distance(c);
		double ab = a.distance(b);
		double ca = c.distance(a);

		double sinPi3A = calculSinPi3(b, a, c);
		double sinPi3B = calculSinPi3(c, b, a);
		double sinPi3C = calculSinPi3(a, c, b);

		x = ((bc / sinPi3A) * a.getX() + (ab / sinPi3B) * b.getX() + (ca / sinPi3C) * c.getX())
				/ ((bc / sinPi3A) + (ab / sinPi3B) + (ca / sinPi3C));
		y = ((bc / sinPi3A) * a.getY() + (ab / sinPi3B) * b.getY() + (ca / sinPi3C) * c.getY())
				/ ((bc / sinPi3A) + (ab / sinPi3B) + (ca / sinPi3C));

		this.x = (int) x;
		this.y = (int) y;
		setLocation(x, y);

	}
	
	public void setGain(double gain) {
		this.gain = gain;
	}
	
	public double getGain() {
		return gain;
	}

	public Fermat(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Calcul de sin (PQR + PI/3)
	private double calculSinPi3(Point p, Point q, Point r) {
		double a = q.distance(r);
		double b = p.distance(q);
		double c = p.distance(r);

		double angle = Math.acos(((a * a) + (b * b) - (c * c)) / (2 * a * b));

		return Math.sin(angle + PI3);
	}

	public static ArrayList<Fermat> generateFromPoints(ArrayList<Point> points) {
		ArrayList<Fermat> fermats = new ArrayList<Fermat>();
		for (int i = 0; i < points.size() - 2; ++i) {
			Point p = points.get(i);
			for (int j = i + 1; j < points.size() - 1; ++j) {
				Point q = points.get(j);
				for (int k = j + 1; k < points.size(); ++k) {
					Point r= points.get(k);
					if(Math.random()<0.00001)
					fermats.add(new Fermat(p,q,r));
				}
			}
		}
		return fermats;
	}

	@Override
	public int compareTo(Fermat o) {
		return java.lang.Double.compare(gain, o.gain);
	}

}
