package ControlUnit.FSM.Main;

import ControlUnit.FSM.logic.ControlTrafficLight;

public class MainTrafficLight {

    public static void main(String[] args) {
        ControlTrafficLight main = new ControlTrafficLight();
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