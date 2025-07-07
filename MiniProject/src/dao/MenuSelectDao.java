package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.RenderOptions;
import utils.RenderTitle;

public class MenuSelectDao {
	static RenderTitle title = new RenderTitle();
	static RenderOptions options = new RenderOptions();
	
	public static void startSelect() throws Exception {
		DataSource ds = new DataSource();
		Connection con = null;
		
		Scanner scanner = new Scanner(System.in);

		List<String> subCategories = new ArrayList<>();
		List<String> categories = new ArrayList<>();
		
		while(true) {
			try {
				con = ds.getConnection();
				
				String sql = "SELECT "
						+ "category_name, menu_name, price, is_soldout, iceable, description "
						+ "FROM menus";
				
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				String columns = "";
				
				for (int i = 1; i <= columnCount; i++) {
					String columnName = rsmd.getColumnName(i);
					
					if (columnName.equals("CATEGORY_NAME")) {
						columnName = String.format("%-12s", "카테고리");
					} else if (columnName.equals("MENU_NAME")) {
						columnName = String.format("%-14s", "메뉴이름");
					} else if (columnName.equals("PRICE")) {
						columnName = String.format("%-8s", "메뉴가격");
					} else if (columnName.equals("IS_SOLDOUT")) {
						columnName = String.format("%-10s", "품절여부");
					} else if (columnName.equals("ICEABLE")) {
						columnName = String.format("%-10s", "아이스");
					} else if (columnName.equals("DESCRIPTION")) {
						columnName = String.format("%-24s", "메뉴설명");
					}
					
			    	columns += columnName; 
			    }
				
				title.renderTitle("메뉴 조회하기");
				System.out.println(columns);
				title.printLine();

				while(rs.next()) {
				    String categoryName = rs.getString("category_name");
				    String menuName = rs.getString("menu_name");
				    int price = rs.getInt("price");
				    int isSoldout = rs.getInt("is_soldout");
				    int iceable = rs.getInt("iceable");
				    String description = rs.getString("description");

				    System.out.printf("%-12s %-14s %-8d %-10s %-10s %-24s\n",
				            categoryName,
				            menuName,
				            price,
				            (isSoldout == 1 ? "Yes" : "No"),
				            (iceable == 1 ? "Yes" : "No"),
				            description);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ds.closeConnection(con);
				
				System.out.println("\n[엔터를 누르면 이전 메뉴로 돌아갑니다]");
				scanner.nextLine(); // 사용자 입력 대기
				
				break;
			}
		}
		
	}
	
	public static ArrayList<String> getMenuNames() throws Exception {
		DataSource ds = new DataSource();
		Connection con = null;
		RenderTitle title = new RenderTitle();
		ArrayList<String> menuList = new ArrayList<>();
		
		try {
			con = ds.getConnection();
			
			String sql = "SELECT menu_name FROM menus";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			title.printLine();
			System.out.println();
			System.out.println("\n※ 메뉴 정보를 모두 확인했습니다.");
			
			while(rs.next()) {
				String menuName = rs.getString("menu_name");
				menuList.add(menuName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ds.closeConnection(con);
		}
		
		return menuList;
		
	}
}
