import java.util.Scanner;

import dao.EmployeeDao;

public class EmployeeMain {
	public void start() {
		EmployeeDao empDao = new EmployeeDao();
		Scanner read = new Scanner(System.in);
		
		boolean escape = false;		
		while (true) {
			empDao.selectFunction();
			String userInput = read.nextLine();
			
			switch (userInput) {
			case "1":
				empDao.insertEmployee();
				break;
			case "2":
				empDao.getEmployee();
				break;
			case "3":
				empDao.updateEmployee();
				break;
			case "4":
				empDao.deleteEmployee();
				break;
			case "Q":
			case "q":
				System.out.println("직원 관리 시스템을 종료합니다.");
				escape = true;
				break;
			default:
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}
			
			// 직원 관리 시스템 종료
			if (escape) { break; }
		}
	}
}
