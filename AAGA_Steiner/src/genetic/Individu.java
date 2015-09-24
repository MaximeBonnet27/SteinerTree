package genetic;

import java.awt.Point;
import java.util.ArrayList;

import algorithms.DefaultTeam;
import algorithms.Prim;
import algorithms.Tree2D;

public class Individu implements Comparable<Individu>{
  private static int no = 0;
	private Tree2D res;
	public int score;
	public int nbPointsEnPlus;
	public int[] indexes;
	public int num;
	public Individu(int n, int[] indexes) {
		nbPointsEnPlus = n;
		this.indexes = indexes;
		num = no++;
	}

	public void calculScore(){
		ArrayList<Point> pointsConsideres = new ArrayList<>();
		pointsConsideres.addAll(AlgorithmeGenetique.getPoints());
		for(int i = 0; i < nbPointsEnPlus; ++i){
			pointsConsideres.add(AlgorithmeGenetique.fermats.get(indexes[i] % AlgorithmeGenetique.fermats.size()));
		}
		Tree2D res = Prim.compute(pointsConsideres);
		DefaultTeam dt = new DefaultTeam();
		for(int i = 0; i < 10; ++i){
			dt.barycentreMultiSubs(res);
			dt.barycentresSub(res);
			dt.barycentresSubAndSubSub(res);
		}
		this.res = res;
		score = res.score();
	}

	public int getScore(){
		return res().score();
	}

	@Override
	public int compareTo(Individu autre){
		return new Integer(score) .compareTo(new Integer(autre.score));
	}


	@Override
	public String toString(){
		return "Individu no " + num + " : points = " + nbPointsEnPlus + " -> " + score;
	}

	public Tree2D res(){
		DefaultTeam dt = new DefaultTeam();
		for(int i = 0; i < 10; ++i){
			dt.barycentreMultiSubs(res);
			dt.barycentresSub(res);
			dt.barycentresSubAndSubSub(res);
		}
		System.out.println("res -> " + res.score());
		return res;
	}


}