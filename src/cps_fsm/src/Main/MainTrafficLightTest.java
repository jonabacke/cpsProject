package Main;

import logic.MainControlTrafficLight;

public class MainTrafficLightTest {

    public static void main(String[] args) {
        MainControlTrafficLight main = new MainControlTrafficLight();
        main.start();

        System.out.println(main);

        String buffer [] = new String[4];
        buffer[0] = "green";
        buffer[1] = "normal";
        buffer[2] = "prio";
        //buffer[3] = "prio";

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