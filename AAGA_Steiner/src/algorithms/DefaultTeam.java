package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import algorithms.Tracker.LABELS;

public class DefaultTeam {
	public Tree2D calculSteiner(ArrayList<Point> points) {

		Tracker.addMask(LABELS.INFO.getMask());
		Tracker.addMask(LABELS.ERROR.getMask());
		Tracker.addMask(LABELS.STATUS.getMask());

		Tree2D prim;
		Tree2D bestPrim=null;
		Tree2D best=null;
		int bestScore=Integer.MAX_VALUE;

		boolean changed;
		int i=0;
		int reduce=1;
		int countNoChanged=0;

		do{
			changed=false;



			int scorePrim;
			int bestScorePrim=Integer.MAX_VALUE;
			for(Point p:points){
				prim=Prim.compute(points,p);


				reduce++;

				boolean fermatChanged;
				//if(reduce%2==0){
				do{
					fermatChanged=false;
					if(Tracker.tracke(LABELS.INFO, prim.deleteFermatLeaf(), "deleteFermatLeaf OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
					if(Tracker.tracke(LABELS.INFO, prim.afineFermat(), "afineFermat OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
				}while(fermatChanged);
				//}

				boolean applyed;

				do{
					applyed=false;
					if(Tracker.tracke(LABELS.INFO, Tree2D.applyFermat(prim), "applyFermat OK:"+prim.getPoints().size())){
						applyed=true;
					}
				}while(applyed);

				//if(reduce%2==0){
				do{
					fermatChanged=false;
					if(Tracker.tracke(LABELS.INFO, prim.deleteFermatLeaf(), "deleteFermatLeaf OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
					if(Tracker.tracke(LABELS.INFO, prim.afineFermat(), "afineFermat OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
				}while(fermatChanged);
				//}

				scorePrim=prim.score();
				if(scorePrim<bestScorePrim){
					bestScorePrim=scorePrim;
					bestPrim=prim;
					
				}
			}

			points=bestPrim.getPoints();

			int score=bestPrim.score();
			if(score<bestScore){
				bestScore=score;
				best=bestPrim;
				changed=true;
			}

			if(changed)
				countNoChanged=0;
			else
				countNoChanged++;
			if(countNoChanged==10){
				countNoChanged=0;
				for(int j=0;j<points.size();j++){
					if(Tracker.tracke(LABELS.INFO, (points.get(j) instanceof Fermat) && Math.random()<0.3, "suppression 30% fermat")){
						points.remove(j);
						j--;
					}
				}
			}

			i++;
		}while(Tracker.tracke(LABELS.STATUS, changed || i<10, "changed : "+changed+" size : " + points.size() + " i : "+i+" bestScore : "+bestScore));

		return best;
	}

}
