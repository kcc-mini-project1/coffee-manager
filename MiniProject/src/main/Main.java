package main;
import java.util.Scanner;

import utils.OrderUI;
import utils.RenderSystem;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		UserMenus userMenus = new UserMenus(sc);
		OrderUI orderUI = new OrderUI(sc);
		// 프로그램 시작. 로고 출력
		RenderSystem.printLogo();
		
		boolean run = true;
		
		while (run) {
            // 사용자 선택 화면
            RenderSystem.printTitle(RenderSystem.WIDTH, "카페 시스템");
            System.out.println(" 1. 손님");
            System.out.println(" 2. 직원");
            System.out.println(" 3. 사장님");
            System.out.println(" Q. 프로그램 종료");
            RenderSystem.printDivider(RenderSystem.WIDTH, true);
            
            RenderSystem.printInputForm();
            String input = sc.nextLine();
            RenderSystem.printEmptyLine(2);
            
            switch (input) {
                case "1":
                		orderUI.start();
                    break;
                case "2":
                    userMenus.showEmployeeMenu();
                    break;
                case "3":
                    userMenus.showOwnerMenu();
                    break;
                case "Q":
                case "q":
                case "ㅂ":
                    System.out.println("프로그램을 종료합니다.");
                    run = false;
                    break;
                default:
                    RenderSystem.printInvalidInput();
            }
        }

		sc.close();
	}
}
