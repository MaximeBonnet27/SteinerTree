package algorithms;

import java.awt.Point;
import java.awt.PageAttributes.OriginType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

		ArrayList<Fermat> listFermats=fromFile("score_3878", points);
		do{
			int i=0;
			int bonus=0;
			ArrayList<Point> list=new ArrayList<>();
			for(Point p:points){
				list.add(p);
			}
			for(Fermat f:listFermats)
				list.add(f);
			bestScorePrim=Integer.MAX_VALUE;
			do{

				Tracker.resetInfoMsg();
				Tracker.resetStatusMsg();
				Tracker.resetErrorMsg();

				Tree2D tmp;
				double scoreTmp;
				double ScorePrim=Double.MAX_VALUE;
				prim=null;
				for(Point p:points){
					if(Math.random()<1 || prim==null){
						tmp=Prim.compute(list, points,p);


						int bornceBoucle=0;
						do{
							changed=false;

							int boucle=0;
							fermatChanged=false;
							if(Tracker.trackeInfo(Tree2D.applyFermat(tmp), "applyFermat OK : "+tmp.getPoints().size())){
								fermatChanged=true;
								changed=true;
							}
							boucle++;

							do{
								fermatChanged=false;
								if(Tracker.trackeInfo(tmp.deleteFermatLeaf(), "deleteFermatLeaf OK : "+tmp.getPoints().size())){
									fermatChanged=true;
									changed=true;
								}
								if(Tracker.trackeInfo(tmp.afineFermat(), "afineFermat OK : "+tmp.getPoints().size())){
									fermatChanged=true;
									changed=true;
								}
							}while(fermatChanged);

							bornceBoucle++;
						}while(changed && bornceBoucle <20);


						scoreTmp=tmp.score();
						if(scoreTmp<ScorePrim){
							ScorePrim=scoreTmp;
							prim=tmp;
						}
					}
				}



				score=prim.score();

				if(score<bestScorePrim){
					bestScorePrim=score;
					bestPrim=prim;

					if(bestScorePrim<bestScore){
						bestScore=bestScorePrim;
						best=bestPrim;
						
						if(bestScore<3885){
							Tree2D.print(best);
							Tracker.addInfoMsg("save tree OK");
						}
					}
					
					
				}


				list=bestPrim.getPoints();
				int size=list.size();
				list=Prim.checkDoublons(list,(ArrayList<Point>)points.clone());
				int newSize=list.size();
				Tracker.trackeInfo(size!=newSize,"doublons OK : "+(size-newSize));
				

				i++;
				Tracker.addStatusMsg("j             : "+j);
				Tracker.addStatusMsg("i             : "+i);
				Tracker.addStatusMsg("size points   : "+list.size());
				Tracker.addStatusMsg("score         : "+score);
				Tracker.addStatusMsg("bestScorePrim : "+bestScorePrim);
				Tracker.addStatusMsg("bestScore     : "+bestScore);	


				//Tracker.printError();
				//Tracker.printInfos();
				Tracker.printStatus();

				if(bestScorePrim<3889 && bonus==0){
					bonus=500;
				}
			}while(i<500+bonus);

			j++;
		}while(j<10);
		return best;
	}
	
	public ArrayList<Fermat> fromFile(String fileName,ArrayList<Point> origin){
		
		ArrayList<Fermat> list=new ArrayList<>();
		try {
			
			Scanner scan=new Scanner(new File(fileName));
			int x,y;
			while(scan.hasNextInt()){
				x=scan.nextInt();
				y=scan.nextInt();
				Point p=new Point(x, y);
				if(!origin.contains(p))
					list.add(new Fermat(x,y));
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

}
