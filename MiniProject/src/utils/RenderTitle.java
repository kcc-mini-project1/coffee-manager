package utils;

public class RenderTitle {
	public static final int STARCOUNT = 82;
	
	public static void printLine() {
		String stars = "=".repeat(STARCOUNT);
		
		System.out.println(stars);
	}
	
	 public static void renderTitle(String title) {
	        int innerWidth = STARCOUNT - 4;

	        int blank = innerWidth - title.length();
	        if (blank < 0) blank = 0;

	        int leftPad  = blank / 2;
	        int rightPad = blank - leftPad;

	        String titleLine = "= " + " ".repeat(leftPad) +
	        		title + " ".repeat(rightPad) + " =";

	        System.out.println();
	        printLine();
	        System.out.println(titleLine);
	        printLine();
	        System.out.println();
	    }
}

