package TrafficNode;

public interface ITrafficNode {
    /**
     * Fügt ein TrafficNode hinzu
     * @param trafficNodeUUID, welches hinzugefügt werden soll
     * @param distance, Distance to this trafficNode
     * @param weight, weight of the TrafficUser on this Road
     * @param isDefault
     * @param uuid
     */
    void signInTrafficNode(String trafficNodeUUID, double distance, double weight, boolean isDefault, String uuid);

    /**
     * entfernt ein TrafficNode, dass zb. defekt ist
     * @param trafficNodeUUID, welches entfernt werden soll
     */
    void signOutTrafficNode(String trafficNodeUUID);

    /**
     * Hierrüber kann ein TrafficUser sich bei der TrafficNode anmelden.
     * @param trafficUserNetworkString NetworkString des TrafficUsers in dem alle wichtigen Daten stecken.
     * @param trafficUserUUID ID des TrafficUsers
     */
    void signInTrafficUser( String trafficUserUUID, String trafficUserNetworkString);

    /**
     * Hierrüber kann ein TrafficUser sich wieder abmelden bei einer TrafficNode
     * @param trafficUserUUID ID des TrafficUsers
     */
    void signOutTrafficUser(String trafficUserUUID);

    /**
     * Über diese Funktion kann ein TrafficUser seine Geschwindigkeit an die TrafficNode weitergeben.
     * Diese wird dann im TrafficNode eigenem Object für den TrafficUser überschrieben und gespeichert
     * @param trafficUserUUID ID des TrafficUsers
     * @param tempo neue Geschwindigkeit des TrafficUsers
     */
    void setTempo(String trafficUserUUID, double tempo);

    /**
     * Über diese Methode kann die Priorität eines TrafficUsers bei der TrafficNoed geändert werden.
     * Diese wird dann im TrafficNode eigenem Object für den TrafficUser überschrieben und gespeichert
     * @param trafficUserUUID  ID des TrafficUsers
     * @param priority neue Priorität des TrafficUsers
     */
    void setPriority(String trafficUserUUID, String priority);

    /**
     * Über diese Methode kann die nächste ZielTrafficNode eines TrafficUsers bei der TrafficNoed geändert werden.
     * Diese wird dann im TrafficNode eigenem Object für den TrafficUser überschrieben und gespeichert
     * @param trafficUserUUID ID des TrafficUsers
     * @param nextTrafficNode nächste ZielTrafficNode des TrafficUsers
     */
    void setNextTrafficNode(String trafficUserUUID, String nextTrafficNode);

    /**
     * Über diese Methode kann die ZielTrafficNode eines TrafficUsers bei der TrafficNoed geändert werden.
     * Diese wird dann im TrafficNode eigenem Object für den TrafficUser überschrieben und gespeichert.
     * Auserdem wird die optimale Route für den TrafficUser neu berechnet
     * @param trafficUserUUID ID des TrafficUsers
     * @param finalTrafficNode neue ZielTrafficNode des TrafficUsers
     */
    void setFinalTrafficNode(String trafficUserUUID, String finalTrafficNode);

    /**
     * Prüfen der Auslastung des nächsten Nodes
     * @param trafficNodeUUID sender Node
     * @param amount anzahl der Fahrzeuge
     */
    void setWorkload(String trafficNodeUUID, int amount);

    /**
     * setzt den Ampelstatus einer Straße
     * @param status status der Ampel ("RED", "GREEN")
     * @param nodeUUID id der Straße
     */
    void setStatus(String nodeUUID, String status);
}
