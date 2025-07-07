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

public class MenuDao {
	static RenderTitle title = new RenderTitle();
	static RenderOptions options = new RenderOptions();
	
	public static List<String> getMenuHeader() {
		DataSource ds = new DataSource();
		Connection con = null;
		
		List<String> columns = new ArrayList<>();
		
		try {
			con = ds.getConnection();
			
			String sql = "SELECT "
					+ "category_name, menu_name, price, is_soldout, iceable, description "
					+ "FROM menus WHERE 1=0";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
	        for (int i = 1; i <= columnCount; i++) {
	            columns.add(rsmd.getColumnName(i));
	        }
		    
			return columns;
//			for (int i = 1; i <= columnCount; i++) {
//				String columnName = rsmd.getColumnName(i);
//				
//				if (columnName.equals("CATEGORY_NAME")) {
//					columnName = String.format("%-12s", "카테고리");
//				} else if (columnName.equals("MENU_NAME")) {
//					columnName = String.format("%-14s", "메뉴이름");
//				} else if (columnName.equals("PRICE")) {
//					columnName = String.format("%-8s", "메뉴가격");
//				} else if (columnName.equals("IS_SOLDOUT")) {
//					columnName = String.format("%-10s", "품절여부");
//				} else if (columnName.equals("ICEABLE")) {
//					columnName = String.format("%-10s", "아이스");
//				} else if (columnName.equals("DESCRIPTION")) {
//					columnName = String.format("%-24s", "메뉴설명");
//				}
//				
//		    	columns += columnName; 
//		    }
//			
//			return columns;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
			
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public static void printMenuRows () {
		DataSource ds = new DataSource();
		Connection con = null;
		
		try {
			con = ds.getConnection();
			
			String sql = "SELECT "
					+ "category_name, menu_name, price, is_soldout, iceable, description "
					+ "FROM menus";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
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
			throw new RuntimeException();
			
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public static List<String> getParentCategories () {
		DataSource ds = new DataSource();
		Connection con = null;
		
		List<String> categories = new ArrayList<>();
		
		try {
			con = ds.getConnection();
			
			String sql = "SELECT category_name "
					+ "FROM categories "
					+ "WHERE parent_name IS NULL";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) categories.add(rs.getString("category_name"));
	
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public static List<String> getChildCategories (String parent) {
		DataSource ds = new DataSource();
		Connection con = null;
		
		List<String> subCategories = new ArrayList<>();
		
		try {
			con = ds.getConnection();
			
			String sql = "SELECT category_name FROM"
					+ "(SELECT category_name, parent_name, LEVEL "
					+ "FROM categories "
					+ "START WITH parent_name IS NULL "
					+ "CONNECT BY PRIOR category_name = parent_name) "
					+ "WHERE parent_name = ?";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setString(1, parent);
			ResultSet rs = stmt.executeQuery();
			

			while(rs.next()) subCategories.add(rs.getString("category_name"));
	
			return subCategories;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ds.closeConnection(con);
		}
	}
	
	public static ArrayList<String> getMenuNames() {
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
	
	public static void insertMenu (String subCategory, String menuName, int price, String description, int iceable) {
		DataSource ds = new DataSource();
		Connection con = null;

		
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			String sql = "INSERT INTO menus ("
					+ "category_name, menu_name, price, description, iceable, is_soldout"
					+ ") VALUES (?, ?, ?, ?, ?, 0)";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setString(1, subCategory);
			stmt.setString(2, menuName);
			stmt.setInt(3, price);
			stmt.setString(4, description);
			stmt.setInt(5, iceable);
			
			int insertMenuCount = stmt.executeUpdate();

			if (insertMenuCount == 0) {
				throw new RuntimeException();
			}
			
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ds.closeConnection(con);
			try {con.setAutoCommit(true);} catch (Exception e1) {}
		}
	}
	
	public static void deleteMenu (String tartgetMenu) {
		DataSource ds = new DataSource();
		Connection con = null;
		
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			String sql = "DELETE FROM menus WHERE menu_name = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, tartgetMenu);
			
			int deleteRow = stmt.executeUpdate();
			
			if (deleteRow == 0) {
				throw new RuntimeException();
			}
			
			con.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			try {con.rollback();} catch (Exception e2) {}
			throw new RuntimeException();
		} finally {
			try {con.setAutoCommit(true);} catch (Exception e3) {}
			ds.closeConnection(con);
		}
	}
	
	public static void updateMenu (String columnName, int updateNumber, String targetMenu) {
		DataSource ds = new DataSource();
		Connection con = null;
		
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			String sql = "UPDATE menus SET "+ columnName + " = ? WHERE menu_name = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, updateNumber);
			stmt.setString(2, targetMenu);
			
			int updateRow = stmt.executeUpdate();
			
			
			if (updateRow == 0) {
				System.out.println(targetMenu + " 메뉴 업데이트에 실패했습니다.");
				throw new RuntimeException();
			}
			
			con.commit();
			System.out.println("\u2714 " + targetMenu + " 메뉴가 성공적으로 업데이트 되었습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
			try {con.rollback();} catch (Exception e2) {}
			throw new RuntimeException();
		} finally {
			try {con.setAutoCommit(true);} catch (Exception e3) {}
			ds.closeConnection(con);
		}
	}
	
	public static void updateMenu (String columnName, String  updateString, String targetMenu) {
		DataSource ds = new DataSource();
		Connection con = null;
		
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			String sql = "UPDATE menus SET "+ columnName + " = ? WHERE menu_name = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, updateString);
			stmt.setString(2, targetMenu);
			
			int updateRow = stmt.executeUpdate();
			
			
			if (updateRow == 0) {
				System.out.println(targetMenu + " 메뉴 업데이트에 실패했습니다.");
				throw new RuntimeException();
			}
			
			con.commit();
			System.out.println("\u2714 " + targetMenu + " 메뉴가 성공적으로 업데이트 되었습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
			try {con.rollback();} catch (Exception e2) {}
			throw new RuntimeException();
		} finally {
			try {con.setAutoCommit(true);} catch (Exception e3) {}
			ds.closeConnection(con);
		}
	}	}
	




