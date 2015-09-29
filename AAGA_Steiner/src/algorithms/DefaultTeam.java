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
		int bestScorePrim;
		int score;



		boolean changed;
		boolean fermatChanged;



		int j=0;

		do{
			int i=0;
			int bonus=0;
			ArrayList<Point> list=(ArrayList<Point>)points.clone();
			bestScorePrim=Integer.MAX_VALUE;
			do{

				Tracker.resetInfoMsg();
				Tracker.resetStatusMsg();
				Tracker.resetErrorMsg();

				//System.out.println("avan prim");
				Tree2D tmp;
				double scoreTmp;
				double ScorePrim=Double.MAX_VALUE;
				prim=null;
				for(Point p:points){
					if(Math.random()<0.012 || prim==null){
						tmp=Prim.compute(list, (ArrayList<Point>)points.clone(),p);

						scoreTmp=tmp.score();
						if(scoreTmp<ScorePrim){
							ScorePrim=scoreTmp;
							prim=tmp;
						}
					}
				}
				//Tracker.trackeInfo((prim=Prim.compute(list,(ArrayList<Point>)points.clone()))!=null," prim : OK");

				//System.out.println("apres prim");

				int bornceBoucle=0;
				do{
					changed=false;

					//System.out.println("avant applyfermat");
					int boucle=0;
					//			do{
					fermatChanged=false;
					/*		if(Tracker.tracke(LABELS.INFO, prim.ApplyFermatSubAndSubSub(), "ApplyFermatSubAndSubSub OK:"+prim.getPoints().size())){
							fermatChanged=true;
							changed=true;
						}
						if(Tracker.tracke(LABELS.INFO, prim.applyFermatSubAndSub(), "applyFermatSubAndSub OK:"+prim.getPoints().size())){
							fermatChanged=true;
							changed=true;
						}

					 */	
					if(Tracker.trackeInfo(Tree2D.applyFermat(prim), "applyFermat OK : "+prim.getPoints().size())){
						fermatChanged=true;
						changed=true;
					}
					boucle++;
					//		}while(fermatChanged && boucle<10);

					//System.out.println("apres applyfermat");

					//System.out.println("avant reducefermat");

					do{
						fermatChanged=false;
						if(Tracker.trackeInfo(prim.deleteFermatLeaf(), "deleteFermatLeaf OK : "+prim.getPoints().size())){
							fermatChanged=true;
							changed=true;
						}
						if(Tracker.trackeInfo(prim.afineFermat(), "afineFermat OK : "+prim.getPoints().size())){
							fermatChanged=true;
							changed=true;
						}
					}while(fermatChanged);

					//System.out.println("apres reducefermat");
					bornceBoucle++;
				}while(changed && bornceBoucle <20);

				score=prim.score();

				if(score<bestScorePrim){
					bestScorePrim=score;
					bestPrim=prim;

					if(bestScorePrim<bestScore){
						bestScore=bestScorePrim;
						best=bestPrim;
					}
				}


				list=bestPrim.getPoints();
				int size=list.size();
				list=Prim.checkDoublons(list,(ArrayList<Point>)points.clone());
				int newSize=list.size();
				Tracker.trackeInfo(size!=newSize,"doublons OK : "+(size-newSize));
				/*changed=false;



			int scorePrim;
			int bestScorePrim=Integer.MAX_VALUE;
			for(Point p:points){
				prim=Prim.compute(points,p);


				reduce++;

				boolean fermatChanged;
				if(reduce%2==0){
				do{
					fermatChanged=false;
					if(Tracker.tracke(LABELS.INFO, prim.deleteFermatLeaf(), "deleteFermatLeaf OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
					if(Tracker.tracke(LABELS.INFO, prim.afineFermat(), "afineFermat OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
				}while(fermatChanged);
				}

				boolean applyed;

				do{
					applyed=false;
					if(Tracker.tracke(LABELS.INFO, Tree2D.applyFermat(prim), "applyFermat OK:"+prim.getPoints().size())){
						applyed=true;
					}
				}while(applyed);

				if(reduce%2==0){
				do{
					fermatChanged=false;
					if(Tracker.tracke(LABELS.INFO, prim.deleteFermatLeaf(), "deleteFermatLeaf OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
					if(Tracker.tracke(LABELS.INFO, prim.afineFermat(), "afineFermat OK:"+prim.getPoints().size())){
						fermatChanged=true;
					}
				}while(fermatChanged);
				}

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
			}*/

				i++;
				Tracker.addStatusMsg("j             : "+j);
				Tracker.addStatusMsg("i             : "+i);
				Tracker.addStatusMsg("size points   : "+list.size());
				Tracker.addStatusMsg("score         : "+score);
				Tracker.addStatusMsg("bestScorePrim : "+bestScorePrim);
				Tracker.addStatusMsg("bestScore     : "+bestScore);	


				Tracker.printError();
				Tracker.printInfos();
				Tracker.printStatus();
				
				if(bestScorePrim<3889 && bonus==0){
					bonus=500;
				}
			}while(i<500+bonus);
			
			j++;
		}while(j<10);
		return best;
	}

}
