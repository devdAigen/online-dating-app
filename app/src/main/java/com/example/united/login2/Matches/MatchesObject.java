package com.example.united.login2.Matches;

/**
 * Created by United on 9/26/2018.
 */

public class MatchesObject {

    private String userId;
    private  String name;
    private  String profilurl;
    private String age;
    private String sex;
    private String crush;
    private String heartbreak;
    private String contact;

    public MatchesObject(String userId,String contact, String name,String profilurl,String age,String sex,String crush,String heartbreak){//String profilurl
        this.userId=userId;
        this.contact=contact;
        this.name=name;
        this.profilurl=profilurl;
        this.age=age;
        this.sex=sex;
        this.crush=crush;
        this.heartbreak=heartbreak;

    }
    public String getUserId(){
        return userId;
    }
    public void setUserID(String userId){
        this.userId=userId;
    }


    public String getContact(){
        return contact;
    }
    public void setContact(String contact){
        this.contact=contact;
    }


    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }



    public void setSex(String sex){
        this.sex=sex;
    }
    public String getSex(){
        return sex;
    }


    public void setAge(String age){
        this.age=age;
    }
    public String getAge(){
        return age;
    }

    public void setProfilurl(String profilurl){
        this.profilurl=profilurl;
    }
    public String getProfilurl(){
        return profilurl;
    }


    public void setCrush(String crush){
        this.crush=crush;
    }
    public String getCrush(){
        return crush;
    }

    public void setHeartbreak(String heartbreak){
        this.heartbreak=heartbreak;
    }
    public String getHeartbreak(){
        return heartbreak;
    }


}
