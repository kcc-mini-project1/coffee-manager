package utils;

import dao.OrderDao;
import dao.OrderDao.MenuItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderUI {
	
	public static void start() {
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
            RenderTitle.printLine();
            System.out.print(">>> ");

            String input = sc.nextLine();

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
                case "q":
                    run = false;
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
	
	//1.메뉴판 보기
    public static void printMenuBoard() {
        OrderDao dao = new OrderDao();
        List<MenuItem> menus = dao.getMenuItems();

        String line = "─".repeat(125);
        System.out.println(line);
        System.out.printf("%62s%-62s\n", "", "메뉴판");
        System.out.println(line);
        System.out.printf(" %-10s  %-10s  %-14s  %-6s\t%-8s\t%-8s\t%-30s\n",
                "큰카테고리", "작은카테고리", "메뉴명", "가격", "솔드아웃", "아이스", "메뉴설명");
        System.out.println(line);

        for (MenuItem item : menus) {
            String parent = padKorean(item.parentName, 10);
            String category = padKorean(item.categoryName, 10);
            String name = padKorean(item.menuName, 14);
            String desc = padKorean(item.description, 30);
            String soldout = padKorean((item.isSoldout == 1) ? "솔드아웃" : "주문가능", 8);
            String ice = padKorean((item.iceable == 1) ? "O" : "X", 8);

            System.out.printf(" %-10s  %-13s  %-14s  %-6d\t%-8s\t%-8s\t%-30s \n",
                    parent, category, name, item.price, soldout, ice, desc);
        }
        System.out.println(line);
    }
    

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

    
    private static final Scanner sc = new Scanner(System.in);
    
    
    //2.주문하기
    public static void printInsertOrder() {
        try {
            System.out.println();
            RenderTitle.renderTitle(" 메뉴 주문하기 - 카테고리를 선택하세요. ");
            System.out.print("[큰 카테고리] 1. Drink    2. Desert \n");
            System.out.print(">>> ");
            String input = sc.nextLine();

            String bigCategoryName = switch (input) {
                case "1" -> "Drink";
                case "2" -> "Desert";
                default -> "잘못된 입력입니다.";
            };
            
            System.out.println();
            RenderTitle.renderTitle(" 메뉴 주문하기 - " + bigCategoryName + "의 카테고리를 선택하세요. ");

            String smallCategoryName;
            if (bigCategoryName.equals("Drink")) {
                System.out.print("[Drink] 1. Coffee    2. Tea   q. 이전으로 \n");
                System.out.print(">>> ");
                smallCategoryName = switch (sc.nextLine()) {
                    case "1" -> "Coffee";
                    case "2" -> "Tea";
                    default -> "잘못된 입력입니다.";
                };

            } else {
                System.out.print("[Desert] 1. Cake    2. Bread   q. 이전으로 \n");
                System.out.print(">>> ");
                smallCategoryName = switch (sc.nextLine()) {
                    case "1" -> "Cake";
                    case "2" -> "Bread";
                    default -> "잘못된 입력입니다.";
                };
            }

            OrderDao dao = new OrderDao();
            ResultSet menuRs = dao.getAvailableMenus(smallCategoryName);

            RenderTitle.renderTitle(" 메뉴 주문하기 ");
            System.out.println("[" + smallCategoryName + "] 카테고리 - 주문 가능한 메뉴 목록");
            List<String> availableMenus = new ArrayList<>();
            while (menuRs.next()) {
                String menu = menuRs.getString("menu_name");
                int price = menuRs.getInt("price");
                System.out.println("\u2714 " + menu + " (" + price + "원)");
                availableMenus.add(menu.toLowerCase());
            }
            RenderTitle.printLine();
            System.out.print("\n 위의 주문 가능한 메뉴리스트에서 주문하실 메뉴명을 입력하세요. (q: 주문종료) \n");
            System.out.print(">>> ");
            String menuName = sc.nextLine();
            if (menuName.equalsIgnoreCase("q")) return;
            if (!availableMenus.contains(menuName.toLowerCase())) {
                System.out.println("선택 목록에 없는 메뉴입니다.");
                System.out.println("주문을 종료합니다.");
                return;
            }

            System.out.println();
            System.out.print("전화번호를 입력하세요. (q: 주문종료 / 엔터: 비회원)  \n");
            System.out.print(">>> ");
            String customerId = sc.nextLine();
            if (customerId.equalsIgnoreCase("q")) return;
            if (customerId.isBlank()) customerId = "비회원";

            // 회원이 비회원이 아니면 members 테이블에 존재하는지 확인, 없으면 추가
            if (!customerId.equals("비회원")) {
                if (!dao.isMemberExists(customerId)) {
                    boolean inserted = dao.insertMember(customerId);
                    if (inserted) {
                        System.out.println("신규 회원으로 등록되었습니다.");
                    } else {
                        System.out.println("회원 등록 중 오류가 발생했습니다.");
                        return;
                    }
                }
            }

            boolean useCoupon = false;
            if (!customerId.equals("비회원")) {
                ResultSet cpRs = dao.getUserCoupon(customerId);
                if (cpRs.next() && cpRs.getInt("coupon") > 0) {
                    System.out.print("사용 가능한 쿠폰이 있습니다. 사용하시겠습니까? (y/n) \n");
                    System.out.print(">>> ");
                    useCoupon = sc.nextLine().equalsIgnoreCase("y");
                }
            }
            System.out.println();
            System.out.println("요청사항을 입력하세요. (요청사항 없으면 Enter을 누르세요.)");
            System.out.print(">>> ");
            String request = sc.nextLine();
            System.out.println();
            System.out.println("\"ICE로 주문하시겠습니까? (1: ICE / 0: HOT)");
            System.out.print(">>> ");
            boolean isIce = sc.nextLine().equalsIgnoreCase("0");

            System.out.println();
            RenderTitle.renderTitle("입력하신 주문 정보 확인");
            System.out.println("회원번호: " + customerId);
            System.out.println("메뉴명: " + menuName);
            System.out.println("요청사항: " + request);
            System.out.println("ICE 여부: " + (isIce ? "ICE" : "HOT"));
            System.out.println("쿠폰 사용: " + (useCoupon ? "사용" : "사용 안 함"));
            RenderTitle.printLine();
            System.out.print("위 정보로 주문하시겠습니까? (y/n) \n ");
            System.out.print(">>> ");
            String confirm = sc.nextLine();
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("주문이 취소되었습니다.");
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
      } catch (SQLException e) {
          System.out.println("주문 오류가 발생했습니다.");
          e.printStackTrace();
      }
  }
    
    //3.주문목록 확인
    public static void printOrderList() {
        OrderDao dao = new OrderDao();

        try (ResultSet rs = dao.getOrderList()) {
            RenderTitle.renderTitle("주문내역 확인하기");
            System.out.println("순서 \t 주문번호 \t 회원번호 \t \t주문일		\t주문한 메뉴	 ");
            System.out.println("----------------------------------------------------------------------------------");
            int i = 1;
            while (rs.next()) {
                System.out.printf("%d \t %d \t %s \t %s	\t %s \n",
                        i++,
                        rs.getInt("orderId"),
                        rs.getString("customerId"),
                        rs.getDate("orderDate"),
                        rs.getString("menuName"));
            }

        } catch (SQLException e) {
            System.out.println("주문 내역 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
    
    
    
    //4.주문취소하기
    public static void deleteOrder() {
        OrderDao dao = new OrderDao();
        boolean run = true;

        try (ResultSet listRs = dao.getRecentOrders()) {
            System.out.println("==============================[ 최근 주문 내역 리스트 ]==============================");
            System.out.println("주문번호	\t회원번호	\t메뉴명	\t주문일자	\n");

            while (listRs.next()) {
                System.out.printf("%d	\t%s	\t%s	\t%s\n",
                        listRs.getInt("order_id"),
                        listRs.getString("customer_id"),
                        listRs.getString("menu_name"),
                        listRs.getTimestamp("order_date").toString());
            }

            System.out.println();

            while (run) {
                RenderTitle.renderTitle("주문 취소하기");
                System.out.print("취소할 주문번호를 입력해주세요. (이전으로: q) \n");
                System.out.print(">>> ");
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("q")) break;

                
                int orderId;
                try {
                    orderId = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                    continue;
                }

                try (ResultSet rs = dao.getOrderById(orderId)) {
                    if (!rs.next()) {
                        System.out.println("해당 주문 번호의 정보가 없습니다.");
                        continue;
                    }

                    System.out.println("\n ============================[접수된 주문 정보]============================");
                    System.out.println("주문번호: " + rs.getInt("order_id"));
                    System.out.println("주문일자: " + rs.getTimestamp("order_date"));
                    System.out.println("회원번호: " + rs.getString("customer_id"));
                    System.out.println("메뉴명: " + rs.getString("menu_name"));
                    System.out.println("요청사항: " + rs.getString("request"));
                    System.out.println("ICE 여부: " + (rs.getInt("is_ice") == 1 ? "ICE" : "HOT"));
                    System.out.println("쿠폰 사용 여부: " + (rs.getInt("use_coupon") == 1 ? "사용" : "사용 안 함"));

                    System.out.print("\n정말 이 주문을 취소하시겠습니까? (y/n) \n");
                    System.out.print(">>> ");
                    String confirm = sc.nextLine();
                    if (!confirm.equalsIgnoreCase("y")) {
                        System.out.println("주문 취소가 취소되었습니다.");
                        continue;
                    }

                    int result = dao.deleteOrderById(orderId);
                    if (result > 0) {
                        System.out.println("주문 취소가 완료되었습니다.");
                        break;
                    } else {
                        System.out.println("주문 취소 처리 중 오류가 발생했습니다.");
                    }

                } catch (SQLException e) {
                    System.out.println("주문 상세 조회 중 오류가 발생했습니다.");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("주문 취소 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
    
    
    //5.포인트내역 확인하기
    public static void checkPoint() {
        OrderDao dao = new OrderDao();
        try {
            RenderTitle.renderTitle(" 포인트 확인하기 ");
            System.out.print("회원번호를 입력해주세요. (이전으로 : q) \n");
            System.out.print(">>> ");
            String customerId = sc.nextLine().trim();
            if (customerId.equalsIgnoreCase("q")) return;

            ResultSet rs = dao.getMemberPointInfo(customerId);

            if (rs.next()) {
                int stamp = rs.getInt("stamp");
                int coupon = rs.getInt("coupon");

                System.out.println("회원번호: " + rs.getString("customer_id"));
                System.out.println("스탬프: " + (stamp % 10) + " / 10");
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
