package ControlUnit.FSM.Main;

import ControlUnit.FSM.logic.ControlGreenRed;

public class MainGreenRed {

    public static void main(String[] args) {
        ControlGreenRed main = new ControlGreenRed();
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
