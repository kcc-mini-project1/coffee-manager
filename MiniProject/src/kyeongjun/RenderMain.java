package kyeongjun;

public class RenderMain {
	private static final int WIDTH = 84;
    static final String coffeeEmoji = new String(Character.toChars(0x2615));
    static final String TITLELOGO = "\r\n"
            + "     ██  █████  ██    ██  █████  ██████  ██████  ███████ ███████ ███████  ██████    ;)( ;\r\n"
            + "     ██ ██   ██ ██    ██ ██   ██ ██   ██ ██   ██ ██      ██      ██      ██    ██  :----:\r\n"
            + "     ██ ███████ ██    ██ ███████ ██████  ██████  █████   ███████ ███████ ██    ██ C|====|\r\n"
            + "██   ██ ██   ██  ██  ██  ██   ██ ██      ██   ██ ██           ██      ██ ██    ██  |    |\r\n"
            + " █████  ██   ██   ████   ██   ██ ██      ██   ██ ███████ ███████ ███████  ██████   `----'\r\n"
            + "                                                                                         \r\n";

    static final String SLOGAN = 
            "╔══════════════════════════════════════════════╗\n" +
            "║            "+coffeeEmoji+" 빠른 행복의 시작 자바프레소 "+coffeeEmoji+"         ║      \n" +
            "╚══════════════════════════════════════════════╝";
    
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
    	System.out.println(TITLELOGO);
		alignCenterText(SLOGAN);
		alignCenterText("=========== 카페관리 프로그램을 시작합니다. ===========");
    }
}
