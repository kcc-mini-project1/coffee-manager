package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import utils.RenderOptions;
import utils.RenderTitle;

public class MenuInsertDao {

	public static void startInsert() throws Exception {
		DataSource ds = new DataSource();
		Connection con = null;
		
		Scanner scanner = new Scanner(System.in);
		RenderTitle title = new RenderTitle();
		RenderOptions options = new RenderOptions();
		
		List<String> subCategories = new ArrayList<>();
		List<String> categories = new ArrayList<>();
		String parentCategory, subCategory, menuName, description;
		int price, iceable;
		
		while(true) {
		    parentCategory = null;
		    subCategory = null;
			System.out.println("1. 추가할 메뉴의 대분류를 선택하세요.");
			
			try {
				con = ds.getConnection();
				
				String sql = "SELECT category_name "
						+ "FROM categories "
						+ "WHERE parent_name IS NULL";
				
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) categories.add(rs.getString("category_name"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ds.closeConnection(con);
			}
		
			options.singleLine(categories);
			
			int index =  -1;
			
			try {
		        index = scanner.nextInt();
		    } catch (InputMismatchException e) {
				System.out.println("숫자를 입력해주세요");
				
		        scanner.nextLine();
		        continue;
		    }
			
			
			switch (index) {
				case 1, 2:
					parentCategory = categories.get(index - 1);
				
					System.out.println("\u2714 " + parentCategory + " 카테고리를 선택했습니다.\n");

					break;
				default:
					System.out.println("해당하는 카테고리가 존재하지 않습니다.");
	
					continue;
			}

			System.out.println("2. 추가할 메뉴의 소분류를 선택하세요.");
			
			try {
				con = ds.getConnection();
				
				String sql = "SELECT category_name FROM"
						+ "(SELECT category_name, parent_name, LEVEL "
						+ "FROM categories "
						+ "START WITH parent_name IS NULL "
						+ "CONNECT BY PRIOR category_name = parent_name) "
						+ "WHERE parent_name = ?";
				
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setString(1, parentCategory);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) subCategories.add(rs.getString("category_name"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ds.closeConnection(con);
			}
		
			options.singleLine(subCategories);
			
			index =  -1;
			
			try {
		        index = scanner.nextInt();
		    } catch (InputMismatchException e) {
				System.out.println("숫자를 입력해주세요");
				
		        scanner.nextLine();
		        continue;
		    }
			
			switch (index) {
				case 1, 2:
					subCategory = subCategories.get(index - 1);
				
					System.out.println("\u2714 " + subCategory + " 카테고리를 선택했습니다.\n");

					break;
				default:
					System.out.println("해당하는 카테고리가 존재하지 않습니다.");
	
					continue;
			}

			System.out.println("3. 추가하시려는 메뉴의 이름을 입력해주세요 ex) 아메리카노 > ");
			
			menuName = scanner.next();
			System.out.println("\u2714 메뉴 이름을" + menuName + " 로 설정했습니다.\n");
	
			
			System.out.println("메뉴의 가격을 입력하세요 ex) 4000 > ");
			
			try {
				price = scanner.nextInt();
				System.out.println("\u2714 가격을 " + price + " 로 설정했습니다.\n");
		    } catch (InputMismatchException e) {
				System.out.println("가격을 숫자로만 입력해주세요");
				
		        scanner.nextLine();
		        continue;
		    }
			
			System.out.println("메뉴의 설명을 50자내로 입력하세요. *개행 없이 입력* > ");
			
			scanner.nextLine();
			description = scanner.nextLine();
			System.out.println("\u2714 메뉴 설명: '"+ description +".'\n");
			
			System.out.println("얼음을 선택할 수 있는 메뉴인가요? (1: 가능, 0: 불가능) > ");
			try {
			    iceable = scanner.nextInt();
			    if (iceable != 0 && iceable != 1) throw new InputMismatchException();
			} catch (InputMismatchException e) {
			    System.out.println("0 또는 1만 입력해주세요.");
			    scanner.nextLine();
			    continue;
			}
			
			scanner.nextLine();
			
			while (true) {
                System.out.println("\n입력한 내용은 다음과 같습니다:");
                System.out.println("===============================================");
                System.out.printf("%-15s : %s\n", "상위 카테고리", parentCategory);
                System.out.printf("%-15s : %s\n", "하위 카테고리", subCategory);
                System.out.printf("%-15s : %s\n", "메뉴 이름", menuName);
                System.out.printf("%-15s : %d\n", "가격", price);
                System.out.printf("%-15s : %s\n", "아이스 가능", iceable == 1 ? "가능" : "불가능");
                System.out.printf("%-15s : %s\n", "설명", description);
                System.out.println("===============================================");

                System.out.print("이대로 추가할까요? (Y/N) > ");
                String confirm = scanner.nextLine().trim().toUpperCase();
                
                if (confirm.equals("Y")) {
                    break;
                } else if (confirm.equals("N")) {
                    System.out.println("수정할 항목 번호를 입력하세요:");
                    System.out.println("1. 상위 카테고리\n2. 하위 카테고리\n3. 메뉴 이름\n4. 가격\n5. 아이스 가능\n6. 설명");
                    int field = Integer.parseInt(scanner.nextLine());
                    switch (field) {
                        case 1:
                        	System.out.print("상위 카테고리: ");
                        	parentCategory = scanner.nextLine();
                        	
                        	break;
                        case 2:
                        	System.out.print("하위 카테고리: ");
                        	subCategory = scanner.nextLine();
                        	
                        	break;
                        case 3:
                        	System.out.print("메뉴 이름: ");
                        	menuName = scanner.nextLine();
                        	
                        	break;
                        case 4:
                        	System.out.print("가격: ");
                        	price = Integer.parseInt(scanner.nextLine());
                        	
                        	break;
                        case 5:
                        	System.out.print("아이스 가능 (1/0): ");
                        	iceable = Integer.parseInt(scanner.nextLine());
                        	
                        	break;
                        case 6:
                        	System.out.print("설명: ");
                        	description = scanner.nextLine();
                        	
                        	break;
                        default:
                        	System.out.println("잘못된 번호입니다.");
                        	
                        	break;
                    }
                } else {
                    System.out.println("Y 또는 N만 입력해주세요.");
                }
            }
			
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
				} else {
					System.out.println("메뉴가 정상적으로 추가되었습니다.");
					con.commit();
					break;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				
				System.out.println("메뉴 추가에 실패했습니다.");
			} finally {
				try {con.setAutoCommit(true);} catch (Exception e1) {}
			}
		}
		
	}
}

