package dto;
import java.sql.Date;
import lombok.Data;

@Data
public class Employee {
	private int employeeId;
	private String employeeName;
	private String phoneNumber;
	private String title;
	private int salary;
	private int managerId;
}