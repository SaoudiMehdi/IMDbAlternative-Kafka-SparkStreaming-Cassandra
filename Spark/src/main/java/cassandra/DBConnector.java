package cassandra;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class DBConnector {
    private Cluster cluster;
    private Session session;

    public void connectdb(String seeds, int port) {
        this.cluster = Cluster.builder().addContactPoint(seeds).withPort(port).build();
        final Metadata metadata = cluster.getMetadata();

        for (final Host host : metadata.getAllHosts()) {
            System.out.println("driver version " + host.getCassandraVersion());
        }

        this.session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        cluster.close();
    }
}
