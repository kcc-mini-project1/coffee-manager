import java.util.Scanner;

import dao.MenuMainDao;
import utils.OrderUI;
import utils.RenderMain;
import utils.RenderTitle;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmployeeMain empMain = new EmployeeMain();
        MenuMainDao menuMain = new MenuMainDao();
        
        boolean run = true;
        
		RenderMain.printLogoCLI();
		
        while (run) {
            RenderTitle.renderTitle("전체 관리 시스템");
            System.out.println("1. 주문 관리");
            System.out.println("2. 직원 관리");
            System.out.println("3. 메뉴 관리");
            System.out.println("q. 종료");
            System.out.print(">> ");

            String input = sc.nextLine();

            switch (input) {
                case "1":
                	OrderUI.start();
                    break;
                case "2":
                    empMain.start();
                    break;
                case "3":
                	try {
                	menuMain.start();
                	} catch (Exception e){
                		e.printStackTrace();
                	}
                	
                    break;
                case "q":
                    System.out.println("종료합니다.");
                    run = false;
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
        sc.close();
    }
}