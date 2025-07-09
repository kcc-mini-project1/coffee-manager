package main;

import java.util.Scanner;

import utils.MenuUI;
import utils.OrderUI;
import utils.RenderSystem;

public class UserMenus {
    private Scanner sc;
    private RenderSystem renderSys;
    
    public EmployeeMain empMain;
    public OrderUI orderUI;
    public MenuUI menuUI;
    
    public UserMenus(Scanner sc) {
        this.sc = sc;
        this.empMain = new EmployeeMain(sc);
        this.orderUI = new OrderUI(sc);
    		this.menuUI = new MenuUI(sc);
    		this.renderSys = new RenderSystem();
    }
	
    private static final String EMPLOYEE_PASSWORD = "employee";
    private static final String OWNER_PASSWORD = "owner";
    
    public boolean authenticateUser(String role, String password) {
    		renderSys.printDivider(renderSys.WIDTH, true);
    		renderSys.printInputFormMessage(role + " 비밀번호를 입력해주세요.");
        String passwordInput = sc.nextLine();
        renderSys.printEmptyLine(2);
        
        if (passwordInput.equals(password)) {
        		renderSys.printStatus(role + " 인증이 성공되었습니다.", true);
            renderSys.printEmptyLine(2);
            return true;
        } else {
    			renderSys.printStatus("비밀번호가 일치하지 않습니다.", false);
            renderSys.printEmptyLine(2);
            return false;
        }
    }
    
    public void showOwnerMenu() {
        if (!authenticateUser("사장님", OWNER_PASSWORD)) {
            return;
        }
        
        boolean ownerRun = true;
        
        while (ownerRun) {
            renderSys.printTitle(renderSys.WIDTH, "사장님 메뉴");
            System.out.println(" 1. 주문 관리");
            System.out.println(" 2. 직원 관리");
            System.out.println(" 3. 메뉴 관리");
            System.out.println(" Q. 처음으로");
            renderSys.printDivider(renderSys.WIDTH, true);
            
            renderSys.printInputForm();
            String input = sc.nextLine();
            renderSys.printEmptyLine(2);
            
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
                			renderSys.printStatus("메뉴 관리 시스템 에러 발생", false);
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
                    renderSys.printInvalidInput();
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
            renderSys.printTitle(renderSys.WIDTH, "직원 메뉴");
            System.out.println(" 1. 주문 관리");
            System.out.println(" 2. 메뉴 관리");
            System.out.println(" Q. 처음으로");
            renderSys.printDivider(renderSys.WIDTH, true);
            
            renderSys.printInputForm();
            String input = sc.nextLine();
            renderSys.printEmptyLine(2);
            
            switch (input) {
	            case "1":
	        			orderUI.start();
	                break;
	            case "2":
	            		try {
	                    menuUI.start();
	                } catch (Exception e) {
            				renderSys.printStatus("메뉴 관리 시스템 에러 발생", false);
	                    e.printStackTrace();
	                }
	                break;
	            	case "Q":
	            	case "q":
	         	case "ㅂ":
	         		renderSys.printStatus("이전 메뉴로 돌아갑니다.", true);
	         		employeeRun = false;
	                break;
	            default:
	                renderSys.printInvalidInput();
            }
        }
    };
};
