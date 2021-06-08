package ControlUnit.FSM.stm;

import java.util.function.Supplier;

/**
 * Trigger "horcht" auf ein Event. Das kÃ¶nnen hier nur ChangeEvents (siehe
 * {@link #when(Supplier)} oder TimeEvents ({@link #after(long)} sein.
 *
 * Die ChangeEvents und Guards sind mittels funktionaler Programmierung (Java
 * 8!) umgesetzt, d.h. man Ã¼bergibt eine Funktion (bzw. ein Interface mit einer
 * Single Abstract Method), die dann ausgewertet wird.
 */
public class Trigger {

    /**
     * Spezielle Subklasse die ein "after xx ms" umsetzt.
     */
    private static class TimeEvent implements Supplier<Boolean> {
        final long timeout;

        public TimeEvent(long timeout) {
            this.timeout = timeout;
        }

        long activated = 0;

        void start() {
            activated = System.currentTimeMillis();
        }

        void stop() {
            activated = 0;
        }

        @Override
        public Boolean get() {
            return (activated > 0) ? System.currentTimeMillis() - activated >= timeout : false;

        }
    }

    private Supplier<Boolean> change;
    private Supplier<Boolean> guard;

    public Trigger() {
    }

    /**
     * Evaluiert den Trigger. Gibt wahr zurÃ¼ck wenn sowohl das Event registriert
     * wurde als auch der (optionale) Guard wahr ist.
     */
    public boolean applies() {
        if (change.get()) {
            if (guard != null) {
                return guard.get();
            }
            return true;
        }
        return false;
    }

    /**
     * Fast alle Events sind ChangeEvents. Gibt den Trigger zurÃ¼ck, um optional noch
     * einen Guard setzen zu kÃ¶nnen.
     */
    public Trigger when(Supplier<Boolean> change) {
        this.change = change;
        return this;
    }

    /**
     * FÃ¼r TimeEvents gibt es die obige spezielle Subklasse. Gibt den Trigger
     * zurÃ¼ck, um optional noch einen Guard setzen zu kÃ¶nnen.
     */
    public Trigger after(long timeInMilliSeconds) {
        this.change = new TimeEvent(timeInMilliSeconds);
        return this;
    }

    /**
     * Optionaler Guard
     */
    public void guard(Supplier<Boolean> guard) {
        this.guard = guard;
    }

    void activate() {
        if (change instanceof TimeEvent) {
            ((TimeEvent) change).start();
        }
    }

    void deactivate() {
        if (change instanceof TimeEvent) {
            ((TimeEvent) change).stop();
        }
    }

    @Override
    public String toString() {
        if (change instanceof TimeEvent) {
            return "..waiting";
        }
        return "";
    }

}
