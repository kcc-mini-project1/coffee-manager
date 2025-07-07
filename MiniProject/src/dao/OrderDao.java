package dao;

import kyeongjun.RenderTitle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderDao {
    private Scanner sc = new Scanner(System.in);
    private DataSource ds = new DataSource();
    
    public void start() {
        boolean run = true;
        while (run) {
        	System.out.println();
        	RenderTitle.renderTitle("주문 관리 시스템");
        	System.out.println("1. 메뉴판 보기");
        	System.out.println("2. 메뉴 주문하기");
            System.out.println("3. 주문내역 확인하기");
            System.out.println("4. 주문 취소하기");
            System.out.println("5. 포인트 확인하기");
            System.out.println("q. 이전으로");
            System.out.print(">> ");
            
            String input = sc.nextLine();

            switch (input) {
                case "1":
                    menuBoard();
                    break;
                case "2":
                    insertOrder();
                    break;
                case "3":
                	selectOrderList();
                    break;
                case "4":
                	deleteOrder();
                    break;
                case "5":
                    checkPoint();
                    break;
                case "q":
                    run = false;
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
    
    
    //1. 메뉴판
//    private void menuBoard() {
//    	try(Connection con = ds.getConnection()){
//        	String sql = "SELECT   c.parent_name  AS parentName,"
//    		+ "				m.category_name 	AS categoryName,"
//    		+ "				m.menu_name 		AS menuName,"
//    		+ "				m.price 			AS price,"
//    		+ "				m.description 		AS description, "
//        		+ "				m.is_soldout 		AS isSoldout,"
//        		+ "				m.iceable 			AS iceable "
//			   + "FROM menus m join categories c "
//			   + "on m.category_name = c.category_name"; 
//             + "ORDER BY parent_name desc,m.category_name ";
//    		PreparedStatement stmt = con.prepareStatement(sql);
//          ResultSet rs = stmt.executeQuery();
//          System.out.println("~.~.~.~.~.~.~.~.~.~~.~.~.~.~~.~.~.~.~.~.~.~.~.~.~.~.~.~.~~.~.~.~.~~.~.~.~.~~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~~.~.~.~.~.~.~.~.~.~.~.~.~.~.~..~.~.~.~.~.~.~");
//          System.out.println("                                                   			 메뉴판                                                                                     ");
//          System.out.println("~.~.~.~.~.~.~.~.~.~~.~.~.~.~~.~.~.~.~.~.~.~.~.~.~.~.~.~.~~.~.~.~.~~.~.~.~.~~.~.~.~.~~.~.~.~.~.~.~.~.~.~.~.~.~.~.~..~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~");
//	        System.out.println("큰 카테고리		\t 작은 카테고리	\t 메뉴명		\t 가격			\t 메뉴설명		\t솔드아웃		\t아이스 가능 음료");
//	        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
//	        while(rs.next()) {
//          System.out.printf("%s	\t	%s	\t	%s	\t	%d	\t	%s	\t	%s	\t	%s\n",
//          		rs.getString("parentName"),
//          		rs.getString("categoryName"),
//          		rs.getString("menuName"),
//          		rs.getInt("price"),
//                rs.getString("description"),
//                (rs.getInt("isSoldout") == 1) ? "품절" : "주문가능",
//                (rs.getInt("iceable") == 1) ? "가능" : "불가능");
//          }
//    	} catch (SQLException e) {
//    		System.out.println("메뉴판 출력 오류");
//			e.printStackTrace();
//		}
//    }
    
    
    
    //1. GPT가 알랴준 - 메뉴판 한글 나열 예쁘게 보이기...?ㅎㅎ
    private void menuBoard() {
        try (Connection con = ds.getConnection()) {
            String sql = "SELECT c.parent_name AS parentName, "
                    + "       m.category_name AS categoryName, "
                    + "       m.menu_name AS menuName, "
                    + "       m.price AS price, "
                    + "       m.description AS description, "
                    + "       m.is_soldout AS isSoldout, "
                    + "       m.iceable AS iceable "
                    + "FROM menus m "
                    + "JOIN categories c ON m.category_name = c.category_name  "
                    + "ORDER BY parent_name desc,m.category_name ";

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            String line = "─".repeat(125);
            System.out.println(line);
            System.out.printf("%62s%-62s\n", "", "메뉴판");
            System.out.println(line);
            System.out.printf(" %-10s  %-10s  %-14s  %-6s   %-8s  %-8s  %-30s\n",
                    "큰카테고리", "작은카테고리", "메뉴명", "가격",  "솔드아웃", "아이스" ,"메뉴설명 ");
            System.out.println(line);

            while (rs.next()) {
                String parent = padKorean(rs.getString("parentName"), 10);
                String category = padKorean(rs.getString("categoryName"), 10);
                String name = padKorean(rs.getString("menuName"), 14);
                String desc = padKorean(rs.getString("description"), 30);
                int price = rs.getInt("price");
                String soldout = padKorean((rs.getInt("isSoldout") == 1) ? "솔드아웃" : "주문가능", 8);
                String ice = padKorean((rs.getInt("iceable") == 1) ? "O" : "X", 8);

                System.out.printf(" %-10s  %-13s  %-14s  %-6d  %-8s  %-8s  %-30s \n",
                        parent, category, name, price, soldout, ice, desc);
            }

            System.out.println(line);
        } catch (SQLException e) {
            System.out.println("메뉴판 띄우기 오류");
            e.printStackTrace();
        }
    }

    
    
    private String padKorean(String text, int width) {
        int len = 0;
        for (char ch : text.toCharArray()) {
            // 한글은 2칸, 나머지는 1칸
            len += (Character.toString(ch).matches("[가-힣]")) ? 2 : 1;
        }
        int padding = width - len;
        if (padding > 0) {
            text += " ".repeat(padding);
        }
        return text;
    }

    
    
    
    
    
    
    
    

	//02.주문하기
    private void insertOrder() {
        try (Connection con = ds.getConnection()) {
            System.out.println();
            RenderTitle.renderTitle(" 메뉴 주문하기 - 카테고리를 선택하세요. ");
            System.out.print("[큰 카테고리] 1. Drink    2. Desert\n  >> ");
            String input = sc.nextLine();

            String bigCategoryName = switch (input) {
                case "1" -> "Drink";
                case "2" -> "Desert";
                default -> "잘못된 입력입니다." ;
            };

            System.out.println();
            RenderTitle.renderTitle(" 메뉴 주문하기 - " + bigCategoryName + "의 카테고리를 선택하세요. ");

            String smallCategoryName = null;
            if (bigCategoryName.equals("Drink")) {
                System.out.print("[Drink] 1. Coffee    2. Tea\n    >> ");
                String input2 = sc.nextLine();
                smallCategoryName = switch (input2) {
                    case "1" -> "Coffee";
                    case "2" -> "Tea";
                    default -> "잘못된 입력입니다." ;
                };
            } else {
                System.out.print("[Desert] 1. Cake    2. Bread\n    >> ");
                String input2 = sc.nextLine();
                smallCategoryName = switch (input2) {
                    case "1" -> "Cake";
                    case "2" -> "Bread";
                    default -> "잘못된 입력입니다." ;
                };
            }

            // 주문하기 전에 입력할 값( 메뉴들 리스트 뽑기 ) 추가
            String menuSql = "SELECT menu_name, price FROM menus WHERE category_name = ? AND is_soldout = 0";
            PreparedStatement menuStmt = con.prepareStatement(menuSql);
            menuStmt.setString(1, smallCategoryName);
            ResultSet menuRs = menuStmt.executeQuery();

            System.out.println("\n ===========[ 선택 가능한 메뉴 목록 - " + smallCategoryName + " ]===============");
            List<String> availableMenus = new ArrayList<>();
            while (menuRs.next()) {
                String menu = menuRs.getString("menu_name");
                int price = menuRs.getInt("price");
                System.out.println("- " + menu + " (" + price + "원)");
                availableMenus.add(menu.toLowerCase());
            }
            
            RenderTitle.renderTitle(" 메뉴 주문하기 ");
            System.out.print("\n위 목록 중 메뉴명을 입력하세요. (q: 주문종료) >> ");
            String menuName = sc.nextLine();
            if (menuName.equalsIgnoreCase("q")) return;
            if (!availableMenus.contains(menuName.toLowerCase())) {
                System.out.println("선택 목록에 없는 메뉴입니다. 주문을 종료합니다.");
                return;
            }
            
            System.out.println();
            System.out.print("전화번호를 입력하세요. (q: 주문종료 / 엔터: 비회원) >> ");
            String customerId = sc.nextLine();
            if (customerId.equalsIgnoreCase("q")) return;
            if (customerId.isBlank()) customerId = "비회원";

            
            boolean useCoupon = false;
            if (!customerId.equals("비회원")) {
                String couponSql = "SELECT coupon FROM members WHERE customer_id = ?";
                PreparedStatement cpStmt = con.prepareStatement(couponSql);
                cpStmt.setString(1, customerId);
                ResultSet cpRs = cpStmt.executeQuery();
                if (cpRs.next() && cpRs.getInt("coupon") > 0) {
                    System.out.print("사용 가능한 쿠폰이 있습니다. 사용하시겠습니까? (y/n) >> ");
                    useCoupon = sc.nextLine().equalsIgnoreCase("y");
                }
            }

            System.out.print("요청사항을 입력하세요. (없으면 Enter) >> ");
            String request = sc.nextLine();

            System.out.print("ICE로 주문하시겠습니까? (y: ICE / n: HOT) >> ");
            boolean isIce = sc.nextLine().equalsIgnoreCase("y");

            
            
            
            System.out.println("\n입력하신 주문 정보 확인:");
            System.out.println("회원번호: " + customerId);
            System.out.println("메뉴명: " + menuName);
            System.out.println("요청사항: " + request);
            System.out.println("ICE 여부: " + (isIce ? "ICE" : "HOT"));
            System.out.println("쿠폰 사용: " + (useCoupon ? "사용" : "사용 안 함"));
            System.out.print("위 정보로 주문하시겠습니까? (y/n) >> ");
            String confirm = sc.nextLine();
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("주문이 취소되었습니다.");
                return;
            }

            
            
            String orderSql = "INSERT INTO orders (order_id, order_date, customer_id, menu_name, request, is_ice, use_coupon) " +
                              "VALUES (order_seq.nextval, SYSDATE, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(orderSql);
            stmt.setString(1, customerId);
            stmt.setString(2, menuName);
            stmt.setString(3, request);
            stmt.setInt(4, isIce ? 1 : 0);
            stmt.setInt(5, useCoupon ? 1 : 0);
            stmt.executeUpdate();

            
            
            if (useCoupon && !customerId.equals("비회원")) {
                String updateCouponSql = "UPDATE members SET coupon = coupon - 1 WHERE customer_id = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateCouponSql);
                updateStmt.setString(1, customerId);
                updateStmt.executeUpdate();
                System.out.println("쿠폰 1장이 사용되었습니다.");
            }

            System.out.println("주문이 완료되었습니다.");

        } catch (SQLException e) {
            System.out.println("주문 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }


	// 3. 주문내역 확인하기
    private void selectOrderList() {
        try (Connection con = ds.getConnection()) {
            String sql = "SELECT order_id	    AS orderId,"
					   + "       customer_id 	AS customerId,"
					   + "       menu_name 	    AS menuName "
					   + "FROM orders";

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            RenderTitle.renderTitle("주문내역 확인하기");
            System.out.println("순서 \t 주문번호 \t 회원번호 \t \t 주문한 메뉴");
            System.out.println("----------------------------------------------------------------------------------");
            
            int i = 1;
            while (rs.next()) {
                System.out.printf("%d \t %d \t %s \t %s \n", 
                		i++,
                        rs.getInt("orderId"),
                        rs.getString("customerId"),
                        rs.getString("menuName"));
            }
        } catch (SQLException e) {
            System.out.println("주문 내역 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
    
    
    
    
 // 4. 주문 취소하기 
    private void deleteOrder() {
        try (Connection con = ds.getConnection()) {
            boolean run = true;
            
            
            // 주문 내역 먼저 한번 보여주기 추가
            String listSql = "SELECT order_id, customer_id, menu_name, order_date FROM orders ORDER BY order_date DESC FETCH FIRST 10 ROWS ONLY";
            PreparedStatement listStmt = con.prepareStatement(listSql);
            ResultSet listRs = listStmt.executeQuery();

            System.out.println("=============[ 최근 주문 내역 리스트 ]===============");
            System.out.println("주문번호	\t	회원번호	\t	메뉴명	\t	주문일자 \n");
            while (listRs.next()) {
                System.out.printf("%d	\t	%s	\t	%s	\t	%s\n",
                    listRs.getInt("order_id"),
                    listRs.getString("customer_id"),
                    listRs.getString("menu_name"),
                    listRs.getTimestamp("order_date").toString()
                );
            }
            System.out.println();
            
       
            
            while (run) {
                RenderTitle.renderTitle("주문 취소하기");
                System.out.print("취소할 주문번호를 입력해주세요.(이전으로 : q) >> ");
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("q")) {
                    run = false; 
                    break;
                }

                int orderId;
                try {
                    orderId = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                    continue; 
                }

                
                String selectSql = "SELECT order_id, order_date, customer_id, menu_name, request, is_ice, use_coupon FROM orders WHERE order_id = ?";
                PreparedStatement selectStmt = con.prepareStatement(selectSql);
                selectStmt.setInt(1, orderId);
                ResultSet rs = selectStmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("해당 주문 번호의 정보가 없습니다.");
                    continue; 
                }

                
                
                //내가 취소한다고 한 정보 취소하는지 체크하기 추가
                System.out.println("\n=============[접수된 주문 리스트]=============");
                System.out.println("주문번호: " + rs.getInt("order_id"));
                System.out.println("주문일자: " + rs.getTimestamp("order_date"));
                System.out.println("회원번호: " + rs.getString("customer_id"));
                System.out.println("메뉴명: " + rs.getString("menu_name"));
                System.out.println("요청사항: " + rs.getString("request"));
                System.out.println("ICE 여부: " + (rs.getString("is_ice").equals("1") ? "ICE" : "HOT"));
                System.out.println("쿠폰 사용 여부: " + (rs.getString("use_coupon").equals("1") ? "사용" : "사용 안 함"));

                System.out.print("\n정말 이 주문을 취소하시겠습니까? (y/n) >> ");
                String confirm = sc.nextLine();
                if (!confirm.equalsIgnoreCase("y")) {
                    System.out.println("주문 취소가 취소되었습니다.");
                    continue;
                }

                String deleteSql = "DELETE FROM orders WHERE order_id = ?";
                PreparedStatement deleteStmt = con.prepareStatement(deleteSql);
                deleteStmt.setInt(1, orderId);
                int result = deleteStmt.executeUpdate();

                if (result > 0) {
                    System.out.println("주문 취소가 완료되었습니다.");
                } else {
                    System.out.println("주문 취소 처리 중 오류가 발생했습니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("주문 취소 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    
 // 5. 포인트 확인
    private void checkPoint() {
        try (Connection con = ds.getConnection()) {
            RenderTitle.renderTitle(" 포인트 확인하기 ");
            System.out.print("회원번호를 입력해주세요. (이전으로 : q) >>");
            String customerId = sc.nextLine().trim();
            if (customerId.equalsIgnoreCase("q")) return;

            String sql = "SELECT customer_id, stamp, coupon, FLOOR(stamp / 10) AS expectedCoupon " +
                         "FROM members WHERE customer_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int stamp = rs.getInt("stamp");
                int coupon = rs.getInt("coupon");
                int expectedCoupon = rs.getInt("expectedCoupon"); 

                System.out.println("회원번호: " + rs.getString("customer_id"));
                System.out.println("스탬프: " + (stamp % 10) + "/10 ");
                System.out.println("쿠폰 보유 수: " + coupon + "장");
            } else {
                System.out.println("회원 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("포인트 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    
}