package Main;

import logic.MainControlGreenRed;
import logic.MainControlTrafficLight;

public class MainTrafficLight {

    public static void main(String[] args) {
        MainControlTrafficLight main = new MainControlTrafficLight();
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