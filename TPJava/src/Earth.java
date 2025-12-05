import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.paint.Color;
import javafx.scene.Node;

public class Earth extends Group {

    private Sphere sph;
    private Rotate ry;
    private Group globe; // Le groupe rotatif, contient la Terre ET les sphères marqueurs

    public Earth() {
        // Création de la sphère
        this.sph = new Sphere(300);
        // Création du sous-groupe globe
        this.globe = new Group(sph);

        // Cette ligne empêche Java de détecter les clics sur le carré invisible autour de la sphère.
        // Il ne détectera que les clics qui touchent vraiment la boule.
        this.setPickOnBounds(false);
        // -----------------------------

        // Texture
        PhongMaterial earthMaterial = new PhongMaterial();
        try {

            Image earthImage = new Image(new FileInputStream("./data/earth-texture.jpg"));
            earthMaterial.setDiffuseMap(earthImage);
            this.sph.setMaterial(earthMaterial);
        } catch (FileNotFoundException e) {
            System.err.println(" Erreur : Texture introuvable.");
        }

        // Rotation
        this.ry = new Rotate(0, Rotate.Y_AXIS);
        this.globe.getTransforms().add(ry); // Rotation sur globe

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long time) {
                double angle = (time / 15_000_000_000.0) * 360 % 360;
                ry.setAngle(angle);
            }
        };
        animationTimer.start();

        // Ajout du sous-groupe rotatif à Earth
        this.getChildren().add(globe);
    }

    // Ajoute une petite sphère colorée à la position de l'aéroport sur la Terre
    public Sphere createSphere(Aeroport a, Color color) {
        double R = 300; // rayon de la Terre
        double theta = Math.toRadians(180 - a.getLatitude()); // latitude transformée
        double phi = Math.toRadians(a.getLongitude() + 180); // longitude transformée
        // Correction empirique de la latitude par un facteur (optionnel, ici ignoré ; à ajuster si besoin)
        double x = R * Math.cos(theta) * Math.sin(phi);
        double y = -R * Math.sin(theta);
        double z = -R * Math.cos(theta) * Math.cos(phi);

        Sphere sphere = new Sphere(2);
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);
        PhongMaterial mat = new PhongMaterial(color);
        sphere.setMaterial(mat);
        return sphere;
    }

    // Affiche une sphère rouge sur la position de l'aéroport (dans globe)
    public void displayRedSphere(Aeroport a) {
        Sphere redSphere = createSphere(a, Color.RED);
        this.globe.getChildren().add(redSphere);
    }
}