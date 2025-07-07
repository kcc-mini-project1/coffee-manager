package dao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import utils.RenderOptions;
import utils.RenderTitle;

public class MenuMainDao {
	public static void main(String[] args) throws Exception {
		MenuInsertDao createMenu = new MenuInsertDao();
		MenuSelectDao searchMenu = new MenuSelectDao();
		MenuDeleteDao removeMenu = new MenuDeleteDao();
		MenuUpdateDao updateMenu = new MenuUpdateDao();
		
		RenderTitle title = new RenderTitle();
		RenderOptions options = new RenderOptions();
		
		Scanner scanner = new Scanner(System.in);

		List<String> menuList = new ArrayList<>();
		
		String[] actions = {"조회하기", "추가하기", "삭제하기", "수정하기"};
		
		for (int i = 0; i < 5; i++) {
			if (i == 4) {
				menuList.add("< 이전 단계로");
			} else {
 				menuList.add("메뉴 " + actions[i]);
			}
		}
		
		while (true) {
			System.out.println();
			title.renderTitle("메뉴 관리시스템");
			options.multiLine(menuList);
			title.printLine();
			System.out.println();
			System.out.println("\u2754 원하시는 작업 번호를 입력해주세요 > ");
			
			
			int action;
			
			try {
				action = scanner.nextInt();
				System.out.println("\u2714 " + menuList.get(action - 1) + " \n");
		    } catch (InputMismatchException e) {
				System.out.println("숫자만 입력해주세요");
				
		        scanner.nextLine();
		        continue;
		    }
			
			if (action == 1 || action == 2 || action == 3 || action == 4 || action == 5) {
				if (action == 1) {
					title.renderTitle("메뉴 조회하기");
					System.out.println();
					searchMenu.startSelect();
					
					continue;
				} else if (action == 2) {
					title.renderTitle("메뉴 추가하기");
					System.out.println();
					createMenu.startInsert();
					
					continue;
				} else if (action == 3) {
					title.renderTitle("메뉴 삭제하기");
					System.out.println();
					removeMenu.startRemove();
					
					continue;
				} else if (action == 4) {
					title.renderTitle("메뉴 수정하기");
					System.out.println();
					updateMenu.startUpdate();
					
					continue;
				} else if (action == 5) {
					break;
				}
			} else {
				System.out.println("잘못된 입력값입니다.");
				
				continue;
			}
			
			break;
		}
	}
}
