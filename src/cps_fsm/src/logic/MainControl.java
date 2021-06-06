package logic;

//import lift.boundary.Doors;
//import lift.boundary.Requests;
import stm.State;
import stm.Transition;


public class MainControl {


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
    public MainControl() {

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

        State red = new State("red") {
            @Override
            public void entry() {
                printRedLight();
                System.out.println(counter);

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

        // TODO ergÃ¤nzen Sie hier die fehlenden States

        // und nun alle Transitionen:

        new Transition(initializing, green).when(() -> counter==1);
        new Transition(green, red).when(() -> counter==2);

        // TODO ergÃ¤nzen Sie hier die fehlenden Transitionen
    }

    /**
     * Behaviour: Ã–ffnen der TÃ¼ren
     */
    protected void printGreenLight() {
        System.out.println("GREEN Light OOOOOOOOOOOOOONNNNNNNNNNNNNNN!!!!");;
    }

    /**
     * Behaviour: Stoppen des Aufzugs, aufgrund der aktuellen Implementierung hier
     * einfach als Flag umgesetzt.
     */
    protected void printRedLight() {
        System.out.println("RED Light OOOOOOOOOOOOOONNNNNNNNNNNNNNN!!!!");
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
