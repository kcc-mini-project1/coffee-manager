package utils;

import java.util.List;

public class RenderOptions {
	static final int LPAD = 2;
	
    public static void singleLine(List<String> options) {
        String pad = " ".repeat(LPAD);

        System.out.print(pad);

        for (int i = 0; i < options.size(); i++) {
        	 System.out.print((i + 1) + ". " + options.get(i));
        	 
             if (i < options.size() - 1) {
                 System.out.print(pad);
             }
        }
        System.out.println();
    }
    
    public static void multiLine(List<String> options) {
        String pad = " ".repeat(LPAD);

        for (int i = 0; i < options.size(); i++) {
            System.out.println(pad + (i + 1) + ". " + options.get(i));
        }
    }
}
