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
			if(countNoChanged==100){
				countNoChanged=0;
				for(int j=0;j<points.size();j++){
					if(Tracker.tracke(LABELS.INFO, (points.get(j) instanceof Fermat) && Math.random()<0.3, "suppression 30% fermat")){
						points.remove(j);
						j--;
					}
				}
			}

			i++;
		}while(Tracker.tracke(LABELS.STATUS, changed || i<2000, "changed : "+changed+" size : " + points.size() + " i : "+i+" bestScore : "+bestScore));

		return best;
	}

}
