package util;

import org.json.JSONObject;

public class Actor {

    String id;
    String name;
    String birthDate;
    String birthPlace;
    String gender;
    String realName;

    public Actor(){

    }

    public Actor(String id, String name, String birthDate, String birthPlace, String gender, String realName) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.gender = gender;
        this.realName = realName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", gender='" + gender + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }

    public JSONObject toJson(){
        JSONObject actorJson = new JSONObject();
        actorJson.put("id", id);
        actorJson.put("name", name);
        actorJson.put("birthDate", birthDate);
        actorJson.put("birthPlace", birthPlace);
        actorJson.put("gender", gender);
        actorJson.put("realName", realName);
        return actorJson;
    }
}
