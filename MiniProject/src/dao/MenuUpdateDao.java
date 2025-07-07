package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import utils.RenderOptions;
import utils.RenderTitle;

public class MenuUpdateDao {
	public static void startUpdate() throws Exception {
		DataSource ds = new DataSource();
		Connection con = null;
		RenderTitle title = new RenderTitle();
		RenderOptions options = new RenderOptions();
		Scanner scanner = new Scanner(System.in);
		MenuSelectDao searchMenu = new MenuSelectDao();
		ArrayList<String> menuList = searchMenu.getMenuNames();
		
		while(true) {
			System.out.println("1. 수정할 메뉴의 번호를 입력하세요");
			title.printLine();
			options.singleLine(menuList);
			title.printLine();
			System.out.println();

			
			int targetIndex;
			String targetMenu;
			
			try {
				targetIndex = scanner.nextInt();
				
				if (targetIndex < 1 || targetIndex > menuList.size()) {
					System.out.println("존재하는 메뉴번호가 아닙니다.");
					
					continue;
				}
				
				targetMenu = menuList.get(targetIndex - 1);
			} catch (InputMismatchException e) {
				System.out.println("수정할 메뉴의 번호를 숫자로 입력해주세요");
				
		        scanner.nextLine();
		        continue;
		    }

			System.out.println("\u2754 메뉴 " + targetMenu + " 을 수정하시겠습니까? ex) 예(Y) / 아니오(N) > ");
			
			char answer = scanner.next().toUpperCase().charAt(0);
			
			switch (answer) {
				case 'Y':
					try {
						con = ds.getConnection();
						con.setAutoCommit(false);
						
						String sql = "SELECT "
								+ "category_name, "
								+ "menu_name, "
								+ "price, "
								+ "description, "
								+ "is_soldout, "
								+ "iceable "
								+ "FROM menus "
								+ "WHERE menu_name = ?";
			
						PreparedStatement stmt = con.prepareStatement(sql);
						stmt.setString(1, targetMenu);
						
						ResultSet rs = stmt.executeQuery();
						
						if (!rs.next()) {
							System.out.println("해당 메뉴가 존재하지 않습니다.");
							break;
						}
						
						ResultSetMetaData rsmd = rs.getMetaData();
						int columnCount = rsmd.getColumnCount();
						ArrayList<String> columnList = new ArrayList<>();
						
						for (int i = 1; i <= columnCount; i++) {
							columnList.add(rsmd.getColumnName(i));
					    }
						
						options.singleLine(columnList);
						title.printLine();
						
						System.out.println("2. 해당 메뉴의 수정할 내용을 선택하세요.");

						int columnIndex;
						String columnName;
						
						try {
							columnIndex = scanner.nextInt();
							scanner.nextLine();
							
							if (columnIndex < 1 || columnIndex > columnList.size()) {
								System.out.println("존재하지 않는 컬럼입니다.");
								
								continue;
							}
							columnName = columnList.get(columnIndex - 1);
							System.out.println("\u2714 메뉴의 " + columnName + " 을 수정합니다.\n");
					    } catch (InputMismatchException e) {
							System.out.println("숫자로만 입력해주세요");
							
					        scanner.nextLine();
					        continue;
					    }
						
						System.out.println("3. " + columnName +"의 변경할 값을 입력해주세요.\n");
						System.out.println("이전 " + columnName + "값: " + rs.getString(columnName));

						String updateString = null;
						Integer updateNumber = null;
						
						List<String> stringColumns = Arrays.asList("CATEGORY_NAME", "MENU_NAME", "DESCRIPTION");
						
						String sql1 = "UPDATE menus SET "+ columnName + " = ? WHERE menu_name = ?";
						stmt = con.prepareStatement(sql1);
						if (stringColumns.contains(columnName.toUpperCase())) {
							updateString = scanner.nextLine();
							stmt.setString(1, updateString);
						} else {
							System.out.println("원하시는 숫자를 입력해주세요");
							if (columnName.equals("IS_SOLDOUT")) {
								System.out.print("0: 판매 가능 | 1: 메뉴 품절 > ");
							} else if (columnName.equals("ICEABLE")){
								System.out.print("0: 아이스 메뉴 가능 | 0: 핫 메뉴만 가능 > ");
							}
							
							updateNumber = scanner.nextInt();
							scanner.nextLine();
							stmt.setInt(1, updateNumber);
						}
						
						stmt.setString(2, targetMenu);

						System.out.println("\n[수정 내용 확인]");
						System.out.printf("%-15s : %s\n", "메뉴 이름", targetMenu);
						System.out.printf("%-15s : %s\n", columnName, 
							updateString != null ? updateString : String.valueOf(updateNumber));
						System.out.println("===============================================");
						System.out.print("위 내용으로 수정할까요? (Y/N): ");
						
						String confirm = scanner.nextLine().trim().toUpperCase();
						
						if (!confirm.equals("Y")) {
							System.out.println("수정을 취소했습니다.");
							continue;
						}

						int updateRow = stmt.executeUpdate();
						if (updateRow == 0) {
							System.out.println(targetMenu + " 메뉴 업데이트에 실패했습니다.");
							throw new RuntimeException();
						}

						System.out.println("\u2714 " + targetMenu + " 메뉴가 성공적으로 업데이트 되었습니다.");
						con.commit();
						
						System.out.print("계속 수정하시겠습니까? (Y/N): ");
						
						char next = scanner.nextLine().trim().toUpperCase().charAt(0);
						if (next != 'Y') {
							return;
						}
					} catch (SQLException e) {
						System.out.println(e.getMessage());
						try {con.rollback();} catch (Exception e2) {}
					} finally {
						try {con.setAutoCommit(true);} catch (Exception e3) {}
						ds.closeConnection(con);
					}
					
					break;
				case 'N':
					System.out.println("\u2714 메뉴 " + targetMenu + " 수정을 취소합니다.");
					System.out.println();
					
					break;
				default:
					System.out.println("Y 또는 N 중에서 입력해주세요.");
					
					break;
			}
		}
		
	}
}
