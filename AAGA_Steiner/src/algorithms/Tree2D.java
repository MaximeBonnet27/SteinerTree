package algorithms;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
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
  
  public ArrayList<Point> getListePoints(){
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
  
  public void deleteIfLeaf(Point p){
	  Stack<Tree2D> stack = new Stack<Tree2D>();
	  stack.add(this);
	  while(!stack.isEmpty()){
		  Tree2D tree = stack.pop();
		  for(int i = 0; i < tree.subtrees.size(); ++i){
			  if(tree.subtrees.get(i).isLeaf() && tree.subtrees.get(i).getRoot().equals(p)){
				  tree.subtrees.remove(i);
				  i--;
			  }
		  }
	  }
  }
  
  public boolean check(ArrayList<Point> points){
    for(Point p: points){
      if(!getListePoints().contains(p))
        return false;
    }
    return true;
  }

}
