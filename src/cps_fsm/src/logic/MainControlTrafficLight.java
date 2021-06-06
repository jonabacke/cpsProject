package logic;

//import lift.boundary.Doors;
//import lift.boundary.Requests;
import stm.State;
import stm.Transition;


public class MainControlTrafficLight {


    public String message = "";
    boolean greenLight = true;
    boolean redLight = true;
    public int counter = 0 ;

    public State currentState = null;

    /**
     * Der initiale Zustand, wird im Konstruktor gesetzt
     */
    final State initialState;

    /**
     * Erzeugt die States mit Transitionen.
     */
    public MainControlTrafficLight() {

        // zuerst alle States mit Verhalten (Behavior)
        State initializing = new State("initializing") {
            @Override
            public void entry() {
            }

            @Override
            public void do_() {

            }
        };

        initialState = initializing;

        State normal = new State("normal") {
            @Override
            public void entry() {
                printNormalMode();
            }

            @Override
            public void do_() {
            }
        };
        State prio = new State("prio") {
            @Override
            public void entry() {
                printPrioMode();
            }

            @Override
            public void do_() {
            }
        };
        State stau = new State("stau") {
            @Override
            public void entry() {
                printStauMode();
                //publish andere Nodes benachrichtigen
            }

            @Override
            public void do_() {
            }
        };
        State green = new State("green") {
            @Override
            public void entry() {
                printGreenLight();
            }

            @Override
            public void do_() {
            }
        };
        State red = new State("red") {
            @Override
            public void entry() {
                printRedLight();
            }

            @Override
            public void do_() {
            }
        };

        // TODO ergÃ¤nzen Sie hier die fehlenden States

        // und nun alle Transitionen:

        new Transition(initializing, normal).when(() -> true);
        new Transition(normal, stau).when(() -> message == "stau");
        new Transition(normal, prio).when(() -> message == "prio");
        new Transition(stau, normal).when(() -> message == "normal");
        new Transition(stau, prio).when(() -> message == "prio");
        new Transition(prio, normal).when(() -> message == "normal");
        new Transition(prio, green).when(() -> message == "green");

        // TODO ergÃ¤nzen Sie hier die fehlenden Transitionen
    }

    protected void printRedLight() {
        //System.out.println("RED Light OOOOOOOOOOOOOONNNNNNNNNNNNNNN!!!!");
    }
    protected void printGreenLight() {
        //System.out.println("GREEN Light OOOOOOOOOOOOOONNNNNNNNNNNNNNN!!!!");
    }
    protected void printPrioMode() {
        //System.out.println("MODE = PRIO!!!!");
    }
    protected void printStauMode() {
        //System.out.println("MODE = Stau!!!!");
    }
    protected void printNormalMode() {
        //System.out.println("MODE = Normal!!!!");
    }

    // TODO ergÃ¤nzen Sie bei Bedarf hier weitere "Behaviours"

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        if (currentState == null) {
            return "no state set";
        }
        strb.append(currentState).append(": currentState=").append(currentState.name);
        return strb.toString();
    }

    public void start() {
        currentState = initialState;
        currentState.activate();
    }

    /**
     * @return true wenn der State geÃ¤ndert wurde
     */
    public boolean step() {
        boolean changed = false;
        Transition transition = currentState.trigger();
        if (transition != null) {
            currentState.deactivate();
            currentState.exit();
            currentState = transition.target;
            currentState.entry();
            currentState.activate();
            changed = true;

        }
        currentState.do_();
        return changed;
    }

}
