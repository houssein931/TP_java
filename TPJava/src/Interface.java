import javafx.application.Application;
import javafx.application.Platform; // Important pour mettre à jour l'IHM depuis un autre Thread
import javafx.geometry.Point2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.Group;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Interface extends Application {

    // Variables pour la gestion du mouvement de la caméra
    private double mousePosX;
    private double mousePosY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Projet Aviation 3D - Final");

        // 1. Chargement de la base de données des aéroports
        System.out.println("Chargement du monde...");
        World w = new World("./data/airport-codes_no_comma.csv");
        System.out.println("Monde chargé avec " + w.getList().size() + " aéroports.");

        // 2. Création de la Terre et de la scène
        Earth earth = new Earth();
        Group group = new Group(earth);
        Scene theScene = new Scene(group, 600, 400, true);

        // 3. Configuration de la Caméra 3D
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(35);
        theScene.setCamera(camera);

        // 4. GESTION DES CLICS SOURIS
        theScene.addEventHandler(MouseEvent.ANY, event -> {

            // --- CLIC DROIT : Sélection d'aéroport + Requête API ---
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                if (event.getButton() == MouseButton.SECONDARY) {

                    PickResult pickResult = event.getPickResult();

                    // Si on a cliqué sur la Terre (et pas dans le vide)
                    if (pickResult.getIntersectedNode() != null) {
                        Point2D ord = pickResult.getIntersectedTexCoord();
                        if (ord != null) {
                            // Récupération des coordonnées de texture et conversion en GPS
                            double x = ord.getX();
                            double y = ord.getY();
                            double latitude = 180 * (0.5 - y);
                            double longitude = 360 * (x - 0.5);

                            // Recherche de l'aéroport le plus proche dans notre 'World'
                            Aeroport nearest = w.findNearestAirport(longitude, latitude);

                            if (nearest != null) {
                                System.out.println("--------------------------------------------------");
                                System.out.println("Aéroport sélectionné : " + nearest);

                                // Affichage immédiat de la sphère rouge
                                earth.displayRedSphere(nearest);

                                // --- LANCEMENT DU PROCESSUS EN ARRIÈRE-PLAN (THREAD) ---
                                // Cela évite que l'application ne "gèle" pendant le téléchargement
                                Thread thread = new Thread(() -> {
                                    try {
                                        System.out.println("Envoi de la requête API...");

                                        // cle api
                                        String apiKey = "5a8e64617d5cfb951e3ecf09a558506d";

                                        String apiString = "http://api.aviationstack.com/v1/flights?access_key=" + apiKey + "&arr_iata=" + nearest.getIATA();

                                        // A. Requete HTTP
                                        HttpClient client = HttpClient.newHttpClient();
                                        HttpRequest request = HttpRequest.newBuilder()
                                                .uri(URI.create(apiString))
                                                .build();
                                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                                        // B. Analyse du JSON reçu
                                        JsonFlightFiller filler = new JsonFlightFiller(response.body(), w);

                                        // C. Retour sur le "Fil d'exécution principal" (UI Thread) pour l'affichage
                                        // JavaFX interdit de modifier la scène depuis un autre Thread, d'où l'usage de Platform.runLater
                                        Platform.runLater(() -> {
                                            System.out.println("Données reçues ! " + filler.getList().size() + " vols trouvés.");

                                            if (filler.getList().isEmpty()) {
                                                System.out.println("Note : Aucun vol trouvé ou erreur API (vérifiez votre quota ou la clé).");
                                            }

                                            for (Flight f : filler.getList()) {
                                                if (f.getDeparture() != null) {
                                                    earth.displayYellowSphere(f.getDeparture());
                                                    System.out.println(" -> Vol depuis " + f.getDeparture().getIATA());
                                                }
                                            }
                                            System.out.println("Mise à jour graphique terminée.");
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                                // Démarrage effectif du Thread
                                thread.start();
                                // -------------------------------------------------------

                            }
                        }
                    }
                }
            }

            // --- CLIC GAUCHE + GLISSER : Zoom (Déplacement Caméra) ---
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
            }
            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                if (event.isPrimaryButtonDown()) { // Uniquement si clic gauche maintenu
                    double dy = event.getSceneY() - mousePosY;
                    camera.setTranslateZ(camera.getTranslateZ() + dy * 5);
                }
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
            }
        });

        primaryStage.setScene(theScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}