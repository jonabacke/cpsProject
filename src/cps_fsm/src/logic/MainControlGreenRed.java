package logic;

import stm.State;
import stm.Transition;

enum Status
{
    GREEN, RED,;
}

public class MainControlGreenRed {
    public String message = "";

    public Status status = Status.RED;

    public State currentState = null;

    /**
     * Der initiale Zustand, wird im Konstruktor gesetzt
     */
    final State initialState;

    /**
     * Erzeugt die States mit Transitionen.
     */
    public MainControlGreenRed() {

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

        State green = new State("green") {
            @Override
            public void entry() {
                status = Status.GREEN;
                printGreenLight();
                //publish Green
            }

            @Override
            public void do_() {
            }
        };
        State red = new State("red") {
            @Override
            public void entry() {
                printRedLight();
                status = Status.RED;
                //publish red
            }

            @Override
            public void do_() {
            }
        };

        // TODO ergÃ¤nzen Sie hier die fehlenden States

        // und nun alle Transitionen:

        new Transition(initializing, red).when(() -> true);
        new Transition(red, green).when(() -> message == "green");
        new Transition(green, red).when(() -> message == "red");

        // TODO ergÃ¤nzen Sie hier die fehlenden Transitionen
    }

    protected void printRedLight() {
        //System.out.println("RED Light OOOOOOOOOOOOOONNNNNNNNNNNNNNN!!!!");
    }

    protected void printGreenLight() {
        //System.out.println("GREEN Light OOOOOOOOOOOOOONNNNNNNNNNNNNNN!!!!");
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
