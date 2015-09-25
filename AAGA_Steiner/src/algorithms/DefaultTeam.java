package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import algorithms.Tracker.LABELS;

public class DefaultTeam {
	public Tree2D calculSteiner(ArrayList<Point> points) {

		Tree2D treeTmp;
		Tree2D best=null;
		int bestScore=Integer.MAX_VALUE;

		boolean changed;
		int i=0;
		int reduce=1;
		int countNoChanged=0;
		do{
			changed=false;

			Tree2D bestPrim=null;
			int scorePrim;
			int bestScorePrim=Integer.MAX_VALUE;

			for(Point p:points){
				treeTmp=Prim.compute(points,p);
				scorePrim=treeTmp.score();
				if(scorePrim<bestScorePrim){
					bestScorePrim=scorePrim;
					bestPrim=treeTmp;
				}
			}

			treeTmp=bestPrim;
			reduce++;

			boolean fermatChanged;
			if(reduce%2==0){
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
			}
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

			if(reduce%2==0){
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
			}

			points=treeTmp.getPoints();

			int score=treeTmp.score();
			if(score<bestScore){
				bestScore=score;
				best=treeTmp;
			}

			if(changed)
				countNoChanged=0;
			else
				countNoChanged++;
			if(countNoChanged==5){
				countNoChanged=0;
				for(int j=0;j<points.size();j++){
					if(Tracker.tracke(LABELS.INFO, (points.get(j) instanceof Fermat) && Math.random()<0.3, "suppression 30% fermat")){
						points.remove(j);
						j--;
					}
				}
			}

			i++;
		}while(Tracker.tracke(LABELS.STATUS, changed || i<20, "changed : "+changed+" size : " + points.size() + " i : "+i+" bestScore : "+bestScore));

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

}
