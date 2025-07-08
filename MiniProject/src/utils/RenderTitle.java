package utils;

public class RenderTitle {
	public static final int STARCOUNT = 82;
	
	public static void printLine() {
		String stars = "=".repeat(STARCOUNT);
		
		System.out.println(stars);
	}
	
	 public static void renderTitle(String title) {
	        int innerWidth = STARCOUNT;

	        int blank = innerWidth - title.length();
	        if (blank < 0) blank = 0;

	        int leftPad  = blank / 2;
	        int rightPad = blank - leftPad;

	        String contentLine = " ".repeat(leftPad) + title + " ".repeat(rightPad);

	        printLine();
	        System.out.println(contentLine);
	        printLine();
	    }
	 
	 public static void renderTitle(String title, boolean underLine) {
	        int innerWidth = STARCOUNT;

	        int blank = innerWidth - title.length();
	        if (blank < 0) blank = 0;

	        int leftPad  = blank / 2;
	        int rightPad = blank - leftPad;

	        String contentLine = " ".repeat(leftPad) + title + " ".repeat(rightPad);

	        if (underLine) {
	        	printLine();
		        System.out.println(contentLine);
		        printLine();
	        } else {
	        	printLine();
		        System.out.println(contentLine);
	        }
	        
	    }
}

