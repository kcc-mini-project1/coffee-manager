package utils;

public class RenderSystem {
	// 브랜드 로고 출력
	public void printLogo() {
		System.out.println("\r\n"
				+ "     ██  █████  ██    ██  █████  ██████  ██████  ███████ ███████ ███████  ██████    ;)( ;\n"
				+ "     ██ ██   ██ ██    ██ ██   ██ ██   ██ ██   ██ ██      ██      ██      ██    ██  :----:\n"
				+ "     ██ ███████ ██    ██ ███████ ██████  ██████  █████   ███████ ███████ ██    ██ C|====|\n"
				+ "██   ██ ██   ██  ██  ██  ██   ██ ██      ██   ██ ██           ██      ██ ██    ██  |    |\n"
				+ " █████  ██   ██   ████   ██   ██ ██      ██   ██ ███████ ███████ ███████  ██████   `----'\n\n");
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
	
	// 부제목 출력
	public void printSubTitle(int size, String subTitle) {
		int blankSize = (int)(Math.round((size - (subTitle.length() * 1.5)) / 2));
		
		String divider = "=".repeat(size);
		String content = " ".repeat(blankSize) + subTitle;
		System.out.println(divider);
		System.out.println(content);
		System.out.println(divider);
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
}