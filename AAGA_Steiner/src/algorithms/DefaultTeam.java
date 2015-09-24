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
		//ArrayList<Fermat> fermats = generate(100, xMin, yMin, xMax, yMax);
		//points.addAll(fermats);

		/*******/
		Tree2D treeTmp;
		Tree2D best=null;
		int bestScore=Integer.MAX_VALUE;

		boolean changed;
		int i=0;
		int reduce=1;
		int countNoChanged=0;
		do{
			changed=false;

			treeTmp=Prim.compute(points);
			reduce++;

			boolean fermatChanged;
			//if(reduce%5==0){
			do{
				fermatChanged=false;
				if(Tracker.tracke(LABELS.INFO, treeTmp.deleteFermatLeaf(), "deleteFermatLeaf OK:"+treeTmp.getPoints().size())){
					changed=true;
					fermatChanged=true;
				}
				if(Tracker.tracke(LABELS.INFO, treeTmp.afineFermat(), "afineFermat OK:"+treeTmp.getPoints().size())){
					changed=true;
					fermatChanged=true;
				}
			}while(fermatChanged);
			//}
			/*if(Tracker.tracke(LABELS.INFO, treeTmp.applyFermatSubAndSub(), "ApplyFermatSubAndSub OK:"+treeTmp.getPoints().size()))
				changed=true;

			if(Tracker.tracke(LABELS.INFO, treeTmp.ApplyFermatSubAndSubSub(), "ApplyFermatSubAndSubSub OK:"+treeTmp.getPoints().size()))
				changed=true;*/
			boolean applyed;

			do{
				applyed=false;
				if(Tracker.tracke(LABELS.INFO, Tree2D.applyFermat(treeTmp), "applyFermat OK:"+treeTmp.getPoints().size())){
					changed=true;
					applyed=true;
				}
			}while(applyed);

			//if(reduce%5==0){
			do{
				fermatChanged=false;
				if(Tracker.tracke(LABELS.INFO, treeTmp.deleteFermatLeaf(), "deleteFermatLeaf OK:"+treeTmp.getPoints().size())){
					changed=true;
					fermatChanged=true;
				}
				if(Tracker.tracke(LABELS.INFO, treeTmp.afineFermat(), "afineFermat OK:"+treeTmp.getPoints().size())){
					changed=true;
					fermatChanged=true;
				}
			}while(fermatChanged);
			//}

			points=treeTmp.getPoints();

			int score=treeTmp.score();
			if(score<bestScore){
				bestScore=score;
				best=treeTmp;
			}

			//if(Tracker.tracke(LABELS.INFO, checkUnusedFermat(points, fermats), "CheckUnusedFermat OK:"+treeTmp.getPoints().size()))
			//changed=true;

			if(changed)
				countNoChanged=0;
			else
				countNoChanged++;
			if(countNoChanged==100){
				countNoChanged=0;
				for(int j=0;j<points.size();j++){
					if(Tracker.tracke(LABELS.INFO, points.get(j) instanceof Fermat, "suppression 30% fermat")){
						points.remove(j);
						j--;
					}
				}
			}

			i++;
		}while(Tracker.tracke(LABELS.INFO, changed || i<1000, "boucle: size : " + points.size() + " changed:"+changed+" i:"+i+" bestScore:"+bestScore));

		return best;
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

	public boolean checkUnusedFermat(ArrayList<Point> points, ArrayList<Fermat> fermats){
		boolean estCeQueJaiFaitQuelquechose = false;
		double scoreMin = Double.MAX_VALUE;
		Tree2D tree;
		for(Fermat fermat : fermats){

			points.remove(fermat);

			tree = Prim.compute(points);
			double score = tree.score();
			if(score < scoreMin){
				estCeQueJaiFaitQuelquechose = true;
				scoreMin = score;
			}
			else{
				if(!points.contains(fermat)){
					points.add(fermat);
				}
			}
		}

		return estCeQueJaiFaitQuelquechose;
	}

}
