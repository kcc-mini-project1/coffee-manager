import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloJDBC {
	public static void main(String[] args) {
		DataSource ds = new DataSource();
		Connection con = null;
		
		try {
			con = ds.getConnection();
			System.out.println("데이터 베이스 접속 완료");
			
			String sql = "select * from test";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				System.out.println("result: " + rs.getString(1));
			} else {
				System.out.println("데이터가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ds.closeConnection(con);
		}
	}
}