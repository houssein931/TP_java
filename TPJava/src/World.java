import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.lang.Math;

public class World {

    // 1. Attribut: la liste de tous les aéroports
    private ArrayList<Aeroport> list;

       // 2. Constructeur: lit le fichier CSV et remplit la liste d'aéroports


    public World(String fileName) {
        // Initialise la liste
        this.list = new ArrayList<Aeroport>();


        try {
            BufferedReader buf = new BufferedReader(new FileReader(fileName));
            String s = buf.readLine(); // Lire la ligne d'en-tête pour la sauter
            s = buf.readLine(); // Lire la première vraie ligne de données

            while (s != null) {
                // Enlève les guillemets qui séparent les champs de données GPS
                s = s.replaceAll("\"", "");
                String[] fields = s.split(","); // Sépare la ligne par les virgules

                // Filtre pour ne garder que les "large_airport"
                if (fields[1].equals("large_airport")) {


                    // 9 = code IATA, 11 = longitude, 12 = latitude
                    // (Basé sur l'analyse de l'exemple "AYPY" )
                    if (fields.length > 12 && !fields[9].isEmpty() && !fields[11].isEmpty() && !fields[12].isEmpty()) {

                        try {
                            String iata = fields[9];
                            String name = fields[2];
                            String country = fields[5];

                            double longitude = Double.parseDouble(fields[11]);
                            double latitude = Double.parseDouble(fields[12]);

                            // Crée l'aéroport et l'ajoute à la liste
                            Aeroport airport = new Aeroport(iata, name, country, latitude, longitude);
                            this.list.add(airport);

                        } catch (NumberFormatException e) {
                            // Au cas où les coordonnées ne sont pas des nombres valides
                            // System.err.println("Erreur de format de nombre pour la ligne: " + s);
                        }
                    }

                }
                s = buf.readLine(); // Lit la ligne suivante
            }
            buf.close();
        } catch (Exception e) {
            System.out.println("Maybe the file isn't there ?");
            if (list != null && !list.isEmpty()) {
                System.out.println(list.get(list.size() - 1));
            }
            e.printStackTrace();
        }
    }


          // 3. Getter pour la liste

    public ArrayList<Aeroport> getList() {
        return list;
    }


         // 4. Méthode pour trouver un aéroport par son code IATA

    public Aeroport findByCode(String code) {
        for (Aeroport a : this.list) {
            if (a.getIATA().equals(code)) {
                return a; // On l'a trouvé
            }
        }
        return null; // Pas trouvé
    }


     // 5. Méthode pour trouver l'aéroport le plus proche de coordonnées données

    public Aeroport findNearestAirport(double lon, double lat) {
        if (this.list.isEmpty()) {
            return null; // Pas d'aéroports à comparer
        }

        Aeroport nearest = null;
        double minDistance = Double.MAX_VALUE; // Commence avec une distance "infinie"

        // On calcule la distance pour tous les aéroports
        for (Aeroport a : this.list) {
            double d = this.distance(lon, lat, a.getLongitude(), a.getLatitude());
            if (d < minDistance) {
                minDistance = d;
                nearest = a;
            }
        }
        return nearest;
    }


          // 6. Méthode 'distance' (utilisée par le main de test et findNearestAirport)
    // Calcule la norme basée sur la formule du sujet

    public double distance(double lon1, double lat1, double lon2, double lat2) {
        // Θ = latitude, Φ = longitude
        // norme = (Θ2−Θ1)² + ((Φ2−Φ1)cos((Θ2+Θ1)/2))²

        double dLat = lat2 - lat1; // (Θ2−Θ1)
        double dLon = lon2 - lon1; // (Φ2−Φ1)
        double avgLat = (lat2 + lat1) / 2.0; // (Θ2+Θ1)/2

        // Math.cos s'attend à des radians
        double cosAvgLat = Math.cos(Math.toRadians(avgLat));

        double lonFactor = dLon * cosAvgLat;

        // (dLat)² + (lonFactor)²
        double norme = (dLat * dLat) + (lonFactor * lonFactor);

        return norme;
    }



          // 7. Main de test (Listing 3)

    public static void main(String[] args) {

        World w = new World("./data/airport-codes_no_comma.csv");

        System.out.println("Found " + w.getList().size() + " airports.");

        // Paris (lattitude: 48.866, longitude 2.316) 

        Aeroport paris = w.findNearestAirport(2.316, 48.866);
        Aeroport cdg = w.findByCode("CDG");

        // Le sujet indique que le plus proche doit être Orly (ORY)
        System.out.println("L'aéroport le plus proche de Paris (2.316, 48.866) est:");
        System.out.println(paris);

        System.out.println("\nInfos pour CDG:");
        System.out.println(cdg);

        // Calcul des distances de test
        if (paris != null) {
            double distance = w.distance(2.316, 48.866, paris.getLongitude(), paris.getLatitude());
            System.out.println("Distance (norme) de Paris à " + paris.getIATA() + ": " + distance);
        }
        if (cdg != null) {
            double distanceCDG = w.distance(2.316, 48.866, cdg.getLongitude(), cdg.getLatitude());
            System.out.println("Distance (norme) de Paris à CDG: " + distanceCDG);
        }
    }
}