package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import utils.RenderOptions;
import utils.RenderTitle;

public class MenuDeleteDao {
	static RenderTitle title = new RenderTitle();
	static RenderOptions options = new RenderOptions();
	
	public static void startRemove() throws Exception {
		MenuSelectDao searchMenu = new MenuSelectDao();

		DataSource ds = new DataSource();
		Connection con = null;
		
		Scanner scanner = new Scanner(System.in);

		while(true) {
			ArrayList<String> menuList = searchMenu.getMenuNames();
			options.singleLine(menuList);
			System.out.println();
			System.out.println("삭제할 메뉴의 번호를 입력하세요");
			
			int targetIndex;
			String tartgetMenu;
			
			try {
				targetIndex = scanner.nextInt();
				
				if (targetIndex < 1 || targetIndex > menuList.size()) {
					System.out.println("존재하는 메뉴번호가 아닙니다.");
					
					continue;
				}
				
				tartgetMenu = menuList.get(targetIndex - 1);
			} catch (InputMismatchException e) {
				System.out.println("삭제할 메뉴의 번호를 숫자로 입력해주세요");
				
		        scanner.nextLine();
		        continue;
		    }

			tartgetMenu = menuList.get(targetIndex - 1);
			System.out.println("\u2754 메뉴 " + tartgetMenu + " 을 삭제하시겠습니까? ex) 동의(Y) /미동의 (N) > ");
			
			char answer = scanner.next().toUpperCase().charAt(0);
			
			switch (answer) {
				case 'Y':
					try {
						con = ds.getConnection();
						con.setAutoCommit(false);
						
						String sql = "DELETE FROM menus WHERE menu_name = ?";

						PreparedStatement stmt = con.prepareStatement(sql);
						stmt.setString(1, tartgetMenu);
						
						int deleteRow = stmt.executeUpdate();
						
						if (deleteRow == 0) {
							System.out.println(tartgetMenu + "메뉴 삭제에 실패했습니다.");
							throw new RuntimeException();
						}
						
						System.out.println("\u2714" + tartgetMenu + " 메뉴가 성공적으로 삭제되었습니다.");
						con.commit();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
						try {con.rollback();} catch (Exception e2) {}
					} finally {
						try {con.setAutoCommit(true);} catch (Exception e3) {}
						ds.closeConnection(con);
					}
					
					break;
				case 'N':
					System.out.println("\u2714 메뉴 " + tartgetMenu + " 삭제를 취소합니다.");
					break;
				default:
					break;
			}
			
			break;
		}
		
	}
}
