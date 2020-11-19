package util;

public class Movie {
    private String id;
    private int rate;

    public Movie(String id, int rate) {
        this.id = id;
        this.rate = rate;

    }

    public String getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


}
