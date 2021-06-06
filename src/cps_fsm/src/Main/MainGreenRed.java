package Main;

import logic.MainControlGreenRed;

public class MainGreenRed {

    public static void main(String[] args) {
        MainControlGreenRed main = new MainControlGreenRed();
        main.start();

        System.out.println(main);

        while(true){
            //subscribe topic
            // main.message = message from topic
            if(main.step()){
                System.out.println(main);
            }
        }
    }
}
