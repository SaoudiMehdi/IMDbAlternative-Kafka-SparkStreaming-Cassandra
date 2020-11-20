
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runnable.MovieProducerThread;
import util.AppConfig;

import java.util.List;
import java.util.concurrent.*;

public class MovieProducerMain {

    private Logger log = LoggerFactory.getLogger(MovieProducerMain.class.getSimpleName());

    private ExecutorService executor;
    private CountDownLatch latch;
    private MovieProducerThread movieProducer;

    public static void main(String[] args) {
        MovieProducerMain app = new MovieProducerMain();
        app.start();
    }

    private MovieProducerMain() {
        AppConfig appConfig = new AppConfig(ConfigFactory.load());
        latch = new CountDownLatch(2);
        executor = Executors.newFixedThreadPool(2);

        movieProducer = new MovieProducerThread(appConfig, latch);
    }

    private void start() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!executor.isShutdown()){
                log.info("Shutdown requested");
                shutdown();
            }
        }));

        log.info("Application started!");
        executor.submit(movieProducer);
        log.info("Stuff submit");
        try {
            log.info("Latch await");
            latch.await();
            log.info("Threads completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            shutdown();
            log.info("Application closed succesfully");
        }
    }

    private void shutdown() {
        if (!executor.isShutdown()) {
            log.info("Shutting down");
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(2000, TimeUnit.MILLISECONDS)) { //optional *
                    log.warn("Executor did not terminate in the specified time."); //optional *
                    List<Runnable> droppedTasks = executor.shutdownNow(); //optional **
                    log.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed."); //optional **
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
