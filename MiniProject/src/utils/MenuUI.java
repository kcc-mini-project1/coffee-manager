package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dao.DataSource;
import dao.MenuDao;

public class MenuUI {
	static RenderTitle title = new RenderTitle();
	static RenderOptions options = new RenderOptions();
	static MenuDao menuDao = new MenuDao();
	
	public static void startMain() throws Exception {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println();
			title.renderTitle("메뉴 관리 시스템");
			System.out.println("1. 메뉴 조회하기");
			System.out.println("2. 메뉴 추가하기");
			System.out.println("3. 메뉴 수정하기");
			System.out.println("4. 메뉴 삭제하기");
			System.out.println("q. 이전 단계로");
			title.printLine();
			System.out.println("원하시는 작업 번호를 입력해주세요");
			System.out.print(">>> ");
			
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
		    
			System.out.println();
			if (action == 1) {
				title.renderTitle("메뉴 조회하기");
				
				try {
					List<String> menuHeader = menuDao.getMenuHeader();
					
					for (String col : menuHeader) {
						System.out.print(formatColumnName(col));
					}
					
					System.out.println();
					title.printLine();
					
					menuDao.printMenuRows();
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
					
					break;
				} finally {
					System.out.println("\n[엔터를 누르면 이전 메뉴로 돌아갑니다]");
					scanner.nextLine();
				}
				
				continue;
			} else if (action == 2) {
				title.renderTitle("메뉴 추가하기");
				
				List<String> subCategories = new ArrayList<>();
				List<String> categories = new ArrayList<>();
				String parentCategory, subCategory, menuName, description;
				int price, iceable;
				
				while(true) {
				    parentCategory = null;
				    subCategory = null;

					categories = menuDao.getParentCategories();
					options.singleLine(categories);
					title.printLine();
					System.out.println("추가할 메뉴의 대분류 번호를 선택하세요.");
					System.out.print(">>> ");
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
					
					System.out.println();
					title.printLine();
					options.singleLine(subCategories);
					title.printLine();
					System.out.println("추가할 메뉴의 소분류 번호를 선택하세요.");
					System.out.print(">>> ");
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

					System.out.println();
					System.out.println("추가할 메뉴의 이름을 입력해주세요. ex) 아메리카노");
					System.out.print(">>> ");
					menuName = scanner.next();
			
					System.out.println();
					System.out.println("추가할 메뉴의 가격을 입력하세요. ex) 4000");
					System.out.print(">>> ");
					try {
						price = scanner.nextInt();
				    } catch (InputMismatchException e) {
						System.out.println("가격을 숫자로만 입력해주세요");
						
				        scanner.nextLine();
				        continue;
				    }
					
					System.out.println();
					System.out.println("추가할 메뉴의 설명을 50자내로 입력하세요.");
					System.out.print(">>> ");
					scanner.nextLine();
					description = scanner.nextLine();
					
					System.out.println();
					System.out.println("얼음을 선택할 수 있는 메뉴인가요? ex) 1:가능 | 0:불가능");
					System.out.print(">>> ");
					
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
						title.renderTitle("메뉴 추가 내용 확인하기");
		                System.out.printf("%-15s : %s\n", "상위 카테고리", parentCategory);
		                System.out.printf("%-15s : %s\n", "하위 카테고리", subCategory);
		                System.out.printf("%-15s : %s\n", "메뉴 이름", menuName);
		                System.out.printf("%-15s : %d\n", "가격", price);
		                System.out.printf("%-15s : %s\n", "아이스 가능", iceable == 1 ? "가능" : "불가능");
		                System.out.printf("%-15s : %s\n", "설명", description);
		                title.printLine();

		                System.out.println("이대로 추가할까요? (Y/N)");
		                System.out.print(">>> ");
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
				title.renderTitle("메뉴 삭제하기");
				
				while(true) {
					ArrayList<String> menuNameList = menuDao.getMenuNames();
					options.singleLine(menuNameList);
					title.printLine();
					System.out.println("삭제할 메뉴의 번호를 입력하세요");
					System.out.print(">>> ");
					
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
						System.out.println();
						System.out.println("삭제할 메뉴의 번호를 숫자로 입력해주세요");
						System.out.print(">>> ");
						
				        scanner.nextLine();
				        continue;
				    }

					tartgetMenu = menuNameList.get(targetIndex - 1);
					System.out.println();
					System.out.println("메뉴 " + tartgetMenu + " 을 삭제하시겠습니까? ex) Y:예 / N:아니오");
					System.out.print(">>> ");
					char answer = scanner.next().toUpperCase().charAt(0);
					
					switch (answer) {
						case 'Y':
							try {
								menuDao.deleteMenu(tartgetMenu);
								
								System.out.println();
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
				title.renderTitle("메뉴 수정하기", false);
				
				while(true) {
					ArrayList<String> menuNameList = menuDao.getMenuNames();
					ArrayList<String> koreanColumns = new ArrayList<>();
					List<String> columnList = new ArrayList<>();
					String updateTargetMenu;
					
					while(true) {
						title.printLine();
						options.singleLine(menuNameList);
						title.printLine();
						System.out.println("수정할 메뉴의 번호를 입력하세요");
						System.out.print(">>> ");
						
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

						System.out.println();
						System.out.println("메뉴 '" + updateTargetMenu + "' 을 수정하시겠습니까? ex) Y:예 / N:아니오");
						System.out.print(">>> ");
						
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
								System.out.println();
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
						System.out.println();
						title.renderTitle("수정할 메뉴 옵션");
						options.singleLine(koreanColumns);
						title.printLine();
						System.out.println("수정할 메뉴의 옵션 번호를 선택하세요.");
						System.out.print(">>> ");
						
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
						System.out.println();
						System.out.println(formatColumn(updateTargetcolumn) +"의 변경할 값을 입력해주세요.");
						System.out.print(">>> ");
						
						updateString = scanner.nextLine();
					} else {
						System.out.print(formatColumn(updateTargetcolumn) +"의 변경할 값의 숫자를 입력해주세요.");
						
						if (updateTargetcolumn.equals("IS_SOLDOUT")) {
							System.out.println("0: 판매 가능 | 1: 메뉴 품절");
							System.out.print(">>> ");
						} else if (updateTargetcolumn.equals("ICEABLE")){
							System.out.println("0: 아이스 메뉴로만 | 0: 핫 메뉴로만");
							System.out.print(">>> ");
						}
						
						updateNumber = scanner.nextInt();
						scanner.nextLine();
					}
					

					System.out.println();
					title.renderTitle("변경된 내용 확인하기");
					System.out.printf("%-10s : %s\n", "수정할 메뉴", updateTargetMenu);
					System.out.printf("%-10s : %s\n", "수정할 설정", formatColumn(updateTargetcolumn));
					if (updateString != null) {
					    System.out.printf("%-10s : %s\n", "변경된 내용", updateString);
					} else {
					    System.out.printf("%-10s : %s\n", "변경된 내용", String.valueOf(updateNumber));
					}
					title.printLine();
				
					System.out.println();
					System.out.println("위 내용으로 수정할까요? ex) Y:예 / N:아니오 ");
					System.out.print(">>> ");
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
						
						System.out.println();
						System.out.println(updateTargetMenu + " 메뉴가 성공적으로 업데이트 되었습니다.");
					} catch(RuntimeException e) {
						System.out.println(e.getMessage());
						System.out.println(updateTargetMenu + " 메뉴 업데이트에 실패했습니다.");
						
						continue;
					}
					
					System.out.println();
					System.out.println("다른 메뉴도 계속 수정하시겠습니까? ex) Y:예 / N:아니오");
					System.out.print(">>> ");
					
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
	}
	
	public static String formatColumn(String engColumn) {
		String korColumn = null;
		
		if (engColumn.equals("CATEGORY_NAME")) {
			korColumn = "카테고리";
		} else if (engColumn.equals("MENU_NAME")) {
			korColumn = "메뉴이름";
		} else if (engColumn.equals("PRICE")) {
			korColumn = "메뉴가격";
		} else if (engColumn.equals("IS_SOLDOUT")) {
			korColumn = "품절여부";
		} else if (engColumn.equals("ICEABLE")) {
			korColumn = "아이스가능";
		} else if (engColumn.equals("DESCRIPTION")) {
			korColumn = "메뉴설명";
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
	
	public static String formatColumnName(String columnName) {
	    return switch (columnName) {
	        case "CATEGORY_NAME" -> String.format("%-12s", "카테고리");
	        case "MENU_NAME" -> String.format("%-14s", "메뉴이름");
	        case "PRICE" -> String.format("%-8s", "메뉴가격");
	        case "IS_SOLDOUT" -> String.format("%-10s", "품절여부");
	        case "ICEABLE" -> String.format("%-10s", "아이스");
	        case "DESCRIPTION" -> String.format("%-24s", "메뉴설명");
	        default -> columnName;
	    };
	}
	
	public static ArrayList<String> getMenuNames() throws Exception {
		DataSource ds = new DataSource();
		Connection con = null;

		ArrayList<String> menuList = new ArrayList<>();
		
		try {
			con = ds.getConnection();
			
			String sql = "SELECT menu_name FROM menus";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("메뉴 정보를 모두 확인했습니다.");
			
			while(rs.next()) {
				String menuName = rs.getString("menu_name");
				menuList.add(menuName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ds.closeConnection(con);
		}
		
		return menuList;
	}
}
