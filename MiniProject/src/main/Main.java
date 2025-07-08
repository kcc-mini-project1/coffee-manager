package main;
import java.util.Scanner;

import utils.MenuUI;
import utils.OrderUI;
import utils.RenderSystem;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		RenderSystem renderSys = new RenderSystem();

		EmployeeMain empMain = new EmployeeMain();
		MenuUI menuUI = new MenuUI();
		
		int displaySize = 110;
		
		// 프로그램 시작. 로고 출력
		renderSys.printLogo();
		
		boolean run = true;
		while (run) {
			// 관리 시스템 접속 화면 출력 
			renderSys.printTitle(displaySize, "카페 관리 시스템");
			System.out.println(" 1. 주문 관리 시스템 접속");
			System.out.println(" 2. 직원 관리 시스템 접속");
			System.out.println(" 3. 메뉴 관리 시스템 접속");
			System.out.println(" Q. 프로그램 종료");
			renderSys.printDivider(displaySize, true);
			
			// 관리 시스템 선택
            renderSys.printInputForm();
            String input = sc.nextLine();
            renderSys.printEmptyLine(2);
            
            // 1, 2, 3 입력에 따라 관리 시스템 실행
            switch (input) {
            case "1":
            		OrderUI.start();
            		break;
            case "2":
            		empMain.start();
                break;
            case"3":
            		try {
            			menuUI.startMain();
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
                	break;
            case "Q":
            	case "q":
            	case "ㅂ":
            		System.out.println("프로그램을 종료합니다.");
            		run = false;
            		break;
            default:
            		renderSys.printInvalidInput();
            }
        }
		sc.close();
	}
}
