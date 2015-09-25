package algorithms;

public class Tracker {
	public static enum LABELS{
		INFO("[ INFO ]"),
		ERROR("[ ERROR ]"),
		STATUS("[ STATUS ]");
		
		private String label;
		
		
		LABELS(String label) {
			this.label=label;
		}
		
		public String getLabel(){
			return this.label;
		}
	}
	
	public static boolean tracke(LABELS label,boolean expr,String message){
		if(expr)
			System.out.println(label.getLabel()+" : "+message);
		
		return expr;
	}
	
}
