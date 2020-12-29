import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainClass {
    public static void main(String[] args) {
        ActorProducerThread actorProducerThread = new ActorProducerThread();
        actorProducerThread.run();


        //MovieProducerThread movieProducerThread = new MovieProducerThread();
        //movieProducerThread.run();
    }
}
