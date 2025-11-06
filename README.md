1. Cœur des Données (Session 1)
Classe Aeroport.java : Créée avec tous les attributs (IATA, Nom, latitude, longitude) et les méthodes requises, y compris calculDistance().


Classe World.java : Créée et fonctionnelle. Le constructeur lit le fichier data/airport-codes_no_comma.csv.
La liste d'aéroports est correctement filtrée pour ne garder que les "large_airport".


Tests de Données : Les méthodes findByCode() et findNearestAirport() sont implémentées. Le test main de la classe World confirme que l'aéroport le plus proche des coordonnées de Paris (48.866, 2.316) est bien Orly (ORY), comme attendu.


2. Interface 3D 
Configuration JavaFX : Le projet est entièrement configuré pour utiliser le SDK JavaFX. La bibliothèque JavaFX est ajoutée aux dépendances du projet. Les "VM Options" sont configurées pour l'exécution (--module-path et --add-modules).
Fenêtre Principale : La classe Interface.java est créée et lance avec succès une fenêtre JavaFX "Hello World".
