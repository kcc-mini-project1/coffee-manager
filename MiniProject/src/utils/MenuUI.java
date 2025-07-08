package utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dao.MenuDao;

public class MenuUI {
	private static Scanner scanner;
	
	public MenuUI (Scanner scanner) {
		MenuUI.scanner = scanner;
	}
	static MenuDao menuDao = new MenuDao();
	public OrderUI orderUI = new OrderUI(scanner);
	public ErrorUtil errorUtil = new ErrorUtil;
	
    public static void showError(SQLException e) {
        String friendlyMsg = ErrorUtil.getFriendlyErrorMessage(e);
        RenderSystem.printErrorMessage(friendlyMsg);
    };
    
	public void start() throws Exception {
		while (true) {
			RenderSystem.printEmptyLine(1);
			RenderSystem.printTitle(RenderSystem.WIDTH, "메뉴 관리 시스템");
			System.out.print("1. 메뉴 조회하기\n"
					+ "2. 메뉴 추가하기\n"
					+ "3. 메뉴 삭제하기\n"
					+ "4. 메뉴 수정하기\n"
					+ "q. 이전 단계로\n");
			RenderSystem.printDivider(RenderSystem.WIDTH, false);
			RenderSystem.printInputForm();
			
			String input = scanner.nextLine().trim();
			
			if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("Q")) {
		        System.out.println("이전 단계로 이동합니다.");
		        break;
		    }
			
			int action;
			try {
		        action = Integer.parseInt(input);
		    } catch (NumberFormatException e) {
		        System.out.println("숫자 혹은 'q'만 입력해주세요.");
		        continue;
		    }
			
		    if (action < 1 || action > 4) {
		        System.out.println("1 ~ 4 사이 숫자만 입력해주세요.");
		        continue;
		    }
		    
			RenderSystem.printEmptyLine(1);
			if (action == 1) {
				orderUI.printMenuBoard();
				
				System.out.println("\n[엔터를 누르면 이전 메뉴로 돌아갑니다]");
				scanner.nextLine();
				
				continue;
			} else if (action == 2) {
				RenderSystem.printSubTitle(RenderSystem.WIDTH, "메뉴 추가하기");
				
				List<String> subCategories = new ArrayList<>();
				List<String> categories = new ArrayList<>();
				String parentCategory, subCategory, menuName, description;
				int price, iceable;
				
				while(true) {
				    parentCategory = null;
				    subCategory = null;

					categories = menuDao.getParentCategories();
					RenderSystem.printSingleMenu(categories);
					RenderSystem.printDivider(RenderSystem.WIDTH, false);
					RenderSystem.printInputFormMessage("추가할 메뉴의 대분류 번호를 선택하세요.");
					
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

							break;
						default:
							System.out.println("해당하는 카테고리가 존재하지 않습니다.");
			
							continue;
					}
					
					try {
						subCategories = menuDao.getChildCategories(parentCategory);
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
						
						continue;
					}
					
					RenderSystem.printEmptyLine(1);
					RenderSystem.printDivider(RenderSystem.WIDTH, false);
					RenderSystem.printSingleMenu(subCategories);
					RenderSystem.printDivider(RenderSystem.WIDTH, false);
					RenderSystem.printInputFormMessage("추가할 메뉴의 소분류 번호를 선택하세요.");
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

							break;
						default:
							System.out.println("해당하는 카테고리가 존재하지 않습니다.");
			
							continue;
					}

					RenderSystem.printEmptyLine(1);
					RenderSystem.printInputFormMessage("추가할 메뉴의 이름을 입력해주세요. ex) 아메리카노");
					menuName = scanner.next();

					RenderSystem.printEmptyLine(1);
					RenderSystem.printInputFormMessage("추가할 메뉴의 가격을 입력하세요. ex) 4000");
					try {
						price = scanner.nextInt();
				    } catch (InputMismatchException e) {
						System.out.println("가격을 숫자로만 입력해주세요");
						
				        scanner.nextLine();
				        continue;
				    }
					
					RenderSystem.printEmptyLine(1);
					RenderSystem.printInputFormMessage("추가할 메뉴의 설명을 50자내로 입력하세요.");
					scanner.nextLine();
					description = scanner.nextLine();
					
					RenderSystem.printEmptyLine(1);
					RenderSystem.printInputFormMessage("얼음을 선택할 수 있는 메뉴인가요? ex) 1:가능 | 0:불가능");
					
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
						RenderSystem.printSubTitle(RenderSystem.WIDTH, "메뉴 추가 내용 확인하기");
		                System.out.printf("%-15s : %s\n", "상위 카테고리", parentCategory);
		                System.out.printf("%-15s : %s\n", "하위 카테고리", subCategory);
		                System.out.printf("%-15s : %s\n", "메뉴 이름", menuName);
		                System.out.printf("%-15s : %d\n", "가격", price);
		                System.out.printf("%-15s : %s\n", "아이스 가능", iceable == 1 ? "가능" : "불가능");
		                System.out.printf("%-15s : %s\n", "설명", description);
		                RenderSystem.printDivider(RenderSystem.WIDTH, false);
		                RenderSystem.printInputFormMessage("이대로 추가할까요? (Y: 예/N: 아니오)");
		                
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
						menuDao.insertMenu(subCategory, menuName, price, description, iceable);
					
						System.out.println("메뉴가 정상적으로 추가되었습니다.");
					} catch (RuntimeException e1) {
						System.out.println("메뉴 추가에 실패했습니다.");
						System.out.println(e1.getMessage());
					}
					
					break;
				}
			} else if (action == 3) {
				RenderSystem.printSubTitle(RenderSystem.WIDTH, "메뉴 삭제하기");
				
				while(true) {
					ArrayList<String> menuNameList = menuDao.getMenuNames();
					RenderSystem.printSingleMenu(menuNameList);
					RenderSystem.printDivider(RenderSystem.WIDTH, false);
					RenderSystem.printInputFormMessage("삭제할 메뉴의 번호를 입력하세요");
					
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
						RenderSystem.printEmptyLine(1);
						RenderSystem.printInputFormMessage("삭제할 메뉴의 번호를 숫자로 입력해주세요");
						
				        scanner.nextLine();
				        continue;
				    }

					tartgetMenu = menuNameList.get(targetIndex - 1);
					RenderSystem.printEmptyLine(1);
					RenderSystem.printInputFormMessage("메뉴 " + tartgetMenu + " 을 삭제하시겠습니까? ex) Y:예 / N:아니오");
					char answer = scanner.next().toUpperCase().charAt(0);
					
					switch (answer) {
						case 'Y':
							try {
								menuDao.deleteMenu(tartgetMenu);
								
								RenderSystem.printEmptyLine(1);
								System.out.println("\u2714" + tartgetMenu + " 메뉴가 성공적으로 삭제되었습니다.");
							} catch (RuntimeException e) {
								System.out.println(tartgetMenu + "메뉴 삭제에 실패했습니다.");
								System.out.println(e.getMessage());
								
								continue;
							}
							
							break;
						case 'N':
							System.out.println("\u2714 메뉴 " + tartgetMenu + " 삭제를 취소합니다.");
							break;
						default:
							break;
					}
					
					break;
				}
				
				continue;
			} else if (action == 4) {
				RenderSystem.printSubTitle(RenderSystem.WIDTH, "메뉴 수정하기", false);
				
				while(true) {
					ArrayList<String> menuNameList = menuDao.getMenuNames();
					ArrayList<String> koreanColumns = new ArrayList<>();
					List<String> columnList = new ArrayList<>();
					String updateTargetMenu;
					
					while(true) {
						RenderSystem.printDivider(RenderSystem.WIDTH, false);
						RenderSystem.printSingleMenu(menuNameList);

						RenderSystem.printDivider(RenderSystem.WIDTH, false);
						RenderSystem.printInputFormMessage("수정할 메뉴의 번호를 입력하세요");
						
						int targetIndex;
						
						try {
							targetIndex = scanner.nextInt();
							
							if (targetIndex < 1 || targetIndex > menuNameList.size()) {
								System.out.println("존재하는 메뉴번호가 아닙니다.");
								
								continue;
							}
							
							updateTargetMenu = menuNameList.get(targetIndex - 1);
						} catch (InputMismatchException e) {
							System.out.println("수정할 메뉴의 번호를 숫자로 입력해주세요");
							
					        scanner.nextLine();
					        continue;
					    }

						RenderSystem.printEmptyLine(1);
						RenderSystem.printInputFormMessage("메뉴 '" + updateTargetMenu + "' 을 수정하시겠습니까? ex) Y:예 / N:아니오");
						
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
									
									System.out.println("메뉴 불러오기에 실패했습니다.");

									continue;
								}
							case 'N':
								RenderSystem.printEmptyLine(1);
								System.out.println("메뉴 '" + updateTargetMenu + "' 수정을 취소합니다. 다른 메뉴를 선택합니다.");
								
								continue;
							default:
								System.out.println("Y 또는 N 중에서 입력해주세요. 수정 메뉴 선택으로 돌아갑니다.");
								break;
						}
						
						if (menuSelectAnswer == 'Y') break;
					}
					
					String updateTargetcolumn;
					
					while (true) {
						RenderSystem.printEmptyLine(1);
						RenderSystem.printSubTitle(RenderSystem.WIDTH, "수정할 메뉴 옵션");
						RenderSystem.printSingleMenu(koreanColumns);
						RenderSystem.printEmptyLine(1);
						RenderSystem.printInputForm();
						RenderSystem.printInputFormMessage("수정할 메뉴의 옵션 번호를 선택하세요.");
						
						int columnIndex;
						
						try {
							columnIndex = scanner.nextInt();
							scanner.nextLine();
							
							if (columnIndex < 1 || columnIndex > columnList.size()) {
								System.out.println("존재하지 않는 옵션입니다. 옳바른 번호를 입력해주세요");
								continue;
							}
							updateTargetcolumn = columnList.get(columnIndex - 1);
							
							break;
					    } catch (InputMismatchException e) {
							System.out.println("숫자로만 입력해주세요");
							
					        scanner.nextLine();
					        continue;
					    }
					}
					
					
					List<String> englishColumns = Arrays.asList("CATEGORY_NAME", "MENU_NAME", "DESCRIPTION");

					String updateString = null;
					Integer updateNumber = null;
					
					if (englishColumns.contains(updateTargetcolumn)) {
						RenderSystem.printEmptyLine(1);
						RenderSystem.printInputFormMessage(formatColumn(updateTargetcolumn) +"의 변경할 값을 입력해주세요.");
						
						updateString = scanner.nextLine();
					} else {
						System.out.print(formatColumn(updateTargetcolumn) +"의 변경할 값의 숫자를 입력해주세요.");
						
						if (updateTargetcolumn.equals("IS_SOLDOUT")) {
							RenderSystem.printInputFormMessage("0: 판매 가능 | 1: 메뉴 품절");
						} else if (updateTargetcolumn.equals("ICEABLE")){
							RenderSystem.printInputFormMessage("0: 아이스 메뉴로만 | 0: 핫 메뉴로만");
						}
						
						updateNumber = scanner.nextInt();
						scanner.nextLine();
					}
					

					RenderSystem.printEmptyLine(1);
					RenderSystem.printTitle(RenderSystem.WIDTH, "변경된 내용 확인하기");
					System.out.printf("%-10s : %s\n", "수정할 메뉴", updateTargetMenu);
					System.out.printf("%-10s : %s\n", "수정할 설정", formatColumn(updateTargetcolumn));
					if (updateString != null) {
					    System.out.printf("%-10s : %s\n", "변경된 내용", updateString);
					} else {
					    System.out.printf("%-10s : %s\n", "변경된 내용", String.valueOf(updateNumber));
					}
					RenderSystem.printDivider(1, false);
					RenderSystem.printEmptyLine(1);
					RenderSystem.printInputFormMessage("위 내용으로 수정할까요? ex) Y:예 / N:아니오 ");
					
					String confirm = scanner.nextLine().trim().toUpperCase();
					
					if (!confirm.equals("Y")) {
						System.out.println("수정을 취소했습니다. 수정할 메뉴를 선택해주세요");
						continue;
					}
					
					try {
						if (updateString != null) {
							menuDao.updateMenu(updateTargetcolumn, updateString, updateTargetMenu);
						} else {
							menuDao.updateMenu(updateTargetcolumn, updateNumber, updateTargetMenu);
						}
						
						RenderSystem.printEmptyLine(1);
						System.out.println(updateTargetMenu + " 메뉴가 성공적으로 업데이트 되었습니다.");
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
						System.out.println(updateTargetMenu + " 메뉴 업데이트에 실패했습니다.");
						
						continue;
					}
					
					RenderSystem.printEmptyLine(1);
					RenderSystem.printInputFormMessage("다른 메뉴도 계속 수정하시겠습니까? ex) Y:예 / N:아니오");
					
					char nextStep = scanner.nextLine().trim().toUpperCase().charAt(0);
					
					if (nextStep == 'Y') {
						continue;
					} else {
						break;
					}
				}
			} else {	
				break;
			}
		}
		
		scanner.close();
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
