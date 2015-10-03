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
		ArrayList<Fermat> listFermats=fromFile("score_3876", points);

		ArrayList<Point> list=new ArrayList<>();
		for(Point p:points){
			list.add(p);
		}
		for(Fermat f:listFermats)
			list.add(f);

		Point p=points.get(50);
		Tree2D steinerTree=Prim.compute(list, points, p,0);
		return steinerTree;
	}


	public Tree2D calculSteinerBudget(ArrayList<Point> points) {

		Tracker.addMask(LABELS.STATUS.getMask());

		Tree2D steinerTree=calculSteiner(points);
		//	ArrayList<Point> notLeaf=steinerTree.getPointNoLeaf();
		double budget=1664;
		Random rand=new Random();


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

					Tree2D tt=Tree2D.getTreeWithRoot(steinerTree, leafMax);
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









		/*
		pointsToJoin.remove(points.get(50));

		ArrayList<Point> meilleurList=new ArrayList<>(); //list avec meilleur score
		ArrayList<Point> list=new ArrayList<>();
		ArrayList<Point> pointsJoins=new ArrayList<>();
		ArrayList<Point> listFiltrer=null;
		pointsJoins.add(points.get(50));

		int i=0;
		int bestScore,score;
		bestScore=0;
		double budgetRest=budget;
		Tree2D meilleurTree=null;
		do{
			Tracker.resetInfoMsg();

			for(Point p:pointsToJoin){
				if(!pointsJoins.contains(p))
					list.add(p);
			}

			boolean found;
			do{

				// 1 list des noeud non feuille déja dans pointsJoins
				ArrayList<Point> poolNotLeaf=(ArrayList<Point>) notLeaf.clone();
				for (int k=0;k<poolNotLeaf.size();k++) {
					if(!pointsJoins.contains(poolNotLeaf.get(k))){
						poolNotLeaf.remove(k);
						k--;
					}
				}

				Point from=null;
				ArrayList<Point> childFrom=null;

				found=false;
				while(!found && !poolNotLeaf.isEmpty()){
					// 2 pop list non feuille
					from=poolNotLeaf.remove(rand.nextInt(poolNotLeaf.size()));
					childFrom=new ArrayList<>();

					Tree2D t=Tree2D.getTreeWithRoot(steinerTree, from);

					// cherche noeud suivant de from dans steiner
					// a ajouter
					for(Tree2D childT:t.getSubTrees()){
						if(!pointsJoins.contains(childT.getRoot()))
							childFrom.add(childT.getRoot());
					}

					// 3 filtrer
					listFiltrer=filter(from, childFrom, budgetRest);

					// 4 si filtrer vide 2
					found=!listFiltrer.isEmpty();
				}


				if(found){
					Point to;
					if(Math.random()<0.003)
						to=listFiltrer.get(rand.nextInt(listFiltrer.size()));
					else
						to=plusPres(listFiltrer, from);
					list.remove(to);
					pointsJoins.add(to);
					budgetRest-=from.distance(to);
				}

			}while(!list.isEmpty() && found);

			score=getScore(pointsJoins, points);
			if(score>bestScore){
				meilleurList=(ArrayList<Point>)pointsJoins.clone();
				bestScore=score;
			}



			meilleurTree=Prim.compute(meilleurList, points, points.get(50), 0);

			int nbSupMax=meilleurTree.getPoints().size();
			Tracker.addInfoMsg("nombre de suppression max : "+nbSupMax);
			int count=0;
			for(int k=0;k<nbSupMax;k++){
				ArrayList<Point> leaf=meilleurTree.getPointLeaf();
				for(int j=0;j<leaf.size();j++){
					if(leaf.get(j) instanceof Fermat){
						count++;
						leaf.remove(leaf.get(j));
					}
				}
				if(Math.random()<0.3){
					count++;

					meilleurTree.deleteIfLeaf(leaf.get(rand.nextInt(leaf.size())));
					meilleurTree=Prim.compute(meilleurList, points, points.get(50), 0);
				}
			}
			Tracker.addInfoMsg("nombre de suppression : "+count);

			pointsJoins=meilleurTree.getPoints();

			Tracker.printInfos();

			Tracker.resetStatusMsg();
			Tracker.addStatusMsg("i             : "+i);
			Tracker.addStatusMsg("score         : "+score);
			Tracker.addStatusMsg("Bestscore     : "+bestScore);
			Tracker.printStatus();
			i++;
		}while(i<100000);

		return Prim.compute(meilleurList, points, points.get(50),0);*/
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
