package algorithms;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
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

		Random rand= new Random();
		int bestScore=Integer.MAX_VALUE;
		int bestScorePrim;
		int score;

		boolean changed;
		boolean fermatChanged;

		int j=0;

		//ArrayList<Fermat> listFermats=fromFile("score_3878", points);
		do{
			int i=0;
			int bonus=0;
			ArrayList<Point> list=new ArrayList<>();
			for(Point p:points){
				list.add(p);
			}
			//for(Fermat f:listFermats)
				//list.add(f);
			bestScorePrim=Integer.MAX_VALUE;
			do{

				Tracker.resetInfoMsg();
				Tracker.resetStatusMsg();
				Tracker.resetErrorMsg();

				Tree2D tmp;
				double scoreTmp;
				double ScorePrim=Double.MAX_VALUE;
				prim=null;
				Point p=points.get(rand.nextInt(points.size()));
				tmp=Prim.compute(list, points,p,0.0003);

				int bornceBoucle=0;
				do{
					changed=false;

					fermatChanged=false;
					if(Tracker.trackeInfo(Tree2D.applyFermat(tmp), "applyFermat OK : "+tmp.getPoints().size())){
						fermatChanged=true;
						changed=true;
					}

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


				score=prim.score();

				if(score<bestScorePrim){
					bestScorePrim=score;
					bestPrim=prim;

					if(bestScorePrim<bestScore){
						bestScore=bestScorePrim;
						best=bestPrim;

						if(bestScore<3876){
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


	public Tree2D calculSteinerBudget(ArrayList<Point> points) {

		Tracker.addMask(LABELS.STATUS.getMask());

		ArrayList<Fermat> listFermats=fromFile("score_3876", points);

		ArrayList<Point> list=new ArrayList<>();
		for(Point p:points){
			list.add(p);
		}
		for(Fermat f:listFermats)
			list.add(f);

		Tree2D steinerTree=Prim.compute(list, points, points.get(50),0);
		double budget=1664;


		/*liste de points a joindre*/


		int score=steinerTree.score();
		while(score>budget){
			ArrayList<Point> pointsSteiner=steinerTree.getPoints();
			double distance;
			double distanceMax=Double.MIN_VALUE;
			Point leafMax=null;
			ArrayList<Point> leafs=steinerTree.getPointLeaf();

			for(Point p:leafs){
				distance=p.distance(plusPres(pointsSteiner, p));
				if (distanceMax<distance) {
					distanceMax=distance;
					leafMax=p;

				}
			}


			steinerTree.deleteIfLeaf(leafMax);
			steinerTree.deleteFermatLeaf();

			score=steinerTree.score();

			Tracker.resetStatusMsg();
			Tracker.addStatusMsg("distance : "+score);
			Tracker.addStatusMsg("score :"+getScore(steinerTree.getPoints(), points));
			Tracker.printStatus();
		}

		return steinerTree;

	}



	public int getScore(ArrayList<Point> list,ArrayList<Point> origins){
		int score=0;
		for(Point p:list){
			if(origins.contains(p)){
				score++;
			}
		}
		return score;
	}


	public ArrayList<Point> filter(Point from,ArrayList<Point> list,double budget){
		ArrayList<Point> resultat=new ArrayList<>();
		for(Point p:list){
			if(p.distance(from)<=budget){
				resultat.add(p);
			}
		}
		return resultat;
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
			e.printStackTrace();
		}

		return list;
	}

	public Point plusPres(ArrayList<Point> points,Point from){
		double distanceMin=Double.MAX_VALUE;
		double distance;
		Point min=null;

		for(Point p:points){
			if(!p.equals(from)){
				distance=p.distance(from);
				if(distance<distanceMin){
					min=p;
					distanceMin=distance;
				}
			}
		}
		return min;
	}

}
