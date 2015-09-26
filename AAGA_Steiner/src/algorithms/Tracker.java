package algorithms;

public class Tracker {
	public static enum LABELS{
		INFO("[ INFO ]",1),
		ERROR("[ ERROR ]",1<<1),
		STATUS("[ STATUS ]",1<<2);

		private String label;
		private Byte mask;
		private int count;

		LABELS(String label,byte mask) {
			this.mask=mask;
			this.label=label;
			this.count++;
		}

		private String getLabel(){
			this.count++;
			return this.label;
		}

		public byte getMask() {
			return mask;
		}
	}

	private static byte mask;
	public static void setMask(byte mask) {
		Tracker.mask = mask;
	}

	public static boolean tracke(LABELS label,boolean expr,String message){
		if(mask & label.getMask()){
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
