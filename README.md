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


TP du 19/12

Durant cette séance, je me suis concentré sur l'intégration des données réelles (Live Data) pour connecter notre globe 3D à l'API AviationStack. L'objectif était de remplacer les données statiques par de vrais vols en cours. J'ai commencé par modifier la classe Flight pour y ajouter un attribut correspondant à l'aéroport de départ, ce qui permet désormais de relier géographiquement un vol à son origine.
Ensuite, j'ai développé la classe JsonFlightFiller pour traiter les réponses de l'API. En utilisant la bibliothèque javax.json, j'ai mis en place le parsing du flux JSON pour extraire les informations essentielles comme les numéros de vol, les dates et surtout les codes IATA. Cette classe fait le lien avec notre classe World pour retrouver les objets Aeroport correspondants et générer la liste des vols.
Pour la partie graphique dans la classe Earth, j'ai ajouté la gestion des marqueurs visuels. Lorsqu'on sélectionne une destination par un clic droit, elle s'affiche en rouge. Le programme récupère ensuite la liste des vols arrivant à cet endroit et matérialise tous les aéroports de provenance par des sphères jaunes directement sur le globe.
Enfin, j'ai traité le problème du blocage de l'interface lors des appels réseau. Comme la requête vers l'API met du temps à répondre et figeait l'animation de la Terre, j'ai déplacé cette opération dans un Thread séparé. J'ai utilisé la méthode Platform.runLater pour permettre à ce processus d'arrière-plan de mettre à jour l'interface graphique sans causer d'erreurs, ce qui assure une navigation fluide pendant le chargement des données.




