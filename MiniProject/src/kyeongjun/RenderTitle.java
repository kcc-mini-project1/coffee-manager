package kyeongjun;

public class RenderTitle {
	public static final int STARCOUNT = 82;
	
	public static String printLine() {
		String result = "=".repeat(STARCOUNT);
		
		return result;
	}
	
	 public static void renderTitle(String title) {
	        int innerWidth = STARCOUNT - 4;

	        int blank = innerWidth - title.length();
	        if (blank < 0) blank = 0;

	        int leftPad  = blank / 2;
	        int rightPad = blank - leftPad;

	        String titleLine = "= " + " ".repeat(leftPad) +
	        		title + " ".repeat(rightPad) + " =" + '\n';

	        String result = printLine() + '\n' + titleLine + printLine();
	        System.out.println(result);
	    }
}

