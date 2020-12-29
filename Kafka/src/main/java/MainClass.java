import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainClass {
    public static void main(String[] args) {
        ActorProducerThread actorProducerThread = new ActorProducerThread();
        actorProducerThread.run();
       /* ArrayList<String> test = new ArrayList<String>();
        ArrayList<String> dest = new ArrayList<String>();
        test.add("/name/nm0001667/");
        test.add("/name/nm00016688/");

        String[] parts = test.get(0).split("/");
        String[] parts2 = test.get(1).split("/");
        dest.add(parts2[2]);
        dest.add(parts[2]);
        System.out.println(dest.get(0));
        System.out.println(dest.get(1));*/



        //MovieProducerThread movieProducerThread = new MovieProducerThread();
        //movieProducerThread.run();
    }
}
