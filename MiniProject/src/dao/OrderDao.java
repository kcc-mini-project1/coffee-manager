package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderDao {
    private DataSource ds = new DataSource();

    	//1.메뉴판
        public static class MenuItem {
            public String parentName;
            public String categoryName;
            public String menuName;
            public int price;
            public String description;
            public int isSoldout;
            public int iceable;
        }

        public List<MenuItem> getMenuItems() {
            List<MenuItem> menuList = new ArrayList<>();

            String sql = "SELECT c.parent_name AS parentName, "
                       + "       m.category_name AS categoryName, "
                       + "       m.menu_name AS menuName, "
                       + "       m.price AS price, "
                       + "       m.description AS description, "
                       + "       m.is_soldout AS isSoldout, "
                       + "       m.iceable AS iceable "
                       + "FROM menus m "
                       + "JOIN categories c ON m.category_name = c.category_name "
                       + "ORDER BY parent_name DESC, m.category_name";

            try (Connection con = ds.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    MenuItem item = new MenuItem();
                    item.parentName = rs.getString("parentName");
                    item.categoryName = rs.getString("categoryName");
                    item.menuName = rs.getString("menuName");
                    item.price = rs.getInt("price");
                    item.description = rs.getString("description");
                    item.isSoldout = rs.getInt("isSoldout");
                    item.iceable = rs.getInt("iceable");

                    menuList.add(item);
                }

            } catch (SQLException e) {
                System.out.println("메뉴 조회 중 오류 발생");
                e.printStackTrace();
            }

            return menuList;
        }

    
        //2.주문하기
        public boolean insertOrder(String customerId, String menuName, String request, boolean isIce, boolean useCoupon) {
            try (Connection con = ds.getConnection()) {
                String orderSql = "INSERT INTO orders (order_id, order_date, customer_id, menu_name, request, is_ice, use_coupon) " +
                                  "VALUES (order_seq.nextval, SYSDATE, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(orderSql);
                stmt.setString(1, customerId);
                stmt.setString(2, menuName);
                stmt.setString(3, request);
                stmt.setInt(4, isIce ? 1 : 0);
                stmt.setInt(5, useCoupon ? 1 : 0);
                stmt.executeUpdate();

                if (useCoupon && !customerId.equals("비회원")) {
                    String updateCouponSql = "UPDATE members SET coupon = coupon - 1 WHERE customer_id = ?";
                    PreparedStatement updateStmt = con.prepareStatement(updateCouponSql);
                    updateStmt.setString(1, customerId);
                    updateStmt.executeUpdate();
                }

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public ResultSet getAvailableMenus(String categoryName) throws SQLException {
            Connection con = ds.getConnection();
            String sql = "SELECT menu_name, price FROM menus WHERE category_name = ? AND is_soldout = 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, categoryName);
            return stmt.executeQuery();
        }

        public ResultSet getUserCoupon(String customerId) throws SQLException {
            Connection con = ds.getConnection();
            String sql = "SELECT coupon FROM members WHERE customer_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, customerId);
            return stmt.executeQuery();
        }

    
        // 3. 주문내역 확인하기
        public ResultSet getOrderList() throws SQLException {
            Connection con = ds.getConnection();
            String sql = "SELECT order_id AS orderId, "
                       + "customer_id AS customerId, "
                       + "menu_name AS menuName,"
                       + "order_date AS orderDate "
                       + "FROM orders "
                       + "WHERE TRUNC(order_date) = TRUNC(SYSDATE) "
                       + "ORDER BY order_date DESC ";
            PreparedStatement stmt = con.prepareStatement(sql);
            return stmt.executeQuery();
        }
    
    
    
    
        // 4. 주문 취소하기 
        public ResultSet getRecentOrders() throws SQLException {
            Connection con = ds.getConnection();
            String sql = "SELECT order_id, customer_id, menu_name, order_date "
                       + "FROM orders "
                       + "WHERE order_date BETWEEN (SYSDATE- 1/24) AND SYSDATE "
                       + "ORDER BY order_date DESC ";
            PreparedStatement stmt = con.prepareStatement(sql);
            return stmt.executeQuery();
        }

        // 특정 주문 상세 조회
        public ResultSet getOrderById(int orderId) throws SQLException {
            Connection con = ds.getConnection();
            String sql = "SELECT order_id, order_date, customer_id, menu_name, request, is_ice, use_coupon "
                       + "FROM orders WHERE order_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, orderId);
            return stmt.executeQuery();
        }

        // 주문 취소
        public int deleteOrderById(int orderId) throws SQLException {
            Connection con = ds.getConnection();
            String sql = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, orderId);
            return stmt.executeUpdate();
        }
    
    
    
        // 5. 포인트 확인
        public ResultSet getMemberPointInfo(String customerId) throws SQLException {
            Connection con = ds.getConnection();
            String sql = "SELECT customer_id, stamp, coupon, FLOOR(stamp / 10) AS expectedCoupon " +
                         "FROM members WHERE customer_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, customerId);
            return stmt.executeQuery();
        }

}
