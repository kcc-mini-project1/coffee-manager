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

		List<String> menuList = Arrays.asList("메뉴 조회하기", "메뉴 추가하기", "메뉴 삭제하기", "메뉴 수정하기", "이전 단계로");
		
		while (true) {
			System.out.println();
			title.renderTitle("메뉴 관리시스템");
			options.multiLine(menuList);
			System.out.println();
			System.out.println("원하시는 작업 번호를 입력해주세요");
			System.out.print(">>> ");
			int action;
			
			try {
				action = scanner.nextInt();
				scanner.nextLine();
		    } catch (InputMismatchException e) {
				System.out.println("숫자만 입력해주세요");
				
		        scanner.nextLine();
		        continue;
		    }
			
			if (action == 1 || action == 2 || action == 3 || action == 4 || action == 5) {
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
						System.out.println("추가할 메뉴의 대분류 번호를 선택하세요.");
						System.out.print(">>> ");
						categories = menuDao.getParentCategories();
						options.singleLine(categories);
						
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

						System.out.println();
						System.out.println("추가할 메뉴의 소분류 번호를 선택하세요.");
						System.out.print(">>> ");
						
						try {
							subCategories = menuDao.getChildCategories(parentCategory);
						} catch (RuntimeException e) {
							System.out.println(e.getMessage());
							
							continue;
						}
						
						options.singleLine(subCategories);
						
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
			                System.out.println("\n입력한 내용은 다음과 같습니다:");
			                System.out.println("===============================================");
			                System.out.printf("%-15s : %s\n", "상위 카테고리", parentCategory);
			                System.out.printf("%-15s : %s\n", "하위 카테고리", subCategory);
			                System.out.printf("%-15s : %s\n", "메뉴 이름", menuName);
			                System.out.printf("%-15s : %d\n", "가격", price);
			                System.out.printf("%-15s : %s\n", "아이스 가능", iceable == 1 ? "가능" : "불가능");
			                System.out.printf("%-15s : %s\n", "설명", description);
			                System.out.println("===============================================");

			                System.out.print("이대로 추가할까요? (Y/N)");
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
						
						System.out.println();
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
					title.renderTitle("메뉴 수정하기");
					System.out.println();
					
					DataSource ds = new DataSource();
					Connection con = null;
					
					ArrayList<String> menuNameList = menuDao.getMenuNames();
					
					while(true) {
						System.out.println();
						title.printLine();
						options.singleLine(menuNameList);
						title.printLine();
						
						System.out.println();
						System.out.println("수정할 메뉴의 번호를 입력하세요");
						System.out.print(">>> ");
						
						int targetIndex;
						String targetMenu;
						
						try {
							targetIndex = scanner.nextInt();
							
							if (targetIndex < 1 || targetIndex > menuNameList.size()) {
								System.out.println("존재하는 메뉴번호가 아닙니다.");
								
								continue;
							}
							
							targetMenu = menuNameList.get(targetIndex - 1);
						} catch (InputMismatchException e) {
							System.out.println("수정할 메뉴의 번호를 숫자로 입력해주세요");
							
					        scanner.nextLine();
					        continue;
					    }

						System.out.println();
						System.out.println("메뉴 '" + targetMenu + "' 을 수정하시겠습니까? ex) Y:예 / N:아니오");
						System.out.print(">>> ");
						
						char answer = scanner.next().toUpperCase().charAt(0);
						
						switch (answer) {
							case 'Y':
								List<String> columnList;
								ArrayList<String> korColumns = new ArrayList<>();
								
								try {
									columnList = menuDao.getMenuHeader();
									
									for (String col : columnList) {
										korColumns.add(formatColumn(col));
									}
								} catch (RuntimeException e) {
									System.out.println(e.getMessage());
									
									System.out.println("메뉴 불러오기에 실패했습니다.");
									
									continue;
								}
								
								System.out.println();
								title.printLine();
								options.singleLine(korColumns);
								title.printLine();
								
								System.out.println();
								System.out.println("해당 메뉴의 수정할 내용을 선택하세요.");
								System.out.print(">>> ");
								int columnIndex;
								String columnName;
								
								try {
									columnIndex = scanner.nextInt();
									scanner.nextLine();
									
									if (columnIndex < 1 || columnIndex > columnList.size()) {
										System.out.println("존재하지 않는 컬럼입니다. 수정 메뉴 선택으로 돌아갑니다.");
										
										continue;
									}
									columnName = columnList.get(columnIndex - 1);
							    } catch (InputMismatchException e) {
									System.out.println("숫자로만 입력해주세요");
									
							        scanner.nextLine();
							        continue;
							    }
								

							
								List<String> stringColumns = Arrays.asList("CATEGORY_NAME", "MENU_NAME", "DESCRIPTION");

								String updateString = null;
								Integer updateNumber = null;
								
								if (stringColumns.contains(columnName)) {
									System.out.println();
									System.out.println(formatColumn(columnName) +"의 변경할 값을 입력해주세요.");
									System.out.print(">>> ");
									
									updateString = scanner.nextLine();
								} else {
									System.out.print(formatColumn(columnName) +"의 변경할 값의 숫자를 입력해주세요.");
									
									if (columnName.equals("IS_SOLDOUT")) {
										System.out.println("0: 판매 가능 | 1: 메뉴 품절");
										System.out.print(">>> ");
									} else if (columnName.equals("ICEABLE")){
										System.out.println("0: 아이스 메뉴로만 | 0: 핫 메뉴로만");
										System.out.print(">>> ");
									}
									
									updateNumber = scanner.nextInt();
									scanner.nextLine();
								}
								

								System.out.println();
								title.renderTitle("변경된 내용 확인하기");
								System.out.printf("%-10s : %s\n", "수정할 메뉴", targetMenu);
								System.out.printf("%-10s : %s\n", "수정할 설정", formatColumn(columnName));
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
									System.out.println("수정을 취소했습니다. 수정 메뉴 선택 페이지로 넘어갑니다.");
									continue;
								}
								
								try {
									if (updateString != null) {
										menuDao.updateMenu(columnName, updateString, targetMenu);
									} else {
										menuDao.updateMenu(columnName, updateNumber, targetMenu);
									}
									
									System.out.println();
									System.out.println(targetMenu + " 메뉴가 성공적으로 업데이트 되었습니다.");
								} catch(RuntimeException e) {
									System.out.println(e.getMessage());
									System.out.println(targetMenu + " 메뉴 업데이트에 실패했습니다.");
									
									continue;
								}
								
								System.out.println();
								System.out.println("계속 수정하시겠습니까? ex) Y:예 / N:아니오");
								System.out.print(">>> ");
								
								char next = scanner.nextLine().trim().toUpperCase().charAt(0);
								if (next != 'Y') {
									continue;
								}
								
								break;
							case 'N':
								System.out.println();
								System.out.println("메뉴 " + targetMenu + " 수정을 취소합니다.");
								
								break;
							default:
								System.out.println("Y 또는 N 중에서 입력해주세요.");
								
								continue;
						}
						
						break;
					}
				} else if (action == 5) {	
					break;
				}
			} else {
				System.out.println("잘못된 입력값입니다.");
				
				continue;
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
