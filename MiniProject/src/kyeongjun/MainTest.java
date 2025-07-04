package kyeongjun;

import java.util.Scanner;

public class MainTest {
	public static void main (String[] args) {
		RenderMain rednerMain = new RenderMain();
		RenderTitle renderTitle = new RenderTitle();
		
		RenderMain.printLogoCLI();
		RenderTitle.renderTitle("원하시는 메뉴의 타이틀을 추가할수 있습니다.");
	}
};
