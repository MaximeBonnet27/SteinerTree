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
		Tree2D steinerTree=Prim.compute(list, points, p);
		return steinerTree;
	}

	public Tree2D calculSteinerBudget(ArrayList<Point> points) {
		Tracker.addMask(LABELS.STATUS.getMask());

		Tree2D tree=calculSteiner(points);
		double budget=1664;
		Random rand=new Random();


		ArrayList<Point> toJoin=tree.getPoints();
		toJoin.remove(points.get(50));

		ArrayList<Point> meilleurList=new ArrayList<>();
		int i=0;

		do{
			ArrayList<Point> list=new ArrayList<>();
			list.addAll(toJoin);

			ArrayList<Point> joins=new ArrayList<>();
			joins.add(points.get(50));
			double budgetRest=budget;
			do{
				Point from=joins.get(rand.nextInt(joins.size()));
				filter(from, list, budgetRest);
				if(!list.isEmpty()){
					Point to=list.remove(rand.nextInt(list.size()));
					joins.add(to);
					budgetRest-=from.distance(to);
				}
			}while(!list.isEmpty());

			if(meilleurList.size()<joins.size()){
				meilleurList=joins;
			}
			Tracker.resetStatusMsg();
			Tracker.addStatusMsg("i             : "+i);
			Tracker.addStatusMsg("score         : "+joins.size());
			Tracker.addStatusMsg("Bestscore     : "+meilleurList.size());
			Tracker.printStatus();
			i++;
		}while(i<999999999);

		return Prim.compute(meilleurList, points, points.get(50));
	}

	public void filter(Point from,ArrayList<Point> list,double budget){
		for(int i=0;i<list.size();i++){
			if(list.get(i).distance(from)>budget){
				list.remove(i);
				i--;
			}
		}
	}
	int i=0;
	private ArrayList<Point> joignable(Point root, ArrayList<Tree2D> subTrees, double budget) {
		System.out.println(i++);
		ArrayList<Point> res=new ArrayList<>();

		if(budget<0)
			return res;

		res.add(root);

		if(subTrees.isEmpty()){

			return res;

		}
		ArrayList<Point> score=new ArrayList<>();
		ArrayList<Point> tmp;
		for (Tree2D t : subTrees) {
			ArrayList<Tree2D> sub=(ArrayList<Tree2D>)subTrees.clone();
			sub.addAll(t.getSubTrees());
			tmp=joignable(t.getRoot(), sub, budget-root.distance(t.getRoot()));
			if(score.size()<tmp.size()){
				score=tmp;
			}
		}

		res.addAll(score);
		return res;
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
}
