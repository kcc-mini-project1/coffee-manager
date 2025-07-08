package utils;

import java.sql.SQLException;

public class ErrorUtil {
	public static String getErrorMessage(SQLException e) {
		if (e == null) return "알 수 없는 오류가 발생했습니다.";
		
		switch (e.getErrorCode()) {
			case 12899:
				return "입력한 값이 너무 깁니다. 최대 허용 길이를 확인해주세요.";
			case 1: // ORA-00001 unique constraint violation
				return "중복된 값이 존재합니다. 다른 값을 입력해주세요.";
            default:
            		return "데이터베이스 오류가 발생했습니다. 관리자에게 문의하세요.";
        }
    }
}
