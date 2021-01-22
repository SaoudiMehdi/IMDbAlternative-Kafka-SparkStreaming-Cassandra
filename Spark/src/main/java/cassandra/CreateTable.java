package cassandra;

public class CreateTable {
    public CreateTable() {
        try {
            DBConnector connector = new DBConnector();
            connector.connectdb("localhost", 9042);

            final String createmovieTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace2.movies"
                    + "(idMovie varchar, rating varchar, title varchar,releaseDate varchar, runningTimeInMinutes varchar, PRIMARY KEY (idMovie))";
            connector.getSession().execute(createmovieTable);

            final String createTopRatedMovieTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace2.topRatedMovies"
                    + "(idMovie varchar, ranking varchar, PRIMARY KEY (idMovie))";
            connector.getSession().execute(createTopRatedMovieTable);

            final String createActorTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace2.actors"
                    + "(idActor varchar, name varchar, birthDate varchar, birthPlace varchar, gender varchar, PRIMARY KEY (idActor))";
            connector.getSession().execute(createActorTable);

            final String createActorMovieTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace2.actorMovies"
                    + "(idActor varchar, idMovie varchar, PRIMARY KEY (idActor, idMovie))";
            connector.getSession().execute(createActorMovieTable);

            final String createRecentNewsTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace2.newsTable"
                    + "(id varchar, body varchar, head varchar, link varchar, id_actor varchar, publishTime varchar, common_words varchar, PRIMARY KEY (id))";
            connector.getSession().execute(createRecentNewsTable);


            connector.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
