package utils;

public class RenderMain {
	private static final int WIDTH = 84;
             
    static final String TITLELOGO = "\r\n"
    		+ "                  ██  █████  ██    ██  █████  ██████  ██████  ███████ ███████ ███████  ██████    ;)( ;             \r\n"
    		+ "                  ██ ██   ██ ██    ██ ██   ██ ██   ██ ██   ██ ██      ██      ██      ██    ██  :----:             \r\n"
    		+ "                  ██ ███████ ██    ██ ███████ ██████  ██████  █████   ███████ ███████ ██    ██ C|====|             \r\n"
    		+ "             ██   ██ ██   ██  ██  ██  ██   ██ ██      ██   ██ ██           ██      ██ ██    ██  |    |             \r\n"
    		+ "              █████  ██   ██   ████   ██   ██ ██      ██   ██ ███████ ███████ ███████  ██████   `----'             \r\n"
    		+ "                                                                                                                   \r\n";

    static final String SLOGAN =
    		"             ╔═════════════════════════════════════════════════════════════════════════╗\n" +
    		"             ║                       빠른 행복의 시작 자바프레소                       ║\n" +
    		"             ╚═════════════════════════════════════════════════════════════════════════╝";
    
    public static void alignCenterText(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            int padding = (WIDTH - line.length()) / 2;
            if (padding < 0) padding = 0;
            String spaces = " ".repeat(padding);
            System.out.println(spaces + line);
        }
    }
 
    public static void printLogoCLI() {
	    	System.out.print(RenderTitle.colorize(TITLELOGO, "pink"));
	    	System.out.println(SLOGAN);
    }
}
