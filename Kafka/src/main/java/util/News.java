package util;

public class News {
    String body;
    String head;
    String id;
    String link;
    String publishTime;

    public News(String body, String head, String id, String link, String publishTime) {
        this.body = body;
        this.head = head;
        this.id = id;
        this.link = link;
        this.publishTime = publishTime;
    }

    public News() {
    }

    public String getBody() {
        return body;
    }

    public String getHead() {
        return head;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
