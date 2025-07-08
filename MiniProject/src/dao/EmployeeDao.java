package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDao {
	
	public int insertEmployee(Connection con, String name, String phone, String title, String salary) {
		try {
			String sql = "INSERT INTO employees "
					+ "(employee_id, employee_name, phone_number, title, salary) "
					+ "VALUES (emp_seq.NEXTVAL, ?, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, phone);
			stmt.setString(3, title);
			stmt.setInt(4, Integer.parseInt(salary));
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public ResultSet getEmployee(Connection con, String id) {
		try {
			String sql = "SELECT "
					+ "employee_id, employee_name, phone_number, "
					+ "title, salary, manager_id "
					+ "FROM employees "
					+ "WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(id));
			return stmt.executeQuery();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public ResultSet getEmployeeAll(Connection con) {
		try {
			String sql = "SELECT "
					+ "e.employee_id AS employee_id, e.employee_name AS name, "
					+ "e.phone_number AS phone, title, salary, manager_id, "
					+ "NVL(rst.total_price, 0) AS performance "
					+ "FROM employees e LEFT JOIN ("
					+ "SELECT s.employee_id, SUM(m.price) AS total_price "
					+ "FROM schedule s LEFT JOIN orders o "
					+ "ON o.order_date BETWEEN s.start_time AND s.end_time "
					+ "LEFT JOIN menus m ON o.menu_name = m.menu_name "
					+ "WHERE TRUNC(s.start_time, 'MM') = TRUNC(SYSDATE, 'MM') "
					+ "GROUP BY s.employee_id) rst "
					+ "ON e.employee_id = rst.employee_id "
					+ "ORDER BY e.employee_id";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			return stmt.executeQuery();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public int updateEmployeeName(Connection con, String id, String name) {
		try {
			String sql = "UPDATE employees "
					+ "SET employee_name = ? "
					+ "WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1,  name);
			stmt.setInt(2,  Integer.parseInt(id));
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int updateEmployeePhone(Connection con, String id, String phone) {
		try {
			String sql = "UPDATE employees "
					+ "SET phone_number = ? "
					+ "WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1,  phone);
			stmt.setInt(2,  Integer.parseInt(id));
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int updateEmployeeSalary(Connection con, String id, String salary) {
		try {
			String sql = "UPDATE employees "
					+ "SET salary = ? "
					+ "WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1,  salary);
			stmt.setInt(2,  Integer.parseInt(id));
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int updateEmployeeTitle(Connection con, String id, String title) {
		try {
			String sql = "UPDATE employees "
					+ "SET title = ? "
					+ "WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1,  title);
			stmt.setInt(2,  Integer.parseInt(id));
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int updateEmployeeManager(Connection con, String id, String manager_id) {
		try {
			String sql = "UPDATE employees "
					+ "SET manager_id = ? "
					+ "WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1,  manager_id);
			stmt.setInt(2,  Integer.parseInt(id));
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public void deleteEmployeeSchedule(Connection con, String id) {
		try {
			String sql = "DELETE FROM schedule WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1,  Integer.parseInt(id));
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public int deleteEmployee(Connection con, String id) {
		try {
			String sql = "DELETE FROM employees WHERE employee_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(id));
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
}
