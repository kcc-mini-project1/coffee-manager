package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import dao.DataSource;
import dao.EmployeeDao;

public class EmployeeUI {
	DataSource ds = new DataSource();
	RenderSystem renderSys = new RenderSystem();
	EmployeeDao empDao = new EmployeeDao();
	int displaySize = 110;
	
	// 숫자로 바꿀수 있는 값인지 확인 함수
	public boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	// 직원 관리 시스템에서 할 수 있는 기능 출력 함수
	public void printFunction() {
		renderSys.printTitle(displaySize, "직원 관리 시스템");
		System.out.println(
				" 1. 새로운 직원 정보 등록하기\n"
				+ " 2. 전체 직원 정보 조회하기\n"
				+ " 3. 직원 정보 수정하기\n"
				+ " 4. 퇴사한 직원 정보 삭제하기\n"
				+ " Q. 이전으로");
		renderSys.printDivider(displaySize, true);
		renderSys.printInputForm();
	}
	
	// 1. 새로운 직원 정보 등록하기
	public void insertEmployee(Connection con, Scanner read) {
		try {
			// 신규 직원의 이름, 전화번호, 직급, 급여를 저장할 변수
			String inputName;
			String inputPhoneNumber;
			String inputTitle;
			String inputSalary;
			
			// 등록할 직원 정보가 올바르게 입력될 때까지 반복
			while (true) {
				// 제목 출력
				renderSys.printTitle(displaySize, "신규 직원 등록하기");
				
				// 직원 이름 입력받기
				renderSys.printInputFormMessage("직원의 이름을 입력해주세요.");
				inputName = read.nextLine();
				renderSys.printEmptyLine(2);

				// 직원 전화번호 입력받기
				renderSys.printInputFormMessage("직원의 전화번호를 입력해주세요.");
				inputPhoneNumber = read.nextLine();
				renderSys.printEmptyLine(2);
				
				// 직원 직급 입력받기 (점장, 매니저, 알바를 제외한 입력값은 재입력)
				while (true) {
					renderSys.printTitle(displaySize, "신규 직원 등록하기 - 직급 종류");
					System.out.println("\u2714 점장");
					System.out.println("\u2714 매니저");
					System.out.println("\u2714 알바");
					renderSys.printDivider(displaySize, true);
					renderSys.printInputFormMessage("직원의 직급을 입력헤주세요.");
					inputTitle = read.nextLine();
					renderSys.printEmptyLine(2);
					
					// 입력 확인
					if (inputTitle.equals("점장")) {
						System.out.println("현재 점장으로는 등록할 수 없습니다.");
						renderSys.printEmptyLine(2);
					} else if (inputTitle.equals("매니저") || inputTitle.equals("알바")) {
						break;
					} else {
						renderSys.printInvalidInput();
						renderSys.printEmptyLine(2);
					}
				}
				
				// 직원 급여 입력받기
				renderSys.printInputFormMessage("직원의 급여를 입력해주세요.");
				inputSalary = read.nextLine();
				renderSys.printEmptyLine(2);
				
				// 입력한 모든 정보 출력하기
				renderSys.printTitle(displaySize, "입력한 직원정보");
				System.out.printf("%-6s: %s\n", "이름", inputName);
				System.out.printf("%-5s: %s\n", "전화번호", inputPhoneNumber);
				System.out.printf("%-6s: %s\n", "직급", inputTitle);
				System.out.printf("%-6s: %s\n", "급여", inputSalary);
				renderSys.printDivider(displaySize, true);
				
				// 등록하려는 정보가 제대로 입력되었는지 확인
				renderSys.printInputFormMessage("입력하신 정보가 맞으시면 'y'를 입력해주세요.");
				String inputOK = read.nextLine();
				if (inputOK.equals("Y") || inputOK.equals("y") || inputOK.equals("ㅂ")) { break; }
				System.out.println("직원 정보를 다시 입력해주세요.");
				renderSys.printEmptyLine(2);
			}
			
			// 입력받은 정보를 DB에 추가
			con = ds.getConnection();
			empDao.insertEmployee(con, inputName, inputPhoneNumber, inputTitle, inputSalary);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			renderSys.printEmptyLine(2);
		} finally {
			ds.closeConnection(con);
		}
	}
	
	// 2. 등록된 모든 직원정보 조회 및 출력
	public void getEmployeeAll(Connection con, Scanner read) {
		try {
			// DB에서 모든 직원정보 불러오기
			con = ds.getConnection();
			ResultSet rs = empDao.getEmployeeAll(con);
			
			// 부제목 출력
			renderSys.printSubTitle(displaySize, "전체 직원정보 조회");
			System.out.printf("%3s\t%6s\t%6s\t%10s\t%10s\t%13s\n",
					"ID", "이름", "전화번호", "급여", "직급", "이번달 매출");
			renderSys.printDivider(displaySize, true);
			
			// 읽어온 직원 정보 출력
			while (rs.next()) {
				System.out.printf("%3s\t%6s\t%12s\t%,14d\t%10s\t%,16d\n",
						rs.getInt("employee_id"),
						rs.getString("name"),
						rs.getString("phone"),
						rs.getInt("salary"),
						rs.getString("title"),
						rs.getInt("performance"));
			}
			renderSys.printDivider(displaySize, true);
			renderSys.printEmptyLine(2);
			
			// 조회 종료 여부 입력받기
			while (true) {
				renderSys.printInputFormMessage("직원 조회가 성공적으로 이루어졌습니다. 확인을 마치셨으면 'Q'를 입력해주세요.");
				String quitMessage = read.nextLine();
				
				// 'q', 'Q', 'ㅂ' 을 입력할 경우 종료
				if (quitMessage.equals("q") || quitMessage.equals("Q") || quitMessage.equals("ㅂ")) {
					renderSys.printEmptyLine(2);
					break;
				} else {
					renderSys.printInvalidInput();
					renderSys.printEmptyLine(2);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			renderSys.printEmptyLine(2);
		} finally {
			ds.closeConnection(con);
		}
	}
	
	// 3. 기존 직원정보 수정하기
	public void updateEmployee(Connection con, Scanner read) {
		try {
			// 직원 정보를 수정에 성공할 때까지 반복
			while (true) {
				// 제목 출력
				renderSys.printTitle(displaySize, "직원 정보 수정 - 수정할 직원 선정");
				
				// 수정할 직원 아이디 입력받기
				renderSys.printInputFormMessage("수정할 직원의 아이디를 입력해주세요. ( Q : 이전으로 , R : 직원 ID 조회하기 )");
				String targetId = read.nextLine();
				renderSys.printEmptyLine(2);
				
				// 입력 확인
				if (targetId.equals("q") || targetId.equals("Q") || targetId.equals("ㅂ")) { return; }
				else if (targetId.equals("r") || targetId.equals("R") || targetId.equals("ㄱ")) {
					this.getEmployeeAll(con, read);
					continue;
				} else if (!this.isNumber(targetId)) {
					renderSys.printInvalidInput();
					renderSys.printEmptyLine(2);
					continue;
				}

				// 수정할 직원 정보 조회
				con = ds.getConnection();
				ResultSet targetEmp = empDao.getEmployee(con, targetId);
				
				// 수정할 직원 정보 조회 실패
				if (!targetEmp.next()) { 
					System.out.println("입력하신 ID의 직원이 존재하지 않습니다.");
					renderSys.printEmptyLine(2);
					continue;
				}
				
				// 선택한 직원의 수정 범위 선택하기
				renderSys.printSubTitle(displaySize, "직원 정보 수정 - 수정할 내용 선택");
				System.out.printf(" 1. 이름 수정하기 [현재값: %s]\n", targetEmp.getString("employee_name"));
				System.out.printf(" 2. 번호 수정하기 [현재값: %s]\n", targetEmp.getString("phone_number"));
				System.out.printf(" 3. 급여 수정하기 [현재값: %s]\n", targetEmp.getInt("salary"));
				System.out.printf(" 4. 직급 수정하기 [현재값: %s]\n", targetEmp.getString("title"));
				System.out.printf(" 5. 관리자 아이디 수정하기 [현재값: %s]\n", targetEmp.getInt("manager_id"));
				renderSys.printDivider(displaySize, true);
				
				while (true) {
					// 수정 범위 입력받기
					renderSys.printInputFormMessage("직원의 수정할 부분을 선택해주세요.");
					String part = read.nextLine();
					renderSys.printEmptyLine(2);
					
					int successUpdate = 0;  // 수정 성공 확인
					switch (part) {
					case "1":
						// 수정할 이름 입력받기
						renderSys.printInputFormMessage("수정할 이름을 입력해주세요.");
						String newName = read.nextLine();
						renderSys.printEmptyLine(2);
						
						// DB에서 직원의 이름 수정
						successUpdate = empDao.updateEmployeeName(con, targetId, newName);
						break;
					case "2":
						// 수정할 전화번호 입력받기
						renderSys.printInputFormMessage("수정할 전화번호를 입력해주세요.");
						String newPhone = read.nextLine();
						renderSys.printEmptyLine(2);
						
						// DB에서 직원의 전화번호 수정
						successUpdate = empDao.updateEmployeePhone(con, targetId, newPhone);
						break;
					case "3":
						// 수정할 급여 입력받기
						renderSys.printInputFormMessage("수정할 급여를 입력해주세요.");
						String newSalary = read.nextLine();
						renderSys.printEmptyLine(2);
						
						// DB에서 직원의 급여 수정
						successUpdate = empDao.updateEmployeeSalary(con, targetId, newSalary);
						break;
					case "4":
						// 수정할 직급 입력받기
						renderSys.printInputFormMessage("수정할 직급을 입력해주세요.");
						String newTitle = read.nextLine();
						renderSys.printEmptyLine(2);
						
						// DB에서 직원의 직급 수정
						successUpdate = empDao.updateEmployeeTitle(con, targetId, newTitle);
						break;
					case "5":
						// 수정할 관리자 아이디 입력받기
						renderSys.printInputFormMessage("수정할 관리자 아이디를 입력해주세요.");
						String newManagerId = read.nextLine();
						renderSys.printEmptyLine(2);
						
						// DB에서 직원의 관리자 아이디 수정
						successUpdate = empDao.updateEmployeeManager(con, targetId, newManagerId);
						break;
					default:
						// 잘못된 형식의 입력 알림
						renderSys.printInvalidInput();
						renderSys.printEmptyLine(2);
					}
					
					// 직원 정보 수정 성공시 탈출
					if (successUpdate == 1) {
						System.out.println("직원 정보 수정이 완료되었습니다.");
						renderSys.printEmptyLine(2);
						return;
					} else {
						System.out.println("직원 정보 수정에 실패했습니다.");
						renderSys.printEmptyLine(2);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			renderSys.printEmptyLine(2);
		} finally {
			ds.closeConnection(con);
		}
	}
	
	// 4. 퇴사한 직원 정보 삭제하기
	public void deleteEmployee(Connection con, Scanner read) {
		try {
			while (true) {
				// 제목 출력하기
				renderSys.printTitle(displaySize, "직원 정보 삭제 - 삭제할 직원 선정");
				
				// 삭제할 직원 아이디 입력받기
				renderSys.printInputFormMessage("삭제할 직원의 아이디를 입력해주세요. (Q: 이전으로, R: 직원 ID 조회하기)");
				String targetId = read.nextLine();
				renderSys.printEmptyLine(2);
				
				// 입력 확인
				if (targetId.equals("q") || targetId.equals("Q") || targetId.equals("ㅂ")) { return; }
				else if (targetId.equals("r") || targetId.equals("R") || targetId.equals("ㄱ")) {
					this.getEmployeeAll(con, read);
					continue;
				} else if (!this.isNumber(targetId)) {
					renderSys.printInvalidInput();
					renderSys.printEmptyLine(2);
					continue;
				}
				
				// DB에서 삭제 (직원의 근무일정과 직원정보 모두 삭제)
				con = ds.getConnection();
				empDao.deleteEmployeeSchedule(con, targetId);
				int successDelete = empDao.deleteEmployee(con, targetId);
				
				// 직원 정보 삭제 성공시 탈출
				if (successDelete == 1) {
					System.out.println("직원 정보 삭제가 완료되었습니다.");
					renderSys.printEmptyLine(2);
					return;
				} else {
					System.out.println("직원 정보 삭제에 실패했습니다.");
					renderSys.printEmptyLine(2);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			renderSys.printEmptyLine(2);
		} finally {
			ds.closeConnection(con);
		}
	}
}
