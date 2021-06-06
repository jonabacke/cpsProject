package Main;

import logic.MainControl;
public class Main {
    public static void main(String[] args) {
        MainControl main = new MainControl();
        main.start();

        System.out.println(main);

        main.counter = 1;
        while(true){

            if(main.step()){
                //System.out.println(main);
            }
            main.counter = 2;
        }


    }
}
