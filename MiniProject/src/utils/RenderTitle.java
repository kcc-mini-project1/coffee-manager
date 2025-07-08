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
	public static final String PINK = "\033[38;2;241;101;138m";
	public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String WHITE  = "\u001B[37m";
    public static final String EXIT   = "\u001B[0m";
    
    public static String colorize(String message, String colorName) {
        String colorCode = BLACK;

        if (colorName != null) {
            switch(colorName.toLowerCase()) {
                case "red":    colorCode = RED;    break;
                case "green":  colorCode = GREEN;  break;
                case "yellow": colorCode = YELLOW; break;
                case "blue":   colorCode = BLUE;   break;
                case "pink":   colorCode = PINK;   break;
                case "black":  colorCode = BLACK;  break;
                default:       colorCode = BLACK;  break;
            }
        }
        return colorCode + message + EXIT;
    }
}
