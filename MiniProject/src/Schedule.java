import java.sql.Date;

import lombok.Data;

@Data
public class Schedule {
	private int scheduleId;
	private int employeeId;
	private Date startTime;
	private Date endTime;
}