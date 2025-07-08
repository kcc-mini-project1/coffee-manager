package main;
import java.sql.Connection;
import java.util.Scanner;

import utils.EmployeeUI;
import utils.RenderSystem;

public class EmployeeMain {
	public void start() {
		Connection con = null;
		Scanner read = new Scanner(System.in);
		RenderSystem renderSys = new RenderSystem();
		EmployeeUI employeeInterAction = new EmployeeUI();
		
		boolean escape = false;
		while (true) {
			// 직원 관리 시스템 기능 출력 및 입력받기
			employeeInterAction.printFunction();
			String userInput = read.nextLine();
			renderSys.printEmptyLine(2);
			
			// 입력 확인
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
			case "ㅂ":
				System.out.println("직원 관리 시스템을 종료합니다.");
				renderSys.printEmptyLine(2);
				escape = true;
				break;
			// 사용자 입력이 잘못된 경우 (1, 2, 3, 4, Q, q 의 입력만 기능 제공)
			default:
				renderSys.printInvalidInput();
				renderSys.printEmptyLine(2);
			}
			// 직원 관리 시스템 종료
			if (escape) { break; }
		}
	}
}
