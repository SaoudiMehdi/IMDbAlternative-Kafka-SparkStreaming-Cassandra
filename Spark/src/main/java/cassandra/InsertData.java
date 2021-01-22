package cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.List;

public class InsertData {

    private final String ALL_KNOWN_FOR_PATH = "src/main/resources/movie_actor/actorKnownFor.csv";
    private final String TOP_RATED_PATH = "src/main/resources/movie/topRatedMovies.csv";
    private final String MOVIES_PATH = "src/main/resources/movie/knownMovies.csv";
    private final String MOST_POPULAR_PATH = "src/main/resources/actor/mostPopularCelebs.csv";
    private final String NEWS_PATH = "src/main/resources/actor/recentNews.csv";


    public InsertData() {
        DBConnector connector = new DBConnector();
        connector.connectdb("localhost", 9042);

        insertDataMovies(connector);
        insertDataTopRatedMovies(connector);
        insertDataActors(connector);
        insertDataActorMovie(connector);

        connector.close();



    }

    public void insertDataMovies(DBConnector connector) {
        try {

            final String insertQuery = "INSERT INTO imdb_keyspace2.movies (idMovie, rating, title,releaseDate, runningTimeInMinutes) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement psInsert = connector.getSession().prepare(insertQuery);

            FileReader filereader = new FileReader(MOVIES_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

                for (int i= 1; i<allData.size(); i++) {

                BoundStatement bsInsert = psInsert.bind(allData.get(i)[0],allData.get(i)[1],allData.get(i)[3],allData.get(i)[5],allData.get(i)[6]);
                connector.getSession().execute(bsInsert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertDataTopRatedMovies(DBConnector connector) {
        try {

            final String insertQuery = "INSERT INTO imdb_keyspace2.topRatedMovies (idMovie, ranking) "
                    + "VALUES (?,?)";
            PreparedStatement psInsert = connector.getSession().prepare(insertQuery);

            FileReader filereader = new FileReader(TOP_RATED_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {
                //String ranking = Integer.toString(allData.get(i)[0]);
                BoundStatement bsInsert = psInsert.bind(allData.get(i)[1],allData.get(i)[0]);
                connector.getSession().execute(bsInsert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertDataActors(DBConnector connector) {
        try {

            final String insertQuery = "INSERT INTO imdb_keyspace2.actors (idActor, name, birthDate, birthPlace, gender) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement psInsert = connector.getSession().prepare(insertQuery);

            FileReader filereader = new FileReader(MOST_POPULAR_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {
                BoundStatement bsInsert = psInsert.bind(allData.get(i)[0],allData.get(i)[1],allData.get(i)[2],allData.get(i)[3],allData.get(i)[4]);
                connector.getSession().execute(bsInsert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertDataActorMovie(DBConnector connector) {
        try {

            final String insertQuery = "INSERT INTO imdb_keyspace2.actorMovies (idActor , idMovie) "
                    + "VALUES (?,?)";
            PreparedStatement psInsert = connector.getSession().prepare(insertQuery);

            FileReader filereader = new FileReader(ALL_KNOWN_FOR_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {
                BoundStatement bsInsert = psInsert.bind(allData.get(i)[0],allData.get(i)[1]);
                connector.getSession().execute(bsInsert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void insertDataNews(DBConnector connector) {
        try {

            final String insertQuery = "INSERT INTO imdb_keyspace2.newsTable (id, body, head, link, id_actor, publishTime, common_words) "
                    + "VALUES (?,?,?,?,?,?)";
            PreparedStatement psInsert = connector.getSession().prepare(insertQuery);

            FileReader filereader = new FileReader(NEWS_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {

                BoundStatement bsInsert = psInsert.bind(allData.get(i)[0],allData.get(i)[1],allData.get(i)[3],allData.get(i)[5],allData.get(i)[6],allData.get(i)[7]);
                connector.getSession().execute(bsInsert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
