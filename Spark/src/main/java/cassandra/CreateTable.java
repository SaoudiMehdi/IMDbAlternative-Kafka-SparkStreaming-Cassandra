package cassandra;

public class CreateTable {
    public CreateTable() {
        try {
            DBConnector connector = new DBConnector();
            connector.connectdb("localhost", 9042);

            final String createmovieTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace.movies"
                    + "(idMovie varchar, rating varchar, title varchar,releaseDate varchar, runningTimeInMinutes int, PRIMARY KEY (idMovie))";
            connector.getSession().execute(createmovieTable);

            final String createTopRatedMovieTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace.topRatedMovies"
                    + "(idMovie varchar, ranking int, PRIMARY KEY (idMovie))";
            connector.getSession().execute(createTopRatedMovieTable);

            final String createActorTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace.actors"
                    + "(idActor varchar, name varchar, birthDate varchar, birthPlace varchar, gender varchar, PRIMARY KEY (idActor))";
            connector.getSession().execute(createActorTable);

            final String createActorMovieTable = "CREATE TABLE IF NOT EXISTS imdb_keyspace.actorMovies"
                    + "(idActor varchar, idMovie varchar, PRIMARY KEY (idActor, idMovie))";
            connector.getSession().execute(createActorMovieTable);


            connector.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
