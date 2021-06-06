package Main;

import logic.MainControlGreenRed;

public class MainGreenRedTest {
    public static void main(String[] args) {
        MainControlGreenRed main = new MainControlGreenRed();
        main.start();

        System.out.println(main);

        String buffer [] = new String[4];
        buffer[0] = "green";
        buffer[1] = "red";
        //while (true) {

        //main.message = "stau";
        //if (main.step()) {
        //System.out.println(main);
        //}

        for(int i = 0; i <= 3; i++){
            if(main.step()){
                System.out.println(main);
            }
            main.message = buffer[i];
        }


    }
}
