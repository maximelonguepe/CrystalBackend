# README Backend (Spring Boot)

Ce document présente l'API Spring Boot qui accompagne l'application front-end pour gérer les positions GPS. L'API offre les fonctionnalités suivantes :

1. **Création de Position GPS** : Vous pouvez créer une nouvelle position GPS en utilisant l'endpoint approprié.

2. **Liste des Positions GPS** : Vous pouvez récupérer la liste complète des positions GPS enregistrées.

3. **Suppression de Position GPS** : Vous pouvez supprimer une position GPS existante en fournissant son identifiant.

4. **Vérification de la Proximité de Positions GPS** : Vous pouvez déterminer si deux positions GPS sont à moins de 10 km l'une de l'autre en utilisant l'endpoint dédié.

L'API repose sur une base de données MongoDB, ce qui permet une gestion efficace des distances grâce à ses index. Cette approche offre également la possibilité de mise à l'échelle en cas de besoin.

## Choix de MongoDB pour la Gestion des Positions GPS

### Une Base de Données Géospatiale pour des Calculs Efficaces

Lors du développement de cette API, le choix de MongoDB comme système de gestion de base de données s'est avéré judicieux. MongoDB offre une fonctionnalité essentielle pour notre application : la capacité de gérer efficacement les distances grâce à ses index géospatiaux. Cette fonctionnalité nous permet de stocker les positions GPS en tant que données géospatiales, ce qui facilite grandement le calcul de la distance entre deux points.  De plus, MongoDB offre une extensibilité et une évolutivité qui sont précieuses pour un avenir potentiel applicatif, car notre application pourrait potentiellement gérer un grand nombre de positions GPS,et être scalable. 

## Endpoints de l'API

### Liste des Positions GPS

- **Endpoint** : `/api/locations`
- **Méthode HTTP** : GET
- **Description** : Récupère la liste complète des positions GPS enregistrées.
- **Réponse** : Liste des positions GPS au format JSON.

### Création de Position GPS

- **Endpoint** : `/api/locations`
- **Méthode HTTP** : POST
- **Description** : Crée une nouvelle position GPS.
- **Corps de la Requête** : Objet JSON représentant la nouvelle position GPS.
- **Réponse** : La nouvelle position GPS créée au format JSON.

### Suppression de Position GPS

- **Endpoint** : `/api/locations`
- **Méthode HTTP** : DELETE
- **Description** : Supprime une position GPS existante en spécifiant son identifiant.
- **Paramètre de la Requête** : `id` (Identifiant de la position GPS à supprimer).
- **Réponse** : Réponse HTTP OK.

### Vérification de la Proximité de Positions GPS

- **Endpoint** : `/api/locations/near`
- **Méthode HTTP** : POST Ici il aurait aussi être possible de passer l'id des deux positions en parametre.
- **Description** : Détermine si deux positions GPS sont à moins de 10 km l'une de l'autre.
- **Corps de la Requête** : Liste JSON contenant deux positions GPS à comparer.
- **Réponse** : Un objet JSON indiquant le résultat de la comparaison. Le résultat sera soit `"<="` (Distance inférieure ou égale à 10 km) soit `">"` (Distance supérieure à 10 km).

## Configuration

Pour exécuter cette API Spring Boot, assurez-vous d'avoir configuré une base de données MongoDB avec les paramètres nécessaires. N'oubliez pas de modifier le fichier properties pour se connecter à la base de données.

