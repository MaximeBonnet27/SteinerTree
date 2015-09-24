package genetic;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import algorithms.Fermat;
public class AlgorithmeGenetique{

	private final int NOMBRE_GENERATIONS = 100; //100*100
	private final int NOMBRE_MEILLEURS = 10; //100*100
	private final double BRUIT = 0.25;
	private final int TAILLE_POP = 100;
	public Individu[] population;
	private static ArrayList<Point> points;
	public static ArrayList<Point> fermats;
	
	public Individu best;
	private int bestScore = Integer.MAX_VALUE;
	
	public AlgorithmeGenetique(ArrayList<Point> points) {
		fermats = Fermat.computeAll(points);
		AlgorithmeGenetique.points = points;
		population = new Individu[TAILLE_POP];
		for(int i = 0; i < TAILLE_POP; ++i){
			population[i] = individuAleatoire();
		}
	}

	private void calculerGeneration(){
		calculFitness();
		selectionEtReproduction();
	}

	public Individu[] calculer(){
		for(int i = 0; i < NOMBRE_GENERATIONS; ++i){
			System.out.println("Generation : " + i);
			calculerGeneration();
			System.out.println("Meilleur score " + population[0]);
			if(population[0].getScore() < bestScore ){
				bestScore = population[0].getScore();
				best = population[0];
				System.out.println("best score : " + bestScore);
			}

		}
		return population;
	}

	private void calculFitness(){
		for(Individu individu : population){
			individu.calculScore();
		}
	}
	
	public Individu individuAleatoire(){
		int n = 3 + (int) (Math.random() * 20);
		int[] tab = new int[n];
		for(int i = 0; i < n; ++i){
			tab[i] = (int) (Math.random() * fermats.size());
		}
		return new Individu(n, tab);
	}

	private void selectionEtReproduction(){
		Arrays.sort(population);
		for(int i = NOMBRE_MEILLEURS; i < population.length; ++i){
			population[i] = reproduction();
		}

	}

	private Individu reproduction(){
		Random random = new Random();
		Individu parentA = population[random.nextInt(NOMBRE_MEILLEURS)];
		Individu parentB;
		parentB = population[random.nextInt(NOMBRE_MEILLEURS)];
		int taille = (parentA.nbPointsEnPlus + parentB.nbPointsEnPlus) / 2 + ((int) (Math.random()*10) % 2);
		int[] tab = reproductionTabIndexes(taille, parentA.indexes, parentB.indexes);
		bruiter(tab);
		return new Individu(taille, tab);
	}

	public int[] reproductionTabIndexes(int n, int[] tab1, int[] tab2){
		int[] res = new int[n];
		Random random = new Random();
		for(int i = 0; i < n; ++i){
			if(i%2 == 0)
				res[i] = tab1[random.nextInt(tab1.length)];
			else
				res[i] = tab2[random.nextInt(tab2.length)];
		}
		return res;
	}

	public void bruiter(int[] tab){
		for(int i = 0; i < tab.length; ++i){
			tab[i] = (int) (tab[i] + (Math.random() - 0.5) * tab[i] * BRUIT); 
		}
	}
	
	public static ArrayList<Point> getPoints() {
		return points;
	}

}