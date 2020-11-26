package Runnable;

import client.fetchingData;
import com.mashape.unirest.http.exceptions.UnirestException;
import util.AppConfig;
import util.RatingApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class RatingFetcherThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(RatingFetcherThread.class.getSimpleName());

    private final AppConfig appConfig;
    private final ArrayBlockingQueue<RatingApi> reviewsQueue;
    private final CountDownLatch latch;
    private RatingApi RatingApi;


    public RatingFetcherThread(AppConfig appConfig, ArrayBlockingQueue<RatingApi> reviewsQueue, CountDownLatch latch) {
        this.appConfig = appConfig;
        this.reviewsQueue = reviewsQueue;
        this.latch = latch;
    }
    @Override
    public void run() {
            try {
                fetchingData fetch = new fetchingData();
                Boolean keepOnRunning = true;
                while (keepOnRunning){
                    ArrayList<RatingApi> rating;
                    try {
                        rating= fetch.getMovies();
                        log.info("Fetched " + rating.size() + " rating");
                        if (rating.size() == 0){
                            keepOnRunning = false;
                        } else {
                            // this may block if the queue is full - this is flow control
                            log.info("Queue size :" + reviewsQueue.size());
                            for (RatingApi rt: rating){
                                reviewsQueue.put(rt);
                            }
                        }
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    } finally {
                        Thread.sleep(50);
                    }
                }
            } catch (InterruptedException e) {
                log.warn("REST Client interrupted");
            } finally {
                this.close();
            }
    }
    private void close() {
        log.info("Closing");
        fetchingData.close();
        latch.countDown();
        log.info("Closed");
    }
}
