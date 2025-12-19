import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.paint.Color;
import java.util.ArrayList; // Import nécessaire pour la liste

public class Earth extends Group {

    private Sphere sph;
    private Rotate ry;
    private Group globe; // Le groupe rotatif

    // Liste pour stocker les sphères jaunes (conforme à l'UML)
    private ArrayList<Sphere> yellowSphere = new ArrayList<>();

    public Earth() {
        // Création de la sphère
        this.sph = new Sphere(300);
        // Création du sous-groupe globe
        this.globe = new Group(sph);

        // Permet de cliquer à travers la boîte englobante vide
        this.setPickOnBounds(false);

        // Texture
        PhongMaterial earthMaterial = new PhongMaterial();
        try {
            Image earthImage = new Image(new FileInputStream("./data/earth-texture.jpg"));
            earthMaterial.setDiffuseMap(earthImage);
            this.sph.setMaterial(earthMaterial);
        } catch (FileNotFoundException e) {
            System.err.println(" Erreur : Texture introuvable. Vérifiez le dossier data.");
        }

        // Rotation
        this.ry = new Rotate(0, Rotate.Y_AXIS);
        this.globe.getTransforms().add(ry);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long time) {
                // Rotation lente : 1 tour complet en ~15 secondes
                double angle = (time / 15_000_000_000.0) * 360 % 360;
                ry.setAngle(angle);
            }
        };
        animationTimer.start();

        // Ajout du sous-groupe rotatif à Earth
        this.getChildren().add(globe);
    }

    // Calcule la position 3D d'un aéroport et renvoie une sphère
    public Sphere createSphere(Aeroport a, Color color) {
        double R = 300; // rayon de la Terre

        // Formules de conversion (assurez-vous qu'elles correspondent à votre texture)
        double theta = Math.toRadians(180 - a.getLatitude());
        double phi = Math.toRadians(a.getLongitude() + 180);

        double x = R * Math.cos(theta) * Math.sin(phi);
        double y = -R * Math.sin(theta);
        double z = -R * Math.cos(theta) * Math.cos(phi);

        Sphere sphere = new Sphere(2); // Rayon de 2 pixels pour les marqueurs
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);

        PhongMaterial mat = new PhongMaterial(color);
        sphere.setMaterial(mat);
        return sphere;
    }

    // Affiche l'aéroport cible en ROUGE
    public void displayRedSphere(Aeroport a) {
        // Optionnel : On peut nettoyer les anciennes sphères jaunes quand on change de cible
        this.globe.getChildren().removeAll(yellowSphere);
        yellowSphere.clear();

        Sphere redSphere = createSphere(a, Color.RED);
        this.globe.getChildren().add(redSphere);
    }

    // Affiche un aéroport d'origine en JAUNE
    public void displayYellowSphere(Aeroport a) {
        Sphere yellow = createSphere(a, Color.YELLOW);

        // On l'ajoute à notre liste pour pouvoir les supprimer plus tard
        this.yellowSphere.add(yellow);

        // On l'ajoute au groupe "globe" pour qu'il tourne avec la Terre
        this.globe.getChildren().add(yellow);
    }
}