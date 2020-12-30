

public class MainClass {
    public static void main(String[] args) {
        NewsProducerThread newsProducerThread = new NewsProducerThread();
        newsProducerThread.run();
    }
}
