package cassandra;

public class CreateKeySpace {

    public CreateKeySpace() {
        try {
            DBConnector connector = new DBConnector();
            connector.connectdb("localhost", 9042);

            final String createmovieKeySpace = "CREATE KEYSPACE IF NOT EXISTS imdb_keyspace1 WITH "
                    + "replication = {'class' : 'SimpleStrategy','replication_factor' :3}";

            connector.getSession().execute(createmovieKeySpace);

            connector.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
