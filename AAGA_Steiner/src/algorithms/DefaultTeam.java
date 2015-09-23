package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import algorithms.Tracker.LABELS;

public class DefaultTeam {
	public Tree2D calculSteiner(ArrayList<Point> points) {
		Tree2D res;
		
		boolean changed=false;
		do{
			points=checkDoublons(points);
			res=Prim.compute(points);
			if(Tracker.tracke(LABELS.INFO, res.ApplyFermatSubAndSubSub(), "ApplyFermatSubAndSubSub score:"+res.score()))
				changed=true;
			
			if(Tracker.tracke(LABELS.INFO, res.ApplyFermatSubAndSub(), "ApplyFermatSubAndSub score:"+res.score()))
				changed=true;
			
			points=res.getPoints();
		}while(changed);
		
		return res;
	}
	
	public ArrayList<Point> checkDoublons(ArrayList<Point> list){
		ArrayList<Point> resultat=new ArrayList<Point>();
		
		for (Point point : list) {
			if(!Tracker.tracke(LABELS.INFO, resultat.contains(point), "doublons repéré"))
				resultat.add(point);
		}
		return resultat;
	}
}
