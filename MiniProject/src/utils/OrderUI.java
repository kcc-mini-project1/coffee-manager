package utils;

import dao.OrderDao;
import dao.OrderDao.MenuItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderUI {
	private static final Scanner sc = new Scanner(System.in);
	private static final RenderSystem renderSys = new RenderSystem();
	private static int displaySize = 40;
	private static int menuSize = 110;
	
	public static void start() {
		boolean run = true;
		while (run) {
			// 주문 관리 시스템 기능 출력
			renderSys.printTitle(displaySize, "주문 관리 시스템");
			System.out.println(" 1. 메뉴판 보기");
			System.out.println(" 2. 메뉴 주문하기");
			System.out.println(" 3. 주문내역 확인하기");
			System.out.println(" 4. 주문 취소하기");
			System.out.println(" 5. 포인트 확인하기");
			System.out.println(" Q. 카페 관리 시스템으로 돌아가기");
			renderSys.printDivider(displaySize, true);
            
			// 주문 관리 시스템 기능 선택
			renderSys.printInputForm();
			String input = sc.nextLine();
            renderSys.printEmptyLine(2);
			
			// 선택에 따른 기능 수행
			switch (input) {
			case "1":
				printMenuBoard();
				break;
			case "2":
				printInsertOrder();
				break;
			case "3":
				printOrderList();
				break;
			case "4":
				deleteOrder();
				break;
			case "5":
				checkPoint();
				break;
			case "Q":
			case "q":
			case "ㅂ":
				run = false;
				break;
			default:
				renderSys.printInvalidInput();
			}
		}
	}
	
	// 1. 메뉴판 보기
	public static void printMenuBoard() {
		OrderDao dao = new OrderDao();
		List<MenuItem> menus = dao.getMenuItems();
		
		renderSys.printTitle(menuSize, "메뉴판");
        System.out.printf(" %-10s%-10s%-15s%-9s%-10s%-10s%-30s\n",
                "큰카테고리", "작은카테고리", "메뉴명", "가격", "솔드아웃", "아이스", "메뉴설명");
        renderSys.printDivider(menuSize, true);

        for (MenuItem item : menus) {
            String parent = padKorean(item.parentName, 10);
            String category = padKorean(item.categoryName, 10);
            String name = padKorean(item.menuName, 14);
            String desc = padKorean(item.description, 30);
            String soldout = padKorean((item.isSoldout == 1) ? "솔드아웃" : "주문가능", 8);
            String ice = padKorean((item.iceable == 1) ? "O" : "X", 8);

            System.out.printf(" %-10s  %-11s  %-10s\t  %-10d%-12s%-10s%-30s \n",
                    parent, category, name, item.price, soldout, ice, desc);
        }
        renderSys.printDivider(menuSize, true);
        renderSys.printEmptyLine(2);
    }
    
	// 한글 패딩 설정
    private static String padKorean(String text, int width) {
        int len = 0;
        for (char ch : text.toCharArray()) {
            len += (Character.toString(ch).matches("[가-힣]")) ? 2 : 1;
        }
        int padding = width - len;
        if (padding > 0) {
            text += " ".repeat(padding);
        }
        return text;
    }
    
    
    // 2. 주문하기
    public static void printInsertOrder() {
    		try {
    			// (1) 카테고리 선택하기
    			boolean vaildCategoryInput = false;
    			String categoryName = "";
    			while (!vaildCategoryInput) {
    				// 카테고리 선택 출력
    				renderSys.printTitle(displaySize, "메뉴 주문하기 - 카테고리 선택");
    				System.out.println(" 1. Drink");
    				System.out.println(" 2. Desert");
    				System.out.println(" Q. 주문 취소하기");
    				// System.out.print(" 1. Drink\t2. Desert\tQ.주문 취소하기\n");
    				renderSys.printDivider(displaySize, true);
    				
    				// 사용자로부터 카테고리 입력받기
    				renderSys.printInputFormMessage("카테고리를 선택해주세요.");
    				String input = sc.nextLine();
    				
    				// 입력 확인
    				if (input.equals("1")) {
    					categoryName = "Drink";
    					vaildCategoryInput = true;
    				} else if (input.equals("2")) {
    					categoryName = "Desert";
    					vaildCategoryInput = true;
    				} else if (input.equals("q") || input.equals("Q") || input.equals("ㅂ")) {
    					renderSys.printEmptyLine(2);
    					return;
    				} else {
    					renderSys.printInvalidInput();
    				}
    				renderSys.printEmptyLine(2);
    			}
            
    			// (2) 서브카테고리 선택하기
    			boolean vaildSubCategoryInput = false;
    			String subCategoryName = "";
    			while (!vaildSubCategoryInput) {
    				// 서브 카테고리 선택 출력
    				renderSys.printTitle(displaySize, "메뉴 주문하기 - 세부분류 선택");
    				if (categoryName.equals("Drink")) {
    					// System.out.print(" 1. Coffee\t2. Tea\t\tQ.주문 취소하기\n");
        				System.out.println(" 1. Coffee");
        				System.out.println(" 2. Tea");
        				System.out.println(" Q. 주문 취소하기");
        				renderSys.printDivider(displaySize, true);
        				
        				// 사용자로부터 서브카테고리 입력받기
        				renderSys.printInputFormMessage(categoryName + "의 세부분류를 선택해주세요.");
        				String input = sc.nextLine();
        				
        				// 입력 확인
        				if (input.equals("1")) {
        					subCategoryName = "Coffee";
        					vaildSubCategoryInput = true;
        				} else if (input.equals("2")) {
        					subCategoryName = "Tea";
        					vaildSubCategoryInput = true;
        				} else if (input.equals("q") || input.equals("Q") || input.equals("ㅂ")) {
        					renderSys.printEmptyLine(2);
        					return;
        				} else {
        					renderSys.printInvalidInput();
        				}
        				renderSys.printEmptyLine(2);
    				} else if (categoryName.equals("Desert")) {
    					// System.out.print(" 1. Cake\t2. Bread\t\tQ.주문 취소하기\n");
        				System.out.println(" 1. Cake");
        				System.out.println(" 2. Bread");
        				System.out.println(" Q. 주문 취소하기");
        				renderSys.printDivider(displaySize, true);
        				
        				// 사용자로부터 서브카테고리 입력받기
        				renderSys.printInputFormMessage(categoryName + "의 세부분류를 선택해주세요.");
        				String input = sc.nextLine();
        				
        				// 입력 확인
        				if (input.equals("1")) {
        					subCategoryName = "Cake";
        					vaildSubCategoryInput = true;
        				} else if (input.equals("2")) {
        					subCategoryName = "Bread";
        					vaildSubCategoryInput = true;
        				} else if (input.equals("q") || input.equals("Q") || input.equals("ㅂ")) {
        					renderSys.printEmptyLine(2);
        					return;
        				} else {
        					renderSys.printInvalidInput();
        				}
        				renderSys.printEmptyLine(2);
    				}
    			}
    			
    			// (3) 주문 목록 확인 후 주문할 메뉴 선택하기
            OrderDao dao = new OrderDao();
            ResultSet menuRs = dao.getAvailableMenus(subCategoryName);
            
            // 제목 + 메뉴판 출력
            renderSys.printTitle(displaySize, "메뉴 주문하기 - " + subCategoryName + " 메뉴목록");
            List<String> availableMenus = new ArrayList<>();
            while (menuRs.next()) {
                String menu = menuRs.getString("menu_name");
                int price = menuRs.getInt("price");
                System.out.println("\u2714 " + menu + " (" + price + "원)");
                availableMenus.add(menu.toLowerCase());
            }
            renderSys.printDivider(displaySize, true);
            
            // 메뉴명 사용자로부터 입력받기
            renderSys.printInputFormMessage("주문하실 메뉴명을 입력해주세요. (Q: 주문 종료하기)");
            String menuName = sc.nextLine();
            renderSys.printEmptyLine(2);
            
            // 입력 확인
            if (menuName.equals("q") || menuName.equals("Q") || menuName.equals("ㅂ")) {
            		renderSys.printEmptyLine(2);
            		return;
            }
            if (!availableMenus.contains(menuName.toLowerCase())) {
                System.out.println("메뉴목록에 없는 메뉴입니다.\n주문을 종료합니다.");
                renderSys.printEmptyLine(2);
                return;
            }

            // (4) 회원번호 입력받기
            renderSys.printInputFormMessage("전화번호를 입력해주세요. (Q: 주문 종료 / 엔터: 비회원)");
            String customerId = sc.nextLine();
            renderSys.printEmptyLine(2);
            
            // 입력 확인
            if (customerId.equals("q") || customerId.equals("Q") || customerId.equals("ㅂ")) {
	        		return;
            }
            if (customerId.isBlank()) customerId = "비회원";
            
            // 회원이 비회원이 아니면 members 테이블에 존재하는지 확인, 없으면 추가
            if (!customerId.equals("비회원")) {
            		if (!dao.isMemberExists(customerId)) {
            			boolean inserted = dao.insertMember(customerId);
            			if (inserted) {
            				System.out.println("신규 회원으로 등록되었습니다.");
            				renderSys.printEmptyLine(2);
        				} else {
        					System.out.println("회원 등록 중 오류가 발생했습니다.");
        					renderSys.printEmptyLine(2);
        					return;
                    }
                }
            }
            
            // (5) 쿠폰 사용 확인
            boolean useCoupon = false;
            if (!customerId.equals("비회원")) {
                ResultSet cpRs = dao.getUserCoupon(customerId);
                if (cpRs.next() && cpRs.getInt("coupon") > 0) {
                		renderSys.printInputFormMessage("사용 가능한 쿠폰이 있습니다. 사용하시겠습니까? (y/n)");
                    useCoupon = sc.nextLine().equalsIgnoreCase("y");
                    renderSys.printEmptyLine(2);
                }
            }
            
            // (6) 요청사항 확인
            renderSys.printInputFormMessage("요청사항을 입력하세요. (요청사항 없으면 Enter을 누르세요.)");
            String request = sc.nextLine();
            renderSys.printEmptyLine(2);
            
            // (7) ICE 선택 확인
            renderSys.printInputFormMessage("ICE로 주문하시겠습니까? (1: ICE / 0: HOT)");
            boolean isIce = sc.nextLine().equalsIgnoreCase("0");
            renderSys.printEmptyLine(2);
            
            // (8) 입력받은 주문 정보 확인
            renderSys.printTitle(displaySize, "입력하신 주문 정보 확인");
            System.out.println("회원번호\t: " + customerId);
            System.out.println("메뉴명\t: " + menuName);
            System.out.println("요청사항\t: " + request);
            System.out.println("ICE 여부\t: " + (isIce ? "ICE" : "HOT"));
            System.out.println("쿠폰 사용\t: " + (useCoupon ? "사용" : "사용 안 함"));
            renderSys.printDivider(displaySize, true);
            
            // 입력 확인
            renderSys.printInputFormMessage("위 정보로 주문하시겠습니까? (y/n)");
            String confirm = sc.nextLine();
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("주문이 취소되었습니다.");
                renderSys.printEmptyLine(2);
                return;
            }

            boolean success = dao.insertOrder(customerId, menuName, request, isIce, useCoupon);
            if (success) {
            		if (useCoupon && !customerId.equals("비회원")) {
            			System.out.println("쿠폰 1장이 사용되었습니다.");
            		}
            		System.out.println("주문이 완료되었습니다.");
        		} else {
        			System.out.println("주문 처리 중 오류가 발생했습니다.");
    			}
            renderSys.printEmptyLine(2);
      } catch (SQLException e) {
          System.out.println("주문 오류가 발생했습니다.");
          e.printStackTrace();
      }
  }
    
    // 3.주문목록 확인
    public static void printOrderList() {
    		OrderDao dao = new OrderDao();
    		
    		try (ResultSet rs = dao.getOrderList()) {
    			renderSys.printTitle(menuSize, "주문내역 확인하기");
    			System.out.println("순서\t주문번호\t    회원번호\t   주문일\t\t  주문한 메뉴");
    			renderSys.printDivider(menuSize, true);
            int i = 1;
            while (rs.next()) {
                System.out.printf(" %d \t %d \t %s    %s	   %s \n",
                        i++,
                        rs.getInt("orderId"),
                        rs.getString("customerId"),
                        rs.getDate("orderDate"),
                        rs.getString("menuName"));
            }
            renderSys.printDivider(menuSize, true);
            renderSys.printEmptyLine(2);
        } catch (SQLException e) {
            System.out.println("주문 내역 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
    
    
    
    // 4.주문취소하기
    public static void deleteOrder() {
        OrderDao dao = new OrderDao();
        boolean run = true;

        try (ResultSet listRs = dao.getRecentOrders()) {
        		renderSys.printSubTitle(menuSize, "최근 주문내역 리스트");
            System.out.println("주문번호\t\t  회원번호\t\t\t  메뉴명\t\t\t    주문일자\n");
            while (listRs.next()) {
                System.out.printf(" %d\t\t%s\t\t%s\t\t\t%s\n",
                        listRs.getInt("order_id"),
                        listRs.getString("customer_id"),
                        listRs.getString("menu_name"),
                        listRs.getTimestamp("order_date").toString());
            }
            renderSys.printEmptyLine(2);
            
            while (run) {
            		renderSys.printTitle(displaySize, "주문 취소하기");
            		
            		renderSys.printInputFormMessage("취소할 주문번호를 입력해주세요. (Q: 돌아가기)");
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("q")) break;
                renderSys.printEmptyLine(2);
                
                int orderId;
                try {
                		orderId = Integer.parseInt(input);
            		} catch (NumberFormatException e) {
            			renderSys.printInvalidInput();
            			renderSys.printEmptyLine(2);
                    continue;
                }

                try (ResultSet rs = dao.getOrderById(orderId)) {
                    if (!rs.next()) {
                        System.out.println("해당 주문 번호의 정보가 없습니다.");
                        renderSys.printEmptyLine(2);
                        continue;
                    }
                    
                    renderSys.printSubTitle(menuSize, "접수된 주문정보");
                    System.out.println("주문번호\t: " + rs.getInt("order_id"));
                    System.out.println("주문일자\t: " + rs.getTimestamp("order_date"));
                    System.out.println("회원번호\t: " + rs.getString("customer_id"));
                    System.out.println("메뉴명\t: " + rs.getString("menu_name"));
                    System.out.println("요청사항\t: " + rs.getString("request"));
                    System.out.println("ICE 여부\t: " + (rs.getInt("is_ice") == 1 ? "ICE" : "HOT"));
                    System.out.println("쿠폰\t: " + (rs.getInt("use_coupon") == 1 ? "사용" : "사용 안 함"));
                    renderSys.printDivider(displaySize, true);
                    
                    renderSys.printInputFormMessage("이 주문을 취소하시겠습니까? (y/n)");
                    String confirm = sc.nextLine();
                    if (!confirm.equalsIgnoreCase("y")) {
                        System.out.println("주문 취소가 취소되었습니다.");
                        renderSys.printEmptyLine(2);
                        continue;
                    }

                    int result = dao.deleteOrderById(orderId);
                    if (result > 0) {
                        System.out.println("주문 취소가 완료되었습니다.");
                        break;
                    } else {
                        System.out.println("주문 취소 처리 중 오류가 발생했습니다.");
                    }
                    renderSys.printEmptyLine(2);
                } catch (SQLException e) {
                    System.out.println("주문 상세 조회 중 오류가 발생했습니다.");
                    renderSys.printEmptyLine(2);
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("주문 취소 중 오류가 발생했습니다.");
            renderSys.printEmptyLine(2);
            e.printStackTrace();
        }
    }
    
    
    //5.포인트내역 확인하기
    public static void checkPoint() {
        OrderDao dao = new OrderDao();
        try {
        		renderSys.printTitle(displaySize, "포인트 확인하기");
        		
        		renderSys.printInputFormMessage("회원번호를 입력해주세요. (Q: 이전으로)");
            String customerId = sc.nextLine().trim();
            renderSys.printEmptyLine(2);
            if (customerId.equalsIgnoreCase("q")) return;

            ResultSet rs = dao.getMemberPointInfo(customerId);
            if (rs.next()) {
                int stamp = rs.getInt("stamp");
                int coupon = rs.getInt("coupon");

                renderSys.printSubTitle(displaySize, "내 포인트");
                System.out.println("회원번호\t: " + rs.getString("customer_id"));
                System.out.println("스탬프\t: " + (stamp % 10) + " / 10");
                System.out.println("쿠폰\t: " + coupon + "장");
            } else {
                System.out.println("회원 정보를 찾을 수 없습니다.");
            }
            renderSys.printEmptyLine(2);
        } catch (SQLException e) {
            System.out.println("포인트 조회 중 오류가 발생했습니다.");
            renderSys.printEmptyLine(2);
            e.printStackTrace();
        }
    }
    
}
