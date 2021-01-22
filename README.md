# Kafka-SparkStreaming-Cassandra(Pipeline)

- Pipeline (Kafka - Spark Streaming - Cassandra) fonctionne en exploitant l'API IMDb Alternative qui vous donne l'opportunité de manipuler les données relatives au 
différents acteurs et films.
## Modèle d’arborescence 
### Pour la partie Kafka :
- L'utilité de Kafka est de pouvoir récupérer l'ensemble des données via l'API. Dans un premier temps, vous allez travailler sur la partie streaming : vous pouvez
récupérer les "news" relatives aux acteurs ainsi qu'une liste de ces derniers en temps réel. Puis, pour la partie des données statique, vous pouvez récupérer un 
ensemble des données et les stoquer dans des fichiers .csv afin de les exploiter ultérieurement.
* dossier api : contient deux sous dossiers. Le dossier actor pour la récupération des données statiques des acteurs et le dossier movie pour la récupération des
données statiques des films.
* ActorProducerThread.java : Permet la récupération des données en temps réel des différents acteurs.
* NewsProducerThread : Permet la récupération des données en temps réel des différents news relatifs aux acteurs.
### Pour la partie Spark :
- L'utilité de Spark dans ce projet et de faire un Streaming de données via Spark Streaming en exploitant les données en temps réel récupérées par Kafka et établir
un simple traitement de données. Le traitement de données permet de déterminer les "tags" des différents news(les mots les plus répétitifs dans un article en éliminant les mots qui ne 
referent pas a l'article en utilisant une liste stopword.txt).
* SparkActorConsumer.java : Permet de faire le streaming d'une liste des acteurs récupérée en temps réel et la stoque sur Cassandra.
* SparkNewsConsumer.java : Permet de faire le streaming d'une liste des news( différents articles écrit sur les acteurs en temps réels), puis établis le traitement
en déterminant les mots les plus mentionnées(tags). Le but est de faciliter la recherche d'un article par un utilisateur a l'aide de ces tags détérminés.
### Pour la partie Cassandra:
- L'utilité de Cassandra dans ce projet et de faire un stockage d'une part des données statiques et d'une autre part des données en temps réel et de faciliter
l'opération de visualisation par la suite.
* CreateKeySpace.java : Permet de créer un Keyspace.
* CreateTable.java : Permet de créer les tables pour stoquer les différents éléments
* DBConnector.java : Permet d'établir la connexion avec la base de données.
* InsertData.java : Permet l'insertion des données

```
├── Kafka-SparkStreaming-Cassandra
│   ├── Kafka
│   │   ├── src
│   │   │   ├── main
│   │   │   │   └── java
│   │   │   │         ├── api
|   |   |   |         ├── ActorProducerThread.java
│   │   │   |         └── NewsProducerThread.java
│   │   │   │  
│   │   │   │   
└── └── Spark
        └── src
            └── main
                 └── java
                      ├── SparkActorConsumer.java
                      ├── SparkNewsConsumer.java
                      └── cassandra 
                            ├── CreateTable.java
                            ├── CreateKeySpace.java
                            ├── DBConnector.java
                            ├── InsertData.java
                            └── MainClass.java
```
