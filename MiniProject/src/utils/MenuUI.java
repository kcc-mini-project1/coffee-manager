package utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dao.MenuDao;

public class MenuUI {
	private Scanner scanner;
	private OrderUI orderUI;
	private RenderSystem renderSys;
	
	public MenuUI (Scanner scanner) {
		this.scanner = scanner;
		this.orderUI = new OrderUI(scanner);
		this.renderSys = new RenderSystem();
	}
	static MenuDao menuDao = new MenuDao();
	public ErrorUtil errorUtil = new ErrorUtil();
	
    public void showError(SQLException e) {
        String friendlyMsg = errorUtil.getErrorMessage(e);
        renderSys.printErrorMessage(friendlyMsg);
    };
    
	public void start() throws Exception {
		while (true) {
			renderSys.printEmptyLine(1);
			renderSys.printTitle(renderSys.WIDTH, "메뉴 관리 시스템");
			System.out.print(" 1. 메뉴 조회하기\n"
					+ " 2. 메뉴 추가하기\n"
					+ " 3. 메뉴 삭제하기\n"
					+ " 4. 메뉴 수정하기\n"
					+ " q. 이전 단계로\n");
			renderSys.printDivider(renderSys.WIDTH, false);
			renderSys.printInputForm();
			
			String input = scanner.nextLine().trim();
			
			if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("Q")) {
				renderSys.printStatus("이전 단계로 이동합니다.", true);
				renderSys.printEmptyLine(2);
		        break;
		    }
			
			int action;
			try {
		        action = Integer.parseInt(input);
		    } catch (NumberFormatException e) {
		    		renderSys.printStatus("숫자 혹은 'q'만 입력해주세요.", false);
		        continue;
		    }
			
		    if (action < 1 || action > 4) {
		    	renderSys.printStatus("1 ~ 4 사이 숫자만 입력해주세요.", false);
		        continue;
		    }
		    
			renderSys.printEmptyLine(2);
			if (action == 1) {
				orderUI.printMenuBoard();
				
				System.out.println("\n[엔터를 누르면 이전 메뉴로 돌아갑니다]");
				scanner.nextLine();
				
				continue;
			} else if (action == 2) {
				renderSys.printSubTitle(renderSys.WIDTH, "메뉴 추가하기");
				
				List<String> subCategories = new ArrayList<>();
				List<String> categories = new ArrayList<>();
				String parentCategory, subCategory, menuName, description;
				int price, iceable;
				
				while(true) {
				    parentCategory = null;
				    subCategory = null;

					categories = menuDao.getParentCategories();
					renderSys.printSingleMenu(categories);
					renderSys.printDivider(renderSys.WIDTH, false);
					renderSys.printInputFormMessage("추가할 메뉴의 대분류 번호를 선택하세요.");
					
					int index =  -1;
					
					try {
				        index = scanner.nextInt();
						renderSys.printEmptyLine(2);
				    } catch (InputMismatchException e) {
				    		renderSys.printStatus("숫자를 입력해주세요", false);
				        scanner.nextLine();
						renderSys.printEmptyLine(2);
				        continue;
				    }
					
					
					switch (index) {
						case 1, 2:
							parentCategory = categories.get(index - 1);
							break;
						default:
							renderSys.printStatus("해당하는 카테고리가 존재하지 않습니다.", false);
							continue;
					}
					
					try {
						subCategories = menuDao.getChildCategories(parentCategory);
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
						
						continue;
					}
					
					renderSys.printDivider(renderSys.WIDTH, false);
					renderSys.printSingleMenu(subCategories);
					renderSys.printDivider(renderSys.WIDTH, false);
					renderSys.printInputFormMessage("추가할 메뉴의 소분류 번호를 선택하세요.");
					index =  -1;
					
					try {
				        index = scanner.nextInt();
						renderSys.printEmptyLine(2);
				    } catch (InputMismatchException e) {
				    		renderSys.printStatus("숫자를 입력해주세요", false);
						renderSys.printEmptyLine(2);
				        scanner.nextLine();
				        continue;
				    }
					
					switch (index) {
						case 1, 2:
							subCategory = subCategories.get(index - 1);

							break;
						default:
							renderSys.printStatus("해당하는 카테고리가 존재하지 않습니다.", false);
			
							continue;
					}


					renderSys.printInputFormMessage("추가할 메뉴의 이름을 입력해주세요. ex) 아메리카노");
					menuName = scanner.next();
					renderSys.printEmptyLine(2);
					
					renderSys.printInputFormMessage("추가할 메뉴의 가격을 입력하세요. ex) 4000");
					try {
						price = scanner.nextInt();
						renderSys.printEmptyLine(2);
				    } catch (InputMismatchException e) {
				    		renderSys.printStatus("가격을 숫자로만 입력해주세요.", false);
						renderSys.printEmptyLine(2);
				        scanner.nextLine();
				        continue;
				    }
					
					renderSys.printInputFormMessage("추가할 메뉴의 설명을 50자내로 입력하세요.");
					scanner.nextLine();
					description = scanner.nextLine();
					renderSys.printEmptyLine(2);
					renderSys.printInputFormMessage("얼음을 선택할 수 있는 메뉴인가요? ex) 1:가능 | 0:불가능");
					
					try {
					    iceable = scanner.nextInt();
					    if (iceable != 0 && iceable != 1) throw new InputMismatchException();
					} catch (InputMismatchException e) {
						renderSys.printStatus("0 또는 1만 입력해주세요.", false);
					    scanner.nextLine();
					    renderSys.printEmptyLine(2);
					    continue;
					}
					
					scanner.nextLine();
					
					while (true) {
						renderSys.printEmptyLine(2);
						renderSys.printSubTitle(renderSys.WIDTH, "메뉴 추가 내용 확인하기");
		                System.out.println("  대   분류   :  " + parentCategory);
		                System.out.println("  소   분류   :  "+ subCategory);
		                System.out.println("  메뉴 이름   :  " + menuName);
		                System.out.println("  메뉴 가격   :  " + price);
		                System.out.println("  아이스 가능 :  " + (iceable == 1 ? "가능" : "불가능"));
		                System.out.println("  메뉴 설명   :  " + description);
		                renderSys.printDivider(renderSys.WIDTH, false);
		                renderSys.printInputFormMessage("이대로 추가할까요? (Y: 예/N: 아니오)");
		                
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
		                        	renderSys.printStatus("잘못된 번호입니다.", false);
		                        	
		                        	break;
		                    }
		                } else {
		                		renderSys.printStatus("Y 또는 N만 입력해주세요.", false);
		                }
		            }
					
					try {
						menuDao.insertMenu(subCategory, menuName, price, description, iceable);
						renderSys.printEmptyLine(2);
						renderSys.printStatus("메뉴가 정상적으로 추가되었습니다.", true);
						renderSys.printEmptyLine(1);
					} catch (RuntimeException e1) {
						renderSys.printEmptyLine(2);
						renderSys.printStatus("메뉴 추가에 실패했습니다.", false);					
						System.out.println(e1.getMessage());
						renderSys.printEmptyLine(1);
					}
					
					break;
				}
			} else if (action == 3) {
				renderSys.printSubTitle(renderSys.WIDTH, "메뉴 삭제하기");
				
				while(true) {
					ArrayList<String> menuNameList = menuDao.getMenuNames();
					renderSys.printSingleMenu(menuNameList);
					renderSys.printDivider(renderSys.WIDTH, false);
					renderSys.printInputFormMessage("삭제할 메뉴의 번호를 입력하세요");
					
					int targetIndex;
					String tartgetMenu;
					
					try {
						targetIndex = scanner.nextInt();
						
						if (targetIndex < 1 || targetIndex > menuNameList.size()) {
							System.out.println("존재하는 메뉴번호가 아닙니다.");
							
							continue;
						}
						
						tartgetMenu = menuNameList.get(targetIndex - 1);
					} catch (InputMismatchException e) {
						renderSys.printEmptyLine(2);
						renderSys.printInputFormMessage("삭제할 메뉴의 번호를 숫자로 입력해주세요");
						
				        scanner.nextLine();
				        continue;
				    }

					tartgetMenu = menuNameList.get(targetIndex - 1);
					renderSys.printEmptyLine(2);
					renderSys.printInputFormMessage("메뉴 " + tartgetMenu + " 을 삭제하시겠습니까? ex) Y:예 / N:아니오");
					char answer = scanner.next().toUpperCase().charAt(0);
					
					switch (answer) {
						case 'Y':
							try {
								menuDao.deleteMenu(tartgetMenu);
								renderSys.printEmptyLine(2);
								renderSys.printStatus("메뉴가 성공적으로 삭제되었습니다.", true);
								renderSys.printEmptyLine(1);
								scanner.nextLine();
							} catch (RuntimeException e) {
								renderSys.printStatus("메뉴 삭제에 실패했습니다.", false);
								
								System.out.println(e.getMessage());
								
								continue;
							}
							
							break;
						case 'N':
							renderSys.printStatus("메뉴 삭제를 취소합니다.", true);
							scanner.nextLine();
							break;
						default:
							break;
					}
					
					break;
				}
				
				continue;
			} else if (action == 4) {
				renderSys.printSubTitle(renderSys.WIDTH, "메뉴 수정하기", false);
				
				while(true) {
					ArrayList<String> menuNameList = menuDao.getMenuNames();
					ArrayList<String> koreanColumns = new ArrayList<>();
					List<String> columnList = new ArrayList<>();
					String updateTargetMenu;
					
					while(true) {
						renderSys.printDivider(renderSys.WIDTH, false);
						renderSys.printSingleMenu(menuNameList);

						renderSys.printDivider(renderSys.WIDTH, false);
						renderSys.printInputFormMessage("수정할 메뉴의 번호를 입력하세요");
						
						int targetIndex;
						
						try {
							targetIndex = scanner.nextInt();
							
							if (targetIndex < 1 || targetIndex > menuNameList.size()) {
								renderSys.printStatus("존재하는 메뉴번호가 아닙니다.", false);
								
								continue;
							}
							
							updateTargetMenu = menuNameList.get(targetIndex - 1);
							renderSys.printEmptyLine(2);
						} catch (InputMismatchException e) {
							System.out.println("수정할 메뉴의 번호를 숫자로 입력해주세요");
							renderSys.printEmptyLine(2);
					        scanner.nextLine();
					        continue;
					    }

						renderSys.printInputFormMessage("메뉴 '" + updateTargetMenu + "' 을 수정하시겠습니까? ex) Y:예 / N:아니오");
						
						char menuSelectAnswer = scanner.next().toUpperCase().charAt(0);

						switch (menuSelectAnswer) {
							case 'Y':
								try {
									columnList = menuDao.getMenuHeader();
									
									for (String col : columnList) {
										koreanColumns.add(formatColumn(col));
									}
									
									break;
								} catch (RuntimeException e) {
									System.out.println(e.getMessage());
									renderSys.printStatus("메뉴 불러오기에 실패했습니다.", false);

									continue;
								}
							case 'N':
								renderSys.printEmptyLine(2);
								renderSys.printStatus("메뉴 '" + updateTargetMenu + "' 수정을 취소합니다. 다른 메뉴를 선택합니다.", true);
								
								continue;
							default:
								System.out.println("Y 또는 N 중에서 입력해주세요. 수정 메뉴 선택으로 돌아갑니다.");
								break;
						}
						
						if (menuSelectAnswer == 'Y') break;
					}
					
					String updateTargetcolumn;
					
					while (true) {
						renderSys.printEmptyLine(1);
						renderSys.printSubTitle(renderSys.WIDTH, "수정할 메뉴 옵션");
						renderSys.printSingleMenu(koreanColumns);
						renderSys.printDivider(renderSys.WIDTH, false);
						renderSys.printInputFormMessage("수정할 메뉴의 옵션 번호를 선택하세요.");
						
						int columnIndex;
						
						try {
							columnIndex = scanner.nextInt();
							scanner.nextLine();
							
							if (columnIndex < 1 || columnIndex > columnList.size()) {
								renderSys.printStatus("존재하지 않는 옵션입니다. 옳바른 번호를 입력해주세요", false);
								continue;
							}
							updateTargetcolumn = columnList.get(columnIndex - 1);
							
							break;
					    } catch (InputMismatchException e) {
					    		renderSys.printStatus("숫자로만 입력해주세요", false);
							
					        scanner.nextLine();
					        continue;
					    }
					}
					
					
					List<String> englishColumns = Arrays.asList("CATEGORY_NAME", "MENU_NAME", "DESCRIPTION");

					String updateString = null;
					Integer updateNumber = null;
					
					if (englishColumns.contains(updateTargetcolumn)) {
						renderSys.printEmptyLine(2);
						renderSys.printInputFormMessage(formatColumn(updateTargetcolumn) +"의 변경할 값을 입력해주세요.");
						
						updateString = scanner.nextLine();
					} else {
						System.out.print(formatColumn(updateTargetcolumn) +"의 변경할 값의 숫자를 입력해주세요.");
						
						if (updateTargetcolumn.equals("IS_SOLDOUT")) {
							renderSys.printInputFormMessage("0: 판매 가능 | 1: 메뉴 품절");
						} else if (updateTargetcolumn.equals("ICEABLE")){
							renderSys.printInputFormMessage("0: 아이스 메뉴로만 | 0: 핫 메뉴로만");
						}
						
						updateNumber = scanner.nextInt();
						scanner.nextLine();
					}
					

					renderSys.printEmptyLine(2);
					renderSys.printTitle(renderSys.WIDTH, "변경된 내용 확인하기");
					System.out.println("수정할 메뉴  :  " + updateTargetMenu);
					System.out.println("수정할 설정  :  " + formatColumn(updateTargetcolumn));
					if (updateString != null) {
					    System.out.println("변경된 내용  :  " + updateString);
					} else {
					    System.out.println("변경된 내용  :  " + String.valueOf(updateNumber));
					}
					renderSys.printDivider(renderSys.WIDTH, false);
					renderSys.printEmptyLine(2);
					renderSys.printInputFormMessage("위 내용으로 수정할까요? ex) Y:예 / N:아니오 ");
					
					String confirm = scanner.nextLine().trim().toUpperCase();
					
					if (!confirm.equals("Y")) {
						renderSys.printStatus("수정을 취소합니다. 수정할 다른 메뉴를 선택해주세요", true);
						renderSys.printEmptyLine(2);
						continue;
					}
					
					try {
						if (updateString != null) {
							menuDao.updateMenu(updateTargetcolumn, updateString, updateTargetMenu);
						} else {
							menuDao.updateMenu(updateTargetcolumn, updateNumber, updateTargetMenu);
						}
						
						renderSys.printEmptyLine(2);
						renderSys.printStatus("'" + updateTargetMenu + "' 업데이트가 완료되었습니다.", true);
					} catch(RuntimeException e) {
						renderSys.printEmptyLine(2);
						System.out.println(e.getMessage());
						renderSys.printStatus("'" + updateTargetMenu + "' 업데이트에 실패했습니다.", false);
						
						continue;
					}
					
					renderSys.printEmptyLine(2);
					renderSys.printInputFormMessage("다른 메뉴도 계속 수정하시겠습니까? ex) Y:예 / N:아니오");
					
					char nextStep = scanner.nextLine().trim().toUpperCase().charAt(0);
					
					if (nextStep == 'Y') {
						continue;
					} else {
						renderSys.printEmptyLine(1);
						break;
					}
				}
			} else {	
				break;
			}
		}
	}
	
	public static String formatColumn(String engColumn) {
		String korColumn = "";
		
		switch(engColumn) {
			case "CATEGORY_NAME":
				korColumn = "카테고리";
				break;
			case "MENU_NAME":
				korColumn = "메뉴이름";
				break;
			case "PRICE":
				korColumn = "메뉴가격";
				break;
			case "IS_SOLDOUT":
				korColumn = "품절여부";
				break;
			case "ICEABLE":
				korColumn = "아이스가능";
				break;
			case "DESCRIPTION":
				korColumn = "메뉴설명";
				break;
		}

		return korColumn;
	};
	
	public static void printMenuRow(
	    String categoryName,
	    String menuName,
	    int price,
	    int isSoldout,
	    int iceable,
	    String description
	) {
	    System.out.printf("%-12s %-14s %-8d %-10s %-10s %-24s\n",
	        categoryName,
	        menuName,
	        price,
	        (isSoldout == 1 ? "Yes" : "No"),
	        (iceable == 1 ? "Yes" : "No"),
	        description);
	};
}
