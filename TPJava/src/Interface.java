import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Group;

public class Interface extends Application {

    // Variables pour le Zoom (mouvement de souris)
    private double mousePosX;
    private double mousePosY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Projet Aviation 3D - Mode Debug");

        // Chargement de la base de données
        System.out.println("Chargement du monde...");
        World w = new World("./data/airport-codes_no_comma.csv");
        System.out.println("Monde chargé avec " + w.getList().size() + " aéroports.");

        // Création de la scène 3D
        Earth earth = new Earth();
        Group group = new Group(earth);
        Scene theScene = new Scene(group, 600, 400, true);

        // Configuration de la Caméra
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(35);
        theScene.setCamera(camera);

        // GESTION DES CLICS SOURIS
        theScene.addEventHandler(MouseEvent.ANY, event -> {

            // TEST CLIC DROIT (Recherche Aéroport)
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                // Petit mouchard pour voir si le clic est reçu
                System.out.println(" Clic reçu ! Bouton : " + event.getButton());

                if (event.getButton() == MouseButton.SECONDARY) {
                    System.out.println(" Clic Droit détecté. Analyse en cours...");

                    PickResult pickResult = event.getPickResult();

                    if (pickResult.getIntersectedNode() != null) {
                        // On a touché quelque chose
                        System.out.println(" Objet touché : " + pickResult.getIntersectedNode());

                        Point2D ord = pickResult.getIntersectedTexCoord();
                        if (ord != null) {
                            double x = ord.getX();
                            double y = ord.getY();
                            System.out.println(" Texture Coords: x=" + x + ", y=" + y);

                            // Conversion GPS
                            double latitude = 180 * (0.5 - y);
                            double longitude = 360 * (x - 0.5);
                            System.out.println(" GPS calculé : Lat=" + latitude + ", Lon=" + longitude);

                            // Recherche Aéroport
                            Aeroport nearest = w.findNearestAirport(longitude, latitude);
                            if (nearest != null) {
                                System.out.println(" AÉROPORT TROUVÉ : " + nearest);
                                earth.displayRedSphere(nearest);
                            } else {
                                System.out.println(" Aucun aéroport trouvé (bizarre...).");
                            }
                        } else {
                            System.out.println(" Pas de coordonnées de texture. Vérifie earth-texture.jpg !");
                        }
                    } else {
                        System.out.println(" Clic dans le vide (pas sur la Terre).");
                    }
                }
            }

            // B. GESTION DU ZOOM (Clic Gauche + Glisser)
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
            }
            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                double dx = event.getSceneX() - mousePosX;
                double dy = event.getSceneY() - mousePosY;

                // Correction : vérifie si le bouton gauche est enfoncé
                if (event.isPrimaryButtonDown()) {
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