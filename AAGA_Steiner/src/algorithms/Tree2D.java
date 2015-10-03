package algorithms;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;



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

	public ArrayList<Point> getPointNoLeaf(){
		ArrayList<Point> liste = new ArrayList<Point>();
		Stack<Tree2D> pile = new Stack<Tree2D>();
		pile.push(this);

		while(!pile.isEmpty()){
			Tree2D tree = pile.pop();
			if(!tree.isLeaf()){
				liste.add(tree.root);

				for(Tree2D sub : tree.subtrees){
					pile.push(sub);
				}
			}
		}
		return liste;
	}

	public ArrayList<Point> getPointLeaf(){
		ArrayList<Point> liste = new ArrayList<Point>();
		Stack<Tree2D> pile = new Stack<Tree2D>();
		pile.push(this);

		while(!pile.isEmpty()){
			Tree2D tree = pile.pop();
			if(tree.isLeaf()){
				liste.add(tree.root);
			}
			for(Tree2D sub : tree.subtrees){
				pile.push(sub);
			}

		}
		return liste;
	}

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
					return changed;
				}
			}
			stack.addAll(tree.getSubTrees());
		}
		
		return changed;
	}

	// Retourne l'arbre dont la racine est le point p.
	public static Tree2D getTreeWithRoot(Tree2D tree,Point p) {
		Stack<Tree2D> stack=new Stack<Tree2D>();
		stack.add(tree);

		while(!stack.isEmpty()){
			Tree2D elt=stack.pop();
			if (elt.getRoot().equals(p)) {
				return elt;
			}else{
				stack.addAll(elt.getSubTrees());
			}
		}
		return null;
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
					if(fermat.getGain()>0.0){
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
						if(fermat.getGain()>0.0){
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

			for(int i=0;i<fermats.size();i++){
				ArrayList<Point> impliquer=hashFermatToFils.get(fermats.get(i));
				if(impliquer.contains(a) || impliquer.contains(b) || impliquer.contains(rootConcerne)){
					fermats.remove(i);
					i--;
				}
			}

			Tree2D rootFermat=Tree2D.getTreeWithRoot(tree,rootConcerne);
			Tree2D fermatTree=new Tree2D(best, new ArrayList<Tree2D>());
			Tree2D treeA=Tree2D.getTreeWithRoot(tree,a);
			Tree2D treeB=Tree2D.getTreeWithRoot(tree,b);
			fermatTree.subtrees.add(treeA);
			fermatTree.subtrees.add(treeB);

			rootFermat.subtrees.remove(treeA) ;
			rootFermat.subtrees.remove(treeB);

			treeA.subtrees.remove(treeB);
			treeB.subtrees.remove(treeA);

			rootFermat.subtrees.add(fermatTree);

			changed=true;
		}
		return changed;
	}

	public static void print(Tree2D tree){
		int score=tree.score();

		try {
			File file=new File("score_"+score);
			FileWriter fw=new FileWriter(file);
			BufferedWriter bw=new BufferedWriter(fw);

			Stack<Tree2D> stack=new Stack<>();
			stack.add(tree);

			while(!stack.isEmpty()){
				Tree2D elt=stack.pop();
				stack.addAll(elt.getSubTrees());
				bw.write(elt.root.x+" "+elt.root.y+"\n");
			}

			bw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}