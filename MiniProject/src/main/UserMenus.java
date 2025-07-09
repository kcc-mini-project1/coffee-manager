package main;

import java.util.Scanner;

import utils.MenuUI;
import utils.OrderUI;
import utils.RenderSystem;

public class UserMenus {
    private Scanner sc;
    
    public EmployeeMain empMain;
    public OrderUI orderUI;
    public MenuUI menuUI;
    
    public UserMenus(Scanner sc) {
        this.sc = sc;
        this.empMain = new EmployeeMain(sc);
        this.orderUI = new OrderUI(sc);
    		this.menuUI = new MenuUI(sc);
    }
	
    private static final String EMPLOYEE_PASSWORD = "employee";
    private static final String OWNER_PASSWORD = "owner";
    
    public boolean authenticateUser(String role, String password) {
    		RenderSystem.printDivider(RenderSystem.WIDTH, true);
    		RenderSystem.printInputFormMessage(role + " 비밀번호를 입력해주세요.");
        String passwordInput = sc.nextLine();
        RenderSystem.printEmptyLine(2);
        
        if (passwordInput.equals(password)) {
        		RenderSystem.printStatus(role + " 인증이 성공되었습니다.", true);
            RenderSystem.printEmptyLine(2);
            return true;
        } else {
    			RenderSystem.printStatus("비밀번호가 일치하지 않습니다.", false);
            RenderSystem.printEmptyLine(2);
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
                			RenderSystem.printStatus("메뉴 관리 시스템 에러 발생", false);
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
            				RenderSystem.printStatus("메뉴 관리 시스템 에러 발생", false);
	                    e.printStackTrace();
	                }
	                break;
	            	case "Q":
	            	case "q":
	         	case "ㅂ":
	         		RenderSystem.printStatus("이전 메뉴로 돌아갑니다.", true);
	         		employeeRun = false;
	                break;
	            default:
	                RenderSystem.printInvalidInput();
            }
        }
    };
};
