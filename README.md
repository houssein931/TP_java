1. Cœur des Données (Session 1)
Classe Aeroport.java : Créée avec tous les attributs (IATA, Nom, latitude, longitude) et les méthodes requises, y compris calculDistance().


Classe World.java : Créée et fonctionnelle. Le constructeur lit le fichier data/airport-codes_no_comma.csv.
La liste d'aéroports est correctement filtrée pour ne garder que les "large_airport".


Tests de Données : Les méthodes findByCode() et findNearestAirport() sont implémentées. Le test main de la classe World confirme que l'aéroport le plus proche des coordonnées de Paris (48.866, 2.316) est bien Orly (ORY), comme attendu.


2. Interface 3D 
Configuration JavaFX : Le projet est entièrement configuré pour utiliser le SDK JavaFX. La bibliothèque JavaFX est ajoutée aux dépendances du projet. Les "VM Options" sont configurées pour l'exécution (--module-path et --add-modules).
Fenêtre Principale : La classe Interface.java est créée et lance avec succès une fenêtre JavaFX "Hello World".


TP2 du 5/12

Fonctionnalités et corrections implémentées :
Gestion du zoom et du déplacement caméra
Correction de l’EventHandler pour que le zoom (clic gauche + glisser) fonctionne sur tous les systèmes, grâce à l’utilisation de event.isPrimaryButtonDown().
Construction de la scène 3D en JavaFX :
Utilisation d’un Group (et non d’un Pane) pour contenir la Terre, respectant les meilleures pratiques de la 3D sous JavaFX.
Affichage de la Terre texturée et rotation animée :
La Terre (une sphère 3D) reçoit sa texture terrestre et effectue désormais une rotation complète en 15 secondes grâce à un AnimationTimer.
Interaction utilisateur avancée – clic droit sur la Terre :
Lors d’un clic droit, le programme :
calcule les coordonnées GPS à partir du point cliqué,
recherche l’aéroport le plus proche,
affiche ses informations en console,
ajoute une boule rouge en 3D sur la position correspondante à la surface de la Terre.
Marqueurs synchronisés à la rotation :
Les sphères colorées (boules rouges) représentant les aéroports « marqués » sont désormais intégrées dans un même groupe rotatif que la Terre pour tourner avec elle.
