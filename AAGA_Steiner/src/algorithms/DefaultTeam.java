package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import algorithms.Tracker.LABELS;

public class DefaultTeam {
	public Tree2D calculSteiner(ArrayList<Point> points) {
		
		/*******/
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
		ArrayList<Fermat> generes = generate(300, xMin, yMin, xMax, yMax);
		points.addAll(generes);
		/*******/
		
		Tree2D treeTmp;
		Tree2D best=null;
		int bestScore=Integer.MAX_VALUE;

		boolean changed;
		int i=0;
		do{
			changed=false;

			points=checkDoublons(points);
			treeTmp=Prim.compute(points);
			if(Tracker.tracke(LABELS.INFO, treeTmp.ApplyFermatSubAndSubSub(), "ApplyFermatSubAndSubSub OK:"))
				changed=true;

			if(Tracker.tracke(LABELS.INFO, treeTmp.ApplyFermatSubAndSub(), "ApplyFermatSubAndSub OK:"))
				changed=true;

			boolean fermatChanged;
			do{
				fermatChanged=false;
				if(Tracker.tracke(LABELS.INFO, treeTmp.deleteFermatLeaf(), "deleteFermatLeaf OK")){
					changed=true;
					fermatChanged=true;
				}
				if(Tracker.tracke(LABELS.INFO, treeTmp.afineFermat(), "afineFermat OK")){
					changed=true;
					fermatChanged=true;
				}
			}while(fermatChanged);

			points=treeTmp.getPoints();
			
			int score=treeTmp.score();
			if(score<bestScore){
				bestScore=score;
				best=treeTmp;
			}
			i++;
		}while(Tracker.tracke(LABELS.INFO, changed || i<1000, "boucle: changed:"+changed+" i:"+i+" bestScore:"+bestScore));

		return best;
	}

	public ArrayList<Point> checkDoublons(ArrayList<Point> list){
		ArrayList<Point> resultat=new ArrayList<Point>();

		for (Point point : list) {
			if(!Tracker.tracke(LABELS.INFO, resultat.contains(point), "doublons repéré"))
				resultat.add(point);
		}
		return resultat;
	}
	
	public ArrayList<Fermat> generate(int n, double xMin, double yMin, double xMax, double yMax){
		ArrayList<Fermat> liste = new ArrayList<Fermat>();
		int x, y;
		Random random = new Random();
		for(int i = 0; i < n; ++i){
			x = (int) (xMin + random.nextInt((int) (xMax - xMin)));
			y = (int) (yMin + random.nextInt((int) (yMax - yMin)));
			liste.add(new Fermat(x, y));
		}
		return liste;
	}
}
