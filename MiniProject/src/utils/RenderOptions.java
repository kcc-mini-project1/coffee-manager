package utils;

import java.util.List;

public class RenderOptions {
	static final int LPAD = 2;
	
    public static void singleLine(List<String> options) {
        String pad = " ".repeat(LPAD);

        for (int i = 0; i < options.size(); i++) {
        	 System.out.print((i + 1) + ". " + options.get(i) + pad);
        }
        System.out.println();
    }
    
    public static void multiLine(List<String> options) {
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }
}
