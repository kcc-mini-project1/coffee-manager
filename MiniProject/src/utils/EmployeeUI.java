package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import dao.DataSource;
import dao.EmployeeDao;

public class EmployeeUI {
	DataSource ds = new DataSource();
	EmployeeDao empDao = new EmployeeDao();
	RenderTitle title = new RenderTitle();
	
	// 직원 관리 시스템에서 할 수 있는 기능 출력 함수
	public void printFunction() {
		title.renderTitle("직원 관리 시스템");
		System.out.print(
				"1. 직원 추가하기\n"
				+ "2. 직원 조회하기\n"
				+ "3. 직원 정보수정하기\n"
				+ "4. 직원 삭제하기\n"
				+ "q. 이전으로\n");
		title.printLine();
		System.out.println();
		System.out.println("원하시는 작업을 입력해주세요");
		System.out.print(">>> ");
	}
	
	public void insertEmployee(Connection con, Scanner read) {
		try {
			String inputName;
			String inputPhoneNumber;
			String inputTitle;
			String inputSalary;
			
			while (true) {
				title.renderTitle("추가할 직원 정보 입력하기");
				System.out.println("직원의 이름을 입력하세요.");
				System.out.print(">>> ");
				inputName = read.nextLine();
				System.out.println();
				System.out.println("직원의 전화번호를 입력하세요.");
				System.out.print(">>> ");
				inputPhoneNumber = read.nextLine();
				System.out.println();
				System.out.println("직원의 직급을 입력하세요.");
				System.out.print(">>> ");
				inputTitle = read.nextLine();
				System.out.println();
				System.out.println("직원의 급여를 입력하세요.");
				System.out.print(">>> ");
				inputSalary = read.nextLine();
				
				title.renderTitle("입력한 직원정보");
				System.out.printf("%-6s: %s\n", "이름", inputName);
				System.out.printf("%-5s: %s\n", "전화번호", inputPhoneNumber);
				System.out.printf("%-6s: %s\n", "직급", inputTitle);
				System.out.printf("%-6s: %s\n", "급여", inputSalary);
				title.printLine();
				
				System.out.println();
				System.out.println("입력하신 정보가 맞으시면 'Y'를 입력해주세요.");
				System.out.print(">>> ");
				String inputOK = read.nextLine();
				if (inputOK.equals("Y") || inputOK.equals("y")) { break; }
				System.out.println("직원 정보를 다시 입력해주세요.");
			}
			
			con = ds.getConnection();
			empDao.insertEmployee(con, inputName, inputPhoneNumber, inputTitle, inputSalary);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public void getEmployeeAll(Connection con, Scanner read) {
		try {
			con = ds.getConnection();
			ResultSet rs = empDao.getEmployeeAll(con);
			
			title.renderTitle("전체 직원을 조회합니다.");
			System.out.printf("%3s%6s%9s%12s%7s%10s\n",
					"ID", "이름", "전화번호", "급여", "직급", "실적");
			title.printLine();
			
			while (rs.next()) {
				System.out.printf("%3s%6s%14s%10s%6s%10s\n",
						rs.getInt("employee_id"),
						rs.getString("name"),
						rs.getString("phone"),
						rs.getInt("salary"),
						rs.getString("title"),
						rs.getInt("performance"));
			}
			
			System.out.println("\n\n직원 조회가 성공적으로 이루어졌습니다.");
			while (true) {				
				System.out.println();
				System.out.println("확인을 마치셨으면 'Q'를 입력해주세요.");
				System.out.print(">>> ");
				String quitMessage = read.nextLine();
				if (quitMessage.equals("q")) {
					System.out.println("\n\n");
					break;
				} else if (quitMessage.equals("Q")) {
					System.out.println("\n\n");
					break;
				} else { 
					System.out.println("다시 입력해주세요");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public void updateEmployee(Connection con, Scanner read) {
		try {
			boolean loop = true;
			while (loop) {
				title.renderTitle("직원 정보 수정하기");
				System.out.println("수정할 직원의 아이디를 입력하세요.( q : 이전으로 , r : 직원id 조회하기 )");
				System.out.print(">>> ");
				String targetId = read.nextLine();
				
				if (targetId.equals("q") || targetId.equals("Q")) { return; }
				if (targetId.equals("r") || targetId.equals("R")) {
					this.getEmployeeAll(con, read);
					continue;
				}
				loop = false;

				con = ds.getConnection();
				ResultSet targetEmp = empDao.getEmployee(con, targetId);
				
				if (!targetEmp.next()) { System.out.println("업데이트할 직원 정보 조회 실패");}
				title.renderTitle("수정할 직원정보");
				System.out.printf("1. 이름 수정하기 (현재값: %s)\n", targetEmp.getString("employee_name"));
				System.out.printf("2. 번호 수정하기 (현재값: %s)\n", targetEmp.getString("phone_number"));
				System.out.printf("3. 급여 수정하기 (현재값: %s)\n", targetEmp.getInt("salary"));
				System.out.printf("4. 직급 수정하기 (현재값: %s)\n", targetEmp.getString("title"));
				System.out.printf("5. 관리자 아이디 수정하기 (현재값: %s)\n", targetEmp.getInt("manager_id"));
				title.printLine();
				
				String part = read.nextLine();
				
				boolean escape = true;
				while (escape) {	
					escape = false;
					switch (part) {
					case "1":
						System.out.println("수정할 이름을 입력해주세요.");
						System.out.print(">>> ");
						String newName = read.nextLine();
						
						empDao.updateEmployeeName(con, targetId, newName);
						break;
					case "2":
						System.out.println("수정할 전화번호를 입력해주세요.");
						System.out.print(">>> ");
						String newPhone = read.nextLine();
						
						empDao.updateEmployeePhone(con, targetId, newPhone);
						break;
					case "3":
						System.out.println("수정할 급여를 입력해주세요.");
						System.out.print(">>> ");
						String newSalary = read.nextLine();
						
						empDao.updateEmployeeSalary(con, targetId, newSalary);
						break;
					case "4":
						System.out.println("수정할 직급을 입력해주세요.");
						System.out.print(">>> ");
						String newTitle = read.nextLine();
						
						empDao.updateEmployeeTitle(con, targetId, newTitle);
						break;
					case "5":
						System.out.println("수정할 관리자 아이디를 입력해주세요.");
						System.out.print(">>> ");
						String newManagerId = read.nextLine();
						
						empDao.updateEmployeeManager(con, targetId, newManagerId);
						break;
					default:
						System.out.println("입력이 잘못되었습니다.");
						escape = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public void deleteEmployee(Connection con, Scanner read) {
		try {
			boolean loop = true;
			while (loop) {
				System.out.println();
				System.out.println("삭제할 직원의 아이디를 입력해주세요. (q : 이전으로 , r : 직원id 조회하기)");
				System.out.print(">>> ");
				
				String targetId = read.nextLine();
				if (targetId.equals("q") || targetId.equals("Q")) { return; }
				if (targetId.equals("r") || targetId.equals("R")) {
					this.getEmployeeAll(con, read);
					continue;
				}
				loop = false;
				
				con = ds.getConnection();
				empDao.deleteEmployeeSchedule(con, targetId);
				empDao.deleteEmployee(con, targetId);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
}
