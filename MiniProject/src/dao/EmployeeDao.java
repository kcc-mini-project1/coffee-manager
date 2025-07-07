package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeDao {
	DataSource ds = new DataSource();
	Scanner read = new Scanner(System.in);
	
	public void selectFunction() {
		System.out.print(
				"\n\n========================================\n"
				+ "=             직원 관리 시스템             =\n"
				+ "========================================\n"
				+ "1. 직원 추가하기\n"
				+ "2. 직원 조회하기\n"
				+ "3. 직원 정보수정하기\n"
				+ "4. 직원 삭제하기\n"
				+ "q. 이전으로\n"
				+ "========================================\n"
				+ ">>> ");
	}
	
	public void insertEmployee() {
		Connection con = null;
		
		try {
			String inputName;
			String inputPhoneNumber;
			String inputTitle;
			String inputSalary;
			
			while (true) {
				System.out.print(
						"\n\n========================================\n"
						+ "=          추가할 직원 정보 입력하기        =\n"
						+ "========================================");
				System.out.print("\n직원의 이름을 입력하세요.\n>>> ");
				inputName = read.nextLine();
				System.out.print("\n직원의 전화번호를 입력하세요.\n>>> ");
				inputPhoneNumber = read.nextLine();
				System.out.print("\n직원의 직급을 입력하세요.\n>>> ");
				inputTitle = read.nextLine();
				System.out.print("\n직원의 급여를 입력하세요.\n>>> ");
				inputSalary = read.nextLine();
				
				System.out.println("\n\n==============입력한 직원정보==============");
				System.out.printf("%-6s: %s\n", "이름", inputName);
				System.out.printf("%-5s: %s\n", "전화번호", inputPhoneNumber);
				System.out.printf("%-6s: %s\n", "직급", inputTitle);
				System.out.printf("%-6s: %s\n", "급여", inputSalary);
				System.out.println("========================================");
				
				System.out.print("\n입력하신 정보가 맞으시면 'Y'를 입력해주세요.\n>>> ");
				String inputOK = read.nextLine();
				if (inputOK.equals("y")) { break; }
				else if (inputOK.equals("y")) { break; }
				else { System.out.println("직원 정보를 다시 입력해주세요."); }
			}
			
			con = ds.getConnection();
			
			String sql = "INSERT INTO employees "
					+ "(employee_id, employee_name, phone_number, title, salary) "
					+ "VALUES (emp_seq.NEXTVAL, ?, ?, ?, ?)";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, inputName);
			stmt.setString(2, inputPhoneNumber);
			stmt.setString(3, inputTitle);
			stmt.setInt(4, Integer.parseInt(inputSalary));
			
			int insertRowSize = stmt.executeUpdate();
			if (insertRowSize == 1) {
				System.out.println("직원정보가 입력이 완료되었습니다.");				
			} else {
				System.out.println("에러 던지기 기능 만들어야함.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public void getEmployee() {
		Connection con = null;
		
		try {
			con = ds.getConnection();
			
			String sql = "SELECT "
					+ "e.employee_id AS employee_id, e.employee_name AS name, "
					+ "e.phone_number AS phone, title, salary, manager_id, "
					+ "NVL(rst.total_price, 0) AS performance "
					+ "FROM employees e LEFT JOIN ("
					+ "SELECT s.employee_id, SUM(m.price) AS total_price "
					+ "FROM schedule s LEFT JOIN orders o "
					+ "ON o.order_date BETWEEN s.start_time AND s.end_time "
					+ "LEFT JOIN menus m ON o.menu_name = m.menu_name "
					+ "WHERE TRUNC(s.start_time, 'MM') = TRUNC(SYSDATE, 'MM') "
					+ "GROUP BY s.employee_id) rst "
					+ "ON e.employee_id = rst.employee_id "
					+ "ORDER BY e.employee_id";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("\n전체 직원을 조회합니다.\n");
			System.out.printf("%3s%6s%9s%12s%7s%10s\n",
					"ID", "이름", "전화번호", "급여", "직급", "실적");

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
				System.out.print("확인을 마치셨으면 'Q'를 입력해주세요\n>>> ");
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
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public void updateEmployee() {
		Connection con = null;
		
		try {
			con = ds.getConnection();
			
			boolean loop = true;
			while (loop) {
				System.out.print(
						"\n\n========================================\n"
						+ "=             직원 정보 수정하기           =\n"
						+ "========================================");
				System.out.print("\n수정할 직원의 아이디를 입력하세요.( q : 이전으로 , r : 직원id 조회하기 )\n>>> ");
				String inputID = read.nextLine();
				
				if (inputID.equals("q") || inputID.equals("Q")) { return; }
				if (inputID.equals("r") || inputID.equals("R")) {
					String sqlReadAll = "SELECT "
							+ "employee_id, employee_name, phone_number, "
							+ "title, salary, manager_id "
							+ "FROM employees "
							+ "ORDER BY employee_id";
					
					PreparedStatement stmt = con.prepareStatement(sqlReadAll);
					ResultSet rs1 = stmt.executeQuery();
					
					System.out.println("\n전체 직원을 조회합니다.\n");
					System.out.printf("%3s%6s%9s%12s%7s\n",
							"ID", "이름", "전화번호", "급여", "직급");
	
					while (rs1.next()) {
						System.out.printf("%3s%6s%14s%10s%6s\n",
								rs1.getInt("employee_id"),
								rs1.getString("employee_name"),
								rs1.getString("phone_number"),
								rs1.getInt("salary"),
								rs1.getString("title"));
					}
					continue;
				}
				loop = false;
				int successUpdate = 0;
				
				String sqlRead = "SELECT "
						+ "employee_id, employee_name, phone_number, "
						+ "title, salary, manager_id "
						+ "FROM employees "
						+ "WHERE employee_id = ?";
				PreparedStatement stmt2 = con.prepareStatement(sqlRead);
				stmt2.setInt(1, Integer.parseInt(inputID));
				ResultSet rs2 = stmt2.executeQuery();
				
				if (!rs2.next()) { System.out.println("TODO: 에러처리");}
				System.out.println("\n\n==============수정할 직원정보==============");
				System.out.printf("1. 이름 수정하기 (현재값: %s)\n", rs2.getString("employee_name"));
				System.out.printf("2. 번호 수정하기 (현재값: %s)\n", rs2.getString("phone_number"));
				System.out.printf("3. 급여 수정하기 (현재값: %s)\n", rs2.getInt("salary"));
				System.out.printf("4. 직급 수정하기 (현재값: %s)\n", rs2.getString("title"));
				System.out.printf("5. 관리자 아이디 수정하기 (현재값: %s)\n", rs2.getInt("manager_id"));
				System.out.print("========================================\n>>> ");
				
				String part = read.nextLine();
				
				boolean escape = true;
				while (escape) {	
					escape = false;
					switch (part) {
					case "1":
						String sqlName = "UPDATE employees "
								+ "SET employee_name = ? "
								+ "WHERE employee_id = ?";
						PreparedStatement stmt3 = con.prepareStatement(sqlName);
						System.out.print("수정할 이름을 입력해주세요.\n>>> ");
						stmt3.setString(1,  read.nextLine());
						stmt3.setInt(2,  Integer.parseInt(inputID));
						
						successUpdate = stmt3.executeUpdate();
						if (successUpdate == 1) {
							System.out.println("수정 성공");
						} else {
							System.out.println("수정에 실패했습니다. 다시 시도해주세요.");
						}
						break;
					case "2":
						String sqlPhone = "UPDATE employees "
								+ "SET phone_number = ? "
								+ "WHERE employee_id = ?";
						PreparedStatement stmt4 = con.prepareStatement(sqlPhone);
						System.out.print("수정할 전화번호를 입력해주세요.\n>>> ");
						stmt4.setString(1,  read.nextLine());
						stmt4.setInt(2,  Integer.parseInt(inputID));
						
						successUpdate = stmt4.executeUpdate();
						if (successUpdate == 1) {
							System.out.println("수정 성공");
						} else {
							System.out.println("수정에 실패했습니다. 다시 시도해주세요.");
						}
						break;
					case "3":
						String sqlSalary = "UPDATE employees "
								+ "SET salary = ? "
								+ "WHERE employee_id = ?";
						PreparedStatement stmt5 = con.prepareStatement(sqlSalary);
						System.out.print("수정할 급여를 입력해주세요.\n>>> ");
						stmt5.setInt(1,  Integer.parseInt(read.nextLine()));
						stmt5.setInt(2,  Integer.parseInt(inputID));
						
						successUpdate = stmt5.executeUpdate();
						if (successUpdate == 1) {
							System.out.println("수정 성공");
						} else {
							System.out.println("수정에 실패했습니다. 다시 시도해주세요.");
						}
						break;
					case "4":
						String sqlTitle = "UPDATE employees "
								+ "SET title = ? "
								+ "WHERE employee_id = ?";
						PreparedStatement stmt6 = con.prepareStatement(sqlTitle);
						System.out.print("수정할 직급을 입력해주세요.\n>>> ");
						stmt6.setString(1,  read.nextLine());
						stmt6.setInt(2,  Integer.parseInt(inputID));
						
						successUpdate = stmt6.executeUpdate();
						if (successUpdate == 1) {
							System.out.println("수정 성공");
						} else {
							System.out.println("수정에 실패했습니다. 다시 시도해주세요.");
						}
						break;
					case "5":
						String sqlManager = "UPDATE employees "
								+ "SET manager_id = ? "
								+ "WHERE employee_id = ?";
						PreparedStatement stmt7 = con.prepareStatement(sqlManager);
						System.out.print("수정할 관리자 아이디를 입력해주세요.\n>>> ");
						stmt7.setString(1,  read.nextLine());
						stmt7.setInt(2,  Integer.parseInt(inputID));
						
						successUpdate = stmt7.executeUpdate();
						if (successUpdate == 1) {
							System.out.println("수정 성공");
						} else {
							System.out.println("수정에 실패했습니다. 다시 시도해주세요.");
						}
						break;
					default:
						System.out.println("입력이 잘못되었습니다.");
						escape = false;
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public void deleteEmployee() {
		Connection con = null;
		
		try {
			con = ds.getConnection();
			
			boolean loop = true;
			while (loop) {
				System.out.print("\n\n삭제할 직원의 아이디를 입력해주세요.( q : 이전으로 , r : 직원id 조회하기 )\n>>> ");
				String deleteId = read.nextLine();
				if (deleteId.equals("q") || deleteId.equals("Q")) { return; }
				if (deleteId.equals("r") || deleteId.equals("R")) {
					String sqlReadAll = "SELECT "
							+ "employee_id, employee_name, phone_number, "
							+ "title, salary, manager_id "
							+ "FROM employees "
							+ "ORDER BY employee_id";
					
					PreparedStatement stmt1 = con.prepareStatement(sqlReadAll);
					ResultSet rs1 = stmt1.executeQuery();
					
					System.out.println("\n전체 직원을 조회합니다.\n");
					System.out.printf("%3s%6s%9s%12s%7s\n",
							"ID", "이름", "전화번호", "급여", "직급");
	
					while (rs1.next()) {
						System.out.printf("%3s%6s%14s%10s%6s\n",
								rs1.getInt("employee_id"),
								rs1.getString("employee_name"),
								rs1.getString("phone_number"),
								rs1.getInt("salary"),
								rs1.getString("title"));
					}
					continue;
				}
				loop = false;
				String sql_schedule = "DELETE FROM schedule WHERE employee_id = ?";
				PreparedStatement stmt2 = con.prepareStatement(sql_schedule);
				stmt2.setInt(1,  Integer.parseInt(deleteId));
				int deleteEmpCount2 = stmt2.executeUpdate();
				
				
				String sql2 = "DELETE FROM employees WHERE employee_id = ?";
				PreparedStatement stmt3 = con.prepareStatement(sql2);
				stmt3.setInt(1, Integer.parseInt(deleteId));
				int deleteEmpCount = stmt3.executeUpdate();
				
				if (deleteEmpCount != 1) { System.out.println("TODO: 에러"); }
				System.out.println("삭제가 완료되었습니다.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ds.closeConnection(con);
		}
	}
}
