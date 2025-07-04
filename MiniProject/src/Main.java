

import java.util.Scanner;

import dao.OrderDao;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean run = true;

        while (run) {
            System.out.println("=== 전체 관리 시스템 ==="); 
            System.out.println("1. 주문 관리");
            System.out.println("2. 직원 관리");
            System.out.println("3. 메뉴 관리");
            System.out.println("q. 종료");
            System.out.print(">> ");

            String input = sc.nextLine();

            switch (input) {
                case "1":
//                    new OrderDao().start();
                    break;
                case "2":
//                    new EmployeeDao().start();
                    break;
                case "3":
//                    new MenuDao().start();
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