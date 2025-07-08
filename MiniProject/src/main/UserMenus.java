package main;

import java.util.Scanner;

import utils.MenuUI;
import utils.OrderUI;
import utils.RenderSystem;

public class UserMenus {
    private Scanner sc;
    
    public UserMenus(Scanner sc) {
        this.sc = sc;
    }
    
	EmployeeMain empMain = new EmployeeMain(sc);
	MenuUI menuUI = new MenuUI(sc);
	OrderUI orderUI = new OrderUI(sc);
	
    private static final String EMPLOYEE_PASSWORD = "employee";
    private static final String OWNER_PASSWORD = "owner";
    
    public boolean authenticateUser(String role, String password) {
    		RenderSystem.printDivider(RenderSystem.WIDTH, true);
    		RenderSystem.printInputFormMessage(role + " 비밀번호를 입력해주세요.");
        String passwordInput = sc.nextLine();
        RenderSystem.printEmptyLine(2);
        
        if (passwordInput.equals(password)) {
            System.out.println("인증 성공!");
            RenderSystem.printEmptyLine(1);
            return true;
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
            RenderSystem.printEmptyLine(1);
            return false;
        }
    }
    
    public void showOwnerMenu() {
        if (!authenticateUser("사장님", OWNER_PASSWORD)) {
            return;
        }
        
        boolean ownerRun = true;
        
        while (ownerRun) {
            RenderSystem.printTitle(RenderSystem.WIDTH, "사장님 메뉴");
            System.out.println(" 1. 주문 관리");
            System.out.println(" 2. 직원 관리");
            System.out.println(" 3. 메뉴 관리");
            System.out.println(" Q. 처음으로");
            RenderSystem.printDivider(RenderSystem.WIDTH, true);
            
            RenderSystem.printInputForm();
            String input = sc.nextLine();
            RenderSystem.printEmptyLine(2);
            
            switch (input) {
                case "1":
            			orderUI.start();
                    break;
                case "2":
                    empMain.start();
                    break;
                case "3":
                		try {
                        menuUI.start();
                    } catch (Exception e) {
                        System.out.println("메뉴 관리 시스템 오류: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                	case "Q":
                	case "q":
             	case "ㅂ":
             		System.out.println("이전 메뉴로 돌아갑니다.");
                    ownerRun = false;
                    break;
                default:
                    RenderSystem.printInvalidInput();
            }
        }
    };
    
    // 직원 메뉴
    public void showEmployeeMenu() {
        if (!authenticateUser("직원", EMPLOYEE_PASSWORD)) {
            return;
        }
        
        boolean employeeRun = true;
        
        while (employeeRun) {
            RenderSystem.printTitle(RenderSystem.WIDTH, "직원 메뉴");
            System.out.println(" 1. 주문 관리");
            System.out.println(" 2. 메뉴 관리");
            System.out.println(" B. 처음으로");
            RenderSystem.printDivider(RenderSystem.WIDTH, true);
            
            RenderSystem.printInputForm();
            String input = sc.nextLine();
            RenderSystem.printEmptyLine(2);
            
            switch (input) {
	            case "1":
	        			orderUI.start();
	                break;
	            case "2":
	            		try {
	                    menuUI.start();
	                } catch (Exception e) {
	                    System.out.println("메뉴 관리 시스템 오류: " + e.getMessage());
	                    e.printStackTrace();
	                }
	                break;
	            	case "Q":
	            	case "q":
	         	case "ㅂ":
	         		System.out.println("이전 메뉴로 돌아갑니다.");
	         		employeeRun = false;
	                break;
	            default:
	                RenderSystem.printInvalidInput();
            }
        }
    };
};
