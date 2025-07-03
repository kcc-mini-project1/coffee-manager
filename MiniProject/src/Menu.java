import java.sql.Date;
import lombok.Data;

@Data
public class Menu {
	private String categoryName;
	private String menuName;
	private int price;
	private String description;
	private boolean isSoldout;
	private boolean iceable;
}