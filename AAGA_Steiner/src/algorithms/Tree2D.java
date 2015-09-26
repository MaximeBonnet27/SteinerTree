package algorithms;

import java.awt.Point;
import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;


import algorithms.Tracker.LABELS;

public class Tree2D {
	private Point root;
	private ArrayList<Tree2D> subtrees;

	public Tree2D (Point p, ArrayList<Tree2D> trees){
		this.root=p;
		this.subtrees=trees;
	}
	
	public Point getRoot(){
		return this.root;
	}
	
	public ArrayList<Tree2D> getSubTrees(){
		return this.subtrees;
	}
	
	public double distanceRootToSubTrees(){
		double d=0;
		for (int i=0;i<this.subtrees.size();i++){
			d+= Math.sqrt(Math.pow(this.root.getX()-this.subtrees.get(i).getRoot().getX(),2)+Math.pow(this.root.getY()-this.subtrees.get(i).getRoot().getY(),2));
		}
		return d;
	}


	public ArrayList<Point> getPoints(){
		ArrayList<Point> liste = new ArrayList<Point>();
		Stack<Tree2D> pile = new Stack<Tree2D>();
		pile.push(this);

		while(!pile.isEmpty()){
			Tree2D tree = pile.pop();
			liste.add(tree.root);

			for(Tree2D sub : tree.subtrees){
				pile.push(sub);
			}
		}
		return liste;

	}

	// Fonction d'évaluation
	public int score() {
		double res = 0;
		ArrayList<Tree2D> trees = new ArrayList<>();
		trees.add(this);
		while (!trees.isEmpty()) {
			Tree2D current = trees.remove(0);
			res += current.distanceRootToSubTrees();
			trees.addAll(current.getSubTrees());
		}
		return (int) res;
	}

	public boolean isLeaf(){
		return subtrees.isEmpty();
	}

	public boolean deleteIfLeaf(Point p){
		boolean changed=false;
		Stack<Tree2D> stack = new Stack<Tree2D>();
		stack.add(this);
		while(!stack.isEmpty()){
			Tree2D tree = stack.pop();
			for(int i = 0; i < tree.subtrees.size(); ++i){
				if(tree.subtrees.get(i).isLeaf() && tree.subtrees.get(i).getRoot().equals(p)){
					tree.subtrees.remove(i);
					i--;
					changed=true;
				}
			}
		}
		return changed;
	}

	// Retourne l'arbre dont la racine est le point p.
	public Tree2D getTreeWithRoot(Point p) {
		if (this.getRoot().equals(p)) {
			return this;
		}else {
			Tree2D res = null;
			for (int i=0;i<this.subtrees.size();i++) {
				if ((res = this.subtrees.get(i).getTreeWithRoot(p)) != null) {
					return res;
				}
			}
			return null;
		}
	}

	// Ajoute des Fermat à l'arbre passé en paramètre
	// si ceux-ci permettent d'améliorer le score.
	// Configuration étudiée ici :
	// Racine - Fils - Petit-Fils
	public boolean ApplyFermatSubAndSubSub(){
		boolean changed=false;

		if(this.subtrees.isEmpty()){
			return changed;
		}

		for(int i=0;i<this.subtrees.size();i++){
			Tree2D subTree=this.subtrees.get(i);
			if(subTree.ApplyFermatSubAndSubSub())
				changed=true;

			Tree2D bestFermatTree=null;
			double bestNewDistance=Double.MAX_VALUE;

			for(int j=0;j<subTree.subtrees.size();j++){
				//System.out.println("ApplyFermatSubAndSubSub i:"+i+" j:"+j+" size:"+subTree.subtrees.size());
				Tree2D subSubTree=subTree.subtrees.get(j);
				Fermat fermat=new Fermat(this.root, subTree.root, subSubTree.root);

				double regularDistance=this.root.distance(subTree.root)+subTree.root.distance(subSubTree.root);
				double newDistance=this.root.distance(fermat)+fermat.distance(subTree.root)+fermat.distance(subSubTree.root);

				if(newDistance<bestNewDistance && regularDistance>newDistance){
					bestNewDistance=newDistance;
					bestFermatTree=new Tree2D(fermat, new ArrayList<Tree2D>());
					bestFermatTree.subtrees.add(subTree);
					bestFermatTree.subtrees.add(subSubTree);
				}
			}

			if(bestFermatTree!=null){
				this.subtrees.remove(bestFermatTree.subtrees.get(0));
				bestFermatTree.subtrees.get(0).subtrees.remove(bestFermatTree.subtrees.get(1));
				this.subtrees.add(i, bestFermatTree);
				changed=true;
			}

		}
		return changed;
	}

	// Idem que la méthode précédente.
	// Configuration étudiée ici :
	// Fils - Racine - Fils
	public boolean applyFermatSubAndSub(){
		boolean changed=false;

		if(this.subtrees.size()<2){
			return changed;
		}

		for(int i=0;i<this.subtrees.size();i++){
			if(this.subtrees.get(i).applyFermatSubAndSub())
				changed=true;
		}

		for(int i=0;i<this.subtrees.size()-1;i++){
			Tree2D subTree1=this.subtrees.get(i);

			Tree2D bestFermatTree=null;
			double bestNewDistance=Double.MAX_VALUE;

			for(int j=i+1;j<this.subtrees.size();j++){
				//System.out.println("ApplyFermatSubAndSub");
				Tree2D subTree2=this.subtrees.get(j);
				Fermat fermat=new Fermat(this.root, subTree1.root, subTree2.root);

				double regularDistance=this.root.distance(subTree1.root)+this.root.distance(subTree2.root);
				double newDistance=this.root.distance(fermat)+fermat.distance(subTree1.root)+fermat.distance(subTree2.root);

				if(newDistance<bestNewDistance && regularDistance>newDistance){
					bestNewDistance=newDistance;
					bestFermatTree=new Tree2D(fermat, new ArrayList<Tree2D>());
					bestFermatTree.subtrees.add(subTree1);
					bestFermatTree.subtrees.add(subTree2);
				}
			}

			if(bestFermatTree!=null){
				this.subtrees.remove(bestFermatTree.subtrees.get(0));
				this.subtrees.remove(bestFermatTree.subtrees.get(0));
				this.subtrees.add(i, bestFermatTree);
				changed=true;
			}
		}
		return changed;
	}

	public boolean afineFermat(){
		boolean changed=false;
		boolean iChanged;

		for(int i=0;i<this.subtrees.size();i++){
			Tree2D subTree;
			do{
				iChanged=false;
				subTree=this.subtrees.get(i);


				if(subTree.hasRootFermatAndOneSub()){
					this.subtrees.remove(subTree);
					this.subtrees.add(i,subTree.subtrees.get(0));
					iChanged=true;
					changed=true;
				}
			}while(iChanged);
			if(subTree.afineFermat())
				changed=true;
		}

		return changed;
	}

	public boolean deleteFermatLeaf(){
		boolean changed=false;
		ArrayList<Point> points=this.getPoints();
		for(Point p:points){
			if(p instanceof Fermat){
				if(this.deleteIfLeaf(p))
					changed=true;
			}
		}

		return changed;
	}

	public boolean hasRootFermatAndOneSub(){
		return (this.root instanceof Fermat) && this.subtrees.size()==1;
	}

	public static boolean applyFermat(Tree2D tree){
		boolean changed=false;
		boolean newFermat;

		do{
			newFermat=false;
			LinkedList<Tree2D> pile=new LinkedList<>();
			pile.add(tree);

			for(int i=0;i<pile.size();i++){
				Tree2D elt=pile.get(i);
				pile.addAll(elt.subtrees);
			}

			HashMap<Fermat, ArrayList<Point>> hashFermatToFils=new HashMap<>();

			while(!pile.isEmpty()){
				Tree2D elt=pile.pollLast();

				for(int i=0;i<elt.subtrees.size();i++){
					Tree2D subTree=elt.subtrees.get(i);

					//avec petit fils
					for(int j=0;j<subTree.subtrees.size();j++){
						Tree2D subSubTree=subTree.subtrees.get(j);

						Fermat fermat=new Fermat(elt.root, subTree.root, subSubTree.root);

						double regularDistance=elt.root.distance(subTree.root)+subTree.root.distance(subSubTree.root);
						double newDistance=elt.root.distance(fermat)+fermat.distance(subTree.root)+fermat.distance(subSubTree.root);

						fermat.setGain(regularDistance-newDistance);
						if(fermat.getGain()>0){
							ArrayList<Point> pointsImpliquer=new ArrayList<>();
							pointsImpliquer.add(elt.root);
							pointsImpliquer.add(subTree.root);
							pointsImpliquer.add(subSubTree.root);
							hashFermatToFils.put(fermat, pointsImpliquer);
						}
					}

					//avec fils
					if(i<elt.subtrees.size()-1){
						for(int j=i+1;j<elt.subtrees.size();j++){
							Tree2D subTreeBis=elt.subtrees.get(j);

							Fermat fermat=new Fermat(elt.root, subTree.root, subTreeBis.root);

							double regularDistance=elt.root.distance(subTree.root)+elt.root.distance(subTreeBis.root);
							double newDistance=elt.root.distance(fermat)+fermat.distance(subTree.root)+fermat.distance(subTreeBis.root);

							fermat.setGain(regularDistance-newDistance);
							if(fermat.getGain()>0){
								ArrayList<Point> pointsImpliquer=new ArrayList<>();
								pointsImpliquer.add(elt.root);
								pointsImpliquer.add(subTree.root);
								pointsImpliquer.add(subTreeBis.root);
								hashFermatToFils.put(fermat, pointsImpliquer);
							}
						}
					}
				}
			}


			//application dabor meilleur fermat
			ArrayList<Fermat> fermats=new ArrayList<>(hashFermatToFils.keySet());

			Collections.sort(fermats);
			Collections.reverse(fermats);

			while(!fermats.isEmpty()){
				Fermat best=fermats.remove(0);
				Point rootConcerne=hashFermatToFils.get(best).get(0);
				Point a=hashFermatToFils.get(best).get(1);
				Point b=hashFermatToFils.get(best).get(2);

				//filtre fermat implquant meme fils
				for(int i=0;i<fermats.size();i++){
					ArrayList<Point> impliquer=hashFermatToFils.get(fermats.get(i));
					if(impliquer.contains(a) || impliquer.contains(b) || impliquer.contains(rootConcerne)){
						fermats.remove(i);
						i--;
					}
				}

				Tree2D rootFermat=tree.getTreeWithRoot(rootConcerne);
				Tree2D fermatTree=new Tree2D(best, new ArrayList<Tree2D>());
				Tree2D treeA=tree.getTreeWithRoot(a);
				Tree2D treeB=tree.getTreeWithRoot(b);
				fermatTree.subtrees.add(treeA);
				fermatTree.subtrees.add(treeB);

				rootFermat.subtrees.remove(treeA) ;
				rootFermat.subtrees.remove(treeB);

				treeA.subtrees.remove(treeB);
				treeB.subtrees.remove(treeA);

				rootFermat.subtrees.add(fermatTree);

				changed=true;
				newFermat=true;
			}

		}while(newFermat);

		return changed;
	}

	private static ArrayList<Fermat> bestCombine(ArrayList<Fermat> fermats,HashMap<Fermat, ArrayList<Point>> hashFermatToFils){
		double bestGainTotal=Integer.MIN_VALUE;
		double gainTotal;

		if(fermats.size()<2){
			return fermats;
		}


		ArrayList<Fermat> clone;
		ArrayList<Fermat> bestList=null;
		ArrayList<Fermat> list;
		for(int i=0;i<fermats.size();i++){
			clone=new ArrayList<>(fermats.subList(i, fermats.size()));

			Fermat f1=fermats.get(i);
			clone.remove(f1);

			//filtre fermat implquant meme fils
			for(int j=0;j<clone.size();j++){
				ArrayList<Point> impliquer=hashFermatToFils.get(clone.get(j));
				Point rootConcerne=hashFermatToFils.get(f1).get(0);
				Point a=hashFermatToFils.get(f1).get(1);
				Point b=hashFermatToFils.get(f1).get(2);
				if(impliquer.contains(a) || impliquer.contains(b) || impliquer.contains(rootConcerne)){
					clone.remove(j);
					j--;
				}
			}
			list=bestCombine(clone, hashFermatToFils);

			gainTotal=f1.getGain();
			for(Fermat f:list)
				gainTotal+=f.getGain();

			if(gainTotal>bestGainTotal){
				bestGainTotal=gainTotal;
				bestList=list;
			}
		}


		return bestList;
	}

}
