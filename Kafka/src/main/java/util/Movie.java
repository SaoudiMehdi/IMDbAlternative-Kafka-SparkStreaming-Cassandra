package util;

import org.json.JSONObject;

public class Movie {
    String id;
    double rate;
    String title;
    String titletype;

    public Movie(){

    }

    public Movie(String id, String titletype, String title) {
        this.id = id;
        this.title = title;
        this.titletype = titletype;
    }

    public Movie(String id, double rate, String title, String titletype) {
        this.id = id;
        this.rate = rate;
        this.title = title;
        this.titletype = titletype;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitletype() {
        return titletype;
    }

    public void setTitletype(String titletype) {
        this.titletype = titletype;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", rate=" + rate +
                ", title='" + title + '\'' +
                ", titletype='" + titletype + '\'' +
                '}';
    }

    public JSONObject toJson(){
        JSONObject movieJson = new JSONObject();
        movieJson.put("id", id);
        movieJson.put("rate", rate);
        movieJson.put("title", title);
        movieJson.put("titletype", titletype);
        return movieJson;
    }
}
