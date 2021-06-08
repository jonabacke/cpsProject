package ControlUnit.FSM.logic;

//import lift.boundary.Doors;
//import lift.boundary.Requests;
import Config.ConfigFile;
import ControlUnit.FSM.stm.State;
import ControlUnit.FSM.stm.Transition;


public class ControlTrafficLight {


    private String message = "";
    private boolean greenLight = true;
    private boolean redLight = true;
    private int counter = 0 ;
    private State currentState = null;

    /**
     * Der initiale Zustand, wird im Konstruktor gesetzt
     */
    private final State initialState;

    /**
     * Erzeugt die States mit Transitionen.
     */
    public ControlTrafficLight() {

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

        State normal = new State(ConfigFile.NORMAL_MESSAGE) {
            @Override
            public void entry() {
                printNormalMode();
            }

            @Override
            public void do_() {
            }
        };
        State prio = new State(ConfigFile.PRIO_MESSAGE) {
            @Override
            public void entry() {
                printPrioMode();
            }

            @Override
            public void do_() {
            }
        };
        State stau = new State(ConfigFile.STAU_MESSAGE) {
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
        new Transition(normal, stau).when(()    -> message.equalsIgnoreCase(ConfigFile.STAU_MESSAGE));
        new Transition(normal, prio).when(()    -> message.equalsIgnoreCase(ConfigFile.PRIO_MESSAGE));
        new Transition(stau, normal).when(()    -> message.equalsIgnoreCase(ConfigFile.NORMAL_MESSAGE));
        new Transition(stau, prio).when(()      -> message.equalsIgnoreCase(ConfigFile.PRIO_MESSAGE));
        new Transition(prio, normal).when(()    -> message.equalsIgnoreCase(ConfigFile.NORMAL_MESSAGE));
        new Transition(prio, stau).when(()      -> message.equalsIgnoreCase(ConfigFile.STAU_MESSAGE));

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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGreenLight() {
        return greenLight;
    }

    public boolean isRedLight() {
        return redLight;
    }

    public int getCounter() {
        return counter;
    }

    public State getCurrentState() {
        return currentState;
    }

}
