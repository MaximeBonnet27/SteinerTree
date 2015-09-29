package algorithms;

import java.util.ArrayList;

public class Tracker {
	public static enum LABELS{
		INFO("[ INFO ]",0),
		ERROR("[ ERROR ]",1),
		STATUS("[ STATUS ]",2);

		private String label;
		private final int mask;
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


		@Override
		public String toString() {
			return this.label;
		}

		public int getMask() {
			return mask;
		}
	}

	private static ArrayList<Integer> mask=new ArrayList<>();
	private static String statusMsg="";
	private static String infoMsg="";
	private static String errorMsg="";
	public static void addMask(int mask) {
		if(!Tracker.mask.contains(mask))
			Tracker.mask.add(mask);
	}

	public static void removeMask(int mask) {
		Tracker.mask.remove(new Integer(mask));

	}

	public static void printInfos(){
		System.out.println(LABELS.INFO+" : "+infoMsg);
	}

	public static void printError(){
		System.out.println(LABELS.ERROR+" : "+errorMsg);

	}
	public static void printStatus(){
		System.out.println(LABELS.STATUS+" : "+statusMsg);

	}
	
	public static boolean tracke(LABELS label,boolean expr,String message){
		if(Tracker.mask.contains(label.getMask())){
			if(expr){
				switch (label) {
				case INFO:
					addInfoMsg(message);
					break;
				case STATUS:
					addStatusMsg(message);
					break;
				case ERROR:
					addErrorMsg(message);
					break;
				default:
					break;
				}
			}
			//System.out.println(label.getLabel()+" : "+message);
		}

		return expr;
	}

	public static boolean trackeInfo(boolean expr,String message){
		return tracke(LABELS.INFO, expr, message);
	}
	public static boolean trackeError(boolean expr,String message){
		return tracke(LABELS.ERROR, expr, message);
	}
	public static boolean trackeStatus(boolean expr,String message){
		return tracke(LABELS.STATUS, expr, message);
	}

	public static void addStatusMsg(String msg){
		statusMsg+="\n\t"+msg;
	}

	public static void addInfoMsg(String msg){
		infoMsg+="\n\t"+msg;
	}

	public static void addErrorMsg(String msg){
		errorMsg+="\n\t"+msg;
	}

	public static void resetErrorMsg(){
		errorMsg="";
	}
	public static void resetStatusMsg(){
		statusMsg="";
	}

	public static void resetInfoMsg(){
		infoMsg="";
	}
}
