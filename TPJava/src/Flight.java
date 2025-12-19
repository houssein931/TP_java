import java.time.LocalDateTime;

public class Flight {

    // Attributs définis dans l'UML + l'association vers l'aéroport de départ
    private String airLineCode;
    private String airlineName;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private int number;
    private Aeroport departure; // Nouvel attribut pour stocker l'aéroport de départ

    // Constructeur mis à jour
    public Flight(String airLineCode, String airlineName, int number, Aeroport departure, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.airLineCode = airLineCode;
        this.airlineName = airlineName;
        this.number = number;
        this.departure = departure; // On enregistre l'aéroport
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    // Getters
    public Aeroport getDeparture() {
        return departure;
    }

    public LocalDateTime getArrival() {
        return arrivalTime;
    }

    // Autres getters utiles
    public String getAirLineCode() {
        return airLineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public int getNumber() {
        return number;
    }

    // Surcharge de toString pour un affichage propre dans la console
    @Override
    public String toString() {
        // On affiche le code IATA de l'aéroport si l'objet departure n'est pas null
        String depCode = (departure != null) ? departure.getIATA() : "Unknown";

        return "Vol " + airlineName + " " + number +
                " [" + airLineCode + "] " +
                "| Départ : " + depCode + " (" + departureTime + ") " +
                "-> Arrivée : " + arrivalTime;
    }
}