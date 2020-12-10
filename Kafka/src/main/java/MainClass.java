


public class MainClass {
    public static void main(String[] args) {
        ActorProducerThread actorProducerThread = new ActorProducerThread();
        actorProducerThread.run();

        //MovieProducerThread movieProducerThread = new MovieProducerThread();
        //movieProducerThread.run();
    }
}
