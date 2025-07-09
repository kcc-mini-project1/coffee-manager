package utils;

import java.util.List;

public class RenderSystem {
	public int WIDTH = 110;
	
	public String PINK = "\033[38;2;241;101;138m";
	public String BLACK  = "\u001B[30m";
    public String RED    = "\u001B[31m";
    public String GREEN  = "\u001B[32m";
    public String YELLOW = "\u001B[33m";
    public String BLUE   = "\u001B[34m";
    public String WHITE  = "\u001B[37m";
    public String EXIT   = "\u001B[0m";
    
    public String colorize(String message, String colorName) {
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
	
	// 구분선 출력
	public void printDivider(int size, boolean single) {
		if (single) {
			System.out.println("-".repeat(size));
		} else {
			System.out.println("=".repeat(size));			
		}
	}
	
	// 제목 출력
	public void printTitle(int size, String title) {
		int blankSize = (int)(Math.round((size - (title.length() * 1.5)) / 2));
		
		String divider = "=".repeat(size);
		String content = " ".repeat(blankSize) + title;
		System.out.println(divider);
		System.out.println(content);
		System.out.println(divider);
	}
	
	public void printTitle(int size, String title, boolean underline) {
		int blankSize = (int)(Math.round((size - (title.length() * 1.5)) / 2));
		
		String divider = "=".repeat(size);
		String content = " ".repeat(blankSize) + title;
		System.out.println(divider);
		System.out.println(content);
		if (underline) System.out.println(divider);
	}
	
	// 부제목 출력
	public void printSubTitle(int size, String subTitle) {
		int blankSize = (int)(Math.round((size - (subTitle.length() * 1.5)) / 2));
		
		String divider = "=".repeat(size);
		String content = " ".repeat(blankSize) + subTitle;

		System.out.println(colorize(divider, "blue"));
		System.out.println(colorize(content, "blue"));
		System.out.println(colorize(divider, "blue"));
	}
	
	public void printSubTitle(int size, String subTitle, boolean underline) {
		int blankSize = (int)(Math.round((size - (subTitle.length() * 1.5)) / 2));
		
		String divider = "=".repeat(size);
		String content = " ".repeat(blankSize) + subTitle;

		System.out.println(colorize(divider, "blue"));
		System.out.println(colorize(content, "blue"));
		if (underline) System.out.println(colorize(divider, "blue"));
	}
	
	// 사용자로부터 입력 요청메시지 출력
	public void printInputForm() {
		System.out.print("원하시는 작업을 입력해주세요.\n>>> ");
	}
	
	// 원하는 메세지를 출력 후 입력 요청메세지 출력
	public void printInputFormMessage(String msg) {
		System.out.print(msg + "\n>>> ");
	}
	
	// 원하는 수의 개행 출력
	public void printEmptyLine(int size) {
		String content = "\n".repeat(size);
		System.out.print(content);
	}
	
	// [사용자 입력 에러 발생] 재입력 요청 메세지 출력
	public void printInvalidInput() {
		System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
	}
	
    public void printErrorMessage(String msg) {
        System.out.print("\n[에러] " + msg + "\n");
    }
    
	// 문자열 배열 단일행 출력
    public void printSingleMenu(List<String> options) {
		int PADDING = 2;
	    String pad = " ".repeat(PADDING);
	
	    for (int i = 0; i < options.size(); i++) {
	    		System.out.print((i + 1) + ". " + options.get(i) + pad);
	    }
	    
	    System.out.println();
	}

    // 문자열 배열 복수행 출력
	public void printMultiMenu(List<String> options) {
	    for (int i = 0; i < options.size(); i++) {
	        System.out.println((i + 1) + ". " + options.get(i));
	    }
	}
	
	// 쿠폰갯수 시각화
	public String printStamp(int currentStamp) {
	    int MAX_STAMP = 10;

	    if (currentStamp < 0) currentStamp = 0;
	    if (currentStamp > MAX_STAMP) currentStamp = MAX_STAMP;

	    int emptyCount = MAX_STAMP - currentStamp;

	    String coupon = "[" + "■".repeat(currentStamp) + "□".repeat(emptyCount) + "] ";

	    return coupon + currentStamp + "/" + MAX_STAMP;
	}
	
	// 원화 숫자 포맷
	public String formatWon(int amount) {
	    return String.format("%,d원", amount);
	};
	
	// 상태메세지
    public void printStatus(String message, boolean success) {
    		if (success) {
    			System.out.println("✔️ " + message);
    		} else {
    			System.out.println("❌ " + message);
    		}
    };
    
	// 브랜드 로고 출력
	public void printLogo() {
		String TITLELOGO = "\r\n"
	    		+ "                  ██  █████  ██    ██  █████  ██████  ██████  ███████ ███████ ███████  ██████    ;)( ;             \r\n"
	    		+ "                  ██ ██   ██ ██    ██ ██   ██ ██   ██ ██   ██ ██      ██      ██      ██    ██  :----:             \r\n"
	    		+ "                  ██ ███████ ██    ██ ███████ ██████  ██████  █████   ███████ ███████ ██    ██ C|====|             \r\n"
	    		+ "             ██   ██ ██   ██  ██  ██  ██   ██ ██      ██   ██ ██           ██      ██ ██    ██  |    |             \r\n"
	    		+ "              █████  ██   ██   ████   ██   ██ ██      ██   ██ ███████ ███████ ███████  ██████   `----'             \r\n"
	    		+ "                                                                                                                   \r\n";
		
		 String SLOGAN =
	    		"                 ╔═════════════════════════════════════════════════════════════════════════╗\n" +
	    		"                 ║                       빠른 행복의 시작 자바프레소                       ║\n" +
	    		"                 ╚═════════════════════════════════════════════════════════════════════════╝\n";
		
		 System.out.print(colorize(TITLELOGO, "pink"));
		 System.out.print(colorize(SLOGAN, "pink"));
		 printEmptyLine(2);
	};
};