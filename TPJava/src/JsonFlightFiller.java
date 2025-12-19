import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JsonFlightFiller {

    // Liste qui contiendra les vols créés
    private ArrayList<Flight> list = new ArrayList<Flight>();

    // Constructeur : c'est ici qu'on transforme le JSON en objets Flight
    public JsonFlightFiller(String jsonString, World w) {
        try {
            // Conversion de la chaîne JSON en flux pour la lecture
            InputStream is = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("data");

            // Formatter pour lire les dates du fichier JSON (format ISO 8601)
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

            // Boucle sur chaque vol du tableau "data"
            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                try {
                    // 1. Récupération des infos de la compagnie (Airline)
                    JsonObject airlineObj = result.getJsonObject("airline");
                    String airlineName = airlineObj.getString("name");
                    String airlineCode = airlineObj.getString("iata");

                    // 2. Récupération du numéro de vol
                    JsonObject flightObj = result.getJsonObject("flight");
                    // On convertit le numéro (String dans le JSON) en entier
                    int number = Integer.parseInt(flightObj.getString("number"));

                    // 3. Récupération des dates de départ et d'arrivée
                    JsonObject departureObj = result.getJsonObject("departure");
                    JsonObject arrivalObj = result.getJsonObject("arrival");

                    // Récupération du code IATA de l'aéroport de départ
                    String departureIata = departureObj.getString("iata");

                    // On utilise "scheduled" (horaire prévu)
                    String departureString = departureObj.getString("scheduled");
                    String arrivalString = arrivalObj.getString("scheduled");

                    // Conversion des chaînes en objets LocalDateTime
                    LocalDateTime departureTime = LocalDateTime.parse(departureString, formatter);
                    LocalDateTime arrivalTime = LocalDateTime.parse(arrivalString, formatter);

                    // Recherche de l'aéroport dans le Monde
                    Aeroport departureAeroport = w.findByCode(departureIata);

                    // 4. Création de l'objet Flight
                    // On ne crée le vol que si on a trouvé l'aéroport de départ dans notre base
                    if (departureAeroport != null) {

                        Flight f = new Flight(airlineCode, airlineName, number, departureAeroport, departureTime, arrivalTime);
                        list.add(f);
                    }

                } catch (Exception e) {
                    // Si un vol spécifique pose problème, on l'ignore et on continue
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter pour récupérer la liste depuis l'extérieur
    public ArrayList<Flight> getList() {
        return list;
    }


    // MAIN DE TEST
    public static void main(String[] args) {
        try {
            System.out.println("Lecture du fichier CSV des aéroports...");

            World w = new World("./data/airport-codes_no_comma.csv");

            System.out.println("Lecture du fichier JSON de test...");
            // Lecture du fichier test.txt
            BufferedReader br = new BufferedReader(new FileReader("data/test.txt"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            String jsonContent = sb.toString();

            System.out.println("Parsing du JSON...");
            JsonFlightFiller filler = new JsonFlightFiller(jsonContent, w);

            System.out.println("--------------------------------------------------");
            System.out.println("Résultat du test :");
            System.out.println("Nombre de vols trouvés (avec aéroport connu) : " + filler.getList().size());

            // Affichage des 5 premiers vols pour vérifier
            int count = 0;
            for (Flight f : filler.getList()) {
                System.out.println(f);

                count++;
                if (count >= 5) break;
            }
            System.out.println("--------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}