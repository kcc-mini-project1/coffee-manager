package main;
import java.sql.Connection;
import java.util.Scanner;

import utils.EmployeeUI;

public class EmployeeMain {
	public void start() {
		Connection con = null;
		Scanner read = new Scanner(System.in);
		EmployeeUI employeeInterAction = new EmployeeUI();
		
		boolean escape = false;
		while (true) {
			employeeInterAction.printFunction();
			String userInput = read.nextLine();
			
			switch (userInput) {
			case "1":
				employeeInterAction.insertEmployee(con, read);
				break;
			case "2":
				employeeInterAction.getEmployeeAll(con, read);
				break;
			case "3":
				employeeInterAction.updateEmployee(con, read);
				break;
			case "4":
				employeeInterAction.deleteEmployee(con, read);
				break;
			case "Q":
			case "q":
				System.out.println("직원 관리 시스템을 종료합니다.");
				escape = true;
				break;
			// 사용자 입력이 잘못된 경우 (1, 2, 3, 4, Q, q 의 입력만 기능 제공)
			default:
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}
			// 직원 관리 시스템 종료
			if (escape) {
				break;
			}
		}
	}
}
