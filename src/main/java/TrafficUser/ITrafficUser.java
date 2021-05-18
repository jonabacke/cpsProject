package TrafficUser;

public interface ITrafficUser {

    /**
     * Über diese Methode kann eine TrafficNode das Tempo eines Fahrzeugs
     * regulieren falls zB. die Ampel glecih Rot wird und das Fahrzeug
     * deshalb schon mal vom Gas gehen sollte oder sein Tempo auf eine
     * Grüne welle anpassen soll
     * @param tempo Geschwindigkeit, die der TrafficUser annehmen soll.
     */
    void setTempo(double tempo);

    /**
     * Falls eio TrafficUser mit erhöhter Priorität durch muss können die
     * TrafficUser benachrichtigt werden eine Rettungsgasse zu bilden
     */
    void buildEmergencyCorridor();

    /**
     * Hat der TrafficUser den TrafficNode passiert wird ihm der nächste
     * TrafficNode gesendet bei dem er sich anmelden muss
     * @param trafficNodeUUID ID des nächsten TrafficNodes
     */
    void setNextTrafficNode(String trafficNodeUUID);
}
