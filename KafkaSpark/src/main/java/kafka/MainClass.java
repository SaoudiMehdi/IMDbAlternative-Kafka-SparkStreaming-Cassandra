package kafka;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.typesafe.config.ConfigFactory;
import runnable.MovieProducerThread;
import util.AppConfig;
import util.Movie;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class MainClass {

    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig(ConfigFactory.load());
        CountDownLatch latch = new CountDownLatch(2);
        MovieProducerThread mpt = new MovieProducerThread(appConfig, latch);
        mpt.run();
    }
}
