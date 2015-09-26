package algorithms;

import java.util.ArrayList;

public class Tracker {
	public static enum LABELS{
		INFO("[ INFO ]",0),
		ERROR("[ ERROR ]",1),
		STATUS("[ STATUS ]",2);

		private String label;
		private int mask;
		private int count;

		LABELS(String label,int mask) {
			this.mask=mask;
			this.label=label;
			this.count++;
		}

		private String getLabel(){
			this.count++;
			return this.label;
		}

		public int getMask() {
			return mask;
		}
	}

	private static ArrayList<Integer> mask=new ArrayList<>();
	
	public static void addMask(int mask) {
		if(!Tracker.mask.contains(mask))
			Tracker.mask.add(mask);
	}
	
	public static void removeMask(int mask) {
		Tracker.mask.remove(new Integer(mask));
		
	}

	public static boolean tracke(LABELS label,boolean expr,String message){
		if(Tracker.mask.contains(label.getMask())){
			if(expr)
				System.out.println(label.getLabel()+" : "+message);
		}

		return expr;
	}

	public static boolean trakeInfo(boolean expr,String message){
		return tracke(LABELS.INFO, expr, message);
	}
	public static boolean trakeError(boolean expr,String message){
		return tracke(LABELS.ERROR, expr, message);
	}
	public static boolean trakeStatus(boolean expr,String message){
		return tracke(LABELS.STATUS, expr, message);
	}


}
