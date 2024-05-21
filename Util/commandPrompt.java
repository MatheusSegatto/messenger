
package Util;

public class commandPrompt {

    public static void clearPrompt(){
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else if(System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("Mac")){
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
